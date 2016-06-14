package com.farm.adapter;

import android.content.Context;
import android.graphics.Color;
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
public class Adapter_SelectBatchTime extends BaseExpandableListAdapter
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

    public Adapter_SelectBatchTime(Context context, List<parktab> listData, ExpandableListView mainlistview)
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
        public TextView tv_saleinfo;
        public LinearLayout ll_allsele;
        public LinearLayout ll_saleinfo;
        public ProgressBar pb;
        public TextView tv_pb;
        public TextView tv_allnumber;
        public TextView tv_batchtime;
        public TextView tv_number;
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_batchtime_adater, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.ll_saleinfo = (LinearLayout) convertView.findViewById(R.id.ll_saleinfo);
            listItemView.ll_allsele = (LinearLayout) convertView.findViewById(R.id.ll_allsele);
            listItemView.pb = (ProgressBar) convertView.findViewById(R.id.pb);
            listItemView.tv_pb = (TextView) convertView.findViewById(R.id.tv_pb);
            listItemView.tv_saleinfo = (TextView) convertView.findViewById(R.id.tv_saleinfo);
            listItemView.tv_allnumber = (TextView) convertView.findViewById(R.id.tv_allnumber);
            listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            View view = (View) convertView.findViewById(R.id.view);
            listItemView.ll_batchtime = (LinearLayout) convertView.findViewById(R.id.ll_batchtime);
//            listItemView.ll_batchtime.setTag(batchTime);
//            listItemView.ll_batchtime.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    BatchTime batchTime = (BatchTime) v.getTag();
//                    Intent intent = new Intent(context, NCZ_FarmSale_BatchDetail_.class);
//                    intent.putExtra("parkid", batchTime.getParkId());
//                    intent.putExtra("batchTime", batchTime.getBatchTime());
//                    context.startActivity(intent);
//                }
//            });
            if (batchTime.getBatchColor().equals("红色"))
            {
                listItemView.ll_batchtime.setBackgroundColor(Color.parseColor("#ff4444"));
               /* groupExpand.setBackgroundColor(Color.parseColor("#ff4444"));
                tv_park.setBackgroundColor(Color.parseColor("#ff4444"));
                tv_nums.setBackgroundColor(Color.parseColor("#ff4444"));*/
            } else if (batchTime.getBatchColor().equals("蓝色"))
            {

                listItemView.ll_batchtime.setBackgroundColor(Color.parseColor("#add8e6"));
            } else if (batchTime.getBatchColor().equals("绿色"))
            {
                listItemView.ll_batchtime.setBackgroundColor(Color.parseColor("#90ee90"));
            } else if (batchTime.getBatchColor().equals("紫色"))
            {
                listItemView.ll_batchtime.setBackgroundColor(Color.parseColor("#d8bfd8"));
            } else
            {
                listItemView.ll_batchtime.setBackgroundColor(Color.parseColor("#ffff00"));
            }
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
//            String p = String.valueOf(percent);
//            if (p.contains("."))
//            {
//                p = p.substring(0, p.lastIndexOf("."));
//            } else
//            {
//
//            }
            listItemView.pb.setProgress(percent);
            listItemView.tv_pb.setText("共售" + percent + "%");
            listItemView.tv_allnumber.setText("总产量" + allnumber);
            listItemView.tv_batchtime.setText(batchTime.getBatchTime());
            listItemView.tv_saleinfo.setText("已售" + batchTime.getAllsaleout() + "    售中" + batchTime.getAllsalein() + "    拟售" + batchTime.getAllnewsale());
            if (batchTime.getAllsalefor().equals("0"))
            {
                if (salenumber != 0)
                {
                    listItemView.tv_number.setText("已售完");
                } else
                {
                    listItemView.tv_number.setText("产量未上报");
                }

            } else
            {
                listItemView.tv_number.setText("待售" + batchTime.getAllsalefor());
            }

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
            convertView = inflater.inflate(R.layout.ncz_batchtime_parent, null);
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
            tv_number.setText("总株树:" + parktab.getAllsalefor());
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
