package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Common_CommandExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
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

@EActivity(R.layout.pg_commanddetail)
public class Common_CommandDetail extends Activity
{

    @ViewById
    TextView tv_zyts;
    @ViewById
    Button btn_delete;
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
    ListView lv;
    String filename;
    commandtab commandtab;
    @ViewById
    ImageButton btn_back;
    @ViewById
    LinearLayout ll_yl_tip;
    @ViewById
    RelativeLayout rl_jobname_tip;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void btn_delete()
    {
    }


    @AfterViews
    void afterOncreate()
    {
        if (commandtab.getcommFromVPath().equals("0"))
        {
            btn_delete.setVisibility(View.GONE);
        } else
        {

        }
        getJobList();
        showData(commandtab);
        // if (commandtab.getcommFromVPath() != null &&
        // !commandtab.getcommFromVPath().equals(""))
        // {
        // filename =
        // commandtab.getcommFromVPath().toString().substring(commandtab.getcommFromVPath().lastIndexOf("/"),
        // commandtab.getcommFromVPath().length());
        // downloadLuYin(AppConfig.testurl + commandtab.getcommFromVPath(),
        // AppConfig.MEDIA_PATH + filename);
        // } else
        // {
        // Toast.makeText(this, "暂无录音", Toast.LENGTH_SHORT).show();
        // }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commandtab = getIntent().getParcelableExtra("bean");
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
            flyl = flyl + nongzi[i] +  "\n";
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

    private void getJobList()
    {
        commembertab commembertab = AppContext.getUserInfo(Common_CommandDetail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("commandid", commandtab.getId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("action", "jobListGetByComID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<commandtab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), commandtab.class);
                        List<jobtab> jobtabs = listNewData.get(0).getJobList();
                        if (jobtabs == null)
                        {

                        } else
                        {
                            Common_CommandExecute_Adapter common_commandExecute_adapter = new Common_CommandExecute_Adapter(Common_CommandDetail.this, jobtabs);
                            lv.setAdapter(common_commandExecute_adapter);
                        }

                    } else
                    {
                        listNewData = new ArrayList<commandtab>();
                    }
                } else
                {
                    AppContext.makeToast(Common_CommandDetail.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {

            }
        });
    }

}
