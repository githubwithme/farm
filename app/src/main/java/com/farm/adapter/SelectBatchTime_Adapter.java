package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BatchTime;
import com.farm.bean.parktab;
import com.farm.widget.CustomDialog_ListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class SelectBatchTime_Adapter extends BaseExpandableListAdapter
{
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<parktab> listData;
    ListView list;

    public SelectBatchTime_Adapter(Context context, List<parktab> listData, ExpandableListView mainlistview)
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
    }

    static class ListItemView
    {
        public TextView tv_salefor;
        public TextView tv_color;
        public TextView tv_batchtime;
        public LinearLayout ll_batchtime;
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
        List<BatchTime> childData = listData.get(groupPosition).getBatchTimeList();
        final BatchTime batchTime = childData.get(childPosition);

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
                convertView = LayoutInflater.from(context).inflate(R.layout.ncz_batchtime_topadater, null);
            } else
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.adater_batchtime_child, null);
            }

            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_color = (TextView) convertView.findViewById(R.id.tv_color);
            listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
            listItemView.tv_salefor = (TextView) convertView.findViewById(R.id.tv_salefor);
            listItemView.ll_batchtime = (LinearLayout) convertView.findViewById(R.id.ll_batchtime);
            if (batchTime.getBatchColor().equals("红色"))
            {
//                listItemView.tv_color.setBackgroundColor(Color.parseColor("#ff4444"));
                listItemView.tv_color.setText("红色");
            } else if (batchTime.getBatchColor().equals("蓝色"))
            {
//                listItemView.tv_color.setBackgroundColor(Color.parseColor("#add8e6"));
                listItemView.tv_color.setText("蓝色");
            } else if (batchTime.getBatchColor().equals("绿色"))
            {
//                listItemView.tv_color.setBackgroundColor(Color.parseColor("#90ee90"));
                listItemView.tv_color.setText("绿色");
            } else if (batchTime.getBatchColor().equals("紫色"))
            {
//                listItemView.tv_color.setBackgroundColor(Color.parseColor("#d8bfd8"));
                listItemView.tv_color.setText("紫色");
            } else
            {
//                listItemView.tv_color.setBackgroundColor(Color.parseColor("#ffff00"));
                listItemView.tv_color.setText("青色");
            }

            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            int percent = 0;
            float allnumber = Integer.valueOf(batchTime.getAllsaleout()) + Integer.valueOf(batchTime.getAllsalein()) + Integer.valueOf(batchTime.getAllnewsale()) + Integer.valueOf(batchTime.getAllsalefor());
            float salenumber = Integer.valueOf(batchTime.getAllsaleout()) + Integer.valueOf(batchTime.getAllsalein()) + Integer.valueOf(batchTime.getAllnewsale());
            if (allnumber != 0)
            {
                percent = (int) ((salenumber / allnumber) * 100);
            }
            listItemView.tv_batchtime.setText(batchTime.getBatchTime());
            listItemView.tv_salefor.setText(batchTime.getAllsalefor());
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
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getBatchTimeList() == null)
        {
            return 0;
        }
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_batchtime_parent, null);
        }
        TextView tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        LinearLayout ll_saleinfo = (LinearLayout) convertView.findViewById(R.id.ll_saleinfo);
        LinearLayout ll_allsele = (LinearLayout) convertView.findViewById(R.id.ll_allsele);
        TextView tv_saleinfo = (TextView) convertView.findViewById(R.id.tv_saleinfo);
        TextView tv_allnumber = (TextView) convertView.findViewById(R.id.tv_allnumber);
        TextView tv_pb = (TextView) convertView.findViewById(R.id.tv_pb);
        ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.pb);
        View view = (View) convertView.findViewById(R.id.view);
        view.setTag(convertView);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View ll = (View) v.getTag();
                LinearLayout ll_saleinfo = (LinearLayout) ll.findViewById(R.id.ll_saleinfo);
                LinearLayout ll_allsele = (LinearLayout) ll.findViewById(R.id.ll_allsele);
                if (ll_saleinfo.isShown())
                {
                    ll_saleinfo.setVisibility(View.GONE);
                    ll_allsele.setVisibility(View.GONE);
                } else
                {
                    ll_saleinfo.setVisibility(View.VISIBLE);
                    ll_allsele.setVisibility(View.VISIBLE);
                }
            }
        });
        parktab parktab = listData.get(groupPosition);
        int percent = 0;
        float allnumber = Integer.valueOf(parktab.getAllsaleout()) + Integer.valueOf(parktab.getAllsalein()) + Integer.valueOf(parktab.getAllnewsale()) + Integer.valueOf(parktab.getAllsalefor());
        float salenumber = Integer.valueOf(parktab.getAllsaleout()) + Integer.valueOf(parktab.getAllsalein()) + Integer.valueOf(parktab.getAllnewsale());
        if (allnumber != 0)
        {
            percent = (int) ((salenumber / allnumber) * 100);
        }
        pb.setProgress(percent);
        tv_pb.setText("共售" + percent + "%");

        tv_parkname.setText(parktab.getparkName());
        tv_allnumber.setText("总产量" + allnumber);
        tv_saleinfo.setText("已售" + parktab.getAllsaleout() + "    售中" + parktab.getAllsalein() + "    拟售" + parktab.getAllnewsale());
        if (parktab.getAllsalefor().equals("0"))
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
            tv_number.setText("库存量" + parktab.getAllsalefor() + "株");
        }
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
