package com.farm.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.lidroid.xutils.http.client.entity.FileUploadEntity;
import com.media.HomeFragmentActivity;
import com.media.MediaChooser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Created by user on 2016/4/14.
 */
@EFragment
public class PG_ISAddHandle extends Fragment{
    HandleBean handleBean;//传值
    CountDownLatch latch;
    MyDialog myDialog;
    CustomDialog_ListView customDialog_listView;
    com.farm.bean.commembertab commembertab;
    AppContext appContext;

    @ViewById
    RelativeLayout rl_match;
    @ViewById
    ProgressBar pb_uploaded;
    @ViewById
    TextView et_sjms;
    @ViewById
    Button btn_upload;
    @ViewById
    ImageButton imgbtn_addvideo;
    @ViewById
    ImageButton imgbtn_addpicture;
    @ViewById
    LinearLayout ll_picture;
    @ViewById
    LinearLayout ll_video;
    List<FJxx> list_picture = new ArrayList<FJxx>();
    List<FJxx> list_video = new ArrayList<FJxx>();
    List<FJxx> list_allfj = new ArrayList<FJxx>();
    @Click
    void btn_upload() {
        if( et_sjms.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(), "请先填选相关信息！", Toast.LENGTH_SHORT).show();

        }else
        {
            btn_upload.setVisibility(View.GONE);
            pb_uploaded.setVisibility(View.VISIBLE);
            saveData();
            makeData();
        }
    }
    @AfterViews
    void afteroncreate()
    {

        et_sjms.setText(handleBean.getResult());
        if(!handleBean.getFjxx().equals(""))
        {
            for(int i=0;i<handleBean.getFjxx().size();i++) {
                if (i==(handleBean.getFjxx().size()-1))
                {
                    handleBean.getFjxx().size();
                }
                if (handleBean.getFjxx().get(i).getFJLX().equals("1")) {
                    addServerPicture(handleBean.getFjxx().get(i));
                }
                if (handleBean.getFjxx().get(i).getFJLX().equals("2")) {
                    ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
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
    @Click
    void imgbtn_addpicture() {
        Intent intent = new Intent(getActivity(), HomeFragmentActivity.class);
        intent.putExtra("type", "picture");
        startActivity(intent);
    }

    @Click
    void imgbtn_addvideo() {
        Intent intent = new Intent(getActivity(), HomeFragmentActivity.class);
        intent.putExtra("type", "video");
        startActivity(intent);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pg_isaddhandle, container, false);
        handleBean = getArguments().getParcelable("handleBean");
//        appContext=(AppContext)getContext();//0
//        appContext = (AppContext) getApplication();
        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
       getActivity(). registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        getActivity(). registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }

    BroadcastReceiver videoBroadcastReceiver = new BroadcastReceiver()// 植物（0为整体照，1为花照，2为果照，3为叶照）；动物（0为整体照，1为脚印照，2为粪便照）
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> list = intent.getStringArrayListExtra("list");
            for (int i = 0; i < list.size(); i++) {
                String FJBDLJ = list.get(i);
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setTag(FJBDLJ);
                imageView.setImageBitmap(BitmapHelper.getVideoThumbnail(FJBDLJ, 120, 120, MediaStore.Images.Thumbnails.MICRO_KIND));

                FJxx fj_SCFJ = new FJxx();
                fj_SCFJ.setFJBDLJ(FJBDLJ);
                fj_SCFJ.setFJLX("2");

                list_video.add(fj_SCFJ);
                list_allfj.add(fj_SCFJ);
                ll_video.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int index_zp = ll_video.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "图片", "查看该视频?", "查看", "删除", new MyDialog.CustomDialogListener() {
                            @Override
                            public void OnClick(View v) {
                                switch (v.getId()) {
                                    case R.id.btn_sure:
                                        File file = new File(list_video.get(index_zp).getFJBDLJ());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "video");
                                        startActivity(intent);
                                        break;
                                    case R.id.btn_cancle:
                                        ll_video.removeViewAt(index_zp);
                                        list_video.remove(index_zp);
                                        myDialog.dismiss();
                                        break;
                                }
                            }
                        });
                        myDialog.show();
                    }
                });
            }
        }
    };
    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver()// 植物（0为整体照，1为花照，2为果照，3为叶照）；动物（0为整体照，1为脚印照，2为粪便照）
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> list = intent.getStringArrayListExtra("list");
            for (int i = 0; i < list.size(); i++) {
                String FJBDLJ = list.get(i);
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageView(getActivity(), imageView, FJBDLJ);
                imageView.setTag(FJBDLJ);

                FJxx fj_SCFJ = new FJxx();
                fj_SCFJ.setFJBDLJ(FJBDLJ);
                fj_SCFJ.setFJLX("1");

                list_picture.add(fj_SCFJ);
                list_allfj.add(fj_SCFJ);
                ll_picture.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int index_zp = ll_picture.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "取消", "删除", new MyDialog.CustomDialogListener() {
                            @Override
                            public void OnClick(View v) {
                                switch (v.getId()) {
                                    case R.id.btn_sure:
                                     /*   File file = new File(list_picture.get(index_zp).getFJBDLJ());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "image");
                                        startActivity(intent);*/
                                        myDialog.dismiss();
                                        break;
                                    case R.id.btn_cancle:
                                        ll_picture.removeViewAt(index_zp);
                                        list_picture.remove(index_zp);
                                        myDialog.dismiss();
                                        break;
                                }
                            }
                        });
                        myDialog.show();
                    }
                });
            }
        }
    };
    private void saveData() {
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
        Date curDate   =   new Date(System.currentTimeMillis());//获取当前时间
        String   str   =   formatter.format(curDate);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "eventHandleEd");
        params.addQueryStringParameter("eventId",handleBean.getEventId());
        params.addQueryStringParameter("id", handleBean.getResultId());
        params.addQueryStringParameter("state","1");
        params.addQueryStringParameter("solveId",handleBean.getSolveId());
        params.addQueryStringParameter("solveName",handleBean.getSolveName());

        params.addQueryStringParameter("result", et_sjms.getText().toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<HandleBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), HandleBean.class);
                    if (listData == null) {
                        AppContext.makeToast(getActivity(), "error_connectDataBase");
                    } else {
                        if (list_allfj.size() > 0 )
                        {
                            latch = new CountDownLatch(list_allfj.size() );
                            for (int j = 0; j < list_allfj.size(); j++)
                            {
                                uploadMedia(handleBean.getResultId(), list_allfj.get(j).getFJBDLJ(),list_allfj.get(j).getFJLX());
//                                uploadMedia(event, list_picture.get(j).getFJBDLJ());
                            }

                        } else {
                            Toast.makeText(getActivity(),"保存成功！", Toast.LENGTH_SHORT).show();
                             getActivity().finish();
                        }
                    }

                } else {
                    btn_upload.setVisibility(View.VISIBLE);
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {

                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }
    private void uploadMedia(String eventId, String path,String aa)
    {
        File file = new File(path);
        UUID uuid = UUID.randomUUID();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "UpLoadFileResultImg");
        params.addQueryStringParameter("FJID", uuid.toString());
        params.addQueryStringParameter("SCR", commembertab.getId());
        params.addQueryStringParameter("SCRXM", commembertab.getrealName());
        params.addQueryStringParameter("BZ", "");
        params.addQueryStringParameter("GLID", eventId);
        params.addQueryStringParameter("FJLX", aa);
//        params.addQueryStringParameter("imagename", plantId);s
        params.addQueryStringParameter("FJMC", file.getName());
        params.setBodyEntity(new FileUploadEntity(file, "text/html"));
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
        http.configSoTimeout(60000);
        http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        showProgress();
                    } else
                    {
                        AppContext.makeToast(getActivity(), "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {

                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void showProgress()
    {
        latch.countDown();
        Long l = latch.getCount();
        if (l.intValue() == 0) // 全部线程是否已经结束
        {
            Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    private void makeData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "eventRecordEd");
        params.addQueryStringParameter("state", "2");
        params.addQueryStringParameter("eventId", handleBean.getEventId());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
//                    Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                } else {
                    btn_upload.setVisibility(View.VISIBLE);
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void addServerPicture(FJxx flview)
    {

        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.setMargins(25, 4, 0, 4);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        BitmapHelper.setImageView(PG_EventDetail.this, imageView, AppConfig.url + fj_SCFJ.getFJLJ());// ?
        BitmapHelper.setImageView(getActivity(), imageView, AppConfig.baseurl + flview.getFJLJ());

        FJxx fj_SCFJ = new FJxx();
//            fj_SCFJ.setFJBDLJ(FJBDLJ);
        fj_SCFJ.setFJLX("1");
        ll_picture.addView(imageView);
        list_picture.add(flview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index_zp = ll_picture.indexOfChild(v);
                View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new MyDialog.CustomDialogListener() {
                    @Override
                    public void OnClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_sure:
                          /*      Intent intent = new Intent(PG_EventDetail.this, ShowPhoto_.class);
                                intent.putExtra("url", list_picture.get(index_zp).getFJLJ());
                                startActivity(intent);*/
                               /* File file = new File(list_picture.get(index_zp).getFJBDLJ());
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "image");
                                startActivity(intent);*/
                                Intent intent = new Intent(getActivity(), DisplayImage_.class);
                                intent.putExtra("url", AppConfig.baseurl + list_picture.get(index_zp).getFJLJ());
                                startActivity(intent);
                                break;
                            case R.id.btn_cancle:
                                deleteFJ(list_picture.get(index_zp).getFJID(), list_picture, ll_picture, index_zp);
                             /*   ll_picture.removeViewAt(index_zp);
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
    public void deleteFJ(String FJID,final List<FJxx> list_pic, final LinearLayout ll_pic, final int index_zp) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "UpLoadDeleteFJ");
        params.addQueryStringParameter("FJID", FJID);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {

                    ll_pic.removeViewAt(index_zp);
                    list_pic.remove(index_zp);
                    Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
               /*     listData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);
                    if (listData == null) {
                        AppContext.makeToast(PG_EventDetail.this, "error_connectDataBase");
                    } else {
                        String event = listData.get(0).getEventId();

                    }
*/
                } else {
                    Toast.makeText(getActivity(), "删除失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }
}
