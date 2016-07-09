package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.bean.BatchTime;
import com.farm.bean.areatab;
import com.farm.common.utils;
import com.farm.ui.CZ_ContractBatchTimeSale_;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.CustomListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class Adapter_BatchTimeSale_CZ extends BaseExpandableListAdapter
{
    GridViewAdapter_SellOrDetail_NCZ gridViewAdapter_sellOrDetail_ncz;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<BatchTime> listData;
    ListView list;

    public Adapter_BatchTimeSale_CZ(Context context, List<BatchTime> listData, ExpandableListView mainlistview)
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
        public CustomListView lv;

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

        List<areatab> childData = listData.get(groupPosition).getAreatabList();
        final areatab areatab = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_batchtimesale_cz, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.lv = (CustomListView) convertView.findViewById(R.id.lv);
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }
            gridViewAdapter_sellOrDetail_ncz = new GridViewAdapter_SellOrDetail_NCZ(context, childData, listData.get(groupPosition).getBatchTime());
            listItemView.lv.setAdapter(gridViewAdapter_sellOrDetail_ncz);
            utils.setListViewHeight(listItemView.lv);
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
        if (listData.get(groupPosition).getAreatabList().size() == 0)
        {
            Toast.makeText(context, "该园区暂无批次", Toast.LENGTH_SHORT).show();
        }
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getAreatabList().size() == 0)
        {
            return 0;
        }
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
            if (groupPosition == 0)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.adapter_czbatchtimesale_parenttopitem, null);
            } else
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.adapter_czbatchtimesale_parent, null);
            }

        }

        TextView tv_salein = (TextView) convertView.findViewById(R.id.tv_salein);
        TextView tv_saleout = (TextView) convertView.findViewById(R.id.tv_saleout);
        TextView tv_allsale = (TextView) convertView.findViewById(R.id.tv_allsale);
        TextView tv_salefor = (TextView) convertView.findViewById(R.id.tv_salefor);
        TextView tv_contractname = (TextView) convertView.findViewById(R.id.tv_contractname);
        BatchTime batchTime = listData.get(groupPosition);

        tv_contractname.setText(batchTime.getBatchTime());
        tv_salefor.setText(batchTime.getAllsalefor());
        tv_allsale.setText(batchTime.getAllnumber());
        tv_saleout.setText(batchTime.getAllsaleout());
        tv_salein.setText(batchTime.getAllsalein());

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


    public class GridViewAdapter_SellOrDetail_NCZ extends BaseAdapter
    {
        List<areatab> list;
        EditText et_number;
        CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
        private Context context;
        Holder view;
        String batchtime;

        public GridViewAdapter_SellOrDetail_NCZ(Context context, List<areatab> list, String batchtime)
        {
            this.batchtime = batchtime;
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount()
        {
            if (list != null && list.size() > 0) return list.size();
            else return 0;
        }

        @Override
        public Object getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = View.inflate(context, R.layout.adapter_czbatchtimesale_childitem, null);
                view = new Holder(convertView);
                areatab areatab = list.get(position);
                view.tv_salein.setText(areatab.getAllsalein());
                view.tv_saleout.setText(areatab.getAllsaleout());
                view.tv_salefor.setText(areatab.getAllsalefor());
                view.tv_allsale.setText(areatab.getAllnumber());
                view.tv_contractname.setText(list.get(position).getareaName());
                convertView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String areaid = (String) v.getTag(R.id.tag_areaid);
                        String areaname = (String) v.getTag(R.id.tag_areaname);
                        String batchtime = (String) v.getTag(R.id.tag_batchtime);
                        Intent intent = new Intent(context, CZ_ContractBatchTimeSale_.class);
                        intent.putExtra("areaid", areaid);
                        intent.putExtra("areaname", areaname);
                        intent.putExtra("batchtime", batchtime);
                        context.startActivity(intent);
                    }
                });
                convertView.setTag(view);
                convertView.setTag(R.id.tag_areaid, areatab.getAreaid());
                convertView.setTag(R.id.tag_areaname, areatab.getareaName());
                convertView.setTag(R.id.tag_batchtime, batchtime);
            } else
            {
                view = (Holder) convertView.getTag();
            }


            return convertView;
        }

        private class Holder
        {
            private TextView tv_salefor;
            private TextView tv_salein;
            private TextView tv_saleout;
            private TextView tv_allsale;
            private TextView tv_contractname;

            public Holder(View view)
            {
                tv_contractname = (TextView) view.findViewById(R.id.tv_contractname);
                tv_allsale = (TextView) view.findViewById(R.id.tv_allsale);
                tv_saleout = (TextView) view.findViewById(R.id.tv_saleout);
                tv_salein = (TextView) view.findViewById(R.id.tv_salein);
                tv_salefor = (TextView) view.findViewById(R.id.tv_salefor);
            }
        }


    }
}
