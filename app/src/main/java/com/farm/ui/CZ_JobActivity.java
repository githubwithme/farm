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
 * Created by hasee on 2016/6/15.
 */
@EActivity(R.layout.ncz_jobfragment)
public class CZ_JobActivity extends Activity
{
    //    NCZ_DoingJobFragment ncz_doingJobFragment;
//    NCZ_CompleteJobFragment ncz_completeJobFragment;
    CZ_DoingJobFragment cz_doingJobFragment;
    CZ_CompleteJobFragment cz_completeJobFragment;
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
        switchContent(mContent, cz_doingJobFragment);
    }

    @Click
    void tv_jobcomplete()
    {
        setBackground(1);
        switchContent(mContent, cz_completeJobFragment);
    }


    @AfterViews
    void afterOncreate()
    {
        cz_doingJobFragment =new CZ_DoingJobFragment_();
        cz_completeJobFragment=new CZ_CompleteJobFragment_();
//        ncz_doingJobFragment = new NCZ_DoingJobFragment_();
//        ncz_completeJobFragment = new NCZ_CompleteJobFragment_();
        setBackground(0);
        switchContent(mContent, cz_doingJobFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    public void setBackground(int pos)
    {

        switch (pos)
        {
            case 0:

                view_jobcomplete.setVisibility(View.GONE);
                view_jobdoing.setVisibility(View.VISIBLE);
                break;
            case 1:

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
