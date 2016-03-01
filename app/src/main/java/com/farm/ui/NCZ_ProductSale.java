package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/1/21.
 */
@EActivity(R.layout.ncz_productsale)
public class NCZ_ProductSale extends Activity
{
    com.farm.bean.commembertab commembertab;
    Fragment mContent = new Fragment();
//    Fragment_Sale sale_fragment;
Fragment_MegaSales fragment_megaSales;
    Fragment_MainSales fragment_mainSales;
    Fragment_SmallSales fragment_smallSales;
    @ViewById
    ImageButton btn_back;
    @ViewById
    TextView tv_mainsales;
    @ViewById
    TextView tv_megasales;
    @ViewById
    TextView tv_smallsales;


    @Click
    void btn_back()
    {
        NCZ_ProductSale.this.finish();
    }

    @Click
    void tv_mainsales()
    {
        setBackground(0);
        switchContent(mContent, fragment_mainSales);
    }
    @Click
    void tv_megasales()
    {
        setBackground(1);
        switchContent(mContent, fragment_megaSales);
    }
    @Click
    void tv_smallsales()
    {
        setBackground(2);
        switchContent(mContent, fragment_smallSales);
    }

    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        switchContent(mContent, fragment_mainSales);
    }

    private void setBackground(int pos)
    {
        tv_mainsales.setSelected(false);
        tv_megasales.setSelected(false);
        tv_smallsales.setSelected(false);

        tv_mainsales.setBackgroundResource(R.color.white);
        tv_megasales.setBackgroundResource(R.color.white);
        tv_smallsales.setBackgroundResource(R.color.white);

        tv_mainsales.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_megasales.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_smallsales.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_mainsales.setSelected(false);
                tv_mainsales.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_mainsales.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_megasales.setSelected(false);
                tv_megasales.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_megasales.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 2:
                tv_smallsales.setSelected(false);
                tv_smallsales.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_smallsales.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        fragment_mainSales = new Fragment_MainSales_();
        fragment_megaSales = new Fragment_MegaSales_();
        fragment_smallSales = new Fragment_SmallSales_();
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
}