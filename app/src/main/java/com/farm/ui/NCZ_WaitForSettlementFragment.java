package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_WaitForSettlementAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.AllType;
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
import java.util.Iterator;
import java.util.List;

@SuppressLint("NewApi")
@EFragment
public class NCZ_WaitForSettlementFragment extends Fragment
{
    List<AllType> listdata_cp = new ArrayList<AllType>();
    List<Purchaser> listData_CG = new ArrayList<Purchaser>();
    List<Wz_Storehouse> listpark = new ArrayList<Wz_Storehouse>();
    String parkname="";
    String cpname="";
    String cgsname="";
//    private NCZ_OrderAdapter listAdapter;
    private NCZ_WaitForSettlementAdapter listAdapter;
    private int listSumData;
    private List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
    private AppContext appContext;
    private View list_footer;
    private TextView list_foot_more;
    private ProgressBar list_foot_progress;
    PopupWindow pw_tab;
    View pv_tab;
    PopupWindow pw_command;
    View pv_command;
    @ViewById
    View line;
    @ViewById
    ListView lv;
    @ViewById
    Spinner provinceSpinner;
    @ViewById
    Spinner citySpinner;
    @ViewById
    Spinner countySpinner;
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    ArrayAdapter<String> countyAdapter = null;    //县级适配器
    static int provincePosition = 3;
    private String[] mProvinceDatas=new String[]{"全部分场","乐丰分场","双桥分场"};
    private String[] mCitisDatasMap=new String[]{"全部产品","香蕉","柑橘"};
    private String[] mAreaDatasMap=new String[]{"不限采购商","李四","张三"};

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @AfterViews
    void afterOncreate()
    {
        getchanpin();//产品
        getpurchaser();//采购商
        getlistdata();//园区
//        getNewSaleList_test();
        setSpinner();
        getAllOrders();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_waitforsettlementfragment, container, false);
        appContext = (AppContext) getActivity().getApplication();
//        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATENOTPAYORDER);
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
            getAllOrders();
        }
    };


    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(getActivity(), "getOrderList", SellOrder_New.class);
        if (listData != null)
        {
//            listAdapter = new NCZ_NotpayAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATENOTPAYORDER);
            listAdapter = new NCZ_WaitForSettlementAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATEAllORDER);
            lv.setAdapter(listAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    commembertab commembertab = AppContext.getUserInfo(getActivity());
                    AppContext.eventStatus(getActivity(), "8",  listData.get(position).getUuid(), commembertab.getId());
//                    Intent intent = new Intent(getActivity(), NCZ_OrderDetail_.class);
                    Intent intent = new Intent(getActivity(), NCZ_NewOrderDetail_.class);
                    intent.putExtra("bean", listData.get(position));
                    getActivity().startActivity(intent);
                }
            });
        }

    }

    private void getAllOrders()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("year", utils.getYear());
//        params.addQueryStringParameter("type", "0");
//        params.addQueryStringParameter("action", "GetSpecifyOrderByNCZ");//jobGetList1
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkid", "-1");
        params.addQueryStringParameter("productname","-1");
        params.addQueryStringParameter("buyer","-1");
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("status", "0");
        params.addQueryStringParameter("action", "NCZ_getWaitForSettlementOrder");
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
                        listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);
                        Iterator<SellOrder_New> it = listData.iterator();
                        while (it.hasNext())
                        {
                            String value = it.next().getSelltype();
                            if (value.equals("已完成")||value.equals("待审批"))
                            {
                                it.remove();
                            }
                        }


                        listAdapter = new NCZ_WaitForSettlementAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATENOTPAYORDER);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
