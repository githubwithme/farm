package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.AddPlantObservation_StepOne_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
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
 * Created by ${hmj} on 2015/12/18.
 */
@EFragment
public class AddPlantObservation_StepOne extends Fragment
{
    CustomDialog_ListView customDialog_listView;
    String  from="";
    String  gcdid="";
    String  gcd="";
    FragmentCallBack fragmentCallBack = null;
    @ViewById
    ListView lv;
    AddPlantObservation_StepOne_Adapter addStd_cmd_stepOne_adapter;
    private List<Dictionary> listData = new ArrayList<Dictionary>();
    com.farm.bean.commembertab commembertab;
    Dictionary dic_comm;
    Dictionary_wheel dictionary_wheel;


    @AfterViews
    void afterOncreate()
    {
//        getCommandlist();
        getTestData("gcd");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation__step_one, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }
    private void getTestData(String from)
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        Result result = JSON.parseObject(jsonObject.getString(from), Result.class);
        List<Dictionary> lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
        dic_comm = lsitNewData.get(0);
        addStd_cmd_stepOne_adapter = new AddPlantObservation_StepOne_Adapter(getActivity(), dic_comm, fragmentCallBack);
        lv.setAdapter(addStd_cmd_stepOne_adapter);
    }
    private void getCommandlist()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("name", "Zuoye");
        params.addQueryStringParameter("action", "getDict");
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
                            dic_comm = lsitNewData.get(0);
                            addStd_cmd_stepOne_adapter = new AddPlantObservation_StepOne_Adapter(getActivity(), dic_comm, fragmentCallBack);
                            lv.setAdapter(addStd_cmd_stepOne_adapter);
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
        fragmentCallBack = (AddPlantObservation) activity;
    }
}
