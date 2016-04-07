package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_CKWZDetailAdapter;
import com.farm.adapter.NCZ_WZ_PCExpAdapter;
import com.farm.adapter.NCZ_WZ_PCInAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.WZ_Detail;
import com.farm.bean.WZ_Pcxx;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 2016/4/7.
 */
@EActivity(R.layout.ncz_wz_pcdatail)
public class NCZ_WZ_PCDetail extends FragmentActivity {
//    goodslisttab goods;
WZ_Detail goods;
    WZ_Pcxx wz_pcxx;
    NCZ_WZ_PCInAdapter ncz_wz_pcInAdapter;
    NCZ_WZ_PCExpAdapter listadpater;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView batchName;
    @ViewById
    TextView inDate;
    @ViewById
    TextView exeDate;
    @ViewById
    ListView exdata;
    @ViewById
    ListView indata;
    @ViewById
    ImageButton imgbtn_back;
    @Click
    void imgbtn_back() {
        finish();
        ;
    }
    @AfterViews
    void afteroncreate() {
        tv_title.setText(goods.getGoodsName());
        batchName.setText("批次号" + wz_pcxx.getBatchName());
        inDate.setText(wz_pcxx.getInDate());
        exeDate.setText(wz_pcxx.getExpDate());
        getExdata();
        getIndata();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        goods = getIntent().getParcelableExtra("goods");
        wz_pcxx = getIntent().getParcelableExtra("wz_pcxx");

    }
    private void getExdata()
    {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsId", goods.getGoodsId());
        params.addQueryStringParameter("action", "getWzpcByWzid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<WZ_Pcxx> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_Pcxx.class);
                        Iterator<WZ_Pcxx> it = listNewData.iterator();
                        while (it.hasNext()) {
                            String value = it.next().getBatchNumber();
                            if (!value.equals(wz_pcxx.getBatchNumber()))
                            {
                                it.remove();
                            }
                        }
                        listadpater=new NCZ_WZ_PCExpAdapter(NCZ_WZ_PCDetail.this, listNewData);
                        exdata.setAdapter(listadpater);
                    } else {
                        listNewData = new ArrayList<WZ_Pcxx>();
                    }
                } else {
                    AppContext.makeToast(NCZ_WZ_PCDetail.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_WZ_PCDetail.this, "error_connectServer");

            }
        });
    }
    private void getIndata()
    {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsId", goods.getGoodsId());
        params.addQueryStringParameter("goodsInInfoId",wz_pcxx.getBatchNumber());
        params.addQueryStringParameter("action", "getWzPCNumByWzidPCid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<WZ_Pcxx> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_Pcxx.class);
                        ncz_wz_pcInAdapter=new NCZ_WZ_PCInAdapter(NCZ_WZ_PCDetail.this, listNewData);
                        indata.setAdapter(ncz_wz_pcInAdapter);
                    } else {
                        listNewData = new ArrayList<WZ_Pcxx>();
                    }
                } else {
                    AppContext.makeToast(NCZ_WZ_PCDetail.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_WZ_PCDetail.this, "error_connectServer");

            }
        });
    }
}
