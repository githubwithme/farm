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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_CZBreakOff;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomHorizontalScrollView_Allitem;
import com.farm.widget.CustomHorizontalScrollView_Allitem;
import com.farm.widget.MyDatepicker;
import com.farm.widget.MyDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${hmj} on 2016/6/57.
 */
@EActivity(R.layout.cz_breakoffactivity)
public class CZ_BreakOffActivity extends Activity implements CustomHorizontalScrollView_Allitem.CustomOntouch {
    @ViewById
    CustomHorizontalScrollView_Allitem item_scroll_title;
    @ViewById
    CustomHorizontalScrollView_Allitem totalScroll;
    CustomHorizontalScrollView_Allitem.CustomOntouch customOntouch = null;
    commembertab commembertab;
    MyDialog myDialog;
    Adapter_CZBreakOff adapter_czBreakOff;
    List<BatchTime> listData = null;

    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_Allitem> mHScrollViews = new ArrayList<CustomHorizontalScrollView_Allitem>();
    private ScrollAdapter mAdapter;
    int screenWidth = 0;
    int allnumber = 0;
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
    @ViewById
    TextView tv_title;
    @ViewById
    LinearLayout cz_startdl;
    @ViewById
    TextView startdl;
    @ViewById
    TextView tv_timelimit;
    @ViewById
    RelativeLayout rl_view;

    @Click
    void tv_timelimit() {
        MyDatepicker myDatepicker = new MyDatepicker(CZ_BreakOffActivity.this, tv_timelimit);
        myDatepicker.getDialog().show();
    }

