package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.AddPlantObservation_stepFour_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.com.custominterface.FragmentCallBack_AddPlantObservation;
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

import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddPlantObservation_stepfour extends Fragment
{

    FragmentCallBack_AddPlantObservation fragmentCallBack;
    @ViewById
    Button btn_next;
    @ViewById
    ExpandableListView mainlistview;


    @Click
    void btn_next()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 0);
        fragmentCallBack.callbackFun2(bundle);
    }


    @AfterViews
    void afterOncreate()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation_stepfour, container, false);
        return rootView;
    }

    public void updateData(String gcq, String zw)
    {
        getCommandlist(gcq,zw);
    }

    private void getCommandlist(String gcq, String zw)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("gcq", gcq);
        params.addQueryStringParameter("zw", zw);
        params.addQueryStringParameter("action", "getcommandFXDICT");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)
                {
                    if (result.getAffectedRows() != 0)
                    {
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            Dictionary dic = lsitNewData.get(0);
                            AddPlantObservation_stepFour_Adapter customExpandableListAdapter = new AddPlantObservation_stepFour_Adapter(getActivity(), dic, mainlistview);
                            mainlistview.setAdapter(customExpandableListAdapter);
                        }

                    } else
                    {

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


    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack_AddPlantObservation) activity;
    }
}
