package com.farm.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.adapter.Department_Adapter;
import com.farm.app.AppContext;
import com.farm.bean.CoordinatesBean;
import com.farm.bean.DepartmentBean;
import com.farm.bean.PolygonBean;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;
import com.farm.bean.parktab;
import com.farm.common.SqliteDb;
import com.farm.widget.CustomDialog_EditDLInfor;
import com.farm.widget.CustomDialog_EditPolygonInfo;
import com.farm.widget.swipelistview.CustomDialog_OperatePolygon;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ${hmj} on 2016/3/27.
 */
@EActivity(R.layout.maputils)
public class MapUtils  extends FragmentActivity implements TencentLocationListener, View.OnClickListener
{
    List<areatab> list_areatab_all = new ArrayList<>();
    List<contractTab> list_contractTab_all = new ArrayList<>();
    Polygon parkpolygon;
    LatLng lastlatLng = null;
    LatLng lastlatLng_cx = null;
    LatLng prelatLng = null;
    LatLng prelatLng_drawerparklayer = null;
    List<LatLng> pointsList = new ArrayList<>();
    List<LatLng> listlatlng_park = new ArrayList<>();
    List<LatLng> listlatlng_cx= new ArrayList<>();
    DepartmentBean departmentselected;
    Department_Adapter department_adapter;
    List<parktab> list_parktab;
    List<areatab> list_areatab;
    List<contractTab> list_contractTab;

