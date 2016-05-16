package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_DLExecute_Adapter;
import com.farm.adapter.PQ_DLExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.utils;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/5/1.
 */
@EFragment
public class PQ_DLFragment extends Fragment{
    commembertab commembertab;
    @ViewById
    TextView tv_title;
    PQ_DLExecute_Adapter pq_dlExecute_adapter;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    RelativeLayout rl_dl;
    @AfterViews
  void afterview()
  {
      tv_title.setText(commembertab.getareaName()+"断蕾情况");
      getBatchTimeOfAreaId();
   }
    @Click
    void shuaxin()
    {
        getBatchTimeOfAreaId();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pq_dlfragment, container, false);
         commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }

public void getBatchTimeOfAreaId(){
//    commembertab commembertab = AppContext.getUserInfo(getActivity());
     commembertab = AppContext.getUserInfo(getActivity());
    RequestParams params = new RequestParams();
    params.addQueryStringParameter("uid", commembertab.getuId());
    params.addQueryStringParameter("parkid",commembertab.getparkId());
    params.addQueryStringParameter("year", utils.getYear());
    params.addQueryStringParameter("areaid", commembertab.getareaId());
    params.addQueryStringParameter("action", "getBatchTimeOfArea");
    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
    {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo)
        {
            String a = responseInfo.result;
            List<BatchTime> listNewData = null;
            Result result = JSON.parseObject(responseInfo.result, Result.class);
            if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
            {
                if (result.getAffectedRows() > 0)
                {
                    rl_dl.setVisibility(View.GONE);
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                    pq_dlExecute_adapter=new PQ_DLExecute_Adapter(getActivity(), listNewData, expandableListView);
                    expandableListView.setAdapter(pq_dlExecute_adapter);

                   for (int i = 0; i < listNewData.size(); i++)
                    {
                        expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
                    }

                } else
                {

                    listNewData = new ArrayList<BatchTime>();
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

