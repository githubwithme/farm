package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2016/7/1.
 */
@EActivity(R.layout.pg_ordermanager)
public class PG_OrderManager extends Activity
{
    PG_NeedApproveOrderFragment pg_needApproveOrderFragment;
    PG_ScheduleOrderFragment pg_scheduleOrderFragment;//排单
    PG_NotPayFragment pg_notPayFragment;//交易中
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
//        switchContent(mContent, ncz_allOrderFragment);

    }

    @Click
    void tv_dealing()
    {
        setBackground(3);
//        switchContent(mContent, ncz_dealingOrderFragment);
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
}
