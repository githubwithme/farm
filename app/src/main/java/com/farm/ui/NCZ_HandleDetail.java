package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.FJxx;
import com.farm.bean.HandleBean;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.common.BitmapHelper;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/4/14.
 */
@EActivity(R.layout.ncz_handledetail)
public class NCZ_HandleDetail extends Activity
{
@ViewById
    RelativeLayout rl_match;
    MyDialog myDialog;
    @ViewById
    LinearLayout ll_picture;
    @ViewById
    LinearLayout ll_video;
    CustomDialog_ListView customDialog_listView;
    List<FJxx> list_picture = new ArrayList<FJxx>();
    List<FJxx> list_video = new ArrayList<FJxx>();
    List<FJxx> list_allfj = new ArrayList<FJxx>();
    @ViewById
    TextView tv_reported;
    @ViewById
    TextView tv_time;
    @ViewById
    TextView et_sjms;
    @ViewById
    Button btn_delete;

    HandleBean handleBean;


    @Click
    void btn_delete()
    {
        deletehandle();
    }
    @Click
    void imgbtn_back()
    {
        finish();
    }
    @AfterViews
    void afteroncreate()
    {
        if (handleBean.getState().equals("1"))
        {
            btn_delete.setVisibility(View.GONE);
        }
        tv_reported.setText(handleBean.getSolveName());
        tv_time.setText(handleBean.getRegistime());
        et_sjms.setText(handleBean.getResult());
        if(!handleBean.getFjxx().equals(""))
        {
            for(int i=0;i<handleBean.getFjxx().size();i++)
            {
                if(i==(handleBean.getFjxx().size()-1))
                {
                    rl_match.setVisibility(View.GONE);
                }
                if (handleBean.getFjxx().get(i).getFJLX().equals("1")) {
                    addServerPicture(handleBean.getFjxx().get(i));
                }
                if (handleBean.getFjxx().get(i).getFJLX().equals("2")) {
                    ProgressBar progressBar = new ProgressBar(NCZ_HandleDetail.this, null, android.R.attr.progressBarStyleHorizontal);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    lp.setMargins(25, 4, 0, 4);
                    progressBar.setLayoutParams(lp);
                    ll_video.addView(progressBar);// BitmapHelper.setImageView(PG_EventDetail.this, imageView, AppConfig.baseurl +flview.getFJLJ());
                    downloadVideo(handleBean.getFjxx().get(i), AppConfig.baseurl + handleBean.getFjxx().get(i).getFJLJ(), AppConfig.DOWNLOADPATH_VIDEO + handleBean.getFjxx().get(i).getFJMC(), progressBar);
                }
            }
        }else
        {
            rl_match.setVisibility(View.GONE);
        }
        rl_match.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        handleBean=getIntent().getParcelableExtra("handleBean");
    }

