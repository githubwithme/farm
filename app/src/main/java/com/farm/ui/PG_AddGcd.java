package com.farm.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.OrderList_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.CoordinatesBean;
import com.farm.bean.CusPoint;
import com.farm.bean.PlantGcd;
import com.farm.bean.PolygonBean;
import com.farm.bean.Result;
import com.farm.bean.SellOrder;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;
import com.farm.bean.parktab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.CustomDialog;
import com.farm.widget.CustomDialog_AddSaleInInfo;
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
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.pg_addgcd)
public class PG_AddGcd extends Activity implements TencentLocationListener, View.OnClickListener
{
    boolean isanimateToCenter = false;
    PopupWindow pw_orderdetail;
    View pv_orderdetail;
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

    SellOrderDetail polygon_needsale;

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

    List<PolygonBean> list_polygon_road;
    List<PolygonBean> list_polygon_house;
    List<PolygonBean> list_polygon_point;
    List<PolygonBean> list_polygon_line;
    List<PolygonBean> list_polygon_mian;
    List<PolygonBean> list_polygon_park;
    List<PolygonBean> list_polygon_area;
    List<PolygonBean> list_polygon_contract;
    List<SellOrder> list_SellOrder;
    com.farm.bean.commembertab commembertab;
    int error;
    LatLng location_latLng;
    SellOrder CurrentsellOrder;//当前选择的订单
    TencentMap tencentMap;//地图
    UiSettings uiSettings;//地图设置
    Projection mProjection;
    List<Object> Overlays;

    @ViewById
    TextView tv_tip;
    @ViewById
    TextView tv_batchcolor;
    @ViewById
    FrameLayout fl_salelist;
    @ViewById
    RelativeLayout rl_batchofproduct;
    @ViewById
    Button btn_showlayer;
    @ViewById
    Button btn_yx;
    @ViewById
    LinearLayout ll_sm;
    @ViewById
    MapView mapview;
    @ViewById
    Button btn_batchofproduct;
    @ViewById
    ImageView iv_arrow;
    @ViewById
    View line_batch;
    @ViewById
    FrameLayout fl_map;
    @ViewById
    CheckBox cb_park;
    @ViewById
    CheckBox cb_area;
    @ViewById
    CheckBox cb_contract;
    @ViewById
    CheckBox cb_parkdata;
    @ViewById
    CheckBox cb_areadata;
    @ViewById
    CheckBox cb_contractdata;
    @ViewById
    CheckBox cb_saledata;
    @ViewById
    CheckBox cb_breakoffdata;
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
    @ViewById
    Button btn_canclepaint;

    @Click
    void btn_canclepaint()
    {
        reloadMap();
    }

