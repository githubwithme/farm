package com.farm.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by ${hmj} on 2016/1/6.
 */
public class CustomPagerAdapter extends PagerAdapter
{

    @Override
    public int getCount()
    {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return false;
    }
}
