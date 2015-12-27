package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.farm.R;
import com.farm.com.custominterface.FragmentCallBack_AddPlantObservation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddPlantObservation_stepfour extends Fragment
{
    FragmentCallBack_AddPlantObservation fragmentCallBack;
    @ViewById
    EditText et_jj;
    @ViewById
    EditText et_y;
    @ViewById
    EditText et_gx;


    @Click
    void btn_next()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 0);
        fragmentCallBack.callbackFun2(bundle);
    }

    @AfterViews
    void afterOncreate()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation_stepfour, container, false);
        return rootView;
    }

    public String getJJBB()
    {
        return et_jj.getText().toString();
    }

    public String getYBX()
    {
        return et_y.getText().toString();
    }

    public String getGXBX()
    {
        return et_gx.getText().toString();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack_AddPlantObservation) activity;
    }
}
