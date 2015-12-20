package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.com.custominterface.FragmentCallBack;
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
public class CustomExpandableListAdapter extends BaseExpandableListAdapter
{
    commandtab commandtab;
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
    FragmentCallBack fragmentCallBack;
    private AddStd_Cmd_goodslistdapter adapter;
    GridView gridview;
    TextView tv_head;

    public CustomExpandableListAdapter(Context context, Dictionary_wheel dictionary_wheel, ExpandableListView mainlistview, GridView gridview, TextView tv_head, FragmentCallBack fragmentCallBack, commandtab commandtab)
    {
        this.tv_head = tv_head;
        this.gridview = gridview;
        this.fragmentCallBack = fragmentCallBack;
        this.dictionary_wheel = dictionary_wheel;
        this.mainlistview = mainlistview;
        this.context = context;
        this.parentId = dictionary_wheel.getFirstItemID();
        this.map_id = dictionary_wheel.getSecondItemID();
        this.parentData = dictionary_wheel.getFirstItemName();
        this.map = dictionary_wheel.getSecondItemName();
        this.commandtab = commandtab;
//        mainlistview.setSelectedChild(0, 0, true);
//        tv_head.setText(parentData[1] + "-" + map.get(parentData[1])[1]);
        currentParentId = parentId[1];
        currentChildId = map_id.get(parentId[1])[1];
        getGoodslist();

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
        tv.setText(info);
        tv.setTag(R.id.tag_fi, parentId[groupPosition]);
        tv.setTag(R.id.tag_fn, key);
        tv.setTag(R.id.tag_si, map_id.get(parentId[groupPosition])[childPosition] );
        tv.setTag(R.id.tag_sn, info);
        tv.setTag(R.id.tag_childsize, childData.length);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentParentId = (String) v.getTag(R.id.tag_fi);
                currentParentName = (String) v.getTag(R.id.tag_fn);
                currentChildId = (String) v.getTag(R.id.tag_si);
                currentChildName = (String) v.getTag(R.id.tag_sn);
                currentChildsize = (Integer) v.getTag(R.id.tag_childsize);

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
                            tv_head.setText(currentParentName + "-" + currentChildName);
                            adapter = new AddStd_Cmd_goodslistdapter(context, list_goods);
                            gridview.setAdapter(adapter);
                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3)
                                {
                                    commandtab.setnongziName(list_goods.get(pos).getgoodsName());
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("INDEX", 1);
                                    CustomExpandableListAdapter.this.fragmentCallBack.callbackFun2(bundle);
                                }
                            });
                            adapter.notifyDataSetChanged();
                        } else
                        {
                            list_goods = new ArrayList<goodslisttab>();
                        }

                    } else
                    {
                        list_goods = new ArrayList<goodslisttab>();
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
