package com.farm.common;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

import java.io.File;

public class MediaHelper
{
	public static void playAudio(Context context, String target)
	{
		File file = new File(target);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "audio/*");
		context.startActivity(intent);
	}
	public static void playRecord(File audioFile)
	{
		try
		{
			if (audioFile != null)
			{
				MediaPlayer mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(audioFile.getAbsolutePath());
				mediaPlayer.prepare();
				mediaPlayer.start();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
					}
				});
			}
		} catch (Exception e)
		{
		}
	}
}