    List<DepartmentBean> list_department;
    CustomDialog_EditPolygonInfo customdialog_editpolygoninfor;
    Button btn_addmore;
    TextView tv_note;
    Button btn_sure;
    Button btn_cancle;
    Button btn_close;
    EditText et_note;
    EditText et_polygonnote;
    CustomDialog_EditDLInfor customdialog_editdlinfor;
    CustomDialog_EditDLInfor showDialog_pickpointinfo;
    CustomDialog_OperatePolygon customdialog_operatepolygon;
    private List<Object> Overlays;
    List<Polyline> list_Objects_road;
    List<Marker> list_Objects_road_centermarker;
    List<Marker> list_Objects_house;
    List<Marker> list_Objects_point;
    List<Polyline> list_Objects_line;
    List<Marker> list_Objects_line_centermarker;
    List<Marker> list_Objects_mian_centermarker;
    List<Polygon> list_Objects_mian;
    List<Polygon> list_Objects_park;
    List<Polygon> list_Objects_area;
    List<Polygon> list_Objects_contract;
    List<PolygonBean> list_polygon_road;
    List<PolygonBean> list_polygon_house;
    List<PolygonBean> list_polygon_point;
    List<PolygonBean> list_polygon_line;
    List<PolygonBean> list_polygon_mian;
    List<PolygonBean> list_polygon_park;
    List<PolygonBean> list_polygon_area;
    List<PolygonBean> list_polygon_contract;
    List<LinearLayout> list_ll_second;
    List<LinearLayout> list_ll_first;
    List<LinearLayout> list_ll_third;
    List<Marker> list_centermark;//边界上的marker集合
    Marker marker;
    com.farm.bean.commembertab commembertab;
    int error;
    private TencentMap tencentMap;
    @ViewById
    Button btn_addlayer;
    @ViewById
    TextView tv_tip;
    @ViewById
    Button btn_complete;
    @ViewById
    Button btn_setting;
    @ViewById
    Button btn_showlayer;
    @Click
    void btn_addlayer()
    {
        if (btn_addlayer.getText().equals("取消"))
        {
            reloadMap();
        } else
        {
            btn_addlayer.setText("取消");
            btn_addmore.setVisibility(View.GONE);
            btn_showlayer.setVisibility(View.GONE);
            btn_setting.setVisibility(View.GONE);
            showDialog_department();
        }


    }
    @Override
    protected void onCreate(Bundle arg0)
    {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(MapUtils.this);
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(MapUtils.this);
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);

    }
    @AfterViews
    void afterOncreate()
    {
        Overlays = new ArrayList<Object>();

        list_centermark = new ArrayList<>();

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

        list_polygon_road = new ArrayList<>();
        list_polygon_house = new ArrayList<>();
        list_polygon_point = new ArrayList<>();
        list_polygon_line = new ArrayList<>();
        list_polygon_mian = new ArrayList<>();
        list_polygon_park = new ArrayList<>();
        list_polygon_area = new ArrayList<>();

        list_ll_second = new ArrayList<>();
        list_ll_first = new ArrayList<>();
        list_ll_third = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.frag_map);
        tencentMap = mapFragment.getMap();
        initPolyLineClickListening();
        initInfoWindowClickListener();
        initMapData();
        initMarkOnclick();
        initMapOnclickListening();
        initMapCameraChangeListener();
    }
    private void reloadMap()
    {
        tv_tip.setVisibility(View.GONE);
        tencentMap.clear();


        btn_complete.setVisibility(View.GONE);
        tv_tip.setVisibility(View.GONE);
        btn_addlayer.setText("添加区域");
//        btn_addmore.setVisibility(View.VISIBLE);
//        btn_showlayer.setVisibility(View.VISIBLE);
//        btn_setting.setVisibility(View.VISIBLE);
//        listlatlng_park = new ArrayList<>();
//        prelatLng_drawerparklayer = null;
//
//        list_Polyline = new ArrayList<>();
//        list_mark_inboundary = new ArrayList<>();
//        pointsList = new ArrayList<>();
//        list_polygon = new ArrayList<>();
//        list_LatLng_boundarynotselect = new ArrayList<>();
//        list_LatLng_boundaryselect = new ArrayList<>();
//        list_LatLng_inboundary = new ArrayList<>();
//        last_pos = 0;
//        number_markerselect = 0;
//        number_pointselect = 0;
//        firstmarkerselect = true;
//        prelatLng = null;

        btn_addlayer.setVisibility(View.VISIBLE);
//        btn_showlayer.setVisibility(View.VISIBLE);
//        btn_setting.setVisibility(View.VISIBLE);
//        btn_addmore.setText("更多");
//        drawerType = "";
//        currentpointmarker=null;//点
//        listlatlng_park = new ArrayList<>();//线、面
//        prelatLng_drawerparklayer = null;;//线、面
//        btn_addmore.setClickable(true);

        initPolyLineClickListening();
        initInfoWindowClickListener();
        initMapData();
        initMarkOnclick();
        initMapOnclickListening();
        initMapCameraChangeListener();
    }
    public void initPolyLineClickListening()
    {
        tencentMap.setOnPolylineClickListener(new TencentMap.OnPolylineClickListener()
        {
            @Override
            public boolean onPolylineClick(Polyline polyline, LatLng latLng)
            {
                final Marker marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title("cust infowindow").snippet("25.046083,121.513000").icon(BitmapDescriptorFactory.fromResource(R.drawable.location1)));
                Toast.makeText(MapUtils.this, latLng.toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    public void initMapCameraChangeListener()
    {
        tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener()
        {
            @Override
            public void onCameraChange(CameraPosition cameraPosition)
            {
//                int zoomlevel = tencentMap.getZoomLevel();
//                //规划图层信息显示控制
//                if (zoomlevel == 15)
//                {
//                    showFirstMarker();
//                }else
//                {
//                    for (int i = 0; i < list_Marker_first.size(); i++)
//                    {
//                        list_Marker_first.get(i).setVisible(false);
//                    }
//                }
//
//                //片区图层信息显示控制
//                if (zoomlevel ==14)
//                {
//                    showSecondMarker();
//                } else
//                {
//                    for (int i = 0; i < list_Marker_second.size(); i++)
//                    {
//                        list_Marker_second.get(i).setVisible(false);
//                    }
//                }
//
//                //其他图层信息显示控制
//                if (zoomlevel == 13 )
//                {
//                    showThirdMarker();
//                } else
//                {
//                    for (int i = 0; i < list_Marker_third.size(); i++)
//                    {
//                        list_Marker_third.get(i).setVisible(false);
//                    }
//                }
            }

            @Override
            public void onCameraChangeFinished(CameraPosition cameraPosition)
            {

            }
        });
    }
    public void initMapOnclickListening()
    {
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {

            }
        });
    }
    private void initMarkOnclick()
    {
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                String uuid = marker.getSnippet();
                showDialog_OperatePolygon(uuid);
                return false;
            }

        });
    }

    public void initInfoWindowClickListener()
    {
        TencentMap.InfoWindowAdapter infoWindowAdapter = new TencentMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindowPressState(Marker arg0)
            {
                return null;
            }

            @Override
            public View getInfoWindow(final Marker marker)
            {
                String uuid=marker.getSnippet();
                PolygonBean polygonbean = SqliteDb.getLayerbyuuid(MapUtils.this, uuid);
                String note=polygonbean.getNote();
                    View custInfowindow = LayoutInflater.from(MapUtils.this).inflate(R.layout.markerinfo, null);
                    TextView textView = (TextView) custInfowindow.findViewById(R.id.tv_note);
                    if (note == null || note.equals(""))
                    {
                        textView.setText("暂无说明");
                    } else
                    {
                        textView.setText(note);
                    }
                    textView.setTextColor(getResources().getColor(R.color.bg_yellow));
                    textView.setTextSize(12);
                    return custInfowindow;
            }

            @Override
            public View getInfoContents(Marker arg0)
            {
                // TODO Auto-generated method stub
                return null;
            }
        };
        tencentMap.setInfoWindowAdapter(infoWindowAdapter);
        tencentMap.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener()
        {

            @Override
            public void onInfoWindowClick(Marker arg0)
            {

            }
        });
    }
    public void initMapData()
    {
        List<parktab> list_parktab = SqliteDb.getparktab(MapUtils.this, "60");
        for (int i = 0; i < list_parktab.size(); i++)//每个园区
        {
            PolygonBean polygonBean_park = SqliteDb.getLayer_park(MapUtils.this, list_parktab.get(i).getid());
            if (polygonBean_park != null)
            {
                LatLng latlng = new LatLng(Double.valueOf(polygonBean_park.getLat()), Double.valueOf(polygonBean_park.getLng()));
                addCustomMarkerWithProgressbar(getResources().getColor(R.color.bg_blue), latlng, polygonBean_park.getUuid(), polygonBean_park.getNote());
                List<CoordinatesBean> list_park = SqliteDb.getPoints(MapUtils.this, polygonBean_park.getUuid());
                if (list_park != null && list_park.size() != 0)
                {
//                    initBoundary(Color.argb(130, 0, 202, 215), 0f, list_park);
                }

            }

            List<areatab> list_areatab = SqliteDb.getareatab(MapUtils.this, list_parktab.get(i).getid());
            for (int k = 0; k < list_areatab.size(); k++)//每个片区
            {
                PolygonBean polygonBean_area = SqliteDb.getLayer_area(MapUtils.this, list_parktab.get(i).getid(), list_areatab.get(k).getid());
                if (polygonBean_area != null)
                {
                    LatLng latlng = new LatLng(Double.valueOf(polygonBean_area.getLat()), Double.valueOf(polygonBean_area.getLng()));
                    addCustomMarkerWithProgressbar(getResources().getColor(R.color.bg_green), latlng, polygonBean_area.getUuid(), polygonBean_area.getNote());
                    List<CoordinatesBean> list_area = SqliteDb.getPoints(MapUtils.this, polygonBean_area.getUuid());
                    if (list_area != null && list_area.size() != 0)
                    {
//                        initBoundary(Color.argb(150, 4, 181, 0), 100f, list_area);
                        initBoundaryLine(Color.argb(1000, 57, 72, 61), 0f, list_area);
                    }

                }

                List<contractTab> list_contractTab = SqliteDb.getcontracttab(MapUtils.this, list_areatab.get(k).getid());
                for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
                {
                    PolygonBean polygonBean_contract = SqliteDb.getLayer_contract(MapUtils.this, list_parktab.get(i).getid(), list_areatab.get(k).getid(), list_contractTab.get(m).getid());
                    if (polygonBean_contract != null)
                    {
                        LatLng latlng = new LatLng(Double.valueOf(polygonBean_contract.getLat()), Double.valueOf(polygonBean_contract.getLng()));
                        addCustomMarkerWithProgressbar(getResources().getColor(R.color.bg_ask), latlng, polygonBean_contract.getUuid(), polygonBean_contract.getNote());
                        List<CoordinatesBean> list_contract = SqliteDb.getPoints(MapUtils.this, polygonBean_contract.getUuid());
                        if (list_contract != null && list_contract.size() != 0)
                        {
//                            initBoundary(Color.argb(160, 177, 15, 0), 200f, list_contract);
                            initBoundaryLine(Color.argb(1000, 57, 72, 61), 0f, list_contract);
                        }

                    }

                }
            }

        }
//显示点
        initPointPolygon();
//显示房子
        initHousePolygon();
//显示线
        initLinePolygon();
//显示道路
        initRoadPolygon();
//显示面
        initMianPolygon();
    }
    private void initBoundaryLine(int color, float z, List<CoordinatesBean> list_coordinates)
    {
        List<LatLng> list_AllLatLng = new ArrayList<>();
        for (int i = 0; i < list_coordinates.size(); i++)
        {
           LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
            list_AllLatLng.add(latlng);
        }
      PolylineOptions lineOpt = new PolylineOptions();
        lineOpt.addAll(list_AllLatLng);
        Polyline line = tencentMap.addPolyline(lineOpt);
        line.setColor(color);
//        line.setDottedLine(true);
        line.setWidth(4f);
        Overlays.add(line);
    }

    private void initBoundary(int color, float z, List<CoordinatesBean> list_coordinates)
    {
        List<LatLng> list_AllLatLng = new ArrayList<>();
        for (int i = 0; i < list_coordinates.size(); i++)
        {
            LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
            if (i == 0)
            {
                tencentMap.animateToNaviPosition(latlng,10f,10f);
            }
            list_AllLatLng.add(latlng);
        }
       Polygon polygon = drawPolygon(list_AllLatLng, color);
        polygon.setZIndex(z);
        Overlays.add(polygon);
    }
    private Polygon drawPolygon(List<LatLng> list_LatLng, int color)
    {
        PolygonOptions polygonOp = new PolygonOptions();
        polygonOp.fillColor(color);// 填充色
        polygonOp.strokeColor(color);// 线宽
        polygonOp.strokeWidth(1f);// 线宽
        for (int i = 0; i < list_LatLng.size(); i++)
        {
            polygonOp.add(list_LatLng.get(i));
        }
        Polygon polygon =tencentMap.addPolygon(polygonOp);
        return polygon;
    }
    public void showDialog_department()
    {
        final View dialog_layout = LayoutInflater.from(MapUtils.this).inflate(R.layout.customdialog_editpolygoninfo, null);
        customdialog_editpolygoninfor = new CustomDialog_EditPolygonInfo(MapUtils.this, R.style.MyDialog, dialog_layout);
        ListView lv_department = (ListView) dialog_layout.findViewById(R.id.lv_department);
        final Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editpolygoninfor.dismiss();
                btn_addmore.setVisibility(View.VISIBLE);
                btn_showlayer.setVisibility(View.VISIBLE);
                btn_setting.setVisibility(View.VISIBLE);
                btn_addlayer.setText("添加区域");
            }
        });
        customdialog_editpolygoninfor.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                customdialog_editpolygoninfor.dismiss();
                btn_addmore.setVisibility(View.VISIBLE);
                btn_showlayer.setVisibility(View.VISIBLE);
                btn_setting.setVisibility(View.VISIBLE);
                btn_addlayer.setText("添加区域");
            }
        });
        list_department = getDepartment(MapUtils.this);
        department_adapter = new Department_Adapter(MapUtils.this, list_department);
        lv_department.setAdapter(department_adapter);
        lv_department.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                initMapOnclick();
