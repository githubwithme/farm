package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.farm.R;
import com.farm.adapter.AddStd_Cmd_StepTwo_Adapter;
import com.farm.com.custominterface.FragmentCallBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepTwo extends BaseFragment
{
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    FragmentCallBack fragmentCallBack = null;
    @ViewById ListView lv_steptwo;
    String[] secondItem = new String[]{"数据异常"};
    String ss;
    AddStd_Cmd_StepTwo_Adapter addStd_cmd_stepTwo_adapter;

    @AfterViews
    void afterOncreate()
    {
        addStd_cmd_stepTwo_adapter = new AddStd_Cmd_StepTwo_Adapter(getActivity(), secondItem,fragmentCallBack);
        lv_steptwo.setAdapter(addStd_cmd_stepTwo_adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        isPrepared = true;
        lazyLoad();
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_two, container, false);
        Bundle bundle = new Bundle();
        bundle = getArguments();
        secondItem = bundle.getStringArray("SN");
        return rootView;
    }

    @Override
    protected void lazyLoad()
    {
        if (!isPrepared || !isVisible || mHasLoadedOnce)
        {
            return;
        }

    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (AddStd_Cmd) activity;
    }
}
