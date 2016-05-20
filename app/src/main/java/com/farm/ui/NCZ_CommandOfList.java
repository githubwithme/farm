package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by user on 2016/5/19.
 */
@EFragment
public class NCZ_CommandOfList extends Fragment {
    com.farm.bean.commembertab commembertab;
    Fragment mContent = new Fragment();
    //    PG_EventReported pg_eventReported;
//    PG_EventProcessed pg_eventProcessed;
    NCZ_Commanding ncz_commanding;
    NCZ_Commanded ncz_commanded;
    @ViewById
    TextView commanding;
    @ViewById
    TextView commanded;
    String workuserid;

    @Click
    void commanding() {
        setBackground(0);
        switchContent(mContent, ncz_commanding);
    }

    @Click
    void commanded() {
        setBackground(1);
        switchContent(mContent, ncz_commanded);
    }

    @AfterViews
    void afterOncreate() {
        setBackground(0);
        switchContent(mContent, ncz_commanding);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.commadoflist, container, false);
//        pg_eventReported = new PG_EventReported_();
//        pg_eventProcessed = new PG_EventProcessed_();
//        workuserid = getArguments().getString("workuserid");
//        Bundle bundle = new Bundle();
//        bundle.putString("workuserid", workuserid);
//        ncz_commanding.setArguments(bundle);
//        ncz_commanded.setArguments(bundle);
        ncz_commanding = new NCZ_Commanding_();
        ncz_commanded = new NCZ_Commanded_();
        return rootView;
    }

    private void setBackground(int pos) {
        commanding.setSelected(false);
        commanded.setSelected(false);

        commanding.setBackgroundResource(R.color.white);
        commanded.setBackgroundResource(R.color.white);

        commanding.setTextColor(getResources().getColor(R.color.menu_textcolor));
        commanded.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos) {
            case 0:
                commanding.setSelected(false);
                commanding.setTextColor(getResources().getColor(R.color.bg_blue));
                commanding.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                commanded.setSelected(false);
                commanded.setTextColor(getResources().getColor(R.color.bg_blue));
                commanded.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }

    public void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(from).add(R.id.wt_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

}
