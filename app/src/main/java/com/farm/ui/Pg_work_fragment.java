package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.farm.R;
import com.farm.adapter.AddStd_cmd_steponestemp_GridViewAdapter;
import com.farm.bean.Type;
import com.farm.bean.commandtab_single;
import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;

import org.androidannotations.annotations.EFragment;

/**
 * Created by hasee on 2016/6/22.
 */
@EFragment
public class Pg_work_fragment extends Fragment
{
    FragmentCallBack fragmentCallBack = null;
    private GridView gridView;
    private AddStd_cmd_steponestemp_GridViewAdapter adapter;
    private Type type;
    private String typename;
    private String FI;
    private String ZY;
    String[] SI;
    String[] sn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.cmdlist_cmd_fragment, null);
        gridView = (GridView) view.findViewById(R.id.listView);
//        gridView.setAdapter(null);

        typename = getArguments().getString("FN");
        FI = getArguments().getString("FI");
        sn = getArguments().getStringArray("SN");
        SI = getArguments().getStringArray("SI");
        ZY = getArguments().getString("ZY");


        adapter = new AddStd_cmd_steponestemp_GridViewAdapter(getActivity(), sn);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3)
            {
                commandtab_single commandtab_single = com.farm.bean.commandtab_single.getInstance();
                commandtab_single.setstdJobType(FI);
                commandtab_single.setstdJobTypeName(typename);
                commandtab_single.setstdJobId(SI[pos]);
                commandtab_single.setstdJobName(sn[pos]+"("+ZY+")");
                String a=FI;
                String b=typename;
                String c=SI[pos];
                String d=sn[pos]+"("+ZY+")";
                String e="";


/*                SqliteDb.deleteAllSelectCmdArea(getActivity(), goodslisttab.class);
                SqliteDb.deleteAllSelectCmdArea(getActivity(), goodslisttab_flsl.class);

                Bundle bundle = new Bundle();
                bundle.putInt("INDEX", 0);
                fragmentCallBack.callbackFun2(bundle);

                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "已经选择:" + typename + "-" + sn[pos]);*/
//                fragmentCallBack.callbackFun_setText(bundle1);
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
//        fragmentCallBack = (FragmentCallBack) activity;
    }
}
