package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_FilferData_Sale;
import com.farm.adapter.Adapter_SelectProduct;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FilferBean_Sale;
import com.farm.bean.OrderTypeNum;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomExpandableListView;
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
    List<FilferBean_Sale> list_filferbean;
    PopupWindow pw_tab;
    View pv_tab;
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
    View view_topline;
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
    @ViewById
    TextView tv_number_needapprove;
    @ViewById
    TextView tv_number_notpaydeposit;
    @ViewById
    TextView tv_number_waitingForHarvest;
    @ViewById
    TextView tv_number_waitingForSettlement;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void btn_filfer()
    {
        if (list_filferbean == null)
        {
            getSaleFilferData();
        } else
        {
            showPop_Filfer();
        }

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
        getSaleFilferData();
        orderTypeNum();
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
      /*  IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATELISTNUMBER);
        registerReceiver(receiver_update, intentfilter_update);*/
    }

    private void getSaleFilferData()
    {
        list_filferbean = FileHelper.getAssetsData(NCZ_OrderManager.this, "getSaleFilferData", FilferBean_Sale.class);
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String type = intent.getStringExtra("type");
            int number = intent.getIntExtra("number", 0);
            if (type.equals(AppContext.order_waitForApprove))
            {
                if (number > 0)
                {
                    tv_number_needapprove.setVisibility(View.VISIBLE);
                    tv_number_needapprove.setText("(" + number + ")");
                } else
                {
                    tv_number_needapprove.setVisibility(View.GONE);
                }

            } else if (type.equals(AppContext.order_waitForDeposit))
            {
                if (number > 0)
                {
                    tv_number_notpaydeposit.setVisibility(View.VISIBLE);
                    tv_number_notpaydeposit.setText("(" + number + ")");
                } else
                {
                    tv_number_notpaydeposit.setVisibility(View.GONE);
                }
            } else if (type.equals(AppContext.order_waitForHarvest))
            {
                if (number > 0)
                {
                    tv_number_waitingForHarvest.setVisibility(View.VISIBLE);
                    tv_number_waitingForHarvest.setText("(" + number + ")");
                } else
                {
                    tv_number_waitingForHarvest.setVisibility(View.GONE);
                }
            } else if (type.equals(AppContext.order_waitForSettlement))
            {
                if (number > 0)
                {
                    tv_number_waitingForSettlement.setVisibility(View.VISIBLE);
                    tv_number_waitingForSettlement.setText("(" + number + ")");
                } else
                {
                    tv_number_waitingForSettlement.setVisibility(View.GONE);
                }
            }
        }
    };

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

        tv_schedule.setTextColor(getResources().getColor(R.color.titlebar_top));
        tv_pending.setTextColor(getResources().getColor(R.color.titlebar_top));
        tv_notpaydeposit.setTextColor(getResources().getColor(R.color.titlebar_top));
        tv_waitingForHarvest.setTextColor(getResources().getColor(R.color.titlebar_top));
        tv_waitingForSettlement.setTextColor(getResources().getColor(R.color.titlebar_top));
        tv_allorder.setTextColor(getResources().getColor(R.color.titlebar_top));
        switch (pos)
        {
            case 0:
                tv_schedule.setSelected(false);
//                tv_schedule.setTextColor(getResources().getColor(R.color.red));
                tv_schedule.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_pending.setSelected(false);
//                tv_pending.setTextColor(getResources().getColor(R.color.red));
                tv_pending.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 2:
                tv_notpaydeposit.setSelected(false);
//                tv_notpaydeposit.setTextColor(getResources().getColor(R.color.red));
                tv_notpaydeposit.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 3:
                tv_waitingForHarvest.setSelected(false);
//                tv_waitingForHarvest.setTextColor(getResources().getColor(R.color.red));
                tv_waitingForHarvest.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 4:
                tv_waitingForSettlement.setSelected(false);
//                tv_waitingForSettlement.setTextColor(getResources().getColor(R.color.red));
                tv_waitingForSettlement.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 5:
                tv_allorder.setSelected(false);
//                tv_allorder.setTextColor(getResources().getColor(R.color.red));
                tv_allorder.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }


    private void orderTypeNum()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_OrderManager.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "orderTypeNum");//
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<OrderTypeNum> listData = new ArrayList<OrderTypeNum>();
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        OrderTypeNum orderTypeNum = new OrderTypeNum();
                        listData = JSON.parseArray(result.getRows().toJSONString(), OrderTypeNum.class);
                        if (listData.size() > 0)
                        {
                            orderTypeNum = listData.get(0);
                        }
                        //1
                        if (!orderTypeNum.getTypeNum1().equals("0"))
                        {
                            tv_number_needapprove.setVisibility(View.VISIBLE);
                            tv_number_needapprove.setText("(" + orderTypeNum.getTypeNum1() + ")");
                        } else
                        {
                            tv_number_needapprove.setVisibility(View.GONE);
                        }
