package com.farm.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.farm.R;
import com.farm.adapter.DL_ZS_Adapter;
import com.farm.adapter.Department_Adapter;
import com.farm.adapter.Operation_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.CoordinatesBean;
import com.farm.bean.DepartmentBean;
import com.farm.bean.Gps;
import com.farm.bean.Points;
import com.farm.bean.Result;
import com.farm.bean.ZS;
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.bean.parktab;
import com.farm.common.CoordinateConvertUtil;
import com.farm.common.FileHelper;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditDLInfor;
import com.farm.widget.CustomDialog_EditPolygonInfo;
import com.farm.widget.swipelistview.CustomDialog_OperatePolygon;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@EFragment
public class CZ_MakeMap_MakeLayer extends Fragment implements TencentLocationListener, View.OnClickListener
{
    List<String> list_operation;
    int intervalNumber=0;//marker间隔数
    List<DepartmentBean> list_department;
    List<parktab> list_parktab;
    List<areatab> list_areatab;
    List<areatab> list_areatab_all=new ArrayList<>();
    List<contractTab> list_contractTab;
    List<contractTab> list_contractTab_all=new ArrayList<>();
    boolean isend_select=false;
    boolean isend_notselect=false;
    List<Polygon> list_polygon_all;
    List<CoordinatesBean> list_CoordinatesBean_currentPolygon;
    List<  List<CoordinatesBean>> list_polygon_allCoordinatesBean;
    List<Polyline> list_Polyline;
    List<LatLng> list_LatLng_boundaryselect;
    List<LatLng> list_LatLng_boundarynotselect;
    List<LatLng> list_LatLng_inboundary=new ArrayList<>();
    List<CoordinatesBean> list_coordinatesbean;
    List<Marker> list_mark;//边界上的marker集合
    List<Marker> list_mark_inboundary;
    int last_pos = 0;
    int number_pointselect = 0;
    int number_markerselect = 0;
    boolean firstmarkerselect = true;
    LatLng latlng_one;
    LatLng latlng_two;
    List<LatLng> list_point_pq;//边界上的marker的坐标集
    HashMap<String, List<Marker>> map;
    Polygon polygon_select;
    List<Polygon> list_polygon_pq;

