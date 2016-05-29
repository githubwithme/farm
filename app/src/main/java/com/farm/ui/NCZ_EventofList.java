package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by user on 2016/4/11.
 */
@EFragment
public class NCZ_EventofList  extends Fragment
{
    NCZ_Eventing ncz_eventing;
    NCZ_Evented ncz_evented;
    com.farm.bean.commembertab commembertab;
    Fragment mContent = new Fragment();
    @ViewById
    TextView tv_reported;
    @ViewById
    TextView tv_processed;


    @Click
    void tv_reported()
    {
        setBackground(0);
        switchContent(mContent, ncz_eventing);
    }

    @Click
    void tv_processed()
    {
        setBackground(1);
        switchContent(mContent, ncz_evented);
    }

    @AfterViews
    void afterOncreate()
    {
        ncz_eventing=new NCZ_Eventing_();
        ncz_evented=new NCZ_Evented_();
        setBackground(0);
        switchContent(mContent, ncz_eventing);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ncz_eventoflist, container, false);

        return rootView;
    }
    private void setBackground(int pos)
    {
        tv_reported.setSelected(false);
        tv_processed.setSelected(false);

        tv_reported.setBackgroundResource(R.color.white);
        tv_processed.setBackgroundResource(R.color.white);

        tv_reported.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_processed.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_reported.setSelected(false);
                tv_reported.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_reported.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_processed.setSelected(false);
                tv_processed.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_processed.setBackgroundResource(R.drawable.red_bottom);
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
                transaction.hide(from).add(R.id.fl_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
