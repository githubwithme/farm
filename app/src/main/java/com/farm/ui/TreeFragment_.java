//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.2.
//


package com.farm.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.widget.swipelistview.ExpandAniLinearLayout;

import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class TreeFragment_
    extends com.farm.ui.TreeFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    @Override
    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        return contentView_;
    }

    @Override
    public void onDestroyView() {
        contentView_ = null;
        super.onDestroyView();
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static TreeFragment_.FragmentBuilder_ builder() {
        return new TreeFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        lv_tree = ((ListView) hasViews.findViewById(com.farm.R.id.lv_tree));
        swipe_list_ani = ((ExpandAniLinearLayout) hasViews.findViewById(com.farm.R.id.swipe_list_ani));
        tv_tip = ((TextView) hasViews.findViewById(com.farm.R.id.tv_tip));
        afterOncreate();
    }

    public static class FragmentBuilder_
        extends FragmentBuilder<TreeFragment_.FragmentBuilder_, com.farm.ui.TreeFragment>
    {


        @Override
        public com.farm.ui.TreeFragment build() {
            TreeFragment_ fragment_ = new TreeFragment_();
            fragment_.setArguments(args);
            return fragment_;
        }

    }

}
