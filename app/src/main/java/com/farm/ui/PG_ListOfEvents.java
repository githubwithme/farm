package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by user on 2016/4/8.
 */
@EFragment
public class PG_ListOfEvents extends Fragment
{
    com.farm.bean.commembertab commembertab;
    Fragment mContent = new Fragment();
//    Fragment_DL dl_fragment;
//    Fragment_Sale sale_fragment;

    PG_EventReported pg_eventReported;
    PG_EventProcessed pg_eventProcessed;
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    TextView tv_reported;
    @ViewById
    TextView tv_processed;


    @Click
    void imgbtn_back()
    {
        getActivity().finish();
    }


    @Click
    void tv_reported()
    {
        setBackground(0);
        switchContent(mContent, pg_eventReported);
    }

    @Click
    void tv_processed()
    {
        setBackground(1);
        switchContent(mContent, pg_eventProcessed);
    }

    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        switchContent(mContent, pg_eventReported);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.pg_listofevents, container, false);
        pg_eventReported = new PG_EventReported_();
        pg_eventProcessed = new PG_EventProcessed_();
        return rootView;
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
