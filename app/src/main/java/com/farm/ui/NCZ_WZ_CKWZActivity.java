package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_WZ_CKWZAdapter;
import com.farm.adapter.NCZ_WZ_CKXXlistAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.common.StringUtils;
import com.farm.common.UIHelper;
import com.farm.widget.NewDataToast;
import com.farm.widget.PullToRefreshListView;
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
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/4/6.
 */
@EActivity(R.layout.ncz_wz_ckwzactivity)
public class NCZ_WZ_CKWZActivity extends FragmentActivity
{
    private NCZ_WZ_CKWZAdapter listadpater;
    private int listSumData;
    private View list_footer;//5
    private TextView list_foot_more;//5
    private ProgressBar list_foot_progress;//5
    private List<Wz_Storehouse> listData = new ArrayList<Wz_Storehouse>();
    private AppContext appContext;
    Wz_Storehouse Wz_Storehouse;
    @ViewById
    TextView tv_title;
    @ViewById
    PullToRefreshListView wz_frame_listview;

    @Click
    void btn_back()
    {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();;
        Wz_Storehouse=getIntent().getParcelableExtra("storehouseId");
    }
    @AfterViews
    void afterwzoncreat() {
        tv_title.setText(Wz_Storehouse.getParkName()+"-"+Wz_Storehouse.getStorehouseName());
        initAnimalListView();
    }
    private void initAnimalListView()
    {
        listadpater=new NCZ_WZ_CKWZAdapter(this, listData);
        list_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        list_foot_more = (TextView) list_footer.findViewById(R.id.listview_foot_more);
        list_foot_progress = (ProgressBar) list_footer.findViewById(R.id.listview_foot_progress);
        wz_frame_listview.addFooterView(list_footer);// 添加底部视图 必须在setAdapter前
        wz_frame_listview.setAdapter(listadpater);
        wz_frame_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击头部、底部栏无效
                if (position == 0 || view == list_footer)
                    return;
                Wz_Storehouse wz_storehouse=listData.get(position-1);
                Intent intent=new Intent(NCZ_WZ_CKWZActivity.this,NCZ_CKWZDetail_.class);
                intent.putExtra("storehouseId",Wz_Storehouse.getStorehouseId());
                intent.putExtra("goodsId",wz_storehouse.getGoodsId());
                intent.putExtra("localName",Wz_Storehouse.getParkName()+"-"+Wz_Storehouse.getStorehouseName());
                intent.putExtra("goodsName",wz_storehouse.getGoodsName());
//                intent.putExtra("storehouseId",Wz_Storehouse);
//                intent.putExtra("goods",wz_storehouse);
                startActivity(intent);
               /* commandtab commandtab = listData.get(position - 1);
                if (commandtab == null) return;
                commembertab commembertab = AppContext.getUserInfo(getActivity());
                AppContext.updateStatus(getActivity(), "0", commandtab.getId(), "2", commembertab.getId());
                Intent intent = new Intent(getActivity(), CommandDetail_Show_.class);
                intent.putExtra("bean", commandtab);// 因为list中添加了头部,因此要去掉一个
                startActivity(intent);*/
            }
        });
        wz_frame_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                wz_frame_listview.onScrollStateChanged(view, scrollState);

                // 数据为空--不用继续下面代码了
                if (listData.isEmpty()) return;

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(list_footer) == view.getLastVisiblePosition())
                        scrollEnd = true;
                } catch (Exception e) {
                    scrollEnd = false;
                }

                int lvDataState = StringUtils.toInt(wz_frame_listview.getTag());
                if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
                    wz_frame_listview.setTag(UIHelper.LISTVIEW_DATA_LOADING);
                    list_foot_more.setText(R.string.load_ing);// 之前显示为"完成"加载
                    list_foot_progress.setVisibility(View.VISIBLE);
                    // 当前pageIndex
                    int pageIndex = listSumData / AppContext.PAGE_SIZE;// 总数里面包含几个PAGE_SIZE
                    getListData(UIHelper.LISTVIEW_ACTION_SCROLL, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, pageIndex);
                    // loadLvNewsData(curNewsCatalog, pageIndex, lvNewsHandler,
                    // UIHelper.LISTVIEW_ACTION_SCROLL);
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                wz_frame_listview.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
        wz_frame_listview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
                // loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
                // UIHelper.LISTVIEW_ACTION_REFRESH);
                getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
            }
        });
        // 加载资讯数据
        if (listData.isEmpty()) {
            getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, wz_frame_listview, listadpater, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
        }
    }
    private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX) {

        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("storehouseId", Wz_Storehouse.getStorehouseId());
        params.addQueryStringParameter("action", "getGoodsByStoreID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<Wz_Storehouse> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                    } else {
                        listNewData = new ArrayList<Wz_Storehouse>();
                    }
                } else {
                    AppContext.makeToast(NCZ_WZ_CKWZActivity.this, "error_connectDataBase");

                    return;
                }
                // 数据处理
                int size = listNewData.size();

                switch (actiontype) {
                    case UIHelper.LISTVIEW_ACTION_INIT:// 初始化
                    case UIHelper.LISTVIEW_ACTION_REFRESH:// 顶部刷新
                    case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:// 页面切换
                        int newdata = 0;// 该变量为新加载数据数量-只有顶部刷新才会使用到
                        switch (objtype) {
                            case UIHelper.LISTVIEW_DATATYPE_NEWS:
                                listSumData = size;
                                if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
                                    if (listData.size() > 0)// 页面切换时，若之前列表中已有数据，则往上面添加，并判断去除重复
                                    {
                                        for (Wz_Storehouse Wz_Storehouse1 : listNewData) {
                                            boolean b = false;
                                            for (Wz_Storehouse Wz_Storehouse2 : listData) {
                                                if (Wz_Storehouse1.getGoodsId().equals(Wz_Storehouse2.getGoodsId())) {
                                                    b = true;
                                                    break;
                                                }
                                            }
                                            if (!b)// 两个不相等才添加
                                                newdata++;
                                        }
                                    } else {
                                        newdata = size;
                                    }
                                }
                                listData.clear();// 先清除原有数据
                                listData.addAll(listNewData);
                                break;
                            case UIHelper.LISTVIEW_DATATYPE_BLOG:
                            case UIHelper.LISTVIEW_DATATYPE_COMMENT:
                        }
                        if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
                            // 提示新加载数据
                            if (newdata > 0) {
                                NewDataToast.makeText(NCZ_WZ_CKWZActivity.this, getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
                            } else {
                                // NewDataToast.makeText(NCZ_PQ_CommandList.this,
                                // getString(R.string.new_data_toast_none), false,
                                // R.raw.newdatatoast).show();
                            }
                        }
                        break;
                    case UIHelper.LISTVIEW_ACTION_SCROLL:// 底部刷新，并且判断去除重复数据
                        switch (objtype) {
                            case UIHelper.LISTVIEW_DATATYPE_NEWS:
                                listSumData += size;
                                if (listNewData.size() > 0) {
                                    for (Wz_Storehouse Wz_Storehouse1 : listNewData) {
                                        boolean b = false;
                                        for (Wz_Storehouse Wz_Storehouse2 : listData) {
                                            if (Wz_Storehouse1.getGoodsId().equals(Wz_Storehouse2.getGoodsId())) {
                                                b = true;
                                                break;
                                            }
                                        }
                                        if (!b) listData.add(Wz_Storehouse1);
                                    }
                                } else {
                                    listData.addAll(listNewData);
                                }
                                break;
                            case UIHelper.LISTVIEW_DATATYPE_BLOG:
                                break;
                        }
                        break;
                }
                // 刷新列表
                if (size >= 0) {
                    if (size < PAGESIZE) {
                        lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_full);// 已经全部加载完毕
                    } else if (size == PAGESIZE) {// 还有数据可以加载
                        lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                        adapter.notifyDataSetChanged();
                        more.setText(R.string.load_more);
                    }

                } else if (size == -1) {
                    // 有异常--显示加载出错 & 弹出错误消息
                    lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
                    more.setText(R.string.load_error);
                    AppContext.makeToast(NCZ_WZ_CKWZActivity.this, "load_error");
                }
                if (adapter.getCount() == 0) {
                    lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
                    more.setText(R.string.load_empty);
                }
                progressBar.setVisibility(ProgressBar.GONE);
                // main_head_progress.setVisibility(ProgressBar.GONE);
                if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
                    lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
                    lv.setSelection(0);
                } else if (actiontype == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
                    lv.onRefreshComplete();
                    lv.setSelection(0);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_WZ_CKWZActivity.this, "error_connectServer");

            }
        });
    }

}
