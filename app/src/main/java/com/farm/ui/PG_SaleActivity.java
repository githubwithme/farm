package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomHorizontalScrollView_PGSale;
import com.guide.DensityUtil;
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
import java.util.Map;

/**
 * Created by ${hmj} on 2016/6/57.
 */
@EActivity(R.layout.pg_saleactivity)
public class PG_SaleActivity extends Activity
{
    List<BatchTime> listData = null;

    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_PGSale> mHScrollViews = new ArrayList<CustomHorizontalScrollView_PGSale>();
    private ScrollAdapter mAdapter;
    //    String[] item_batchtimedata;
//    String[] item_parkid;
    int screenWidth = 0;
    int allnumber = 0;
    //    List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
    @ViewById
    LinearLayout ll_park;
    @ViewById
    LinearLayout ll_total;
    @ViewById
    TextView alltoatal;
    @ViewById
    TextView tv_top_left;
    @ViewById
    TextView tv_top_right;
    @ViewById
    TextView tv_bottom_left;

    @AfterViews
    void afterOncreate()
    {
        getActionBar().hide();
        getfarmSalesData();
//        getNewSaleList_test();
    }


    @Click
    void btn_createorders()
    {
        Intent intent = new Intent(this, PG_CreateOrder_.class);
        startActivity(intent);
    }

    @Click
    void btn_orders()
    {
        Intent intent = new Intent(this, PG_OrderManager_.class);
        startActivity(intent);
    }

    @Click
    void btn_customer()
    {
//        Intent intent = new Intent(NCZ_SaleInfor.this, NCZ_OrderManager_.class);
//        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(PG_SaleActivity.this, "getsaledata", BatchTime.class);
        if (listData != null)
        {
            DensityUtil densityUtil = new DensityUtil(PG_SaleActivity.this);
            screenWidth = densityUtil.getScreenWidth();
            int size = listData.get(0).getContracttabList().size();
            if (size == 1)
            {
                screenWidth = screenWidth / 3;
            } else if (size == 2)
            {
                screenWidth = screenWidth / 4;
            } else
            {
                screenWidth = screenWidth / 5;
            }
            tv_top_left.getLayoutParams().width = (screenWidth);
            tv_top_right.getLayoutParams().width = (screenWidth);
            tv_bottom_left.getLayoutParams().width = (screenWidth);
            alltoatal.getLayoutParams().width = (screenWidth);
            initViews();
        }

    }

    private void getfarmSalesData()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_SaleActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaid", commembertab.getareaId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "PG_GetBatchTimeSaleData");//jobGetList1
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
                    if (result.getAffectedRows() >0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                        DensityUtil densityUtil = new DensityUtil(PG_SaleActivity.this);
                        screenWidth = densityUtil.getScreenWidth();
                        int size = listData.get(0).getContracttabList().size();
                        if (size == 1)
                        {
                            screenWidth = screenWidth / 3;
                        } else if (size == 2)
                        {
                            screenWidth = screenWidth / 4;
                        } else
                        {
                            screenWidth = screenWidth / 5;
                        }
                        tv_top_left.getLayoutParams().width = (screenWidth);
                        tv_top_right.getLayoutParams().width = (screenWidth);
                        tv_bottom_left.getLayoutParams().width = (screenWidth);
                        alltoatal.getLayoutParams().width = (screenWidth);
                        initViews();
                    } else
                    {
                        listData = new ArrayList<BatchTime>();
                    }

                } else
                {
                    AppContext.makeToast(PG_SaleActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_SaleActivity.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        LayoutInflater inflater = (LayoutInflater) PG_SaleActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < listData.get(0).getContracttabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.pg_sale_contractitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(listData.get(0).getContracttabList().get(i).getContractname());
            ll_park.addView(view);
        }
        for (int i = 0; i < listData.get(0).getContracttabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.pg_sale_totalitem, null);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_total.getLayoutParams().width = (screenWidth);
            int totalnumber = 0;
            for (int j = 0; j < listData.size(); j++)
            {
                totalnumber = totalnumber + Integer.valueOf(listData.get(j).getContracttabList().get(i).getAllnumber());
            }
            tv_total.setText(String.valueOf(totalnumber));
            ll_total.addView(view);
            allnumber = allnumber + totalnumber;
        }
        alltoatal.setText(String.valueOf(allnumber));

        Map<String, String> data = null;
        CustomHorizontalScrollView_PGSale headerScroll = (CustomHorizontalScrollView_PGSale) findViewById(R.id.item_scroll_title);
        CustomHorizontalScrollView_PGSale totalScroll = (CustomHorizontalScrollView_PGSale) findViewById(R.id.totalScroll);
        // 添加头滑动事件
        mHScrollViews.add(headerScroll);
        mHScrollViews.add(totalScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void addHViews(final CustomHorizontalScrollView_PGSale hScrollView)
    {
        if (!mHScrollViews.isEmpty())
        {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView_PGSale scrollView = mHScrollViews.get(size - 1);
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
        for (CustomHorizontalScrollView_PGSale scrollView : mHScrollViews)
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
            public TextView item_total;
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
            convertView = LayoutInflater.from(PG_SaleActivity.this).inflate(R.layout.pg_sale_scrolladapteritem, null);
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
            listItemView.item_total = (TextView) convertView.findViewById(R.id.item_total);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
            listItemView.item_total.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);
            listItemView.item_titlev.setText(listData.get(position).getBatchTime());
            int totalnumber = 0;
            List<contractTab> list = listData.get(position).getContracttabList();
            for (int j = 0; j < list.size(); j++)
            {
                totalnumber = totalnumber + Integer.valueOf(list.get(j).getAllnumber());
            }
            listItemView.item_total.setText(String.valueOf(totalnumber));

            for (int i = 0; i < listData.get(position).getContracttabList().size(); i++)
            {
                View view = LayoutInflater.from(PG_SaleActivity.this).inflate(R.layout.pg_sale_dataitem, null);
                listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
                listItemView.btn_data.setText(listData.get(position).getContracttabList().get(i).getAllnumber());
                listItemView.btn_data.getLayoutParams().width = (screenWidth);
                ll_middle.addView(view);

                listItemView.btn_data.requestFocusFromTouch();
                listItemView.btn_data.setTag(R.id.tag_kg, listData.get(position).getContracttabList().get(i).getContractid());
                listItemView.btn_data.setTag(R.id.tag_hg, listData.get(position).getBatchTime());
                listItemView.btn_data.setTag(R.id.tag_parkname, listData.get(position).getContracttabList().get(i).getContractname());
                listItemView.btn_data.setOnClickListener(clickListener);

            }
            // 第一次初始化的时候装进来
            addHViews((CustomHorizontalScrollView_PGSale) convertView.findViewById(R.id.item_chscroll_scroll));
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
            String batchTimes = (String) v.getTag(R.id.tag_hg);
            String parkid = (String) v.getTag(R.id.tag_kg);
            String parkname = (String) v.getTag(R.id.tag_parkname);
//            Intent intent = new Intent(PG_SaleActivity.this, NCZ_AreaSaleData_.class);
//            intent.putExtra("parkid", parkid);
//            intent.putExtra("parkname", parkname);
//            intent.putExtra("batchTime", batchTimes);
//            PG_SaleActivity.this.startActivity(intent);

            Toast.makeText(PG_SaleActivity.this, parkid + "/" + batchTimes, Toast.LENGTH_SHORT).show();
        }
    };
}