//                                Intent intent = new Intent(getActivity(), NCZ_OrderDetail_.class);
                                Intent intent = new Intent(getActivity(), NCZ_NewOrderDetail_.class);
                                intent.putExtra("bean", listData.get(position));
                                getActivity().startActivity(intent);
                            }
                        });

                    } else
                    {
                        listData = new ArrayList<SellOrder_New>();
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
    /*
        * 设置下拉框
        */
    private void setSpinner()
    {
        //绑定适配器和值
        provinceAdapter = new CustomArrayAdapter(getActivity(), mProvinceDatas);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值

        cityAdapter = new CustomArrayAdapter(getActivity(), mCitisDatasMap);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0, true);  //默认选中第0个

        countyAdapter = new CustomArrayAdapter(getActivity(), mAreaDatasMap);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setSelection(0, true);

        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }

        });


        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
    //园区
    private void getlistdata()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
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
                        wz_storehouses.setParkId("");
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
                        provinceSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值
                        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                            {

                                parkname = listpark.get(i).getParkName();
                                getAllOrders();
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
    //产品
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
                        allType.setId("");
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

                                cpname = listdata_cp.get(i).getProductName();
                                getAllOrders();
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
                            purchaser.setId("");
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
                                    getAllOrders();
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


//    private void getAllOrdersname()
//    {
//        commembertab commembertab = AppContext.getUserInfo(getActivity());
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("year", utils.getYear());
//        params.addQueryStringParameter("type", "0");
//        params.addQueryStringParameter("action", "GetSpecifyOrderByNCZ");//jobGetList1
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
//                        listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);
//                        Iterator<SellOrder_New> ita = listData.iterator();
//                        while (ita.hasNext())
//                        {
//                            String value = ita.next().getSelltype();
//                            if (value.equals("已完成")||value.equals("待审批"))
//                            {
//                                ita.remove();
//                            }
//                        }
//
//                        if (!parkname.equals(""))
//                        {
//                            if (!parkname.equals("全部分场"))
//                            {
//                                Iterator<SellOrder_New> it = listData.iterator();
//                                while (it.hasNext())
//                                {
//                                    String value = it.next().getProducer();
//                                    if (value.indexOf(parkname) == -1)
//                                    {
//                                        it.remove();
//                                    }
//                                }
//                            }
//                        }
//                        if (!cgsname.equals(""))
//                        {
//
//                            if (!cgsname.equals("全部采购商"))
//                            {
//                                Iterator<SellOrder_New> its = listData.iterator();
//                                while (its.hasNext())
//                                {
//                                    String value = its.next().getPurchaName();
//                                    if (value.indexOf(cgsname) == -1)
//                                    {
//                                        its.remove();
//                                    }
//                                }
//                            }
//                        }
//
//                        if (!cpname.equals(""))
//                        {
//
//                            if (!cpname.equals("全部产品"))
//                            {
//                                Iterator<SellOrder_New> its = listData.iterator();
//                                while (its.hasNext())
//                                {
//                                    String value = its.next().getGoodsname();
////                            if (!value.equals("已完成"))
//                                    if (value.indexOf(cpname) == -1)
//                                    {
//                                        its.remove();
//                                    }
//                                }
//                            }
//                        }
//
//                        listAdapter = new NCZ_WaitForSettlementAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATEAllORDER);
//                        lv.setAdapter(listAdapter);
//                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
//                        {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                            {
//
//                                commembertab commembertab = AppContext.getUserInfo(getActivity());
//                                AppContext.eventStatus(getActivity(), "8", listData.get(position).getUuid(), commembertab.getId());
////                                Intent intent = new Intent(getActivity(), NCZ_OrderDetail_.class);
//                                Intent intent = new Intent(getActivity(), NCZ_NewOrderDetail_.class);
//                                intent.putExtra("bean", listData.get(position));
//                                getActivity().startActivity(intent);
//                            }
//                        });
//
//                    } else
//                    {
//                        listData = new ArrayList<SellOrder_New>();
//                    }
//
//                } else
//                {
//                    AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    return;
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg)
//            {
//                AppContext.makeToast(getActivity(), "error_connectServer");
//
//            }
//        });
//    }
}
