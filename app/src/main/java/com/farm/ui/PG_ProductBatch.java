package com.farm.ui;

import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.BatchColor_Adapter;
import com.farm.adapter.BatchTime_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchColor;
import com.farm.bean.BatchOfProduct;
import com.farm.bean.BreakOff;
import com.farm.bean.CoordinatesBean;
import com.farm.bean.CusPoint;
import com.farm.bean.PolygonBean;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.CustomDialog;
import com.farm.widget.CustomDialog_AddSaleInInfo;
import com.farm.widget.CustomDialog_BatchColor;
import com.farm.widget.CustomDialog_BatchTime;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_OverlayInfo;
import com.farm.widget.swipelistview.CustomDialog_OperatePolygon;
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
import com.tencent.mapsdk.raster.model.CameraPosition;
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
import com.tencent.tencentmap.mapsdk.map.UiSettings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/4/5.
 */
@EFragment
public class PG_ProductBatch extends Fragment implements TencentLocationListener, View.OnClickListener
{
    List<BatchOfProduct> list_BatchOfProduct;
    boolean isanimateToCenter = false;
    List<CoordinatesBean> list_coordinate_select = new ArrayList<CoordinatesBean>();
    List<CoordinatesBean> list_coordinate_notselect = new ArrayList<CoordinatesBean>();
    BreakOff breakOff_select = new BreakOff();
    BreakOff breakOff_notselect = new BreakOff();
    SellOrderDetail sellOrderDetail_new;
    TextView tv_batchcolor;
    TextView tv_batchtime;
    List<BatchColor> list_BatchColor;
    //    List<BatchTimeBean> list_BatchTimeBean;
    BatchTime_Adapter batchtime_adapter;
    BatchColor_Adapter batchcolor_adapter;
    CustomDialog_BatchTime customDialog_BatchTime;
    CustomDialog_BatchColor customDialog_BatchColor;
    String batchtime;
    CustomDialog customdialog_deletetip;
    CustomDialog_AddSaleInInfo customDialog_addSaleInInfo;
    Polygon polygon_divide1;
    Polygon polygon_divide2;
    List<LatLng> list_latlng_pick = new ArrayList<>();
    boolean isInner = false;
    LatLng touchLatlng1;
    LatLng touchLatlng2;
    int pos_line1 = 0;
    int pos_line2 = 0;
    List<LatLng> list_latlng_firstline;
    List<LatLng> list_latlng_secondline;
    LatLng lastselect_latlng;
    List<List<LatLng>> list_latlng_needplanline;
    List<LatLng> list_latlng_needplanboundary;

    BreakOff polygon_needbreakoff;

    List<LatLng> list_latlng_divide1;
    List<LatLng> list_latlng_divide2;

    boolean isable = false;

    EditText et_note;
    List<Polyline> list_Objects_divideline;

    PopupWindow pw_command;
    PopupWindow pw_batch;
    View pv_command;
    View pv_batch;

    CustomDialog_OverlayInfo customDialog_overlayInfo;
    CustomDialog_OperatePolygon customdialog_operatepolygon;
    CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;

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
    List<Marker> list_Marker_block;//承包区标识对象
    List<Marker> list_Marker_breakoff;

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
    List<Polygon> list_Objects_saleout;//已售区域
    List<Polygon> list_Objects_salein;//待售区域
    List<Polygon> list_Objects_newsale;//待售区域
    List<Polygon> list_Objects_salefor;//售中区域

    List<PolygonBean> list_polygon_road;
    List<PolygonBean> list_polygon_house;
    List<PolygonBean> list_polygon_point;
    List<PolygonBean> list_polygon_line;
    List<PolygonBean> list_polygon_mian;
    List<PolygonBean> list_polygon_park;
    List<PolygonBean> list_polygon_area;
    List<PolygonBean> list_polygon_contract;
    com.farm.bean.commembertab commembertab;
    int error;
    LatLng location_latLng;
    TencentMap tencentMap;//地图
    UiSettings uiSettings;//地图设置
    Projection mProjection;
    List<Object> Overlays;

    @ViewById
    ProgressBar pb_upload;
    @ViewById
    TextView tv_tip;
    @ViewById
    Button btn_showlayer;
    @ViewById
    Button btn_yx;
    @ViewById
    Button btn_canclepaint;
    @ViewById
    LinearLayout ll_sm;
    @ViewById
    MapView mapview;
    @ViewById
    FrameLayout fl_map;
    @ViewById
    CheckBox cb_block;
    @ViewById
    CheckBox cb_park;
    @ViewById
    CheckBox cb_area;
    @ViewById
    CheckBox cb_contract;
    @ViewById
    CheckBox cb_house;
    @ViewById
    CheckBox cb_road;
    @ViewById
    CheckBox cb_point;
    @ViewById
    CheckBox cb_line;
    @ViewById
    CheckBox cb_mian;
    @ViewById
    CheckBox cb_all_gesture;
    @ViewById
    CheckBox cb_compass;
    @ViewById
    CheckBox cb_zoom_widget;
    @ViewById
    CheckBox cb_location_button;
    @ViewById
    CheckBox cb_rotate_gesture;
    @ViewById
    CheckBox cb_scroll_gesture;
    @ViewById
    CheckBox cb_tilt_gesture;
    @ViewById
    CheckBox cb_zoom_gesture;
    @ViewById
    LinearLayout ll_setting;
    @ViewById
    LinearLayout ll_showlayer;
    @ViewById
    Button btn_setting;

