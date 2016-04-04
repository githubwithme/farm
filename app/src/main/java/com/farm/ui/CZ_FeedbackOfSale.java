package com.farm.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.farm.R;
import com.farm.adapter.BatchList_Adapter;
import com.farm.adapter.DL_ZS_Adapter;
import com.farm.adapter.Department_Adapter;
import com.farm.adapter.Operation_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchOfProduct;
import com.farm.bean.BreakOff;
import com.farm.bean.CoordinatesBean;
import com.farm.bean.DepartmentBean;
import com.farm.bean.Gps;
import com.farm.bean.Points;
import com.farm.bean.PolygonBean;
import com.farm.bean.Result;
import com.farm.bean.SellOrder;
import com.farm.bean.SellOrderDetail;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@EFragment
public class CZ_FeedbackOfSale extends Fragment implements TencentLocationListener, View.OnClickListener
{
    int[] fillcolor_park;
    int[] fillcolor_area;
    int[] fillcolor_contract;
    int[] ic_breakoff;
    List<BreakOff> list_breakoff;
    List<BatchOfProduct> list_BatchOfProduct;
    List<SellOrder> list_SellOrder;
    List<PolygonBean> list_polygon_road;
    List<PolygonBean> list_polygon_house;
    List<PolygonBean> list_polygon_point;
    List<PolygonBean> list_polygon_line;
    List<PolygonBean> list_polygon_mian;
    List<PolygonBean> list_polygon_park;
    List<PolygonBean> list_polygon_area;
    List<PolygonBean> list_polygon_contract;

    List<Polyline> list_Objects_road;
    List<Marker> list_Objects_road_centermarker;
    List<Marker> list_Objects_breakoff;
    List<Marker> list_Objects_sale;
    List<Marker> list_Objects_house;
    List<Marker> list_Objects_point;
    List<Polyline> list_Objects_line;
    List<Marker> list_Objects_line_centermarker;
    List<Marker> list_Objects_mian_centermarker;
    List<Polygon> list_Objects_mian;
    List<Polygon> list_Objects_park;
    List<Polygon> list_Objects_area;
    List<Polygon> list_Objects_contract;
    UiSettings uiSettings;
    boolean isfirstcomplete = true;
    PolygonBean polygonBean_needPlan;
    LatLng currentPoint;
    String drawerType = "";
    Polygon parkpolygon;
    DepartmentBean departmentselected;
    Polygon polygon_complete;
    List<String> list_operation;
    int intervalNumber = 0;//marker间隔数
    List<DepartmentBean> list_department;
    List<parktab> list_parktab;
    List<areatab> list_areatab;
    List<areatab> list_areatab_all = new ArrayList<>();
    List<contractTab> list_contractTab;
    List<contractTab> list_contractTab_all = new ArrayList<>();
    boolean isend_select = false;
    boolean isend_notselect = false;
    List<Polygon> list_polygon_all;
    PolygonBean currentPolygonbean_select;
    List<Polygon> list_contractplanpolygon;
    List<PolygonBean> list_contractplanpolygonbean;
    List<CoordinatesBean> list_CoordinatesBean_currentPolygon;
    List<List<CoordinatesBean>> list_polygon_allCoordinatesBean;
    List<Polyline> list_Polyline;
    List<LatLng> list_LatLng_boundaryselect;
    List<LatLng> list_LatLng_boundarynotselect;
    List<LatLng> list_LatLng_inboundary = new ArrayList<>();
    List<CoordinatesBean> list_coordinatesbean;
    List<Marker> list_mark;//边界上的marker集合
    List<Marker> list_centermark;//边界上的marker集合
    List<Marker> list_mark_inboundary;
    List<LinearLayout> list_ll_second;
    List<LinearLayout> list_ll_first;
    List<LinearLayout> list_ll_third;
    List<Marker> list_Marker_second;
    List<Marker> list_Marker_first;
    List<Marker> list_Marker_third;
    List<Marker> list_Marker_orders;
    List<Marker> list_Marker_park;
    List<Marker> list_Marker_area;
    List<Marker> list_Marker_contract;
    List<SellOrderDetail> list_SellOrderDetail;
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
    BatchList_Adapter batchlist_adapter;
    Department_Adapter department_adapter;
    Operation_Adapter operation_adapter;
    commembertab commembertab;
    ListView lv_zs;
    ListView lv_batch;
    List<Polygon> list_polygon;
    LatLng latlng_clickpostion;
    PopupWindow pw_command;
    PopupWindow pw_batch;
    View pv_command;
    View pv_batch;
    Button btn_sure;
    CheckBox cb_sfsw;
    Button btn_cancle;
    Button btn_close;
    TextView tv_note;
    TextView tv_ssqy;
    TextView tv_lng;
    TextView tv_lat;
    RelativeLayout rl_getlocation;
    LinearLayout ll_locationinfo;
    EditText et_note;
    EditText et_polygonnote;
    EditText et_numofbreakoff;
    EditText et_time;
    EditText et_dlzs;
    CustomDialog_EditPolygonInfo customdialog_editpolygoninfor;
    CustomDialog_EditDLInfor customdialog_editdlinfor;
    CustomDialog_EditDLInfor showDialog_pickpointinfo;
    CustomDialog_OperatePolygon customdialog_operatepolygon;
    boolean isStart = false;
    //    List<Object> area_Overlays = new ArrayList<Object>();
    LatLng lastlatLng = null;
    LatLng lastlatLng_cx = null;
    LatLng prelatLng = null;
    LatLng prelatLng_drawerparklayer = null;
    int error;
    LatLng location_latLng = new LatLng(24.430833, 113.298611);// 初始化定位
    private List<Object> Overlays;
    private Projection mProjection;
    Marker marker;
    Marker currentpointmarker;
    TencentMap tencentMap;

    List<LatLng> pointsList = new ArrayList<>();
    List<LatLng> listlatlng_park = new ArrayList<>();
    List<LatLng> listlatlng_cx = new ArrayList<>();
    @ViewById
    MapView mapview;
    @ViewById
    Button btn_showlayer;
    @ViewById
    TextView tv_tip;
    @ViewById
    Button btn_yx;

