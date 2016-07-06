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
import com.farm.adapter.NCZ_DealingAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
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
 * Created by hasee on 2016/7/5.
 */
@SuppressLint("NewApi")
@EFragment
public class PG_DealingOrder extends Fragment
{
    private NCZ_DealingAdapter listAdapter;
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
//        getNewSaleList_test();
        setSpinner();
        getAllOrders();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_allorderfragment, container, false);
        appContext = (AppContext) getActivity().getApplication();
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATEDEALINGORDER);
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
            listAdapter = new NCZ_DealingAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATEDEALINGORDER);
            lv.setAdapter(listAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    commembertab commembertab = AppContext.getUserInfo(getActivity());
                    AppContext.eventStatus(getActivity(), "8", listData.get(position).getUuid(), commembertab.getId());
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
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                commembertab commembertab = AppContext.getUserInfo(getActivity());
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
                            if (!value.equals("已完成"))
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
                        listAdapter = new NCZ_DealingAdapter(getActivity(), listData, AppContext.BROADCAST_UPDATEDEALINGORDER);
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
}
