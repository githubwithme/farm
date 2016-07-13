package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.ParkGoods;
import com.farm.bean.ParkGoodsBean;
import com.farm.bean.WZ_Pcxx;
import com.farm.ui.NCZ_GoodsDistribution_;
import com.farm.widget.CustomDialog_ListView;
import com.swipelistview.SwipeLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/12.
 */
public class NCZ_GoodsDis_Adater extends BaseExpandableListAdapter
{
    int[] color;
    SwipeLayout swipeLayout;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<ParkGoodsBean> listData;
    ListView list;

    public NCZ_GoodsDis_Adater(Context context, List<ParkGoodsBean> listData, ExpandableListView mainlistview)
    {
        color = new int[]{R.color.bg_ask, R.color.bg_work, R.color.gray, R.color.green, R.color.bg_job, R.color.gray, R.color.green, R.color.bg_job, R.color.bg_plant, R.color.bg_text_small, R.color.bg_job, R.color.bg_plant, R.color.bg_text_small, R.color.bg_job, R.color.bg_plant, R.color.bg_text_small};
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getBatchNameLists() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getBatchNameLists().get(childPosition);
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
        public TextView tv_batchtime;
        public TextView tv_number;
        public TextView tv_value;
        public TextView tv_data;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<WZ_Pcxx> childData = listData.get(groupPosition).getBatchNameLists();
        final WZ_Pcxx wz_pcxx = childData.get(childPosition);
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            if (childPosition == 0)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.ncz_goodsdis_topadater, null);
            } else
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.ncz_goodsdis_child, null);
            }
      /*      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_goods_ncz, null);*/
            listItemView = new ListItemView();
            listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            listItemView.tv_data = (TextView) convertView.findViewById(R.id.tv_data);
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            listItemView.tv_batchtime.setText(wz_pcxx.getBatchName());
            listItemView.tv_value.setText(wz_pcxx.getInGoodsValue()+"元");
            listItemView.tv_data.setText(wz_pcxx.getExpDate().substring(0,wz_pcxx.getExpDate().length()-8));
            if (!wz_pcxx.getThree().equals(""))
            {
                listItemView.tv_number.setText(wz_pcxx.getThreeNum()+wz_pcxx.getThree() );
            }else if (wz_pcxx.getThree().equals("")&&!wz_pcxx.getSec().equals(""))
            {
                listItemView.tv_number.setText(wz_pcxx.getSecNum()+wz_pcxx.getSec() );
            }else
            {
                listItemView.tv_number.setText(wz_pcxx.getFirsNum()+wz_pcxx.getFirs());
            }
/*            String danwei="";

            //数据添加
            listItemView.tv_type.setText(ParkGoods.getUserDefTypeName());
            listItemView.tv_values.setText(ParkGoods.getInGoodsValue()+"元");
            listItemView.tv_goodsname.setText(ParkGoods.getGoodsName());
            listItemView.tv_gg.setText(ParkGoods.getGoodsStatistical()+ParkGoods.getGoodsunit()+"/"+danwei);//规格*/
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
//        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
//        super.onGroupCollapsed(groupPosition);
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getBatchNameLists() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getBatchNameLists().size();
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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_nczgoods_parent, null);
        }
        TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);

        tv_type.setText(listData.get(groupPosition).getStorehouseName());
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
