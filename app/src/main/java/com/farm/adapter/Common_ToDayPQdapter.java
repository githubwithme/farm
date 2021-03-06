package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.HaveReadNumber;
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.common.SqliteDb;
import com.farm.ui.CZ_PQ_TodayJob_;
import com.farm.ui.CZ_PQ_TodayPlant_;
import com.farm.ui.Common_PQYQCommandList_;
import com.farm.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

public class Common_ToDayPQdapter extends BaseAdapter
{
	String audiopath;
	private Context context;// 运行上下文
	private List<areatab> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	areatab areatab;
	ListItemView listItemView = null;
	commembertab commembertab;

	static class ListItemView
	{
		public TextView tv_plantnumber_new;
		public TextView tv_worknumber_new;
		public TextView tv_cmdnumber_new;
		public TextView tv_pq;
		public TextView tv_worknumber;
		public TextView tv_cmdnumber;
		public TextView tv_plantnumber;
		public TextView tv_username;
		public LinearLayout ll_pgwork;
		public LinearLayout ll_plant;
		public LinearLayout ll_cmd;
		public CircleImageView iv_userimg;
		public FrameLayout fl_worknumber_new;
		public FrameLayout fl_cmdnumber_new;
		public FrameLayout fl_plantnumber_new;
	}

	public Common_ToDayPQdapter(Context context, List<areatab> data)
	{
		this.context = context;
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = data;
		commembertab = AppContext.getUserInfo(context);
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
		areatab = listItems.get(position);
		// 自定义视图

		if (lmap.get(position) == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.cz_workitem, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.fl_cmdnumber_new = (FrameLayout) convertView.findViewById(R.id.fl_cmdnumber_new);
			listItemView.fl_worknumber_new = (FrameLayout) convertView.findViewById(R.id.fl_worknumber_new);
			listItemView.fl_plantnumber_new = (FrameLayout) convertView.findViewById(R.id.fl_plantnumber_new);
			listItemView.ll_pgwork = (LinearLayout) convertView.findViewById(R.id.ll_pgwork);
			listItemView.ll_plant = (LinearLayout) convertView.findViewById(R.id.ll_plant);
			listItemView.ll_cmd = (LinearLayout) convertView.findViewById(R.id.ll_cmd);
			listItemView.tv_cmdnumber_new = (TextView) convertView.findViewById(R.id.tv_cmdnumber_new);
			listItemView.tv_plantnumber_new = (TextView) convertView.findViewById(R.id.tv_plantnumber_new);
			listItemView.tv_worknumber_new = (TextView) convertView.findViewById(R.id.tv_worknumber_new);
			listItemView.tv_pq = (TextView) convertView.findViewById(R.id.tv_pq);
			listItemView.tv_plantnumber = (TextView) convertView.findViewById(R.id.tv_plantnumber);
			listItemView.tv_worknumber = (TextView) convertView.findViewById(R.id.tv_worknumber);
			listItemView.tv_cmdnumber = (TextView) convertView.findViewById(R.id.tv_cmdnumber);
			listItemView.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
			listItemView.iv_userimg = (CircleImageView) convertView.findViewById(R.id.iv_userimg);
			// 设置控件集到convertView
			lmap.put(position, convertView);
			convertView.setTag(listItemView);
			listItemView.ll_plant.setId(position);
			listItemView.ll_pgwork.setId(position);
			listItemView.ll_cmd.setId(position);
			// if (commembertab.getnlevel().equals("0"))
			// {
			// listItemView.ll_cmd.setVisibility(View.GONE);
			// }
			listItemView.ll_cmd.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					FrameLayout fl_new = ((ListItemView) (lmap.get(v.getId()).getTag())).fl_cmdnumber_new;
					fl_new.setVisibility(View.GONE);
					areatab area = listItems.get(v.getId());
					int allCount_plant = Integer.valueOf(area.getPlantGrowCount()) + Integer.valueOf(area.getPlantGrowVideoCount());
					int allCount_job = Integer.valueOf(area.getJobCount()) + Integer.valueOf(area.getJobVideoCount());
					int allCount_cmd = Integer.valueOf(area.getCommandCount()) + Integer.valueOf(area.getCommandVideoCount());
					HaveReadNumber haveReadNumber = getHaveReadData(commembertab.getId() + area.getWorkuserid());
					if (haveReadNumber != null)
					{
						updateHaveReadData(area.getWorkuserid(), "pq_cmdnum", String.valueOf(allCount_cmd));
					} else
					{
						saveHaveReadData(area.getWorkuserid(), String.valueOf(allCount_plant), String.valueOf(allCount_job), String.valueOf(allCount_cmd));
					}
					Intent intent = new Intent(context, Common_PQYQCommandList_.class);
					intent.putExtra("workuserid", listItems.get(v.getId()).getWorkuserid());// 因为list中添加了头部,因此要去掉一个
					context.startActivity(intent);
				}
			});
			listItemView.ll_plant.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					FrameLayout fl_new = ((ListItemView) (lmap.get(v.getId()).getTag())).fl_plantnumber_new;
					fl_new.setVisibility(View.GONE);
					areatab area = listItems.get(v.getId());
					int allCount_plant = Integer.valueOf(area.getPlantGrowCount()) + Integer.valueOf(area.getPlantGrowVideoCount());
					int allCount_job = Integer.valueOf(area.getJobCount()) + Integer.valueOf(area.getJobVideoCount());
					int allCount_cmd = Integer.valueOf(area.getCommandCount()) + Integer.valueOf(area.getCommandVideoCount());
					HaveReadNumber haveReadNumber = getHaveReadData(commembertab.getId() + area.getWorkuserid());
					if (haveReadNumber != null)
					{
						updateHaveReadData(area.getWorkuserid(), "pq_plantnum", String.valueOf(allCount_plant));
					} else
					{
						saveHaveReadData(area.getWorkuserid(), String.valueOf(allCount_plant), String.valueOf(allCount_job), String.valueOf(allCount_cmd));
					}
					Intent intent = new Intent(context, CZ_PQ_TodayPlant_.class);
					intent.putExtra("bean", listItems.get(v.getId()));// 因为list中添加了头部,因此要去掉一个
					context.startActivity(intent);
				}
			});
			listItemView.ll_pgwork.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					FrameLayout fl_new = ((ListItemView) (lmap.get(v.getId()).getTag())).fl_worknumber_new;
					fl_new.setVisibility(View.GONE);
					areatab area = listItems.get(v.getId());
					int allCount_plant = Integer.valueOf(area.getPlantGrowCount()) + Integer.valueOf(area.getPlantGrowVideoCount());
					int allCount_job = Integer.valueOf(area.getJobCount()) + Integer.valueOf(area.getJobVideoCount());
					int allCount_cmd = Integer.valueOf(area.getCommandCount()) + Integer.valueOf(area.getCommandVideoCount());
					HaveReadNumber haveReadNumber = getHaveReadData(commembertab.getId() + area.getWorkuserid());
					if (haveReadNumber != null)
					{
						updateHaveReadData(area.getWorkuserid(), "pq_jobnum", String.valueOf(allCount_job));
					} else
					{
						saveHaveReadData(area.getWorkuserid(), String.valueOf(allCount_plant), String.valueOf(allCount_job), String.valueOf(allCount_cmd));
					}
					Intent intent = new Intent(context, CZ_PQ_TodayJob_.class);
					intent.putExtra("bean", listItems.get(v.getId()));// 因为list中添加了头部,因此要去掉一个
					context.startActivity(intent);
				}
			});
		} else
		{
			convertView = lmap.get(position);
			listItemView = (ListItemView) convertView.getTag();
		}
		// 设置文字和图片
		int allCount_plant = Integer.valueOf(areatab.getPlantGrowCount()) + Integer.valueOf(areatab.getPlantGrowVideoCount());
		int allCount_job = Integer.valueOf(areatab.getJobCount()) + Integer.valueOf(areatab.getJobVideoCount());
		int allCount_cmd = Integer.valueOf(areatab.getCommandCount()) + Integer.valueOf(areatab.getCommandVideoCount());
		HaveReadNumber haveReadNumber = getHaveReadData(commembertab.getId() + areatab.getWorkuserid());
		if (haveReadNumber != null)
		{
			String num_job = haveReadNumber.getPq_jobnum();
			String num_plant = haveReadNumber.getPq_plantnum();
			String num_cmd = haveReadNumber.getPq_cmdnum();
			if (num_plant != null && !num_plant.equals("") && (Integer.valueOf(num_plant) < allCount_plant))
			{
				int num = allCount_plant - Integer.valueOf(num_plant);
				listItemView.fl_plantnumber_new.setVisibility(View.VISIBLE);
				listItemView.tv_plantnumber_new.setText(String.valueOf(num));
			}
			if (num_job != null && !num_job.equals("") && (Integer.valueOf(num_job) < allCount_job))
			{
				int num = allCount_job - Integer.valueOf(num_job);
				listItemView.fl_worknumber_new.setVisibility(View.VISIBLE);
				listItemView.tv_worknumber_new.setText(String.valueOf(num));
			}
			if (num_cmd != null && !num_cmd.equals("") && (Integer.valueOf(num_cmd) < allCount_cmd))
			{
				int num = allCount_cmd - Integer.valueOf(num_cmd);
				listItemView.fl_cmdnumber_new.setVisibility(View.VISIBLE);
				listItemView.tv_cmdnumber_new.setText(String.valueOf(num));
			}
		} else
		{
			saveHaveReadData(areatab.getWorkuserid(), String.valueOf(allCount_plant), String.valueOf(allCount_job), String.valueOf(allCount_cmd));
		}
		listItemView.tv_pq.setText(areatab.getareaName());
		listItemView.tv_worknumber.setText(areatab.getJobCount());
		listItemView.tv_plantnumber.setText(areatab.getPlantGrowCount());
		listItemView.tv_cmdnumber.setText(areatab.getCommandCount());
		if (areatab.getRealName().equals(""))
		{
			listItemView.tv_username.setText("暂无");
		} else
		{
			listItemView.tv_username.setText(areatab.getRealName());
		}

		BitmapHelper.setImageViewBackground(context, listItemView.iv_userimg, AppConfig.baseurl + areatab.getImgurl());
		return convertView;
	}

	private void saveHaveReadData(String id, String plantnum, String jobnum, String cmdnum)
	{
		HaveReadNumber haveReadNumber = new HaveReadNumber();
		haveReadNumber.setId(commembertab.getId() + id);
		haveReadNumber.setPlantnum("");
		haveReadNumber.setAsknum("");
		haveReadNumber.setJobnum("");
		haveReadNumber.setPq_jobnum(jobnum);
		haveReadNumber.setPq_plantnum(plantnum);
		haveReadNumber.setPq_cmdnum(cmdnum);
		SqliteDb.save(context, haveReadNumber);
	}

	private HaveReadNumber getHaveReadData(String id)
	{
		HaveReadNumber haveReadNumber = (HaveReadNumber) SqliteDb.getHaveReadData(context, HaveReadNumber.class, id);
		return haveReadNumber;
	}

	private void updateHaveReadData(String id, String columnname, String columvalues)
	{
		HaveReadNumber haveReadNumber = new HaveReadNumber();
		haveReadNumber.setId(commembertab.getId() + id);
		if (columnname.equals("plantnum"))
		{
			haveReadNumber.setPlantnum(columvalues);
		} else if (columnname.equals("jobnum"))
		{
			haveReadNumber.setJobnum(columvalues);
		} else if (columnname.equals("asknum"))
		{
			haveReadNumber.setAsknum(columvalues);
		} else if (columnname.equals("pq_plantnum"))
		{
			haveReadNumber.setPq_plantnum(columvalues);
		} else if (columnname.equals("pq_jobnum"))
		{
			haveReadNumber.setPq_jobnum(columvalues);
		} else if (columnname.equals("pq_cmdnum"))
		{
			haveReadNumber.setPq_cmdnum(columvalues);
		}
		SqliteDb.updateHaveReadData(context, haveReadNumber, id, columnname);
	}

}