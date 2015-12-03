/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.media;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.app.AppConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("NewApi")
public class HomeFragmentActivity extends FragmentActivity implements ImageFragment.OnImageSelectedListener, VideoFragment.OnVideoSelectedListener, LuYinFragment.OnLuYinSelectedListener
{

	private FrameLayout contain;
	private TextView headerBarTitle;
	private ImageView headerBarCamera;
	private ImageView headerBarBack;
	private ImageView headerBarDone;

	VideoFragment videoFragment;
	ImageFragment imageFragment;
	LuYinFragment luyinFragment;
	private static Uri fileUri;
	String type;
	String From;
	private MediaRecorder recorder;
	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_home);
		getActionBar().hide();
		headerBarTitle = (TextView) findViewById(R.id.titleTextViewFromMediaChooserHeaderBar);
		headerBarCamera = (ImageView) findViewById(R.id.cameraImageViewFromMediaChooserHeaderBar);
		headerBarBack = (ImageView) findViewById(R.id.backArrowImageViewFromMediaChooserHeaderView);
		headerBarDone = (ImageView) findViewById(R.id.doneImageViewFromMediaChooserHeaderView);
		contain = (FrameLayout) findViewById(R.id.contain);

		headerBarBack.setOnClickListener(clickListener);
		headerBarCamera.setOnClickListener(clickListener);
		headerBarDone.setOnClickListener(clickListener);

		type = getIntent().getExtras().getString("type");
		From = getIntent().getExtras().getString("From");
		if (!MediaChooserConstants.showCameraVideo)
		{
			headerBarCamera.setVisibility(View.GONE);
		}
		if (type.equals("video"))
		{
			headerBarTitle.setText(getResources().getString(R.string.video));
			setHeaderBarCameraBackground(getResources().getDrawable(R.drawable.selector_video_button));
			headerBarCamera.setTag(getResources().getString(R.string.video));
			videoFragment = new VideoFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.contain, videoFragment).commit();
		} else if (type.equals("picture"))
		{
			headerBarTitle.setText(getResources().getString(R.string.image));
			setHeaderBarCameraBackground(getResources().getDrawable(R.drawable.selector_camera_button));
			headerBarCamera.setTag(getResources().getString(R.string.image));
			imageFragment = new ImageFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.contain, imageFragment).commit();
		} else
		{
			headerBarTitle.setText(getResources().getString(R.string.luyin));
			setHeaderBarCameraBackground(getResources().getDrawable(R.drawable.selector_camera_button));
			headerBarCamera.setTag(getResources().getString(R.string.luyin));
			luyinFragment = new LuYinFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.contain, luyinFragment).commit();
		}
	}

	OnClickListener clickListener = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			if (view == headerBarCamera)
			{
				if (view.getTag().toString().equals(getResources().getString(R.string.video)))
				{// 录像
					Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
					fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_VIDEO);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					startActivityForResult(intent, MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
				} else if (view.getTag().toString().equals(getResources().getString(R.string.image)))
				{// 拍照
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_IMAGE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					startActivityForResult(intent, MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				} else
				// 录音
				{
					fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_LUYIN);
					String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
					Intent intent = new Intent(HomeFragmentActivity.this, RecordActivity.class);
					intent.putExtra("fileUri", fileUriString);
					startActivityForResult(intent, MediaChooserConstants.CAPTURE_LUYIN_ACTIVITY_REQUEST_CODE);

					// Intent intent = new
					// Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
					// fileUri =
					// getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_LUYIN);
					// intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					// startActivityForResult(intent,
					// MediaChooserConstants.CAPTURE_LUYIN_ACTIVITY_REQUEST_CODE);
				}
			} else if (view == headerBarDone)
			{
				if (videoFragment != null || imageFragment != null || luyinFragment != null)
				{
					if (videoFragment != null)
					{
						if (videoFragment.getSelectedVideoList() != null && videoFragment.getSelectedVideoList().size() > 0)
						{
							Intent videoIntent = new Intent();
							videoIntent.setAction(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
							videoIntent.putStringArrayListExtra("list", videoFragment.getSelectedVideoList());
							sendBroadcast(videoIntent);
						}
					}
					if (imageFragment != null)
					{
						if (imageFragment.getSelectedImageList() != null && imageFragment.getSelectedImageList().size() > 0)
						{
							Intent imageIntent = new Intent();
							imageIntent.setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
							imageIntent.putStringArrayListExtra("list", imageFragment.getSelectedImageList());
							imageIntent.putExtra("From", From);
							sendBroadcast(imageIntent);
						}
					}
					if (luyinFragment != null)
					{
						if (luyinFragment.getSelectedImageList() != null && luyinFragment.getSelectedImageList().size() > 0)
						{
							Intent imageIntent = new Intent();
							imageIntent.setAction(MediaChooser.LUYIN_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
							imageIntent.putStringArrayListExtra("list", luyinFragment.getSelectedImageList());
							sendBroadcast(imageIntent);
						}
					}

					finish();
				} else
				{
					Toast.makeText(HomeFragmentActivity.this, getString(R.string.plaese_select_file), Toast.LENGTH_SHORT).show();
				}

			} else if (view == headerBarBack)
			{
				finish();
			}
		}
	};

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(int type)
	{
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type)
	{

		File mediaStorageDir = new File(AppConfig.MEDIA_PATH);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode == MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE)
		{// 录像
			if (resultCode == RESULT_OK)
			{

				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
				final AlertDialog alertDialog = MediaChooserConstants.getDialog(HomeFragmentActivity.this).create();
				alertDialog.show();
				handler.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						// Do something after 5000ms
						String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
						if (videoFragment == null)
						{
							VideoFragment newVideoFragment = new VideoFragment();
							newVideoFragment.addItem(fileUriString);

						} else
						{
							videoFragment.addItem(fileUriString);
						}
						alertDialog.cancel();
					}
				}, 1500);

			} else if (resultCode == RESULT_CANCELED)
			{
				// User cancelled the video capture
			} else
			{
				// Video capture failed, advise user
			}
		} else if (requestCode == MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
		{// 拍照
			if (resultCode == RESULT_OK)
			{
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
				final AlertDialog alertDialog = MediaChooserConstants.getDialog(HomeFragmentActivity.this).create();
				alertDialog.show();
				handler.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						// Do something after 5000ms
						String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
						if (imageFragment == null)
						{
							ImageFragment newImageFragment = new ImageFragment();
							newImageFragment.addItem(fileUriString);

						} else
						{
							imageFragment.addItem(fileUriString);
						}
						alertDialog.cancel();
					}
				}, 1500);
			}
		} else if (requestCode == 300)
		{// 录音
			if (resultCode == RESULT_OK)
			{
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
				final AlertDialog alertDialog = MediaChooserConstants.getDialog(HomeFragmentActivity.this).create();
				alertDialog.show();
				handler.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						luyinFragment = new LuYinFragment();
						getSupportFragmentManager().beginTransaction().replace(R.id.contain, luyinFragment).commit();
						// Do something after 5000ms
						// String fileUriString =
						// fileUri.toString().replaceFirst("file:///",
						// "/").trim();
						// if(luyinFragment == null)
						// {
						// LuYinFragment newLuYinFragment = new LuYinFragment();
						// newLuYinFragment.addItem(fileUriString);
						//
						// }else
						// {
						// luyinFragment.addItem(fileUriString);
						// }
						alertDialog.cancel();
					}
				}, 1500);
			}
		}
	}

	@Override
	public void onImageSelected(int count)
	{
		setHeaderBarTitleText(count);
	}

	@Override
	public void onVideoSelected(int count)
	{
		setHeaderBarTitleText(count);
	}

	@Override
	public void onLuYinSelected(int count)
	{
		setHeaderBarTitleText(count);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setHeaderBarCameraBackground(Drawable drawable)
	{
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
			headerBarCamera.setBackgroundDrawable(drawable);
		} else
		{
			headerBarCamera.setBackground(drawable);
		}
	}

	private void setHeaderBarTitleText(int count)
	{
		if (type.equals("video"))
		{
			headerBarTitle.setText("已选择了" + count + "段视频");
		}

		if (type.equals("picture"))
		{
			headerBarTitle.setText("已选择了" + count + "张图片");
		}
		if (type.equals("luyin"))
		{
			headerBarTitle.setText("已选择了" + count + "段录音");
		}
	}

	// private void initializeAudio()
	// {
	// recorder = new MediaRecorder();// new出MediaRecorder对象
	// recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	// // 设置MediaRecorder的音频源为麦克风
	// recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
	// // 设置MediaRecorder录制的音频格式
	// recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	// // 设置MediaRecorder录制音频的编码为amr.
	// recorder.setOutputFile("/sdcard/peipei.amr");
	// // 设置录制好的音频文件保存路径
	// try {
	// recorder.prepare();// 准备录制
	// recorder.start();// 开始录制
	// } catch (IllegalStateException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

}
