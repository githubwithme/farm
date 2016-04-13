package com.farm.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.ReportedBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by user on 2016/4/12.
 */
@EFragment
public class NCZ_EventDetail extends Fragment
{
    ReportedBean reportedBean;

    @ViewById
    TextView tv_reported;
    @ViewById
    TextView tv_time;
    @ViewById
    TextView tv_type;
    @ViewById
    TextView et_sjms;


    @AfterViews
    void aftercreate() {
        tv_reported.setText(reportedBean.getReportor());
        tv_time.setText(reportedBean.getReporTime());
        tv_type.setText(reportedBean.getEventType());
//       et_sjms.setText(reportedBean.getEventContent());
        et_sjms.setText(reportedBean.getEventContent());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_detaillayout, container, false);
//        appContext = (AppContext) getActivity().getApplication();
//        commembertab commembertab = AppContext.getUserInfo(getActivity());
//        commembertab = AppContext.getUserInfo(getActivity());
//        goods=getArguments().getParcelable("goods");
        reportedBean = getArguments().getParcelable("reportedBean");
        return rootView;
    }
}
