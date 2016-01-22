package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.farm.R;
import com.farm.bean.commandtab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.commanddetail)
public class NCZ_CommandDetail extends Activity
{
    Fragment mContent = new Fragment();
    Fragment_CommandDetail fragment_commandDetail;
    Fragment_CommandExecute fragment_commandExecute;
    commandtab commandtab;
    @ViewById
    ImageButton btn_back;
    @ViewById
    Button btn_detail;
    @ViewById
    Button btn_execute;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void btn_execute()
    {
        switchContent(mContent, fragment_commandExecute);
        btn_detail.setBackgroundResource(R.color.white);
        btn_execute.setBackgroundResource(R.color.bg_blue);
        btn_detail.setBackgroundResource(R.drawable.white);
        btn_execute.setBackgroundResource(R.drawable.red_bottom);
    }

    @Click
    void btn_detail()
    {
        switchContent(mContent, fragment_commandDetail);
        btn_detail.setBackgroundResource(R.color.bg_blue);
        btn_execute.setBackgroundResource(R.color.white);
        btn_detail.setBackgroundResource(R.drawable.red_bottom);
        btn_execute.setBackgroundResource(R.drawable.white);

    }

    @AfterViews
    void afterOncreate()
    {
        switchContent(mContent, fragment_commandDetail);
        btn_detail.setBackgroundResource(R.drawable.red_bottom);
        btn_execute.setBackgroundResource(R.drawable.white);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commandtab = getIntent().getParcelableExtra("bean");
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", commandtab);
        fragment_commandDetail = new Fragment_CommandDetail_();
        fragment_commandDetail.setArguments(bundle);
        fragment_commandExecute = new Fragment_CommandExecute_();
        fragment_commandExecute.setArguments(bundle);
    }

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }


}
