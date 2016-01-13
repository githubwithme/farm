package com.farm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.farm.ui.AddStd_Cmd_StepSix_Self;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter_AddStd_Cmd_Self extends PagerAdapter implements OnPageChangeListener
{
    private List<String> tagList = new ArrayList<String>();
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;
    private ViewPager viewPager;
    private int currentPageIndex = 0;

    private OnExtraPageChangeListener onExtraPageChangeListener;

    public ViewPagerAdapter_AddStd_Cmd_Self(FragmentManager fragmentManager, ViewPager viewPager, List<Fragment> fragments)
    {
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
        this.viewPager.setOnPageChangeListener(this);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o)
    {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(fragments.get(position).getView());
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

    public int getCurrentPageIndex()
    {
        return currentPageIndex;
    }

    public OnExtraPageChangeListener getOnExtraPageChangeListener()
    {
        return onExtraPageChangeListener;
    }

    public void setOnExtraPageChangeListener(OnExtraPageChangeListener onExtraPageChangeListener)
    {
        this.onExtraPageChangeListener = onExtraPageChangeListener;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2)
    {
        if (null != onExtraPageChangeListener)
        {
            onExtraPageChangeListener.onExtraPageScrolled(i, v, i2);
        }
    }

    @Override
    public void onPageSelected(int i)
    {
        fragments.get(currentPageIndex).onPause();
        // fragments.get(currentPageIndex).onStop();
        if (fragments.get(i).isAdded())
        {
            // fragments.get(i).onStart();
            fragments.get(i).onResume();
        }
        currentPageIndex = i;

        if (null != onExtraPageChangeListener)
        {
            onExtraPageChangeListener.onExtraPageSelected(i);
        }

    }

    @Override
    public void onPageScrollStateChanged(int i)
    {
        if (null != onExtraPageChangeListener)
        {
            onExtraPageChangeListener.onExtraPageScrollStateChanged(i);
        }
    }

    public static class OnExtraPageChangeListener
    {
        public void onExtraPageScrolled(int i, float v, int i2)
        {
        }

        public void onExtraPageSelected(int i)
        {
        }

        public void onExtraPageScrollStateChanged(int i)
        {
        }
    }

    public void updateData(int item)
    {
        Fragment fragment = fragmentManager.findFragmentByTag(tagList.get(item));
        if (fragment != null)
        {
            switch (item)
            {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    ((AddStd_Cmd_StepSix_Self) fragment).update();
                    break;

                default:
                    break;
            }
        }
    }
}
