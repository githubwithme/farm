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
import com.farm.bean.PlantGcd;
import com.farm.bean.commembertab;
import com.farm.ui.RecordList_;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class NCZ_PlantGcdListAdapter extends BaseAdapter
{
    ListItemView listItemView = null;
    String audiopath;
    private Context context;// 运行上下文
    private List<PlantGcd> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    PlantGcd PlantGcd;

    static class ListItemView
    { // 自定义控件集合
        //        public ImageView img;
//        public Button btn_plantlist;
        public TextView tv_plantname;
        public TextView tv_time;
        //        public TextView tv_type;
        public FrameLayout fl_new;
        public FrameLayout fl_new_item;
        public TextView tv_new;
        public TextView tv_area;
        public TextView tv_average_zg;
        public TextView tv_average_wj;
        public TextView tv_average_ys;
        public TextView tv_average_lys;
        public TextView tv_status;
        public ImageView iv_record;
    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param context
     */
    public NCZ_PlantGcdListAdapter(Context context, List<PlantGcd> data)
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

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        PlantGcd = listItems.get(position);
        // 自定义视图

        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.ncz_plantgcdlistadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
            listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
            listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
            listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
//            listItemView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            listItemView.tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname);
            listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            listItemView.tv_average_zg = (TextView) convertView.findViewById(R.id.tv_average_zg);
            listItemView.tv_average_wj = (TextView) convertView.findViewById(R.id.tv_average_wj);
            listItemView.tv_average_ys = (TextView) convertView.findViewById(R.id.tv_average_ys);
            listItemView.tv_average_lys = (TextView) convertView.findViewById(R.id.tv_average_lys);
            listItemView.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
//            listItemView.img = (ImageView) convertView.findViewById(R.id.img);
//            listItemView.btn_plantlist = (Button) convertView.findViewById(R.id.btn_plantlist);
//            listItemView.btn_plantlist.setId(position);
            listItemView.iv_record.setId(position);
//			commembertab commembertab = AppContext.getUserInfo(context);
//			if (commembertab.getnlevel().equals("0"))// 农场主去掉添加功能
//			{
//				listItemView.btn_plantlist.setVisibility(View.GONE);
//			}
            listItemView.iv_record.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                /*	commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.updateStatus(context, "1", jobtab1.getId(), "1", commembertab.getId());*/
                    PlantGcd plantGcd = listItems.get(v.getId());
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.updateStatus(context, "1", plantGcd.getId(), "3", commembertab.getId());
                    Intent intent = new Intent(context, RecordList_.class);
                    intent.putExtra("type", "3");
                    intent.putExtra("workid", listItems.get(v.getId()).getId());
                    context.startActivity(intent);
                }
            });
//            listItemView.btn_plantlist.setOnClickListener(new OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    Intent intent = new Intent(context, PG_PlantList_.class);
//                    intent.putExtra("gcdid", listItems.get(v.getId()).getId());
//                    intent.putExtra("gcdName", listItems.get(v.getId()).getPlantgcdName());
//                    intent.putExtra("bean", listItems.get(v.getId()));
//                    context.startActivity(intent);
//                }
//            });
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        if (PlantGcd.getImgUrl() != null && !PlantGcd.getImgUrl().get(0).equals(""))
        {
//            BitmapHelper.setImageView(context, listItemView.img, AppConfig.baseurl + PlantGcd.getImgUrl().get(0));
        }
        // 设置文字和图片
        if (Integer.valueOf(PlantGcd.getGrowthCount()) > 0)
        {
            listItemView.fl_new_item.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_new_item.setVisibility(View.GONE);
        }
        if (Integer.valueOf(PlantGcd.getGrowthvidioCount()) > 0)
        {
            listItemView.fl_new.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_new.setVisibility(View.GONE);
        }
        String status = "";
        if (!PlantGcd.getPlanttype().equals("0"))
        {
            status = status + PlantGcd.getPlanttype() + "株异常";
        } else
        {
            status = status + "正常";
        }
        if (!PlantGcd.getCdate().equals("0"))
        {
            status = status + "|" + PlantGcd.getPlanttype() + "株抽蕾";
        }
        if (!PlantGcd.getZdate().equals("0"))
        {
            status = status + "|" + PlantGcd.getPlanttype() + "株留芽";
        }
        listItemView.tv_status.setText(status);

        if (PlantGcd.getAverage_hnum().contains("."))
        {
            listItemView.tv_average_zg.setText(PlantGcd.getAverage_hnum().substring(0, PlantGcd.getAverage_hnum().lastIndexOf(".")) + "m");
        } else
        {
            listItemView.tv_average_zg.setText(PlantGcd.getAverage_hnum() + "m");
        }


        if (PlantGcd.getAverage_wnum().contains("."))
        {
            listItemView.tv_average_wj.setText(PlantGcd.getAverage_wnum().substring(0, PlantGcd.getAverage_wnum().lastIndexOf(".")) + "cm");
        } else
        {
            listItemView.tv_average_wj.setText(PlantGcd.getAverage_wnum() + "cm");
        }


        if (PlantGcd.getAverage_ynum().contains("."))
        {
            listItemView.tv_average_ys.setText(PlantGcd.getAverage_ynum().substring(0, PlantGcd.getAverage_ynum().lastIndexOf(".")) + "张");
        } else
        {
            listItemView.tv_average_ys.setText(PlantGcd.getAverage_ynum() + "张");
        }


        if (PlantGcd.getAverage_xnum().contains("."))
        {
            listItemView.tv_average_lys.setText(PlantGcd.getAverage_xnum().substring(0, PlantGcd.getAverage_xnum().lastIndexOf(".")) + "张");
        } else
        {
            listItemView.tv_average_lys.setText(PlantGcd.getAverage_xnum() + "张");
        }

        listItemView.tv_area.setText(PlantGcd.getparkName() + PlantGcd.getareaName());
        listItemView.tv_plantname.setText(PlantGcd.getPlantgcdName());
        listItemView.tv_time.setText(PlantGcd.getregDate().substring(5, PlantGcd.getregDate().lastIndexOf(":")));
        return convertView;
    }
}