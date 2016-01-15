package com.farm.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_AddNotStd_Cmd_Self;
import com.farm.bean.SelectCmdArea;
import com.farm.bean.commandtab_single;
import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;
import com.farm.widget.MyDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_add_notstd__cmd_self)
public class AddNotStd_Cmd_Self extends FragmentActivity implements FragmentCallBack
{
    String level = "";
    MyDialog myDialog;
    int currentItem = 0;
    ViewPagerAdapter_AddNotStd_Cmd_Self adapter;
    AddStd_Cmd_Self_StepTwo addStd_cmd_self_stepTwo;
    AddStd_Cmd_StepFive addStd_cmd_stepFive;
    AddStd_Cmd_StepSix_Self addStd_cmd_stepSix;
    private ArrayList<Fragment> fragmentList;
    @ViewById
    TextView text_three;
    @ViewById
    TextView text_five;
    @ViewById
    TextView text_six;
    @ViewById
    ImageView image_three;
    @ViewById
    ImageView image_five;
    @ViewById
    ImageView image_six;
    @ViewById
    android.support.v4.view.ViewPager vPager;

    @Click
    void imgbtn_back()
    {
        finish();
    }

    @Click
    void text_three()
    {
        vPager.setCurrentItem(0);
    }


    @Click
    void text_five()
    {
        vPager.setCurrentItem(1);
    }

    @Click
    void text_six()
    {
        vPager.setCurrentItem(2);
    }

    @AfterViews
    void afterOncreate()
    {
        commandtab_single.getInstance().clearAll();
        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd_Self.this, SelectCmdArea.class);
        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd_Self.this, goodslisttab.class);
        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd_Self.this, goodslisttab_flsl.class);

        fragmentList = new ArrayList<Fragment>();
        addStd_cmd_self_stepTwo = new AddStd_Cmd_Self_StepTwo_();
        addStd_cmd_stepFive = new AddStd_Cmd_StepFive_();
        addStd_cmd_stepSix = new AddStd_Cmd_StepSix_Self_();

        fragmentList.add(addStd_cmd_self_stepTwo);
        fragmentList.add(addStd_cmd_stepFive);
        fragmentList.add(addStd_cmd_stepSix);

        setBackground(0);
        //关闭预加载，默认一次只加载一个Fragment
        vPager.setOffscreenPageLimit(0);
        adapter = new ViewPagerAdapter_AddNotStd_Cmd_Self(AddNotStd_Cmd_Self.this.getSupportFragmentManager(), vPager, fragmentList);

        adapter.setOnExtraPageChangeListener(new ViewPagerAdapter_AddNotStd_Cmd_Self.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageSelected(int i)
            {
                currentItem = i;
                setBackground(i);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        level = getIntent().getStringExtra("level");
        commandtab_single.getInstance().clearAll();
        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd_Self.this, SelectCmdArea.class);
    }


    private void setBackground(int pos)
    {
        image_three.setBackgroundResource(R.drawable.line);
        image_five.setBackgroundResource(R.drawable.line);
        image_six.setBackgroundResource(R.drawable.line);

        text_three.setBackgroundResource(R.color.white);
        text_five.setBackgroundResource(R.color.white);
        text_six.setBackgroundResource(R.color.white);

        text_three.setTextColor(Color.parseColor("#5B5B5B"));
        text_five.setTextColor(Color.parseColor("#5B5B5B"));
        text_six.setTextColor(Color.parseColor("#5B5B5B"));

        switch (pos)
        {

            case 0:
                image_three.setBackgroundResource(R.drawable.green_line);
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_next);
                break;
            case 1:
                image_five.setBackgroundResource(R.drawable.green_line);
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_five.setBackgroundResource(R.drawable.tag_next);
                break;
            case 2:
                image_six.setBackgroundResource(R.drawable.green_line);
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_six.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_five.setBackgroundResource(R.drawable.tag_red);
                text_six.setBackgroundResource(R.drawable.tag_next);
                break;
        }


    }

    @Override
    public void callbackFun1(Bundle arg)
    {
//        switchFragment();//通过回调方式切换
    }

    @Override
    public void callbackFun_setText(Bundle arg)
    {
        String st = arg.getString("type");
    }

    @Override
    public void stepTwo_setHeadText(Bundle arg)
    {
        String st = arg.getString("type");
        addStd_cmd_self_stepTwo.setHeadText(st);
    }


    @Override
    public void callbackFun2(Bundle arg)
    {

        switch (currentItem)
        {
            case 0:
                break;
            case 1:
                AddStd_Cmd_StepSix_Self addStd_cmd_stepSix_self = (AddStd_Cmd_StepSix_Self) adapter.getFragment(currentItem + 1);
                addStd_cmd_stepSix_self.update();
                break;
            case 2:

                break;
        }
        vPager.setCurrentItem(currentItem + 1);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
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
        myDialog = new MyDialog(AddNotStd_Cmd_Self.this, R.style.MyDialog, dialog_layout, "取消标准生产指令", "确定取消吗？", "是", "否", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        myDialog.dismiss();
                        commandtab_single.getInstance().clearAll();
                        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd_Self.this, SelectCmdArea.class);
                        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd_Self.this, goodslisttab.class);
                        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd_Self.this, goodslisttab_flsl.class);
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
