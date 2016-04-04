package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import com.farm.R;
import com.farm.adapter.BatchList_Adapter;
import com.farm.app.AppContext;
import com.farm.bean.BatchOfProduct;
import com.farm.bean.BreakOff;
import com.farm.bean.CoordinatesBean;
import com.farm.bean.CusPoint;
import com.farm.bean.PolygonBean;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/4/5.
 */
@EFragment
public class NCZ_MainSale extends Fragment implements TencentLocationListener, View.OnClickListener
{
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
    List<SellOrderDetail> list_SellOrderDetail_select=new ArrayList<>();
    List<SellOrderDetail> list_SellOrderDetail_notselect=new ArrayList<>();
    List<CoordinatesBean> list_CoordinatesBean_select=new ArrayList<>();
    List<CoordinatesBean> list_CoordinatesBean_notselect=new ArrayList<>();

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

    List<Marker> list_Objects_house;//房子对象
    List<Marker> list_Objects_road_centermarker;//路中心点对象
    List<Marker> list_Objects_point;//随意点对象
    List<Marker> list_Objects_line_centermarker;//线中心点对象
    List<Marker> list_Objects_mian_centermarker;//面中心点对象

    List<Marker> list_Marker_park;//园区标识对象
    List<Marker> list_Marker_area;//片区标识对象
    List<Marker> list_Marker_contract;//承包区标识对象

    List<Marker> list_Marker_saleout;
    List<Marker> list_Marker_salein;
    List<Marker> list_Marker_salefor;

    List<Marker> list_Marker_AreaChart;//图表标识
    List<Marker> list_Marker_ParkChart;//图表标识
    List<Marker> list_Marker_ContractChart;//图表标识

    List<Polygon> list_Objects_mian;//多边形
    List<Polygon> list_Objects_park;//多边形
    List<Polygon> list_Objects_area;//多边形
    List<Polygon> list_Objects_contract;//多边形
    List<Polygon> list_Objects_saleout;//已售区域
    List<Polygon> list_Objects_salein;//待售区域
    List<Polygon> list_Objects_salefor;//售中区域

    List<PolygonBean> list_polygon_road;
    List<PolygonBean> list_polygon_house;
    List<PolygonBean> list_polygon_point;
    List<PolygonBean> list_polygon_line;
    List<PolygonBean> list_polygon_mian;
    List<PolygonBean> list_polygon_park;
    List<PolygonBean> list_polygon_area;
    List<PolygonBean> list_polygon_contract;
    List<BatchOfProduct> list_BatchOfProduct;
    com.farm.bean.commembertab commembertab;
    int error;
    LatLng  location_latLng;
    String batchTime = "";//产品批次时间
    TencentMap tencentMap;//地图
    UiSettings uiSettings;//地图设置
    Projection mProjection;
    List<Object> Overlays;

