package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.farm.R;
import com.farm.adapter.Attendance_Park_Adapter;
import com.farm.adapter.DL_ZS_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.CoordinatesBean;
import com.farm.bean.Gps;
import com.farm.bean.LocationBean;
import com.farm.bean.Points;
import com.farm.bean.PolygonBean;
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
import org.androidannotations.annotations.EFragment;
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
@EFragment
public class Map_Farm extends Fragment implements TencentLocationListener, View.OnClickListener
{
    boolean ishidding = false;
    boolean isanimateToCenter = false;
    List<Polyline> list_Objects_road;//线段对象
    List<Polyline> list_Objects_line;//线段对象

    List<Marker> list_Objects_gcd;//随意点对象
    List<Marker> list_Objects_house;//房子对象
    List<Marker> list_Objects_road_centermarker;//路中心点对象
    List<Marker> list_Objects_point;//随意点对象
    List<Marker> list_Objects_line_centermarker;//线中心点对象
    List<Marker> list_Objects_mian_centermarker;//面中心点对象

    List<Marker> list_Marker_park;//园区标识对象
    List<Marker> list_Marker_area;//片区标识对象
    List<Marker> list_Marker_contract;//承包区标识对象
    List<Marker> list_Marker_block;
    List<Marker> list_Marker_breakoff;

    List<Marker> list_Marker_allsale;
    List<Marker> list_Marker_saleout;
    List<Marker> list_Marker_salein;
    List<Marker> list_Marker_newsale;
    List<Marker> list_Marker_salefor;

    List<Marker> list_Marker_AreaChart;//图表标识
    List<Marker> list_Marker_ParkChart;//图表标识
    List<Marker> list_Marker_ContractChart;//图表标识

    List<Polygon> list_Objects_breakoff;//多边形

    List<Polygon> list_Objects_mian;//多边形
    List<Polygon> list_Objects_park;//多边形
    List<Polygon> list_Objects_area;//多边形
    List<Polygon> list_Objects_contract;//多边形
    List<Polygon> list_Objects_allsale;//所有销售区域
    List<Polygon> list_Objects_saleout;//已售区域
    List<Polygon> list_Objects_salein;//待售区域
    List<Polygon> list_Objects_newsale;//待售区域
    List<Polygon> list_Objects_salefor;//售中区域

    List<PolygonBean> list_PolygonBean_park;//园区标识对象
    TimeThread timethread;
    List<Marker> list_mark_userlocation = new ArrayList<>();
    List<LocationBean> list_locationInfo;
    List<String> list_yq = new ArrayList<>();
    List<String> list_user = new ArrayList<>();
    String note_lg = "";
    Map<String, Polygon> map_polygon = new HashMap<>();
    DL_ZS_Adapter dl_zs_adapter;
    commembertab commembertab;
    ListView lv_zs;
    LatLng latlng_clickpostion;
    PopupWindow pw_command;
    View pv_command;
    Button btn_sure;
    EditText et_note;
    EditText et_time;
    EditText et_dlzs;
    CustomDialog_EditDLInfor customdialog_editdlinfor;
    boolean isStart = false;
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
    void btn_more()
    {
        Intent intent = new Intent(getActivity(), DaoGangList_.class);
        startActivity(intent);
    }

    @Click
    void btn_people()
    {
        showPop_user(list_user);
    }

    @Click
    void btn_yq()
    {
        if (list_yq.size() > 0)
        {
            showPop_park(list_yq);
        } else
        {
            Toast.makeText(getActivity(), "园区获取失败", Toast.LENGTH_SHORT).show();
        }

    }


