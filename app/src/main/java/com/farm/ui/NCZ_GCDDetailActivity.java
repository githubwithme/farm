package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_PlantLastObserve;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.PlantGcd;
import com.farm.bean.Result;
import com.farm.bean.plantgrowthtab;
import com.farm.bean.planttab;
import com.farm.common.BitmapHelper;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomArrayAdapter;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomHorizontalScrollView_PlantObserve;
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

/**
 * Created by ${hmj} on 2016/7/6.
 */
@EActivity(R.layout.ncz_gcddetailactivity)
public class NCZ_GCDDetailActivity extends Activity
{
    CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
    PlantGcd plantgcd;
    List<planttab> list_plants;
    String[] mDatas;
    Adapter_PlantLastObserve adapter_plantLastObserve = null;
    List<plantgrowthtab> listData = null;
    private ListView mListView;
    public HorizontalScrollView mTouchView;
    protected List<CustomHorizontalScrollView_PlantObserve> mHScrollViews = new ArrayList<CustomHorizontalScrollView_PlantObserve>();
    private ScrollAdapter mAdapter;
    int screenWidth = 0;
    int allnumber = 0;
    @ViewById
    TextView tv_top_left;
    @ViewById
    TextView tv_status;
    @ViewById
    TextView tv_photo;
    @ViewById
    TextView tv_lys;
    @ViewById
    TextView tv_ys;
    @ViewById
    TextView tv_wj;
    @ViewById
    TextView tv_zg;
    @ViewById
    TextView tv_average_zg;
    @ViewById
    TextView tv_average_wj;
    @ViewById
    TextView tv_average_ys;
    @ViewById
    TextView tv_average_lys;
    @ViewById
    TextView tv_zs;
    @ViewById
    TextView tv_nodatatip;
    @ViewById
    ListView lv;
    @ViewById
    Spinner spinner;
    ArrayAdapter<String> provinceAdapter = null;

