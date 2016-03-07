package com.farm.ui;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.farm.adapter.DL_ZS_Adapter;
import com.farm.app.AppContext;
import com.farm.bean.Gps;
import com.farm.bean.Points;
import com.farm.bean.Result;
import com.farm.bean.ZS;
import com.farm.bean.commembertab;
import com.farm.common.CoordinateConvertUtil;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditDLInfor;
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

@EFragment
public class DL_Fragment extends Fragment implements TencentLocationListener, View.OnClickListener
{
    List<Points> list_point_pq;
    HashMap<String, List<Marker>> map;
    Polygon polygon_select;
    List<Polygon> list_polygon_pq;

    List<ZS> list_zs;
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
    TextView tv_tip;
    @ViewById
    TextView tv_zs;
    @ViewById
    TextView tv_gk;
    @ViewById
    View line;
    @ViewById
    FrameLayout fl_map;

    @Click
    void tv_gk()
    {
        showPop_gk();
    }

    @Click
    void tv_zs()
    {
        //1、选取任意一个点，判断是否在任意一个承包区内,高亮所选定的承包区A，及显示其marker
        //2、以后每次选取一个点时都要先判断该点是否在承包区A内，在则画线，不在则提示
        //3、当选取任意一个marker时则设置点击地图不响应事件，并设置往后只能选择marker，
//        list_zs = SqliteDb.getZS(getActivity(), ZS.class, commembertab.getareaId());
//        showPop_addcommand();
        if (tv_zs.getText().equals("确定"))
        {
            tv_tip.setVisibility(View.GONE);
            tv_zs.setText("添加断蕾区");
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
                                Toast.makeText(getActivity(), "在范围内", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });
//            SqliteDb.saveAll(getActivity(), pointsList);
            Polygon polygon = drawPolygon(pointsList, R.color.bg_blue);
            list_polygon.add(polygon);
            showDialog_EditDL();
        } else
        {
            tv_tip.setVisibility(View.VISIBLE);
            tv_tip.setText("请在承包区内选取点");
            tv_zs.setText("确定");
            isStart = true;
            tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
            {
                @Override
                public void onMapClick(LatLng latlng)
                {
                    if (polygon_select == null)//是否已经选定一个承包区
                    {
                        for (int i = 0; i < list_polygon_pq.size(); i++)
                        {
                            polygon_select = list_polygon_pq.get(i);
                            if (polygon_select.contains(latlng))
                            {
                                break;//包含该点，就跳出for循环
                            }
                            if (i == list_polygon_pq.size())
                            {
                                tv_tip.setText("请在承包区内选取点");
                                tv_tip.setBackgroundResource(R.color.bg_job);
                                return;
                            }
                        }
                    }
                    if (polygon_select.contains(latlng))//第一个确定的承包区是否包含第第 一个以后的点
                    {
                        addMarker(latlng, R.drawable.location_start);
                        pointsList.add(latlng);
                        if (isStart)
                        {
                            prelatLng = latlng;
                            isStart = false;
                        }
                        PolylineOptions lineOpt = new PolylineOptions();
                        lineOpt.add(prelatLng);
                        prelatLng = latlng;
                        lineOpt.add(latlng);
                        Polyline line = tencentMap.addPolyline(lineOpt);
                        line.setColor(getActivity().getResources().getColor(R.color.black));
                        line.setWidth(4f);
                        Overlays.add(line);
                    } else//是否点击了marker
                    {
//                        List<Marker> list_marker = map.get("a");
//                        for (int i = 0; i < list_marker.size(); i++)
//                        {
//                            if (list_marker.get(i).getPosition().equals(latlng))
//                            {
//                                pointsList.add(latlng);
//                                if (isStart)
//                                {
//                                    prelatLng = latlng;
//                                    isStart = false;
//                                }
//                                PolylineOptions lineOpt = new PolylineOptions();
//                                lineOpt.add(prelatLng);
//                                prelatLng = latlng;
//                                lineOpt.add(latlng);
//                                Polyline line = tencentMap.addPolyline(lineOpt);
//                                line.setColor(getActivity().getResources().getColor(R.color.black));
//                                line.setWidth(4f);
//                                Overlays.add(line);
//                                tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
//                                {
//                                    @Override
//                                    public void onMapClick(LatLng latlng)
//                                    {
//                                        tv_tip.setText("请按顺序选择边界上的点");
//                                        tv_tip.setBackgroundResource(R.color.bg_blue);
//                                    }
//                                });
//                            } else
//                            {
//                                tv_tip.setText("请在承包区内选取点或选取边界上的点");
//                                tv_tip.setBackgroundResource(R.color.bg_green);
//                            }
//                        }
                    }
//                    if (pointsList.size() <= 1)
//                    {
//                        pointsList.add(latlng);
//                        if (isStart)
//                        {
//                            prelatLng = latlng;
//                            isStart = false;
//                        }
//                        PolylineOptions lineOpt = new PolylineOptions();
//                        lineOpt.add(prelatLng);
//                        prelatLng = latlng;
//                        lineOpt.add(latlng);
//                        Polyline line = tencentMap.addPolyline(lineOpt);
//                        line.setColor(getActivity().getResources().getColor(R.color.black));
//                        line.setWidth(4f);
//                        Overlays.add(line);
//                    }
//                    else
//                    {
//                        for (int i = 0; i < list_polygon_pq.size(); i++)
//                        {
//
//                            int a = pointsList.size();
//                            Polygon polygon = list_polygon_pq.get(i);
//                            List<LatLng> list_point = polygon.getPoints();
//                            for (int j = 0; j < list_point.size(); j++)
//                            {
//                                for (int k = 0; k < pointsList.size(); k++)
//                                {
//                                    if (list_point.get(j).equals(pointsList.get(k)))
//                                    {
//                                        addMarker(pointsList.get(k),R.drawable.location_start);
//                                    }
//                                }
//                            }
//                        }
//                    }

                }
            });
        }
    }
