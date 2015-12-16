package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.AddStd_cmd_steponestemp_GridViewAdapter;
import com.farm.bean.Type;
import com.farm.com.custominterface.FragmentCallBack;

public class CmdList_Cmd_Fragment extends Fragment
{
    FragmentCallBack fragmentCallBack = null;
    //    private ArrayList<Type> list;
    private GridView gridView;
    private AddStd_cmd_steponestemp_GridViewAdapter adapter;
    private Type type;
    private String typename;
    //    private int icon;
    String[] sn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.cmdlist_cmd_fragment, null);
        gridView = (GridView) view.findViewById(R.id.listView);
//        int index = getArguments().getInt("index");
        typename = getArguments().getString("FN");
        sn = getArguments().getStringArray("SN");
//        icon = Model.iconList[index];

        ((TextView) view.findViewById(R.id.toptype)).setText(typename);
//        GetTypeList();
        adapter = new AddStd_cmd_steponestemp_GridViewAdapter(getActivity(), sn);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("INDEX", 0);
                fragmentCallBack.callbackFun2(bundle);
            }
        });

        return view;
    }

//    private void GetTypeList()
//    {
//        list = new ArrayList<Type>();
//        for (int i = 1; i < 23; i++)
//        {
//            type = new Type(i, sn[i], icon);
//            list.add(type);
//        }
//    }

    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentCallBack = (AddStd_Cmd) activity;
    }
}
