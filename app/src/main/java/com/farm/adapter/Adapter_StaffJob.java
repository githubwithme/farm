package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.ui.RecordList_;
import com.farm.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_StaffJob extends BaseAdapter
{
    int colorpostion = 0;
    String audiopath;
    private Context context;// 运行上下文
    private List<String> list_username = new ArrayList<>();// 数据集合
    private List<Map<String, String>> list_map = new ArrayList<>();// 数据集合
    private List<jobtab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    jobtab jobtab;
    ListItemView listItemView = null;

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

    public Adapter_StaffJob(Context context, List<jobtab> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }

    public int getCount()
    {
        return listItems.size();
    }

    public Object getItem(int arg0)
    {
        return null;
    }

    public long getItemId(int arg0)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        jobtab = listItems.get(position);
        // 自定义视图
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_staffjob, null);
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


            listItemView.iv_record.setId(position);
            listItemView.iv_record.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    jobtab job = listItems.get(v.getId());
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.updateStatus(context, "1", job.getId(), "1", commembertab.getId());
                    Intent intent = new Intent(context, RecordList_.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("workid", listItems.get(v.getId()).getId());
                    context.startActivity(intent);
                }
            });


            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
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


        int[] color = new int[]{R.color.bg_ask, R.color.bg_work, R.color.red, R.color.gray, R.color.green, R.color.yellow, R.color.blue, R.color.color_orange, R.color.bg_job, R.color.bg_plant, R.color.bg_main, R.color.bg_titlebar, R.color.bg_text_small};
        if (position == 0)
        {
            colorpostion = 0;
            listItemView.circle_img.setImageResource(R.color.bg_ask);
        }
        for (int i = 0; i < position; i++)
        {
            if (listItems.get(i).getjobFromName().equals(jobtab.getjobFromName()))
            {
                listItemView.circle_img.setImageResource(color[colorpostion]);
                break;
            } else if (i == position - 1)
            {
                colorpostion = colorpostion + 1;
                listItemView.circle_img.setImageResource(color[colorpostion]);
            }
        }

        listItemView.tv_staffname.setText(jobtab.getjobFromName());
        listItemView.tv_area.setText("正在"+jobtab.getareaName() + "执行");
        return convertView;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
        colorpostion = 0;
    }
}