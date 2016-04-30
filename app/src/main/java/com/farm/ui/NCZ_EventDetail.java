package com.farm.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.FJxx;
import com.farm.bean.ReportedBean;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/4/12.
 */
@EFragment
public class NCZ_EventDetail extends Fragment
{
    @ViewById
    RelativeLayout rl_match;
    ReportedBean reportedBean;
    MyDialog myDialog;
    @ViewById
    LinearLayout ll_picture;
    @ViewById
    LinearLayout ll_video;
    List<FJxx> list_picture = new ArrayList<FJxx>();
    List<FJxx> list_video = new ArrayList<FJxx>();
    List<FJxx> list_allfj = new ArrayList<FJxx>();
    com.farm.bean.commembertab commembertab;
    CustomDialog_ListView customDialog_listView;
    @ViewById
    TextView tv_reported;
    @ViewById
    TextView tv_time;
    @ViewById
    TextView tv_type;
    @ViewById
    TextView et_sjms;


    @AfterViews
    void aftercreate() {
        tv_reported.setText(reportedBean.getReportor());
        tv_time.setText(reportedBean.getReporTime());
        tv_type.setText(reportedBean.getEventType());
//       et_sjms.setText(reportedBean.getEventContent());
        et_sjms.setText(reportedBean.getEventContent());
        if(!reportedBean.getFjxx().equals(""))
        {
            for(int i=0;i<reportedBean.getFjxx().size();i++) {
                if (i==reportedBean.getFjxx().size()-1)
                {
                    rl_match.setVisibility(View.GONE);
                }
                if (reportedBean.getFjxx().get(i).getFJLX().equals("1")) {
                    addServerPicture(reportedBean.getFjxx().get(i));
                }
                if (reportedBean.getFjxx().get(i).getFJLX().equals("2")) {
                    ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    lp.setMargins(25, 4, 0, 4);
                    progressBar.setLayoutParams(lp);
                    ll_video.addView(progressBar);// BitmapHelper.setImageView(PG_EventDetail.this, imageView, AppConfig.baseurl +flview.getFJLJ());
                    downloadVideo(reportedBean.getFjxx().get(i), AppConfig.baseurl + reportedBean.getFjxx().get(i).getFJLJ(), AppConfig.DOWNLOADPATH_VIDEO + reportedBean.getFjxx().get(i).getFJMC(), progressBar);
                }
            }
        }else {
            rl_match.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_detaillayout, container, false);
//        appContext = (AppContext) getActivity().getApplication();
//        commembertab commembertab = AppContext.getUserInfo(getActivity());
//        commembertab = AppContext.getUserInfo(getActivity());
//        goods=getArguments().getParcelable("goods");
        reportedBean = getArguments().getParcelable("reportedBean");
        return rootView;
    }

