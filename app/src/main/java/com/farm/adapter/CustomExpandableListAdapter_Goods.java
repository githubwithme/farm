package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.ui.SingleGoodList_;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/17.
 * 适配肥料选择界面的左边导航栏
 */
public class CustomExpandableListAdapter_Goods extends BaseExpandableListAdapter
{
    TextView tv_top;
    TextView tempParentView;
    TextView tempChildView;
    private int currentItem = 0;
    //    ShopAdapter shopAdapter;
    List<goodslisttab> list_goods = new ArrayList<goodslisttab>();
    Dictionary_wheel dictionary_wheel;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    String[] parentData = null;
    String[] parentId = null;
    HashMap<String, String[]> map = null;
    HashMap<String, String[]> map_id = null;
    String currentParentName = "";
    String currentChildName = "";
    String currentParentId = "";
    String currentChildId = "";
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    ListView list;

    public CustomExpandableListAdapter_Goods(Context context, Dictionary_wheel dictionary_wheel, ExpandableListView mainlistview, ListView list, TextView tv_top)
    {
        this.tv_top = tv_top;
        this.list = list;
        this.dictionary_wheel = dictionary_wheel;
        this.mainlistview = mainlistview;
        this.context = context;
        this.parentId = dictionary_wheel.getFirstItemID();
        this.map_id = dictionary_wheel.getSecondItemID();
        this.parentData = dictionary_wheel.getFirstItemName();
        this.map = dictionary_wheel.getSecondItemName();
        currentParentId = parentId[0];
        currentChildId = map_id.get(parentId[0])[0];
        currentParentName = parentData[0];
        currentChildName = map.get(parentData[0])[0];

    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        String key = parentData[groupPosition];
        return (map.get(key)[childPosition]);
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        String key = parentData[groupPosition];
        String[] childData = map.get(key);
        String info = map.get(key)[childPosition];
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_children_goods, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.second_textview);
        if (groupPosition == 0 && childPosition == 0)
        {
//            TextView textView = (TextView) parent.getChildAt(0);
//            textView.setTextColor(0xFFFF5D5E);
//            TextPaint tp = textView.getPaint();
//            tp.setFakeBoldText(true);

            tv.setTextColor(0xFFFF5D5E);
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
            getGoodslist();
            tempChildView = tv;

            tv_top.setText(currentParentName+"-"+currentChildName);
        }
        tv.setText(info);
        tv.setTag(R.id.tag_fi, parentId[groupPosition]);
        tv.setTag(R.id.tag_fn, key);
        tv.setTag(R.id.tag_si, map_id.get(parentId[groupPosition])[childPosition]);
        tv.setTag(R.id.tag_sn, info);
        tv.setTag(R.id.tag_childsize, childData.length);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (tempChildView != null)
                {
                    tempChildView.setTextColor(context.getResources().getColor(R.color.bg_text));
                    TextPaint tp = tempChildView.getPaint();
                    tp.setFakeBoldText(false);
                }

                TextView textView = (TextView) v;
                textView.setTextColor(0xFFFF5D5E);
                TextPaint tp = textView.getPaint();
                tp.setFakeBoldText(true);
                tempChildView = textView;
                currentParentId = (String) v.getTag(R.id.tag_fi);
                currentParentName = (String) v.getTag(R.id.tag_fn);
                currentChildId = (String) v.getTag(R.id.tag_si);
                currentChildName = (String) v.getTag(R.id.tag_sn);
                currentChildsize = (Integer) v.getTag(R.id.tag_childsize);
                tv_top.setText(currentParentName+"-"+currentChildName);
                getGoodslist();
            }
        });
        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        // mExpListView 是列表实例，通过判断它的状态，关闭已经展开的。
        for (int i = 0, cnt = getGroupCount(); i < cnt; i++)
        {
            if (groupPosition != i && mainlistview.isGroupExpanded(i))
            {
                mainlistview.collapseGroup(i);
            }
        }
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);

    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        String key = parentData[groupPosition];
        int size = map.get(key).length;
        return size;
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return parentData[groupPosition];
    }

    @Override
    public int getGroupCount()
    {
        return parentData.length;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    //设置父item组件
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_parent_goods, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.parent_textview);

        tv.setTag(groupPosition);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mainlistview.expandGroup(Integer.valueOf(v.getTag().toString()));
                if (tempParentView != null)
                {
                    tempParentView.setTextColor(context.getResources().getColor(R.color.bg_text));
                    TextPaint tp = tempParentView.getPaint();
                    tp.setFakeBoldText(false);
                }

                TextView textView = (TextView) v;
                textView.setTextColor(0xFFFF5D5E);
                TextPaint tp = textView.getPaint();
                tp.setFakeBoldText(true);
                tempParentView = textView;
            }
        });
        tv.setText(parentData[groupPosition]);
        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    private void getGoodslist()
    {
        commembertab commembertab = AppContext.getUserInfo(context);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("wz1", currentParentId);
        params.addQueryStringParameter("wz2", currentChildId);
        params.addQueryStringParameter("action", "getGoodsList");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)
                {
                    if (result.getAffectedRows() != 0)
                    {
                        String aa = result.getRows().toJSONString();
                        list_goods = JSON.parseArray(result.getRows().toJSONString(), goodslisttab.class);
                        if (list_goods != null)
                        {
                            adapter = new GoodsAdapter(context, list_goods);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3)
                                {
                                    Intent intent = new Intent(context, SingleGoodList_.class);
                                    context.startActivity(intent);
                                }
                            });
                            adapter.notifyDataSetChanged();
                        } else
                        {
                            list_goods = new ArrayList<goodslisttab>();
                            adapter = new GoodsAdapter(context, list_goods);
                            list.setAdapter(adapter);
                        }

                    } else
                    {
                        list_goods = new ArrayList<goodslisttab>();
                        adapter = new GoodsAdapter(context, list_goods);
                        list.setAdapter(adapter);
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
