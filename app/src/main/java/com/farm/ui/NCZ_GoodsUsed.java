package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/7/12.
 */
@EActivity(R.layout.ncz_goodsused)
public class NCZ_GoodsUsed extends Activity
{
    Fragment mContent_daygoodsused = new Fragment();
    Fragment mContent_contractgoodsused = new Fragment();
    @ViewById
    Button btn_back;
    @ViewById
    FrameLayout contain_everydaygoodsused;
    @ViewById
    FrameLayout contain_everycontractgoodsused;
    @ViewById
    FrameLayout contain_goodsusedchart;


    @AfterViews
    void afterOncreate()
    {
        NCZ_CommandEvearyDayGoodsUsed ncz_commandEvearyDayGoodsUsed = new NCZ_CommandEvearyDayGoodsUsed_();
        switchContent_daygoodsused(mContent_daygoodsused, ncz_commandEvearyDayGoodsUsed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    public void switchContent_daygoodsused(Fragment from, Fragment to)
    {
        if (mContent_daygoodsused != to)
        {
            mContent_daygoodsused = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.contain_everydaygoodsused, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public void switchContent_contractgoodsused(Fragment from, Fragment to)
    {
        if (mContent_contractgoodsused != to)
        {
            mContent_contractgoodsused = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.contain_goodsusedchart, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
