package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.farm.R;
import com.farm.adapter.AddStd_Cmd_StepOne_Adapter;
import com.farm.bean.Dictionary;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepOne extends Fragment
{
    @ViewById
    ListView lv;
    AddStd_Cmd_StepOne_Adapter addStd_cmd_stepOne_adapter;
    private List<Dictionary> listData = new ArrayList<Dictionary>();

    @AfterViews
    void afterOncreate()
    {
        addStd_cmd_stepOne_adapter = new AddStd_Cmd_StepOne_Adapter(getActivity(), listData);
        lv.setAdapter(addStd_cmd_stepOne_adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_one, container, false);
        return rootView;
    }
}
