package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.CZ_PG_JobDetail_ExpandAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
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
    TextView tv_qx;
    @ViewById
    TextView tv_note;
    @ViewById
    TextView tv_pfnote;

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

    private void getCommandlist()
    {
        commembertab commembertab = AppContext.getUserInfo(Common_JobDetail_Show.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("comid", jobtab.getstdJobId());
        params.addQueryStringParameter("action", "getcommandPFBZ");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)
                {
                    if (result.getAffectedRows() != 0)
                    {
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            Dictionary dic = lsitNewData.get(0);
                            cz_pg_assess_expandAdapter = new CZ_PG_JobDetail_ExpandAdapter(Common_JobDetail_Show.this, dic, expandableListView);
                            expandableListView.setAdapter(cz_pg_assess_expandAdapter);
//                            for (int i = 0; i < dic.getFirstItemName().size(); i++)
//                            {
//                                expandableListView.expandGroup(i);
//                            }
                        }

                    } else
                    {

                    }
                } else
                {
                    AppContext.makeToast(Common_JobDetail_Show.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Common_JobDetail_Show.this, "error_connectServer");
            }
        });
    }

}
