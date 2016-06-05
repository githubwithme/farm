package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.CZ_PG_JobDetail_ExpandAdapter;
import com.farm.bean.jobtab;
import com.farm.widget.CustomExpandableListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.common_jobdetail_show)
public class Common_JobDetail_Show extends Activity
{
    CZ_PG_JobDetail_ExpandAdapter cz_pg_assess_expandAdapter;
    @ViewById
    CustomExpandableListView expandableListView;
    @ViewById
    TextView tv_importance;
    @ViewById
    RelativeLayout rl_jobname_tip;
    @ViewById
    LinearLayout ll_yl_tip;
    @ViewById
    TextView tv_tip_yl;
    @ViewById
    TextView tv_tip_nz;
    @ViewById
    TextView tv_jobname;
    @ViewById
    TextView tv_yl;
    @ViewById
    TextView tv_date_pf;
    @ViewById
    TextView tv_qx;
    @ViewById
    TextView tv_pf;
    @ViewById
    TextView tv_pfnr;
    @ViewById
    TextView tv_fkjg;
    @ViewById
    TextView tv_pfsm;
    @ViewById
    TextView tv_note;
    @ViewById
    TextView tv_pfnote;
    @ViewById
    TextView tv_title;

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
        tv_title.setText("工作详情");
        jobtab = getIntent().getParcelableExtra("bean");
        showData(jobtab);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
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
//        String[] amountdw = jobtab.getAmountDW().split("[.]");
        String flyl = "";
        if (jobtab.getAmountDW().equals(""))
        {
            for (int i = 0; i < nongzi.length; i++)
            {
                flyl = flyl + nongzi[i] + ":" + yl[i] + ";";
            }
        } else
        {
            String[] daiwei = jobtab.getAmountDW().split("[.]");
            for (int i = 0; i < nongzi.length; i++)
            {
                flyl = flyl + nongzi[i] + ":" + yl[i] + daiwei[i] + ";";
            }
        }
/*        for (int i = 0; i < nongzi.length; i++)
        {
            flyl = flyl + nongzi[i] + "：" + yl[i] +"\n";
//            flyl = flyl + nongzi[i] + "：" + yl[i] + amountdw[i] + "\n";
//            flyl = flyl + nongzi[i] + "：" + yl[i] + "/株" + "\n";
        }*/
        tv_qx.setText(jobtab.getregDate().substring(0, jobtab.getregDate().lastIndexOf(" ")));
        tv_jobname.setText(jobtab.getstdJobTypeName() + "——" + jobtab.getstdJobName());
        tv_yl.setText(flyl);
        tv_note.setText(jobtab.getjobNote());
        if (jobtab.getaudioJobExecPath().equals(""))
        {
            tv_pf.setText("暂未评分");
        } else
        {
            tv_pf.setText(jobtab.getaudioJobExecPath() + "分");
        }

        List<String> pfnr = jobtab.getPF();
        String nr = "";
        if (pfnr.size() == 0)
        {
            tv_pfnr.setText("此项工作无评分标准");
            tv_pfnr.setGravity(Gravity.RIGHT);
        } else
        {
            for (int i = 0; i < pfnr.size(); i++)
            {
                nr = nr + pfnr.get(i) + "\n\n";
            }
            tv_pfnr.setText(nr);

        }
        tv_date_pf.setText(jobtab.getassessDate().substring(0, jobtab.getassessDate().lastIndexOf(" ")));
        tv_pfsm.setText(jobtab.getassessNote());
        tv_fkjg.setText(jobtab.getaudioJobAssessPath());


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
