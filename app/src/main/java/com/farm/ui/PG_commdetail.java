package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_GcdDetail;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.PlantGcd;
import com.farm.bean.Result;
import com.farm.widget.CustomViewPager;
import com.farm.widget.MyDialog;
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
 * Created by hasee on 2016/6/16.
 */
@EActivity(R.layout.pg_commdetail)
public class PG_commdetail extends FragmentActivity
{
    MyDialog myDialog;
    com.farm.bean.commembertab commembertab;
    PlantGcd plantGcd;
    com.farm.bean.commandtab commandtab;
    int currentItem = 0;
    List<Fragment> fragmentList;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    android.app.Fragment mContent = new android.app.Fragment();
    //    GrowthTreeFragment_GCD growthTreeFragment_gcd;
//    GrowthTreeFragment_ZZ growthTreeFragment_zz;
    CommandDetail_Show_DetailFragment commandDetail_show_detailFragment;
    CommandDetail_Show_ExecuteFragment commandDetail_show_executeFragment;
    @ViewById
    ImageButton btn_back;
    @ViewById
    CustomViewPager vPager;
    @ViewById
    TextView tv_detail;
    @ViewById
    TextView tv_execute;
    @ViewById
    Button btn_delete;

    @Click
    void btn_delete()
    {
        showDeleteTip(commandtab.getId(), commandtab.getStatusid());
    }

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void tv_execute()
    {
        vPager.setCurrentItem(1);
    }

    @Click
    void tv_detail()
    {
        vPager.setCurrentItem(0);
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
        setBackground(0);
        vPager.setOffscreenPageLimit(1);
        vPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(PG_commdetail.this.getSupportFragmentManager(), vPager, fragmentList);
        viewPagerAdapter_gcdDetail.setOnExtraPageChangeListener(new ViewPagerAdapter_GcdDetail.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageSelected(int i)
            {
//                Toast.makeText(GcdDetail.this, "show", Toast.LENGTH_SHORT).show();
                currentItem = i;
                setBackground(i);
            }
        });
    }

    private void setBackground(int pos)
    {
        tv_detail.setBackgroundResource(R.color.white);
        tv_execute.setBackgroundResource(R.color.white);
        switch (pos)
        {
            case 0:
                tv_detail.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_execute.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        Bundle bundle = new Bundle();
        getIntent().getStringExtra("from");
        commandtab = getIntent().getParcelableExtra("bean");
        bundle.putParcelable("bean", commandtab);

        commembertab = AppContext.getUserInfo(PG_commdetail.this);
        fragmentList = new ArrayList<>();
        commandDetail_show_detailFragment = new CommandDetail_Show_DetailFragment_();
        commandDetail_show_executeFragment = new CommandDetail_Show_ExecuteFragment_();
        commandDetail_show_executeFragment.setArguments(bundle);
        commandDetail_show_detailFragment.setArguments(bundle);

        fragmentList.add(commandDetail_show_executeFragment);
        fragmentList.add(commandDetail_show_detailFragment);

    }

    private void showDeleteTip(final String cmdid, final String statusID)
    {
        View dialog_layout = getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(PG_commdetail.this, R.style.MyDialog, dialog_layout, "指令", "确定删除吗?", "删除", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        deleteCmd(cmdid, statusID);
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void deleteCmd(String cmdid, String statusID)
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(PG_commdetail.this);
        RequestParams params = new RequestParams();
        // params.addQueryStringParameter("workuserid", workuserid);
        params.addQueryStringParameter("statusID", statusID);
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("comID", cmdid);
        params.addQueryStringParameter("action", "delCommandByID");
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
                        Toast.makeText(PG_commdetail.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
                        finish();
                    } else
                    {
                        Toast.makeText(PG_commdetail.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    AppContext.makeToast(PG_commdetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_commdetail.this, "error_connectServer");
            }
        });
    }
}