    @CheckedChange
    void cb_block()
    {
        if (cb_block.isSelected())
        {
            cb_block.setSelected(false);
            for (int i = 0; i < list_Marker_block.size(); i++)
            {
                list_Marker_block.get(i).setVisible(false);
            }
        } else
        {
            cb_block.setSelected(true);
            for (int i = 0; i < list_Marker_block.size(); i++)
            {
                list_Marker_block.get(i).setVisible(true);
            }
        }

    }
    @CheckedChange
    void cb_park()
    {

        if (cb_park.isSelected())
        {
            cb_park.setSelected(false);
            for (int i = 0; i < list_Marker_park.size(); i++)
            {
                list_Marker_park.get(i).setVisible(false);
            }
        } else
        {
            cb_park.setSelected(true);
            for (int i = 0; i < list_Marker_park.size(); i++)
            {
                list_Marker_park.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_area()
    {
        if (cb_area.isSelected())
        {
            cb_area.setSelected(false);
            for (int i = 0; i < list_Marker_area.size(); i++)
            {
                list_Marker_area.get(i).setVisible(false);
            }
        } else
        {
            cb_area.setSelected(true);
            for (int i = 0; i < list_Marker_area.size(); i++)
            {
                list_Marker_area.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_contract()
    {
        if (cb_contract.isSelected())
        {
            cb_contract.setSelected(false);
            for (int i = 0; i < list_Marker_contract.size(); i++)
            {
                list_Marker_contract.get(i).setVisible(false);
            }
        } else
        {
            cb_contract.setSelected(true);
            for (int i = 0; i < list_Marker_contract.size(); i++)
            {
                list_Marker_contract.get(i).setVisible(true);
            }
        }
    }


    @CheckedChange
    void cb_house()
    {
        if (cb_house.isSelected())
        {
            cb_house.setSelected(false);
            for (int i = 0; i < list_Objects_house.size(); i++)
            {
                list_Objects_house.get(i).setVisible(false);
            }
        } else
        {
            cb_house.setSelected(true);
            for (int i = 0; i < list_Objects_house.size(); i++)
            {
                list_Objects_house.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_road()
    {
        if (cb_road.isSelected())
        {
            cb_road.setSelected(false);
            for (int i = 0; i < list_Objects_road.size(); i++)
            {
                list_Objects_road.get(i).setVisible(false);
                list_Objects_road_centermarker.get(i).setVisible(false);
            }
        } else
        {
            cb_road.setSelected(true);
            for (int i = 0; i < list_Objects_road.size(); i++)
            {
                list_Objects_road.get(i).setVisible(true);
                list_Objects_road_centermarker.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_point()
    {
        if (cb_point.isSelected())
        {
            cb_point.setSelected(false);
            for (int i = 0; i < list_Objects_point.size(); i++)
            {
                list_Objects_point.get(i).setVisible(false);
            }
        } else
        {
            cb_point.setSelected(true);
            for (int i = 0; i < list_Objects_point.size(); i++)
            {
                list_Objects_point.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_line()
    {
        if (cb_line.isSelected())
        {
            cb_line.setSelected(false);
            for (int i = 0; i < list_Objects_line.size(); i++)
            {
                list_Objects_line.get(i).setVisible(false);
                list_Objects_line_centermarker.get(i).setVisible(false);
            }
        } else
        {
            cb_line.setSelected(true);
            for (int i = 0; i < list_Objects_line.size(); i++)
            {
                list_Objects_line.get(i).setVisible(true);
                list_Objects_line_centermarker.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_mian()
    {
        if (cb_mian.isSelected())
        {
            cb_mian.setSelected(false);
            for (int i = 0; i < list_Objects_mian.size(); i++)
            {
                list_Objects_mian.get(i).setVisible(false);
                list_Objects_mian_centermarker.get(i).setVisible(false);
            }
        } else
        {
            cb_mian.setSelected(true);
            for (int i = 0; i < list_Objects_mian.size(); i++)
            {
                list_Objects_mian.get(i).setVisible(true);
                list_Objects_mian_centermarker.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_all_gesture()
    {
        cb_rotate_gesture.setChecked(true);
        cb_scroll_gesture.setChecked(true);
        cb_tilt_gesture.setChecked(true);
        cb_zoom_gesture.setChecked(true);
        cb_location_button.setChecked(true);
        cb_zoom_widget.setChecked(true);
        cb_compass.setChecked(true);
    }

    @CheckedChange
    void cb_compass()
    {
        cb_compass.setChecked(true);

    }

    @CheckedChange
    void cb_zoom_widget()
    {
        cb_zoom_widget.setChecked(true);
    }

    @CheckedChange
    void cb_location_button()
    {
        cb_location_button.setChecked(true);
    }

    @CheckedChange
    void cb_rotate_gesture()
    {
        cb_rotate_gesture.setChecked(true);
    }

    @CheckedChange
    void cb_scroll_gesture()
    {
        cb_scroll_gesture.setChecked(true);
        uiSettings.setScrollGesturesEnabled(true);
    }

    @CheckedChange
    void cb_tilt_gesture()
    {
        cb_tilt_gesture.setChecked(true);
    }

    @CheckedChange
    void cb_zoom_gesture()
    {
        cb_zoom_gesture.setChecked(true);
        uiSettings.setZoomGesturesEnabled(true);
    }


    @Click
    void btn_setting()
    {
        if (ll_setting.isShown())
        {
            ll_setting.setVisibility(View.GONE);
        } else
        {
            ll_setting.setVisibility(View.VISIBLE);
        }

    }


    @Click
    void btn_showlayer()
    {
        if (ll_showlayer.isShown())
        {
            ll_showlayer.setVisibility(View.GONE);
        } else
        {
            ll_showlayer.setVisibility(View.VISIBLE);
        }
    }


    @Click
    void btn_yx()
    {
        if (btn_yx.getText().equals("影像"))
        {
            btn_yx.setText("2D");
            tencentMap.setSatelliteEnabled(true);
        } else
        {
            btn_yx.setText("影像");
            tencentMap.setSatelliteEnabled(false);
        }

    }


    @Click
    void btn_canclepaint()
    {
        reloadMap();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.pg_productbatch, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(getActivity());
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
        return rootView;
    }


    @AfterViews
    void afterOncreate()
    {

//        SqliteDb.getTemp1(getActivity());
//        SqliteDb.startBreakoff(getActivity(), commembertab.getuId());
//        SqliteDb.initPark(getActivity());
//        SqliteDb.initArea(getActivity());
//        SqliteDb.initContract(getActivity());
//        SqliteDb.startBreakoff(getActivity(), commembertab.getuId());
        tencentMap = mapview.getMap();
        tencentMap.setZoom(18);
        uiSettings = mapview.getUiSettings();
        tencentMap.setSatelliteEnabled(true);
        mProjection = mapview.getProjection();
        Overlays = new ArrayList<Object>();

        initParam();//初始化参数
        initBasicData();//初始化基础数据
        initBreakoffData();//初始化断蕾数据
        initMarkerClickListener();
        initMapCameraChangeListener();
        initMapClickListener();
        initMapLongClickListener();

        cb_house.setChecked(true);
        cb_road.setChecked(true);

        cb_point.setChecked(true);
        cb_line.setChecked(true);
        cb_mian.setChecked(true);

        cb_park.setChecked(false);
        cb_area.setChecked(false);
        cb_contract.setChecked(false);

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
        list_Marker_block = new ArrayList<>();
    }


    public void initBreakoffData()
    {
        //将承包区标识隐藏
        for (int a = 0; a < list_Marker_contract.size(); a++)
        {
            list_Marker_contract.get(a).setVisible(false);
        }
        list_Objects_breakoff = new ArrayList<>();
        list_Marker_breakoff = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaid", commembertab.getareaId());
        params.addQueryStringParameter("Year", utils.getYear());
        params.addQueryStringParameter("action", "getBreakOffByAreaId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<BreakOff> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), BreakOff.class);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            BreakOff breakOff = listNewData.get(i);
                            int batchcolor = utils.getBatchColorByName(breakOff.getBatchColor());
                            Polygon p = null;
                            List<CoordinatesBean> list_coor = breakOff.getCoordinatesBeanList();
                            if (breakOff.getStatus().equals("1"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(breakOff.getLat()), Double.valueOf(breakOff.getLng()));
                                Marker marker = addCustomMarker(breakOff, "breakoff", R.drawable.ic_breakoff, getResources().getColor(R.color.white), latlng, breakOff.getUuid(), "断" + breakOff.getnumberofbreakoff());
                                list_Marker_breakoff.add(marker);
                                p = initBoundary(batchcolor, 10f, list_coor, 2, R.color.red);//红色
                            } else
                            {
                                LatLng latlng = new LatLng(Double.valueOf(breakOff.getLat()), Double.valueOf(breakOff.getLng()));
                                Marker marker = addCustomMarker(breakOff, "notbreakoff", R.drawable.ic_breakoff_spare, getResources().getColor(R.color.white), latlng, breakOff.getUuid(), "剩" + breakOff.getnumberofbreakoff());
                                list_Marker_breakoff.add(marker);
                                p = initBoundary(batchcolor, 10f, list_coor, 2, R.color.red);//绿色
                            }
                            list_Objects_breakoff.add(p);
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


    public void initBasicData()
    {
//显示规划图层
//        InitPlanMap();
        //显示区块图层
        InitArea();
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
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PolygonBean.class);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            PolygonBean polygonBean = listNewData.get(i);
                            if (polygonBean.getAreaId().equals("-1"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(polygonBean.getLat()), Double.valueOf(polygonBean.getLng()));
                                Marker marker = addCustomMarker(polygonBean, "normal", R.drawable.ic_flag_park, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getNote());
                                marker.setVisible(false);
                                list_Marker_park.add(marker);
                                List<CoordinatesBean> list_park = polygonBean.getCoordinatesBeanList();
                                if (list_park != null && list_park.size() != 0)
                                {
                                    initBoundary(Color.argb(150, 144, 144, 144), 0f, list_park, 0, R.color.transparent);
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
                                    initBoundary(Color.argb(150, 144, 144, 144), 0f, list_area, 0, R.color.transparent);
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
                                    initBoundary(Color.argb(150, 144, 144, 144), 0f, list_contract, 0, R.color.transparent);
                                }
                                initBoundaryLine(100f,list_contract,6,getResources().getColor(R.color.red));

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
//    public void initLinePolygon()
//    {
//        list_Objects_line = new ArrayList<>();
//        list_Objects_line_centermarker = new ArrayList<>();
//        List<PolygonBean> list_polygon_line = SqliteDb.getMoreLayer_line(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_polygon_line.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_line.get(i).getUuid());
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
//            list_Objects_line.add(line);
//
//            if (list_latlang.size() > 0)
//            {
//                int halfsize = list_latlang.size() / 2;
//                if (halfsize == 0)
//                {
//                    Marker marker = addCustomMarker(list_polygon_line.get(i), "normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(0), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
//                    list_Objects_line_centermarker.add(marker);
//                } else
//                {
//                    Marker marker = addCustomMarker(list_polygon_line.get(i), "normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
//                    list_Objects_line_centermarker.add(marker);
//                }
//
//            }
//
//        }
//    }
//
//    public void initRoadPolygon()
//    {
//        list_Objects_road = new ArrayList<>();
//        list_Objects_road_centermarker = new ArrayList<>();
//        list_polygon_road = SqliteDb.getMoreLayer_road(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_polygon_road.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_road.get(i).getUuid());
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
//            list_Objects_road.add(line);
//
//            if (list_latlang.size() > 0)
//            {
//                int halfsize = list_latlang.size() / 2;
//                Marker marker = addCustomMarker(list_polygon_road.get(i), "normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_road.get(i).getUuid(), list_polygon_road.get(i).getNote());
//                list_Objects_road_centermarker.add(marker);
//            }
//
//        }
//    }
//
//
//    public void initMianPolygon()
//    {
//        list_Objects_mian_centermarker = new ArrayList<>();
//        list_Objects_mian = new ArrayList<>();
//        list_polygon_mian = SqliteDb.getMoreLayer_mian(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_polygon_mian.size(); i++)
//        {
//            LatLng latlng = new LatLng(Double.valueOf(list_polygon_mian.get(i).getLat()), Double.valueOf(list_polygon_mian.get(i).getLng()));
//            Marker marker = addCustomMarker(list_polygon_mian.get(i), "normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_ask), latlng, list_polygon_mian.get(i).getUuid(), list_polygon_mian.get(i).getNote());
//            list_Objects_mian_centermarker.add(marker);
//            List<CoordinatesBean> list_mian = SqliteDb.getPoints(getActivity(), list_polygon_mian.get(i).getUuid());
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
//                list_Objects_mian.add(polygon);
//            }
//
//        }
//    }
//
//    public void initHousePolygon()
//    {
//        list_Objects_house = new ArrayList<>();
//        list_polygon_house = SqliteDb.getMoreLayer_house(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_polygon_house.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_house.get(i).getUuid());
//            if (list != null && list.size() != 0)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
//                Marker marker = addCustomMarker(list_polygon_house.get(i), "normal", R.drawable.icon_house, getResources().getColor(R.color.bg_ask), latlng, list_polygon_house.get(i).getUuid(), list_polygon_house.get(i).getNote());
//                list_Objects_house.add(marker);
//            }
//
//        }
//    }
//
//    public void initPointPolygon()
//    {
//        list_Objects_point = new ArrayList<>();
//        list_polygon_point = SqliteDb.getMoreLayer_point(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_polygon_point.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_point.get(i).getUuid());
//            if (list != null && list.size() != 0)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
//                Marker marker = addCustomMarker(list_polygon_point.get(i), "normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_job), latlng, list_polygon_point.get(i).getUuid(), list_polygon_point.get(i).getNote());
//                list_Objects_point.add(marker);
//            }
//
//        }
//    }

    public void initMapClickListener()
    {
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {

            }
        });
    }

    public void initMapLongClickListener()
    {
        tencentMap.setOnMapLongClickListener(new TencentMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
            }
        });

    }

    private void initMapCameraChangeListener()
    {
        tencentMap.setOnMapCameraChangeListener(new TencentMap.OnMapCameraChangeListener()
        {
            @Override
            public void onCameraChange(CameraPosition cameraPosition)
            {
                int zoomlevel = tencentMap.getZoomLevel();
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition)
            {

            }
        });
    }

    private void initMarkerClickListener()
    {
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                Bundle bundle = (Bundle) marker.getTag();
                String note = bundle.getString("note");
                String uuid = bundle.getString("uuid");
                String type = bundle.getString("type");
                if (type.equals("normal"))
                {
//                    PolygonBean polygonbean = SqliteDb.getLayerbyuuid(getActivity(), uuid);
                    PolygonBean polygonbean = bundle.getParcelable("bean");
                    showDialog_overlayInfo(polygonbean.getNote());
                } else if (type.equals("breakoff"))
                {
                    BreakOff breakoff = bundle.getParcelable("bean");
                    if (breakoff.getareaid().equals(commembertab.getareaId()))
                    {
                        showDialog_OperateBreakoff(breakoff, marker);
                    } else
                    {
                        showDialog_overlayInfo(breakoff.getparkname() + breakoff.getareaname() + breakoff.getcontractname() + "\n" + "已断蕾" + breakoff.getnumberofbreakoff() + "株");
                    }

                } else if (type.equals("notbreakoff"))
                {
                    polygon_needbreakoff = bundle.getParcelable("bean");
                    if (polygon_needbreakoff.getareaid().equals(commembertab.getareaId()))
                    {
                        showDialog_OperateNotBreakoff(polygon_needbreakoff, marker);
                    } else
                    {
                        showDialog_overlayInfo(polygon_needbreakoff.getparkname() + polygon_needbreakoff.getareaname() + polygon_needbreakoff.getcontractname() + "\n" + "剩余" + polygon_needbreakoff.getnumberofbreakoff() + "株未断蕾");
                    }
                }

                return false;
            }
        });
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
        Polygon polygon = drawPolygon(z, list_AllLatLng, fillcolor, strokesize, strokecolor);
        polygon.setZIndex(z);
        Overlays.add(polygon);
        return polygon;
    }

    private void initBoundaryLine(float z, List<CoordinatesBean> list_coordinates, int strokesize, int strokecolor)
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
        line.setColor(strokecolor);
        line.setWidth(strokesize);
        line.setZIndex(z);
        Overlays.add(line);
    }


    private void showNeedPlanBoundary(List<CoordinatesBean> list_coordinates)
    {
        list_latlng_needplanboundary = new ArrayList<>();
        for (int i = 0; i < list_coordinates.size(); i++)
        {
            LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
            if (i == 0)
            {
                tencentMap.animateTo(latlng);
            }
            list_latlng_needplanboundary.add(latlng);
        }
        Polygon polygon = drawPolygon(20f, list_latlng_needplanboundary, Color.argb(1000, 0, 0, 255), 0, R.color.transparent);
        Overlays.add(polygon);
//保存边界线
        list_latlng_needplanline = new ArrayList<>();
        LatLng prelatlng = new LatLng(Double.valueOf(list_coordinates.get(0).getLat()), Double.valueOf(list_coordinates.get(0).getLng()));
        for (int i = 1; i < list_coordinates.size(); i++)//从1开始
        {
            LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
            List<LatLng> list_lintpoint = new ArrayList<>();
            list_lintpoint.add(prelatlng);
            list_lintpoint.add(latlng);
            prelatlng = latlng;
            list_latlng_needplanline.add(list_lintpoint);
        }
        LatLng firstlatlng = new LatLng(Double.valueOf(list_coordinates.get(0).getLat()), Double.valueOf(list_coordinates.get(0).getLng()));
        LatLng lastlatlng = new LatLng(Double.valueOf(list_coordinates.get(list_coordinates.size() - 1).getLat()), Double.valueOf(list_coordinates.get(list_coordinates.size() - 1).getLng()));
        List<LatLng> list_lintpoint = new ArrayList<>();
        list_lintpoint.add(lastlatlng);
        list_lintpoint.add(firstlatlng);
        list_latlng_needplanline.add(list_lintpoint);
    }

    public void initMapLongPressWhenPaint()
    {
        tencentMap.setOnMapLongClickListener(new TencentMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
//                showDialog_SelectWholePolygon(latLng);
            }
        });

    }

    public void initMapClickWhenPaint()
    {
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                PolylineOptions lineOpt = new PolylineOptions();
                lineOpt.add(lastselect_latlng);
                lineOpt.add(latLng);
                Polyline line = tencentMap.addPolyline(lineOpt);
                line.setColor(Color.argb(500, 255, 255, 255));
                line.setWidth(4f);
                Overlays.add(line);
                list_Objects_divideline.add(line);

                if (lastselect_latlng != null)
                {
                    Point p1 = mProjection.toScreenLocation(lastselect_latlng);
                    Point p2 = mProjection.toScreenLocation(latLng);
                    CusPoint cuspoint1 = new CusPoint(p1.x, p1.y);
                    CusPoint cuspoint2 = new CusPoint(p2.x, p2.y);

                    //判断交点数
                    int number_crosspoint = 0;
                    for (int i = 0; i < list_latlng_needplanline.size(); i++)
                    {
                        List<LatLng> list = list_latlng_needplanline.get(i);
                        LatLng latlng0 = list.get(0);
                        LatLng latlng1 = list.get(1);
                        Point p5 = mProjection.toScreenLocation(latlng0);
                        Point p6 = mProjection.toScreenLocation(latlng1);
                        CusPoint cuspoint5 = new CusPoint(p5.x, p5.y);
                        CusPoint cuspoint6 = new CusPoint(p6.x, p6.y);
                        CusPoint crosspoint = utils.getCrossPoint(cuspoint1, cuspoint2, cuspoint5, cuspoint6);
                        if (crosspoint != null)
                        {
                            number_crosspoint = number_crosspoint + 1;
                        }
                    }
                    //仅仅切割一条边界，开始划分区域
                    if (number_crosspoint == 1)
                    {
                        for (int i = 0; i < list_latlng_needplanline.size(); i++)
                        {
                            List<LatLng> list = list_latlng_needplanline.get(i);
                            LatLng latlng0 = list.get(0);
                            LatLng latlng1 = list.get(1);
                            Point p5 = mProjection.toScreenLocation(latlng0);
                            Point p6 = mProjection.toScreenLocation(latlng1);
                            CusPoint cuspoint5 = new CusPoint(p5.x, p5.y);
                            CusPoint cuspoint6 = new CusPoint(p6.x, p6.y);
                            CusPoint crosspoint = utils.getCrossPoint(cuspoint1, cuspoint2, cuspoint5, cuspoint6);
                            if (crosspoint != null)
                            {
                                Point cp = new Point();
                                int x = Integer.valueOf(String.valueOf(crosspoint.x).substring(0, String.valueOf(crosspoint.x).indexOf(".")));
                                int y = Integer.valueOf(String.valueOf(crosspoint.y).substring(0, String.valueOf(crosspoint.y).indexOf(".")));
                                cp.set(x, y);
                                LatLng latlng_crosspoint = mProjection.fromScreenLocation(cp);
                                if (list_latlng_firstline == null)
                                {
                                    list_latlng_firstline = list;
                                    pos_line1 = i;
                                    touchLatlng1 = latlng_crosspoint;
                                    isInner = true;
                                } else
                                {
                                    list_latlng_secondline = list;
                                    pos_line2 = i;
                                    touchLatlng2 = latlng_crosspoint;
                                    isInner = false;
                                    divideArea(pos_line1, pos_line2, list_latlng_firstline, list_latlng_secondline, touchLatlng1, touchLatlng2);
                                }
                                break;
                            }
                        }
                    } else if (number_crosspoint > 1)
                    {
                        Toast.makeText(getActivity(), "错误操作！请不要一次切割多条边界！", Toast.LENGTH_SHORT).show();
                        //撤销该次操作，并重置参数
                        tencentMap.removeOverlay(line);
                        list_Objects_divideline.remove(line);
                        Overlays.remove(line);
                        lastselect_latlng = list_latlng_pick.get(list_latlng_pick.size() - 1);
                        return;
                    } else
                    {
                    }

                }
                lastselect_latlng = latLng;//放在后面
                if (isInner)//放在后面
                {
                    list_latlng_pick.add(latLng);
                }

            }
        });
    }

    private void divideArea(int pos_line1, int pos_line2, List<LatLng> list_line1, List<LatLng> list_line2, LatLng touchLatlng1, LatLng touchLatlng2)
    {
        list_latlng_divide1 = new ArrayList<>();
        list_latlng_divide2 = new ArrayList<>();
        int pos_start = 0;
        int pos_end = 0;
        if (pos_line1 > pos_line2)
        {
            //已选区域
            list_latlng_divide1.add(touchLatlng2);//添加已选起始点
            for (int i = 0; i < list_latlng_needplanboundary.size(); i++)
            {
                if (list_latlng_needplanboundary.get(i).toString().equals(list_line2.get(1).toString()))
                {
                    pos_start = i;
                }
                if (list_latlng_needplanboundary.get(i).toString().equals(list_line1.get(0).toString()))
                {
                    pos_end = i;
                }
            }
            for (int i = pos_start; i <= pos_end; i++)//要等于
            {
                list_latlng_divide1.add(list_latlng_needplanboundary.get(i));
            }
            list_latlng_divide1.add(touchLatlng1);//添加已选终点
            for (int i = 0; i < list_latlng_pick.size(); i++)
            {
                list_latlng_divide1.add(list_latlng_pick.get(i));
            }
            // 未选区域
            list_latlng_divide2.add(touchLatlng1);//添加未选起始点
            for (int i = 0; i < list_latlng_needplanboundary.size(); i++)
            {
                if (list_latlng_needplanboundary.get(i).equals(list_line1.get(1)))
                {
                    pos_start = i;
                }
                if (list_latlng_needplanboundary.get(i).equals(list_line2.get(0)))
                {
                    pos_end = i;
                }
            }
            if (pos_start != 0)
            {
                for (int i = pos_start; i < list_latlng_needplanboundary.size(); i++)
                {
                    list_latlng_divide2.add(list_latlng_needplanboundary.get(i));
                }
            }
//            list_latlng_divide2.add(list_latlng_needplanboundary.get(0));//添加分界点
            for (int i = 0; i <= pos_end; i++)//要等于
            {
                list_latlng_divide2.add(list_latlng_needplanboundary.get(i));
            }
            list_latlng_divide2.add(touchLatlng2);//添加未选终点
            for (int i = list_latlng_pick.size() - 1; i >= 0; i--)
            {
                list_latlng_divide2.add(list_latlng_pick.get(i));
            }
        } else
        {
            list_latlng_divide1.add(touchLatlng1);//添加已选起始点
            for (int i = 0; i < list_latlng_needplanboundary.size(); i++)
            {
                if (list_latlng_needplanboundary.get(i).equals(list_line1.get(1)))
                {
                    pos_start = i;
                }
                if (list_latlng_needplanboundary.get(i).equals(list_line2.get(0)))
                {
                    pos_end = i;
                }
            }
            for (int i = pos_start; i <= pos_end; i++)//要等于
            {
                list_latlng_divide1.add(list_latlng_needplanboundary.get(i));
            }
            list_latlng_divide1.add(touchLatlng2);//添加已选终点
            for (int i = list_latlng_pick.size() - 1; i >= 0; i--)
            {
                list_latlng_divide1.add(list_latlng_pick.get(i));
            }
            // 未选区域
            list_latlng_divide2.add(touchLatlng2);//添加未选起始点
            for (int i = 0; i < list_latlng_needplanboundary.size(); i++)
            {
                if (list_latlng_needplanboundary.get(i).equals(list_line2.get(1)))
                {
                    pos_start = i;
                }
                if (list_latlng_needplanboundary.get(i).equals(list_line1.get(0)))
                {
                    pos_end = i;
                }
            }
            if (pos_start != 0)
            {
                for (int i = pos_start; i < list_latlng_needplanboundary.size(); i++)
                {
                    list_latlng_divide2.add(list_latlng_needplanboundary.get(i));
                }
            }

//            list_latlng_divide2.add(list_latlng_needplanboundary.get(0));//添加分界点
            for (int i = 0; i <= pos_end; i++)//要等于
            {
                list_latlng_divide2.add(list_latlng_needplanboundary.get(i));
            }
            list_latlng_divide2.add(touchLatlng1);//添加未选终点
            for (int i = 0; i < list_latlng_pick.size(); i++)
            {
                list_latlng_divide2.add(list_latlng_pick.get(i));
            }
        }

//        //画出已选区域
        polygon_divide1 = drawPolygon(100f, list_latlng_divide1, Color.argb(1000, 255, 255, 0), 14, R.color.white);
        Overlays.add(polygon_divide1);
        //画出未选区域
        polygon_divide2 = drawPolygon(200f, list_latlng_divide2, Color.argb(150, 0, 0, 255), 8, R.color.black);
        Overlays.add(polygon_divide2);

        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setBackgroundResource(R.color.bg_job);
        tv_tip.setText("请选择需要的区域");

        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                for (int i = 0; i < list_Objects_divideline.size(); i++)
                {
                    tencentMap.removeOverlay(list_Objects_divideline.get(i));
                }
                Point[] pts1 = new Point[list_latlng_divide1.size()];
                Point p1 = mProjection.toScreenLocation(latLng);
                for (int i = 0; i < list_latlng_divide1.size(); i++)
                {
                    pts1[i] = mProjection.toScreenLocation(list_latlng_divide1.get(i));
                }
                int n = utils.CheckInPloy(pts1, pts1.length, p1);
                if (n == -1)
                {
                    initMapClickListener();
                    showDialog_addsaleinfo(latLng, list_latlng_divide1, list_latlng_divide2);
                    return;
                }

                Point[] pts2 = new Point[list_latlng_divide2.size()];
                Point p2 = mProjection.toScreenLocation(latLng);
                for (int i = 0; i < list_latlng_divide2.size(); i++)
                {
                    pts2[i] = mProjection.toScreenLocation(list_latlng_divide2.get(i));
                }
                n = utils.CheckInPloy(pts2, pts2.length, p2);
                if (n == -1)
                {
                    initMapClickListener();
                    showDialog_addsaleinfo(latLng, list_latlng_divide2, list_latlng_divide1);
                    return;
                }
                tv_tip.setText("请在划分的两个区域中选择");
            }
        });


        for (int i = 0; i < list_Objects_divideline.size(); i++)
        {
            tencentMap.removeOverlay(list_Objects_divideline.get(i));
        }
    }

    public void showDialog_addwholesaleinfo(final BreakOff breakoff)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_addbreakoff, null);
        customDialog_addSaleInInfo = new CustomDialog_AddSaleInInfo(getActivity(), R.style.MyDialog, dialog_layout);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        tv_batchtime = (TextView) dialog_layout.findViewById(R.id.tv_batchtime);
        tv_batchcolor = (TextView) dialog_layout.findViewById(R.id.tv_batchcolor);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
//        list_BatchTimeBean = utils.getBatchTime(getActivity(), polygon_needbreakoff.getcontractid(), "2016-04-10", 5);
//        list_BatchColor = SqliteDb.getBatchColor(getActivity(), polygon_needbreakoff.getcontractid());
//        tv_batchtime.setText(list_BatchOfProduct.get(0).getBatchTime());
//        tv_batchcolor.setText(list_BatchColor.get(0).getBatchColor());
        et_note.setText(breakoff.getnumberofbreakoff());
        tv_batchcolor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getBatchColor(tv_batchcolor);
            }
        });
        tv_batchtime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getBatchTime(tv_batchtime);
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_addSaleInInfo.dismiss();
                saveWholePolygonInfo(breakoff);
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_addSaleInInfo.dismiss();
            }
        });
        customDialog_addSaleInInfo.show();
    }

