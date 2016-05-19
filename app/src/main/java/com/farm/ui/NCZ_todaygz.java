package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_todaygzExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.Today_job;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
@EFragment
public class NCZ_todaygz extends Fragment
{
    TimeThread timethread;
    boolean ishidding = false;
    NCZ_todaygzExecute_Adapter ncz_todaygzExecute_adapter;
    @ViewById
    ExpandableListView expandableListView;

    List<Today_job> listdate;

    public void setThreadStatus(boolean hidden)
    {
        ishidding = hidden;
        super.onHiddenChanged(hidden);//true

        if (timethread != null)
        {
            if (hidden == true)
            {
                timethread.setSleep(true);
            } else
            {
                timethread = new TimeThread();
                timethread.setStop(false);
                timethread.setSleep(false);
                timethread.start();
            }

        }
    }

    @AfterViews
    void afterOncreate()
    {

        getBreakOffInfoOfContract();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_todaygz, container, false);
        timethread = new TimeThread();
        timethread.setSleep(false);
        timethread.start();
        return rootView;
    }

    private void getBreakOffInfoOfContract()
    {
        if (getActivity() == null)
        {
            return;
        }
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("strWhere", "");
  /*      params.addQueryStringParameter("page_size", String.valueOf(PAGESIZE));
        params.addQueryStringParameter("page_index", String.valueOf(PAGEINDEX));*/
        params.addQueryStringParameter("action", "jobGetListByUid");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Today_job> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Today_job.class);
                        ncz_todaygzExecute_adapter = new NCZ_todaygzExecute_Adapter(getActivity(), listNewData, expandableListView);
                        expandableListView.setAdapter(ncz_todaygzExecute_adapter);
                        utils.setListViewHeight(expandableListView);
//                        String aa=listNewData.get(0).getJoblist().get(0).getamount();
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
                        }

                    } else
                    {
                        listNewData = new ArrayList<Today_job>();
                    }

                } else
                {
                    if (!ishidding && timethread != null)
                    {
                        timethread.setSleep(false);
                    }
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                if (!ishidding && timethread != null)
                {
                    timethread.setSleep(false);
                }
                AppContext.makeToast(getActivity(), "error_connectServer");

            }
        });
    }

    class TimeThread extends Thread
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
                    return;
                } else
                {
                    try
                    {
                        timethread.sleep(AppContext.TIME_GZ);
                        starttime = starttime + 1000;
                        getBreakOffInfoOfContract();
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
        if (timethread != null && timethread.isAlive())
        {
            timethread.setStop(true);
            timethread.interrupt();
            timethread = null;
        }

    }

}
