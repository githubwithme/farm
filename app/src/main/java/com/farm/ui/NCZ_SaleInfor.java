package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ParkDataBean;
import com.farm.bean.Result;
import com.farm.bean.SaleDataBean;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
import com.farm.widget.CustomHorizontalScrollView;
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
@EActivity(R.layout.ncz_saleinfor)
public class NCZ_SaleInfor extends Activity
{
    List<SaleDataBean> listData = null;

    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView> mHScrollViews = new ArrayList<CustomHorizontalScrollView>();
    private ScrollAdapter mAdapter;
    String[] item_batchtimedata;
    String[] item_parkid;
    int screenWidth = 0;
    int allnumber = 0;
    List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
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
//        getfarmSalesData();
        getNewSaleList_test();
    }

    @Click
    void btn_createorders()
    {
        Intent intent = new Intent(NCZ_SaleInfor.this, NCZ_CreateNewOrder_.class);
        startActivity(intent);
    }


    @Click
    void btn_orders()
    {
        Intent intent = new Intent(NCZ_SaleInfor.this, NCZ_OrderManager_.class);
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
        listData = FileHelper.getAssetsData(NCZ_SaleInfor.this, "getsaledata", SaleDataBean.class);
        if (listData != null)
        {
            DensityUtil densityUtil = new DensityUtil(NCZ_SaleInfor.this);
            screenWidth = densityUtil.getScreenWidth();
           int size= listData.get(0).getParklist().size();
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
        commembertab commembertab = AppContext.getUserInfo(NCZ_SaleInfor.this);
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
                        listData = JSON.parseArray(result.getRows().toJSONString(), SaleDataBean.class);
                        DensityUtil densityUtil = new DensityUtil(NCZ_SaleInfor.this);
                        screenWidth = densityUtil.getScreenWidth();
                        int size= listData.get(0).getParklist().size();
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
                        listData = new ArrayList<SaleDataBean>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_SaleInfor.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_SaleInfor.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        LayoutInflater inflater = (LayoutInflater) NCZ_SaleInfor.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        item_batchtimedata = new String[listData.get(0).getParklist().size() + 1];//batchtime占了一位
        item_parkid = new String[listData.get(0).getParklist().size() + 1];//batchtime占了一位
        item_batchtimedata[0] = "batchtime";
        item_parkid[0] = "parkid";
        for (int i = 0; i < item_batchtimedata.length - 1; i++)
        {
            //顶部园区控件
            item_batchtimedata[i + 1] = "data_" + i;
            item_parkid[i + 1] = "parkid_" + i;
        }
        for (int i = 0; i < listData.get(0).getParklist().size(); i++)
        {
            View view = inflater.inflate(R.layout.saleinfo_parkitem, null);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth /5, ViewGroup.LayoutParams.MATCH_PARENT, 1);
//            lp.gravity = Gravity.CENTER;
//            view.setLayoutParams(lp);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(listData.get(0).getParklist().get(i).getParkname());
            ll_park.addView(view);
        }
        for (int i = 0; i < listData.get(0).getParklist().size(); i++)
        {
            View view = inflater.inflate(R.layout.saleinfo_totalitem, null);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth /5, ViewGroup.LayoutParams.MATCH_PARENT, 1);
//            lp.gravity = Gravity.CENTER;
//            view.setLayoutParams(lp);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_total.getLayoutParams().width = (screenWidth);
            int totalnumber = 0;
            for (int j = 0; j < listData.size(); j++)
            {
                totalnumber = totalnumber + Integer.valueOf(listData.get(j).getParklist().get(i).getNumber());
            }
            tv_total.setText(String.valueOf(totalnumber));
            ll_total.addView(view);
            allnumber = allnumber + totalnumber;
        }
        alltoatal.setText(String.valueOf(allnumber));

        Map<String, String> data = null;
        CustomHorizontalScrollView headerScroll = (CustomHorizontalScrollView) findViewById(R.id.item_scroll_title);
        CustomHorizontalScrollView totalScroll = (CustomHorizontalScrollView) findViewById(R.id.totalScroll);
        // 添加头滑动事件
        mHScrollViews.add(headerScroll);
        mHScrollViews.add(totalScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        for (int i = 0; i < listData.size(); i++)
        {
            data = new HashMap<String, String>();
            data.put("batchtime", listData.get(i).getBatchtime());
            for (int j = 0; j < item_batchtimedata.length - 1; j++)
            {
                data.put("data_" + (j), listData.get(i).getParklist().get(j).getNumber());
                data.put("parkid_" + (j), listData.get(i).getParklist().get(j).getParkid());
            }
            datas.add(data);
        }
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void addHViews(final CustomHorizontalScrollView hScrollView)
    {
        if (!mHScrollViews.isEmpty())
        {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView scrollView = mHScrollViews.get(size - 1);
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
        for (CustomHorizontalScrollView scrollView : mHScrollViews)
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

        @Override
        public int getCount()
        {
            return datas.size();
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;
            if (v == null)
            {
                View[] views = new View[item_batchtimedata.length];
                v = LayoutInflater.from(NCZ_SaleInfor.this).inflate(R.layout.scrolladapter_item, null);
                TextView item_titlev = (TextView) v.findViewById(R.id.item_titlev);
                TextView item_total = (TextView) v.findViewById(R.id.item_total);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.MATCH_PARENT, 1);
//                lp.gravity = Gravity.CENTER;
//                item_total.setLayoutParams(lp);
//                item_titlev.setLayoutParams(lp);
                item_titlev.getLayoutParams().width = (screenWidth);
                item_total.getLayoutParams().width = (screenWidth);
                LinearLayout ll_middle = (LinearLayout) v.findViewById(R.id.ll_middle);
                item_titlev.setText(datas.get(position).get(item_batchtimedata[0]).toString());
                int totalnumber = 0;
                List<ParkDataBean> list = listData.get(position).getParklist();
                for (int j = 0; j < list.size(); j++)
                {
                    totalnumber = totalnumber + Integer.valueOf(list.get(j).getNumber());
                }
                item_total.setText(String.valueOf(totalnumber));


                for (int i = 0; i < item_batchtimedata.length - 1; i++)
                {
                    View view = LayoutInflater.from(NCZ_SaleInfor.this).inflate(R.layout.saleinfo_dataitem, null);
                    TextView tv_data = (TextView) view.findViewById(R.id.tv_data);
                    tv_data.setText(datas.get(position).get(item_batchtimedata[i + 1]).toString());
//                    lp.gravity = Gravity.CENTER;
//                    view.setLayoutParams(lp);
                    tv_data.getLayoutParams().width = (screenWidth);
                    ll_middle.addView(view);

                    tv_data.setOnClickListener(clickListener);
                    tv_data.setTag(R.id.tag_kg, datas.get(position).get(item_parkid[i + 1]));
                    tv_data.setTag(R.id.tag_hg, datas.get(position).get("batchtime"));
                    tv_data.setTag(R.id.tag_parkname, listData.get(0).getParklist().get(i).getParkname());
                    views[i] = tv_data;
                }
                // 第一次初始化的时候装进来
                addHViews((CustomHorizontalScrollView) v.findViewById(R.id.item_chscroll_scroll));

//                if (position == datas.size() - 1)
//                {
//                    addHViews(totalScroll);
//                    mHScrollViews.add(totalScroll);
//                }

//                LayoutInflater inflater = (LayoutInflater) NCZ_SaleInfor.this.getSystemService(LAYOUT_INFLATER_SERVICE);
//                View view1 = inflater.inflate(R.layout.scrolladapter_item, null);
//                TextView item_titlev = (TextView) view1.findViewById(R.id.item_titlev);
//                int[] views_data = new int[listData.get(0).getParklist().size() + 1];
//                views_data[0] = item_titlev.getId();//批次控件
//
//                item_batchtimedata = new String[listData.get(0).getParklist().size() + 1];//batchtime占了一位
//                item_batchtimedata[0] = "batchtime";
//                for (int i = 0; i < item_batchtimedata.length - 1; i++)
//                {
//                    //顶部园区控件
//                    item_batchtimedata[i + 1] = "data_" + i;
//                    View view = inflater.inflate(R.layout.saleinfo_parkitem, null);
//                    TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
//                    ll_park.addView(view);
//                    //批次+销售数据控件
//                    View view2 = inflater.inflate(R.layout.saleinfo_dataitem, null);
//                    TextView tv_data = (TextView) view2.findViewById(R.id.tv_data);
//                    views_data[i + 1] = tv_data.getId();
//
//                }


//                View[] views = new View[to.length];
                // 单元格点击事件
//                for (int i = 0; i < to.length; i++)
//                {
//                    View tv = v.findViewById(to[i]);
//                    tv.setOnClickListener(clickListener);
//                    views[i] = tv;
//                }
                // 每行点击事件
                /*
                 * for(int i = 0 ; i < from.length; i++) { View tv =
				 * v.findViewById(row_hlistview[i]); }
				 */
                //
                v.setTag(views);
            }
//            View[] holders = (View[]) v.getTag();
//            int len = holders.length;
//            for (int i = 0; i < len; i++)
//            {
//                ((TextView) holders[i]).setText(this.datas.get(position).get(from[i]).toString());
//            }
            return v;
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
            Intent intent = new Intent(NCZ_SaleInfor.this, NCZ_AreaSaleData_.class);
            intent.putExtra("parkid", parkid);
            intent.putExtra("parkname", parkname);
            intent.putExtra("batchTime", batchTimes);
            NCZ_SaleInfor.this.startActivity(intent);

            Toast.makeText(NCZ_SaleInfor.this, parkid + "/" + batchTimes, Toast.LENGTH_SHORT).show();
        }
    };
}