    public void showDialog_addsaleinfo(final LatLng centerlatLng, final List<LatLng> list_latlng_selectpart, final List<LatLng> list_latlng_notselectpart)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_addbreakoff, null);
        customDialog_addSaleInInfo = new CustomDialog_AddSaleInInfo(getActivity(), R.style.MyDialog, dialog_layout);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        tv_batchtime = (TextView) dialog_layout.findViewById(R.id.tv_batchtime);
        tv_batchcolor = (TextView) dialog_layout.findViewById(R.id.tv_batchcolor);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
//        list_BatchTimeBean = utils.getBatchTime(getActivity(), polygon_needbreakoff.getcontractid(), "2016-04-10", 5);
//        list_BatchColor = SqliteDb.getBatchColor(getActivity(), polygon_needbreakoff.getcontractid());
//        if (list_BatchOfProduct.size() == 0)
//        {
//            Toast.makeText(getActivity(), "批次时间获取失败！", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (list_BatchColor.size() == 0)
//        {
//            Toast.makeText(getActivity(), "批次颜色已经用完，请先在网页端添加批次颜色！", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        tv_batchtime.setText(list_BatchOfProduct.get(0).getBatchTime());
//        tv_batchcolor.setText(list_BatchColor.get(0).getBatchColor());
        tv_batchcolor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getBatchColor(tv_batchcolor);
            }
        });
        tv_batchtime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getBatchTime(tv_batchtime);
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_addSaleInInfo.dismiss();
                savedividedPolygonInfo(et_note.getText().toString(), centerlatLng, list_latlng_selectpart, list_latlng_notselectpart);
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_addSaleInInfo.dismiss();
            }
        });
        customDialog_addSaleInInfo.show();
    }

    public void showDialog_batchtime(final List<BatchOfProduct> list_BatchOfProduct, final TextView tv_batchtime)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_batchtime, null);
        customDialog_BatchTime = new CustomDialog_BatchTime(getActivity(), R.style.MyDialog, dialog_layout);
        ListView lv_department = (ListView) dialog_layout.findViewById(R.id.lv_department);
        final Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_BatchTime.dismiss();
            }
        });
        customDialog_BatchTime.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                customDialog_BatchTime.dismiss();
            }
        });
        batchtime_adapter = new BatchTime_Adapter(getActivity(), list_BatchOfProduct);
        lv_department.setAdapter(batchtime_adapter);
        lv_department.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                customDialog_BatchTime.dismiss();
                tv_batchtime.setText(list_BatchOfProduct.get(position).getBatchTime());
