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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.BatchList_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchOfProduct;
import com.farm.bean.BreakOff;
import com.farm.bean.CoordinatesBean;
import com.farm.bean.CusPoint;
import com.farm.bean.PlantGcd;
import com.farm.bean.PolygonBean;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
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
public class NCZ_MainSale extends Fragment implements TencentLocationListener, View.OnClickListener
{
    boolean isanimateToCenter = false;
    List<CoordinatesBean> list_coordinate_select = new ArrayList<CoordinatesBean>();
    List<CoordinatesBean> list_coordinate_notselect = new ArrayList<CoordinatesBean>();
    SellOrderDetail sellOrderDetail_select = new SellOrderDetail();
    SellOrderDetail sellOrderDetail_notselect = new SellOrderDetail();
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

    List<Marker> list_Objects_house;//房子对象
    List<Marker> list_Objects_road_centermarker;//路中心点对象
    List<Marker> list_Objects_point;//随意点对象
    List<Marker> list_Objects_gcd;//随意点对象
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
    List<Marker> list_Marker_block;//承包区标识对象
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
    List<PolygonBean> list_polygon_gcd;
    List<PolygonBean> list_polygon_line;
    List<PolygonBean> list_polygon_mian;
    List<PolygonBean> list_polygon_park;
    List<PolygonBean> list_polygon_area;
    List<PolygonBean> list_polygon_contract;
    List<BatchOfProduct> list_BatchOfProduct;
    com.farm.bean.commembertab commembertab;
    int error;
    LatLng location_latLng;
    String batchTime = "";//产品批次时间
    TencentMap tencentMap;//地图
    UiSettings uiSettings;//地图设置
    Projection mProjection;
    List<Object> Overlays;
    @ViewById
    ProgressBar pb_upload;
    @ViewById
    TextView tv_tip;
    @ViewById
    FrameLayout menu_container;
    @ViewById
    LinearLayout ll_container;
    @ViewById
    Button btn_canclecontainer;
    @ViewById
    Button btn_shoppingcart;
    @ViewById
    FrameLayout fl_salelist;
    @ViewById
    RelativeLayout rl_batchofproduct;
    @ViewById
    TextView tv_salelist;
    @ViewById
    Button btn_showlayer;
    @ViewById
    Button btn_yx;
    @ViewById
    Button btn_orders;
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
    CheckBox cb_block;
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
    void btn_canclecontainer()
    {
        ll_container.setVisibility(View.GONE);
    }

    @Click
    void btn_orders()
    {
        Intent intent = new Intent(getActivity(), NCZ_SaleMap_.class);
        getActivity().startActivity(intent);
    }

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
    void btn_shoppingcart()
    {
        getShoppingCart();
    }

