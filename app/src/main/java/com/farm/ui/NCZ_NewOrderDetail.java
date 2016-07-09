package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_New_SellDetail;
import com.farm.adapter.Adapter_SellOrderDetail;
import com.farm.adapter.PG_JSD_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/7/6.
 */

@EActivity(R.layout.ncz_new_orderdetail)
public class NCZ_NewOrderDetail extends Activity
{
    List<SellOrderDetail_New> list_orderdetail;
    String batchtime;
    SellOrder_New sellOrder;
    Adapter_New_SellDetail adapter_sellOrderDetail;
    @ViewById
    ListView lv;
    @ViewById
    TextView   tv_cbh;

    //表头
    @ViewById
    TextView buyers;//采购商
    @ViewById
    TextView mainPepole;//负责人
     @ViewById
    TextView price;//单价
    @ViewById
    TextView saletime;//采收时间
    @ViewById
    TextView tv_planweight;//重量
    @ViewById
    TextView tv_plansumvalues;//总价
    @ViewById
    TextView tv_deposit;//定金
    @ViewById
    TextView tv_finalpayment;//发往城市
    @ViewById
    TextView goodsName;//产品名字
    @ViewById
    TextView packPec;//包装规格
    @ViewById
    TextView contractorId;//包装负责人
    @ViewById
    TextView packPrice;//包装单价
   @ViewById
    TextView pickId;//搬运负责人
    @ViewById
    TextView carryPrice;//搬运价格
    @ViewById
    TextView tv_note;//说明


    //结算表
    @ViewById
    TextView total;//总件数
    @ViewById
    TextView TotalWeight;//总净重
    @ViewById
    TextView qualityBalance;//正品结算金额
    @ViewById
    TextView defectBalance;//次品结算金额
    @ViewById
    TextView packFee;//总包装费
    @ViewById
    TextView carryFee;//总搬运费
    @ViewById
    TextView totalFee;//合计金额
    @ViewById
    TextView actualMoney;//实际金额
    @ViewById
    TextView qualityWaterWeight;//正品带水重
    @ViewById
    TextView defectWaterWeight;//次品带水重
    @ViewById
    TextView qualityNetWeight;//正品净重
    @ViewById
    TextView defectNetWeight;//次品净重
    @ViewById
    TextView qualityTotalWeight;//正品重净重
    @ViewById
    TextView defectTotalWeight;//次品总净重
    @ViewById
    TextView actualweight;//正品件数
    @ViewById
    TextView defectNum ;//次品件数
    @ViewById
    TextView actualprice;//正品单价
    @ViewById
    TextView defectPrice;//次品单价


    @ViewById
    TextView tv_jsd;
    @ViewById
    LinearLayout ll_jsd;
    @AfterViews
    void afterview()
    {

        if (sellOrder.getSelltype().equals("审批结算")||sellOrder.getSelltype().equals("已完成"))
        {
            tv_jsd.setVisibility(View.VISIBLE);
            ll_jsd.setVisibility(View.VISIBLE);
        }
        showData();
        getsellOrderDetailBySaleId();
    }

    private void showData()
    {
        if (!sellOrder.getSaletime().equals(""))
        {
            saletime.setText(sellOrder.getSaletime().substring(0,sellOrder.getSaletime().length()-8));
        }

        price.setText(sellOrder.getPrice());
        buyers.setText(sellOrder.getPurchaName());
        mainPepole.setText(sellOrder.getMainPepName());
        tv_planweight.setText(sellOrder.getWeight());
        tv_plansumvalues.setText(sellOrder.getSumvalues());
        tv_deposit.setText(sellOrder.getWaitDeposit());
        tv_finalpayment.setText(sellOrder.getAddress());
        goodsName.setText(sellOrder.getGoodsname());
        packPec.setText(sellOrder.getPackPec());
        contractorId.setText(sellOrder.getContractorName());
        packPrice.setText(sellOrder.getPackPrice());
        pickId.setText(sellOrder.getPickName());
        carryPrice.setText(sellOrder.getCarryPrice());
        tv_note.setText(sellOrder.getNote());

        //
        total.setText(sellOrder.getTotal());
        TotalWeight.setText(sellOrder.getTotalWeight());
        qualityBalance.setText(sellOrder.getQualityBalance());
        defectBalance.setText(sellOrder.getDefectBalance());
        packFee.setText(sellOrder.getPackFee());
        carryFee.setText(sellOrder.getCarryFee());
        totalFee.setText(sellOrder.getTotalFee());
        actualMoney.setText(sellOrder.getActualMoney());
        qualityWaterWeight.setText(sellOrder.getQualityWaterWeight());
        defectWaterWeight.setText(sellOrder.getDefectWaterWeight());
        qualityNetWeight.setText(sellOrder.getQualityNetWeight());
        defectNetWeight.setText(sellOrder.getDefectNetWeight());
        qualityTotalWeight.setText(sellOrder.getQualityTotalWeight());
        defectTotalWeight.setText(sellOrder.getDefectTotalWeight());
        actualweight.setText(sellOrder.getActualnumber());
        defectNum.setText(sellOrder.getDefectNum());
        actualprice.setText(sellOrder.getActualprice());
        defectPrice.setText(sellOrder.getDefectPrice());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder = getIntent().getParcelableExtra("bean");
//        list_orderdetail = sellOrder.getSellOrderDetailList();
    }

    private void getsellOrderDetailBySaleId()
    {



        commembertab commembertab = AppContext.getUserInfo(NCZ_NewOrderDetail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleId", sellOrder.getUuid());
        params.addQueryStringParameter("action", "getsellOrderDetailBySaleId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrderDetail_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {


                        list_orderdetail = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
//                        adapter_sellOrderDetail = new Adapter_New_SellDetail(NCZ_NewOrderDetail.this, list_orderdetail);
                        adapter_sellOrderDetail = new Adapter_New_SellDetail(NCZ_NewOrderDetail.this, list_orderdetail);
                        lv.setAdapter(adapter_sellOrderDetail);
                        utils.setListViewHeight(lv);
                        if (list_orderdetail.size()>0)
                        {
                            tv_cbh.setText(list_orderdetail.get(0).getparkname()+"承包户产量");
                        }

                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_NewOrderDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_NewOrderDetail.this, "error_connectServer");
            }
        });
    }
}
