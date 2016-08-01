package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.areatab;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/8/1.
 */
@EActivity(R.layout.pg_new_areasaleactivity)
public class PG_New_SaleActinity extends Activity implements CustomHorizontalScrollView_Allitem.CustomOntouch
{
    DialogFragment_WaitTip dialog;
    String parkid;
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
    TextView tv_parkname;
    @ViewById
    TextView tv_bottom_left;
    private String id;
    private String name;
    List<Wz_Storehouse> parklist = new ArrayList<Wz_Storehouse>();
    PopupWindow pw_tab;
    View pv_tab;
    @ViewById
    View line;
    //    PG_CKofListAdapter pg_cKlistAdapter;
    Adapter_Park adapter_park;
    com.farm.bean.commembertab commembertab;
    MyDialog myDialog;
    Fragment mContent = new Fragment();
    @ViewById
    LinearLayout cz_startdl;
    @ViewById
    TextView startdl;
    @ViewById
    TextView tv_timelimit;
    @ViewById
    RelativeLayout rl_view;


    @Click
    void ib_suspen_menu()
    {
//        if (listData.size() == 0)
//        {
//            NCZ_getContractSaleData();
//        } else
//        {
            showPop_Menu();
//        }
    }



    @Click
    void btn_back()
    {
        finish();
    }


    @Click
    void tv_more()
    {
        Intent intent = new Intent(PG_New_SaleActinity.this, NCZ_SaleModuleActivity_.class);
        startActivity(intent);
    }

    @AfterViews
    void afterOncreate()
    {
        customOntouch = this;
        item_scroll_title.setCuttomOntouch(customOntouch);
        totalScroll.setCuttomOntouch(customOntouch);
//        getNewSaleList_test();
        NCZ_getContractSaleData();
        tv_parkname.setText(commembertab.getareaName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(PG_New_SaleActinity.this);
    }




    public void NCZ_getContractSaleData()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("userId", commembertab.getId());
        params.addQueryStringParameter("areaid", commembertab.getareaId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "NCZ_getContractSaleData");
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
                        DensityUtil densityUtil = new DensityUtil(PG_New_SaleActinity.this);
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
                        cz_startdl.setVisibility(View.GONE);

                    } else
                    {
                        listData = new ArrayList<BatchTime>();
                    }

                } else
                {
                    AppContext.makeToast(PG_New_SaleActinity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_New_SaleActinity.this, "error_connectServer");
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
        LayoutInflater inflater = (LayoutInflater) PG_New_SaleActinity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < listData.get(0).getContracttabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.areasale_parkitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(listData.get(0).getContracttabList().get(i).getContractname());


            ll_park.addView(view);
        }
        for (int i = 0; i < listData.get(0).getContracttabList().size(); i++)
        {
            View view = inflater.inflate(R.layout.areasale_totalitem, null);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_total.getLayoutParams().width = (screenWidth);
            int totalnumber = 0;
            for (int j = 0; j < listData.size(); j++)
            {
                totalnumber = totalnumber + Integer.valueOf(listData.get(j).getContracttabList().get(i).getAllnumber());
//                totalnumber = totalnumber + Integer.valueOf(listData.get(j).getAreatabList().get(i).getAllsalefor());
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
            convertView = LayoutInflater.from(PG_New_SaleActinity.this).inflate(R.layout.areasale_scrolladapter_item, null);
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
                View view = LayoutInflater.from(PG_New_SaleActinity.this).inflate(R.layout.areasale_dataitem, null);
                listItemView.tv_data = (TextView) view.findViewById(R.id.tv_data);
                listItemView.tv_data.setText(listData.get(position).getContracttabList().get(i).getAllnumber());
                listItemView.tv_data.getLayoutParams().width = (screenWidth);
                ll_middle.addView(view);

                listItemView.tv_data.requestFocusFromTouch();
                listItemView.tv_data.setTag(R.id.tag_areaid, listData.get(position).getContracttabList().get(i).getContractid());
                listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position).getBatchTime());
                listItemView.tv_data.setTag(R.id.tag_number, listData.get(position).getContracttabList().get(i).getAllnumber());
                listItemView.tv_data.setTag(R.id.tag_areaname, listData.get(position).getContracttabList().get(i).getareaName());
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
//                Toast.makeText(PG_New_SaleActinity.this, "该片区该批次暂无断蕾数据", Toast.LENGTH_SHORT).show();
//            } else
//            {
//                Intent intent = new Intent(PG_New_SaleActinity.this, NCZ_ContractBatchTimeSale_.class);
//                intent.putExtra("areaid", areaid);
//                intent.putExtra("areaname", areaname);
//                intent.putExtra("batchTime", batchTimes);
//                PG_New_SaleActinity.this.startActivity(intent);
//            }

        }
    };



    public void showPop_Menu()
    {
        LayoutInflater layoutInflater = (LayoutInflater) PG_New_SaleActinity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        pv_tab = layoutInflater.inflate(R.layout.pop_salemenu, null);// 外层
        pv_tab = layoutInflater.inflate(R.layout.mainpeople_salemenu, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing()))
                {
                    pw_tab.dismiss();
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
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置layout在PopupWindow中显示的位置
        pw_tab.setAnimationStyle(R.style.bottominbottomout);
        pw_tab.showAtLocation(getLayoutInflater().inflate(R.layout.ncz_areasaleactivity, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        pw_tab.setOutsideTouchable(true);

        RelativeLayout rl_createOrder = (RelativeLayout) pv_tab.findViewById(R.id.rl_createOrder);
        RelativeLayout rl_orderManager = (RelativeLayout) pv_tab.findViewById(R.id.rl_orderManager);
        ImageView iv_cancle = (ImageView) pv_tab.findViewById(R.id.iv_cancle);

        iv_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_tab.dismiss();
            }
        });
        rl_createOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_tab.dismiss();