//    @Click
//    void tv_zs()
//    {
//        list_zs = SqliteDb.getZS(getActivity(), ZS.class, commembertab.getareaId());
//        showPop_addcommand();
//    }

    @Click
    void tv_adddl()
    {
        if (tv_adddl.getText().equals("确定"))
        {
            tv_adddl.setText("添加断蕾区");
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
                                Toast.makeText(getActivity(), "在范围内", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });
//            SqliteDb.saveAll(getActivity(), pointsList);
            Polygon polygon = drawPolygon(pointsList, R.color.bg_blue);
            list_polygon.add(polygon);
            showDialog_EditDL();
        } else
        {
            tv_adddl.setText("确定");
            isStart = true;
            tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
            {

                @Override
                public void onMapClick(LatLng latlng)
                {
                    pointsList.add(latlng);
                    if (isStart)
                    {
                        prelatLng = latlng;
                        isStart = false;
//                        addMarker(prelatLng, R.drawable.location_start);
                    }
                    PolylineOptions lineOpt = new PolylineOptions();
                    lineOpt.add(prelatLng);
                    prelatLng = latlng;
                    lineOpt.add(latlng);
                    Polyline line = tencentMap.addPolyline(lineOpt);
                    line.setColor(getActivity().getResources().getColor(R.color.black));
                    line.setWidth(4f);
                    Overlays.add(line);
                }
            });
        }

    }

    public void showPop_gk()
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_zs, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    getActivity().getWindow().setAttributes(lp);
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
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.MATCH_PARENT, 300, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);

//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getActivity().getWindow().setAttributes(lp);
        pv_command.findViewById(R.id.btn_standardprocommand).setOnClickListener(DL_Fragment.this);
        pv_command.findViewById(R.id.btn_nonstandardprocommand).setOnClickListener(DL_Fragment.this);
        pv_command.findViewById(R.id.btn_nonprocommand).setOnClickListener(DL_Fragment.this);
    }

    public void showPop_addcommand()
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_zs, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    getActivity().getWindow().setAttributes(lp);
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
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, 250, LinearLayout.LayoutParams.MATCH_PARENT, true);
        pw_command.setAnimationStyle(R.style.leftinleftout);
        pw_command.showAsDropDown(line, 0, 0);
//        pw_command.showAtLocation(fl_map, Gravity.LEFT, 0, 500);
        pw_command.setOutsideTouchable(true);

//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getActivity().getWindow().setAttributes(lp);
        lv_zs = (ListView) pv_command.findViewById(R.id.lv_zs);
        dl_zs_adapter = new DL_ZS_Adapter(getActivity(), list_zs);
        lv_zs.setAdapter(dl_zs_adapter);
        pv_command.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                showDialog_addZS();
