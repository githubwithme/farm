package com.farm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/6.
 */
public class CustomFragmentPagerAdapter extends FragmentPagerAdapter
{
    private List<String> list;
    private List<String> tagList = new ArrayList<String>();
    private FragmentManager fragmentManager;
    private int currentPageIndex = 0;
    private List<Fragment> fragments;

    public CustomFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public CustomFragmentPagerAdapter(FragmentManager fragmentManager, List<String> list, List<Fragment> fragments)
    {
        super(fragmentManager);
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
        this.list = list;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position)
//    {
//        Fragment fragment = fragments.get(position);
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        if (!fragment.isAdded())
//        {
//
//            ft.add(fragment, fragment.getClass().getSimpleName());
//            String tag = fragment.getTag();
//
//            tagList.add(tag);
//            ft.commitAllowingStateLoss();
//            fragmentManager.executePendingTransactions();
//        } else
//        {
//            ft.replace(position, fragment);
//        }
//
//        if (fragment.getView().getParent() == null)
//        {
//            container.addView(fragment.getView());
//        }
//
//        return fragment.getView();
//    }

    @Override
    public int getItemPosition(Object object)
    {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        super.destroyItem(container, position, object);
    }

    public void setFragments(List fragments)
    {
        if (this.fragments != null)
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            for (Fragment f : this.fragments)
            {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fragmentManager.executePendingTransactions();
        }
        this.fragments = fragments;
//        notifyDataSetChanged();
    }
}
