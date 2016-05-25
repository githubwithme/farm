package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/25.
 */
@EFragment
public class NCZ_AllCostFragment extends Fragment
{
    @ViewById
    ImageButton btn_add;

    @Click
    void btn_add()
    {
        Intent intent = new Intent(getActivity(), AddSpecialCost_.class);
        startActivity(intent);
    }

    @AfterViews
    void afterOncreate()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_allcostfragment, container, false);
        return rootView;
    }
}
