package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.Dictionary;
import com.farm.bean.goodslisttab;
import com.farm.widget.CustomDialog_ListView_Assess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/17.
 * 适配肥料选择界面的左边导航栏
 */
public class CZ_PG_JobDetail_ExpandAdapter extends BaseExpandableListAdapter
{
    List<View> list_view = new ArrayList<>();
    int currentgroupPosition = 0;
    int currentchildPosition = 0;
    TextView currentTextView;
    CustomDialog_ListView_Assess customDialog_listView;
    private int currentItem = 0;
    List<goodslisttab> list_goods = new ArrayList<goodslisttab>();
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    List<String> firstItemName;
    List<String> firstItemID;
    List<List<String>> secondItemName;
    List<List<String>> secondItemID;
    List<List<List<String>>> ThirdItemName;
    List<List<List<String>>> ThirdItemId;
    List<List<List<String>>> ThirdItemValues;
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    ListView list;

    public CZ_PG_JobDetail_ExpandAdapter(Context context, Dictionary dictionary, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.context = context;
        firstItemName = dictionary.getFirstItemName();
        secondItemName = dictionary.getSecondItemName();
        ThirdItemName = dictionary.getThirdItemName();
        ThirdItemId = dictionary.getThirdItemID();
        ThirdItemValues = dictionary.getThirdItemValue();
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return secondItemName.get(groupPosition).get(childPosition);
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    HashMap<Integer, HashMap<Integer, View>> lmap = new HashMap<Integer, HashMap<Integer, View>>();
    HashMap<Integer, View> map = new HashMap<>();
    ListItemView listItemView = null;

    static class ListItemView
    {
        public TextView tv_tip;
        public TextView tv;
    }

    //设置子item的组件
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
//        tempDic.getThirdItemID().get(groupPosition).get(childPosition).clear();
//        tempDic.getThirdItemID().get(groupPosition).get(childPosition).add("-1");
        String key = firstItemName.get(groupPosition);
        List<String> childData = secondItemName.get(groupPosition);
        String info = childData.get(childPosition);
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_children_jobdetail, null);
            listItemView = new ListItemView();
            listItemView.tv_tip = (TextView) convertView.findViewById(R.id.tv_tip);
            listItemView.tv = (TextView) convertView.findViewById(R.id.second_textview);
            convertView.setTag(listItemView);

            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            listItemView.tv_tip.setText(info);
            listItemView.tv.setText("请选择" + info);
            listItemView.tv.setTag(R.id.tag_fi, firstItemName.get(groupPosition));
            listItemView.tv.setTag(R.id.tag_fn, key);
            listItemView.tv.setTag(R.id.tag_si, secondItemName.get(groupPosition).get(childPosition));
            listItemView.tv.setTag(R.id.tag_sn, info);
            listItemView.tv.setTag(R.id.tag_ti, ThirdItemId.get(groupPosition).get(childPosition));
            listItemView.tv.setTag(R.id.tag_tn, ThirdItemName.get(groupPosition).get(childPosition));
            listItemView.tv.setTag(R.id.tag_val, ThirdItemValues.get(groupPosition).get(childPosition));
            listItemView.tv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    currentTextView = (TextView) v;
                    List<String> list = (List<String>) v.getTag(R.id.tag_tn);
                    List<String> listid = (List<String>) v.getTag(R.id.tag_ti);
                    List<String> listvalues = (List<String>) v.getTag(R.id.tag_val);
                    currentgroupPosition = groupPosition;
                    currentchildPosition = childPosition;
                    showDialog(list, listid, listvalues);
                }
            });
        } else
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
//        for (int i = 0, cnt = getGroupCount(); i < cnt; i++)
//        {
//            if (groupPosition != i && mainlistview.isGroupExpanded(i))
//            {
//                mainlistview.collapseGroup(i);
//            }
//        }
//        for (int i = 0; i < firstItemName.size(); i++)
//        {
//            mainlistview.expandGroup(i);
//        }
//        mainlistview.expandGroup(groupPosition);
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
        String key = firstItemName.get(groupPosition);
        int size = secondItemName.get(groupPosition).size();
        return size;
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return firstItemName.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return firstItemName.size();
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
            convertView = inflater.inflate(R.layout.layout_parent_jobdetail, null);
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
            }
        });
        tv.setText(firstItemName.get(groupPosition));
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

    public void showDialog(List<String> list, List<String> listid, List<String> listvalues)
    {
        View dialog_layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView_Assess(context, R.style.MyDialog, dialog_layout, list, listid, listvalues, new CustomDialog_ListView_Assess.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                currentTextView.setText(bundle.getString("name"));
                currentTextView.setTextColor(context.getResources().getColor(R.color.bg_yellow));
                TextPaint tp = currentTextView.getPaint();
                tp.setFakeBoldText(true);

                currentTextView.setTag(bundle);
                for (int i = 0; i < list_view.size(); i++)
                {
                    if (currentTextView.getId() == list_view.get(i).getId())
                    {
                        list_view.remove(i);
                        list_view.add(currentTextView);
                        return;
                    }
                }
                list_view.add(currentTextView);
            }
        });
        customDialog_listView.show();
    }

    public List<View> getListView()
    {
        return list_view;
    }
}
