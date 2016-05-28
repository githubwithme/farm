package com.farm.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parkweathertab;
import com.farm.chart.ChartItem;
import com.farm.chart.LineChartItem;
import com.farm.common.utils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class HoursWeatherFragment extends Fragment
{
    ListView lv;
    TextView tv_tip;
    ArrayList<ChartItem> list;
    ChartDataAdapter cda;
    List<parkweathertab> listNewData = null;
    String parkid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        parkid = getArguments().getString("parkid");
        View rootView = inflater.inflate(R.layout.mutilchartfragment, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        lv = (ListView) rootView.findViewById(R.id.listView1);
        tv_tip = (TextView) rootView.findViewById(R.id.tv_tip);
        list = new ArrayList<ChartItem>();

        // for (int i = 0; i < 3; i++)
        // {
        //
        // if (i % 3 == 0)
        // {
        // list.add(new LineChartItem(generateDataLine(i + 1),
        // getActivity().getApplicationContext()));
        // }
        // }
        getToDayWeather();
        return rootView;
    }

    /**
     * adapter that supports 3 different item types
     */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem>
    {

        public ChartDataAdapter(Context context, List<ChartItem> objects)
        {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position)
        {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount()
        {
            return 3; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine(int cnt)
    {

        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 0; i < listNewData.size(); i++)
        {
            e1.add(new Entry(Float.valueOf(listNewData.get(i).gettempM()), i));
        }
        LineDataSet d1 = new LineDataSet(e1, "每小时平均温度" + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(3.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < listNewData.size(); i++)
        {
            e2.add(new Entry(Float.valueOf(listNewData.get(i).gettempL()), i));
        }

        LineDataSet d2 = new LineDataSet(e2, "每小时最低温度" + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(3.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<Entry> e3 = new ArrayList<Entry>();

        for (int i = 0; i < listNewData.size(); i++)
        {
            e3.add(new Entry(Float.valueOf(listNewData.get(i).gettempH()), i));
        }
        LineDataSet d3 = new LineDataSet(e3, "每小时最高温度" + cnt + ", (3)");
        d3.setLineWidth(2.5f);
        d3.setCircleSize(3.5f);
        d3.setHighLightColor(Color.rgb(244, 117, 117));
        d3.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        d3.setDrawValues(false);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);
        sets.add(d3);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    private ArrayList<String> getMonths()
    {

        ArrayList<String> m = new ArrayList<String>();
        m.add("00:00");
        m.add("01:00");
        m.add("02:00");
        m.add("03:00");
        m.add("04:00");
        m.add("05:00");
        m.add("06:00");
        m.add("07:00");
        m.add("08:00");
        m.add("09:00");
        m.add("10:00");
        m.add("11:00");
        m.add("12:00");
        m.add("13:00");
        m.add("14:00");
        m.add("15:00");
        m.add("16:00");
        m.add("17:00");
        m.add("18:00");
        m.add("19:00");
        m.add("20:00");
        m.add("21:00");
        m.add("22:00");
        m.add("23:00");

        return m;
    }

    private void getToDayWeather()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("day", utils.getToday());
        params.addQueryStringParameter("parkID", parkid);
        params.addQueryStringParameter("action", "getDayWeatherAllHour");
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
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), parkweathertab.class);
                        for (int i = 0; i < 3; i++)
                        {
                            if (i % 3 == 0)
                            {
                                list.add(new LineChartItem(R.layout.list_item_linechart, generateDataLine(i + 1), getActivity().getApplicationContext()));
                            }
                        }
                        cda = new ChartDataAdapter(getActivity().getApplicationContext(), list);
                        lv.setAdapter(cda);
//                        cda.notifyDataSetChanged();
                    } else
                    {
                        tv_tip.setVisibility(View.VISIBLE);
                        listNewData = new ArrayList<parkweathertab>();
                    }
                } else
                {
                    tv_tip.setVisibility(View.VISIBLE);
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                tv_tip.setVisibility(View.VISIBLE);
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }
}
