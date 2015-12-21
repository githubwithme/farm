package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.commandtab_single;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepSix extends Fragment
{
    @ViewById
    TextView tv_importance;
    @ViewById
    TextView tv_selectcmd;
    @ViewById
    TextView tv_workday;
    @ViewById
    TextView tv_nz;
    @ViewById
    TextView tv_timelimit;
    @ViewById
    TextView tv_area;
    @ViewById
    TextView tv_note;

    com.farm.bean.commandtab commandtab;

    @AfterViews
    void afterOncreate()
    {
        showData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_six, container, false);
        return rootView;
    }

    public void showData()
    {
        commandtab_single commandtab_single= com.farm.bean.commandtab_single.getInstance();
        tv_importance.setText(commandtab_single.getimportance());
        tv_selectcmd.setText(commandtab_single.getstdJobName()+"-"+commandtab_single.getstdJobTypeName());
        tv_workday.setText(commandtab_single.getcommDays());
        tv_note.setText(commandtab_single.getcommNote());
        tv_timelimit.setText(commandtab_single.getcommComDate());
    }

}
