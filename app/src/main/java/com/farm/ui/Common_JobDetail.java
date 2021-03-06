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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.widget.MyDialog;
import com.farm.widget.MyDialog.CustomDialogListener;
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

@EActivity(R.layout.pg_jobdetail)
public class Common_JobDetail extends Activity
{

    @ViewById
    LinearLayout ll_yl_tip;
    @ViewById
    RelativeLayout rl_jobname_tip;
    @ViewById
    Button btn_delete;
    @ViewById
    TextView tv_score;
    @ViewById
    TextView tv_importance;
    @ViewById
    TextView tv_scorenote;
    @ViewById
    TextView btn_score;
    @ViewById
    TextView tv_tip_yl;
    @ViewById
    TextView tv_jobname;
    @ViewById
    TextView tv_yl;
    @ViewById
    TextView tv_qx;
    @ViewById
    TextView tv_note;

    jobtab jobtab;
    @ViewById
    ImageButton btn_back;

    @Click
    void btn_delete()
    {
        showDeleteTip(jobtab.getId());
    }

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
        String[] dw = jobtab.getAmountDW().split(",");
        String flyl = "";
        for (int i = 0; i < nongzi.length; i++)
        {
//            flyl = flyl + nongzi[i] + "：" + yl[i] +"/株" + "\n";
            flyl = flyl + nongzi[i] + "：" + yl[i] + dw[i] + "\n";
        }

        tv_jobname.setText(jobtab.getstdJobTypeName() + "——" + jobtab.getstdJobName());
        tv_yl.setText(flyl);
        tv_note.setText(jobtab.getjobNote());



        if (jobtab.getassessScore().equals("-1"))
        {
            tv_score.setText("暂无");
            tv_scorenote.setText("暂无");
        } else
        {
            tv_score.setTextColor(Common_JobDetail.this.getResources().getColor(R.color.red));
            if (jobtab.getassessScore().equals("0"))
            {
                tv_score.setText("不合格");
            } else if (jobtab.getassessScore().equals("8"))
            {
                tv_score.setText("合格");
            } else if (jobtab.getassessScore().equals("10"))
            {
                tv_score.setText("优");
            }
            tv_scorenote.setText(jobtab.getassessNote());
        }

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

    private void deletejob(String jobID)
    {
        commembertab commembertab = AppContext.getUserInfo(Common_JobDetail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("jobID", jobID);
        params.addQueryStringParameter("action", "deljobByID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        Toast.makeText(Common_JobDetail.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
                        finish();
                    } else
                    {
                        Toast.makeText(Common_JobDetail.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    AppContext.makeToast(Common_JobDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(Common_JobDetail.this, "error_connectServer");
            }
        });
    }

    MyDialog myDialog;

    private void showDeleteTip(final String jobID)
    {
        View dialog_layout = (LinearLayout) Common_JobDetail.this.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(Common_JobDetail.this, R.style.MyDialog, dialog_layout, "工作", "确定删除吗?", "删除", "取消", new CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        deletejob(jobID);
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }
}
