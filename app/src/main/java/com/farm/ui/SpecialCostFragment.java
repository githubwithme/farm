package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/25.
 */
@EActivity(R.layout.fragment_special_cost)
public class SpecialCostFragment extends Activity
{
    NCZ_NotPayCostFragment ncz_notPayCostFragment;
    NCZ_NotCheckCostFragment ncz_notCheckCostFragment;
    NCZ_AllCostFragment ncz_allCostFragment;
    Fragment mContent = new Fragment();
    @ViewById
    TextView tv_notpay;
    @ViewById
    TextView tv_notcheck;
    @ViewById
    TextView tv_all;


    @Click
    void tv_notpay()
    {
        setBackground(0);
        switchContent(mContent, ncz_notPayCostFragment);
    }

    @Click
    void tv_notcheck()
    {
        setBackground(1);
        switchContent(mContent, ncz_notCheckCostFragment);
    }

    @Click
    void tv_all()
    {
        setBackground(2);
        switchContent(mContent, ncz_allCostFragment);
    }

    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        switchContent(mContent, ncz_notPayCostFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        ncz_notPayCostFragment = new NCZ_NotPayCostFragment_();
        ncz_notCheckCostFragment = new NCZ_NotCheckCostFragment_();
        ncz_allCostFragment = new NCZ_AllCostFragment_();
    }

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container_special, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private void setBackground(int pos)
    {
        tv_notpay.setSelected(false);
        tv_notcheck.setSelected(false);
        tv_all.setSelected(false);

        tv_notpay.setBackgroundResource(R.color.white);
        tv_notcheck.setBackgroundResource(R.color.white);
        tv_all.setBackgroundResource(R.color.white);

        tv_notpay.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_notcheck.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_all.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_notpay.setSelected(false);
                tv_notpay.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_notpay.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_notcheck.setSelected(false);
                tv_notcheck.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_notcheck.setBackgroundResource(R.drawable.red_bottom);
                break;

            case 2:
                tv_all.setSelected(false);
                tv_all.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_all.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }
}
