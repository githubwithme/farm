package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.ContactsBean;
import com.farm.bean.ContractGoodsUsed;
import com.farm.bean.commembertab;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_ListView;
import com.swipelistview.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_PGGoodsUsed extends BaseExpandableListAdapter
{
    CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
    int[] color;
    SwipeLayout swipeLayout;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<ContactsBean> listData;
    ListView list;

    public Adapter_PGGoodsUsed(Context context, List<ContactsBean> listData, ExpandableListView mainlistview)
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
        if (listData.get(groupPosition).getCommembertablist() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getCommembertablist().get(childPosition);
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
        public TextView tv_name;
        public TextView tv_number;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<commembertab> childData = listData.get(groupPosition).getCommembertablist();
        final commembertab commembertab = childData.get(childPosition);
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_pggoodsused_child, null);
            listItemView = new ListItemView();
            listItemView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            convertView.setTag(listItemView);
            convertView.setTag(R.id.tag_fi, listData.get(groupPosition).getType());
            listItemView.tv_number.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    showDialog_editBreakoffinfo();
                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            //数据添加
//            listItemView.tv_phone.setText(commembertab.getuserCell());
//            listItemView.tv_name.setText(commembertab.getrealName());
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
        if (listData.get(groupPosition).getCommembertablist() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getCommembertablist().size();
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
            convertView = inflater.inflate(R.layout.adapter_pggoodsused_parent, null);
        }
        TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);
        Button btn_addGoods = (Button) convertView.findViewById(R.id.btn_addGoods);
        btn_addGoods.setTag("");
        btn_addGoods.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_addgoods();
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

    public void showDialog_addgoods()
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_addgoods, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(context, R.style.MyDialog, dialog_layout);
        ListView lv = (ListView) dialog_layout.findViewById(R.id.lv);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
            }
        });
        List<ContractGoodsUsed> list = new ArrayList<>();
        ContractGoodsUsed contractGoodsUsed = new ContractGoodsUsed();
        contractGoodsUsed.setGoodsName("钾肥");
        ContractGoodsUsed contractGoodsUsed1 = new ContractGoodsUsed();
        contractGoodsUsed1.setGoodsName("磷肥");
        list.add(contractGoodsUsed);
        list.add(contractGoodsUsed1);
        Adapter_PGAddGoodsUsed adapter_pgAddGoodsUsed = new Adapter_PGAddGoodsUsed(context, list);
        lv.setAdapter(adapter_pgAddGoodsUsed);
        customDialog_editSaleInInfo.show();
    }

}