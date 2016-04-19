package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by user on 2016/4/11.
 */
@EActivity(R.layout.pg_evendetail)
public class PG_EventDetail extends Activity {

    String[] imgurls;
    MyDialog myDialog;

    @ViewById
    RelativeLayout rl_match;
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
    commembertab commembertab;
    CustomDialog_ListView customDialog_listView;
    FragmentCallBack_AddPlantObservation fragmentCallBack = null;
    String zzsl = "";
    ReportedBean reportedBean;
    @ViewById
    TextView tv_reported;
    @ViewById
    TextView tv_time;
    @ViewById
    TextView tv_type;
    @ViewById
    EditText et_sjms;
    @ViewById
    Button tv_bianjie;
    @ViewById
    Button tv_delete;
    @ViewById
    Button btn_save;
    @ViewById
    LinearLayout ll_add;
    @ViewById
    TableRow tablie2;
    @ViewById
    TableRow tablie1;
@ViewById
View view_line1;
    @ViewById
View view_line2;
    @Click
    void imgbtn_back()
    {
        finish();
    }
    @Click
    void tv_bianjie()
    {

        ll_add.setVisibility(View.VISIBLE);
        imgbtn_addpicture.setVisibility(View.VISIBLE);
        imgbtn_addvideo.setVisibility(View.VISIBLE);
        tablie2.setVisibility(View.GONE);
        tablie1.setVisibility(View.GONE);
        view_line2.setVisibility(View.GONE);
        view_line1.setVisibility(View.GONE);
        tv_bianjie.setVisibility(View.GONE);
        tv_delete.setVisibility(View.GONE);
        tv_type.setEnabled(true);
        et_sjms.setEnabled(true);
    }
    @Click
    void btn_case() {
        ll_add.setVisibility(View.GONE);
        imgbtn_addpicture.setVisibility(View.GONE);
        imgbtn_addvideo.setVisibility(View.GONE);
        tablie2.setVisibility(View.VISIBLE);
        tablie1.setVisibility(View.VISIBLE);
        view_line2.setVisibility(View.VISIBLE);
        view_line1.setVisibility(View.VISIBLE);
        tv_bianjie.setVisibility(View.VISIBLE);
        tv_delete.setVisibility(View.VISIBLE);
        tv_type.setEnabled(false);
        et_sjms.setEnabled(false);
    }
    @Click
    void tv_delete()
    {

//        delete();
        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(PG_EventDetail.this, R.style.MyDialog, dialog_layout, "事件删除", "是否确认删除?", "确认", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        delete();
                        break;
                    case R.id.btn_cancle:

                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }





    @AfterViews
    void aftercreate() {
        if(!reportedBean.getEventType().equals("0"))
        {
            tv_bianjie.setVisibility(View.GONE);
            tv_delete.setVisibility(View.GONE);
        }


        tv_type.setEnabled(false);
        et_sjms.setEnabled(false);



        tv_reported.setText(reportedBean.getReportor());
//        tv_reported.setText(reportedBean.getReportorId() + "-" + reportedBean.getReportor());
        tv_time.setText(reportedBean.getReporTime());
        tv_type.setText(reportedBean.getEventType());
        et_sjms.setText(reportedBean.getEventContent());


        if(!reportedBean.getFjxx().equals(""))
        {
           for(int i=0;i<reportedBean.getFjxx().size();i++)
           {
               if (reportedBean.getFjxx().get(i).getFJLX().equals("1")) {
                   addServerPicture(reportedBean.getFjxx().get(i));
               }
               if (reportedBean.getFjxx().get(i).getFJLX().equals("2")) {
                   ProgressBar progressBar = new ProgressBar(PG_EventDetail.this, null, android.R.attr.progressBarStyleHorizontal);
                   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, LayoutParams.MATCH_PARENT, 0);
                   lp.setMargins(25, 4, 0, 4);
                   progressBar.setLayoutParams(lp);
                   ll_video.addView(progressBar);// BitmapHelper.setImageView(PG_EventDetail.this, imageView, AppConfig.baseurl +flview.getFJLJ());
                   downloadVideo(reportedBean.getFjxx().get(i), AppConfig.baseurl + reportedBean.getFjxx().get(i).getFJLJ(), AppConfig.DOWNLOADPATH_VIDEO + reportedBean.getFjxx().get(i).getFJMC(), progressBar);
               }
               rl_match.setVisibility(View.GONE);
           }

        }else
        {
            rl_match.setVisibility(View.GONE);
        }
    }

    @Click
    void tv_type() {

        JSONObject jsonObject = utils.parseJsonFile(PG_EventDetail.this, "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("Happen"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.getString(i));
        }
        showDialog_workday(list);
    }

