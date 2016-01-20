package com.farm.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ${hmj} on 2016/1/20.
 */
public class CustomViewPager extends ViewPager
{
    boolean isScrollable=false;
    public CustomViewPager(Context context)
    {
        super(context);
    }
    public CustomViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setIsScrollable(boolean isScrollable)
    {
        this.isScrollable = isScrollable;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }
}