    private void addServerPicture(FJxx flview) {

        ImageView imageView = new ImageView(NCZ_HandleDetail.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.setMargins(25, 4, 0, 4);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        BitmapHelper.setImageView(PG_EventDetail.this, imageView, AppConfig.url + fj_SCFJ.getFJLJ());// ?
        BitmapHelper.setImageView(NCZ_HandleDetail.this, imageView, AppConfig.baseurl + flview.getFJLJ());

        FJxx fj_SCFJ = new FJxx();
//            fj_SCFJ.setFJBDLJ(FJBDLJ);
        fj_SCFJ.setFJLX("1");
        ll_picture.addView(imageView);
        list_picture.add(flview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index_zp = ll_picture.indexOfChild(v);
                View dialog_layout = (LinearLayout)getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                myDialog = new MyDialog(NCZ_HandleDetail.this, R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new MyDialog.CustomDialogListener() {
                    @Override
                    public void OnClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_sure:
                       /*         Intent intent = new Intent(PG_EventDetail.this,DisplayImage_.class);
                                intent.putExtra("url",AppConfig.baseurl+list_picture.get(index_zp).getFJLJ());
                                startActivity(intent);*/
                              /*  File file = new File(list_picture.get(index_zp).getFJBDLJ());
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "image");
                                startActivity(intent);*/
                                Intent intent = new Intent(NCZ_HandleDetail.this,DisplayImage_.class);
                                intent.putExtra("url", AppConfig.baseurl+list_picture.get(index_zp).getFJLJ());
                                startActivity(intent);
                                break;
                            case R.id.btn_cancle:
//                                deletePhotos(list_picture.get(index_zp).getFJID(), list_picture, ll_picture, index_zp);
//                                ll_picture.removeViewAt(index_zp);
//                                list_picture.remove(index_zp);
                                myDialog.dismiss();
                                break;
                        }
                    }
                });
                myDialog.show();
            }
        });
    }

    private void deletehandle()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "HandleDelete");
        params.addQueryStringParameter("id", handleBean.getResultId());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {

                    NCZ_HandleDetail.this.finish();
               /*     listData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);
                    if (listData == null) {
                        AppContext.makeToast(PG_EventDetail.this, "error_connectDataBase");
                    } else {
                        String event = listData.get(0).getEventId();

                    }
*/
                } else {
                    AppContext.makeToast(NCZ_HandleDetail.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_HandleDetail.this, "error_connectServer");
            }
        });
    }

    public void downloadVideo(final FJxx fj_SCFJ, String path, final String target, final ProgressBar progressBar)
    {
        HttpUtils http = new HttpUtils();
        http.download(path, target, true, true, new RequestCallBack<File>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (total > 0) {
                    progressBar.setProgress((int) ((double) current / (double) total * 100));
                } else {
                    progressBar.setProgress(0);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (msg.equals("maybe the file has downloaded completely")) {
                    ll_video.removeView(progressBar);
                    ImageView imageView = new ImageView(NCZ_HandleDetail.this);
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

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int index_zp = ll_video.indexOfChild(v);
                            View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                            myDialog = new MyDialog(NCZ_HandleDetail.this, R.style.MyDialog, dialog_layout, "视频", "查看该视频?", "查看", "取消", new MyDialog.CustomDialogListener() {
                                @Override
                                public void OnClick(View v) {
                                    switch (v.getId()) {
                                        case R.id.btn_sure:
//                                            File file = new File(list_video.get(index_zp).getFJBDLJ());
                                            File file = new File(list_video.get(index_zp).getFJLJ());
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(Uri.fromFile(file), "video/*");
                                            startActivity(intent);
                                            break;
                                        case R.id.btn_cancle:
//                                            FJxx fj_SCFJ = list_video.get(index_zp);
//                                            deleteFJ(list_video.get(index_zp).getFJID(), list_video, ll_video, index_zp);
                                            myDialog.dismiss();
                                            break;
                                    }
                                }
                            });
                            myDialog.show();
                        }
                    });
                } else {
                    Toast.makeText(NCZ_HandleDetail.this, "下载失败！找不到文件!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                // progressBar.setVisibility(View.INVISIBLE);
                ll_video.removeView(progressBar);
                Toast.makeText(NCZ_HandleDetail.this, "下载成功！", Toast.LENGTH_SHORT).show();

                ImageView imageView = new ImageView(NCZ_HandleDetail.this);
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

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int index_zp = ll_video.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(NCZ_HandleDetail.this, R.style.MyDialog, dialog_layout, "视频", "查看该视频?", "查看", "取消", new MyDialog.CustomDialogListener() {
                            @Override
                            public void OnClick(View v) {
                                switch (v.getId()) {
                                    case R.id.btn_sure:
//                                        File file = new File(list_video.get(index_zp).getFJBDLJ());
                                        File file = new File(list_video.get(index_zp).getFJLJ());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "video/*");
                                        startActivity(intent);
                                        break;
                                    case R.id.btn_cancle:
//                                        FJxx fj_SCFJ = list_video.get(index_zp);
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
