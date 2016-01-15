package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SelectCmdArea;
import com.farm.bean.commandtab_single;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.farm.bean.jobtab;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;
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
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddNotStd_Cmd_StepSix_Self extends Fragment
{
    FragmentCallBack fragmentCallBack;
    @ViewById
    TextView tv_importance;
    @ViewById
    TextView tv_workday;
    @ViewById
    TextView tv_timelimit;
    @ViewById
    TextView tv_note;
    @ViewById
    TextView tv_flyl;
    commandtab_single commandtab_single;
    com.farm.bean.commandtab commandtab;
    String nongzi_temp = "";
    String nongzi = "";
    String nongziId = "";
    String tempareaId = "";
    String tempflyl = "";
    String tempareaName = "";
    List<goodslisttab> list_goodslisttab = new ArrayList<goodslisttab>();

    @Click
    void btn_sure()
    {
        commandTabAdd();
    }

    @AfterViews
    void afterOncreate()
    {
        showData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_notstd__cmd__step_six_self, container, false);
        return rootView;
    }

    public void showData()
    {
        nongzi = "";
        nongziId = "";
        nongzi_temp = "";
        tempareaId = "";
        tempareaName = "";
        tempflyl = "";
        list_goodslisttab = SqliteDb.getSelectCmdArea(getActivity(), goodslisttab.class);
        for (int i = 0; i < list_goodslisttab.size(); i++)
        {
            nongzi = nongzi + list_goodslisttab.get(i).getgoodsName() + ",";
            nongziId = nongziId + list_goodslisttab.get(i).getId() + ",";
            nongzi_temp = nongzi_temp + list_goodslisttab.get(i).getgoodsName() + "\n";
        }
        for (int i = 0; i < list_goodslisttab.size(); i++)
        {
            tempareaId = tempareaId + list_goodslisttab.get(i).getParkId() + ":" + list_goodslisttab.get(i).getAreaId() + ":" + list_goodslisttab.get(i).getYL() + ",";
            tempareaName = tempareaName + list_goodslisttab.get(i).getParkName() + ":" + list_goodslisttab.get(i).getAreaName() + ",";
            tempflyl =tempflyl+list_goodslisttab.get(i).getgoodsName() +"  "+list_goodslisttab.get(i).getYL()+ "\n";
        }
        commandtab_single = com.farm.bean.commandtab_single.getInstance();
        tv_importance.setText(commandtab_single.getImportancetype());
        tv_flyl.setText(tempflyl);
        tv_workday.setText(commandtab_single.getcommDays());
        tv_note.setText(commandtab_single.getcommNote());
        tv_timelimit.setText(commandtab_single.getcommComDate());
    }

    public void update()
    {
        showData();
    }

    private void commandTabAdd()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaId", tempareaId.substring(0, tempareaId.length() - 1));
        params.addQueryStringParameter("areaName", tempareaName.substring(0, tempareaName.length() - 1));
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("userName", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "commandTabAdd");
        params.addQueryStringParameter("parkId", commandtab_single.getparkId());
        params.addQueryStringParameter("parkName", commandtab_single.getparkName());
        params.addQueryStringParameter("nongziName", nongzi.substring(0, nongzi.length() - 1));
        params.addQueryStringParameter("amount", nongziId.substring(0, nongziId.length() - 1));
        params.addQueryStringParameter("commNote", commandtab_single.getcommNote());
        params.addQueryStringParameter("commDays", commandtab_single.getcommDays());
        params.addQueryStringParameter("commComDate", commandtab_single.getcommComDate());
        params.addQueryStringParameter("stdJobType", commandtab_single.getstdJobType());
        params.addQueryStringParameter("stdJobTypeName", commandtab_single.getstdJobTypeName());
        params.addQueryStringParameter("stdJobId", commandtab_single.getstdJobId());
        params.addQueryStringParameter("stdJobName", commandtab_single.getstdJobName());
        params.addQueryStringParameter("importance", commandtab_single.getimportance());
        params.addQueryStringParameter("execLevel", "1");
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
                        SqliteDb.deleteAllSelectCmdArea(getActivity(), goodslisttab.class);
                        SqliteDb.deleteAllSelectCmdArea(getActivity(),goodslisttab_flsl.class);
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
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        getActivity().finish();
    }
}