    @Click
    void btn_batchofproduct()
    {
        if (list_BatchOfProduct != null && list_BatchOfProduct.size() > 0)
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
        InitProductBatch();
        tencentMap = mapview.getMap();
        tencentMap.setZoom(18);
        uiSettings = mapview.getUiSettings();
        tencentMap.setSatelliteEnabled(true);
        mProjection = mapview.getProjection();
        Overlays = new ArrayList<Object>();

        initParam();//初始化参数
        initBasicData();//初始化基础数据
//        initShoppingCart();//初始化购物车清单
//        initSaleData();//初始化销售数据
//        initBreakoffData();//初始化断蕾数据
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
        list_Marker_block = new ArrayList<>();
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

    //    public void initShoppingCart()
//    {
//        List<SellOrderDetail> list = SqliteDb.getNewSaleList(getActivity(), commembertab.getuId(), batchTime);
//        if (list != null && list.size() > 0)
//        {
//            fl_salelist.setVisibility(View.VISIBLE);
//            tv_salelist.setText(String.valueOf(list.size()));
//        } else
//        {
////            Toast.makeText(getActivity(), "请先添加出售区域", Toast.LENGTH_SHORT).show();
//            fl_salelist.setVisibility(View.GONE);
//            tv_salelist.setText("0");
//        }
//    }
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
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("batchTime", batchTime);
        params.addQueryStringParameter("action", "GetBreakOffByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<BreakOff> list_BreakOff = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        list_BreakOff = JSON.parseArray(result.getRows().toJSONString(), BreakOff.class);
//                        int count = 0;
                        if (list_BreakOff != null && list_BreakOff.size() > 0)
                        {
//                            for (int n = 0; n < list_BreakOff.size(); n++)
//                            {
//                                count = count + Integer.valueOf(list_BreakOff.get(n).getnumberofbreakoff());
//                            }
                            for (int j = 0; j < list_BreakOff.size(); j++)
                            {
                                BreakOff breakOff = list_BreakOff.get(j);
                                if (breakOff.getStatus().equals("1"))//该批次已断蕾情况
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(breakOff.getLat()), Double.valueOf(breakOff.getLng()));
                                    Marker marker = addCustomMarker(breakOff, "breakoff", R.drawable.ic_breakoff, getResources().getColor(R.color.white), latlng, breakOff.getUuid(), "断" + breakOff.getnumberofbreakoff());
                                    marker.setVisible(false);
                                    list_Marker_breakoff.add(marker);
                                    Polygon p = null;
                                    List<CoordinatesBean> list_breakoff = breakOff.getCoordinatesBeanList();
                                    int batchcolor = utils.getBatchColorByName(breakOff.getBatchColor());
                                    p = initBoundary(batchcolor, 200f, list_breakoff, 0, R.color.transparent);//注意20f，数值越大越在顶层
                                    p.setVisible(false);
                                    list_Objects_breakoff.add(p);
                                } else//该批次未断蕾情况
                                {
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
//    public void initBreakoffData()
//    {
//        list_Objects_breakoff = new ArrayList<>();
//        list_Marker_breakoff = new ArrayList<>();
//        List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_parktab.size(); i++)//每个园区
//        {
//            List<areatab> list_areatab = SqliteDb.getareatab(getActivity(), list_parktab.get(i).getid());
//            for (int k = 0; k < list_areatab.size(); k++)//每个片区
//            {
//                List<contractTab> list_contractTab = SqliteDb.getcontracttab(getActivity(), list_areatab.get(k).getid());
//                for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
//                {
//                    List<BreakOff> list_BreakOff = SqliteDb.getBreakOffInfo(getActivity(), list_contractTab.get(m).getid(), batchTime);
//                    int count = 0;
//                    if (list_BreakOff != null && list_BreakOff.size() > 0)
//                    {
//                        for (int n = 0; n < list_BreakOff.size(); n++)
//                        {
//                            count = count + Integer.valueOf(list_BreakOff.get(n).getnumberofbreakoff());
//                        }
//                        for (int j = 0; j < list_BreakOff.size(); j++)
//                        {
//                            BreakOff breakOff = list_BreakOff.get(j);
//                            if (breakOff.getStatus().equals("1"))//该批次已断蕾情况
//                            {
//                                LatLng latlng = new LatLng(Double.valueOf(breakOff.getLat()), Double.valueOf(breakOff.getLng()));
//                                Marker marker = addCustomMarker(breakOff, "breakoff", R.drawable.ic_breakoff, getResources().getColor(R.color.white), latlng, breakOff.getUuid(), "批次" + batchTime + "共断蕾" + String.valueOf(count) + "株");
//                                marker.setVisible(false);
//                                list_Marker_breakoff.add(marker);
//                                Polygon p = null;
//                                List<CoordinatesBean> list_breakoff = SqliteDb.getPoints(getActivity(), breakOff.getUuid());
//                                int batchcolor = utils.getBatchColorByName(breakOff.getBatchColor());
//                                p = initBoundary(batchcolor, 20f, list_breakoff, 0, R.color.transparent);//注意20f，数值越大越在顶层
//                                p.setVisible(false);
//                                list_Objects_breakoff.add(p);
//                            } else//该批次未断蕾情况
//                            {
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    public void initSaleData()
//    {
//        list_Marker_allsale = new ArrayList<>();
//        list_Marker_saleout = new ArrayList<>();
//        list_Marker_salein = new ArrayList<>();
//        list_Marker_newsale = new ArrayList<>();
//        list_Marker_salefor = new ArrayList<>();
//        list_Objects_allsale = new ArrayList<>();
//        list_Objects_saleout = new ArrayList<>();
//        list_Objects_salein = new ArrayList<>();
//        list_Objects_newsale = new ArrayList<>();
//        list_Objects_salefor = new ArrayList<>();
//        List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_parktab.size(); i++)//每个园区
//        {
//            List<areatab> list_areatab = SqliteDb.getareatab(getActivity(), list_parktab.get(i).getid());
//            for (int k = 0; k < list_areatab.size(); k++)//每个片区
//            {
//                List<contractTab> list_contractTab = SqliteDb.getcontracttab(getActivity(), list_areatab.get(k).getid());
//                for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
//                {
//                    List<SellOrderDetail> list_SellOrderDetail = SqliteDb.getSaleLayer_contract(getActivity(), list_contractTab.get(m).getid(), batchTime, utils.getYear());
//                    if (list_SellOrderDetail != null)
//                    {
//                        for (int j = 0; j < list_SellOrderDetail.size(); j++)
//                        {
//                            SellOrderDetail sellorderdetail = list_SellOrderDetail.get(j);
//                            Polygon p = null;
//                            if (sellorderdetail.getType().equals("saleout"))
//                            {
//                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
//                                Marker marker = addCustomMarker(sellorderdetail, "saleout", R.drawable.ic_saleout, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "已售" + sellorderdetail.getactualnumber());
////                                marker.setVisible(false);
//                                list_Marker_allsale.add(marker);
//                                list_Marker_saleout.add(marker);
//                                List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), sellorderdetail.getUuid());
//                                p = initBoundary(Color.argb(1000, 255, 0, 0), 10f, list_contract, 2, R.color.bg_text);//红色
//                                list_Objects_allsale.add(p);
//                                list_Objects_saleout.add(p);
//                            } else if (sellorderdetail.getType().equals("salein"))
//                            {
//                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
//                                Marker marker = addCustomMarker(sellorderdetail, "salein", R.drawable.ic_salein, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "售中" + sellorderdetail.getplannumber());
////                                marker.setVisible(false);
//                                list_Marker_allsale.add(marker);
//                                list_Marker_salein.add(marker);
//                                List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), sellorderdetail.getUuid());
//                                p = initBoundary(Color.argb(1000, 0, 255, 0), 10f, list_contract, 2, R.color.bg_text);//绿色
//                                list_Objects_allsale.add(p);
//                                list_Objects_salein.add(p);
//                            } else if (sellorderdetail.getType().equals("newsale"))
//                            {
//                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
//                                Marker marker = addCustomMarker(sellorderdetail, "newsale", R.drawable.ic_newsale, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "拟售" + sellorderdetail.getplannumber());
////                                marker.setVisible(false);
//                                list_Marker_allsale.add(marker);
//                                list_Marker_newsale.add(marker);
//                                List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), sellorderdetail.getUuid());
//                                p = initBoundary(Color.argb(1000, 53, 5, 5), 10f, list_contract, 2, R.color.bg_text);//黄色
//                                list_Objects_allsale.add(p);
//                                list_Objects_newsale.add(p);
//                            } else if (sellorderdetail.getType().equals("salefor"))
//                            {
//                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
//                                Marker marker = addCustomMarker(sellorderdetail, "salefor", R.drawable.ic_saleout, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "待售" + sellorderdetail.getplannumber());
////                                marker.setVisible(false);
//                                list_Marker_allsale.add(marker);
//                                list_Marker_salefor.add(marker);
//                                List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), sellorderdetail.getUuid());
//                                p = initBoundary(Color.argb(1000, 0, 0, 255), 10f, list_contract, 2, R.color.bg_text);//蓝色
//                                list_Objects_allsale.add(p);
//                                list_Objects_salefor.add(p);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    public void initBasicData()
    {
//显示规划图
//        InitPlanMap();
        InitArea();
//显示点
        getGcdList();
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
                                initBoundaryLine(100f, list_contract, 6, getResources().getColor(R.color.red));
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

    private void InitProductBatch()
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
                        if (list_BatchOfProduct.size() == 0)
                        {
                            btn_batchofproduct.setText("暂无产品批次");
                        } else
                        {
                            batchTime = list_BatchOfProduct.get(0).getBatchTime();
                            btn_batchofproduct.setText(batchTime);
                        }
                        initShoppingCart();//初始化购物车清单
                        initSaleData();//初始化销售数据
                        initBreakoffData();//初始化断蕾数据
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

    private void initSaleData()
    {
        list_Marker_allsale = new ArrayList<>();
        list_Marker_saleout = new ArrayList<>();
        list_Marker_salein = new ArrayList<>();
        list_Marker_newsale = new ArrayList<>();
        list_Marker_salefor = new ArrayList<>();
        list_Objects_allsale = new ArrayList<>();
        list_Objects_saleout = new ArrayList<>();
        list_Objects_salein = new ArrayList<>();
        list_Objects_newsale = new ArrayList<>();
        list_Objects_salefor = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("batchTime", batchTime);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getSellOrderDetail");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrderDetail> list_SellOrderDetail = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        list_SellOrderDetail = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail.class);
                        for (int j = 0; j < list_SellOrderDetail.size(); j++)
                        {
                            SellOrderDetail sellorderdetail = list_SellOrderDetail.get(j);
                            Polygon p = null;
                            if (sellorderdetail.getType().equals("saleout"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
                                Marker marker = addCustomMarker(sellorderdetail, "saleout", R.drawable.ic_saleout, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "已售" + sellorderdetail.getactualnumber());
//                                marker.setVisible(false);
                                list_Marker_allsale.add(marker);
                                list_Marker_saleout.add(marker);
                                List<CoordinatesBean> list_contract = sellorderdetail.getCoordinatesBeanList();
                                p = initBoundary(Color.argb(1000, 255, 0, 0), 10f, list_contract, 2, R.color.bg_text);//红色
                                list_Objects_allsale.add(p);
                                list_Objects_saleout.add(p);
                            } else if (sellorderdetail.getType().equals("salein"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
                                Marker marker = addCustomMarker(sellorderdetail, "salein", R.drawable.ic_salein, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "售中" + sellorderdetail.getplannumber());
//                                marker.setVisible(false);
                                list_Marker_allsale.add(marker);
                                list_Marker_salein.add(marker);
                                List<CoordinatesBean> list_contract = sellorderdetail.getCoordinatesBeanList();
                                p = initBoundary(Color.argb(1000, 0, 255, 0), 10f, list_contract, 2, R.color.bg_text);//绿色
                                list_Objects_allsale.add(p);
                                list_Objects_salein.add(p);
                            } else if (sellorderdetail.getType().equals("newsale"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
                                Marker marker = addCustomMarker(sellorderdetail, "newsale", R.drawable.ic_newsale, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "拟售" + sellorderdetail.getplannumber());
//                                marker.setVisible(false);
                                list_Marker_allsale.add(marker);
                                list_Marker_newsale.add(marker);
                                List<CoordinatesBean> list_contract = sellorderdetail.getCoordinatesBeanList();
                                p = initBoundary(Color.argb(1000, 53, 5, 5), 10f, list_contract, 2, R.color.bg_text);//黄色
                                list_Objects_allsale.add(p);
                                list_Objects_newsale.add(p);
                            } else if (sellorderdetail.getType().equals("salefor"))
                            {
                                LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
                                Marker marker = addCustomMarker(sellorderdetail, "salefor", R.drawable.ic_saleout, getResources().getColor(R.color.white), latlng, sellorderdetail.getUuid(), "待售" + sellorderdetail.getplannumber());
//                                marker.setVisible(false);
                                list_Marker_allsale.add(marker);
                                list_Marker_salefor.add(marker);
                                List<CoordinatesBean> list_contract = sellorderdetail.getCoordinatesBeanList();
                                p = initBoundary(Color.argb(1000, 0, 0, 255), 10f, list_contract, 2, R.color.bg_text);//蓝色
                                list_Objects_allsale.add(p);
                                list_Objects_salefor.add(p);
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

    private void getShoppingCart()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("batchTime", batchTime);
        params.addQueryStringParameter("action", "getShoppingCart");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrderDetail> list_newSale;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        list_newSale = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail.class);
                        if (list_newSale != null && list_newSale.size() > 0)
                        {
                            showDialog_OperateShoppingcart(list_newSale);
                        } else
                        {
                            Toast.makeText(getActivity(), "请先添加出售区域", Toast.LENGTH_SHORT).show();
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

    private void initShoppingCart()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("batchTime", batchTime);
        params.addQueryStringParameter("action", "getShoppingCart");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrderDetail> list = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        list = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail.class);
                        if (list != null && list.size() > 0)
                        {
                            fl_salelist.setVisibility(View.VISIBLE);
                            tv_salelist.setText(String.valueOf(list.size()));
                        } else
                        {
                            fl_salelist.setVisibility(View.GONE);
                            tv_salelist.setText("0");
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

    private void deletePolygon(final SellOrderDetail sellOrderDetail)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", sellOrderDetail.getUuid());
        params.addQueryStringParameter("action", "deleteSellOrderDetail");
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
                        Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                        reloadMap();
                    } else
                    {
                        Toast.makeText(getActivity(), "删除失败！", Toast.LENGTH_SHORT).show();
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

    private void editPolygon(final SellOrderDetail sellOrderDetail, String number_new, String number_difference)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", sellOrderDetail.getuid());
        params.addQueryStringParameter("contractid", sellOrderDetail.getcontractid());
        params.addQueryStringParameter("year", sellOrderDetail.getYear());
        params.addQueryStringParameter("uuid", sellOrderDetail.getUuid());
        params.addQueryStringParameter("batchTime", sellOrderDetail.getBatchTime());
        params.addQueryStringParameter("number_difference", number_difference);
        params.addQueryStringParameter("number_new", number_new);
        params.addQueryStringParameter("action", "editSellOrderDetail");
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
                        Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
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
//                Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_road.get(i).getUuid(), list_polygon_road.get(i).getNote());
//                list_Objects_road_centermarker.add(marker);
//            }
//
//        }
//    }


//    public void initMianPolygon()
//    {
//        list_Objects_mian_centermarker = new ArrayList<>();
//        list_Objects_mian = new ArrayList<>();
//        list_polygon_mian = SqliteDb.getMoreLayer_mian(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_polygon_mian.size(); i++)
//        {
//            LatLng latlng = new LatLng(Double.valueOf(list_polygon_mian.get(i).getLat()), Double.valueOf(list_polygon_mian.get(i).getLng()));
//            Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_ask), latlng, list_polygon_mian.get(i).getUuid(), list_polygon_mian.get(i).getNote());
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
//                Marker marker = addCustomMarker("normal", R.drawable.icon_house, getResources().getColor(R.color.bg_ask), latlng, list_polygon_house.get(i).getUuid(), list_polygon_house.get(i).getNote());
//                list_Objects_house.add(marker);
//            }
//
//        }
//    }

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
//                Marker marker = addCustomMarker("normal", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_job), latlng, list_polygon_point.get(i).getUuid(), list_polygon_point.get(i).getNote());
//                list_Objects_point.add(marker);
//            }
//
//        }
//    }

//    public void initGCDPolygon()
//    {
//        list_Objects_gcd = new ArrayList<>();
//        list_polygon_gcd = SqliteDb.getMoreLayer_gcd(getActivity(), commembertab.getuId());
//        for (int i = 0; i < list_polygon_gcd.size(); i++)
//        {
//            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_gcd.get(i).getUuid());
//            if (list != null && list.size() != 0)
//            {
//                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
//                Marker marker = addCustomMarker("gcd", R.drawable.umeng_socialize_follow_on, getResources().getColor(R.color.bg_job), latlng, list_polygon_gcd.get(i).getUuid(), list_polygon_gcd.get(i).getNote());
//                list_Objects_gcd.add(marker);
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
//                    PolygonBean polygonbean =(PolygonBean)obj;
                    showDialog_overlayInfo(note);
                } else if (type.equals("gcd"))
                {
                    PlantGcd plantgcd = (PlantGcd) obj;
                    ll_container.setVisibility(View.VISIBLE);
                    PictureScrollFragment pictureScrollFragment = new PictureScrollFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putStringArrayList("imgurl", (ArrayList<String>) plantgcd.getImgUrl());
                    pictureScrollFragment.setArguments(bundle1);
                    getFragmentManager().beginTransaction().replace(R.id.menu_container, pictureScrollFragment).commit();
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
                    showDialog_overlayInfo(sellOrderDetail.getparkname() + sellOrderDetail.getareaname() + sellOrderDetail.getcontractname() + "\n" + "计划出售" + sellOrderDetail.getplannumber() + "株" + "\n" + "实际出售" + sellOrderDetail.getactualnumber() + "株");
                } else if (type.equals("salefor"))
                {
                    polygon_needsale = bundle.getParcelable("bean");
                    SellOrderDetail sellOrderDetail = (SellOrderDetail) obj;
                    showDialog_OperateSalefor(sellOrderDetail, marker);
                } else if (type.equals("newsale"))
                {
                    SellOrderDetail sellOrderDetail = (SellOrderDetail) obj;
                    showDialog_OperateNewSale(sellOrderDetail, marker);
                }

                return false;
            }
        });
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

    private List<String> initURL()
    {
        List<String> list = new ArrayList<String>();
        list.add("http://www.bsnyy.com/cpzs/images/banana02.jpg");
        list.add("http://img.jdzj.com/UserDocument/2014d/MandyRH/Picture/201410892155.jpg");
        list.add("http://pic51.nipic.com/file/20141027/19372969_105919439000_2.jpg");
        list.add("http://pic19.nipic.com/20120223/7238648_182611701193_2.jpg");
        list.add("http://newsxml.cnool.net/newspic2010/2010/2010-4/2010-4-15/634069236900937500.jpg");
        return list;
    }

    public void showDialog_OperateSalefor(final SellOrderDetail sellOrderDetail, final Marker marker)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_operatepolygon_salefor, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(getActivity(), R.style.MyDialog, dialog_layout);
        Button btn_salewhole = (Button) dialog_layout.findViewById(R.id.btn_salewhole);
        Button btn_sale = (Button) dialog_layout.findViewById(R.id.btn_sale);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_overlayInfo(sellOrderDetail.getparkname() + sellOrderDetail.getareaname() + sellOrderDetail.getcontractname() + "\n" + "待出售" + sellOrderDetail.getplannumber() + "株");
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
                List<CoordinatesBean> list_coordinatesbean = sellOrderDetail.getCoordinatesBeanList();
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
                addWholeSellOrderDetailLayer(sellOrderDetail.getUuid());
//                SellOrderDetail sellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(getActivity(), uuid);
//                sellOrderDetail.setsaleid("");
//                sellOrderDetail.setType("newsale");
//                boolean issuccess = SqliteDb.salewhole(getActivity(), sellOrderDetail);
//                if (issuccess)
//                {
//                    Toast.makeText(getActivity(), "已添加到出售清单！", Toast.LENGTH_SHORT).show();
//                    reloadMap();
//                } else
//                {
//                    Toast.makeText(getActivity(), "出售失败！", Toast.LENGTH_SHORT).show();
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
//    private void showNeedPlanBoundary(List<CoordinatesBean> list_coordinates)
//    {
//        list_latlng_needplanboundary = new ArrayList<>();
//        for (int i = 0; i < list_coordinates.size(); i++)
//        {
//            LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
//            if (i == 0)
//            {
//                tencentMap.animateTo(latlng);
//            }
//            list_latlng_needplanboundary.add(latlng);
//        }
//        Polygon polygon = drawPolygon(100f, list_latlng_needplanboundary, R.color.black, 4, R.color.red);
//        Overlays.add(polygon);
////保存边界线
//        list_latlng_needplanline = new ArrayList<>();
//        LatLng prelatlng = new LatLng(Double.valueOf(list_coordinates.get(0).getLat()), Double.valueOf(list_coordinates.get(0).getLng()));
//        for (int i = 1; i < list_coordinates.size(); i++)//从1开始
//        {
//            LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
//            List<LatLng> list_lintpoint = new ArrayList<>();
//            list_lintpoint.add(prelatlng);
//            list_lintpoint.add(latlng);
//            prelatlng = latlng;
//            list_latlng_needplanline.add(list_lintpoint);
//        }
//        LatLng firstlatlng = new LatLng(Double.valueOf(list_coordinates.get(0).getLat()), Double.valueOf(list_coordinates.get(0).getLng()));
//        LatLng lastlatlng = new LatLng(Double.valueOf(list_coordinates.get(list_coordinates.size() - 1).getLat()), Double.valueOf(list_coordinates.get(list_coordinates.size() - 1).getLng()));
//        List<LatLng> list_lintpoint = new ArrayList<>();
//        list_lintpoint.add(lastlatlng);
//        list_lintpoint.add(firstlatlng);
//        list_latlng_needplanline.add(list_lintpoint);
//    }

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
//    public void initMapClickWhenPaint()
//    {
//        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
//        {
//            @Override
//            public void onMapClick(LatLng latLng)
//            {
//                PolylineOptions lineOpt = new PolylineOptions();
//                lineOpt.add(lastselect_latlng);
//                lineOpt.add(latLng);
//                Polyline line = tencentMap.addPolyline(lineOpt);
//                line.setColor(Color.argb(500, 255, 255, 255));
//                line.setWidth(4f);
//                Overlays.add(line);
//                list_Objects_divideline.add(line);
//
//                if (lastselect_latlng != null)
//                {
//                    Point p1 = mProjection.toScreenLocation(lastselect_latlng);
//                    Point p2 = mProjection.toScreenLocation(latLng);
//                    CusPoint cuspoint1 = new CusPoint(p1.x, p1.y);
//                    CusPoint cuspoint2 = new CusPoint(p2.x, p2.y);
//
//                    //判断交点数
//                    int number_crosspoint = 0;
//                    for (int i = 0; i < list_latlng_needplanline.size(); i++)
//                    {
//                        List<LatLng> list = list_latlng_needplanline.get(i);
//                        LatLng latlng0 = list.get(0);
//                        LatLng latlng1 = list.get(1);
//                        Point p5 = mProjection.toScreenLocation(latlng0);
//                        Point p6 = mProjection.toScreenLocation(latlng1);
//                        CusPoint cuspoint5 = new CusPoint(p5.x, p5.y);
//                        CusPoint cuspoint6 = new CusPoint(p6.x, p6.y);
//                        CusPoint crosspoint = utils.getCrossPoint(cuspoint1, cuspoint2, cuspoint5, cuspoint6);
//                        if (crosspoint != null)
//                        {
//                            number_crosspoint = number_crosspoint + 1;
//                        }
//                    }
//                    //仅仅切割一条边界，开始划分区域
//                    if (number_crosspoint == 1)
//                    {
//                        for (int i = 0; i < list_latlng_needplanline.size(); i++)
//                        {
//                            List<LatLng> list = list_latlng_needplanline.get(i);
//                            LatLng latlng0 = list.get(0);
//                            LatLng latlng1 = list.get(1);
//                            Point p5 = mProjection.toScreenLocation(latlng0);
//                            Point p6 = mProjection.toScreenLocation(latlng1);
//                            CusPoint cuspoint5 = new CusPoint(p5.x, p5.y);
//                            CusPoint cuspoint6 = new CusPoint(p6.x, p6.y);
//                            CusPoint crosspoint = utils.getCrossPoint(cuspoint1, cuspoint2, cuspoint5, cuspoint6);
//                            if (crosspoint != null)
//                            {
//                                Point cp = new Point();
//                                int x = Integer.valueOf(String.valueOf(crosspoint.x).substring(0, String.valueOf(crosspoint.x).indexOf(".")));
//                                int y = Integer.valueOf(String.valueOf(crosspoint.y).substring(0, String.valueOf(crosspoint.y).indexOf(".")));
//                                cp.set(x, y);
//                                LatLng latlng_crosspoint = mProjection.fromScreenLocation(cp);
//                                if (list_latlng_firstline == null)
//                                {
//                                    list_latlng_firstline = list;
//                                    pos_line1 = i;
//                                    touchLatlng1 = latlng_crosspoint;
//                                    isInner = true;
//                                } else
//                                {
//                                    list_latlng_secondline = list;
//                                    pos_line2 = i;
//                                    touchLatlng2 = latlng_crosspoint;
//                                    isInner = false;
//                                    divideArea(pos_line1, pos_line2, list_latlng_firstline, list_latlng_secondline, touchLatlng1, touchLatlng2);
//                                }
//                                break;
//                            }
//                        }
//                    } else if (number_crosspoint > 1)
//                    {
//                        Toast.makeText(getActivity(), "错误操作！请不要一次切割多条边界！", Toast.LENGTH_SHORT).show();
//                        //撤销该次操作，并重置参数
//                        tencentMap.removeOverlay(line);
//                        list_Objects_divideline.remove(line);
//                        Overlays.remove(line);
//                        lastselect_latlng = list_latlng_pick.get(list_latlng_pick.size() - 1);
//                        return;
//                    } else
//                    {
//                    }
//
//                }
//                lastselect_latlng = latLng;//放在后面
//                if (isInner)//放在后面
//                {
//                    list_latlng_pick.add(latLng);
//                }
//
//            }
//        });
//    }

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

    public void showDialog_addsaleinfo(final LatLng centerlatLng, final List<LatLng> list_latlng_selectpart, final List<LatLng> list_latlng_notselectpart)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_addsaleifo, null);
        customDialog_addSaleInInfo = new CustomDialog_AddSaleInInfo(getActivity(), R.style.MyDialog, dialog_layout);
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
        SellOrderDetail.setType("newsale");
        SellOrderDetail.setYear(utils.getYear());
//        SqliteDb.save(getActivity(), SellOrderDetail);
        sellOrderDetail_select = SellOrderDetail;
        for (int i = 0; i < list_select.size(); i++)
        {
            CoordinatesBean coordinatesBean = new CoordinatesBean();
            coordinatesBean.setLat(String.valueOf(list_select.get(i).getLatitude()));
            coordinatesBean.setLng(String.valueOf(list_select.get(i).getLongitude()));
            coordinatesBean.setUuid(uuid_sale);
            coordinatesBean.setRegistime(utils.getTime());
            list_coordinate_select.add(coordinatesBean);
//            SqliteDb.save(getActivity(), coordinatesBean);
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
                SellOrderDetail_notsale.setYear(utils.getYear());
                sellOrderDetail_notselect = SellOrderDetail_notsale;
//                SqliteDb.save(getActivity(), SellOrderDetail_notsale);
                for (int i = 0; i < list_notselect.size(); i++)
                {
                    CoordinatesBean coordinatesBean = new CoordinatesBean();
                    coordinatesBean.setLat(String.valueOf(list_notselect.get(i).getLatitude()));
                    coordinatesBean.setLng(String.valueOf(list_notselect.get(i).getLongitude()));
                    coordinatesBean.setUuid(uuid_notsale);
                    coordinatesBean.setRegistime(utils.getTime());
                    list_coordinate_notselect.add(coordinatesBean);
//                    SqliteDb.save(getActivity(), coordinatesBean);
                }
//                addCustomMarker("salefor", R.drawable.umeng_socialize_follow_on, R.color.white, centerlatlng, uuid_notsale, String.valueOf(Integer.valueOf(polygon_needsale.getplannumber()) - Integer.valueOf(salenumber)));
//                boolean issuccess = SqliteDb.deleteSaleForInfo(getActivity(), polygon_needsale.getUuid());
//                if (issuccess)
//                {
//                    Toast.makeText(getActivity(), "区域选择成功！", Toast.LENGTH_SHORT).show();
//                } else
//                {
//                    Toast.makeText(getActivity(), "区域选择失败！", Toast.LENGTH_SHORT).show();
//                }

                List<CoordinatesBean> CoordinatesBeanLists = new ArrayList<>();
                List<SellOrderDetail> SellOrderDetailList = new ArrayList<>();
                SellOrderDetailList.add(sellOrderDetail_select);
                SellOrderDetailList.add(sellOrderDetail_notselect);
                CoordinatesBeanLists.addAll(list_coordinate_select);
                CoordinatesBeanLists.addAll(list_coordinate_notselect);
                StringBuilder builder = new StringBuilder();
                builder.append("{\"SellOrderDetailList\": ");
                builder.append(JSON.toJSONString(SellOrderDetailList));
                builder.append(", \"CoordinatesBeanLists\": ");
                builder.append(JSON.toJSONString(CoordinatesBeanLists));
                builder.append("} ");
                addPlanMap(builder.toString());
            }
        });

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
        initShoppingCart();//初始化购物车清单
        initSaleData();//初始化销售数据
        initBreakoffData();//初始化断蕾数据
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
                boolean issuccess = SqliteDb.deleteShoppingcartByBatchtime(getActivity(), commembertab.getuId(), batchTime);
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

    public void showDialog_deletetip_salein(final SellOrderDetail sellOrderDetail, final Marker marker)
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
                deletePolygon(sellOrderDetail);
//                sellOrderDetail.setsaleid("");
//                sellOrderDetail.setType("salefor");
//                boolean issuccess = SqliteDb.deleteSaleInInfo(getActivity(), sellOrderDetail);
//                if (issuccess)
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

    public void showDialog_OperateShoppingcart(final List<SellOrderDetail> list_newSale)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_operateshoppingcart, null);
        customdialog_operatepolygon = new CustomDialog_OperatePolygon(getActivity(), R.style.MyDialog, dialog_layout);
        Button btn_see = (Button) dialog_layout.findViewById(R.id.btn_see);
        Button btn_createorder = (Button) dialog_layout.findViewById(R.id.btn_createorder);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        Button btn_delete = (Button) dialog_layout.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
//                showDialog_deletetip_shoppingcart();
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
        btn_createorder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_operatepolygon.dismiss();
                if (list_newSale != null && list_newSale.size() > 0)
                {
                    Intent intent = new Intent(getActivity(), CreateOrder_.class);
                    intent.putExtra("batchtime", batchTime);
                    intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list_newSale);
                    startActivity(intent);
                } else
                {
                    Toast.makeText(getActivity(), "请先添加出售区域", Toast.LENGTH_SHORT).show();
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

    public void showDialog_OperateSalein(final SellOrderDetail sellOrderDetail, final Marker marker)
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
                showDialog_deletetip_salein(sellOrderDetail, marker);
            }
        });
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

    public void showDialog_OperateNewSale(final SellOrderDetail sellOrderDetail, final Marker marker)
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
                showDialog_deletetip_salein(sellOrderDetail, marker);
            }
        });
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_overlayInfo(sellOrderDetail.getparkname() + sellOrderDetail.getareaname() + sellOrderDetail.getcontractname() + "\n" + "拟售" + sellOrderDetail.getplannumber() + "株");
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
//    public void showDialog_GCDoverlayInfo(final String note)
//    {
//        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_showgcdpolygonifo, null);
//        customDialog_overlayInfo = new CustomDialog_OverlayInfo(getActivity(), R.style.MyDialog, dialog_layout);
//        TextView tv_note = (TextView) dialog_layout.findViewById(R.id.tv_note);
//        Button btn_close = (Button) dialog_layout.findViewById(R.id.btn_close);
//        btn_close.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                customDialog_overlayInfo.dismiss();
//            }
//        });
//        if (note != null && !note.equals(""))
//        {
//            tv_note.setText(note);
//        }
//        customDialog_overlayInfo.show();
//    }

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
                int number_difference = Integer.valueOf(sellOrderDetail.getplannumber()) - Integer.valueOf(et_note.getText().toString());
