package com.farm.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.WZ_RKExecute_Adapter;
import com.farm.adapter.WZ_YCExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ListYc;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by hasee on 2016/7/12.
 */
@EFragment
public class NCZ_NewYCFragment extends Fragment
{
    WZ_YCExecute_Adapter wz_rkExecute_adapter;
    @ViewById
    ExpandableListView expandableListView;

    @AfterViews
    void afterview()
    {
        getGoodsExecptionByUid1();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_wz_yclayout, container, false);
        return rootView;
    }


    private void getGoodsExecptionByUid1()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getGoodsExecptionByUid1");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<ListYc> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {

                    listNewData = JSON.parseArray(result.getRows().toJSONString(), ListYc.class);
                    wz_rkExecute_adapter = new WZ_YCExecute_Adapter(getActivity(), listNewData, expandableListView);
                    expandableListView.setAdapter(wz_rkExecute_adapter);

                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            expandableListView.expandGroup(i);//展开
//                            expandableListView.collapseGroup(i);//关闭
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
