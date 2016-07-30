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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.PeopelList;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.contractTab;
import com.farm.bean.parktab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditSaleInInfo;
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
 * Created by ${hmj} on 2016/6/57.
 */
@EActivity(R.layout.ncz_selectproduct)
public class NCZ_SelectProduct extends Activity implements CustomHorizontalScrollView_Allitem.CustomOntouch
{
    List<Wz_Storehouse> parklist = new ArrayList<Wz_Storehouse>();
    DialogFragment_WaitTip dialog;
    String uuid;
    EditText et_number;
    CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
    List<parktab> list_park = null;
    String parkid;
    @ViewById
    CustomHorizontalScrollView_Allitem item_scroll_title;
//    @ViewById
//    CustomHorizontalScrollView_Allitem totalScroll;

    CustomHorizontalScrollView_Allitem.CustomOntouch customOntouch = null;

    List<contractTab> listData = null;
    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_Allitem> mHScrollViews = null;
    private ScrollAdapter mAdapter;
    int screenWidth = 0;
    @ViewById
    LinearLayout ll_park;
    @ViewById
    View view_line;
    //    @ViewById
//    LinearLayout ll_total;
//    @ViewById
//    TextView alltoatal;
    @ViewById
    TextView tv_top_left;
    //    @ViewById
//    TextView tv_top_right;
//    @ViewById
//    TextView tv_bottom_left;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_allnumber;
    @ViewById
    TextView tv_selectnumber;
    @ViewById
    TextView tv_parkname;
    //    @ViewById
//    RelativeLayout rl_upload;
//    @ViewById
//    TextView tv_totalnumber;
    private String id;
    private String name;
    PopupWindow pw_tab;
    View pv_tab;
    Adapter_Park adapter_park;
    com.farm.bean.commembertab commembertab;
    MyDialog myDialog;
    Fragment mContent = new Fragment();

    @ViewById
    TextView tv_nodatatip;


    @Click
    void btn_back()
    {
        cancleOrder();
    }

    @Click
    void btn_cancleorder()
    {
        cancleOrder();
    }

    @Click
    void ll_switchpark()
    {
        if (parklist.size() == 0)
        {
            getParkList();
        } else
        {
            showPop_ParkName();
        }
    }

    @Click
    void ll_productnumber()
    {
        Intent intent = new Intent(NCZ_SelectProduct.this, ProductSelectedList_.class);
        intent.putExtra("saleid", uuid);
        startActivity(intent);
    }

    @Click
    void btn_createorder()
    {
        Intent intent = new Intent(NCZ_SelectProduct.this, NCZ_CreateNewOrder_.class);
        intent.putExtra("uuid", uuid);
        NCZ_SelectProduct.this.startActivity(intent);
    }

    @Click
    void rl_tab()
    {
        showPop_park();
    }


    @AfterViews
    void afterOncreate()
    {
        customOntouch = this;
        item_scroll_title.setCuttomOntouch(customOntouch);
//        totalScroll.setCuttomOntouch(customOntouch);
        deleNewSaleAddsalefor();
//        getParknameByUid();
        getParkList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        uuid = java.util.UUID.randomUUID().toString();
        commembertab = AppContext.getUserInfo(NCZ_SelectProduct.this);
        parkid = getIntent().getStringExtra("parkid");
    }


    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(NCZ_SelectProduct.this, "getAllContractSaleData", contractTab.class);
        if (listData != null)
        {
            DensityUtil densityUtil = new DensityUtil(NCZ_SelectProduct.this);
            screenWidth = densityUtil.getScreenWidth();
            int size = listData.get(0).getBatchTimeList().size();
            if (size == 1)
            {
                screenWidth = screenWidth / 3;
            } else if (size == 2)
            {
                screenWidth = screenWidth / 3;
            } else
            {
                screenWidth = screenWidth / 3;
            }
            tv_top_left.getLayoutParams().width = (screenWidth);
//            tv_top_right.getLayoutParams().width = (screenWidth);
//            tv_bottom_left.getLayoutParams().width = (screenWidth);
//            alltoatal.getLayoutParams().width = (screenWidth);
            initViews();
            tv_nodatatip.setVisibility(View.GONE);
        }

    }

    public void showDialog_waitTip()
    {
        dialog = new DialogFragment_WaitTip();
        Bundle bundle1 = new Bundle();
        dialog.setArguments(bundle1);
        dialog.show(NCZ_SelectProduct.this.getFragmentManager(), "TIP");
    }

    private void getParkList()
    {
        showDialog_waitTip();
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(NCZ_SelectProduct.this);
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
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        parklist = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                    } else
                    {
                        parklist = new ArrayList<Wz_Storehouse>();
                        Toast.makeText(NCZ_SelectProduct.this, "暂无更多园区", Toast.LENGTH_SHORT).show();
                    }
                    tv_parkname.setText(parklist.get(0).getParkName() + "库存量");
                    getNewSaleList_test();
                } else
                {
                    AppContext.makeToast(NCZ_SelectProduct.this, "error_connectDataBase");
                    return;
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_SelectProduct.this, "error_connectServer");
                dialog.dismiss();
            }
        });

    }

