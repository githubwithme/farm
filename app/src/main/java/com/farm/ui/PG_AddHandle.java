package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.HandleBean;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
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

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by user on 2016/4/12.
 */
@EActivity(R.layout.pg_addhandle)
public class PG_AddHandle extends Activity {
    HandleBean handleBean;
    CountDownLatch latch;
    MyDialog myDialog;
    CustomDialog_ListView customDialog_listView;
    com.farm.bean.commembertab commembertab;
    AppContext appContext;
    @ViewById
    TextView et_sjms;
    @ViewById
    ImageButton imgbtn_addvideo;
    @ViewById
    ImageButton imgbtn_addpicture;
    @ViewById
    LinearLayout ll_picture;
    @ViewById
    LinearLayout ll_video;
    List<FJ_SCFJ> list_picture = new ArrayList<FJ_SCFJ>();
    List<FJ_SCFJ> list_video = new ArrayList<FJ_SCFJ>();
    List<FJ_SCFJ> list_allfj = new ArrayList<FJ_SCFJ>();

    @Click
    void btn_upload() {
        if( et_sjms.getText().toString().equals(""))
        {
            Toast.makeText(PG_AddHandle.this, "请先填选相关信息！", Toast.LENGTH_SHORT).show();

        }else
        {
            saveData();
            makeData();
        }
    }

    @Click
    void imgbtn_addpicture() {
        Intent intent = new Intent(PG_AddHandle.this, HomeFragmentActivity.class);
        intent.putExtra("type", "picture");
        startActivity(intent);
    }

    @Click
    void imgbtn_addvideo() {
        Intent intent = new Intent(PG_AddHandle.this, HomeFragmentActivity.class);
        intent.putExtra("type", "video");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        appContext = (AppContext) getApplication();
        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        handleBean=getIntent().getParcelableExtra("handleBean");
        commembertab = AppContext.getUserInfo(this);
    }

    BroadcastReceiver videoBroadcastReceiver = new BroadcastReceiver()// 植物（0为整体照，1为花照，2为果照，3为叶照）；动物（0为整体照，1为脚印照，2为粪便照）
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> list = intent.getStringArrayListExtra("list");
            for (int i = 0; i < list.size(); i++) {
                String FJBDLJ = list.get(i);
                ImageView imageView = new ImageView(PG_AddHandle.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setTag(FJBDLJ);
                imageView.setImageBitmap(BitmapHelper.getVideoThumbnail(FJBDLJ, 120, 120, MediaStore.Images.Thumbnails.MICRO_KIND));

                FJ_SCFJ fj_SCFJ = new FJ_SCFJ();
                fj_SCFJ.setFJBDLJ(FJBDLJ);
                fj_SCFJ.setFJLX("2");

                list_video.add(fj_SCFJ);
                list_allfj.add(fj_SCFJ);
                ll_video.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int index_zp = ll_video.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(PG_AddHandle.this, R.style.MyDialog, dialog_layout, "图片", "查看该视频?", "查看", "删除", new MyDialog.CustomDialogListener() {
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
                ImageView imageView = new ImageView(PG_AddHandle.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageView(PG_AddHandle.this, imageView, FJBDLJ);
                imageView.setTag(FJBDLJ);

                FJ_SCFJ fj_SCFJ = new FJ_SCFJ();
                fj_SCFJ.setFJBDLJ(FJBDLJ);
                fj_SCFJ.setFJLX("1");

                list_picture.add(fj_SCFJ);
                list_allfj.add(fj_SCFJ);
                ll_picture.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int index_zp = ll_picture.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(PG_AddHandle.this, R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new MyDialog.CustomDialogListener() {
                            @Override
                            public void OnClick(View v) {
                                switch (v.getId()) {
                                    case R.id.btn_sure:
                                        File file = new File(list_picture.get(index_zp).getFJBDLJ());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "image");
                                        startActivity(intent);
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
                        AppContext.makeToast(PG_AddHandle.this, "error_connectDataBase");
                    } else {
                        if (list_picture.size() > 0 ) {
                            latch = new CountDownLatch(list_picture.size() );
                            for (int j = 0; j < list_picture.size(); j++) {
                                uploadMedia(handleBean.getResultId(), list_picture.get(j).getFJBDLJ());
//                                uploadMedia(event, list_picture.get(j).getFJBDLJ());
                            }

                        } else {
                            Toast.makeText(PG_AddHandle.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            PG_AddHandle.this.finish();
                        }
                    }

                } else {
                    AppContext.makeToast(PG_AddHandle.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(PG_AddHandle.this, "error_connectServer");
            }
        });
    }
    private void uploadMedia(String eventId, String path)
    {
        File file = new File(path);

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "UpLoadFileResultImg");
        params.addQueryStringParameter("resultId", eventId);
//        params.addQueryStringParameter("imagename", plantId);
        params.addQueryStringParameter("file", file.getName());
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
                        AppContext.makeToast(PG_AddHandle.this, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(PG_AddHandle.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(PG_AddHandle.this, "error_connectServer");
            }
        });
    }

    private void showProgress()
    {
        latch.countDown();
        Long l = latch.getCount();
        if (l.intValue() == 0) // 全部线程是否已经结束
        {
            Toast.makeText(PG_AddHandle.this, "保存成功！", Toast.LENGTH_SHORT).show();
            PG_AddHandle.this.finish();
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
                    Toast.makeText(PG_AddHandle.this, "保存成功！", Toast.LENGTH_SHORT).show();
                } else {
                    AppContext.makeToast(PG_AddHandle.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(PG_AddHandle.this, "error_connectServer");
            }
        });
    }
}
