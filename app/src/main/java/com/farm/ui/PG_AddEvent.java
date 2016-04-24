package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.FJxx;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.com.custominterface.FragmentCallBack_AddPlantObservation;
import com.farm.common.BitmapHelper;
import com.farm.common.utils;
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
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Created by user on 2016/4/10.
 */
@EActivity(R.layout.pg_event)
public class PG_AddEvent extends Activity {
    commembertab commembertab;
    CountDownLatch latch;
    AppContext appContext;
    MyDialog myDialog;
    CustomDialog_ListView customDialog_listView;
    FragmentCallBack_AddPlantObservation fragmentCallBack = null;
    String zzsl = "";
    @ViewById
    ProgressBar pb_upload;
    @ViewById
    Button btn_upload;
    @ViewById
    ImageView imageview111;
    @ViewById
    EditText tv_type;
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
    List<FJxx> list_picture = new ArrayList<FJxx>();
    List<FJxx> list_video = new ArrayList<FJxx>();
    List<FJxx> list_allfj = new ArrayList<FJxx>();

    @Click
    void btn_upload()
    {

        if( tv_type.getText().toString().equals("")||et_sjms.getText().toString().equals(""))
        {
            Toast.makeText(PG_AddEvent.this, "请先填选相关信息！", Toast.LENGTH_SHORT).show();

        }else
        {
            btn_upload.setVisibility(View.GONE);
            pb_upload.setVisibility(View.VISIBLE);
            saveData();
        }


    }
    @LongClick
    void tv_type()
    {
        JSONObject jsonObject = utils.parseJsonFile(PG_AddEvent.this, "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("Happen"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        showDialog_workday(list);
    }


    @Click
    void imgbtn_addpicture()
    {
        Intent intent = new Intent(PG_AddEvent.this, HomeFragmentActivity.class);
        intent.putExtra("type", "picture");
        startActivity(intent);
    }

    @Click
    void imgbtn_addvideo()
    {
        Intent intent = new Intent(PG_AddEvent.this, HomeFragmentActivity.class);
        intent.putExtra("type", "video");
        startActivity(intent);
    }
    @AfterViews
    void afterOncreate() {


    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        appContext = (AppContext) getApplication();
        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        commembertab = AppContext.getUserInfo(this);


    }
    public void showDialog_workday(List<String> list)
    {
        View dialog_layout = (RelativeLayout) PG_AddEvent.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(PG_AddEvent.this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                zzsl = bundle.getString("name");
                tv_type.setText(zzsl);
            }
        });
        customDialog_listView.show();
    }
    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver()// 植物（0为整体照，1为花照，2为果照，3为叶照）；动物（0为整体照，1为脚印照，2为粪便照）
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            List<String> list = intent.getStringArrayListExtra("list");
            for (int i = 0; i < list.size(); i++)
            {
                String FJBDLJ = list.get(i);
                final ImageView imageView = new ImageView(PG_AddEvent.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageView(PG_AddEvent.this, imageView, FJBDLJ);
                imageView.setTag(FJBDLJ);

                FJxx fj_SCFJ = new FJxx();
                fj_SCFJ.setFJBDLJ(FJBDLJ);
                fj_SCFJ.setFJLX("1");

                list_picture.add(fj_SCFJ);
                list_allfj.add(fj_SCFJ);
                ll_picture.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int index_zp = ll_picture.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(PG_AddEvent.this, R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "取消", "删除", new MyDialog.CustomDialogListener()
                        {
                            @Override
                            public void OnClick(View v)
                            {
                                switch (v.getId())
                                {
                                    case R.id.btn_sure:

                                     /*   File file = new File(list_picture.get(index_zp).getFJBDLJ());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "image");
                                        startActivity(intent);*/
                                 /*       try {
                                            FileInputStream fis = new FileInputStream(list_picture.get(index_zp).getFJBDLJ());
                                            Bitmap bitmap = BitmapFactory.decodeStream(fis);
                                            imageview111 .setImageBitmap(bitmap);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }*/

                                        //只有远程有用
                                  /*      Intent intent = new Intent(PG_AddEvent.this,DisplayImage_.class);
                                        intent.putExtra("url", AppConfig.baseurl+list_picture.get(index_zp).getFJBDLJ());
                                        startActivity(intent);*/
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
    BroadcastReceiver videoBroadcastReceiver = new BroadcastReceiver()// 植物（0为整体照，1为花照，2为果照，3为叶照）；动物（0为整体照，1为脚印照，2为粪便照）
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            List<String> list = intent.getStringArrayListExtra("list");
            for (int i = 0; i < list.size(); i++)
            {
                String FJBDLJ = list.get(i);
                ImageView imageView = new ImageView(PG_AddEvent.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, LayoutParams.MATCH_PARENT, 0);
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

                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int index_zp = ll_video.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(PG_AddEvent.this, R.style.MyDialog, dialog_layout, "图片", "查看该视频?", "查看", "删除", new MyDialog.CustomDialogListener()
                        {
                            @Override
                            public void OnClick(View v)
                            {
                                switch (v.getId())
                                {
                                    case R.id.btn_sure:
                                        File file = new File(list_video.get(index_zp).getFJBDLJ());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "video/*");
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

    private void saveData() {
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
        Date   curDate   =   new Date(System.currentTimeMillis());//获取当前时间
        String   str   =   formatter.format(curDate);
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("eventId", commembertab.getId());
        params.addQueryStringParameter("reportorId", commembertab.getId());
        params.addQueryStringParameter("reportor", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "eventRecordAdd");
        params.addQueryStringParameter("reporTime",str);
        params.addQueryStringParameter("X", appContext.getLOCATION_X());
        params.addQueryStringParameter("Y", appContext.getLOCATION_Y());
        params.addQueryStringParameter("state","0");

        params.addQueryStringParameter("eventType", tv_type.getText().toString());
        params.addQueryStringParameter("eventContent", et_sjms.getText().toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);
                    if (listData == null) {
                        AppContext.makeToast(PG_AddEvent.this, "error_connectDataBase");
                    } else {
                        String event = listData.get(0).getEventId();
                        if (list_allfj.size() > 0 ) {
                            latch = new CountDownLatch(list_allfj.size() );
                            for (int j = 0; j < list_allfj.size(); j++) {
//                                uploadMedia(listData.get(0).getEventId(), list_picture.get(j).getFJBDLJ(),list_picture.get(j).getFJLX());
                                uploadMedia(listData.get(0).getEventId(), list_allfj.get(j).getFJBDLJ(),list_allfj.get(j).getFJLX());
                            }

                        } else {
                            Toast.makeText(PG_AddEvent.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            PG_AddEvent.this.finish();
                        }
                    }

                } else {
                    btn_upload.setVisibility(View.VISIBLE);
                    pb_upload.setVisibility(View.GONE);
                    AppContext.makeToast(PG_AddEvent.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(PG_AddEvent.this, "error_connectServer");
            }
        });
    }
    private void uploadMedia(String eventId, String path,String aa)
    {
        File file = new File(path);
        UUID uuid = UUID.randomUUID();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "UpLoadFileEventImg");
        params.addQueryStringParameter("FJID", uuid.toString());
        params.addQueryStringParameter("SCR", commembertab.getId());
        params.addQueryStringParameter("SCRXM", commembertab.getrealName());
        params.addQueryStringParameter("BZ", "test");
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
                        AppContext.makeToast(PG_AddEvent.this, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(PG_AddEvent.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(PG_AddEvent.this, "error_connectServer");
            }
        });
    }

    private void showProgress()
    {
        latch.countDown();
        Long l = latch.getCount();
        if (l.intValue() == 0) // 全部线程是否已经结束
        {
            Toast.makeText(PG_AddEvent.this, "保存成功！", Toast.LENGTH_SHORT).show();
            PG_AddEvent.this.finish();
        }
    }

 /*   @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack_AddPlantObservation) activity;
    }*/
 public void getGoodsSum(goodslisttab goodslisttab)
 {
     RequestParams params = new RequestParams();
     params.addQueryStringParameter("uid", commembertab.getuId());
     params.addQueryStringParameter("goodsId", goodslisttab.getId());
//        params.addQueryStringParameter("parkId", commembertab.getparkId());
     params.addQueryStringParameter("parkId", "16");
     params.addQueryStringParameter("areaId", commembertab.getareaId());
//        params.addQueryStringParameter("action", "getGoodsSum");
     params.addQueryStringParameter("action", "getGoodsSumAndPlants");
     HttpUtils http = new HttpUtils();
     http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
     {
         @Override
         public void onSuccess(ResponseInfo<String> responseInfo)
         {
             String a = responseInfo.result;
             Result result = JSON.parseObject(responseInfo.result, Result.class);
             if (result.getResultCode() == 1)
             {
                 if (result.getAffectedRows() != 0)
                 {
                     String parkId = result.getRows().getJSONObject(0).getString("parkId");
                     String parkName = result.getRows().getJSONObject(0).getString("parkName");
                     String areaPlants = result.getRows().getJSONObject(0).getString("areaPlants");
                     String jsonarray = result.getRows().getJSONObject(0).getString("goodsSum");


                 } else
                 {
//                        lsitNewData = new ArrayList<Dictionary>();
                 }
             } else
             {
                 AppContext.makeToast(PG_AddEvent.this, "error_connectDataBase");
                 return;
             }
         }

         @Override
         public void onFailure(HttpException error, String msg)
         {
             AppContext.makeToast(PG_AddEvent.this, "error_connectServer");
         }
     });
 }
}
