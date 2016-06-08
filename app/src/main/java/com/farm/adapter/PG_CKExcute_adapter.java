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
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.PG_WZckbean;
import com.farm.bean.WZ_CRk;
import com.farm.bean.commembertab;
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
        if (listData.get(groupPosition).getBreakOffList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getBreakOffList().get(childPosition);
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
        public TextView batchnums;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<PG_WZckbean> childData = listData.get(groupPosition).getBreakOffList();
        final PG_WZckbean pg_wZckbean = childData.get(childPosition);
        final String inType = listData.get(groupPosition).getIsConfirm();
        final String indate = listData.get(groupPosition).getRegDate();
        final String batchname = listData.get(groupPosition).getBatchName();
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pg_ck_itemlayout, null);
            listItemView = new ListItemView();
            listItemView.goodsname = (TextView) convertView.findViewById(R.id.goodsname);
            listItemView.local = (TextView) convertView.findViewById(R.id.local);
            listItemView.inGoodsvalue = (TextView) convertView.findViewById(R.id.inGoodsvalue);
            listItemView.batchnums = (TextView) convertView.findViewById(R.id.batchnums);
            convertView.setTag(listItemView);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    Intent intent = new Intent(context, NCZ_WZ_CKDetail_.class);
                    Intent intent = new Intent(context, PG_WZ_CKDetail_.class);
                    intent.putExtra("pg_wZckbean", pg_wZckbean);
                    intent.putExtra("inType", inType);
                    intent.putExtra("indate", indate);
                    intent.putExtra("batchname", batchname);
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
            listItemView.batchnums.setText("批次号:"+batchname);
            listItemView.goodsname.setText(pg_wZckbean.getGoodsName());
            listItemView.local.setText("出库位置:" + pg_wZckbean.getParkName() + "-" + pg_wZckbean.getStoreName());
            if (!pg_wZckbean.getThree().equals(""))
            {
                listItemView.inGoodsvalue.setText("出库" + pg_wZckbean.getThreeNum() + pg_wZckbean.getThree());
            } else if (pg_wZckbean.getThree().equals("") && !pg_wZckbean.getSec().equals(""))
            {
                listItemView.inGoodsvalue.setText("出库" + pg_wZckbean.getSecNum() + pg_wZckbean.getSec());
            } else
            {
                listItemView.inGoodsvalue.setText("出库" + pg_wZckbean.getFirsNum() + pg_wZckbean.getFirs());
            }

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
        if (listData.get(groupPosition).getBreakOffList() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getBreakOffList().size();
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
            convertView = inflater.inflate(R.layout.pg_ck_parentlayout, null);
        }
        TextView inDate = (TextView) convertView.findViewById(R.id.inDate);
        TextView batchName = (TextView) convertView.findViewById(R.id.batchName);

        TextView inGoodsValue = (TextView) convertView.findViewById(R.id.inGoodsValue);
        FrameLayout fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);


        convertView.setTag(R.id.tag_pgck, listData.get(groupPosition));
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
                WZ_CRk wz_cRk2 = (WZ_CRk) view.getTag(R.id.tag_pgck);
                if (wz_cRk2.getFlashStr().equals("1"))
                {
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.eventStatus(context, "6", wz_cRk2.getId(), commembertab.getId());
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
        String indata = listData.get(groupPosition).getRegDate().substring(0, listData.get(groupPosition).getRegDate().length() - 8);
        inDate.setText(indata + "出库一次");

        batchName.setText("出库批次号:" + listData.get(groupPosition).getBatchName());

        inGoodsValue.setText(listData.get(groupPosition).getIsConfirm());
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
