package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_PlantLastObserve;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.plantgrowthtab;
import com.farm.common.FileHelper;
import com.farm.widget.CustomArrayAdapter;
import com.farm.widget.CustomHorizontalScrollView_PlantObserve;
import com.guide.DensityUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2016/7/6.
 */
@EActivity(R.layout.ncz_gcddetailactivity)
public class NCZ_GCDDetailActivity extends Activity
{
    Adapter_PlantLastObserve adapter_plantLastObserve = null;
    List<plantgrowthtab> listData = null;
    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_PlantObserve> mHScrollViews = new ArrayList<CustomHorizontalScrollView_PlantObserve>();
    private ScrollAdapter mAdapter;
    int screenWidth = 0;
    int allnumber = 0;
    @ViewById
    TextView tv_top_left;
    @ViewById
    TextView tv_status;
    @ViewById
    TextView tv_photo;
    @ViewById
    TextView tv_lys;
    @ViewById
    TextView tv_ys;
    @ViewById
    TextView tv_wj;
    @ViewById
    TextView tv_zg;
    @ViewById
    ListView lv;
    @ViewById
    Spinner spinner;
    ArrayAdapter<String> provinceAdapter = null;
    private String[] mProvinceDatas = new String[]{"植株一", "植株二", "植株三"};

    @AfterViews
    void afterOncreate()
    {
        getActionBar().hide();
        DensityUtil densityUtil = new DensityUtil(NCZ_GCDDetailActivity.this);
        screenWidth = densityUtil.getScreenWidth() / 5;
        tv_top_left.getLayoutParams().width = (screenWidth);
        tv_status.getLayoutParams().width = (screenWidth);
        tv_photo.getLayoutParams().width = (screenWidth);
        tv_lys.getLayoutParams().width = (screenWidth);
        tv_ys.getLayoutParams().width = (screenWidth);
        tv_wj.getLayoutParams().width = (screenWidth);
        tv_zg.getLayoutParams().width = (screenWidth);
//        getfarmSalesData();
        setSpinner();
        getNewSaleList_test();
        getPlantObservData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    private void setSpinner()
    {
        //绑定适配器和值
        provinceAdapter = new CustomArrayAdapter(NCZ_GCDDetailActivity.this, mProvinceDatas);
        spinner.setAdapter(provinceAdapter);
        spinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值


        //省级下拉框监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

    }

    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(NCZ_GCDDetailActivity.this, "getplantobserve", plantgrowthtab.class);
        if (listData != null)
        {
            initViews();
        }

    }

    private void getfarmSalesData()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_GCDDetailActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getfarmSalesData");//jobGetList1
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
                    if (result.getAffectedRows() == 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), plantgrowthtab.class);
                        initViews();
                    } else
                    {
                        listData = new ArrayList<plantgrowthtab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectServer");
            }
        });
    }

    private void getPlantObservData()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_GCDDetailActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "NCZ_getPlantLastObserve");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<plantgrowthtab> list_lastObserveData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() == 0)
                    {
                        list_lastObserveData = JSON.parseArray(result.getRows().toJSONString(), plantgrowthtab.class);
                        adapter_plantLastObserve = new Adapter_PlantLastObserve(NCZ_GCDDetailActivity.this, list_lastObserveData);
                        lv.setAdapter(adapter_plantLastObserve);
                    } else
                    {
                        list_lastObserveData = new ArrayList<plantgrowthtab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        CustomHorizontalScrollView_PlantObserve headerScroll = (CustomHorizontalScrollView_PlantObserve) findViewById(R.id.item_scroll_title);
        // 添加头滑动事件
        mHScrollViews.add(headerScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void addHViews(final CustomHorizontalScrollView_PlantObserve hScrollView)
    {
        if (!mHScrollViews.isEmpty())
        {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView_PlantObserve scrollView = mHScrollViews.get(size - 1);
            final int scrollX = scrollView.getScrollX();
            // 第一次满屏后，向下滑动，有一条数据在开始时未加入
            if (scrollX != 0)
            {
                mListView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // 当listView刷新完成之后，把该条移动到最终位置
                        hScrollView.scrollTo(scrollX, 0);
                    }
                });
            }
        }
        mHScrollViews.add(hScrollView);
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        for (CustomHorizontalScrollView_PlantObserve scrollView : mHScrollViews)
        {
            // 防止重复滑动
            if (mTouchView != scrollView) scrollView.smoothScrollTo(l, t);
        }
    }

    class ScrollAdapter extends BaseAdapter
    {

        public ScrollAdapter()
        {

        }

        ListItemView listItemView = null;

        class ListItemView
        {
            public TextView item_titlev;
            public Button btn_data;
        }

        @Override
        public int getCount()
        {
            return listData.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        HashMap<Integer, View> lmap = new HashMap<Integer, View>();

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // 自定义视图
//            if (lmap.get(position) == null)
//            {
            // 获取list_item布局文件的视图
            convertView = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_scrolladapter_item, null);
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);
            listItemView.item_titlev.setText(listData.get(position).getcjDate());
            View view = null;

            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).gethNum());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);

            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).getwNum());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);

            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).getyNum());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);

            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).getxNum());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);

            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).getplantGrowthNote());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);

            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).getplantGrowthNote());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);

            // 第一次初始化的时候装进来
            addHViews((CustomHorizontalScrollView_PlantObserve) convertView.findViewById(R.id.item_chscroll_scroll));
            // 设置控件集到convertView
//                lmap.put(position, convertView);
//                convertView.setTag(listItemView);
//            } else
//            {
//                convertView = lmap.get(position);
//                listItemView = (ListItemView) convertView.getTag();
//            }

            return convertView;
        }
    }

    // 测试点击的事件
    protected View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            v.setBackgroundResource(R.drawable.linearlayout_green_round_selector);
//            String batchTimes = (String) v.getTag(R.id.tag_hg);
//            String parkid = (String) v.getTag(R.id.tag_kg);
//            String parkname = (String) v.getTag(R.id.tag_parkname);
//            Intent intent = new Intent(NCZ_GCDDetailActivity.this, NCZ_AreaSaleData_.class);
//            intent.putExtra("parkid", parkid);
//            intent.putExtra("parkname", parkname);
//            intent.putExtra("batchTime", batchTimes);
//            NCZ_GCDDetailActivity.this.startActivity(intent);
        }
    };
}
