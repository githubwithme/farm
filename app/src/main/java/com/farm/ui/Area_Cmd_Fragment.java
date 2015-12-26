package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.Area_Cmd_Adapter;
import com.farm.bean.Type;
import com.farm.bean.commandtab_single;
import com.farm.com.custominterface.FragmentCallBack;

public class Area_Cmd_Fragment extends Fragment
{
    FragmentCallBack fragmentCallBack = null;
    private ListView morelist;
    private Area_Cmd_Adapter area_cmd_adapter;
    private Type type;
    private String GOODSNUMBER;
    private String FN;
    private String FI;
    //    private int icon;
    String[] SI;
    String[] SN;
    TextView toptype;
    TextView tv_dw;
    TextView tv_number;
    TextView tv_gg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.area_cmd_fragment, null);
        morelist = (ListView) view.findViewById(R.id.morelist);
        toptype = (TextView) view.findViewById(R.id.toptype);
        tv_number = (TextView) view.findViewById(R.id.tv_number);
        tv_dw = (TextView) view.findViewById(R.id.tv_dw);
        tv_gg = (TextView) view.findViewById(R.id.tv_gg);
        GOODSNUMBER = getArguments().getString("GOODSNUMBER");
        FN = getArguments().getString("FN");
        FI = getArguments().getString("FI");
        SN = getArguments().getStringArray("SN");
        SI = getArguments().getStringArray("SI");

        toptype.setText(FN + " - " + "“" + commandtab_single.getInstance().getnongziName() + "”");
        tv_number.setText(GOODSNUMBER);
        tv_dw.setText(commandtab_single.getInstance().getNongzidw());
        tv_gg.setText(commandtab_single.getInstance().getNongzigg());
        area_cmd_adapter = new Area_Cmd_Adapter(getActivity(), fragmentCallBack, FI, FN, SI, SN,GOODSNUMBER);
        morelist.setAdapter(area_cmd_adapter);
        morelist.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                deleteSelectRecords(BELONG, firstItemName, secondItemName.get(position));
//                saveSelectRecords(BELONG, firstItemId, firstItemName, secondItemid.get(position), secondItemName.get(position));
//                area_cmd_adapter.setSelectItem(position);
//                area_cmd_adapter.notifyDataSetChanged();
            }
        });
        morelist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        return view;
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }
}
