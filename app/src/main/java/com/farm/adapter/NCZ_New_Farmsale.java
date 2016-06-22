package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.parktab;
import com.farm.ui.NCZ_FarmSale_BatchDetail_;
import com.farm.widget.CustomDialog_ListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/6/20.
 */
public class NCZ_New_Farmsale extends BaseExpandableListAdapter
{

    NCZ_New_farmsaleItem ncz_new_farmsaleItem;
    List<parktab> listData;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    public NCZ_New_Farmsale(Context context, List<parktab> listData, ExpandableListView mainlistview)
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
//        return listData.get(groupPosition).getBatchTimeList();
    }
    static class ListItemView
    {
//        public ListView exdata;
        public TextView tv_pc;
        public TextView quantity;
        public TextView leftquantity;
        public LinearLayout ll_linear;
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
    public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<BatchTime> childData = listData.get(groupPosition).getBatchTimeList();
        BatchTime batchTime=childData.get(childPosition);
        String id=listData.get(groupPosition).getid();
        View v = null;
  /*      if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }*/
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_new_farmsale, null);
            listItemView = new ListItemView();
//            listItemView.exdata = (ListView) convertView.findViewById(R.id.exdata);
            listItemView.tv_pc = (TextView) convertView.findViewById(R.id.tv_pc);
            listItemView.quantity = (TextView) convertView.findViewById(R.id.quantity);
            listItemView.leftquantity = (TextView) convertView.findViewById(R.id.leftquantity);
            listItemView.ll_linear = (LinearLayout) convertView.findViewById(R.id.ll_linear);


            listItemView.leftquantity.setTag(R.id.tag_kg,id);
            listItemView.leftquantity.setTag(R.id.tag_hg,childData.get(childPosition));
            listItemView.leftquantity.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AppContext.makeToast(context, "error_connectDataBase");

                    BatchTime batchTimes= (BatchTime) view.getTag(R.id.tag_hg);
//                    parktab parktab = (parktab) view.getTag(R.id.tag_hg);
                    String a= (String) view.getTag(R.id.tag_kg);
                    Intent intent = new Intent(context, NCZ_FarmSale_BatchDetail_.class);
                    intent.putExtra("parkid", a);
                    intent.putExtra("batchTime", batchTimes.getBatchTime());
                    context.startActivity(intent);
                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            int allnumber = Integer.valueOf(batchTime.getAllsaleout()) + Integer.valueOf(batchTime.getAllsalein()) + Integer.valueOf(batchTime.getAllnewsale()) + Integer.valueOf(batchTime.getAllsalefor());
            listItemView.tv_pc.setText(batchTime.getBatchTime());
            listItemView.quantity.setText(allnumber + "");
            if (Integer.valueOf(batchTime.getAllsalefor())>0)
            {
                listItemView.leftquantity.setTextColor(context.getResources().getColor(R.color.red));
//            tv.setTextColor(this.getResources().getColor(R.color.red))
            }
            listItemView.leftquantity.setText(batchTime.getAllsalefor());

            //数据添加
//            listItemView.goodsname.setText(wz_rKxx.getGoodsname());
/*            ncz_new_farmsaleItem = new NCZ_New_farmsaleItem(context, childData);
            listItemView.exdata.setAdapter(ncz_new_farmsaleItem);
            listItemView.exdata.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    parktab parktab = listData.get(groupPosition);
                    BatchTime batchTime = parktab.getBatchTimeList().get(childPosition);
                    Intent intent = new Intent(context, NCZ_FarmSale_BatchDetail_.class);
                    intent.putExtra("parkid", parktab.getid());
                    intent.putExtra("batchTime", batchTime.getBatchTime());
                    context.startActivity(intent);
                }
            });*/

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

    }
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
//        return listData.get(groupPosition).getWzcrkxx().size();
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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_new_farmsaleparent, null);
        }
        TextView tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        TextView tv_allnumber = (TextView) convertView.findViewById(R.id.tv_allnumber);

        parktab parktab = listData.get(groupPosition);
        int percent = 0;
        int allnumber = Integer.valueOf(parktab.getAllsaleout()) + Integer.valueOf(parktab.getAllsalein()) + Integer.valueOf(parktab.getAllnewsale()) + Integer.valueOf(parktab.getAllsalefor());
        float salenumber = Integer.valueOf(parktab.getAllsaleout()) + Integer.valueOf(parktab.getAllsalein()) + Integer.valueOf(parktab.getAllnewsale());

        if (Integer.valueOf(parktab.getAllsalefor())>0)
        {
            tv_number.setTextColor(context.getResources().getColor(R.color.red));
//            tv.setTextColor(this.getResources().getColor(R.color.red))
        }
        tv_parkname.setText(listData.get(groupPosition).getparkName());
        tv_allnumber.setText(allnumber+"");
        tv_number.setText(parktab.getAllsalefor());
/*        if (parktab.getAllsalefor().equals("0"))
        {
            if (salenumber != 0)
            {
                tv_number.setText("已售完");
            } else
            {
                tv_number.setText("产量未上报");
            }
        } else
        {
            tv_number.setText("待售" + parktab.getAllsalefor()+"株");
        }*/
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
        return false;
    }
}
