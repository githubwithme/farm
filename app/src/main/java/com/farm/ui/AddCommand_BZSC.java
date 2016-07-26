package com.farm.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
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

/**
 * Created by ${hmj} on 2016/1/19.
 */
@EActivity(R.layout.addcommand_bzsc)
public class AddCommand_BZSC extends FragmentActivity implements FragmentCallBack
{
    Fragment mContent = new Fragment();
    String level = "";
    MyDialog myDialog;
    int currentItem = 0;
    AddStd_Cmd_StepOne_Temp addStd_cmd_stepOne;
    AddStd_Cmd_StepTwo addStd_cmd_stepTwo;
    AddStd_Cmd_StepThree_Temp addStd_cmd_stepThree_temp;
    AddStd_Cmd_StepFive addStd_cmd_stepFive;
    AddStd_Cmd_StepSix addStd_cmd_stepSix;
    private ArrayList<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();
    @ViewById
    TextView text_one;
    @ViewById
    Button btn_back;
    @ViewById
    Button btn_next;
    @ViewById
    TextView text_three;
    @ViewById
    TextView text_four;
    @ViewById
    TextView text_five;
    @ViewById
    TextView text_six;


    @Click
    void imgbtn_back()
    {
        finish();
    }

    @Click
    void btn_next()
    {
        currentItem = currentItem + 1;
        switchContent(mContent, fragmentList.get(currentItem));
        updateUI(currentItem);
    }

    @Click
    void btn_back()
    {
        currentItem = currentItem - 1;
        setBackground(currentItem);
        switchContent(mContent, fragmentList.get(currentItem));

    }

    @Click
    void text_one()
    {
    }


    @Click
    void text_three()
    {
    }

    @Click
    void text_four()
    {
    }

    @Click
    void text_five()
    {
    }

    @Click
    void text_six()
    {
    }

    @AfterViews
    void afterOncreate()
    {
        getActionBar().hide();
        commandtab_single.getInstance().clearAll();
        SqliteDb.deleteAllSelectCmdArea(AddCommand_BZSC.this, SelectCmdArea.class);
        SqliteDb.deleteAllSelectCmdArea(AddCommand_BZSC.this, goodslisttab.class);
        SqliteDb.deleteAllSelectCmdArea(AddCommand_BZSC.this, goodslisttab_flsl.class);
        level = getIntent().getStringExtra("level");

        addStd_cmd_stepOne = new AddStd_Cmd_StepOne_Temp_();
        addStd_cmd_stepTwo = new AddStd_Cmd_StepTwo_();
        addStd_cmd_stepThree_temp = new AddStd_Cmd_StepThree_Temp_();
        addStd_cmd_stepFive = new AddStd_Cmd_StepFive_();
        addStd_cmd_stepSix = new AddStd_Cmd_StepSix_();
        fragmentList.add(addStd_cmd_stepOne);
        fragmentList.add(addStd_cmd_stepTwo);
        fragmentList.add(addStd_cmd_stepThree_temp);
        fragmentList.add(addStd_cmd_stepFive);
        fragmentList.add(addStd_cmd_stepSix);

        switchContent(mContent, fragmentList.get(0));
        setBackground(0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    private void setBackground(int pos)
    {

        text_one.setBackgroundResource(R.color.white);
        text_three.setBackgroundResource(R.color.white);
        text_four.setBackgroundResource(R.color.white);
        text_five.setBackgroundResource(R.color.white);
        text_six.setBackgroundResource(R.color.white);

        text_one.setTextColor(Color.parseColor("#5B5B5B"));
        text_three.setTextColor(Color.parseColor("#5B5B5B"));
        text_four.setTextColor(Color.parseColor("#5B5B5B"));
        text_five.setTextColor(Color.parseColor("#5B5B5B"));
        text_six.setTextColor(Color.parseColor("#5B5B5B"));

        switch (pos)
        {
            case 0:
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_next);
                break;

            case 1:
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_next);
                break;
            case 2:
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_next);
                break;
            case 3:
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_red);
                text_five.setBackgroundResource(R.drawable.tag_next);
                break;
            case 4:
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_six.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_red);
                text_five.setBackgroundResource(R.drawable.tag_red);
                text_six.setBackgroundResource(R.drawable.tag_next);
                break;
        }

    }


    public void updateUI(int currentItem)
    {
        setBackground(currentItem);
        switch (currentItem)
        {
            case 0:

                break;
            case 1:
                ((AddStd_Cmd_StepTwo) fragmentList.get(currentItem)).update();
                break;
            case 2:
                addStd_cmd_stepThree_temp.update();
                break;
            case 3:
                break;
            case 4:
                addStd_cmd_stepSix.update();
                break;
        }
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
        View dialog_layout = getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(AddCommand_BZSC.this, R.style.MyDialog, dialog_layout, "取消标准生产指令", "确定取消吗？", "是", "否", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        myDialog.dismiss();
                        commandtab_single.getInstance().clearAll();
                        SqliteDb.deleteAllSelectCmdArea(AddCommand_BZSC.this, SelectCmdArea.class);
                        SqliteDb.deleteAllSelectCmdArea(AddCommand_BZSC.this, goodslisttab.class);
                        SqliteDb.deleteAllSelectCmdArea(AddCommand_BZSC.this, goodslisttab_flsl.class);
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

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = AddCommand_BZSC.this.getSupportFragmentManager().beginTransaction();
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
    public void callbackFun1(Bundle arg)
    {

    }

    @Override
    public void callbackFun2(Bundle arg)
    {

    }

    @Override
    public void callbackFun_setText(Bundle arg)
    {

    }

    @Override
    public void stepTwo_setHeadText(Bundle arg)
    {

    }
}
