package com.farm.adapter;

import android.annotation.SuppressLint;
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
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.ui.RecordList_;
import com.farm.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class NCZ_CommandAdapter extends BaseAdapter
{
    static String name = "";
    int colorpostion = 0;
    private Context context;// 运行上下文
    private List<commandtab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    commandtab commandtab;

    static class ListItemView
    {
        public TextView tv_area;
        public TextView tv_cmdname;
        public TextView tv_qyts;
        public TextView tv_qx;
        public TextView tv_time;
        public TextView tv_importance;
        public CircleImageView circle_img;
        public ImageView iv_record;
        public FrameLayout rl_record;
        public FrameLayout fl_new_item;
        public FrameLayout fl_new;
        public TextView tv_new;
        public TextView tv_new_item;
        public TextView tv_zf;
        public TextView tv_type;
    }

    public NCZ_CommandAdapter(Context context, List<commandtab> data)
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
        commandtab = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.listitem_command, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
            listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
            listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
            listItemView.tv_new_item = (TextView) convertView.findViewById(R.id.tv_new_item);
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
            listItemView.rl_record = (FrameLayout) convertView.findViewById(R.id.rl_record);
            listItemView.tv_qyts = (TextView) convertView.findViewById(R.id.tv_qyts);
            listItemView.tv_qx = (TextView) convertView.findViewById(R.id.tv_qx);
            listItemView.tv_importance = (TextView) convertView.findViewById(R.id.tv_importance);
            listItemView.tv_cmdname = (TextView) convertView.findViewById(R.id.tv_cmdname);
            listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            listItemView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            listItemView.tv_zf = (TextView) convertView.findViewById(R.id.tv_zf);

            listItemView.rl_record.setId(position);
            listItemView.iv_record.setId(position);
            listItemView.rl_record.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    commandtab command = listItems.get(v.getId());
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.updateStatus(context, "1", command.getId(), "2", commembertab.getId());
                    Intent intent = new Intent(context, RecordList_.class);
                    intent.putExtra("type", "2");
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
        if (Integer.valueOf(commandtab.getComCount()) > 0)
        {
            listItemView.fl_new_item.setVisibility(View.VISIBLE);
//            listItemView.tv_new_item.setText(commandtab.getComCount());

        } else
        {
            listItemView.fl_new_item.setVisibility(View.GONE);
        }
        if (Integer.valueOf(commandtab.getComvidioCount()) > 0)
        {
            listItemView.fl_new.setVisibility(View.VISIBLE);
            listItemView.tv_new.setText(commandtab.getComvidioCount());
        } else
        {
            listItemView.fl_new.setVisibility(View.GONE);
        }
        if (commandtab.getstdJobType().equals("0") || commandtab.getstdJobType().equals("-1"))
        {
            if (commandtab.getcommNote().toString().length() > 20)
            {
                listItemView.tv_cmdname.setText(commandtab.getcommNote().toString().substring(0, 19));
            } else
            {
                listItemView.tv_cmdname.setText(commandtab.getcommNote().toString());
            }

        } else
        {
            listItemView.tv_cmdname.setText(commandtab.getstdJobTypeName() + "-" + commandtab.getstdJobName());
        }

//        listItemView.tv_zf.setText();
//        listItemView.tv_type.setText();
        if (commandtab.getcommFromVPath().equals("0"))
        {
            listItemView.tv_zf.setText(commandtab.getcommFromName() + "下发");
        } else
        {
            listItemView.tv_zf.setText(commandtab.getcommFromName() + "自发");
        }
        if (commandtab.getstdJobType().equals("0"))
        {
            listItemView.tv_type.setText("非标准生产指令");
        } else if (commandtab.getstdJobType().equals("-1"))
        {
            listItemView.tv_type.setText("非生产指令");
        } else
        {
            listItemView.tv_type.setText("标准生产指令");
        }
        listItemView.circle_img.setImageResource(R.drawable.yb);
        listItemView.tv_area.setText(commandtab.getfeedbackuserName() + "正在执行中...");
        listItemView.tv_importance.setText(commandtab.getcommFromName());
        listItemView.tv_qyts.setText("要求天数-" + commandtab.getcommDays() + "天");
        listItemView.tv_qx.setText("开始 " + commandtab.getcommComDate());
        listItemView.tv_time.setText("发布于 " + commandtab.getregDate().subSequence(5, commandtab.getregDate().lastIndexOf(":")));
//        if (commandtab.getimportance().equals("0"))
//        {
//            listItemView.tv_importance.setText("一般");
////            listItemView.circle_img.setImageResource(R.color.bg_blue);
//            listItemView.circle_img.setImageResource(R.drawable.yb);
//        } else if (commandtab.getimportance().equals("1"))
//        {
//            listItemView.tv_importance.setText("重要");
////            listItemView.circle_img.setImageResource(R.color.bg_green);
//            listItemView.circle_img.setImageResource(R.drawable.zyx);
//        } else if (commandtab.getimportance().equals("2"))
//        {
//            listItemView.tv_importance.setText("非常重要");
////            listItemView.circle_img.setImageResource(R.color.color_orange);
//            listItemView.circle_img.setImageResource(R.drawable.fczy);
//        } else if (commandtab.getimportance().equals("3"))
//        {
//            listItemView.tv_importance.setTextColor(context.getResources().getColor(R.color.bg_text));
//            listItemView.tv_importance.setText("未知");
////			 listItemView.circle_img.setBorderColor(context.getResources().getColor(R.color.bg_text));
//        }
        int[] color = new int[]{R.color.bg_ask, R.color.red, R.color.blue, R.color.gray, R.color.green, R.color.bg_work,  R.color.blue, R.color.color_orange, R.color.bg_job, R.color.bg_plant, R.color.bg_main, R.color.bg_text_small,};
/*        if (position == 0)
        {
            colorpostion = 0;
            listItemView.circle_img.setImageResource(color[0]);
        }
        for (int i = 0; i < position; i++)
        {
            if (listItems.get(i).getcommFromName().equals(commandtab.getcommFromName()))
            {
                listItemView.circle_img.setImageResource(color[colorpostion]);
                break;
            } else if (i == position - 1)
            {
                colorpostion = colorpostion + 1;
                listItemView.circle_img.setImageResource(color[colorpostion]);
            }
        }*/
        commembertab commembertabss = AppContext.getUserInfo(context);

        if (name.equals(""))
        {
            name += commembertabss.getrealName() + ",";
        }

//        for(int i=0;i<position;i++)
        int str = position;


        //                String[] nongzi = commandtab.getnongziName().split(",");

        if (name.indexOf(listItems.get(position).getcommFromName()) != -1)
        {
            String[] data = name.split(",");
            for (int j = 0; j < data.length; j++)
            {
                if (data[j].equals(listItems.get(position).getcommFromName()))
                    listItemView.circle_img.setImageResource(color[j % color.length]);
                int x = j % color.length;
            }
        } else
        {
            String[] data = name.split(",");
            name += listItems.get(position).getcommFromName() + ",";
            listItemView.circle_img.setImageResource(color[(data.length ) % color.length]);
            int y = (data.length ) % color.length;
        }


        return convertView;
    }

}