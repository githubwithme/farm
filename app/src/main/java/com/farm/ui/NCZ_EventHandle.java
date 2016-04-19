package com.farm.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_EventHandleAdapter;
import com.farm.adapter.PeopleAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.HandleBean;
import com.farm.bean.PeopelList;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.bean.WZ_Detail;
import com.farm.bean.commembertab;
import com.farm.common.StringUtils;
import com.farm.common.UIHelper;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDialog;
import com.farm.widget.NewDataToast;
import com.farm.widget.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by user on 2016/4/12.
 */
@EFragment
public class NCZ_EventHandle extends Fragment
{

    NCZ_EventHandleAdapter listadpater;
    Fragment mContent = new Fragment();
    private int listSumData;
    private View list_footer;//5
    private TextView list_foot_more;//5
    private ProgressBar list_foot_progress;//5
    private List<HandleBean> listData = new ArrayList<HandleBean>();
    private List<String> wzdata;
    private AppContext appContext;
    private ListView listView;
    String id;
    String name;
    PopupWindow pw_tab;
    View pv_tab;
    List<PeopelList> listpeople=new ArrayList<PeopelList>();
    PeopleAdapter peopleAdapter;
    CustomDialog_ListView customDialog_listView;
    String zzsl = "";
    MyDialog myDialog;
    ReportedBean reportedBean;
    @ViewById
    TextView handle;
    @ViewById
    View line;

    @ViewById
    PullToRefreshListView wz_frame_listview;
//下拉选人
    @Click
    void handle()
    {
        showPop_title();
    }

//事件修改状态  saveData()，处理结果的添加 addDate();
    @Click
    void btn_handle()
    {

        View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "指派人员", "是否指派"+name+"处理这件事", "确认", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        saveData();
                        addDate();
                        break;
                    case R.id.btn_cancle:

                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
//        saveData();
//        addDate();
    }
    @AfterViews
    void afteroncreate()
    {
        //获取人员信息
        getlistdata();
//listview
        initAnimalListView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ncz_eventhandle, container, false);