    @Click
    void startdl() {
        View dialog_layout = CZ_BreakOffActivity.this.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(CZ_BreakOffActivity.this, R.style.MyDialog, dialog_layout, "断蕾", "是否选择" + tv_timelimit.getText().toString() + "这个时间为开始断蕾时间？", "确认", "取消", new MyDialog.CustomDialogListener() {
            @Override
            public void OnClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_sure:
//                        getcreateBatchTime();
                        AddFirstBatchTime();
                        myDialog.dismiss();
                        break;
                    case R.id.btn_cancle:

                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

    @AfterViews
    void afterOncreate() {
        getActionBar().hide();
        customOntouch = this;
        item_scroll_title.setCuttomOntouch(customOntouch);
        totalScroll.setCuttomOntouch(customOntouch);
        commembertab = AppContext.getUserInfo(CZ_BreakOffActivity.this);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        tv_timelimit.setText(str);
        getIsStartBreakOff();
    }

    @Click
    void btn_createorders() {
        Intent intent = new Intent(CZ_BreakOffActivity.this, NCZ_CreateNewOrder_.class);
        startActivity(intent);
    }


    @Click
    void btn_orders() {
        Intent intent = new Intent(CZ_BreakOffActivity.this, NCZ_OrderManager_.class);
        startActivity(intent);
    }

    @Click
    void btn_customer() {
//        Intent intent = new Intent(NCZ_SaleInfor.this, NCZ_OrderManager_.class);
//        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getNewSaleList_test() {
        listData = FileHelper.getAssetsData(CZ_BreakOffActivity.this, "getsaledata", BatchTime.class);
        if (listData != null) {
            DensityUtil densityUtil = new DensityUtil(CZ_BreakOffActivity.this);
            screenWidth = densityUtil.getScreenWidth();
            int size = listData.get(0).getAreatabList().size();
            if (size == 1) {
                screenWidth = screenWidth / 4;
            } else if (size == 2) {
                screenWidth = screenWidth / 4;
            } else {
                screenWidth = screenWidth / 4;
            }
            tv_top_left.getLayoutParams().width = (screenWidth);
            tv_top_right.getLayoutParams().width = (screenWidth);
            tv_bottom_left.getLayoutParams().width = (screenWidth);
            alltoatal.getLayoutParams().width = (screenWidth);
            initViews();
        }

    }

    public void getIsStartBreakOff() {
        commembertab = AppContext.getUserInfo(CZ_BreakOffActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkid", commembertab.getparkId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "IsStartBreakOff");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<BatchTime> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() > 0) {
                        rl_view.setVisibility(View.GONE);
                        cz_startdl.setVisibility(View.GONE);
                        getBatchTimeBreakoffData();
//                        getAreaBreakoffData();
                    } else {
                        rl_view.setVisibility(View.GONE);
                    }

                } else {
                    AppContext.makeToast(CZ_BreakOffActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(CZ_BreakOffActivity.this, "error_connectServer");
            }
        });
    }


    public void AddFirstBatchTime() {
        commembertab commembertab = AppContext.getUserInfo(CZ_BreakOffActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkid", commembertab.getparkId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("startday", tv_timelimit.getText().toString());
        params.addQueryStringParameter("action", "AddFirstBatchTime");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<BatchTime> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() > 0) {

                        cz_startdl.setVisibility(View.GONE);
                        getBatchTimeBreakoffData();

                    } else {
                        listNewData = new ArrayList<BatchTime>();
                    }

                } else {
                    AppContext.makeToast(CZ_BreakOffActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(CZ_BreakOffActivity.this, "error_connectServer");
            }
        });
    }

    private void getBatchTimeBreakoffData() {
        commembertab commembertab = AppContext.getUserInfo(CZ_BreakOffActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("parkid", commembertab.getparkId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "CZ_getBatchTimeBreakoffData");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                        DensityUtil densityUtil = new DensityUtil(CZ_BreakOffActivity.this);
                        screenWidth = densityUtil.getScreenWidth();
                        int size = listData.get(0).getAreatabList().size();
                        if (size == 1) {
                            screenWidth = screenWidth / 4;
                        } else if (size == 2) {
                            screenWidth = screenWidth / 4;
                        } else {
                            screenWidth = screenWidth / 4;
                        }
                        tv_top_left.getLayoutParams().width = (screenWidth);
                        tv_top_right.getLayoutParams().width = (screenWidth);
                        tv_bottom_left.getLayoutParams().width = (screenWidth);
                        alltoatal.getLayoutParams().width = (screenWidth);
                        initViews();
                    } else {
                        listData = new ArrayList<BatchTime>();
                    }

                } else {
                    AppContext.makeToast(CZ_BreakOffActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(CZ_BreakOffActivity.this, "error_connectServer");
            }
        });
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) CZ_BreakOffActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < listData.get(0).getAreatabList().size(); i++) {
            View view = inflater.inflate(R.layout.cz_breakoff_areaitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(listData.get(0).getAreatabList().get(i).getareaName());
            ll_park.addView(view);
//            tv_parkname.setTag(listData.get(0).getAreatabList().get(i).getAreaid());
            tv_parkname.setTag(R.id.areaname, listData.get(0).getAreatabList().get(i).getAreaid());
            tv_parkname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String areaid = (String) v.getTag();
                    String areaid = (String) v.getTag(R.id.areaname);
//                    Intent intent = new Intent(CZ_BreakOffActivity.this, CZ_ContractSaleData_.class);
                    Intent intent = new Intent(CZ_BreakOffActivity.this, CZ_ContractBreakOff_.class);
                    intent.putExtra("areaid", areaid);
                    CZ_BreakOffActivity.this.startActivity(intent);
                }
            });
        }
        for (int i = 0; i < listData.get(0).getAreatabList().size(); i++) {
            View view = inflater.inflate(R.layout.cz_breakoff_totalitem, null);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_total.getLayoutParams().width = (screenWidth);
            int totalnumber = 0;
            for (int j = 0; j < listData.size(); j++) {
                totalnumber = totalnumber + Integer.valueOf(listData.get(j).getAreatabList().get(i).getAllnumber());
            }
            tv_total.setText(String.valueOf(totalnumber));
            ll_total.addView(view);
            allnumber = allnumber + totalnumber;
        }
        alltoatal.setText(String.valueOf(allnumber));

        Map<String, String> data = null;