    //    @Click
//    void tv_observate()
//    {
//        Intent intent = new Intent(NCZ_GCDDetailActivity.this, AddPlantObservation_.class);
//        intent.putExtra("gcdid", plantgcd.getId());
//        startActivity(intent);
//    }
//
//    @Click
//    void tv_addplant()
//    {
//        Intent intent = new Intent(NCZ_GCDDetailActivity.this, AddPlant_.class);
//        intent.putExtra("gcdid", plantgcd.getId());
//        intent.putExtra("gcdName", plantgcd.getPlantgcdName());
//        NCZ_GCDDetailActivity.this.startActivity(intent);
//    }
    @AfterViews
    void afterOncreate()
    {
        plantgcd = getIntent().getParcelableExtra("bean_gcd");
        getActionBar().hide();
        DensityUtil densityUtil = new DensityUtil(NCZ_GCDDetailActivity.this);
        screenWidth = densityUtil.getScreenWidth() / 5;
        tv_top_left.getLayoutParams().width = (screenWidth);
        tv_status.getLayoutParams().width = (screenWidth);
        tv_photo.getLayoutParams().width = (screenWidth);
        tv_lys.getLayoutParams().width = (screenWidth);
        tv_ys.getLayoutParams().width = (screenWidth);
        tv_wj.getLayoutParams().width = (screenWidth);
        tv_zg.getLayoutParams().width = (screenWidth);
        getAllPlants();
        //getNewSaleList_test();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    private void setSpinner()
    {
        mDatas = new String[list_plants.size()];
        for (int i = 0; i < list_plants.size(); i++)
        {
            mDatas[i] = list_plants.get(i).getplantName();
        }
        //绑定适配器和值
        provinceAdapter = new CustomArrayAdapter(NCZ_GCDDetailActivity.this, mDatas);
        spinner.setAdapter(provinceAdapter);
        spinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值


        //省级下拉框监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                getPlantAllObserveData(list_plants.get(position).getId().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }

        });

    }

    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(NCZ_GCDDetailActivity.this, "getplantobserve", plantgrowthtab.class);
        if (listData != null)
        {
            initViews();
        }

    }

    private void getPlantAllObserveData(String plantid)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("plantid", plantid);
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "NCZ_getPlantAllObserveData");//jobGetList1
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
                        listData = JSON.parseArray(result.getRows().toJSONString(), plantgrowthtab.class);
                        initViews();
                    } else
                    {
                        listData = new ArrayList<plantgrowthtab>();
                        initViews();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectServer");
            }
        });
    }

    private void getAllPlants()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("gcdid", plantgcd.getId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "NCZ_getPlantsByGcd");//jobGetList1
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
                        list_plants = JSON.parseArray(result.getRows().toJSONString(), planttab.class);
                        setSpinner();
                        getPlantAllObserveData(list_plants.get(0).getId().toString());
                        getPlantLastObserve();
                    } else
                    {
                        tv_nodatatip.setVisibility(View.VISIBLE);
                        list_plants = new ArrayList<planttab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectServer");
            }
        });
    }

    private void getPlantLastObserve()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("gcdid", plantgcd.getId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "NCZ_getPlantLastObserve");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<plantgrowthtab> list_lastObserveData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() > 0)
                    {
                        list_lastObserveData = JSON.parseArray(result.getRows().toJSONString(), plantgrowthtab.class);
                        adapter_plantLastObserve = new Adapter_PlantLastObserve(NCZ_GCDDetailActivity.this, list_lastObserveData);
                        lv.setAdapter(adapter_plantLastObserve);
                        int size = list_lastObserveData.size();
                        int zg = 0;
                        int wj = 0;
                        int ys = 0;
                        int lys = 0;
                        for (int i = 0; i < list_lastObserveData.size(); i++)
                        {
                            zg = zg + Integer.valueOf(list_lastObserveData.get(i).gethNum());
                            wj = wj + Integer.valueOf(list_lastObserveData.get(i).getwNum());
                            ys = ys + Integer.valueOf(list_lastObserveData.get(i).getyNum());
                            lys = lys + Integer.valueOf(list_lastObserveData.get(i).getxNum());
                        }
                        tv_average_zg.setText(String.valueOf(zg / size) + "m");
                        tv_average_wj.setText(String.valueOf(wj / size) + "cm");
                        tv_average_ys.setText(String.valueOf(ys / size) + "张");
                        tv_average_lys.setText(String.valueOf(lys / size) + "张");
                        tv_zs.setText(String.valueOf(size) + "株");
                    } else
                    {
                        list_lastObserveData = new ArrayList<plantgrowthtab>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_GCDDetailActivity.this, "error_connectServer");
            }
        });
    }

    private void initViews()
    {
        mHScrollViews.clear();
        CustomHorizontalScrollView_PlantObserve headerScroll = (CustomHorizontalScrollView_PlantObserve) findViewById(R.id.item_scroll_title);
        // 添加头滑动事件
        mHScrollViews.add(headerScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        mAdapter = new ScrollAdapter();
        mListView.setAdapter(mAdapter);
    }

    public void addHViews(final CustomHorizontalScrollView_PlantObserve hScrollView)
    {
        if (!mHScrollViews.isEmpty())
        {
            int size = mHScrollViews.size();
            CustomHorizontalScrollView_PlantObserve scrollView = mHScrollViews.get(size - 1);
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
        for (CustomHorizontalScrollView_PlantObserve scrollView : mHScrollViews)
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
            public Button btn_data;
            public ImageView iv;
            public TextView tv_tip;
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
            convertView = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_scrolladapter_item, null);
            listItemView = new ListItemView();
            listItemView.item_titlev = (TextView) convertView.findViewById(R.id.item_titlev);
            listItemView.item_titlev.getLayoutParams().width = (screenWidth);
            LinearLayout ll_middle = (LinearLayout) convertView.findViewById(R.id.ll_middle);
            listItemView.item_titlev.setText(listData.get(position).getcjDate().substring(0, listData.get(position).getcjDate().lastIndexOf(" ")));
            View view = null;

            //注意下列顺序
            //0
            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            String status = "";
            if (listData.get(position).getplantType().equals("1"))
            {
                status = status + "变异";
            } else
            {
                status = "正常";
            }
            if (listData.get(position).getcDate().equals("1"))
            {
                status = status + "|已抽蕾";
            } else if (listData.get(position).getzDate().equals("1"))
            {
                status = status + "|已留芽";
            }
            listItemView.btn_data.setText(status);
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);
            //1
            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem_photos, null);
            listItemView.iv = (ImageView) view.findViewById(R.id.iv);
            listItemView.tv_tip = (TextView) view.findViewById(R.id.tv_tip);
            List<String> urls = listData.get(position).getImgUrl();
            if (urls.size() != 0)
            {
                BitmapHelper.setImageViewBackground(NCZ_GCDDetailActivity.this, listItemView.iv, AppConfig.baseurl + urls.get(0));
            }
            listItemView.tv_tip.setText(urls.size() + "张");
            listItemView.iv.getLayoutParams().width = (screenWidth);
            listItemView.iv.setTag(urls);
            listItemView.iv.setOnClickListener(clickListener);
            ll_middle.addView(view);
            //2
            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).gethNum());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);

            //3
            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).getwNum());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);
            //4
            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).getyNum());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);
            //5
            view = LayoutInflater.from(NCZ_GCDDetailActivity.this).inflate(R.layout.plantobserve_dataitem, null);
            listItemView.btn_data = (Button) view.findViewById(R.id.btn_data);
            listItemView.btn_data.setText(listData.get(position).getxNum());
            listItemView.btn_data.getLayoutParams().width = (screenWidth);
            ll_middle.addView(view);


            // 第一次初始化的时候装进来
            addHViews((CustomHorizontalScrollView_PlantObserve) convertView.findViewById(R.id.item_chscroll_scroll));
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
            List<String> urls = (List<String>) v.getTag();
            PictureScrollFragment_DialogFragment dialog = new PictureScrollFragment_DialogFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putStringArrayList("imgurl", (ArrayList<String>) urls);
            dialog.setArguments(bundle1);
            dialog.show(getFragmentManager(), "EditNameDialog");
        }
    };

