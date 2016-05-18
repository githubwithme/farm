package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BatchTime;
import com.farm.bean.parktab;
import com.farm.ui.NCZ_FarmSale_BatchDetail_;
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
        public TextView tv_saleinfo;
        public TextView tv_batchtime;
        public TextView tv_number;
        public LinearLayout ll_batchtime;

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
            listItemView.tv_saleinfo = (TextView) convertView.findViewById(R.id.tv_saleinfo);
            listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.ll_batchtime = (LinearLayout) convertView.findViewById(R.id.ll_batchtime);
            listItemView.ll_batchtime.setTag(listItemView);
            listItemView.ll_batchtime.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, NCZ_FarmSale_BatchDetail_.class);
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

            listItemView.tv_number.setText(batchTime.getNumberofsalefor());
            listItemView.tv_batchtime.setText(batchTime.getBatchTime());
            listItemView.tv_saleinfo.setText("已售" + batchTime.getNumberofsaleout() + "    售中" + batchTime.getNumberofselein() + "    拟售" + batchTime.getNumberofnewsale());
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
        TextView tv_saleinfo = (TextView) convertView.findViewById(R.id.tv_saleinfo);
        View view = (View) convertView.findViewById(R.id.view);
        view.setTag(convertView);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View ll = (View) v.getTag();
                TextView tv_saleinfo = (TextView) ll.findViewById(R.id.tv_saleinfo);
                if (tv_saleinfo.isShown())
                {
                    tv_saleinfo.setVisibility(View.GONE);
                } else
                {
                    tv_saleinfo.setVisibility(View.VISIBLE);
                }

            }
        });
        parktab parktab = listData.get(groupPosition);
        tv_parkname.setText(parktab.getparkName());
        tv_saleinfo.setText("已售" + parktab.getNumberofsaleout() + "    售中" + parktab.getNumberofselein() + "    拟售" + parktab.getNumberofnewsale());
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
