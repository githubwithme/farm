package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_PGGoodsUsed;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ContactsBean;
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
 * Created by ${hmj} on 2016/7/12.
 */
@EActivity(R.layout.pg_goodsused)
public class PG_GoodsUsed extends Activity
{
    List<ContactsBean> listNewData = null;
    Adapter_PGGoodsUsed adapter_pgGoodsUsed;
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
        getBreakOffInfoOfContract();
//        getNewSaleList_test();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    private void getNewSaleList_test()
    {
        listNewData = FileHelper.getAssetsData(PG_GoodsUsed.this, "getUserInfo", ContactsBean.class);
        if (listNewData != null)
        {
            adapter_pgGoodsUsed = new Adapter_PGGoodsUsed(PG_GoodsUsed.this, listNewData, expandableListView);
            expandableListView.setAdapter(adapter_pgGoodsUsed);

            for (int i = 0; i < listNewData.size(); i++)
            {
                expandableListView.expandGroup(i);//展开
            }
        }

    }

    private void getBreakOffInfoOfContract()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_GoodsUsed.this);
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
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), ContactsBean.class);
                    adapter_pgGoodsUsed = new Adapter_PGGoodsUsed(PG_GoodsUsed.this, listNewData, expandableListView);
                    expandableListView.setAdapter(adapter_pgGoodsUsed);

                    for (int i = 0; i < listNewData.size(); i++)
                    {
                        expandableListView.expandGroup(i);//展开
                    }

                } else
                {
                    AppContext.makeToast(PG_GoodsUsed.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_GoodsUsed.this, "error_connectServer");
            }
        });
    }
}
