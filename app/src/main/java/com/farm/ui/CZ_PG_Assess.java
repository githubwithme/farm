package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.CZ_PG_Assess_ExpandAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
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

@EActivity(R.layout.cz_pg_assess)
public class CZ_PG_Assess extends Activity
{
    String bfb="20";
    CustomDialog_ListView customDialog_listView;
    CZ_PG_Assess_ExpandAdapter cz_pg_assess_expandAdapter;
    jobtab jobtab;
    // AudioFragment audioFragment;
    String scoreselected = "";
    String assessScore = "";
    @ViewById
    ImageButton imgbtn_home;
    @ViewById
    Button btn_sure;
    @ViewById
    EditText et_note;
    @ViewById
    TextView tv_comment;
    @ViewById
    TextView tv_fl;
    @ViewById
    TextView tv_starttime;
    @ViewById
    TextView tv_zyts;
    @ViewById
    TextView tv_bfb;
    @ViewById
    TextView tv_note;
    @ViewById
    ProgressBar pb_jd;
    @ViewById
    RadioGroup rg_score;
    @ViewById
    ExpandableListView expandableListView;

    @Click
    void btn_sure()
    {
        addScore();
    }    @Click
    void tv_bfb()
    {
        JSONObject jsonObject = utils.parseJsonFile(CZ_PG_Assess.this, "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("bfb"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            if (Integer.valueOf(jsonArray.getString(i))>Integer.valueOf(bfb))
            {
                list.add(jsonArray.getString(i));
            }

        }
        showDialog_workday(list);
    }

    @AfterViews
    void afterOncreate()
    {
        tv_bfb.setText(bfb);
        rg_score.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1)
            {
                RadioButton radioButton = (RadioButton) findViewById(arg0.getCheckedRadioButtonId());
                scoreselected = (String) radioButton.getText();
                assessScore = String.valueOf(getScore(scoreselected));
            }
        });
        getCommand();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        jobtab = getIntent().getParcelableExtra("bean");
    }

    Fragment mContent = new Fragment();


    private void addScore()
    {
        List<View> list_view = cz_pg_assess_expandAdapter.getListView();
        for (int i = 0; i < list_view.size(); i++)
        {
            Bundle bundle = (Bundle) list_view.get(i).getTag();
            assessScore = assessScore + bundle.get("id") + ",";
        }
        if (assessScore.equals(""))
        {
            Toast.makeText(CZ_PG_Assess.this, "请先评分", Toast.LENGTH_SHORT).show();
            return;
        }
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "jobTabAssessByID");
        params.addQueryStringParameter("jobID", jobtab.getId());
        params.addQueryStringParameter("assessScore", assessScore);
        params.addQueryStringParameter("percent", tv_bfb.getText().toString());
        params.addQueryStringParameter("assessNote", et_note.getText().toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<jobtab> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
                        // if (audioFragment.getAudiopath() != null)
                        // {
                        // uploadAudio(listData.get(0).getId(),
                        // audioFragment.getAudiopath());
                        // } else
                        // {
                        Toast.makeText(CZ_PG_Assess.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        finish();
                        // }
                    } else
                    {
                        AppContext.makeToast(CZ_PG_Assess.this, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(CZ_PG_Assess.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
                AppContext.makeToast(CZ_PG_Assess.this, "error_connectServer");
            }
        });
    }


    private void getCommand()
    {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("commandid", jobtab.getcommandID());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "commandGetByID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String aa = responseInfo.result;
                List<commandtab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), commandtab.class);
                        showCommand(listNewData.get(0));
                        if (listNewData.get(0).getstdJobType().equals("-1") || listNewData.get(0).getstdJobType().equals("0"))
                        {
                            rg_score.setVisibility(View.VISIBLE);
                        } else
                        {
                            getCommandlist();
                        }
                    } else
                    {
                        listNewData = new ArrayList<commandtab>();
                    }
                } else
                {
                    AppContext.makeToast(CZ_PG_Assess.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {

            }
        });
    }

    private void showCommand(commandtab commandtab)
    {
        String[] nongzi = commandtab.getnongziName().split(",");
        String[] yl = commandtab.getamount().split(";");
        String flyl = "";
        for (int i = 0; i < nongzi.length; i++)
        {
            flyl = flyl + nongzi[i] + "  ;  ";
        }
        tv_comment.setText(commandtab.getstdJobTypeName() + "-" + commandtab.getstdJobName());
        tv_fl.setText(flyl);
        tv_note.setText(commandtab.getcommNote());
        tv_zyts.setText(commandtab.getcommDays());
        tv_starttime.setText(commandtab.getcommComDate());
        pb_jd.setProgress(0);
    }

    private void getCommandlist()
    {
        commembertab commembertab = AppContext.getUserInfo(CZ_PG_Assess.this);
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
                            cz_pg_assess_expandAdapter = new CZ_PG_Assess_ExpandAdapter(CZ_PG_Assess.this, dic, expandableListView);
                            expandableListView.setAdapter(cz_pg_assess_expandAdapter);
                            utils.setListViewHeight(expandableListView);
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
                    AppContext.makeToast(CZ_PG_Assess.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_PG_Assess.this, "error_connectServer");
            }
        });
    }

    private int getScore(String score)
    {
        if (score.equals("优"))
        {
            return 10;
        } else if (score.equals("合格"))
        {
            return 8;
        } else if (score.equals("不合格"))
        {
            return 0;
        }
        return 0;
    }
    public void showDialog_workday(List<String> list)
    {
        View dialog_layout = (RelativeLayout) CZ_PG_Assess.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(CZ_PG_Assess.this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                bfb = bundle.getString("name");
                tv_bfb.setText(bfb);
            }
        });
        customDialog_listView.show();
    }
}
