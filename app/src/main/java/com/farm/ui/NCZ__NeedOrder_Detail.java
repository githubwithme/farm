package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_Look_JSD_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
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
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by hasee on 2016/7/19.
 */

@EActivity(R.layout.ncz_look_jsd)
public class NCZ__NeedOrder_Detail extends Activity
{

    @ViewById
    Button button_yes;
    @ViewById
    Button button_no;
    NCZ_Look_JSD_Adapter ncz_look_jsd_adapter;
    @ViewById
    TextView zp_jingzhong;

    @ViewById
    LinearLayout other_baozhuang;
    @ViewById
    LinearLayout nc_baozhuang;

    @ViewById
    LinearLayout ll_nobanyun;
    @ViewById
    LinearLayout ll_banyun;

    @ViewById
    TextView allnum;
    @ViewById
    TextView all_zhengpin;
    @ViewById
    TextView allcipin;
    @ViewById
    TextView all_jinzhong;

    @ViewById
    TextView bz_khnote; //包装客户自带说明
    @ViewById
    TextView bz_nc_danjia;//包装农场  包装单价
    @ViewById
    TextView bz_fzrid;//包装负责人Id

    @ViewById
    TextView by_khnote; //搬运客户自带说明
    @ViewById
    TextView by_nc_danjia;//搬运农场  搬运单价
    @ViewById
    TextView by_fzrid;//搬运负责人Id

    @ViewById
    TextView jsd_zongjianshu;//总件数
    @ViewById
    TextView jsd_zongjingzhong;//总净重
    @ViewById
    TextView jsd_zpjz;//正品总净重
    @ViewById
    TextView jsd_cpzjs;//次品总净重
    @ViewById
    TextView jsd_zpprice;//正品单价
    @ViewById
    TextView jsd_cpprice;//次品单价
    @ViewById
    TextView zp_jianshu;//正品件数
    @ViewById
    TextView cp_jianshu;//次品件数

    @ViewById
    TextView zp_ds_zhong;//正品带水重
    @ViewById
    TextView cp_ds_zhong;//次品带水重
    @ViewById
    TextView zp_bds_zhong;  //正品净重
    @ViewById
    TextView cp_jingzhong;   //次品净重
    @ViewById
    TextView zp_jsje;       //正品结算金额
    @ViewById
    TextView cp_jsje;      //次品 结算金额

    @ViewById
    TextView carryFee; //总搬运费
    @ViewById
    TextView packFee;// 总包装费
    @ViewById
    TextView totalFee;// 合计金额
    @ViewById
    TextView actualMoney;//实际金额

    @ViewById
    TextView packPec;
    @ViewById
    LinearLayout goods_xx;   //一文字
    @ViewById
    LinearLayout isgoods_xx;//产品信息

    @ViewById
    LinearLayout ll_cbhlist;//承包户

    @ViewById
    LinearLayout ll_jsd;//结算单统计
    @ViewById
    TextView tv_isgood;
    @ViewById
    TextView plateNumber;

    @ViewById
    ListView frame_listview_news;

    SellOrder_New sellOrder_new;

    @Click
    void button_yes()
    {
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder=sellOrder_new;
        sellOrder.setIsNeedAudit("1");
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sellOrderSettlementlist\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("]} ");
        updatesellOrderSettlement(builder.toString());
    }
    @Click
    void button_no()
    {
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder=sellOrder_new;
        sellOrder.setIsNeedAudit("-1");
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sellOrderSettlementlist\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("]} ");
        updatesellOrderSettlement(builder.toString());

    }
    @AfterViews
    void afterview()
    {
        button_yes.setVisibility(View.VISIBLE);
        button_no.setVisibility(View.VISIBLE);
        showData();
    }

    private void showData()
    {
        plateNumber.setText(sellOrder_new.getPlateNumber());
        bz_nc_danjia.setText(sellOrder_new.getPackPrice());
        by_nc_danjia.setText(sellOrder_new.getCarryPrice());
        by_fzrid.setText(sellOrder_new.getContractorName());
        bz_fzrid.setText(sellOrder_new.getPickName());
        jsd_zpprice.setText(sellOrder_new.getActualprice());
        actualMoney.setText(sellOrder_new.getActualMoney());
        packPec.setText(sellOrder_new.getPackPec());
        packFee.setText(sellOrder_new.getPackFee());
        carryFee.setText(sellOrder_new.getCarryFee());
        cp_jsje.setText(sellOrder_new.getDefectBalance());
        zp_jsje.setText(sellOrder_new.getQualityBalance());
        cp_jingzhong.setText(sellOrder_new.getDefectNetWeight());
        zp_bds_zhong.setText(sellOrder_new.getQualityNetWeight());
        cp_ds_zhong.setText(sellOrder_new.getDefectWaterWeight());
        zp_ds_zhong.setText(sellOrder_new.getQualityWaterWeight());
        cp_jianshu.setText(sellOrder_new.getDefectNum());
        zp_jianshu.setText(sellOrder_new.getActualnumber());
        jsd_cpprice.setText(sellOrder_new.getDefectPrice());
        jsd_cpzjs.setText(sellOrder_new.getDefectTotalWeight());
        jsd_zpjz.setText(sellOrder_new.getQualityTotalWeight());
        jsd_zongjingzhong.setText(sellOrder_new.getTotalWeight());
        jsd_zongjianshu.setText(sellOrder_new.getTotal());
        totalFee.setText(sellOrder_new.getTotalFee());


        if (sellOrder_new.getDetailSecLists().size() > 0)
        {
            ncz_look_jsd_adapter = new NCZ_Look_JSD_Adapter(NCZ__NeedOrder_Detail.this, sellOrder_new.getDetailSecLists(), sellOrder_new.getQualityNetWeight(), sellOrder_new.getDefectNetWeight());
            frame_listview_news.setAdapter(ncz_look_jsd_adapter);
            utils.setListViewHeight(frame_listview_news);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("bean");
    }
    private void updatesellOrderSettlement(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "updatesellOrderSettlement");
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        Intent intent = new Intent();
                        intent.setAction(AppContext.UPDATEMESSAGE_PGDETAIL_UPDATE_DELETE);
                        sendBroadcast(intent);
                        Toast.makeText(NCZ__NeedOrder_Detail.this, "保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ__NeedOrder_Detail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ__NeedOrder_Detail.this, "error_connectServer");
            }
        });
    }
}
