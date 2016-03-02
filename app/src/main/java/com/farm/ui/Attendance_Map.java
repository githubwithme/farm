package com.farm.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.Attendance_Park_Adapter;
import com.farm.adapter.DL_ZS_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BoundaryBean;
import com.farm.bean.Gps;
import com.farm.bean.LocationBean;
import com.farm.bean.Points;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.farm.common.CoordinateConvertUtil;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditDLInfor;
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
import com.tencent.mapsdk.raster.model.BitmapDescriptor;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.mapsdk.raster.model.Polygon;
import com.tencent.mapsdk.raster.model.PolygonOptions;
import com.tencent.mapsdk.raster.model.Polyline;
import com.tencent.mapsdk.raster.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.Projection;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1、在地图画出农场所有园区的区域范围:
 * 2、在地图上标记所有人员的最新位置信息:
 * 3、判断工作人员在上班时间是否在工作岗位：area.parkid=user.parkid
 * 4、按人员、园区切换地图视图：人员列表、园区列表
 * 5、工作人员离岗马上提醒：Toast
 */
@EActivity(R.layout.attendance_map)
public class Attendance_Map extends Activity implements TencentLocationListener, View.OnClickListener
{
    List<LocationBean> list_locationInfo;
    List<BoundaryBean> list_boundary;
    List<String> list_yq = new ArrayList<>();
    List<String> list_user = new ArrayList<>();
    String note_lg = "";
    Map<String, Polygon> map_polygon = new HashMap<>();
    DL_ZS_Adapter dl_zs_adapter;
    commembertab commembertab;
    ListView lv_zs;
    List<Polygon> list_polygon;
    LatLng latlng_clickpostion;
    PopupWindow pw_command;
    View pv_command;
    Button btn_sure;
    EditText et_note;
    EditText et_time;
    EditText et_dlzs;
    CustomDialog_EditDLInfor customdialog_editdlinfor;
    boolean isStart = false;
    //    List<Object> area_Overlays = new ArrayList<Object>();
    LatLng lastlatLng = null;
    LatLng prelatLng = null;
    int error;
    LatLng location_latLng = new LatLng(24.430833, 113.298611);// 初始化定位
    private List<Object> Overlays;
    private Projection mProjection;
    Marker marker;
    TencentMap tencentMap;

    List<LatLng> pointsList = new ArrayList<>();
    @ViewById
    MapView mapview;
    @ViewById
    TextView tv_adddl;
    @ViewById
    TextView tv_zs;
    @ViewById
    TextView tv_gk;
    @ViewById
    LinearLayout ll_note;
    @ViewById
    View line;
    @ViewById
    FrameLayout fl_map;

    @Click
    void btn_people()
    {
        showPop_user(list_user);
    }

    @Click
    void btn_yq()
    {
        showPop_park(list_yq);
    }


