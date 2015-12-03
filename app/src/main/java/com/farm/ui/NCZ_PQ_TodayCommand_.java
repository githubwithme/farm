//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.2.
//


package com.farm.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R.id;
import com.farm.R.layout;
import com.farm.widget.PullToRefreshListView;

import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

@SuppressLint({
    "NewApi"
})
public final class NCZ_PQ_TodayCommand_
    extends NCZ_PQ_TodayCommand
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.ncz_pq_todaycommand);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static NCZ_PQ_TodayCommand_.IntentBuilder_ intent(Context context) {
        return new NCZ_PQ_TodayCommand_.IntentBuilder_(context);
    }

    public static NCZ_PQ_TodayCommand_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new NCZ_PQ_TodayCommand_.IntentBuilder_(fragment);
    }

    public static NCZ_PQ_TodayCommand_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new NCZ_PQ_TodayCommand_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        btn_add = ((ImageButton) hasViews.findViewById(id.btn_add));
        line = hasViews.findViewById(id.line);
        tv_title = ((TextView) hasViews.findViewById(id.tv_title));
        frame_listview_news = ((PullToRefreshListView) hasViews.findViewById(id.frame_listview_news));
        if (btn_add!= null) {
            btn_add.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    NCZ_PQ_TodayCommand_.this.btn_add();
                }

            }
            );
        }
        afterOncreate();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<NCZ_PQ_TodayCommand_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, NCZ_PQ_TodayCommand_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), NCZ_PQ_TodayCommand_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), NCZ_PQ_TodayCommand_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent, requestCode);
                } else {
                    super.startForResult(requestCode);
                }
            }
        }

    }

}
