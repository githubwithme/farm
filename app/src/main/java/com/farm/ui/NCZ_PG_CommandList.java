package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_GcdDetail;
import com.farm.app.AppContext;
import com.farm.bean.PlantGcd;
import com.farm.widget.CustomViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/21.
 */
@EActivity(R.layout.ncz_pg_commandlist)
public class NCZ_PG_CommandList extends FragmentActivity
{
    com.farm.bean.commembertab commembertab;
    PlantGcd plantGcd;
    String workuserid;
    int currentItem = 0;
    List<android.support.v4.app.Fragment> fragmentList;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    Fragment mContent = new Fragment();
    //    GrowthTreeFragment_GCD growthTreeFragment_gcd;
//    GrowthTreeFragment_ZZ growthTreeFragment_zz;
    NCZ_PQ_TodayCommandFragment ncz_pq_todayCommandFragment;
    NCZ_PQ_MoreCommandFragment ncz_pq_moreCommandFragment;
    @ViewById
    ImageButton btn_back;
    @ViewById
    CustomViewPager vPager;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_zz;

    @Click
    void btn_add()
    {
        Intent intent = new Intent(NCZ_PG_CommandList.this, AddPlantObservation_.class);
        intent.putExtra("gcdid", plantGcd.getId());
        startActivity(intent);
    }

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void tv_zz()
    {
        vPager.setCurrentItem(1);
    }

    @Click
    void tv_title()
    {
        vPager.setCurrentItem(0);
    }

    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        vPager.setOffscreenPageLimit(1);
        vPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(NCZ_PG_CommandList.this.getSupportFragmentManager(), vPager, fragmentList);
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
        tv_zz.setBackgroundResource(R.color.white);
        tv_title.setBackgroundResource(R.color.white);
        switch (pos)
        {
            case 0:
                tv_title.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_zz.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        Bundle bundle = new Bundle();
        workuserid = getIntent().getStringExtra("workuserid");
        bundle.putString("workuserid", workuserid);

        commembertab = AppContext.getUserInfo(NCZ_PG_CommandList.this);
        fragmentList = new ArrayList<>();
        ncz_pq_todayCommandFragment = new NCZ_PQ_TodayCommandFragment_();
        ncz_pq_moreCommandFragment = new NCZ_PQ_MoreCommandFragment_();

        ncz_pq_todayCommandFragment.setArguments(bundle);
        ncz_pq_moreCommandFragment.setArguments(bundle);
        fragmentList.add(ncz_pq_todayCommandFragment);
        fragmentList.add(ncz_pq_moreCommandFragment);
    }


}