    List<ZS> list_zs;
    DL_ZS_Adapter dl_zs_adapter;
    Department_Adapter department_adapter;
    Operation_Adapter operation_adapter;
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
    CustomDialog_EditPolygonInfo customdialog_editpolygoninfor;
    CustomDialog_EditDLInfor customdialog_editdlinfor;
    CustomDialog_OperatePolygon customdialog_operatepolygon;
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
    Button btn_addlayer;
    @ViewById
    Button btn_addmore;
    @ViewById
    TextView tv_tip;
    @ViewById
    Button btn_addorder;
    @ViewById
    TextView tv_gk;
    @ViewById
    View line;
    @ViewById
    FrameLayout fl_map;
    @Click
    void btn_addlayer()
    {
        showDialog_department();
    }
    @Click
    void btn_addmore()
    {
        showDialog_SelectOperation();
    }
    @Click
    void tv_gk()
    {
        if (tv_gk.getText().equals("确定"))
        {
            for (int i = 0; i < list_mark.size(); i++)
            {
                list_mark.get(i).setVisible(false);
            }

            for (int i = 0; i < pointsList.size(); i++)
            {
                String uuid = java.util.UUID.randomUUID().toString();
                CoordinatesBean coordinatesBean = new CoordinatesBean();
                coordinatesBean.setLat(String.valueOf(pointsList.get(i).getLatitude()));
                coordinatesBean.setLng(String.valueOf(pointsList.get(i).getLongitude()));
                coordinatesBean.setNumofplant("10000");
                coordinatesBean.setType("farm_boundary");
                coordinatesBean.setUid("60");
                coordinatesBean.setparkId("15");
                coordinatesBean.setparkName("一号园区");
                coordinatesBean.setUuid(uuid);
                coordinatesBean.setAreaId("10");
                coordinatesBean.setareaName("片区一号");
                coordinatesBean.setContractid("");
                coordinatesBean.setContractname("");
                coordinatesBean.setBatchid("");
                coordinatesBean.setCoordinatestime(utils.getTime());
                coordinatesBean.setRegistime(utils.getTime());
                coordinatesBean.setWeightofplant("400000");
                coordinatesBean.setSaleid("");
                coordinatesBean.setOrders("");
//                SqliteDb.save(AddOrderMap.this, coordinatesBean);
//                uploadCoordinatesBean(coordinatesBean);
            }

//            resetData();
//            StringBuffer build = new StringBuffer();
//            build.append("{\"ResultCode\":1,\"Exception\":\"\",\"AffectedRows\":\"3\",\"Rows\":[");
//            for (int i = 0; i < pointsList.size(); i++)
//            {
//                build.append("{" + "\"" + "lat" + "\"" + ":" + "\"" + pointsList.get(i).getLatitude() + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + pointsList.get(i).getLongitude() + "\"" + "}" + ",");
//
//            }
//            build.replace(build.length() - 1, build.length(), "");
//            build.append("]}");
//            build.toString();
            tv_gk.setText("添加断蕾区");
        } else
        {
            tv_gk.setText("确定");
        }
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {
//                addMarker_Paint(0, latlng, R.drawable.location_start);
                pointsList.add(latlng);
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

    @Click
    void btn_addorder()
    {
        if (btn_addorder.getText().equals("确定"))
        {
            tv_tip.setVisibility(View.GONE);
            btn_addorder.setText("添加断蕾区");
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
            Polygon polygon = drawPolygon(pointsList, R.color.bg_blue);
            list_polygon.add(polygon);
//            showDialog_EditPolygonInfo();
            //重置数据
            resetData();

        } else
        {
            //重置数据
            list_Polyline = new ArrayList<>();
            list_mark_inboundary = new ArrayList<>();
            pointsList = new ArrayList<>();
            list_polygon = new ArrayList<>();
            list_LatLng_boundarynotselect=new ArrayList<>();
            list_LatLng_boundaryselect=new ArrayList<>();
            list_LatLng_inboundary=new ArrayList<>();
            last_pos = 0;
            number_markerselect = 0;
            number_pointselect = 0;
            firstmarkerselect = true;
            prelatLng = null;
//            polygon_select = null;


            tv_tip.setVisibility(View.VISIBLE);
            tv_tip.setText("请在承包区内选取点");
            btn_addorder.setText("确定");
            isStart = true;
//            polygon_select = list_polygon_pq.get(0);

            setMarkerListenner();
            tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
            {
                @Override
                public void onMapClick(LatLng latlng)
                {
//                    polygon_select = list_polygon_pq.get(0);
                    if (!polygon_select.contains(latlng))
                    {
                        tv_tip.setText("请在承包区内选取点");
                        tv_tip.setBackgroundResource(R.color.bg_job);
                        return;
                    }
                    if (number_markerselect> 0)//只要边界上有一个marker被选中,而且区域内也已经选取点了，就不能再点击地图了，只能点击marker
                    {
                        if (latlng_one == null)
                        {
                            latlng_one=pointsList.get(pointsList.size()-1);
                        }
                    }
                    list_LatLng_inboundary.add(latlng);
                    number_pointselect = number_pointselect + 1;
                    Marker m=addMarker_Paint(0, latlng, R.drawable.location_start);
                    list_mark_inboundary.add(m);
                    last_pos = 0;
                    firstmarkerselect = true;
                    pointsList.add(latlng);
                    PolylineOptions lineOpt = new PolylineOptions();
                    lineOpt.add(prelatLng);
                    prelatLng = latlng;
                    lineOpt.add(latlng);
                    Polyline line = tencentMap.addPolyline(lineOpt);
                    line.setColor(getActivity().getResources().getColor(R.color.black));
                    line.setWidth(4f);
                    list_Polyline.add(line);
                    Overlays.add(line);
                }
            });
        }
    }
//    @Click
//    void btn_addorder()
//    {
//        list_zs = SqliteDb.getZS(AddOrderMap.this, ZS.class, commembertab.getareaId());
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
//            SqliteDb.saveAll(AddOrderMap.this, pointsList);
            Polygon polygon = drawPolygon(pointsList, R.color.bg_blue);
            list_polygon.add(polygon);
//            showDialog_EditPolygonInfo();
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
                    list_Polyline.add(line);
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
//                    WindowManager.LayoutParams lp = AddOrderMap.this.getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    AddOrderMap.this.getWindow().setAttributes(lp);
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
//                    WindowManager.LayoutParams lp = AddOrderMap.this.getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    AddOrderMap.this.getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.MATCH_PARENT, 300, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);

//        WindowManager.LayoutParams lp = AddOrderMap.this.getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        AddOrderMap.this.getWindow().setAttributes(lp);
        pv_command.findViewById(R.id.btn_standardprocommand).setOnClickListener(CZ_MakeMap_MakeLayer.this);
        pv_command.findViewById(R.id.btn_nonstandardprocommand).setOnClickListener(CZ_MakeMap_MakeLayer.this);
        pv_command.findViewById(R.id.btn_nonprocommand).setOnClickListener(CZ_MakeMap_MakeLayer.this);
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
//                    WindowManager.LayoutParams lp = AddOrderMap.this.getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    AddOrderMap.this.getWindow().setAttributes(lp);
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
//                    WindowManager.LayoutParams lp = AddOrderMap.this.getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    AddOrderMap.this.getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, 250, LinearLayout.LayoutParams.MATCH_PARENT, true);
        pw_command.setAnimationStyle(R.style.leftinleftout);
        pw_command.showAsDropDown(line, 0, 0);
//        pw_command.showAtLocation(fl_map, Gravity.LEFT, 0, 500);
        pw_command.setOutsideTouchable(true);

//        WindowManager.LayoutParams lp = AddOrderMap.this.getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        AddOrderMap.this.getWindow().setAttributes(lp);
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
    public   List<DepartmentBean>  getDepartment(Context context)
    {
        DbUtils db = DbUtils.create(context);
        List<DepartmentBean> list = new ArrayList<>();
        try
        {
            list_parktab = db.findAll(Selector.from(parktab.class));
            if (list_parktab != null)
            {
                for (int i = 0; i <list_parktab.size() ; i++)
                {
                    DepartmentBean department=new DepartmentBean();
                    department.setId(list_parktab.get(i).getid());
                    department.setUid(list_parktab.get(i).getuId());
                    department.setParkid(list_parktab.get(i).getid());
                    department.setAreaid("");
                    department.setContractid("");
                    department.setType("园区");
                    department.setName(list_parktab.get(i).getparkName());
                    list.add(department);//添加
                    list_areatab = db.findAll(Selector.from(areatab.class).where("parkid","=",list_parktab.get(i).getid()));
                    list_areatab_all.addAll(list_areatab);
                    if (list_areatab != null)
                    {
                        for (int j = 0; j <list_areatab.size() ; j++)
                        {
                            DepartmentBean department1=new DepartmentBean();
                            department1.setId(list_areatab.get(j).getid());
                            department1.setUid(list_areatab.get(j).getuId());
                            department1.setParkid(list_areatab.get(j).getparkId());
                            department1.setAreaid(list_areatab.get(j).getid());
                            department1.setContractid("");
                            department1.setType("片区");
                            department1.setName(list_parktab.get(i).getparkName() + "-" + list_areatab.get(j).getareaName());
                            list.add(department1);//添加
                            list_contractTab = db.findAll(Selector.from(contractTab.class));
                            list_contractTab_all.addAll(list_contractTab);
                            if (list_contractTab != null)
                            {
                                for (int k = 0; k < list_contractTab.size(); k++)
                                {
                                    DepartmentBean department2=new DepartmentBean();
                                    department2.setId(list_contractTab.get(k).getid());
                                    department2.setUid(list_contractTab.get(k).getuId());
                                    department2.setParkid(list_contractTab.get(k).getparkId());
                                    department2.setAreaid(list_contractTab.get(k).getAreaId());
                                    department2.setContractid(list_contractTab.get(k).getid());
                                    department2.setType("承包区");
                                    department2.setName(list_parktab.get(i).getparkName() + "-" + list_areatab.get(j).getareaName() + "-"+list_contractTab.get(k).getContractNum());
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
    public void showDialog_SelectOperation()
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_editpolygoninfo, null);
        customdialog_editpolygoninfor = new CustomDialog_EditPolygonInfo(getActivity(), R.style.MyDialog, dialog_layout);
        ListView lv_department = (ListView) dialog_layout.findViewById(R.id.lv_department);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        list_operation=new ArrayList<>();
        JSONArray jsonArray=FileHelper.getAssetsJsonArray(getActivity(), "operation");
        for (int i = 0; i <jsonArray.size() ; i++)
        {
            list_operation.add(jsonArray.get(i).toString());
        }
        operation_adapter = new Operation_Adapter(getActivity(), list_operation);
        lv_department.setAdapter(operation_adapter);
        lv_department.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                customdialog_editpolygoninfor.dismiss();
               String operation= list_operation.get(position);
                if (operation.equals("添加规划图"))
                {

                }else if (operation.equals("画点"))
                {

                }else if (operation.equals("画线"))
                {

                }else if (operation.equals("画面"))
                {

                }else if (operation.equals("采点"))
                {

                }else if (operation.equals("采线"))
                {

                }else if (operation.equals("采面"))
                {

                }
            }
        });
        customdialog_editpolygoninfor.show();
    }
    public void showDialog_department( )
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_editpolygoninfo, null);
        customdialog_editpolygoninfor = new CustomDialog_EditPolygonInfo(getActivity(), R.style.MyDialog, dialog_layout);
        ListView lv_department = (ListView) dialog_layout.findViewById(R.id.lv_department);
        final Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        list_department=getDepartment(getActivity());
        department_adapter = new Department_Adapter(getActivity(), list_department);
        lv_department.setAdapter(department_adapter);
        lv_department.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                initMapOnclick();
                initMapAndParam();
                customdialog_editpolygoninfor.dismiss();
                DepartmentBean d = list_department.get(position);
                list_coordinatesbean= SqliteDb.getBoundaryByID(getActivity(),"60", d.getParkid(), d.getAreaid(), d.getContractid());
                showNeedPlanBoundary(list_coordinatesbean);
            }
        });
        customdialog_editpolygoninfor.show();
    }
    public void showDialog_EditPolygonInfo(final List<LatLng> list_select, final List<LatLng> list_notselect)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_editpolygoninfo, null);
        customdialog_editpolygoninfor = new CustomDialog_EditPolygonInfo(getActivity(), R.style.MyDialog, dialog_layout);
        ListView lv_department = (ListView) dialog_layout.findViewById(R.id.lv_department);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        list_department=getDepartment(getActivity());
        department_adapter = new Department_Adapter(getActivity(), list_department);
        lv_department.setAdapter(department_adapter);
        lv_department.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                customdialog_editpolygoninfor.dismiss();
                DepartmentBean d = list_department.get(position);
                String orders=list_coordinatesbean.get(0).getOrders();
                if (orders.equals(""))
                {
                    orders="0";
                }
               String order_new= String.valueOf(Integer.valueOf(orders) + 1);
                if (d.getType().equals("园区"))
                {
                    for (int i = 0; i < list_parktab.size(); i++)
                    {
                        if (d.getId().equals(list_parktab.get(i).getid()))
                        {
                            for (int m = 0; m < list_select.size(); m++)
                            {
                                String uuid = java.util.UUID.randomUUID().toString();
                                CoordinatesBean coordinatesBean = new CoordinatesBean();
                                coordinatesBean.setLat(String.valueOf(list_select.get(m).getLatitude()));
                                coordinatesBean.setLng(String.valueOf(list_select.get(m).getLongitude()));
                                coordinatesBean.setNumofplant("10000");
                                coordinatesBean.setType("farm_boundary");
                                coordinatesBean.setUid("60");
                                coordinatesBean.setparkId(list_parktab.get(i).getid());
                                coordinatesBean.setparkName(list_parktab.get(i).getparkName());
                                coordinatesBean.setUuid(uuid);
                                coordinatesBean.setAreaId("");
                                coordinatesBean.setareaName("");
                                coordinatesBean.setContractid("");
                                coordinatesBean.setContractname("");
                                coordinatesBean.setBatchid("");
                                coordinatesBean.setCoordinatestime(utils.getTime());
                                coordinatesBean.setRegistime(utils.getTime());
                                coordinatesBean.setWeightofplant("400000");
                                coordinatesBean.setSaleid("");
                                coordinatesBean.setOrders(order_new);
                                SqliteDb.save(getActivity(), coordinatesBean);
//                uploadCoordinatesBean(coordinatesBean);
                            }
                            for (int m = 0; m < list_notselect.size(); m++)
                            {
                                String uuid = java.util.UUID.randomUUID().toString();
                                CoordinatesBean coordinatesBean = new CoordinatesBean();
                                coordinatesBean.setLat(String.valueOf(list_notselect.get(m).getLatitude()));
                                coordinatesBean.setLng(String.valueOf(list_notselect.get(m).getLongitude()));
                                coordinatesBean.setNumofplant("10000");
                                coordinatesBean.setType("farm_boundary_free");
                                coordinatesBean.setUid("60");
                                coordinatesBean.setparkId(list_parktab.get(i).getid());
                                coordinatesBean.setparkName(list_parktab.get(i).getparkName());
                                coordinatesBean.setUuid(uuid);
                                coordinatesBean.setAreaId("");
                                coordinatesBean.setareaName("");
                                coordinatesBean.setContractid("");
                                coordinatesBean.setContractname("");
                                coordinatesBean.setBatchid("");
                                coordinatesBean.setCoordinatestime(utils.getTime());
                                coordinatesBean.setRegistime(utils.getTime());
                                coordinatesBean.setWeightofplant("400000");
                                coordinatesBean.setSaleid("");
                                coordinatesBean.setOrders(order_new);
                                SqliteDb.save(getActivity(), coordinatesBean);
//                uploadCoordinatesBean(coordinatesBean);
                            }
                            break;
                        }
                    }
                } else if (d.getType().equals("片区"))
                {
                    for (int i = 0; i < list_areatab_all.size(); i++)
                    {
                        if (d.getId().equals(list_areatab_all.get(i).getid()))
                        {
                            for (int m = 0; m < list_select.size(); m++)
                            {
                                String uuid = java.util.UUID.randomUUID().toString();
                                CoordinatesBean coordinatesBean = new CoordinatesBean();
                                coordinatesBean.setLat(String.valueOf(list_select.get(m).getLatitude()));
                                coordinatesBean.setLng(String.valueOf(list_select.get(m).getLongitude()));
                                coordinatesBean.setNumofplant("10000");
                                coordinatesBean.setType("farm_boundary");
                                coordinatesBean.setUid("60");
                                coordinatesBean.setparkId(list_areatab_all.get(i).getparkId());
                                coordinatesBean.setparkName(list_areatab_all.get(i).getparkName());
                                coordinatesBean.setUuid(uuid);
                                coordinatesBean.setAreaId(list_areatab_all.get(i).getid());
                                coordinatesBean.setareaName(list_areatab_all.get(i).getareaName());
                                coordinatesBean.setContractid("");
                                coordinatesBean.setContractname("");
                                coordinatesBean.setBatchid("");
                                coordinatesBean.setCoordinatestime(utils.getTime());
                                coordinatesBean.setRegistime(utils.getTime());
                                coordinatesBean.setWeightofplant("400000");
                                coordinatesBean.setSaleid("");
                                coordinatesBean.setOrders(order_new);
                                SqliteDb.save(getActivity(), coordinatesBean);
//                uploadCoordinatesBean(coordinatesBean);
                            }
                            for (int m = 0; m < list_notselect.size(); m++)
                            {
                                String uuid = java.util.UUID.randomUUID().toString();
                                CoordinatesBean coordinatesBean = new CoordinatesBean();
                                coordinatesBean.setLat(String.valueOf(list_notselect.get(m).getLatitude()));
                                coordinatesBean.setLng(String.valueOf(list_notselect.get(m).getLongitude()));
                                coordinatesBean.setNumofplant("10000");
                                coordinatesBean.setType("farm_boundary_free");
                                coordinatesBean.setUid("60");
                                coordinatesBean.setparkId(list_areatab_all.get(i).getparkId());
                                coordinatesBean.setparkName(list_areatab_all.get(i).getparkName());
                                coordinatesBean.setUuid(uuid);
                                coordinatesBean.setAreaId("");//画选片区，list_notselect则只能为园区的未规划图，所以为空
                                coordinatesBean.setareaName("");//画选片区，list_notselect则只能为园区的未规划图，所以为空
                                coordinatesBean.setContractid("");
                                coordinatesBean.setContractname("");
                                coordinatesBean.setBatchid("");
                                coordinatesBean.setCoordinatestime(utils.getTime());
                                coordinatesBean.setRegistime(utils.getTime());
                                coordinatesBean.setWeightofplant("400000");
                                coordinatesBean.setSaleid("");
                                coordinatesBean.setOrders(order_new);
                                SqliteDb.save(getActivity(), coordinatesBean);
//                uploadCoordinatesBean(coordinatesBean);
                            }
                            break;
                        }
                    }
                } else
                {
                    for (int i = 0; i < list_contractTab_all.size(); i++)
                    {
                        if (d.getId().equals(list_contractTab.get(i).getid()))
                        {
                            for (int m = 0; m < list_select.size(); m++)
                            {
                                String uuid = java.util.UUID.randomUUID().toString();
                                CoordinatesBean coordinatesBean = new CoordinatesBean();
                                coordinatesBean.setLat(String.valueOf(list_select.get(m).getLatitude()));
                                coordinatesBean.setLng(String.valueOf(list_select.get(m).getLongitude()));
                                coordinatesBean.setNumofplant("10000");
                                coordinatesBean.setType("farm_boundary");
                                coordinatesBean.setUid("60");
                                coordinatesBean.setparkId(list_contractTab_all.get(i).getparkId());
                                coordinatesBean.setparkName(list_contractTab_all.get(i).getparkName());
                                coordinatesBean.setUuid(uuid);
                                coordinatesBean.setAreaId(list_contractTab_all.get(i).getAreaId());
                                coordinatesBean.setareaName(list_contractTab_all.get(i).getareaName());
                                coordinatesBean.setContractid(list_contractTab_all.get(i).getid());
                                coordinatesBean.setContractname(list_contractTab_all.get(i).getContractNum());
                                coordinatesBean.setBatchid("");
                                coordinatesBean.setCoordinatestime(utils.getTime());
                                coordinatesBean.setRegistime(utils.getTime());
                                coordinatesBean.setWeightofplant("400000");
                                coordinatesBean.setSaleid("");
                                coordinatesBean.setOrders(order_new);
                                SqliteDb.save(getActivity(), coordinatesBean);
//                uploadCoordinatesBean(coordinatesBean);
                            }
                            for (int m = 0; m < list_notselect.size(); m++)
                            {
                                String uuid = java.util.UUID.randomUUID().toString();
                                CoordinatesBean coordinatesBean = new CoordinatesBean();
                                coordinatesBean.setLat(String.valueOf(list_notselect.get(m).getLatitude()));
                                coordinatesBean.setLng(String.valueOf(list_notselect.get(m).getLongitude()));
                                coordinatesBean.setNumofplant("10000");
                                coordinatesBean.setType("farm_boundary_free");
                                coordinatesBean.setUid("60");
                                coordinatesBean.setparkId(list_contractTab_all.get(i).getparkId());
                                coordinatesBean.setparkName(list_contractTab_all.get(i).getparkName());
                                coordinatesBean.setUuid(uuid);
                                coordinatesBean.setAreaId(list_contractTab_all.get(i).getAreaId());
                                coordinatesBean.setareaName(list_contractTab_all.get(i).getareaName());
                                coordinatesBean.setContractid("");//画选承包区，list_notselect则只能为片区的未规划图，所以为空
                                coordinatesBean.setContractname("");//画选承包区，list_notselect则只能为片区的未规划图，所以为空
                                coordinatesBean.setBatchid("");
                                coordinatesBean.setCoordinatestime(utils.getTime());
                                coordinatesBean.setRegistime(utils.getTime());
                                coordinatesBean.setWeightofplant("400000");
                                coordinatesBean.setSaleid("");
                                coordinatesBean.setOrders(order_new);
                                SqliteDb.save(getActivity(), coordinatesBean);
//                uploadCoordinatesBean(coordinatesBean);
                            }
                            break;
                        }
                    }
                }

            }
        });
        customdialog_editpolygoninfor.show();
    }
    public void showDialog_OperatePolygon()
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_operatepolygon, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(getActivity(), R.style.MyDialog, dialog_layout);
        Button  btn_paint = (Button) dialog_layout.findViewById(R.id.btn_paint);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_change = (Button) dialog_layout.findViewById(R.id.btn_change);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        Button btn_delete = (Button) dialog_layout.findViewById(R.id.btn_delete);
        btn_paint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                initMapAndParam();
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                initMapAndParam();
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.cz_makemap_makelayer, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }

    @AfterViews
    void afterOncreate()
    {
//       List<CoordinatesBean> list11=SqliteDb.getTemp1(getActivity());
//        CoordinatesBean cc= list11.get(0);
//        for (int i = 0; i < list11.size(); i++)
//        {
//            CoordinatesBean cc= list11.get(i);
//            cc.setAreaId("");
//            cc.setareaName("");
//            SqliteDb.updateCoordinatesOrder(getActivity(),cc);
//        }
//        SqliteDb.initPark(AddOrderMap.this);
//        SqliteDb.initArea(AddOrderMap.this);
//        SqliteDb.initContract(AddOrderMap.this);


//        setBoundary_park(list_boundary_park);
//        for (int i = 0; i <list_boundary_park.size() ; i++)
//        {
//            uploadCoordinatesBean(list_boundary_park.get(i));
//        }
//        getBoundary_farm();
//        SqliteDb.deletetemp(getActivity());

        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(getActivity());
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
        Overlays = new ArrayList<Object>();
        list_polygon = new ArrayList<Polygon>();
        list_polygon_pq = new ArrayList<Polygon>();
        list_point_pq = new ArrayList<>();
        list_polygon_all = new ArrayList<>();
        list_polygon_allCoordinatesBean = new ArrayList<>();
        map = new HashMap<>();
        if (!utils.isOPen(getActivity()))
        {
            utils.openGPSSettings(getActivity());
        }
//        mapview.setAlpha(0.2f);
        tencentMap = mapview.getMap();
        tencentMap.setZoom(14);
//        setMarkerListenner();
//        tencentMap.setSatelliteEnabled(true);
        mProjection = mapview.getProjection();

        //        animateToLocation();
//        getTestData("points");
//        initMapOnclick();



//        String str_farm_boundary_free = SqliteDb.getBoundary_farm_free(getActivity(), "60", "farm_boundary_free");
//        Result result_farm_boundary_free = JSON.parseObject(str_farm_boundary_free, Result.class);
//        if (result_farm_boundary_free.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//        {
//            if (result_farm_boundary_free.getAffectedRows() != 0)
//            {
//                JSONArray jsonArray_Rows = result_farm_boundary_free.getRows();
//                for (int i = 0; i < jsonArray_Rows.size(); i++)
//                {
//                    JSONArray jsonArray_boundary = jsonArray_Rows.getJSONArray(i);
//                    if (jsonArray_boundary.size() > 0)
//                    {
//                        list_coordinatesbean = JSON.parseArray(jsonArray_boundary.toJSONString(), CoordinatesBean.class);
//                        setBoundary_park(list_coordinatesbean);
//                    }
//                }
//            }
//        }

        String str_farm_boundary = SqliteDb.getBoundary_farm(getActivity(), "60", "farm_boundary");
        Result result_farm_boundary = JSON.parseObject(str_farm_boundary, Result.class);
        if (result_farm_boundary.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
        {
            if (result_farm_boundary.getAffectedRows() != 0)
            {
                JSONArray jsonArray_Rows = result_farm_boundary.getRows();
                for (int i = 0; i < jsonArray_Rows.size(); i++)
                {
                    JSONArray jsonArray_boundary = jsonArray_Rows.getJSONArray(i);
                    if (jsonArray_boundary.size() > 0)
                    {
                      List<CoordinatesBean>  list = JSON.parseArray(jsonArray_boundary.toJSONString(), CoordinatesBean.class);
                        initBoundary(list);
                    }
                }
            }
        }

//        String str_farm_boundary = SqliteDb.getBoundary_farm_free(getActivity(), "60", "farm_boundary");
//        Result result_farm_boundary = JSON.parseObject(str_farm_boundary, Result.class);
//        if (result_farm_boundary.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//        {
//            if (result_farm_boundary.getAffectedRows() != 0)
//            {
//                JSONArray jsonArray_Rows = result_farm_boundary.getRows();
//                for (int i = 0; i < jsonArray_Rows.size(); i++)
//                {
//                    JSONArray jsonArray_boundary = jsonArray_Rows.getJSONArray(i);
//                    if (jsonArray_boundary.size() > 0)
//                    {
//                        list_coordinatesbean = JSON.parseArray(jsonArray_boundary.toJSONString(), CoordinatesBean.class);
//                        setBoundary_park(list_coordinatesbean);
//                    }
//                }
//            }
//        }
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
            // Toast.makeText(AddOrderMap.this, " 位置权限拒绝！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == 2)// 定位服务关闭
        {
            // Toast.makeText(AddOrderMap.this, " 定位服务关闭！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == 1)// 定位服务开启
        {
            // Toast.makeText(AddOrderMap.this, "定位服务开启！",
            // Toast.LENGTH_SHORT).show();
        } else if (status == -1)// 定位服务未知
        {
            // Toast.makeText(AddOrderMap.this, "定位服务未知！",
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

//    private void getTestData(String from)
//    {
//        JSONObject jsonObject = utils.parseJsonFile(AddOrderMap.this, "dictionary.json");
//        Result result = JSON.parseObject(jsonObject.getString(from), Result.class);
//        list_point_pq = JSON.parseArray(result.getRows().toJSONString(), Points.class);
//        List<LatLng> list_LatLng = new ArrayList<>();
//        list_mark = new ArrayList<>();
//        for (int i = 0; i < list_point_pq.size(); i++)
//        {
//            LatLng latlng = new LatLng(Double.valueOf(list_point_pq.get(i).getLat()), Double.valueOf(list_point_pq.get(i).getLon()));
//            if (i == 0)
//            {
//                tencentMap.animateTo(latlng);
//            }
//            list_LatLng.add(latlng);
//            Marker marker = addMarker_Paint(i, latlng, R.drawable.location_start);
//            list_mark.add(marker);
//        }
//        map.put("a", list_mark);
//        Polygon polygon = drawPolygon(list_LatLng, R.color.bg_yellow);
//        list_polygon_pq.add(polygon);
//        Overlays.add(polygon);
//    }

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
            list_Polyline.add(line);
        }
    }

    private void setMarkerListenner()
    {
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                String[] str = marker.getTitle().toString().split(",");
                LatLng latlng = new LatLng(Double.valueOf(str[0]), Double.valueOf(str[1]));
                int pos = Integer.valueOf(str[2]);
                for (int i = 0; i < pointsList.size(); i++)//判断点是否已经选取过
                {
                    if (pointsList.get(i).getLatitude() == latlng.getLatitude() && pointsList.get(i).getLongitude() == latlng.getLongitude())
                    {
                        //已经选取过，判断是否完成区域选择
                        if (number_markerselect > 2 && pointsList.get(0).getLatitude() == latlng.getLatitude() && pointsList.get(0).getLongitude() == latlng.getLongitude())
                        {

                            for (int j = 0; j <list_LatLng_inboundary.size() ; j++)
                            {
                                if (latlng.toString().equals(list_LatLng_inboundary.get(j).toString()))//最终点为内点
                                {
                                    if (latlng_two == null)
                                    {
                                        latlng_two=pointsList.get(pointsList.size()-1);
                                    }else   if (latlng_one== null)
                                    {
                                        latlng_one=pointsList.get(pointsList.size()-1);
                                    }
                                }
                                if (j == list_LatLng_inboundary.size()-1)//最终点为边界点
                                {
                                    if (latlng_two == null)
                                    {
                                        latlng_two=latlng;
                                    }else   if (latlng_one== null)
                                    {
                                        latlng_one=latlng;
                                    }
                                }
                            }
                            tv_tip.setVisibility(View.GONE);
                            btn_addorder.setText("添加断蕾区");
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
                                            }
                                        }

                                    }
                                }
                            });
                            if (pos > last_pos)
                            {
                                if (pos - last_pos == 1)//边界相邻两点
                                {
                                    LatLng ll = list_point_pq.get(last_pos);
                                    int currentmarker_pos = 0;
                                    int lastmarker_pos = 0;
                                    for (int j = 0; j < list_coordinatesbean.size(); j++)
                                    {
                                        String a = list_coordinatesbean.get(j).getLat();
                                        String c = String.valueOf(latlng.getLatitude());

                                        String b = list_coordinatesbean.get(j).getLng();
                                        String d = String.valueOf(latlng.getLongitude());
                                        if ((list_coordinatesbean.get(j).getLat().equals(String.valueOf(latlng.getLatitude())) && list_coordinatesbean.get(j).getLng().equals(String.valueOf(latlng.getLongitude()))))
                                        {
                                            currentmarker_pos = j+1;
                                        }
                                        if ((list_coordinatesbean.get(j).getLat().equals(String.valueOf(ll.getLatitude())) && list_coordinatesbean.get(j).getLng().equals(String.valueOf(ll.getLongitude()))))
                                        {
                                            lastmarker_pos = j+1;
                                        }
                                    }

                                    if (lastmarker_pos > currentmarker_pos)//80-0-7   80-7-0
                                    {
                                        if (lastmarker_pos == currentmarker_pos + intervalNumber)//非首尾相邻
                                        {
                                            for (int k = lastmarker_pos; k > currentmarker_pos; k--)
                                            {
                                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(k).getLat()), Double.valueOf(list_coordinatesbean.get(k).getLng()));
                                                number_markerselect = number_markerselect + 1;
                                                last_pos = pos;
                                                pointsList.add(l);
                                                PolylineOptions lineOpt = new PolylineOptions();
                                                lineOpt.add(prelatLng);
                                                prelatLng = l;
                                                lineOpt.add(l);
                                                Polyline line = tencentMap.addPolyline(lineOpt);
                                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                                line.setWidth(4f);
                                                Overlays.add(line);
                                                list_Polyline.add(line);
                                            }

                                        } else//首尾相邻80-0-7   80-7-0
                                        {
                                            for (int k = lastmarker_pos; k < list_coordinatesbean.size() - 1; k++)
                                            {
                                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(k).getLat()), Double.valueOf(list_coordinatesbean.get(k).getLng()));
                                                number_markerselect = number_markerselect + 1;
                                                last_pos = pos;
                                                pointsList.add(l);
                                                PolylineOptions lineOpt = new PolylineOptions();
                                                lineOpt.add(prelatLng);
                                                prelatLng = l;
                                                lineOpt.add(l);
                                                Polyline line = tencentMap.addPolyline(lineOpt);
                                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                                line.setWidth(4f);
                                                Overlays.add(line);
                                                list_Polyline.add(line);
                                            }

                                            for (int k = 0; k < currentmarker_pos; k++)
                                            {
                                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(k).getLat()), Double.valueOf(list_coordinatesbean.get(k).getLng()));
                                                number_markerselect = number_markerselect + 1;
                                                last_pos = pos;
                                                pointsList.add(l);
                                                PolylineOptions lineOpt = new PolylineOptions();
                                                lineOpt.add(prelatLng);
                                                prelatLng = l;
                                                lineOpt.add(l);
                                                Polyline line = tencentMap.addPolyline(lineOpt);
                                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                                line.setWidth(4f);
                                                Overlays.add(line);
                                                list_Polyline.add(line);
                                            }


                                        }
                                    } else//现在点大7>80
                                    {
                                        if (currentmarker_pos == lastmarker_pos + intervalNumber)//非首尾相邻
                                        {
                                            for (int k = lastmarker_pos; k < currentmarker_pos; k++)
                                            {
                                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(k).getLat()), Double.valueOf(list_coordinatesbean.get(k).getLng()));
                                                number_markerselect = number_markerselect + 1;
                                                last_pos = pos;
                                                pointsList.add(l);
                                                PolylineOptions lineOpt = new PolylineOptions();
                                                lineOpt.add(prelatLng);
                                                prelatLng = l;
                                                lineOpt.add(l);
                                                Polyline line = tencentMap.addPolyline(lineOpt);
                                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                                line.setWidth(4f);
                                                Overlays.add(line);
                                                list_Polyline.add(line);
                                            }

                                        } else//首尾相邻7>80
                                        {
                                            for (int k = lastmarker_pos; k >= 0; k--)//
                                            {
                                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(k).getLat()), Double.valueOf(list_coordinatesbean.get(k).getLng()));
                                                number_markerselect = number_markerselect + 1;
                                                last_pos = pos;
                                                pointsList.add(l);
                                                PolylineOptions lineOpt = new PolylineOptions();
                                                lineOpt.add(prelatLng);
                                                prelatLng = l;
                                                lineOpt.add(l);
                                                Polyline line = tencentMap.addPolyline(lineOpt);
                                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                                line.setWidth(4f);
                                                Overlays.add(line);
                                                list_Polyline.add(line);
                                            }
                                            for (int k = list_coordinatesbean.size() - 1; k>= currentmarker_pos; k--)
                                            {
                                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(k).getLat()), Double.valueOf(list_coordinatesbean.get(k).getLng()));
                                                number_markerselect = number_markerselect + 1;
                                                last_pos = pos;
                                                pointsList.add(l);
                                                PolylineOptions lineOpt = new PolylineOptions();
                                                lineOpt.add(prelatLng);
                                                prelatLng = l;
                                                lineOpt.add(l);
                                                Polyline line = tencentMap.addPolyline(lineOpt);
                                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                                line.setWidth(4f);
                                                Overlays.add(line);
                                                list_Polyline.add(line);
                                            }


                                        }
                                    }

                                } else
                                {

                                }
                            } else
                            {

                            }

