package com.farm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Created by user on 2016/4/26.
 */
@EActivity(R.layout.gcddetail)
public class NCZ_todaymyDetail extends FragmentActivity
{
    com.farm.bean.commembertab commembertab;
    PlantGcd plantGcd;
    com.farm.bean.areatab areatab;
    int currentItem = 0;
    List<Fragment> fragmentList;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    android.app.Fragment mContent = new android.app.Fragment();
    NCZ_todayDetail_zz  ncz_todayDetail_zz;
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
   /*     Intent intent = new Intent(NCZ_todaymyDetail.this, AddPlantObservation_.class);
        intent.putExtra("gcdid", plantGcd.getId());
        startActivity(intent);*/
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
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(NCZ_todaymyDetail.this.getSupportFragmentManager(), vPager, fragmentList);
        viewPagerAdapter_gcdDetail.setOnExtraPageChangeListener(new ViewPagerAdapter_GcdDetail.OnExtraPageChangeListener() {
            @Override
            public void onExtraPageSelected(int i) {
//                Toast.makeText(GcdDetail.this, "show", Toast.LENGTH_SHORT).show();
                currentItem = i;
                setBackground(i);
            }
        });
    }

    private void setBackground(int pos) {
        tv_zz.setBackgroundResource(R.color.white);
        tv_title.setBackgroundResource(R.color.white);
        switch (pos) {
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
        plantGcd = getIntent().getParcelableExtra("bean_gcd");
//        areatab = getIntent().getParcelableExtra("bean_areatab");
        bundle.putParcelable("bean_gcd", plantGcd);
//        bundle.putParcelable("bean_areatab", areatab);

        commembertab = AppContext.getUserInfo(NCZ_todaymyDetail.this);
        fragmentList = new ArrayList<>();
        ncz_todayDetail_zz=new NCZ_todayDetail_zz_();


//        growthTreeFragment_gcd = new GrowthTreeFragment_GCD_();
//        growthTreeFragment_zz = GrowthTreeFragment_ZZ.newInstance(0);
//        growthTreeFragment_gcd.setArguments(bundle);
//        growthTreeFragment_zz.setArguments(bundle);
        ncz_todayDetail_zz.setArguments(bundle);
        fragmentList.add(ncz_todayDetail_zz);
//        fragmentList.add(growthTreeFragment_zz);
    }

}
