package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ${hmj} on 2016/5/16.
 */
@EActivity(R.layout.ncz_ordermanager)
public class NCZ_OrderManager extends Activity
{
    //    NCZ_AllOrderFragment ncz_allOrderFragment;//删除
    //    NCZ_DealingOrderFragment ncz_dealingOrderFragment;//完成
//    NCZ_ScheduleOrderFragment ncz_scheduleOrderFragment;//排单
//    NCZ_NotPayFragment ncz_notPayFragment;  //交易中
    NCZ_OrderPlanFragment ncz_orderPlanFragment;//订单排班
    NCZ_NeedApproveOrderFragment ncz_needApproveOrderFragment;  //审批
    NCZ_NotPayDepositFragment ncz_notPayDepositFragment;//待付定金   //搜索是开了
    NCZ_WaitForHarvestFragment ncz_waitForHarvestFragment;//待采收
    NCZ_WaitForSettlementFragment ncz_waitForSettlementFragment;//待结算
    NCZ_AllOrderFragment_New ncz_allOrderFragment_new;//全部订单
    Fragment mContent = new Fragment();
    @ViewById
    Button btn_back;
    @ViewById
    TextView tv_allorder;//全部订单
    @ViewById
    TextView tv_pending;//待审核
    @ViewById
    TextView tv_schedule;//订单排班
    @ViewById
    TextView tv_notpaydeposit;//待付定金
    @ViewById
    TextView tv_waitingForHarvest; //待采收
    @ViewById
    TextView tv_waitingForSettlement;//待结算

    @ViewById
    FrameLayout fl_schedule;
    @ViewById
    TextView tv_schedule_tip;
    @ViewById
    FrameLayout fl_pending;
    @ViewById
    TextView tv_pending_tip;
    @ViewById
    FrameLayout fl_notpaydeposit;
    @ViewById
    TextView tv_notpaydeposit_tip;
    @ViewById
    FrameLayout fl_waitingForHarvest;
    @ViewById
    TextView tv_waitingForHarvest_tip;
    @ViewById
    FrameLayout fl_waitingForSettlement;
    @ViewById
    TextView tv_waitingForSettlement_tip;
    @ViewById
    FrameLayout fl_allorder;
    @ViewById
    TextView tv_allorder_tip;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void tv_schedule()
    {
        setBackground(0);
        switchContent(mContent, ncz_orderPlanFragment);
    }

    @Click
    void tv_pending()
    {
        setBackground(1);
        switchContent(mContent, ncz_needApproveOrderFragment);
    }

    @Click
    void tv_notpaydeposit()
    {
        setBackground(2);
        switchContent(mContent, ncz_notPayDepositFragment);
    }

    @Click
    void tv_waitingForHarvest()
    {
        setBackground(3);
        switchContent(mContent, ncz_waitForHarvestFragment);
    }

    @Click
    void tv_waitingForSettlement()
    {
        setBackground(4);
        switchContent(mContent, ncz_waitForSettlementFragment);
    }

    @Click
    void tv_allorder()
    {
        setBackground(5);
        switchContent(mContent, ncz_allOrderFragment_new);

    }


    @AfterViews
    void afterOncreate()
    {
//        getNeedOrders();
//        getAllOrders();
        setBackground(0);
        switchContent(mContent, ncz_orderPlanFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        ncz_orderPlanFragment = new NCZ_OrderPlanFragment_();
        ncz_notPayDepositFragment = new NCZ_NotPayDepositFragment_();
        ncz_needApproveOrderFragment = new NCZ_NeedApproveOrderFragment_();
        ncz_waitForHarvestFragment = new NCZ_WaitForHarvestFragment_();
        ncz_waitForSettlementFragment = new NCZ_WaitForSettlementFragment_();
        ncz_allOrderFragment_new = new NCZ_AllOrderFragment_New_();
    }

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.fl_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private void setBackground(int pos)
    {
        tv_schedule.setSelected(false);
        tv_pending.setSelected(false);
        tv_notpaydeposit.setSelected(false);
        tv_waitingForHarvest.setSelected(false);
        tv_waitingForSettlement.setSelected(false);
        tv_allorder.setSelected(false);


        tv_schedule.setBackgroundResource(R.color.white);
        tv_pending.setBackgroundResource(R.color.white);
        tv_notpaydeposit.setBackgroundResource(R.color.white);
        tv_waitingForHarvest.setBackgroundResource(R.color.white);
        tv_waitingForSettlement.setBackgroundResource(R.color.white);
        tv_allorder.setBackgroundResource(R.color.white);

        tv_schedule.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_pending.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_notpaydeposit.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_waitingForHarvest.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_waitingForSettlement.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_allorder.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_schedule.setSelected(false);
                tv_schedule.setTextColor(getResources().getColor(R.color.red));
                tv_schedule.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_pending.setSelected(false);
                tv_pending.setTextColor(getResources().getColor(R.color.red));
                tv_pending.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 2:
                tv_notpaydeposit.setSelected(false);
                tv_notpaydeposit.setTextColor(getResources().getColor(R.color.red));
                tv_notpaydeposit.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 3:
                tv_waitingForHarvest.setSelected(false);
                tv_waitingForHarvest.setTextColor(getResources().getColor(R.color.red));
                tv_waitingForHarvest.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 4:
                tv_waitingForSettlement.setSelected(false);
                tv_waitingForSettlement.setTextColor(getResources().getColor(R.color.red));
                tv_waitingForSettlement.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 5:
                tv_allorder.setSelected(false);
                tv_allorder.setTextColor(getResources().getColor(R.color.red));
                tv_allorder.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }


    private void getAllOrders()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_OrderManager.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("type", "0");
        params.addQueryStringParameter("action", "GetSpecifyOrderByNCZ");//
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
                        Iterator<SellOrder_New> it = listData.iterator();
                        while (it.hasNext())
                        {
                            String value = it.next().getSelltype();
                            if (value.equals("已完成") || value.equals("待审批"))
                            {
                                it.remove();
                            }
                        }

                        int b = 0;
                        if (listData.size() > 0)
                        {
                            for (int j = 0; j < listData.size(); j++)
                            {
                                if (listData.get(j).getFlashStr().equals("1"))
                                {
                                    b++;
                                }
                            }
                        }

                        if (b > 0)
                        {
                            fl_schedule.setVisibility(View.VISIBLE);
                            tv_schedule_tip.setText(b + "");
//                            fl_.setVisibility(View.VISIBLE);
//                            tv_jyz.setText(b + "");
                        }

                    } else
                    {
                        listData = new ArrayList<SellOrder_New>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_OrderManager.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_OrderManager.this, "error_connectServer");

            }
        });
    }

    private void getNeedOrders()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_OrderManager.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("type", "0");
        params.addQueryStringParameter("isApprove", "1");//不为空
        params.addQueryStringParameter("action", "GetSpecifyOrderByNCZ");//
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

                        int b = 0;
                        if (listData.size() > 0)
                        {
                            for (int j = 0; j < listData.size(); j++)
                            {
                                if (listData.get(j).getFlashStr().equals("1"))
                                {
                                    b++;
                                }
                            }
                        }
                        if (b > 0)
                        {
                            fl_waitingForSettlement.setVisibility(View.VISIBLE);
                            tv_waitingForSettlement_tip.setText(b + "");
                        }

                    } else
                    {
                        listData = new ArrayList<SellOrder_New>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_OrderManager.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_OrderManager.this, "error_connectServer");

            }
        });
    }

}
