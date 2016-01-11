package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.CZ_PG_JobDetail_ExpandAdapter;
import com.farm.bean.jobtab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.common_jobdetail_assess)
public class Common_JobDetail_Assess extends Activity
{
    CZ_PG_JobDetail_ExpandAdapter cz_pg_assess_expandAdapter;
    @ViewById
    RelativeLayout rl_jobname_tip;
    @ViewById
    LinearLayout ll_yl_tip;
    @ViewById
    LinearLayout ll_more;
    @ViewById
    TextView tv_importance;
    @ViewById
    TextView tv_tip_yl;
    @ViewById
    TextView tv_tip_nz;
    @ViewById
    TextView tv_jobname;
    @ViewById
    TextView tv_nz;
    @ViewById
    TextView tv_yl;
    @ViewById
    TextView tv_qx;
    @ViewById
    TextView tv_note;
    @ViewById
    Button btn_score;

    jobtab jobtab;
    @ViewById
    ImageButton btn_back;

    @Click
    void btn_score()
    {
        Intent intent = new Intent(this, CZ_PG_Assess_.class);
        intent.putExtra("bean", jobtab);
        startActivity(intent);
    }

    @Click
    void btn_back()
    {
        finish();
    }

    @AfterViews
    void afterOncreate()
    {
        showData(jobtab);
//        getCommandlist();
        // if (!jobtab.getaudioJobExecPath().equals(""))
        // {
        // downloadLuYin(AppConfig.baseurl + jobtab.getaudioJobExecPath(),
        // AppConfig.MEDIA_PATH +
        // jobtab.getaudioJobExecPath().substring(jobtab.getaudioJobExecPath().lastIndexOf("/"),
        // jobtab.getaudioJobExecPath().length()));
        // }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        jobtab = getIntent().getParcelableExtra("bean");
    }

    private void showData(jobtab jobtab)
    {
        if (jobtab.getstdJobType().equals("-1"))
        {
            ll_yl_tip.setVisibility(View.GONE);
            rl_jobname_tip.setVisibility(View.GONE);
        }
        if (jobtab.getstdJobType().equals("0"))
        {
            rl_jobname_tip.setVisibility(View.GONE);
        }
        String[] nongzi = jobtab.getnongziName().split(",");
        String[] yl = jobtab.getamount().split(";");
        String flyl = "";
        for (int i = 0; i < nongzi.length; i++)
        {
            flyl = flyl + nongzi[i] + "：" + yl[i] + "/株" + "\n";
        }

        tv_jobname.setText(jobtab.getstdJobTypeName() + "——" + jobtab.getstdJobName());
        tv_yl.setText(flyl);
        tv_note.setText(jobtab.getjobNote());


        if (jobtab.getImportance().equals("0"))
        {
            tv_importance.setText("一般");
        } else if (jobtab.getImportance().equals("1"))
        {
            tv_importance.setText("重要");
        } else if (jobtab.getImportance().equals("2"))
        {
            tv_importance.setText("非常重要");
        }


    }


}
