package com.farm.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.customview.AudioRecorder;
import com.customview.RecordsButton;
import com.farm.R;
import com.farm.adapter.Event_ProcessAdapter;
import com.farm.adapter.PeopleAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FJxx;
import com.farm.bean.HandleBean;
import com.farm.bean.PeopelList;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.common.StringUtils;
import com.farm.common.UIHelper;
import com.farm.widget.MyDialog;
import com.farm.widget.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;
import com.media.HomeFragmentActivity;
import com.media.MediaChooser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by user on 2016/5/12.
 */
@EActivity(R.layout.event_process)
public class Event_Process extends FragmentActivity
{
    static int p;
    String solove = "";
    MyDialog myDialog;
    private String id;
    private String name;
    PopupWindow pw_tab;
    View pv_tab;
    @ViewById
    View line;
    PeopleAdapter peopleAdapter;
    //    NCZ_EventHandleAdapter listadpater;
    private AppContext appContext;
    private int listSumData;
    Event_ProcessAdapter listadpater;
    private View list_footer;//5
    private TextView list_foot_more;//5
    private ProgressBar list_foot_progress;//5
    List<PeopelList> listpeople = new ArrayList<PeopelList>();
    private List<HandleBean> listData = new ArrayList<HandleBean>();
    List<FJxx> list_picture = new ArrayList<FJxx>();
    List<FJxx> list_video = new ArrayList<FJxx>();
    List<FJxx> list_allfj = new ArrayList<FJxx>();
    ReportedBean reportedBean;
    @ViewById
    TextView tv_title;
    @ViewById
    Button btn_sent;
    @ViewById
    EditText et_content;
    @ViewById
    Button btn_record;
    @ViewById
    PullToRefreshListView wz_frame_listview;

    @ViewById
    Button tv_bianjie;
    @ViewById
    Button tv_delete;
    @ViewById
    RecordsButton btn_records;

    @Click
    void imgbtn_back()
    {
        finish();
    }

    @Click
    void tv_bianjie()
    {
        showPop_title();
    }

    AudioRecorder audioRecorder;

    @Click
    void tv_delete()
    {
        View dialog_layout = (LinearLayout) Event_Process.this.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(Event_Process.this, R.style.MyDialog, dialog_layout, "事件", "是否确认事件处理完成?", "是", "否", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        makeData();
                        break;
                    case R.id.btn_cancle:

                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

    @AfterViews
    void afteroncreate()
    {
        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);
        if (commembertab.getnlevel().equals("0"))
        {
            tv_bianjie.setVisibility(View.VISIBLE);
            tv_delete.setVisibility(View.VISIBLE);
        }
        if (reportedBean.getState().equals("2"))
        {
            tv_bianjie.setVisibility(View.GONE);
            tv_delete.setVisibility(View.GONE);
        }
        solove = reportedBean.getRemark2();
        initAnimalListView();
        getlistdata();
        btn_records.setEventId(reportedBean.getEventId());
        btn_records.setAudioRecord(new AudioRecorder());
    }

    @Click
    void btn_record()
    {
        Intent intent = new Intent(Event_Process.this, HomeFragmentActivity.class);
        intent.putExtra("type", "picture");
        startActivity(intent);
    }

    @Click
    void btn_sent()
    {
        if (et_content.getText().toString().equals(""))
        {
            Toast.makeText(Event_Process.this, "请填写要发送的消息！", Toast.LENGTH_SHORT).show();

        } else
        {
            addDate();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        reportedBean = getIntent().getParcelableExtra("event");
        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        Event_Process.this.registerReceiver(imageBroadcastReceiver, imageIntentFilter);


        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_Record);
        Event_Process.this.registerReceiver(receiver_update, intentfilter_update);
    }

    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver()// 植物（0为整体照，1为花照，2为果照，3为叶照）；动物（0为整体照，1为脚印照，2为粪便照）
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            List<String> list = intent.getStringArrayListExtra("list");
            for (int i = 0; i < list.size(); i++)
            {
                String FJBDLJ = list.get(i);
                ImageView imageView = new ImageView(Event_Process.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageView(Event_Process.this, imageView, FJBDLJ);
                imageView.setTag(FJBDLJ);

                FJxx fj_SCFJ = new FJxx();
                fj_SCFJ.setFJBDLJ(FJBDLJ);
                fj_SCFJ.setFJLX("1");

                list_picture.add(fj_SCFJ);
                list_allfj.add(fj_SCFJ);
                saveData(fj_SCFJ);
            }
        }
    };

