package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * 图片左右滑动
 *
 * @author hmj
 */
@SuppressLint("NewApi")
@EFragment
public class DialogFragment_WaitTip extends DialogFragment
{
    @AfterViews
    void AfterViews()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialogfragment_waittip, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        return rootView;
    }


}