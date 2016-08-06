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

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_New_SellDetail;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.widget.CustomHorizontalScrollView_Allitem;
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
 * Created by hasee on 2016/7/18.
 */
//@EActivity(R.layout.ncz_new_jsdandplan_detail)
@EActivity(R.layout.ncz_look_jsddetail)
public class NCZ_All_OneOrder_Detail extends Activity implements CustomHorizontalScrollView_Allitem.CustomOntouch
{
    @ViewById
    CustomHorizontalScrollView_Allitem item_scroll_title;
    @ViewById
    CustomHorizontalScrollView_Allitem totalScroll;
    @ViewById
    TextView tv_cbh;
    Adapter_New_SellDetail adapter_sellOrderDetail;
    @ViewById
    ListView lv;
    @ViewById
    LinearLayout is_setveiw;
    @ViewById
    LinearLayout ll_detail;
    List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
    CustomHorizontalScrollView_Allitem.CustomOntouch customOntouch = null;
    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_Allitem> mHScrollViews = new ArrayList<CustomHorizontalScrollView_Allitem>();
    private ScrollAdapter mAdapter;
    int screenWidth = 0;
    SellOrder_New sellOrder_new;
    String broadcast;

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
    TextView buyers;  //采购商
    @ViewById
    TextView mainPepole;//负责人
    @ViewById
    TextView price; //单价
    @ViewById
    TextView saletime;//采收时间
    @ViewById
    TextView tv_planweight;//重量
    @ViewById
    TextView tv_plansumvalues;//总价
    @ViewById
    TextView tv_deposit;//定金
    @ViewById
    TextView deposit;//已付定金
    @ViewById
    TextView goodsName;//产品名字
    @ViewById
    TextView tv_finalpayment;//发往城市
    @ViewById
    TextView tv_note;

    @Click
    void tv_clicks()
    {
        if (ll_detail.isShown())
        {
            ll_detail.setVisibility(View.GONE);
        } else
        {
            ll_detail.setVisibility(View.VISIBLE);
        }
    }

    @Click
    void saletime()
    {
        if (ll_detail.isShown())
        {
            ll_detail.setVisibility(View.GONE);
        } else
        {
            ll_detail.setVisibility(View.VISIBLE);
        }
    }

    @Click
    void is_setveiw()
    {
        if (ll_detail.isShown())
        {
            ll_detail.setVisibility(View.GONE);
        } else
        {
            ll_detail.setVisibility(View.VISIBLE);
        }
    }

    @AfterViews
    void afterview()
    {
        getsellOrderDetailBySaleId();
        showData();
        customOntouch = this;
        getDetailSecBysettleId();
    }

    private void showData()
    {
        buyers.setText(sellOrder_new.getBuyersName());
        mainPepole.setText(sellOrder_new.getMainPeople());
        price.setText(sellOrder_new.getPrice());
        if (!sellOrder_new.getSaletime().equals(""))
        {
            saletime.setText(sellOrder_new.getSaletime().substring(0, sellOrder_new.getSaletime().length() - 8));
        }

        tv_planweight.setText(sellOrder_new.getWeight());


            tv_deposit.setText(sellOrder_new.getWaitDeposit());

            deposit.setText(sellOrder_new.getDeposit());



        tv_plansumvalues.setText(sellOrder_new.getSumvalues());
        goodsName.setText(sellOrder_new.getProduct());
        tv_finalpayment.setText(sellOrder_new.getAddress());
    }

