package com.farm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by ${hmj} on 2016/1/6.
 */
public class CustomFragmentStatePagerAdapter extends FragmentStatePagerAdapter
{
    public CustomFragmentStatePagerAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position)
    {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount()
    {
        return 0;
    }
}
