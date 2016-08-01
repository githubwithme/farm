package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_DLAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.PeopelList;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.areatab;
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
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${hmj} on 2016/6/57.
 */
@EActivity(R.layout.cz_selectproduct)
public class CZ_SelectProduct extends Activity implements CustomHorizontalScrollView_Allitem.CustomOntouch
{
    List<SellOrderDetail_New> list_SellOrderDetail;
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
    @ViewById
    TextView tv_top_left;
    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_parkname;
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
    List<Map<String, String>> uuids;
    List<SellOrderDetail_New> list_sell;

    @ViewById
    TextView tv_selectnumber;
    @ViewById
    TextView tv_allnumber;

    @Click
    void btn_back()
    {
        cancleOrder();
    }

    @Click
    void ll_productnumber()
    {
        Intent intent = new Intent(CZ_SelectProduct.this, ProductSelectedList_.class);
        intent.putExtra("uuid", uuid);
        CZ_SelectProduct.this.startActivity(intent);
    }

    @Click
    void btn_cancleorder()
    {
        cancleOrder();
    }


    @Click
    void btn_createorder()
    {
        Intent intent = new Intent(CZ_SelectProduct.this, CZ_CreateOrder_.class);
        intent.putExtra("uuid", uuid);
        CZ_SelectProduct.this.startActivity(intent);
    }


    @AfterViews
    void afterOncreate()
    {
        customOntouch = this;
        item_scroll_title.setCuttomOntouch(customOntouch);
        deleNewSaleAddsalefor();
//        getParkList();
        tv_parkname.setText(commembertab.getparkName());
        getNCZ_getAllContractSaleData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();


        IntentFilter intentfilter_update = new IntentFilter(AppContext.UPDATEMESSAGE_NCZ_XL_REFRESH);
        registerReceiver(receiver_update, intentfilter_update);


        uuid = java.util.UUID.randomUUID().toString();
        commembertab = AppContext.getUserInfo(CZ_SelectProduct.this);
        parkid = getIntent().getStringExtra("parkid");
        name = getIntent().getStringExtra("parkname");
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getNewSaleList();
            getNCZ_getAllContractSaleData();
        }
    };


    public void getNCZ_getAllContractSaleData()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("userId", commembertab.getId());
        params.addQueryStringParameter("parkid", commembertab.getparkId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "NCZ_getAllContractSaleData");
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
                        DensityUtil densityUtil = new DensityUtil(CZ_SelectProduct.this);
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
                        initViews();
                        tv_nodatatip.setVisibility(View.GONE);

                    } else
                    {
                        listData = new ArrayList<contractTab>();
                    }

                } else
                {
                    AppContext.makeToast(CZ_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_SelectProduct.this, "error_connectServer");
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
        LayoutInflater inflater = (LayoutInflater) CZ_SelectProduct.this.getSystemService(LAYOUT_INFLATER_SERVICE);
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
            convertView = LayoutInflater.from(CZ_SelectProduct.this).inflate(R.layout.nczselectproduct_scrolladapter_item, null);
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);
            listItemView.item_titlev.setText(listData.get(position).getareaName() + listData.get(position).getContractname());//problem

            int totalnumber = 0;
            List<BatchTime> list = listData.get(position).getBatchTimeList();
            for (int j = 0; j < list.size(); j++)
            {
                totalnumber = totalnumber + Integer.valueOf(list.get(j).getAllnumber());
            }
