package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.common.BitmapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 图片左右滑动
 *
 * @author hmj
 */
@SuppressLint("NewApi")
public class PictureScrollFragment_DialogFragment extends DialogFragment
{
    private ScheduledExecutorService scheduledExecutorService;
    private List<View> list_view; // 滑动的图片集合
    private List<View> list_dots; // 图片标题正文的那些点
    ViewPager vp_tonggao;
    LinearLayout ll_tonggao_dot;
    List<String> imgurl;

    int currentItem;
    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.arg1)
            {
                case 0:
                    Log.d("handler", "handler");
                    vp_tonggao.setCurrentItem(currentItem);// 切换当前显示的图片
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialogfragment_photos, container, false);
        Bundle bundle = getArguments();
        imgurl = bundle.getStringArrayList("imgurl");
        vp_tonggao = (ViewPager) rootView.findViewById(R.id.vp_tonggao);
        ll_tonggao_dot = (LinearLayout) rootView.findViewById(R.id.ll_tonggao_dot);
        buildView();
        vp_tonggao.setAdapter(new MyAdapter());
        vp_tonggao.setOnPageChangeListener(new PageChangeListener());
        return rootView;
    }

    private void buildView()
    {
        list_view = new ArrayList<View>();
        list_dots = new ArrayList<View>();
        ImageView iv_photo;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (int i = 0; i < imgurl.size(); i++)
        {
            View inflateView = inflater.inflate(R.layout.tonggao_item, null);
            iv_photo = (ImageView) inflateView.findViewById(R.id.iv_photo);
            iv_photo.setId(i);
            iv_photo.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), DisplayImage_.class);
                    intent.putExtra("url", imgurl.get(v.getId()));
                    getActivity().startActivity(intent);
                }
            });
            BitmapHelper.setImageViewBackground(getActivity(), iv_photo, AppConfig.baseurl+imgurl.get(i));
            list_view.add(inflateView);

            View view = inflater.inflate(R.layout.dotview, null);
            view.setLayoutParams(new LayoutParams(15, 15));
            ll_tonggao_dot.addView(view);
            list_dots.add(ll_tonggao_dot.getChildAt(i));
        }
    }

    @Override
    public void onStart()
    {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 4, TimeUnit.SECONDS);// 当Activity显示出来后，每4秒钟切换一次图片显示
        super.onStart();
    }

    @Override
    public void onStop()
    {
        scheduledExecutorService.shutdown();// 当Activity不可见的时候停止切换
        super.onStop();
    }

    /**
     * 换行切换任务
     *
     * @author Administrator
     */
    private class ScrollTask implements Runnable
    {
        public void run()
        {
            synchronized (vp_tonggao)
            {
                currentItem = (currentItem + 1) % imgurl.size();
                Message message = handler.obtainMessage();
                message.arg1 = 0;
                message.sendToTarget(); // 通过Handler切换图片
            }
        }
    }

    /**
     * 填充ViewPager页面的适配器
     *
     * @author hmj
     */
    public class MyAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return list_view.size();
        }

        @Override
        public Object instantiateItem(View viewPager, int position)
        {
            ((ViewPager) viewPager).addView(list_view.get(position));
            return list_view.get(position);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2)
        {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1)
        {

        }

        @Override
        public Parcelable saveState()
        {
            return null;
        }

        @Override
        public void startUpdate(View arg0)
        {

        }

        @Override
        public void finishUpdate(View arg0)
        {

        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author hmj
     */
    public class PageChangeListener implements OnPageChangeListener
    {
        private int oldPosition = 0;

        public void onPageSelected(int position)
        {
            currentItem = position;
            list_dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            list_dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
            list_view.get(position).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Intent intent = new Intent(getActivity(),
                    // ShowPhotos_.class);
                    // intent.putExtra("url", imgurl.get(oldPosition));
                    // getActivity().startActivity(intent);
                }
            });
        }

        public void onPageScrollStateChanged(int arg0)
        {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2)
        {

        }
    }

}