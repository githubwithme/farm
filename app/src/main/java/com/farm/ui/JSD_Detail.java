package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.PG_CBF_CLAdapyer;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BreakOff_New;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomHorizontalScrollView_PGBreakOff;
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
 * Created by hasee on 2016/7/15.
 */
@EActivity(R.layout.jsd_detail)
public class JSD_Detail extends Activity
{
    private ListView mListView;
    //界面
    public HorizontalScrollView mTouchView;
    private ScrollAdapter mAdapter;
    protected List<CustomHorizontalScrollView_JSDDetail> mHScrollViews = new ArrayList<CustomHorizontalScrollView_JSDDetail>();
    int screenWidth = 0;
    @ViewById
    LinearLayout ll_park;
    @ViewById
    LinearLayout ll_total;
    @ViewById
    TextView alltoatal;
    /*  @ViewById
      TextView tv_top_left;*/
    @ViewById
    RelativeLayout tv_top_left;
    @ViewById
    TextView tv_top_right;
    @ViewById
    TextView tv_bottom_left;


    List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
    PG_CBF_CLAdapyer pg_cbf_adapter;
    @ViewById
    ListView liat_jsd;
    SellOrder_New sellOrder_new;
    String broadcast;

    @Click
    void button_add()
    {
        Intent intent = new Intent(JSD_Detail.this, Charge_Settlement_.class);
        intent.putExtra("bean", sellOrder_new);
        startActivity(intent);
/*        Intent intent = new Intent(JSD_Detail.this, RecoveryDetail_.class);
        intent.putExtra("bean", sellOrder_new);
        startActivity(intent);*/

    }

