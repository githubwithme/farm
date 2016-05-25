package com.farm.ui;

import android.app.Activity;

import com.farm.R;

import org.androidannotations.annotations.EActivity;

/**
 * Created by ${hmj} on 2016/5/25.
 */
@EActivity(R.layout.ncz_finance)
public class NCZ_Finance extends Activity
{
//    SpecialCostFragment specialCostFragment;
//    LargeCostFragment largeCostFragment;
//    SmallCostFragment smallCostFragment;
//    Fragment mContent = new Fragment();
//    @ViewById
//    TextView tv_largecost;
//    @ViewById
//    TextView tv_specialcost;
//    @ViewById
//    TextView tv_smallcost;
//
//
//    @Click
//    void tv_specialcost()
//    {
//        setBackground(0);
//        switchContent(mContent, specialCostFragment);
//    }
//
//    @Click
//    void tv_largecost()
//    {
//        setBackground(1);
//        switchContent(mContent, largeCostFragment);
//    }
//
//    @Click
//    void tv_smallcost()
//    {
//        setBackground(2);
//        switchContent(mContent, smallCostFragment);
//    }
//
//    @AfterViews
//    void afterOncreate()
//    {
//        setBackground(0);
//        switchContent(mContent, specialCostFragment);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        getActionBar().hide();
//        specialCostFragment = new SpecialCostFragment_();
//        largeCostFragment = new LargeCostFragment_();
//        smallCostFragment = new SmallCostFragment_();
//    }
//
//
//    public void switchContent(Fragment from, Fragment to)
//    {
//        if (mContent != to)
//        {
//            mContent = to;
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            if (!to.isAdded())
//            { // 先判断是否被add过
//                transaction.hide(from).add(R.id.fl_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
//            } else
//            {
//                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
//            }
//        }
//    }
//
//    private void setBackground(int pos)
//    {
//        tv_specialcost.setSelected(false);
//        tv_largecost.setSelected(false);
//        tv_smallcost.setSelected(false);
//
//        tv_specialcost.setBackgroundResource(R.color.white);
//        tv_largecost.setBackgroundResource(R.color.white);
//        tv_smallcost.setBackgroundResource(R.color.white);
//
//        tv_specialcost.setTextColor(getResources().getColor(R.color.menu_textcolor));
//        tv_largecost.setTextColor(getResources().getColor(R.color.menu_textcolor));
//        tv_smallcost.setTextColor(getResources().getColor(R.color.menu_textcolor));
//        switch (pos)
//        {
//            case 0:
//                tv_specialcost.setSelected(false);
//                tv_specialcost.setTextColor(getResources().getColor(R.color.bg_blue));
//                tv_specialcost.setBackgroundResource(R.drawable.red_bottom);
//                break;
//            case 1:
//                tv_largecost.setSelected(false);
//                tv_largecost.setTextColor(getResources().getColor(R.color.bg_blue));
//                tv_largecost.setBackgroundResource(R.drawable.red_bottom);
//                break;
//
//            case 2:
//                tv_smallcost.setSelected(false);
//                tv_smallcost.setTextColor(getResources().getColor(R.color.bg_blue));
//                tv_smallcost.setBackgroundResource(R.drawable.red_bottom);
//                break;
//        }
//
//    }
}
