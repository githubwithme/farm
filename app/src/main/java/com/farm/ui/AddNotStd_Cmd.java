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
import android.widget.Toast;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_AddNotStd_Cmd;
import com.farm.bean.SelectCmdArea;
import com.farm.bean.commandtab_single;
import com.farm.bean.commembertab_singleton;
import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;
import com.farm.widget.CustomViewPager;
import com.farm.widget.MyDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_add_notstd__cmd)
public class AddNotStd_Cmd extends FragmentActivity implements FragmentCallBack
{
    commandtab_single commandtab_single;
    List<goodslisttab_flsl> list_SelectCmdArea = new ArrayList<goodslisttab_flsl>();
    List<goodslisttab> list_goodslisttab = new ArrayList<goodslisttab>();
    String level = "";
    MyDialog myDialog;
    int currentItem = 0;
    ViewPagerAdapter_AddNotStd_Cmd adapter;
    AddStd_Cmd_StepTwo addStd_cmd_stepTwo;
    AddStd_Cmd_StepThree_Temp addStd_cmd_stepThree_temp;
    AddStd_Cmd_StepFive addStd_cmd_stepFive;
    AddNotStd_Cmd_StepSix addStd_cmd_stepSix;
    commembertab_singleton commembertab_singleton;
    private ArrayList<Fragment> fragmentList;
    @ViewById
    TextView text_three;
    @ViewById
    TextView text_four;
    @ViewById
    TextView text_five;
    @ViewById
    TextView text_six;
    @ViewById
    ImageView image_three;
    @ViewById
    ImageView image_four;
    @ViewById
    ImageView image_five;
    @ViewById
    ImageView image_six;
    @ViewById
    CustomViewPager vPager;

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
    void text_four()
    {
        vPager.setCurrentItem(1);
    }

    @Click
    void text_five()
    {
        vPager.setCurrentItem(2);
    }

    @Click
    void text_six()
    {
        vPager.setCurrentItem(3);
    }