    private void saveData(final FJxx fj_SCFJ)
    {
        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        params.addQueryStringParameter("solveId", commembertab.getId());
        params.addQueryStringParameter("solveName", commembertab.getrealName());
        params.addQueryStringParameter("state", "0");
        params.addQueryStringParameter("result", et_content.getText().toString());
        params.addQueryStringParameter("action", "eventHandleAdd");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<HandleBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), HandleBean.class);
                    if (listData == null)
                    {

                        AppContext.makeToast(Event_Process.this, "error_connectDataBase");
                    } else
                    {

                      /*  if (list_allfj.size() > 0) {

                            for (int j = 0; j < list_allfj.size(); j++) {
                                uploadMedia(listData.get(0).getEventId(), list_picture.get(j).getFJBDLJ(), list_picture.get(j).getFJLX());
                            }
                        } else {
                            Toast.makeText(Event_Process.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        }*/
                        uploadMedia(listData.get(0).getEventId(), fj_SCFJ.getFJBDLJ(), fj_SCFJ.getFJLX());
                    }

                } else
                {
                    AppContext.makeToast(Event_Process.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {

                String a = error.getMessage();
                AppContext.makeToast(Event_Process.this, "error_connectServer");
            }
        });
    }

    private void uploadMedia(String eventId, String path, String aa)
    {
        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);
        File file = new File(path);
        UUID uuid = UUID.randomUUID();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "UpLoadFileResultImg");
        params.addQueryStringParameter("FJID", uuid.toString());
        params.addQueryStringParameter("SCR", commembertab.getId());
        params.addQueryStringParameter("SCRXM", commembertab.getrealName());
        params.addQueryStringParameter("BZ", "1测试1");
        params.addQueryStringParameter("GLID", eventId);
        params.addQueryStringParameter("FJLX", aa);
