package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.NCZ_DLAdapter;
import com.farm.adapter.ViewPagerAdapter_GcdDetail;
import com.farm.app.AppContext;
import com.farm.bean.Wz_Storehouse;
import com.farm.widget.CustomViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/7/11.
 */
@EActivity(R.layout.ncz_goodsmanageractivity)
public class NCZ_GoogdsManagerActivity extends FragmentActivity
{
    NCZ_DLAdapter ncz_dlAdapter;
    private String id;
    private String name;
    List<Wz_Storehouse> listpeople = new ArrayList<Wz_Storehouse>();
    PopupWindow pw_tab;
    View pv_tab;
    @ViewById
    View line;
    com.farm.bean.commembertab commembertab;
    String wzgl;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    NCZ_GoodsFragment ncz_goodsFragment;
    int currentItem = 0;
    List<Fragment> fragmentList;
    Fragment mContent = new Fragment();
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    TextView wz_ll;
    @ViewById
    TextView wzck;
    @ViewById
    TextView wz_rk;
    @ViewById
    TextView wz_ck;
    @ViewById
    TextView wz_yc;
    @ViewById
    View view_cangku;
    @ViewById
    View view_ck;
    @ViewById
    View view_rk;
    @ViewById
    View view_wz;
    @ViewById
    View view_yc;
    @ViewById
    CustomViewPager cvPager;
    @ViewById
    Button btn_search;


    @Click
    void imgbtn_back()
    {
        finish();
    }

    @Click
    void wz_ll()
    {
        cvPager.setCurrentItem(3);
    }


    @Click
    void wz_rk()
    {
        cvPager.setCurrentItem(1);
    }

    @Click
    void wz_ck()
    {
        cvPager.setCurrentItem(0);
    }

    @Click
    void wz_yc()
    {
        cvPager.setCurrentItem(2);
    }

    @AfterViews
    void afterOncreat()
    {
        commembertab = AppContext.getUserInfo(NCZ_GoogdsManagerActivity.this);
        fragmentList = new ArrayList<>();
        ncz_goodsFragment = new NCZ_GoodsFragment_();
        fragmentList.add(ncz_goodsFragment);
        fragmentList.add(ncz_goodsFragment);
        fragmentList.add(ncz_goodsFragment);
        fragmentList.add(ncz_goodsFragment);
        currentItem = 0;
        setBackground(0);
        cvPager.setOffscreenPageLimit(0);
        cvPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(NCZ_GoogdsManagerActivity.this.getSupportFragmentManager(), cvPager, fragmentList);
        viewPagerAdapter_gcdDetail.setOnExtraPageChangeListener(new ViewPagerAdapter_GcdDetail.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageSelected(int i)
            {
                currentItem = i;
                setBackground(i);
            }
        });
    }

    private void setBackground(int pos)
    {
        view_ck.setVisibility(View.GONE);
        view_rk.setVisibility(View.GONE);
        view_yc.setVisibility(View.GONE);
        view_wz.setVisibility(View.GONE);
//        view_cangku.setVisibility(View.GONE);
        switch (pos)
        {
            case 0:
                view_wz.setVisibility(View.VISIBLE);

                break;
            case 1:
                view_ck.setVisibility(View.VISIBLE);

                break;
            case 2:
                view_rk.setVisibility(View.VISIBLE);

                break;
            case 3:
                view_yc.setVisibility(View.VISIBLE);
                break;
        /*    case 4:
                view_cangku.setVisibility(View.VISIBLE);
                break;*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


}
