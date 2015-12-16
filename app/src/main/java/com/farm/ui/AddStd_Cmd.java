package com.farm.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.FragmentViewPagerAdapter;
import com.farm.com.custominterface.FragmentCallBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_add_std__cmd)
public class AddStd_Cmd extends FragmentActivity implements FragmentCallBack
{
    private ArrayList<Fragment> fragmentList;
    @ViewById TextView text_one;
    @ViewById TextView text_two;
    @ViewById TextView text_three;
    @ViewById TextView text_four;
    @ViewById TextView text_five;
    @ViewById ImageView image_one;
    @ViewById ImageView image_two;
    @ViewById ImageView image_three;
    @ViewById ImageView image_four;
    @ViewById ImageView image_five;
    @ViewById android.support.v4.view.ViewPager vPager;

    @Click
    void text_one()
    {
        vPager.setCurrentItem(0);
    }

    @Click
    void text_two()
    {
        vPager.setCurrentItem(1);
    }

    @Click
    void text_three()
    {
        vPager.setCurrentItem(2);
    }

    @Click
    void text_four()
    {
        vPager.setCurrentItem(3);
    }

    @Click
    void text_five()
    {
        vPager.setCurrentItem(4);
    }

    @AfterViews
    void afterOncreate()
    {
        fragmentList = new ArrayList<Fragment>();
        AddStd_Cmd_StepOne addStd_cmd_stepOne = new AddStd_Cmd_StepOne_();

        AddStd_Cmd_StepTwo addStd_cmd_stepTwo = new AddStd_Cmd_StepTwo_();
        String[] sn = new String[]{""};
        Bundle bundle = new Bundle();
        bundle.putStringArray("SN", sn);
        addStd_cmd_stepTwo.setArguments(bundle);

        AddStd_Cmd_StepThree addStd_cmd_stepThree = new AddStd_Cmd_StepThree_();
        AddStd_Cmd_StepFour addStd_cmd_stepFour = new AddStd_Cmd_StepFour_();
        AddStd_Cmd_StepFive addStd_cmd_stepFive = new AddStd_Cmd_StepFive_();


        fragmentList.add(addStd_cmd_stepOne);
        fragmentList.add(addStd_cmd_stepTwo);
        fragmentList.add(addStd_cmd_stepThree);
        fragmentList.add(addStd_cmd_stepFour);
        fragmentList.add(addStd_cmd_stepFive);

        setBackground(0);
        //关闭预加载，默认一次只加载一个Fragment
        vPager.setOffscreenPageLimit(1);
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(AddStd_Cmd.this.getSupportFragmentManager(), vPager, fragmentList);

        adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageSelected(int i)
            {
                setBackground(i);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    private void setBackground(int pos)
    {
        image_one.setBackgroundResource(R.drawable.line);
        image_two.setBackgroundResource(R.drawable.line);
        image_three.setBackgroundResource(R.drawable.line);
        image_four.setBackgroundResource(R.drawable.line);
        image_five.setBackgroundResource(R.drawable.line);

        text_one.setBackgroundResource(R.color.white);
        text_two.setBackgroundResource(R.color.white);
        text_three.setBackgroundResource(R.color.white);
        text_four.setBackgroundResource(R.color.white);
        text_five.setBackgroundResource(R.color.white);

        text_one.setTextColor(Color.parseColor("#5B5B5B"));
        text_two.setTextColor(Color.parseColor("#5B5B5B"));
        text_three.setTextColor(Color.parseColor("#5B5B5B"));
        text_four.setTextColor(Color.parseColor("#5B5B5B"));
        text_five.setTextColor(Color.parseColor("#5B5B5B"));

        switch (pos)
        {
            case 0:
                image_one.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_next);
                break;
            case 1:
                image_two.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_two.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_two.setBackgroundResource(R.drawable.tag_next);
                break;
            case 2:
                image_three.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_two.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_two.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_next);
                break;
            case 3:
                image_four.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_two.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_two.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_next);
                break;
            case 4:
                image_five.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_two.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_two.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_red);
                text_five.setBackgroundResource(R.drawable.tag_next);
                break;
        }

    }

    @Override
    public void callbackFun1(Bundle arg)
    {
//        switchFragment();//通过回调方式切换
    }

    @Override
    public void callbackFun2(Bundle arg)
    {
        int currentItem = arg.getInt("INDEX");
        switch (currentItem)
        {
            case 0:
                String[] sn = arg.getStringArray("SN");
                Bundle bundle = new Bundle();
                bundle.putStringArray("SN", sn);
                AddStd_Cmd_StepTwo addStd_cmd_stepTwo = new AddStd_Cmd_StepTwo_();
                addStd_cmd_stepTwo.setArguments(bundle);
                fragmentList.set(currentItem + 1, addStd_cmd_stepTwo);
                vPager.setCurrentItem(currentItem + 1);
                break;
            case 1:
                vPager.setCurrentItem(currentItem + 1);
                break;
            case 2:
                vPager.setCurrentItem(currentItem + 1);
            case 3:
                vPager.setCurrentItem(currentItem + 1);
                break;
        }
    }
}
