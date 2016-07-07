package com.farm.ui;

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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_ScheduleOrderAdapter;
import com.farm.adapter.PG_scheduleOrderAdapter;
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

/**
 * Created by hasee on 2016/7/1.
 */
@EFragment
public class PG_ScheduleOrderFragment extends Fragment
{
    List<AllType> listdata_cp = new ArrayList<AllType>();
    List<Purchaser> listData_CG = new ArrayList<Purchaser>();
    String cpname = "";
    String cgsname = "";
    //    private NCZ_ScheduleOrderAdapter listAdapter;
    private PG_scheduleOrderAdapter listAdapter;
    private int listSumData;
    private List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
    static private List<SellOrder_New> secletpark;
    static private List<SellOrder_New> secletchanpin;
    static private List<SellOrder_New> secletcgs;
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
/*    @ViewById
    FrameLayout fr_id;
    @ViewById
    View lins;*/
    CustomArrayAdapter provinceAdapter = null;  //省级适配器
    CustomArrayAdapter cityAdapter = null;    //地级适配器
    CustomArrayAdapter countyAdapter = null;    //县级适配器
    static int provincePosition = 3;
    private String[] mProvinceDatas = new String[]{"全部分场", "乐丰分场", "双桥分场"};
    private String[] mCitisDatasMap = new String[]{"全部产品", "香蕉", "柑橘"};
    private String[] mAreaDatasMap = new String[]{"不限采购商", "李四", "张三"};




    @Override
    public void onResume()
    {
        super.onResume();
    }

    @AfterViews
    void afterOncreate()
    {
       /* lins.setVisibility(View.GONE);
        fr_id.setVisibility(View.GONE);*/

        getchanpin();//产品
        getpurchaser();//采购商
//        getlistdata();//园区
//        getNewSaleList_test();
//        setSpinner();
        getAllOrders();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.cz_allorderfarment, container, false);
        appContext = (AppContext) getActivity().getApplication();
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
            listAdapter = new PG_scheduleOrderAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATEAllORDER);
            lv.setAdapter(listAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(getActivity(), NCZ_OrderDetail_.class);
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
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("type", "0");
        params.addQueryStringParameter("action", "GetSpecifyOrderByNCZ");//jobGetList1
//        params.addQueryStringParameter("action", "getOrderByPGOrCC");//jobGetList1
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

                        commembertab commembertab = AppContext.getUserInfo(getActivity());
                         listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);
                        Iterator<SellOrder_New> it = listData.iterator();
                        while (it.hasNext())
                        {
                            String value = it.next().getSelltype();
                            if (value.equals("已完成"))
                            {
                                it.remove();
                            }
                        }
                        Iterator<SellOrder_New> its = listData.iterator();
                        while (its.hasNext())
                        {
                            String value = its.next().getMainPepole();
                            if (!value.equals(commembertab.getId()))
                            {
                                its.remove();
                            }
                        }
                        listAdapter = new PG_scheduleOrderAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATEAllORDER);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {

                                commembertab commembertab = AppContext.getUserInfo(getActivity());
                                AppContext.eventStatus(getActivity(), "8", listData.get(position).getUuid(), commembertab.getId());
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
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

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




    private void getAllOrdersname()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("type", "0");
        params.addQueryStringParameter("action", "GetSpecifyOrderByNCZ");//jobGetList1
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
                        commembertab commembertab = AppContext.getUserInfo(getActivity());
                        listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);

                        Iterator<SellOrder_New> ist = listData.iterator();
                        while (ist.hasNext())
                        {
                            String value = ist.next().getMainPepole();
                            if (!value.equals(commembertab.getId()))
                            {
                                ist.remove();
                            }
                        }
                        Iterator<SellOrder_New> it = listData.iterator();
                        while (it.hasNext())
                        {
                            String value = it.next().getSelltype();
                            if (value.equals("已完成"))
                            {
                                it.remove();
                            }
                        }

                        if (!cgsname.equals(""))
                        {

                            if (!cgsname.equals("全部采购商"))
                            {
                                Iterator<SellOrder_New> its = listData.iterator();
                                while (its.hasNext())
                                {
                                    String value = its.next().getBuyersName();
//                            if (!value.equals("已完成"))
                                    if (value.indexOf(cgsname) == -1)
                                    {
                                        its.remove();
                                    }
                                }
                            }
                        }
                        if (!cpname.equals(""))
                        {

                            if (!cpname.equals("全部产品"))
                            {
                                Iterator<SellOrder_New> its = listData.iterator();
                                while (its.hasNext())
                                {
                                    String value = its.next().getGoodsname();
//                            if (!value.equals("已完成"))
                                    if (value.indexOf(cpname) == -1)
                                    {
                                        its.remove();
                                    }
                                }
                            }
                        }

                        listAdapter = new PG_scheduleOrderAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATEAllORDER);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {

                                commembertab commembertab = AppContext.getUserInfo(getActivity());
                                AppContext.eventStatus(getActivity(), "8", listData.get(position).getUuid(), commembertab.getId());
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
                                    getAllOrdersname();
//                                    getAllOrdersname("采购商", listData_CG.get(i).getName());
//                                    secletcgs = new ArrayList<SellOrder_New>();
//                                    if (secletchanpin.size()==0||secletchanpin.equals("")||secletchanpin==null)
//                                    {
//                                        secletcgs.addAll(listData);
//                                    }else
//                                    {
//                                        secletcgs.addAll(secletchanpin);
//                                    }
//                                    if (!listData_CG.get(i).getName().equals("不限采购商"))
//                                    {
//                                        Iterator<SellOrder_New> it = secletcgs.iterator();
//                                        while (it.hasNext())
//                                        {
//                                            String value = it.next().getBuyersName();
////                            if (!value.equals("已完成"))
//                                            if (value.indexOf(listData_CG.get(i).getName()) == -1)
//                                            {
//                                                it.remove();
//                                            }
//                                        }
//                                    }
//
//
//                                    listAdapter = new NCZ_ScheduleOrderAdapter(getActivity(), secletcgs, AppContext.BROADCAST_UPDATEAllORDER, mCallback);
//                                    lv.setAdapter(listAdapter);
//                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
//                                    {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//                                        {
//
//                                            commembertab commembertab = AppContext.getUserInfo(getActivity());
//                                            AppContext.eventStatus(getActivity(), "8", secletcgs.get(position).getUuid(), commembertab.getId());
//                                            Intent intent = new Intent(getActivity(), NCZ_OrderDetail_.class);
//                                            intent.putExtra("bean", secletcgs.get(position));
//                                            getActivity().startActivity(intent);
//                                        }
//                                    });

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
                                getAllOrdersname();

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
}
