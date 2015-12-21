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

@EActivity(R.layout.addplantobservation)
public class AddPlantObservation extends FragmentActivity implements FragmentCallBack
{
    private ArrayList<Fragment> fragmentList;
    @ViewById
    TextView text_one;
    @ViewById
    TextView text_three;
    @ViewById
    TextView text_four;
    @ViewById
    TextView text_five;
    @ViewById
    ImageView image_one;
    @ViewById
    ImageView image_three;
    @ViewById
    ImageView image_four;
    @ViewById
    ImageView image_five;
    @ViewById
    android.support.v4.view.ViewPager vPager;

    @Click
    void imgbtn_back()
    {
        finish();
    }

    @Click
    void text_one()
    {
        vPager.setCurrentItem(0);
    }


    @Click
    void text_three()
    {
        vPager.setCurrentItem(1);
    }

    @Click
    void text_four()
    {
        vPager.setCurrentItem(2);
    }

    @Click
    void text_five()
    {
        vPager.setCurrentItem(3);
    }

    @Click
    void text_six()
    {
        vPager.setCurrentItem(4);
    }

    @AfterViews
    void afterOncreate()
    {
        fragmentList = new ArrayList<Fragment>();
        AddPlantObservation_StepOne addPlantObservation_stepOne = new AddPlantObservation_StepOne_();
        AddPlantObservation_StepTwo addPlantObservation_stepTwo = new AddPlantObservation_StepTwo_();
        AddPlantObservation_StepThree addPlantObservation_stepThree = new AddPlantObservation_StepThree_();
        AddPlantObservation_stepfour addPlantObservation_stepfour = new AddPlantObservation_stepfour_();

        fragmentList.add(addPlantObservation_stepOne);
        fragmentList.add(addPlantObservation_stepTwo);
        fragmentList.add(addPlantObservation_stepThree);
        fragmentList.add(addPlantObservation_stepfour);

        setBackground(0);
        //关闭预加载，默认一次只加载一个Fragment
        vPager.setOffscreenPageLimit(1);
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(AddPlantObservation.this.getSupportFragmentManager(), vPager, fragmentList);

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
        image_three.setBackgroundResource(R.drawable.line);
        image_four.setBackgroundResource(R.drawable.line);
        image_five.setBackgroundResource(R.drawable.line);

        text_one.setBackgroundResource(R.color.white);
        text_three.setBackgroundResource(R.color.white);
        text_four.setBackgroundResource(R.color.white);
        text_five.setBackgroundResource(R.color.white);

        text_one.setTextColor(Color.parseColor("#5B5B5B"));
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
                image_three.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_next);
                break;
            case 2:
                image_four.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
                text_three.setBackgroundResource(R.drawable.tag_red);
                text_four.setBackgroundResource(R.drawable.tag_next);
                break;
            case 3:
                image_five.setBackgroundResource(R.drawable.green_line);
                text_one.setTextColor(Color.parseColor("#FFFFFF"));
                text_three.setTextColor(Color.parseColor("#FFFFFF"));
                text_four.setTextColor(Color.parseColor("#FFFFFF"));
                text_five.setTextColor(Color.parseColor("#FFFFFF"));
                text_one.setBackgroundResource(R.drawable.tag_red);
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
    public void callbackFun_setText(Bundle arg)
    {
    }    @Override
    public void stepTwo_setHeadText(Bundle arg)
    {
    }

    @Override
    public void callbackFun2(Bundle arg)
    {
        int currentItem = arg.getInt("INDEX");
        switch (currentItem)
        {
            case 0:
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
            case 4:
                vPager.setCurrentItem(currentItem + 1);
                break;
        }
    }
}
