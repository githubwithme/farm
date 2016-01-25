package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
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

@EActivity(R.layout.edituserinfo)
public class EditUserInfo extends Activity
{
    commembertab commembertab;
    @ViewById
    CircleImageView circle_img;
    @ViewById
    TextView tv_name;
    @ViewById
    EditText tv_contractName;
    @ViewById
    TextView tv_zw;
    @ViewById
    EditText tv_phone;
    @ViewById
    EditText tv_area;
    @ViewById
    EditText tv_userWX;
    @ViewById
    EditText tv_userQQ;
    String userid;

    @Click
    void ib_back()
    {
        finish();
    }
    @Click
    void btn_delete()
    {

    }


    @AfterViews
    void afterOncreate()
    {
        showData(commembertab);
//		getShowUserInfo(userid);
//		BitmapHelper.setImageViewBackground(EditUserInfo.this, circle_img, "http://img.popoho.com/UploadPic/2010-12/201012297441441.png");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        userid = getIntent().getStringExtra("userid");
        commembertab = AppContext.getUserInfo(EditUserInfo.this);
    }

    private void getShowUserInfo(String userid)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", userid);
        params.addQueryStringParameter("action", "getUserInfo");
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
                            Toast.makeText(EditUserInfo.this, "获取数据异常!", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            showData(listData.get(0));
                        }
                    } else
                    {
                        Toast.makeText(EditUserInfo.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    Toast.makeText(EditUserInfo.this, "连接数据服务器出错了!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                Toast.makeText(EditUserInfo.this, "连接服务器出错了!", Toast.LENGTH_SHORT).show();
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
        BitmapHelper.setImageViewBackground(EditUserInfo.this, circle_img, AppConfig.baseurl + commembertab.getimgurl());
    }

}
