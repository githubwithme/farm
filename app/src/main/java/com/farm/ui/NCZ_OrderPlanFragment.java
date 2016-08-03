package com.farm.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_OrderPlan;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.AllType;
import com.farm.bean.OrderPlanBean;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomArrayAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/2/26.
 */
@EFragment
public class NCZ_OrderPlanFragment extends Fragment
{
    List<OrderPlanBean> listNewData = null;
    Adapter_OrderPlan adapter_orderPlan;
    String goodsName;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    Spinner provinceSpinner;
    @ViewById
    Spinner citySpinner;
    @ViewById
    Spinner countySpinner;
    String parkname = "";
    String parkId = "-1";
    String cpname = "-1";
    String cgsname = "";
    String cgsId = "-1";
    CustomArrayAdapter provinceAdapter = null;  //省级适配器
    CustomArrayAdapter cityAdapter = null;    //地级适配器
    CustomArrayAdapter countyAdapter = null;    //县级适配器
    List<AllType> listdata_cp = new ArrayList<AllType>();
    List<Purchaser> listData_CG = new ArrayList<Purchaser>();
    List<Wz_Storehouse> listpark = new ArrayList<Wz_Storehouse>();
    static private List<SellOrder_New> secletpark;
    static private List<SellOrder_New> secletchanpin;
    static private List<SellOrder_New> secletcgs;

    @AfterViews
    void afterOncreate()
    {
//      getBreakOffInfoOfContract();
        secletchanpin = new ArrayList<SellOrder_New>();
        secletcgs = new ArrayList<SellOrder_New>();
        secletpark = new ArrayList<SellOrder_New>();
        getchanpin();//产品
        getpurchaser();//采购商
        getlistdata();//园区
//        getNewSaleList_test();
        getOrderPlan();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_orderplanfragment_new, container, false);
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATEAllORDER);
        getActivity().registerReceiver(receiver_update, intentfilter_update);
        return rootView;
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
//            getAllOrders();
            getOrderPlan();
        }
    };



    private void getOrderPlan()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkid", parkId);
        params.addQueryStringParameter("productname", cpname);
        params.addQueryStringParameter("buyer", cgsId);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("status", "0");
        params.addQueryStringParameter("action", "NCZ_getOrderPlan");
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
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), OrderPlanBean.class);
                    adapter_orderPlan = new Adapter_OrderPlan(getActivity(), listNewData, AppContext.BROADCAST_UPDATEAllORDER, expandableListView);
                    expandableListView.setAdapter(adapter_orderPlan);
//                    for (int i = 0; i < listNewData.size(); i++)
//                    {
//                        expandableListView.expandGroup(i);//展开
//                    }
                    
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

    private void getchanpin()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getProduct");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<AllType> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), AllType.class);
                        AllType allType = new AllType();
                        allType.setId("-1");
                        allType.setProductName("全部产品");
                        listdata_cp.add(allType);
                        listdata_cp.addAll(listNewData);


                        String park[] = new String[listdata_cp.size()];
                        for (int i = 0; i < listdata_cp.size(); i++)
                        {
                            park[i] = listdata_cp.get(i).getProductName();
                        }
                        cityAdapter = new CustomArrayAdapter(getActivity(), park);
                        citySpinner.setAdapter(cityAdapter);
                        citySpinner.setSelection(0, true);  //默认选中第0个
                        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                            {

                                if (listdata_cp.get(i).getProductName().equals("全部产品"))
                                {
                                    cpname = "-1";


                                }else
                                {
                                    cpname = listdata_cp.get(i).getProductName();
                                }
                                getOrderPlan();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView)
                            {

                            }
                        });
                    } else
                    {
                        listNewData = new ArrayList<AllType>();
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
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");

            }
        });

    }

    //采购商
    private void getpurchaser()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getpurchaser");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Purchaser> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        if (result.getAffectedRows() != 0)
                        {
                            listNewData = JSON.parseArray(result.getRows().toJSONString(), Purchaser.class);
                            Purchaser purchaser = new Purchaser();
                            purchaser.setId("-1");
                            purchaser.setName("全部采购商");
                            listData_CG.add(purchaser);
                            for (int i = 0; i < listNewData.size(); i++)
                            {

                                if (listNewData.get(i).userType.equals("采购商"))
                                {
                                    listData_CG.add(listNewData.get(i));
                                }/* else if (listNewData.get(i).userType.equals("包装工头"))
                                {
                                    listData_BZ.add(listNewData.get(i));
                                } else
                                {
                                    listData_BY.add(listNewData.get(i));
                                }*/
                            }


                            String park[] = new String[listData_CG.size()];
                            for (int i = 0; i < listData_CG.size(); i++)
                            {
                                park[i] = listData_CG.get(i).getName();
                            }
                            countyAdapter = new CustomArrayAdapter(getActivity(), park);
                            countySpinner.setAdapter(countyAdapter);
                            countySpinner.setSelection(0, true);
                            countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                                {
                                    cgsname = listData_CG.get(i).getName();
                                    cgsId = listData_CG.get(i).getId();
//                                    getAllOrdersname();
                                    getOrderPlan();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView)
                                {

                                }
                            });
                        } else
                        {
                            listNewData = new ArrayList<Purchaser>();
                        }

                    } else
                    {
                        listNewData = new ArrayList<Purchaser>();
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

    //园区
    private void getlistdata()
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("parkId", "16");
//        params.addQueryStringParameter("action", "getGoodsByUid");
        params.addQueryStringParameter("action", "getcontractByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Wz_Storehouse> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        Wz_Storehouse wz_storehouses = new Wz_Storehouse();
                        wz_storehouses.setId("-1");
                        wz_storehouses.setParkName("全部分场");
                        listpark.add(wz_storehouses);
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                        listpark.addAll(listNewData);


                        String park[] = new String[listpark.size()];
                        for (int i = 0; i < listpark.size(); i++)
                        {
                            park[i] = listpark.get(i).getParkName();
                        }

                        //绑定适配器和值
//        provinceAdapter = new CustomArrayAdapter(getActivity(), mProvinceDatas);
                        provinceAdapter = new CustomArrayAdapter(getActivity(), park);
                        provinceSpinner.setAdapter(provinceAdapter);
                        provinceSpinner.setSelection(1, true);  //设置默认选中项，此处为默认选中第4个值
                        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                            {

                                parkname = listpark.get(i).getParkName();
                                parkname = listpark.get(i).getId();
                                getOrderPlan();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView)
                            {

                            }
                        });
                    } else
                    {
                        listNewData = new ArrayList<Wz_Storehouse>();
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
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");

            }
        });

    }
}
