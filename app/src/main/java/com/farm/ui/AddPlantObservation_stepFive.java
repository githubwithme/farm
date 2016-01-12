package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.AddPlantObservation_stepFive_bx_Adapter;
import com.farm.adapter.AddPlantObservation_stepFive_zz_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.PlantGcjl;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.bean.plantgrowthtab;
import com.farm.com.custominterface.FragmentCallBack_AddPlantObservation;
import com.farm.common.utils;
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
    AddPlantObservation_stepFive_bx_Adapter addPlantObservation_stepFive_bx_adapter;
    AddPlantObservation_stepFive_zz_Adapter addPlantObservation_stepFive_zz_adapter;
    Dictionary dic;
    List<plantgrowthtab> list_plantgrowthtab = new ArrayList<>();
    CountDownLatch latch;
    List<FJ_SCFJ> list_fj_scfj = new ArrayList<>();
    AddPlantObservation_stepfour addPlantObservation_stepfour;
    AddPlantObservation_StepThree addPlantObservation_stepThree;
    AddPlantObservation_StepTwo addPlantObservation_stepTwo;
    String gcjlid;
    String gcq;
    String GXBX = "";
    String JJBX = "";
    String YBX = "";
    String LDBX = "";
    String GSBX = "";
    String CSGBX = "";
    String CYGBX = "";
    List<Fragment> list_fragment;
    FragmentCallBack_AddPlantObservation fragmentCallBack = null;
    @ViewById
    TextView tv_gcq;
    @ViewById
    ProgressBar pb_upload;
    @ViewById
    Button btn_save;
    @ViewById
    ExpandableListView expanded_bx;
    @ViewById
    ExpandableListView expanded_zz;

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
//        AddPlantObservationAdapter_MakeSure addPlantObservationAdapter = new AddPlantObservationAdapter_MakeSure(getActivity(), list_plantgrowthtab, list_fj_scfj);
//        lv.setAdapter(addPlantObservationAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation_stepfive, container, false);
        return rootView;
    }

    public void updateData(List<Fragment> list_fragment, Dictionary dictionary)
    {
        dic = dictionary;
        addPlantObservation_stepFive_bx_adapter = new AddPlantObservation_stepFive_bx_Adapter(getActivity(), dic, expanded_bx);
        expanded_bx.setAdapter(addPlantObservation_stepFive_bx_adapter);
//        utils.setListViewHeightBasedOnChildren(expanded_bx);
        utils.setListViewHeight(expanded_bx);
//        for (int i = 0; i < dic.getFirstItemName().size(); i++)
//        {
//            expanded_bx.expandGroup(i);
//        }

        addPlantObservation_stepTwo = (AddPlantObservation_StepTwo) (list_fragment.get(0));
        addPlantObservation_stepThree = (AddPlantObservation_StepThree) (list_fragment.get(1));
        addPlantObservation_stepfour = (AddPlantObservation_stepfour) (list_fragment.get(2));

        list_fj_scfj = addPlantObservation_stepThree.getFJ_SCFJList();

        list_plantgrowthtab = addPlantObservation_stepThree.getPlanttabList();
//        AddPlantObservationAdapter_MakeSure addPlantObservationAdapter = new AddPlantObservationAdapter_MakeSure(getActivity(), list_plantgrowthtab, list_fj_scfj);
//        lv.setAdapter(addPlantObservationAdapter);

        addPlantObservation_stepFive_zz_adapter = new AddPlantObservation_stepFive_zz_Adapter(getActivity(), expanded_zz, list_plantgrowthtab, list_fj_scfj);
        expanded_zz.setAdapter(addPlantObservation_stepFive_zz_adapter);
//        utils.setListViewHeightBasedOnChildren(expanded_zz);
        utils.setListViewHeight(expanded_zz);
//        for (int i = 0; i < list_plantgrowthtab.size(); i++)
//        {
//            expanded_zz.expandGroup(i);
//        }
        gcq = addPlantObservation_stepTwo.getGcq();
        tv_gcq.setText(gcq);


        List<String> firstItemName = dic.getFirstItemName();
        List<List<String>> secondItemName = dic.getSecondItemName();
        List<List<List<String>>> thirdItemID = dic.getThirdItemID();
        for (int i = 0; i < firstItemName.size(); i++)
        {
            if (firstItemName.get(i).equals("假茎表现"))
            {
                for (int j = 0; j < secondItemName.get(i).size(); j++)
                {
                    JJBX = JJBX + secondItemName.get(i).get(j) + ":" + thirdItemID.get(i).get(j).get(0) + ",";
                }
            }
            if (firstItemName.get(i).equals("叶表现"))
            {
                for (int j = 0; j < secondItemName.get(i).size(); j++)
                {
                    YBX = YBX + secondItemName.get(i).get(j) + ":" + thirdItemID.get(i).get(j).get(0) + ",";
                }
            }
            if (firstItemName.get(i).equals("根系表现"))
            {
                for (int j = 0; j < secondItemName.get(i).size(); j++)
                {
                    GXBX = GXBX + secondItemName.get(i).get(j) + ":" + thirdItemID.get(i).get(j).get(0) + ",";
                }
            }
            if (firstItemName.get(i).equals("蕾的表现"))
            {
                for (int j = 0; j < secondItemName.get(i).size(); j++)
                {
                    LDBX = LDBX + secondItemName.get(i).get(j) + ":" + thirdItemID.get(i).get(j).get(0) + ",";
                }
            }
            if (firstItemName.get(i).equals("果穗表现"))
            {
                for (int j = 0; j < secondItemName.get(i).size(); j++)
                {
                    GSBX = GSBX + secondItemName.get(i).get(j) + ":" + thirdItemID.get(i).get(j).get(0) + ",";
                }
            }
            if (firstItemName.get(i).equals("采收果表现"))
            {
                for (int j = 0; j < secondItemName.get(i).size(); j++)
                {
                    CSGBX = CSGBX + secondItemName.get(i).get(j) + ":" + thirdItemID.get(i).get(j).get(0) + ",";
                }
            }
            if (firstItemName.get(i).equals("储运果表现"))
            {
                for (int j = 0; j < secondItemName.get(i).size(); j++)
                {
                    CYGBX = CYGBX + secondItemName.get(i).get(j) + ":" + thirdItemID.get(i).get(j).get(0) + ",";
                }
            }

        }
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

        if (JJBX == null || JJBX.equals(""))
        {
            params.addQueryStringParameter("jjbx", "");
        } else
        {
            params.addQueryStringParameter("jjbx", JJBX.substring(0, JJBX.length() - 1));
        }
        if (YBX == null || YBX.equals(""))
        {
            params.addQueryStringParameter("ybx", "");
        } else
        {
            params.addQueryStringParameter("ybx", YBX.substring(0, YBX.length() - 1));
        }
        if (GXBX == null || GXBX.equals(""))
        {
            params.addQueryStringParameter("gxbx", "");
        } else
        {
            params.addQueryStringParameter("gxbx", GXBX.substring(0, GXBX.length() - 1));
        }

        if (LDBX == null || LDBX.equals(""))
        {
            params.addQueryStringParameter("lbx", "");
        } else
        {
            params.addQueryStringParameter("lbx", LDBX.substring(0, LDBX.length() - 1));
        }
        if (GSBX == null || GSBX.equals(""))
        {
            params.addQueryStringParameter("ghbx", "");
        } else
        {
            params.addQueryStringParameter("ghbx", GSBX.substring(0, GSBX.length() - 1));
        }
        if (CSGBX == null || CSGBX.equals(""))
        {
            params.addQueryStringParameter("csgbx", "");
        } else
        {
            params.addQueryStringParameter("csgbx", CSGBX.substring(0, CSGBX.length() - 1));
        }
        if (CYGBX == null || CYGBX.equals(""))
        {
            params.addQueryStringParameter("cygbx", "");
        } else
        {
            params.addQueryStringParameter("cygbx", CYGBX.substring(0, CYGBX.length() - 1));
        }
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
                                uploadMedia(listData.get(0).getId(), list_fj_scfj.get(j).getFJBDLJ(), list_fj_scfj.get(j).getFJID());
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
//                    listData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
//                    if (listData == null)
//                    {
//                        AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    } else
//                    {
//                        Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
//                        getActivity().finish();
//                    }

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
            public void onFailure(HttpException error, String arg1)
            {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void uploadMedia(String plantgrowthId, String path, String plantId)
    {
        File file = new File(path);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "UpLoadFilePlantImg");
        params.addQueryStringParameter("plantgrowthId", plantgrowthId);
        params.addQueryStringParameter("plantId", plantId);
        params.addQueryStringParameter("file", file.getName());
        params.setBodyEntity(new FileUploadEntity(file, "text/html"));
        HttpUtils http = new HttpUtils();
//        http.configRequestThreadPoolSize(15);
//        http.configRequestRetryCount(15);
        http.configTimeout(60000);
        http.configSoTimeout(60000);
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
