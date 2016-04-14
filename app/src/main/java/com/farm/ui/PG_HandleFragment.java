package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_WZ;
import com.farm.bean.HandleBean;
import com.farm.widget.CustomViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/4/14.
 */
@EActivity(R.layout.pg_handlefragment)
public class PG_HandleFragment extends FragmentActivity
{
    PG_ISEventDatail pg_isEventDatail;
    PG_ISAddHandle  pg_isAddHandle;
    HandleBean handleBean;
    ViewPagerAdapter_WZ viewPagerAdapter_wz;
    List<Fragment> fragmentList;
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
        viewPagerAdapter_wz=new ViewPagerAdapter_WZ(PG_HandleFragment.this.getSupportFragmentManager(), cvPager, fragmentList);
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
        handleBean=getIntent().getParcelableExtra("handleBean");
        Bundle bundle = new Bundle();
        bundle.putParcelable("handleBean",handleBean);
        fragmentList = new ArrayList<>();
        pg_isEventDatail=new PG_ISEventDatail_();
        pg_isAddHandle=new PG_ISAddHandle_();
        pg_isEventDatail.setArguments(bundle);
        pg_isAddHandle.setArguments(bundle);
        fragmentList.add(pg_isEventDatail);
        fragmentList.add(pg_isAddHandle);
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
