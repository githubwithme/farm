package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
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
@EActivity(R.layout.gcddetail)
public class GcdDetail extends FragmentActivity
{
    com.farm.bean.commembertab commembertab;
    PlantGcd plantGcd;
    int currentItem = 0;
    List<android.support.v4.app.Fragment> fragmentList;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    Fragment mContent = new Fragment();
    GrowthTreeFragment_GCD growthTreeFragment_gcd;
    GrowthTreeFragment_ZZ growthTreeFragment_zz;
    @ViewById
    ImageButton btn_back;
    @ViewById
    CustomViewPager vPager;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_zz;
    @ViewById
    Button btn_add;

    @Click
    void btn_add()
    {
        Intent intent = new Intent(GcdDetail.this, AddPlantObservation_.class);
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
        if (commembertab.getnlevel().equals("0") || commembertab.getnlevel().equals("1"))
        {
            btn_add.setVisibility(View.GONE);
        } else
        {

        }
        setBackground(0);
        vPager.setOffscreenPageLimit(1);
        vPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(GcdDetail.this.getSupportFragmentManager(), vPager, fragmentList);
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
        plantGcd = getIntent().getParcelableExtra("bean");
        bundle.putParcelable("bean", plantGcd);

        commembertab = AppContext.getUserInfo(GcdDetail.this);
        fragmentList = new ArrayList<>();
        growthTreeFragment_gcd = new GrowthTreeFragment_GCD_();
        growthTreeFragment_zz = GrowthTreeFragment_ZZ.newInstance(0);
        growthTreeFragment_gcd.setArguments(bundle);
        growthTreeFragment_zz.setArguments(bundle);
        fragmentList.add(growthTreeFragment_gcd);
        fragmentList.add(growthTreeFragment_zz);
    }


}
