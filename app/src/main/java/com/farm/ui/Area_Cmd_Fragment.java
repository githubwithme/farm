package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.farm.R;
import com.farm.adapter.Area_Cmd_Adapter;
import com.farm.bean.Type;
import com.farm.bean.commandtab;
import com.farm.com.custominterface.FragmentCallBack;

public class Area_Cmd_Fragment extends Fragment
{
    FragmentCallBack fragmentCallBack = null;
    commandtab commandtab;
    private ListView morelist;
    private Area_Cmd_Adapter area_cmd_adapter;
    private Type type;
    private String FN;
    private String FI;
    //    private int icon;
    String[] SI;
    String[] SN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.area_cmd_fragment, null);
        morelist = (ListView) view.findViewById(R.id.morelist);
        FN = getArguments().getString("FN");
        FI = getArguments().getString("FI");
        commandtab = getArguments().getParcelable("bean");
        SN = getArguments().getStringArray("SN");
        SI = getArguments().getStringArray("SI");

        area_cmd_adapter = new Area_Cmd_Adapter(getActivity(),fragmentCallBack, FI, FN, SI, SN,commandtab);
        morelist.setAdapter(area_cmd_adapter);
        morelist.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                area_cmd_adapter.setSelectItem(position);
                area_cmd_adapter.notifyDataSetChanged();
            }
        });
        morelist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
