package com.farm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_ZZ;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.PlantGcd;
import com.farm.bean.PlantGcjl;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.planttab;
import com.farm.common.BitmapHelper;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomViewPager;
import com.farm.widget.swipelistview.ExpandAniLinearLayout;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-23 下午3:37:11
 * @description :
 */
@EFragment
public class GrowthTreeFragment_ZZ extends Fragment
{
    int currentItem = 0;
    List<android.support.v4.app.Fragment> fragmentList;
    ViewPagerAdapter_ZZ viewPagerAdapter_zz;
    LayoutInflater layoutinflater;
    View[] views;
    TextView[] tvList;
    List<planttab> list_plant = null;
    PlantGcd plantGcd;
    int[] resleft = new int[]{R.drawable.centerleft, R.drawable.centerleft1, R.drawable.centerleft2};
    int[] resright = new int[]{R.drawable.centerright, R.drawable.centerright1, R.drawable.centerright2};
    String[] list_img;
    List<PlantGcjl> listNewData = new ArrayList<PlantGcjl>();
    @ViewById
    ExpandAniLinearLayout swipe_list_ani;
    @ViewById
    ListView lv_tree;
    TreeAdapter treeAdapter;
    @ViewById
    RelativeLayout rl_pb;
    @ViewById
    LinearLayout ll_tip;
    @ViewById
    ProgressBar pb;
    @ViewById
    TextView tv_tip;
    @ViewById
    TextView tv_yq;
    @ViewById
    TextView tv_pq;
    @ViewById
    TextView tv_zs;
    @ViewById
    HorizontalScrollView cmd_tools_scrlllview;
    @ViewById
    LinearLayout cmd_tools;
    @ViewById
    CustomViewPager vPager_zz;


    @AfterViews
    void afterOncreate()
    {
        getPlantList();
//        lv_tree.setAdapter(new TreeAdapter(getActivity(), listNewData));
//        lv_tree.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
//            {
//                // Intent intent = new Intent(GrowthTreeActivity.this,
//                // AddPlantObservationRecord_.class);
//                // GrowthTreeActivity.this.startActivity(intent);
//            }
//        });
//        if (listNewData.isEmpty())
//        {
//            getTask(20, 0);
//        }
    }

    /**
     * 创建新实例
     *
     * @param index
     * @return
     */
    public static GrowthTreeFragment_ZZ newInstance(int index)
    {
        Bundle bundle = new Bundle();
        GrowthTreeFragment_ZZ fragment = new GrowthTreeFragment_ZZ_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.growthtreefragment_zz, container, false);
        plantGcd = getArguments().getParcelable("bean");
        return rootView;
    }


    private void getPlantList()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaid", plantGcd.getareaId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("strWhere", "");
        params.addQueryStringParameter("gcdid", plantGcd.getId());
        params.addQueryStringParameter("page_size", "100");
        params.addQueryStringParameter("page_index", "100");
        params.addQueryStringParameter("action", "plantGetListByGCD");
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
                        rl_pb.setVisibility(View.GONE);
                        list_plant = JSON.parseArray(result.getRows().toJSONString(), planttab.class);
                        showToolsView();
                        tvList[0].setBackgroundResource(R.drawable.red_bottom);
                        fragmentList = new ArrayList<Fragment>();
                        for (int i = 0; i < list_plant.size(); i++)
                        {
                            GrowthTreeFragment_GCZ f = GrowthTreeFragment_GCZ.newInstance(i);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("bean",list_plant.get(i));
                            f.setArguments(bundle);
                            fragmentList.add(f);
                        }
                        vPager_zz.setOffscreenPageLimit(1);
                        vPager_zz.setIsScrollable(true);
                        viewPagerAdapter_zz = new ViewPagerAdapter_ZZ(getActivity().getSupportFragmentManager(), vPager_zz, fragmentList);
                        viewPagerAdapter_zz.setOnExtraPageChangeListener(new ViewPagerAdapter_ZZ.OnExtraPageChangeListener()
                        {
                            @Override
                            public void onExtraPageSelected(int i)
                            {
                                currentItem = i;
                                changeTextLocation(i);
                                changeTextColor(i);
                                setBackground(i);
                            }
                        });
                    } else
                    {
                        list_plant = new ArrayList<planttab>();
                        ll_tip.setVisibility(View.VISIBLE);
                        tv_tip.setText("暂无数据！");
                        pb.setVisibility(View.GONE);
                    }
                } else
                {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("暂无数据！");
                    pb.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {
                ll_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("网络连接异常！");
                pb.setVisibility(View.GONE);
            }
        });
    }

    private void getpPlantGrowth(final int PAGESIZE, int PAGEINDEX)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("gcdid", plantGcd.getId());
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("page_size", String.valueOf(PAGESIZE));
        params.addQueryStringParameter("page_index", String.valueOf(PAGEINDEX));
        params.addQueryStringParameter("action", "plantGrowthTabGetList");
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

                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PlantGcjl.class);
