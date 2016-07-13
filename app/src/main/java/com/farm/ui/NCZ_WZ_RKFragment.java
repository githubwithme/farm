package com.farm.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.WZ_RKExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.WZ_CRk;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomSpinnerAdapter;
import com.farm.widget.MyDatepicker;
import com.farm.widget.ReturnData;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by user on 2016/4/7.
 */
@EFragment
public class NCZ_WZ_RKFragment extends Fragment
{
    private int weeks = 0;
    final Calendar c = Calendar.getInstance();
    List<Wz_Storehouse> listparkData = new ArrayList<Wz_Storehouse>();
    CustomSpinnerAdapter parkAdapter = null;
    private List<String> parkData = new ArrayList<String>();

    CustomSpinnerAdapter dateAdapter = null;
    private List<String> dateData = new ArrayList<String>();
    String parkId;

    @ViewById
    Spinner parkSpinner;
    @ViewById
    Spinner dataSpinner;
    String id;
    String name;

    String goodsName;
    String indate;
    WZ_RKExecute_Adapter wz_rkExecute_adapter;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    ImageButton imgbtn;
    @ViewById
    TextView et_goodsname;

    @ViewById
    TextView tv_data1;
    @ViewById
    TextView tv_data2;
    String data = "";
    String data1 = "";
    String dateDuring = "";


/*    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    String str = formatter.format(curDate);*/

    @Click
    void tv_data1()
    {
        ReturnData returnData = new ReturnData(getActivity(), tv_data1);
        returnData.getDialog().show();

    }

    @Click
    void tv_data2()
    {
        ReturnData returnData = new ReturnData(getActivity(), tv_data2);
        returnData.getDialog().show();

    }

    @Click
    void et_goodsname()
    {
        et_goodsname.setText("");
    }

    @Click
    void imgbtn()
    {


        goodsName = et_goodsname.getText().toString();
        getBreakOffInfoOfContract("", "");
    }

    @AfterViews
    void afterOncreate()
    {
        tv_data2.addTextChangedListener(alldata);
        tv_data1.addTextChangedListener(alldata);

        getlistdata();
        getBreakOffInfoOfContract("", "");

        showData();
    }

    private void showData()
    {
        dateData.add("所有");
        dateData.add("今天");
        dateData.add("本周");
        dateData.add("本月");

        //绑定适配器和值
        dateAdapter = new CustomSpinnerAdapter(getActivity(), dateData);
        dataSpinner.setAdapter(dateAdapter);
        dataSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值
        dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (dateData.get(i).equals("所有"))
                {
                    getBreakOffInfoOfContract("", "");
                } else if (dateData.get(i).equals("今天"))
                {
              /*      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    data = formatter.format(curDate) + " 0:00:00:000";*/
                    data=utils.getToday() + " 0:00:00:000";

                    java.util.Calendar b = Calendar.getInstance();
                    b.add(Calendar.DAY_OF_MONTH, 1);
                    java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    data1 = sdf.format(b.getTime()) + " 0:00:00:000";
                  /*  c.add(Calendar.DAY_OF_MONTH, 1);
                    java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    data1 = sdf.format(c.getTime()) + " 0:00:00:000";*/
                    getBreakOffInfoOfContract(data, data1);
                } else if (dateData.get(i).equals("本周"))
                {
                    SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
                    weeks = 0;
                    int mondayPlus = getMondayPlus();
                    GregorianCalendar currentDate = new GregorianCalendar();
                    currentDate.add(GregorianCalendar.DATE, mondayPlus);
                    Date monday = currentDate.getTime();

//                    DateFormat df = DateFormat.getDateInstance();
                    data = sdfs.format(monday) + " 0:00:00:000";   //本周一

                    weeks++;
                    int mondayPlusnest = getMondayPlus();
                    GregorianCalendar currentDatenext = new GregorianCalendar();
                    currentDatenext.add(GregorianCalendar.DATE, mondayPlusnest + 7);
                    Date mondaynext = currentDatenext.getTime();
//                    DateFormat dfnext = DateFormat.getDateInstance();
                    data1 = sdfs.format(mondaynext) + " 0:00:00:000";
                    getBreakOffInfoOfContract(data, data1);
                } else if (dateData.get(i).equals("本月"))
                {
                    SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");

//                    DateFormat sd = DateFormat.getDateInstance();
                    Calendar lastDate = Calendar.getInstance();
                    lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
                    data = sdfs.format(lastDate.getTime()) + " 0:00:00:000";//本月第一天


                    Calendar lastDates = Calendar.getInstance();
                    lastDates.add(Calendar.MONTH, 1);// 减一个月
                    lastDates.set(Calendar.DATE, 1);// 把日期设置为当月第一天
                    data1 = sdfs.format(lastDates.getTime()) + " 0:00:00:000";//下月第一天

                    getBreakOffInfoOfContract(data, data1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    public int getMondayPlus()
    {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1)
        {
            return 0;
        } else
        {
            return 1 - dayOfWeek;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_wz_crlayout, container, false);
  /*      id = getArguments().getString("id");
        name = getArguments().getString("name");*/
        return rootView;
    }

    //获取时间
    private TextWatcher alldata = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            if (!tv_data1.getText().toString().equals("") && !tv_data2.getText().toString().equals(""))
            {

//                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tv_data1.getText().toString());
                data = tv_data1.getText().toString() + " 0:00:00:000";
                data1 = tv_data2.getText().toString() + " 0:00:00:000";
                getBreakOffInfoOfContract(data, data1);
            }


        }
    };

    private void getBreakOffInfoOfContract(String data1, String data2)
    {
        if (!data1.equals("") && !data2.equals(""))
        {
            dateDuring = data1 + "?" + data2;
        } else
        {
            dateDuring = "";
        }
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsName", goodsName);
        params.addQueryStringParameter("parkId", parkId);
        params.addQueryStringParameter("dateDuring", dateDuring);
        params.addQueryStringParameter("action", "getGoodsInByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<WZ_CRk> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    /*if (result.getAffectedRows() != 0)
                    {*/
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_CRk.class);
                    wz_rkExecute_adapter = new WZ_RKExecute_Adapter(getActivity(), listNewData, expandableListView);
                    expandableListView.setAdapter(wz_rkExecute_adapter);

//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
////                            expandableListView.expandGroup(i);//展开
//                            expandableListView.collapseGroup(i);//关闭
//                        }

                /*    } else
                    {
                        listNewData = new ArrayList<WZ_CRk>();
                    }*/

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
                        parkData = new ArrayList<String>();
                        Wz_Storehouse wz_storehouses = new Wz_Storehouse();
                        wz_storehouses.setParkId("");
                        wz_storehouses.setParkName("全部分场");
                        listparkData.add(wz_storehouses);
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                        listparkData.addAll(listNewData);


                        for (int i = 0; i < listparkData.size(); i++)
                        {
                            parkData.add(listparkData.get(i).getParkName());
                        }

                        //绑定适配器和值
                        parkAdapter = new CustomSpinnerAdapter(getActivity(), parkData);
                        parkSpinner.setAdapter(parkAdapter);
                        parkSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值
                        parkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                            {
                                parkId = listparkData.get(i).getId();
                                getBreakOffInfoOfContract("", "");
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