//        params.addQueryStringParameter("imagename", plantId);s
        params.addQueryStringParameter("FJMC", file.getName());
        params.setBodyEntity(new FileUploadEntity(file, "text/html"));
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
        http.configSoTimeout(60000);
        http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() != 0)// -1出错；0结果集数量为0；结果列表
                {
                    Toast.makeText(Event_Process.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
                } else
                {
                    AppContext.makeToast(Event_Process.this, "error_connectDataBase");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {

                String a = error.getMessage();
                AppContext.makeToast(Event_Process.this, "error_connectServer");
            }
        });
    }

    //获取人员列表
    private void getlistdata()
    {
        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("nlevel", "1,2");
        params.addQueryStringParameter("action", "getUserlisttByUID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PeopelList> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
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
                    } else
                    {
                        listNewData = new ArrayList<PeopelList>();
                    }
                } else
                {
                    AppContext.makeToast(Event_Process.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(Event_Process.this, "error_connectServer");

            }
        });

    }


    private void addDate()
    {
        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        params.addQueryStringParameter("solveId", commembertab.getId());
        params.addQueryStringParameter("solveName", commembertab.getrealName());
        params.addQueryStringParameter("state", "0");
        params.addQueryStringParameter("result", et_content.getText().toString());
        params.addQueryStringParameter("action", "eventHandleAdd");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<HandleBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), HandleBean.class);
                        et_content.setText("");
                        getListData(UIHelper.LISTVIEW_ACTION_SCROLL, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
//                        Toast.makeText(Event_Process.this, "保存成功！", Toast.LENGTH_SHORT).show();

                    } else
                    {
                        listNewData = new ArrayList<HandleBean>();
                    }
                } else
                {
                    AppContext.makeToast(Event_Process.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(Event_Process.this, "error_connectServer");

            }
        });
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
//            getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
            getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
        }
    };

    private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX)
    {

        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        params.addQueryStringParameter("userId", commembertab.getId());
        params.addQueryStringParameter("action", "getEventHandleByEventId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<HandleBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), HandleBean.class);
                        if (reportedBean.getState().equals("0"))
                        {
                            stateone();
                        }

                        HandleBean handleBean = listNewData.get(0);
                        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);
                        AppContext.eventStatus(Event_Process.this, "2", handleBean.getEventId(), commembertab.getId());
                    } else
                    {
                        listNewData = new ArrayList<HandleBean>();
                    }
                } else
                {
                    AppContext.makeToast(Event_Process.this, "error_connectDataBase");

                    return;
                }
                // 数据处理
                int size = listNewData.size();

                switch (actiontype)
                {
                    case UIHelper.LISTVIEW_ACTION_INIT:// 初始化
                    case UIHelper.LISTVIEW_ACTION_REFRESH:// 顶部刷新
                    case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:// 页面切换
                        int newdata = 0;// 该变量为新加载数据数量-只有顶部刷新才会使用到
                        switch (objtype)
                        {
                            case UIHelper.LISTVIEW_DATATYPE_NEWS:
                                listSumData = size;
                                if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
                                {
                                    if (listData.size() > 0)// 页面切换时，若之前列表中已有数据，则往上面添加，并判断去除重复
                                    {
                                        for (HandleBean Wz_Storehouse1 : listNewData)
                                        {
                                            boolean b = false;
                                            for (HandleBean Wz_Storehouse2 : listData)
                                            {
                                                if (Wz_Storehouse1.getResultId().equals(Wz_Storehouse2.getResultId()))
                                                {
                                                    b = true;
                                                    break;
                                                }
                                            }
                                            if (!b)// 两个不相等才添加
                                                newdata++;
                                        }
                                    } else
                                    {
                                        newdata = size;
                                    }
                                }
                                listData.clear();// 先清除原有数据
                                listData.addAll(listNewData);
                                break;
                            case UIHelper.LISTVIEW_DATATYPE_BLOG:
                            case UIHelper.LISTVIEW_DATATYPE_COMMENT:
                        }
                        if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
                        {
                            // 提示新加载数据
                            if (newdata > 0)
                            {
//                                NewDataToast.makeText(getActivity(), getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
                            } else
                            {
                                // NewDataToast.makeText(NCZ_PQ_CommandList.this,
                                // getString(R.string.new_data_toast_none), false,
                                // R.raw.newdatatoast).show();
                            }
                        }
                        break;
                    case UIHelper.LISTVIEW_ACTION_SCROLL:// 底部刷新，并且判断去除重复数据
                        switch (objtype)
                        {
                            case UIHelper.LISTVIEW_DATATYPE_NEWS:
                                listSumData += size;
                                if (listNewData.size() > 0)
                                {
                                    for (HandleBean Wz_Storehouse1 : listNewData)
                                    {
                                        boolean b = false;
                                        for (HandleBean Wz_Storehouse2 : listData)
                                        {
                                            if (Wz_Storehouse1.getResultId().equals(Wz_Storehouse2.getResultId()))
                                            {
                                                b = true;
                                                break;
                                            }
                                        }
                                        if (!b) listData.add(Wz_Storehouse1);
                                    }
                                } else
                                {
                                    listData.addAll(listNewData);
                                }
                                break;
                            case UIHelper.LISTVIEW_DATATYPE_BLOG:
                                break;
                        }
                        break;
                }
                // 刷新列表
                int xx = size;
                int yy = PAGESIZE;
                if (size >= 0)
                {
                    if (size < PAGESIZE)
                    {
                        lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_full);// 已经全部加载完毕
                    } else if (size == PAGESIZE)
                    {// 还有数据可以加载
                        lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_more);
                    }

                } else if (size == -1)
                {
                    // 有异常--显示加载出错 & 弹出错误消息
                    lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                    more.setText(R.string.load_error);
                    AppContext.makeToast(Event_Process.this, "load_error");
                }
                if (adapter.getCount() == 0)
                {
                    lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
                    more.setText(R.string.load_empty);
                }
                progressBar.setVisibility(ProgressBar.GONE);
                // main_head_progress.setVisibility(ProgressBar.GONE);
                if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
                {
                    lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new java.util.Date().toLocaleString());
                    lv.setSelection(0);
                } else if (actiontype == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
                {
                    lv.onRefreshComplete();
                    lv.setSelection(0);
                }
         /*       if (!ishidding  && timethread!=null)
                {
                    timethread.setSleep(false);
                }*/
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(Event_Process.this, "error_connectServer");
         /*       if (!ishidding  && timethread!=null)
                {
                    timethread.setSleep(false);
                }*/
            }
        });
    }

    private void initAnimalListView()
    {

        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);
