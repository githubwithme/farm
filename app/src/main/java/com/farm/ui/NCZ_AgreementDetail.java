package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.AgreementBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/7/9.
 */
@EActivity(R.layout.ncz_agreementdetail)
public class NCZ_AgreementDetail extends Activity
{
    @ViewById
    TextView tv_date;
    @ViewById
    TextView tv_jf;
    @ViewById
    TextView tv_yf;
    @ViewById
    TextView tv_bh;

    @AfterViews
    void AfterOncreate()
    {
        AgreementBean agreementBean = getIntent().getParcelableExtra("bean");
        String name = getIntent().getStringExtra("name");
        tv_date.setText(agreementBean.getAgreementTime().substring(0,agreementBean.getAgreementTime().lastIndexOf(" ")));
        tv_yf.setText(name);
        tv_bh.setText(agreementBean.getAgreementNumber());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
}
