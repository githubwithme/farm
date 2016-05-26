package com.farm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.PG_WZckbean;
import com.farm.bean.Result;
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
 * Created by user on 2016/5/21.
 */
@EActivity(R.layout.pg_wz_ckdetail)
public class PG_WZ_CKDetail extends FragmentActivity {

    private PG_WZckbean pg_wZckbean;
    private String inType;
    private String indate;
    private String batchnames;
    String dw;
    String quiltys;
    @ViewById
    RelativeLayout ll_add;
    @ViewById
    LinearLayout ll_pgck;

    @ViewById
    TextView goodsname;
    @ViewById
    TextView parkName;
    @ViewById
    TextView storehouseName;
    @ViewById
    TextView batchName;
    @ViewById
    TextView inDate;
    @ViewById
    TextView outType;
    @ViewById
    EditText quantity;
    @ViewById
    ProgressBar add_upload;
    @ViewById
    Button btn_save;
    @ViewById
    Button btn_case;


    @ViewById
    ImageButton imgbtn_back;

    @Click
    void imgbtn_back() {
        finish();
    }

    @Click
    void btn_case() {
        ll_add.setVisibility(View.GONE);
        quantity.setEnabled(false);
        quantity.setText(quiltys);
    }

    @AfterViews
    void afteroncreate() {
        if (inType.equals("已确认")) {
            ll_pgck.setVisibility(View.GONE);
            ll_add.setVisibility(View.GONE);
        }
        quantity.setEnabled(false);
        init();
    }

    @Click
    void tv_bianjie() {
        quantity.setEnabled(true);
        quantity.setText("");
        ll_add.setVisibility(View.VISIBLE);

    }


    @Click
    void btn_save() {
        if (quantity.getText().toString().equals("")) {
            Toast.makeText(PG_WZ_CKDetail.this, "请填写数量！", Toast.LENGTH_SHORT).show();
        } else {
            String firsNum = "";
            String secNum = "";
            String threeNum = "";
            if (dw.equals("firsNum")) {
                firsNum = quantity.getText().toString();
            } else if (dw.equals("secNum")) {
                secNum = quantity.getText().toString();
            } else {
                threeNum = quantity.getText().toString();
            }
            add_upload.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.GONE);
            UpdatePgck(firsNum, secNum, threeNum);
        }

    }

    @Click
    void tv_delete() {
        deletePgck();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        pg_wZckbean = getIntent().getParcelableExtra("pg_wZckbean");
        inType = getIntent().getStringExtra("inType");
        indate = getIntent().getStringExtra("indate");
        batchnames = getIntent().getStringExtra("batchname");
    }

    public void init() {
        goodsname.setText(pg_wZckbean.getGoodsName());
        parkName.setText(pg_wZckbean.getParkName());
        storehouseName.setText(pg_wZckbean.getStoreName());
        batchName.setText(batchnames);
        outType.setText(inType);

        String indates = indate.substring(0, indate.length() - 8);
        inDate.setText(indates);

        if (!pg_wZckbean.getThree().equals("")) {
            quantity.setText(pg_wZckbean.getThreeNum() + pg_wZckbean.getThree());
            dw = "threeNum";
            quiltys=pg_wZckbean.getThreeNum() + pg_wZckbean.getThree();
        } else if (pg_wZckbean.getThree().equals("") && !pg_wZckbean.getSec().equals("")) {
            quantity.setText(pg_wZckbean.getSecNum() + pg_wZckbean.getSec());
            dw = "secNum";
            quiltys=pg_wZckbean.getSecNum() + pg_wZckbean.getSec();
        } else {
            quantity.setText(pg_wZckbean.getFirsNum() + pg_wZckbean.getFirs());
            dw = "firsNum";
            quiltys=pg_wZckbean.getFirsNum() + pg_wZckbean.getFirs();
        }
    }

    public void deletePgck() {
        commembertab commembertab = AppContext.getUserInfo(PG_WZ_CKDetail.this);
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("Id", pg_wZckbean.getId());
        params.addQueryStringParameter("action", "deleteGoodsOutByPG");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        Intent intent = new Intent();
                        intent.setAction(AppContext.BROADCAST_PG_REFASH);
                        PG_WZ_CKDetail.this.sendBroadcast(intent);
                        Toast.makeText(PG_WZ_CKDetail.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    AppContext.makeToast(PG_WZ_CKDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(PG_WZ_CKDetail.this, "error_connectServer");
            }
        });
    }

    public void UpdatePgck(String firsNum, String secNum, String threeNum) {
        commembertab commembertab = AppContext.getUserInfo(PG_WZ_CKDetail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("Id", pg_wZckbean.getId());
        params.addQueryStringParameter("firsNum", firsNum);
        params.addQueryStringParameter("secNum", secNum);
        params.addQueryStringParameter("threeNum", threeNum);
        params.addQueryStringParameter("action", "UpGoodsOutByPG");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        Intent intent = new Intent();
                        intent.setAction(AppContext.BROADCAST_PG_REFASH);
                        PG_WZ_CKDetail.this.sendBroadcast(intent);
                        Toast.makeText(PG_WZ_CKDetail.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    AppContext.makeToast(PG_WZ_CKDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(PG_WZ_CKDetail.this, "error_connectServer");
            }
        });
    }
}