//                uiSettings.setZoomGesturesEnabled(false);
                customdialog_editpolygoninfor.dismiss();
                departmentselected = list_department.get(position);
//                list_coordinatesbean = SqliteDb.getBoundaryByID(MapUtils.this, "60", departmentselected.getParkid(), departmentselected.getAreaid(), departmentselected.getContractid());
//                showNeedPlanBoundary(list_coordinatesbean);

                if (departmentselected.getIsdrawer().equals("1"))
                {
                    Toast.makeText(MapUtils.this, "这个区域已画！请选择其他区域", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (departmentselected.getType().equals("园区"))
                {
                    btn_complete.setVisibility(View.VISIBLE);
                    tv_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("请在地图内画选区域");
                    tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
                    {
                        @Override
                        public void onMapClick(LatLng latlng)
                        {
                            listlatlng_park.add(latlng);
                            PolylineOptions lineOpt = new PolylineOptions();
                            lineOpt.add(prelatLng_drawerparklayer);
                            prelatLng_drawerparklayer = latlng;
                            lineOpt.add(latlng);
                            Polyline line = tencentMap.addPolyline(lineOpt);
                            line.setColor(MapUtils.this.getResources().getColor(R.color.black));
                            line.setWidth(4f);
                            Overlays.add(line);

                            Overlays.remove(parkpolygon);
                            parkpolygon = drawPolygon(listlatlng_park, R.color.bg_yellow);
                            Overlays.add(parkpolygon);
                        }
                    });
                } else
                {
//                    initMapAndParam();
//                    polygonBean_needPlan = SqliteDb.getNeedPlanlayer(MapUtils.this, "60", departmentselected.getParkid(), departmentselected.getAreaid(), departmentselected.getContractid());
//                    if (polygonBean_needPlan != null)
//                    {
//                        LatLng latlng = new LatLng(Double.valueOf(polygonBean_needPlan.getLat()), Double.valueOf(polygonBean_needPlan.getLng()));
//                        addCustomMarker(getResources().getColor(R.color.bg_blue), latlng, polygonBean_needPlan.getUuid(), polygonBean_needPlan.getNote());
//                        list_coordinatesbean = SqliteDb.getPoints(MapUtils.this, polygonBean_needPlan.getUuid());
//                        if (list_coordinatesbean != null && list_coordinatesbean.size() != 0)
//                        {
//                            showNeedPlanBoundary(list_coordinatesbean);
//                        }
//
//                    }
                }

            }
        });
        customdialog_editpolygoninfor.show();
    }
    
    public List<DepartmentBean> getDepartment(Context context)
    {
        DbUtils db = DbUtils.create(context);
        List<DepartmentBean> list = new ArrayList<>();
        try
        {
            list_parktab = db.findAll(Selector.from(parktab.class));
            if (list_parktab != null)
            {
                for (int i = 0; i < list_parktab.size(); i++)
                {
                    String isdrawer_park = "0";
                    boolean isexistpark = SqliteDb.isexistpark(MapUtils.this, list_parktab.get(i).getid());
                    if (isexistpark)
                    {
                        isdrawer_park = "1";
                    }
                    DepartmentBean department = new DepartmentBean();
                    department.setId(list_parktab.get(i).getid());
                    department.setUid(list_parktab.get(i).getuId());
                    department.setParkid(list_parktab.get(i).getid());
                    department.setParkname(list_parktab.get(i).getparkName());
                    department.setAreaid("");
                    department.setAreaname("");
                    department.setContractid("");
                    department.setContractname("");
                    department.setType("园区");
                    department.setName(list_parktab.get(i).getparkName());
                    department.setIsdrawer(isdrawer_park);
                    list.add(department);//添加
                    list_areatab = db.findAll(Selector.from(areatab.class).where("uid", "=", "60").and("parkid", "=", list_parktab.get(i).getid()));
                    list_areatab_all.addAll(list_areatab);
                    if (list_areatab != null)
                    {
                        for (int j = 0; j < list_areatab.size(); j++)
                        {
                            String isdrawer_area = "0";
                            boolean isexistarea = SqliteDb.isexistarea(MapUtils.this, list_parktab.get(i).getid(), list_areatab.get(j).getid());
                            if (isexistarea)
                            {
                                isdrawer_area = "1";
                            }
                            DepartmentBean department1 = new DepartmentBean();
                            department1.setId(list_areatab.get(j).getid());
                            department1.setUid(list_areatab.get(j).getuId());
                            department1.setParkid(list_areatab.get(j).getparkId());
                            department1.setParkname(list_areatab.get(j).getparkName());
                            department1.setAreaid(list_areatab.get(j).getid());
                            department1.setAreaname(list_areatab.get(j).getareaName());
                            department1.setContractid("");
                            department1.setContractname("");
                            department1.setType("片区");
                            department1.setName(list_parktab.get(i).getparkName() + "-" + list_areatab.get(j).getareaName());
                            department1.setIsdrawer(isdrawer_area);
                            list.add(department1);//添加
                            list_contractTab = db.findAll(Selector.from(contractTab.class).where("uid", "=", "60").and("parkid", "=", list_parktab.get(i).getid()).and("areaid", "=", list_areatab.get(j).getid()));
                            list_contractTab_all.addAll(list_contractTab);
                            if (list_contractTab != null)
                            {
                                for (int k = 0; k < list_contractTab.size(); k++)
                                {
                                    String isdrawer_contract = "0";
                                    boolean isexistcontract = SqliteDb.isexistcontract(MapUtils.this, list_parktab.get(i).getid(), list_areatab.get(j).getid(), list_contractTab.get(k).getid());
                                    if (isexistcontract)
                                    {
                                        isdrawer_contract = "1";
                                    }
                                    DepartmentBean department2 = new DepartmentBean();
                                    department2.setId(list_contractTab.get(k).getid());
                                    department2.setUid(list_contractTab.get(k).getuId());
                                    department2.setParkid(list_contractTab.get(k).getparkId());
                                    department2.setParkname(list_contractTab.get(k).getparkName());
                                    department2.setAreaid(list_contractTab.get(k).getAreaId());
                                    department2.setAreaname(list_contractTab.get(k).getareaName());
                                    department2.setContractid(list_contractTab.get(k).getid());
                                    department2.setContractname(list_contractTab.get(k).getContractNum());
                                    department2.setType("承包区");
                                    department2.setName(list_parktab.get(i).getparkName() + "-" + list_areatab.get(j).getareaName() + "-" + list_contractTab.get(k).getContractNum());
                                    department2.setIsdrawer(isdrawer_contract);
                                    list.add(department2);//添加
                                }
                            }
                        }

                    }
                }
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public void showDialog_OperatePolygon(final String uuid)
    {
        final View dialog_layout = LayoutInflater.from(MapUtils.this).inflate(R.layout.customdialog_operatepolygon, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(MapUtils.this, R.style.MyDialog, dialog_layout);
        Button btn_paint = (Button) dialog_layout.findViewById(R.id.btn_paint);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_change = (Button) dialog_layout.findViewById(R.id.btn_change);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        Button btn_delete = (Button) dialog_layout.findViewById(R.id.btn_delete);
        btn_paint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                initMapAndParam();
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                showDialog_deletetip(uuid);
            }
        });
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PolygonBean polygonbean = SqliteDb.getLayerbyuuid(MapUtils.this, uuid);
                showDialog_polygonnote(polygonbean.getNote());
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PolygonBean polygonbean = SqliteDb.getLayerbyuuid(MapUtils.this, uuid);
                showDialog_editpointinfo(polygonbean);
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
            }
        });
        customdialog_operatepolygon.show();
    }
    public void showDialog_editpointinfo(final PolygonBean polygonbean)
    {
        final View dialog_layout = LayoutInflater.from(MapUtils.this).inflate(R.layout.customdialog_polygonifo, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(MapUtils.this, R.style.MyDialog, dialog_layout);
        et_polygonnote = (EditText) dialog_layout.findViewById(R.id.et_note);
        et_polygonnote.setText(polygonbean.getNote());
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
                polygonbean.setNote(et_polygonnote.getText().toString());
                boolean issuccess = SqliteDb.editPolygoninfo(MapUtils.this, polygonbean);
                if (issuccess)
                {
                    reloadMap();
                } else
                {
                    Toast.makeText(MapUtils.this, "修改失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }
    public void showDialog_polygonnote(final String note)
    {
        final View dialog_layout = LayoutInflater.from(MapUtils.this).inflate(R.layout.customdialog_showpolygonifo, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(MapUtils.this, R.style.MyDialog, dialog_layout);
        tv_note = (TextView) dialog_layout.findViewById(R.id.tv_note);
        btn_close = (Button) dialog_layout.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
            }
        });
        if (note != null && !note.equals(""))
        {
            tv_note.setText(note);
        }
        customdialog_editdlinfor.show();
    }
    public void showDialog_deletetip(final String uuid)
    {
        final View dialog_layout = LayoutInflater.from(MapUtils.this).inflate(R.layout.customdialog_deletetip, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(MapUtils.this, R.style.MyDialog, dialog_layout);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
                boolean issuccess = SqliteDb.deleteplanPolygon(MapUtils.this, uuid);
                if (issuccess)
                {
                    Toast.makeText(MapUtils.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    reloadMap();
                } else
                {
                    Toast.makeText(MapUtils.this, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }
    private Marker addCustomMarker(int textcolor, LatLng latLng, String uuid, String note)
    {
        final Marker marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title(note).snippet(uuid).icon(BitmapDescriptorFactory.fromResource(R.drawable.umeng_socialize_follow_on)));
        marker.showInfoWindow();
        list_centermark.add(marker);
//        View view = LayoutInflater.from(MapUtils.this).inflate(R.layout.markerinfo, null);
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
//        Bundle bundle=new Bundle();
//        bundle.putString("uuid", uuid);
//        bundle.putString("note", note);
//        marker.setTag(bundle);
        return marker;
    }
    private Marker addCustomMarkerWithProgressbar(int textcolor,LatLng latLng, String uuid, String note)
    {
//        Drawable drawable = getResources().getDrawable(R.drawable.location1);
//        Bitmap bitmap = utils.drawable2Bitmap(drawable);
//        marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        final Marker marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title(note).snippet(uuid).icon(BitmapDescriptorFactory.fromResource(R.drawable.umeng_socialize_follow_on)));
        marker.showInfoWindow();
        list_centermark.add(marker);
//        View view = View.inflate(MapUtils.this,R.layout.markerwithprogressbar, null);
//        TextView textView = (TextView) view.findViewById(R.id.tv_note);
//        LinearLayout ll_second = (LinearLayout) view.findViewById(R.id.ll_second);
//        LinearLayout ll_first = (LinearLayout) view.findViewById(R.id.ll_first);
//        LinearLayout ll_third = (LinearLayout) view.findViewById(R.id.ll_third);
//        list_ll_first.add(ll_first);
//        list_ll_second.add(ll_second);
//        list_ll_third.add(ll_third);
//        if (note == null || note.equals(""))
//        {
//            textView.setText("暂无说明");
//        } else
//        {
//            textView.setText(note);
//        }
//        textView.setTextColor(textcolor);
//        textView.setTextSize(12);
        return marker;
    }
    public void initPointPolygon()
    {
        list_polygon_point = SqliteDb.getMoreLayer_point(MapUtils.this, "60");
        for (int i = 0; i < list_polygon_point.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(MapUtils.this, list_polygon_point.get(i).getUuid());
            if (list != null && list.size() != 0)
            {
               LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
                Marker marker = addCustomMarker(getResources().getColor(R.color.bg_job), latlng, list_polygon_point.get(i).getUuid(), list_polygon_point.get(i).getNote());
                list_Objects_point.add(marker);
            }

        }
    }

    public void initLinePolygon()
    {
        list_polygon_line = SqliteDb.getMoreLayer_line(MapUtils.this, "60");
        for (int i = 0; i < list_polygon_line.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(MapUtils.this, list_polygon_line.get(i).getUuid());
            List<LatLng> list_latlang = new ArrayList();
            for (int j = 0; j < list.size(); j++)
            {
               LatLng latlng = new LatLng(Double.valueOf(list.get(j).getLat()), Double.valueOf(list.get(j).getLng()));
                list_latlang.add(latlng);
            }
            PolylineOptions lineOpt = new PolylineOptions();
//            lineOpt.setDottedLine(true);
            lineOpt.addAll(list_latlang);
           Polyline line = tencentMap.addPolyline(lineOpt);
//            line.setGeodesic(true);
            line.setColor(Color.argb(500, 177, 15, 0));
            line.setWidth(8f);
            Overlays.add(line);
            list_Objects_line.add(line);

            if (list_latlang.size() > 0)
            {
                int halfsize = list_latlang.size() / 2;
                if (halfsize == 0)
                {
                   Marker marker = addCustomMarker(getResources().getColor(R.color.bg_sq), list_latlang.get(0), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
                    list_Objects_line_centermarker.add(marker);
                }else
                {
                   Marker marker = addCustomMarker(getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
                    list_Objects_line_centermarker.add(marker);
                }

            }

        }
    }

    public void initMianPolygon()
    {
        list_polygon_mian = SqliteDb.getMoreLayer_mian(MapUtils.this, "60");
        for (int i = 0; i < list_polygon_mian.size(); i++)
        {
           LatLng latlng = new LatLng(Double.valueOf(list_polygon_mian.get(i).getLat()), Double.valueOf(list_polygon_mian.get(i).getLng()));
           Marker marker = addCustomMarker(getResources().getColor(R.color.bg_ask), latlng, list_polygon_mian.get(i).getUuid(), list_polygon_mian.get(i).getNote());
            list_Objects_mian_centermarker.add(marker);
            List<CoordinatesBean> list_mian = SqliteDb.getPoints(MapUtils.this, list_polygon_mian.get(i).getUuid());
            List<LatLng> list_LatLng = new ArrayList<>();
            if (list_mian != null && list_mian.size() != 0)
            {
                for (int j = 0; j < list_mian.size(); j++)
                {
                   LatLng ll = new LatLng(Double.valueOf(list_mian.get(j).getLat()), Double.valueOf(list_mian.get(j).getLng()));
                    list_LatLng.add(ll);
                }
//                initBoundaryLine(Color.argb(180, 70, 101, 10), 0f, list_mian);
                Polygon polygon = drawPolygon(list_LatLng, Color.argb(180, 70, 101, 10));
                polygon.setZIndex(0f);
                Overlays.add(polygon);
                list_Objects_mian.add(polygon);
            }

        }
    }
    public void initHousePolygon()
    {
        list_polygon_house = SqliteDb.getMoreLayer_house(MapUtils.this, "60");
        for (int i = 0; i < list_polygon_house.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(MapUtils.this, list_polygon_house.get(i).getUuid());
            if (list != null && list.size() != 0)
            {
               LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
               Marker marker = addCustomMarkerWithHouse(getResources().getColor(R.color.bg_ask), latlng, list_polygon_house.get(i).getUuid(), list_polygon_house.get(i).getNote());
                list_Objects_house.add(marker);
            }

        }
    }
    private Marker addCustomMarkerWithHouse(int textcolor, LatLng latLng, String uuid, String note)
    {
//        Drawable drawable = getResources().getDrawable(R.drawable.location1);
//        Bitmap bitmap = utils.drawable2Bitmap(drawable);
//        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).icon(new BitmapDescriptor(bitmap)));
        final Marker marker = tencentMap.addMarker(new MarkerOptions().position(latLng).title(note).snippet(uuid).icon(BitmapDescriptorFactory.fromResource(R.drawable.umeng_socialize_follow_on)));
        marker.showInfoWindow();
        list_centermark.add(marker);
//        marker.set2Top();
//        View view = LayoutInflater.from(MapUtils.this).inflate(R.layout.markerwithhouse, null);
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
//        Bundle bundle=new Bundle();
//        bundle.putString("uuid",uuid);
//        bundle.putString("note", note);
//        marker.setTag(bundle);
        return marker;
    }
    public void initRoadPolygon()
    {
        list_polygon_road = SqliteDb.getMoreLayer_road(MapUtils.this, "60");
        for (int i = 0; i < list_polygon_road.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(MapUtils.this, list_polygon_road.get(i).getUuid());
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
            list_Objects_road.add(line);

            if (list_latlang.size() > 0)
            {
                int halfsize = list_latlang.size() / 2;
               Marker marker = addCustomMarker(getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_road.get(i).getUuid(), list_polygon_road.get(i).getNote());
                list_Objects_road_centermarker.add(marker);
            }

        }
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s)
    {

    }

    @Override
    public void onStatusUpdate(String s, int i, String s1)
    {

    }
}
