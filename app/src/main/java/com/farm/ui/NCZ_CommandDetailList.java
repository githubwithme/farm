package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.ListViewCommandDetailAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.common.DictionaryHelper;
import com.farm.common.StringUtils;
import com.farm.common.UIHelper;
import com.farm.widget.NewDataToast;
import com.farm.widget.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EActivity(R.layout.commanddetaillist)
public class NCZ_CommandDetailList extends Activity implements OnClickListener
{
	SelectorFragment selectorUi;
	Fragment mContent = new Fragment();
	private ListViewCommandDetailAdapter listAdapter;
	private int listSumData;
	private List<commandtab> listData = new ArrayList<commandtab>();
	private AppContext appContext;
	private View list_footer;
	private TextView list_foot_more;
	private ProgressBar list_foot_progress;
	PopupWindow pw_command;
	View pv_command;
	@ViewById
	View line;
	@ViewById
	TextView tv_nz;
	@ViewById
	TextView tv_nz_tip;
	@ViewById
	TextView tv_yl_tip;
	@ViewById
	ImageButton btn_back;
	@ViewById
	TextView tv_cmdname;
	@ViewById
	TextView tv_yl;
	@ViewById
	TextView tv_note;
	@ViewById
	TextView tv_qx;
	@ViewById
	PullToRefreshListView frame_listview_news;
	commandtab commandtab;
	Dictionary dictionary;

	@Click
	void btn_back()
	{
		finish();
	}

	@AfterViews
	void afterOncreate()
	{
		dictionary = DictionaryHelper.getDictionaryFromAssess(NCZ_CommandDetailList.this, "PG_MQ");
		selectorUi = new SelectorFragment_();
		Bundle bundle = new Bundle();
		bundle.putSerializable("bean", dictionary);
		selectorUi.setArguments(bundle);
		switchContent(mContent, selectorUi);
		initAnimalListView();
		showData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		appContext = (AppContext) getApplication();
		commandtab = getIntent().getParcelableExtra("bean");

	}

	public void switchContent(Fragment from, Fragment to)
	{
		if (mContent != to)
		{
			mContent = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.top_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	private void showData()
	{
		tv_note.setText(commandtab.getcommNote());
		tv_yl.setText(commandtab.getamount());
		tv_qx.setText(commandtab.getcommComDate());
		if (commandtab.getstdJobType().equals("0") || commandtab.getstdJobType().equals("-1"))
		{
			tv_nz_tip.setVisibility(View.GONE);
			tv_nz.setVisibility(View.GONE);
			tv_yl_tip.setVisibility(View.GONE);
			tv_yl.setVisibility(View.GONE);
			if (commandtab.getcommNote().equals(""))
			{
				tv_cmdname.setText("暂无说明");
			} else
			{
				tv_cmdname.setText(commandtab.getcommNote());
			}

		} else
		{
			tv_cmdname.setText(commandtab.getstdJobTypeName() + "——" + commandtab.getstdJobName());
		}

		tv_nz.setText(commandtab.getnongziName());
	}

	private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX)
	{
		commembertab commembertab = AppContext.getUserInfo(NCZ_CommandDetailList.this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("comID", commandtab.getId());
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("username", commembertab.getuserName());
		params.addQueryStringParameter("page_size", String.valueOf(PAGESIZE));
		params.addQueryStringParameter("page_index", String.valueOf(PAGEINDEX));
		params.addQueryStringParameter("action", "commandGetListBycomID");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<commandtab> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listNewData = JSON.parseArray(result.getRows().toJSONString(), commandtab.class);
					} else
					{
						listNewData = new ArrayList<commandtab>();
					}
				} else
				{
					AppContext.makeToast(NCZ_CommandDetailList.this, "error_connectDataBase");
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
								for (commandtab commandtab1 : listNewData)
								{
									boolean b = false;
									for (commandtab commandtab2 : listData)
									{
										if (commandtab1.getId().equals(commandtab2.getId()))
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
							NewDataToast.makeText(NCZ_CommandDetailList.this, getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
						} else
						{
							// NewDataToast.makeText(CommandDetailList.this,
							// getString(R.string.new_data_toast_none), false,
							// R.raw.newdatatoast).show();
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
							for (commandtab commandtab1 : listNewData)
							{
								boolean b = false;
								for (commandtab commandtab2 : listData)
								{
									if (commandtab1.getId().equals(commandtab2.getId()))
									{
										b = true;
										break;
									}
								}
								if (!b)
									listData.add(commandtab1);
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
					AppContext.makeToast(NCZ_CommandDetailList.this, "load_error");
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
				String a = error.getMessage();
				AppContext.makeToast(NCZ_CommandDetailList.this, "error_connectServer");
			}
		});
	}

	private void initAnimalListView()
	{
		listAdapter = new ListViewCommandDetailAdapter(NCZ_CommandDetailList.this, listData);
		list_footer = NCZ_CommandDetailList.this.getLayoutInflater().inflate(R.layout.listview_footer, null);
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
				commandtab commandtab = listData.get(position - 1);
				if (commandtab == null)
					return;
				Intent intent = new Intent(NCZ_CommandDetailList.this, Common_CommandDetail_.class);
				intent.putExtra("bean", commandtab);// 因为list中添加了头部,因此要去掉一个
				startActivity(intent);
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

	@Override
	public void onClick(View v)
	{
		Intent intent;
		switch (v.getId())
		{

		default:
			break;
		}
	}

}