    @Click
    void btn_editorder()
    {
        if (CurrentsellOrder != null)
        {
            showPop_orderdetail();
        } else
        {
            Toast.makeText(PG_AddGcd.this, "暂无订单", Toast.LENGTH_SHORT).show();
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
    void cb_saledata()
    {
        if (cb_saledata.isSelected())
        {
            cb_saledata.setSelected(false);
            for (int i = 0; i < list_Marker_allsale.size(); i++)
            {
                list_Marker_allsale.get(i).setVisible(false);
            }
        } else
        {
            cb_saledata.setSelected(true);
            for (int i = 0; i < list_Marker_allsale.size(); i++)
            {
                list_Marker_allsale.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_breakoffdata()
    {
        if (cb_breakoffdata.isSelected())
        {
            cb_breakoffdata.setSelected(false);
            for (int i = 0; i < list_Marker_breakoff.size(); i++)
            {
                list_Marker_breakoff.get(i).setVisible(false);
                list_Objects_breakoff.get(i).setVisible(false);
            }
        } else
        {
            cb_breakoffdata.setSelected(true);
            for (int i = 0; i < list_Marker_breakoff.size(); i++)
            {
                list_Marker_breakoff.get(i).setVisible(true);
                list_Objects_breakoff.get(i).setVisible(true);
            }
        }
    }

    @CheckedChange
    void cb_parkdata()
    {
        if (cb_parkdata.isSelected())
        {
            cb_parkdata.setSelected(false);
            for (int i = 0; i < list_Marker_ParkChart.size(); i++)
            {
                list_Marker_ParkChart.get(i).setVisible(false);
            }
        } else
        {
            cb_parkdata.setSelected(true);
            showFirstMarker();
        }

    }

    @CheckedChange
    void cb_areadata()
    {
        if (cb_areadata.isSelected())
        {
            cb_areadata.setSelected(false);
            for (int i = 0; i < list_Marker_AreaChart.size(); i++)
            {
                list_Marker_AreaChart.get(i).setVisible(false);
            }
        } else
        {
            cb_areadata.setSelected(true);
            showSecondMarker();
        }
    }

    @CheckedChange
    void cb_contractdata()
    {
        if (cb_contractdata.isSelected())
        {
            cb_contractdata.setSelected(false);
            for (int i = 0; i < list_Marker_ContractChart.size(); i++)
            {
                list_Marker_ContractChart.get(i).setVisible(false);
            }
        } else
        {
            cb_contractdata.setSelected(true);
            showThirdMarker();
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
    void btn_batchofproduct()
    {
        if (list_SellOrder.size() > 0)
        {
            showPop_batch();
        } else
        {
            Toast.makeText(PG_AddGcd.this, "暂无产品批次", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        commembertab = AppContext.getUserInfo(PG_AddGcd.this);
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(PG_AddGcd.this);
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
    }


    @AfterViews
    void afterOncreate()
    {
        tencentMap = mapview.getMap();
        tencentMap.setZoom(18);
        uiSettings = mapview.getUiSettings();
        tencentMap.setSatelliteEnabled(true);
        mProjection = mapview.getProjection();
        Overlays = new ArrayList<Object>();


        initParam();//初始化参数
        initBasicData();//初始化基础数据
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

        cb_parkdata.setChecked(false);
        cb_areadata.setChecked(false);
        cb_contractdata.setChecked(false);

        cb_saledata.setChecked(false);
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


//    public void initBreakoffData()
//    {
//        list_Objects_breakoff = new ArrayList<>();
//        list_Marker_breakoff = new ArrayList<>();
//        List<parktab> list_parktab = SqliteDb.getparktab(PG_AddGcd.this, commembertab.getuId());
//        for (int i = 0; i < list_parktab.size(); i++)//每个园区
//        {
//            List<BreakOff> list_BreakOff = SqliteDb.getBreakOffInfoByParkId(PG_AddGcd.this, list_parktab.get(i).getid(), CurrentsellOrder.getBatchTime(), CurrentsellOrder.getYear());
//            if (list_BreakOff != null && list_BreakOff.size() > 0)
//            {
//                for (int j = 0; j < list_BreakOff.size(); j++)
//                {
//                    BreakOff breakOff = list_BreakOff.get(j);
//                    if (breakOff.getStatus().equals("1"))//该批次已断蕾情况
//                    {
//                        LatLng latlng = new LatLng(Double.valueOf(breakOff.getLat()), Double.valueOf(breakOff.getLng()));
//                        Marker marker = addCustomMarker("breakoff", R.drawable.ic_breakoff, getResources().getColor(R.color.white), latlng, breakOff.getUuid(), "批次" + breakOff.getBatchTime() + "共断蕾" + breakOff.getnumberofbreakoff() + "株");
//                        marker.setVisible(false);
//                        list_Marker_breakoff.add(marker);
//                        Polygon p = null;
//                        List<CoordinatesBean> list_CoordinatesBean = SqliteDb.getPoints(PG_AddGcd.this, breakOff.getUuid());
//                        int batchcolor = utils.getBatchColorByName(breakOff.getBatchColor());
//                        p = initBoundary(batchcolor, 10f, list_CoordinatesBean, 0, R.color.transparent);//注意10f，数值越大越在顶层
//                        list_Objects_breakoff.add(p);
//                        tv_batchcolor.setBackgroundColor(getResources().getColor(utils.returnBatchColorByName(breakOff.getBatchColor())));
//                    } else//该批次未断蕾情况
//                    {
//                    }
//                }
//            }
//        }
//    }
//
//    public void initSaleData()
//    {
//        list_Marker_allsale = new ArrayList<>();
//        list_Marker_salein = new ArrayList<>();
//        list_Marker_saleout = new ArrayList<>();
//        list_Objects_allsale = new ArrayList<>();
//        list_Objects_salein = new ArrayList<>();
//        list_Objects_saleout = new ArrayList<>();
//
//        List<SellOrderDetail> list_SellOrderDetail = SqliteDb.getSaleLayerBySaleId(PG_AddGcd.this, CurrentsellOrder.getUuid());
//        if (list_SellOrderDetail != null)
//        {
//            for (int j = 0; j < list_SellOrderDetail.size(); j++)
//            {
//                if (list_SellOrderDetail.get(j).getType().equals("salein"))
//                {
//                    SellOrderDetail sellorderdetail = list_SellOrderDetail.get(j);
//                    Polygon p = null;
//                    LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
//                    Marker marker = addCustomMarker("salein", R.drawable.ic_salein, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "售中" + sellorderdetail.getplannumber());
////                    marker.setVisible(false);
//                    list_Marker_allsale.add(marker);
//                    list_Marker_salein.add(marker);
//                    List<CoordinatesBean> list_contract = SqliteDb.getPoints(PG_AddGcd.this, sellorderdetail.getUuid());
//                    p = initBoundary(Color.argb(1000, 0, 255, 0), 20f, list_contract, 2, R.color.bg_text);//绿色
//                    list_Objects_allsale.add(p);
//                    list_Objects_salein.add(p);
//                } else if (list_SellOrderDetail.get(j).getType().equals("saleout"))
//                {
//                    SellOrderDetail sellorderdetail = list_SellOrderDetail.get(j);
//                    Polygon p = null;
//                    LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
//                    Marker marker = addCustomMarker("saleout", R.drawable.ic_salein, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "已售" + sellorderdetail.getactualnumber());
////                    marker.setVisible(false);
//                    list_Marker_allsale.add(marker);
//                    list_Marker_saleout.add(marker);
//                    List<CoordinatesBean> list_contract = SqliteDb.getPoints(PG_AddGcd.this, sellorderdetail.getUuid());
//                    p = initBoundary(Color.argb(1000, 255, 0, 0), 10f, list_contract, 2, R.color.bg_text);//绿色
//                    list_Objects_allsale.add(p);
//                    list_Objects_saleout.add(p);
//                }
//
//            }
//        }
//    }

    //    public void initBasicData()
//    {
//        //初始化规划图
//        list_Marker_park = new ArrayList<>();
//        list_Marker_area = new ArrayList<>();
//        list_Marker_contract = new ArrayList<>();
//        List<parktab> list_parktab = SqliteDb.getparktab(PG_AddGcd.this, commembertab.getuId());
//        for (int i = 0; i < list_parktab.size(); i++)//每个园区
//        {
//            PolygonBean polygonBean_park = SqliteDb.getLayer_park(PG_AddGcd.this, list_parktab.get(i).getid());
//            if (polygonBean_park != null)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(polygonBean_park.getLat()), Double.valueOf(polygonBean_park.getLng()));
//                Marker marker = addCustomMarker("normal", R.drawable.ic_flag_park, getResources().getColor(R.color.white), latlng, polygonBean_park.getUuid(), polygonBean_park.getNote());
//                marker.setVisible(false);
//                list_Marker_park.add(marker);
//                List<CoordinatesBean> list_park = SqliteDb.getPoints(PG_AddGcd.this, polygonBean_park.getUuid());
//                if (list_park != null && list_park.size() != 0)
//                {
//                    initBoundary(getResources().getColor(R.color.transparent), 0f, list_park, 0, R.color.transparent);
//                }
//            }
//
//            List<areatab> list_areatab = SqliteDb.getareatab(PG_AddGcd.this, list_parktab.get(i).getid());
//            for (int k = 0; k < list_areatab.size(); k++)//每个片区
//            {
//                PolygonBean polygonBean_area = SqliteDb.getLayer_area(PG_AddGcd.this, list_parktab.get(i).getid(), list_areatab.get(k).getid());
//                if (polygonBean_area != null)
//                {
//                    LatLng latlng = new LatLng(Double.valueOf(polygonBean_area.getLat()), Double.valueOf(polygonBean_area.getLng()));
//                    Marker marker = addCustomMarker("normal", R.drawable.ic_flag_area, getResources().getColor(R.color.white), latlng, polygonBean_area.getUuid(), polygonBean_area.getNote());
//                    marker.setVisible(false);
//                    list_Marker_area.add(marker);
//                    List<CoordinatesBean> list_area = SqliteDb.getPoints(PG_AddGcd.this, polygonBean_area.getUuid());
//                    if (list_area != null && list_area.size() != 0)
//                    {
//                        initBoundary(getResources().getColor(R.color.transparent), 0f, list_area, 2, R.color.bg_text);
//                    }
//                }
//
//                List<contractTab> list_contractTab = SqliteDb.getcontracttab(PG_AddGcd.this, list_areatab.get(k).getid());
//                for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
//                {
//                    //承包区规划图
//                    PolygonBean polygonBean_contract = SqliteDb.getLayer_contract(PG_AddGcd.this, list_parktab.get(i).getid(), list_areatab.get(k).getid(), list_contractTab.get(m).getid());
//                    if (polygonBean_contract != null)
//                    {
//                        LatLng latlng = new LatLng(Double.valueOf(polygonBean_contract.getLat()), Double.valueOf(polygonBean_contract.getLng()));
//                        Marker marker = addCustomMarker("normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, polygonBean_contract.getUuid(), polygonBean_contract.getNote());
//                        marker.setVisible(false);
//                        list_Marker_contract.add(marker);
//                        List<CoordinatesBean> list_contract = SqliteDb.getPoints(PG_AddGcd.this, polygonBean_contract.getUuid());
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
        InitPlanMap();
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
                                    initBoundary(Color.argb(150, 144, 144, 144), 0f, list_park, 2, R.color.bg_green);
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
                    AppContext.makeToast(PG_AddGcd.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_AddGcd.this, "error_connectServer");
            }
        });
    }

    public void initGCDPolygon()
    {
        list_Objects_gcd = new ArrayList<>();
        getGCDList();
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
//    public void initLinePolygon()
//    {
//        list_Objects_line = new ArrayList<>();
//        list_Objects_line_centermarker = new ArrayList<>();
//        List<PolygonBean> list_polygon_line = SqliteDb.getMoreLayer_line(PG_AddGcd.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_line.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(PG_AddGcd.this, list_polygon_line.get(i).getUuid());
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
//                    Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(0), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
//                    list_Objects_line_centermarker.add(marker);
//                } else
//                {
//                    Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
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
//        list_polygon_road = SqliteDb.getMoreLayer_road(PG_AddGcd.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_road.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(PG_AddGcd.this, list_polygon_road.get(i).getUuid());
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
//                Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_road.get(i).getUuid(), list_polygon_road.get(i).getNote());
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
//        list_polygon_mian = SqliteDb.getMoreLayer_mian(PG_AddGcd.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_mian.size(); i++)
//        {
//            LatLng latlng = new LatLng(Double.valueOf(list_polygon_mian.get(i).getLat()), Double.valueOf(list_polygon_mian.get(i).getLng()));
//            Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_ask), latlng, list_polygon_mian.get(i).getUuid(), list_polygon_mian.get(i).getNote());
//            list_Objects_mian_centermarker.add(marker);
//            List<CoordinatesBean> list_mian = SqliteDb.getPoints(PG_AddGcd.this, list_polygon_mian.get(i).getUuid());
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
//        list_polygon_house = SqliteDb.getMoreLayer_house(PG_AddGcd.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_house.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(PG_AddGcd.this, list_polygon_house.get(i).getUuid());
//            if (list != null && list.size() != 0)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
//                Marker marker = addCustomMarker("normal", R.drawable.icon_house, getResources().getColor(R.color.bg_ask), latlng, list_polygon_house.get(i).getUuid(), list_polygon_house.get(i).getNote());
//                list_Objects_house.add(marker);
//            }
//
//        }
//    }
//
//    public void initPointPolygon()
//    {
//        list_Objects_point = new ArrayList<>();
//        list_polygon_point = SqliteDb.getMoreLayer_point(PG_AddGcd.this, commembertab.getuId());
//        for (int i = 0; i < list_polygon_point.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(PG_AddGcd.this, list_polygon_point.get(i).getUuid());
//            if (list != null && list.size() != 0)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
//                Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_job), latlng, list_polygon_point.get(i).getUuid(), list_polygon_point.get(i).getNote());
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
                if (zoomlevel >= 13 && cb_parkdata.isSelected())
                {
//                    ll_sm.setVisibility(View.VISIBLE);
                } else
                {
//                    ll_sm.setVisibility(View.GONE);
                }
                //规划图层信息显示控制
//                if (cb_parkdata.isSelected())
//                {
//                    if (zoomlevel <= 13)
//                    {
//                        showFirstMarker();
//                    } else
//                    {
//                        for (int i = 0; i < list_Marker_ParkChart.size(); i++)
//                        {
//                            list_Marker_ParkChart.get(i).setVisible(false);
//                        }
//                    }
//                }


                //片区图层信息显示控制
//                if (cb_areadata.isSelected())
//                {
//                    if (zoomlevel == 14)
//                    {
//                        showSecondMarker();
//                    } else
//                    {
//                        for (int i = 0; i < list_Marker_AreaChart.size(); i++)
//                        {
//                            list_Marker_AreaChart.get(i).setVisible(false);
//                        }
//                    }
//                }


                //其他图层信息显示控制
//                if (cb_contractdata.isSelected())
//                {
//                    if (zoomlevel >= 15)
//                    {
//                        showThirdMarker();
//                    } else
//                    {
//                        for (int i = 0; i < list_Marker_ContractChart.size(); i++)
//                        {
//                            list_Marker_ContractChart.get(i).setVisible(false);
//                        }
//                    }
//                }


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
                Parcelable obj = bundle.getParcelable("bean");
                if (type.equals("normal"))
                {
                    PolygonBean polygonbean = (PolygonBean) obj;
                    showDialog_overlayInfo(polygonbean.getNote());
                } else if (type.equals("breakoff"))
                {
                    showDialog_overlayInfo(note);
                } else if (type.equals("salein"))
                {
                    SellOrderDetail sellOrderDetail = (SellOrderDetail) obj;
                    showDialog_OperateSalein(sellOrderDetail, marker);
                } else if (type.equals("saleout"))
                {
                    SellOrderDetail sellOrderDetail = (SellOrderDetail) obj;
                    showDialog_OperateSaleOut(sellOrderDetail, marker);
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

    public void showDialog_OperateSalefor(final String uuid, final Marker marker)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.customdialog_operatepolygon_salefor, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(PG_AddGcd.this, R.style.MyDialog, dialog_layout);
        Button btn_salewhole = (Button) dialog_layout.findViewById(R.id.btn_salewhole);
        Button btn_sale = (Button) dialog_layout.findViewById(R.id.btn_sale);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SellOrderDetail SellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(PG_AddGcd.this, uuid);
                showDialog_overlayInfo(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname() + "\n" + "待出售" + SellOrderDetail.getplannumber() + "株");
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_sale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                list_Objects_divideline = new ArrayList<Polyline>();
                tencentMap.setZoom(14);
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
                polygon_needsale = SqliteDb.getSellOrderDetailbyuuid(PG_AddGcd.this, uuid);
                List<CoordinatesBean> list_coordinatesbean = SqliteDb.getPoints(PG_AddGcd.this, polygon_needsale.getUuid());
                if (list_coordinatesbean != null && list_coordinatesbean.size() != 0)
                {
                    showNeedPlanBoundary(list_coordinatesbean);
                }

            }
        });
        btn_salewhole.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                SellOrderDetail sellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(PG_AddGcd.this, uuid);
                sellOrderDetail.setsaleid("");
                sellOrderDetail.setType("newsale");
                boolean issuccess = SqliteDb.salewhole(PG_AddGcd.this, sellOrderDetail);
                if (issuccess)
                {
                    Toast.makeText(PG_AddGcd.this, "已添加到出售清单！", Toast.LENGTH_SHORT).show();
                    reloadMap();
                } else
                {
                    Toast.makeText(PG_AddGcd.this, "出售失败！", Toast.LENGTH_SHORT).show();
                }
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


    private void editOrder(String uuid, String actualprice, String actualweight, String actualnumber, String actualsumvalues, String feedbacknote)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("actualprice", actualprice);
        params.addQueryStringParameter("actualweight", actualweight);
        params.addQueryStringParameter("actualnumber", actualnumber);
        params.addQueryStringParameter("actualsumvalues", actualsumvalues);
        params.addQueryStringParameter("feedbacknote", feedbacknote);
        params.addQueryStringParameter("action", "editSellOrder");
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
                        Toast.makeText(PG_AddGcd.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        reloadMap();
                    } else if (rows.equals("0"))
                    {
                        Toast.makeText(PG_AddGcd.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    AppContext.makeToast(PG_AddGcd.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_AddGcd.this, "error_connectServer");
            }
        });
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
        Polygon polygon = drawPolygon(10f, list_latlng_needplanboundary, R.color.black, 4, R.color.red);
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
                        Toast.makeText(PG_AddGcd.this, "错误操作！请不要一次切割多条边界！", Toast.LENGTH_SHORT).show();
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

        //画出已选区域
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

    private void getPlanMap_Custom(String uid, final String type)
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(PG_AddGcd.this);
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
                    AppContext.makeToast(PG_AddGcd.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_AddGcd.this, "error_connectServer");
            }
        });
    }

