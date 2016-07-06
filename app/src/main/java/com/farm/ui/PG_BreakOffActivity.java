package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomHorizontalScrollView_PGBreakOff;
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
import java.util.Map;

/**
 * Created by ${hmj} on 2016/6/57.
 */
@EActivity(R.layout.pg_breakoffactivity)
public class PG_BreakOffActivity extends Activity
{
    EditText et_number;
    List<BatchTime> listData = null;
    CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_PGBreakOff> mHScrollViews = new ArrayList<CustomHorizontalScrollView_PGBreakOff>();
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
    RelativeLayout rl_dl;

    @AfterViews
    void afterOncreate()
    {
        getActionBar().hide();
        getfarmSalesData();
//        getNewSaleList_test();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(PG_BreakOffActivity.this, "getsaledata", BatchTime.class);
        if (listData != null)
        {
            DensityUtil densityUtil = new DensityUtil(PG_BreakOffActivity.this);
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
        commembertab commembertab = AppContext.getUserInfo(PG_BreakOffActivity.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("areaid", commembertab.getareaId());
        params.addQueryStringParameter("action", "PG_getContractBreakoffData");
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
                        rl_dl.setVisibility(View.GONE);
                        listData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                        DensityUtil densityUtil = new DensityUtil(PG_BreakOffActivity.this);
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
                    AppContext.makeToast(PG_BreakOffActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_BreakOffActivity.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        LayoutInflater inflater = (LayoutInflater) PG_BreakOffActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < listData.get(0).getContracttabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.pg_breakoff_contractitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(listData.get(0).getContracttabList().get(i).getContractname());
            ll_park.addView(view);
        }
        for (int i = 0; i < listData.get(0).getContracttabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.pg_breakoff_totalitem, null);
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
        CustomHorizontalScrollView_PGBreakOff headerScroll = (CustomHorizontalScrollView_PGBreakOff) findViewById(R.id.item_scroll_title);
        CustomHorizontalScrollView_PGBreakOff totalScroll = (CustomHorizontalScrollView_PGBreakOff) findViewById(R.id.totalScroll);
        // 添加头滑动事件
        mHScrollViews.add(headerScroll);
        mHScrollViews.add(totalScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void addHViews(final CustomHorizontalScrollView_PGBreakOff hScrollView)
    {
        if (!mHScrollViews.isEmpty())
        {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView_PGBreakOff scrollView = mHScrollViews.get(size - 1);
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
        for (CustomHorizontalScrollView_PGBreakOff scrollView : mHScrollViews)
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
            convertView = LayoutInflater.from(PG_BreakOffActivity.this).inflate(R.layout.pg_breakoff_scrolladapteritem, null);
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
                View view = LayoutInflater.from(PG_BreakOffActivity.this).inflate(R.layout.pg_breakoff_dataitem, null);
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
            addHViews((CustomHorizontalScrollView_PGBreakOff) convertView.findViewById(R.id.item_chscroll_scroll));
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
            String contractid = (String) v.getTag(R.id.tag_kg);
            String contractname = (String) v.getTag(R.id.tag_parkname);
        }
    };

    public void showDialog_editBreakoffinfo(final contractTab contractTab, final Button button, final String oldNumber)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(PG_BreakOffActivity.this).inflate(R.layout.customdialog_editcontractsale, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(PG_BreakOffActivity.this, R.style.MyDialog, dialog_layout);
        et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
        et_number.setText(contractTab.getAllsalefor());
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (et_number.getText().toString().equals(""))
                {
                    Toast.makeText(PG_BreakOffActivity.this, "请先填写数量", Toast.LENGTH_SHORT).show();
                } else if (oldNumber.equals("0"))//第一次添加
                {
//                    addNewData();
                } else//修改数据
                {
//                    updateData();
                }

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
            }
        });
        customDialog_editSaleInInfo.show();
    }

//    public void updateData(contractTab contracttab, String number)
//    {
//        String difference = list_BreakOff_New.get(0).getnumberofbreakoff();
//        updateBreakOff(list_BreakOff_New.get(0).getUuid(), difference, number);
//        list_BreakOff_New.get(0).setnumberofbreakoff(number);
//    }
//
//    public void addNewData(contractTab contracttab, String number)
//    {
//        String uuid = java.util.UUID.randomUUID().toString();
//        BreakOff_New breakoff = new BreakOff_New();
//        breakoff.setUuid(uuid);
//        breakoff.setuid(contracttab.getuId());
//        breakoff.setareaid(contracttab.getAreaId());
//        breakoff.setYear(utils.getYear());
//        breakoff.setparkid(contracttab.getparkId());
//        breakoff.setparkname(contracttab.getparkName());
//        breakoff.setareaname(contracttab.getareaName());
//        breakoff.setBatchColor(batchcolor);
//        breakoff.setBatchTime(batchtime);
//        breakoff.setBreakofftime(utils.getTime());
//        breakoff.setcontractid(contracttab.getid());
//        breakoff.setcontractname(contracttab.getContractNum());
//        breakoff.setLat("");
//        breakoff.setLatlngsize("");
//        breakoff.setLng("");
//        breakoff.setnumberofbreakoff(number);
//        breakoff.setregdate(utils.getTime());
//        breakoff.setStatus("0");
//        breakoff.setWeight("0");
//        breakoff.setXxzt("0");
//        list_BreakOff_New.add(breakoff);
//
//
//        SellOrderDetail_New sellorderdetail = new SellOrderDetail_New();
//        sellorderdetail.setYear(utils.getYear());
//        sellorderdetail.setXxzt("0");
//        sellorderdetail.setuid(contracttab.getuId());
//        sellorderdetail.setactuallat("");
//        sellorderdetail.setactuallatlngsize("");
//        sellorderdetail.setactuallng("");
//        sellorderdetail.setactualnote("");
//        sellorderdetail.setactualnumber("");
//        sellorderdetail.setactualprice("");
//        sellorderdetail.setactualweight("");
//        sellorderdetail.setareaid(contracttab.getAreaId());
//        sellorderdetail.setareaname(contracttab.getareaName());
//        sellorderdetail.setcontractid(contracttab.getid());
//        sellorderdetail.setcontractname(contracttab.getContractNum());
//        sellorderdetail.setBatchTime(batchtime);
//        sellorderdetail.setisSoldOut("");
//        sellorderdetail.setparkid(contracttab.getparkId());
//        sellorderdetail.setparkname(contracttab.getparkName());
//        sellorderdetail.setPlanlat("");
//        sellorderdetail.setplanlatlngsize("");
//        sellorderdetail.setplanlng("");
//        sellorderdetail.setplannote("");
//        sellorderdetail.setplanprice("");
//        sellorderdetail.setplanweight("");
//        sellorderdetail.setplannumber(number);
//        sellorderdetail.setreg(utils.getTime());
//        sellorderdetail.setreg(utils.getTime());
//        sellorderdetail.setType("salefor");
//        sellorderdetail.setXxzt("0");
//        sellorderdetail.setstatus("0");
//        sellorderdetail.setUuid(uuid);
//        sellorderdetail.setYear(utils.getYear());
//        sellorderdetail.setsaleid("");
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("{\"breakoff\": [");
//        builder.append(JSON.toJSONString(breakoff));
//        builder.append("],\"sellorderdetail\": [");
//        builder.append(JSON.toJSONString(sellorderdetail));
//        builder.append("]");
//        builder.append("} ");
//        saveBreakOffList(builder.toString());
//    }
}
