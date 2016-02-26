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
public class ProductSale extends Fragment
{
    com.farm.bean.commembertab commembertab;
    Fragment mContent = new Fragment();
    Fragment_DL dl_fragment;
    Fragment_Sale sale_fragment;
    @ViewById
    ImageButton btn_back;
    @ViewById
    TextView tv_dl;
    @ViewById
    TextView tv_sale;


    @Click
    void btn_back()
    {
        getActivity().finish();
    }


    @Click
    void tv_dl()
    {
        setBackground(0);
        switchContent(mContent, dl_fragment);
    }

    @Click
    void tv_sale()
    {
        setBackground(1);
        switchContent(mContent, sale_fragment);
    }

    @AfterViews
    void afterOncreate()
    {
        setBackground(0);
        switchContent(mContent, dl_fragment);
    }

    private void setBackground(int pos)
    {
        tv_dl.setSelected(false);
        tv_sale.setSelected(false);

        tv_dl.setBackgroundResource(R.color.white);
        tv_sale.setBackgroundResource(R.color.white);

        tv_dl.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_dl.setSelected(false);
                tv_dl.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_dl.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_sale.setSelected(false);
                tv_sale.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_sale.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.productsale, container, false);
        dl_fragment = new Fragment_DL_();
        sale_fragment = new Fragment_Sale_();
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
                transaction.hide(from).add(R.id.fl_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
