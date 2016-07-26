package com.farm.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.Park_AllCBH;
import com.farm.bean.WZ_CRk;
import com.farm.bean.WZ_RKxx;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.ui.NCZ_WZ_RKDetail_;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/6/24.
 */
public class CustomDialog_Expandlistview extends Dialog
{
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    CustomDialogListener cdListener;
    Context context;
    View layout;
    List<Park_AllCBH> listData;
    String currentDat = "";
    ExpandableListView expandableListView;

    public CustomDialog_Expandlistview(Context context, int theme, View layout, List<Park_AllCBH> listData, CustomDialogListener cdListener)
    {
        super(context, theme);
        this.context = context;
        this.layout = layout;
        this.listData = listData;
        this.cdListener = cdListener;
    }

    public CustomDialog_Expandlistview(Context context, int theme)
    {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(layout);
        this.setCanceledOnTouchOutside(true);
//        lv=findViewById(R.id.action_settings);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new Cbaohudechose());
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l)
            {
                Bundle bundle = new Bundle();
                bundle.putString("areaId", listData.get(i).getId());
                bundle.putString("areaName", listData.get(i).getAreaName());
                bundle.putString("contractId", listData.get(i).getContractList().get(i1).getid());
                bundle.putString("contractNum", listData.get(i).getContractList().get(i1).getContractNum());
                bundle.putString("parkId", listData.get(i).getParkId());
                bundle.putString("parkName", listData.get(i).getParkName());
                cdListener.OnClick(bundle);
                dismiss();
                return true;
            }
        });
/*        lv.setAdapter(new Dialog_ListViewAdapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Bundle bundle = new Bundle();
                bundle.putString("id", listid.get(position));
                bundle.putString("name", listdata.get(position));
                cdListener.OnClick(bundle);
                dismiss();
            }
        });*/
     /*    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                parktab parktab = listNewData.get(groupPosition);
                BatchTime batchTime = parktab.getBatchTimeList().get(childPosition);
                Intent intent = new Intent(NCZ_FarmSale.this, NCZ_FarmSale_BatchDetail_.class);
                intent.putExtra("parkid", parktab.getid());
                intent.putExtra("batchTime", batchTime.getBatchTime());
                NCZ_FarmSale.this.startActivity(intent);
                return true;
            }
        });*/
    }


    public class Cbaohudechose extends BaseExpandableListAdapter
    {


        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            if (listData.get(groupPosition).getContractList() == null)
            {
                return null;
            }
            return listData.get(groupPosition).getContractList().get(childPosition);
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

            List<contractTab> childData = listData.get(groupPosition).getContractList();
            final contractTab contractTab = childData.get(childPosition);

            View v = null;
            if (lmap.get(groupPosition) != null)
            {
                HashMap<Integer, View> map1 = lmap.get(groupPosition);
                v = lmap.get(groupPosition).get(childPosition);
            }
            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.cut_children, null);
                listItemView = new ListItemView();
                listItemView.contractTab = (TextView) convertView.findViewById(R.id.contractTab);
/*                convertView.setTag(listItemView);
                convertView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(context, NCZ_WZ_RKDetail_.class);

                        intent.putExtra("wz_rKxx", wz_rKxx);
                        intent.putExtra("batchname", batchname);
                        intent.putExtra("indate", indate);
                        context.startActivity(intent);

                    }
                });*/
                map.put(childPosition, convertView);
                lmap.put(groupPosition, map);
                if (isLastChild)
                {
                    map = new HashMap<>();
                }

                //数据添加
                listItemView.contractTab.setText(contractTab.getContractNum());

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
            super.onGroupExpanded(groupPosition);
            int len = this.getGroupCount();

            for (int i = 0; i < len; i++) {
                if (i != groupPosition) {
                    expandableListView.collapseGroup(i);
                }
            }
        }
        @Override
        public void onGroupCollapsed(int groupPosition)
        {
            super.onGroupCollapsed(groupPosition);


//        mainlistview  WZ_RKExecute_Adapter
        }
        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition)
        {
            if (listData.get(groupPosition).getContractList() == null)
            {
                return 0;
            }
            return listData.get(groupPosition).getContractList().size();
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
                convertView = inflater.inflate(R.layout.cut_parent, null);
            }
            TextView inGoodsValue = (TextView) convertView.findViewById(R.id.inGoodsValue);



            inGoodsValue.setText(listData.get(groupPosition).getAreaName());

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

    static class ListItemView
    {
        public TextView contractTab;
    }

    public interface CustomDialogListener
    {
        void OnClick(Bundle bundle);
    }

}
