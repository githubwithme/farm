package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/1/21.
 */
@EFragment
public class Fragment_MainSales extends Fragment
{
    com.farm.bean.commembertab commembertab;
    Fragment mContent = new Fragment();
    Fragment_Sale sale_fragment;
    Fragment_Order fragment_order;
    @ViewById
    ImageButton btn_back;
    @ViewById
    TextView tv_product;
    @ViewById
    TextView tv_order;


    @Click
    void btn_back()
    {
       getActivity().finish();
    }

    @Click
    void tv_product()
    {
        setBackground(0);
        switchContent(mContent, sale_fragment);
    }
    @Click
    void tv_order()
    {
        setBackground(1);
        switchContent(mContent, fragment_order);
    }
    @Click
    void tv_smallsales()
    {
        setBackground(1);
        switchContent(mContent, fragment_order);
    }

    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        switchContent(mContent, sale_fragment);
    }

    private void setBackground(int pos)
    {
        tv_product.setSelected(false);
        tv_order.setSelected(false);

        tv_product.setBackgroundResource(R.color.white);
        tv_order.setBackgroundResource(R.color.white);

        tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_order.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_product.setSelected(false);
                tv_product.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_product.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_order.setSelected(false);
                tv_order.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_order.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_mainsales, container, false);
        sale_fragment = new Fragment_Sale_();
        fragment_order = new Fragment_Order_();
        return rootView;
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
