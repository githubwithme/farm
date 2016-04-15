package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.app.AppManager;
import com.farm.bean.ExceptionInfo;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.MyDialog;
import com.farm.widget.MyDialog.CustomDialogListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.LatLng;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.cz_activity)
public class CZ_MainActivity extends Activity implements TencentLocationListener
{
    commembertab commembertab;
    LatLng location_latLng = new LatLng(24.430833, 113.298611);// 初始化定位
    Long newtime = 0L;
    Long lasttime = 0L;
    int error;
    MyDialog myDialog;
    Fragment mContent = new Fragment();
    CZ_MainFragment mainFragment;
    CZ_FeedbackOfSale cz_feedbackOfSale;
    IFragment iFragment;
    PG_ListOfEvents pg_listOfEvents;
    @ViewById
    ImageButton imgbtn_home;
    @ViewById
    ImageButton imgbtn_me;
    @ViewById
    ImageButton imgbtn_sale;
    @ViewById
    ImageButton imgbtn_event;

    @ViewById
    TextView tv_home;
    @ViewById
    TextView tv_me;
    @ViewById
    TextView tv_sale;
    @ViewById
    TextView tv_event;

    @ViewById
    TableLayout tl_home;
    @ViewById
    TableLayout tl_me;
    @ViewById
    TableLayout tl_sale;
    @ViewById
    TableLayout tl_event;

    @Click
    void tl_home()
    {
        tv_home.setTextColor(getResources().getColor(R.color.bg_blue));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_event.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_home.setSelected(true);
        tl_me.setSelected(false);
        tl_sale.setSelected(false);
        tl_event.setSelected(false);
        switchContent(mContent, mainFragment);
    }

    @Click
    void tl_sale()
    {
        tv_sale.setTextColor(getResources().getColor(R.color.bg_blue));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_event.setTextColor(getResources().getColor(R.color.menu_textcolor));

        tl_sale.setSelected(true);
        tl_home.setSelected(false);
        tl_me.setSelected(false);
        tl_event.setSelected(false);
        switchContent(mContent, cz_feedbackOfSale);
    }

    @Click
    void tl_me()
    {
        tv_me.setTextColor(getResources().getColor(R.color.bg_blue));
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_event.setTextColor(getResources().getColor(R.color.menu_textcolor));


        tl_me.setSelected(true);
        tl_home.setSelected(false);
        tl_sale.setSelected(false);
        tl_event.setSelected(false);
        switchContent(mContent, iFragment);
    }
    @Click
    void tl_event()
    {
        tv_event.setTextColor(getResources().getColor(R.color.bg_blue));
        tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));


        tl_event.setSelected(true);
        tl_home.setSelected(false);
        tl_sale.setSelected(false);
        tl_me.setSelected(false);
        switchContent(mContent, pg_listOfEvents);
    }

    @AfterViews
    void afterOncreate()
    {
        //将错误信息提交
        List<ExceptionInfo> list_exception = SqliteDb.getExceptionInfo(CZ_MainActivity.this);
        if (list_exception != null)
        {
            for (int i = 0; i < list_exception.size(); i++)
            {
                sendExceptionInfoToServer(list_exception.get(i));
            }
        }

        switchContent(mContent, mainFragment);
        tv_home.setTextColor(getResources().getColor(R.color.red));
        tl_home.setSelected(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        AppManager.getAppManager().addActivity(this);
        mainFragment = new CZ_MainFragment_();
        iFragment = new IFragment_();
        cz_feedbackOfSale = new CZ_FeedbackOfSale_();
        pg_listOfEvents=new PG_ListOfEvents_();
        commembertab = AppContext.getUserInfo(CZ_MainActivity.this);

        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(CZ_MainActivity.this);
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            showExistTip();
        }
        return false;

    }

    private void showExistTip()
    {
        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(CZ_MainActivity.this, R.style.MyDialog, dialog_layout, "确定退出吗？", "确定退出吗？", "退出", "取消", new CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        finish();
                        break;
                    case R.id.btn_cancle:
                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason)
    {
        if (TencentLocation.ERROR_OK == error) // 定位成功
        {
//            Gps gPS= CoordinateConvertUtil.gps84_To_Gcj02(location.getLatitude(), location.getLongitude());
//            location_latLng = new LatLng(gPS.getWgLat(), gPS.getWgLon());
            // 用于定位
            location_latLng = new LatLng(location.getLatitude(), location.getLongitude());
            AppContext appContext = (AppContext) CZ_MainActivity.this.getApplication();
            appContext.setLOCATION_X(String.valueOf(location_latLng.getLatitude()));
            appContext.setLOCATION_Y(String.valueOf(location_latLng.getLongitude()));
            // 每隔15秒记录轨迹
            newtime = System.currentTimeMillis();
            int diff = (int) (newtime - lasttime) / 1000;
            if (diff > 3)// 每隔15秒记录一次
            {
                lasttime = newtime;
                MarkLocation(location);
//                Toast.makeText(CZ_MainActivity.this,location.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1)
    {

    }

    private void MarkLocation(TencentLocation location)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "AddLocationInfo");
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("parkid", commembertab.getparkId());
        params.addQueryStringParameter("parkname", commembertab.getparkName());
        params.addQueryStringParameter("areaid", commembertab.getareaId());
        params.addQueryStringParameter("areaname", commembertab.getareaName());
        params.addQueryStringParameter("lat", String.valueOf(location.getLatitude()));
        params.addQueryStringParameter("lng", String.valueOf(location.getLongitude()));
        params.addQueryStringParameter("time", utils.getTime());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
//                    Toast.makeText(CZ_MainActivity.this,"位置获取成功",Toast.LENGTH_SHORT).show();
                } else
                {
//                    Toast.makeText(CZ_MainActivity.this,"位置错误",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
//                Toast.makeText(CZ_MainActivity.this,"位置错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendExceptionInfoToServer(final ExceptionInfo exception)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("UUID", exception.getUuid());
        params.addQueryStringParameter("exceptionInfo", exception.getExceptionInfo());
        params.addQueryStringParameter("userid", exception.getUserid());
        params.addQueryStringParameter("username", exception.getUsername());
        params.addQueryStringParameter("action", "saveAppException");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        SqliteDb.deleteExceptionInfo(CZ_MainActivity.this, exception.getExceptionid());
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
            }
        });

    }


}