    @ViewById
    Button btn_complete;
    @ViewById
    TextView tv_noproducttip;
    @ViewById
    LinearLayout ll_sm;
    @ViewById
    View line;
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
    LinearLayout ll_first;
    @ViewById
    LinearLayout ll_second;
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
            for (int i = 0; i < list_Marker_first.size(); i++)
            {
                list_Marker_first.get(i).setVisible(false);
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
            for (int i = 0; i < list_Marker_second.size(); i++)
            {
                list_Marker_second.get(i).setVisible(false);
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
            for (int i = 0; i < list_Marker_third.size(); i++)
            {
                list_Marker_third.get(i).setVisible(false);
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
    void btn_complete()
    {
        btn_complete.setVisibility(View.GONE);
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("请在所画的区域内选取一个中心点");
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {
                if (parkpolygon.contains(latlng))
                {
                    String uuid_polygon = java.util.UUID.randomUUID().toString();
                    PolygonBean polygonBean = new PolygonBean();
                    polygonBean.setLat(String.valueOf(latlng.getLatitude()));
                    polygonBean.setLng(String.valueOf(latlng.getLongitude()));
                    polygonBean.setNumofplant("10000");
                    polygonBean.setType("farm_boundary");
                    polygonBean.setUid("60");
                    polygonBean.setparkId(departmentselected.getParkid());
                    polygonBean.setparkName(departmentselected.getParkname());
                    polygonBean.setUuid(uuid_polygon);
                    polygonBean.setAreaId("");
                    polygonBean.setareaName("");
                    polygonBean.setContractid("");
                    polygonBean.setContractname("");
                    polygonBean.setBatchid("");
                    polygonBean.setCoordinatestime(utils.getTime());
                    polygonBean.setRegistime(utils.getTime());
                    polygonBean.setWeightofplant("400000");
                    polygonBean.setSaleid("");
                    polygonBean.setOrders("");
                    polygonBean.setXxzt("0");
                    polygonBean.setNote(departmentselected.getParkname());
                    SqliteDb.save(getActivity(), polygonBean);
                    for (int i = 0; i < listlatlng_park.size(); i++)
                    {
                        CoordinatesBean coordinatesBean = new CoordinatesBean();
                        coordinatesBean.setLat(String.valueOf(listlatlng_park.get(i).getLatitude()));
                        coordinatesBean.setLng(String.valueOf(listlatlng_park.get(i).getLongitude()));
                        coordinatesBean.setNumofplant("");
                        coordinatesBean.setType("");
                        coordinatesBean.setUid("");
                        coordinatesBean.setparkId("");
                        coordinatesBean.setparkName("");
                        coordinatesBean.setUuid(uuid_polygon);
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
                    tv_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("你已成功画选一个区域！");
                    listlatlng_park = new ArrayList<>();
                    reloadMap();
                    btn_complete.setVisibility(View.GONE);
                } else
                {
                    tv_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("请在所画的区域内选取一个中心点");
                }
            }
        });

    }


    public void showDialog_editnewsaleinfo(final SellOrderDetail sellOrderDetail, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_polygonifo, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        et_polygonnote = (EditText) dialog_layout.findViewById(R.id.et_note);
        et_polygonnote.setText(sellOrderDetail.getplannumber());
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
                tencentMap.removeOverlay(marker);
                LatLng latlng = new LatLng(Double.valueOf(sellOrderDetail.getPlanlat()), Double.valueOf(sellOrderDetail.getplanlng()));
                addCustomMarker_newsale(R.drawable.ic_newsale, R.color.bg_ask, latlng, sellOrderDetail.getUuid(), sellOrderDetail.getparkname() + sellOrderDetail.getareaname() + sellOrderDetail.getcontractname() + "\n" + "出售" + et_polygonnote.getText().toString() + "株");
                for (int i = 0; i < list_SellOrderDetail.size(); i++)
                {
                    if (list_SellOrderDetail.get(i).getUuid().equals(sellOrderDetail.getUuid()))
                    {
                        list_SellOrderDetail.get(i).setplannumber(et_polygonnote.getText().toString());
                        break;
                    }
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

    public void showDialog_editsaleininfo(final SellOrderDetail sellOrderDetail, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_feedbacksale, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        et_polygonnote = (EditText) dialog_layout.findViewById(R.id.et_note);
        et_polygonnote.setText(sellOrderDetail.getplannumber());
        cb_sfsw = (CheckBox) dialog_layout.findViewById(R.id.cb_sfsw);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
                String issoldout="0";
                if (cb_sfsw.isSelected())
                {
                    issoldout="1";
                }
                sellOrderDetail.setactualnumber(et_polygonnote.getText().toString());
                sellOrderDetail.setstatus("1");
                sellOrderDetail.setactualnumber(et_polygonnote.getText().toString());
                sellOrderDetail.setisSoldOut(issoldout);
                boolean issuccess = SqliteDb.feedBackSellOrderDetail(getActivity(), sellOrderDetail);
                if (issuccess)
                {
                    tencentMap.removeOverlay(marker);
                    LatLng latlng = new LatLng(Double.valueOf(sellOrderDetail.getPlanlat()), Double.valueOf(sellOrderDetail.getplanlng()));
                    addCustomMarker_newsale(R.drawable.ic_newsale, R.color.bg_ask, latlng, sellOrderDetail.getUuid(), sellOrderDetail.getparkname() + sellOrderDetail.getareaname() + sellOrderDetail.getcontractname() + "\n" + "出售" + et_polygonnote.getText().toString() + "株");
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
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }

    public void showDialog_editpointinfo(final PolygonBean polygonbean)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_polygonifo, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
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
                boolean issuccess = SqliteDb.editPolygoninfo(getActivity(), polygonbean);
                if (issuccess)
                {
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
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }


    public void initSaleLayer()
    {
        initMarkOnclick();
        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            SellOrderDetail sellorderdetail = list_SellOrderDetail.get(i);
            LatLng latlng = new LatLng(Double.valueOf(sellorderdetail.getPlanlat()), Double.valueOf(sellorderdetail.getplanlng()));
            addCustomMarker_newsale(R.drawable.ic_newsale, R.color.bg_ask, latlng, sellorderdetail.getUuid(), sellorderdetail.getparkname() + sellorderdetail.getareaname() + sellorderdetail.getcontractname() + "\n" + "出售" + sellorderdetail.getplannumber() + "株");
        }
    }


    public void showDialog_salenote(final String note)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_showpolygonifo, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
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

    public void showDialog_breakoffnote(final String note)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_showpolygonifo, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
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

    public void showDialog_polygonnote(final String note)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_showpolygonifo, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
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

    public void showDialog_deletetip_salein(final String uuid, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_deletetip, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
                boolean issuccess = SqliteDb.deleteSellOrderDetailByUuid(getActivity(), uuid);
                if (issuccess)
                {
                    Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                    tencentMap.removeOverlay(marker);
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
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }

    public void showDialog_deletetip_newsale(final String uuid, final Marker marker)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_deletetip, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
                for (int i = 0; i < list_SellOrderDetail.size(); i++)
                {
                    if (list_SellOrderDetail.get(i).getUuid().equals(uuid))
                    {
                        list_SellOrderDetail.remove(i);
                        Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                        tencentMap.removeOverlay(marker);
                        break;
                    }
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

    public void showDialog_deletetip_breakoff(final String uuid)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_deletetip, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
                boolean issuccess = SqliteDb.deleteSellOrderDetailByUuid(getActivity(), uuid);
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
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }

    public void showDialog_deletetip(final String uuid)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_deletetip, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
                boolean issuccess = SqliteDb.deleteplanPolygon(getActivity(), uuid);
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
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
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
                    boolean isexistpark = SqliteDb.isexistpark(getActivity(), list_parktab.get(i).getid());
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
                            boolean isexistarea = SqliteDb.isexistarea(getActivity(), list_parktab.get(i).getid(), list_areatab.get(j).getid());
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
                                    boolean isexistcontract = SqliteDb.isexistcontract(getActivity(), list_parktab.get(i).getid(), list_areatab.get(j).getid(), list_contractTab.get(k).getid());
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

    public void showDialog_SelectOperation()
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_selectoperation, null);
        customdialog_editpolygoninfor = new CustomDialog_EditPolygonInfo(getActivity(), R.style.MyDialog, dialog_layout);
        customdialog_editpolygoninfor.setCanceledOnTouchOutside(false);
        customdialog_editpolygoninfor.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                customdialog_editpolygoninfor.dismiss();
                reloadMap();
            }
        });
        ListView lv_department = (ListView) dialog_layout.findViewById(R.id.lv_department);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editpolygoninfor.dismiss();
                reloadMap();
            }
        });
        list_operation = new ArrayList<>();
        JSONArray jsonArray = FileHelper.getAssetsJsonArray(getActivity(), "operation");
        for (int i = 0; i < jsonArray.size(); i++)
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
                String operation = list_operation.get(position);
                if (operation.equals("断蕾"))
                {
                    drawerType = "断蕾";
                    tv_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("请在地图内选取断蕾的位置");
                    tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
                    {
                        @Override
                        public void onMapClick(LatLng latlng)
                        {
                            Overlays.remove(currentpointmarker);
                            currentPoint = latlng;
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_breakoff);
                            Bitmap bitmap = utils.drawable2Bitmap(drawable);
                            currentpointmarker = tencentMap.addMarker(new MarkerOptions().position(latlng).title(latlng.getLatitude() + "," + latlng.getLongitude()).icon(new BitmapDescriptor(bitmap)));
                        }
                    });
                }
                customdialog_editpolygoninfor.dismiss();
            }
        });
        customdialog_editpolygoninfor.show();
    }

    public void drawerPolygon()
    {
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
                line.setColor(getActivity().getResources().getColor(R.color.black));
                line.setWidth(4f);
                Overlays.add(line);

                Overlays.remove(parkpolygon);
                parkpolygon = drawPolygon(listlatlng_park, R.color.bg_yellow);
                Overlays.add(parkpolygon);
            }
        });
    }


    public void savePolygonInfo(LatLng centerlatlng, final List<LatLng> list_select, final List<LatLng> list_notselect)
    {
        String orders = list_coordinatesbean.get(0).getOrders();
        if (orders.equals(""))
        {
            orders = "0";
        }
        String order_new = String.valueOf(Integer.valueOf(orders) + 1);
        for (int i = 0; i < list_select.size(); i++)
        {
            saveCoordinatesBean("farm_boundary", "0", "0", order_new, list_select.get(i), departmentselected);
        }
        for (int i = 0; i < list_notselect.size(); i++)
        {
            saveCoordinatesBean("farm_boundary_free", "0", "0", order_new, list_notselect.get(i), departmentselected);
        }
        saveCoordinatesBean("farm_boundary", "0", "1", order_new, centerlatlng, departmentselected);
    }

    public void savedividedPolygonInfo(LatLng centerlatlng, final List<LatLng> list_select, final List<LatLng> list_notselect)
    {
        String type = polygonBean_needPlan.getType();
        String orders = "";
        String order_new = "";
        if (type.equals("farm_boundary"))
        {
            orders = "0";
        } else if (type.equals("farm_boundary_free"))
        {
            orders = polygonBean_needPlan.getOrders();
        }
        order_new = String.valueOf(Integer.valueOf(orders) + 1);

        if (departmentselected.getType().equals("园区"))
        {

        } else if (departmentselected.getType().equals("片区"))
        {
            //已选择区域
            String uuid_polygon = java.util.UUID.randomUUID().toString();
            PolygonBean polygonBean = new PolygonBean();
            polygonBean.setLat(String.valueOf(centerlatlng.getLatitude()));
            polygonBean.setLng(String.valueOf(centerlatlng.getLongitude()));
            polygonBean.setNumofplant("");
            polygonBean.setType("farm_boundary");
            polygonBean.setUid("60");
            polygonBean.setparkId(departmentselected.getParkid());
            polygonBean.setparkName(departmentselected.getParkname());
            polygonBean.setUuid(uuid_polygon);
            polygonBean.setAreaId(departmentselected.getAreaid());
            polygonBean.setareaName(departmentselected.getAreaname());
            polygonBean.setContractid("");//空
            polygonBean.setContractname("");
            polygonBean.setBatchid("");
            polygonBean.setCoordinatestime(utils.getTime());
            polygonBean.setRegistime(utils.getTime());
            polygonBean.setWeightofplant("");
            polygonBean.setSaleid("");
            polygonBean.setOrders("");
            polygonBean.setXxzt("0");
            polygonBean.setOtherhalf("");
            polygonBean.setNote(departmentselected.getParkname() + departmentselected.getAreaname() + departmentselected.getContractname());
            SqliteDb.save(getActivity(), polygonBean);
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
                coordinatesBean.setUuid(uuid_polygon);
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
            //未选择区域
            String uuid_notselectpolygon = java.util.UUID.randomUUID().toString();
            PolygonBean polygonBean_notselect = new PolygonBean();
            polygonBean_notselect.setLat(String.valueOf(centerlatlng.getLatitude()));
            polygonBean_notselect.setLng(String.valueOf(centerlatlng.getLongitude()));
            polygonBean_notselect.setNumofplant("");
            polygonBean_notselect.setType("farm_boundary_free");
            polygonBean_notselect.setUid("60");
            polygonBean_notselect.setparkId(departmentselected.getParkid());
            polygonBean_notselect.setparkName(departmentselected.getParkname());
            polygonBean_notselect.setUuid(uuid_notselectpolygon);
            polygonBean_notselect.setAreaId("");//画选片区，list_notselect则只能为园区的未规划图，所以为空
            polygonBean_notselect.setareaName("");//画选片区，list_notselect则只能为园区的未规划图，所以为空
            polygonBean_notselect.setContractid("");
            polygonBean_notselect.setContractname("");
            polygonBean_notselect.setBatchid("");
            polygonBean_notselect.setCoordinatestime(utils.getTime());
            polygonBean_notselect.setRegistime(utils.getTime());
            polygonBean_notselect.setWeightofplant("");
            polygonBean_notselect.setSaleid("");
            polygonBean_notselect.setOrders(order_new);
            polygonBean_notselect.setXxzt("0");
            polygonBean_notselect.setOtherhalf(uuid_polygon);
            polygonBean_notselect.setNote(departmentselected.getParkname() + departmentselected.getAreaname() + departmentselected.getContractname());
            SqliteDb.save(getActivity(), polygonBean_notselect);
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
                coordinatesBean.setUuid(uuid_notselectpolygon);
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
        } else if (departmentselected.getType().equals("承包区"))
        {
            //已选择区域
            String uuid_polygon = java.util.UUID.randomUUID().toString();
            PolygonBean polygonBean = new PolygonBean();
            polygonBean.setLat(String.valueOf(centerlatlng.getLatitude()));
            polygonBean.setLng(String.valueOf(centerlatlng.getLongitude()));
            polygonBean.setNumofplant("");
            polygonBean.setType("farm_boundary");
            polygonBean.setUid("60");
            polygonBean.setparkId(departmentselected.getParkid());
            polygonBean.setparkName(departmentselected.getParkname());
            polygonBean.setUuid(uuid_polygon);
            polygonBean.setAreaId(departmentselected.getAreaid());
            polygonBean.setareaName(departmentselected.getAreaname());
            polygonBean.setContractid(departmentselected.getContractid());
            polygonBean.setContractname(departmentselected.getContractname());
            polygonBean.setBatchid("");
            polygonBean.setCoordinatestime(utils.getTime());
            polygonBean.setRegistime(utils.getTime());
            polygonBean.setWeightofplant("");
            polygonBean.setSaleid("");
            polygonBean.setOrders("");
            polygonBean.setXxzt("0");
            polygonBean.setOtherhalf("");
            polygonBean.setNote(departmentselected.getParkname() + departmentselected.getAreaname() + departmentselected.getContractname());
            SqliteDb.save(getActivity(), polygonBean);
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
                coordinatesBean.setUuid(uuid_polygon);
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
            //未选择区域
            String uuid_notselectpolygon = java.util.UUID.randomUUID().toString();
            PolygonBean polygonBean_notselect = new PolygonBean();
            polygonBean_notselect.setLat(String.valueOf(centerlatlng.getLatitude()));
            polygonBean_notselect.setLng(String.valueOf(centerlatlng.getLongitude()));
            polygonBean_notselect.setNumofplant("");
            polygonBean_notselect.setType("farm_boundary_free");
            polygonBean_notselect.setUid("60");
            polygonBean_notselect.setparkId(departmentselected.getParkid());
            polygonBean_notselect.setparkName(departmentselected.getParkname());
            polygonBean_notselect.setUuid(uuid_notselectpolygon);
            polygonBean_notselect.setAreaId(departmentselected.getAreaid());
            polygonBean_notselect.setareaName(departmentselected.getAreaname());
            polygonBean_notselect.setContractid("");//画选承包区，list_notselect则只能为片区的未规划图，所以为空
            polygonBean_notselect.setContractname("");
            polygonBean_notselect.setBatchid("");
            polygonBean_notselect.setCoordinatestime(utils.getTime());
            polygonBean_notselect.setRegistime(utils.getTime());
            polygonBean_notselect.setWeightofplant("");
            polygonBean_notselect.setSaleid("");
            polygonBean_notselect.setOrders(order_new);
            polygonBean_notselect.setXxzt("0");
            polygonBean_notselect.setOtherhalf(uuid_polygon);
            polygonBean_notselect.setNote(departmentselected.getParkname() + departmentselected.getAreaname() + departmentselected.getContractname());
            SqliteDb.save(getActivity(), polygonBean_notselect);
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
                coordinatesBean.setUuid(uuid_notselectpolygon);
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
        }

        reloadMap();
    }

    public void saveCoordinatesBean(String type, String isInnerPoint, String isCenterPoint, String orders, LatLng latlng, DepartmentBean departmentbean)
    {
        String uuid = java.util.UUID.randomUUID().toString();
        CoordinatesBean coordinatesBean = new CoordinatesBean();
        coordinatesBean.setLat(String.valueOf(latlng.getLatitude()));
        coordinatesBean.setLng(String.valueOf(latlng.getLongitude()));
        coordinatesBean.setNumofplant("10000");
        coordinatesBean.setType(type);
        coordinatesBean.setUid("60");
        coordinatesBean.setparkId(departmentbean.getParkid());
        coordinatesBean.setparkName("");
        coordinatesBean.setUuid(uuid);
        coordinatesBean.setAreaId(departmentbean.getAreaid());
        coordinatesBean.setareaName("");
        coordinatesBean.setContractid(departmentbean.getContractid());
        coordinatesBean.setContractname("");
        coordinatesBean.setBatchid("");
        coordinatesBean.setCoordinatestime(utils.getTime());
        coordinatesBean.setRegistime(utils.getTime());
        coordinatesBean.setWeightofplant("400000");
        coordinatesBean.setSaleid("");
        coordinatesBean.setOrders(orders);
        coordinatesBean.setIsInnerPoint(isInnerPoint);
        coordinatesBean.setIsCenterPoint(isCenterPoint);
        SqliteDb.save(getActivity(), coordinatesBean);
    }

    public void showDialog_EditPolygonInfo(final List<LatLng> list_select, final List<LatLng> list_notselect)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_editpolygoninfo, null);
        customdialog_editpolygoninfor = new CustomDialog_EditPolygonInfo(getActivity(), R.style.MyDialog, dialog_layout);
        ListView lv_department = (ListView) dialog_layout.findViewById(R.id.lv_department);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        list_department = getDepartment(getActivity());
        department_adapter = new Department_Adapter(getActivity(), list_department);
        lv_department.setAdapter(department_adapter);
        lv_department.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                customdialog_editpolygoninfor.dismiss();
                DepartmentBean d = list_department.get(position);
                String orders = list_coordinatesbean.get(0).getOrders();
                if (orders.equals(""))
                {
                    orders = "0";
                }
                String order_new = String.valueOf(Integer.valueOf(orders) + 1);
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

    public void showDialog_OperatenewSale(final String uuid, final Marker marker)
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
                showDialog_deletetip_newsale(uuid, marker);
            }
        });
        btn_see.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for (int i = 0; i < list_SellOrderDetail.size(); i++)
                {
                    if (list_SellOrderDetail.get(i).getUuid().equals(uuid))
                    {
                        SellOrderDetail SellOrderDetail = list_SellOrderDetail.get(i);
                        showDialog_salenote(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname() + "\n" + "出售" + SellOrderDetail.getplannumber() + "株");
                        break;
                    }
                }
                customdialog_operatepolygon.dismiss();
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for (int i = 0; i < list_SellOrderDetail.size(); i++)
                {
                    if (list_SellOrderDetail.get(i).getUuid().equals(uuid))
                    {
                        SellOrderDetail sellOrderDetail = list_SellOrderDetail.get(i);
                        showDialog_editnewsaleinfo(sellOrderDetail, marker);
                        break;
                    }
                }
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
                showDialog_salenote(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname() + "\n" + "出售" + SellOrderDetail.getplannumber() + "株");
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

    public void showDialog_OperateSaleout(final String uuid, final Marker marker)
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
                showDialog_salenote(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname() + "\n" + "出售" + SellOrderDetail.getactualnumber() + "株");
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.cz_feedbackofsale, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(getActivity());
        locationManager.setCoordinateType(1);//设置坐标系为gcj02坐标，1为GCJ02，0为WGS84
        error = locationManager.requestLocationUpdates(request, this);
        return rootView;
    }

    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        mapview.onResume();
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapview.onDestroy();
    }

    public void settile()
    {
//        TileOverlay tileOverlay = null;
//        UrlTileProvider u = null;
//        Tile tile = new Tile("","","");
//        TileProvider tileProvider = null;
//        tileProvider.getTile();
//        TileOverlayOptions tileOverlayOptions=new TileOverlayOptions();
//        tileOverlayOptions.tileProvider(tileProvider);
//        tencentMap.addTileOverlay(tileOverlayOptions);
    }

    @AfterViews
    void afterOncreate()
    {
//        SqliteDb.initPark(getActivity());
//        SqliteDb.initArea(getActivity());
//        SqliteDb.initContract(getActivity());
//        SqliteDb.getTemp1(getActivity());

        list_BatchOfProduct = SqliteDb.getBatchOfProductByuid(getActivity(), "60");
        if (list_BatchOfProduct.size() == 0)
        {
            tv_noproducttip.setVisibility(View.VISIBLE);
            tv_noproducttip.setText("暂无产品需要销售");
        } else
        {
            fillcolor_park = new int[]{Color.argb(150, 218, 112, 214), Color.argb(150, 106, 90, 205), Color.argb(150, 135, 206, 250), Color.argb(150, 0, 128, 128), Color.argb(150, 127, 255, 170), Color.argb(150, 245, 255, 250), Color.argb(150, 60, 179, 113), Color.argb(150, 34, 139, 34), Color.argb(150, 127, 255, 0), Color.argb(150, 255, 69, 0)};
            fillcolor_area = new int[]{Color.argb(150, 218, 112, 214), Color.argb(150, 106, 90, 205), Color.argb(150, 135, 206, 250), Color.argb(150, 0, 128, 128), Color.argb(150, 127, 255, 170), Color.argb(150, 245, 255, 250), Color.argb(150, 60, 179, 113), Color.argb(150, 34, 139, 34), Color.argb(150, 127, 255, 0), Color.argb(150, 255, 69, 0)};
            fillcolor_contract = new int[]{Color.argb(150, 218, 112, 214), Color.argb(150, 106, 90, 205), Color.argb(150, 135, 206, 250), Color.argb(150, 0, 128, 128), Color.argb(150, 127, 255, 170), Color.argb(150, 245, 255, 250), Color.argb(150, 60, 179, 113), Color.argb(150, 34, 139, 34), Color.argb(150, 127, 255, 0), Color.argb(150, 255, 69, 0)};
            ic_breakoff = new int[]{R.drawable.ic_breakoff_black, R.drawable.ic_breakoff_blue1, R.drawable.ic_breakoff_end, R.drawable.ic_breakoff_start, R.drawable.ic_breakoff_red, R.drawable.ic_breakoff, R.drawable.ic_breakoff_gray};
            tencentMap = mapview.getMap();
            tencentMap.setZoom(13);
            uiSettings = mapview.getUiSettings();
            tencentMap.setSatelliteEnabled(true);
            mProjection = mapview.getProjection();

            Overlays = new ArrayList<Object>();
            list_polygon = new ArrayList<Polygon>();
            list_polygon_pq = new ArrayList<Polygon>();
            list_point_pq = new ArrayList<>();
            list_contractplanpolygonbean = new ArrayList<>();
            list_contractplanpolygon = new ArrayList<>();
            list_polygon_all = new ArrayList<>();
            list_centermark = new ArrayList<>();
            list_ll_second = new ArrayList<>();
            list_ll_first = new ArrayList<>();
            list_ll_third = new ArrayList<>();
            list_Marker_first = new ArrayList<>();
            list_Marker_second = new ArrayList<>();
            list_Marker_third = new ArrayList<>();
            list_Marker_orders = new ArrayList<>();

            list_Marker_park = new ArrayList<>();
            list_Marker_area = new ArrayList<>();
            list_Marker_contract = new ArrayList<>();

            list_SellOrderDetail = new ArrayList<>();
            list_SellOrder = new ArrayList<>();

            list_Objects_sale = new ArrayList<>();

            list_breakoff = new ArrayList<>();


            list_polygon_road = new ArrayList<>();
            list_polygon_house = new ArrayList<>();
            list_polygon_point = new ArrayList<>();
            list_polygon_line = new ArrayList<>();
            list_polygon_mian = new ArrayList<>();
            list_polygon_park = new ArrayList<>();
            list_polygon_area = new ArrayList<>();

            list_Objects_road = new ArrayList<>();
            list_Objects_road_centermarker = new ArrayList<>();
            list_Objects_breakoff = new ArrayList<>();
            list_Objects_house = new ArrayList<>();
            list_Objects_point = new ArrayList<>();
            list_Objects_line = new ArrayList<>();
            list_Objects_line_centermarker = new ArrayList<>();
            list_Objects_mian_centermarker = new ArrayList<>();
            list_Objects_mian = new ArrayList<>();
            list_Objects_park = new ArrayList<>();
            list_Objects_area = new ArrayList<>();
            list_Objects_contract = new ArrayList<>();


            cb_house.setChecked(true);
            cb_road.setChecked(true);
            cb_point.setChecked(true);
            cb_line.setChecked(true);
            cb_mian.setChecked(true);
            cb_park.setChecked(true);
            cb_area.setChecked(true);
            cb_contract.setChecked(true);

            cb_parkdata.setChecked(true);
            cb_areadata.setChecked(true);
            cb_contractdata.setChecked(true);

            list_polygon_allCoordinatesBean = new ArrayList<>();
            map = new HashMap<>();
            if (!utils.isOPen(getActivity()))
            {
                utils.openGPSSettings(getActivity());
            }

            initMapData();
            initMarkOnclick();
            initMapCameraChangeListener();
            initMapOnclickListening();
        }
    }

    public void initMapData()
    {
        List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), "60");
        for (int i = 0; i < list_parktab.size(); i++)//每个园区
        {
            PolygonBean polygonBean_park = SqliteDb.getLayer_park(getActivity(), list_parktab.get(i).getid());
            if (polygonBean_park != null)
            {
                LatLng latlng = new LatLng(Double.valueOf(polygonBean_park.getLat()), Double.valueOf(polygonBean_park.getLng()));
                Marker marker=addCustomMarkerWithFlag(R.drawable.ic_flag_park,getResources().getColor(R.color.white), latlng, polygonBean_park.getUuid(), polygonBean_park.getNote());
                list_Marker_park.add(marker);
                List<CoordinatesBean> list_park = SqliteDb.getPoints(getActivity(), polygonBean_park.getUuid());
                if (list_park != null && list_park.size() != 0)
                {
                    initBoundary(fillcolor_park[i], 0f, list_park);
//                    initBoundaryLine(Color.argb(1000, 57, 72, 61), 0f, list_park);
                }

            }

            List<areatab> list_areatab = SqliteDb.getareatab(getActivity(), list_parktab.get(i).getid());
            for (int k = 0; k < list_areatab.size(); k++)//每个片区
            {
                PolygonBean polygonBean_area = SqliteDb.getLayer_area(getActivity(), list_parktab.get(i).getid(), list_areatab.get(k).getid());
                if (polygonBean_area != null)
                {
//                    setCenterPointInfo(polygonBean_area);
                    LatLng latlng = new LatLng(Double.valueOf(polygonBean_area.getLat()), Double.valueOf(polygonBean_area.getLng()));
                    Marker marker=addCustomMarkerWithFlag(R.drawable.ic_flag_area,getResources().getColor(R.color.white), latlng, polygonBean_area.getUuid(), polygonBean_area.getNote());
                    list_Marker_area.add(marker);
                    List<CoordinatesBean> list_area = SqliteDb.getPoints(getActivity(), polygonBean_area.getUuid());
                    if (list_area != null && list_area.size() != 0)
                    {

                        int[] random = utils.randomInt(0, 10, 1);
                        initBoundary(fillcolor_area[k], 100f, list_area);
                        initBoundaryLine(Color.argb(1000, 57, 72, 61), 0f, list_area);
                    }

                }


                List<contractTab> list_contractTab = SqliteDb.getcontracttab(getActivity(), list_areatab.get(k).getid());
                for (int m = 0; m < list_contractTab.size(); m++)//每个承包区
                {
                    PolygonBean polygonBean_contract = SqliteDb.getLayer_contract(getActivity(), list_parktab.get(i).getid(), list_areatab.get(k).getid(), list_contractTab.get(m).getid());
                    if (polygonBean_contract != null)
                    {
                        LatLng latlng = new LatLng(Double.valueOf(polygonBean_contract.getLat()), Double.valueOf(polygonBean_contract.getLng()));
                        Marker marker=addCustomMarkerWithFlag(R.drawable.ic_flag_contract,getResources().getColor(R.color.white), latlng, polygonBean_contract.getUuid(), polygonBean_contract.getNote());
                        list_Marker_contract.add(marker);
                        List<CoordinatesBean> list_contract = SqliteDb.getPoints(getActivity(), polygonBean_contract.getUuid());
                        if (list_contract != null && list_contract.size() != 0)
                        {
                            list_contractplanpolygonbean.add(polygonBean_contract);
                            int[] random = utils.randomInt(0, 10, 1);
                            Polygon p = initBoundary(fillcolor_contract[m], 200f, list_contract);
                            list_contractplanpolygon.add(p);
                            initBoundaryLine(Color.argb(1000, 57, 72, 61), 0f, list_contract);
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
        //显示断蕾情况
        initBreakOff(list_BatchOfProduct.get(0).getBatchTime());
        //显示销售情况
        initSale(list_BatchOfProduct.get(0).getBatchTime());

    }

    public void initSale(String batchtime)
    {
        list_SellOrder = SqliteDb.getSellOrder(getActivity(), "60", batchtime);
        for (int i = 0; i < list_SellOrder.size(); i++)
        {
            List<SellOrderDetail> list_SellOrderDetail = SqliteDb.getSellOrderDetailBySaleId(getActivity(), list_SellOrder.get(i).getUuid());
            if (list_SellOrderDetail != null && list_SellOrderDetail.size() != 0)
            {
                for (int j = 0; j < list_SellOrderDetail.size(); j++)
                {
                    LatLng latlng = new LatLng(Double.valueOf(list_SellOrderDetail.get(j).getPlanlat()), Double.valueOf(list_SellOrderDetail.get(j).getplanlng()));
                    if (list_SellOrderDetail.get(j).getstatus().equals("0"))//进行中
                    {
                        Marker marker = addCustomMarker_SaleIn(R.drawable.ic_salein, getResources().getColor(R.color.bg_ask), latlng, list_SellOrderDetail.get(j).getUuid(), list_SellOrderDetail.get(j).getplannumber() + "株");
                        list_Objects_sale.add(marker);
                    } else if (list_SellOrderDetail.get(j).getstatus().equals("1"))//已结束
                    {
                        Marker marker = addCustomMarker_SaleOut(R.drawable.ic_saleout, getResources().getColor(R.color.bg_green), latlng, list_SellOrderDetail.get(j).getUuid(), list_SellOrderDetail.get(j).getactualnumber() + "株");
                        list_Objects_sale.add(marker);
                    }
                }
            }
        }


    }

    public void initBreakOff(String batchtime)
    {
        list_breakoff = SqliteDb.getBreakoffByBatchTime(getActivity(), "60", batchtime);
        if (list_breakoff != null && list_breakoff.size() != 0)
        {
            for (int j = 0; j < list_breakoff.size(); j++)
            {
                LatLng latlng = new LatLng(Double.valueOf(list_breakoff.get(j).getLat()), Double.valueOf(list_breakoff.get(j).getLng()));
                if (list_breakoff.get(j).getStatus().equals("0"))//起点
                {
                    Marker marker = addCustomMarker_BreakOff(R.drawable.ic_breakoff_start, getResources().getColor(R.color.bg_ask), latlng, list_breakoff.get(j).getUuid(), list_breakoff.get(j).getBreakofftime().substring(5, 10) + "/" + list_breakoff.get(j).getnumberofbreakoff());
                    list_Objects_breakoff.add(marker);
                } else if (list_breakoff.get(j).getStatus().equals("1"))//终点
                {
                    Marker marker = addCustomMarker_BreakOff(R.drawable.ic_breakoff_end, getResources().getColor(R.color.bg_ask), latlng, list_breakoff.get(j).getUuid(), list_breakoff.get(j).getBreakofftime().substring(5, 10) + "/" + list_breakoff.get(j).getnumberofbreakoff());
                    list_Objects_breakoff.add(marker);
                } else
                {
                    Marker marker = addCustomMarker_BreakOff(R.drawable.ic_breakoff, getResources().getColor(R.color.bg_ask), latlng, list_breakoff.get(j).getUuid(), list_breakoff.get(j).getBreakofftime().substring(5, 10) + "/" + list_breakoff.get(j).getnumberofbreakoff());
                    list_Objects_breakoff.add(marker);
                }
//                if (i < ic_breakoff.length)
//                {
//                    Marker marker = addCustomMarker_BreakOff(ic_breakoff[i], getResources().getColor(R.color.bg_ask), latlng, list_breakoff.get(j).getUuid(), list_breakoff.get(j).getBreakofftime().substring(5, 10) + "/" + list_breakoff.get(j).getnumberofbreakoff());
//                    list_Objects_breakoff.add(marker);
//                } else
//                {
//                    Marker marker = addCustomMarker_BreakOff(R.drawable.ic_breakoff_spare, getResources().getColor(R.color.bg_ask), latlng, list_breakoff.get(j).getUuid(), list_breakoff.get(j).getBreakofftime().substring(5, 10) + "/" + list_breakoff.get(j).getnumberofbreakoff());
//                    list_Objects_breakoff.add(marker);
//                }


            }


        }

    }

    public void initHousePolygon()
    {
        list_polygon_house = SqliteDb.getMoreLayer_house(getActivity(), "60");
        for (int i = 0; i < list_polygon_house.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_house.get(i).getUuid());
            if (list != null && list.size() != 0)
            {
                LatLng latlng = new LatLng(Double.valueOf(list.get(0).getLat()), Double.valueOf(list.get(0).getLng()));
                Marker marker = addCustomMarkerWithHouse(getResources().getColor(R.color.bg_ask), latlng, list_polygon_house.get(i).getUuid(), list_polygon_house.get(i).getNote());
                list_Objects_house.add(marker);
            }

        }
    }

    public void initRoadPolygon()
    {
        list_polygon_road = SqliteDb.getMoreLayer_road(getActivity(), "60");
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

    public void initParkPolygon()
    {

    }

    public void initAreaPolygon()
    {

    }

    public void initContractPolygon()
    {

    }

    public void initPointPolygon()
    {
        list_polygon_point = SqliteDb.getMoreLayer_point(getActivity(), "60");
        for (int i = 0; i < list_polygon_point.size(); i++)
        {
            List<CoordinatesBean> list = SqliteDb.getPoints(getActivity(), list_polygon_point.get(i).getUuid());
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
        list_polygon_line = SqliteDb.getMoreLayer_line(getActivity(), "60");
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
                    Marker marker = addCustomMarker(getResources().getColor(R.color.bg_sq), list_latlang.get(0), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
                    list_Objects_line_centermarker.add(marker);
                } else
                {
                    Marker marker = addCustomMarker(getResources().getColor(R.color.bg_sq), list_latlang.get(halfsize), list_polygon_line.get(i).getUuid(), list_polygon_line.get(i).getNote());
                    list_Objects_line_centermarker.add(marker);
                }

            }

        }
    }

    public void initMianPolygon()
    {
        list_polygon_mian = SqliteDb.getMoreLayer_mian(getActivity(), "60");
        for (int i = 0; i < list_polygon_mian.size(); i++)
        {
            LatLng latlng = new LatLng(Double.valueOf(list_polygon_mian.get(i).getLat()), Double.valueOf(list_polygon_mian.get(i).getLng()));
            Marker marker = addCustomMarker(getResources().getColor(R.color.bg_ask), latlng, list_polygon_mian.get(i).getUuid(), list_polygon_mian.get(i).getNote());
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
//                initBoundaryLine(Color.argb(180, 70, 101, 10), 0f, list_mian);
                Polygon polygon = drawPolygon(list_LatLng, Color.argb(180, 70, 101, 10));
                polygon.setZIndex(0f);
                Overlays.add(polygon);
                list_Objects_mian.add(polygon);
            }

        }
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

            if (drawerType.equals("采点"))
            {
                tencentMap.animateTo(location_latLng);
                if (showDialog_pickpointinfo.isShowing())
                {
                    rl_getlocation.setVisibility(View.GONE);
                    ll_locationinfo.setVisibility(View.VISIBLE);
                }
            } else if (drawerType.equals("采线"))
            {
                tencentMap.animateTo(location_latLng);
                PolylineOptions lineOpt = new PolylineOptions();
                lineOpt.add(lastlatLng_cx);
                lastlatLng_cx = location_latLng;
                lineOpt.add(location_latLng);
                Polyline line = tencentMap.addPolyline(lineOpt);
                line.setGeodesic(true);
                line.setColor(this.getResources().getColor(R.color.black));
                line.setWidth(4f);
                Overlays.add(line);
                listlatlng_cx.add(location_latLng);
            } else if (drawerType.equals("采面"))
            {
                tencentMap.animateTo(location_latLng);
                listlatlng_park.add(location_latLng);
//                PolylineOptions lineOpt = new PolylineOptions();
//                lineOpt.add(prelatLng_drawerparklayer);
//                prelatLng_drawerparklayer = location_latLng;
//                lineOpt.add(location_latLng);
//                Polyline line = tencentMap.addPolyline(lineOpt);
//                line.setColor(getActivity().getResources().getColor(R.color.black));
//                line.setWidth(4f);
//                Overlays.add(line);
                Overlays.remove(parkpolygon);
                parkpolygon = drawPolygon(listlatlng_park, R.color.bg_yellow);
                Overlays.add(parkpolygon);
            }
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

    private Marker addFirstView(String qy, int breakoffnumber, int number_wdl, LatLng latLng, String uuid, String note)
    {
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        list_centermark.add(marker);
        marker.set2Top();
        View view = View.inflate(getActivity(), R.layout.addview_breakoffinfo, null);
        LinearLayout ll_first = (LinearLayout) view.findViewById(R.id.ll_first);
        TextView tv_ssqy = (TextView) view.findViewById(R.id.tv_ssqy);
        TextView tv_wd = (TextView) view.findViewById(R.id.tv_wd);
        TextView tv_yd = (TextView) view.findViewById(R.id.tv_yd);
        tv_ssqy.setText(qy);
        tv_wd.setText(String.valueOf(number_wdl));
        tv_yd.setText(String.valueOf(breakoffnumber));

        tv_ssqy.setHeight(40);
        tv_wd.setHeight(10);
        tv_yd.setHeight(20);
        marker.setMarkerView(view);
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid);
        bundle.putString("note", note);
        bundle.putString("type", "polygon");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addSecondView(String qy, int breakoffnumber, int number_wdl, LatLng latLng, String uuid, String note)
    {
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        list_centermark.add(marker);
        marker.set2Top();
        View view = View.inflate(getActivity(), R.layout.addview_breakoffinfo, null);
        LinearLayout ll_second = (LinearLayout) view.findViewById(R.id.ll_second);
        TextView tv_ssqy = (TextView) view.findViewById(R.id.tv_ssqy);
        TextView tv_wd = (TextView) view.findViewById(R.id.tv_wd);
        TextView tv_yd = (TextView) view.findViewById(R.id.tv_yd);
        tv_ssqy.setText(qy);
        tv_wd.setText(String.valueOf(number_wdl));
        tv_yd.setText(String.valueOf(breakoffnumber));
        tv_ssqy.setHeight(40);
        tv_wd.setHeight(10);
        tv_yd.setHeight(20);
        marker.setMarkerView(view);
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid);
        bundle.putString("note", note);
        bundle.putString("type", "polygon");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addThirdView(String qy, int breakoffnumber, int number_wdl, LatLng latLng, String uuid, String note)
    {
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        list_centermark.add(marker);
        marker.set2Top();
        View view = View.inflate(getActivity(), R.layout.addview_breakoffinfo, null);
        LinearLayout ll_third = (LinearLayout) view.findViewById(R.id.ll_third);
        TextView tv_ssqy = (TextView) view.findViewById(R.id.tv_ssqy);
        TextView tv_wd = (TextView) view.findViewById(R.id.tv_wd);
        TextView tv_yd = (TextView) view.findViewById(R.id.tv_yd);
        tv_ssqy.setText(qy);
        tv_wd.setText(String.valueOf(number_wdl));
        tv_yd.setText(String.valueOf(breakoffnumber));
        tv_ssqy.setHeight(40);
        tv_wd.setHeight(10);
        tv_yd.setHeight(20);
        marker.setMarkerView(view);
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid);
        bundle.putString("note", note);
        bundle.putString("type", "polygon");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addCustomMarkerWithFlag(int icon,int textcolor, LatLng latLng, String uuid, String note)
    {
        Drawable drawable = getResources().getDrawable(R.drawable.location1);
        Bitmap bitmap = utils.drawable2Bitmap(drawable);
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).icon(new BitmapDescriptor(bitmap)));
        list_centermark.add(marker);
        marker.set2Top();
        View view = View.inflate(getActivity(), R.layout.markerwithflag, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_note);
        View view_marker = (View) view.findViewById(R.id.view_marker);
        LinearLayout ll_second = (LinearLayout) view.findViewById(R.id.ll_second);
        LinearLayout ll_first = (LinearLayout) view.findViewById(R.id.ll_first);
        LinearLayout ll_third = (LinearLayout) view.findViewById(R.id.ll_third);
        view_marker.setBackgroundResource(icon);
        list_ll_first.add(ll_first);
        list_ll_second.add(ll_second);
        list_ll_third.add(ll_third);
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
        bundle.putString("type", "polygon");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addCustomMarker_newsale(int icon, int textcolor, LatLng latLng, String uuid, String note)
    {
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        list_centermark.add(marker);
        marker.set2Top();
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
        bundle.putString("type", "newsale");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addCustomMarker_SaleOut(int icon, int textcolor, LatLng latLng, String uuid, String note)
    {
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        list_centermark.add(marker);
        marker.set2Top();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.markerwithsale, null);
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
        bundle.putString("type", "saleout");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addCustomMarker_SaleIn(int icon, int textcolor, LatLng latLng, String uuid, String note)
    {
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        list_centermark.add(marker);
        marker.set2Top();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.markerwithsale, null);
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
        bundle.putString("type", "salein");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addCustomMarker_BreakOff(int icon, int textcolor, LatLng latLng, String uuid, String note)
    {
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng));
        list_centermark.add(marker);
        marker.set2Top();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.markerwithbreafoff, null);
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
        bundle.putString("type", "breakoff");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addCustomMarkerWithHouse(int textcolor, LatLng latLng, String uuid, String note)
    {
        Drawable drawable = getResources().getDrawable(R.drawable.location1);
        Bitmap bitmap = utils.drawable2Bitmap(drawable);
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).icon(new BitmapDescriptor(bitmap)));
        list_centermark.add(marker);
        marker.set2Top();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.markerwithhouse, null);
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
//        textView.setTag(latLng);
//        textView.setTag(R.id.tag_latlng, latLng);
//        textView.setTag(R.id.tag_uuid, uuid);
        marker.setMarkerView(view);
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid);
        bundle.putString("note", note);
        bundle.putString("type", "polygon");
        marker.setTag(bundle);
        return marker;
    }

    private Marker addCustomMarker(int textcolor, LatLng latLng, String uuid, String note)
    {
        Drawable drawable = getResources().getDrawable(R.drawable.location1);
        Bitmap bitmap = utils.drawable2Bitmap(drawable);
        marker = tencentMap.addMarker(new MarkerOptions().position(latLng).icon(new BitmapDescriptor(bitmap)));
        list_centermark.add(marker);
        marker.set2Top();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.markerinfo, null);
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
//        textView.setTag(latLng);
//        textView.setTag(R.id.tag_latlng, latLng);
//        textView.setTag(R.id.tag_uuid, uuid);
        marker.setMarkerView(view);
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid);
        bundle.putString("note", note);
        bundle.putString("type", "polygon");
        marker.setTag(bundle);
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
//        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
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
            line.setGeodesic(true);
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

                            for (int j = 0; j < list_LatLng_inboundary.size(); j++)
                            {
                                if (latlng.toString().equals(list_LatLng_inboundary.get(j).toString()))//最终点为内点
                                {
                                    if (latlng_two == null)
                                    {
                                        latlng_two = pointsList.get(pointsList.size() - 1);
                                    } else if (latlng_one == null)
                                    {
                                        latlng_one = pointsList.get(pointsList.size() - 1);
                                    }
                                }
                                if (j == list_LatLng_inboundary.size() - 1)//最终点为边界点
                                {
                                    if (latlng_two == null)
                                    {
                                        latlng_two = latlng;
                                    } else if (latlng_one == null)
                                    {
                                        latlng_one = latlng;
                                    }
                                }
                            }
                            tv_tip.setVisibility(View.GONE);
