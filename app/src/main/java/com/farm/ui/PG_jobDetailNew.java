package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.jobtab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2016/6/12.
 */
@EActivity(R.layout.pg_jobdetailnew)
public class PG_jobDetailNew extends Activity
{
    PG_JobDetail pg_jobDetail;
    PG_cbhdetail pg_cbhdetail;
    jobtab jobtab;
    Fragment mContent = new Fragment();
    @ViewById
    TextView tv_jobdetail;
    @ViewById
    TextView tv_cbhdetail;
    @ViewById
    ImageButton imgbtn_back;
    @Click
    void imgbtn_back()
    {
        finish();
    }
    @Click
    void tv_jobdetail()
    {
        setBackground(0);
        switchContent(mContent, pg_jobDetail);
    }
    @Click
    void tv_cbhdetail()
    {
        setBackground(1);
        switchContent(mContent, pg_cbhdetail);
    }
    @AfterViews
    void afterview()
    {

        setBackground(0);
        switchContent(mContent, pg_jobDetail);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        jobtab =getIntent().getParcelableExtra("bean");
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean",jobtab);
        pg_jobDetail=new PG_JobDetail_();
        pg_cbhdetail=new PG_cbhdetail_();
        pg_jobDetail.setArguments(bundle);
        pg_cbhdetail.setArguments(bundle);
    }
    private void setBackground(int pos)
    {
        tv_jobdetail.setSelected(false);
        tv_cbhdetail.setSelected(false);

        tv_jobdetail.setBackgroundResource(R.color.white);
        tv_cbhdetail.setBackgroundResource(R.color.white);

        switch (pos)
        {
            case 0:
                tv_jobdetail.setSelected(false);
                tv_jobdetail.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_cbhdetail.setSelected(false);
                tv_cbhdetail.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }
    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.event_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
