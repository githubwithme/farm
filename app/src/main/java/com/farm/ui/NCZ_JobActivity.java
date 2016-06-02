package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EActivity(R.layout.ncz_jobfragment)
public class NCZ_JobActivity extends Activity
{
    NCZ_DoingJobFragment ncz_doingJobFragment;
    NCZ_CompleteJobFragment ncz_completeJobFragment;
    Fragment mContent = new Fragment();
    @ViewById
    TextView tv_jobdoning;
    @ViewById
    TextView tv_jobcomplete;
    @ViewById
    View view_jobcomplete;
    @ViewById
    View view_jobdoing;

    @Click
    void tv_jobdoning()
    {
        setBackground(0);
        switchContent(mContent, ncz_doingJobFragment);
    }

    @Click
    void tv_jobcomplete()
    {
        setBackground(1);
        switchContent(mContent, ncz_completeJobFragment);
    }


    @AfterViews
    void afterOncreate()
    {
        ncz_doingJobFragment = new NCZ_DoingJobFragment_();
        ncz_completeJobFragment = new NCZ_CompleteJobFragment_();
        setBackground(0);
        switchContent(mContent, ncz_doingJobFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    private void setBackground(int pos)
    {
//        tv_jobdoning.setSelected(false);
//        tv_jobcomplete.setSelected(false);
//
//        tv_jobdoning.setBackgroundResource(R.color.white);
//        tv_jobcomplete.setBackgroundResource(R.color.white);
//
//        tv_jobdoning.setTextColor(getResources().getColor(R.color.menu_textcolor));
//        tv_jobcomplete.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
//                tv_jobdoning.setSelected(false);
//                tv_jobdoning.setTextColor(getResources().getColor(R.color.bg_blue));
//                tv_jobdoning.setBackgroundResource(R.drawable.red_bottom);
                view_jobcomplete.setVisibility(View.GONE);
                view_jobdoing.setVisibility(View.VISIBLE);
//                break;
            case 1:
//                tv_jobcomplete.setSelected(false);
//                tv_jobcomplete.setTextColor(getResources().getColor(R.color.bg_blue));
//                tv_jobcomplete.setBackgroundResource(R.drawable.red_bottom);
                view_jobcomplete.setVisibility(View.VISIBLE);
                view_jobdoing.setVisibility(View.GONE);
                break;
        }

    }

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.job_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