    @Click
    void btn_save() {
        if (tv_type.getText().toString().equals("") || et_sjms.getText().toString().equals("")) {
            Toast.makeText(PG_EventDetail.this, "请先填选相关信息！", Toast.LENGTH_SHORT).show();

        } else {
            btn_save.setVisibility(View.INVISIBLE);
            saveData();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        reportedBean = getIntent().getParcelableExtra("reportedBean");
        commembertab = AppContext.getUserInfo(this);
    }


    public void showDialog_workday(List<String> list) {
        View dialog_layout = (RelativeLayout) PG_EventDetail.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(PG_EventDetail.this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener() {
            @Override
            public void OnClick(Bundle bundle) {
                zzsl = bundle.getString("name");
                tv_type.setText(zzsl);
            }
        });
        customDialog_listView.show();
    }

    private void saveData() {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("eventId", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "eventRecordEd");
        params.addQueryStringParameter("state", "0");
        params.addQueryStringParameter("eventId", reportedBean.getEventId());

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
                        AppContext.makeToast(PG_EventDetail.this, "error_connectDataBase");
                    } else {
                        String event = listData.get(0).getEventId();

                    }

                } else {
                    btn_save.setVisibility(View.VISIBLE);
                    AppContext.makeToast(PG_EventDetail.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(PG_EventDetail.this, "error_connectServer");
            }
        });
    }

    public void delete() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "EventDelete");
        params.addQueryStringParameter("eventId", reportedBean.getEventId());

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<ReportedBean> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {

                    PG_EventDetail.this.finish();
               /*     listData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);
                    if (listData == null) {
                        AppContext.makeToast(PG_EventDetail.this, "error_connectDataBase");
                    } else {
                        String event = listData.get(0).getEventId();

                    }
*/
                } else {
                    AppContext.makeToast(PG_EventDetail.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(PG_EventDetail.this, "error_connectServer");
            }
        });
    }


    private void addServerPicture(FJxx flview) {

            ImageView imageView = new ImageView(PG_EventDetail.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, LayoutParams.MATCH_PARENT, 0);
            lp.setMargins(25, 4, 0, 4);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        BitmapHelper.setImageView(PG_EventDetail.this, imageView, AppConfig.url + fj_SCFJ.getFJLJ());// ?
            BitmapHelper.setImageView(PG_EventDetail.this, imageView, AppConfig.baseurl +flview.getFJLJ());

//            FJ_SCFJ fj_SCFJ = new FJ_SCFJ();
//            fj_SCFJ.setFJBDLJ(FJBDLJ);
//            fj_SCFJ.setFJLX("1");
            ll_picture.addView(imageView);
//            list_picture.add(fj_SCFJ);
            list_picture.add(flview);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int index_zp = ll_picture.indexOfChild(v);
                    View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                    myDialog = new MyDialog(PG_EventDetail.this, R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new MyDialog.CustomDialogListener() {
                        @Override
                        public void OnClick(View v) {
                            switch (v.getId()) {
                                case R.id.btn_sure:
                          /*      Intent intent = new Intent(PG_EventDetail.this, ShowPhoto_.class);
                                intent.putExtra("url", list_picture.get(index_zp).getFJLJ());
                                startActivity(intent);*/
                                    File file = new File(list_picture.get(index_zp).getFJLJ());
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "image");
                                    startActivity(intent);
                                    break;
                                case R.id.btn_cancle:
                                deleteFJ(list_picture.get(index_zp).getFJID(), list_picture, ll_picture, index_zp);
//                                    ll_picture.removeViewAt(index_zp);
//                                    list_picture.remove(index_zp);
                                    myDialog.dismiss();
                                    break;
                            }
                        }
                    });
                    myDialog.show();
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
                    Toast.makeText(PG_EventDetail.this, "删除成功！", Toast.LENGTH_SHORT).show();
               /*     listData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);
                    if (listData == null) {
                        AppContext.makeToast(PG_EventDetail.this, "error_connectDataBase");
                    } else {
                        String event = listData.get(0).getEventId();

                    }
*/
                } else {
                    Toast.makeText(PG_EventDetail.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                String a = error.getMessage();
                AppContext.makeToast(PG_EventDetail.this, "error_connectServer");
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
                    ImageView imageView = new ImageView(PG_EventDetail.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, LayoutParams.MATCH_PARENT, 0); // ,
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
                            View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                            myDialog = new MyDialog(PG_EventDetail.this, R.style.MyDialog, dialog_layout, "视频", "查看该视频?", "查看", "删除", new MyDialog.CustomDialogListener()
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
                                            FJxx fj_SCFJ =list_video.get(index_zp);
                                            deleteFJ(list_video.get(index_zp).getFJID(), list_video, ll_video, index_zp);
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
                    Toast.makeText(PG_EventDetail.this, "下载失败！找不到文件!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo)
            {
                // progressBar.setVisibility(View.INVISIBLE);
                ll_video.removeView(progressBar);
                Toast.makeText(PG_EventDetail.this, "下载成功！", Toast.LENGTH_SHORT).show();

                ImageView imageView = new ImageView(PG_EventDetail.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, LayoutParams.MATCH_PARENT, 0);
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
                        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(PG_EventDetail.this, R.style.MyDialog, dialog_layout, "视频", "查看该视频?", "查看", "删除", new MyDialog.CustomDialogListener()
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
                                        FJxx fj_SCFJ =list_video.get(index_zp);
                                        deleteFJ(list_video.get(index_zp).getFJID(), list_video, ll_video, index_zp);
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
