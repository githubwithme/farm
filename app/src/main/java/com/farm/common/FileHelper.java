package com.farm.common;

import android.net.Uri;

import com.media.MediaChooserConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHelper
{
	/** Create a file Uri for saving an image or video */
	public static String getOutputMediaFileUriString(int type, String path)
	{
		Uri uri = Uri.fromFile(getOutputMediaFile(type, path));
		String fileUriString = uri.toString().replaceFirst("file:///", "/").trim();
		return fileUriString;
	}

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type, String path)
	{
		return Uri.fromFile(getOutputMediaFile(type, path));
	}

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(int type, String path)
	{

		File mediaStorageDir = new File(path);
		if (!mediaStorageDir.exists())
		{
			if (!mediaStorageDir.mkdirs())
			{
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile = null;
		if (type == MediaChooserConstants.MEDIA_TYPE_IMAGE)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else if (type == MediaChooserConstants.MEDIA_TYPE_VIDEO)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} else if (type == MediaChooserConstants.MEDIA_TYPE_LUYIN)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "AUD_" + timeStamp + ".mp3");
		}
		return mediaFile;
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void RecursionDeleteFile(File file)
	{
		if (file.isFile())
		{
			file.delete();
			return;
		}
		if (file.isDirectory())
		{
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0)
			{
				file.delete();
				return;
			}
			for (File f : childFile)
			{
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}
}
