package com.farm.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.FragmentViewPagerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_add_std__cmd)
public class AddStd_Cmd extends FragmentActivity
{
    private ArrayList<Fragment> fragmentList;
    @ViewById
    TextView text_one;
    @ViewById
    TextView text_two;
    @ViewById
    TextView text_three;
    @ViewById
    ImageView image_one;
    @ViewById
    ImageView image_two;
    @ViewById
    ImageView image_three;
    @ViewById
    android.support.v4.view.ViewPager vPager;

    @AfterViews
    void afterOncreate()
    {
        fragmentList = new ArrayList<Fragment>();
        AddStd_Cmd_StepOne addStd_cmd_stepOne=new AddStd_Cmd_StepOne();
        AddStd_Cmd_StepThree addStd_cmd_stepThree=new AddStd_Cmd_StepThree();
        AddStd_Cmd_StepTwo addStd_cmd_stepTwo=new AddStd_Cmd_StepTwo();
        fragmentList.add(addStd_cmd_stepOne);
        fragmentList.add(addStd_cmd_stepTwo);
        fragmentList.add(addStd_cmd_stepThree);

        setBackground(0);
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

        text_one.setTextColor(Color.parseColor("#5B5B5B"));
        text_two.setTextColor(Color.parseColor("#5B5B5B"));
        text_three.setTextColor(Color.parseColor("#5B5B5B"));

        switch (pos)
        {
            case 0:
                image_one.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#45C01A"));
                break;
            case 1:
                image_two.setBackgroundResource(R.drawable.green_line);
                text_two.setTextColor(Color.parseColor("#45C01A"));
                break;
            case 2:
                image_three.setBackgroundResource(R.drawable.green_line);
                text_three.setTextColor(Color.parseColor("#45C01A"));
                break;
        }

    }
}
