package com.farm.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.PagerAdapter_AddPlantObservation;
import com.farm.bean.plantgrowthtab_single;
import com.farm.com.custominterface.FragmentCallBack_AddPlantObservation;
import com.farm.widget.MyDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.addplantobservation)
public class AddPlantObservation extends FragmentActivity implements FragmentCallBack_AddPlantObservation
{
    String gcdid = "";
    MyDialog myDialog;
    PagerAdapter_AddPlantObservation adapter;
    int currentItem;
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
        fragmentList = new ArrayList<Fragment>();
        AddPlantObservation_StepTwo addPlantObservation_stepTwo = new AddPlantObservation_StepTwo_();
        AddPlantObservation_StepThree addPlantObservation_stepThree = new AddPlantObservation_StepThree_();
        AddPlantObservation_stepfour addPlantObservation_stepfour = new AddPlantObservation_stepfour_();
        AddPlantObservation_stepFive addPlantObservation_stepFive = new AddPlantObservation_stepFive_();

        fragmentList.add(addPlantObservation_stepTwo);
        fragmentList.add(addPlantObservation_stepThree);
        fragmentList.add(addPlantObservation_stepfour);
        fragmentList.add(addPlantObservation_stepFive);

        setBackground(0);
        //关闭预加载，默认一次只加载一个Fragment
        vPager.setOffscreenPageLimit(1);
        adapter = new PagerAdapter_AddPlantObservation(AddPlantObservation.this.getSupportFragmentManager(), vPager, fragmentList);

        adapter.setOnExtraPageChangeListener(new PagerAdapter_AddPlantObservation.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageSelected(int i)
            {
                setBackground(i);
                currentItem = i;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        gcdid = getIntent().getStringExtra("gcdid");
        getActionBar().hide();
    }


    private void setBackground(int pos)
    {
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
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_next);
                break;
            case 1:
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_next);
                break;
            case 2:
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_red);
                text_five.setBackgroundResource(R.drawable.tag_next);
                break;
            case 3:
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
    }

    @Override
    public String getGcdId()
    {
        return gcdid;
    }

    @Override
    public Fragment getFragment(Bundle arg)
    {
        int index = Integer.valueOf(arg.getString("index"));
        Fragment fragment = adapter.getFragment(index);
        return fragment;
    }


    @Override
    public void callbackFun2(Bundle arg)
    {
        switch (currentItem)
        {
            case 0:
                break;
            case 1:
                AddPlantObservation_StepTwo fragment1 = (AddPlantObservation_StepTwo) adapter.getFragment(0);
                String gcq = fragment1.getGcq();
                AddPlantObservation_stepfour fragment2 = (AddPlantObservation_stepfour) adapter.getFragment(2);
                fragment2.updateData(gcq, "香蕉");
                break;
            case 2:
                AddPlantObservation_stepFive fragment3 = (AddPlantObservation_stepFive) adapter.getFragment(3);
                List<Fragment> list_fragment = (List<Fragment>) adapter.getAllFragment();
                fragment3.updateData(list_fragment);
                break;
            case 3:
                break;

        }
        vPager.setCurrentItem(currentItem + 1);//要放到后面，否则切换之后currentitem会变
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
        myDialog = new MyDialog(AddPlantObservation.this, R.style.MyDialog, dialog_layout, "取消观测", "确定取消吗？", "是", "否", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        myDialog.dismiss();
                        plantgrowthtab_single.getInstance().clearAll();
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