    public void showDialog_addsaleinfo(final LatLng centerlatLng, final List<LatLng> list_latlng_selectpart, final List<LatLng> list_latlng_notselectpart)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.customdialog_addsaleifo, null);
        customDialog_addSaleInInfo = new CustomDialog_AddSaleInInfo(PG_AddGcd.this, R.style.MyDialog, dialog_layout);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
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

    public void savedividedPolygonInfo(final String salenumber, final LatLng centerlatlng, final List<LatLng> list_select, final List<LatLng> list_notselect)
    {
        //已选择区域
        final String uuid_sale = java.util.UUID.randomUUID().toString();
        SellOrderDetail SellOrderDetail = new SellOrderDetail();
        SellOrderDetail.setid("");
        SellOrderDetail.setUuid(uuid_sale);
        SellOrderDetail.setsaleid("");
        SellOrderDetail.setBatchTime("");
        SellOrderDetail.setuid(commembertab.getuId());
        SellOrderDetail.setparkid(polygon_needsale.getparkid());
        SellOrderDetail.setparkname(polygon_needsale.getparkname());
        SellOrderDetail.setareaid(polygon_needsale.getareaid());
        SellOrderDetail.setareaname(polygon_needsale.getareaname());
        SellOrderDetail.setcontractid(polygon_needsale.getcontractid());
        SellOrderDetail.setcontractname(polygon_needsale.getcontractname());
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
        SellOrderDetail.setType("newsale");
        SqliteDb.save(PG_AddGcd.this, SellOrderDetail);
        for (int i = 0; i < list_select.size(); i++)
        {
            CoordinatesBean coordinatesBean = new CoordinatesBean();
            coordinatesBean.setLat(String.valueOf(list_select.get(i).getLatitude()));
            coordinatesBean.setLng(String.valueOf(list_select.get(i).getLongitude()));
            coordinatesBean.setUuid(uuid_sale);
            coordinatesBean.setRegistime(utils.getTime());
            SqliteDb.save(PG_AddGcd.this, coordinatesBean);
        }
//        addCustomMarker("newsale", R.drawable.umeng_socialize_follow_on, R.color.white, centerlatlng, uuid_sale, et_note.getText().toString());
//        initBoundary()

        //设置剩余部分的中心点位置
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("请为剩余部分选择一个中心点");
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                //未选择区域
                String uuid_notsale = java.util.UUID.randomUUID().toString();
                SellOrderDetail SellOrderDetail_notsale = new SellOrderDetail();
                SellOrderDetail_notsale.setid("");
                SellOrderDetail_notsale.setUuid(uuid_notsale);
                SellOrderDetail_notsale.setsaleid("");
                SellOrderDetail_notsale.setBatchTime("");
                SellOrderDetail_notsale.setuid(commembertab.getuId());
                SellOrderDetail_notsale.setparkid(polygon_needsale.getparkid());
                SellOrderDetail_notsale.setparkname(polygon_needsale.getparkname());
                SellOrderDetail_notsale.setareaid(polygon_needsale.getareaid());
                SellOrderDetail_notsale.setareaname(polygon_needsale.getareaname());
                SellOrderDetail_notsale.setcontractid(polygon_needsale.getcontractid());
                SellOrderDetail_notsale.setcontractname(polygon_needsale.getcontractname());
                SellOrderDetail_notsale.setplanprice("");
                SellOrderDetail_notsale.setactualprice("");
                SellOrderDetail_notsale.setPlanlat(String.valueOf(latLng.getLatitude()));
                SellOrderDetail_notsale.setplanlatlngsize("");
                SellOrderDetail_notsale.setactuallatlngsize("");
                SellOrderDetail_notsale.setplanlng(String.valueOf(latLng.getLongitude()));
                SellOrderDetail_notsale.setactuallat("");
                SellOrderDetail_notsale.setactuallng("");
                SellOrderDetail_notsale.setplannumber(String.valueOf(Integer.valueOf(polygon_needsale.getplannumber()) - Integer.valueOf(salenumber)));
                SellOrderDetail_notsale.setplanweight("");
                SellOrderDetail_notsale.setactualnumber("");
                SellOrderDetail_notsale.setactualweight("");
                SellOrderDetail_notsale.setplannote("");
                SellOrderDetail_notsale.setactualnote("");
                SellOrderDetail_notsale.setreg(utils.getTime());
                SellOrderDetail_notsale.setstatus("");
                SellOrderDetail_notsale.setisSoldOut("0");
                SellOrderDetail_notsale.setXxzt("0");
                SellOrderDetail_notsale.setType("salefor");
                SqliteDb.save(PG_AddGcd.this, SellOrderDetail_notsale);
                for (int i = 0; i < list_notselect.size(); i++)
                {
                    CoordinatesBean coordinatesBean = new CoordinatesBean();
                    coordinatesBean.setLat(String.valueOf(list_notselect.get(i).getLatitude()));
                    coordinatesBean.setLng(String.valueOf(list_notselect.get(i).getLongitude()));
                    coordinatesBean.setUuid(uuid_notsale);
                    coordinatesBean.setRegistime(utils.getTime());
                    SqliteDb.save(PG_AddGcd.this, coordinatesBean);
                }
//                addCustomMarker("salefor", R.drawable.umeng_socialize_follow_on, R.color.white, centerlatlng, uuid_notsale, String.valueOf(Integer.valueOf(polygon_needsale.getplannumber()) - Integer.valueOf(salenumber)));
                boolean issuccess = SqliteDb.deleteSaleForInfo(PG_AddGcd.this, polygon_needsale.getUuid());
                if (issuccess)
                {
                    Toast.makeText(PG_AddGcd.this, "区域选择成功！", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(PG_AddGcd.this, "区域选择失败！", Toast.LENGTH_SHORT).show();
                }

                lastselect_latlng = null;
                reloadMap();
            }
        });

    }

    public void showPop_orderdetail()
    {
        LayoutInflater layoutInflater = (LayoutInflater) PG_AddGcd.this.getSystemService(PG_AddGcd.this.LAYOUT_INFLATER_SERVICE);
        pv_orderdetail = layoutInflater.inflate(R.layout.pop_editorderdetail, null);// 外层
        pv_orderdetail.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_orderdetail.isShowing()))
                {
                    pw_orderdetail.dismiss();
                    WindowManager.LayoutParams lp = PG_AddGcd.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    PG_AddGcd.this.getWindow().setAttributes(lp);
                    return true;
                }
                return false;
            }
        });
        pv_orderdetail.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_orderdetail.isShowing())
                {
                    pw_orderdetail.dismiss();
                    WindowManager.LayoutParams lp = PG_AddGcd.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    PG_AddGcd.this.getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_orderdetail = new PopupWindow(pv_orderdetail, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_orderdetail.showAsDropDown(line_batch, 0, 0);
        pw_orderdetail.setOutsideTouchable(false);
        TextView tv_name = (TextView) pv_orderdetail.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) pv_orderdetail.findViewById(R.id.tv_phone);
        TextView tv_address = (TextView) pv_orderdetail.findViewById(R.id.tv_address);
        TextView tv_email = (TextView) pv_orderdetail.findViewById(R.id.tv_email);
        TextView tv_planprice = (TextView) pv_orderdetail.findViewById(R.id.tv_planprice);
        final EditText et_actualprice = (EditText) pv_orderdetail.findViewById(R.id.et_actualprice);
        TextView tv_plannumber = (TextView) pv_orderdetail.findViewById(R.id.tv_plannumber);
        final EditText et_actualnumber = (EditText) pv_orderdetail.findViewById(R.id.et_actualnumber);
        TextView tv_planweight = (TextView) pv_orderdetail.findViewById(R.id.tv_planweight);
        final EditText et_actualweight = (EditText) pv_orderdetail.findViewById(R.id.et_actualweight);
        TextView tv_planallvalues = (TextView) pv_orderdetail.findViewById(R.id.tv_planallvalues);
        final EditText et_actualallvalues = (EditText) pv_orderdetail.findViewById(R.id.et_actualallvalues);
        TextView tv_deposit = (TextView) pv_orderdetail.findViewById(R.id.tv_deposit);
        TextView tv_note = (TextView) pv_orderdetail.findViewById(R.id.tv_note);
        final TextView et_feedbacknote = (TextView) pv_orderdetail.findViewById(R.id.et_feedbacknote);
        Button btn_sure = (Button) pv_orderdetail.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) pv_orderdetail.findViewById(R.id.btn_cancle);

        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_orderdetail.dismiss();
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_orderdetail.dismiss();
                editOrder(CurrentsellOrder.getUuid(), CurrentsellOrder.getActualprice(), CurrentsellOrder.getActualweight(), CurrentsellOrder.getActualnumber(), CurrentsellOrder.getActualsumvalues(), CurrentsellOrder.getFeedbacknote());
