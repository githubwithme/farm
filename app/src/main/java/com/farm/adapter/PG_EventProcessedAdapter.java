package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.HandleBean;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.bean.Today_job;
import com.farm.bean.commembertab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 2016/4/12.
 */
public class PG_EventProcessedAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<com.farm.bean.HandleBean> listItems;// 数据集合
    private LayoutInflater listContainer;
    HandleBean HandleBean;
    String names;
    public PG_EventProcessedAdapter(Context context, List<HandleBean> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }
    static class ListItemView
    {
        public TextView name;
        public TextView state;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HandleBean = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_eventhandleadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.name = (TextView) view.findViewById(R.id.name);
            listItemView.state = (TextView) view.findViewById(R.id.state);
            lmap.put(i, view);
            view.setTag(listItemView);
        }else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }
//        getBreakOffInfoOfContract();
        listItemView.name.setText("突发事件"+HandleBean.getEventId());
//        listItemView.name.setText(names);
        if(HandleBean.getState().equals("0"))
        {
            listItemView.state.setText("未处理");
        }else
        {
            listItemView.state.setText("已处理");
        }


        return view;
    }
    private void getBreakOffInfoOfContract()
    {
        commembertab commembertab = AppContext.getUserInfo(context);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getEventListByUID");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<ReportedBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);

                        Iterator<ReportedBean> it = listNewData.iterator();
                        while (it.hasNext())
                        {
                            String value = it.next().getEventId();
                            if (!value.equals(HandleBean.getEventId()))
                            {
                                it.remove();
                            }
                        }
                        names=listNewData.get(0).getEventType();
                        //数据处理

                    } else
                    {
                        listNewData = new ArrayList<ReportedBean>();
                    }

                } else
                {
                    AppContext.makeToast(context, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(context, "error_connectServer");
            }
        });
    }
}