    public void showPop_user(List<String> list)
    {
        LayoutInflater layoutInflater = (LayoutInflater) Attendance_Map.this.getSystemService(Attendance_Map.this.LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_attendance, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = Attendance_Map.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    Attendance_Map.this.getWindow().setAttributes(lp);
                    return true;
                }
                return false;
            }
        });
        pv_command.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_command.isShowing())
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = Attendance_Map.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    Attendance_Map.this.getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.MATCH_PARENT, 600, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);
        ListView lv = (ListView) pv_command.findViewById(R.id.lv);
        Attendance_Park_Adapter attendance_park_adapter = new Attendance_Park_Adapter(Attendance_Map.this, list_user);
        lv.setAdapter(attendance_park_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                for (int i = 0; i < list_locationInfo.size(); i++)
                {
                    TextView textview = (TextView) view.findViewById(R.id.tv_name);
                    String str = textview.getText().toString();
                    if (list_locationInfo.get(i).getuserName().equals(str))
                    {
                        String lat = list_locationInfo.get(i).getLat();
                        String lon = list_locationInfo.get(i).getLng();
                        LatLng latlng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                        tencentMap.animateTo(latlng);
                        WindowManager.LayoutParams lp = Attendance_Map.this.getWindow().getAttributes();
                        lp.alpha = 1f;
                        Attendance_Map.this.getWindow().setAttributes(lp);
                        pw_command.dismiss();
                    }
                }
            }
        });
        WindowManager.LayoutParams lp = Attendance_Map.this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        Attendance_Map.this.getWindow().setAttributes(lp);
    }

    public void showPop_park(List<String> list_yq)
    {
        LayoutInflater layoutInflater = (LayoutInflater) Attendance_Map.this.getSystemService(Attendance_Map.this.LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_attendance, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = Attendance_Map.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    Attendance_Map.this.getWindow().setAttributes(lp);
                    return true;
                }
                return false;
            }
        });
        pv_command.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_command.isShowing())
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = Attendance_Map.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    Attendance_Map.this.getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.MATCH_PARENT, 600, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);
        ListView lv = (ListView) pv_command.findViewById(R.id.lv);
        Attendance_Park_Adapter attendance_park_adapter = new Attendance_Park_Adapter(Attendance_Map.this, list_yq);
        lv.setAdapter(attendance_park_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                for (int i = 0; i < list_boundary.size(); i++)
                {
                    TextView textview = (TextView) view.findViewById(R.id.tv_name);
                    String str = textview.getText().toString();
                    if (list_boundary.get(i).getParkname().equals(str))
                    {
                        String lat = list_boundary.get(i).getPointsList().get(0).getLat();
                        String lon = list_boundary.get(i).getPointsList().get(0).getLon();
                        LatLng latlng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                        tencentMap.animateTo(latlng);
                        WindowManager.LayoutParams lp = Attendance_Map.this.getWindow().getAttributes();
                        lp.alpha = 1f;
                        Attendance_Map.this.getWindow().setAttributes(lp);
                        pw_command.dismiss();
                    }
                }
            }
        });

