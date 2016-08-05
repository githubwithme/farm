package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_DLAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.contractTab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomHorizontalScrollView_Allitem;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/6/57.
 */
@EActivity(R.layout.ncz_contractbreakoff)
public class NCZ_ContractBreakOff extends Activity implements CustomHorizontalScrollView_Allitem.CustomOntouch
{
    String areaid;
    @ViewById
    CustomHorizontalScrollView_Allitem item_scroll_title;
    @ViewById
    CustomHorizontalScrollView_Allitem totalScroll;
    CustomHorizontalScrollView_Allitem.CustomOntouch customOntouch = null;

    List<BatchTime> listData = null;
    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_Allitem> mHScrollViews = null;
    private ScrollAdapter mAdapter;
    int screenWidth = 0;
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
    private String id;
    private String name;
    List<Wz_Storehouse> listpeople = new ArrayList<Wz_Storehouse>();
    PopupWindow pw_tab;
    View pv_tab;
    @ViewById
    View line;
    //    PG_CKofListAdapter pg_cKlistAdapter;
    NCZ_DLAdapter ncz_dlAdapter;
    com.farm.bean.commembertab commembertab;
    MyDialog myDialog;
    Fragment mContent = new Fragment();
    //    @ViewById
//    LinearLayout cz_startdl;
    @ViewById
    TextView startdl;
    @ViewById
    TextView tv_timelimit;
    //    @ViewById
//    RelativeLayout rl_view;
    DialogFragment_WaitTip dialog;

    public void showDialog_waitTip()
    {
        dialog = new DialogFragment_WaitTip_();
        Bundle bundle1 = new Bundle();
        dialog.setArguments(bundle1);
        dialog.show(getFragmentManager(), "TIP");
    }

    //    dialog.loadingTip(getText(R.string.error_data).toString());
//    dialog.loadingTip(getText(R.string.error_network).toString());
    @Click
    void btn_back()
    {
        finish();
    }


    @AfterViews
    void afterOncreate()
    {
        showDialog_waitTip();
        customOntouch = this;
        item_scroll_title.setCuttomOntouch(customOntouch);
        totalScroll.setCuttomOntouch(customOntouch);
//        getNewSaleList_test();
        getBatchTimeOfPark();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(NCZ_ContractBreakOff.this);
        areaid = getIntent().getStringExtra("areaid");
    }


    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(NCZ_ContractBreakOff.this, "getContractBreakOff", BatchTime.class);
        if (listData != null)
        {
            DensityUtil densityUtil = new DensityUtil(NCZ_ContractBreakOff.this);
            screenWidth = densityUtil.getScreenWidth();
            int size = listData.get(0).getContracttabList().size();
            if (size == 1)
            {
                screenWidth = screenWidth / 4;
            } else if (size == 2)
            {
                screenWidth = screenWidth / 4;
            } else
            {
                screenWidth = screenWidth / 4;
            }
            tv_top_left.getLayoutParams().width = (screenWidth);
            tv_top_right.getLayoutParams().width = (screenWidth);
            tv_bottom_left.getLayoutParams().width = (screenWidth);
            alltoatal.getLayoutParams().width = (screenWidth);
            initViews();
        }

    }

    public void getBatchTimeOfPark()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("userId", commembertab.getId());
        params.addQueryStringParameter("areaid", areaid);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "NCZ_getContractBreakoffData");
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
                    if (result.getRows().size() > 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                        DensityUtil densityUtil = new DensityUtil(NCZ_ContractBreakOff.this);
                        screenWidth = densityUtil.getScreenWidth();
                        int size = listData.get(0).getContracttabList().size();
                        if (size == 1)
                        {
                            screenWidth = screenWidth / 4;
                        } else if (size == 2)
                        {
                            screenWidth = screenWidth / 4;
                        } else
                        {
                            screenWidth = screenWidth / 4;
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
                    AppContext.makeToast(NCZ_ContractBreakOff.this, "error_connectDataBase");
                    dialog.loadingTip(getText(R.string.error_data).toString());
                    return;
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_ContractBreakOff.this, "error_connectServer");
                dialog.loadingTip(getText(R.string.error_network).toString());
            }
        });
    }

    private void initViews()
    {
        //初始化控件及数据
        mHScrollViews = new ArrayList<CustomHorizontalScrollView_Allitem>();
        ll_total.removeAllViews();
        ll_park.removeAllViews();
        int allnumber = 0;
        LayoutInflater inflater = (LayoutInflater) NCZ_ContractBreakOff.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < listData.get(0).getContracttabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.contractbreakoff_parkitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(listData.get(0).getContracttabList().get(i).getContractname());
            ll_park.addView(view);
        }
        for (int i = 0; i < listData.get(0).getContracttabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.contractbreakoff_totalitem, null);
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

