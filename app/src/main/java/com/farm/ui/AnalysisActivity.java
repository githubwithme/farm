package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@SuppressLint("NewApi")
@EFragment
public class AnalysisActivity extends Fragment
{
    HoursWeatherFragment hoursWeatherFragment;
    Fragment mContent_hours = new Fragment();

    String parkid;

    @Click
    void rl_kc()
    {
        Intent intent = new Intent(getActivity(), AnalysisGoodActivity_.class);
        startActivity(intent);
    }

    @Click
    void rl_zz()
    {
        Intent intent = new Intent(getActivity(), AnalysisProductActivity_.class);
        startActivity(intent);
    }

    @Click
    void rl_gz()
    {
        Intent intent = new Intent(getActivity(), AnalysisProductActivity_.class);
        startActivity(intent);
    }

    @Click
    void rl_cw()
    {
        Intent intent = new Intent(getActivity(), AnalysisProductActivity_.class);
        startActivity(intent);
    }

    @Click
    void rl_xs()
    {
        Intent intent = new Intent(getActivity(), AnalysisSaleActivity_.class);
        startActivity(intent);
    }

    @Click
    void rl_cp()
    {
        Intent intent = new Intent(getActivity(), AnalysisProductActivity_.class);
        startActivity(intent);
    }

    @Click
    void btn_back()
    {
        getActivity().finish();
    }

    @AfterViews
    void afterOncreate()
    {
        switchContent_day(mContent_hours, hoursWeatherFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.analysisactivity, container, false);
        Bundle bundle = new Bundle();
        bundle.putString("parkid", "8");
        hoursWeatherFragment = new HoursWeatherFragment();
        hoursWeatherFragment.setArguments(bundle);
        return rootView;
    }

    public void switchContent_day(Fragment from, Fragment to)
    {
        if (mContent_hours != to)
        {
            mContent_hours = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.chart_day, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

}
