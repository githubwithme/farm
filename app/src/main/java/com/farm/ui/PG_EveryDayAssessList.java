package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.PG_ListViewEveryDayAssessAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.plantgrowthtab;
import com.farm.common.ConnectionHelper;
import com.farm.common.ResultDeal;
import com.farm.common.StringUtils;
import com.farm.common.UIHelper;
import com.farm.widget.NewDataToast;
import com.farm.widget.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@EFragment
public class PG_EveryDayAssessList extends Fragment implements OnClickListener
{
	TimeSeLectorFragment timeSeLectorFragment;
	Fragment mContent = new Fragment();
	Fragment mContent_timefragment = new Fragment();
	private PG_ListViewEveryDayAssessAdapter listAdapter;
	private int listSumData;
	private List<plantgrowthtab> listData = new ArrayList<plantgrowthtab>();
	private AppContext appContext;
	private View list_footer;
	private TextView list_foot_more;
	private ProgressBar list_foot_progress;
	PopupWindow pw_tab;
	View pv_tab;
	PopupWindow pw_command;
	View pv_command;
	@ViewById
	TextView tv_title;
	@ViewById
	View line;
	@ViewById
	ImageView iv_dowm_tab;
	@ViewById
	PullToRefreshListView frame_listview_news;

	@AfterViews
	void afterOncreate()
	{
		switchContent_timefragment(mContent_timefragment, timeSeLectorFragment);
		initAnimalListView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.pg_everydayassesslist, container, false);
		appContext = (AppContext) getActivity().getApplication();
		timeSeLectorFragment = new TimeSeLectorFragment_();
/*		IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATEPLANT);
		getActivity().registerReceiver(receiver_update, intentfilter_update);*/
		return rootView;
	}

