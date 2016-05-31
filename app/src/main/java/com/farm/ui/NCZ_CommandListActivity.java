package com.farm.ui;

import android.annotation.SuppressLint;
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

@SuppressLint("NewApi")
@EActivity(R.layout.ncz_commandlistactivity)
public class NCZ_CommandListActivity extends Activity
{
    com.farm.bean.commembertab commembertab;
    Fragment mContent = new Fragment();
    NCZ_Commanding ncz_commanding;
    NCZ_Commanded ncz_commanded;
    @ViewById
    TextView commanding;
    @ViewById
    TextView commanded;
    String workuserid;

    @Click
    void commanding()
    {
        setBackground(0);
        switchContent(mContent, ncz_commanding);
    }

    @Click
    void commanded()
    {
        setBackground(1);
        switchContent(mContent, ncz_commanded);
    }

    @AfterViews
    void afterOncreate()
    {
        ncz_commanding = new NCZ_Commanding_();
        ncz_commanded = new NCZ_Commanded_();
        setBackground(0);
        switchContent(mContent, ncz_commanding);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    private void setBackground(int pos)
    {
        commanding.setSelected(false);
        commanded.setSelected(false);

        commanding.setBackgroundResource(R.color.white);
        commanded.setBackgroundResource(R.color.white);

        commanding.setTextColor(getResources().getColor(R.color.menu_textcolor));
        commanded.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                commanding.setSelected(false);
                commanding.setTextColor(getResources().getColor(R.color.bg_blue));
                commanding.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                commanded.setSelected(false);
                commanded.setTextColor(getResources().getColor(R.color.bg_blue));
                commanded.setBackgroundResource(R.drawable.red_bottom);
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
                transaction.hide(from).add(R.id.wt_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
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
        ncz_commanding.onDestroyView();
        ncz_commanded.onDestroyView();
    }
}
