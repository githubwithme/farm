package com.farm.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
    TextView tempParentView;
    TextView tempChildView;
    ImageView currentiv_tip = null;
    ImageView currentiv_point = null;
    View parentView;
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
    ListView listview;

    public CustomExpandableListAdapter(Context context, Dictionary_wheel dictionary_wheel, ExpandableListView mainlistview, ListView listview, TextView tv_head, FragmentCallBack fragmentCallBack)
    {
        this.listview = listview;
        this.fragmentCallBack = fragmentCallBack;
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
        adapter = new AddStd_Cmd_goodslistdapter(context, currentiv_tip, list_goods);
        listview.setAdapter(adapter);
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
    HashMap<Integer, HashMap<Integer, View>> lmap = new HashMap<Integer, HashMap<Integer, View>>();
    HashMap<Integer, View> map_view = new HashMap<>();
    ListItemView listItemView = null;

    static class ListItemView
    {
        public ImageView iv_point;
        public ImageView iv_tip;
        public TextView tv;
    }
    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        String key = parentData[groupPosition];
        String[] childData = map.get(key);
        String info = map.get(key)[childPosition];
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_children_goods, null);
            listItemView = new ListItemView();
            listItemView.iv_tip = (ImageView) convertView.findViewById(R.id.iv_tip);
            listItemView.iv_point = (ImageView) convertView.findViewById(R.id.iv_point);
            listItemView.tv = (TextView) convertView.findViewById(R.id.second_textview);
            if (groupPosition == 0 && childPosition == 0)
            {
                listItemView.iv_point.setVisibility(View.VISIBLE);
                listItemView.tv.setTextColor(0xFFFF5D5E);
                TextPaint tp = listItemView.tv.getPaint();
                tp.setFakeBoldText(true);
                getGoodslist();
                tempChildView = listItemView.tv;
                currentiv_tip = listItemView.iv_tip;
                currentiv_point = listItemView.iv_point;
            }
            listItemView.tv.setText(info);
            listItemView.tv.setTag(R.id.tag_fi, parentId[groupPosition]);
            listItemView. tv.setTag(R.id.tag_fn, key);
            listItemView.tv.setTag(R.id.tag_si, map_id.get(parentId[groupPosition])[childPosition]);
            listItemView.tv.setTag(R.id.tag_sn, info);
            listItemView.tv.setTag(R.id.tag_childsize, childData.length);
            listItemView.tv.setTag(R.id.tag_parentview, convertView);
            listItemView.tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (tempChildView != null)
                    {
                        tempChildView.setTextColor(context.getResources().getColor(R.color.bg_text));
                        TextPaint tp = tempChildView.getPaint();
                        tp.setFakeBoldText(false);
                        currentiv_point.setVisibility(View.GONE);
                    }


                    TextView textView = (TextView) v;
                    textView.setTextColor(0xFFFF5D5E);
                    TextPaint tp = textView.getPaint();
                    tp.setFakeBoldText(true);


                    currentParentId = (String) v.getTag(R.id.tag_fi);
                    currentParentName = (String) v.getTag(R.id.tag_fn);
                    currentChildId = (String) v.getTag(R.id.tag_si);
                    currentChildName = (String) v.getTag(R.id.tag_sn);
                    currentChildsize = (Integer) v.getTag(R.id.tag_childsize);
                    parentView = (View) v.getTag(R.id.tag_parentview);
                    currentiv_tip = (ImageView) parentView.findViewById(R.id.iv_tip);
                    currentiv_point = (ImageView) parentView.findViewById(R.id.iv_point);
                    currentiv_point.setVisibility(View.VISIBLE);
                    tempChildView = textView;
                    getGoodslist();
                }
            });
            convertView.setTag(listItemView);
            map_view.put(childPosition, convertView);
            lmap.put(groupPosition, map_view);
            if (isLastChild)
            {
                map_view = new HashMap<>();
            }
        }else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }

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
                if (mainlistview.isGroupExpanded(Integer.valueOf(v.getTag().toString())))
                {
                    mainlistview.collapseGroup(Integer.valueOf(v.getTag().toString()));
                } else
                {
                    mainlistview.expandGroup(Integer.valueOf(v.getTag().toString()));
                }
//                mainlistview.expandGroup(Integer.valueOf(v.getTag().toString()));
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
                        list_goods.clear();
                        List<goodslisttab> list = JSON.parseArray(result.getRows().toJSONString(), goodslisttab.class);
                        list_goods.addAll(list);
                        if (list_goods != null)
                        {
                            adapter = new AddStd_Cmd_goodslistdapter(context, currentiv_tip, list_goods);
                            listview.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
//                            {
//                                @Override
//                                public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3)
//                                {
//                                    SqliteDb.deleteAllSelectCmdArea(context, SelectCmdArea.class);
//
//                                    commandtab_single commandtab_single = com.farm.bean.commandtab_single.getInstance();
//                                    commandtab_single.setnongziName(list_goods.get(pos).getgoodsName());
//                                    commandtab_single.setNongziId(list_goods.get(pos).getId());
//                                    commandtab_single.setNongzigg(list_goods.get(pos).getgoodsSpec());
//                                    commandtab_single.setNongzidw(list_goods.get(pos).getgoodsunit());
//                                    Bundle bundle = new Bundle();
//                                    bundle.putInt("INDEX", 1);
//                                    CustomExpandableListAdapter.this.fragmentCallBack.callbackFun2(bundle);
//
//                                    Bundle bundle1 = new Bundle();
//                                    bundle1.putString("type", "已选择：" + currentParentName + "-" + currentChildName + "-" + list_goods.get(pos).getgoodsName().toString());
//                                    CustomExpandableListAdapter.this.fragmentCallBack.stepTwo_setHeadText(bundle1);
//                                }
//                            });
//                            adapter.notifyDataSetChanged();
                        } else
                        {
                            list_goods = new ArrayList<goodslisttab>();
                            adapter = new AddStd_Cmd_goodslistdapter(context, currentiv_tip, list_goods);
                            listview.setAdapter(adapter);
                        }

                    } else
                    {
                        list_goods = new ArrayList<goodslisttab>();
                        adapter = new AddStd_Cmd_goodslistdapter(context, currentiv_tip, list_goods);
                        listview.setAdapter(adapter);
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
