package com.farm.widget.swipelistview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

public class ExpandAniLinearLayout extends LinearLayout implements AnimatorUpdateListener, OnClickListener
{
	private static final String TAG = "ExpandAniLinearLayout";
	private LayoutInflater mLayoutInflater;
	private static final float END_FRAME = 100;
	private static final int BACK_TIME = 200;
	private static final int SCALE_TIME = 500;
	private static final int DOWN_TIME = 3000;
	private PropertyValuesHolder mTranslate;
	private ValueAnimator mAnimator;
	private int mItemHeight;
	private onItemSelectListener onItemSelectListener;
	private OnLayoutAnimatListener onLayoutAnimatListener;
	private int mSelectPosition;
	private static final float overAni = 0f;
	private boolean mCareAni = true;
	private int mCurrentbackAni = -1;
	private AnimatorListenerAdapter mAnimatorListener;

	private int downAnimatId, scaleAnimatId, alphaAnimatId;

	public ExpandAniLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mLayoutInflater = LayoutInflater.from(context);
		setChildrenDrawingOrderEnabled(true);
		Keyframe maskBegin = Keyframe.ofFloat(0f, 0);
		Keyframe maskEnd = Keyframe.ofFloat(1f, END_FRAME);
		mTranslate = PropertyValuesHolder.ofKeyframe("translate", maskBegin, maskEnd);
		mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, mTranslate);
		mAnimator.setDuration(DOWN_TIME);
		mAnimator.addUpdateListener(this);
		mAnimatorListener = new AnimatorListenerAdapter()
		{
			private boolean mCanceld = false;

			@Override
			public void onAnimationStart(Animator animation)
			{
				mCanceld = false;
			}

			@Override
			public void onAnimationEnd(Animator animation)
			{
				if (!mCanceld)
				{
					resumeAllChildSafely();
				}

			}

			@Override
			public void onAnimationCancel(Animator animation)
			{
				mCanceld = true;
				setChildVisibility(false);

			}

		};
		mAnimator.addListener(mAnimatorListener);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		if (mAnimator.isRunning())
		{
			return true;
		} else
		{
			return super.dispatchTouchEvent(ev);
		}
	}

	private void backAnimator(final View child)
	{
		final View title = child.findViewById(scaleAnimatId);
		AnimatorSet childAnimatorSet = new AnimatorSet();
		ObjectAnimator result = ObjectAnimator.ofFloat(child, "translationY", child.getTranslationY(), 0);
		final boolean select = indexOfChild(child) == mSelectPosition;
		AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationCancel(Animator animation)
			{
				setChildVisible(child, false);

			}
		};
		result.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				title.setPivotX(0);
				title.setPivotY(0);
				title.setVisibility(View.VISIBLE);
				child.setSelected(select);
			}
		});
		result.setDuration(BACK_TIME);
		result.setInterpolator(new OvershootInterpolator(6));
		if (select && alphaAnimatId > -1)
		{
			final View bg = child.findViewById(alphaAnimatId);
			ObjectAnimator alpha = ObjectAnimator.ofFloat(bg, "alpha", 0, 1);
			childAnimatorSet.play(alpha).after(result);
		}
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(title, "scaleX", 0, 1);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(title, "scaleY", 0, 1);
		scaleX.setInterpolator(new OvershootInterpolator(1f));
		scaleY.setInterpolator(new OvershootInterpolator(1f));
		if (getChildAt(getChildCount() - 1) == child)
		{
			scaleY.addListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation)
				{
					super.onAnimationEnd(animation);

					if (onLayoutAnimatListener != null)
					{
						onLayoutAnimatListener.onAnimatEnd();
					}
				}
			});
		}
		scaleX.setDuration(SCALE_TIME);
		scaleY.setDuration(SCALE_TIME);
		childAnimatorSet.play(result);
		childAnimatorSet.play(scaleX).after(result);
		childAnimatorSet.play(scaleY).after(result);
		childAnimatorSet.addListener(animatorListenerAdapter);
		childAnimatorSet.start();
		child.setTag(childAnimatorSet);

	}

	// public void addItem(int iconId, int titleId) {
	// View child = mLayoutInflater.inflate(R.layout.search_engine_ani_item,
	// null);
	// child.findViewById(alphaAnimatId).setBackgroundResource(mItemBgRes);
	// TextView title = (TextView) child.findViewById(scaleAnimatId);
	// title.setText(titleId);
	// title.setVisibility(View.INVISIBLE);
	// ImageView icon = (ImageView) child.findViewById(downAnimatId);
	// icon.setImageResource(iconId);
	// int height = (int)
	// getResources().getDimension(R.dimen.search_item_height);
	// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.MATCH_PARENT, height);
	// addView(child, params);
	// child.setOnClickListener(this);
	//
	// }

	public void addView(View child, int downAnimatId, int scaleAnimatId, int alphaAnimatId, int height, boolean canClick)
	{
		this.downAnimatId = downAnimatId;
		this.scaleAnimatId = scaleAnimatId;
		this.alphaAnimatId = alphaAnimatId;
		if (height > 0)
		{
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
			addView(child, params);
		} else
		{
			addView(child);
		}
		if (canClick)
		{
			child.setOnClickListener(this);
		}
	}

	public void setSelectPosition(int position)
	{
		this.mSelectPosition = position;
	}

	private void setChildVisibility(boolean visible)
	{
		for (int i = 0; i < getChildCount(); i++)
		{
			View child = getChildAt(i);
			setChildVisible(child, visible);
			if (i == mSelectPosition && visible)
			{
				child.setSelected(visible);
			}
		}
	}

	private void setChildVisible(View child, boolean visible)
	{
		int visiblity = visible ? View.VISIBLE : View.INVISIBLE;
		child.setVisibility(visiblity);
		child.findViewById(scaleAnimatId).setVisibility(visiblity);
		child.setSelected(false);
	}

	private void cancelAllAnimator()
	{
		if (mAnimator.isRunning())
		{
			mAnimator.cancel();
		}
		for (int i = 0; i < getChildCount(); i++)
		{
			View child = getChildAt(i);
			if (child.getTag() != null && child.getTag() instanceof AnimatorSet)
			{
				AnimatorSet animatorSet = (AnimatorSet) child.getTag();
				if (animatorSet.isRunning())
				{
					animatorSet.cancel();
				}
			}
		}
	}

	public void startExpand()
	{
		if (onLayoutAnimatListener != null)
		{
			onLayoutAnimatListener.onAnimatStart();
		}
		cancelAllAnimator();
		setChildVisibility(false);
		mCareAni = true;
		mCurrentbackAni = -1;
		mAnimator.start();
	}

	public void show()
	{
		cancelAllAnimator();
		setChildVisibility(true);
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i)
	{
		int result = childCount - i - 1;
		return result;

	}

	private void resumeAllChildSafely()
	{

		for (int i = 0; i < getChildCount(); i++)
		{
			View child = getChildAt(i);
			child.setVisibility(View.VISIBLE);
			child.setTranslationY(0);
			child.setSelected(i == mSelectPosition);
			View title = child.findViewById(scaleAnimatId);
			title.setVisibility(View.VISIBLE);
			title.setScaleX(1);
			title.setScaleY(1);
			if (alphaAnimatId > -1)
			{
				View bg = child.findViewById(alphaAnimatId);
				bg.setAlpha(1);
			}
		}

	}

	public void onAnimationUpdate(ValueAnimator animation)
	{

		if (mCareAni && animation == mAnimator)
		{
			int aniCount = getChildCount() + 1;

			float y = Float.valueOf(animation.getAnimatedValue().toString());
			int index = (int) (y * aniCount / END_FRAME);

			if (index < aniCount)
			{
				float childAnimatorY = (y - index * END_FRAME / aniCount) / (END_FRAME / aniCount);
				int height = 0;
				height = getChildAt(Math.max(0, index - 1)).getMeasuredHeight();

				float currentTranslateY = (childAnimatorY - 1) * height;
				if (index > 0)
				{
					int preIndex = index - 1;
					View pre = getChildAt(preIndex);
					if (childAnimatorY < overAni)
					{
						float preTranslateY = childAnimatorY * pre.getMeasuredHeight();
						pre.setTranslationY(preTranslateY);
					} else
					{
						if (mCurrentbackAni < preIndex)
						{
							mCurrentbackAni = preIndex;
							if (overAni <= 0.01)
							{
								pre.setTranslationY(0);
							}
							backAnimator(pre);
						}
					}

				}
				if (index < getChildCount())
				{
					View current = getChildAt(index);
					current.setVisibility(View.VISIBLE);
					translateY(current, currentTranslateY);
				}

			}
		}
	}

	private void translateY(View target, float y)
	{
		if (target != null)
		{
			target.setTranslationY(y);
		}
		System.out.println("translateY" + target + "y=" + y);

	}

	@Override
	public void onClick(View v)
	{
		if (onItemSelectListener != null)
		{
			int position = indexOfChild(v);
			onItemSelectListener.onSelect(position);
		}
	}

	public onItemSelectListener getOnItemSelectListener()
	{
		return onItemSelectListener;
	}

	public void setOnItemSelectListener(onItemSelectListener onItemSelectListener)
	{
		this.onItemSelectListener = onItemSelectListener;
	}

	public interface onItemSelectListener
	{
		void onSelect(int position);

	}

	public interface OnLayoutAnimatListener
	{
		void onAnimatEnd();

		void onAnimatStart();
	}

	public OnLayoutAnimatListener getOnLayoutAnimatListener()
	{
		return onLayoutAnimatListener;
	}

	public void setOnLayoutAnimatListener(OnLayoutAnimatListener onLayoutAnimatListener)
	{
		this.onLayoutAnimatListener = onLayoutAnimatListener;
	}

}
