package com.farm.ui;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.common.FileHelper;
import com.media.MediaChooserConstants;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EFragment
public class AudioFragment extends Fragment
{
	@ViewById
	Button btn_play;
	@ViewById
	Button btn_start;
	// String fileUri;
	private MediaPlayer mediaPlayer;
	private MediaRecorder mediaRecorder = new MediaRecorder();
	private File audioFile;
	String audiopath;

	@Click
	void btn_play()
	{
		try
		{
			if (audioFile != null)
			{
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(audioFile.getAbsolutePath());
				mediaPlayer.prepare();
				mediaPlayer.start();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						// 播放完毕
					}
				});
			}
		} catch (Exception e)
		{
		}
	}

	@Click
	void btn_start()
	{
		try
		{
			String str = btn_start.getText().toString();
			if (str.equals("开始录音") || str.equals("重新录音"))
			{
				btn_start.setText("完成");
				// 设置音频来源(一般为麦克风)
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置音频输出格式（默认的输出格式）
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				// 设置音频编码方式（默认的编码方式）
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				// 创建一个临时的音频输出文件
				audioFile = new File(audiopath);
				mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
				mediaRecorder.prepare();
				mediaRecorder.start();
			} else if (str.equals("完成"))
			{
				if (audioFile != null)
				{
					mediaRecorder.stop();
					btn_start.setText("重新录音");
					btn_play.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e)
		{
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.audiofragment, container, false);
		audiopath = FileHelper.getOutputMediaFileUriString(MediaChooserConstants.MEDIA_TYPE_LUYIN, AppConfig.MEDIA_PATH);
		return rootView;
	}

	public File getAudiopath()
	{
		return audioFile;
	}
}