//    private void getParknameByUid()
//    {
//        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(NCZ_SelectProduct.this);
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("action", "getcontractByUid");
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
//        {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo)
//            {
//                String a = responseInfo.result;
//                Result result = JSON.parseObject(responseInfo.result, Result.class);
//                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//                {
//                    if (result.getAffectedRows() != 0)
//                    {
//                        list_park = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
//                        tv_title.setText(list_park.get(0).getparkName());
//                        //        getBatchTimeOfPark();
//                        getNewSaleList_test();
//                    } else
//                    {
//                        list_park = new ArrayList<parktab>();
//                    }
//                } else
//                {
//                    AppContext.makeToast(NCZ_SelectProduct.this, "error_connectDataBase");
//
//                    return;
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg)
//            {
//                String a = error.getMessage();
//                AppContext.makeToast(NCZ_SelectProduct.this, "error_connectServer");
//
//            }
//        });
//
//    }

    public void showPop_park()
    {
        LayoutInflater layoutInflater = (LayoutInflater) NCZ_SelectProduct.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        pw_tab.showAsDropDown(view_line, 0, 0);
        pw_tab.setOutsideTouchable(true);


        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
        adapter_park = new Adapter_Park(NCZ_SelectProduct.this, parklist);
        listview.setAdapter(adapter_park);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
            {
                pw_tab.dismiss();
                tv_title.setText(list_park.get(postion).getparkName());
                //        getBatchTimeOfPark();
                getNewSaleList_test();
            }
        });
    }

    public void getBatchTimeOfPark()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("userId", commembertab.getId());
        params.addQueryStringParameter("parkid", parkid);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "NCZ_getAreaSaleData");
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
                        listData = JSON.parseArray(result.getRows().toJSONString(), contractTab.class);
                        DensityUtil densityUtil = new DensityUtil(NCZ_SelectProduct.this);
                        screenWidth = densityUtil.getScreenWidth();
                        int size = listData.get(0).getBatchTimeList().size();
                        if (size == 1)
                        {
                            screenWidth = screenWidth / 3;
                        } else if (size == 2)
                        {
                            screenWidth = screenWidth / 3;
                        } else
                        {
                            screenWidth = screenWidth / 3;
                        }
                        tv_top_left.getLayoutParams().width = (screenWidth);
//                        tv_top_right.getLayoutParams().width = (screenWidth);
//                        tv_bottom_left.getLayoutParams().width = (screenWidth);
//                        alltoatal.getLayoutParams().width = (screenWidth);
                        initViews();
                        tv_nodatatip.setVisibility(View.GONE);

                    } else
                    {
                        listData = new ArrayList<contractTab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_SelectProduct.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        //初始化控件及数据
        mHScrollViews = new ArrayList<CustomHorizontalScrollView_Allitem>();
//        ll_total.removeAllViews();
        ll_park.removeAllViews();
        int allnumber = 0;
        LayoutInflater inflater = (LayoutInflater) NCZ_SelectProduct.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < listData.get(0).getBatchTimeList().size(); i++)
        {
            View view = inflater.inflate(R.layout.nczselectproduct_titleitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(listData.get(0).getBatchTimeList().get(i).getBatchTime());
            tv_parkname.setTag(listData.get(0).getBatchTimeList().get(i).getBatchTime());
            tv_parkname.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    String areaid = (String) v.getTag();
//                    Intent intent = new Intent(NCZ_SelectProduct.this, NCZ_ContractSaleData_.class);
//                    intent.putExtra("areaid", areaid);
//                    NCZ_SelectProduct.this.startActivity(intent);
                }
            });
            ll_park.addView(view);
        }
