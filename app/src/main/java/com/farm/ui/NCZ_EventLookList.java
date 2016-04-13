package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_WZ;
import com.farm.app.AppContext;
import com.farm.bean.ReportedBean;
import com.farm.widget.CustomViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/4/11.
 */

@EActivity(R.layout.ncz_eventlooklist)
public class NCZ_EventLookList extends FragmentActivity
{

    NCZ_EventDetail ncz_eventDetail;
    NCZ_EventHandle ncz_eventHandle;

    ViewPagerAdapter_WZ viewPagerAdapter_wz;
    List<Fragment> fragmentList;
    ReportedBean reportedBean;
    int currentItem = 0;


    @ViewById
    CustomViewPager cvPager;
    @ViewById
    TextView event_ed;
    @ViewById
    TextView event_ing;
    @Click
    void wzbtn_account(){
        finish();
    }
    @Click
    void event_ing()
    {
        cvPager.setCurrentItem(0);

    }
    @Click
    void event_ed()
    {
        cvPager.setCurrentItem(1);
    }

    @AfterViews
    void afterOncreat(){
        setBackground(0);
        cvPager.setOffscreenPageLimit(1);
        cvPager.setIsScrollable(true);
        viewPagerAdapter_wz=new ViewPagerAdapter_WZ(NCZ_EventLookList.this.getSupportFragmentManager(), cvPager, fragmentList);
        viewPagerAdapter_wz.setOnExtraPageChangeListener(new ViewPagerAdapter_WZ.OnExtraPageChangeListener() {
            @Override
            public void onExtraPageScrolled(int i, float v, int i2) {
                currentItem = i;
                setBackground(i);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        reportedBean=getIntent().getParcelableExtra("reportedBean");
        Bundle bundle = new Bundle();
        bundle.putParcelable("reportedBean",reportedBean);
        fragmentList = new ArrayList<>();
        ncz_eventDetail=new NCZ_EventDetail_();
        ncz_eventHandle=new NCZ_EventHandle_();
        ncz_eventDetail.setArguments(bundle);
        ncz_eventHandle.setArguments(bundle);
        fragmentList.add(ncz_eventDetail);
        fragmentList.add(ncz_eventHandle);
/*        Bundle bundle = new Bundle();
        bundle.putParcelable("goods",goods);
        commembertab = AppContext.getUserInfo(NCZ_WZ_XXList.this);
        fragmentList = new ArrayList<>();
        ncz_wz_pc=new NCZ_WZ_PC_();
        ncz_wz_fb=new NCZ_WZ_FB_();
        ncz_wz_pc.setArguments(bundle);
        ncz_wz_fb.setArguments(bundle);
        fragmentList.add(ncz_wz_pc);
        fragmentList.add(ncz_wz_fb);*/
    }

    private void setBackground(int pos) {
        event_ing.setBackgroundResource(R.color.white);
        event_ed.setBackgroundResource(R.color.white);

        switch (pos) {
            case 0:
                event_ing.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                event_ed.setBackgroundResource(R.drawable.red_bottom);
                break;

        }
    }

}
