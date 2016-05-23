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
import com.farm.bean.WZ_CRk;
import com.farm.bean.WZ_RKxx;
import com.farm.ui.NCZ_WZ_RKDetail_;
import com.farm.ui.PG_WZ_CKDetail_;
import com.farm.widget.CustomDialog_ListView;
import com.swipelistview.SwipeLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/5/20.
 */
public class PG_CKExcute_adapter extends BaseExpandableListAdapter
{
    SwipeLayout swipeLayout;
    int currentgroupPosition = 0;
    int currentchildPosition = 0;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    //    List<goodslisttab> list_goods = new ArrayList<goodslisttab>();
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<WZ_CRk> listData;
    ListView list;

    public PG_CKExcute_adapter(Context context, List<WZ_CRk> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getWzcrkxx() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getWzcrkxx().get(childPosition);
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
        public TextView goodsname;
        public TextView local;
        public TextView quantity;
        public TextView inGoodsvalue;
    }
    //设置子item的组件
    @Override
    public View getChildView( int groupPosition,  int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<WZ_RKxx> childData = listData.get(groupPosition).getWzcrkxx();
        final WZ_RKxx wz_rKxx = childData.get(childPosition);
        final String inType=listData.get(groupPosition).getInType();
        final String indate=listData.get(groupPosition).getInDate();
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_children_rkexecute, null);
            listItemView = new ListItemView();
            listItemView.goodsname = (TextView) convertView.findViewById(R.id.goodsname);
            listItemView.local = (TextView) convertView.findViewById(R.id.local);
            listItemView.quantity = (TextView) convertView.findViewById(R.id.quantity);
            listItemView.inGoodsvalue = (TextView) convertView.findViewById(R.id.inGoodsvalue);
            convertView.setTag(listItemView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, NCZ_WZ_CKDetail_.class);
                    Intent intent = new Intent(context, PG_WZ_CKDetail_.class);

                    intent.putExtra("wz_rKxx", wz_rKxx);
                    intent.putExtra("inType", inType);
                    intent.putExtra("indate", indate);
                    context.startActivity(intent);

                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            //数据添加
            listItemView.goodsname.setText(wz_rKxx.getGoodsname());
            listItemView.local.setText(wz_rKxx.getParkName() + "-" + wz_rKxx.getStorehouseName());
            listItemView.quantity.setText("数量:"+wz_rKxx.getQuantity());
            listItemView.inGoodsvalue.setText("总值:"+wz_rKxx.getOutGoodsvalue() + "元");
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
    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);

    }
    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getWzcrkxx() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getWzcrkxx().size();
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
            convertView = inflater.inflate(R.layout.layout_parent_rkexecute, null);
        }
        TextView inDate = (TextView) convertView.findViewById(R.id.inDate);
        TextView batchName = (TextView) convertView.findViewById(R.id.batchName);
        TextView loadingFee = (TextView) convertView.findViewById(R.id.loadingFee);
        TextView shippingFee = (TextView) convertView.findViewById(R.id.shippingFee);
        TextView inGoodsValue = (TextView) convertView.findViewById(R.id.inGoodsValue);
//        String indata=listData.get(groupPosition).getInDate().substring(0,listData.get(groupPosition).getInDate().length()-8);
        inDate.setText(listData.get(groupPosition).getInDate());
//        inDate.setText(indata);
        batchName.setText(listData.get(groupPosition).getInType());
        loadingFee.setText("装卸费:"+listData.get(groupPosition).getLoadingFee()+"元");
        shippingFee.setText("运费:" + listData.get(groupPosition).getShippingFee() + "元");
        inGoodsValue.setText(listData.get(groupPosition).getInGoodsValue() + "元");
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
