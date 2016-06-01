package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.commandtab_single;
import com.farm.bean.commembertab;
import com.farm.com.custominterface.FragmentCallBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2016/6/1.
 */
@EFragment
public class AddNotStd_Cmd_StepFirst extends Fragment {
    @ViewById
    TextView et_note;
    @ViewById
    Button btn_next;
    com.farm.bean.commembertab commembertab;
    FragmentCallBack fragmentCallBack = null;


    @Click
    void btn_next()
    {

        commandtab_single.getInstance().setcommNote(et_note.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 0);
        fragmentCallBack.callbackFun2(bundle);
    }
    @AfterViews
    void afterov()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addnotstd_cmd_stepfirst, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }
    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        getActivity().finish();
    }
}