//                            int currentmarker_pos = 0;
//                            int lastmarker_pos = 0;
//                            for (int k = 0; k < list_coordinatesbean.size(); k++)
//                            {
//                                if ((list_coordinatesbean.get(k).getLat().equals(String.valueOf(latlng_one.getLatitude())) && list_coordinatesbean.get(k).getLng().equals(String.valueOf(latlng_one.getLongitude()))))
//                                {
//                                    currentmarker_pos = Integer.valueOf(list_coordinatesbean.get(k).getId());
//                                }
//                                if ((list_coordinatesbean.get(k).getLat().equals(String.valueOf(latlng_two.getLatitude())) && list_coordinatesbean.get(k).getLng().equals(String.valueOf(latlng_two.getLongitude()))))
//                                {
//                                    lastmarker_pos = Integer.valueOf(list_coordinatesbean.get(k).getId());
//                                }
//                            }
//                            int max=0;
//                            int min=0;
//                            if (lastmarker_pos>currentmarker_pos)
//                            {
//                                max=lastmarker_pos;
//                                min=currentmarker_pos;
//                            }else
//                            {
//                                max=currentmarker_pos;
//                                min=lastmarker_pos;
//                            }
//                                for (int m = min; m <=max; m++)
//                                {
//                                    LatLng l=new LatLng(Double.valueOf(list_coordinatesbean.get(m).getLat()),Double.valueOf(list_coordinatesbean.get(m).getLng()));
//                                    list_LatLng_boundaryselect.add(l);
//                                }
//                                for (int n =max; n <list_coordinatesbean.size() ; n++)
//                                {
//                                    LatLng l=new LatLng(Double.valueOf(list_coordinatesbean.get(n).getLat()),Double.valueOf(list_coordinatesbean.get(n).getLng()));
//                                    list_LatLng_boundarynotselect.add(l);
//                                }
//                                for (int n =0; n <=min ; n++)
//                                {
//                                    LatLng l=new LatLng(Double.valueOf(list_coordinatesbean.get(n).getLat()),Double.valueOf(list_coordinatesbean.get(n).getLng()));
//                                    list_LatLng_boundarynotselect.add(l);
//                                }
//
//                            if (list_LatLng_inboundary != null)
//                            {
//                                for (int j = list_LatLng_inboundary.size()-1; j >=0 ; j--)
//                                {
//                                    list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(j));
//                                }
//                                list_LatLng_boundaryselect.addAll(list_LatLng_inboundary);
//                            }
//                            Polygon polygon1 = drawPolygon(list_LatLng_boundarynotselect, R.color.bg_blue);
//                            list_polygon.add(polygon1);
//
//                            Polygon polygon2 = drawPolygon(list_LatLng_boundaryselect, R.color.bg_yellow);
//                            list_polygon.add(polygon2);
////                            Overlays.remove(list_Polyline);
//                            for (int m = 0; m <list_Polyline.size() ; m++)
//                            {
//                                list_Polyline.get(m).remove();
//                            }
//                            for (int m = 0; m <list_mark.size() ; m++)
//                            {
//                                list_mark.get(m).remove();
//                            }
//                            for (int m = 0; m <list_mark_inboundary.size() ; m++)
//                            {
//                                list_mark_inboundary.get(m).remove();
//                            }
//                            Overlays.remove(list_);
//                            showDialog_EditPolygonInfo();
                            //重置数据
