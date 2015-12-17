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
import com.farm.adapter.AddStd_Cmd_goodslistdapter;
import com.farm.bean.goodslisttab;
import com.farm.com.custominterface.FragmentCallBack;

import java.util.ArrayList;
import java.util.List;

public class GoodList_Cmd_Fragment extends Fragment
{
    FragmentCallBack fragmentCallBack = null;
    private GridView gridView;
    private AddStd_Cmd_goodslistdapter adapter;
    private String fn;
    private String sn;
    private int fi;
    private int si;
    List<goodslisttab> list_goods = new ArrayList<goodslisttab>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.goodlist_cmd_fragment, null);
        gridView = (GridView) view.findViewById(R.id.listView);
        fi = getArguments().getInt("fi");
        si = getArguments().getInt("si");
        fn = getArguments().getString("FN");
        sn = getArguments().getString("SN");
        list_goods = getArguments().getParcelableArrayList("beanlist");

        ((TextView) view.findViewById(R.id.toptype)).setText(fn + "-" + sn);
        adapter = new AddStd_Cmd_goodslistdapter(getActivity(), list_goods);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("INDEX", 1);
                fragmentCallBack.callbackFun2(bundle);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentCallBack = (AddStd_Cmd) activity;
    }
}
