package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.AddPlantObservation_StepTwo_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.com.custominterface.FragmentCallBack_AddPlantObservation;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/18.
 */
@EFragment
public class AddPlantObservation_StepTwo extends Fragment
{
    Button currentView;
    FragmentCallBack_AddPlantObservation fragmentCallBack = null;
    @ViewById
    ListView lv;
    @ViewById
    Button btn_szqq;
    @ViewById
    Button btn_szzq;
    @ViewById
    Button btn_szhq;
    AddPlantObservation_StepTwo_Adapter addStd_cmd_stepOne_adapter;
    private List<Dictionary> listData = new ArrayList<Dictionary>();
    com.farm.bean.commembertab commembertab;
    Dictionary dic_comm;
    Dictionary_wheel dictionary_wheel;

    @Click
    void btn_szqq()
    {
        currentView=btn_szqq;
        btn_szqq.setSelected(true);
        btn_szzq.setSelected(false);
        btn_szhq.setSelected(false);
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 0);
        fragmentCallBack.callbackFun2(bundle);
    }
    @Click
    void btn_szzq()
    {
        currentView=btn_szzq;
        btn_szqq.setSelected(false);
        btn_szzq.setSelected(true);
        btn_szhq.setSelected(false);
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 0);
        fragmentCallBack.callbackFun2(bundle);
    }
    @Click
    void btn_szhq()
    {
        currentView=btn_szhq;
        btn_szqq.setSelected(false);
        btn_szzq.setSelected(false);
        btn_szhq.setSelected(true);
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 0);
        fragmentCallBack.callbackFun2(bundle);
    }
    @AfterViews
    void afterOncreate()
    {
//        getCommandlist();
//        getTestData("gcq");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation__step_two, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }

    private void getTestData(String from)
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        Result result = JSON.parseObject(jsonObject.getString(from), Result.class);
        List<Dictionary> lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
        dic_comm = lsitNewData.get(0);
        addStd_cmd_stepOne_adapter = new AddPlantObservation_StepTwo_Adapter(getActivity(), dic_comm, fragmentCallBack);
        lv.setAdapter(addStd_cmd_stepOne_adapter);
    }

    public String getGcq()
    {
        return currentView.getText().toString();
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
                            addStd_cmd_stepOne_adapter = new AddPlantObservation_StepTwo_Adapter(getActivity(), dic_comm, fragmentCallBack);
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
        fragmentCallBack = (FragmentCallBack_AddPlantObservation) activity;
    }
}
