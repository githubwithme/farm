package com.farm.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.guide.ViewPager;

import java.util.List;

/**
 * Created by user on 2016/2/24.
 */

public class Ncz_wzgl_CVP extends PagerAdapter{
    private List<View> viewLists;
    public  Ncz_wzgl_CVP(List<View> list){
     this.viewLists=list;
    }
    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public void destroyItem(View view, int position, Object object)
    {
        ((ViewPager) view).removeView(viewLists.get(position));
    }

    //实例化Item
    @Override
    public Object instantiateItem(View view, int position)
    {
        ((ViewPager) view).addView(viewLists.get(position), 0);
        return viewLists.get(position);
    }
}
