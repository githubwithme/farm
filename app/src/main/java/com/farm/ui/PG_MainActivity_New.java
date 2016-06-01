package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.app.AppManager;
import com.farm.bean.ExceptionInfo;
import com.farm.bean.LogInfo;
import com.farm.bean.Result;
import com.farm.common.GetMobilePhoneInfo;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
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
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

@EActivity(R.layout.pg_mainactivity_new)
public class PG_MainActivity_New extends Activity
{
    NCZ_ContactsFragment ncz_contactsFragment;//联系人fragment
    MyDialog myDialog;
    Fragment mContent = new Fragment();
    PG_DynamicFragment pg_dynamicFragment;//动态fragment
    NCZ_JobFragment ncz_jobFragment;//工作fragment
    FarmManagerFragment farmManagerFragment;//农场工作fragment
    IFragment iFragment;//个人信息fragment
    @ViewById
    FrameLayout fl_new;
    @ViewById
    TextView tv_new;
    @ViewById
    TextView tv_farmlive_new;
    @ViewById
    ImageButton imgbtn_home;
    @ViewById
    ImageButton imgbtn_me;
    @ViewById
    ImageButton imgbtn_product;
    @ViewById
    ImageButton imgbtn_sale;
    @ViewById
    ImageButton imgbtn_money;
    @ViewById
    ImageButton imgbtn_farmlive;

    @ViewById
    TextView tv_home;
    @ViewById
    TextView tv_me;
    @ViewById
    TextView tv_product;
    @ViewById
    TextView tv_sale;
    @ViewById
    TextView tv_farmlive;

    @ViewById
    TableLayout tl_home;
    @ViewById
    TableLayout tl_product;
    @ViewById
    TableLayout tl_sale;
    @ViewById
    TableLayout tl_me;
    @ViewById
    TableLayout tl_farmlive;

    @Click
    void tl_home()
    {
        tv_home.setTextColor(getResources().getColor(R.color.red));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_farmlive.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_home.setSelected(true);
        tl_me.setSelected(false);
        tl_product.setSelected(false);
        tl_sale.setSelected(false);
        tl_farmlive.setSelected(false);
        switchContent(mContent, pg_dynamicFragment);
    }

    @Click
    void tl_farmlive()
    {
        tv_farmlive.setTextColor(getResources().getColor(R.color.red));
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_farmlive.setSelected(true);
        tl_home.setSelected(false);
        tl_me.setSelected(false);
        tl_product.setSelected(false);
        tl_sale.setSelected(false);
        switchContent(mContent, ncz_jobFragment);
    }

    @Click
    void tl_product()
    {
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_product.setTextColor(getResources().getColor(R.color.red));
        tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_farmlive.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_home.setSelected(false);
        tl_me.setSelected(false);
        tl_product.setSelected(true);
        tl_sale.setSelected(false);
        tl_farmlive.setSelected(false);
//		switchContent(mContent, ncz_CommandList);
        switchContent(mContent, farmManagerFragment);
    }

    @Click
    void tl_sale()
    {
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_sale.setTextColor(getResources().getColor(R.color.red));
        tv_farmlive.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_home.setSelected(false);
        tl_me.setSelected(false);
        tl_product.setSelected(false);
        tl_sale.setSelected(true);
        tl_farmlive.setSelected(false);

        switchContent(mContent, ncz_contactsFragment);
    }

    @Click
    void tl_me()
    {
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_me.setTextColor(getResources().getColor(R.color.red));
        tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_farmlive.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_home.setSelected(false);
        tl_me.setSelected(true);
        tl_product.setSelected(false);
        tl_sale.setSelected(false);
        tl_farmlive.setSelected(false);
        switchContent(mContent, iFragment);
    }

