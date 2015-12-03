package com.farm.adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.planttab;

import java.util.HashMap;
import java.util.List;

public class ListViewDaoGangAdapter extends BaseAdapter
{
	String number;
	private Context context;// 运行上下文
	private List<planttab> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	planttab planttab;

	static class ListItemView
	{
		public TextView tv_cmdname;
		public Button btn_call;
		public TextView tv_state;
		public TextView tv_yq;
		public TextView tv_pq;
		public TextView tv_bzzl;
		public TextView tv_zfx;
		public TextView tv_starttime;
		public TextView tv_time;
		public TextView tv_day;
	}

	public ListViewDaoGangAdapter(Context context, List<planttab> data)
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
		planttab = listItems.get(position);
		// 自定义视图
		ListItemView listItemView = null;
		if (lmap.get(position) == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.listitem_daogang, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.btn_call = (Button) convertView.findViewById(R.id.btn_call);
			listItemView.tv_starttime = (TextView) convertView.findViewById(R.id.tv_starttime);
			listItemView.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
			listItemView.btn_call.setId(position);
			listItemView.btn_call.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					TextView tv_phone = ((ListItemView) (lmap.get(v.getId()).getTag())).tv_starttime;
					number = tv_phone.getText().toString();
					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle("是否拨打此电话？");
					builder.setIcon(R.drawable.logo);
					builder.setPositiveButton("是", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface arg0, int arg1)
						{
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
							context.startActivity(intent);
						}
					});
					builder.setNegativeButton("否", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface arg0, int arg1)
						{
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
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
		if (planttab.getplantType().equals("1"))
		{
			listItemView.tv_state.setText("到岗");
		} else if (planttab.getplantType().equals("2"))
		{
			listItemView.tv_state.setText("离岗");
		} else if (planttab.getplantType().equals("11"))
		{
			listItemView.tv_state.setText("请假");
		}

		return convertView;
	}
}