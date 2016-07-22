package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_PGAssess;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ContractAssessBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
import com.farm.widget.CustomArrayAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by ${hmj} on 2016/7/12.
 */
@EActivity(R.layout.pg_assessactivity)
public class PG_AssessActivity extends Activity
{
    List<ContractAssessBean> listNewData = null;
    Adapter_PGAssess adapter_pgGoodsUsed;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    Spinner provinceSpinner;
    @ViewById
    Spinner citySpinner;
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private String[] mProvinceDatas = new String[]{"不限时间", "今天", "昨天"};
    private String[] mCitisDatasMap = new String[]{"考核类型", "警告", "不合格", "合格"};


    @AfterViews
    void afterOncreate()
    {
//        getBreakOffInfoOfContract();
        getNewSaleList_test();
        setSpinner();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    private void getNewSaleList_test()
    {
        listNewData = FileHelper.getAssetsData(PG_AssessActivity.this, "getContractAssess", ContractAssessBean.class);
        if (listNewData != null)
        {
            adapter_pgGoodsUsed = new Adapter_PGAssess(PG_AssessActivity.this, listNewData, expandableListView);
            expandableListView.setAdapter(adapter_pgGoodsUsed);

            for (int i = 0; i < listNewData.size(); i++)
            {
                expandableListView.expandGroup(i);//展开
            }
        }

    }

    private void getBreakOffInfoOfContract()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_AssessActivity.this);
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
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), ContractAssessBean.class);
                    adapter_pgGoodsUsed = new Adapter_PGAssess(PG_AssessActivity.this, listNewData, expandableListView);
                    expandableListView.setAdapter(adapter_pgGoodsUsed);

                    for (int i = 0; i < listNewData.size(); i++)
                    {
                        expandableListView.expandGroup(i);//展开
                    }

                } else
                {
                    AppContext.makeToast(PG_AssessActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_AssessActivity.this, "error_connectServer");
            }
        });
    }

    private void setSpinner()
    {
        //绑定适配器和值
        provinceAdapter = new CustomArrayAdapter(PG_AssessActivity.this, mProvinceDatas);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值

        cityAdapter = new CustomArrayAdapter(PG_AssessActivity.this, mCitisDatasMap);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0, true);  //默认选中第0个


        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }

        });


        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

}
