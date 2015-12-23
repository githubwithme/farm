package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commandtab_single;
import com.farm.bean.commembertab;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDatepicker;
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
public class AddStd_Cmd_StepFive extends Fragment
{
    CustomDialog_ListView customDialog_listView;
    Dictionary dic_comm;
    String importance_id;
    String workday;
    String importance;

    @Click
    void tv_importance()
    {
        getCommandlist();
    }

    @Click
    void tv_workday()
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        showDialog_workday(list);
    }

    @Click
    void tv_timelimit()
    {
        MyDatepicker myDatepicker = new MyDatepicker(getActivity(), tv_timelimit);
        myDatepicker.getDialog().show();
    }

    @ViewById
    TextView tv_importance;
    @ViewById
    TextView tv_workday;
    @ViewById
    TextView tv_timelimit;
    @ViewById
    EditText et_note;
    FragmentCallBack fragmentCallBack = null;

    @Click
    void btn_next()
    {
        commandtab_single.getInstance().setimportance(importance);
        commandtab_single.getInstance().setcommComDate(tv_timelimit.getText().toString());//期限
        commandtab_single.getInstance().setcommDays(tv_workday.getText().toString());
        commandtab_single.getInstance().setcommNote(et_note.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 3);
        fragmentCallBack.callbackFun2(bundle);
    }

    @AfterViews
    void afterOncreate()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_five, container, false);
        return rootView;
    }

    private void getCommandlist()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
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
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            dic_comm = lsitNewData.get(0);
                            showDialog_Importance();
                        }

                    } else
                    {
                        lsitNewData = new ArrayList<Dictionary>();
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

    public void showDialog_workday(List<String> list)
    {
        View dialog_layout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(getActivity(), R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                workday = bundle.getString("name");
                tv_workday.setText(workday);
            }
        });
        customDialog_listView.show();
    }

    public void showDialog_Importance()
    {
        View dialog_layout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(getActivity(), R.style.MyDialog, dialog_layout, dic_comm.getFirstItemName(), dic_comm.getFirstItemID(), new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                importance_id = bundle.getString("id");
                importance = bundle.getString("name");
                tv_importance.setText(importance);
            }
        });
        customDialog_listView.show();
    }

    public void showDialog_litmitday()
    {
        View dialog_layout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(getActivity(), R.style.MyDialog, dialog_layout, dic_comm.getFirstItemName(), dic_comm.getFirstItemID(), new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                importance_id = bundle.getString("id");
                importance = bundle.getString("name");
                tv_importance.setText(importance);
            }
        });
        customDialog_listView.show();
    }

    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
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