//                            resetData();
//                            divide();
                            dividePolygon();
                            return false;
                        }
                        if (number_markerselect == 2 && number_pointselect > 0 && pointsList.get(0).getLatitude() == latlng.getLatitude() && pointsList.get(0).getLongitude() == latlng.getLongitude())
                        {

                            tv_tip.setVisibility(View.GONE);
                            btn_addorder.setText("添加断蕾区");
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
//                            divide();
                            dividePolygon();
//                            Polygon polygon = drawPolygon(pointsList, R.color.bg_blue);
//                            list_polygon.add(polygon);
//                            showDialog_EditPolygonInfo();
//                            resetData();
                            return false;
                        }
                        tv_tip.setText("该点已经选取，请按顺序选择边界上的点");
                        tv_tip.setBackgroundResource(R.color.bg_blue);
                        return false;
                    }
                }
                if (firstmarkerselect)
                {
                    number_markerselect = number_markerselect + 1;
                    last_pos = pos;
                    firstmarkerselect = false;
                    pointsList.add(latlng);
                    PolylineOptions lineOpt = new PolylineOptions();
                    lineOpt.add(prelatLng);
                    prelatLng = latlng;
                    lineOpt.add(latlng);
                    Polyline line = tencentMap.addPolyline(lineOpt);
                    line.setColor(getActivity().getResources().getColor(R.color.black));
                    line.setWidth(4f);
                    Overlays.add(line);
                    list_Polyline.add(line);
                    if (number_pointselect > 0)//已经有点
                    {
                        if (latlng_two == null)
                        {
                            latlng_two=latlng;
                        }
                        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
                        {
                            @Override
                            public void onMapClick(LatLng latlng)
                            {
                                tv_tip.setText("请按顺序选择边界上的点");
                                tv_tip.setBackgroundResource(R.color.bg_blue);
                            }
                        });
                    }

                    return false;
                }

                if (number_pointselect > 0)//只要边界上有一个marker被选中,而且区域内也已经选取点了，就不能再点击地图了，只能点击marker
                {
                    if (latlng_two == null)
                    {
                        latlng_two=latlng;
                    }

                    tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
                    {
                        @Override
                        public void onMapClick(LatLng latlng)
                        {
                            tv_tip.setText("请按顺序选择边界上的点");
                            tv_tip.setBackgroundResource(R.color.bg_blue);
                        }
                    });
                }

                LatLng point_next;
                LatLng point_last;
                if (last_pos == 0)
                {
                    point_next = new LatLng(Double.valueOf(list_point_pq.get(last_pos + 1).getLatitude()), Double.valueOf(list_point_pq.get(last_pos + 1).getLongitude()));
                    point_last = new LatLng(Double.valueOf(list_point_pq.get(list_point_pq.size() - 1).getLatitude()), Double.valueOf(list_point_pq.get(list_point_pq.size() - 1).getLongitude()));
                } else if (last_pos == list_point_pq.size() - 1)//画区域时会再加多第一个点
                {
                    point_next = new LatLng(Double.valueOf(list_point_pq.get(0).getLatitude()), Double.valueOf(list_point_pq.get(0).getLongitude()));
                    point_last = new LatLng(Double.valueOf(list_point_pq.get(last_pos - 1).getLatitude()), Double.valueOf(list_point_pq.get(last_pos - 1).getLongitude()));
                } else
                {
                    point_next = new LatLng(Double.valueOf(list_point_pq.get(last_pos + 1).getLatitude()), Double.valueOf(list_point_pq.get(last_pos + 1).getLongitude()));
                    point_last = new LatLng(Double.valueOf(list_point_pq.get(last_pos - 1).getLatitude()), Double.valueOf(list_point_pq.get(last_pos - 1).getLongitude()));
                }
                if ((point_last.getLatitude() == (latlng.getLatitude()) && point_last.getLongitude() == (latlng.getLongitude())) || (point_next.getLatitude() == (latlng.getLatitude()) && point_next.getLongitude() == (latlng.getLongitude())))
                {
                    LatLng ll = list_point_pq.get(last_pos);
                    int currentmarker_pos = 0;
                    int lastmarker_pos = 0;
                    for (int i = 0; i < list_coordinatesbean.size(); i++)
                    {
                        String a = list_coordinatesbean.get(i).getLat();
                        String c = String.valueOf(latlng.getLatitude());

                        String b = list_coordinatesbean.get(i).getLng();
                        String d = String.valueOf(latlng.getLongitude());
                        if ((list_coordinatesbean.get(i).getLat().equals(String.valueOf(latlng.getLatitude())) && list_coordinatesbean.get(i).getLng().equals(String.valueOf(latlng.getLongitude()))))
                        {
                            currentmarker_pos = i+1;
                        }
                        if ((list_coordinatesbean.get(i).getLat().equals(String.valueOf(ll.getLatitude())) && list_coordinatesbean.get(i).getLng().equals(String.valueOf(ll.getLongitude()))))
                        {
                            lastmarker_pos = i+1;
                        }
                    }

                    if (lastmarker_pos > currentmarker_pos)//80-0-7   80-7-0
                    {
                        if (lastmarker_pos == currentmarker_pos + intervalNumber)//非首尾相邻
                        {
                            for (int i = lastmarker_pos; i > currentmarker_pos; i--)
                            {
                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                                number_markerselect = number_markerselect + 1;
                                last_pos = pos;
                                pointsList.add(l);
                                PolylineOptions lineOpt = new PolylineOptions();
                                lineOpt.add(prelatLng);
                                prelatLng = l;
                                lineOpt.add(l);
                                Polyline line = tencentMap.addPolyline(lineOpt);
                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                line.setWidth(4f);
                                Overlays.add(line);
                                list_Polyline.add(line);
                            }

                        } else//首尾相邻80-0-7   80-7-0
                        {
                            for (int i = lastmarker_pos; i < list_coordinatesbean.size() - 1; i++)
                            {
                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                                number_markerselect = number_markerselect + 1;
                                last_pos = pos;
                                pointsList.add(l);
                                PolylineOptions lineOpt = new PolylineOptions();
                                lineOpt.add(prelatLng);
                                prelatLng = l;
                                lineOpt.add(l);
                                Polyline line = tencentMap.addPolyline(lineOpt);
                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                line.setWidth(4f);
                                Overlays.add(line);
                                list_Polyline.add(line);
                            }

                            for (int i = 0; i < currentmarker_pos; i++)
                            {
                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                                number_markerselect = number_markerselect + 1;
                                last_pos = pos;
                                pointsList.add(l);
                                PolylineOptions lineOpt = new PolylineOptions();
                                lineOpt.add(prelatLng);
                                prelatLng = l;
                                lineOpt.add(l);
                                Polyline line = tencentMap.addPolyline(lineOpt);
                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                line.setWidth(4f);
                                Overlays.add(line);
                                list_Polyline.add(line);
                            }

//                            number_markerselect = number_markerselect + 1;
//                            last_pos = pos;
//                            pointsList.add(latlng);
//                            PolylineOptions lineOpt = new PolylineOptions();
//                            lineOpt.add(prelatLng);
//                            prelatLng = latlng;
//                            lineOpt.add(latlng);
//                            Polyline line = tencentMap.addPolyline(lineOpt);
//                            line.setColor(AddOrderMap.this.getResources().getColor(R.color.black));
//                            line.setWidth(4f);
//                            Overlays.add(line);
                        }
                    } else//现在点大7>80
                    {
                        if (currentmarker_pos == lastmarker_pos + intervalNumber)//非首尾相邻
                        {
                            for (int i = lastmarker_pos; i < currentmarker_pos; i++)
                            {
                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                                number_markerselect = number_markerselect + 1;
                                last_pos = pos;
                                pointsList.add(l);
                                PolylineOptions lineOpt = new PolylineOptions();
                                lineOpt.add(prelatLng);
                                prelatLng = l;
                                lineOpt.add(l);
                                Polyline line = tencentMap.addPolyline(lineOpt);
                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                line.setWidth(4f);
                                Overlays.add(line);
                                list_Polyline.add(line);
                            }

                        } else//首尾相邻7>80
                        {
                            for (int i = lastmarker_pos; i >= 0; i--)//
                            {
                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                                number_markerselect = number_markerselect + 1;
                                last_pos = pos;
                                pointsList.add(l);
                                PolylineOptions lineOpt = new PolylineOptions();
                                lineOpt.add(prelatLng);
                                prelatLng = l;
                                lineOpt.add(l);
                                Polyline line = tencentMap.addPolyline(lineOpt);
                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                line.setWidth(4f);
                                Overlays.add(line);
                                list_Polyline.add(line);
                            }
                            for (int i = list_coordinatesbean.size() - 1; i >= currentmarker_pos; i--)
                            {
                                LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                                number_markerselect = number_markerselect + 1;
                                last_pos = pos;
                                pointsList.add(l);
                                PolylineOptions lineOpt = new PolylineOptions();
                                lineOpt.add(prelatLng);
                                prelatLng = l;
                                lineOpt.add(l);
                                Polyline line = tencentMap.addPolyline(lineOpt);
                                line.setColor(getActivity().getResources().getColor(R.color.black));
                                line.setWidth(4f);
                                Overlays.add(line);
                                list_Polyline.add(line);
                            }


//                            number_markerselect = number_markerselect + 1;
//                            last_pos = pos;
//                            pointsList.add(latlng);
//                            PolylineOptions lineOpt = new PolylineOptions();
//                            lineOpt.add(prelatLng);
//                            prelatLng = latlng;
//                            lineOpt.add(latlng);
//                            Polyline line = tencentMap.addPolyline(lineOpt);
//                            line.setColor(AddOrderMap.this.getResources().getColor(R.color.black));
//                            line.setWidth(4f);
//                            Overlays.add(line);
                        }
                    }
//                    number_markerselect = number_markerselect + 1;
//                    last_pos = pos;
//                    pointsList.add(latlng);
//                    PolylineOptions lineOpt = new PolylineOptions();
//                    lineOpt.add(prelatLng);
//                    prelatLng = latlng;
//                    lineOpt.add(latlng);
//                    Polyline line = tencentMap.addPolyline(lineOpt);
//                    line.setColor(AddOrderMap.this.getResources().getColor(R.color.black));
//                    line.setWidth(4f);
//                    Overlays.add(line);
                } else
                {
                    tv_tip.setText("请按顺序选择边界上的点");
                    tv_tip.setBackgroundResource(R.color.bg_blue);
                }
                return false;
            }
        });
    }
    private void divide()
    {
        int currentmarker_pos = 0;
        int lastmarker_pos = 0;
        for (int k = 0; k < list_coordinatesbean.size(); k++)
        {
            if ((list_coordinatesbean.get(k).getLat().equals(String.valueOf(latlng_one.getLatitude())) && list_coordinatesbean.get(k).getLng().equals(String.valueOf(latlng_one.getLongitude()))))
            {
                currentmarker_pos =k+1;
            }
            if ((list_coordinatesbean.get(k).getLat().equals(String.valueOf(latlng_two.getLatitude())) && list_coordinatesbean.get(k).getLng().equals(String.valueOf(latlng_two.getLongitude()))))
            {
                lastmarker_pos = k+1;
            }
        }
        int max=0;
        int min=0;
        if (lastmarker_pos>currentmarker_pos)
        {
            max=lastmarker_pos;
            min=currentmarker_pos;
        }else
        {
            max=currentmarker_pos;
            min=lastmarker_pos;
        }
        for (int m = min; m <=max; m++)
        {
            LatLng l=new LatLng(Double.valueOf(list_coordinatesbean.get(m).getLat()),Double.valueOf(list_coordinatesbean.get(m).getLng()));
            list_LatLng_boundaryselect.add(l);
        }
        for (int n =max; n <list_coordinatesbean.size() ; n++)
        {
            LatLng l=new LatLng(Double.valueOf(list_coordinatesbean.get(n).getLat()),Double.valueOf(list_coordinatesbean.get(n).getLng()));
            list_LatLng_boundarynotselect.add(l);
        }
        for (int n =0; n <=min ; n++)
        {
            LatLng l=new LatLng(Double.valueOf(list_coordinatesbean.get(n).getLat()),Double.valueOf(list_coordinatesbean.get(n).getLng()));
            list_LatLng_boundarynotselect.add(l);
        }

        if (list_LatLng_inboundary != null)
        {
            int pos_latlng_one_inboundarynotselect=0;
            int pos_latlng_one_inboundaryselect=0;
            int pos_latlng_two_inboundarynotselect=0;
            int pos_latlng_two_inboundaryselect=0;
            int pos_latlng_one_inpointsList=0;
            int pos_latlng_two_inpointsList=0;
            int pos_latlng_in__one_inpointsList=0;
            int pos_latlng_in__two_inpointsList=0;

            LatLng latlng_in__one=list_LatLng_inboundary.get(0);
            LatLng latlng_in__two=list_LatLng_inboundary.get(list_LatLng_inboundary.size()-1);

            for (int j = 0; j <list_LatLng_boundarynotselect.size() ; j++)//没选择区域
            {
                if (list_LatLng_boundarynotselect.get(j).toString().equals(latlng_one.toString()))
                {
                    pos_latlng_one_inboundarynotselect=j;
                }
                if (list_LatLng_boundarynotselect.get(j).toString().equals(latlng_two.toString()))
                {
                    pos_latlng_two_inboundarynotselect=j;
                }
            }
            for (int j = 0; j <list_LatLng_boundaryselect.size() ; j++)//已选择区域
            {
                if (list_LatLng_boundaryselect.get(j).toString().equals(latlng_one.toString()))
                {
                    pos_latlng_one_inboundaryselect=j;
                }
                if (list_LatLng_boundaryselect.get(j).toString().equals(latlng_two.toString()))
                {
                    pos_latlng_two_inboundaryselect=j;
                }
            }

            for (int j = 0; j <pointsList.size() ; j++)
            {
                if (pointsList.get(j).toString().equals(latlng_in__one.toString()))
                {
                    pos_latlng_in__one_inpointsList=j;
                }
                if (pointsList.get(j).toString().equals(latlng_in__two.toString()))
                {
                    pos_latlng_in__two_inpointsList=j;
                }
                if (pointsList.get(j).toString().equals(latlng_one.toString()))
                {
                    pos_latlng_one_inpointsList=j;
                }
                if (pointsList.get(j).toString().equals(latlng_two.toString()))
                {
                    pos_latlng_two_inpointsList=j;
                }
            }
//                                int pos_latlng_one_inboundarynotselect=0;//latlng_one在没选择边界位置
//                                int pos_latlng_one_inboundaryselect=0;//latlng_one在选择边界位置
//                                int pos_latlng_two_inboundarynotselect=0;//latlng_two在没选择边界位置
//                                int pos_latlng_two_inboundaryselect=0;//latlng_one在选择边界位置
//                                int pos_latlng_one_inpointsList=0;//latlng_one在已选取点钟位置
//                                int pos_latlng_two_inpointsList=0;//latlng_two在已选取点钟位置
//                                int pos_latlng_in__one_inpointsList=0;//第一个内点在已选取点钟位置
//                                int pos_latlng_in__two_inpointsList=0;//第二个内点在已选取点钟位置
            int pos_connectpoint[]={pos_latlng_one_inpointsList,pos_latlng_two_inpointsList,pos_latlng_in__one_inpointsList,pos_latlng_in__two_inpointsList};

            Arrays.sort(pos_connectpoint);
            LatLng LatLng0=pointsList.get(pos_connectpoint[0]);
            LatLng LatLng1=pointsList.get(pos_connectpoint[1]);
            LatLng LatLng2=pointsList.get(pos_connectpoint[2]);
            LatLng LatLng3=pointsList.get(pos_connectpoint[3]);
//将已选取边界点和已选取内点相加
            if (!isend_select && list_LatLng_boundaryselect.get(list_LatLng_boundaryselect.size()-2).toString().equals(LatLng0.toString()))
            {
                for (int k = 0; k <list_LatLng_boundaryselect.size() ; k++)
                {
                    if (LatLng1.toString().equals(list_LatLng_boundaryselect.get(k).toString()))//不加LatLng1
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng2);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }


                    }else
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng1);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }

                    }
                }
            }
            if (!isend_select && list_LatLng_boundaryselect.get(list_LatLng_boundaryselect.size()-2).toString().equals(LatLng1.toString()))
            {
                for (int k = 0; k <list_LatLng_boundaryselect.size() ; k++)//判断是否已有点
                {
                    if (LatLng2.toString().equals(list_LatLng_boundaryselect.get(k).toString()))//不加LatLng2
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng3);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }


                    }else
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng2);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }

                    }
                }
            }
            if (!isend_select && list_LatLng_boundaryselect.get(list_LatLng_boundaryselect.size()-2).toString().equals(LatLng2.toString()))
            {
                for (int k = 0; k <list_LatLng_boundaryselect.size() ; k++)//判断是否已有点
                {
                    if (LatLng3.toString().equals(list_LatLng_boundaryselect.get(k).toString()))//不加LatLng3
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng0);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }


                    }else
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng3);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }

                    }
                }
            }
            if (!isend_select && list_LatLng_boundaryselect.get(list_LatLng_boundaryselect.size()-2).toString().equals(LatLng3.toString()))
            {
                for (int k = 0; k <list_LatLng_boundaryselect.size() ; k++)//判断是否已有点
                {
                    if (LatLng0.toString().equals(list_LatLng_boundaryselect.get(k).toString()))//不加LatLng0
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng1);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }


                    }else
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng0);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select=true;
                            break;
                        }

                    }
                }
            }
            //将非选取点和已选取内点相加
            if (!isend_notselect && list_LatLng_boundarynotselect.get(list_LatLng_boundarynotselect.size()-2).toString().equals(LatLng0.toString()))
            {
                for (int k = 0; k <list_LatLng_boundarynotselect.size() ; k++)//判断是否已有点
                {
                    if (LatLng1.toString().equals(list_LatLng_boundarynotselect.get(k).toString()))//不加LatLng0
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng2);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }


                    }else
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng1);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }

                    }
                }
            }
            if (!isend_notselect && list_LatLng_boundarynotselect.get(list_LatLng_boundarynotselect.size()-2).toString().equals(LatLng1.toString()))
            {
                for (int k = 0; k <list_LatLng_boundarynotselect.size() ; k++)//判断是否已有点
                {
                    if (LatLng2.toString().equals(list_LatLng_boundarynotselect.get(k).toString()))//不加LatLng0
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng3);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }


                    }else
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng2);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }

                    }
                }
            }
            if (!isend_notselect && list_LatLng_boundarynotselect.get(list_LatLng_boundarynotselect.size()-2).toString().equals(LatLng2.toString()))
            {
                for (int k = 0; k <list_LatLng_boundarynotselect.size() ; k++)//判断是否已有点
                {
                    if (LatLng3.toString().equals(list_LatLng_boundarynotselect.get(k).toString()))//不加LatLng0
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng0);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }


                    }else
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng3);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }

                    }
                }
            }
            if (!isend_notselect && list_LatLng_boundarynotselect.get(list_LatLng_boundarynotselect.size()-2).toString().equals(LatLng3.toString()))
            {
                for (int k = 0; k <list_LatLng_boundarynotselect.size() ; k++)//判断是否已有点
                {
                    if (LatLng0.toString().equals(list_LatLng_boundarynotselect.get(k).toString()))//不加LatLng0
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng1);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }


                    }else
                    {
                        int index=list_LatLng_inboundary.indexOf(LatLng0);
                        if (index==0)//
                        {
                            for (int n = 0; n <list_LatLng_inboundary.size() ; n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }else
                        {
                            for (int n =list_LatLng_inboundary.size()-1; n >=0 ; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect=true;
                            break;
                        }

                    }
                }
            }