/*	BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
	{
		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context context, Intent intent)
		{
			getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
		}
	};*/

	public void switchContent_timefragment(Fragment from, Fragment to)
	{
		if (mContent_timefragment != to)
		{
			mContent_timefragment = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.time_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX)
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("PAGESIZE", String.valueOf(PAGESIZE));
		hashMap.put("PAGEINDEX", String.valueOf(PAGEINDEX));
		String params = ConnectionHelper.setParams("APP.GetplantgrowthtabList", "0", hashMap);
		new HttpUtils().send(HttpRequest.HttpMethod.POST, AppConfig.testurl, ConnectionHelper.getParas(params), new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				List<plantgrowthtab> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 200)// 连接数据库成功
				{
					listNewData = JSON.parseArray(ResultDeal.getAllRow(result), plantgrowthtab.class);
					plantgrowthtab b = listNewData.get(0);
					if (listNewData == null)
					{
						listNewData = new ArrayList<plantgrowthtab>();
					}
				} else
				{
					AppContext.makeToast(getActivity(), "error_connectDataBase");
					return;
				}

				// 数据处理
				int size = listNewData.size();

				switch (actiontype)
				{
				case UIHelper.LISTVIEW_ACTION_INIT:// 初始化
				case UIHelper.LISTVIEW_ACTION_REFRESH:// 顶部刷新
				case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:// 页面切换
					int newdata = 0;// 该变量为新加载数据数量-只有顶部刷新才会使用到
					switch (objtype)
					{
					case UIHelper.LISTVIEW_DATATYPE_NEWS:
						listSumData = size;
						if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
						{
							if (listData.size() > 0)// 页面切换时，若之前列表中已有数据，则往上面添加，并判断去除重复
							{
								for (plantgrowthtab plantgrowthtab1 : listNewData)
								{
									boolean b = false;
									for (plantgrowthtab plantgrowthtab2 : listData)
									{
										if (plantgrowthtab1.getplantId().equals(plantgrowthtab2.getplantId()))
										{
											b = true;
											break;
										}
									}
									if (!b)// 两个不相等才添加
										newdata++;
								}
							} else
							{
								newdata = size;
							}
						}
						listData.clear();// 先清除原有数据
						listData.addAll(listNewData);
						break;
					case UIHelper.LISTVIEW_DATATYPE_BLOG:
					case UIHelper.LISTVIEW_DATATYPE_COMMENT:
					}
					if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
					{
						// 提示新加载数据
						if (newdata > 0)
						{
							NewDataToast.makeText(getActivity(), getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
						} else
						{
//							NewDataToast.makeText(getActivity(), getString(R.string.new_data_toast_none), false, R.raw.newdatatoast).show();
						}
					}
					break;
				case UIHelper.LISTVIEW_ACTION_SCROLL:// 底部刷新，并且判断去除重复数据
					switch (objtype)
					{
					case UIHelper.LISTVIEW_DATATYPE_NEWS:
						listSumData += size;
						if (listNewData.size() > 0)
						{
							for (plantgrowthtab plantgrowthtab1 : listNewData)
							{
								boolean b = false;
								for (plantgrowthtab plantgrowthtab2 : listData)
								{
									if (plantgrowthtab1.getplantId().equals(plantgrowthtab2.getplantId()))
									{
										b = true;
										break;
									}
								}
								if (!b)
									listData.add(plantgrowthtab1);
							}
						} else
						{
							listData.addAll(listNewData);
						}
						break;
					case UIHelper.LISTVIEW_DATATYPE_BLOG:
						break;
					}
					break;
				}
				// 刷新列表
				if (size >= 0)
				{
					if (size < PAGESIZE)
					{
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);// 已经全部加载完毕
					} else if (size == PAGESIZE)
					{// 还有数据可以加载
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);
					}

				} else if (size == -1)
				{
					// 有异常--显示加载出错 & 弹出错误消息
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					AppContext.makeToast(getActivity(), "load_error");
				}
				if (adapter.getCount() == 0)
				{
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progressBar.setVisibility(ProgressBar.GONE);
				// main_head_progress.setVisibility(ProgressBar.GONE);
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
				{
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
					lv.setSelection(0);
				} else if (actiontype == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
				{
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				AppContext.makeToast(getActivity(), "error_connectServer");
			}
		});
	}

	private void initAnimalListView()
	{
		listAdapter = new PG_ListViewEveryDayAssessAdapter(getActivity(), listData);
		list_footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
		list_foot_more = (TextView) list_footer.findViewById(R.id.listview_foot_more);
		list_foot_progress = (ProgressBar) list_footer.findViewById(R.id.listview_foot_progress);
		frame_listview_news.addFooterView(list_footer);// 添加底部视图 必须在setAdapter前
		frame_listview_news.setAdapter(listAdapter);
		frame_listview_news.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// 点击头部、底部栏无效
				if (position == 0 || view == list_footer)
					return;

				// Animal animal = null;
				// // 判断是否是TextView
				// if (view instanceof TextView)
				// {
				// animal = (Animal) view.getTag();
				// } else
				// {
				// TextView tv = (TextView)
				// view.findViewById(R.id.news_listitem_title);
				// animal = (Animal) tv.getTag();
				// }
				// if (animal == null)
				// return;
				plantgrowthtab plantgrowthtab = listData.get(position - 1);
				if (plantgrowthtab == null)
					return;

			}
		});
		frame_listview_news.setOnScrollListener(new AbsListView.OnScrollListener()
		{
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				frame_listview_news.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (listData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try
				{
					if (view.getPositionForView(list_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e)
				{
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(frame_listview_news.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE)
				{
					frame_listview_news.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					list_foot_more.setText(R.string.load_ing);// 之前显示为"完成"加载
					list_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = listSumData / AppContext.PAGE_SIZE;// 总数里面包含几个PAGE_SIZE
					getListData(UIHelper.LISTVIEW_ACTION_SCROLL, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, pageIndex);
					// loadLvNewsData(curNewsCatalog, pageIndex, lvNewsHandler,
					// UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				frame_listview_news.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
		frame_listview_news.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
		{
			public void onRefresh()
			{
				// loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
				// UIHelper.LISTVIEW_ACTION_REFRESH);
				getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
			}
		});
		// 加载资讯数据
		if (listData.isEmpty())
		{
			getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
		}
	}

	public void showPop_addcommand()
	{
		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		pv_command = layoutInflater.inflate(R.layout.pop_addcommand, null);// 外层
		pv_command.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
				{
					pw_command.dismiss();
					return true;
				}
				return false;
			}
		});
		pv_command.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (pw_command.isShowing())
				{
					pw_command.dismiss();
				}
				return false;
			}
		});
		pw_command = new PopupWindow(pv_command, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		pw_command.showAsDropDown(line, 0, 0);
		pw_command.setOutsideTouchable(true);
		pv_command.findViewById(R.id.btn_standardprocommand).setOnClickListener(this);
		pv_command.findViewById(R.id.btn_nonstandardprocommand).setOnClickListener(this);
		pv_command.findViewById(R.id.btn_nonprocommand).setOnClickListener(this);
	}

	public class TitleAdapter extends BaseAdapter
	{
		private Context context;
		private List<String> listItems;
		private LayoutInflater listContainer;
		String type;

		class ListItemView
		{
			public TextView tv_yq;
		}

		public TitleAdapter(Context context, List<String> data)
		{
			this.context = context;
			this.listContainer = LayoutInflater.from(context);
			this.listItems = data;
		}

		HashMap<Integer, View> lmap = new HashMap<Integer, View>();

		public View getView(int position, View convertView, ViewGroup parent)
		{
			type = listItems.get(position);
			ListItemView listItemView = null;
			if (lmap.get(position) == null)
			{
				convertView = listContainer.inflate(R.layout.yq_item, null);
				listItemView = new ListItemView();
				listItemView.tv_yq = (TextView) convertView.findViewById(R.id.tv_yq);
				lmap.put(position, convertView);
				convertView.setTag(listItemView);
			} else
			{
				convertView = lmap.get(position);
				listItemView = (ListItemView) convertView.getTag();
			}
			listItemView.tv_yq.setText(type);
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
			return null;
		}

		@Override
		public long getItemId(int arg0)
		{
			return 0;
		}
	}

	@Override
	public void onClick(View v)
	{
		Intent intent;
		switch (v.getId())
		{
		case R.id.btn_standardprocommand:
			intent = new Intent(getActivity(), AddStandardCommand_.class);
			startActivity(intent);
			break;
		case R.id.btn_nonstandardprocommand:
			intent = new Intent(getActivity(), AddNotStandardCommand_.class);
			startActivity(intent);
			break;
		case R.id.btn_nonprocommand:
			intent = new Intent(getActivity(), AddNotProductCommand_.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
