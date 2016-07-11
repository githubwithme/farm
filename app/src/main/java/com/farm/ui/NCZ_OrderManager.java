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
    NCZ_AllOrderFragment ncz_allOrderFragment;//删除
    NCZ_NotPayFragment ncz_notPayFragment;  //交易中
    NCZ_DealingOrderFragment ncz_dealingOrderFragment;//完成
    NCZ_ScheduleOrderFragment ncz_scheduleOrderFragment;//排单
    NCZ_NeedApproveOrderFragment ncz_needApproveOrderFragment;  //审批
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
        switchContent(mContent, ncz_scheduleOrderFragment);
    }

    @Click
    void tv_allorder()
    {
        setBackground(4);
        switchContent(mContent, ncz_allOrderFragment);

    }

    @Click
    void tv_dealing()
    {
        setBackground(3);
        switchContent(mContent, ncz_dealingOrderFragment);
    }

    @Click
    void tv_notpay()
    {
        setBackground(2);
        switchContent(mContent, ncz_notPayFragment);
    }

    @Click
    void tv_pending()
    {
        setBackground(1);
        switchContent(mContent, ncz_needApproveOrderFragment);
    }

    @AfterViews
    void afterOncreate()
    {
        getNeedOrders();
        getAllOrders();
        setBackground(0);
        switchContent(mContent, ncz_scheduleOrderFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        ncz_scheduleOrderFragment = new NCZ_ScheduleOrderFragment_();
        ncz_allOrderFragment = new NCZ_AllOrderFragment_();
        ncz_notPayFragment = new NCZ_NotPayFragment_();
        ncz_dealingOrderFragment = new NCZ_DealingOrderFragment_();
        ncz_needApproveOrderFragment = new NCZ_NeedApproveOrderFragment_();
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
                tv_schedule.setTextColor(getResources().getColor(R.color.red));
                tv_schedule.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_pending.setSelected(false);
                tv_pending.setTextColor(getResources().getColor(R.color.red));
                tv_pending.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 2:
                tv_notpay.setSelected(false);
                tv_notpay.setTextColor(getResources().getColor(R.color.red));
                tv_notpay.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 3:
                tv_dealing.setSelected(false);
                tv_dealing.setTextColor(getResources().getColor(R.color.red));
                tv_dealing.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 4:
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

                        if (b>0)
                        {
                            fl_dynamic.setVisibility(View.VISIBLE);
                            tv_dynamic_new.setText(b + "");
                            fl_jyz.setVisibility(View.VISIBLE);
                            tv_jyz.setText(b + "");
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
                            fl_dsp.setVisibility(View.VISIBLE);
                            tv_dsp.setText(b + "");
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
