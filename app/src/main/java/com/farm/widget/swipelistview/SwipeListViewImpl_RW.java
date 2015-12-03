package com.farm.widget.swipelistview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.plantgrowthtab;
import com.farm.common.utils;
import com.farm.widget.MyDialog;
import com.farm.widget.swipelistview.ExpandAniLinearLayout.OnLayoutAnimatListener;

import java.util.ArrayList;
import java.util.List;

public class SwipeListViewImpl_RW implements OnLayoutAnimatListener
{
	String dialog_content;
	MyDialog myDialog;
	View dialog_layout;
	View parent;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private ListView listView_fuwuchaxun;
	Context context;
	MyListAdapter listAdapter;
	List<plantgrowthtab> list_RW_RW;
	String from;
	ExpandAniLinearLayout swipeListAni;
	AnimateDismissAdapter animateDismissAdapter;

	public void setMyadapter(String from, Context context, ExpandAniLinearLayout swipeListAni, List<plantgrowthtab> list_RW_RW, ListView listView_fuwuchaxun)
	{
		this.from = from;
		this.listView_fuwuchaxun = listView_fuwuchaxun;
		this.context = context;
		this.swipeListAni = swipeListAni;
		this.list_RW_RW = list_RW_RW;
		swipeListAni.removeAllViews();
		int totalHeight = 0;
		int position = 0;
		View testView = newConvertView(0);
		for (int i = 0; i < list_RW_RW.size(); i++)
		{
			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			screenWidth = displayMetrics.widthPixels;
			screenHeight = displayMetrics.heightPixels;
			int height = measureHeight(i, list_RW_RW.get(i), testView);
			totalHeight = totalHeight + height;
			position = i;
			if (totalHeight > screenHeight)
			{
				break;
			}
			System.out.println("measureHeight" + height);
		}
		for (int i = 0; i <= position; i++)
		{
			View child = newConvertView(i);
			ViewHolder viewHolder = (ViewHolder) child.getTag();
			generateUi(i, list_RW_RW.get(i), viewHolder);
			swipeListAni.addView(child, R.id.rotate_y_view, R.id.message_contents, -1, -1, false);
		}
		ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
		listView_fuwuchaxun.setDivider(colorDrawable);
		swipeListAni.setDividerDrawable(colorDrawable);
		swipeListAni.setOnLayoutAnimatListener(SwipeListViewImpl_RW.this);// ???????????

		listAdapter = new MyListAdapter(context, R.layout.activity_animateremoval_row, list_RW_RW);
		animateDismissAdapter = new AnimateDismissAdapter(listAdapter, new MyOnDismissCallback());
		listView_fuwuchaxun.setAdapter(animateDismissAdapter);
		animateDismissAdapter.setAbsListView(listView_fuwuchaxun);
		swipeListAni.startExpand();
	}

