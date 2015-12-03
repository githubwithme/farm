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
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.HaveReadRecord;
import com.farm.bean.commembertab;
import com.farm.bean.planttab;
import com.farm.common.BitmapHelper;
import com.farm.common.SqliteDb;
import com.farm.ui.AddPlantObservationRecord_;
import com.farm.ui.RecordList_;

import java.util.HashMap;
import java.util.List;

/**
 * 
 */
public class CZ_PQ_TodayPlantAdapter extends BaseAdapter
{
	ListItemView listItemView = null;
	String audiopath;
	private Context context;// 运行上下文
	private List<planttab> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	planttab planttab;

	static class ListItemView
	{ // 自定义控件集合
		public ImageView img;
		public Button iv_addplant;
		public TextView tv_plantname;
		public TextView tv_time;
		public TextView tv_type;
		public FrameLayout fl_new;
		public TextView tv_new;
		public ImageView iv_record;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param context
	 */
	public CZ_PQ_TodayPlantAdapter(Context context, List<planttab> data)
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
		planttab = listItems.get(position);
		// 自定义视图

		if (lmap.get(position) == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.cz_pq_todayplantadapter, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
			listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
			listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
			listItemView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			listItemView.tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname);
			listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			listItemView.img = (ImageView) convertView.findViewById(R.id.img);
			listItemView.iv_addplant = (Button) convertView.findViewById(R.id.iv_addplant);
			listItemView.iv_addplant.setId(position);
			listItemView.iv_record.setId(position);
			commembertab commembertab = AppContext.getUserInfo(context);
			if (commembertab.getnlevel().equals("0"))// 农场主去掉添加功能
			{
				listItemView.iv_addplant.setVisibility(View.GONE);
			}
			listItemView.iv_record.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					planttab plant = listItems.get(v.getId());
					HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(context, plant.getId());
					if (haveReadRecord != null)
					{
						SqliteDb.updateHaveReadRecord(context, plant.getId(), plant.getPlantvidioCount());
						FrameLayout fl_new = ((ListItemView) (lmap.get(v.getId()).getTag())).fl_new;
						fl_new.setVisibility(View.GONE);
					} else
					{
						SqliteDb.saveHaveReadRecord(context, plant.getId(), plant.getPlantvidioCount());
					}
					Intent intent = new Intent(context, RecordList_.class);
					intent.putExtra("type", "3");
					intent.putExtra("workid", listItems.get(v.getId()).getId());
					context.startActivity(intent);
				}
			});
			listItemView.iv_addplant.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(context, AddPlantObservationRecord_.class);
					intent.putExtra("bean", listItems.get(v.getId()));
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
		if (planttab.getImgUrl() != null && !planttab.getImgUrl().get(0).equals(""))
		{
			BitmapHelper.setImageView(context, listItemView.img, AppConfig.baseurl + planttab.getImgUrl().get(0));
		}
		// 设置文字和图片
		HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(context, planttab.getId());
		if (haveReadRecord != null)
		{
			String num = haveReadRecord.getNum();
			if (num != null && !num.equals("") && (Integer.valueOf(num) < Integer.valueOf(planttab.getPlantvidioCount())))
			{
				int num_new = Integer.valueOf(planttab.getPlantvidioCount()) - Integer.valueOf(num);
				listItemView.fl_new.setVisibility(View.VISIBLE);
				listItemView.tv_new.setText(String.valueOf(num_new));
			}
		} else
		{
			SqliteDb.saveHaveReadRecord(context, planttab.getId(), planttab.getPlantvidioCount());
		}
		String a = planttab.getIsobser();
		if (planttab.getIsobser().equals("0"))
		{
			listItemView.iv_addplant.setBackgroundColor(context.getResources().getColor(R.color.red));
			listItemView.iv_addplant.setText("未观测");
		} else
		{
		}
		if (planttab.getplantType().equals("1"))
		{
			listItemView.tv_type.setText("异常");
		}
		listItemView.tv_plantname.setText(planttab.getplantName());
		listItemView.tv_time.setText("最近：" + planttab.getGrowthDate().substring(5, planttab.getregDate().lastIndexOf(":")));
		return convertView;
	}
}