//        lv.setAdapter(new ArrayAdapter<String>(Attendance_Map.this, R.layout.commember_item, list_yq));
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                for (int i = 0; i < list_boundary.size(); i++)
//                {
//                    if (list_boundary.get(i).getParkname().equals(((TextView) view).getText().toString()))
//                    {
//                        String lat = list_boundary.get(i).getPointsList().get(0).getLat();
//                        String lon = list_boundary.get(i).getPointsList().get(0).getLat();
//                        LatLng latlng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
//                        tencentMap.animateTo(latlng);
//                    }
//                }
//            }
//        });
        WindowManager.LayoutParams lp = Attendance_Map.this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        Attendance_Map.this.getWindow().setAttributes(lp);
    }


    @AfterViews
    void afterOncreate()
    {
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(Attendance_Map.this);
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
        Overlays = new ArrayList<Object>();
        list_polygon = new ArrayList<Polygon>();
        if (!utils.isOPen(Attendance_Map.this))
        {
            utils.openGPSSettings(Attendance_Map.this);
        }
//        mapview.setAlpha(0.2f);
        tencentMap = mapview.getMap();
        tencentMap.setZoom(17);
//        tencentMap.setSatelliteEnabled(true);
        mProjection = mapview.getProjection();
//        animateToLocation();
        getTestData("boundary");
        getLocationInfo(commembertab.getuId());
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {
                latlng_clickpostion = latlng;
                if (latlng_clickpostion != null)
                {
                    for (int i = 0; i < list_polygon.size(); i++)
                    {
                        if (list_polygon.get(i).contains(latlng_clickpostion))
                        {
                            Toast.makeText(Attendance_Map.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(this);
    }


    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason)
    {
        if (TencentLocation.ERROR_OK == error) // 定位成功
        {
            // 用于定位
            location_latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //全局记录坐标
            AppContext appContext = (AppContext) Attendance_Map.this.getApplication();
            appContext.setLOCATION_X(String.valueOf(location_latLng.getLatitude()));
            appContext.setLOCATION_Y(String.valueOf(location_latLng.getLongitude()));
        }

    }

    @Override
    public void onStatusUpdate(String name, int status, String desc)
    {

        if (status == 2)// 位置权限拒绝
        {
            // Toast.makeText(Attendance_Map.this, " 位置权限拒绝！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == 2)// 定位服务关闭
        {
            // Toast.makeText(Attendance_Map.this, " 定位服务关闭！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == 1)// 定位服务开启
        {
            // Toast.makeText(Attendance_Map.this, "定位服务开启！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == -1)// 定位服务未知
        {
            // Toast.makeText(Attendance_Map.this, "定位服务未知！",
            // Toast.LENGTH_SHORT).show();
        }
    }

    private void animateToLocation(LatLng latLng)
    {
        if (latLng != null)
        {
            tencentMap.removeOverlay(marker);
            tencentMap.animateTo(latLng);
            addMarker(latLng, R.drawable.location1, "");
        }
    }

    private void addMarker(LatLng latLng, int icon, String info)
    {
        Drawable drawable = getResources().getDrawable(icon);
        Bitmap bitmap = utils.drawable2Bitmap(drawable);
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title(info).icon(new BitmapDescriptor(bitmap)));
        marker.showInfoWindow();
    }

    private void getTestData(String from)
    {
        JSONObject jsonObject = utils.parseJsonFile(Attendance_Map.this, "dictionary.json");
        Result result = JSON.parseObject(jsonObject.getString(from), Result.class);
        list_boundary = JSON.parseArray(result.getRows().toJSONString(), BoundaryBean.class);
//        showPatro(listData);

        for (int i = 0; i < list_boundary.size(); i++)
        {
            String parkid = list_boundary.get(i).getParkid();
            String parkname = list_boundary.get(i).getParkname();
            list_yq.add(parkname);
            List<Points> list = list_boundary.get(i).getPointsList();
            List<LatLng> list_LatLng = new ArrayList<>();
            for (int j = 0; j < list.size(); j++)
            {
                if (j == 0)
                {
                    LatLng latlng = new LatLng(Double.valueOf(list.get(j).getLat()), Double.valueOf(list.get(j).getLon()));
                    tencentMap.animateTo(latlng);
                    addMarker(latlng, R.drawable.location_start, parkname);
                }
                LatLng latlng = new LatLng(Double.valueOf(list.get(j).getLat()), Double.valueOf(list.get(j).getLon()));
                list_LatLng.add(latlng);
            }
            Polygon polygon = drawPolygon(list_LatLng, R.color.bg_yellow);
            map_polygon.put(parkid, polygon);
            list_polygon.add(polygon);
            Overlays.add(polygon);
        }

    }

    private void showPatro(List<Points> listNewData)
    {
        LatLng startlatLng = null;
//        tencentMap.setZoom(15);// 设置地图显示大小
        List<Object> Overlays = new ArrayList<Object>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            Gps gPS = CoordinateConvertUtil.gps84_To_Gcj02(Double.valueOf(listNewData.get(i).getLat()), Double.valueOf(listNewData.get(i).getLon()));
            LatLng latLng = new LatLng(gPS.getWgLat(), gPS.getWgLon());
            if (i == 0)
            {
                Gps gPS0 = CoordinateConvertUtil.gps84_To_Gcj02(Double.valueOf(listNewData.get(i).getLat()), Double.valueOf(listNewData.get(i).getLon()));
                startlatLng = new LatLng(gPS0.getWgLat(), gPS0.getWgLon());
//                addMarker(startlatLng, R.drawable.location_start);
                tencentMap.animateTo(startlatLng);
                lastlatLng = latLng;
            }
            if (i == listNewData.size() - 1)
            {
//                addMarker(latLng, R.drawable.location_end);
            }
            // latLng = new LatLng(utils.randomCommon(lastlatLng.getLatitude(),
            // lastlatLng.getLatitude() + 0.2, 1)[0],
            // utils.randomCommon(lastlatLng.getLongitude(),
            // lastlatLng.getLongitude() + 0.2, 1)[0]);
            PolylineOptions lineOpt = new PolylineOptions();
            lineOpt.add(lastlatLng);
            lastlatLng = latLng;
            lineOpt.add(latLng);
            Polyline line = tencentMap.addPolyline(lineOpt);
            line.setColor(this.getResources().getColor(R.color.black));
            line.setWidth(4f);
            Overlays.add(line);
        }
    }

    private Polygon drawPolygon(List<LatLng> list_LatLng, int color)
    {
        PolygonOptions polygonOp = new PolygonOptions();
        polygonOp.fillColor(color);// 填充色
        polygonOp.strokeWidth(0);// 线宽
        for (int i = 0; i < list_LatLng.size(); i++)
        {
            polygonOp.add(list_LatLng.get(i));
        }
        Polygon polygon = mapview.getMap().addPolygon(polygonOp);
        return polygon;
    }

    private void getLocationInfo(String uid)
    {
        commembertab commembertab = AppContext.getUserInfo(Attendance_Map.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("action", "GetLocationInfo");
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
                        list_locationInfo = JSON.parseArray(result.getRows().toJSONString(), LocationBean.class);
                        for (int i = 0; i < list_locationInfo.size(); i++)
                        {
                            LatLng latLng = new LatLng(Double.valueOf(list_locationInfo.get(i).getLat().toString()), Double.valueOf(list_locationInfo.get(i).getLng()));
                            if (latLng != null)
                            {
                                list_user.add(list_locationInfo.get(i).getuserName());
                                addMarker(latLng, R.drawable.user, list_locationInfo.get(i).getparkName() + "-" + list_locationInfo.get(i).getuserName());
                                Polygon polygon = map_polygon.get(list_locationInfo.get(i).getparkId());
                                if (polygon != null)
                                {
                                    if (!polygon.contains(latLng))
                                    {
                                        TextView textview = new TextView(Attendance_Map.this);
                                        textview.setPadding(20, 20, 20, 20);
                                        textview.setBackgroundResource(R.drawable.round_orange);
                                        textview.setText(list_locationInfo.get(i).getparkName() + "-" + list_locationInfo.get(i).getuserName() + "离岗了;");
                                        textview.setTag(latLng);
                                        textview.setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v)
                                            {
                                                tencentMap.animateTo((LatLng) v.getTag());
                                            }
                                        });
                                        ll_note.addView(textview);
//                                        Toast.makeText(Attendance_Map.this, list.get(i).getuserName() + "离岗了", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        }

                    } else
                    {
                    }
                } else
                {
//                    AppContext.makeToast(PG_MainActivity.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
//                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void getUserInfo(String uid)
    {
        commembertab commembertab = AppContext.getUserInfo(Attendance_Map.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("action", "GetLocationInfo");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<commembertab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        List<commembertab> list = JSON.parseArray(result.getRows().toJSONString(), commembertab.class);
//                        showPop_user(list);
                    } else
                    {
                        List<commembertab> list = new ArrayList<commembertab>();
//                        showPop_user(list);
                    }
                } else
                {
//                    AppContext.makeToast(PG_MainActivity.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
//                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void getParkInfo(String uid)
    {
        commembertab commembertab = AppContext.getUserInfo(Attendance_Map.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("action", "GetLocationInfo");
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
                        List<parktab> list = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
//                        showPop_park(list_boundary);
                    } else
                    {
                        List<parktab> list = new ArrayList<parktab>();
//                        showPop_park(list_boundary);
                    }
                } else
                {
//                    AppContext.makeToast(PG_MainActivity.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
//                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    @Override
    public void onClick(View v)
    {

    }
}