//                CurrentsellOrder.setActualprice(et_actualprice.getText().toString());
//                CurrentsellOrder.setActualnumber(et_actualnumber.getText().toString());
//                CurrentsellOrder.setActualweight(et_actualweight.getText().toString());
//                CurrentsellOrder.setActualsumvalues(et_actualallvalues.getText().toString());
//                CurrentsellOrder.setFeedbacknote(et_feedbacknote.getText().toString());
//                boolean issuccess = SqliteDb.save(PG_AddGcd.this, CurrentsellOrder);
//                if (issuccess)
//                {
//                    Toast.makeText(PG_AddGcd.this, "修改成功！", Toast.LENGTH_SHORT).show();
//                } else
//                {
//                    Toast.makeText(PG_AddGcd.this, "修改失败！", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        tv_name.setText(CurrentsellOrder.getBuyers());
        tv_phone.setText(CurrentsellOrder.getPhone());
        tv_address.setText(CurrentsellOrder.getAddress());
        tv_email.setText(CurrentsellOrder.getEmail());
        tv_planprice.setText(CurrentsellOrder.getPrice());
        et_actualprice.setText(CurrentsellOrder.getActualprice());
        tv_planweight.setText(CurrentsellOrder.getWeight());
        et_actualweight.setText(CurrentsellOrder.getActualweight());
        tv_plannumber.setText(CurrentsellOrder.getNumber());
        et_actualnumber.setText(CurrentsellOrder.getActualnumber());
        tv_planallvalues.setText(CurrentsellOrder.getSumvalues());
        et_actualallvalues.setText(CurrentsellOrder.getActualsumvalues());
        tv_deposit.setText(CurrentsellOrder.getDeposit());
        tv_note.setText(CurrentsellOrder.getNote());
        et_feedbacknote.setText(CurrentsellOrder.getFeedbacknote());
    }

    private void reloadMap()
    {
        tv_tip.setVisibility(View.GONE);
        tencentMap.clearAllOverlays();
        btn_showlayer.setVisibility(View.VISIBLE);
        btn_setting.setVisibility(View.VISIBLE);
        Overlays = new ArrayList<Object>();

        initParamAfterPaint();//初始化参数
        initParam();//初始化参数
        initBasicData();//初始化基础数据
        initMarkerClickListener();
        initMapCameraChangeListener();
        initMapClickListener();
        initMapLongClickListener();

//        showFirstMarker();
//        showSecondMarker();
//        showThirdMarker();


        cb_house.setChecked(true);
        cb_road.setChecked(true);

        cb_point.setChecked(true);
        cb_line.setChecked(true);
        cb_mian.setChecked(true);

        cb_park.setChecked(false);
        cb_area.setChecked(false);
        cb_contract.setChecked(false);

        cb_parkdata.setChecked(false);
        cb_areadata.setChecked(false);
        cb_contractdata.setChecked(false);

        cb_saledata.setChecked(false);
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

    public void showDialog_deletetip_shoppingcart()
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.customdialog_deletetip, null);
        customdialog_deletetip = new CustomDialog(PG_AddGcd.this, R.style.MyDialog, dialog_layout);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_deletetip.dismiss();
                boolean issuccess = SqliteDb.deleteShoppingcartByBatchtime(PG_AddGcd.this, commembertab.getuId(), "");
                if (issuccess)
                {
                    Toast.makeText(PG_AddGcd.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    reloadMap();
                } else
                {
                    Toast.makeText(PG_AddGcd.this, "删除失败！", Toast.LENGTH_SHORT).show();
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


    public void showDialog_OperateSalein(final SellOrderDetail sellOrderDetail, final Marker marker)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.customdialog_feedbackofsale, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(PG_AddGcd.this, R.style.MyDialog, dialog_layout);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_change = (Button) dialog_layout.findViewById(R.id.btn_change);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);


        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_overlayInfo(sellOrderDetail.getparkname() + sellOrderDetail.getareaname() + sellOrderDetail.getcontractname() + "\n" + sellOrderDetail.getplannumber() + "株" + "出售中...");
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_editsaleininfo(sellOrderDetail, marker);
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

    public void showDialog_OperateSaleOut(final SellOrderDetail sellOrderDetail, final Marker marker)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.customdialog_feedbackofsale, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(PG_AddGcd.this, R.style.MyDialog, dialog_layout);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_change = (Button) dialog_layout.findViewById(R.id.btn_change);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);


        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_overlayInfo(sellOrderDetail.getparkname() + sellOrderDetail.getareaname() + sellOrderDetail.getcontractname() + "\n" + sellOrderDetail.getplannumber() + "株" + "出售中...");
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_editsaleininfo(sellOrderDetail, marker);
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
        final View dialog_layout = (LinearLayout) LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.customdialog_showpolygonifo, null);
        customDialog_overlayInfo = new CustomDialog_OverlayInfo(PG_AddGcd.this, R.style.MyDialog, dialog_layout);
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

    public void showDialog_editsaleininfo(final SellOrderDetail sellOrderDetail, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.customdialog_editsaleininfo, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(PG_AddGcd.this, R.style.MyDialog, dialog_layout);
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
                int number_difference = Integer.valueOf(sellOrderDetail.getplannumber()) - Integer.valueOf(et_note.getText().toString());
                sellOrderDetail.setactualnumber(et_note.getText().toString());
                sellOrderDetail.setType("saleout");
                boolean issuccess = SqliteDb.editSellOrderDetail_feedbacksale(PG_AddGcd.this, sellOrderDetail, number_difference);
                if (issuccess)
                {
                    Toast.makeText(PG_AddGcd.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    reloadMap();
                } else
                {
                    Toast.makeText(PG_AddGcd.this, "修改失败！", Toast.LENGTH_SHORT).show();
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

    private void showThirdMarker()
    {
        if (list_Marker_ContractChart.size() > 0)
        {
            for (int i = 0; i < list_Marker_ContractChart.size(); i++)
            {
                list_Marker_ContractChart.get(i).setVisible(true);
            }
        } else
        {
            List<parktab> list_parktab = SqliteDb.getparktab(PG_AddGcd.this, commembertab.getuId());
            for (int i = 0; i < list_parktab.size(); i++)//每个园区
            {
                List<areatab> list_areatab = SqliteDb.getareatab(PG_AddGcd.this, list_parktab.get(i).getid());
                for (int k = 0; k < list_areatab.size(); k++)//每个片区
                {
                    List<contractTab> list_contractTab = SqliteDb.getcontracttab(PG_AddGcd.this, list_areatab.get(k).getid());
                    for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
                    {
                        PolygonBean polygonBean_contract = SqliteDb.getLayer_contract(PG_AddGcd.this, list_parktab.get(i).getid(), list_areatab.get(k).getid(), list_contractTab.get(m).getid());

                        if (polygonBean_contract != null)
                        {
                            int[] count_saleout = SqliteDb.getdataofcontractsaleByContractId(PG_AddGcd.this, commembertab.getuId(), polygonBean_contract.getparkId(), polygonBean_contract.getAreaId(), polygonBean_contract.getContractid());
                            LatLng latlng = new LatLng(Double.valueOf(polygonBean_contract.getLat()), Double.valueOf(polygonBean_contract.getLng()));
                            Marker marker = addChartView(polygonBean_contract.getparkName() + polygonBean_contract.getareaName() + polygonBean_contract.getContractname(), count_saleout[0], count_saleout[1], count_saleout[2], latlng, polygonBean_contract.getUuid(), polygonBean_contract.getNote() + "相关信息");
                            list_Marker_ContractChart.add(marker);
                        }
                    }
                }
            }
        }
    }

    private void showSecondMarker()
    {
        if (list_Marker_AreaChart.size() > 0)
        {
            for (int i = 0; i < list_Marker_AreaChart.size(); i++)
            {
                list_Marker_AreaChart.get(i).setVisible(true);
            }
        } else
        {
            List<parktab> list_parktab = SqliteDb.getparktab(PG_AddGcd.this, commembertab.getuId());
            for (int i = 0; i < list_parktab.size(); i++)//每个园区
            {
                List<areatab> list_areatab = SqliteDb.getareatab(PG_AddGcd.this, list_parktab.get(i).getid());
                for (int k = 0; k < list_areatab.size(); k++)//每个片区
                {
                    PolygonBean polygonBean_area = SqliteDb.getLayer_area(PG_AddGcd.this, list_parktab.get(i).getid(), list_areatab.get(k).getid());

                    if (polygonBean_area != null)
                    {
                        int[] count_saleout = SqliteDb.getdataofareasaleByAreaId(PG_AddGcd.this, commembertab.getuId(), polygonBean_area.getparkId(), polygonBean_area.getAreaId());
                        LatLng latlng = new LatLng(Double.valueOf(polygonBean_area.getLat()), Double.valueOf(polygonBean_area.getLng()));
                        Marker marker = addChartView(polygonBean_area.getparkName() + polygonBean_area.getareaName(), count_saleout[0], count_saleout[1], count_saleout[2], latlng, polygonBean_area.getUuid(), polygonBean_area.getNote() + "相关信息");
                        list_Marker_AreaChart.add(marker);
                    }
                }
            }
        }
    }

    public void showPop_batch()
    {
        LayoutInflater layoutInflater = (LayoutInflater) PG_AddGcd.this.getSystemService(PG_AddGcd.this.LAYOUT_INFLATER_SERVICE);
        pv_batch = layoutInflater.inflate(R.layout.pop_batch, null);// 外层
        pv_batch.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_batch.isShowing()))
                {
                    pw_batch.dismiss();
                    WindowManager.LayoutParams lp = PG_AddGcd.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    PG_AddGcd.this.getWindow().setAttributes(lp);
                    return true;
                }
                return false;
            }
        });
        pv_batch.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_batch.isShowing())
                {
                    pw_batch.dismiss();
                    WindowManager.LayoutParams lp = PG_AddGcd.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    PG_AddGcd.this.getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_batch = new PopupWindow(pv_batch, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//        pw_batch.setAnimationStyle(R.style.topintopout);
        pw_batch.showAsDropDown(line_batch, 0, 0);
        pw_batch.setOutsideTouchable(true);
        ListView lv_batch = (ListView) pv_batch.findViewById(R.id.lv_batch);
        OrderList_Adapter orderList_adapter = new OrderList_Adapter(PG_AddGcd.this, list_SellOrder);
        lv_batch.setAdapter(orderList_adapter);
        lv_batch.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CurrentsellOrder = list_SellOrder.get(position);
                btn_batchofproduct.setText("\"" + CurrentsellOrder.getBuyers() + "\"" + "的订单");
                pw_batch.dismiss();
                WindowManager.LayoutParams lp = PG_AddGcd.this.getWindow().getAttributes();
                lp.alpha = 1f;
                PG_AddGcd.this.getWindow().setAttributes(lp);
                reloadMap();
            }
        });
    }

    private void showFirstMarker()
    {
        if (list_Marker_ParkChart.size() > 0)
        {
            for (int i = 0; i < list_Marker_ParkChart.size(); i++)
            {
                list_Marker_ParkChart.get(i).setVisible(true);
            }
        } else
        {
            List<parktab> list_parktab = SqliteDb.getparktab(PG_AddGcd.this, commembertab.getuId());
            for (int i = 0; i < list_parktab.size(); i++)//每个园区
            {
                PolygonBean polygonBean_park = SqliteDb.getLayer_park(PG_AddGcd.this, list_parktab.get(i).getid());

                if (polygonBean_park != null)
                {
                    int[] count_saleout = SqliteDb.getdataofparksaleByParkId(PG_AddGcd.this, commembertab.getuId(), list_parktab.get(i).getid());
                    LatLng latlng = new LatLng(Double.valueOf(polygonBean_park.getLat()), Double.valueOf(polygonBean_park.getLng()));
                    Marker marker = addChartView(polygonBean_park.getparkName(), count_saleout[0], count_saleout[1], count_saleout[2], latlng, polygonBean_park.getUuid(), polygonBean_park.getNote() + "相关信息");
                    list_Marker_ParkChart.add(marker);
                }
            }
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


    private Marker addChartView(String qy, int number_sakein, int number_saleout, int number_forsale, LatLng latLng, String uuid, String note)
    {
        Marker marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        View view = View.inflate(PG_AddGcd.this, R.layout.addview_saleinfo, null);
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
        View view = LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.marker_sale, null);
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
//        View view = LayoutInflater.from(PG_AddGcd.this).inflate(R.layout.marker_sale, null);
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
//            AppContext appContext = (AppContext) PG_AddGcd.this.getApplication();
//            appContext.setLOCATION_X(String.valueOf(location_latLng.getLatitude()));
//            appContext.setLOCATION_Y(String.valueOf(location_latLng.getLongitude()));
//        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1)
    {

    }

    private void getGCDList()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaid", commembertab.getareaId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("strWhere", "");
        params.addQueryStringParameter("action", "getGCDList");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PlantGcd> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PlantGcd.class);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            LatLng latlng = new LatLng(Double.valueOf(listNewData.get(i).getX()), Double.valueOf(listNewData.get(i).getY()));
                            Marker marker = addCustomMarker(listNewData.get(i), "gcd", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, listNewData.get(i).getId(), listNewData.get(i).getPlantgcdName());
                            list_Objects_gcd.add(marker);
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_AddGcd.this, "error_connectServer");
            }
        });
    }