//        CustomHorizontalScrollView_Allitem headerScroll = (CustomHorizontalScrollView_Allitem) findViewById(R.id.item_scroll_title);
//        CustomHorizontalScrollView_Allitem totalScroll = (CustomHorizontalScrollView_Allitem) findViewById(R.id.totalScroll);
        // 添加头滑动事件
        mHScrollViews.add(item_scroll_title);
        mHScrollViews.add(totalScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
        utils.setListViewHeight(mListView);
    }

    public void addHViews(final CustomHorizontalScrollView_Allitem hScrollView) {
        if (!mHScrollViews.isEmpty()) {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView_Allitem scrollView = mHScrollViews.get(size - 1);
            final int scrollX = scrollView.getScrollX();
            // 第一次满屏后，向下滑动，有一条数据在开始时未加入
            if (scrollX != 0) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 当listView刷新完成之后，把该条移动到最终位置
                        hScrollView.scrollTo(scrollX, 0);
                    }
                });
            }
        }
        mHScrollViews.add(hScrollView);
    }

    @Override
    public void customOnTouchEvent(HorizontalScrollView horizontalScrollView) {
        mTouchView = horizontalScrollView;
    }

    @Override
    public void customOnScrollChanged(int l, int t, int oldl, int oldt) {
        for (CustomHorizontalScrollView_Allitem scrollView : mHScrollViews) {
            // 防止重复滑动
            if (mTouchView != scrollView) scrollView.smoothScrollTo(l, t);
        }
    }

    @Override
    public HorizontalScrollView getmTouchView() {
        return mTouchView;
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (CustomHorizontalScrollView_Allitem scrollView : mHScrollViews) {
            // 防止重复滑动
            if (mTouchView != scrollView) scrollView.smoothScrollTo(l, t);
        }
    }

    class ScrollAdapter extends BaseAdapter {

        public ScrollAdapter() {

        }

        ListItemView listItemView = null;

        class ListItemView {
            public TextView item_titlev;
            public TextView item_total;
            public TextView tv_data;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        HashMap<Integer, View> lmap = new HashMap<Integer, View>();

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 自定义视图
//            if (lmap.get(position) == null)
//            {
            // 获取list_item布局文件的视图
            convertView = LayoutInflater.from(CZ_BreakOffActivity.this).inflate(R.layout.cz_breakoff_scrolladapteritem, null);
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
            listItemView.item_total = (TextView) convertView.findViewById(R.id.item_total);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
            listItemView.item_total.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);
            listItemView.item_titlev.setText(listData.get(position).getBatchTime());
            int totalnumber = 0;
            List<areatab> list = listData.get(position).getAreatabList();
            for (int j = 0; j < list.size(); j++) {
                totalnumber = totalnumber + Integer.valueOf(list.get(j).getAllnumber());
            }
            listItemView.item_total.setText(String.valueOf(totalnumber));

            for (int i = 0; i < listData.get(position).getAreatabList().size(); i++) {
                View view = LayoutInflater.from(CZ_BreakOffActivity.this).inflate(R.layout.cz_breakoff_dataitem, null);
                listItemView.tv_data = (TextView) view.findViewById(R.id.tv_data);
                listItemView.tv_data.setText(listData.get(position).getAreatabList().get(i).getAllnumber());
                listItemView.tv_data.getLayoutParams().width = (screenWidth);
                ll_middle.addView(view);

                listItemView.tv_data.requestFocusFromTouch();
                listItemView.tv_data.setTag(R.id.tag_areaid, listData.get(position).getAreatabList().get(i).getAreaid());
                listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position).getBatchTime());
                listItemView.tv_data.setTag(R.id.tag_areaname, listData.get(position).getAreatabList().get(i).getareaName());
                listItemView.tv_data.setTag(R.id.tag_number, listData.get(position).getAreatabList().get(i).getAllnumber());
                listItemView.tv_data.setOnClickListener(clickListener);

            }
            // 第一次初始化的时候装进来
            // 第一次初始化的时候装进来
            CustomHorizontalScrollView_Allitem customHorizontalScrollView = (CustomHorizontalScrollView_Allitem) convertView.findViewById(R.id.item_chscroll_scroll);
            addHViews(customHorizontalScrollView);
            customHorizontalScrollView.setCuttomOntouch(customOntouch);
//            addHViews((CustomHorizontalScrollView_Allitem) convertView.findViewById(R.id.item_chscroll_scroll));
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
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setBackgroundResource(R.drawable.linearlayout_green_round_selector);
            String batchTimes = (String) v.getTag(R.id.tag_batchtime);
            String number = (String) v.getTag(R.id.tag_number);
            String areaid = (String) v.getTag(R.id.tag_areaid);
            String areaname = (String) v.getTag(R.id.tag_areaname);
            if (number.equals("0")) {
                Toast.makeText(CZ_BreakOffActivity.this, "该片区该批次暂无断蕾数据", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CZ_BreakOffActivity.this, CZ_AreaBatchTimeBreakOff_.class);
                intent.putExtra("areaid", areaid);
                intent.putExtra("areaname", areaname);
                intent.putExtra("batchTime", batchTimes);
                CZ_BreakOffActivity.this.startActivity(intent);
            }
        }
    };
}
