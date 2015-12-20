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
import com.farm.bean.commandtab;
import com.farm.com.custominterface.FragmentCallBack;

public class CmdList_Cmd_Fragment extends Fragment
{
    FragmentCallBack fragmentCallBack = null;
    commandtab commandtab;
    private GridView gridView;
    private AddStd_cmd_steponestemp_GridViewAdapter adapter;
    private Type type;
    private String typename;
    private String FI;
    //    private int icon;
    String[] SI;
    String[] sn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.cmdlist_cmd_fragment, null);
        gridView = (GridView) view.findViewById(R.id.listView);
        typename = getArguments().getString("FN");
        FI = getArguments().getString("FI");
        commandtab = getArguments().getParcelable("bean");
        sn = getArguments().getStringArray("SN");
        SI = getArguments().getStringArray("SI");

        ((TextView) view.findViewById(R.id.toptype)).setText(typename);
        adapter = new AddStd_cmd_steponestemp_GridViewAdapter(getActivity(), sn);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3)
            {
//                commandtab.setstdJobType(FI);
//                commandtab.setstdJobTypeName(typename);
//                commandtab.setstdJobId(SI[pos]);
//                commandtab.setstdJobName(sn[pos]);
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