//                BatchTimeBean batchtimebean = list_BatchTimeBean.get(position);
//                if (batchtimebean.getIsexist().equals("1"))
//                {
//                    Toast.makeText(getActivity(), "这个批次已存在！请选择其他批次", Toast.LENGTH_SHORT).show();
//                    return;
//                } else
//                {
//                    tv_batchtime.setText(batchtimebean.getBatchtime());
//                }

            }
        });
        customDialog_BatchTime.show();
    }

    public void showDialog_batchcolor(final List<BatchColor> list_BatchColor, final TextView tv_batchtime)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_batchtime, null);
        customDialog_BatchColor = new CustomDialog_BatchColor(getActivity(), R.style.MyDialog, dialog_layout);
        ListView lv_department = (ListView) dialog_layout.findViewById(R.id.lv_department);
        final Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_BatchColor.dismiss();
            }
        });
        customDialog_BatchColor.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                customDialog_BatchColor.dismiss();
            }
        });
        batchcolor_adapter = new BatchColor_Adapter(getActivity(), list_BatchColor);
        lv_department.setAdapter(batchcolor_adapter);
        lv_department.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                customDialog_BatchColor.dismiss();
                tv_batchcolor.setText(list_BatchColor.get(position).getBatchColor());
            }
        });
        customDialog_BatchColor.show();
    }

    public void saveWholePolygonInfo(final BreakOff breakoff)
    {
//        LatLng latLng = new LatLng(Double.valueOf(breakoff.getLat()), Double.valueOf(breakoff.getLng()));
//        breakoff.setStatus("1");
//        breakoff.setBatchTime(tv_batchtime.getText().toString());
//        breakoff.setBatchColor(tv_batchcolor.getText().toString());
//        breakoff.setXxzt("0");
//        breakoff.setYear(utils.getYear());
//        breakOff_select=breakoff;
//        boolean issuccess = SqliteDb.breakoffwhole(getActivity(), breakoff);
        //添加产品销售批次
        SellOrderDetail SellOrderDetail = new SellOrderDetail();
        SellOrderDetail.setid("");
        SellOrderDetail.setUuid(breakoff.getUuid());
        SellOrderDetail.setsaleid("");
        SellOrderDetail.setBatchTime(tv_batchtime.getText().toString());
        SellOrderDetail.setuid(commembertab.getuId());
        SellOrderDetail.setparkid(polygon_needbreakoff.getparkid());
        SellOrderDetail.setparkname(polygon_needbreakoff.getparkname());
        SellOrderDetail.setareaid(polygon_needbreakoff.getareaid());
        SellOrderDetail.setareaname(polygon_needbreakoff.getareaname());
        SellOrderDetail.setcontractid(polygon_needbreakoff.getcontractid());
        SellOrderDetail.setcontractname(polygon_needbreakoff.getcontractname());
        SellOrderDetail.setplanprice("");
        SellOrderDetail.setactualprice("");
        SellOrderDetail.setPlanlat(String.valueOf(breakoff.getLat()));
        SellOrderDetail.setplanlatlngsize("");
        SellOrderDetail.setactuallatlngsize("");
        SellOrderDetail.setplanlng(String.valueOf(breakoff.getLng()));
        SellOrderDetail.setactuallat("");
        SellOrderDetail.setactuallng("");
        SellOrderDetail.setplannumber(breakoff.getnumberofbreakoff());
        SellOrderDetail.setplanweight("");
        SellOrderDetail.setactualnumber("");
        SellOrderDetail.setactualweight("");
        SellOrderDetail.setplannote("");
        SellOrderDetail.setactualnote("");
        SellOrderDetail.setreg(utils.getTime());
        SellOrderDetail.setstatus("");
        SellOrderDetail.setisSoldOut("0");
        SellOrderDetail.setXxzt("0");
        SellOrderDetail.setType("salefor");
        SellOrderDetail.setYear(utils.getYear());
//                SqliteDb.save(getActivity(), SellOrderDetail);
//                if (issuccess)
//                {
//                    Toast.makeText(getActivity(), "已经全部断蕾！", Toast.LENGTH_SHORT).show();
//                    reloadMap();
//                } else
//                {
//                    Toast.makeText(getActivity(), "断蕾失败！", Toast.LENGTH_SHORT).show();
//                }
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrderDetail\": [");
        builder.append(JSON.toJSONString(SellOrderDetail));
        builder.append("]");
        builder.append("} ");
        addWholePlanMap(builder.toString(), breakoff.getUuid(), tv_batchtime.getText().toString(), tv_batchcolor.getText().toString(), utils.getYear());
    }

    public void savedividedPolygonInfo(final String salenumber, final LatLng centerlatlng, final List<LatLng> list_select, final List<LatLng> list_notselect)
    {
        //已选择区域
        final String uuid_breakoff = java.util.UUID.randomUUID().toString();
        BreakOff breakoff = new BreakOff();
        breakoff.setid("");
        breakoff.setUuid(uuid_breakoff);
        breakoff.setuid(commembertab.getuId());
        breakoff.setBreakofftime(utils.getTime());
        breakoff.setparkid(polygon_needbreakoff.getparkid());
        breakoff.setparkname(polygon_needbreakoff.getparkname());
        breakoff.setareaid(polygon_needbreakoff.getareaid());
        breakoff.setareaname(polygon_needbreakoff.getareaname());
        breakoff.setcontractid(polygon_needbreakoff.getcontractid());
        breakoff.setcontractname(polygon_needbreakoff.getcontractname());
        breakoff.setLat(String.valueOf(centerlatlng.getLatitude()));
        breakoff.setLng(String.valueOf(centerlatlng.getLongitude()));
        breakoff.setLatlngsize("");
        breakoff.setnumberofbreakoff(salenumber);
        breakoff.setregdate(utils.getTime());
        breakoff.setWeight("");
        breakoff.setStatus("1");
        breakoff.setBatchTime(tv_batchtime.getText().toString());
        breakoff.setBatchColor(tv_batchcolor.getText().toString());
        breakoff.setXxzt("0");
        breakoff.setYear(utils.getYear());
        breakOff_select = breakoff;
//        SqliteDb.save(getActivity(), breakoff);
        for (int i = 0; i < list_select.size(); i++)
        {
            CoordinatesBean coordinatesBean = new CoordinatesBean();
            coordinatesBean.setLat(String.valueOf(list_select.get(i).getLatitude()));
            coordinatesBean.setLng(String.valueOf(list_select.get(i).getLongitude()));
            coordinatesBean.setUuid(uuid_breakoff);
            coordinatesBean.setRegistime(utils.getTime());
            list_coordinate_select.add(coordinatesBean);
//            SqliteDb.save(getActivity(), coordinatesBean);
        }
//添加产品销售批次
        SellOrderDetail SellOrderDetail = new SellOrderDetail();
        SellOrderDetail.setid("");
        SellOrderDetail.setUuid(uuid_breakoff);
        SellOrderDetail.setsaleid("");
        SellOrderDetail.setBatchTime(tv_batchtime.getText().toString());
        SellOrderDetail.setuid(commembertab.getuId());
        SellOrderDetail.setparkid(polygon_needbreakoff.getparkid());
        SellOrderDetail.setparkname(polygon_needbreakoff.getparkname());
        SellOrderDetail.setareaid(polygon_needbreakoff.getareaid());
        SellOrderDetail.setareaname(polygon_needbreakoff.getareaname());
        SellOrderDetail.setcontractid(polygon_needbreakoff.getcontractid());
        SellOrderDetail.setcontractname(polygon_needbreakoff.getcontractname());
        SellOrderDetail.setplanprice("");
        SellOrderDetail.setactualprice("");
        SellOrderDetail.setPlanlat(String.valueOf(centerlatlng.getLatitude()));
        SellOrderDetail.setplanlatlngsize("");
        SellOrderDetail.setactuallatlngsize("");
        SellOrderDetail.setplanlng(String.valueOf(centerlatlng.getLongitude()));
        SellOrderDetail.setactuallat("");
        SellOrderDetail.setactuallng("");
        SellOrderDetail.setplannumber(salenumber);
        SellOrderDetail.setplanweight("");
        SellOrderDetail.setactualnumber("");
        SellOrderDetail.setactualweight("");
        SellOrderDetail.setplannote("");
        SellOrderDetail.setactualnote("");
        SellOrderDetail.setreg(utils.getTime());
        SellOrderDetail.setstatus("");
        SellOrderDetail.setisSoldOut("0");
        SellOrderDetail.setXxzt("0");
        SellOrderDetail.setType("salefor");
        SellOrderDetail.setYear(utils.getYear());
        sellOrderDetail_new = SellOrderDetail;
//        SqliteDb.save(getActivity(), SellOrderDetail);
        //设置剩余部分的中心点位置
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("请为剩余部分选择一个中心点");
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                //未选择区域
                String uuid_notbreakoff = java.util.UUID.randomUUID().toString();
                BreakOff breakoff = new BreakOff();
                breakoff.setid("");
                breakoff.setUuid(uuid_notbreakoff);
                breakoff.setuid(commembertab.getuId());
                breakoff.setBreakofftime(utils.getTime());
                breakoff.setparkid(polygon_needbreakoff.getparkid());
                breakoff.setparkname(polygon_needbreakoff.getparkname());
                breakoff.setareaid(polygon_needbreakoff.getareaid());
                breakoff.setareaname(polygon_needbreakoff.getareaname());
                breakoff.setcontractid(polygon_needbreakoff.getcontractid());
                breakoff.setcontractname(polygon_needbreakoff.getcontractname());
                breakoff.setLat(String.valueOf(latLng.getLatitude()));
                breakoff.setLng(String.valueOf(latLng.getLongitude()));
                breakoff.setLatlngsize("");
                breakoff.setnumberofbreakoff(String.valueOf(Integer.valueOf(polygon_needbreakoff.getnumberofbreakoff()) - Integer.valueOf(salenumber)));
                breakoff.setregdate(utils.getTime());
                breakoff.setWeight("");
                breakoff.setStatus("0");
                breakoff.setBatchTime(tv_batchtime.getText().toString());
                breakoff.setBatchColor("绿色");
                breakoff.setXxzt("0");
                breakoff.setYear(utils.getYear());
                breakOff_notselect = breakoff;
//                SqliteDb.save(getActivity(), breakoff);
                for (int i = 0; i < list_notselect.size(); i++)
                {
                    CoordinatesBean coordinatesBean = new CoordinatesBean();
                    coordinatesBean.setLat(String.valueOf(list_notselect.get(i).getLatitude()));
                    coordinatesBean.setLng(String.valueOf(list_notselect.get(i).getLongitude()));
                    coordinatesBean.setUuid(uuid_notbreakoff);
                    coordinatesBean.setRegistime(utils.getTime());
                    list_coordinate_notselect.add(coordinatesBean);
//                    SqliteDb.save(getActivity(), coordinatesBean);
                }
//                boolean issuccess = SqliteDb.deleteBreakoffInfo(getActivity(), polygon_needbreakoff.getUuid());
//                if (issuccess)
//                {
//                    Toast.makeText(getActivity(), "区域选择成功！", Toast.LENGTH_SHORT).show();
//                } else
//                {
//                    Toast.makeText(getActivity(), "区域选择失败！", Toast.LENGTH_SHORT).show();
//                }

                List<CoordinatesBean> CoordinatesBeanLists = new ArrayList<>();
                List<BreakOff> BreakOffList = new ArrayList<>();
                BreakOffList.add(breakOff_select);
                BreakOffList.add(breakOff_notselect);
                CoordinatesBeanLists.addAll(list_coordinate_select);
                CoordinatesBeanLists.addAll(list_coordinate_notselect);

                StringBuilder builder = new StringBuilder();
                builder.append("{\"BreakOffList\": ");
                builder.append(JSON.toJSONString(BreakOffList));
                builder.append(", \"CoordinatesBeanLists\": ");
                builder.append(JSON.toJSONString(CoordinatesBeanLists));
                builder.append(",\"SellOrderDetail\": [");
                builder.append(JSON.toJSONString(sellOrderDetail_new));
                builder.append("]");
                builder.append("} ");
                addPlanMap(builder.toString());
            }
        });
    }

    private void addWholePlanMap(String data, String uuid, String batchtime, String batchcolor, String year)
    {
        pb_upload.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        params.addQueryStringParameter("uuid", polygon_needbreakoff.getUuid());
        params.addQueryStringParameter("batchTime", batchtime);
        params.addQueryStringParameter("batchColor", batchcolor);
        params.addQueryStringParameter("year", year);
        params.addQueryStringParameter("action", "addWholeBreakOffLayer");
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
                        pb_upload.setVisibility(View.GONE);
                        reloadMap();
                        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    pb_upload.setVisibility(View.GONE);
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    Toast.makeText(getActivity(), "保存失败，请重试！", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                pb_upload.setVisibility(View.GONE);
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void addPlanMap(String data)
    {
        pb_upload.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        params.addQueryStringParameter("uuid", polygon_needbreakoff.getUuid());
        params.addQueryStringParameter("action", "addBreakOffLayer");
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
                        pb_upload.setVisibility(View.GONE);
                        reloadMap();
                        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    pb_upload.setVisibility(View.GONE);
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    Toast.makeText(getActivity(), "保存失败，请重试！", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                pb_upload.setVisibility(View.GONE);
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void reloadMap()
    {
        tv_tip.setVisibility(View.GONE);
        tencentMap.clearAllOverlays();
        btn_showlayer.setVisibility(View.VISIBLE);
        btn_setting.setVisibility(View.VISIBLE);
        tencentMap.removeOverlay(Overlays);
        Overlays = new ArrayList<Object>();

        initParamAfterPaint();//初始化参数
        initParam();//初始化参数
        initBasicData();//初始化基础数据
        initBreakoffData();//初始化断蕾数据
        initMarkerClickListener();
        initMapCameraChangeListener();
        initMapClickListener();
        initMapLongClickListener();

    }

    public void initParamAfterPaint()
    {
        btn_canclepaint.setVisibility(View.GONE);

        isInner = false;
        pos_line1 = 0;
        pos_line2 = 0;
        list_Objects_divideline = new ArrayList<>();
        list_latlng_needplanboundary = new ArrayList<>();
        list_latlng_needplanline = new ArrayList<>();
        touchLatlng1 = null;
        touchLatlng2 = null;

        list_latlng_pick = new ArrayList<>();
        lastselect_latlng = null;
        list_latlng_firstline = null;
        list_latlng_secondline = null;
    }

    public void showDialog_deletetip_breakoff(final BreakOff breakOff, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_deletetip, null);
        customdialog_deletetip = new CustomDialog(getActivity(), R.style.MyDialog, dialog_layout);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_deletetip.dismiss();
                deletePolygon(breakOff);
//                BreakOff breakoff = SqliteDb.getBreakoffbyuuid(getActivity(), uuid);
//                breakoff.setStatus("0");
//                breakoff.setBatchColor("绿色");
//                breakoff.setBatchTime("");
//                breakoff.setYear(utils.getYear());
//                boolean issuccess1 = SqliteDb.deleteBreakoffInfo(getActivity(), breakoff);
//                boolean issuccess2 = SqliteDb.deleteSellOrderDetailByUuid(getActivity(), uuid);
//                if (issuccess1 && issuccess2)
//                {
//                    Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
//                    reloadMap();
//                } else
//                {
//                    Toast.makeText(getActivity(), "删除失败！", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_deletetip.dismiss();
            }
        });
        customdialog_deletetip.show();
    }

    public void showDialog_deletetip_salein(final String uuid, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_deletetip, null);
        customdialog_deletetip = new CustomDialog(getActivity(), R.style.MyDialog, dialog_layout);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_deletetip.dismiss();
                SellOrderDetail sellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(getActivity(), uuid);
                sellOrderDetail.setsaleid("");
                sellOrderDetail.setType("salefor_boundary");
                boolean issuccess = SqliteDb.deleteSaleInInfo(getActivity(), sellOrderDetail);
                if (issuccess)
                {
                    Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                    reloadMap();
                } else
                {
                    Toast.makeText(getActivity(), "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_deletetip.dismiss();
            }
        });
        customdialog_deletetip.show();
    }

    public void showDialog_OperateBreakoff(final BreakOff breakOff, final Marker marker)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_operatepolygon, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(getActivity(), R.style.MyDialog, dialog_layout);
        Button btn_paint = (Button) dialog_layout.findViewById(R.id.btn_paint);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_change = (Button) dialog_layout.findViewById(R.id.btn_change);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        Button btn_delete = (Button) dialog_layout.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                showDialog_deletetip_breakoff(breakOff, marker);
