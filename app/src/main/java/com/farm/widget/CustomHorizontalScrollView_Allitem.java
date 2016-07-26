package com.farm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.farm.ui.PG_SaleActivity;

/**
 * Created by hasee on 2016/7/18.
 */
public class CustomHorizontalScrollView_Allitem extends HorizontalScrollView
{
    CustomOntouch customOntouch;

    public CustomHorizontalScrollView_Allitem(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public CustomHorizontalScrollView_Allitem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomHorizontalScrollView_Allitem(Context context)
    {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        // 进行触摸赋值
        customOntouch.customOnTouchEvent(this);
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        // 当当前的CHSCrollView被触摸时，滑动其它
        if (customOntouch.getmTouchView() == this)
        {
            customOntouch.customOnScrollChanged(l, t, oldl, oldt);
        } else
        {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }
    public void setCuttomOntouch(CustomOntouch customOntouch)
    {
        this.customOntouch=customOntouch;
    }

    public interface CustomOntouch
    {
        void customOnTouchEvent(HorizontalScrollView horizontalScrollView);
        void customOnScrollChanged(int l, int t, int oldl, int oldt);
        HorizontalScrollView getmTouchView();
    }
}