//        for (int i = 0; i < listData.get(0).getBatchTimeList().size(); i++)
//        {
//            View view = inflater.inflate(R.layout.nczselectproduct_totalitem, null);
//            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
//            tv_total.getLayoutParams().width = (screenWidth);
//            int totalnumber = 0;
//            for (int j = 0; j < listData.size(); j++)
//            {
//                totalnumber = totalnumber + Integer.valueOf(listData.get(j).getBatchTimeList().get(i).getAllnumber());
//            }
//            tv_total.setText(String.valueOf(totalnumber));
//            ll_total.addView(view);
//            allnumber = allnumber + totalnumber;
//        }
//        alltoatal.setText(String.valueOf(allnumber));
        // 添加头滑动事件
        mHScrollViews.add(item_scroll_title);
//        mHScrollViews.add(totalScroll);
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
            //            public TextView item_total;
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
            convertView = LayoutInflater.from(NCZ_SelectProduct.this).inflate(R.layout.nczselectproduct_scrolladapter_item, null);
            if (position % 2 == 0)
            {
                convertView.setBackgroundResource(R.color.bg_table_row);
            } else
            {
                convertView.setBackgroundResource(R.color.white);
            }
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
//            listItemView.item_total = (TextView) convertView.findViewById(R.id.item_total);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
//            listItemView.item_total.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);
            listItemView.item_titlev.setText(listData.get(position).getContractname());
            int totalnumber = 0;
            List<BatchTime> list = listData.get(position).getBatchTimeList();
            for (int j = 0; j < list.size(); j++)
            {
                totalnumber = totalnumber + Integer.valueOf(list.get(j).getAllnumber());
            }
