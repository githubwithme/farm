package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BatchTime;
import com.farm.bean.BreakOff_New;
import com.farm.widget.CustomDialog_ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/5/1.
 */
public class NCZ_DLExecute_Adapter extends BaseExpandableListAdapter
{
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<BatchTime> listData;
    ListView list;
    public NCZ_DLExecute_Adapter(Context context, List<BatchTime> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }
    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getBreakOff_newList() == null)
        {
            return null;
        }
//        return listData.get(groupPosition).getBreakOff_newList().get(childPosition);
        return listData.get(groupPosition).getBreakOff_newList();
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

        List<BreakOff_New> childData = listData.get(groupPosition).getBreakOff_newList();
        final BreakOff_New breakOff_new = childData.get(childPosition);
//        final String batchname=listData.get(groupPosition).getBatchName();
//        final String indate=listData.get(groupPosition).getInDate();
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.children_dladapter, null);
            listItemView = new ListItemView();
            listItemView.gridView = (GridView) convertView.findViewById(R.id.gridView);

            convertView.setTag(listItemView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
      /*              Intent intent = new Intent(context, NCZ_WZ_RKDetail_.class);
                    intent.putExtra("wz_rKxx", wz_rKxx);
                    intent.putExtra("batchname", batchname);
                    intent.putExtra("indate", indate);
                    context.startActivity(intent);*/

                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            //数据添加
//            listItemView.goodsname.setText(wz_rKxx.getGoodsname());

            data_list = new ArrayList<Map<String, Object>>();
            for(int i=0;i<childData.size();i++){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", childData.get(i).getareaname()+childData.get(i).getcontractname());
                map.put("num", childData.get(i).getBatchColor()+childData.get(i).getnumberofbreakoff());
                data_list.add(map);
            }

            String [] from ={"name","num"};
            int [] to = {R.id.tv_area,R.id.tv_num};
            sim_adapter=new SimpleAdapter(context,data_list,R.layout.dl_gridview_item,from,to);
            listItemView.gridView.setAdapter(sim_adapter);
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
        if (listData.get(groupPosition).getBreakOff_newList() == null)
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dl_parentitem, null);
        }
        TextView tv_park = (TextView) convertView.findViewById(R.id.tv_park);


        tv_park.setText(listData.get(groupPosition).getBatchTime()+"-"+listData.get(groupPosition).getBatchColor());
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