//                sellOrderDetail.setplannumber(et_note.getText().toString());
//                sellOrderDetail.setstatus("0");
//                sellOrderDetail.setisSoldOut("0");
//                boolean issuccess = SqliteDb.editSellOrderDetail_salein(getActivity(), sellOrderDetail, number_difference);
//                if (issuccess)
//                {
//                    Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
//                    reloadMap();
//                } else
//                {
//                    Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
//                }
                editPolygon(sellOrderDetail, et_note.getText().toString(), String.valueOf(number_difference));
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
//        pw_batch.setAnimationStyle(R.style.topintopout);
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

                reloadMap();

//                initSaleData();//初始化销售数据
//                initBreakoffData();//初始化断蕾数据
//                initMarkerClickListener();
//                initMapLongClickListener();
//                initMapClickListener();
//                initMapCameraChangeListener();
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

    private void getGcdList()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getGCDListByUid");
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
                            addCustomMarker(listNewData.get(i), "gcd", R.drawable.ic_flag, R.color.bg_text, latlng, listNewData.get(i).getId(), listNewData.get(i).getPlantgcdName());
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

    private void getPlanMap(final String witcharea, String parkId, String areaid, String contractid, String type, String xxzt)
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("parkId", parkId);
        params.addQueryStringParameter("areaid", areaid);
        params.addQueryStringParameter("contractid", contractid);
        params.addQueryStringParameter("type", type);
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
                    listNewData = new ArrayList<PolygonBean>();
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PolygonBean.class);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            PolygonBean polygonBean = listNewData.get(i);
                            if (witcharea.equals("park"))
                            {
                                if (polygonBean != null)
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(polygonBean.getLat()), Double.valueOf(polygonBean.getLng()));
                                    Marker marker = addCustomMarker(polygonBean, "normal", R.drawable.ic_flag_park, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getNote());
                                    marker.setVisible(false);
                                    list_Marker_park.add(marker);
                                    List<CoordinatesBean> list_park = SqliteDb.getPoints(getActivity(), polygonBean.getUuid());
                                    if (list_park != null && list_park.size() != 0)
                                    {
                                        initBoundary(getResources().getColor(R.color.transparent), 0f, list_park, 0, R.color.transparent);
                                    }

                                }
                            } else if (witcharea.equals("area"))
                            {
                                if (polygonBean != null)
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(polygonBean.getLat()), Double.valueOf(polygonBean.getLng()));
                                    Marker marker = addCustomMarker(polygonBean, "normal", R.drawable.ic_flag_area, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getNote());
                                    marker.setVisible(false);
                                    list_Marker_area.add(marker);
                                    List<CoordinatesBean> list_area = SqliteDb.getPoints(getActivity(), polygonBean.getUuid());
                                    if (list_area != null && list_area.size() != 0)
                                    {
                                        initBoundary(getResources().getColor(R.color.transparent), 0f, list_area, 2, R.color.bg_text);
                                    }

                                }
                            } else if (witcharea.equals("contract"))
                            {
                                if (polygonBean != null)
                                {
                                    LatLng latlng = new LatLng(Double.valueOf(polygonBean.getLat()), Double.valueOf(polygonBean.getLng()));
                                    Marker marker = addCustomMarker(polygonBean, "normal", R.drawable.ic_flag_contract, getResources().getColor(R.color.white), latlng, polygonBean.getUuid(), polygonBean.getNote());
                                    marker.setVisible(false);
                                    list_Marker_contract.add(marker);
                                    List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), polygonBean.getUuid());
                                    if (list_contract != null && list_contract.size() != 0)
                                    {
                                        initBoundary(Color.argb(150, 144, 144, 144), 0f, list_contract, 2, R.color.bg_text);
                                    }
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

    private void addWholeSellOrderDetailLayer(String uuid)
    {
        pb_upload.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "addWholeSellOrderDetailLayer");
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
                    if (result.getAffectedRows() > 0)
                    {
                        Toast.makeText(getActivity(), "已添加到出售清单！", Toast.LENGTH_SHORT).show();
                        reloadMap();
                    } else
                    {
                        Toast.makeText(getActivity(), "出售失败！", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    pb_upload.setVisibility(View.GONE);
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
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
        params.addQueryStringParameter("action", "addSellOrderDetailMap");
        params.addQueryStringParameter("uuid", polygon_needsale.getUuid());
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
//                        lastselect_latlng = null;
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
}
