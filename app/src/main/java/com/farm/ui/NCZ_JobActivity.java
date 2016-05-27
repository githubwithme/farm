package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EFragment
public class NCZ_JobActivity extends Fragment
{
    NCZ_DoingJobFragment ncz_doingJobFragment;
    NCZ_CompleteJobFragment ncz_completeJobFragment;
    Fragment mContent = new Fragment();
    @ViewById
    TextView tv_jobdoning;
    @ViewById
    TextView tv_jobcomplete;

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
        setBackground(0);
        switchContent(mContent, ncz_doingJobFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_jobfragment, container, false);
        ncz_doingJobFragment = new NCZ_DoingJobFragment_();
        ncz_completeJobFragment = new NCZ_CompleteJobFragment_();
        return rootView;
    }

    private void setBackground(int pos)
    {
        tv_jobdoning.setSelected(false);
        tv_jobcomplete.setSelected(false);

        tv_jobdoning.setBackgroundResource(R.color.white);
        tv_jobcomplete.setBackgroundResource(R.color.white);

        tv_jobdoning.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_jobcomplete.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_jobdoning.setSelected(false);
                tv_jobdoning.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_jobdoning.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_jobcomplete.setSelected(false);
                tv_jobcomplete.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_jobcomplete.setBackgroundResource(R.drawable.red_bottom);
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

}