    @AfterViews
    void afterOncreate()
    {
        commandtab_single.getInstance().clearAll();
        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd.this, SelectCmdArea.class);
        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd.this, goodslisttab.class);
        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd.this, goodslisttab_flsl.class);

        fragmentList = new ArrayList<Fragment>();
        addStd_cmd_stepTwo = new AddStd_Cmd_StepTwo_();
        addStd_cmd_stepThree_temp = new AddStd_Cmd_StepThree_Temp_();
        addStd_cmd_stepFive = new AddStd_Cmd_StepFive_();
        addStd_cmd_stepSix = new AddNotStd_Cmd_StepSix_();

        fragmentList.add(addStd_cmd_stepTwo);
        fragmentList.add(addStd_cmd_stepThree_temp);
        fragmentList.add(addStd_cmd_stepFive);
        fragmentList.add(addStd_cmd_stepSix);

        setBackground(0);
        setMenuUnCliable();
        //关闭预加载，默认一次只加载一个Fragment
        vPager.setOffscreenPageLimit(0);
        vPager.setIsScrollable(false);
        adapter = new ViewPagerAdapter_AddNotStd_Cmd(AddNotStd_Cmd.this.getSupportFragmentManager(), vPager, fragmentList);

        adapter.setOnExtraPageChangeListener(new ViewPagerAdapter_AddNotStd_Cmd.OnExtraPageChangeListener()
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
        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd.this, SelectCmdArea.class);
    }


    private void setBackground(int pos)
    {
        image_three.setBackgroundResource(R.drawable.line);
        image_four.setBackgroundResource(R.drawable.line);
        image_five.setBackgroundResource(R.drawable.line);
        image_six.setBackgroundResource(R.drawable.line);

        text_three.setBackgroundResource(R.color.white);
        text_four.setBackgroundResource(R.color.white);
        text_five.setBackgroundResource(R.color.white);
        text_six.setBackgroundResource(R.color.white);

        text_three.setTextColor(Color.parseColor("#5B5B5B"));
        text_four.setTextColor(Color.parseColor("#5B5B5B"));
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
                image_four.setBackgroundResource(R.drawable.green_line);
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_next);
                break;
            case 2:
                image_five.setBackgroundResource(R.drawable.green_line);
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_red);
                text_five.setBackgroundResource(R.drawable.tag_next);
                break;
            case 3:
                image_six.setBackgroundResource(R.drawable.green_line);
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_six.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_red);
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
    }

    @Override
    public void stepTwo_setHeadText(Bundle arg)
    {
        String st = arg.getString("type");
        addStd_cmd_stepTwo.setHeadText(st);
    }


    @Override
    public void callbackFun2(Bundle arg)
    {

        switch (currentItem)
        {

            case 0:
                text_three.setClickable(true);
                text_four.setClickable(true);
                list_goodslisttab= SqliteDb.getSelectCmdArea(AddNotStd_Cmd.this, goodslisttab.class);
                if (list_goodslisttab.size()>0)
                {
                    AddStd_Cmd_StepThree_Temp addStd_cmd_stepThree_temp = (AddStd_Cmd_StepThree_Temp) adapter.getFragment(currentItem + 1);
                    addStd_cmd_stepThree_temp.update();
                    vPager.setCurrentItem(currentItem + 1);
                }else
                {
                    Toast.makeText(AddNotStd_Cmd.this, "必须完整填写或者选择相应信息！", Toast.LENGTH_SHORT).show();
                }

                break;
            case 1:
                text_four.setClickable(true);
                text_five.setClickable(true);
                list_SelectCmdArea = SqliteDb.getSelectCmdArea(AddNotStd_Cmd.this, goodslisttab_flsl.class);
                if (list_SelectCmdArea.size()>0)
                {
                    vPager.setCurrentItem(currentItem + 1);
                }else
                {
                    Toast.makeText(AddNotStd_Cmd.this,"必须完整填写或者选择相应信息！",Toast.LENGTH_SHORT).show();
                }

                break;
            case 2:
                text_five.setClickable(true);
                text_six.setClickable(true);
                commandtab_single = com.farm.bean.commandtab_single.getInstance();
                if (commandtab_single.getimportance().equals("") || commandtab_single.getcommComDate().equals("") || commandtab_single.getcommDays().equals("") || commandtab_single.getcommNote().equals(""))
                {
                    Toast.makeText(AddNotStd_Cmd.this,"必须完整填写或者选择相应信息！",Toast.LENGTH_SHORT).show();
                }else
                {
                    vPager.setIsScrollable(true);
                    setMenuCliable();
                    AddNotStd_Cmd_StepSix addNotStd_cmd_stepSix = (AddNotStd_Cmd_StepSix) adapter.getFragment(currentItem + 1);
                    addNotStd_cmd_stepSix.update();
                    vPager.setCurrentItem(currentItem + 1);

                }

                break;
            case 3:
                text_six.setClickable(true);
                break;
        }
    }
    private void setMenuCliable()
    {
        text_three.setClickable(true);
        text_four.setClickable(true);
        text_five.setClickable(true);
        text_six.setClickable(true);

    }
    private void setMenuUnCliable()
    {
        text_three.setClickable(false);
        text_four.setClickable(false);
        text_five.setClickable(false);
        text_six.setClickable(false);
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
        myDialog = new MyDialog(AddNotStd_Cmd.this, R.style.MyDialog, dialog_layout, "取消非标准生产指令", "确定取消吗？", "是", "否", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        myDialog.dismiss();
                        commandtab_single.getInstance().clearAll();
                        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd.this, SelectCmdArea.class);
                        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd.this, goodslisttab.class);
                        SqliteDb.deleteAllSelectCmdArea(AddNotStd_Cmd.this, goodslisttab_flsl.class);
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