//                            btn_addorder.setText("添加断蕾区");
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
                                            currentmarker_pos = j + 1;
                                        }
                                        if ((list_coordinatesbean.get(j).getLat().equals(String.valueOf(ll.getLatitude())) && list_coordinatesbean.get(j).getLng().equals(String.valueOf(ll.getLongitude()))))
                                        {
                                            lastmarker_pos = j + 1;
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
                                            for (int k = list_coordinatesbean.size() - 1; k >= currentmarker_pos; k--)
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
//                            btn_addorder.setText("添加断蕾区");
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
                            latlng_two = latlng;
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
                        latlng_two = latlng;
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
                            currentmarker_pos = i + 1;
                        }
                        if ((list_coordinatesbean.get(i).getLat().equals(String.valueOf(ll.getLatitude())) && list_coordinatesbean.get(i).getLng().equals(String.valueOf(ll.getLongitude()))))
                        {
                            lastmarker_pos = i + 1;
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
//                            line.setColor(getActivity().getResources().getColor(R.color.black));
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
//                            line.setColor(getActivity().getResources().getColor(R.color.black));
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
//                    line.setColor(getActivity().getResources().getColor(R.color.black));
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
                currentmarker_pos = k + 1;
            }
            if ((list_coordinatesbean.get(k).getLat().equals(String.valueOf(latlng_two.getLatitude())) && list_coordinatesbean.get(k).getLng().equals(String.valueOf(latlng_two.getLongitude()))))
            {
                lastmarker_pos = k + 1;
            }
        }
        int max = 0;
        int min = 0;
        if (lastmarker_pos > currentmarker_pos)
        {
            max = lastmarker_pos;
            min = currentmarker_pos;
        } else
        {
            max = currentmarker_pos;
            min = lastmarker_pos;
        }
        for (int m = min; m <= max; m++)
        {
            LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(m).getLat()), Double.valueOf(list_coordinatesbean.get(m).getLng()));
            list_LatLng_boundaryselect.add(l);
        }
        for (int n = max; n < list_coordinatesbean.size(); n++)
        {
            LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(n).getLat()), Double.valueOf(list_coordinatesbean.get(n).getLng()));
            list_LatLng_boundarynotselect.add(l);
        }
        for (int n = 0; n <= min; n++)
        {
            LatLng l = new LatLng(Double.valueOf(list_coordinatesbean.get(n).getLat()), Double.valueOf(list_coordinatesbean.get(n).getLng()));
            list_LatLng_boundarynotselect.add(l);
        }

        if (list_LatLng_inboundary != null)
        {
            int pos_latlng_one_inboundarynotselect = 0;
            int pos_latlng_one_inboundaryselect = 0;
            int pos_latlng_two_inboundarynotselect = 0;
            int pos_latlng_two_inboundaryselect = 0;
            int pos_latlng_one_inpointsList = 0;
            int pos_latlng_two_inpointsList = 0;
            int pos_latlng_in__one_inpointsList = 0;
            int pos_latlng_in__two_inpointsList = 0;

            LatLng latlng_in__one = list_LatLng_inboundary.get(0);
            LatLng latlng_in__two = list_LatLng_inboundary.get(list_LatLng_inboundary.size() - 1);

            for (int j = 0; j < list_LatLng_boundarynotselect.size(); j++)//没选择区域
            {
                if (list_LatLng_boundarynotselect.get(j).toString().equals(latlng_one.toString()))
                {
                    pos_latlng_one_inboundarynotselect = j;
                }
                if (list_LatLng_boundarynotselect.get(j).toString().equals(latlng_two.toString()))
                {
                    pos_latlng_two_inboundarynotselect = j;
                }
            }
            for (int j = 0; j < list_LatLng_boundaryselect.size(); j++)//已选择区域
            {
                if (list_LatLng_boundaryselect.get(j).toString().equals(latlng_one.toString()))
                {
                    pos_latlng_one_inboundaryselect = j;
                }
                if (list_LatLng_boundaryselect.get(j).toString().equals(latlng_two.toString()))
                {
                    pos_latlng_two_inboundaryselect = j;
                }
            }

            for (int j = 0; j < pointsList.size(); j++)
            {
                if (pointsList.get(j).toString().equals(latlng_in__one.toString()))
                {
                    pos_latlng_in__one_inpointsList = j;
                }
                if (pointsList.get(j).toString().equals(latlng_in__two.toString()))
                {
                    pos_latlng_in__two_inpointsList = j;
                }
                if (pointsList.get(j).toString().equals(latlng_one.toString()))
                {
                    pos_latlng_one_inpointsList = j;
                }
                if (pointsList.get(j).toString().equals(latlng_two.toString()))
                {
                    pos_latlng_two_inpointsList = j;
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
            int pos_connectpoint[] = {pos_latlng_one_inpointsList, pos_latlng_two_inpointsList, pos_latlng_in__one_inpointsList, pos_latlng_in__two_inpointsList};

            Arrays.sort(pos_connectpoint);
            LatLng LatLng0 = pointsList.get(pos_connectpoint[0]);
            LatLng LatLng1 = pointsList.get(pos_connectpoint[1]);
            LatLng LatLng2 = pointsList.get(pos_connectpoint[2]);
            LatLng LatLng3 = pointsList.get(pos_connectpoint[3]);
//将已选取边界点和已选取内点相加
            if (!isend_select && list_LatLng_boundaryselect.get(list_LatLng_boundaryselect.size() - 2).toString().equals(LatLng0.toString()))
            {
                for (int k = 0; k < list_LatLng_boundaryselect.size(); k++)
                {
                    if (LatLng1.toString().equals(list_LatLng_boundaryselect.get(k).toString()))//不加LatLng1
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng2);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        }


                    } else
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng1);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        }

                    }
                }
            }
            if (!isend_select && list_LatLng_boundaryselect.get(list_LatLng_boundaryselect.size() - 2).toString().equals(LatLng1.toString()))
            {
                for (int k = 0; k < list_LatLng_boundaryselect.size(); k++)//判断是否已有点
                {
                    if (LatLng2.toString().equals(list_LatLng_boundaryselect.get(k).toString()))//不加LatLng2
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng3);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        }


                    } else
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng2);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        }

                    }
                }
            }
            if (!isend_select && list_LatLng_boundaryselect.get(list_LatLng_boundaryselect.size() - 2).toString().equals(LatLng2.toString()))
            {
                for (int k = 0; k < list_LatLng_boundaryselect.size(); k++)//判断是否已有点
                {
                    if (LatLng3.toString().equals(list_LatLng_boundaryselect.get(k).toString()))//不加LatLng3
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng0);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        }


                    } else
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng3);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        }

                    }
                }
            }
            if (!isend_select && list_LatLng_boundaryselect.get(list_LatLng_boundaryselect.size() - 2).toString().equals(LatLng3.toString()))
            {
                for (int k = 0; k < list_LatLng_boundaryselect.size(); k++)//判断是否已有点
                {
                    if (LatLng0.toString().equals(list_LatLng_boundaryselect.get(k).toString()))//不加LatLng0
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng1);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        }


                    } else
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng0);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundaryselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_select = true;
                            break;
                        }

                    }
                }
            }
            //将非选取点和已选取内点相加
            if (!isend_notselect && list_LatLng_boundarynotselect.get(list_LatLng_boundarynotselect.size() - 2).toString().equals(LatLng0.toString()))
            {
                for (int k = 0; k < list_LatLng_boundarynotselect.size(); k++)//判断是否已有点
                {
                    if (LatLng1.toString().equals(list_LatLng_boundarynotselect.get(k).toString()))//不加LatLng0
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng2);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        }


                    } else
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng1);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        }

                    }
                }
            }
            if (!isend_notselect && list_LatLng_boundarynotselect.get(list_LatLng_boundarynotselect.size() - 2).toString().equals(LatLng1.toString()))
            {
                for (int k = 0; k < list_LatLng_boundarynotselect.size(); k++)//判断是否已有点
                {
                    if (LatLng2.toString().equals(list_LatLng_boundarynotselect.get(k).toString()))//不加LatLng0
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng3);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        }


                    } else
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng2);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        }

                    }
                }
            }
            if (!isend_notselect && list_LatLng_boundarynotselect.get(list_LatLng_boundarynotselect.size() - 2).toString().equals(LatLng2.toString()))
            {
                for (int k = 0; k < list_LatLng_boundarynotselect.size(); k++)//判断是否已有点
                {
                    if (LatLng3.toString().equals(list_LatLng_boundarynotselect.get(k).toString()))//不加LatLng0
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng0);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        }


                    } else
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng3);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        }

                    }
                }
            }
            if (!isend_notselect && list_LatLng_boundarynotselect.get(list_LatLng_boundarynotselect.size() - 2).toString().equals(LatLng3.toString()))
            {
                for (int k = 0; k < list_LatLng_boundarynotselect.size(); k++)//判断是否已有点
                {
                    if (LatLng0.toString().equals(list_LatLng_boundarynotselect.get(k).toString()))//不加LatLng0
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng1);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        }


                    } else
                    {
                        int index = list_LatLng_inboundary.indexOf(LatLng0);
                        if (index == 0)//
                        {
                            for (int n = 0; n < list_LatLng_inboundary.size(); n++)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
                            break;
                        } else
                        {
                            for (int n = list_LatLng_inboundary.size() - 1; n >= 0; n--)
                            {
                                list_LatLng_boundarynotselect.add(list_LatLng_inboundary.get(n));
                            }
                            isend_notselect = true;
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

        polygon_complete = drawPolygon(list_LatLng_boundaryselect, R.color.bg_yellow);
        list_polygon.add(polygon_complete);
//        saveCoordinate(list_LatLng_boundaryselect, "13", "片区二号");
//        saveCoordinate(list_LatLng_boundarynotselect,"-2","未选区域");
        for (int m = 0; m < list_Polyline.size(); m++)
        {
            list_Polyline.get(m).remove();
        }
        for (int m = 0; m < list_mark.size(); m++)
        {
            list_mark.get(m).remove();
        }
        for (int m = 0; m < list_mark_inboundary.size(); m++)
        {
            list_mark_inboundary.get(m).remove();
        }
        isfirstcomplete = true;
        setMapClickListener_AfterDrawerPolygon(polygon_complete);
//        showDialog_EditPolygonInfo(list_LatLng_boundaryselect, list_LatLng_boundarynotselect);
        //重置数据
//        resetData();
    }

    private void setMapClickListener_AfterDrawerPolygon(final Polygon polygon)
    {
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {
                if (isfirstcomplete)
                {
                    isfirstcomplete = false;
                } else
                {
                    if (!polygon.contains(latlng))
                    {
                        tv_tip.setText("请在所画选区内选取点");
                        tv_tip.setBackgroundResource(R.color.bg_green);
                    } else
                    {
                        savedividedPolygonInfo(latlng, list_LatLng_boundaryselect, list_LatLng_boundarynotselect);
//                    savePolygonInfo(latlng, list_LatLng_boundaryselect, list_LatLng_boundarynotselect);
//                    showDialog_EditPolygonInfo(list_LatLng_boundaryselect, list_LatLng_boundarynotselect);
                    }
                }

            }
        });
    }

    private void dividePolygon()
    {
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("请在所选区域内选择一个中心点");
        tv_tip.setBackgroundResource(R.color.bg_yellow);
        LatLng a = latlng_one;
        LatLng b = latlng_two;
        if (latlng_one == null && latlng_two == null)
        {
            for (int i = 0; i < list_coordinatesbean.size(); i++)
            {
                LatLng latlng = new LatLng(Double.valueOf(list_coordinatesbean.get(i).getLat()), Double.valueOf(list_coordinatesbean.get(i).getLng()));
                list_LatLng_boundaryselect.add(latlng);
                list_LatLng_boundarynotselect.add(latlng);
            }
            setMapClickListener_AfterDrawerPolygon(polygon_select);
        } else
        {
            divide();
        }
    }

    private void resetData()
    {
        initMapAndParam();
//        initMapOnclick();
        initMapOnclickListening();
    }

    private void reloadMap()
    {
        tv_tip.setVisibility(View.GONE);
        tencentMap.clearAllOverlays();


        btn_complete.setVisibility(View.GONE);
        tv_tip.setVisibility(View.GONE);
        btn_showlayer.setVisibility(View.VISIBLE);
        btn_setting.setVisibility(View.VISIBLE);
        listlatlng_park = new ArrayList<>();
        prelatLng_drawerparklayer = null;

        list_SellOrderDetail = new ArrayList<>();

        list_BatchOfProduct = new ArrayList<>();

        list_Marker_first = new ArrayList<>();
        list_Marker_second = new ArrayList<>();
        list_Marker_third = new ArrayList<>();

        list_Polyline = new ArrayList<>();
        list_mark_inboundary = new ArrayList<>();
        pointsList = new ArrayList<>();
        list_polygon = new ArrayList<>();
        list_LatLng_boundarynotselect = new ArrayList<>();
        list_LatLng_boundaryselect = new ArrayList<>();
        list_LatLng_inboundary = new ArrayList<>();
        last_pos = 0;
        number_markerselect = 0;
        number_pointselect = 0;
        firstmarkerselect = true;
        prelatLng = null;

        btn_showlayer.setVisibility(View.VISIBLE);
        btn_setting.setVisibility(View.VISIBLE);
        drawerType = "";
        currentpointmarker = null;//点
        listlatlng_park = new ArrayList<>();//线、面
        prelatLng_drawerparklayer = null;
        //线、面


        initMapData();
        initMarkOnclick();
        initMapOnclickListening();
        initMapCameraChangeListener();
    }

    private void showThirdMarker()
    {
        if (list_Marker_third.size() > 0)
        {
            for (int i = 0; i < list_Marker_third.size(); i++)
            {
                list_Marker_third.get(i).setVisible(true);
            }
        } else
        {
            List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), "60");
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
                            int number_breakoff = SqliteDb.getAllNumberOfBreakoff_contract(getActivity(), polygonBean_contract.getparkId(), polygonBean_contract.getAreaId(), polygonBean_contract.getContractid());
                            int number_wdl = 20000;
                            LatLng latlng = new LatLng(Double.valueOf(polygonBean_contract.getLat()), Double.valueOf(polygonBean_contract.getLng()));
                            Marker marker = addThirdView(polygonBean_contract.getparkName() + polygonBean_contract.getareaName() + polygonBean_contract.getContractname(), number_breakoff, number_wdl, latlng, polygonBean_contract.getUuid(), polygonBean_contract.getNote() + "相关信息");
                            list_Marker_third.add(marker);
                        }
                    }
                }
            }
        }
    }

    private void showSecondMarker()
    {
        if (list_Marker_second.size() > 0)
        {
            for (int i = 0; i < list_Marker_second.size(); i++)
            {
                list_Marker_second.get(i).setVisible(true);
            }
        } else
        {
            List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), "60");
            for (int i = 0; i < list_parktab.size(); i++)//每个园区
            {
                List<areatab> list_areatab = SqliteDb.getareatab(getActivity(), list_parktab.get(i).getid());
                for (int k = 0; k < list_areatab.size(); k++)//每个片区
                {
                    PolygonBean polygonBean_area = SqliteDb.getLayer_area(getActivity(), list_parktab.get(i).getid(), list_areatab.get(k).getid());

                    if (polygonBean_area != null)
                    {
                        int number_breakoff = SqliteDb.getAllNumberOfBreakoff_area(getActivity(), polygonBean_area.getparkId(), polygonBean_area.getAreaId(), polygonBean_area.getContractid());
                        int number_wdl = 20000;
                        LatLng latlng = new LatLng(Double.valueOf(polygonBean_area.getLat()), Double.valueOf(polygonBean_area.getLng()));
                        Marker marker = addSecondView(polygonBean_area.getparkName() + polygonBean_area.getareaName(), number_breakoff, number_wdl, latlng, polygonBean_area.getUuid(), polygonBean_area.getNote() + "相关信息");
                        list_Marker_second.add(marker);
                    }
                }
            }
        }
    }

    private void showFirstMarker()
    {
        if (list_Marker_first.size() > 0)
        {
            for (int i = 0; i < list_Marker_first.size(); i++)
            {
                list_Marker_first.get(i).setVisible(true);
            }
        } else
        {
            List<parktab> list_parktab = SqliteDb.getparktab(getActivity(), "60");
            for (int i = 0; i < list_parktab.size(); i++)//每个园区
            {
                PolygonBean polygonBean_park = SqliteDb.getLayer_park(getActivity(), list_parktab.get(i).getid());

                if (polygonBean_park != null)
                {
                    int number_breakoff = SqliteDb.getAllNumberOfBreakoff_park(getActivity(), list_parktab.get(i).getid(), "", "");
                    int number_wdl = 20000;
                    LatLng latlng = new LatLng(Double.valueOf(polygonBean_park.getLat()), Double.valueOf(polygonBean_park.getLng()));
                    Marker marker = addFirstView(polygonBean_park.getparkName(), number_breakoff, number_wdl, latlng, polygonBean_park.getUuid(), polygonBean_park.getNote() + "相关信息");
                    list_Marker_first.add(marker);
                }
            }
        }
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

                        for (int i = 0; i < list_Marker_first.size(); i++)
                        {
                            list_Marker_first.get(i).setVisible(false);
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
                        for (int i = 0; i < list_Marker_second.size(); i++)
                        {
                            list_Marker_second.get(i).setVisible(false);
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
                        for (int i = 0; i < list_Marker_third.size(); i++)
                        {
                            list_Marker_third.get(i).setVisible(false);
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


    private void initMarkOnclick()
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
                if (type.equals("polygon"))
                {
                    PolygonBean polygonbean = SqliteDb.getLayerbyuuid(getActivity(), uuid);
                    showDialog_polygonnote(polygonbean.getNote());
                } else if (type.equals("breakoff"))
                {
                    BreakOff breakoff = SqliteDb.getbreakoffByuuid(getActivity(), uuid);
                    showDialog_breakoffnote(breakoff.getBreakofftime() + "断蕾" + breakoff.getnumberofbreakoff() + "株");
                } else if (type.equals("salein"))
                {
                    showDialog_OperateSalein(uuid, marker);
                } else if (type.equals("saleout"))
                {
                    showDialog_OperateSaleout(uuid, marker);
//                    SellOrderDetail SellOrderDetail = SqliteDb.getSellOrderDetailbyuuid(getActivity(), uuid);
//                    showDialog_salenote(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname() + "\n" + "出售" + SellOrderDetail.getactualnumber() + "株");
                } else if (type.equals("newsale"))
                {
                    showDialog_OperatenewSale(uuid, marker);
                }

                return false;
            }
        });
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
//                    Toast.makeText(getActivity(), "添加成功！", Toast.LENGTH_SHORT).show();
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

    private void setCenterPointInfo(PolygonBean polygonbean)
    {
        LatLng latlng = new LatLng(Double.valueOf(polygonbean.getLat()), Double.valueOf(polygonbean.getLng()));
//        Drawable drawable = getResources().getDrawable(icon);
//        Bitmap bitmap = utils.drawable2Bitmap(drawable);
        marker = tencentMap.addMarker(new MarkerOptions().position(latlng));
        marker.set2Top();
        TextView textView = new TextView(getActivity());
        textView.setText(polygonbean.getNote());
        textView.setTextColor(getResources().getColor(R.color.bg_text));
        textView.setTextSize(16);
        marker.setMarkerView(textView);
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "good", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBoundaryLine(int color, float z, List<CoordinatesBean> list_coordinates)
    {
        List<LatLng> list_AllLatLng = new ArrayList<>();
        for (int i = 0; i < list_coordinates.size(); i++)
        {
            LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(i).getLat()), Double.valueOf(list_coordinates.get(i).getLng()));
//            if (i == 0)
//            {
//                tencentMap.animateTo(latlng);
//            }
            list_AllLatLng.add(latlng);
        }
        LatLng latlng = new LatLng(Double.valueOf(list_coordinates.get(0).getLat()), Double.valueOf(list_coordinates.get(0).getLng()));
        list_AllLatLng.add(latlng);
        PolylineOptions lineOpt = new PolylineOptions();
        lineOpt.addAll(list_AllLatLng);
        Polyline line = tencentMap.addPolyline(lineOpt);
        line.setColor(color);
//        line.setDottedLine(true);
        line.setWidth(4f);
        Overlays.add(line);
    }

    private Polygon initBoundary(int color, float z, List<CoordinatesBean> list_coordinates)
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
//        int colors = 0;
//        if (list_coordinates.get(0).getAreaId().equals("10"))
//        {
//            colors = Color.argb(150, 4, 181, 0);
//        } else if (list_coordinates.get(0).getAreaId().equals("13"))
//        {
//            colors = Color.argb(150, 4, 181, 0);
//        } else if (list_coordinates.get(0).getAreaId().equals("14"))
//        {
//            colors = Color.argb(150, 4, 181, 0);
//        } else
//        {
//            colors = Color.argb(150, 4, 181, 0);
//        }
        Polygon polygon = drawPolygon(list_AllLatLng, color);
        polygon.setZIndex(z);