//                boolean isexist = SqliteDb.isExistSellOrderDetail(getActivity(), uuid);
//                if (isexist)
//                {
//                    showDialog_deletetip_breakoff(uuid, marker);
//                } else
//                {
//                    Toast.makeText(getActivity(), "该批次已经在出售，不能删除！", Toast.LENGTH_SHORT).show();
//                }

            }
        });
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_overlayInfo(breakOff.getparkname() + breakOff.getareaname() + breakOff.getcontractname() + "\n" + "已断蕾" + breakOff.getnumberofbreakoff() + "株");
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                showDialog_editBreakoffinfo(breakOff, marker);
//                boolean isexist = SqliteDb.isExistSellOrderDetail(getActivity(), uuid);
//                if (isexist)
//                {
//                    BreakOff breakoff = SqliteDb.getbreakoffByuuid(getActivity(), uuid);
//                    showDialog_editBreakoffinfo(breakoff, marker);
//                } else
//                {
//                    Toast.makeText(getActivity(), "该批次已经在出售，不能修改了！", Toast.LENGTH_SHORT).show();
//                }

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

    public void showDialog_OperateNotBreakoff(final BreakOff breakoff, final Marker marker)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_operatepolygon_notbreakoff, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(getActivity(), R.style.MyDialog, dialog_layout);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        Button btn_breakoff = (Button) dialog_layout.findViewById(R.id.btn_breakoff);
        Button btn_allbreakoff = (Button) dialog_layout.findViewById(R.id.btn_allbreakoff);

        btn_breakoff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                list_Objects_divideline = new ArrayList<Polyline>();
                tencentMap.setZoom(18);
                initMapClickWhenPaint();
                tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(Marker marker)
                    {
                        return false;
                    }
                });
                btn_canclepaint.setVisibility(View.VISIBLE);
                List<CoordinatesBean> list_coordinatesbean = breakoff.getCoordinatesBeanList();
                if (list_coordinatesbean != null && list_coordinatesbean.size() != 0)
                {
                    showNeedPlanBoundary(list_coordinatesbean);
                }

            }
        });
        btn_allbreakoff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                showDialog_addwholesaleinfo(breakoff);