    public void showPop_user(List<String> list)
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_attendance, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
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
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.MATCH_PARENT, 600, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);
        ListView lv = (ListView) pv_command.findViewById(R.id.lv);
        Attendance_Park_Adapter attendance_park_adapter = new Attendance_Park_Adapter(getActivity(), list_user);
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
                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                        lp.alpha = 1f;
                        getActivity().getWindow().setAttributes(lp);
                        pw_command.dismiss();
                    }
                }
            }
        });
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
    }

    public void showPop_park(List<String> list_yq)
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_attendance, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
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
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.MATCH_PARENT, 600, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);
        ListView lv = (ListView) pv_command.findViewById(R.id.lv);
        Attendance_Park_Adapter attendance_park_adapter = new Attendance_Park_Adapter(getActivity(), list_yq);
        lv.setAdapter(attendance_park_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                for (int i = 0; i < list_PolygonBean_park.size(); i++)
                {
                    TextView textview = (TextView) view.findViewById(R.id.tv_name);
                    String str = textview.getText().toString();
                    if (list_PolygonBean_park.get(i).getparkName().equals(str))
                    {
                        String lat = list_PolygonBean_park.get(i).getLat();
                        String lon = list_PolygonBean_park.get(i).getLng();
                        LatLng latlng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                        tencentMap.animateTo(latlng);
                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                        lp.alpha = 1f;
                        getActivity().getWindow().setAttributes(lp);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
    }


    @AfterViews
    void afterOncreate()
    {

        if (!utils.isOPen(getActivity()))
        {
            utils.openGPSSettings(getActivity());
        }

        tencentMap = mapview.getMap();
        tencentMap.setZoom(18);
        tencentMap.setSatelliteEnabled(true);
        mProjection = mapview.getProjection();
        Overlays = new ArrayList<Object>();

        initParam();
        initBasicData();//初始化基础数据
        getLocationInfo(commembertab.getuId());

        timethread = new TimeThread();
        timethread.setStop(false);
        timethread.setSleep(false);
        timethread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.map_farm, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(getActivity());
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
        return rootView;
    }
    public void setThreadStatus(boolean hidden)
    {
        ishidding = hidden;
        super.onHiddenChanged(hidden);//true

        if (timethread != null)
        {
            if (hidden == true)
            {
                timethread.setSleep(true);
            } else
            {
                timethread = new TimeThread();
                timethread.setStop(false);
                timethread.setSleep(false);
                timethread.start();
            }

        }
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason)
    {
        if (TencentLocation.ERROR_OK == error) // 定位成功
        {
            location_latLng = new LatLng(location.getLatitude(), location.getLongitude());
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

    private Marker addMarker(LatLng latLng, int icon, String info)
    {
        Drawable drawable = getResources().getDrawable(icon);
        Bitmap bitmap = utils.drawable2Bitmap(drawable);
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title(info).icon(new BitmapDescriptor(bitmap)));
        marker.showInfoWindow();
        return marker;
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

    private Polygon drawPolygon(float z, List<LatLng> list_LatLng, int fillcolor, int strokesize, int strokecolor)
    {
        PolygonOptions polygonOp = new PolygonOptions();
        polygonOp.fillColor(fillcolor);// 填充色
        polygonOp.strokeColor(getResources().getColor(strokecolor));// 线颜色
        polygonOp.strokeWidth(strokesize);// 线宽
        for (int i = 0; i < list_LatLng.size(); i++)
        {
            polygonOp.add(list_LatLng.get(i));
        }
        Polygon polygon = mapview.getMap().addPolygon(polygonOp);
        polygon.setZIndex(z);
        return polygon;
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

    //    public void initBasicData()
//    {
//        list_PolygonBean_park=new ArrayList<>();
//        //初始化规划图
//        List<parktab> list_parktab = SqliteDb.getparktab(Attendance_Map.this, commembertab.getuId());
//        for (int i = 0; i < list_parktab.size(); i++)//每个园区
//        {
//            PolygonBean polygonBean_park = SqliteDb.getLayer_park(Attendance_Map.this, list_parktab.get(i).getid());
//            if (polygonBean_park != null)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(polygonBean_park.getLat()), Double.valueOf(polygonBean_park.getLng()));
//                Marker marker = addCustomMarker("normal", R.drawable.ic_flag_park, getResources().getColor(R.color.white), latlng, polygonBean_park.getUuid(), polygonBean_park.getNote());
//                List<CoordinatesBean> list_park = SqliteDb.getPoints(Attendance_Map.this, polygonBean_park.getUuid());
//                if (list_park != null && list_park.size() != 0)
//                {
//                   Polygon p= initBoundary(getResources().getColor(R.color.transparent), 0f, list_park, 0, R.color.transparent);
//                    map_polygon.put(list_parktab.get(i).getid(), p);
//                    list_PolygonBean_park.add(polygonBean_park);
//                    list_yq.add(list_parktab.get(i).getparkName());
//                }
//
//            }
//
//            List<areatab> list_areatab = SqliteDb.getareatab(Attendance_Map.this, list_parktab.get(i).getid());
//            for (int k = 0; k < list_areatab.size(); k++)//每个片区
//            {
//                PolygonBean polygonBean_area = SqliteDb.getLayer_area(Attendance_Map.this, list_parktab.get(i).getid(), list_areatab.get(k).getid());
//                if (polygonBean_area != null)
//                {
//                    LatLng latlng = new LatLng(Double.valueOf(polygonBean_area.getLat()), Double.valueOf(polygonBean_area.getLng()));
//                    Marker marker = addCustomMarker("normal", R.drawable.ic_flag_area, getResources().getColor(R.color.white), latlng, polygonBean_area.getUuid(), polygonBean_area.getNote());
//                    marker.setVisible(false);
//                    List<CoordinatesBean> list_area = SqliteDb.getPoints(Attendance_Map.this, polygonBean_area.getUuid());
//                    if (list_area != null && list_area.size() != 0)
//                    {
//                        initBoundary(getResources().getColor(R.color.transparent), 0f, list_area, 2, R.color.bg_text);
//                    }
//                }
//
//                List<contractTab> list_contractTab = SqliteDb.getcontracttab(Attendance_Map.this, list_areatab.get(k).getid());
//                for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
//                {
//                    //承包区规划图
//                    PolygonBean polygonBean_contract = SqliteDb.getLayer_contract(Attendance_Map.this, list_parktab.get(i).getid(), list_areatab.get(k).getid(), list_contractTab.get(m).getid());
//                    if (polygonBean_contract != null)
//                    {
//                        LatLng latlng = new LatLng(Double.valueOf(polygonBean_contract.getLat()), Double.valueOf(polygonBean_contract.getLng()));
//                        Marker marker = addCustomMarker("normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, polygonBean_contract.getUuid(), polygonBean_contract.getNote());
//                        marker.setVisible(false);
//                        List<CoordinatesBean> list_contract = SqliteDb.getPoints(Attendance_Map.this, polygonBean_contract.getUuid());
//                        if (list_contract != null && list_contract.size() != 0)
//                        {
//                            initBoundary(Color.argb(150, 144, 144, 144), 0f, list_contract, 2, R.color.bg_text);
//                        }
//                    }
//                }
//            }
//
//        }
////显示点
//        initPointPolygon();
////显示断蕾
//        initHousePolygon();
////显示线
//        initLinePolygon();
////显示道路
//        initRoadPolygon();
////显示面
//        initMianPolygon();
//    }
    public void initBasicData()
    {
//显示规划图
//        InitPlanMap();
        InitArea();
//显示点
        initGCDPolygon();
//显示点
        initPointPolygon();
//显示断蕾
        initHousePolygon();
//显示线
        initLinePolygon();
//显示道路
        initRoadPolygon();
//显示面
        initMianPolygon();
    }
    private void InitArea()
    {
        list_Marker_park = new ArrayList<>();
        list_Marker_area = new ArrayList<>();
        list_Marker_contract = new ArrayList<>();
        list_Marker_block = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getAreaMapByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PolygonBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PolygonBean.class);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            PolygonBean polygonBean = listNewData.get(i);
                            LatLng latlng = new LatLng(Double.valueOf(polygonBean.getLat()), Double.valueOf(polygonBean.getLng()));
                            Marker marker = null;
                            if (polygonBean.getContractname().equals(""))
                            {
                                marker = addCustomMarker(polygonBean, "farm_boundary", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getNote());
                            } else
                            {
                                marker = addCustomMarker(polygonBean, "farm_boundary", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getparkName() + polygonBean.getareaName() + polygonBean.getContractname());
                            }
                            marker.setVisible(false);
                            list_Marker_block.add(marker);
                            List<CoordinatesBean> list_contract = polygonBean.getCoordinatesBeanList();
                            if (list_contract != null && list_contract.size() != 0)
                            {
                                initBoundary(Color.argb(150, 144, 144, 144), 0f, list_contract, 2, R.color.bg_green);
                            }
                        }
                    }

                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }
    private void InitPlanMap()
    {
        list_Marker_park = new ArrayList<>();
        list_Marker_area = new ArrayList<>();
        list_Marker_contract = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getPlanMap");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PolygonBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        list_PolygonBean_park = new ArrayList<>();
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PolygonBean.class);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            PolygonBean polygonBean = listNewData.get(i);
                            if (polygonBean.getAreaId().equals("-1"))
                            {
                                list_PolygonBean_park.add(polygonBean);
                                list_yq.add(polygonBean.getparkName());
                                LatLng latlng = new LatLng(Double.valueOf(polygonBean.getLat()), Double.valueOf(polygonBean.getLng()));
                                Marker marker = addCustomMarker(polygonBean, "normal", R.drawable.ic_flag_park, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getNote());
                                marker.setVisible(false);
                                list_Marker_park.add(marker);
                                List<CoordinatesBean> list_park = polygonBean.getCoordinatesBeanList();
                                if (list_park != null && list_park.size() != 0)
                                {
                                    Polygon p = initBoundary(Color.argb(150, 144, 144, 144), 0f, list_park, 2, R.color.bg_green);
                                    map_polygon.put(polygonBean.getparkId(), p);
                                }
                                if (!isanimateToCenter)
                                {
                                    tencentMap.animateTo(latlng);
                                    isanimateToCenter = true;
                                }
                            } else if (polygonBean.getContractid().equals("-1"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(polygonBean.getLat()), Double.valueOf(polygonBean.getLng()));
                                Marker marker = addCustomMarker(polygonBean, "normal", R.drawable.ic_flag_area, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getNote());
                                marker.setVisible(false);
                                list_Marker_area.add(marker);
                                List<CoordinatesBean> list_area = polygonBean.getCoordinatesBeanList();
                                if (list_area != null && list_area.size() != 0)
                                {
                                    initBoundary(Color.argb(150, 144, 144, 144), 0f, list_area, 2, R.color.bg_green);
                                }

                            } else
                            {
                                LatLng latlng = new LatLng(Double.valueOf(polygonBean.getLat()), Double.valueOf(polygonBean.getLng()));
                                Marker marker = addCustomMarker(polygonBean, "normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getNote());
                                marker.setVisible(false);
                                list_Marker_contract.add(marker);
                                List<CoordinatesBean> list_contract = polygonBean.getCoordinatesBeanList();
                                if (list_contract != null && list_contract.size() != 0)
                                {
                                    initBoundary(Color.argb(150, 144, 144, 144), 0f, list_contract, 2, R.color.bg_green);
                                }

                            }
                        }
                    }

                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    public void initGCDPolygon()
    {
        list_Objects_gcd = new ArrayList<>();
        getPlanMap_Custom(commembertab.getuId(), "gcd");
    }

    public void initHousePolygon()
    {
        list_Objects_house = new ArrayList<>();
        getPlanMap_Custom(commembertab.getuId(), "house");
    }

    public void initRoadPolygon()
    {
        list_Objects_road = new ArrayList<>();
        list_Objects_road_centermarker = new ArrayList<>();
        getPlanMap_Custom(commembertab.getuId(), "road");
    }

    public void initPointPolygon()
    {
        list_Objects_point = new ArrayList<>();
        getPlanMap_Custom(commembertab.getuId(), "D");
    }

    public void initLinePolygon()
    {
        list_Objects_line = new ArrayList<>();
        list_Objects_line_centermarker = new ArrayList<>();
        getPlanMap_Custom(commembertab.getuId(), "X");
    }

    public void initMianPolygon()
    {
        list_Objects_mian_centermarker = new ArrayList<>();
        list_Objects_mian = new ArrayList<>();
        getPlanMap_Custom(commembertab.getuId(), "M");
    }

    private Polygon initBoundary(int fillcolor, float z, List<CoordinatesBean> list_coordinates, int strokesize, int strokecolor)
    {
        List<LatLng> list_AllLatLng = new ArrayList<>();
        for (int i = 0; i < list_coordinates.size(); i++)
        {
            LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
            if (i == 0)
            {
                tencentMap.animateTo(latlng);
            }
            list_AllLatLng.add(latlng);
        }
        Polygon polygon = drawPolygon(10f, list_AllLatLng, fillcolor, strokesize, strokecolor);
        polygon.setZIndex(z);
        Overlays.add(polygon);
        return polygon;
    }

    private Marker addCustomMarker(Parcelable obj, String type, int icon, int textcolor, LatLng latLng, String uuid, String note)
    {
        Marker marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.marker_sale, null);
        View view_marker = (View) view.findViewById(R.id.view_marker);
        view_marker.setBackgroundResource(icon);
        TextView textView = (TextView) view.findViewById(R.id.tv_note);
        if (note == null || note.equals(""))
        {
            textView.setText("暂无说明");
        } else
        {
            textView.setText(note);
        }
        textView.setTextColor(textcolor);
        textView.setTextSize(12);
        marker.setMarkerView(view);
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid);
        bundle.putString("note", note);
        bundle.putString("type", type);
        marker.setTag(bundle);
        return marker;
    }

    //    private Marker addCustomMarker(String type, int icon, int textcolor, LatLng latLng, String uuid, String note)
