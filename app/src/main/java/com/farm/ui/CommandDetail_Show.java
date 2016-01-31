package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_GcdDetail;
import com.farm.app.AppContext;
import com.farm.bean.PlantGcd;
import com.farm.widget.CustomViewPager;
import com.farm.widget.MyDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/21.
 */
@EActivity(R.layout.commanddetail_show)
public class CommandDetail_Show extends FragmentActivity
{
    MyDialog myDialog;
    com.farm.bean.commembertab commembertab;
    PlantGcd plantGcd;
    com.farm.bean.commandtab commandtab;
    int currentItem = 0;
    List<android.support.v4.app.Fragment> fragmentList;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    Fragment mContent = new Fragment();
    //    GrowthTreeFragment_GCD growthTreeFragment_gcd;
//    GrowthTreeFragment_ZZ growthTreeFragment_zz;
    CommandDetail_Show_DetailFragment commandDetail_show_detailFragment;
    CommandDetail_Show_ExecuteFragment commandDetail_show_executeFragment;
    @ViewById
    ImageButton btn_back;
    @ViewById
    CustomViewPager vPager;
    @ViewById
    TextView tv_detail;
    @ViewById
    TextView tv_execute;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void tv_execute()
    {
        vPager.setCurrentItem(1);
    }

    @Click
    void tv_detail()
    {
        vPager.setCurrentItem(0);
    }

    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        vPager.setOffscreenPageLimit(1);
        vPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(CommandDetail_Show.this.getSupportFragmentManager(), vPager, fragmentList);
        viewPagerAdapter_gcdDetail.setOnExtraPageChangeListener(new ViewPagerAdapter_GcdDetail.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageSelected(int i)
            {
//                Toast.makeText(GcdDetail.this, "show", Toast.LENGTH_SHORT).show();
                currentItem = i;
                setBackground(i);
            }
        });
    }

    private void setBackground(int pos)
    {
        tv_detail.setBackgroundResource(R.color.white);
        tv_execute.setBackgroundResource(R.color.white);
        switch (pos)
        {
            case 0:
                tv_detail.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_execute.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        Bundle bundle = new Bundle();
        getIntent().getStringExtra("from");
        commandtab = getIntent().getParcelableExtra("bean");
        bundle.putParcelable("bean", commandtab);

        commembertab = AppContext.getUserInfo(CommandDetail_Show.this);
        fragmentList = new ArrayList<>();
        commandDetail_show_detailFragment = new CommandDetail_Show_DetailFragment_();
        commandDetail_show_executeFragment = new CommandDetail_Show_ExecuteFragment_();
        commandDetail_show_detailFragment.setArguments(bundle);
        commandDetail_show_executeFragment.setArguments(bundle);
        fragmentList.add(commandDetail_show_detailFragment);
        fragmentList.add(commandDetail_show_executeFragment);
    }


}
