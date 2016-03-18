package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/1/21.
 */
@EActivity(R.layout.cz_makemap)
public class CZ_MakeMap extends Activity
{
    com.farm.bean.commembertab commembertab;
    Fragment mContent = new Fragment();
//    Fragment_Order fragment_order;
//    Fragment_CurrentSale fragment_currentSale;
    CZ_MakeMap_MakeLayer cz_makeMap_makeLayer;
    CZ_MakeMap_ShowLayer cz_makeMap_showLayer;
    CZ_MakeMap_OperateLayer cz_makeMap_operateLayer;
    @ViewById
    ImageButton btn_back;
    @ViewById
    TextView tv_show;
    @ViewById
    TextView tv_operate;
    @ViewById
    TextView tv_make;


    @Click
    void btn_back()
    {
        CZ_MakeMap.this.finish();
    }

    @Click
    void tv_show()
    {
        setBackground(0);
        switchContent(mContent, cz_makeMap_showLayer);
    }
    @Click
    void tv_make()
    {
        setBackground(2);
        switchContent(mContent, cz_makeMap_makeLayer);
    }

    @Click
    void tv_operate()
    {
        setBackground(1);
        switchContent(mContent, cz_makeMap_operateLayer);
    }



    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        switchContent(mContent, cz_makeMap_showLayer);
    }

    private void setBackground(int pos)
    {
        tv_show.setSelected(false);
        tv_operate.setSelected(false);
        tv_make.setSelected(false);

        tv_show.setBackgroundResource(R.color.white);
        tv_operate.setBackgroundResource(R.color.white);
        tv_make.setBackgroundResource(R.color.white);

        tv_show.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_operate.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_make.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_show.setSelected(false);
                tv_show.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_show.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_operate.setSelected(false);
                tv_operate.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_operate.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 2:
                tv_make.setSelected(false);
                tv_make.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_make.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        cz_makeMap_makeLayer=new  CZ_MakeMap_MakeLayer_();
        cz_makeMap_showLayer=new  CZ_MakeMap_ShowLayer_();
        cz_makeMap_operateLayer=new CZ_MakeMap_OperateLayer_();
    }


    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container_mainsales, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
