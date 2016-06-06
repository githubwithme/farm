package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.WZ_RKxx;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by user on 2016/4/8.
 */
@EActivity(R.layout.ncz_wz_ckdetail)
public class NCZ_WZ_CKDetail extends FragmentActivity {
    private WZ_RKxx wz_rKxx;
    private String inType;
    private String indate;
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
    TextView quantity;
    @ViewById
    TextView outGoodsvalue;
    @ViewById
    TextView note;
    @ViewById
    TextView zhongliang;
    @ViewById
    ImageButton imgbtn_back;

    @Click
    void imgbtn_back() {
      finish();
    }

    @AfterViews
    void afteroncreate() {
        goodsname.setText(wz_rKxx.getGoodsname());
        parkName.setText(wz_rKxx.getParkName());
        storehouseName.setText(wz_rKxx.getStorehouseName());
        outType.setText(inType);

        String indates = indate.substring(0, indate.length() - 8);
        inDate.setText(indate);
        batchName.setText(wz_rKxx.getBatchName());
        quantity.setText(wz_rKxx.getQuantity());
        outGoodsvalue.setText(wz_rKxx.getOutGoodsvalue() + "元");
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
        inType = getIntent().getStringExtra("inType");
        indate = getIntent().getStringExtra("indate");
    }
/*    private void getGoodsxx() {

        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsId", wz_rKxx.getGoodsid());
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
                        zhongliang.setText(utils.getNum(wz_rKxx.getQuantity())*Double.valueOf(listNewData.get(0).getGoodsStatistical())+listNewData.get(0).getGoodsunit());
                    } else {
                        listNewData = new ArrayList<WZ_Detail>();
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_WZ_CKDetail.this, "error_connectServer");

            }
        });
    }*/
}
