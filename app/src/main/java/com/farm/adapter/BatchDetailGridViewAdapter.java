package com.farm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.Type;
import com.farm.bean.planttab;

import java.util.List;

public class BatchDetailGridViewAdapter extends BaseAdapter
{

	private List<planttab> list;
	private Type type;
	private Context context;
	Holder view;

	public BatchDetailGridViewAdapter(Context context, List<planttab> list)
	{
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		if (list != null && list.size() > 0)
			return list.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = View.inflate(context, R.layout.item_batchdetailgridview, null);
			view = new Holder(convertView);
			convertView.setTag(view);
		} else
		{
			view = (Holder) convertView.getTag();
		}
		if (list != null && list.size() > 0)
		{
			// view.name.setText(type.getTypename());
		}

		return convertView;
	}

	private class Holder
	{
		private ImageView icon;
		private TextView name;

		public Holder(View view)
		{
			// icon = (ImageView) view.findViewById(R.id.typeicon);
			// name = (TextView) view.findViewById(R.id.typename);
		}
	}

}
