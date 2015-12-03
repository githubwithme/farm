//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.2.
//


package com.farm.ui;

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

public final class NCZ_CommandDetailList_
    extends NCZ_CommandDetailList
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.commanddetaillist);
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

    public static NCZ_CommandDetailList_.IntentBuilder_ intent(Context context) {
        return new NCZ_CommandDetailList_.IntentBuilder_(context);
    }

    public static NCZ_CommandDetailList_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new NCZ_CommandDetailList_.IntentBuilder_(fragment);
    }

    public static NCZ_CommandDetailList_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new NCZ_CommandDetailList_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        tv_yl = ((TextView) hasViews.findViewById(id.tv_yl));
        tv_nz_tip = ((TextView) hasViews.findViewById(id.tv_nz_tip));
        tv_nz = ((TextView) hasViews.findViewById(id.tv_nz));
        frame_listview_news = ((PullToRefreshListView) hasViews.findViewById(id.frame_listview_news));
        tv_note = ((TextView) hasViews.findViewById(id.tv_note));
        tv_yl_tip = ((TextView) hasViews.findViewById(id.tv_yl_tip));
        tv_cmdname = ((TextView) hasViews.findViewById(id.tv_cmdname));
        tv_qx = ((TextView) hasViews.findViewById(id.tv_qx));
        btn_back = ((ImageButton) hasViews.findViewById(id.btn_back));
        line = hasViews.findViewById(id.line);
        if (btn_back!= null) {
            btn_back.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    NCZ_CommandDetailList_.this.btn_back();
                }

            }
            );
        }
        afterOncreate();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<NCZ_CommandDetailList_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, NCZ_CommandDetailList_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), NCZ_CommandDetailList_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), NCZ_CommandDetailList_.class);
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
