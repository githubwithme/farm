package com.farm.ui;

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
import com.farm.bean.WZ_RKxx;
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
 * Created by user on 2016/4/8.
 */
@EActivity(R.layout.ncz_wz_rkdetail)
public class NCZ_WZ_RKDetail extends FragmentActivity {
    private WZ_RKxx wz_rKxx;
    private String batchname;
    private String indate;

    @ViewById
    TextView guige;
    WZ_Detail wz_detail;
    @ViewById
    TextView goodsname;
    @ViewById
    TextView tv_title;

    @ViewById
    TextView parkName;
    @ViewById
    TextView zhongliang;
    @ViewById
    TextView storehouseName;
    @ViewById
    TextView batchName;
    @ViewById
    TextView inDate;
    @ViewById
    TextView exeDate;
    @ViewById
    TextView inType;
    @ViewById
    TextView quantity;
    @ViewById
    TextView price;
    @ViewById
    TextView inGoodsvalue;
    @ViewById
    TextView note;


    @ViewById
    ImageButton imgbtn_back;

    @Click
    void imgbtn_back() {
        finish();
    }

    @AfterViews
    void aftercreate() {
        getGoodsxx();
//        zhongliang.setText(utils.getNum(wz_rKxx.getQuantity()));
        goodsname.setText(wz_rKxx.getGoodsname());
//        tv_title.setText(batchname);

        parkName.setText(wz_rKxx.getParkName());
        storehouseName.setText(wz_rKxx.getStorehouseName());
        batchName.setText(batchname);
        inDate.setText(indate);
        String expdate=wz_rKxx.getExpDate().substring(0,wz_rKxx.getExpDate().length()-8);
        exeDate.setText(expdate);
        inType.setText(wz_rKxx.getInType());
        quantity.setText(wz_rKxx.getQuantity());
        price.setText(wz_rKxx.getPrice());
        inGoodsvalue.setText(String.format("%.2f", Double.valueOf(wz_rKxx.getInGoodsvalue())) + "元");
        if (wz_rKxx.getNote().equals("")) {
            note.setText("无");
        } else {
            note.setText(wz_rKxx.getNote());
        }
        zhongliang.setText(wz_rKxx.getSumWeight());
//        getGoodsxx();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        wz_rKxx = getIntent().getParcelableExtra("wz_rKxx");
        batchname = getIntent().getStringExtra("batchname");
        indate = getIntent().getStringExtra("indate");

    }

    private void getGoodsxx()
    {

        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsId", wz_rKxx.getGoodsid());
        params.addQueryStringParameter("action", "getGoodsXxById");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<WZ_Detail> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() == 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_Detail.class);

                        wz_detail = listNewData.get(0);
                        if (!wz_detail.getThree().equals(""))
                        {
                            guige.setText(wz_detail.getGoodsStatistical() + wz_detail.getGoodsunit() + "/" + wz_detail.getThree());
                        } else if (wz_detail.getThree().equals("") && !wz_detail.getSec().equals(""))
                        {
                            guige.setText(wz_detail.getGoodsStatistical() + wz_detail.getGoodsunit() + "/" + wz_detail.getSec());
                        } else
                        {
                            guige.setText(wz_detail.getGoodsStatistical() + wz_detail.getGoodsunit() + "/" + wz_detail.getFirs());
                        }
                    } else
                    {
                        listNewData = new ArrayList<WZ_Detail>();
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_WZ_RKDetail.this, "error_connectServer");

            }
        });
    }
}
