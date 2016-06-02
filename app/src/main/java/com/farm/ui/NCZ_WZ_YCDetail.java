package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.WZ_Detail;
import com.farm.bean.WZ_YCxx;
import com.farm.bean.commembertab;
import com.farm.common.utils;
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
 * Created by user on 2016/4/8.
 */
@EActivity(R.layout.ncz_wz_ycdetail)
public class NCZ_WZ_YCDetail extends Activity{
    WZ_YCxx wz_yCxx;


    @ViewById
    LinearLayout llview1;
    @ViewById
    View ltview1;
    @ViewById
    LinearLayout llview2;
    @ViewById
    View ltview2;
    @ViewById
    LinearLayout llview3;
    @ViewById
    LinearLayout llview4;
    @ViewById
    View ltview3;
    @ViewById
    View ltview4;
    @ViewById
    TextView goodsname;
    @ViewById
    TextView parkName;
    @ViewById
    TextView storehouseName;
    @ViewById
    TextView nowDate;
    @ViewById
    TextView type;
    @ViewById
    TextView batchName;
    @ViewById
    TextView expDate;
    @ViewById
    TextView expQuantity;
    @ViewById
    TextView quantity;
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    TextView allzhongliang;
    @ViewById
    TextView expzhongliang;

    @Click
    void imgbtn_back()
    {
        finish();
    }

    @AfterViews
    void afteroncreate()
    {
        goodsname.setText(wz_yCxx.getGoodsName());
        parkName.setText(wz_yCxx.getParkName());
        storehouseName.setText(wz_yCxx.getStorehouseName());
        nowDate.setText(wz_yCxx.getNowDate());

        if (wz_yCxx.getType().equals("0"))
        {
            type.setText("将要过期");
//            type.setText(wz_yCxx.getGoodsName()+"将要过期");
            batchName.setText(wz_yCxx.getBatchName());
            expDate.setText(wz_yCxx.getExpDate()+"天");
            expQuantity.setText(wz_yCxx.getExpQuantity());
            quantity.setText(wz_yCxx.getQuantity());
        }else if (wz_yCxx.getType().equals("2"))
        {
            type.setText("将要过期|库存过低");
//            type.setText(wz_yCxx.getGoodsName()+"将要过期|库存过低");
            batchName.setText(wz_yCxx.getBatchName());
            expDate.setText(wz_yCxx.getExpDate()+"天");
            expQuantity.setText(wz_yCxx.getExpQuantity());
            quantity.setText(wz_yCxx.getQuantity());
        }else
        {
            llview1.setVisibility(View.GONE);
            llview2.setVisibility(View.GONE);
            llview3.setVisibility(View.GONE);
            llview4.setVisibility(View.GONE);
            ltview1.setVisibility(View.GONE);
            ltview2.setVisibility(View.GONE);
            ltview3.setVisibility(View.GONE);
            ltview4.setVisibility(View.GONE);
            type.setText("库存过低");
//            type.setText(wz_yCxx.getGoodsName()+"库存过低");
            quantity.setText(wz_yCxx.getQuantity());
        }


        getGoodsxx();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        wz_yCxx=getIntent().getParcelableExtra("wz_yCxx");
    }

    private void getGoodsxx() {

        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsId", wz_yCxx.getGoodsId());
        params.addQueryStringParameter("action", "getGoodsXxById");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<WZ_Detail> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() == 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_Detail.class);
                        allzhongliang.setText(utils.getNum(wz_yCxx.getQuantity())*Double.valueOf(listNewData.get(0).getGoodsStatistical())+listNewData.get(0).getGoodsunit());
                        if (!wz_yCxx.getExpQuantity().equals(""))
                        {
                            expzhongliang.setText(utils.getNum(wz_yCxx.getExpQuantity())*Double.valueOf(listNewData.get(0).getGoodsStatistical())+listNewData.get(0).getGoodsunit());
                        }

                    } else {
                        listNewData = new ArrayList<WZ_Detail>();
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_WZ_YCDetail.this, "error_connectServer");

            }
        });
    }

}
