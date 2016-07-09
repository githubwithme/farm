package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.PG_NeedAdapter;
import com.farm.adapter.PG_scheduleOrderAdapter;
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
 * Created by hasee on 2016/7/1.
 */
@EActivity(R.layout.pg_ordermanager)
public class PG_OrderManager extends Activity
{
    PG_DealingOrder pg_dealingOrder;
    PG_NeedApproveOrderFragment pg_needApproveOrderFragment;
    PG_ScheduleOrderFragment pg_scheduleOrderFragment;//排单
    PG_NotPayFragment pg_notPayFragment;//交易中
    PG_AllOrderFragment pg_allOrderFragment;
    Fragment mContent = new Fragment();
    @ViewById
    Button btn_back;
    @ViewById
    TextView tv_allorder;
    @ViewById
    TextView tv_pending;
    @ViewById
    TextView tv_schedule;
    @ViewById
    TextView tv_dealing;
    @ViewById
    TextView tv_notpay;



    @ViewById
    FrameLayout fl_dynamic;
    @ViewById
    TextView tv_dynamic_new;
    @ViewById
    FrameLayout fl_dsp;
    @ViewById
    TextView tv_dsp;
    @ViewById
    FrameLayout fl_jyz;
    @ViewById
    TextView tv_jyz;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void tv_schedule()
    {
        setBackground(0);
        switchContent(mContent, pg_scheduleOrderFragment);
    }

    @Click
    void tv_allorder()
    {
        setBackground(4);
        switchContent(mContent, pg_allOrderFragment);

    }

    @Click
    void tv_dealing()
    {
        setBackground(3);
        switchContent(mContent, pg_dealingOrder);
    }

    @Click
    void tv_notpay()
    {
        setBackground(2);
        switchContent(mContent, pg_notPayFragment);
    }
    @Click
    void tv_pending()
    {
        setBackground(1);
        switchContent(mContent, pg_needApproveOrderFragment);
    }

    @AfterViews
    void afterOncreate()
    {
        getAllOrders();
        getNeedOrders();
        setBackground(0);
        switchContent(mContent, pg_scheduleOrderFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        pg_scheduleOrderFragment=new PG_ScheduleOrderFragment_();
        pg_notPayFragment=new PG_NotPayFragment_();
        pg_needApproveOrderFragment=new PG_NeedApproveOrderFragment_();
        pg_dealingOrder=new PG_DealingOrder_();
        pg_allOrderFragment=new PG_AllOrderFragment_();
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
        tv_allorder.setSelected(false);
        tv_notpay.setSelected(false);
        tv_dealing.setSelected(false);
        tv_schedule.setSelected(false);
        tv_pending.setSelected(false);

        tv_allorder.setBackgroundResource(R.color.white);
        tv_notpay.setBackgroundResource(R.color.white);
        tv_dealing.setBackgroundResource(R.color.white);
        tv_schedule.setBackgroundResource(R.color.white);
        tv_pending.setBackgroundResource(R.color.white);

        tv_allorder.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_notpay.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_dealing.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_schedule.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_pending.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_schedule.setSelected(false);
                tv_schedule.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_schedule.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_pending.setSelected(false);
                tv_pending.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_pending.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 2:
                tv_notpay.setSelected(false);
                tv_notpay.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_notpay.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 3:
                tv_dealing.setSelected(false);
                tv_dealing.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_dealing.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 4:
                tv_allorder.setSelected(false);
                tv_allorder.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_allorder.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }
    private void getAllOrders()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_OrderManager.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("userId", commembertab.getId());
        params.addQueryStringParameter("isPass", "-1");
        params.addQueryStringParameter("action", "getSellOrderByUserId");//
//        params.addQueryStringParameter("action", "GetSpecifyOrderByNCZ");//
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

                        commembertab commembertab = AppContext.getUserInfo(PG_OrderManager.this);
                        listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);
                        Iterator<SellOrder_New> it = listData.iterator();
                        while (it.hasNext())
                        {
                            String value = it.next().getSelltype();
                            if (value.equals("已完成"))
                            {
                                it.remove();
                            }
                        }
                        Iterator<SellOrder_New> its = listData.iterator();
                        while (its.hasNext())
                        {
                            String value = its.next().getMainPepole();
                            if (!value.equals(commembertab.getId()))
                            {
                                its.remove();
                            }
                        }
                        if (listData.size() > 0)
                        {
                            fl_dynamic.setVisibility(View.VISIBLE);
                            tv_dynamic_new.setText(listData.size() + "");
                            fl_jyz.setVisibility(View.VISIBLE);
                            tv_jyz.setText(listData.size() + "");
                        }

                    } else
                    {
                        listData = new ArrayList<SellOrder_New>();
                    }

                } else
                {
                    AppContext.makeToast(PG_OrderManager.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_OrderManager.this, "error_connectServer");

            }
        });
    }
    private void getNeedOrders()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_OrderManager.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("type", "0");
        params.addQueryStringParameter("isApprove", "1");//不为空
        params.addQueryStringParameter("userid", commembertab.getId());//不为空
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
                        if (listData.size()>0)
                        {
                            fl_dsp.setVisibility(View.VISIBLE);
                            tv_dsp.setText(listData.size() + "");
                        }
                    } else
                    {
                        listData = new ArrayList<SellOrder_New>();
                    }

                } else
                {
                    AppContext.makeToast(PG_OrderManager.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_OrderManager.this, "error_connectServer");

            }
        });
    }
}
