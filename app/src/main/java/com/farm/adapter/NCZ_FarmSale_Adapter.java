package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BatchTime;
import com.farm.bean.parktab;
import com.farm.widget.CustomDialog_ListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class NCZ_FarmSale_Adapter extends BaseExpandableListAdapter
{
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<parktab> listData;
    ListView list;

    public NCZ_FarmSale_Adapter(Context context, List<parktab> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getBatchTimeList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getBatchTimeList().get(childPosition);
    }

    static class ListItemView
    {
        public TextView tv_batchtime;
        public ProgressBar pb_sale;
        public CheckBox cb_select;

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

        List<BatchTime> childData = listData.get(groupPosition).getBatchTimeList();
        final BatchTime batchTime = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_batchtime_adater, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
            listItemView.pb_sale = (ProgressBar) convertView.findViewById(R.id.pb_sale);
            listItemView.cb_select = (CheckBox) convertView.findViewById(R.id.cb_select);
            convertView.setTag(listItemView);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, NCZ_SellOrderDetail_.class);
                    intent.putExtra("bean", batchTime);
                    context.startActivity(intent);
                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            String numberofsaleout = batchTime.getNumberofsaleout();
            String numberofselein = batchTime.getNumberofselein();
            String numberofsalefor = batchTime.getNumberofsalefor();
            String numberofnewsale = batchTime.getNumberofnewsale();
            listItemView.tv_batchtime.setText(batchTime.getBatchTime() + "  " + batchTime.getBatchColor() + "  " + "已售:" + numberofsaleout + "售中:" + numberofselein + "拟售:" + numberofsalefor + "待售:" + numberofnewsale);
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
        if (listData.get(groupPosition).getBatchTimeList() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getBatchTimeList().size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_batchtime_parent, null);
        }
        TextView tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);

        String numberofsaleout = listData.get(groupPosition).getNumberofsaleout();
        String numberofselein = listData.get(groupPosition).getNumberofselein();
        String numberofsalefor = listData.get(groupPosition).getNumberofsalefor();
        String numberofnewsale = listData.get(groupPosition).getNumberofnewsale();
        String name = listData.get(groupPosition).getparkName() + "已售:" + numberofsaleout + "售中:" + numberofselein + "拟售:" + numberofsalefor + "待售:" + numberofnewsale;
        tv_parkname.setText(name);
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
