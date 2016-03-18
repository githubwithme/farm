package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by ${hmj} on 2016/3/18.
 */
@EFragment
public class CZ_MakeMap_OperateLayer extends Fragment
{
    @AfterViews
    void afterOncreate()
    {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.cz_makemap_showlayer, container, false);
        return rootView;
    }
}