//2
                        if (!orderTypeNum.getTypeNum2().equals("0"))
                        {
                            tv_number_notpaydeposit.setVisibility(View.VISIBLE);
                            tv_number_notpaydeposit.setText("(" + orderTypeNum.getTypeNum2() + ")");
                        } else
                        {
                            tv_number_notpaydeposit.setVisibility(View.GONE);
                        }

//3
                        if (!orderTypeNum.getTypeNum3().equals("0"))
                        {
                            tv_number_waitingForHarvest.setVisibility(View.VISIBLE);
                            tv_number_waitingForHarvest.setText("(" + orderTypeNum.getTypeNum3() + ")");
                        } else
                        {
                            tv_number_waitingForHarvest.setVisibility(View.GONE);
                        }
//4
                        if (!orderTypeNum.getTypeNum4().equals("0"))
                        {
                            tv_number_waitingForSettlement.setVisibility(View.VISIBLE);
                            tv_number_waitingForSettlement.setText("(" + orderTypeNum.getTypeNum4() + ")");
                        } else
                        {
                            tv_number_waitingForSettlement.setVisibility(View.GONE);
                        }
                    }


            }

        }

        @Override
        public void onFailure (HttpException error, String msg)
        {
            AppContext.makeToast(NCZ_OrderManager.this, "error_connectServer");

        }
    }

    );
}


    public void showPop_Filfer()
    {
        LayoutInflater layoutInflater = (LayoutInflater) NCZ_OrderManager.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.pop_salefilfer, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing()))
                {
                    pw_tab.dismiss();
                    return true;
                }
                return false;
            }
        });
        pv_tab.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_tab.isShowing())
                {
                    pw_tab.dismiss();
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //设置layout在PopupWindow中显示的位置
        pw_tab.setAnimationStyle(R.style.rightinrightout);
        pw_tab.showAsDropDown(view_topline, 0, 0);//置于顶部线下方
        pw_tab.showAtLocation(getLayoutInflater().inflate(R.layout.ncz_ordermanager, null), Gravity.RIGHT | Gravity.TOP, 0, 0);
        pw_tab.setOutsideTouchable(true);
        //关闭事件
        pw_tab.setOnDismissListener(new popupDismissListener());
        //设置背景半透明
        backgroundAlpha(0.5f);
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        pw_tab.setBackgroundDrawable(dw);

        Button btn_sure = (Button) pv_tab.findViewById(R.id.btn_sure);
        CustomExpandableListView expandableListView = (CustomExpandableListView) pv_tab.findViewById(R.id.expandableListView);
        Adapter_FilferData_Sale adapter_filfer_sale = new Adapter_FilferData_Sale(NCZ_OrderManager.this, list_filferbean, expandableListView);
        expandableListView.setAdapter(adapter_filfer_sale);
        utils.setListViewHeight(expandableListView);
        for (int i = 0; i < list_filferbean.size(); i++)
        {
            expandableListView.expandGroup(i);//展开
        }
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pw_tab.dismiss();
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

/**
 * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
 */
class popupDismissListener implements PopupWindow.OnDismissListener
{

    @Override
    public void onDismiss()
    {
        backgroundAlpha(1f);
    }

}
}