    @AfterViews
    void afterview()
    {
        getDetailSecBysettleId();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("bean");
//        broadcast = getIntent().getStringExtra("broadcast");
        IntentFilter intentfilter_update = new IntentFilter(AppContext.UPDATEMESSAGE_PGDETAIL_UPDATE_DELETE);
        registerReceiver(receiver_update, intentfilter_update);
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getDetailSecBysettleId();
        }
    };

    public void getDetailSecBysettleId()
    {
        commembertab commembertab = AppContext.getUserInfo(JSD_Detail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", sellOrder_new.getUuid());
        params.addQueryStringParameter("action", "getDetailSecBysettleId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                listData = new ArrayList<SellOrder_New>();
                String a = responseInfo.result;
                List<SellOrder_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
//                    if (result.getAffectedRows() != 0)
//                    {

                    listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);
                    listData.addAll(listNewData);

                    DensityUtil densityUtil = new DensityUtil(JSD_Detail.this);
                    screenWidth = densityUtil.getScreenWidth();
                    int size = 3;
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



//                    } else
                    {
                        listNewData = new ArrayList<SellOrder_New>();
                    }

                } else
                {
                    AppContext.makeToast(JSD_Detail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(JSD_Detail.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        int allnumber = 0;
        mHScrollViews.clear();
        ll_park.removeAllViews();
        ll_total.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) JSD_Detail.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        String[] name = new String[]{"车牌号","实际金额", "合计金额", "总净重", "审批情况", "正品结算金额", "正品单价", "正品净重", "总包装费", "总搬运费"};
        for (int i = 0; i < name.length; i++)
        {
            View view = inflater.inflate(R.layout.pg_breakoff_contractitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(name[i]);   //最上边滑动承包区
            ll_park.addView(view);
        }

        //最下面滑动合计
        for (int i = 0; i < name.length; i++)
        {
            View view = inflater.inflate(R.layout.pg_breakoff_totalitem, null);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_total.getLayoutParams().width = (screenWidth);
            double totalnumber = 0;
            switch (i)
            {
                case 0:
                    tv_total.setText(listData.size()+"车");
                    ll_total.addView(view);
                    break;
                case 1:

                    for (int j = 0; j < listData.size(); j++)
                    {
                        if (!listData.get(j).getActualMoney().equals(""))
                        {
                            totalnumber = totalnumber + Double.valueOf(listData.get(j).getActualMoney());
                        }
                    }
                    tv_total.setText(String.valueOf(totalnumber));
                    ll_total.addView(view);
                    break;
                case 2:
                    totalnumber = 0;
                    for (int j = 0; j < listData.size(); j++)
                    {
                        if (!listData.get(j).getTotalFee().equals(""))
                        {
                            totalnumber = totalnumber + Double.valueOf(listData.get(j).getTotalFee());
                        }
                    }
                    tv_total.setText(String.valueOf(totalnumber));
                    ll_total.addView(view);
                    break;
                case 3:
                    totalnumber = 0;
                    for (int j = 0; j < listData.size(); j++)
                    {
                        if (!listData.get(j).getTotalWeight().equals(""))
                        {
                            totalnumber = totalnumber + Double.valueOf(listData.get(j).getTotalWeight());
                        }
                    }
                    tv_total.setText(String.valueOf(totalnumber));
                    ll_total.addView(view);
                    break;
                case 4:

                    tv_total.setText("-");
                    ll_total.addView(view);
                    break;
                case 5:
                    totalnumber = 0;
                    for (int j = 0; j < listData.size(); j++)
                    {
                        if (!listData.get(j).getQualityBalance().equals(""))
                        {
                            totalnumber = totalnumber + Double.valueOf(listData.get(j).getQualityBalance());
                        }
                    }
                    tv_total.setText(String.valueOf(totalnumber));
                    ll_total.addView(view);
                    break;
                case 6:
                    totalnumber = 0;
                    for (int j = 0; j < listData.size(); j++)
                    {
                        if (!listData.get(j).getActualprice().equals(""))
                        {
                            totalnumber = totalnumber + Double.valueOf(listData.get(j).getActualprice());
                        }
                    }
                    tv_total.setText(String.valueOf(totalnumber));
                    ll_total.addView(view);
                    break;
                case 7:
                    totalnumber = 0;
                    for (int j = 0; j < listData.size(); j++)
                    {
                        if (!listData.get(j).getQualityTotalWeight().equals(""))
                        {
                            totalnumber = totalnumber + Double.valueOf(listData.get(j).getQualityTotalWeight());
                        }
                    }
                    tv_total.setText(String.valueOf(totalnumber));
                    ll_total.addView(view);
                    break;
                case 8:
                    totalnumber = 0;
                    for (int j = 0; j < listData.size(); j++)
                    {
                        if (!listData.get(j).getPackFee().equals(""))
                        {
                            totalnumber = totalnumber + Double.valueOf(listData.get(j).getPackFee());
                        }
                    }
                    tv_total.setText(String.valueOf(totalnumber));
                    ll_total.addView(view);
                    break;
                case 9:
                    totalnumber = 0;
                    for (int j = 0; j < listData.size(); j++)
                    {
                        if (!listData.get(j).getCarryFee().equals(""))
                        {
                            totalnumber = totalnumber + Double.valueOf(listData.get(j).getCarryFee());
                        }
                    }
                    tv_total.setText(String.valueOf(totalnumber));
                    ll_total.addView(view);
                    break;
            }
//            String [] name=new String []{"实际金额","合计金额","总净重",,"正品结算金额","正品单价","正品净重","总包装费","总搬运费"};
        }
        Map<String, String> data = null;
        CustomHorizontalScrollView_JSDDetail headerScroll = (CustomHorizontalScrollView_JSDDetail) findViewById(R.id.item_scroll_title);
        CustomHorizontalScrollView_JSDDetail totalScroll = (CustomHorizontalScrollView_JSDDetail) findViewById(R.id.totalScroll);
        // 添加头滑动事件
        mHScrollViews.add(headerScroll);
        mHScrollViews.add(totalScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);

    }


    public void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        for (CustomHorizontalScrollView_JSDDetail scrollView : mHScrollViews)
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
            public TextView btn_data;
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

            // 获取list_item布局文件的视图
            convertView = LayoutInflater.from(JSD_Detail.this).inflate(R.layout.jsd_detail_scrolladapteritem, null);
            listItemView = new ListItemView();
//            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
            listItemView.item_total = (TextView) convertView.findViewById(R.id.item_total);

//            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
            listItemView.item_total.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);
            if (position % 2 == 0)
            {
                convertView.setBackgroundResource(R.color.light_gray);
            } else
            {
                convertView.setBackgroundResource(R.color.white);
            }

            if (listData.get(position).getIsNeedAudit().equals("0"))
            {

            } else if (listData.get(position).getIsNeedAudit().equals("1"))
            {
                listItemView.item_total.setTextColor(JSD_Detail.this.getResources().getColor(R.color.green));
            } else if (listData.get(position).getIsNeedAudit().equals("-1"))
            {
                listItemView.item_total.setTextColor(JSD_Detail.this.getResources().getColor(R.color.red));
            }
            listItemView.item_total.setTag(R.id.tag_batchtime, listData.get(position));
            listItemView.item_total.setOnClickListener(clickListener);
            listItemView.item_total.setText("结算单"+(position+1));

            for (int i = 0; i < 9; i++)
            {
                View view = LayoutInflater.from(JSD_Detail.this).inflate(R.layout.pg_breakoff_dataitem, null);
                listItemView.btn_data = (TextView) view.findViewById(R.id.tv_data);

                switch (i)
                {

                    case 0:
                        listItemView.btn_data.setText(listData.get(position).getPlateNumber());
                        String a=listData.get(position).getPlateNumber();
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 1:
                        listItemView.btn_data.setText(listData.get(position).getActualMoney());
                        String b=listData.get(position).getActualMoney();
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 2:
                        listItemView.btn_data.setText(listData.get(position).getTotalFee());
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 3:
                        listItemView.btn_data.setText(listData.get(position).getTotalWeight());
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 4:
                        if (listData.get(position).getIsNeedAudit().equals("0"))
                        {
                            listItemView.btn_data.setText("待审批");
                        } else if (listData.get(position).getIsNeedAudit().equals("1"))
                        {
                            listItemView.btn_data.setText("审批通过");
                        } else if (listData.get(position).getIsNeedAudit().equals("-1"))
                        {
                            listItemView.btn_data.setText("审批不通过");
                        }

                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 5:
                        listItemView.btn_data.setText(listData.get(position).getQualityBalance());
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 6:
                        listItemView.btn_data.setText(listData.get(position).getActualprice());
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 7:
                        listItemView.btn_data.setText(listData.get(position).getQualityTotalWeight());
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 8:
                        listItemView.btn_data.setText(listData.get(position).getPackFee());
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                    case 9:
                        listItemView.btn_data.setText(listData.get(position).getCarryFee());
                        listItemView.btn_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.btn_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.btn_data.setOnClickListener(clickListener);
                        break;
                }
            }
            // 第一次初始化的时候装进来
            addHViews((CustomHorizontalScrollView_JSDDetail) convertView.findViewById(R.id.item_chscroll_scroll));


            return convertView;
        }
    }

    public void addHViews(final CustomHorizontalScrollView_JSDDetail hScrollView)
    {
        if (!mHScrollViews.isEmpty())
        {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView_JSDDetail scrollView = mHScrollViews.get(size - 1);
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

    // 测试点击的事件
    protected View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            SellOrder_New sellOrder_news = new SellOrder_New();
            sellOrder_news = (SellOrder_New) v.getTag(R.id.tag_batchtime);
            Intent intent = new Intent(JSD_Detail.this, Charge_UpdateSettlement_.class);
            intent.putExtra("bean", sellOrder_news);
            startActivity(intent);
        }
    };


}
