package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.commandtab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/1/20.
 */
@EFragment
public class Fragment_CommandDetail extends Fragment
{
    commandtab commandtab;
    @ViewById
    LinearLayout ll_flyl;
    @ViewById
    TextView tv_zyts;
    @ViewById
    TextView tv_cmdname;
    @ViewById
    TextView tv_yl;
    @ViewById
    TextView tv_note;
    @ViewById
    TextView tv_qx;

    @AfterViews
    void afterOncreate()
    {
        showData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_commanddetail, container, false);
        commandtab = getArguments().getParcelable("bean");
        return rootView;
    }

    private void showData()
    {
        String[] nongzi = commandtab.getnongziName().split(",");
        String flyl = "";
        for (int i = 0; i < nongzi.length; i++)
        {
            flyl = flyl + nongzi[i] + "  ;  ";
        }
        tv_note.setText(commandtab.getcommNote());
        tv_yl.setText(flyl);
        tv_zyts.setText(commandtab.getcommDays() + "天");
        tv_qx.setText(commandtab.getcommComDate());
        if (commandtab.getstdJobType().equals("-1"))
        {
            ll_flyl.setVisibility(View.GONE);
            if (commandtab.getcommNote().equals(""))
            {
                tv_cmdname.setText("暂无说明");
            } else
            {
                tv_cmdname.setText(commandtab.getcommNote());
            }
        } else if (commandtab.getstdJobType().equals("0"))
        {
            if (commandtab.getcommNote().equals(""))
            {
                tv_cmdname.setText("暂无说明");
            } else
            {
                tv_cmdname.setText(commandtab.getcommNote());
            }
        } else
        {
            tv_cmdname.setText(commandtab.getstdJobTypeName() + "——" + commandtab.getstdJobName());
        }
    }
}
