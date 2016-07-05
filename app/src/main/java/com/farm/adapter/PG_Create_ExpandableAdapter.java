package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.widget.CustomListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/5.
 */
public class PG_Create_ExpandableAdapter extends BaseExpandableListAdapter
{
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<areatab> listData;
    ListView list;

    public PG_Create_ExpandableAdapter(Context context, List<areatab> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getAreatabList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getAreatabList().get(childPosition);
    }

    static class ListItemView
    {
        public TextView tv_areaname;
        public TextView tv_number;
        public TextView btn_number;
        public CheckBox cb_selectall;
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

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<SellOrderDetail_New> childData = listData.get(groupPosition).getAreatabList();
        final SellOrderDetail_New sellOrderDetail_new = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_selectproduct_childitem, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.tv_areaname = (TextView) convertView.findViewById(R.id.tv_areaname);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.btn_number = (TextView) convertView.findViewById(R.id.btn_number);

            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }
            listItemView.tv_areaname.setText(sellOrderDetail_new.getcontractname());
            listItemView.tv_number.setText(sellOrderDetail_new.getplannumber());
            listItemView.btn_number.setText(sellOrderDetail_new.getplannumber());
//            listItemView.tv_batchtime.setText(batchTime.getBatchTime() + "  " + batchTime.getBatchColor() + "  " + "已售:" + numberofsaleout + "售中:" + numberofselein + "拟售:" + numberofsalefor + "待售:" + numberofnewsale);
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
        if (listData.get(groupPosition).getAreatabList() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getAreatabList().size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return listData.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return listData.size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//0
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_prents_pg_create, null);
        }
        TextView batchtime = (TextView) convertView.findViewById(R.id.batchtime);
        TextView tv_areaname = (TextView) convertView.findViewById(R.id.tv_areaname);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);

        batchtime.setText(listData.get(groupPosition).getBatchtime());
//        tv_areaname.setText(listData.get(groupPosition).getareaName());
        int num=0;
        for (int i=0;i<listData.get(groupPosition).getAreatabList().size();i++)
        {
            num+=Integer.valueOf(listData.get(groupPosition).getAreatabList().get(i).getplannumber());
        }
        tv_number.setText(num+"");
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
}
