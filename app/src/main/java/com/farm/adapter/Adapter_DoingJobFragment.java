package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.bean.parktab;
import com.farm.ui.RecordList_;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomDialog_ListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class Adapter_DoingJobFragment extends BaseExpandableListAdapter
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

    public Adapter_DoingJobFragment(Context context, List<parktab> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getJobtabList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getJobtabList().get(childPosition);
    }

    static class ListItemView
    {
        public TextView tv_area;
        public TextView tv_jobname;
        public TextView tv_staffname;
        public ImageView iv_record;
        public CircleImageView circle_img;
        public FrameLayout fl_new;
        public FrameLayout fl_new_item;
        public TextView tv_new;
        public TextView tv_new_item;

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
        List<jobtab> childData = listData.get(groupPosition).getJobtabList();
        final jobtab jobtab = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_doingjob_child, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            listItemView.fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
            listItemView.tv_new_item = (TextView) convertView.findViewById(R.id.tv_new_item);
            listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
            listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
            listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            listItemView.tv_jobname = (TextView) convertView.findViewById(R.id.tv_jobname);
            listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
            listItemView.tv_staffname = (TextView) convertView.findViewById(R.id.tv_staffname);


            listItemView.iv_record.setTag(R.id.tag_parentpostion, groupPosition);
            listItemView.iv_record.setTag(R.id.tag_childpostion, childPosition);
            listItemView.iv_record.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int grouppostion = (int) v.getTag(R.id.tag_parentpostion);
                    int childPosition = (int) v.getTag(R.id.tag_childpostion);
                    List<jobtab> jobtabList = (List<jobtab>) listData.get(grouppostion);
                    jobtab jobtab = jobtabList.get(childPosition);
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.updateStatus(context, "1", jobtab.getId(), "1", commembertab.getId());
                    Intent intent = new Intent(context, RecordList_.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("workid", jobtab.getId());
                    context.startActivity(intent);
                }
            });

            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

        } else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片
        if (jobtab.getstdJobType().equals("0") || jobtab.getstdJobType().equals("-1"))
        {
            if (jobtab.getjobNote().equals(""))
            {
                listItemView.tv_jobname.setText("暂无说明");
            } else
            {
                listItemView.tv_jobname.setText(jobtab.getjobNote());
            }
        } else
        {
            listItemView.tv_jobname.setText(jobtab.getstdJobTypeName() + "-" + jobtab.getstdJobName());
        }

        if (Integer.valueOf(jobtab.getJobCount()) > 0)
        {
            listItemView.fl_new_item.setVisibility(View.VISIBLE);
            listItemView.tv_new_item.setText(jobtab.getJobCount());
        } else
        {
            listItemView.fl_new_item.setVisibility(View.GONE);
        }
        if (Integer.valueOf(jobtab.getJobvidioCount()) > 0)
        {
            listItemView.fl_new.setVisibility(View.VISIBLE);
            listItemView.tv_new.setText(jobtab.getJobvidioCount());
        } else
        {
            listItemView.fl_new.setVisibility(View.GONE);
        }
//        listItemView.circle_img.setImageResource(R.drawable.yb);


//        int[] color = new int[]{R.color.color_orange, R.color.blue, R.color.red};
//        if (position == 0)
//        {
//            colorpostion = 0;
//            listItemView.circle_img.setImageResource(R.color.color_orange);
//        }
//        for (int i = 0; i < position; i++)
//        {
//            if (listItems.get(i).getjobFromName().equals(jobtab.getjobFromName()))
//            {
//                listItemView.circle_img.setImageResource(color[colorpostion]);
//                break;
//            } else if (i == position - 1)
//            {
//                colorpostion = colorpostion + 1;
//                listItemView.circle_img.setImageResource(color[colorpostion]);
//            }
//        }

        listItemView.tv_staffname.setText(jobtab.getjobFromName());
        listItemView.tv_area.setText(jobtab.getareaName() + "执行");
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
        if (listData.get(groupPosition).getJobtabList() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getJobtabList().size();
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
            convertView = inflater.inflate(R.layout.adapter_doingjob_parent, null);
        }
        TextView tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);
        tv_parkname.setText(listData.get(groupPosition).getparkName());
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
