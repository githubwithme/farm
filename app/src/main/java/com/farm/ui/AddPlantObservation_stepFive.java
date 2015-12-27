package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.AddPlantObservationAdapter_MakeSure;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SelectCmdArea;
import com.farm.bean.commandtab_single;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.bean.planttab;
import com.farm.common.SqliteDb;
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

import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddPlantObservation_stepFive extends Fragment
{
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_gcq;

    @AfterViews
    void afterOncreate()
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        List<planttab> lsitNewData = JSON.parseArray(JSON.parseObject(jsonObject.getString("plantlist"), Result.class).getRows().toJSONString(), planttab.class);
        if (lsitNewData != null)
        {
            AddPlantObservationAdapter_MakeSure addPlantObservationAdapter = new AddPlantObservationAdapter_MakeSure(getActivity(), lsitNewData);
            lv.setAdapter(addPlantObservationAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation_stepfive, container, false);
        return rootView;
    }

    public void updateData(List<Fragment> list_fragment)
    {
        AddPlantObservation_StepThree addPlantObservation_stepThree = (AddPlantObservation_StepThree) list_fragment.get(1);
        List<planttab> list_planttab = addPlantObservation_stepThree.getPlanttabList();
        AddPlantObservationAdapter_MakeSure addPlantObservationAdapter = new AddPlantObservationAdapter_MakeSure(getActivity(), list_planttab);
        lv.setAdapter(addPlantObservationAdapter);
        tv_gcq.setText(list_planttab.get(0).getyNum());
    }

    private void commandTabAdd()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("areaId", tempareaId.substring(0, tempareaId.length() - 1));
//        params.addQueryStringParameter("areaName", tempareaName.substring(0, tempareaName.length() - 1));
//        params.addQueryStringParameter("userid", commembertab.getId());
//        params.addQueryStringParameter("userName", commembertab.getrealName());
//        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("action", "commandTabAdd");
//        params.addQueryStringParameter("parkId", commandtab_single.getparkId());
//        params.addQueryStringParameter("parkName", commandtab_single.getparkName());
//        params.addQueryStringParameter("nongziName", commandtab_single.getnongziName());
//        params.addQueryStringParameter("amount", "");
//        params.addQueryStringParameter("commNote", commandtab_single.getcommNote());
//        params.addQueryStringParameter("commDays", commandtab_single.getcommDays());
//        params.addQueryStringParameter("commComDate", commandtab_single.getcommComDate());
//        params.addQueryStringParameter("stdJobType", commandtab_single.getstdJobType());
//        params.addQueryStringParameter("stdJobTypeName", commandtab_single.getstdJobTypeName());
//        params.addQueryStringParameter("stdJobId", commandtab_single.getstdJobId());
//        params.addQueryStringParameter("stdJobName", commandtab_single.getstdJobName());
//        params.addQueryStringParameter("importance", commandtab_single.getimportance());
//        params.addQueryStringParameter("execLevel", "1");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<jobtab> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
                    if (listData == null)
                    {
                        AppContext.makeToast(getActivity(), "error_connectDataBase");
                    } else
                    {
//                        HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(getActivity(), AppContext.TAG_NCZ_CMD);
//                        if (haveReadRecord != null)
//                        {
//                            SqliteDb.updateHaveReadRecord(getActivity(), AppContext.TAG_NCZ_CMD, String.valueOf((Integer.valueOf(haveReadRecord.getNum()) + 1)));
//                        } else
//                        {
//                            SqliteDb.saveHaveReadRecord(getActivity(), AppContext.TAG_NCZ_CMD, "1");
//                        }
                        Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        commandtab_single.getInstance().clearAll();
                        SqliteDb.deleteAllSelectCmdArea(getActivity(), SelectCmdArea.class);
                    }

                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }
}