//    {
//        Marker marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
//        View view = LayoutInflater.from(Attendance_Map.this).inflate(R.layout.marker_sale, null);
//        View view_marker = (View) view.findViewById(R.id.view_marker);
//        view_marker.setBackgroundResource(icon);
//        TextView textView = (TextView) view.findViewById(R.id.tv_note);
//        if (note == null || note.equals(""))
//        {
//            textView.setText("暂无说明");
//        } else
//        {
//            textView.setText(note);
//        }
//        textView.setTextColor(textcolor);
//        textView.setTextSize(12);
//        marker.setMarkerView(view);
//        Bundle bundle = new Bundle();
//        bundle.putString("uuid", uuid);
//        bundle.putString("note", note);
//        bundle.putString("type", type);
//        marker.setTag(bundle);
//        return marker;
//    }
    private void getLocationInfo(String uid)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("action", "GetLocationInfo");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                if (getActivity()==null)
                {
                    return;
                }
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        ll_note.removeAllViews();
                        list_locationInfo = JSON.parseArray(result.getRows().toJSONString(), LocationBean.class);
                        for (int i = 0; i < list_mark_userlocation.size(); i++)
                        {
                            tencentMap.removeOverlay(list_mark_userlocation.get(i));
                        }
                        list_user = new ArrayList<String>();
                        list_mark_userlocation = new ArrayList<Marker>();
                        for (int i = 0; i < list_locationInfo.size(); i++)
                        {
                            LatLng latLng = new LatLng(Double.valueOf(list_locationInfo.get(i).getLat().toString()), Double.valueOf(list_locationInfo.get(i).getLng()));
                            if (latLng != null)
                            {
                                LocationBean locationbean = list_locationInfo.get(i);
                                list_user.add(locationbean.getuserName());
                                Marker marker = addCustomMarker(list_locationInfo.get(i), "normal", R.drawable.user, getResources().getColor(R.color.red), latLng, locationbean.getId(), locationbean.getRegtime().substring(5, locationbean.getRegtime().lastIndexOf(":")) + list_locationInfo.get(i).getuserName());
                                list_mark_userlocation.add(marker);
                                Polygon polygon = map_polygon.get(list_locationInfo.get(i).getparkId());
                                if (polygon != null)
                                {
                                    if (!polygon.contains(latLng))
                                    {
                                        TextView textview = new TextView(getActivity());
                                        textview.setTextColor(getResources().getColor(R.color.red));
                                        textview.setTextSize(15);
                                        textview.setPadding(0, 10, 10, 10);
                                        textview.setGravity(Gravity.RIGHT);
//                                        textview.setBackgroundResource(R.drawable.round_orange);
//                                        textview.setText(list_locationInfo.get(i).getparkName() + "-" + list_locationInfo.get(i).getuserName() + "离岗了");
                                        textview.setText( list_locationInfo.get(i).getuserName() + "离岗");
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
//                AppContext.makeToast(Attendance_Map.this, "error_connectServer");
            }
        });
    }

    //    public void initLinePolygon()