    @ViewById
    TextView tv_tip;
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
    Button btn_complete;
    @ViewById
    RelativeLayout rl_createorder;
    @ViewById
    TextView tv_numberoforder;
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
    void rl_createorder()
    {
        if (list_SellOrderDetail.size() > 0)
        {
            Intent intent = new Intent(getActivity(), CreateOrder_.class);
            intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list_SellOrderDetail);
            intent.putExtra("batchtime", batchTime);
            startActivity(intent);
        } else
        {
            Toast.makeText(getActivity(), "请先添加出售区域", Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void btn_batchofproduct()
    {
        if (list_BatchOfProduct.size() > 0)
        {
            showPop_batch();
        } else
        {
            Toast.makeText(getActivity(), "暂无产品批次", Toast.LENGTH_SHORT).show();
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_currentsale, container, false);
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
        list_BatchOfProduct = SqliteDb.getBatchOfProductByuid(getActivity(), commembertab.getuId());
        if (list_BatchOfProduct.size() == 0)
        {
            btn_batchofproduct.setVisibility(View.GONE);
        } else
        {
            batchTime = list_BatchOfProduct.get(0).getBatchTime();
            btn_batchofproduct.setText(batchTime);
            tencentMap = mapview.getMap();
            tencentMap.setZoom(13);
            uiSettings = mapview.getUiSettings();
            tencentMap.setSatelliteEnabled(true);
            mProjection = mapview.getProjection();
            Overlays = new ArrayList<Object>();

            initParam();//初始化参数
            initBasicData();//初始化基础数据
            initSaleData();//初始化销售数据
            initBreakoffData();//初始化断蕾数据
            initMarkerClickListener();
            initMapCameraChangeListener();
            initMapClickListener();
            initMapLongClickListener();
        }
    }
    public void initParam()
    {
        list_Marker_ParkChart=new ArrayList<>();
        list_Marker_AreaChart=new ArrayList<>();
        list_Marker_ContractChart=new ArrayList<>();
    }
    public void initBreakoffData()
    {

    }
    public void initSaleData()
    {
        list_Marker_saleout=new ArrayList<>();
        list_Marker_salein=new ArrayList<>();
        list_Marker_salefor=new ArrayList<>();
        list_Objects_saleout=new ArrayList<>();
        list_Objects_salein=new ArrayList<>();
        list_Objects_salefor=new ArrayList<>();
        List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), commembertab.getuId());
        for (int i = 0; i < list_parktab.size(); i++)//每个园区
        {
            List<areatab> list_areatab = SqliteDb.getareatab(getActivity(), list_parktab.get(i).getid());
            for (int k = 0; k < list_areatab.size(); k++)//每个片区
            {
                List<contractTab> list_contractTab = SqliteDb.getcontracttab(getActivity(), list_areatab.get(k).getid());
                for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
                {
                    List<SellOrderDetail> list_SellOrderDetail = SqliteDb.getSaleLayer_contract(getActivity(), list_contractTab.get(m).getid());
                    if (list_SellOrderDetail != null)
                    {
                        for (int j = 0; j <list_SellOrderDetail.size() ; j++)
                        {
                            SellOrderDetail sellorderdetail=list_SellOrderDetail.get(j);
                            Polygon p = null;
                            if (sellorderdetail.getType().equals("saleout"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getactuallat()), Double.valueOf(sellorderdetail.getactuallng()));
                                Marker marker = addCustomMarker("saleout", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), sellorderdetail.getactualnumber());
                                list_Marker_saleout.add(marker);
                                List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), sellorderdetail.getUuid());
                                p = initBoundary(Color.argb(150, 255, 0, 0), 10f, list_contract, 2, R.color.bg_text);
                                list_Objects_saleout.add(p);
                            }else   if (sellorderdetail.getType().equals("salein"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
                                Marker marker = addCustomMarker("salein", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), sellorderdetail.getplannumber());
                                list_Marker_salein.add(marker);
                                List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), sellorderdetail.getUuid());
                                p = initBoundary(Color.argb(150, 0, 255, 0), 10f, list_contract, 2, R.color.bg_text);
                                list_Objects_salein.add(p);
                            }else    if (sellorderdetail.getType().equals("salefor"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
                                Marker marker = addCustomMarker("salefor", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), sellorderdetail.getactualnumber());
                                list_Marker_salefor.add(marker);
                                List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), sellorderdetail.getUuid());
                                p = initBoundary(Color.argb(150, 0, 0, 255), 10f, list_contract, 2, R.color.bg_text);
                                list_Objects_salefor.add(p);
                            }
                        }
                    }
                }
            }
        }
    }
    public void initBasicData()
    {
        //初始化规划图
        List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), commembertab.getuId());
        for (int i = 0; i < list_parktab.size(); i++)//每个园区
        {
            PolygonBean polygonBean_park = SqliteDb.getLayer_park(getActivity(), list_parktab.get(i).getid());
            if (polygonBean_park != null)
            {
                LatLng latlng = new LatLng(Double.valueOf(polygonBean_park.getLat()), Double.valueOf(polygonBean_park.getLng()));
                Marker marker = addCustomMarker("normal", R.drawable.ic_flag_park, getResources().getColor(R.color.white), latlng, polygonBean_park.getUuid(), polygonBean_park.getNote());
                list_Marker_park.add(marker);
                List<CoordinatesBean> list_park = SqliteDb.getPoints(getActivity(), polygonBean_park.getUuid());
                if (list_park != null && list_park.size() != 0)
                {
                    initBoundary(Color.argb(150, 0, 255, 255), 0f, list_park, 0, R.color.transparent);
                }
            }

            List<areatab> list_areatab = SqliteDb.getareatab(getActivity(), list_parktab.get(i).getid());
            for (int k = 0; k < list_areatab.size(); k++)//每个片区
            {
                PolygonBean polygonBean_area = SqliteDb.getLayer_area(getActivity(), list_parktab.get(i).getid(), list_areatab.get(k).getid());
                if (polygonBean_area != null)
                {
                    LatLng latlng = new LatLng(Double.valueOf(polygonBean_area.getLat()), Double.valueOf(polygonBean_area.getLng()));
                    Marker marker = addCustomMarker("normal", R.drawable.ic_flag_area, getResources().getColor(R.color.white), latlng, polygonBean_area.getUuid(), polygonBean_area.getNote());
                    list_Marker_area.add(marker);
                    List<CoordinatesBean> list_area = SqliteDb.getPoints(getActivity(), polygonBean_area.getUuid());
                    if (list_area != null && list_area.size() != 0)
                    {
                        initBoundary(Color.argb(150, 255, 0, 255), 100f, list_area, 2, R.color.bg_text);
                    }
                }

                List<contractTab> list_contractTab = SqliteDb.getcontracttab(getActivity(), list_areatab.get(k).getid());
                for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
                {
                    //承包区规划图
                    PolygonBean polygonBean_contract = SqliteDb.getLayer_contract(getActivity(), list_parktab.get(i).getid(), list_areatab.get(k).getid(), list_contractTab.get(m).getid());
                    if (polygonBean_contract != null)
                    {
                        LatLng latlng = new LatLng(Double.valueOf(polygonBean_contract.getLat()), Double.valueOf(polygonBean_contract.getLng()));
                        Marker marker = addCustomMarker("normal",R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, polygonBean_contract.getUuid(), polygonBean_contract.getNote());
                        list_Marker_contract.add(marker);
                        List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), polygonBean_contract.getUuid());
                        if (list_contract != null && list_contract.size() != 0)
                        {
                             initBoundary(Color.argb(150, 255, 255, 0), 200f, list_contract, 2, R.color.bg_text);
                        }
                    }
                }
            }

        }
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
    public void initLinePolygon()
    {
        List<PolygonBean> list_polygon_line = SqliteDb.getMoreLayer_line(getActivity(), commembertab.getuId());
        for (int i = 0; i < list_polygon_line.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_line.get(i).getUuid());
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
                    Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on,getResources().getColor(R.color.bg_sq), list_latlang.get(0), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
                    list_Objects_line_centermarker.add(marker);
                } else
                {
                    Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on,getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
                    list_Objects_line_centermarker.add(marker);
                }

            }

        }
    }
    public void initRoadPolygon()
    {
        list_polygon_road = SqliteDb.getMoreLayer_road(getActivity(), commembertab.getuId());
        for (int i = 0; i < list_polygon_road.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_road.get(i).getUuid());
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
//            line.setGeodesic(true);
            line.setColor(Color.argb(500, 255, 255, 255));
            line.setWidth(8f);
            Overlays.add(line);
            list_Objects_road.add(line);

            if (list_latlang.size() > 0)
            {
                int halfsize = list_latlang.size() / 2;
                Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_road.get(i).getUuid(), list_polygon_road.get(i).getNote());
                list_Objects_road_centermarker.add(marker);
            }

        }
    }


    public void initMianPolygon()
    {
        list_polygon_mian = SqliteDb.getMoreLayer_mian(getActivity(), commembertab.getuId());
        for (int i = 0; i < list_polygon_mian.size(); i++)
        {
            LatLng latlng = new LatLng(Double.valueOf(list_polygon_mian.get(i).getLat()), Double.valueOf(list_polygon_mian.get(i).getLng()));
            Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on,getResources().getColor(R.color.bg_ask), latlng, list_polygon_mian.get(i).getUuid(), list_polygon_mian.get(i).getNote());
            list_Objects_mian_centermarker.add(marker);
            List<CoordinatesBean> list_mian = SqliteDb.getPoints(getActivity(), list_polygon_mian.get(i).getUuid());
            List<LatLng> list_LatLng = new ArrayList<>();
            if (list_mian != null && list_mian.size() != 0)
            {
                for (int j = 0; j < list_mian.size(); j++)
                {
                    LatLng ll = new LatLng(Double.valueOf(list_mian.get(j).getLat()), Double.valueOf(list_mian.get(j).getLng()));
                    list_LatLng.add(ll);
                }
                Polygon polygon = drawPolygon(10f, list_LatLng, Color.argb(180, 70, 101, 10), 2, R.color.bg_text);
                polygon.setZIndex(0f);
                Overlays.add(polygon);
                list_Objects_mian.add(polygon);
            }

        }
    }
    public void initHousePolygon()
    {
        list_polygon_house = SqliteDb.getMoreLayer_house(getActivity(), commembertab.getuId());
        for (int i = 0; i < list_polygon_house.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_house.get(i).getUuid());
            if (list != null && list.size() != 0)
            {
                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
                Marker marker = addCustomMarker("normal", R.drawable.icon_house, getResources().getColor(R.color.bg_ask), latlng, list_polygon_house.get(i).getUuid(), list_polygon_house.get(i).getNote());
                list_Objects_house.add(marker);
            }

        }
    }
    public void initPointPolygon()
    {
        list_polygon_point = SqliteDb.getMoreLayer_point(getActivity(), commembertab.getuId());
        for (int i = 0; i < list_polygon_point.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_point.get(i).getUuid());
            if (list != null && list.size() != 0)
            {
                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
                Marker marker = addCustomMarker("normal",R.drawable.umeng_socialize_follow_on,getResources().getColor(R.color.bg_job), latlng, list_polygon_point.get(i).getUuid(), list_polygon_point.get(i).getNote());
                list_Objects_point.add(marker);
            }

        }
    }
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
                    ll_sm.setVisibility(View.VISIBLE);
                } else
                {
                    ll_sm.setVisibility(View.GONE);
                }
                //规划图层信息显示控制
                if (cb_parkdata.isSelected())
                {
                    if (zoomlevel <= 13)
                    {
                        showFirstMarker();
                    } else
                    {
                        for (int i = 0; i < list_Marker_ParkChart.size(); i++)
                        {
                            list_Marker_ParkChart.get(i).setVisible(false);
                        }
                    }
                }


                //片区图层信息显示控制
                if (cb_areadata.isSelected())
                {
                    if (zoomlevel == 14)
                    {
                        showSecondMarker();
                    } else
                    {
                        for (int i = 0; i < list_Marker_AreaChart.size(); i++)
                        {
                            list_Marker_AreaChart.get(i).setVisible(false);
                        }
                    }
                }


                //其他图层信息显示控制
                if (cb_contractdata.isSelected())
                {
                    if (zoomlevel >= 15)
                    {
                        showThirdMarker();
                    } else
                    {
                        for (int i = 0; i < list_Marker_ContractChart.size(); i++)
                        {
                            list_Marker_ContractChart.get(i).setVisible(false);
                        }
                    }
                }


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
                    PolygonBean polygonbean = SqliteDb.getLayerbyuuid(getActivity(), uuid);
                    showDialog_overlayInfo(polygonbean.getNote());
                } else if (type.equals("breakoff"))
                {
                    BreakOff breakoff = SqliteDb.getbreakoffByuuid(getActivity(), uuid);
                    showDialog_overlayInfo(breakoff.getBreakofftime() + "断蕾" + breakoff.getnumberofbreakoff() + "株");
                } else if (type.equals("salein"))
                {
                    showDialog_OperateSalein(uuid, marker);
                } else if (type.equals("saleout"))
                {
                    SellOrderDetail SellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(getActivity(), uuid);
                    showDialog_overlayInfo(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname() + "\n" + "计划出售" + SellOrderDetail.getplannumber() + "株" + "\n" + "实际出售" + SellOrderDetail.getactualnumber() + "株");
                } else if (type.equals("salefor"))
                {
                    showDialog_OperateSalefor(uuid, marker);
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
        Polygon polygon = drawPolygon(10f, list_AllLatLng, fillcolor, strokesize, strokecolor);
        polygon.setZIndex(z);
        Overlays.add(polygon);
        return polygon;
    }
    public void showDialog_OperateSalefor(final String uuid, final Marker marker)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_operatepolygon_salefor, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(getActivity(), R.style.MyDialog, dialog_layout);
        Button btn_sale = (Button) dialog_layout.findViewById(R.id.btn_sale);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SellOrderDetail SellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(getActivity(), uuid);
                showDialog_overlayInfo(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname() + "\n" + "待出售" + SellOrderDetail.getactualnumber() + "株");
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
                isable = true;
                tencentMap.setZoom(14);
                initMapClickWhenPaint();
                initMapLongPressWhenPaint();
                 polygon_needsale=SqliteDb.getSellOrderDetailbyuuid(getActivity(), uuid);
                List<CoordinatesBean> list_coordinatesbean = SqliteDb.getPoints(getActivity(), polygon_needsale.getUuid());
                if (list_coordinatesbean != null && list_coordinatesbean.size() != 0)
                {
                            showNeedPlanBoundary(list_coordinatesbean);
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
    private void showNeedPlanBoundary(List<CoordinatesBean> list_coordinates)
    {
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
                showDialog_SelectWholePolygon(latLng);
            }
        });

    }
    public void showDialog_SelectWholePolygon(final LatLng latlng)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_selectwholepolygon, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(getActivity(), R.style.MyDialog, dialog_layout);
        Button btn_selectwhole = (Button) dialog_layout.findViewById(R.id.btn_selectwhole);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);

        btn_selectwhole.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                list_latlng_divide1 = new ArrayList<LatLng>();
                list_latlng_divide2 = new ArrayList<LatLng>();
                for (int i = 0; i < list_coordinatesbean.size(); i++)
                {
                    LatLng latlng = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                    list_latlng_divide1.add(latlng);
                }
                savedividedPolygonInfo_SelectWhole(latlng, list_latlng_divide1, list_latlng_divide2);
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
        polygon_divide1 = drawPolygon(10f, list_latlng_divide1, Color.argb(1000, 255, 255, 0), 6, R.color.white);
        Overlays.add(polygon_divide1);
        //画出未选区域
        polygon_divide2 = drawPolygon(20f, list_latlng_divide2, Color.argb(100, 0, 0, 255), 2, R.color.black);
        Overlays.add(polygon_divide2);

        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setBackgroundResource(R.color.bg_job);
        tv_tip.setText("请选择需要的区域");

        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                if (polygon_divide1.contains(latLng))
                {
                    initMapClickListener();
                    showDialog_addsaleinfo(latLng,list_latlng_divide1, list_latlng_divide2);
                } else if (polygon_divide2.contains(latLng))
                {
                    initMapClickListener();
                    showDialog_addsaleinfo(latLng, list_latlng_divide2, list_latlng_divide1);
                } else
                {
                    tv_tip.setText("请在划分的两个区域中选择");
                }
            }
        });


        for (int i = 0; i < list_Objects_divideline.size(); i++)
        {
            tencentMap.removeOverlay(list_Objects_divideline.get(i));
        }
    }
    public void showDialog_addsaleinfo(final LatLng centerlatLng,final List<LatLng> list_latlng_selectpart, final List<LatLng> list_latlng_notselectpart)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_addsaleifo, null);
        customDialog_addSaleInInfo = new CustomDialog_AddSaleInInfo(getActivity(), R.style.MyDialog, dialog_layout);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        Button  btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button  btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
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
        String uuid_sale= java.util.UUID.randomUUID().toString();
        SellOrderDetail  SellOrderDetail=new  SellOrderDetail();
        SellOrderDetail.setid("");
        SellOrderDetail.setUuid(uuid_sale);
        SellOrderDetail.setsaleid("");
        SellOrderDetail.setBatchTime(batchTime);
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
        SellOrderDetail.setType("salein_boundary");
        list_SellOrderDetail_newsale.add(SellOrderDetail);
        for (int i = 0; i < list_select.size(); i++)
        {
            CoordinatesBean coordinatesBean = new CoordinatesBean();
            coordinatesBean.setLat(String.valueOf(list_select.get(i).getLatitude()));
            coordinatesBean.setLng(String.valueOf(list_select.get(i).getLongitude()));
            coordinatesBean.setNumofplant("");
            coordinatesBean.setType("");
            coordinatesBean.setUid("");
            coordinatesBean.setparkId("");
            coordinatesBean.setparkName("");
            coordinatesBean.setUuid(uuid_sale);
            coordinatesBean.setAreaId("");
            coordinatesBean.setareaName("");
            coordinatesBean.setContractid("");
            coordinatesBean.setContractname("");
            coordinatesBean.setBatchid("");
            coordinatesBean.setCoordinatestime(utils.getTime());
            coordinatesBean.setRegistime(utils.getTime());
            coordinatesBean.setWeightofplant("");
            coordinatesBean.setSaleid("");
            coordinatesBean.setOrders("");
            SqliteDb.save(getActivity(), coordinatesBean);
        }


        //设置剩余部分的中心点位置
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("请为剩余部分选择一个中心点");
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                //未选择区域
                String uuid_notsale= java.util.UUID.randomUUID().toString();
                SellOrderDetail  SellOrderDetail_notsale=new  SellOrderDetail();
                SellOrderDetail_notsale.setid("");
                SellOrderDetail_notsale.setUuid(uuid_notsale);
                SellOrderDetail_notsale.setsaleid("");
                SellOrderDetail_notsale.setBatchTime(batchTime);
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
                SellOrderDetail_notsale.setplannumber("");
                SellOrderDetail_notsale.setplanweight("");
                SellOrderDetail_notsale.setactualnumber(String.valueOf(Integer.valueOf(polygon_needsale.getactualnumber()) - Integer.valueOf(salenumber)));
                SellOrderDetail_notsale.setactualweight("");
                SellOrderDetail_notsale.setplannote("");
                SellOrderDetail_notsale.setactualnote("");
                SellOrderDetail_notsale.setreg(utils.getTime());
                SellOrderDetail_notsale.setstatus("");
                SellOrderDetail_notsale.setisSoldOut("0");
                SellOrderDetail_notsale.setXxzt("0");
                SellOrderDetail_notsale.setType("salefor_boundary");
                list_SellOrderDetail_newsale.add(SellOrderDetail_notsale);
                for (int i = 0; i < list_notselect.size(); i++)
                {
                    CoordinatesBean coordinatesBean = new CoordinatesBean();
                    coordinatesBean.setLat(String.valueOf(list_notselect.get(i).getLatitude()));
                    coordinatesBean.setLng(String.valueOf(list_notselect.get(i).getLongitude()));
                    coordinatesBean.setNumofplant("");
                    coordinatesBean.setType("");
                    coordinatesBean.setUid("");
                    coordinatesBean.setparkId("");
                    coordinatesBean.setparkName("");
                    coordinatesBean.setUuid(uuid_notsale);
                    coordinatesBean.setAreaId("");
                    coordinatesBean.setareaName("");
                    coordinatesBean.setContractid("");
                    coordinatesBean.setContractname("");
                    coordinatesBean.setBatchid("");
                    coordinatesBean.setCoordinatestime(utils.getTime());
                    coordinatesBean.setRegistime(utils.getTime());
                    coordinatesBean.setWeightofplant("");
                    coordinatesBean.setSaleid("");
                    coordinatesBean.setOrders("");
                    SqliteDb.save(getActivity(), coordinatesBean);
                }
               reloadMap();
            }
        });

    }
    private void reloadMap()
    {
        tv_tip.setVisibility(View.GONE);
        tencentMap.clearAllOverlays();
        btn_complete.setVisibility(View.GONE);
        tv_tip.setVisibility(View.GONE);
        btn_showlayer.setVisibility(View.VISIBLE);
        btn_setting.setVisibility(View.VISIBLE);

        initParam();//初始化参数
        initBasicData();//初始化基础数据
        initSaleData();//初始化销售数据
        initBreakoffData();//初始化断蕾数据
        initMarkerClickListener();
        initMapCameraChangeListener();
        initMapClickListener();
        initMapLongClickListener();
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
                SellOrderDetail sellOrderDetail=SqliteDb.getSellOrderDetailbyuuid(getActivity(),uuid);
                sellOrderDetail.setplannumber("");
                sellOrderDetail.setactualnumber("");
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

    public void showDialog_OperateSalein(final String uuid, final Marker marker)
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
                showDialog_deletetip_salein(uuid, marker);
            }
        });
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SellOrderDetail SellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(getActivity(), uuid);
                showDialog_overlayInfo(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname() + "\n" + SellOrderDetail.getplannumber() + "株" + "出售中..." );
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SellOrderDetail sellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(getActivity(), uuid);
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
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_showpolygonifo, null);
        customDialog_overlayInfo = new CustomDialog_OverlayInfo(getActivity(), R.style.MyDialog, dialog_layout);
        TextView tv_note = (TextView) dialog_layout.findViewById(R.id.tv_note);
        Button  btn_close = (Button) dialog_layout.findViewById(R.id.btn_close);
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
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_editsaleininfo, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(getActivity(), R.style.MyDialog, dialog_layout);
        et_note = (EditText) dialog_layout.findViewById(R.id.et_note);
        et_note.setText(sellOrderDetail.getplannumber());
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button  btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
                sellOrderDetail.setplannumber(et_note.getText().toString());
                sellOrderDetail.setstatus("0");
                sellOrderDetail.setisSoldOut("0");
                boolean issuccess = SqliteDb.editSellOrderDetail_salein(getActivity(), sellOrderDetail);
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
            List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), commembertab.getuId());
            for (int i = 0; i < list_parktab.size(); i++)//每个园区
            {
                List<areatab> list_areatab = SqliteDb.getareatab(getActivity(), list_parktab.get(i).getid());
                for (int k = 0; k < list_areatab.size(); k++)//每个片区
                {
                    List<contractTab> list_contractTab = SqliteDb.getcontracttab(getActivity(), list_areatab.get(k).getid());
                    for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
                    {
                        PolygonBean polygonBean_contract = SqliteDb.getLayer_contract(getActivity(), list_parktab.get(i).getid(), list_areatab.get(k).getid(), list_contractTab.get(m).getid());

                        if (polygonBean_contract != null)
                        {
                            int[] count_saleout = SqliteDb.getdataofcontractsale(getActivity(), commembertab.getuId(), polygonBean_contract.getparkId(), polygonBean_contract.getAreaId(), polygonBean_contract.getContractid(), batchTime);
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
            List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), commembertab.getuId());
            for (int i = 0; i < list_parktab.size(); i++)//每个园区
            {
                List<areatab> list_areatab = SqliteDb.getareatab(getActivity(), list_parktab.get(i).getid());
                for (int k = 0; k < list_areatab.size(); k++)//每个片区
                {
                    PolygonBean polygonBean_area = SqliteDb.getLayer_area(getActivity(), list_parktab.get(i).getid(), list_areatab.get(k).getid());

                    if (polygonBean_area != null)
                    {
                        int[] count_saleout = SqliteDb.getdataofareasale(getActivity(), commembertab.getuId(), polygonBean_area.getparkId(), polygonBean_area.getAreaId(), batchTime);
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
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_batch = layoutInflater.inflate(R.layout.pop_batch, null);// 外层
        pv_batch.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_batch.isShowing()))
                {
                    pw_batch.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
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
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_batch = new PopupWindow(pv_batch, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_batch.setAnimationStyle(R.style.topintopout);
        pw_batch.showAsDropDown(line_batch, 0, 0);
        pw_batch.setOutsideTouchable(true);
        ListView lv_batch = (ListView) pv_batch.findViewById(R.id.lv_batch);
        BatchList_Adapter batchlist_adapter = new BatchList_Adapter(getActivity(), list_BatchOfProduct);
        lv_batch.setAdapter(batchlist_adapter);
        lv_batch.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                batchTime = list_BatchOfProduct.get(position).getBatchTime();
                btn_batchofproduct.setText(batchTime);
                pw_batch.dismiss();
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);

                initSaleData();//初始化销售数据
                initBreakoffData();//初始化断蕾数据
                initMarkerClickListener();
                initMapLongClickListener();
                initMapClickListener();
                initMapCameraChangeListener();
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
            List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), commembertab.getuId());
            for (int i = 0; i < list_parktab.size(); i++)//每个园区
            {
                PolygonBean polygonBean_park = SqliteDb.getLayer_park(getActivity(), list_parktab.get(i).getid());

                if (polygonBean_park != null)
                {
                    int[] count_saleout = SqliteDb.getdataofparksale(getActivity(), commembertab.getuId(), list_parktab.get(i).getid(), batchTime);
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
    private Marker addCustomMarker(String type,int icon, int textcolor, LatLng latLng, String uuid, String note)
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
        bundle.putString("type",type);
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
        if (TencentLocation.ERROR_OK == error) // 定位成功
        {
            // 用于定位
            location_latLng = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
            //全局记录坐标
            AppContext appContext = (AppContext) getActivity().getApplication();
            appContext.setLOCATION_X(String.valueOf(location_latLng.getLatitude()));
            appContext.setLOCATION_Y(String.valueOf(location_latLng.getLongitude()));
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1)
    {

    }
}
