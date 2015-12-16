package com.farm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;

public class AddStd_cmd_stepfour_GridViewAdapter extends BaseAdapter
{

	String[] sn;
	private String type;
	private Context context;
	Holder view;

	public AddStd_cmd_stepfour_GridViewAdapter(Context context,String[] sn)
	{
		this.sn=sn;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		if (sn != null && sn.length > 0)
			return sn.length;
		else
			return 0;
	}

	@Override
	public Object getItem(int position)
	{
		return sn[position];
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
			convertView = View.inflate(context, R.layout.addstd_cmd_stepfour_item, null);
			view = new Holder(convertView);
			convertView.setTag(view);
		} else
		{
			view = (Holder) convertView.getTag();
		}
		if (sn != null &&sn.length  > 0)
		{
			type = sn[position];
			view.name.setText(type);
		}

		return convertView;
	}

	private class Holder
	{
		private TextView name;

		public Holder(View view)
		{
			name = (TextView) view.findViewById(R.id.typename);
		}
	}

}
