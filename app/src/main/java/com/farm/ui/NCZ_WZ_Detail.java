package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.WZ_Detail;
import com.farm.bean.commembertab;
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
 * Created by user on 2016/4/7.
 */
@EActivity(R.layout.ncz_wz_detail)
public class NCZ_WZ_Detail extends FragmentActivity {
    WZ_Detail goods;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView daysBeforeWarning;
    @ViewById
    TextView levelOfWarning;
    @ViewById
    TextView goodsNote;
    @ViewById
    TextView goodsunit;
    @ViewById
    TextView goodsSpec;
    @ViewById
    ImageButton imgbtn_back;
    @Click
    void imgbtn_back() {
        finish();
        ;
    }
    @AfterViews
    void after() {
        tv_title.setText(goods.getGoodsName());
        getIndata();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        goods = getIntent().getParcelableExtra("goods");
    }

    private void getIndata() {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsId", goods.getGoodsId());
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
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_Detail.class);
                          WZ_Detail wz_detail=listNewData.get(0);
                        daysBeforeWarning.setText(wz_detail.getDaysBeforeWarning()+"天");
                        levelOfWarning.setText(wz_detail.getLevelOfWarning()+"天");

                        if (wz_detail.getGoodsNote().equals(""))
                        {
                            goodsNote.setText("暂无信息");
                        }else {
                            goodsNote.setText(wz_detail.getGoodsNote());
                        }


                        goodsunit.setText(wz_detail.getFirs()+"/"+wz_detail.getGoodsStatistical()+wz_detail.getGoodsunit());
                        if (wz_detail.getSec().equals(""))
                        {
                            goodsSpec.setText(wz_detail.getFirs());
                        }else if(!wz_detail.getSec().equals("")||wz_detail.getThree().equals("")){
                            goodsSpec.setText(wz_detail.getFirs()+"/"+wz_detail.getSecNum()+wz_detail.getSec());
                        }else{
                            goodsSpec.setText(wz_detail.getFirs()+"/"+wz_detail.getSecNum()+wz_detail.getSec()+"\n"+
                                                       wz_detail.getSec()+"/"+wz_detail.getThreeNum()+wz_detail.getThree());
                        }

                    } else {
                        listNewData = new ArrayList<WZ_Detail>();
                    }
                } else {
                    AppContext.makeToast(NCZ_WZ_Detail.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_WZ_Detail.this, "error_connectServer");

            }
        });
    }

}
