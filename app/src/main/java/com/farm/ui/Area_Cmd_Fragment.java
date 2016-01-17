package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.farm.R;
import com.farm.adapter.Area_Cmd_Adapter;
import com.farm.adapter.GoodsSelected_Adapter;
import com.farm.bean.Type;
import com.farm.bean.goodslisttab;
import com.farm.com.custominterface.FragmentCallBack;

import java.util.List;

public class Area_Cmd_Fragment extends Fragment
{
    List<goodslisttab> list;
    FragmentCallBack fragmentCallBack = null;
    private ListView morelist;
    private Area_Cmd_Adapter area_cmd_adapter;
    private GoodsSelected_Adapter goodsadapter;
    private Type type;
    private String FN;
    private String FI;
    //    private int icon;
    List<String> SI;
    List<String> SN;
    List<String> TN;
//    TextView toptype;
//    TextView tv_dw;
//    TextView tv_number;
//    TextView tv_gg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.area_cmd_fragment, null);
        morelist = (ListView) view.findViewById(R.id.morelist);
//        toptype = (TextView) view.findViewById(R.id.toptype);
//        tv_number = (TextView) view.findViewById(R.id.tv_number);
//        tv_dw = (TextView) view.findViewById(R.id.tv_dw);
//        tv_gg = (TextView) view.findViewById(R.id.tv_gg);
        list = getArguments().getParcelableArrayList("GOODS");
        FN = getArguments().getString("FN");
        FI = getArguments().getString("FI");
        SI = getArguments().getStringArrayList("SI");
        SN = getArguments().getStringArrayList("SN");
        TN = getArguments().getStringArrayList("TN");


        area_cmd_adapter = new Area_Cmd_Adapter(getActivity(), fragmentCallBack, FI, FN, SI, SN,TN,list);
        morelist.setAdapter(area_cmd_adapter);
        return view;
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }

}
