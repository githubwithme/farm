package com.farm.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.plantgrowthtab;
import com.farm.bean.planttab;
import com.farm.common.BitmapHelper;
import com.farm.common.utils;
import com.farm.widget.CircleImageView;
import com.farm.widget.swipelistview.ExpandAniLinearLayout;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-23 下午3:37:11
 * @description :
 */
@EFragment
public class TreeFragment extends Fragment
{
	planttab planttab;
	int[] resleft = new int[] { R.drawable.centerleft, R.drawable.centerleft1, R.drawable.centerleft2 };
	int[] resright = new int[] { R.drawable.centerright, R.drawable.centerright1, R.drawable.centerright2 };
	String[] list_img;
	List<plantgrowthtab> listNewData = new ArrayList<plantgrowthtab>();
	@ViewById
	ExpandAniLinearLayout swipe_list_ani;
	@ViewById
	ListView lv_tree;
	@ViewById
	TextView tv_tip;
	TreeAdapter treeAdapter;

	@AfterViews
	void afterOncreate()
	{
		lv_tree.setAdapter(new TreeAdapter(getActivity(), listNewData));
		lv_tree.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
			{
				// Intent intent = new Intent(getActivity(),
				// AddPlantObservationRecord_.class);
				// getActivity().startActivity(intent);
			}
		});
		if (listNewData.isEmpty())
		{
			getTask(20, 0);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.treefragment, container, false);
		planttab = getActivity().getIntent().getParcelableExtra("bean");
		return rootView;
	}

	private void getTask(final int PAGESIZE, int PAGEINDEX)
	{
		commembertab commembertab = AppContext.getUserInfo(getActivity());
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("username", commembertab.getuserName());
		params.addQueryStringParameter("orderby", "regDate desc");
		params.addQueryStringParameter("PlantID", planttab.getId());
		params.addQueryStringParameter("page_size", String.valueOf(PAGESIZE));
		params.addQueryStringParameter("page_index", String.valueOf(PAGEINDEX));
		params.addQueryStringParameter("action", "plantGrowthTabGetListByPlantID");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listNewData = JSON.parseArray(result.getRows().toJSONString(), plantgrowthtab.class);
						treeAdapter = new TreeAdapter(getActivity(), listNewData);
						lv_tree.setAdapter(treeAdapter);
//						utils.setListViewHeightBasedOnChildren(lv_tree);
					} else
					{
						listNewData = new ArrayList<plantgrowthtab>();
						tv_tip.setVisibility(View.VISIBLE);
					}
				} else
				{
					AppContext.makeToast(getActivity(), "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{

			}

		});
	}

	public class TreeAdapter extends BaseAdapter
	{
		private Context context;
		private List<plantgrowthtab> listItems;
		private LayoutInflater listContainer;
		plantgrowthtab plantgrowthtab;

		class ListItemView
		{
			public LinearLayout ll_right;
			public LinearLayout ll_left;
			public LinearLayout ll_tip_right;
			public LinearLayout ll_tip_left;
			public ImageView ic_center;
			public ImageView iv_top;
			public ImageView iv_bottom;
			public CircleImageView iv_img_right;
			public CircleImageView iv_img_left;
			public TextView tv_cjtime_right;
			public TextView tv_cjtime_left;
			public TextView tv_sg_right;
			public TextView tv_wj_right;
			public TextView tv_ys_right;
			public TextView tv_sg_left;
			public TextView tv_ys_left;
			public TextView tv_wj_left;

			public TextView tv_sfcl_left;
			public TextView tv_sfly_left;
			public TextView tv_sfcl_right;
			public TextView tv_sfly_right;
		}

		public TreeAdapter(Context context, List<plantgrowthtab> data)
		{
			this.context = context;
			this.listContainer = LayoutInflater.from(context);
			this.listItems = data;
		}

		HashMap<Integer, View> lmap = new HashMap<Integer, View>();

		public View getView(int position, View convertView, ViewGroup parent)
		{
			plantgrowthtab = listItems.get(position);
			ListItemView listItemView = null;
			if (lmap.get(position) == null)
			{
				convertView = listContainer.inflate(R.layout.growthtree_itme, null);
				listItemView = new ListItemView();
				listItemView.ll_right = (LinearLayout) convertView.findViewById(R.id.ll_right);
				listItemView.ll_left = (LinearLayout) convertView.findViewById(R.id.ll_left);
				listItemView.tv_cjtime_left = (TextView) convertView.findViewById(R.id.tv_cjtime_left);
				listItemView.tv_cjtime_right = (TextView) convertView.findViewById(R.id.tv_cjtime_right);
				listItemView.tv_sg_left = (TextView) convertView.findViewById(R.id.tv_sg_left);
				listItemView.tv_ys_left = (TextView) convertView.findViewById(R.id.tv_ys_left);
				listItemView.tv_wj_left = (TextView) convertView.findViewById(R.id.tv_wj_left);
				listItemView.tv_sg_right = (TextView) convertView.findViewById(R.id.tv_sg_right);
				listItemView.tv_ys_right = (TextView) convertView.findViewById(R.id.tv_ys_right);
				listItemView.tv_wj_right = (TextView) convertView.findViewById(R.id.tv_wj_right);

				listItemView.tv_sfcl_left = (TextView) convertView.findViewById(R.id.tv_sfcl_left);
				listItemView.tv_sfly_left = (TextView) convertView.findViewById(R.id.tv_sfly_left);
				listItemView.tv_sfcl_right = (TextView) convertView.findViewById(R.id.tv_sfcl_right);
				listItemView.tv_sfly_right = (TextView) convertView.findViewById(R.id.tv_sfly_right);

				listItemView.ic_center = (ImageView) convertView.findViewById(R.id.ic_center);
				listItemView.iv_bottom = (ImageView) convertView.findViewById(R.id.iv_bottom);
				listItemView.iv_img_left = (CircleImageView) convertView.findViewById(R.id.iv_img_left);
				listItemView.iv_img_right = (CircleImageView) convertView.findViewById(R.id.iv_img_right);
				listItemView.ll_tip_left = (LinearLayout) convertView.findViewById(R.id.ll_tip_left);
				listItemView.ll_tip_right = (LinearLayout) convertView.findViewById(R.id.ll_tip_right);
				listItemView.iv_top = (ImageView) convertView.findViewById(R.id.iv_top);
				listItemView.ll_left.setId(position);
				listItemView.ll_right.setId(position);
				listItemView.ll_left.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						int pos = v.getId();
						Intent intent = new Intent(getActivity(), ObservationRecordActivity_.class);
						intent.putExtra("gcid", listItems.get(pos).getId());
						getActivity().startActivity(intent);
					}
				});
				listItemView.ll_right.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						int pos = v.getId();
						Intent intent = new Intent(getActivity(), ObservationRecordActivity_.class);
						intent.putExtra("gcid", listItems.get(pos).getId());
						getActivity().startActivity(intent);
					}
				});
				lmap.put(position, convertView);
				convertView.setTag(listItemView);
			} else
			{
				convertView = lmap.get(position);
				listItemView = (ListItemView) convertView.getTag();
			}
			if (position == 0)
			{
				listItemView.iv_top.setVisibility(View.VISIBLE);
			}
			if (position == listItems.size() - 1)
			{
				listItemView.iv_bottom.setVisibility(View.VISIBLE);
			}

			if (position % 2 == 0)
			{
				listItemView.ic_center.setBackground(getResources().getDrawable(resright[(int) (Math.random() * resright.length)]));
				listItemView.ll_right.setVisibility(View.VISIBLE);
				listItemView.tv_cjtime_right.setText(plantgrowthtab.getregDate().substring(0, plantgrowthtab.getregDate().lastIndexOf(" ")));
				listItemView.tv_sg_right.setVisibility(View.VISIBLE);
				listItemView.tv_sg_right.setText(plantgrowthtab.gethNum() + "m");
				listItemView.tv_wj_right.setText(plantgrowthtab.getwNum() + "m");
				listItemView.tv_ys_right.setText(plantgrowthtab.getyNum() + "片");
				if (plantgrowthtab.getImgUrl().size() != 0)
				{
					BitmapHelper.setImageViewBackground(context, listItemView.iv_img_right, AppConfig.baseurl + plantgrowthtab.getImgUrl().get(0));
				}
//                if (plantgrowthtab.getSfcl().equals("True"))
//                {
//                    listItemView.ll_tip_right.setVisibility(View.VISIBLE);
//                    listItemView.tv_sfcl_right.setVisibility(View.VISIBLE);
//                }
//                if (plantgrowthtab.getSfly().equals("True"))
//                {
//                    listItemView.ll_tip_right.setVisibility(View.VISIBLE);
//                    listItemView.tv_sfly_right.setVisibility(View.VISIBLE);
//                }

			} else
			{
				listItemView.ic_center.setBackground(getResources().getDrawable(resleft[(int) (Math.random() * resleft.length)]));
				listItemView.ll_left.setVisibility(View.VISIBLE);
				listItemView.tv_cjtime_left.setText(plantgrowthtab.getregDate().substring(0, plantgrowthtab.getregDate().lastIndexOf(" ")));
				listItemView.tv_sg_left.setText(plantgrowthtab.gethNum() + "m");
				listItemView.tv_wj_left.setText(plantgrowthtab.getwNum() + "m");
				listItemView.tv_ys_left.setText(plantgrowthtab.getyNum() + "片");

				if (plantgrowthtab.getImgUrl().size() != 0)
				{
					BitmapHelper.setImageViewBackground(context, listItemView.iv_img_left, AppConfig.baseurl + plantgrowthtab.getImgUrl().get(0));
				}
//                if (plantgrowthtab.getSfcl().equals("True"))
//                {
//                    listItemView.ll_tip_left.setVisibility(View.VISIBLE);
//                    listItemView.tv_sfcl_left.setVisibility(View.VISIBLE);
//                }
//                if (plantgrowthtab.getSfly().equals("True"))
//                {
//                    listItemView.ll_tip_left.setVisibility(View.VISIBLE);
//                    listItemView.tv_sfly_left.setVisibility(View.VISIBLE);
//                }
			}
			return convertView;
		}

		@Override
		public int getCount()
		{
			return listItems.size();
		}

		@Override
		public Object getItem(int arg0)
		{
			return listItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0)
		{
			return 0;
		}
	}

	public void setListViewHeightBasedOnChildren(ListView listView)
	{
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
		{
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++)
		{
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
}
