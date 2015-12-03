package com.customview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.HaveReadRecord;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordButton extends Button
{

	private static final int MIN_RECORD_TIME = 1; // 最短录制时间，单位秒，0为无时间限制
	private static final int RECORD_OFF = 0; // 不在录音
	private static final int RECORD_ON = 1; // 正在录音

	private Dialog mRecordDialog;
	private AudioRecorder mAudioRecorder;
	private Thread mRecordThread;
	private RecordListener listener;

	private int recordState = 0; // 录音状态
	private float recodeTime = 0.0f; // 录音时长，如果录音时间太短则录音失败
	private double voiceValue = 0.0; // 录音的音量值
	public static boolean isCanceled = false; // 是否取消录音
	public static boolean isFinish = false; // 是否完成录音
	private float downY;

	private TextView dialogTextView;
	private ImageView dialogImg;
	private Context mContext;

	String workuserid;
	String workusername;
	String type;
	String workid;
	BaseAdapter adapter;
	String path;
	String fileFolder;
	String fileName;

	public void setAdapter(BaseAdapter adapter)
	{
		this.adapter = adapter;
	}

	public BaseAdapter getAdapter()
	{
		return adapter;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void setWorkid(String workid)
	{
		this.workid = workid;
	}

	public String getWorkid()
	{
		return workid;
	}

	public void setWorkuserid(String workuserid)
	{
		this.workuserid = workuserid;
	}

	public String getWorkuserid()
	{
		return workuserid;
	}

	public void setWorkusername(String workusername)
	{
		this.workusername = workusername;
	}

	public String getWorkusername()
	{
		return workusername;
	}

	public RecordButton(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public RecordButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public RecordButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context)
	{
		mContext = context;
		// this.setText("按住 说话");
	}

	public void setAudioRecord(AudioRecorder record)
	{
		this.mAudioRecorder = record;
	}

	public void setRecordListener(RecordListener listener)
	{
		this.listener = listener;
	}

	// 录音时显示Dialog
	private void showVoiceDialog(int flag)
	{
		if (mRecordDialog == null)
		{
			mRecordDialog = new Dialog(mContext, R.style.Dialogstyle);
			mRecordDialog.setContentView(R.layout.dialog_record);
			dialogImg = (ImageView) mRecordDialog.findViewById(R.id.record_dialog_img);
			dialogTextView = (TextView) mRecordDialog.findViewById(R.id.record_dialog_txt);
		}
		switch (flag)
		{
		case 1:
			dialogImg.setImageResource(R.drawable.record_cancel);
			dialogTextView.setText("松开手指可取消录音");
			// this.setText("松开手指 取消录音");
			break;

		default:
			dialogImg.setImageResource(R.drawable.record_animate_01);
			dialogTextView.setText("向上滑动可取消录音");
			// this.setText("松开手指 完成录音");
			break;
		}
		dialogTextView.setTextSize(14);
		mRecordDialog.show();
	}

	// 录音时间太短时Toast显示
	private void showWarnToast(String toastText)
	{
		Toast toast = new Toast(mContext);
		View warnView = LayoutInflater.from(mContext).inflate(R.layout.toast_warn, null);
		toast.setView(warnView);
		toast.setGravity(Gravity.CENTER, 0, 0);// 起点位置为中间
		toast.show();
	}

	// 开启录音计时线程
	private void callRecordTimeThread()
	{
		mRecordThread = new Thread(recordThread);
		mRecordThread.start();
	}

	// 录音Dialog图片随录音音量大小切换
	private void setDialogImage()
	{
		if (voiceValue < 600.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 600.0 && voiceValue < 1000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 1000.0 && voiceValue < 1200.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 1200.0 && voiceValue < 1400.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1400.0 && voiceValue < 1600.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 1600.0 && voiceValue < 1800.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 1800.0 && voiceValue < 2000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 2000.0 && voiceValue < 3000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 3000.0 && voiceValue < 4000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 4000.0 && voiceValue < 6000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 6000.0 && voiceValue < 8000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 8000.0 && voiceValue < 10000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 10000.0 && voiceValue < 12000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 12000.0)
		{
			dialogImg.setImageResource(R.drawable.record_animate_14);
		}
	}

	// 录音线程
	private Runnable recordThread = new Runnable()
	{

		@Override
		public void run()
		{
			recodeTime = 0.0f;
			while (recordState == RECORD_ON)
			{
				{
					try
					{
						Thread.sleep(100);
						recodeTime += 0.1;
						// 获取音量，更新dialog
						if (!isCanceled)
						{
							voiceValue = mAudioRecorder.getAmplitude();
							recordHandler.sendEmptyMessage(1);
						}
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler recordHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			setDialogImage();
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN: // 按下按钮
			if (recordState != RECORD_ON)
			{
				fileFolder = AppConfig.audiopath;
				fileName = getCurrentDate();
				path = fileFolder + "/" + fileName + ".mp3";
				mAudioRecorder.setFileFolder(fileFolder);
				mAudioRecorder.setFileName(fileName);
				mAudioRecorder.setPath(path);
				showVoiceDialog(0);
				downY = event.getY();
				if (mAudioRecorder != null)
				{
					mAudioRecorder.ready();
					recordState = RECORD_ON;
					mAudioRecorder.start();
					callRecordTimeThread();
				}
			}
			break;
		case MotionEvent.ACTION_MOVE: // 滑动手指
			float moveY = event.getY();
			if (downY - moveY > 50)
			{
				isCanceled = true;
				showVoiceDialog(1);
			}
			if (downY - moveY < 20)
			{
				isCanceled = false;
				showVoiceDialog(0);
			}
			break;
		case MotionEvent.ACTION_UP: // 松开手指
			if (recordState == RECORD_ON)
			{
				recordState = RECORD_OFF;
				if (mRecordDialog.isShowing())
				{
					mRecordDialog.dismiss();
				}
				mAudioRecorder.stop();
				mRecordThread.interrupt();
				voiceValue = 0.0;
				if (isCanceled)
				{
					mAudioRecorder.deleteOldFile();
				} else
				{
					if (recodeTime < MIN_RECORD_TIME)
					{
						showWarnToast("时间太短  录音失败");
						mAudioRecorder.deleteOldFile();
					} else
					{
						uploadAudio(type, workid, workuserid, workusername, new File(path));
						if (listener != null)
						{
							listener.recordEnd();
						}
					}
				}
				isCanceled = false;
				// this.setText("按住 说话");
			}
			break;
		}
		return true;
	}

	public interface RecordListener
	{
		public void recordEnd();
	}

	// 以当前时间作为文件名
	private String getCurrentDate()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	private void uploadAudio(String type, String statusid, String workuserid, String workusername, File audioFile)
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("action", "UpLoadFileJobAudio");
		params.addQueryStringParameter("type", type);
		params.addQueryStringParameter("jobID", statusid);
		params.addQueryStringParameter("workuserid", workuserid);
		params.addQueryStringParameter("workusername", workusername);
		params.addQueryStringParameter("file", audioFile.getName());
		params.setBodyEntity(new FileUploadEntity(audioFile, "text/html"));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						String num;
						HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(mContext, workid);
						if (haveReadRecord != null)
						{
							num = haveReadRecord.getNum();
							SqliteDb.updateHaveReadRecord(mContext, workid, String.valueOf((Integer.valueOf(num) + 1)));
						} else
						{
							SqliteDb.saveHaveReadRecord(mContext, workid, "1");
						}
						commembertab commembertab = AppContext.getUserInfo(mContext);
						if (commembertab.getnlevel().toString().equals("0"))
						{
							HaveReadRecord haveReadd1 = SqliteDb.getHaveReadRecord(mContext, AppContext.TAG_NCZ_CMD);
							if (haveReadd1 != null)
							{
								SqliteDb.updateHaveReadRecord(mContext, AppContext.TAG_NCZ_CMD, String.valueOf((Integer.valueOf(haveReadd1.getNum()) + 1)));
							} else
							{
								SqliteDb.saveHaveReadRecord(mContext, AppContext.TAG_NCZ_CMD, "1");
							}
						} else
						{
							HaveReadRecord haveReadd2 = SqliteDb.getHaveReadRecord(mContext, commembertab.getId());
							if (haveReadd2 != null)
							{
								SqliteDb.updateHaveReadRecord(mContext, commembertab.getId(), String.valueOf((Integer.valueOf(haveReadd2.getNum()) + 1)));
							} else
							{
								SqliteDb.saveHaveReadRecord(mContext, commembertab.getId(), "1");
							}
						}

						Toast.makeText(mContext, "保存成功！", Toast.LENGTH_SHORT).show();
						adapter.notifyDataSetChanged();
						Intent intent = new Intent();
						intent.setAction(AppContext.BROADCAST_REFRESHRECORD);
						mContext.sendBroadcast(intent);
					} else
					{
						AppContext.makeToast(mContext, "error_connectDataBase");
						Toast.makeText(mContext, "发送失败!", Toast.LENGTH_SHORT).show();
					}
				} else
				{
					Toast.makeText(mContext, "发送失败!", Toast.LENGTH_SHORT).show();
					return;
				}

			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String aa = error.getMessage();
				Toast.makeText(mContext, "发送失败!", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
