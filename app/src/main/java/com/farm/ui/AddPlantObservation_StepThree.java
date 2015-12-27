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
import com.farm.adapter.AddPlantObservationAdapter;
import com.farm.adapter.AddPlantObservation_StepTwo_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.Result;
import com.farm.bean.planttab;
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
public class AddPlantObservation_StepThree extends Fragment
{
    AddPlantObservationAdapter addPlantObservationAdapter;
    FragmentCallBack_AddPlantObservation fragmentCallBack = null;
    @ViewById
    ListView lv_plant;
    @ViewById
    Button btn_next;
    AddPlantObservation_StepTwo_Adapter addStd_cmd_stepOne_adapter;
    private List<Dictionary> listData = new ArrayList<Dictionary>();
    com.farm.bean.commembertab commembertab;
    Dictionary dic_comm;
    Dictionary_wheel dictionary_wheel;


    @Click
    void btn_next()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 2);
        fragmentCallBack.callbackFun2(bundle);
    }


    @AfterViews
    void afterOncreate()
    {
//        getPlantlist();
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        List<planttab> lsitNewData = JSON.parseArray(JSON.parseObject(jsonObject.getString("plantlist"), Result.class).getRows().toJSONString(), planttab.class);
        if (lsitNewData != null)
        {
            addPlantObservationAdapter = new AddPlantObservationAdapter(getActivity(), lsitNewData);
            lv_plant.setAdapter(addPlantObservationAdapter);
        }
    }

    public List<FJ_SCFJ> getFJ_SCFJList()
    {
        return addPlantObservationAdapter.getFJ_SCFJList();
    }

    public List<planttab> getPlanttabList()
    {
        return addPlantObservationAdapter.getPlanttabList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation__step_three, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }

    private void getPlantlist()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaid", "4");
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("strWhere", "");
        params.addQueryStringParameter("page_size", String.valueOf(AppContext.PAGE_SIZE));
        params.addQueryStringParameter("page_index", String.valueOf(0));
        params.addQueryStringParameter("action", "plantGetList");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
//                String a = responseInfo.result;
//                List<planttab> lsitNewData = null;
//                Result result = JSON.parseObject(responseInfo.result, Result.class);
//                if (result.getResultCode() == 1)
//                {
//                    if (result.getAffectedRows() != 0)
//                    {
//                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), planttab.class);
//                        if (lsitNewData != null)
//                        {
//                            AddPlantObservationAdapter addPlantObservationAdapter = new AddPlantObservationAdapter(getActivity(), lsitNewData);
//                            lv_plant.setAdapter(addPlantObservationAdapter);
//                        }
//
//                    } else
//                    {
//
//                    }
//                } else
//                {
//                    AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    return;
//                }
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