//    {
//        List<PolygonBean> list_polygon_line = SqliteDb.getMoreLayer_line(Attendance_Map.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_line.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(Attendance_Map.this, list_polygon_line.get(i).getUuid());
//            List<LatLng> list_latlang = new ArrayList();
//            for (int j = 0; j < list.size(); j++)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(j).getLat()), Double.valueOf(list.get(j).getLng()));
//                list_latlang.add(latlng);
//            }
//            PolylineOptions lineOpt = new PolylineOptions();
////            lineOpt.setDottedLine(true);
//            lineOpt.addAll(list_latlang);
//            Polyline line = tencentMap.addPolyline(lineOpt);
////            line.setGeodesic(true);
//            line.setColor(Color.argb(500, 177, 15, 0));
//            line.setWidth(8f);
//            Overlays.add(line);
//
//            if (list_latlang.size() > 0)
//            {
//                int halfsize = list_latlang.size() / 2;
//                if (halfsize == 0)
//                {
//                    Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(0), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
//                } else
//                {
//                    Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
//                }
//
//            }
//
//        }
//    }
//
//    public void initRoadPolygon()
//    {
//       List<PolygonBean> list_polygon_road = SqliteDb.getMoreLayer_road(Attendance_Map.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_road.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(Attendance_Map.this, list_polygon_road.get(i).getUuid());
//            List<LatLng> list_latlang = new ArrayList();
//            for (int j = 0; j < list.size(); j++)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(j).getLat()), Double.valueOf(list.get(j).getLng()));
//                list_latlang.add(latlng);
//            }
//            PolylineOptions lineOpt = new PolylineOptions();
//            lineOpt.setDottedLine(true);
//            lineOpt.addAll(list_latlang);
//            Polyline line = tencentMap.addPolyline(lineOpt);
////            line.setGeodesic(true);
//            line.setColor(Color.argb(500, 255, 255, 255));
//            line.setWidth(8f);
//            Overlays.add(line);
//
//            if (list_latlang.size() > 0)
//            {
//                int halfsize = list_latlang.size() / 2;
//                Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_road.get(i).getUuid(), list_polygon_road.get(i).getNote());
//            }
//
//        }
//    }
//
//
//    public void initMianPolygon()
//    {
//        List<PolygonBean>  list_polygon_mian = SqliteDb.getMoreLayer_mian(Attendance_Map.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_mian.size(); i++)
//        {
//            LatLng latlng = new LatLng(Double.valueOf(list_polygon_mian.get(i).getLat()), Double.valueOf(list_polygon_mian.get(i).getLng()));
//            Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_ask), latlng, list_polygon_mian.get(i).getUuid(), list_polygon_mian.get(i).getNote());
//            List<CoordinatesBean> list_mian = SqliteDb.getPoints(Attendance_Map.this, list_polygon_mian.get(i).getUuid());
//            List<LatLng> list_LatLng = new ArrayList<>();
//            if (list_mian != null && list_mian.size() != 0)
//            {
//                for (int j = 0; j < list_mian.size(); j++)
//                {
//                    LatLng ll = new LatLng(Double.valueOf(list_mian.get(j).getLat()), Double.valueOf(list_mian.get(j).getLng()));
//                    list_LatLng.add(ll);
//                }
//                Polygon polygon = drawPolygon(10f, list_LatLng, Color.argb(180, 70, 101, 10), 2, R.color.bg_text);
//                polygon.setZIndex(0f);
//                Overlays.add(polygon);
//            }
//
//        }
//    }
//
//    public void initHousePolygon()
//    {
//        List<PolygonBean>    list_polygon_house = SqliteDb.getMoreLayer_house(Attendance_Map.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_house.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(Attendance_Map.this, list_polygon_house.get(i).getUuid());
//            if (list != null && list.size() != 0)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
//                Marker marker = addCustomMarker("normal", R.drawable.icon_house, getResources().getColor(R.color.bg_ask), latlng, list_polygon_house.get(i).getUuid(), list_polygon_house.get(i).getNote());
//            }
//
//        }
//    }
//
//    public void initPointPolygon()
//    {
//        List<PolygonBean>  list_polygon_point = SqliteDb.getMoreLayer_point(Attendance_Map.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_point.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(Attendance_Map.this, list_polygon_point.get(i).getUuid());
//            if (list != null && list.size() != 0)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
//                Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_job), latlng, list_polygon_point.get(i).getUuid(), list_polygon_point.get(i).getNote());
//            }
//
//        }
//    }
    private void getUserInfo(String uid)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
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
//                AppContext.makeToast(Attendance_Map.this, "error_connectServer");
            }
        });
    }

    public void initParam()
    {
        list_Marker_ParkChart = new ArrayList<>();
        list_Marker_AreaChart = new ArrayList<>();
        list_Marker_ContractChart = new ArrayList<>();


        list_Objects_road = new ArrayList<>();
        list_Objects_road_centermarker = new ArrayList<>();

        list_Objects_house = new ArrayList<>();
        list_Objects_point = new ArrayList<>();

        list_Objects_line = new ArrayList<>();
        list_Objects_line_centermarker = new ArrayList<>();

        list_Objects_mian_centermarker = new ArrayList<>();
        list_Objects_mian = new ArrayList<>();

        list_Objects_park = new ArrayList<>();
        list_Objects_area = new ArrayList<>();
        list_Objects_contract = new ArrayList<>();
        list_Marker_park = new ArrayList<>();
        list_Marker_area = new ArrayList<>();
        list_Marker_contract = new ArrayList<>();
    }

    private void getParkInfo(String uid)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
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
//                AppContext.makeToast(Attendance_Map.this, "error_connectServer");
            }
        });
    }

    @Override
    public void onClick(View v)
    {

    }

    class TimeThread extends Thread
    {
        private boolean isSleep = true;
        private boolean stop = false;

        public void run()
        {
            Long starttime = 0l;
            while (!stop)
            {
                if (isSleep)
                {
                    return;
                } else
                {
                    try
                    {
                        timethread.sleep(AppContext.TIME_REFRESH);
                        starttime = starttime + 60000;
                        getLocationInfo(commembertab.getuId());
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setSleep(boolean sleep)
        {
            isSleep = sleep;
        }

        public void setStop(boolean stop)
        {
            this.stop = stop;
        }
    }

    private void getPlanMap_Custom(String uid, final String type)
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("action", "getPlanMap_Custom");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PolygonBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listNewData = new ArrayList<PolygonBean>();
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PolygonBean.class);
                        if (type.equals("D"))
                        {
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                List<CoordinatesBean> list = listNewData.get(i).getCoordinatesBeanList();
                                if (list != null && list.size() != 0)
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
                                    Marker marker = addCustomMarker(listNewData.get(i), "normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, listNewData.get(i).getUuid(), listNewData.get(i).getNote());
                                    list_Objects_point.add(marker);
                                }

                            }

                        } else if (type.equals("gcd"))
                        {
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                List<CoordinatesBean> list = listNewData.get(i).getCoordinatesBeanList();
                                if (list != null && list.size() != 0)
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
                                    Marker marker = addCustomMarker(listNewData.get(i), "gcd", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, listNewData.get(i).getUuid(), listNewData.get(i).getNote());
                                    list_Objects_gcd.add(marker);
                                }

                            }
                        } else if (type.equals("X"))
                        {
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                List<CoordinatesBean> list = listNewData.get(i).getCoordinatesBeanList();
                                List<LatLng> list_latlang = new ArrayList();
                                for (int j = 0; j < list.size(); j++)
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(list.get(j).getLat()), Double.valueOf(list.get(j).getLng()));
                                    list_latlang.add(latlng);
                                }
                                PolylineOptions lineOpt = new PolylineOptions();
                                lineOpt.addAll(list_latlang);
                                Polyline line = tencentMap.addPolyline(lineOpt);
                                line.setColor(Color.argb(500, 177, 15, 0));
                                line.setWidth(8f);
                                Overlays.add(line);
                                list_Objects_line.add(line);

                                if (list_latlang.size() > 0)
                                {
                                    int halfsize = list_latlang.size() / 2;
                                    if (halfsize == 0)
                                    {
                                        Marker marker = addCustomMarker(listNewData.get(i), "normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), list_latlang.get(0), listNewData.get(i).getUuid(), listNewData.get(i).getNote());
                                        list_Objects_line_centermarker.add(marker);
                                    } else
                                    {
                                        Marker marker = addCustomMarker(listNewData.get(i), "normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), list_latlang.get(0), listNewData.get(i).getUuid(), listNewData.get(i).getNote());
                                        list_Objects_line_centermarker.add(marker);
                                    }

                                }

                            }
                        } else if (type.equals("M"))
                        {
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                LatLng latlng = new LatLng(Double.valueOf(listNewData.get(i).getLat()), Double.valueOf(listNewData.get(i).getLng()));
                                Marker marker = addCustomMarker(listNewData.get(i), "normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, listNewData.get(i).getUuid(), listNewData.get(i).getNote());
                                list_Objects_mian_centermarker.add(marker);
                                List<CoordinatesBean> list_mian = listNewData.get(i).getCoordinatesBeanList();
                                List<LatLng> list_LatLng = new ArrayList<>();
                                if (list_mian != null && list_mian.size() != 0)
                                {
                                    for (int j = 0; j < list_mian.size(); j++)
                                    {
                                        LatLng ll = new LatLng(Double.valueOf(list_mian.get(j).getLat()), Double.valueOf(list_mian.get(j).getLng()));
                                        list_LatLng.add(ll);
                                    }
                                    Polygon polygon = drawPolygon(0f, list_LatLng, Color.argb(180, 70, 101, 10), 2, R.color.bg_text);
                                    polygon.setZIndex(0f);
                                    Overlays.add(polygon);
                                    list_Objects_mian.add(polygon);
                                }

                            }
                        } else if (type.equals("road"))
                        {
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                List<CoordinatesBean> list = listNewData.get(i).getCoordinatesBeanList();
                                List<LatLng> list_latlang = new ArrayList();
                                for (int j = 0; j < list.size(); j++)
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(list.get(j).getLat()), Double.valueOf(list.get(j).getLng()));
                                    list_latlang.add(latlng);
                                }
                                PolylineOptions lineOpt = new PolylineOptions();
                                lineOpt.setDottedLine(true);
                                lineOpt.addAll(list_latlang);
                                Polyline line = tencentMap.addPolyline(lineOpt);
                                line.setColor(Color.argb(500, 177, 15, 0));
                                line.setWidth(8f);
                                Overlays.add(line);
                                list_Objects_road.add(line);

                                if (list_latlang.size() > 0)
                                {
                                    int halfsize = list_latlang.size() / 2;
                                    Marker marker = addCustomMarker(listNewData.get(i), "normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), list_latlang.get(halfsize), listNewData.get(i).getUuid(), listNewData.get(i).getNote());
                                    list_Objects_road_centermarker.add(marker);
                                }

                            }
                        } else if (type.equals("house"))
                        {
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                List<CoordinatesBean> list = listNewData.get(i).getCoordinatesBeanList();
                                if (list != null && list.size() != 0)
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
                                    Marker marker = addCustomMarker(listNewData.get(i), "normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, listNewData.get(i).getUuid(), listNewData.get(i).getNote());
                                    list_Objects_house.add(marker);
                                }

                            }
                        }

                    }

                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (timethread != null && timethread.isAlive())
        {
            timethread.setStop(true);
            timethread.interrupt();
            timethread = null;
        }

    }
}
