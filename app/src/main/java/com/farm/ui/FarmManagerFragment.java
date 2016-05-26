package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EFragment
public class FarmManagerFragment extends Fragment
{
    @ViewById
    Button btn_add;
    @ViewById
    Button btn_search;
    @ViewById
    GridView gv;

    @AfterViews
    void afterOncrete()
    {
    }

    @Click
    void ll_zl()
    {
        Intent intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_gz()
    {
        Intent intent = new Intent(getActivity(), NCZ_GZActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_mq()
    {
        Intent intent = new Intent(getActivity(), NCZ_MQActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_sj()
    {
        Intent intent = new Intent(getActivity(), NCZ_SJActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_kc()
    {
        Intent intent = new Intent(getActivity(), Ncz_wz_ll_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_xs()
    {
        Intent intent = new Intent(getActivity(), NCZ_FarmSale_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_cw()
    {
        Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_dl()
    {
        Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_sp()
    {
        Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_tj()
    {
        Intent intent = new Intent(getActivity(), NCZ_Statistics_.class);
        getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.farmmanagerfragment, container, false);
        return rootView;
    }


}
