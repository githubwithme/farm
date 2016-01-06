package com.farm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/6.
 */
public class CustomFragmentPagerAdapter extends FragmentPagerAdapter
{
    private List<String> tagList = new ArrayList<String>();
    private FragmentManager fragmentManager;
    private ViewPager viewPager;
    private int currentPageIndex = 0;
    private List<Fragment> fragments;

    public CustomFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        Fragment fragment = fragments.get(position);

        if (!fragment.isAdded())
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            String tag = fragment.getTag();

            tagList.add(tag);
            ft.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }

        if (fragment.getView().getParent() == null)
        {
            container.addView(fragment.getView());
        }

        return fragment.getView();
    }

//    @Override
//    public int getItemPosition(Object object)
//    {
//        return PagerAdapter.POSITION_NONE;
//    }

    @Override
    public Fragment getItem(int position)
    {
        return null;
    }

    @Override
    public int getCount()
    {
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        super.destroyItem(container, position, object);
    }

//    public void setFragments(ArrayList fragments)
//    {
//        if (this.fragments != null)
//        {
//            FragmentTransaction ft = fm.beginTransaction();
//            for (Fragment f : this.fragments)
//            {
//                ft.remove(f);
//            }
//            ft.commit();
//            ft = null;
//            fm.executePendingTransactions();
//        }
//        this.fragments = fragments;
//        notifyDataSetChanged();
//    }
}
