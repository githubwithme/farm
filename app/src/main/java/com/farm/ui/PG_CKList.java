package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.commembertab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by user on 2016/5/17.
 */
@EFragment
public class PG_CKList extends Fragment
{
    @ViewById
    TextView tv_title;
    @ViewById
    ImageButton btn_add;
    com.farm.bean.commembertab commembertab;
    private AppContext appContext;
    @Click
    void btn_add()
    {
//        Intent intent = new Intent(getActivity(), PG_AddEvent_.class);
        Intent intent = new Intent(getActivity(), PG_CK_.class);
        startActivity(intent);
    }


    @AfterViews
    void afssss()
    {

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.pg_cklistlayout, container, false);
        appContext = (AppContext) getActivity().getApplication();
//        commembertab commembertab = AppContext.getUserInfo(getActivity());
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }
}
