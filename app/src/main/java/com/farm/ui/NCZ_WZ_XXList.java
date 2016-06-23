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
import com.farm.adapter.ViewPagerAdapter_WZ;
import com.farm.app.AppContext;
import com.farm.bean.WZ_Detail;
import com.farm.bean.goodslisttab;
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
    String parkid;
//    goodslisttab goods;
    WZ_Detail goods;
    ViewPagerAdapter_WZ viewPagerAdapter_wz;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    com.farm.bean.commembertab commembertab;
    int currentItem = 0;

    List<Fragment> fragmentList;
//    NCZ_WZ_PC ncz_wz_pc;
    NCZ_WZ_FB ncz_wz_fb;
    Fragment mContent = new Fragment();
    @ViewById
    CustomViewPager cvPager;
    @ViewById
    TextView wz_pici;
    @ViewById
    TextView wz_fenbu;
    @ViewById
    TextView wzxx_tab;
    @Click
    void wzbtn_account(){
        finish();
    }
    @Click
    void wz_pici()
    {
        cvPager.setCurrentItem(0);

    }
    @Click
    void wz_fenbu()
    {
        cvPager.setCurrentItem(1);
    }
    @AfterViews
    void afterOncreat(){
//        wzxx_tab.setText(goods.getGoodsName());
        wz_fenbu.setText(goods.getGoodsName());
//        setBackground(0);
        cvPager.setOffscreenPageLimit(1);
        cvPager.setIsScrollable(true);
        viewPagerAdapter_wz=new ViewPagerAdapter_WZ(NCZ_WZ_XXList.this.getSupportFragmentManager(), cvPager, fragmentList);
        viewPagerAdapter_wz.setOnExtraPageChangeListener(new ViewPagerAdapter_WZ.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageScrolled(int i, float v, int i2) {
                currentItem = i;
//                setBackground(i);
            }
        });

    }
    private void setBackground(int pos) {
        wz_pici.setBackgroundResource(R.color.white);
        wz_fenbu.setBackgroundResource(R.color.white);

        switch (pos) {
            case 0:
                wz_fenbu.setBackgroundResource(R.drawable.red_bottom);
                break;
//  /*          case 1:
//                wz_fenbu.setBackgroundResource(R.drawable.red_bottom);
//                break;*/

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        goods=getIntent().getParcelableExtra("goods");
        parkid = getIntent().getStringExtra("parkid");
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        Bundle bundle = new Bundle();
        bundle.putParcelable("goods",goods);
        bundle.putString("parkid", parkid);
        commembertab = AppContext.getUserInfo(NCZ_WZ_XXList.this);
        fragmentList = new ArrayList<>();
//        ncz_wz_pc=new NCZ_WZ_PC_();
        ncz_wz_fb=new NCZ_WZ_FB_();
//        ncz_wz_pc.setArguments(bundle);
        ncz_wz_fb.setArguments(bundle);

        fragmentList.add(ncz_wz_fb);
//        fragmentList.add(ncz_wz_pc);


    }


}
