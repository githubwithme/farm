package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.commandtab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment
public class CommandDetail_Show_DetailFragment extends Fragment
{
    commandtab commandtab;
    @ViewById
    TextView tv_zyts;
    @ViewById
    TextView tv_importance;
    @ViewById
    TextView tv_jobname;
    @ViewById
    TextView tv_yl_tip;
    @ViewById
    TextView tv_yl;
    @ViewById
    TextView tv_qx;
    @ViewById
    TextView tv_note;
    @ViewById
    TextView tv_from;
    @ViewById
    LinearLayout ll_yl_tip;
    @ViewById
    RelativeLayout rl_jobname_tip;


    @AfterViews
    void afterOncreate()
    {
       showData(commandtab);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.commanddetail_show_detail, null);
        commandtab = getArguments().getParcelable("bean");
        return view;
    }

    private void showData(commandtab commandtab)
    {
        if (commandtab.getstdJobType().equals("-1"))
        {
            ll_yl_tip.setVisibility(View.GONE);
            rl_jobname_tip.setVisibility(View.GONE);
        }
        if (commandtab.getstdJobType().equals("0"))
        {
            rl_jobname_tip.setVisibility(View.GONE);
        }
        String[] nongzi = commandtab.getnongziName().split(",");
//        String[] yl = commandtab.getamount().split(";");
        String flyl = "";
        for (int i = 0; i < nongzi.length; i++)
        {
            flyl = flyl + nongzi[i] + "\n";
        }

        tv_yl.setText(flyl);
        tv_jobname.setText(commandtab.getstdJobTypeName() + "-" + commandtab.getstdJobName());

        tv_qx.setText(commandtab.getcommComDate());
        tv_zyts.setText(commandtab.getcommDays());
        tv_note.setText(commandtab.getcommNote());
        tv_from.setText(commandtab.getcommFromName());
        if (commandtab.getimportance().equals("0"))
        {
            tv_importance.setText("一般");
        } else if (commandtab.getimportance().equals("1"))
        {
            tv_importance.setText("重要");
        } else if (commandtab.getimportance().equals("2"))
        {
            tv_importance.setText("非常重要");
        }

    }



}