//            listItemView.item_total.setText(String.valueOf(totalnumber));

            for (int i = 0; i < listData.get(position).getBatchTimeList().size(); i++)
            {
                View view = LayoutInflater.from(NCZ_SelectProduct.this).inflate(R.layout.nczselectproduct_dataitem, null);
                listItemView.tv_data = (TextView) view.findViewById(R.id.tv_data);
                listItemView.tv_data.setText(listData.get(position).getBatchTimeList().get(i).getAllnumber());
                listItemView.tv_data.getLayoutParams().width = (screenWidth);
                ll_middle.addView(view);

                listItemView.tv_data.requestFocusFromTouch();
                listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position).getBatchTimeList().get(i).getBatchTime());
                listItemView.tv_data.setTag(R.id.tag_areaid, listData.get(position).getContractid());
                listItemView.tv_data.setTag(R.id.tag_number, listData.get(position).getBatchTimeList().get(i).getAllnumber());
                listItemView.tv_data.setTag(R.id.tag_areaname, listData.get(position).getContractname());
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
            String number = (String) v.getTag(R.id.tag_number);
            String batchTimes = (String) v.getTag(R.id.tag_batchtime);
            String areaid = (String) v.getTag(R.id.tag_areaid);
            String areaname = (String) v.getTag(R.id.tag_areaname);
            showDialog_selectProduct();

        }
    };

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
            if (tv_parkname.getText().equals(wz_storehouse.getParkName() + "库存量"))
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

    public void showDialog_selectProduct()
    {
        final View dialog_layout = LayoutInflater.from(NCZ_SelectProduct.this).inflate(R.layout.customdialog_editcontractsale, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(NCZ_SelectProduct.this, R.style.MyDialog, dialog_layout);
        et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
//                if (allnumber == 0)
//                {
//                    Toast.makeText(NCZ_SelectProduct.this, "请先选择产品", Toast.LENGTH_SHORT).show();
//                } else if (number_select != 0)
//                {
//                    pb_upload.setVisibility(View.VISIBLE);
//                    setData();
//                    StringBuilder builder = new StringBuilder();
//                    builder.append("{\"SellOrderDetailList\": ");
//                    builder.append(JSON.toJSONString(list_sell));
//                    builder.append(", \"uuids\": ");
//                    builder.append(JSON.toJSONString(uuids));
//                    builder.append("} ");
//                    CreateOrder(builder.toString());
//                }

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

//    private void CreateOrder(String data)
//    {
//        RequestParams params = new RequestParams();
//        params.setContentType("application/json");
//        try
//        {
//            params.setBodyEntity(new StringEntity(data, "utf-8"));
//        } catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//        }
//        params.addQueryStringParameter("action", "saveSellOrderDetailList");
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
//        {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo)
//            {
//                String a = responseInfo.result;
//                Result result = JSON.parseObject(responseInfo.result, Result.class);
//                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//                {
//                    if (result.getAffectedRows() != 0)
//                    {
//                        rl_upload.setVisibility(View.GONE);
//                        Intent intent1 = new Intent();
//                        intent1.setAction(AppContext.BROADCAST_FINISHSELECTBATCHTIME);
//                        sendBroadcast(intent1);
//                        finish();
//                    }
//                } else
//                {
//                    rl_upload.setVisibility(View.GONE);
//                    AppContext.makeToast(NCZ_SelectProduct.this, "error_connectDataBase");
//                    return;
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg)
//            {
//                rl_upload.setVisibility(View.GONE);
//                AppContext.makeToast(NCZ_SelectProduct.this, "error_connectServer");
//            }
//        });
//    }


    private void cancleOrder()
    {
        View dialog_layout = getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(NCZ_SelectProduct.this, R.style.MyDialog, dialog_layout, "取消订单", "取消订单吗？", "取消", "不取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
//                        myDialog.dismiss();
                        deleNewSaleAddsalefor();
                        DeletesellOrderSettlement();
                        Toast.makeText(NCZ_SelectProduct.this, "已取消", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent();
                        intent1.setAction(AppContext.BROADCAST_FINISHSELECTBATCHTIME);
                        sendBroadcast(intent1);

                        finish();
                        break;
                    case R.id.btn_cancle:
                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void deleNewSaleAddsalefor()
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("creatorId", commembertab.getId());
        params.addQueryStringParameter("action", "deleNewSaleAddsalefor");//jobGetList1
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

                } else
                {
                    AppContext.makeToast(NCZ_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_SelectProduct.this, "error_connectServer");

            }
        });
    }

    private void DeletesellOrderSettlement()
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(NCZ_SelectProduct.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("strWhere", "infoId='" + uuid + "'");
        params.addQueryStringParameter("action", "DeletesellOrderSettlement");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PeopelList> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PeopelList.class);

                    } else
                    {
                        listNewData = new ArrayList<PeopelList>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_SelectProduct.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_SelectProduct.this, "error_connectServer");

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            cancleOrder();
        }
        return false;

    }

    public void showPop_ParkName()
    {
        LayoutInflater layoutInflater = (LayoutInflater) NCZ_SelectProduct.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_park, null);// 外层
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
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
//        pw_tab.showAsDropDown(rl_tab, 0, 0);
        //设置layout在PopupWindow中显示的位置
        pw_tab.setAnimationStyle(R.style.bottominbottomout);
        pw_tab.showAtLocation(getLayoutInflater().inflate(R.layout.ncz_selectproduct, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        pw_tab.setOutsideTouchable(true);

        TextView tv_cancle = (TextView) pv_tab.findViewById(R.id.tv_cancle);
        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);

        adapter_park = new Adapter_Park(NCZ_SelectProduct.this, parklist);
        listview.setAdapter(adapter_park);
        tv_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_tab.dismiss();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
            {
                id = parklist.get(postion).getId();
                name = parklist.get(postion).getParkName();
                pw_tab.dismiss();
                tv_parkname.setText(parklist.get(postion).getParkName() + "库存量");
            }
        });
    }

}