//            listItemView.item_total.setText(String.valueOf(totalnumber));

            for (int i = 0; i < listData.get(position).getBatchTimeList().size(); i++)
            {
                View view = LayoutInflater.from(CZ_SelectProduct.this).inflate(R.layout.nczselectproduct_dataitem, null);
                listItemView.tv_data = (TextView) view.findViewById(R.id.tv_data);
                listItemView.tv_data.setText(listData.get(position).getBatchTimeList().get(i).getAllnumber());
                listItemView.tv_data.getLayoutParams().width = (screenWidth);
                ll_middle.addView(view);

                listItemView.tv_data.requestFocusFromTouch();
                listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position).getBatchTimeList().get(i).getBatchTime());
                listItemView.tv_data.setTag(R.id.contractorId, listData.get(position).getContractid());
                listItemView.tv_data.setTag(R.id.tag_number, listData.get(position).getBatchTimeList().get(i).getAllnumber());
                listItemView.tv_data.setTag(R.id.tv_contractName, listData.get(position).getContractname());
                listItemView.tv_data.setTag(R.id.tag_areaid, listData.get(position).getAreaId());
                listItemView.tv_data.setTag(R.id.tag_areaname, listData.get(position).getareaName());
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
            String contractorId = (String) v.getTag(R.id.contractorId);
            String tv_contractName = (String) v.getTag(R.id.tv_contractName);
            String areaid = (String) v.getTag(R.id.tag_areaid);
            String areaname = (String) v.getTag(R.id.tag_areaname);


            showDialog_selectProduct(number, batchTimes, contractorId, tv_contractName, areaid, areaname);

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

    //    showDialog_selectProduct(number,batchTimes,areaid,areaname);
    public void showDialog_selectProduct(final String number, final String batchTimes, final String contractid, final String contractname, final String areaid, final String areaname)
    {
        final View dialog_layout = LayoutInflater.from(CZ_SelectProduct.this).inflate(R.layout.customdialog_editcontractsale, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(CZ_SelectProduct.this, R.style.MyDialog, dialog_layout);
        et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
                if (et_number.getText().toString().equals(""))
                {
                    Toast.makeText(CZ_SelectProduct.this, "请先选择产品", Toast.LENGTH_SHORT).show();
                } else
                {
                    list_sell = new ArrayList<>();
                    uuids = new ArrayList<>();
                    String number_left = String.valueOf(Integer.valueOf(number) - Integer.valueOf(et_number.getText().toString()));
                    HashMap hashMap = new HashMap();
                    hashMap.put("contractid", contractid);
                    hashMap.put("year", utils.getYear());
                    hashMap.put("batchTime", batchTimes);
                    hashMap.put("number_difference", number_left);
                    uuids.add(hashMap);
                    com.farm.bean.commembertab commembertab = AppContext.getUserInfo(CZ_SelectProduct.this);
                    String uuid = java.util.UUID.randomUUID().toString();
                    SellOrderDetail_New sellorderdetail_newsale = new SellOrderDetail_New();
                    sellorderdetail_newsale.setuid(commembertab.getuId());
                    sellorderdetail_newsale.setCreatorId(commembertab.getId());

                    sellorderdetail_newsale.setUuid(uuid);
                    sellorderdetail_newsale.setactuallat("");
                    sellorderdetail_newsale.setactuallatlngsize("");
                    sellorderdetail_newsale.setactuallng("");
                    sellorderdetail_newsale.setactualnote("");
                    sellorderdetail_newsale.setactualnumber("");
                    sellorderdetail_newsale.setactualprice("");
                    sellorderdetail_newsale.setactualweight("");
                    sellorderdetail_newsale.setareaid(areaid);
                    sellorderdetail_newsale.setareaname(areaname);
                    sellorderdetail_newsale.setBatchTime(batchTimes);
                    sellorderdetail_newsale.setcontractid(contractid);
                    sellorderdetail_newsale.setcontractname(contractname);
                    sellorderdetail_newsale.setisSoldOut("0");
                    sellorderdetail_newsale.setparkid(commembertab.getparkId());
                    sellorderdetail_newsale.setparkname(commembertab.getparkName());
                    sellorderdetail_newsale.setPlanlat("");
                    sellorderdetail_newsale.setplanlng("");
                    sellorderdetail_newsale.setplanlatlngsize("");
                    sellorderdetail_newsale.setplannote("");
                    sellorderdetail_newsale.setplannumber(et_number.getText().toString());
                    sellorderdetail_newsale.setplanprice("");
                    sellorderdetail_newsale.setplanweight("");
                    sellorderdetail_newsale.setreg(utils.getTime());
                    sellorderdetail_newsale.setstatus("0");
                    sellorderdetail_newsale.setType("newsale");
                    sellorderdetail_newsale.setsaleid("");
                    sellorderdetail_newsale.setXxzt("0");
                    sellorderdetail_newsale.setYear(utils.getYear());
                    list_sell.add(sellorderdetail_newsale);

                    StringBuilder builder = new StringBuilder();
                    builder.append("{\"SellOrderDetailList\": ");
                    builder.append(JSON.toJSONString(list_sell));
                    builder.append(", \"uuids\": ");
                    builder.append(JSON.toJSONString(uuids));
                    builder.append("} ");
                    CreateOrder(builder.toString());
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
        myDialog = new MyDialog(CZ_SelectProduct.this, R.style.MyDialog, dialog_layout, "取消订单", "取消订单吗？", "取消", "不取消", new MyDialog.CustomDialogListener()
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
                        Toast.makeText(CZ_SelectProduct.this, "已取消", Toast.LENGTH_SHORT).show();
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
                    AppContext.makeToast(CZ_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_SelectProduct.this, "error_connectServer");

            }
        });
    }

    private void DeletesellOrderSettlement()
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(CZ_SelectProduct.this);
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
                    AppContext.makeToast(CZ_SelectProduct.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(CZ_SelectProduct.this, "error_connectServer");

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


    private void addSellOrderDetail_new(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "addSellOrderDetail_new");
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
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

                    }

                } else
                {
                    AppContext.makeToast(CZ_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_SelectProduct.this, "error_connectServer");
            }
        });
    }


    private void CreateOrder(String data)
    {
        RequestParams params = new RequestParams();
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        params.addQueryStringParameter("action", "saveSellOrderDetailList");
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


                        Intent intent1 = new Intent();
                        intent1.setAction(AppContext.UPDATEMESSAGE_NCZ_XL_REFRESH);
                        sendBroadcast(intent1);

                    }
                } else
                {
                    AppContext.makeToast(CZ_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_SelectProduct.this, "error_connectServer");
            }
        });
    }


    private void getNewSaleList()
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(CZ_SelectProduct.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("creatorId", commembertab.getId());
        params.addQueryStringParameter("action", "getSellOrderDetailList");//jobGetList1
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
                        list_SellOrderDetail = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);


                        tv_allnumber.setText("共售" + String.valueOf(countAllNumber()) + "株");
                        tv_selectnumber.setText("已选" + list_SellOrderDetail.size() + "个，点击查看详情");

                    } else
                    {
                        list_SellOrderDetail = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(CZ_SelectProduct.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_SelectProduct.this, "error_connectServer");
            }
        });
    }

    public int countAllNumber()
    {
        int allnumber = 0;
        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            allnumber = allnumber + Integer.valueOf(list_SellOrderDetail.get(i).getplannumber());
        }
        return allnumber;
    }
}
