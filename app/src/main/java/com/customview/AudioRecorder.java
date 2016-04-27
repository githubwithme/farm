package com.customview;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRecorder implements RecordImp
{

	private MediaRecorder recorder;
	private String fileName;
	private String fileFolder;
	private String path;

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileFolder(String fileFolder)
	{
		this.fileFolder = fileFolder;
	}

	public String getFileFolder()
	{
		return fileFolder;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return path;
	}

	private boolean isRecording = false;

	public AudioRecorder()
	{

	}

	@Override
	public void ready()
	{
		File file = new File(fileFolder);
		if (!file.exists())
		{
			file.mkdir();
		}
		recorder = new MediaRecorder();
		recorder.setOutputFile(path);
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);// 设置MediaRecorder录制的音频格式
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置MediaRecorder录制音频的编码为amr
	}

	// 以当前时间作为文件名
	private String getCurrentDate()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	@Override
	public void start()
	{
		// TODO Auto-generated method stub
		if (!isRecording)
		{
			try
			{
				recorder.prepare();
				recorder.start();
			} catch (IllegalStateException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			isRecording = true;
		}

	}

	@Override
	public void stop()
	{
		// TODO Auto-generated method stub
		if (isRecording)
		{
			recorder.stop();
			recorder.release();
			isRecording = false;
		}

	}

	@Override
	public void deleteOldFile()
	{
		// TODO Auto-generated method stub
		File file = new File(path);
		file.deleteOnExit();
	}

	@Override
	public double getAmplitude()
	{
		// TODO Auto-generated method stub
		if (!isRecording)
		{
			return 0;
		}
		return recorder.getMaxAmplitude();
	}

}