//                if (zs.getIsEnd().equals("0"))
//                {
//
//                }
                String uuid = java.util.UUID.randomUUID().toString();
                ZS zs = new ZS();
                zs.setid(uuid);
                zs.setName(utils.getToday() + "年-第一造");
                zs.setIsStart("1");
                zs.setIsEnd("0");
                zs.setparkId(commembertab.getparkId());
                zs.setparkName(commembertab.getparkName());
                zs.setAreaId(commembertab.getareaId());
                zs.setareaName(commembertab.getareaName());
                zs.setNote("暂无备注");
                zs.setregDate(utils.getTime());
                SqliteDb.save(getActivity(), zs);
                if (pv_command.isShown())
                {
                    list_zs = SqliteDb.getZS(getActivity(), ZS.class, commembertab.getareaId());
                    dl_zs_adapter.notifyDataSetChanged();
                }

            }
        });
    }

    public void showDialog_addZS()
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_addzs, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        et_time = (EditText) dialog_layout.findViewById(R.id.et_time);
        et_dlzs = (EditText) dialog_layout.findViewById(R.id.et_dlzs);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        et_time.setText(utils.getTime());
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }

    public void showDialog_EditDL()
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_editarea, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        et_time = (EditText) dialog_layout.findViewById(R.id.et_time);
        et_dlzs = (EditText) dialog_layout.findViewById(R.id.et_dlzs);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        et_time.setText(utils.getTime());
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }

    @AfterViews
    void afterOncreate()
    {
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(getActivity());
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
        Overlays = new ArrayList<Object>();
        list_polygon = new ArrayList<Polygon>();
        list_polygon_pq = new ArrayList<Polygon>();
        map = new HashMap<>();
        if (!utils.isOPen(getActivity()))
        {
            utils.openGPSSettings(getActivity());
        }
//        mapview.setAlpha(0.2f);
        tencentMap = mapview.getMap();
        tencentMap.setZoom(18);
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                String[] str = marker.getTitle().toString().split(",");
                LatLng latlng = new LatLng(Double.valueOf(str[0]), Double.valueOf(str[1]));
                int pos = Integer.valueOf(str[2]);
                Points point_next = list_point_pq.get(pos + 1);
                Points point_last = list_point_pq.get(pos -1);
                if ((point_last.getLon().equals(latlng.getLatitude()) && point_last.getLat().equals(latlng.getLongitude())) || (point_next.getLon().equals(latlng.getLatitude()) && point_next.getLat().equals(latlng.getLongitude())))
                {
                    pointsList.add(latlng);
                    PolylineOptions lineOpt = new PolylineOptions();
                    lineOpt.add(prelatLng);
                    prelatLng = latlng;
                    lineOpt.add(latlng);
                    Polyline line = tencentMap.addPolyline(lineOpt);
                    line.setColor(getActivity().getResources().getColor(R.color.black));
                    line.setWidth(4f);
                    Overlays.add(line);
                    tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
                    {
                        @Override
                        public void onMapClick(LatLng latlng)
                        {
                            tv_tip.setText("请按顺序选择边界上的点");
                            tv_tip.setBackgroundResource(R.color.bg_blue);
                        }
                    });

                } else
                {
                    tv_tip.setText("请按顺序选择边界上的点!");
                    tv_tip.setBackgroundResource(R.color.bg_blue);
                }
                return false;
            }
        });
//        tencentMap.setSatelliteEnabled(true);
        mProjection = mapview.getProjection();
//        animateToLocation();
        getTestData("points");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.dl_fragment, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }


    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason)
    {
        if (TencentLocation.ERROR_OK == error) // 定位成功
        {
            // 用于定位
            location_latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //全局记录坐标
            AppContext appContext = (AppContext) getActivity().getApplication();
            appContext.setLOCATION_X(String.valueOf(location_latLng.getLatitude()));
            appContext.setLOCATION_Y(String.valueOf(location_latLng.getLongitude()));
        }

    }

    @Override
    public void onStatusUpdate(String name, int status, String desc)
    {

        if (status == 2)// 位置权限拒绝
        {
            // Toast.makeText(getActivity(), " 位置权限拒绝！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == 2)// 定位服务关闭
        {
            // Toast.makeText(getActivity(), " 定位服务关闭！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == 1)// 定位服务开启
        {
            // Toast.makeText(getActivity(), "定位服务开启！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == -1)// 定位服务未知
        {
            // Toast.makeText(getActivity(), "定位服务未知！",
            // Toast.LENGTH_SHORT).show();
        }
    }

    private void animateToLocation()
    {
        if (location_latLng != null)
        {
            tencentMap.removeOverlay(marker);
            tencentMap.animateTo(location_latLng);
            addMarker(location_latLng, R.drawable.location1);
        }
    }

    private Marker addMarker_Paint(int pos, LatLng latLng, int icon)
    {
        Drawable drawable = getResources().getDrawable(icon);
        Bitmap bitmap = utils.drawable2Bitmap(drawable);
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title(latLng.getLatitude() + "," + latLng.getLongitude() + "," + pos).icon(new BitmapDescriptor(bitmap)));
        marker.hideInfoWindow();
        return marker;
    }

    private Marker addMarker(LatLng latLng, int icon)
    {
        Drawable drawable = getResources().getDrawable(icon);
        Bitmap bitmap = utils.drawable2Bitmap(drawable);
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title(latLng.getLatitude() + "," + latLng.getLongitude()).icon(new BitmapDescriptor(bitmap)));
        marker.hideInfoWindow();
        return marker;
    }

    private void getTestData(String from)
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        Result result = JSON.parseObject(jsonObject.getString(from), Result.class);
        list_point_pq = JSON.parseArray(result.getRows().toJSONString(), Points.class);
//        showPatro(listData);
        List<LatLng> list_LatLng = new ArrayList<>();
        List<Marker> list_mark = new ArrayList<>();
        for (int i = 0; i < list_point_pq.size(); i++)
        {
            LatLng latlng = new LatLng(Double.valueOf(list_point_pq.get(i).getLat()), Double.valueOf(list_point_pq.get(i).getLon()));
            if (i == 0)
            {

                tencentMap.animateTo(latlng);
            }
            list_LatLng.add(latlng);
            Marker marker = addMarker_Paint(i, latlng, R.drawable.location_start);
            list_mark.add(marker);
        }
        map.put("a", list_mark);
        Polygon polygon = drawPolygon(list_LatLng, R.color.bg_yellow);
        list_polygon_pq.add(polygon);
        Overlays.add(polygon);
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

    @Override
    public void onClick(View v)
    {

    }
}
