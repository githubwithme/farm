package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomHorizontalScrollView_BreakOff;
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
import java.util.Map;

/**
 * Created by ${hmj} on 2016/6/57.
 */
@EActivity(R.layout.ncz_breakoffactivity)
public class NCZ_BreakOffActivity extends Activity
{
    List<BatchTime> listData = null;
    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_BreakOff> mHScrollViews = new ArrayList<CustomHorizontalScrollView_BreakOff>();
    private ScrollAdapter mAdapter;
    //    String[] item_batchtimedata;
    //    String[] item_parkid;
    int screenWidth = 0;
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
//    @ViewById
//    TextView shuaxin;
    @ViewById
    RelativeLayout rl_tab;

//    @Click
//    void shuaxin()
//    {
//        if (!id.equals("")) getIsStartBreakOff(id);
//    }

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void rl_tab()
    {
        showPop_title();
    }

    @AfterViews
    void aftercreat()
    {
        getlistdata();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(NCZ_BreakOffActivity.this);
    }

    public void getIsStartBreakOff(final String parkid)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkid", parkid);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "IsStartBreakOff");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<BatchTime> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() > 0)
                    {
                        rl_view.setVisibility(View.GONE);
                        cz_startdl.setVisibility(View.GONE);
                        getBatchTimeOfPark(parkid);
//                        getNewSaleList_test();

                    } else
                    {
                        cz_startdl.setVisibility(View.VISIBLE);
                        rl_view.setVisibility(View.GONE);
                    }

                } else
                {
                    AppContext.makeToast(NCZ_BreakOffActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_BreakOffActivity.this, "error_connectServer");
            }
        });
    }

    private void getlistdata()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_BreakOffActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
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
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                        listpeople.addAll(listNewData);
                        id = listNewData.get(0).getId();
                        name = listNewData.get(0).getParkName();
                        tv_title.setText(listNewData.get(0).getParkName());
                        getIsStartBreakOff(id);
                    } else
                    {
                        listNewData = new ArrayList<Wz_Storehouse>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_BreakOffActivity.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_BreakOffActivity.this, "error_connectServer");

            }
        });

    }

    public void showPop_title()
    {
        LayoutInflater layoutInflater = (LayoutInflater) NCZ_BreakOffActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing()))
                {
                    pw_tab.dismiss();
//                    iv_dowm_tab.setImageResource(R.drawable.ic_down);
                    return true;
                }
                return false;
            }
        });
        pv_tab.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_tab.isShowing())
                {
                    pw_tab.dismiss();
//                    iv_dowm_tab.setImageResource(R.drawable.ic_down);
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_tab.showAsDropDown(rl_tab, 0, 0);
        pw_tab.setOutsideTouchable(true);


        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
        ncz_dlAdapter = new NCZ_DLAdapter(NCZ_BreakOffActivity.this, listpeople);
        listview.setAdapter(ncz_dlAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
            {
                id = listpeople.get(postion).getId();
                name = listpeople.get(postion).getParkName();
                pw_tab.dismiss();
                tv_title.setText(listpeople.get(postion).getParkName());
                getIsStartBreakOff(listpeople.get(postion).getId());

            }
        });
    }


    public void getBatchTimeOfPark(String parkid)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("userId", commembertab.getId());
        params.addQueryStringParameter("parkid", parkid);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "NCZ_getBreakOffData");
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
                    if (result.getAffectedRows() > 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                        DensityUtil densityUtil = new DensityUtil(NCZ_BreakOffActivity.this);
                        screenWidth = densityUtil.getScreenWidth();
                        int size = listData.get(0).getAreatabList().size();
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
                        cz_startdl.setVisibility(View.GONE);

                    } else
                    {
                        listData = new ArrayList<BatchTime>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_BreakOffActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_BreakOffActivity.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        int allnumber = 0;
        LayoutInflater inflater = (LayoutInflater) NCZ_BreakOffActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < listData.get(0).getAreatabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.breakoff_parkitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(listData.get(0).getAreatabList().get(i).getareaName());
            ll_park.addView(view);
        }
        for (int i = 0; i < listData.get(0).getAreatabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.breakoff_totalitem, null);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_total.getLayoutParams().width = (screenWidth);
            int totalnumber = 0;
            for (int j = 0; j < listData.size(); j++)
            {
                totalnumber = totalnumber + Integer.valueOf(listData.get(j).getAreatabList().get(i).getAllnumber());
            }
            tv_total.setText(String.valueOf(totalnumber));
            ll_total.addView(view);
            allnumber = allnumber + totalnumber;
        }
        alltoatal.setText(String.valueOf(allnumber));

        Map<String, String> data = null;
        CustomHorizontalScrollView_BreakOff headerScroll = (CustomHorizontalScrollView_BreakOff) findViewById(R.id.item_scroll_title);
        CustomHorizontalScrollView_BreakOff totalScroll = (CustomHorizontalScrollView_BreakOff) findViewById(R.id.totalScroll);
        // 添加头滑动事件
        mHScrollViews.add(headerScroll);
        mHScrollViews.add(totalScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void addHViews(final CustomHorizontalScrollView_BreakOff hScrollView)
    {
        if (!mHScrollViews.isEmpty())
        {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView_BreakOff scrollView = mHScrollViews.get(size - 1);
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
        for (CustomHorizontalScrollView_BreakOff scrollView : mHScrollViews)
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
            convertView = LayoutInflater.from(NCZ_BreakOffActivity.this).inflate(R.layout.breakoff_scrolladapter_item, null);
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
            listItemView.item_total = (TextView) convertView.findViewById(R.id.item_total);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
            listItemView.item_total.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);
            listItemView.item_titlev.setText(listData.get(position).getBatchTime());
            int totalnumber = 0;
            List<areatab> list = listData.get(position).getAreatabList();
            for (int j = 0; j < list.size(); j++)
            {
                totalnumber = totalnumber + Integer.valueOf(list.get(j).getAllnumber());
            }
            listItemView.item_total.setText(String.valueOf(totalnumber));

            for (int i = 0; i < listData.get(position).getAreatabList().size(); i++)
            {
                View view = LayoutInflater.from(NCZ_BreakOffActivity.this).inflate(R.layout.breakoff_dataitem, null);
                listItemView.tv_data = (TextView) view.findViewById(R.id.tv_data);
                listItemView.tv_data.setText(listData.get(position).getAreatabList().get(i).getAllnumber());
                listItemView.tv_data.getLayoutParams().width = (screenWidth);
                ll_middle.addView(view);

                listItemView.tv_data.requestFocusFromTouch();
                listItemView.tv_data.setTag(R.id.tag_areaid, listData.get(position).getAreatabList().get(i).getAreaid());
                listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position).getBatchTime());
                listItemView.tv_data.setTag(R.id.tag_areaname, listData.get(position).getAreatabList().get(i).getareaName());
                listItemView.tv_data.setOnClickListener(clickListener);

            }
            // 第一次初始化的时候装进来
            addHViews((CustomHorizontalScrollView_BreakOff) convertView.findViewById(R.id.item_chscroll_scroll));
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
            String batchTimes = (String) v.getTag(R.id.tag_batchtime);
            String areaid = (String) v.getTag(R.id.tag_areaid);
            String areaname = (String) v.getTag(R.id.tag_areaname);
            Intent intent = new Intent(NCZ_BreakOffActivity.this, NCZ_ContractBreakOffActivity_.class);
            intent.putExtra("areaid", areaid);
            intent.putExtra("areaname", areaname);
            intent.putExtra("batchTime", batchTimes);
            NCZ_BreakOffActivity.this.startActivity(intent);
        }
    };
}
