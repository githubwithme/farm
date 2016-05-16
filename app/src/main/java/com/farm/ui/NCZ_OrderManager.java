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
 * Created by ${hmj} on 2016/5/16.
 */
@EActivity(R.layout.ncz_ordermanager)
public class NCZ_OrderManager extends Activity
{
    NCZ_AllOrderFragment ncz_allOrderFragment;
    NCZ_NotPayFragment ncz_notPayFragment;
    NCZ_DealingOrderFragment ncz_dealingOrderFragment;
    Fragment mContent = new Fragment();
    @ViewById
    Button btn_back;
    @ViewById
    TextView tv_allorder;
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
    void tv_allorder()
    {
        setBackground(0);
        switchContent(mContent, ncz_allOrderFragment);
    }

    @Click
    void tv_dealing()
    {
        setBackground(2);
        switchContent(mContent, ncz_dealingOrderFragment);
    }

    @Click
    void tv_notpay()
    {
        setBackground(1);
        switchContent(mContent, ncz_notPayFragment);
    }

    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        switchContent(mContent, ncz_allOrderFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        ncz_allOrderFragment=new NCZ_AllOrderFragment_();
        ncz_notPayFragment=new NCZ_NotPayFragment_();
        ncz_dealingOrderFragment=new NCZ_DealingOrderFragment_();
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

        tv_allorder.setBackgroundResource(R.color.white);
        tv_notpay.setBackgroundResource(R.color.white);
        tv_dealing.setBackgroundResource(R.color.white);

        tv_allorder.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_notpay.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_dealing.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_allorder.setSelected(false);
                tv_allorder.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_allorder.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_notpay.setSelected(false);
                tv_notpay.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_notpay.setBackgroundResource(R.drawable.red_bottom);
                break;

            case 2:
                tv_dealing.setSelected(false);
                tv_dealing.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_dealing.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }
}
