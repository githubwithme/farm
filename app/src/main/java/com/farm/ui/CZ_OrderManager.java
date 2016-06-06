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
 * Created by ${hmj} on 2016/5/16.
 */
@EActivity(R.layout.cz_ordermanager)
public class CZ_OrderManager extends Activity
{
    CZ_AllOrderFragment cz_allOrderFragment;
    CZ_NeedFeedbackOrder cz_needFeedbackOrder;
    Fragment mContent = new Fragment();
    @ViewById
    TextView tv_allorder;
    @ViewById
    TextView tv_needfeedback;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void tv_allorder()
    {
        setBackground(1);
        switchContent(mContent, cz_allOrderFragment);
    }


    @Click
    void tv_needfeedback()
    {
        setBackground(0);
        switchContent(mContent, cz_needFeedbackOrder);
    }

    @AfterViews
    void afterOncreate()
    {
        cz_allOrderFragment = new CZ_AllOrderFragment_();
        cz_needFeedbackOrder = new CZ_NeedFeedbackOrder_();
        setBackground(0);
        switchContent(mContent, cz_needFeedbackOrder);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
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
        tv_needfeedback.setSelected(false);

        tv_allorder.setBackgroundResource(R.color.white);
        tv_needfeedback.setBackgroundResource(R.color.white);

        tv_allorder.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_needfeedback.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_needfeedback.setSelected(false);
                tv_needfeedback.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_needfeedback.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_allorder.setSelected(false);
                tv_allorder.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_allorder.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }
}