    @AfterViews
    void afterOncreate()
    {
        customOntouch = this;
        item_scroll_title.setCuttomOntouch(customOntouch);
        totalScroll.setCuttomOntouch(customOntouch);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("bean");
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

    public void getDetailSecBysettleId()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_All_OneOrder_Detail.this);
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
                /*    if (result.getAffectedRows() != 0)
                    {*/
                    listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);

                    DensityUtil densityUtil = new DensityUtil(NCZ_All_OneOrder_Detail.this);

                    if (listData.size() > 0)
                    {
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
                    }


      /*                  pg_cbf_adapter = new PG_CBF_CLAdapyer(JSD_Detail.this, listNewData, "", "");
                        liat_jsd.setAdapter(pg_cbf_adapter);
                        utils.setListViewHeight(liat_jsd);

                        liat_jsd.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                            {
                                SellOrder_New sellOrder_news=listData.get(i);
                                Intent intent = new Intent(JSD_Detail.this, PG_JSD_Detail_.class);
                                intent.putExtra("bean", sellOrder_news);
                                startActivity(intent);
                            }
                        });*/

          /*          } else
                    {
                        listNewData = new ArrayList<SellOrder_New>();
                    }*/

                } else
                {
                    AppContext.makeToast(NCZ_All_OneOrder_Detail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_All_OneOrder_Detail.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        int allnumber = 0;
        mHScrollViews.clear();
        ll_park.removeAllViews();
        ll_total.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) NCZ_All_OneOrder_Detail.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        String[] name = new String[]{"车牌号", "实际金额", "合计金额", "总净重", "审批情况", "正品结算金额", "正品单价", "正品净重", "总包装费", "总搬运费"};
//        String[] name = new String[]{"实际金额", "合计金额"};
        for (int i = 0; i < name.length; i++)
        {
            View view = inflater.inflate(R.layout.pg_breakoff_contractitem, null);
            TextView tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_parkname.getLayoutParams().width = (screenWidth);
            tv_parkname.setText(name[i]);   //最上边滑动承包区
            ll_park.addView(view);
        }
        for (int i = 0; i < name.length; i++)
        {
            View view = inflater.inflate(R.layout.pg_breakoff_totalitem, null);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_total.getLayoutParams().width = (screenWidth);
            double totalnumber = 0;
            switch (i)
            {
                case 0:


                    tv_total.setText(listData.size() + "辆");
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
        }

        Map<String, String> data = null;
//        CustomHorizontalScrollView_Allitem headerScroll = (CustomHorizontalScrollView_Allitem) findViewById(R.id.item_scroll_title);
        item_scroll_title.setCuttomOntouch(customOntouch);
//        CustomHorizontalScrollView_Allitem totalScroll = (CustomHorizontalScrollView_Allitem) findViewById(R.id.totalScroll);
        totalScroll.setCuttomOntouch(customOntouch);
        // 添加头滑动事件
        mHScrollViews.add(item_scroll_title);
        mHScrollViews.add(totalScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);

        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
        utils.setListViewHeight(mListView);
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

        HashMap<Integer, View> lmap = new HashMap<Integer, View>();

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // 自定义视图
//            if (lmap.get(position) == null)
//            {
            // 获取list_item布局文件的视图
            convertView = LayoutInflater.from(NCZ_All_OneOrder_Detail.this).inflate(R.layout.customhorizontscrollview_allitem, null);
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
//            listItemView.item_total = (TextView) convertView.findViewById(R.id.item_total);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
//            listItemView.item_total.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);


            listItemView.item_titlev.setText(listData.get(position).getBatchTime());
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
//            listItemView.item_total = (TextView) convertView.findViewById(R.id.item_total);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
//            listItemView.item_total.getLayoutParams().width = (screenWidth);

            //颜色
            if (position % 2 == 0)
            {
                convertView.setBackgroundResource(R.color.light_gray);
            } else
            {
                convertView.setBackgroundResource(R.color.white);
            }
            if (listData.get(position).getIsNeedAudit().equals("0")||listData.get(position).getSettlestatus().equals("0"))
            {

            } else if (listData.get(position).getIsNeedAudit().equals("1")||listData.get(position).getSettlestatus().equals("1"))
            {
                listItemView.item_titlev.setTextColor(NCZ_All_OneOrder_Detail.this.getResources().getColor(R.color.green));
            } else if (listData.get(position).getIsNeedAudit().equals("-1")||listData.get(position).getSettlestatus().equals("-1"))
            {
                listItemView.item_titlev.setTextColor(NCZ_All_OneOrder_Detail.this.getResources().getColor(R.color.red));
            }
            listItemView.item_titlev.setText("结算单" + (position + 1));
            listItemView.item_titlev.setTag(R.id.tag_batchtime, listData.get(position));
            listItemView.item_titlev.setOnClickListener(clickListener);
            // 第一次初始化的时候装进来
            for (int i = 0; i < 8; i++)
            {
                View view = LayoutInflater.from(NCZ_All_OneOrder_Detail.this).inflate(R.layout.ncz_orderdetail_dataitem, null);
                listItemView.tv_data = (TextView) view.findViewById(R.id.tv_data);


                switch (i)
                {

                    case 0:
                        listItemView.tv_data.setText(listData.get(position).getPlateNumber());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 1:
                        listItemView.tv_data.setText(listData.get(position).getActualMoney());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 2:
                        listItemView.tv_data.setText(listData.get(position).getTotalFee());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 3:
                        listItemView.tv_data.setText(listData.get(position).getTotalWeight());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 4:
                        if (listData.get(position).getIsNeedAudit().equals("0")||listData.get(position).getSettlestatus().equals("0"))
                        {
                            listItemView.tv_data.setText("待审批");
                        } else if (listData.get(position).getIsNeedAudit().equals("-1")||listData.get(position).getSettlestatus().equals("-1"))
                        {
                            listItemView.tv_data.setText("审批不通过");
                        } else if (listData.get(position).getIsNeedAudit().equals("1")||listData.get(position).getSettlestatus().equals("1"))
                        {
                            listItemView.tv_data.setText("审批通过");
                        }else
                        {
                            listItemView.tv_data.setText("未反馈");
                        }


                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 5:
                        listItemView.tv_data.setText(listData.get(position).getQualityBalance());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 6:
                        listItemView.tv_data.setText(listData.get(position).getActualprice());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 7:
                        listItemView.tv_data.setText(listData.get(position).getQualityTotalWeight());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 8:
                        listItemView.tv_data.setText(listData.get(position).getPackFee());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                    case 9:
                        listItemView.tv_data.setText(listData.get(position).getCarryFee());
                        listItemView.tv_data.getLayoutParams().width = (screenWidth);
                        ll_middle.addView(view);
                        listItemView.tv_data.setTag(R.id.tag_batchtime, listData.get(position));
                        listItemView.tv_data.setOnClickListener(clickListener);
                        break;
                }
//            String [] name=new String []{"实际金额","合计金额","总净重",,"正品结算金额","正品单价","正品净重","总包装费","总搬运费"};
            }
            // 第一次初始化的时候装进来
            CustomHorizontalScrollView_Allitem customHorizontalScrollView_pgSale = (CustomHorizontalScrollView_Allitem) convertView.findViewById(R.id.item_chscroll_scroll);
            addHViews(customHorizontalScrollView_pgSale);
            customHorizontalScrollView_pgSale.setCuttomOntouch(customOntouch);


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
            SellOrder_New sellOrder_news = new SellOrder_New();
            sellOrder_news = (SellOrder_New) v.getTag(R.id.tag_batchtime);

            Intent intent = new Intent(NCZ_All_OneOrder_Detail.this, NCZ_Look_JSD_Detail_.class);
            intent.putExtra("bean", sellOrder_news);
            intent.putExtra("goodsname", sellOrder_new.getProduct());

            startActivity(intent);

        }
    };

    private void getsellOrderDetailBySaleId()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_All_OneOrder_Detail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleId", sellOrder_new.getUuid());
        params.addQueryStringParameter("action", "getsellOrderDetailBySaleId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrderDetail_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
//                    if (result.getAffectedRows() != 0)
//                    {

                    listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
//                        adapter_sellOrderDetail = new Adapter_New_SellDetail(NCZ_NewOrderDetail.this, list_orderdetail);
                    adapter_sellOrderDetail = new Adapter_New_SellDetail(NCZ_All_OneOrder_Detail.this, listNewData);
                    lv.setAdapter(adapter_sellOrderDetail);
                    utils.setListViewHeight(lv);
                    if (listNewData.size() > 0)
                    {
                        tv_cbh.setText(listNewData.get(0).getparkname() + "销售情况");
                    }

//                    } else
//                    {
//                        listNewData = new ArrayList<SellOrderDetail_New>();
//                    }

                } else
                {
                    AppContext.makeToast(NCZ_All_OneOrder_Detail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_All_OneOrder_Detail.this, "error_connectServer");
            }
        });
    }
}