//        list_polygon_allCoordinatesBean.add(list_coordinates);
        Overlays.add(polygon);
        return polygon;
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
        list_CoordinatesBean_currentPolygon = list_coordinates;
        polygon_select = polygon;
        List<LatLng> list_single_polygon = polygon.getPoints();
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
            if (j == num)
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

    public void saveCoordinate(List<LatLng> list, String id, String name)
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
        list_LatLng_boundarynotselect = new ArrayList<>();
        list_LatLng_boundaryselect = new ArrayList<>();
        list_LatLng_inboundary = new ArrayList<>();
        last_pos = 0;
        number_markerselect = 0;
        number_pointselect = 0;
        firstmarkerselect = true;
        prelatLng = null;
//        polygon_select = null;


        isStart = true;
//        polygon_select = list_polygon_pq.get(0);
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("请在承包区内选取点");
//        btn_addorder.setText("确定");
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
                if (number_markerselect > 0)//只要边界上有一个marker被选中,而且区域内也已经选取点了，就不能再点击地图了，只能点击marker
                {
                    if (latlng_one == null)
                    {
                        latlng_one = pointsList.get(pointsList.size() - 1);
                    }
                }
                list_LatLng_inboundary.add(latlng);
                number_pointselect = number_pointselect + 1;
                Marker m = addMarker_Paint(0, latlng, R.drawable.location_start);
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

    public void initMapOnclick()
    {
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latlng)
            {
                for (int i = 0; i < list_polygon_all.size(); i++)
                {
                    Polygon polygon = list_polygon_all.get(i);
                    if (polygon.contains(latlng))
                    {
                        list_CoordinatesBean_currentPolygon = list_polygon_allCoordinatesBean.get(i);
                        polygon_select = polygon;
                        List<LatLng> list_single_polygon = polygon.getPoints();
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
                            if (j == num)
                            {
                                num = num + intervalNumber;
                                Marker marker = addMarker_Paint(list_mark.size(), ll, R.drawable.location_start);
                                list_mark.add(marker);
                                list_point_pq.add(ll);
                            }

                        }
//                        showDialog_OperatePolygon();
                        return;
                    }
                    if (i == list_polygon_all.size() - 1)
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