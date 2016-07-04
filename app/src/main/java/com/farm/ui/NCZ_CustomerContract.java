package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_Customercontract;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.CustomerContractBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
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

import java.util.List;

/**
 * Created by user on 2016/2/26.
 */
@EActivity(R.layout.ncz_customercontract)
public class NCZ_CustomerContract extends Activity
{
    List<CustomerContractBean> listNewData = null;
    Adapter_Customercontract adapter_customercontract;
    String goodsName;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    ImageButton imgbtn;
    @ViewById
    TextView et_goodsname;

    @Click
    void et_goodsname()
    {
        et_goodsname.setText("");
    }

    @Click
    void imgbtn()
    {
        goodsName = et_goodsname.getText().toString();
        getBreakOffInfoOfContract();
    }

    @AfterViews
    void afterOncreate()
    {
        getActionBar().hide();
        getBreakOffInfoOfContract();
//        getNewSaleList_test();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void getNewSaleList_test()
    {
        listNewData = FileHelper.getAssetsData(NCZ_CustomerContract.this, "getCustomerContract", CustomerContractBean.class);
        if (listNewData != null)
        {
            adapter_customercontract = new Adapter_Customercontract(NCZ_CustomerContract.this, listNewData, expandableListView);
            expandableListView.setAdapter(adapter_customercontract);

            for (int i = 0; i < listNewData.size(); i++)
            {
                expandableListView.expandGroup(i);//展开
            }
        }

    }

    private void getBreakOffInfoOfContract()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CustomerContract.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getCustomerContactsData");
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
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), CustomerContractBean.class);
                    adapter_customercontract = new Adapter_Customercontract(NCZ_CustomerContract.this, listNewData, expandableListView);
                    expandableListView.setAdapter(adapter_customercontract);

                    for (int i = 0; i < listNewData.size(); i++)
                    {
                        expandableListView.expandGroup(i);//展开
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CustomerContract.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CustomerContract.this, "error_connectServer");
            }
        });
    }
}
