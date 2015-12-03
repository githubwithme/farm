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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.farm.R.id;
import com.farm.R.layout;
import com.farm.widget.CircleImageView;

import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

@SuppressLint({
    "NewApi"
})
public final class Login_
    extends Login
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.login);
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

    public static Login_.IntentBuilder_ intent(Context context) {
        return new Login_.IntentBuilder_(context);
    }

    public static Login_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new Login_.IntentBuilder_(fragment);
    }

    public static Login_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new Login_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        cb_autologin = ((CheckBox) hasViews.findViewById(id.cb_autologin));
        et_name = ((EditText) hasViews.findViewById(id.et_name));
        pb_logining = ((ProgressBar) hasViews.findViewById(id.pb_logining));
        line = hasViews.findViewById(id.line);
        ll_login = ((LinearLayout) hasViews.findViewById(id.ll_login));
        et_psw = ((EditText) hasViews.findViewById(id.et_psw));
        iv_down = ((ImageView) hasViews.findViewById(id.iv_down));
        btn_login = ((CircleImageView) hasViews.findViewById(id.btn_login));
        if (btn_login!= null) {
            btn_login.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Login_.this.btn_login();
                }

            }
            );
        }
        if (iv_down!= null) {
            iv_down.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    Login_.this.iv_down();
                }

            }
            );
        }
        afterOncreate();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<Login_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, Login_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), Login_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), Login_.class);
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
