package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.customview.AudioRecorder;
import com.customview.RecordButton;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.HaveReadRecord;
import com.farm.bean.RecordBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.common.MediaHelper;
import com.farm.common.SqliteDb;
import com.farm.common.UIHelper;
import com.farm.widget.CircleImageView;
import com.farm.widget.NewDataToast;
import com.farm.widget.PullToRefreshListView;
import com.farm.widget.swipelistview.ExpandAniLinearLayout;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-23 下午3:37:11
 * @description :
 */
@SuppressLint("NewApi")
@EActivity(R.layout.recordlist)
public class RecordList extends Activity
{
	private static View popupWindowView;
	private static PopupWindow popupWindow;
	LinearLayout ll_newtip;
	TextView tv_newtip;
	int oldsieze = 0;
	int newsize = 0;
	TimeThread timethread;
	private int listSumData;
	private List<RecordBean> listData = new ArrayList<RecordBean>();
	private AppContext appContext;
	private View list_footer;
	private TextView list_foot_more;
	private ProgressBar list_foot_progress;
	RecordBean RecordBean;
	int[] resleft = new int[] { R.drawable.centerleft, R.drawable.centerleft1, R.drawable.centerleft2 };
	int[] resright = new int[] { R.drawable.centerright, R.drawable.centerright1, R.drawable.centerright2 };
	String[] list_img;
	// List<RecordBean> listNewData = new ArrayList<RecordBean>();
	@ViewById
	ExpandAniLinearLayout swipe_list_ani;
	@ViewById
	RecordButton btn_record;
	@ViewById
	PullToRefreshListView frame_listview_news;
	@ViewById
	RelativeLayout rl_titlebar;
	@ViewById
	EditText et_content;
	// @ViewById
	// LinearLayout ll_newtip;
	@ViewById
	Button btn_sent;
	// @ViewById
	// TextView tv_newtip;
	@ViewById
	TextView tv_title;
	TreeAdapter treeAdapter;
	String type;
	String workid;
	String audiopath;
	commembertab commembertab;

	@Click
	void btn_sent()
	{
		if (!et_content.getText().toString().equals(""))
		{
			jobTabAdd();
		} else
		{
			Toast.makeText(RecordList.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		timethread.setStop(true);
		timethread.interrupt();
		timethread = null;
	}

	@Click
	void btn_back()
	{
		finish();
	}

	@AfterViews
	void afterOncreate()
	{
		initAnimalListView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		commembertab = AppContext.getUserInfo(this);
		type = getIntent().getStringExtra("type");
		workid = getIntent().getStringExtra("workid");
		IntentFilter refreshIntentFilter = new IntentFilter(AppContext.BROADCAST_REFRESHRECORD);
		registerReceiver(refreshReceiver, refreshIntentFilter);
		timethread = new TimeThread();
		timethread.setStop(false);
		timethread.setSleep(false);
		timethread.start();
	}

	BroadcastReceiver refreshReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, treeAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
		}
	};

