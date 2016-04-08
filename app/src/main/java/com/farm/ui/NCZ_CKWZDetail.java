package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.adapter.CustomExpandableListAdapter_Goods;
import com.farm.adapter.NCZ_CKWZDetailAdapter;
import com.farm.adapter.NCZ_WZ_CKWZAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.R;
import com.farm.bean.commembertab;
import com.farm.common.DictionaryHelper;
import com.farm.common.UIHelper;
import com.farm.widget.NewDataToast;
import com.farm.widget.PullToRefreshListView;
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
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/4/6.
 */
@EActivity(R.layout.ncz_ckwzdetail)
public class NCZ_CKWZDetail extends FragmentActivity {

    String storehouseId;
    String goodsId;
    String localName;
    String goodsName;

    NCZ_CKWZDetailAdapter listadpater;
    commembertab commembertab;
    private List<Wz_Storehouse> listData = new ArrayList<Wz_Storehouse>();
    Wz_Storehouse Wz_Storehouse;
    Wz_Storehouse wz_storehouse;
    @ViewById
    ListView wz_frame_listview;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView goodname;
    @ViewById
    ImageButton btn_back;

    @Click
    void btn_back() {
        finish();
    }

    @AfterViews
    void after() {
//        tv_title.setText(Wz_Storehouse.getParkName()+"-"+Wz_Storehouse.getStorehouseName());
//        tv_title.setText(wz_storehouse.getGoodsName());
        tv_title.setText(localName);
        tv_title.setText(goodsName);
        getListData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
//        Wz_Storehouse=getIntent().getParcelableExtra("storehouseId");
//        wz_storehouse=getIntent().getParcelableExtra("goods");
        storehouseId = getIntent().getStringExtra("storehouseId");
        goodsId = getIntent().getStringExtra("goodsId");
        localName = getIntent().getStringExtra("localName");
        goodsName = getIntent().getStringExtra("goodsName");

    }

    private void getListData() {

        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("storehouseId", storehouseId);
        params.addQueryStringParameter("goodsId", goodsId);
        params.addQueryStringParameter("action", "getPCSLByWzIdCKId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<Wz_Storehouse> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                        listadpater = new NCZ_CKWZDetailAdapter(NCZ_CKWZDetail.this, listNewData);
                        wz_frame_listview.setAdapter(listadpater);
                    } else {
                        listNewData = new ArrayList<Wz_Storehouse>();
                    }
                } else {
                    AppContext.makeToast(NCZ_CKWZDetail.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_CKWZDetail.this, "error_connectServer");

            }
        });
    }

}