	private View newConvertView(int position)
	{
		final int mposition = position;
		LayoutInflater mInflater = LayoutInflater.from(context);
		View convertView = mInflater.inflate(R.layout.message_item_layout, null);
		final ViewHolder holder = new ViewHolder();
		holder.mMessageIcon = (ImageView) convertView.findViewById(R.id.message_icon);
		holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
		holder.message_contents = (LinearLayout) convertView.findViewById(R.id.message_contents);
		holder.mMessageTitle = (TextView) convertView.findViewById(R.id.message_title);
		holder.message_day = (TextView) convertView.findViewById(R.id.message_day);
		holder.btn_SFWC = (Button) convertView.findViewById(R.id.btn_SFWC);
		holder.btn_QXRW = (Button) convertView.findViewById(R.id.btn_QXRW);
		holder.message_time = (TextView) convertView.findViewById(R.id.message_time);
		holder.mRotateYView = (RotateYView) convertView.findViewById(R.id.rotate_y_view);
		holder.mMessageTitle.setTag(position);

		holder.btn_QXRW.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
			}
		});
		holder.btn_SFWC.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
			}
		});
		holder.mMessageTitle.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			}
		});
		if (from.equals("CYR"))
		{
			holder.btn_SFWC.setClickable(false);
			holder.btn_QXRW.setClickable(false);
		}
		convertView.setTag(holder);
		return convertView;
	}

	private static final class ViewHolder
	{
		public LinearLayout message_contents;
		public TextView mMessageTitle;
		public TextView message_time;
		public ImageView mMessageIcon;
		public TextView message_day;
		public TextView tv_status;
		public RotateYView mRotateYView;
		public Button btn_SFWC;
		public Button btn_QXRW;
	}

	private void generateUi(int position, plantgrowthtab RW_RW, ViewHolder viewHolder)
	{
		int imgres;
		String status = new String();
		viewHolder.message_time.setText("截止：2015-8-10");
		viewHolder.message_day.setText("开始：2015-8-8");
		// viewHolder.message_time.setText("截止：" +
		// list_RW_RW.get(position).getWRJZSJ());
		// viewHolder.message_day.setText("开始：" +
		// list_RW_RW.get(position).getWRKSSJ());
		// status = utils.getDayDifference(utils.getToday(),
		// list_RW_RW.get(position).getWRJZSJ());
		status = utils.getDayDifference(utils.getToday(), "2015-8-10");
		if (status.toString().equals("已到期"))
		{
			imgres = R.drawable.point_round;
			// viewHolder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.gray));
		} else
		{
			imgres = R.drawable.point_round;
		}

		// if (list_RW_RW.get(position).getHYSFQX())
		// {
		// viewHolder.btn_QXRW.setClickable(false);
		// //
		// viewHolder.btn_QXRW.setBackgroundColor(context.getResources().getColor(R.color.gray));
		// viewHolder.btn_QXRW.setText("已取消");
		// }

		// if (list_RW_RW.get(position).getRWSFJS())
		// {
		// viewHolder.btn_SFWC.setClickable(false);
		// //
		// viewHolder.btn_SFWC.setBackgroundColor(context.getResources().getColor(R.color.gray));
		// viewHolder.btn_SFWC.setText("已完成");
		// }

		viewHolder.mMessageIcon.setImageResource(imgres);
		viewHolder.mMessageTitle.setId(position);
		viewHolder.mMessageTitle.setText(list_RW_RW.get(position).getplantName());
		viewHolder.tv_status.setText(status);
	}

	boolean isAnimat = false;

	@Override
	public void onAnimatEnd()
	{
		listView_fuwuchaxun.setVisibility(View.VISIBLE);
		parent = (View) swipeListAni.getParent();
		parent.setVisibility(View.GONE);
		isAnimat = false;
	}

	@Override
	public void onAnimatStart()
	{
		listView_fuwuchaxun.setVisibility(View.GONE);
		parent = (View) swipeListAni.getParent();
		parent.setVisibility(View.VISIBLE);
		isAnimat = true;
	}

	private class MyListAdapter extends ArrayAdapter<plantgrowthtab>
	{
		@Override
		public boolean isEnabled(int position)
		{
			return false;
		}

		public MyListAdapter(Context context, int resource, List<plantgrowthtab> list)
		{
			super(context, resource, list);
		}

		@Override
		public plantgrowthtab getItem(int position)
		{
			return super.getItem(position);
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			List<RotateYView> mRotatteYViews = new ArrayList<RotateYView>();
			ViewHolder holder = null;
			System.out.println("getView" + position);
			// if (convertView == null) //有异常
			// {
			convertView = newConvertView(position);
			holder = (ViewHolder) convertView.getTag();
			mRotatteYViews.add(holder.mRotateYView);
			// } else {
			// holder = (ViewHolder) convertView.getTag();
			// }
			holder.mRotateYView.setIndex(position);
			generateUi(position, getItem(position), holder);
			return convertView;
		}

	}

	private class MyOnDismissCallback implements OnDismissCallback
	{

		@Override
		public void onDismiss(AbsListView listView, int[] reverseSortedPositions)
		{
			for (int position : reverseSortedPositions)
			{
				list_RW_RW.remove(position);
			}
			listAdapter.notifyDataSetChanged();
			new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					// edit.setChecked(false);
				}
			}, 10);
		}

	}

	private int measureHeight(int positon, plantgrowthtab RW_RW, View convertView)
	{
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		int measureSpecHeight = MeasureSpec.makeMeasureSpec(screenHeight, MeasureSpec.AT_MOST);
		int measureSpecWidth = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);
		generateUi(positon, RW_RW, viewHolder);
		convertView.measure(measureSpecWidth, measureSpecHeight);
		return convertView.getMeasuredHeight();
	}
}