//                Intent intent = new Intent(CZ_NEW_AreaSaleActivty.this, NCZ_SelectProduct_.class);
                Intent intent = new Intent(PG_New_SaleActinity.this, PG_SelectProduct_.class);
                startActivity(intent);
            }
        });
        rl_orderManager.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_tab.dismiss();
                Intent intent = new Intent(PG_New_SaleActinity.this, PG_OrderManager_.class);
                startActivity(intent);
            }
        });


    }



    public class Adapter_Park extends BaseAdapter
    {
        private Context context;
        private List<Wz_Storehouse> listItems;
        private LayoutInflater listContainer;
        Wz_Storehouse wz_storehouse;

        class ListItemView
        {
            public TextView tv_yq;
            public View view_select;
        }

        public Adapter_Park(Context context, List<Wz_Storehouse> data)
        {
            this.context = context;
            this.listContainer = LayoutInflater.from(context);
            this.listItems = data;
        }

        HashMap<Integer, View> lmap = new HashMap<Integer, View>();

        public View getView(int position, View convertView, ViewGroup parent)
        {
            wz_storehouse = listItems.get(position);
            ListItemView listItemView = null;
            if (lmap.get(position) == null)
            {
                convertView = listContainer.inflate(R.layout.park_item, null);
                listItemView = new ListItemView();
                listItemView.tv_yq = (TextView) convertView.findViewById(R.id.tv_yq);
                listItemView.view_select = (View) convertView.findViewById(R.id.view_select);
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }
            if (tv_parkname.getText().equals(wz_storehouse.getParkName()))
            {
                listItemView.view_select.setVisibility(View.VISIBLE);
            } else
            {
                listItemView.view_select.setVisibility(View.GONE);
            }
            listItemView.tv_yq.setText(wz_storehouse.getParkName() + "库存量");
            return convertView;
        }

        @Override
        public int getCount()
        {
            return listItems.size();
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

    public void showDialog_waitTip()
    {
        dialog = new DialogFragment_WaitTip();
        Bundle bundle1 = new Bundle();
        dialog.setArguments(bundle1);
        dialog.show(PG_New_SaleActinity.this.getFragmentManager(), "TIP");
    }

}
