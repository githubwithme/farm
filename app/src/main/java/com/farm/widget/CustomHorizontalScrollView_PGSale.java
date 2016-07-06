package com.farm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.farm.ui.PG_SaleActivity;

/**
 * Created by ${hmj} on 2016/6/27.
 */
public class CustomHorizontalScrollView_PGSale extends HorizontalScrollView
{

    PG_SaleActivity activity;

    public CustomHorizontalScrollView_PGSale(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        activity = (PG_SaleActivity) context;
    }

    public CustomHorizontalScrollView_PGSale(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        activity = (PG_SaleActivity) context;
    }

    public CustomHorizontalScrollView_PGSale(Context context)
    {
        super(context);
        activity = (PG_SaleActivity) context;
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
