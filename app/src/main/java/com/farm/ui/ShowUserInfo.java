package com.farm.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomDialog_CallTip;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.showuserinfo)
public class ShowUserInfo extends Activity
{
    String type;
    CustomDialog_CallTip custom_calltip;
    commembertab commembertab;
    @ViewById
    CircleImageView circle_img;
    @ViewById
    TextView tv_name;
    @ViewById
    Button btn_call;
    @ViewById
    TextView tv_contractName;
    @ViewById
    TextView tv_zw;
    @ViewById
    TextView tv_phone;
    @ViewById
    TextView tv_area;
    @ViewById
    TextView tv_userWX;
    @ViewById
    TextView tv_userQQ;
    String userid;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void btn_call()
    {
        showDialog_addsaleinfo(commembertab.getuserCell());
    }

    @AfterViews
    void afterOncreate()
    {
        getShowUserInfo(userid);
//        showData(commembertab);
//		getShowUserInfo(userid);
//		BitmapHelper.setImageViewBackground(ShowUserInfo.this, circle_img, "http://img.popoho.com/UploadPic/2010-12/201012297441441.png");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        type = getIntent().getStringExtra("type");
        userid = getIntent().getStringExtra("userid");
        commembertab = AppContext.getUserInfo(ShowUserInfo.this);
    }

    private void getShowUserInfo(String userid)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", userid);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("action", "getContactsDetailData");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<commembertab> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), commembertab.class);
                        if (listData == null)
                        {
                            Toast.makeText(ShowUserInfo.this, "连接数据服务器出错了!", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            showData(listData.get(0));
                        }
                    } else
                    {
                        Toast.makeText(ShowUserInfo.this, "连接数据服务器出错了!", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    Toast.makeText(ShowUserInfo.this, "连接数据服务器出错了!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                Toast.makeText(ShowUserInfo.this, "连接服务器出错了!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showData(commembertab commembertab)
    {
        if (commembertab.getnlevel().equals("0"))
        {
            tv_zw.setText("农场主");
        } else if (commembertab.getnlevel().equals("1"))
        {
            tv_zw.setText("场长");
        } else
        {
            tv_zw.setText("片管");
        }
        tv_area.setText(commembertab.getparkName() + "-" + commembertab.getareaName());
        tv_name.setText(commembertab.getrealName());
        tv_userWX.setText(commembertab.getuserWX());
        tv_userQQ.setText(commembertab.userQQ);
        tv_phone.setText(commembertab.getuserCell());
        tv_contractName.setText(commembertab.getcontractName());
        BitmapHelper.setImageViewBackground(ShowUserInfo.this, circle_img, AppConfig.baseurl + commembertab.getimgurl());
    }

    public void showDialog_addsaleinfo(final String phone)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(ShowUserInfo.this).inflate(R.layout.customdialog_calltip, null);
        custom_calltip = new CustomDialog_CallTip(ShowUserInfo.this, R.style.MyDialog, dialog_layout);
        TextView tv_tips = (TextView) dialog_layout.findViewById(R.id.tv_tips);
        tv_tips.setText(phone + "拨打这个电话吗?");
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                custom_calltip.dismiss();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(ShowUserInfo.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                ShowUserInfo.this.startActivity(intent);
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                custom_calltip.dismiss();
            }
        });
        custom_calltip.show();
    }

}
