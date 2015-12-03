package com.farm.widget.swipelistview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A {@link BaseAdapterDecorator} class that provides animations to the removal
 * of items in the given {@link BaseAdapter}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class AnimateDismissAdapter extends BaseAdapterDecorator {

	private final OnDismissCallback mCallback;

	/**
	 * Create a new AnimateDismissAdapter based on the given {@link BaseAdapter}
	 * .
	 * 
	 * @param callback
	 *            The {@link OnDismissCallback} to trigger when the user has
	 *            indicated that she would like to dismiss one or more list
	 *            items.
	 */
	public AnimateDismissAdapter(final BaseAdapter baseAdapter,
			final OnDismissCallback callback) {
		super(baseAdapter);
		mCallback = callback;
	}

	/**
	 * Animate dismissal of the item at given position.
	 */
	@SuppressWarnings("UnusedDeclaration")
	public void animateDismiss(final int position) {
		animateDismiss(Arrays.asList(position));
	}

	/**
	 * Animate dismissal of the items at given positions.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void animateDismiss(final Collection<Integer> positions) {
		final List<Integer> positionsCopy = new ArrayList<Integer>(positions);
		if (getAbsListView() == null) {
			throw new IllegalStateException(
					"Call setAbsListView() on this AnimateDismissAdapter before calling setAdapter()!");
		}

		List<View> views = getVisibleViewsForPositions(positionsCopy);

		if (!views.isEmpty()) {
			List<Animator> animators = new ArrayList<Animator>();
			for (final View view : views) {
				animators.add(createAnimatorForView(view));
			}

			AnimatorSet animatorSet = new AnimatorSet();

			Animator[] animatorsArray = new Animator[animators.size()];
			for (int i = 0; i < animatorsArray.length; i++) {
				animatorsArray[i] = animators.get(i);
			}

			animatorSet.playTogether(animatorsArray);
			animatorSet.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(final Animator animator) {
					invokeCallback(positionsCopy);
				}
			});
			animatorSet.start();
		} else {
			invokeCallback(positionsCopy);
		}
	}

	private void invokeCallback(final Collection<Integer> positions) {
		ArrayList<Integer> positionsList = new ArrayList<Integer>(positions);
		Collections.sort(positionsList);

		int[] dismissPositions = new int[positionsList.size()];
		for (int i = 0; i < positionsList.size(); i++) {
			dismissPositions[i] = positionsList.get(positionsList.size() - 1
					- i);
		}
		mCallback.onDismiss(getAbsListView(), dismissPositions);
	}

	private List<View> getVisibleViewsForPositions(
			final Collection<Integer> positions) {
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < getAbsListView().getChildCount(); i++) {
			View child = getAbsListView().getChildAt(i);
			if (positions.contains(AdapterViewUtil.getPositionForView(
					getAbsListView(), child))) {
				views.add(child);
			}
		}
		return views;
	}

	private Animator createAnimatorForView(final View view) {
		final ViewGroup.LayoutParams lp = view.getLayoutParams();
		final int originalHeight = view.getHeight();

		ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 0);
		animator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(final Animator animator) {
				lp.height = 0;
				view.setLayoutParams(lp);
			}
		});

		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(final ValueAnimator valueAnimator) {
				lp.height = (Integer) valueAnimator.getAnimatedValue();
				view.setLayoutParams(lp);
			}
		});

		return animator;
	}
}
