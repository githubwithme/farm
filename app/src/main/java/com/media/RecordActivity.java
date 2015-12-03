package com.media;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.farm.R;

import java.io.File;

public class RecordActivity extends Activity implements OnClickListener
{
	String fileUri;
	private MediaPlayer mediaPlayer;
	private MediaRecorder mediaRecorder = new MediaRecorder();
	private File audioFile;

	@Override
	public void onClick(View view)
	{
		try
		{
			String msg = "";
			switch (view.getId())
			{
			case R.id.btnStart:
				// 设置音频来源(一般为麦克风)
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置音频输出格式（默认的输出格式）
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				// 设置音频编码方式（默认的编码方式）
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				// 创建一个临时的音频输出文件
				audioFile = new File(fileUri);
				mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
				mediaRecorder.prepare();
				mediaRecorder.start();
				msg = "正在录音...";
				break;
			case R.id.btnStop:

				if (audioFile != null)
				{
					mediaRecorder.stop();
				}
				msg = "已经停止录音.";

				break;
			case R.id.btnfinish:
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
				break;
			case R.id.btnPlay:
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
							setTitle("录音播放完毕.");

						}
					});
					msg = "正在播放录音...";
				}
				break;

			}
			setTitle(msg);
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		} catch (Exception e)
		{
			setTitle(e.getMessage());
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordactivity);
		Button btnStart = (Button) findViewById(R.id.btnStart);
		Button btnStop = (Button) findViewById(R.id.btnStop);
		Button btnfinish = (Button) findViewById(R.id.btnfinish);
		Button btnPlay = (Button) findViewById(R.id.btnPlay);
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		btnfinish.setOnClickListener(this);
		fileUri = getIntent().getStringExtra("fileUri");
	}
}
