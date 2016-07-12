package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_GcdDetail;
import com.farm.app.AppContext;
import com.farm.bean.PlantGcd;
import com.farm.bean.jobtab;
import com.farm.widget.CustomViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/7/12.
 */
@EActivity(R.layout.pg_commandactivity)
public class PG_CommandActivity extends FragmentActivity
{
    private List<jobtab> joblist;
    com.farm.bean.commembertab commembertab;
    PlantGcd plantGcd;
    com.farm.bean.areatab areatab;
    int currentItem = 0;
    List<android.support.v4.app.Fragment> fragmentList;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    Fragment mContent = new Fragment();
    PG_CommandFragment pg_commandFragment;
    PG_CommandFragment_Finish pg_commandFragment_finish;
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
        Intent intent = new Intent(PG_CommandActivity.this, AddPlantObservation_.class);
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
        commembertab = AppContext.getUserInfo(PG_CommandActivity.this);
        joblist = getIntent().getParcelableArrayListExtra("jobtablist");
        fragmentList = new ArrayList<>();
        pg_commandFragment = new PG_CommandFragment_();
        pg_commandFragment_finish=new PG_CommandFragment_Finish_();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("jobtablist", (ArrayList<? extends Parcelable>) joblist);
        pg_commandFragment.setArguments(bundle);
        pg_commandFragment_finish.setArguments(bundle);
        fragmentList.add(pg_commandFragment);
        fragmentList.add(pg_commandFragment_finish);

        setBackground(0);
        vPager.setOffscreenPageLimit(1);
        vPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(PG_CommandActivity.this.getSupportFragmentManager(), vPager, fragmentList);
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
    }

}