//    private ArrayList<String> generateItem(List<ChartEntity> list)
//    {
//        ArrayList<String> m = new ArrayList<String>();
//        for (int i = 0; i < list.size(); i++)
//        {
//            m.add(list.get(i).getItem());
//        }
//        return m;
//    }

//    private BarData generateDataBar()
//    {
////        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
////        for (int i = 0; i < listNewData.size(); i++)
////        {
////            entries.add(new BarEntry(Float.valueOf(listNewData.get(i).getNumber()), i));
////        }
////        BarDataSet d1 = new BarDataSet(entries, "物资剩余量/kg");
////        d1.setBarSpacePercent(20f);
////        d1.setColor(Color.rgb(255, 0, 255));
////        d1.setHighLightAlpha(255);
////        d1.setStackLabels(new String[1]);
////        d1.setValueTextSize(12);
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//
//        for (int i = 0; i < listNewData.size(); i++)
//        {
//            parktab parktab = listNewData.get(i);
//            float[] f = new float[]{Float.valueOf(parktab.getAllsaleout()), Float.valueOf(parktab.getAllsalein()), Float.valueOf(parktab.getAllsalefor())};
//            yVals1.add(new BarEntry(f, i));
//        }
//        ArrayList<String> listitem = new ArrayList<String>();
//        for (int i = 0; i < listNewData.size(); i++)
//        {
//            listitem.add(listNewData.get(i).getparkName());
//        }
//
//        BarDataSet set1 = new BarDataSet(yVals1, "单位:株");
//        set1.setColors(getColors());
//        set1.setStackLabels(new String[]{"已售", "售中", "待售"});
//
//        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
//        dataSets.add(set1);
//
//        BarData cd = new BarData(listitem, set1);
//        cd.setValueTextSize(16);
//        cd.setValueTextColor(getResources().getColor(R.color.white));
////        data.setValueFormatter(new MyValueFormatter());
//
//
////        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
////        sets.add(d1);
////        BarData cd = new BarData(generateItem(list_leftnumber), sets);
//        return cd;
//    }
//    private LineData generateDataLine_ZG()
//    {
//        ArrayList<Entry> e1 = new ArrayList<Entry>();
//        for (int i = 0; i < list_outnumber.size(); i++)
//        {
//            e1.add(new Entry(Float.valueOf(list_outnumber.get(i).getNumber()), i));
//        }
//        LineDataSet d1 = new LineDataSet(e1, "销售");
//        d1.setLineWidth(1.8f);
//        d1.setCircleSize(3.6f);
//        d1.setHighLightColor(Color.rgb(244, 117, 117));
//        d1.setDrawValues(false);
//        d1.setValueTextSize(20f);
//
//
//
//
//        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
//        sets.add(d1);
////        sets.add(d2);
//
//        LineData cd = new LineData(generateItem(list_outnumber), sets);
//        return cd;
//    }
//
//
//    private class ChartDataAdapter extends ArrayAdapter<ChartItem>
//    {
//
//        public ChartDataAdapter(Context context, List<ChartItem> objects)
//        {
//            super(context, 0, objects);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            return getItem(position).getView(position, convertView, getContext());
//        }
//
//        @Override
//        public int getItemViewType(int position)
//        {
//            return getItem(position).getItemType();
//        }
//
//        @Override
//        public int getViewTypeCount()
//        {
//            return 3; // we have 3 different item-types
//        }
//    }
//
//    private int[] getColors()
//    {
//        int[] colors = new int[]{Color.rgb(255, 0, 0), Color.rgb(0, 0, 255), Color.rgb(0, 255, 0)};
//        return colors;
//    }
}