//    private void AddData(LatLng latlng,String name,String number,String)
//    {
//        String uuid = java.util.UUID.randomUUID().toString();
//        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(this);
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("uuid",uuid);
//        params.addQueryStringParameter("userid", commembertab.getId());
//        params.addQueryStringParameter("userName", commembertab.getrealName());
//        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("parkId", commembertab.getparkId());
//        params.addQueryStringParameter("parkName", commembertab.getparkName());
//        params.addQueryStringParameter("areaId", commembertab.getareaId());
//        params.addQueryStringParameter("areaName", commembertab.getareaName());
//        params.addQueryStringParameter("action", "plantGCDAdd");
//
//        params.addQueryStringParameter("gcdName", et_plantName.getText().toString());
//        params.addQueryStringParameter("gcdNote", et_plantNote.getText().toString());
//
//        params.addQueryStringParameter("x", "112.25482264");
//        params.addQueryStringParameter("y", "23.548768");
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
//        {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo)
//            {
//                String a = responseInfo.result;
//                Result result = JSON.parseObject(responseInfo.result, Result.class);
//                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//                {
//                    if (result.getAffectedRows() != 0)
//                    {
//                        JSONObject jb = result.getRows().getJSONObject(0);
//                        String gcdid = result.getRows().getJSONObject(0).getString("id");
//                        int sl = Integer.valueOf(zzsl);
//                        if (sl > 0)
//                        {
//                            latch = new CountDownLatch(sl);
//                            for (int i = 0; i < sl; i++)
//                            {
//                                AddPlant(gcdid, "植株" + (i + 1), "植株" + (i + 1) + "说明");
//                            }
//
//                        } else
//                        {
//                            Toast.makeText(PG_AddGcd.this, "保存成功！", Toast.LENGTH_SHORT).show();
//                            PG_AddGcd.this.finish();
//                        }
//
//                    } else
//                    {
//                        AppContext.makeToast(PG_AddGcd.this, "error_connectDataBase");
//                    }
//                } else
//                {
//                    AppContext.makeToast(PG_AddGcd.this, "error_connectDataBase");
//                    return;
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException arg0, String arg1)
//            {
//                AppContext.makeToast(PG_AddGcd.this, "error_connectServer");
//            }
//        });
//    }
}
