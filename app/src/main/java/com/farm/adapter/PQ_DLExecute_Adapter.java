package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BatchTime;
import com.farm.bean.BreakOff_New;
import com.farm.ui.PQ_DLbjFragment_;
import com.farm.widget.CustomDialog_ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/5/1.
 */
public class PQ_DLExecute_Adapter extends BaseExpandableListAdapter
{
    PQ_GV_Adapter pq_gv_adapter;
//    private List<Map<String, Object>> data_list;
//    private SimpleAdapter sim_adapter;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<BatchTime> listData;
    ListView list;
    public PQ_DLExecute_Adapter(Context context, List<BatchTime> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }


    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getBreakOffList() == null)
        {
            return null;
        }
//        return listData.get(groupPosition).getBreakOffList().get(childPosition);
        return   listData.get(groupPosition).getBreakOffList();
    }

    static class ListItemView
    {
        public GridView gridView;
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
    public View getChildView( int groupPosition,  int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<BreakOff_New> childData = listData.get(groupPosition).getBreakOffList();
//        final BreakOff_New breakOff_new = childData.get(childPosition);
        View v = null;
/*        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }*/
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.children_dladapter, null);
            listItemView = new ListItemView();
            listItemView.gridView = (GridView) convertView.findViewById(R.id.gridView);

            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            //数据添加
//            listItemView.goodsname.setText(wz_rKxx.getGoodsname());
            pq_gv_adapter = new PQ_GV_Adapter(context, childData);
            listItemView.gridView.setAdapter(pq_gv_adapter);

        } else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }
        //数据添加  都可以数据加载，不过在上面比较好，这里是返回view



        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        // mExpListView 是列表实例，通过判断它的状态，关闭已经展开的。
        super.onGroupExpanded(groupPosition);
        //只展开一项
/*        int len = this.getGroupCount();

        for (int i = 0; i < len; i++) {
            if (i != groupPosition) {
                mainlistview.collapseGroup(i);
            }
        }*/
//        mainlistview  WZ_RKExecute_Adapter

    }
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);

    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getBreakOffList() == null)
        {
            return 0;
        }
//        return listData.get(groupPosition).getWzcrkxx().size();
        return 1;
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
    public View getGroupView(final int groupPosition,final boolean isExpanded, View convertView, ViewGroup parent)
    {
        BatchTime batchTime=listData.get(groupPosition);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pq_parentitem, null);
        }
        TextView tv_park = (TextView) convertView.findViewById(R.id.tv_park);
        Button btn_bianjie = (Button) convertView.findViewById(R.id.btn_bianjie);
        LinearLayout groupExpand = (LinearLayout) convertView.findViewById(R.id.groupExpand);
        tv_park.setText(listData.get(groupPosition).getBatchTime() + "   " + listData.get(groupPosition).getBatchColor()+"绳带");
        if (listData.get(groupPosition).getBatchColor().equals("红色")) {
//            view.setBackgroundColor(Color.parseColor("#365663"));
//            rl_color.setBackground(R.color.red);
            groupExpand.setBackgroundColor(Color.parseColor("#ff4444"));
        } else if (listData.get(groupPosition).getBatchColor().equals("蓝色")) {

            groupExpand.setBackgroundColor(Color.parseColor("#add8e6"));
        } else if (listData.get(groupPosition).getBatchColor().equals("绿色")) {

            groupExpand.setBackgroundColor(Color.parseColor("#90ee90"));
        } else if (listData.get(groupPosition).getBatchColor().equals("紫色")){

            groupExpand.setBackgroundColor(Color.parseColor("#d8bfd8"));
        }else{
            groupExpand.setBackgroundColor(Color.parseColor("#ffff00"));
        }
        groupExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    mainlistview.collapseGroup(groupPosition);
                } else {
                    mainlistview.expandGroup(groupPosition);
                }
            }
        });
        btn_bianjie.setTag(R.id.tag_text, batchTime);
        btn_bianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BatchTime batchTimes = (BatchTime) view.getTag(R.id.tag_text);
                Intent intent = new Intent(context, PQ_DLbjFragment_.class);
                intent.putExtra("batchtime", batchTimes.getBatchTime());
                intent.putExtra("batchcolor", batchTimes.getBatchColor());
                context.startActivity(intent);
            }
        });
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