    private void addServerPicture(FJxx flview) {

        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.setMargins(25, 4, 0, 4);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        BitmapHelper.setImageView(PG_EventDetail.this, imageView, AppConfig.url + fj_SCFJ.getFJLJ());// ?
//        BitmapHelper.setImageView(getActivity(), imageView, AppConfig.baseurl + flview.getFJLJ());
        BitmapHelper.setImageView(getActivity(), imageView, AppConfig.baseurl + flview.getLSTLJ());

        FJxx fj_SCFJ = new FJxx();
//            fj_SCFJ.setFJBDLJ(FJBDLJ);
        fj_SCFJ.setFJLX("1");
        ll_picture.addView(imageView);
        list_picture.add(flview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index_zp = ll_picture.indexOfChild(v);
                View dialog_layout = (LinearLayout)getActivity(). getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "取消", new MyDialog.CustomDialogListener() {
                    @Override
                    public void OnClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_sure:
                          /*      Intent intent = new Intent(PG_EventDetail.this, ShowPhoto_.class);
                                intent.putExtra("url", list_picture.get(index_zp).getFJLJ());
                                startActivity(intent);*/
                         /*       File file = new File(list_picture.get(index_zp).getFJBDLJ());
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "image");
                                startActivity(intent);*/
                                Intent intent = new Intent(getActivity(),DisplayImage_.class);
//                                intent.putExtra("url", AppConfig.baseurl+list_picture.get(index_zp).getFJLJ());
                                intent.putExtra("url", AppConfig.baseurl+list_picture.get(index_zp).getLSTLJ());
                                startActivity(intent);
                                break;
                            case R.id.btn_cancle:
                       /*         if (!list_picture.get(index_zp).getFJID().equals("")) {
                                    deletePhotos(list_picture.get(index_zp).getFJID(), list_picture, ll_picture, index_zp);
                                }
                                ll_picture.removeViewAt(index_zp);
                                list_picture.remove(index_zp);*/
                                myDialog.dismiss();
                                break;
                        }
                    }
                });
                myDialog.show();
            }
        });
    }

    public void downloadVideo(final FJxx fj_SCFJ, String path, final String target, final ProgressBar progressBar)
    {
        HttpUtils http = new HttpUtils();
        http.download(path, target, true, true, new RequestCallBack<File>()
        {
            @Override
            public void onLoading(long total, long current, boolean isUploading)
            {
                if (total > 0)
                {
                    progressBar.setProgress((int) ((double) current / (double) total * 100));
                } else
                {
                    progressBar.setProgress(0);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                if (msg.equals("maybe the file has downloaded completely"))
                {
                    ll_video.removeView(progressBar);
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, ViewGroup.LayoutParams.MATCH_PARENT, 0); // ,
                    // 1是可选写的
                    lp.setMargins(25, 4, 0, 4);
                    imageView.setLayoutParams(lp);// 显示图片的大小
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(BitmapHelper.getVideoThumbnail(target, 120, 120, MediaStore.Images.Thumbnails.MICRO_KIND));

                    /*fj_SCFJ.setFJBDLJ(target);
                    fj_SCFJ.setFJLX("2");
                    fj_SCFJ.setISUPLOAD("1");*/

                    list_video.add(fj_SCFJ);

                    ll_video.addView(imageView);

                    imageView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            final int index_zp = ll_video.indexOfChild(v);
                            View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                            myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "视频", "查看该视频?", "查看", "取消", new MyDialog.CustomDialogListener()
                            {
                                @Override
                                public void OnClick(View v)
                                {
                                    switch (v.getId())
                                    {
                                        case R.id.btn_sure:
//                                            File file = new File(list_video.get(index_zp).getFJBDLJ());
                                            File file = new File(list_video.get(index_zp).getFJLJ());
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(Uri.fromFile(file), "video/*");
                                            startActivity(intent);
                                            break;
                                        case R.id.btn_cancle:

                                            myDialog.dismiss();
                                            break;
                                    }
                                }
                            });
                            myDialog.show();
                        }
                    });
                } else
                {
                    Toast.makeText(getActivity(), "下载失败！找不到文件!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo)
            {
                // progressBar.setVisibility(View.INVISIBLE);
                ll_video.removeView(progressBar);
                Toast.makeText(getActivity(), "下载成功！", Toast.LENGTH_SHORT).show();

                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setTag(target);
                imageView.setImageBitmap(BitmapHelper.getVideoThumbnail(target, 120, 120, MediaStore.Images.Thumbnails.MICRO_KIND));

             /*   fj_SCFJ.setFJBDLJ(target);
                fj_SCFJ.setFJLX("2");
                fj_SCFJ.setISUPLOAD("1");*/

                list_video.add(fj_SCFJ);
                ll_video.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int index_zp = ll_video.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "视频", "查看该视频?", "查看", "取消", new MyDialog.CustomDialogListener()
                        {
                            @Override
                            public void OnClick(View v)
                            {
                                switch (v.getId())
                                {
                                    case R.id.btn_sure:
//                                        File file = new File(list_video.get(index_zp).getFJBDLJ());
                                        File file = new File(list_video.get(index_zp).getFJLJ());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "video/*");
                                        startActivity(intent);
                                        break;
                                    case R.id.btn_cancle:
//                                        FJxx fj_SCFJ =list_video.get(index_zp);
//                                        deleteFJ(list_video.get(index_zp).getFJID(), list_video, ll_video, index_zp);
                                        myDialog.dismiss();
                                        break;
                                }
                            }
                        });
                        myDialog.show();
                    }
                });
            }
        });

    }
}
