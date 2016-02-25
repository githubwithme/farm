package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.Ncz_wzgl_CVP;
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
 * Created by user on 2016/2/24.
 */
@EActivity(R.layout.ncz_wzgl)
public class Ncz_wz_ll extends FragmentActivity {
    com.farm.bean.commembertab commembertab;
    String wzgl;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    NCZ_WZ_LOOKFragment ncz_wz_lookFragment;
    Ncz_wzgl_CVP ncz_wzgl_cvp;
    int currentItem = 0;
    List<Fragment> fragmentList;
    Fragment mContent = new Fragment();
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    TextView wz_ll;
    @ViewById
    TextView wz_ck;
    @ViewById
    TextView wz_cr;
    @ViewById
    TextView wz_yc;
    @ViewById
    CustomViewPager cvPager;

    @Click
    void imgbtn_back() {
        finish();
        ;
    }

    @Click
    void wz_ll() {
        cvPager.setCurrentItem(0);
    }

    @Click
    void wz_ck() {
        cvPager.setCurrentItem(0);
    }

    @Click
    void wz_cr() {
        cvPager.setCurrentItem(0);
    }

    @Click
    void wz_yc() {
        cvPager.setCurrentItem(0);
    }

    @AfterViews
    void afterOncreat() {

        setBackground(0);
        cvPager.setOffscreenPageLimit(1);
        cvPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(Ncz_wz_ll.this.getSupportFragmentManager(), cvPager, fragmentList);
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
        wz_ll.setBackgroundResource(R.color.white);
        wz_ck.setBackgroundResource(R.color.white);
        wz_cr.setBackgroundResource(R.color.white);
        wz_yc.setBackgroundResource(R.color.white);
        switch (pos) {
            case 0:
                wz_ll.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                wz_ck.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 2:
                wz_cr.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 3:
                wz_yc.setBackgroundResource(R.drawable.red_bottom);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar();
        Bundle bundle = new Bundle();
        wzgl = getIntent().getStringExtra("wzgl");
        bundle.putString("wzgl", wzgl);
        commembertab = AppContext.getUserInfo(Ncz_wz_ll.this);
        fragmentList = new ArrayList<>();


    }
}
