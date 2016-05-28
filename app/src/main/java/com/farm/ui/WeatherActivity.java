package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
@EActivity(R.layout.weatheractivity)
public class WeatherActivity extends Activity
{
    List<parktab> list_park = null;
    HoursWeatherFragment hoursWeatherFragment;
    WeekWeatherFragment weekWeatherFragment;
    HistoryWeatherFragment historyWeatherFragment;
    Fragment mContent_hours = new Fragment();
    Fragment mContent_week = new Fragment();
    Fragment mContent_history = new Fragment();
    @ViewById
    View line;
    @ViewById
    TextView tv_tip;
    @ViewById
    ScrollView scrollview;

    @ViewById
    Button btn_changeweather;
    @ViewById
    TextView tv_title;
    PopupWindow pw_tab;
    View pv_tab;
    @ViewById
    ImageView iv_dowm_tab;
    // parkweathertab parkweathertab;
    String parkid;
    @ViewById
    ImageButton btn_back;

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void ll_tab()
    {
        iv_dowm_tab.setBackground(getResources().getDrawable(R.drawable.ic_up));
        getparklist();
    }

    @Click
    void btn_changeweather()
    {
        Intent intent = new Intent(this, CZ_ChangeWeather_.class);
        intent.putExtra("parkid", parkid);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initData();
//        historyWeatherFragment = new HistoryWeatherFragment_();
//        Bundle bundle = new Bundle();
//        bundle.putString("parkid", parkid);
//        historyWeatherFragment.setArguments(bundle);
//        switchContent_history(mContent_history, historyWeatherFragment);
    }

    @AfterViews
    void afterOncreate()
    {
        if (AppContext.getUserInfo(this).getnlevel().equals("1"))
        {
            btn_changeweather.setVisibility(View.VISIBLE);
        }
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    public void showPop_title()
    {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
        pv_tab.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing()))
                {
                    pw_tab.dismiss();
                    iv_dowm_tab.setBackground(getResources().getDrawable(R.drawable.ic_down));
                    return true;
                }
                return false;
            }
        });
        pv_tab.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_tab.isShowing())
                {
                    pw_tab.dismiss();
                    iv_dowm_tab.setBackground(getResources().getDrawable(R.drawable.ic_down));
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
        pw_tab.showAsDropDown(line, 0, 0);
        pw_tab.setOutsideTouchable(true);
        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
        listview.setAdapter(new TitleAdapter(WeatherActivity.this));
        listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
            {
                tv_title.setText(list_park.get(postion).getparkName());
                iv_dowm_tab.setBackground(getResources().getDrawable(R.drawable.ic_down));
                pw_tab.dismiss();
            }
        });
    }

    private void getparklist()
    {
        commembertab commembertab = AppContext.getUserInfo(WeatherActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getcontractByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        scrollview.setVisibility(View.VISIBLE);
                        tv_tip.setVisibility(View.GONE);
                        list_park = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
                        showPop_title();
                    } else
                    {
                        scrollview.setVisibility(View.GONE);
                        tv_tip.setVisibility(View.VISIBLE);
                        list_park = new ArrayList<parktab>();
                    }
                } else
                {
                    AppContext.makeToast(WeatherActivity.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(WeatherActivity.this, "error_connectServer");

            }
        });

    }

    private void initData()
    {
        commembertab commembertab = AppContext.getUserInfo(WeatherActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getcontractByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        scrollview.setVisibility(View.VISIBLE);
                        tv_tip.setVisibility(View.GONE);
                        list_park = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
                        parkid = list_park.get(0).getid();
                        tv_title.setText(list_park.get(0).getparkName());
                        Bundle bundle = new Bundle();
                        bundle.putString("parkid", parkid);
                        hoursWeatherFragment = new HoursWeatherFragment();
                        hoursWeatherFragment.setArguments(bundle);
                        weekWeatherFragment = new WeekWeatherFragment();
                        weekWeatherFragment.setArguments(bundle);
                        historyWeatherFragment = new HistoryWeatherFragment_();
                        historyWeatherFragment.setArguments(bundle);
                        switchContent_day(mContent_hours, hoursWeatherFragment);
                        switchContent_week(mContent_week, weekWeatherFragment);
                        switchContent_history(mContent_history, historyWeatherFragment);
                    } else
                    {
                        scrollview.setVisibility(View.GONE);
                        tv_tip.setVisibility(View.VISIBLE);
                        list_park = new ArrayList<parktab>();
                    }
                } else
                {
                    AppContext.makeToast(WeatherActivity.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(WeatherActivity.this, "error_connectServer");

            }
        });

    }

    public class TitleAdapter extends BaseAdapter
    {
        private Context context;
        private LayoutInflater listContainer;
        parktab parktab;

        class ListItemView
        {
            public TextView tv_yq;
        }

        public TitleAdapter(Context context)
        {
            this.context = context;
            this.listContainer = LayoutInflater.from(context);
        }

        HashMap<Integer, View> lmap = new HashMap<Integer, View>();

        public View getView(int position, View convertView, ViewGroup parent)
        {
            parktab = list_park.get(position);
            ListItemView listItemView = null;
            if (lmap.get(position) == null)
            {
                convertView = listContainer.inflate(R.layout.yq_item, null);
                listItemView = new ListItemView();
                listItemView.tv_yq = (TextView) convertView.findViewById(R.id.tv_yq);
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }
            listItemView.tv_yq.setText(parktab.getparkName());
            return convertView;
        }

        @Override
        public int getCount()
        {
            return list_park.size();
        }

        @Override
        public Object getItem(int arg0)
        {
            return null;
        }

        @Override
        public long getItemId(int arg0)
        {
            return 0;
        }
    }

    public void switchContent_day(Fragment from, Fragment to)
    {
        if (mContent_hours != to)
        {
            mContent_hours = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.chart_day, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public void switchContent_week(Fragment from, Fragment to)
    {
        if (mContent_week != to)
        {
            mContent_week = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.chart_week, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public void switchContent_history(Fragment from, Fragment to)
    {
        if (mContent_history != to)
        {
            mContent_history = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.chart_history, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
