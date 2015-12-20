package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.farm.R;
import com.farm.com.custominterface.FragmentCallBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepFive extends Fragment
{
    @ViewById
    TextView tv_importance;
    @ViewById
    TextView tv_workday;
    @ViewById
    TextView tv_timelimit;
    @ViewById
    EditText et_note;
    com.farm.bean.commandtab commandtab;
    FragmentCallBack fragmentCallBack = null;

    @Click
    void btn_next()
    {
        commandtab.setimportance("");
        commandtab.setcommComDate(tv_timelimit.getText().toString());//期限
        commandtab.setcommDays(tv_workday.getText().toString());
        commandtab.setcommNote(et_note.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 3);
        fragmentCallBack.callbackFun2(bundle);
    }

    @AfterViews
    void afterOncreate()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_five, container, false);
        commandtab = getArguments().getParcelable("bean");
        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentCallBack = (AddStd_Cmd) activity;
    }
}
