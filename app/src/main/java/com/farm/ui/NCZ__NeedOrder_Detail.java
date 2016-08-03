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
import java.util.ArrayList;
import java.util.List;

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
    // @ViewById
    LinearLayout is_jsd;//

    @ViewById
    LinearLayout ll_jsd;//结算单统计
    @ViewById
    TextView tv_isgood;
    @ViewById
    TextView plateNumber;

    @ViewById
    ListView frame_listview_news;

    SellOrder_New sellOrder;
    SellOrder_New sellOrder_new;


    @ViewById
    TextView Order_price;//订单单价
    @ViewById
    TextView Order_weight;//订单重量
    @ViewById
    TextView Order_deposit; //定金
    @ViewById
    TextView Order_waitdeposit;//待付定金
    @ViewById
    TextView Order_Value;//总价
    @ViewById
    TextView Order_Saletime;//采收时间
    @ViewById
    TextView tv_order;

    @ViewById
    LinearLayout ll_order;

    @Click
    void tv_order()
    {

        if (!ll_order.isShown())
        {
            ll_order.setVisibility(View.VISIBLE);
        } else
        {
            ll_order.setVisibility(View.GONE);
        }
    }


    @Click
    void is_jsd()
    {
        if (!ll_jsd.isShown())
        {
            ll_jsd.setVisibility(View.VISIBLE);
        } else
        {
            ll_jsd.setVisibility(View.GONE);
        }
    }

    @Click
    void isgoods_xx()
    {
        if (!goods_xx.isShown())
        {
            goods_xx.setVisibility(View.VISIBLE);
        } else
        {
            goods_xx.setVisibility(View.GONE);
        }
    }

    @Click
    void button_yes()
    {
/*        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder=sellOrder_new;
        sellOrder.setIsNeedAudit("1");
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sellOrderSettlementlist\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("]} ");
        updatesellOrderSettlement(builder.toString());*/


//        isNeedAudit    varchar(50)   -1未通过  0要审核  1通过审核   2 默认（折扣金额审批结果）
//        settlestatus  int 审批结算单  0是待审批，1是审批通过，-1审批部通过， 2会计收到,3默认  （费用审批结算单）
        String isNeedAudit = "";
        String settlestatus = "";

        if (sellOrder.getIsNeedAudit().equals("0"))
        {
            isNeedAudit = "1";
        }
        if (sellOrder.getSettlestatus().equals("0"))
        {
            settlestatus = "1";
        }
        updatesellOrderSettlement(isNeedAudit, settlestatus);
    }

    @Click
    void button_no()
    {


        String isNeedAudit = "";
        String settlestatus = "";

        if (sellOrder.getIsNeedAudit().equals("0"))
        {
            isNeedAudit = "-1";
        }
        if (sellOrder.getSettlestatus().equals("0"))
        {
            settlestatus = "-1";
        }
        updatesellOrderSettlement(isNeedAudit, settlestatus);

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

   /*     TextView Order_price;//订单单价
        TextView Order_weight;//订单重量
        TextView Order_deposit; //定金
        TextView Order_waitdeposit;//待付定金
        TextView Order_Value;//总价
        TextView Order_Saletime;//采收时间*/
        //订单
        Order_price.setText(sellOrder_new.getPrice());
        Order_weight.setText(sellOrder_new.getWeight());
        Order_deposit.setText(sellOrder_new.getDeposit());
        Order_waitdeposit.setText(sellOrder_new.getWaitDeposit());
        Order_Value.setText(sellOrder_new.getSumvalues());
        Order_Saletime.setText(sellOrder_new.getSaletime());

        //结算单
        plateNumber.setText(sellOrder.getPlateNumber());
        bz_nc_danjia.setText(sellOrder.getPackPrice());
        by_nc_danjia.setText(sellOrder.getCarryPrice());
        by_fzrid.setText(sellOrder.getContractorName());
        bz_fzrid.setText(sellOrder.getPickName());
        jsd_zpprice.setText(sellOrder.getActualprice());
        actualMoney.setText(sellOrder.getActualMoney());
        packPec.setText(sellOrder.getPackPec());
        packFee.setText(sellOrder.getPackFee());
        carryFee.setText(sellOrder.getCarryFee());
        cp_jsje.setText(sellOrder.getDefectBalance());
        zp_jsje.setText(sellOrder.getQualityBalance());
        cp_jingzhong.setText(sellOrder.getDefectNetWeight());
        zp_bds_zhong.setText(sellOrder.getQualityNetWeight());
        cp_ds_zhong.setText(sellOrder.getDefectWaterWeight());
        zp_ds_zhong.setText(sellOrder.getQualityWaterWeight());
        cp_jianshu.setText(sellOrder.getDefectNum());
        zp_jianshu.setText(sellOrder.getActualnumber());
        jsd_cpprice.setText(sellOrder.getDefectPrice());
        jsd_cpzjs.setText(sellOrder.getDefectTotalWeight());
        jsd_zpjz.setText(sellOrder.getQualityTotalWeight());
        jsd_zongjingzhong.setText(sellOrder.getTotalWeight());
        jsd_zongjianshu.setText(sellOrder.getTotal());
        totalFee.setText(sellOrder.getTotalFee());


        if (sellOrder.getDetailSecLists().size() > 0)
        {
            ncz_look_jsd_adapter = new NCZ_Look_JSD_Adapter(NCZ__NeedOrder_Detail.this, sellOrder.getDetailSecLists(), sellOrder.getQualityNetWeight(), sellOrder.getDefectNetWeight());
            frame_listview_news.setAdapter(ncz_look_jsd_adapter);
            utils.setListViewHeight(frame_listview_news);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder = getIntent().getParcelableExtra("bean");
        sellOrder_new = getIntent().getParcelableExtra("sellOrder_new");
    }


    //修改
    private void updatesellOrderSettlement(String isNeedAudit, String settlestatus)
    {


        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", sellOrder.getid());
        params.addQueryStringParameter("settlestatus", settlestatus);
        params.addQueryStringParameter("isNeedAudit", isNeedAudit);
        params.addQueryStringParameter("action", "changesellOrderSettlement");

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);
                        finish();
                    } else
                    {
                        listData = new ArrayList<SellOrder_New>();
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