//                breakoff.setStatus("1");
//                boolean issuccess = SqliteDb.breakoffwhole(getActivity(), breakoff);
//                //添加产品销售批次
//                SellOrderDetail SellOrderDetail = new SellOrderDetail();
//                SellOrderDetail.setid("");
//                SellOrderDetail.setUuid(breakoff.getUuid());
//                SellOrderDetail.setsaleid("");
//                SellOrderDetail.setBatchTime(tv_batchtime.getText().toString());
//                SellOrderDetail.setuid(commembertab.getuId());
//                SellOrderDetail.setparkid(polygon_needbreakoff.getparkid());
//                SellOrderDetail.setparkname(polygon_needbreakoff.getparkname());
//                SellOrderDetail.setareaid(polygon_needbreakoff.getareaid());
//                SellOrderDetail.setareaname(polygon_needbreakoff.getareaname());
//                SellOrderDetail.setcontractid(polygon_needbreakoff.getcontractid());
//                SellOrderDetail.setcontractname(polygon_needbreakoff.getcontractname());
//                SellOrderDetail.setplanprice("");
//                SellOrderDetail.setactualprice("");
//                SellOrderDetail.setPlanlat(String.valueOf(breakoff.getLat()));
//                SellOrderDetail.setplanlatlngsize("");
//                SellOrderDetail.setactuallatlngsize("");
//                SellOrderDetail.setplanlng(String.valueOf(breakoff.getLng()));
//                SellOrderDetail.setactuallat("");
//                SellOrderDetail.setactuallng("");
//                SellOrderDetail.setplannumber(breakoff.getnumberofbreakoff());
//                SellOrderDetail.setplanweight("");
//                SellOrderDetail.setactualnumber("");
//                SellOrderDetail.setactualweight("");
//                SellOrderDetail.setplannote("");
//                SellOrderDetail.setactualnote("");
//                SellOrderDetail.setreg(utils.getTime());
//                SellOrderDetail.setstatus("");
//                SellOrderDetail.setisSoldOut("0");
//                SellOrderDetail.setXxzt("0");
//                SellOrderDetail.setType("salefor");
//                SqliteDb.save(getActivity(), SellOrderDetail);
//                if (issuccess)
//                {
//                    Toast.makeText(getActivity(), "已经全部断蕾！", Toast.LENGTH_SHORT).show();
//                    reloadMap();
//                } else
//                {
//                    Toast.makeText(getActivity(), "断蕾失败！", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_overlayInfo(breakoff.getparkname() + breakoff.getareaname() + breakoff.getcontractname() + "\n" + "剩余" + breakoff.getnumberofbreakoff() + "株未断蕾");
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


    public void showDialog_overlayInfo(final String note)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_showpolygonifo, null);
        customDialog_overlayInfo = new CustomDialog_OverlayInfo(getActivity(), R.style.MyDialog, dialog_layout);
        TextView tv_note = (TextView) dialog_layout.findViewById(R.id.tv_note);
        Button btn_close = (Button) dialog_layout.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_overlayInfo.dismiss();
            }
        });
        if (note != null && !note.equals(""))
        {
            tv_note.setText(note);
        }
        customDialog_overlayInfo.show();
    }

    public void showDialog_editBreakoffinfo(final BreakOff breakoff, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_editsaleininfo, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(getActivity(), R.style.MyDialog, dialog_layout);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        et_note.setText(breakoff.getnumberofbreakoff());
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
                int number_difference = Integer.valueOf(breakoff.getnumberofbreakoff()) - Integer.valueOf(et_note.getText().toString());
//                SellOrderDetail sellorderdetail = SqliteDb.getSellOrderDetailbyuuid(getActivity(), breakoff.getUuid());
//                sellorderdetail.setplannumber(et_note.getText().toString());
//                breakoff.setnumberofbreakoff(et_note.getText().toString());
//                boolean issuccess1 = SqliteDb.editSellOrderDetail(getActivity(), sellorderdetail);
//                boolean issuccess2 = SqliteDb.editBreakoff(getActivity(), breakoff, number_difference);
//                if (issuccess1 && issuccess2)
//                {
//                    Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
//                    reloadMap();
//                } else
//                {
//                    Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
//                }
                editPolygon(breakoff, et_note.getText().toString(), String.valueOf(number_difference));
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
            }
        });
        customDialog_editSaleInInfo.show();
    }

    private void deletePolygon(final BreakOff breakOff)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", breakOff.getUuid());
        params.addQueryStringParameter("action", "deleteBreakOff");
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
                    String rows = result.getRows().get(0).toString();
                    if (rows.equals("1"))
                    {
                        Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                        reloadMap();
                    } else if (rows.equals("0"))
                    {
                        Toast.makeText(getActivity(), "该批次已经在出售，不能删除！", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    Toast.makeText(getActivity(), "删除失败！", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void editPolygon(final BreakOff breakOff, String number_new, String number_difference)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("uuid", breakOff.getUuid());
        params.addQueryStringParameter("contractid", breakOff.getcontractid());
        params.addQueryStringParameter("batchTime", breakOff.getBatchTime());
        params.addQueryStringParameter("year", breakOff.getYear());
        params.addQueryStringParameter("number_new", number_new);
        params.addQueryStringParameter("number_difference", number_difference);
        params.addQueryStringParameter("action", "editBreakOff");
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
                    String rows = result.getRows().get(0).toString();
                    if (rows.equals("1"))
                    {
                        Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
                        reloadMap();
                    } else if (rows.equals("0"))
                    {
                        Toast.makeText(getActivity(), "该批次已经在出售，不能修改了！", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    public void showDialog_editsaleininfo(final SellOrderDetail sellOrderDetail, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_editsaleininfo, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(getActivity(), R.style.MyDialog, dialog_layout);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        et_note.setText(sellOrderDetail.getplannumber());
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
                sellOrderDetail.setplannumber(et_note.getText().toString());
                sellOrderDetail.setstatus("0");
                sellOrderDetail.setisSoldOut("0");
                boolean issuccess = SqliteDb.editSellOrderDetail_salein(getActivity(), sellOrderDetail, 0);
                if (issuccess)
                {
                    Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
                    reloadMap();
                } else
                {
                    Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
            }
        });
        customDialog_editSaleInInfo.show();
    }


    private Polygon drawPolygon(float z, List<LatLng> list_LatLng, int fillcolor, int strokesize, int strokecolor)
    {
        PolygonOptions polygonOp = new PolygonOptions();
        polygonOp.fillColor(fillcolor);// 填充色
        polygonOp.strokeColor(getResources().getColor(strokecolor));// 线颜色
        polygonOp.strokeWidth(strokesize);// 线宽
        polygonOp.addAll(list_LatLng);
        Polygon polygon = mapview.getMap().addPolygon(polygonOp);
        polygon.setZIndex(z);
        return polygon;
    }

    private Marker addChartView(String qy, int number_sakein, int number_saleout, int number_forsale, LatLng latLng, String uuid, String note)
    {
        Marker marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        View view = View.inflate(getActivity(), R.layout.addview_saleinfo, null);
        LinearLayout ll_first = (LinearLayout) view.findViewById(R.id.ll_first);
        TextView tv_ssqy = (TextView) view.findViewById(R.id.tv_ssqy);
        TextView tv_forsale = (TextView) view.findViewById(R.id.tv_forsale);
        TextView tv_salein = (TextView) view.findViewById(R.id.tv_salein);
        TextView tv_saleout = (TextView) view.findViewById(R.id.tv_saleout);
        tv_ssqy.setText(qy);
        tv_forsale.setText(String.valueOf(number_forsale));
        tv_salein.setText(String.valueOf(number_sakein));
        tv_saleout.setText(String.valueOf(number_saleout));
        tv_ssqy.setHeight(40);
        tv_forsale.setHeight(10);
        tv_salein.setHeight(20);
        tv_salein.setHeight(30);
        marker.setMarkerView(view);
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid);
        bundle.putString("note", note);
        bundle.putString("type", "chart");
        marker.setTag(bundle);
        return marker;
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
        bundle.putParcelable("bean", obj);
        marker.setTag(bundle);
        return marker;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mapview.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s)
    {
//        if (TencentLocation.ERROR_OK == error) // 定位成功
//        {
//            // 用于定位
//            location_latLng = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
//            //全局记录坐标
//            AppContext appContext = (AppContext) getActivity().getApplication();
//            appContext.setLOCATION_X(String.valueOf(location_latLng.getLatitude()));
//            appContext.setLOCATION_Y(String.valueOf(location_latLng.getLongitude()));
//        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1)
    {

    }

    private void getBatchTime(final TextView tv_batchtime)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getProductBatchList");
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
                        list_BatchOfProduct = JSON.parseArray(result.getRows().toJSONString(), BatchOfProduct.class);
                        showDialog_batchtime(list_BatchOfProduct, tv_batchtime);
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

    private void getBatchColor(final TextView tv_batchcolor)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("contractid", polygon_needbreakoff.getcontractid());
        params.addQueryStringParameter("action", "getBatchColor");
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
                        list_BatchColor = JSON.parseArray(result.getRows().toJSONString(), BatchColor.class);
                        showDialog_batchcolor(list_BatchColor, tv_batchcolor);
                    } else
                    {
                        Toast.makeText(getActivity(), "查询不到批次颜色", Toast.LENGTH_SHORT).show();
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
}
