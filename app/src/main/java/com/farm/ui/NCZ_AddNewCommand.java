package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.Adapter_CommandSelectArea;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ContactsBean;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.CustomExpandableListView;
import com.farm.widget.MyDatepicker;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/7/13.
 */
@EActivity(R.layout.ncz_addnewcommand)
public class NCZ_AddNewCommand extends Activity
{
    @ViewById
    RelativeLayout rl_selectgoods;
    @ViewById
    RelativeLayout rl_selecttime;
    @ViewById
    RelativeLayout rl_selectarea;
    @ViewById
    RelativeLayout rl_selectjob;
    @ViewById
    TextView tv_starttime;
    @ViewById
    TextView tv_days;
    @ViewById
    TextView tv_jobtype;
    @ViewById
    CustomExpandableListView expandableListView;

    CustomDialog_ListView customDialog_listView;
    String workday = "";
    String jobTypeName = "";
    String jobTypeId = "";

    @Click
    void rl_selectjob()
    {
        getCommandlist("19", "香蕉");
    }

    @Click
    void rl_selectgoods()
    {
        DialogFragment_SelectGoods dialog = new DialogFragment_SelectGoods();
        dialog.show(getFragmentManager(), "EditNameDialog");
    }

    @Click
    void rl_selectarea()
    {
        if (expandableListView.isShown())
        {
            expandableListView.setVisibility(View.GONE);
        } else
        {
            expandableListView.setVisibility(View.VISIBLE);
            getAreaList();
        }
    }

    @Click
    void rl_selecttime()
    {
        MyDatepicker myDatepicker = new MyDatepicker(NCZ_AddNewCommand.this, tv_starttime);
        myDatepicker.getDialog().show();
    }

    @Click
    void rl_selectdays()
    {
        JSONObject jsonObject = utils.parseJsonFile(NCZ_AddNewCommand.this, "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        showDialog_workday(list);
    }


    @AfterViews
    void AfterViews()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    public void showDialog_workday(List<String> list)
    {
        View dialog_layout = (RelativeLayout) NCZ_AddNewCommand.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_AddNewCommand.this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                workday = bundle.getString("name");
                tv_days.setText(workday);
            }
        });
        customDialog_listView.show();
    }

    public void showDialog_selectJobType(List<String> list_name, List<String> list_id)
    {
        View dialog_layout = (RelativeLayout) NCZ_AddNewCommand.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_AddNewCommand.this, R.style.MyDialog, dialog_layout, list_name, list_id, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                jobTypeName = bundle.getString("name");
                jobTypeId = bundle.getString("id");
                tv_jobtype.setText(jobTypeName);
            }
        });
        customDialog_listView.show();
    }

    private void getAreaList()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_AddNewCommand.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getContactsData");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<ContactsBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), ContactsBean.class);
                    Adapter_CommandSelectArea adapter_CommandSelectArea = new Adapter_CommandSelectArea(NCZ_AddNewCommand.this, listNewData, expandableListView);
                    expandableListView.setAdapter(adapter_CommandSelectArea);
                    for (int i = 0; i < listNewData.size(); i++)
                    {
                        expandableListView.expandGroup(i);//展开
                    }

                } else
                {
                    AppContext.makeToast(NCZ_AddNewCommand.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_AddNewCommand.this, "error_connectServer");
            }
        });
    }

    private void getCommandlist(String zwid, String zwname)
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_AddNewCommand.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("productid", zwid);
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
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            List<String> list_id = new ArrayList<String>();
                            List<String> list_name = new ArrayList<String>();
                            for (int i = 0; i < lsitNewData.size(); i++)
                            {
                                list_id.add(lsitNewData.get(i).getFirstItemID().toString());
                                list_name.add(lsitNewData.get(i).getFirstItemName().toString());
                            }
                            showDialog_selectJobType(list_name, list_id);
                        } else
                        {

                        }

                    } else
                    {
                        AppContext.makeToast(NCZ_AddNewCommand.this, "error_connectDataBase");
                        return;
                    }
                } else
                {
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_AddNewCommand.this, "error_connectServer");
            }
        });
    }


}
