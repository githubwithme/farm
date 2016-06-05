package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
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
 * Created by user on 2016/2/24.
 */
@EActivity(R.layout.ncz_wzgl)
public class Ncz_wz_ll extends FragmentActivity
{
    com.farm.bean.commembertab commembertab;
    String wzgl;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
//    NCZ_WZ_LOOKFragment nec_wz_lookFragment;
    NCZ_WZ_CKXXFragment ncz_wz_ckxxFragment;
    NCZ_WZ_RKFragment ncz_wz_rkFragment;
    NCZ_WZ_CKFRagment ncz_wz_ckfRagment;
    NCZ_WZ_YCFragment ncz_wz_ycFragment;
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

    @Click
    void imgbtn_back()
    {
        finish();
    }

/*    @Click
    void wz_ll()
    {
        cvPager.setCurrentItem(3);
    }*/

    @Click
    void wzck()
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

        setBackground(0);
        cvPager.setOffscreenPageLimit(1);
        cvPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(Ncz_wz_ll.this.getSupportFragmentManager(), cvPager, fragmentList);
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
        view_ck.setVisibility(View.GONE);
        view_rk.setVisibility(View.GONE);
        view_yc.setVisibility(View.GONE);
//        view_wz.setVisibility(View.GONE);
        view_cangku.setVisibility(View.GONE);
        switch (pos)
        {
            case 0:
                view_ck.setVisibility(View.VISIBLE);
                break;
            case 1:
                view_rk.setVisibility(View.VISIBLE);
                break;
            case 2:
                view_yc.setVisibility(View.VISIBLE);
                break;
            case 3:
                view_cangku.setVisibility(View.VISIBLE);
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
        ;

        commembertab = AppContext.getUserInfo(Ncz_wz_ll.this);
        fragmentList = new ArrayList<>();
        ncz_wz_ckxxFragment = new NCZ_WZ_CKXXFragment_();
//        nec_wz_lookFragment = new NCZ_WZ_LOOKFragment_();
        ncz_wz_rkFragment = new NCZ_WZ_RKFragment_();
        ncz_wz_ckfRagment = new NCZ_WZ_CKFRagment_();
        ncz_wz_ycFragment = new NCZ_WZ_YCFragment_();

        fragmentList.add(ncz_wz_ckfRagment);
        fragmentList.add(ncz_wz_rkFragment);
        fragmentList.add(ncz_wz_ycFragment);
//        fragmentList.add(nec_wz_lookFragment);
        fragmentList.add(ncz_wz_ckxxFragment);


    }
}
