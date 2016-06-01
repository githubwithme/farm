package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_FarmLive;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parkjob;
import com.farm.common.FileHelper;
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
public class NCZ_FarmLive_Fragment extends Fragment
{
    String parkid;
    List<parkjob> list_parkjob;
    Adapter_FarmLive adapter_farmLive;
    @ViewById
    ExpandableListView expandableListView;


    @AfterViews
    void afterOncreate()
    {
        getBatchTimeByUid_test();
//        getAreaJob();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_farmlive_fragment, container, false);
        return rootView;
    }
    private void getBatchTimeByUid_test()
    {
        list_parkjob = FileHelper.getAssetsData(getActivity(), "getJobOfArea", parkjob.class);
        adapter_farmLive = new Adapter_FarmLive(getActivity(), list_parkjob, expandableListView);
        expandableListView.setAdapter(adapter_farmLive);
        utils.setListViewHeight(expandableListView);
        for (int i = 0; i < list_parkjob.size(); i++)
        {
            expandableListView.expandGroup(i);//展开
        }
    }

    private void getAreaJob()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getJobOfArea");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        list_parkjob = JSON.parseArray(result.getRows().toJSONString(), parkjob.class);
                        adapter_farmLive = new Adapter_FarmLive(getActivity(), list_parkjob, expandableListView);
                        expandableListView.setAdapter(adapter_farmLive);
                        utils.setListViewHeight(expandableListView);
                        for (int i = 0; i < list_parkjob.size(); i++)
                        {
                            expandableListView.expandGroup(i);//展开
                        }

                    } else
                    {
                        list_parkjob = new ArrayList<parkjob>();
                    }

                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }


}
