package com.farm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by hasee on 2016/7/15.
 */
public class CustomHorizontalScrollView_JSDDetail extends HorizontalScrollView
{
    JSD_Detail activity;
    public CustomHorizontalScrollView_JSDDetail(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        activity = (JSD_Detail) context;
    }

    public CustomHorizontalScrollView_JSDDetail(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        activity = (JSD_Detail) context;
    }

    public CustomHorizontalScrollView_JSDDetail(Context context)
    {
        super(context);
        activity = (JSD_Detail) context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        // 进行触摸赋值
        activity.mTouchView = this;
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        // 当当前的CHSCrollView被触摸时，滑动其它
        if (activity.mTouchView == this)
        {
            activity.onScrollChanged(l, t, oldl, oldt);
        } else
        {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
