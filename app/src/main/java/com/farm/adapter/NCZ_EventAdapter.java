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
import com.farm.bean.Event;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.ui.RecordList_;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class NCZ_EventAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<Event> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    Event Event;

    static class ListItemView
    {
        public TextView tv_cmdname;
        public TextView tv_qyts;
        public TextView tv_qx;
        public TextView tv_clqk;
        public ImageView circle_img;
        public ImageView iv_record;
        public FrameLayout rl_record;
        public FrameLayout fl_new_item;
        public FrameLayout fl_new;
        public TextView tv_new;
    }

    public NCZ_EventAdapter(Context context, List<Event> data)
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
        Event = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.listitem_event, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
            listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
            listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
            listItemView.circle_img = (ImageView) convertView.findViewById(R.id.circle_img);
            listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
            listItemView.rl_record = (FrameLayout) convertView.findViewById(R.id.rl_record);
            listItemView.tv_qyts = (TextView) convertView.findViewById(R.id.tv_qyts);
            listItemView.tv_qx = (TextView) convertView.findViewById(R.id.tv_qx);
            listItemView.tv_cmdname = (TextView) convertView.findViewById(R.id.tv_cmdname);
            listItemView.tv_clqk = (TextView) convertView.findViewById(R.id.tv_clqk);

            listItemView.rl_record.setId(position);
            listItemView.iv_record.setId(position);
            listItemView.rl_record.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Event command = listItems.get(v.getId());
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.updateStatus(context, "1", command.getSJID(), "2", commembertab.getId());
                    Intent intent = new Intent(context, RecordList_.class);
                    intent.putExtra("type", "2");
                    intent.putExtra("workid", listItems.get(v.getId()).getSJID());
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
//        if (Integer.valueOf(Event.getComCount()) > 0)
//        {
//            listItemView.fl_new_item.setVisibility(View.VISIBLE);
//        } else
//        {
//            listItemView.fl_new_item.setVisibility(View.GONE);
//        }
//        if (Integer.valueOf(Event.getComvidioCount()) > 0)
//        {
//            listItemView.fl_new.setVisibility(View.VISIBLE);
//        } else
//        {
//            listItemView.fl_new.setVisibility(View.GONE);
//        }
//        if (Event.getstdJobType().equals("0") || Event.getstdJobType().equals("-1"))
//        {
//            listItemView.tv_cmdname.setText(Event.getcommNote().toString());
//        } else
//        {
//            listItemView.tv_cmdname.setText(Event.getstdJobTypeName() + "-" + Event.getstdJobName());
//        }

//        listItemView.tv_zf.setText();
//        listItemView.tv_type.setText();
        listItemView.tv_clqk.setText(Event.getCLQK());
        listItemView.tv_cmdname.setText(Event.getSJMS());
        listItemView.tv_qx.setText(Event.getSBSJ());
        BitmapHelper.setImageViewBackground(context, listItemView.circle_img,Event.getImgUrl().get(0));
//        listItemView.tv_clqk.setText("发布于 " + Event.getregDate().subSequence(0, Event.getregDate().lastIndexOf(" ")));
//        if (Event.getimportance().equals("0"))
//        {
//            listItemView.tv_importance.setText("一般");
//            listItemView.circle_img.setImageResource(R.color.bg_blue);
//        } else if (Event.getimportance().equals("1"))
//        {
//            listItemView.tv_importance.setText("重要");
//            listItemView.circle_img.setImageResource(R.color.bg_green);
//        } else if (Event.getimportance().equals("2"))
//        {
//            listItemView.tv_importance.setText("非常重要");
//            listItemView.circle_img.setImageResource(R.color.color_orange);
//        } else if (Event.getimportance().equals("3"))
//        {
//            listItemView.tv_importance.setTextColor(context.getResources().getColor(R.color.bg_text));
//            listItemView.tv_importance.setText("未知");
////			 listItemView.circle_img.setBorderColor(context.getResources().getColor(R.color.bg_text));
//        }
        return convertView;
    }

}