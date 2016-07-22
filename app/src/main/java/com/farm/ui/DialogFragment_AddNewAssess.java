package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 图片左右滑动
 *
 * @author hmj
 */
@SuppressLint("NewApi")
@EFragment
public class DialogFragment_AddNewAssess extends DialogFragment
{
    @ViewById
    ImageView iv_addphotos;

    @AfterViews
    void AfterViews()
    {
    }

    @Click
    void btn_cancle()
    {
        this.dismiss();
    }

    @Click
    void btn_sure()
    {
        this.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.customdialog_addnewassess, container, false);
        return rootView;
    }


}