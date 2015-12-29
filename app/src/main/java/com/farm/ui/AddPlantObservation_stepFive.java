package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.AddPlantObservationAdapter_MakeSure;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.PlantGcjl;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.bean.plantgrowthtab;
import com.farm.com.custominterface.FragmentCallBack_AddPlantObservation;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddPlantObservation_stepFive extends Fragment
{
    List<plantgrowthtab> list_plantgrowthtab = new ArrayList<>();
    CountDownLatch latch;
    List<FJ_SCFJ> list_fj_scfj = new ArrayList<>();
    AddPlantObservation_stepfour addPlantObservation_stepfour;
    AddPlantObservation_StepThree addPlantObservation_stepThree;
    AddPlantObservation_StepTwo addPlantObservation_stepTwo;
    String gcjlid;
    String gcq;
    String GXBX;
    String JJBX;
    String YBX;
    List<Fragment> list_fragment;
    FragmentCallBack_AddPlantObservation fragmentCallBack = null;
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_gcq;
    @ViewById
    TextView tv_jj;
    @ViewById
    TextView tv_y;
    @ViewById
    TextView tv_gx;
    @ViewById
    ProgressBar pb_upload;
    @ViewById
    Button btn_save;

    @Click
    void btn_save()
    {
        btn_save.setVisibility(View.GONE);
        pb_upload.setVisibility(View.VISIBLE);
        saveData();
    }

    @AfterViews
    void afterOncreate()
    {
        AddPlantObservationAdapter_MakeSure addPlantObservationAdapter = new AddPlantObservationAdapter_MakeSure(getActivity(), list_plantgrowthtab, list_fj_scfj);
        lv.setAdapter(addPlantObservationAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation_stepfive, container, false);
        return rootView;
    }

    public void updateData(List<Fragment> list_fragment)
    {
        addPlantObservation_stepTwo = (AddPlantObservation_StepTwo) (list_fragment.get(0));
        addPlantObservation_stepThree = (AddPlantObservation_StepThree) (list_fragment.get(1));
        addPlantObservation_stepfour = (AddPlantObservation_stepfour) (list_fragment.get(2));

        list_fj_scfj = addPlantObservation_stepThree.getFJ_SCFJList();
//        JJBX = addPlantObservation_stepfour.getJJBB();
//        YBX = addPlantObservation_stepfour.getYBX();
//        GXBX = addPlantObservation_stepfour.getGXBX();

        list_plantgrowthtab = addPlantObservation_stepThree.getPlanttabList();
        AddPlantObservationAdapter_MakeSure addPlantObservationAdapter = new AddPlantObservationAdapter_MakeSure(getActivity(), list_plantgrowthtab, list_fj_scfj);
        lv.setAdapter(addPlantObservationAdapter);


        gcq = addPlantObservation_stepTwo.getGcq();
        tv_gcq.setText(gcq);
        tv_jj.setText(JJBX);
        tv_y.setText(YBX);
        tv_gx.setText(GXBX);
    }

    private void saveData()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("userName", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "gcjlAdd");

        params.addQueryStringParameter("gcdid", fragmentCallBack.getGcdId());
        params.addQueryStringParameter("gcq", gcq);
        params.addQueryStringParameter("jjbx", JJBX);
        params.addQueryStringParameter("ybx", YBX);
        params.addQueryStringParameter("gxbx", GXBX);
        params.addQueryStringParameter("lbx", "");
        params.addQueryStringParameter("ghbx", "");
        params.addQueryStringParameter("csgbx", "");
        params.addQueryStringParameter("cygbx", "");
        params.addQueryStringParameter("szfx", "");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PlantGcjl> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), PlantGcjl.class);
                    if (listData == null)
                    {
                        AppContext.makeToast(getActivity(), "error_connectDataBase");
                    } else
                    {
                        gcjlid = listData.get(0).getId();
                        if (list_fj_scfj.size() > 0 || list_plantgrowthtab.size() > 0)
                        {
                            latch = new CountDownLatch(list_fj_scfj.size() + list_plantgrowthtab.size());
                            for (int j = 0; j < list_fj_scfj.size(); j++)
                            {
                                uploadMedia(listData.get(0).getId(), list_fj_scfj.get(j).getFJBDLJ(),list_fj_scfj.get(j).getFJID());
                            }
                            for (int j = 0; j < list_plantgrowthtab.size(); j++)
                            {
                                savePlant(list_plantgrowthtab.get(j));
                            }
                        } else
                        {
                            Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }

                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    pb_upload.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
                pb_upload.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
            }
        });
    }

    private void savePlant(plantgrowthtab plantgrowthtab)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("userName", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkId", commembertab.getparkId());
        params.addQueryStringParameter("parkName", commembertab.getparkName());
        params.addQueryStringParameter("areaId", commembertab.getareaId());
        params.addQueryStringParameter("areaName", commembertab.getareaName());
        params.addQueryStringParameter("action", "plantGrowthTabAdd");

        params.addQueryStringParameter("yNum", plantgrowthtab.getyNum());
        params.addQueryStringParameter("wNum", plantgrowthtab.getwNum());
        params.addQueryStringParameter("hNum", plantgrowthtab.gethNum());
        params.addQueryStringParameter("xNum", plantgrowthtab.getxNum());
        params.addQueryStringParameter("yColor", plantgrowthtab.getyColor());
        params.addQueryStringParameter("Ext1", "");
        params.addQueryStringParameter("cDate", plantgrowthtab.getcDate());
        params.addQueryStringParameter("zDate", plantgrowthtab.getzDate());
        params.addQueryStringParameter("gcjlid", gcjlid);
        params.addQueryStringParameter("plantId", plantgrowthtab.getplantId());
        params.addQueryStringParameter("plantName", plantgrowthtab.getplantName());
        params.addQueryStringParameter("plantType", plantgrowthtab.getplantType());
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

                        Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

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
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void uploadMedia(String plantgrowthId, String path,String plantId)
    {
        File file = new File(path);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "UpLoadFilePlantImg");
        params.addQueryStringParameter("plantgrowthId", plantgrowthId);
        params.addQueryStringParameter("plantId", plantId);
        params.addQueryStringParameter("file", file.getName());
        params.setBodyEntity(new FileUploadEntity(file, "text/html"));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new RequestCallBack<String>()
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
                        showProgress();
                    } else
                    {
                        AppContext.makeToast(getActivity(), "error_connectDataBase");
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
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void showProgress()
    {
        latch.countDown();
        Long l = latch.getCount();
        if (l.intValue() == 0) // 全部线程是否已经结束
        {
            Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack_AddPlantObservation) activity;
    }
}
