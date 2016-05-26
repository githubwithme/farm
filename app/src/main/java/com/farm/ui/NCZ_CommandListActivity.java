package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@SuppressLint("NewApi")
@EActivity(R.layout.ncz_commandlistactivity)
public class NCZ_CommandListActivity extends Activity
{
    NCZ_CommandOfList ncz_commandOfList;
    Fragment mContent = new Fragment();

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @AfterViews
    void afterOncreate()
    {
        switchContent(mContent, ncz_commandOfList);
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onUserLeaveHint()
    {
        super.onUserLeaveHint();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        ncz_commandOfList = new NCZ_CommandOfList_();
    }


    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }


}
