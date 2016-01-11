package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppManager;
import com.farm.widget.MyDialog;
import com.farm.widget.MyDialog.CustomDialogListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_pg)
public class PG_MainActivity extends Activity
{
    MyDialog myDialog;
    Fragment mContent = new Fragment();
    PG_MainFragment mainFragment;
    PG_GddList pg_gddList;
    PG_EventList pg_eventList;
    PG_EveryDayAssessList pg_EveryDayAssessList;
    IFragment iFragment;
    @ViewById
    ImageButton imgbtn_home;
    @ViewById
    ImageButton imgbtn_plant;
    @ViewById
    ImageButton imgbtn_me;
    @ViewById
    ImageButton imgbtn_event;

    @ViewById
    TextView tv_home;
    @ViewById
    TextView tv_plant;
    @ViewById
    TextView tv_me;
    @ViewById
    TextView tv_event;

    @ViewById
    TableLayout tl_home;
    @ViewById
    TableLayout tl_plant;
    @ViewById
    TableLayout tl_me;
    @ViewById
    TableLayout tl_event;

    @Click
    void tl_home()
    {
        tv_home.setTextColor(getResources().getColor(R.color.bg_blue));
        tv_plant.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_event.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_home.setSelected(true);
        tl_plant.setSelected(false);
        tl_me.setSelected(false);
        tl_event.setSelected(false);
        switchContent(mContent, mainFragment);
    }

    @Click
    void tl_event()
    {
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_plant.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_event.setTextColor(getResources().getColor(R.color.bg_blue));

        tl_home.setSelected(false);
        tl_plant.setSelected(false);
        tl_me.setSelected(false);
        tl_event.setSelected(true);
        switchContent(mContent, pg_eventList);
    }

    @Click
    void tl_plant()
    {
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_plant.setTextColor(getResources().getColor(R.color.bg_blue));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_event.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_home.setSelected(false);
        tl_plant.setSelected(true);
        tl_me.setSelected(false);
        tl_event.setSelected(false);
        switchContent(mContent, pg_gddList);
    }

    @Click
    void tl_me()
    {
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_plant.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_me.setTextColor(getResources().getColor(R.color.bg_blue));
        tv_event.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_home.setSelected(false);
        tl_plant.setSelected(false);
        tl_me.setSelected(true);
        tl_event.setSelected(false);
        switchContent(mContent, iFragment);
    }

    @AfterViews
    void afterOncreate()
    {
        switchContent(mContent, mainFragment);
        tv_home.setTextColor(getResources().getColor(R.color.red));
        tl_home.setSelected(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        AppManager.getAppManager().addActivity(this);
        mainFragment = new PG_MainFragment_();
        pg_gddList = new PG_GddList_();
        pg_eventList = new PG_EventList_();
        pg_EveryDayAssessList = new PG_EveryDayAssessList_();
        iFragment = new IFragment_();
    }

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

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
        myDialog = new MyDialog(PG_MainActivity.this, R.style.MyDialog, dialog_layout, "确定退出吗？", "确定退出吗？", "退出", "取消", new CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        finish();
                        break;
                    case R.id.btn_cancle:
                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }
}
