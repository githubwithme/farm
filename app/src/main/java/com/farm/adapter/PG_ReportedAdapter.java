package com.farm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FJxx;
import com.farm.bean.ReportedBean;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.ui.Event_Process_;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/11.
 */
@SuppressLint("NewApi")
public class PG_ReportedAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<ReportedBean> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    List<FJxx> fJxx;
    ReportedBean ReportedBean;

    static class ListItemView
    {
        public TextView tv_cmdname;
        public TextView tv_qyts;
        public TextView tv_qx;
        public TextView tv_clqk;
        public Button btn_dealevent;
        public ImageView circle_img;
        public ImageView iv_record;
        public FrameLayout rl_record;
        public FrameLayout fl_new_item;
        public FrameLayout fl_new;
        public TextView tv_new;
    }

    public PG_ReportedAdapter(Context context, List<ReportedBean> data)
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
        ReportedBean = listItems.get(position);
        fJxx = new ArrayList<FJxx>();
        fJxx = ReportedBean.getFjxx();
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
            listItemView.btn_dealevent = (Button) convertView.findViewById(R.id.btn_dealevent);


            listItemView.iv_record.setId(position);
            listItemView.iv_record.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ReportedBean reportedBean = listItems.get(v.getId());
                    commembertab commembertab = AppContext.getUserInfo(context);
                    Intent intent = new Intent(context, Event_Process_.class);
                    intent.putExtra("event", reportedBean);
                    context.startActivity(intent);
                }
            });
            listItemView.btn_dealevent.setId(position);
            listItemView.btn_dealevent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ReportedBean reportedBean = listItems.get(v.getId());
                    commembertab commembertab = AppContext.getUserInfo(context);
                    Intent intent = new Intent(context, Event_Process_.class);
                    intent.putExtra("event", reportedBean);
                    context.startActivity(intent);
                }
            });
            listItemView.circle_img.setTag(R.id.tag_eventlisttp, fJxx);

            if (!fJxx.equals("") && fJxx.size() > 0)
            {
                for (int i = 0; i < fJxx.size(); i++)
                {
                    if (fJxx.get(i).getFJLX().equals("1"))
                    {
                        BitmapHelper.setImageView(context, listItemView.circle_img, AppConfig.baseurl + fJxx.get(i).getLSTLJ());
                        break;
                    }
                }
            } else
            {
                Bitmap rawBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimg);
                listItemView.circle_img.setImageBitmap(rawBitmap);
            }

            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
            List<FJxx> data = new ArrayList<FJxx>();
            data = (List<FJxx>) listItemView.circle_img.getTag(R.id.tag_eventlisttp);


            if (!fJxx.equals("") && fJxx.size() > 0)
            {
//                BitmapHelper.setImageView(context, listItemView.circle_img, AppConfig.baseurl + fJxx.get(0).getLSTLJ());
                for (int i = 0; i < fJxx.size(); i++)
                {
                    if (fJxx.get(i).getFJLX().equals("1"))
                    {
                        BitmapHelper.setImageView(context, listItemView.circle_img, AppConfig.baseurl + fJxx.get(i).getLSTLJ());
                        break;
                    }
                }
            } else
            {
                Bitmap rawBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimg);
                listItemView.circle_img.setImageBitmap(rawBitmap);
            }

        }
        // 设置文字和图片

//        listItemView.tv_clqk.setText(ReportedBean.getState());
        if (ReportedBean.getState().equals("0"))
        {
            listItemView.tv_clqk.setText("未处理");
        } else if (ReportedBean.getState().equals("1"))
        {
            listItemView.tv_clqk.setText("待处理");
        } else
        {
            listItemView.tv_clqk.setText("已处理");
        }

        listItemView.tv_cmdname.setText(ReportedBean.getEventType());
        listItemView.tv_qx.setText(ReportedBean.getReporTime());
        String x = ReportedBean.getIsflashStr();
        String y = ReportedBean.getResultflashStr();
        if (ReportedBean.getIsflashStr().equals("1"))
        {
            listItemView.fl_new_item.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_new_item.setVisibility(View.GONE);
        }
        if (ReportedBean.getResultflashStr().equals("1"))
        {
            listItemView.fl_new.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_new.setVisibility(View.GONE);
        }
//
        return convertView;
    }
}
