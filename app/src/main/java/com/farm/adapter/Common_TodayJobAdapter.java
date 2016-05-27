package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.ui.Common_CommandDetail_Show_;
import com.farm.ui.RecordList_;
import com.farm.widget.CircleImageView;
import com.swipelistview.SimpleSwipeListener;
import com.swipelistview.SwipeLayout;

import java.util.HashMap;
import java.util.List;

public class Common_TodayJobAdapter extends BaseAdapter
{
    String audiopath;
    private Context context;// 运行上下文
    private List<jobtab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    jobtab jobtab;
    ListItemView listItemView = null;

    static class ListItemView
    {
        public TextView tv_jobname;
        public TextView tv_jobtime;
        public TextView tv_tip_pf;
        public TextView tv_score;
        public TextView tv_importance;
        public CircleImageView circle_img;
        public SwipeLayout swipeLayout;
        public LinearLayout ll_menu;
        public Button btn_connect;
        public TextView tv_new_item;
        public TextView tv_time;
        public TextView tv_type;
        public TextView tv_zf;
        public FrameLayout fl_new;
        public FrameLayout fl_new_item;
        public TextView tv_new;
        public ImageView iv_record;
    }

    public Common_TodayJobAdapter(Context context, List<jobtab> data)
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
            convertView = listContainer.inflate(R.layout.cz_pq_todayjobadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
            listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
            listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
            listItemView.tv_jobtime = (TextView) convertView.findViewById(R.id.tv_jobtime);
            listItemView.tv_jobname = (TextView) convertView.findViewById(R.id.tv_jobname);
            listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
            listItemView.tv_tip_pf = (TextView) convertView.findViewById(R.id.tv_tip_pf);
            listItemView.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            listItemView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            listItemView.tv_new_item = (TextView) convertView.findViewById(R.id.tv_new_item);
            listItemView.tv_zf = (TextView) convertView.findViewById(R.id.tv_zf);
            listItemView.btn_connect = (Button) convertView.findViewById(R.id.btn_connect);
            listItemView.tv_importance = (TextView) convertView.findViewById(R.id.tv_importance);
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            listItemView.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
            listItemView.ll_menu = (LinearLayout) convertView.findViewById(R.id.ll_menu);
            // 当隐藏的删除menu被打开的时候的回调函数
            listItemView.swipeLayout.addSwipeListener(new SimpleSwipeListener()
            {
                @Override
                public void onOpen(SwipeLayout layout)
                {
//                    Toast.makeText(context, "Open", Toast.LENGTH_SHORT).show();
                }
            });
            // 双击的回调函数
            listItemView.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener()
            {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface)
                {
//                    Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
                }
            });
            // 添加删除布局的点击事件
            listItemView.ll_menu.setId(position);
            listItemView.ll_menu.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
//                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                    // 点击完成之后，关闭删除menu
                    listItemView.swipeLayout.close();
                    jobtab job = listItems.get(v.getId());
                    Intent intent = new Intent(context, Common_CommandDetail_Show_.class);
                    intent.putExtra("cmdid", job.getcommandID());
                    context.startActivity(intent);
                }
            });
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
            listItemView.btn_connect.setId(position);
            listItemView.btn_connect.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    jobtab job = listItems.get(v.getId());
                    Intent intent = new Intent(context, Common_CommandDetail_Show_.class);
                    intent.putExtra("cmdid", job.getcommandID());
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
        listItemView.circle_img.setImageResource(R.drawable.yb);
        listItemView.tv_importance.setText(jobtab.getjobFromName());
//        if (jobtab.getImportance().equals("0"))
//        {
//            listItemView.tv_importance.setText("一般");
////            listItemView.circle_img.setImageResource(R.color.bg_blue);
//            listItemView.circle_img.setImageResource(R.drawable.yb);
//        } else if (jobtab.getImportance().equals("1"))
//        {
//            listItemView.tv_importance.setText("重要");
////            listItemView.circle_img.setImageResource(R.color.bg_green);
//            listItemView.circle_img.setImageResource(R.drawable.zyx);
//        } else if (jobtab.getImportance().equals("2"))
//        {
//            listItemView.tv_importance.setText("非常重要");
////            listItemView.circle_img.setImageResource(R.color.color_orange);
//            listItemView.circle_img.setImageResource(R.drawable.fczy);
//        } else if (jobtab.getImportance().equals("3"))
//        {
//            listItemView.tv_importance.setText("自");
//            listItemView.circle_img.setImageResource(R.color.bg_job);
//        }
        if (jobtab.getFrom().equals("0"))
        {
            listItemView.tv_zf.setText("下发");
        } else
        {
            listItemView.tv_zf.setText("自发");
        }
        listItemView.tv_type.setText(jobtab.getparkName() + jobtab.getareaName());
//        if (jobtab.getstdJobType().equals("0"))
//        {
//            listItemView.tv_type.setText("非标准生产指令");
//        } else if (jobtab.getstdJobType().equals("-1"))
//        {
//            listItemView.tv_type.setText("非生产指令");
//        } else
//        {
//            listItemView.tv_type.setText("标准生产指令");
//        }
        listItemView.tv_time.setText(jobtab.getregDate().substring(0, jobtab.getregDate().lastIndexOf(":")));
        listItemView.tv_jobtime.setText(jobtab.getregDate().substring(5, jobtab.getregDate().lastIndexOf(":")));
        if (jobtab.getaudioJobExecPath().equals("-1"))
        {
            listItemView.tv_score.setText("暂无");
        } else
        {
            listItemView.tv_score.setTextColor(context.getResources().getColor(R.color.red));
            if (jobtab.getaudioJobExecPath().equals("0"))
            {
                listItemView.tv_score.setText("0分");
            } else if (jobtab.getaudioJobExecPath().equals("8"))
            {
                listItemView.tv_score.setText("8分");
            } else if (jobtab.getaudioJobExecPath().equals("10"))
            {
                listItemView.tv_score.setText("10分");
            }
        }

        return convertView;
    }
}