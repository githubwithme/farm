package com.tips;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.farm.R;

import java.util.List;

public class BaseActivity extends Activity
{
	List<Integer> guideResourceId;// 引导页图片资源id

	@Override
	protected void onStart()
	{
		super.onStart();
		addGuideImage();// 添加引导页
	}

	/**
	 * 添加引导图片
	 */
	public void addGuideImage()
	{
		View view = getWindow().getDecorView().findViewById(R.id.my_content_view);// 查找通过setContentView上的根布局
		if (view == null)
			return;
		if (MyPreferences.activityIsGuided(this, this.getClass().getName()))
		{
			// 引导过了
			return;
		}
		ViewParent viewParent = view.getParent();
		if (viewParent instanceof FrameLayout)
		{
			final FrameLayout frameLayout = (FrameLayout) viewParent;
			for (int i = 0; i < guideResourceId.size(); i++)
			{
				final ImageView guideImage = new ImageView(this);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
				guideImage.setLayoutParams(params);
				guideImage.setScaleType(ScaleType.FIT_XY);
				guideImage.setImageResource(guideResourceId.get(i));
				guideImage.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						frameLayout.removeView(v);
						MyPreferences.setIsGuided(getApplicationContext(), BaseActivity.this.getClass().getName());// 设为已引导
					}
				});
				frameLayout.addView(guideImage);// 添加引导图片

			}

		}
	}

	/**
	 * 子类在onCreate中调用，设置引导图片的资源id 并在布局xml的根元素上设置android:id="@id/my_content_view"
	 * 
	 * @param resId
	 */
	protected void setGuideResId(List<Integer> resId)
	{
		this.guideResourceId = resId;
	}
}