//                                for (int j = list_LatLng_inboundary.size()-1; j >=0 ; j--)
//                                {
//                                    list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(j));
//                                }
//                                list_LatLng_boundaryselect.addAll(list_LatLng_inboundary);
        }

        Polygon polygon1 = drawPolygon(list_LatLng_boundarynotselect, R.color.bg_blue);
        list_polygon.add(polygon1);

        Polygon polygon2 = drawPolygon(list_LatLng_boundaryselect, R.color.bg_yellow);
        list_polygon.add(polygon2);
//        saveCoordinate(list_LatLng_boundaryselect, "13", "片区二号");
//        saveCoordinate(list_LatLng_boundarynotselect,"-2","未选区域");
        for (int m = 0; m <list_Polyline.size() ; m++)
        {
            list_Polyline.get(m).remove();
        }
        for (int m = 0; m <list_mark.size() ; m++)
        {
            list_mark.get(m).remove();
        }
        for (int m = 0; m <list_mark_inboundary.size() ; m++)
        {
            list_mark_inboundary.get(m).remove();
        }
        showDialog_EditPolygonInfo(list_LatLng_boundaryselect,list_LatLng_boundarynotselect);
        //重置数据
        resetData();
    }
    private void dividePolygon()
    {
        LatLng a=latlng_one;
        LatLng b=latlng_two;
        if (latlng_one==null && latlng_two==null )
        {
            for (int i = 0; i < list_coordinatesbean.size(); i++)
            {
                LatLng latlng = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                list_LatLng_boundaryselect.add(latlng);
            }
            showDialog_EditPolygonInfo(list_LatLng_boundaryselect,list_LatLng_boundarynotselect);
        }else
        {
            divide();
        }
    }
    private void resetData()
    {
        initMapAndParam();
        initMapOnclick();
    }

    private Polygon drawPolygon(List<LatLng> list_LatLng, int color)
    {
        PolygonOptions polygonOp = new PolygonOptions();
        polygonOp.fillColor(getResources().getColor(color));// 填充色
        polygonOp.strokeWidth(0);// 线宽
        for (int i = 0; i < list_LatLng.size(); i++)
        {
            polygonOp.add(list_LatLng.get(i));
        }
        Polygon polygon = mapview.getMap().addPolygon(polygonOp);
        return polygon;
    }

    private void uploadCoordinatesBean(CoordinatesBean coordinatesbean)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "AddCoordinates");
        params.addQueryStringParameter("uuid", coordinatesbean.getUuid());
        params.addQueryStringParameter("type", coordinatesbean.getType());
        params.addQueryStringParameter("uid", coordinatesbean.getUid());
        params.addQueryStringParameter("parkId", coordinatesbean.getparkId());
        params.addQueryStringParameter("parkName", coordinatesbean.getparkName());
        params.addQueryStringParameter("areaId", coordinatesbean.getAreaId());
        params.addQueryStringParameter("areaName", coordinatesbean.getareaName());
        params.addQueryStringParameter("contractid", coordinatesbean.getContractid());
        params.addQueryStringParameter("contractname", coordinatesbean.getContractname());
        params.addQueryStringParameter("batchid", coordinatesbean.getBatchid());
        params.addQueryStringParameter("saleid", coordinatesbean.getSaleid());
        params.addQueryStringParameter("lat", coordinatesbean.getLat());
        params.addQueryStringParameter("lng", coordinatesbean.getLng());
        params.addQueryStringParameter("numofplant", coordinatesbean.getNumofplant());
        params.addQueryStringParameter("weightofplant", coordinatesbean.getWeightofplant());
        params.addQueryStringParameter("registime", coordinatesbean.getRegistime());
        params.addQueryStringParameter("coordinatestime", coordinatesbean.getCoordinatestime());
        params.addQueryStringParameter("orders", "");
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
//                    Toast.makeText(AddOrderMap.this, "添加成功！", Toast.LENGTH_SHORT).show();
                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                String A = "";
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void getBoundary_farm()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", "60");
        params.addQueryStringParameter("action", "Getfarm_boundary");
        params.addQueryStringParameter("type", "farm_boundary");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<CoordinatesBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        JSONArray jsonArray_Rows = result.getRows();
                        String aa = jsonArray_Rows.toString();
                        for (int i = 0; i < jsonArray_Rows.size(); i++)
                        {
                            JSONArray jsonArray_boundary = jsonArray_Rows.getJSONArray(i);
                            listNewData = JSON.parseArray(jsonArray_boundary.toJSONString(), CoordinatesBean.class);
                            setBoundary_park(listNewData);
                        }

                    } else
                    {
                        listNewData = new ArrayList<CoordinatesBean>();
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
                Toast.makeText(getActivity(), "error_connectServer", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initBoundary(List<CoordinatesBean> list_coordinates)
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
        int colors = 0;
        if (list_coordinates.get(0).getAreaId().equals("10"))
        {
            colors=R.color.bg_yellow;
        }else   if (list_coordinates.get(0).getAreaId().equals("13"))
        {
            colors=R.color.bg_ask;
        }else   if (list_coordinates.get(0).getAreaId().equals("14"))
        {
            colors=R.color.bg_blue;
        }else
        {
            colors=R.color.bg_green;
        }
        Polygon polygon = drawPolygon(list_AllLatLng, colors);
//        list_polygon_all.add(polygon);
//        list_polygon_allCoordinatesBean.add(list_coordinates);
        Overlays.add(polygon);
    }
    private void showNeedPlanBoundary(List<CoordinatesBean> list_coordinates)
    {
        //画出区域
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
        Polygon polygon = drawPolygon(list_AllLatLng, R.color.bg_yellow);
        list_polygon_all.add(polygon);
        list_polygon_allCoordinatesBean.add(list_coordinates);
        Overlays.add(polygon);
//显示标志物
        list_CoordinatesBean_currentPolygon= list_coordinates;
        polygon_select=polygon;
        List<LatLng> list_single_polygon=polygon.getPoints();
        list_mark = new ArrayList<>();
        intervalNumber = list_single_polygon.size() / 9;
        int num = intervalNumber;
        for (int j = 0; j < list_single_polygon.size(); j++)
        {
            LatLng ll = new LatLng(Double.valueOf(list_single_polygon.get(j).getLatitude()), Double.valueOf(list_single_polygon.get(j).getLongitude()));
            if (j == 0)
            {
                tencentMap.animateTo(ll);
            }
            if (j== num)
            {
                num = num + intervalNumber;
                Marker marker = addMarker_Paint(list_mark.size(), ll, R.drawable.location_start);
                list_mark.add(marker);
                list_point_pq.add(ll);
            }

        }
    }
    private void setBoundary_park(List<CoordinatesBean> list_coordinates)
    {
        List<LatLng> list_AllLatLng = new ArrayList<>();
//        list_mark = new ArrayList<>();
//        int dd = list_coordinates.size() / 9;
//        int num = dd;
        for (int i = 0; i < list_coordinates.size(); i++)
        {
            LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
            if (i == 0)
            {
                tencentMap.animateTo(latlng);
//                addMarker_Paint(i, latlng, R.drawable.location_start);
            }
            list_AllLatLng.add(latlng);
//            if (i == num)
//            {
//                num = num + dd;
//                Marker marker = addMarker_Paint(list_mark.size(), latlng, R.drawable.location_start);
//                list_mark.add(marker);
//                list_point_pq.add(latlng);
//            }

        }
//        map.put("a", list_mark);
        Polygon polygon = drawPolygon(list_AllLatLng, R.color.bg_yellow);
//        list_polygon_pq.add(polygon);
        list_polygon_all.add(polygon);
        list_polygon_allCoordinatesBean.add(list_coordinates);
        Overlays.add(polygon);

//        resetData();

    }

    public void   saveCoordinate(List<LatLng> list,String id,String name)
    {
        for (int i = 0; i < list.size(); i++)
        {
            String uuid = java.util.UUID.randomUUID().toString();
            CoordinatesBean coordinatesBean = new CoordinatesBean();
            coordinatesBean.setLat(String.valueOf(list.get(i).getLatitude()));
            coordinatesBean.setLng(String.valueOf(list.get(i).getLongitude()));
            coordinatesBean.setNumofplant("10000");
            coordinatesBean.setType("farm_boundary");
            coordinatesBean.setUid("60");
            coordinatesBean.setparkId("15");
            coordinatesBean.setparkName("一号园区");
            coordinatesBean.setUuid(uuid);
            coordinatesBean.setAreaId(id);
            coordinatesBean.setareaName(name);
            coordinatesBean.setContractid("");
            coordinatesBean.setContractname("");
            coordinatesBean.setBatchid("");
            coordinatesBean.setCoordinatestime(utils.getTime());
            coordinatesBean.setRegistime(utils.getTime());
            coordinatesBean.setWeightofplant("400000");
            coordinatesBean.setSaleid("");
            coordinatesBean.setOrders("");
            SqliteDb.save(getActivity(), coordinatesBean);
//                uploadCoordinatesBean(coordinatesBean);
        }
    }
    private void initMapAndParam()
    {
        //重置数据
        list_Polyline = new ArrayList<>();
        list_mark_inboundary = new ArrayList<>();
        pointsList = new ArrayList<>();
        list_polygon = new ArrayList<>();
        list_LatLng_boundarynotselect=new ArrayList<>();
        list_LatLng_boundaryselect=new ArrayList<>();
        list_LatLng_inboundary=new ArrayList<>();
        last_pos = 0;
        number_markerselect = 0;
        number_pointselect = 0;
        firstmarkerselect = true;
        prelatLng = null;
//        polygon_select = null;


        isStart = true;
//        polygon_select = list_polygon_pq.get(0);

        setMarkerListenner();
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {
//                polygon_select = list_polygon_pq.get(0);
                if (!polygon_select.contains(latlng))
                {
                    tv_tip.setText("请在承包区内选取点");
                    tv_tip.setBackgroundResource(R.color.bg_job);
                    return;
                }
                if (number_markerselect> 0)//只要边界上有一个marker被选中,而且区域内也已经选取点了，就不能再点击地图了，只能点击marker
                {
                    if (latlng_one == null)
                    {
                        latlng_one=pointsList.get(pointsList.size()-1);
                    }
                }
                list_LatLng_inboundary.add(latlng);
                number_pointselect = number_pointselect + 1;
                Marker m=addMarker_Paint(0, latlng, R.drawable.location_start);
                list_mark_inboundary.add(m);
                last_pos = 0;
                firstmarkerselect = true;
                pointsList.add(latlng);
                PolylineOptions lineOpt = new PolylineOptions();
                lineOpt.add(prelatLng);
                prelatLng = latlng;
                lineOpt.add(latlng);
                Polyline line = tencentMap.addPolyline(lineOpt);
                line.setColor(getActivity().getResources().getColor(R.color.black));
                line.setWidth(4f);
                list_Polyline.add(line);
                Overlays.add(line);
            }
        });
    }
    public void  initMapOnclick()
    {
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {
                for (int i = 0; i <list_polygon_all.size() ; i++)
                {
                    Polygon polygon= list_polygon_all.get(i);
                    if (polygon.contains(latlng))
                    {
                        list_CoordinatesBean_currentPolygon= list_polygon_allCoordinatesBean.get(i);
                        polygon_select=polygon;
                        List<LatLng> list_single_polygon=polygon.getPoints();
                        list_mark = new ArrayList<>();
                        intervalNumber = list_single_polygon.size() / 9;
                        int num = intervalNumber;
                        for (int j = 0; j < list_single_polygon.size(); j++)
                        {
                            LatLng ll = new LatLng(Double.valueOf(list_single_polygon.get(j).getLatitude()), Double.valueOf(list_single_polygon.get(j).getLongitude()));
                            if (j == 0)
                            {
                                tencentMap.animateTo(ll);
                            }
                            if (j== num)
                            {
                                num = num + intervalNumber;
                                Marker marker = addMarker_Paint(list_mark.size(), ll, R.drawable.location_start);
                                list_mark.add(marker);
                                list_point_pq.add(ll);
                            }

                        }
                        showDialog_OperatePolygon();
                        return;
                    }
                    if (i==list_polygon_all.size()-1)
                    {
                        tv_tip.setText("请在承包区内选取点");
                        tv_tip.setBackgroundResource(R.color.bg_job);
                        return;
                    }
                }
            }
        });
    }
    @Override
    public void onClick(View v)
    {

    }
}
