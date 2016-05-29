package com.farm.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.PG_ReportedAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.StringUtils;
import com.farm.common.UIHelper;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 2016/4/11.
 */
@EFragment
public class NCZ_Evented extends Fragment
{
  /*  boolean ishidding=false;
    TimeThread timethread;*/
    com.farm.bean.commembertab commembertab;
    PG_ReportedAdapter listadpater;
    private AppContext appContext;
    android.support.v4.app.Fragment mContent = new android.support.v4.app.Fragment();
    private int listSumData;
    private View list_footer;//5
    private TextView list_foot_more;//5
    private ProgressBar list_foot_progress;//5
    private List<ReportedBean> listData = new ArrayList<ReportedBean>();

    @ViewById
    ImageButton btn_add;
    @ViewById
    PullToRefreshListView frame_listview_news;

    @AfterViews
    void afssss()
    {
        btn_add.setVisibility(View.GONE);
        initAnimalListView();
//        getBreakOffInfoOfContract();
    }

    @Override
    public void onResume() {
        super.onResume();
        getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
    }

    /*  @Override
    public void onHiddenChanged(boolean hidden)
    {
        ishidding=hidden;
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            if (timethread != null)
            {
//                timethread.setSleep(false);
                timethread = new TimeThread();
                timethread.setStop(false);
                timethread.setSleep(false);
                timethread.start();
            }
        } else
        {
            if (timethread != null)
            {
                timethread.setSleep(true);
            }
        }
    }*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.pg_eventreported, container, false);
        appContext = (AppContext) getActivity().getApplication();
//        commembertab commembertab = AppContext.getUserInfo(getActivity());
        commembertab = AppContext.getUserInfo(getActivity());
   /*     timethread = new TimeThread();
        timethread.setSleep(false);
        timethread.start();*/
        return rootView;
    }

    private void initAnimalListView()
    {
        listadpater=new PG_ReportedAdapter(getActivity(), listData);
        list_footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        list_foot_more = (TextView) list_footer.findViewById(R.id.listview_foot_more);
        list_foot_progress = (ProgressBar) list_footer.findViewById(R.id.listview_foot_progress);
        frame_listview_news.addFooterView(list_footer);// 添加底部视图 必须在setAdapter前
        frame_listview_news.setAdapter(listadpater);
        frame_listview_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击头部、底部栏无效
                if (position == 0 || view == list_footer)
                    return;
//                Intent intent=new Intent(getActivity(),NCZ_EventLookList_.class);
                ReportedBean reportedBean=listData.get(position-1);

                commembertab commembertab = AppContext.getUserInfo(getActivity());
                AppContext.eventStatus(getActivity(), "1", reportedBean.getEventId(),  commembertab.getId());
                Intent intent=new Intent(getActivity(),PG_EventDetail_.class);

                intent.putExtra("reportedBean",reportedBean);
                startActivity(intent);


            }
        });
        frame_listview_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                frame_listview_news.onScrollStateChanged(view, scrollState);

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

                int lvDataState = StringUtils.toInt(frame_listview_news.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    frame_listview_news.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    list_foot_more.setText(R.string.load_ing);// 之前显示为"完成"加载
                    list_foot_progress.setVisibility(View.VISIBLE);
                    // 当前pageIndex
                    int pageIndex = listSumData / AppContext.PAGE_SIZE_RECORD;// 总数里面包含几个PAGE_SIZE_RECORD
                    getListData(UIHelper.LISTVIEW_ACTION_SCROLL, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, pageIndex);
                    // loadLvNewsData(curNewsCatalog, pageIndex, lvNewsHandler,
                    // UIHelper.LISTVIEW_ACTION_SCROLL);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                frame_listview_news.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        frame_listview_news.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
                // loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
                // UIHelper.LISTVIEW_ACTION_REFRESH);
                getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
            }
        });
        // 加载资讯数据
        if (listData.isEmpty()) {
            getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
        }
    }
    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
        }
    };

    private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX)
    {


        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getEventListByUID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<ReportedBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {

                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);
                       Iterator<ReportedBean> it = listNewData.iterator();
                           while (it.hasNext())
                           {
                            String value = it.next().getState();
                            if (value.equals("0")||value.equals("1"))
                                 {
                                it.remove();
                                 }
                             }
                    } else {
                        listNewData = new ArrayList<ReportedBean>();
                    }
                } else {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
              /*      if (!ishidding  && timethread!=null)
                    {
                        timethread.setSleep(false);
                    }*/
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
                                        for (ReportedBean Wz_Storehouse1 : listNewData) {
                                            boolean b = false;
                                            for (ReportedBean Wz_Storehouse2 : listData) {
                                                if (Wz_Storehouse1.getEventId().equals(Wz_Storehouse2.getEventId())) {
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
                                NewDataToast.makeText(getActivity(), getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
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
                                    for (ReportedBean Wz_Storehouse1 : listNewData) {
                                        boolean b = false;
                                        for (ReportedBean Wz_Storehouse2 : listData) {
                                            if (Wz_Storehouse1.getEventId().equals(Wz_Storehouse2.getEventId())) {
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
                    lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
                    lv.setSelection(0);
                } else if (actiontype == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
                    lv.onRefreshComplete();
                    lv.setSelection(0);
                }
            /*    if (!ishidding  && timethread!=null)
                {
                    timethread.setSleep(false);
                }*/
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
        /*        if (!ishidding  && timethread!=null)
                {
                    timethread.setSleep(false);
                }*/
            }
        });
    }
/*    class TimeThread extends Thread
    {
        private boolean isSleep = true;
        private boolean stop = false;

        public void run()
        {
            Long starttime = 0l;
            while (!stop)
            {
                if (isSleep)
                {
                } else
                {
                    try
                    {
                        timethread.sleep(AppContext.TIME_REFRESH);
                        starttime = starttime + 1000;
                        getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
                        timethread.setSleep(true);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setSleep(boolean sleep)
        {
            isSleep = sleep;
        }

        public void setStop(boolean stop)
        {
            this.stop = stop;
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        timethread.setStop(true);
        timethread.interrupt();
        timethread = null;
    }*/

}
