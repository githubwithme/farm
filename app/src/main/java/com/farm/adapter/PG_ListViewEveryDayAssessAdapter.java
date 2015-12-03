package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.bean.plantgrowthtab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class PG_ListViewEveryDayAssessAdapter extends BaseAdapter
{
	ListItemView listItemView = null;
	String audiopath;
	private Context context;// 运行上下文
	private List<plantgrowthtab> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	plantgrowthtab plantgrowthtab;

	static class ListItemView
	{
		public ProgressBar pb_downloaaudio;
		public ImageButton btn_record;
		public TextView tv_cmdname;
		public TextView tv_state;
		public TextView tv_yq;
		public TextView tv_pq;
		public TextView tv_bzzl;
		public TextView tv_zfx;
		public TextView tv_time;
		public TextView tv_day;
	}

	public PG_ListViewEveryDayAssessAdapter(Context context, List<plantgrowthtab> data)
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
		plantgrowthtab = listItems.get(position);
		// 自定义视图
		if (lmap.get(position) == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.pg_listitem_everydayasssess, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.pb_downloaaudio = (ProgressBar) convertView.findViewById(R.id.pb_downloaaudio);
			listItemView.btn_record = (ImageButton) convertView.findViewById(R.id.btn_record);
			listItemView.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
			listItemView.btn_record.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					playAudio(v.getTag().toString());
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
		// String audioName =
		// plantgrowthtab.getaudioJobExecPath().substring(plantgrowthtab.getaudioJobExecPath().lastIndexOf("/"),
		// plantgrowthtab.getaudioJobExecPath().length());
		// audiopath = AppConfig.audiopath + audioName;
		// listItemView.btn_record.setTag(audiopath);
		// downloadLuYin(AppConfig.baseurl + plantgrowthtab.getaudioJobExecPath(), audiopath, listItemView.pb_downloaaudio);
		// 设置文字和图片
		if (plantgrowthtab.getplantType().equals("1"))
		{
			listItemView.tv_state.setText("优");
		} else if (plantgrowthtab.getplantType().equals("2"))
		{
			listItemView.tv_state.setText("良");
		} else if (plantgrowthtab.getplantType().equals("11"))
		{
			listItemView.tv_state.setText("及格");
		}

		return convertView;
	}

	public void downloadLuYin(String path, final String target, final ProgressBar progressBar)
	{
		HttpUtils http = new HttpUtils();
		http.download(path, target, true, true, new RequestCallBack<File>()
		{
			@Override
			public void onFailure(HttpException error, String msg)
			{
				if (msg.equals("maybe the file has downloaded completely"))
				{
					listItemView.btn_record.setVisibility(View.VISIBLE);
					listItemView.pb_downloaaudio.setVisibility(View.GONE);
				} else
				{
					Toast.makeText(context, "下载失败！找不到录音文件!", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo)
			{
				listItemView.btn_record.setVisibility(View.VISIBLE);
				listItemView.pb_downloaaudio.setVisibility(View.GONE);
			}
		});

	}

	private void playAudio(String target)
	{
		File file = new File(target);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "audio/*");
		context.startActivity(intent);
	}

}