package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepFive extends Fragment
{
    @AfterViews
    void afterOncreate()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_five, container, false);
        return rootView;
    }
}