	private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX)
	{
		commembertab commembertab = AppContext.getUserInfo(RecordList.this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("username", commembertab.getuserName());
		params.addQueryStringParameter("type", type);
		params.addQueryStringParameter("workid", workid);
		params.addQueryStringParameter("page_size", String.valueOf(PAGESIZE));
		params.addQueryStringParameter("page_index", String.valueOf(PAGEINDEX));
		params.addQueryStringParameter("action", "getVidioList");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				List<RecordBean> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)//
				{
					if (result.getAffectedRows() != 0)
					{
						listNewData = JSON.parseArray(result.getRows().toJSONString(), RecordBean.class);
						if (listNewData != null)
						{
							List<RecordBean> list = new ArrayList<RecordBean>();
							for (int i = 0; i < listNewData.size(); i++)
							{
								list.add(listNewData.get(listNewData.size() - i - 1));
							}
							listNewData.clear();
							listNewData = list;
							newsize = listNewData.size();
							// HaveReadRecord haveReadRecord =
							// SqliteDb.getHaveReadRecord(RecordList.this,
							// workid);
							// if (haveReadRecord != null)
							// {
							// SqliteDb.updateHaveReadRecord(RecordList.this,
							// workid, String.valueOf(newsize));
							// } else
							// {
							// SqliteDb.saveHaveReadRecord(RecordList.this,
							// workid, String.valueOf(newsize));
							// }
							if (newsize > oldsieze && lv.getLastVisiblePosition() != (lv.getCount() - 1))
							{
								lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
								if (popupWindow != null && popupWindow.isShowing())
								{
									tv_newtip.setText("有" + (newsize - oldsieze) + "条更新");
								} else
								{
									showNewTip();
								}

							} else
							{
								lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
								newsize = oldsieze;
								treeAdapter.notifyDataSetChanged();
								setListViewHeightBasedOnChildren(frame_listview_news);// 87
							}

							// frame_listview_news
							// .setSelection(frame_listview_news .getCount()-1);
						} else
						{
							listNewData = new ArrayList<RecordBean>();
						}
					} else
					{
						listNewData = new ArrayList<RecordBean>();
					}
				} else
				{
					AppContext.makeToast(RecordList.this, "error_connectDataBase");
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
								for (RecordBean RecordBean1 : listNewData)
								{
									boolean b = false;
									for (RecordBean RecordBean2 : listData)
									{
										if (RecordBean1.getId().equals(RecordBean2.getId()))
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
							NewDataToast.makeText(RecordList.this, getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
						} else
						{
							// NewDataToast.makeText(RecordList.this,
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
						// if (listNewData.size() > 0)
						// {
						// for (RecordBean RecordBean1 : listNewData)
						// {
						// boolean b = false;
						// for (RecordBean RecordBean2 : listData)
						// {
						// if (RecordBean1.getId().equals(RecordBean2.getId()))
						// {
						// b = true;
						// break;
						// }
						// }
						// if (!b)
						// listData.add(RecordBean1);
						// }
						// } else
						// {
						// listData.addAll(listNewData);
						// }
						List<RecordBean> list = new ArrayList<RecordBean>();
						for (int i = 0; i < listData.size(); i++)
						{
							listNewData.add(listData.get(i));
						}
						listData.clear();
						for (int i = 0; i < listNewData.size(); i++)
						{
							listData.add(listNewData.get(i));
						}
						// listData = listNewData;
						// listData.addAll(listNewData);
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
					AppContext.makeToast(RecordList.this, "load_error");
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
			public void onFailure(HttpException arg0, String arg1)
			{

			}

		});
	}

	private void initAnimalListView()
	{
		// 初始化语音
		treeAdapter = new TreeAdapter(RecordList.this, listData);
		btn_record.setWorkuserid(commembertab.getId());// 谁登陆谁录音
		btn_record.setWorkusername(commembertab.getrealName());
		btn_record.setType(type);
		btn_record.setWorkid(workid);
		btn_record.setAdapter(treeAdapter);
		btn_record.setAudioRecord(new AudioRecorder());
		// 初始化列表
		list_footer = RecordList.this.getLayoutInflater().inflate(R.layout.listview_footer, null);
		list_foot_more = (TextView) list_footer.findViewById(R.id.listview_foot_more);
		list_foot_progress = (ProgressBar) list_footer.findViewById(R.id.listview_foot_progress);
		frame_listview_news.addFooterView(list_footer);// 添加底部视图 必须在setAdapter前
		frame_listview_news.setAdapter(treeAdapter);
		// frame_listview_news.setOnItemClickListener(new
		// AdapterView.OnItemClickListener()
		// {
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id)
		// {
		// // 点击头部、底部栏无效
		// if (position == 0 || view == list_footer)
		// return;
		//
		// // Animal animal = null;
		// // // 判断是否是TextView
		// // if (view instanceof TextView)
		// // {
		// // animal = (Animal) view.getTag();
		// // } else
		// // {
		// // TextView tv = (TextView)
		// // view.findViewById(R.id.news_listitem_title);
		// // animal = (Animal) tv.getTag();
		// // }
		// // if (animal == null)
		// // return;
		// RecordBean RecordBean = listData.get(position - 1);
		// if (RecordBean == null)
		// return;
		// // Intent intent = new Intent(RecordList.this,
		// // ShowPlant_.class);
		// // intent.putExtra("bean", RecordBean); // 因为list中添加了头部,因此要去掉一个
		// // RecordList.this.startActivity(intent);
		// }
		// });
		// frame_listview_news.setOnScrollListener(new
		// AbsListView.OnScrollListener()
		// {
		// public void onScrollStateChanged(AbsListView view, int scrollState)
		// {
		// frame_listview_news.onScrollStateChanged(view, scrollState);
		//
		// // 数据为空--不用继续下面代码了
		// if (listData.isEmpty())
		// return;
		//
		// // 判断是否滚动到底部
		// boolean scrollEnd = false;
		// try
		// {
		// if (view.getPositionForView(list_footer) ==
		// view.getLastVisiblePosition())
		// scrollEnd = true;
		// } catch (Exception e)
		// {
		// scrollEnd = false;
		// }
		//
		// int lvDataState = StringUtils.toInt(frame_listview_news.getTag());
		// if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE)
		// {
		// frame_listview_news.setTag(UIHelper.LISTVIEW_DATA_LOADING);
		// list_foot_more.setText(R.string.load_ing);// 之前显示为"完成"加载
		// list_foot_progress.setVisibility(View.VISIBLE);
		// // 当前pageIndex
		// // int pageIndex = listSumData / AppContext.PAGE_SIZE;//
		// // 总数里面包含几个PAGE_SIZE
		// // getListData(UIHelper.LISTVIEW_ACTION_SCROLL,
		// // UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news,
		// // treeAdapter, list_foot_more, list_foot_progress,
		// // AppContext.PAGE_SIZE, pageIndex);
		// // loadLvNewsData(curNewsCatalog, pageIndex, lvNewsHandler,
		// // UIHelper.LISTVIEW_ACTION_SCROLL);
		// }
		// }
		//
		// public void onScroll(AbsListView view, int firstVisibleItem, int
		// visibleItemCount, int totalItemCount)
		// {
		// frame_listview_news.onScroll(view, firstVisibleItem,
		// visibleItemCount, totalItemCount);
		// }
		// });
		// frame_listview_news.setOnRefreshListener(new
		// PullToRefreshListView.OnRefreshListener()
		// {
		// public void onRefresh()
		// {
		// // loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
		// // UIHelper.LISTVIEW_ACTION_REFRESH);
		// // getListData(UIHelper.LISTVIEW_ACTION_REFRESH,
		// // UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news,
		// // treeAdapter, list_foot_more, list_foot_progress,
		// // AppContext.PAGE_SIZE, 0);
		//
		// int pageIndex = listSumData / AppContext.PAGE_SIZE;//
		// 总数里面包含几个PAGE_SIZE
		// getListData(UIHelper.LISTVIEW_ACTION_SCROLL,
		// UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, treeAdapter,
		// list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, pageIndex);
		// }
		// });
		// 加载资讯数据
		if (listData.isEmpty())
		{
			getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, treeAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
		}
	}

	public class TreeAdapter extends BaseAdapter
	{
		private Context context;
		private List<RecordBean> listItems;
		private LayoutInflater listContainer;
		RecordBean recordBean;
		ListItemView listItemView = null;

		class ListItemView
		{
			public LinearLayout rl_left;
			public LinearLayout rl_right;
			public CircleImageView iv_img_left;
			public CircleImageView iv_img_right;
			public ImageView iv_record_right;
			public ImageView iv_record_left;
			public ProgressBar pb_upload_left;
			public ProgressBar pb_upload_right;
			public TextView tv_name_right;
			public TextView tv_name_left;
			public TextView tv_time_right;
			public TextView tv_time_left;
			public TextView tv_txt_right;
			public TextView tv_txt_left;
		}

		public TreeAdapter(Context context, List<RecordBean> data)
		{
			this.context = context;
			this.listContainer = LayoutInflater.from(context);
			this.listItems = data;
		}

		HashMap<Integer, View> lmap = new HashMap<Integer, View>();

		public View getView(int position, View convertView, ViewGroup parent)
		{
			recordBean = listItems.get(position);

			if (lmap.get(position) == null)
			{
				convertView = listContainer.inflate(R.layout.record_itme, null);
				listItemView = new ListItemView();
				listItemView.tv_txt_left = (TextView) convertView.findViewById(R.id.tv_txt_left);
				listItemView.tv_txt_right = (TextView) convertView.findViewById(R.id.tv_txt_right);
				listItemView.tv_time_left = (TextView) convertView.findViewById(R.id.tv_time_left);
				listItemView.tv_time_right = (TextView) convertView.findViewById(R.id.tv_time_right);
				listItemView.tv_name_left = (TextView) convertView.findViewById(R.id.tv_name_left);
				listItemView.tv_name_right = (TextView) convertView.findViewById(R.id.tv_name_right);
				listItemView.pb_upload_right = (ProgressBar) convertView.findViewById(R.id.pb_upload_right);
				listItemView.pb_upload_left = (ProgressBar) convertView.findViewById(R.id.pb_upload_left);
				listItemView.rl_left = (LinearLayout) convertView.findViewById(R.id.rl_left);
				listItemView.rl_right = (LinearLayout) convertView.findViewById(R.id.rl_right);
				listItemView.iv_img_left = (CircleImageView) convertView.findViewById(R.id.iv_img_left);
				listItemView.iv_img_right = (CircleImageView) convertView.findViewById(R.id.iv_img_right);
				listItemView.iv_record_right = (ImageView) convertView.findViewById(R.id.iv_record_right);
				listItemView.iv_record_left = (ImageView) convertView.findViewById(R.id.iv_record_left);
				listItemView.iv_record_right.setId(position);
				listItemView.iv_record_left.setId(position);
				listItemView.iv_record_left.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						v.setVisibility(View.GONE);
						ProgressBar pb = ((ListItemView) (lmap.get(v.getId()).getTag())).pb_upload_left;
						pb.setVisibility(View.VISIBLE);
						String filename = listItems.get(v.getId()).getVidiourl().toString().substring(listItems.get(v.getId()).getVidiourl().lastIndexOf("/"), listItems.get(v.getId()).getVidiourl().length());
						downloadLuYin(pb, v, AppConfig.baseurl + listItems.get(v.getId()).getVidiourl(), AppConfig.audiopath + filename);
					}
				});
				listItemView.iv_record_right.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						v.setVisibility(View.GONE);
						ProgressBar pb = ((ListItemView) (lmap.get(v.getId()).getTag())).pb_upload_left;
						pb.setVisibility(View.VISIBLE);
						String filename = listItems.get(v.getId()).getVidiourl().toString().substring(listItems.get(v.getId()).getVidiourl().lastIndexOf("/"), listItems.get(v.getId()).getVidiourl().length());
						downloadLuYin(pb, v, AppConfig.baseurl + listItems.get(v.getId()).getVidiourl(), AppConfig.audiopath + filename);
						// downloadLuYin(listItemView.pb_upload_right, v,
						// AppConfig.baseurl + recordBean.getVidiourl(),
						// AppConfig.audiopath + filename);
					}
				});
				lmap.put(position, convertView);
				convertView.setTag(listItemView);
			} else
			{
				convertView = lmap.get(position);
				listItemView = (ListItemView) convertView.getTag();
			}
			commembertab commembertab = AppContext.getUserInfo(context);
			if (recordBean.getFromUserID().equals(commembertab.getId()))
			{
				listItemView.rl_right.setVisibility(View.VISIBLE);
				if (!recordBean.getFromUserImg().equals(""))
				{
					BitmapHelper.setImageViewBackground(context, listItemView.iv_img_right, AppConfig.baseurl + recordBean.getFromUserImg());
				}
				listItemView.tv_time_right.setText(recordBean.getRegDate().subSequence(5, recordBean.getRegDate().lastIndexOf(":")));
				listItemView.tv_name_right.setText(recordBean.getFromUserName());
				String aa = recordBean.getFromText();
				if (!recordBean.getFromText().equals(""))
				{
					listItemView.iv_record_right.setVisibility(View.GONE);
					listItemView.tv_txt_right.setVisibility(View.VISIBLE);
					listItemView.tv_txt_right.setText(recordBean.getFromText());
				} else
				{
					LayoutParams lp = (LayoutParams) listItemView.iv_record_right.getLayoutParams();
					if (Integer.valueOf(recordBean.getVidiolen()) > 0 && Integer.valueOf(recordBean.getVidiolen()) < 1000)
					{
						lp.width = 150;
						lp.height = LayoutParams.WRAP_CONTENT;
					} else if (Integer.valueOf(recordBean.getVidiolen()) > 1000 && Integer.valueOf(recordBean.getVidiolen()) < 3000)
					{
						lp.width = 200;
						lp.height = LayoutParams.WRAP_CONTENT;
					} else if (Integer.valueOf(recordBean.getVidiolen()) > 3000 && Integer.valueOf(recordBean.getVidiolen()) < 5000)
					{
						lp.width = 250;
						lp.height = LayoutParams.WRAP_CONTENT;
					} else if (Integer.valueOf(recordBean.getVidiolen()) > 5000 && Integer.valueOf(recordBean.getVidiolen()) < 10000)
					{
						lp.width = 300;
						lp.height = LayoutParams.WRAP_CONTENT;
					} else
					{
						lp.width = 350;
						lp.height = LayoutParams.WRAP_CONTENT;
					}
					listItemView.iv_record_right.setLayoutParams(lp);
					listItemView.iv_record_right.setTag(recordBean);
				}

			} else
			{
				listItemView.rl_left.setVisibility(View.VISIBLE);
				if (!recordBean.getFromUserImg().equals(""))
				{
					BitmapHelper.setImageViewBackground(context, listItemView.iv_img_left, AppConfig.baseurl + recordBean.getFromUserImg());
				}
				listItemView.tv_time_left.setText(recordBean.getRegDate().subSequence(5, recordBean.getRegDate().lastIndexOf(":")));
				listItemView.tv_name_left.setText(recordBean.getFromUserName());
				if (!recordBean.getFromText().equals(""))
				{
					listItemView.iv_record_left.setVisibility(View.GONE);
					listItemView.tv_txt_left.setVisibility(View.VISIBLE);
					listItemView.tv_txt_left.setText(recordBean.getFromText());
				} else
				{
					LayoutParams lp = (LayoutParams) listItemView.iv_record_left.getLayoutParams();
					if (Integer.valueOf(recordBean.getVidiolen()) > 0 && Integer.valueOf(recordBean.getVidiolen()) < 1000)
					{
						lp.width = 150;
						lp.height = LayoutParams.WRAP_CONTENT;
					} else if (Integer.valueOf(recordBean.getVidiolen()) > 1000 && Integer.valueOf(recordBean.getVidiolen()) < 3000)
					{
						lp.width = 200;
						lp.height = LayoutParams.WRAP_CONTENT;
					} else if (Integer.valueOf(recordBean.getVidiolen()) > 3000 && Integer.valueOf(recordBean.getVidiolen()) < 5000)
					{
						lp.width = 250;
						lp.height = LayoutParams.WRAP_CONTENT;
					} else if (Integer.valueOf(recordBean.getVidiolen()) > 5000 && Integer.valueOf(recordBean.getVidiolen()) < 10000)
					{
						lp.width = 300;
						lp.height = LayoutParams.WRAP_CONTENT;
					} else
					{
						lp.width = 350;
						lp.height = LayoutParams.WRAP_CONTENT;
					}
					listItemView.iv_record_left.setLayoutParams(lp);
					listItemView.iv_record_left.setTag(recordBean);
				}

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

	public void downloadLuYin(final ProgressBar pb, final View v, String path, final String target)
	{
		HttpUtils http = new HttpUtils();
		http.download(path, target, true, true, new RequestCallBack<File>()
		{
			@Override
			public void onFailure(HttpException error, String msg)
			{
				if (msg.equals("maybe the file has downloaded completely"))
				{
					v.setVisibility(View.VISIBLE);
					pb.setVisibility(View.GONE);
					MediaHelper.playAudio(RecordList.this, target);
					// Toast.makeText(RecordList.this, "下载成功!",
					// Toast.LENGTH_SHORT).show();
				} else
				{
					pb.setVisibility(View.GONE);
					v.setVisibility(View.VISIBLE);
					Toast.makeText(RecordList.this, "语音可能已被删除!", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo)
			{
				v.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				// MediaHelper.playRecord(new File(target));
				MediaHelper.playAudio(RecordList.this, target);
				// Toast.makeText(RecordList.this, "下载成功!",
				// Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void jobTabAdd()
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("fromText", et_content.getText().toString());
		params.addQueryStringParameter("workid", workid);
		params.addQueryStringParameter("type", type);
		params.addQueryStringParameter("action", "vidioTextAdd");
		params.addQueryStringParameter("workuserid", commembertab.getId());
		params.addQueryStringParameter("workusername", commembertab.getrealName());
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<RecordBean> listData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					listData = JSON.parseArray(result.getRows().toJSONString(), RecordBean.class);
					if (listData == null)
					{
						AppContext.makeToast(RecordList.this, "error_connectDataBase");
					} else
					{
						String num;
						HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(RecordList.this, workid);
						if (haveReadRecord != null)
						{
							num = haveReadRecord.getNum();
							SqliteDb.updateHaveReadRecord(RecordList.this, workid, String.valueOf((Integer.valueOf(num) + 1)));
						} else
						{
							SqliteDb.saveHaveReadRecord(RecordList.this, workid, "1");
						}
						commembertab commembertab = AppContext.getUserInfo(RecordList.this);
						if (commembertab.getnlevel().toString().equals("0"))
						{
							HaveReadRecord haveReadd = SqliteDb.getHaveReadRecord(RecordList.this, AppContext.TAG_NCZ_CMD);
							if (haveReadd != null)
							{
								SqliteDb.updateHaveReadRecord(RecordList.this, AppContext.TAG_NCZ_CMD, String.valueOf((Integer.valueOf(haveReadd.getNum()) + 1)));
							} else
							{
								SqliteDb.saveHaveReadRecord(RecordList.this, AppContext.TAG_NCZ_CMD, "1");
							}
						} else
						{
							HaveReadRecord haveReadd2 = SqliteDb.getHaveReadRecord(RecordList.this, commembertab.getId());
							if (haveReadd2 != null)
							{
								SqliteDb.updateHaveReadRecord(RecordList.this, commembertab.getId(), String.valueOf((Integer.valueOf(haveReadd2.getNum()) + 1)));
							} else
							{
								SqliteDb.saveHaveReadRecord(RecordList.this, commembertab.getId(), "1");
							}
						}
						et_content.setText("");
						Toast.makeText(RecordList.this, "发送成功！", Toast.LENGTH_SHORT).show();
						getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, treeAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
					}

				} else
				{
					AppContext.makeToast(RecordList.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1)
			{
				String a = error.getMessage();
				AppContext.makeToast(RecordList.this, "error_connectServer");
			}
		});
	}

	class TimeThread extends Thread
	{
		private boolean isSleep = true;
		private boolean stop = false;

		public void run()
		{
			Long starttime = 0l;
			while (!stop)
			{
				if (isSleep)
				{
				} else
				{
					try
					{
						Thread.sleep(AppContext.TIME_REFRESH);
						starttime = starttime + 1000;
						getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, treeAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE_RECORD, 0);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		public void setSleep(boolean sleep)
		{
			isSleep = sleep;
		}

		public void setStop(boolean stop)
		{
			this.stop = stop;
		}
	}

	private void showNewTip()
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		popupWindowView = inflater.inflate(R.layout.newtip_in_right, null);
		popupWindow = new PopupWindow(popupWindowView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		ll_newtip = (LinearLayout) popupWindowView.findViewById(R.id.ll_newtip);
		tv_newtip = (TextView) popupWindowView.findViewById(R.id.tv_newtip);
		popupWindow.setAnimationStyle(R.style.rightinrightout);// 设置PopupWindow的弹出和消失效果
		popupWindow.showAtLocation(rl_titlebar, Gravity.CENTER, 0, 0);
		popupWindow.setOutsideTouchable(true);
		popupWindowView.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (popupWindow.isShowing()))
				{
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		popupWindowView.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (popupWindow.isShowing())
				{
					popupWindow.dismiss();
				}
				return false;
			}
		});
		ll_newtip.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				popupWindow.dismiss();
				oldsieze = newsize;
				frame_listview_news.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				treeAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(frame_listview_news);
			}
		});
		tv_newtip.setText("有" + (newsize - oldsieze) + "条更新");
	}
}
