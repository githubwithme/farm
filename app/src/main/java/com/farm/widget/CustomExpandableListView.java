package com.farm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by ${hmj} on 2015/12/30.
 */
public class CustomExpandableListView extends ExpandableListView
{

    public CustomExpandableListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setOnGroupCollapseListener(OnGroupCollapseListener onGroupCollapseListener)
    {
        super.setOnGroupCollapseListener(onGroupCollapseListener);
    }

    @Override
    public void setOnGroupExpandListener(OnGroupExpandListener onGroupExpandListener)
    {
        super.setOnGroupExpandListener(onGroupExpandListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

