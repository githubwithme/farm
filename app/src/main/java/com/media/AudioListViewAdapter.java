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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.farm.R;

import java.util.List;

public class AudioListViewAdapter extends ArrayAdapter<MediaModel>
{
	public VideoFragment videoFragment;

	private Context mContext;
	private List<MediaModel> mGalleryModelList;
	private int mWidth;
	private boolean mIsFromVideo;
	LayoutInflater viewInflater;

	public AudioListViewAdapter(Context context, int resource, List<MediaModel> categories, boolean isFromVideo)
	{
		super(context, resource, categories);
		mGalleryModelList = categories;
		mContext = context;
		mIsFromVideo = isFromVideo;
		viewInflater = LayoutInflater.from(mContext);
	}

	public int getCount()
	{
		return mGalleryModelList.size();
	}

	@Override
	public MediaModel getItem(int position)
	{
		return mGalleryModelList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		ViewHolder holder;

		if (convertView == null)
		{

			mWidth = mContext.getResources().getDisplayMetrics().widthPixels;

			convertView = viewInflater.inflate(R.layout.view_list_item_media_chooser, parent, false);

			holder = new ViewHolder();
			holder.checkBoxTextView = (CheckedTextView) convertView.findViewById(R.id.checkTextViewFromMediaChooserGridItemRowView);
			holder.tv_audioName = (TextView) convertView.findViewById(R.id.imageViewFromMediaChooserGridItemRowView);

			convertView.setTag(holder);

		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		// LayoutParams imageParams = (LayoutParams)
		// holder.tv_audioName.getLayoutParams();
		// imageParams.width = mWidth/2;
		// imageParams.height = mWidth/2;

		// holder.tv_audioName.setLayoutParams(imageParams);

		// set the status according to this Category item

		// if(mIsFromVideo)
		// {
		// new VideoLoadAsync(videoFragment, holder.tv_audioName, false,
		// mWidth/2).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR,
		// mGalleryModelList.get(position).url.toString());
		//
		// }else
		// {
		// ImageLoadAsync loadAsync = new ImageLoadAsync(mContext,
		// holder.tv_audioName, mWidth/2);
		// loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR,
		// mGalleryModelList.get(position).url);
		// }
		holder.tv_audioName.setText(mGalleryModelList.get(position).url.substring(mGalleryModelList.get(position).url.lastIndexOf("/") + 1));
		holder.checkBoxTextView.setChecked(mGalleryModelList.get(position).status);
		return convertView;
	}

	class ViewHolder
	{
		TextView tv_audioName;
		CheckedTextView checkBoxTextView;
	}

}
