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

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.CZ_PG_JobDetail_ExpandAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.widget.CustomExpandableListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/4/27.
 */
@EActivity(R.layout.common_jobdetail_assess)
public class NCZ_Todayjob_Common extends Activity {
    com.farm.bean.jobtab jobtab;
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
    ImageButton btn_back;

    @ViewById
    Button btn_score;

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

    @Override
    protected void onResume()
    {
        super.onResume();
        getJob();
    }

    @AfterViews
    void afterOncreate()
    {
        if (jobtab.equals("1"))
        {
            btn_score.setVisibility(View.VISIBLE);
        }
        showData(jobtab);
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
//        String[] dw = jobtab.getAmountDW().split("[.]");
//        String [] ds=jobtab.getAmountDW().split("[.]");
        String flyl = "";
//        for (int i = 0; i < nongzi.length; i++)
        for (int i = 0; i < nongzi.length; i++)
        {
//            flyl = flyl +dw[i]+ "\n";
            flyl = flyl + nongzi[i] + "：" + yl[i]  + "\n";
//            flyl = flyl + nongzi[i] + "：" + yl[i] +dw[i] + "\n";
//            flyl = flyl + nongzi[i] + "：" + yl[i] + "/株" + "\n";
        }
        tv_qx.setText(jobtab.getregDate().substring(0, jobtab.getregDate().lastIndexOf(" ")));
        tv_jobname.setText(jobtab.getstdJobTypeName() + "——" + jobtab.getstdJobName());
        tv_yl.setText(flyl);
        tv_note.setText(jobtab.getjobNote());
        tv_pf.setText(jobtab.getaudioJobExecPath() + "分");
        List<String> pfnr = jobtab.getPF();
        String nr = "";
        for (int i = 0; i < pfnr.size(); i++)
        {
            nr = nr + pfnr.get(i) + "\n\n";
        }
        tv_date_pf.setText(jobtab.getassessDate().substring(0, jobtab.getassessDate().lastIndexOf(" ")));
        tv_pfnr.setText(nr);
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

    private void getJob()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_Todayjob_Common.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("jobid", jobtab.getId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("action", "jobGetByID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<jobtab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
                        if (listNewData.get(0).getjobStatus().equals("1")||(!listNewData.get(0).getareaId().equals("0")))
                        {
                            btn_score.setVisibility(View.GONE);
                        }
                        showData(listNewData.get(0));


                    } else
                    {
                        listNewData = new ArrayList<jobtab>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_Todayjob_Common.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {
                AppContext.makeToast(NCZ_Todayjob_Common.this, "error_connectServer");
            }
        });
    }
}