//        CustomHorizontalScrollView_Allitem headerScroll = (CustomHorizontalScrollView_Allitem) findViewById(R.id.item_scroll_title);
//        CustomHorizontalScrollView_Allitem totalScroll = (CustomHorizontalScrollView_Allitem) findViewById(R.id.totalScroll);
        // 添加头滑动事件
        mHScrollViews.add(item_scroll_title);
        mHScrollViews.add(totalScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void addHViews(final CustomHorizontalScrollView_Allitem hScrollView)
    {
        if (!mHScrollViews.isEmpty())
        {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView_Allitem scrollView = mHScrollViews.get(size - 1);
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
        for (CustomHorizontalScrollView_Allitem scrollView : mHScrollViews)
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
            public TextView tv_data;
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            convertView = LayoutInflater.from(NCZ_ContractBreakOff.this).inflate(R.layout.contractbreakoff_scrolladapter_item, null);
            if (position % 2 == 0)
            {
                convertView.setBackgroundResource(R.color.bg_table_row);
            } else
            {
                convertView.setBackgroundResource(R.color.white);
            }
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
                View view = LayoutInflater.from(NCZ_ContractBreakOff.this).inflate(R.layout.contractbreakoff_dataitem, null);
                listItemView.tv_data = (TextView) view.findViewById(R.id.tv_data);
                listItemView.tv_data.setText(listData.get(position).getContracttabList().get(i).getAllnumber());
                listItemView.tv_data.getLayoutParams().width = (screenWidth);
                ll_middle.addView(view);

                listItemView.tv_data.requestFocusFromTouch();
                listItemView.tv_data.setTag(R.id.tag_areaid, listData.get(position).getContracttabList().get(i).getContractid());
                listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position).getBatchTime());
                listItemView.tv_data.setTag(R.id.tag_number, listData.get(position).getContracttabList().get(i).getAllnumber());
                listItemView.tv_data.setTag(R.id.tag_areaname, listData.get(position).getContracttabList().get(i).getContractname());
                listItemView.tv_data.setOnClickListener(clickListener);

            }
            // 第一次初始化的时候装进来
            CustomHorizontalScrollView_Allitem customHorizontalScrollView = (CustomHorizontalScrollView_Allitem) convertView.findViewById(R.id.item_chscroll_scroll);
            addHViews(customHorizontalScrollView);
            customHorizontalScrollView.setCuttomOntouch(customOntouch);
//            addHViews((CustomHorizontalScrollView_Allitem) convertView.findViewById(R.id.item_chscroll_scroll));
            return convertView;
        }
    }

    @Override
    public void customOnTouchEvent(HorizontalScrollView horizontalScrollView)
    {
        mTouchView = horizontalScrollView;
    }

    @Override
    public void customOnScrollChanged(int l, int t, int oldl, int oldt)
    {
        for (CustomHorizontalScrollView_Allitem scrollView : mHScrollViews)
        {
            // 防止重复滑动
            if (mTouchView != scrollView) scrollView.smoothScrollTo(l, t);
        }
    }

    @Override
    public HorizontalScrollView getmTouchView()
    {
        return mTouchView;
    }

    // 测试点击的事件
    protected View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            v.setBackgroundResource(R.drawable.linearlayout_green_round_selector);
//            String number = (String) v.getTag(R.id.tag_number);
//            String batchTimes = (String) v.getTag(R.id.tag_batchtime);
//            String areaid = (String) v.getTag(R.id.tag_areaid);
//            String areaname = (String) v.getTag(R.id.tag_areaname);
//            if (number.equals("0"))
//            {
//                Toast.makeText(NCZ_ContractBreakOff.this, "该片区该批次暂无断蕾数据", Toast.LENGTH_SHORT).show();
//            } else
//            {
//                Intent intent = new Intent(NCZ_ContractBreakOff.this, NCZ_ContractBreakOffActivity_.class);
//                intent.putExtra("areaid", areaid);
//                intent.putExtra("areaname", areaname);
//                intent.putExtra("batchTime", batchTimes);
//                NCZ_ContractBreakOff.this.startActivity(intent);
//            }

        }
    };
}