    @AfterViews
    void afterOncreate()
    {
        //将错误信息提交
        List<ExceptionInfo> list_exception = SqliteDb.getExceptionInfo(PG_MainActivity_New.this);
        if (list_exception != null)
        {
            for (int i = 0; i < list_exception.size(); i++)
            {
                sendExceptionInfoToServer(list_exception.get(i));
            }
        }
        //将日志信息提交
        List<LogInfo> list_LogInfo = SqliteDb.getLogInfo(PG_MainActivity_New.this);
        if (list_LogInfo != null)
        {
            sendLogInfoToServer(list_LogInfo, GetMobilePhoneInfo.getDeviceUuid(PG_MainActivity_New.this).toString(), utils.getToday());
        }

        AppManager.getAppManager().addActivity(PG_MainActivity_New.this);
        pg_dynamicFragment = new PG_DynamicFragment_();
        ncz_jobFragment = new NCZ_JobFragment_();
        farmManagerFragment = new FarmManagerFragment_();
        ncz_contactsFragment = new NCZ_ContactsFragment_();
        iFragment = new IFragment_();
//        List<Integer> guideResourceId = new ArrayList<Integer>();
//        guideResourceId.add(R.drawable.yd666);
//        guideResourceId.add(R.drawable.yd55555);
//        guideResourceId.add(R.drawable.yd555);
//        guideResourceId.add(R.drawable.yd444);
//        guideResourceId.add(R.drawable.yd333);
//        guideResourceId.add(R.drawable.yd222);
//        guideResourceId.add(R.drawable.yd001);
//        setGuideResId(guideResourceId);// 添加引导页
//        tv_home.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity_New.this, "wsyh.ttf"));
//        tv_me.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity_New.this, "wsyh.ttf"));
//        tv_product.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity_New.this, "wsyh.ttf"));
//        tv_sale.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity_New.this, "wsyh.ttf"));
//        tv_money.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity_New.this, "wsyh.ttf"));
        switchContent(mContent, pg_dynamicFragment);
        tv_home.setTextColor(getResources().getColor(R.color.red));
        tl_home.setSelected(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container_main, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }


    class TimeThread extends Thread
    {
        private boolean isSleep = true;
        private boolean stop = false;

        public void run()
        {
            Long starttime = 0l;
            while (!stop)
            {
                if (isSleep)
                {
                } else
                {
                    try
                    {
                        Thread.sleep(AppContext.TIME_REFRESH);
                        starttime = starttime + 1000;
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setSleep(boolean sleep)
        {
            isSleep = sleep;
        }

        public void setStop(boolean stop)
        {
            this.stop = stop;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        AppManager.getAppManager().AppExit(PG_MainActivity_New.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            showExistTip();
        }
        return false;

    }

    private void showExistTip()
    {
        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(PG_MainActivity_New.this, R.style.MyDialog, dialog_layout, "确定退出吗？", "确定退出吗？", "退出", "取消", new CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        myDialog.dismiss();
                        AppManager.getAppManager().AppExit(PG_MainActivity_New.this);
                        NotificationManager manger = (NotificationManager) PG_MainActivity_New.this.getSystemService(PG_MainActivity_New.this.NOTIFICATION_SERVICE);
                        manger.cancel(101);
                        manger.cancel(100);
                        break;
                    case R.id.btn_cancle:
                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void sendExceptionInfoToServer(final ExceptionInfo exception)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("UUID", exception.getUuid());
        params.addQueryStringParameter("exceptionInfo", exception.getExceptionInfo());
        params.addQueryStringParameter("userid", exception.getUserid());
        params.addQueryStringParameter("username", exception.getUsername());
        params.addQueryStringParameter("action", "saveAppException");
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
                        SqliteDb.deleteExceptionInfo(PG_MainActivity_New.this, exception.getExceptionid());
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
            }
        });

    }

    private void sendLogInfoToServer(final List<LogInfo> list, String deviceuuid, String logday)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{ \"LogInfoList\": ");
        builder.append(JSON.toJSONString(list));
        builder.append("} ");
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("deviceuuid", deviceuuid);
        params.addQueryStringParameter("logday", logday);
        params.addQueryStringParameter("action", "addLogInfo");
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(builder.toString(), "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    String rows = result.getRows().get(0).toString();
                    if (rows.equals("1"))
                    {
                        SqliteDb.updateLogInfo(PG_MainActivity_New.this, list);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
            }
        });

    }
}