        reportedBean = getArguments().getParcelable("reportedBean");
        return rootView;
    }
    private void getlistdata()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("nlevel", "1,2");
        params.addQueryStringParameter("action", "getUserlisttByUID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<PeopelList> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PeopelList.class);

                        listpeople.addAll(listNewData);
                        //方法一
                     /*   List<String> list = new ArrayList<String>();
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            list.add(listNewData.get(i).getUserlevelName()+"-"+listNewData.get(i).getRealName());
//                            list.add(jsonArray.getString(i));
                        }
                        showDialog_workday(list);*/
                    } else {
                        listNewData = new ArrayList<PeopelList>();
                    }
                } else {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");

            }
        });

    }

    public void showPop_title()
    {//LAYOUT_INFLATER_SERVICE
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing())) {
                    pw_tab.dismiss();
                    return true;
                }
                return false;
            }
        });
        pv_tab.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_tab.isShowing())
                {
                    pw_tab.dismiss();
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_tab.showAsDropDown(line, 0, 0);
        pw_tab.setOutsideTouchable(true);


        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
        peopleAdapter=new PeopleAdapter(getActivity(),listpeople);
        listview.setAdapter(peopleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3) {
                handle.setText(listpeople.get(postion).getUserlevelName() + "---" + listpeople.get(postion).getRealName());
                id = listpeople.get(postion).getId();
                name = listpeople.get(postion).getRealName();

                pw_tab.dismiss();
            }
        });
    }

    private void saveData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "eventRecordEd");
        params.addQueryStringParameter("state", "1");
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                } else {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void addDate()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        params.addQueryStringParameter("solveId", id);
        params.addQueryStringParameter("solveName", name);
        params.addQueryStringParameter("state", "0");
        params.addQueryStringParameter("action", "eventHandleAdd");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<HandleBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), HandleBean.class);

                        Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();

                    } else {
                        listNewData = new ArrayList<HandleBean>();
                    }
                } else {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");

            }
        });
    }


    private void initAnimalListView() {
        listadpater=new NCZ_EventHandleAdapter(getActivity(), listData);
        list_footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        list_foot_more = (TextView) list_footer.findViewById(R.id.listview_foot_more);
        list_foot_progress = (ProgressBar) list_footer.findViewById(R.id.listview_foot_progress);
        wz_frame_listview.addFooterView(list_footer);// 添加底部视图 必须在setAdapter前
        wz_frame_listview.setAdapter(listadpater);
        wz_frame_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击头部、底部栏无效
                if (position == 0 || view == list_footer)
                    return;
                Intent intent=new Intent(getActivity(),NCZ_HandleDetail_.class);
                HandleBean handleBean=listData.get(position-1);
                intent.putExtra("handleBean",handleBean);
                startActivity(intent);
    /*            Wz_Storehouse Wz_Storehouse = listData.get((position - 1));
                Intent intent = new Intent(getActivity(), NCZ_WZ_CKWZActivity_.class);
                intent.putExtra("storehouseId", Wz_Storehouse);
                startActivity(intent);*/

            }
        });
        wz_frame_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                wz_frame_listview.onScrollStateChanged(view, scrollState);

                // 数据为空--不用继续下面代码了
                if (listData.isEmpty()) return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(list_footer) == view.getLastVisiblePosition())
                        scrollEnd = true;
                } catch (Exception e) {
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(wz_frame_listview.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    wz_frame_listview.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    list_foot_more.setText(R.string.load_ing);// 之前显示为"完成"加载
                    list_foot_progress.setVisibility(View.VISIBLE);
                    // 当前pageIndex
                    int pageIndex = listSumData / AppContext.PAGE_SIZE;// 总数里面包含几个PAGE_SIZE
                    getListData(UIHelper.LISTVIEW_ACTION_SCROLL, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, pageIndex);
                    // loadLvNewsData(curNewsCatalog, pageIndex, lvNewsHandler,
                    // UIHelper.LISTVIEW_ACTION_SCROLL);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                wz_frame_listview.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        wz_frame_listview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
                // loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
                // UIHelper.LISTVIEW_ACTION_REFRESH);
                getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
            }
        });
        // 加载资讯数据
        if (listData.isEmpty()) {
            getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
        }
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
        }
    };

    private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX) {

        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        params.addQueryStringParameter("action", "getEventHandleByEventId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<HandleBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), HandleBean.class);
                    } else {
                        listNewData = new ArrayList<HandleBean>();
                    }
                } else {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");

                    return;
                }
                // 数据处理
                int size = listNewData.size();

                switch (actiontype) {
                    case UIHelper.LISTVIEW_ACTION_INIT:// 初始化
                    case UIHelper.LISTVIEW_ACTION_REFRESH:// 顶部刷新
                    case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:// 页面切换
                        int newdata = 0;// 该变量为新加载数据数量-只有顶部刷新才会使用到
                        switch (objtype) {
                            case UIHelper.LISTVIEW_DATATYPE_NEWS:
                                listSumData = size;
                                if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
                                    if (listData.size() > 0)// 页面切换时，若之前列表中已有数据，则往上面添加，并判断去除重复
                                    {
                                        for (HandleBean Wz_Storehouse1 : listNewData) {
                                            boolean b = false;
                                            for (HandleBean Wz_Storehouse2 : listData) {
                                                if (Wz_Storehouse1.getResultId().equals(Wz_Storehouse2.getResultId())) {
                                                    b = true;
                                                    break;
                                                }
                                            }
                                            if (!b)// 两个不相等才添加
                                                newdata++;
                                        }
                                    } else {
                                        newdata = size;
                                    }
                                }
                                listData.clear();// 先清除原有数据
                                listData.addAll(listNewData);
                                break;
                            case UIHelper.LISTVIEW_DATATYPE_BLOG:
                            case UIHelper.LISTVIEW_DATATYPE_COMMENT:
                        }
                        if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
                            // 提示新加载数据
                            if (newdata > 0) {
//                                NewDataToast.makeText(getActivity(), getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
                            } else {
                                // NewDataToast.makeText(NCZ_PQ_CommandList.this,
                                // getString(R.string.new_data_toast_none), false,
                                // R.raw.newdatatoast).show();
                            }
                        }
                        break;
                    case UIHelper.LISTVIEW_ACTION_SCROLL:// 底部刷新，并且判断去除重复数据
                        switch (objtype) {
                            case UIHelper.LISTVIEW_DATATYPE_NEWS:
                                listSumData += size;
                                if (listNewData.size() > 0) {
                                    for (HandleBean Wz_Storehouse1 : listNewData) {
                                        boolean b = false;
                                        for (HandleBean Wz_Storehouse2 : listData) {
                                            if (Wz_Storehouse1.getResultId().equals(Wz_Storehouse2.getResultId())) {
                                                b = true;
                                                break;
                                            }
                                        }
                                        if (!b) listData.add(Wz_Storehouse1);
                                    }
                                } else {
                                    listData.addAll(listNewData);
                                }
                                break;
                            case UIHelper.LISTVIEW_DATATYPE_BLOG:
                                break;
                        }
                        break;
                }
                // 刷新列表
                if (size >= 0) {
                    if (size < PAGESIZE) {
                        lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_full);// 已经全部加载完毕
                    } else if (size == PAGESIZE) {// 还有数据可以加载
                        lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_more);
                    }

                } else if (size == -1) {
                    // 有异常--显示加载出错 & 弹出错误消息
                    lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                    more.setText(R.string.load_error);
                    AppContext.makeToast(getActivity(), "load_error");
                }
                if (adapter.getCount() == 0) {
                    lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
                    more.setText(R.string.load_empty);
                }
                progressBar.setVisibility(ProgressBar.GONE);
                // main_head_progress.setVisibility(ProgressBar.GONE);
                if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
                    lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new java.util.Date().toLocaleString());
                    lv.setSelection(0);
                } else if (actiontype == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
                    lv.onRefreshComplete();
                    lv.setSelection(0);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");

            }
        });
    }

}
