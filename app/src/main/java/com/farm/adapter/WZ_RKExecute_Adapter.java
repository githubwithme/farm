package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.WZ_CRk;
import com.farm.bean.WZ_RKxx;
import com.farm.bean.commembertab;
import com.farm.ui.NCZ_WZ_RKDetail_;
import com.farm.widget.CustomDialog_ListView;
import com.swipelistview.SwipeLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/7.
 */
public class WZ_RKExecute_Adapter extends BaseExpandableListAdapter
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

    public WZ_RKExecute_Adapter(Context context, List<WZ_CRk> listData, ExpandableListView mainlistview)
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
        public TextView tv_goodsname;
        public TextView tv_number;
        public TextView tv_values;
        public TextView tv_type;
        public TextView tv_gg;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<WZ_RKxx> childData = listData.get(groupPosition).getWzcrkxx();
        final WZ_RKxx wz_rKxx = childData.get(childPosition);

        final String batchname = listData.get(groupPosition).getBatchName();
        final String indate = listData.get(groupPosition).getInDate();
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_nczrk_child, null);
            listItemView = new ListItemView();
            listItemView.tv_goodsname = (TextView) convertView.findViewById(R.id.tv_goodsname);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.tv_values = (TextView) convertView.findViewById(R.id.tv_values);
            listItemView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            listItemView.tv_gg = (TextView) convertView.findViewById(R.id.tv_gg);
            convertView.setTag(listItemView);
//            convertView.setTag(R.id.tag_bean, jobtab);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    jobtab jobtab = (com.farm.bean.jobtab) v.getTag(R.id.tag_bean);
                    Intent intent = new Intent(context, NCZ_WZ_RKDetail_.class);

                    intent.putExtra("wz_rKxx", wz_rKxx);
                    intent.putExtra("batchname", batchname);
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
            listItemView.tv_goodsname.setText(wz_rKxx.getGoodsname());
            listItemView.tv_values.setText(wz_rKxx.getInGoodsvalue()+"元");
            listItemView.tv_number.setText( wz_rKxx.getQuantity());
            listItemView.tv_gg.setText( wz_rKxx.getStorehouseName());
            listItemView.tv_type.setText( wz_rKxx.getInType());
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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent)
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
        FrameLayout fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
        RelativeLayout groupExpand = (RelativeLayout) convertView.findViewById(R.id.groupExpand);
        convertView.setTag(R.id.tag_rk, listData.get(groupPosition));
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isExpanded)
                {
                    mainlistview.collapseGroup(groupPosition);
                } else
                {
                    mainlistview.expandGroup(groupPosition);
                }
                WZ_CRk wz_cRk2 = (WZ_CRk) view.getTag(R.id.tag_rk);
                if (wz_cRk2.getFlashStr().equals("1"))
                {
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.eventStatus(context, "4", wz_cRk2.getBatchNumber(), commembertab.getId());
                }


            }
        });
        if (listData.get(groupPosition).getFlashStr().equals("1"))
        {
            fl_new_item.setVisibility(View.VISIBLE);
        } else
        {
            fl_new_item.setVisibility(View.GONE);
        }
/*        groupExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    mainlistview.collapseGroup(groupPosition);
                } else {
                    mainlistview.expandGroup(groupPosition);
                }
            }
        });*/
        inDate.setText(listData.get(groupPosition).getInType());
        batchName.setText("批次号:" + listData.get(groupPosition).getBatchName());
        loadingFee.setText("装卸费:" + listData.get(groupPosition).getLoadingFee() + "元");
        shippingFee.setText("运费:" + listData.get(groupPosition).getShippingFee() + "元");
         double a= Double.valueOf(listData.get(groupPosition).getInGoodsValue())+Double.valueOf(listData.get(groupPosition).getLoadingFee())+Double.valueOf(listData.get(groupPosition).getShippingFee());
        inGoodsValue.setText("总值:"+String.format("%.2f",a) + "元");
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
