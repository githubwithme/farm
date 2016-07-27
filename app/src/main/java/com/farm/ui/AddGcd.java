package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
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

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@EActivity(R.layout.addgcd)
public class AddGcd extends Activity implements TencentLocationListener
{
    LatLng location_latLng;
    int error;
    CountDownLatch latch;
    CustomDialog_ListView customDialog_listView;
    Dictionary dic_comm;
    String zzsl = "";
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    EditText et_plantName;
    @ViewById
    EditText et_plantNote;
    @ViewById
    TextView tv_zzsl;
    @ViewById
    TextView tv_location;
    @ViewById
    ProgressBar pb_upload;
    @ViewById
    Button btn_upload;

    @Click
    void tv_zzsl()
    {
        JSONObject jsonObject = utils.parseJsonFile(AddGcd.this, "dictionary.json");
        JSONArray jsonArray = null;
        try
        {
            jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
        } catch (Exception e)
        {

        }

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        showDialog_workday(list);
    }

    @Click
    void imgbtn_back()
    {
        finish();
    }


    @Click
    void btn_upload()
    {

        if (zzsl.equals("") || et_plantName.getText().equals("") || et_plantNote.getText().equals(""))
        {
            Toast.makeText(AddGcd.this, "请先填选相关信息！", Toast.LENGTH_SHORT).show();
        } else
        {
            btn_upload.setVisibility(View.GONE);
            pb_upload.setVisibility(View.VISIBLE);
            AddData();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(AddGcd.this);
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
        getActionBar().hide();
    }

    private void AddData()
    {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("userName", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkId", commembertab.getparkId());
        params.addQueryStringParameter("parkName", commembertab.getparkName());
        params.addQueryStringParameter("areaId", commembertab.getareaId());
        params.addQueryStringParameter("areaName", commembertab.getareaName());
        params.addQueryStringParameter("action", "plantGCDAdd");

        params.addQueryStringParameter("gcdName", et_plantName.getText().toString());
        params.addQueryStringParameter("gcdNote", et_plantNote.getText().toString());

        params.addQueryStringParameter("x", String.valueOf(location_latLng.getLatitude()));
        params.addQueryStringParameter("y", String.valueOf(location_latLng.getLongitude()));
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
                        JSONObject jb = result.getRows().getJSONObject(0);
                        String gcdid = result.getRows().getJSONObject(0).getString("id");
                        int sl = Integer.valueOf(zzsl);
                        if (sl > 0)
                        {
                            latch = new CountDownLatch(sl);
                            for (int i = 0; i < sl; i++)
                            {
                                AddPlant(gcdid, "植株" + (i + 1), "植株" + (i + 1) + "说明");
                            }

                        } else
                        {
                            Toast.makeText(AddGcd.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            AddGcd.this.finish();
                        }

                    } else
                    {
                        AppContext.makeToast(AddGcd.this, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(AddGcd.this, "error_connectDataBase");
                    pb_upload.setVisibility(View.GONE);
                    btn_upload.setVisibility(View.VISIBLE);
                    return;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
                AppContext.makeToast(AddGcd.this, "error_connectServer");
                pb_upload.setVisibility(View.GONE);
                btn_upload.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showDialog_workday(List<String> list)
    {
        View dialog_layout = AddGcd.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(AddGcd.this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                zzsl = bundle.getString("name");
                tv_zzsl.setText(zzsl);
            }
        });
        customDialog_listView.show();
    }

    private void AddPlant(String gcdid, String plantname, String plantnote)
    {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("userName", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkId", commembertab.getparkId());
        params.addQueryStringParameter("parkName", commembertab.getparkName());
        params.addQueryStringParameter("areaId", commembertab.getareaId());
        params.addQueryStringParameter("gcdId", gcdid);
        params.addQueryStringParameter("gcdName", et_plantName.getText().toString());
        params.addQueryStringParameter("areaName", commembertab.getareaName());
        params.addQueryStringParameter("action", "plantAdd");

        params.addQueryStringParameter("plantName", plantname);
        params.addQueryStringParameter("plantNote", plantnote);
        params.addQueryStringParameter("plantType", "0");


        params.addQueryStringParameter("x", String.valueOf(location_latLng.getLatitude()));
        params.addQueryStringParameter("y", String.valueOf(location_latLng.getLongitude()));
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
                        showProgress();
                    } else
                    {
                        AppContext.makeToast(AddGcd.this, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(AddGcd.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
                AppContext.makeToast(AddGcd.this, "error_connectServer");
                AppContext.makeToast(AddGcd.this, "error_connectServer");
            }
        });
    }

    private void showProgress()
    {
        latch.countDown();
        Long l = latch.getCount();
        if (l.intValue() == 0) // 全部线程是否已经结束
        {
            Toast.makeText(AddGcd.this, "保存成功！", Toast.LENGTH_SHORT).show();
            AddGcd.this.finish();
        }
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s)
    {
        if (TencentLocation.ERROR_OK == error) // 定位成功
        {
            // 用于定位
            location_latLng = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
            //全局记录坐标
            AppContext appContext = (AppContext) AddGcd.this.getApplication();
            appContext.setLOCATION_X(String.valueOf(location_latLng.getLatitude()));
            appContext.setLOCATION_Y(String.valueOf(location_latLng.getLongitude()));
            tv_location.setText("经度" + location_latLng.getLatitude() + "\n" + "纬度" + location_latLng.getLongitude());
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1)
    {

    }
}
