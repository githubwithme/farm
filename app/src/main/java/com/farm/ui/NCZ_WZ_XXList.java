package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_GcdDetail;
import com.farm.app.AppContext;
import com.farm.widget.CustomViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/2/26.
 */
@SuppressLint("NewApi")
@EActivity(R.layout.wz_ck_xxlist)
public class NCZ_WZ_XXList extends FragmentActivity
{

    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    com.farm.bean.commembertab commembertab;
    int currentItem = 0;

    List<Fragment> fragmentList;
    NCZ_WZ_PC ncz_wz_pc;
    Fragment mContent = new Fragment();
    @ViewById
    CustomViewPager wz_kucun;
    @ViewById
    TextView wz_pici;
    @ViewById
    TextView wz_fenbu;
    @Click
    void wzbtn_account(){
        finish();
    }
    @Click
    void wz_pici(){
        wz_kucun.setCurrentItem(0);

    }
    @Click
    void wz_fenbu(){
        wz_kucun.setCurrentItem(1);
    }
    @AfterViews
    void afterOncreat(){
        setBackground(0);
        wz_kucun.setOffscreenPageLimit(1);
        wz_kucun.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(NCZ_WZ_XXList.this.getSupportFragmentManager(), wz_kucun, fragmentList);
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
        wz_pici.setBackgroundResource(R.color.white);
        wz_fenbu.setBackgroundResource(R.color.white);

        switch (pos) {
            case 0:
                wz_pici.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                wz_fenbu.setBackgroundResource(R.drawable.red_bottom);
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        commembertab = AppContext.getUserInfo(NCZ_WZ_XXList.this);
        fragmentList = new ArrayList<>();
        ncz_wz_pc=new NCZ_WZ_PC_();

        fragmentList.add(ncz_wz_pc);


    }


}
