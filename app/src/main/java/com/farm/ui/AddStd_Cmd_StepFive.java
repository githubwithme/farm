package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.bean.Dictionary;
import com.farm.bean.commandtab_single;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDatepicker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepFive extends Fragment
{
    CustomDialog_ListView customDialog_listView;
    Dictionary dic_comm;
    String importance_id="0";
    String workday="";
    String importance="一般";

    @Click
    void tv_importance()
    {
        List<String> list_id = new ArrayList<String>();
        list_id.add("0");
        list_id.add("1");
        list_id.add("2");
        List<String> list_data = new ArrayList<String>();
        list_data.add("一般");
        list_data.add("重要");
        list_data.add("非常重要");
        showDialog_Importance(list_id, list_data);
    }

    @Click
    void tv_workday()
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        showDialog_workday(list);
    }

    @Click
    void tv_timelimit()
    {
        MyDatepicker myDatepicker = new MyDatepicker(getActivity(), tv_timelimit);
        myDatepicker.getDialog().show();
    }

    @ViewById
    TextView tv_importance;
    @ViewById
    TextView tv_workday;
    @ViewById
    TextView tv_timelimit;
    @ViewById
    EditText et_note;
    FragmentCallBack fragmentCallBack = null;

    @Click
    void btn_next()
    {
        commandtab_single.getInstance().setimportance(importance_id);
        commandtab_single.getInstance().setImportancetype(importance);
        commandtab_single.getInstance().setcommComDate(tv_timelimit.getText().toString());//期限
        commandtab_single.getInstance().setcommDays(tv_workday.getText().toString());
        commandtab_single.getInstance().setcommNote(et_note.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 3);
        fragmentCallBack.callbackFun2(bundle);
    }

    @AfterViews
    void afterOncreate()
    {
        tv_importance.setText(importance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_five, container, false);
        return rootView;
    }


    public void showDialog_workday(List<String> list)
    {
        View dialog_layout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(getActivity(), R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                workday = bundle.getString("name");
                tv_workday.setText(workday);
            }
        });
        customDialog_listView.show();
    }

    public void showDialog_Importance(List<String> list_id, List<String> listdata)
    {
        View dialog_layout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(getActivity(), R.style.MyDialog, dialog_layout, listdata, list_id, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {

                importance_id = bundle.getString("id");
                importance = bundle.getString("name");
                tv_importance.setText(importance);
            }
        });
        customDialog_listView.show();
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