//                        JSONObject jsonObject = utils.parseJsonFile(GrowthTreeActivity.this, "dictionary.json");
//                        listNewData = JSON.parseArray(JSON.parseObject(jsonObject.getString("gcjllist"), Result.class).getRows().toJSONString(), PlantGcjl.class);

                        treeAdapter = new TreeAdapter(getActivity(), listNewData);
                        lv_tree.setAdapter(treeAdapter);
                        setListViewHeightBasedOnChildren(lv_tree);
                        rl_pb.setVisibility(View.GONE);

//                        tv_yq.setText(listNewData.get(0).getPlantGrowth().get(0).getparkName());
//                        tv_pq.setText(listNewData.get(0).getPlantGrowth().get(0).getareaName());
//                        tv_zs.setText(listNewData.get(0).getPlantGrowth().size());
                    } else
                    {
                        listNewData = new ArrayList<PlantGcjl>();
                        ll_tip.setVisibility(View.VISIBLE);
                        tv_tip.setText("暂无数据！");
                        pb.setVisibility(View.GONE);
                    }
                } else
                {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("数据加载异常！");
                    pb.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
                ll_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("网络连接异常！");
                pb.setVisibility(View.GONE);
            }

        });
    }

    private void setBackground(int pos)
    {
        for (int i = 0; i < tvList.length; i++)
        {
            tvList[i].setBackgroundResource(R.drawable.white);
        }
        tvList[pos].setBackgroundResource(R.drawable.red_bottom);

    }

    private void showToolsView()
    {
        tvList = new TextView[list_plant.size()];
        views = new View[list_plant.size()];
        layoutinflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < list_plant.size(); i++)
        {
            View view = layoutinflater.inflate(R.layout.topitem_zz, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            lp.setMargins(40, 0, 40, 0);
            textView.setLayoutParams(lp);
            textView.setText(list_plant.get(i).getplantName());
            cmd_tools.addView(view);
            tvList[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            vPager_zz.setCurrentItem(v.getId());
        }
    };


    private void changeTextColor(int id)
    {
        for (int i = 0; i < tvList.length; i++)
        {
            if (i != id)
            {
//                tvList[i].setBackgroundColor(0x00000000);
                tvList[i].setTextColor(0xFF000000);
                TextPaint tp = tvList[i].getPaint();
//                tvList[i].setTextSize(getActivity().getResources().getDimension(R.dimen.size_sp_7));
                tp.setFakeBoldText(false);
            }
        }

//        tvList[id].setBackgroundColor(0xFFFFFFFF);
        tvList[id].setTextColor(0xFFFF5D5E);
//        tvList[id].setTextSize(getActivity().getResources().getDimension(R.dimen.size_sp_7));
        TextPaint tp = tvList[id].getPaint();
        tp.setFakeBoldText(true);
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition)
    {
        int x = (views[clickPosition].getWidth());
        cmd_tools_scrlllview.smoothScrollTo(x, 0);
    }

    public class TreeAdapter extends BaseAdapter
    {
        private Context context;
        private List<PlantGcjl> listItems;
        private LayoutInflater listContainer;
        PlantGcjl PlantGcjl;

        class ListItemView
        {
            public LinearLayout ll_right;
            public LinearLayout ll_left;
            public LinearLayout ll_tip_right;
            public LinearLayout ll_tip_left;
            public ImageView ic_center;
            public ImageView iv_top;
            public ImageView iv_bottom;
            public CircleImageView iv_img_right;
            public CircleImageView iv_img_left;
            public TextView tv_cjtime_right;
            public TextView tv_cjtime_left;
            public TextView tv_sg_right;
            public TextView tv_wj_right;
            public TextView tv_ys_right;
            public TextView tv_sg_left;
            public TextView tv_ys_left;
            public TextView tv_wj_left;

            public TextView tv_sfcl_left;
            public TextView tv_sfly_left;
            public TextView tv_sfcl_right;
            public TextView tv_sfly_right;
        }

        public TreeAdapter(Context context, List<PlantGcjl> data)
        {
            this.context = context;
            this.listContainer = LayoutInflater.from(context);
            this.listItems = data;
        }

        HashMap<Integer, View> lmap = new HashMap<Integer, View>();

        public View getView(int position, View convertView, ViewGroup parent)
        {
            PlantGcjl = listItems.get(position);
            ListItemView listItemView = null;
            if (lmap.get(position) == null)
            {
                convertView = listContainer.inflate(R.layout.growthtree_itme, null);
                listItemView = new ListItemView();
                listItemView.ll_right = (LinearLayout) convertView.findViewById(R.id.ll_right);
                listItemView.ll_left = (LinearLayout) convertView.findViewById(R.id.ll_left);
                listItemView.tv_cjtime_left = (TextView) convertView.findViewById(R.id.tv_cjtime_left);
                listItemView.tv_cjtime_right = (TextView) convertView.findViewById(R.id.tv_cjtime_right);
                listItemView.tv_sg_left = (TextView) convertView.findViewById(R.id.tv_sg_left);
                listItemView.tv_ys_left = (TextView) convertView.findViewById(R.id.tv_ys_left);
                listItemView.tv_wj_left = (TextView) convertView.findViewById(R.id.tv_wj_left);
                listItemView.tv_sg_right = (TextView) convertView.findViewById(R.id.tv_sg_right);
                listItemView.tv_ys_right = (TextView) convertView.findViewById(R.id.tv_ys_right);
                listItemView.tv_wj_right = (TextView) convertView.findViewById(R.id.tv_wj_right);

                listItemView.tv_sfcl_left = (TextView) convertView.findViewById(R.id.tv_sfcl_left);
                listItemView.tv_sfly_left = (TextView) convertView.findViewById(R.id.tv_sfly_left);
                listItemView.tv_sfcl_right = (TextView) convertView.findViewById(R.id.tv_sfcl_right);
                listItemView.tv_sfly_right = (TextView) convertView.findViewById(R.id.tv_sfly_right);

                listItemView.ic_center = (ImageView) convertView.findViewById(R.id.ic_center);
                listItemView.iv_bottom = (ImageView) convertView.findViewById(R.id.iv_bottom);
                listItemView.iv_img_left = (CircleImageView) convertView.findViewById(R.id.iv_img_left);
                listItemView.iv_img_right = (CircleImageView) convertView.findViewById(R.id.iv_img_right);
                listItemView.ll_tip_left = (LinearLayout) convertView.findViewById(R.id.ll_tip_left);
                listItemView.ll_tip_right = (LinearLayout) convertView.findViewById(R.id.ll_tip_right);
                listItemView.iv_top = (ImageView) convertView.findViewById(R.id.iv_top);
                listItemView.ll_left.setId(position);
                listItemView.ll_right.setId(position);
                listItemView.ll_left.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = v.getId();
                        Intent intent = new Intent(getActivity(), ObservationRecordActivity_.class);
                        intent.putExtra("gcid", listItems.get(pos).getId());
                        getActivity().startActivity(intent);
                    }
                });
                listItemView.ll_right.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = v.getId();
                        Intent intent = new Intent(getActivity(), ObservationRecordActivity_.class);
                        intent.putExtra("gcid", listItems.get(pos).getId());
                        getActivity().startActivity(intent);
                    }
                });
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }
            if (position == 0)
            {
                listItemView.iv_top.setVisibility(View.VISIBLE);
            }
            if (position == listItems.size() - 1)
            {
                listItemView.iv_bottom.setVisibility(View.VISIBLE);
            }

            if (position % 2 == 0)
            {
                listItemView.ic_center.setBackground(getResources().getDrawable(resright[(int) (Math.random() * resright.length)]));
                listItemView.ll_right.setVisibility(View.VISIBLE);
                listItemView.tv_cjtime_right.setText(PlantGcjl.getRegDate().substring(0, PlantGcjl.getRegDate().lastIndexOf(" ")));
                listItemView.tv_sg_right.setVisibility(View.VISIBLE);
                listItemView.tv_sg_right.setText(PlantGcjl.gethNum() + "m");
                listItemView.tv_wj_right.setText(PlantGcjl.getwNum() + "m");
                listItemView.tv_ys_right.setText(PlantGcjl.getyNum() + "片");
                if (PlantGcjl.getImgUrl().size() != 0)
                {
                    BitmapHelper.setImageViewBackground(context, listItemView.iv_img_right, AppConfig.baseurl + PlantGcjl.getImgUrl().get(0));
                }
                if (PlantGcjl.getSfcl().equals("True"))
                {
                    listItemView.ll_tip_right.setVisibility(View.VISIBLE);
                    listItemView.tv_sfcl_right.setVisibility(View.VISIBLE);
                }
                if (PlantGcjl.getSfly().equals("True"))
                {
                    listItemView.ll_tip_right.setVisibility(View.VISIBLE);
                    listItemView.tv_sfly_right.setVisibility(View.VISIBLE);
                }

            } else
            {
                listItemView.ic_center.setBackground(getResources().getDrawable(resleft[(int) (Math.random() * resleft.length)]));
                listItemView.ll_left.setVisibility(View.VISIBLE);
                listItemView.tv_cjtime_left.setText(PlantGcjl.getRegDate().substring(0, PlantGcjl.getRegDate().lastIndexOf(" ")));
                listItemView.tv_sg_left.setText(PlantGcjl.gethNum() + "m");
                listItemView.tv_wj_left.setText(PlantGcjl.getwNum() + "m");
                listItemView.tv_ys_left.setText(PlantGcjl.getyNum() + "片");

                if (PlantGcjl.getImgUrl().size() != 0)
                {
                    BitmapHelper.setImageViewBackground(context, listItemView.iv_img_left, AppConfig.baseurl + PlantGcjl.getImgUrl().get(0));
                }
                if (PlantGcjl.getSfcl().equals("True"))
                {
                    listItemView.ll_tip_left.setVisibility(View.VISIBLE);
                    listItemView.tv_sfcl_left.setVisibility(View.VISIBLE);
                }
                if (PlantGcjl.getSfly().equals("True"))
                {
                    listItemView.ll_tip_left.setVisibility(View.VISIBLE);
                    listItemView.tv_sfly_left.setVisibility(View.VISIBLE);
                }
            }
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
            return listItems.get(arg0);
        }

        @Override
        public long getItemId(int arg0)
        {
            return 0;
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