//        listadpater=new NCZ_EventHandleAdapter(Event_Process.this, listData);
        listadpater = new Event_ProcessAdapter(Event_Process.this, listData);
        list_footer = this.getLayoutInflater().inflate(R.layout.listview_footer, null);
        list_foot_more = (TextView) list_footer.findViewById(R.id.listview_foot_more);
        list_foot_progress = (ProgressBar) list_footer.findViewById(R.id.listview_foot_progress);
        wz_frame_listview.addFooterView(list_footer);// 添加底部视图 必须在setAdapter前
        wz_frame_listview.setAdapter(listadpater);
        wz_frame_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // 点击头部、底部栏无效
                if (position == 0 || view == list_footer) return;
 /*               Intent intent = new Intent(Event_Process.this, NCZ_HandleDetail_.class);
                HandleBean handleBean = listData.get(position - 1);
                intent.putExtra("handleBean", handleBean);
                startActivity(intent);*/


            }
        });
        wz_frame_listview.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                wz_frame_listview.onScrollStateChanged(view, scrollState);

                // 数据为空--不用继续下面代码了
                if (listData.isEmpty()) return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try
                {
                    if (view.getPositionForView(list_footer) == view.getLastVisiblePosition())
                        scrollEnd = true;
                } catch (Exception e)
                {
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(wz_frame_listview.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE)
                {
                    wz_frame_listview.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    list_foot_more.setText(R.string.load_ing);// 之前显示为"完成"加载
                    list_foot_progress.setVisibility(View.VISIBLE);
                    // 当前pageIndex
                    int pageIndex = listSumData / AppContext.PAGE_SIZE_RECORD;// 总数里面包含几个PAGE_SIZE
                    getListData(UIHelper.LISTVIEW_ACTION_SCROLL, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, pageIndex);
                    // loadLvNewsData(curNewsCatalog, pageIndex, lvNewsHandler,
                    // UIHelper.LISTVIEW_ACTION_SCROLL);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                wz_frame_listview.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        wz_frame_listview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
        {
            public void onRefresh()
            {
                // loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
                // UIHelper.LISTVIEW_ACTION_REFRESH);
                getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
            }
        });
        // 加载资讯数据
        if (listData.isEmpty())
        {
            getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
        }
    }

    public void showPop_title()
    {//LAYOUT_INFLATER_SERVICE
        LayoutInflater layoutInflater = (LayoutInflater) Event_Process.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing()))
                {
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
        peopleAdapter = new PeopleAdapter(Event_Process.this, listpeople);
        listview.setAdapter(peopleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
            {
//                tv_title.setText(listpeople.get(postion).getUserlevelName() + "---" + listpeople.get(postion).getRealName());
                id = listpeople.get(postion).getId();
                name = listpeople.get(postion).getRealName();
                pw_tab.dismiss();

                zhipairen();
            }
        });
    }

    public void zhipairen()
    {
        View dialog_layout = (LinearLayout) Event_Process.this.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(Event_Process.this, R.style.MyDialog, dialog_layout, "指派人员", "是否指派" + name + "处理这件事", "确认", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        //进行添加人员
                        queding();
                        break;
                    case R.id.btn_cancle:

                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void queding()
    {
        commembertab commembertab = AppContext.getUserInfo(Event_Process.this);

        if (!solove.equals(""))
        {
            solove += "," + id;
        } else
        {
            solove = id;

        }

 /*       if (solove==null) {
            if (reportedBean.getRemark2().equals("")||reportedBean.getRemark2()==null) {
                solove = id;
            } else {
                solove = reportedBean.getRemark2() + "," + id;
            }
        }else
        {
            solove+=","+id;
        }*/

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "eventRecordEd");
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("remark2", solove);
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {

//                    Toast.makeText(Event_Process.this, "保存成功！", Toast.LENGTH_SHORT).show();
                } else
                {
                    AppContext.makeToast(Event_Process.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                String a = error.getMessage();
                AppContext.makeToast(Event_Process.this, "error_connectServer");
            }
        });
    }

    private void makeData()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "eventRecordEd");
        params.addQueryStringParameter("state", "2");
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    finish();
//                    Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                } else
                {
                    AppContext.makeToast(Event_Process.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                String a = error.getMessage();
                AppContext.makeToast(Event_Process.this, "error_connectServer");
            }
        });
    }

    private void stateone()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "eventRecordEd");
        params.addQueryStringParameter("state", "1");
        params.addQueryStringParameter("eventId", reportedBean.getEventId());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
//                    Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                } else
                {
                    AppContext.makeToast(Event_Process.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                String a = error.getMessage();
                AppContext.makeToast(Event_Process.this, "error_connectServer");
            }
        });
    }
}
