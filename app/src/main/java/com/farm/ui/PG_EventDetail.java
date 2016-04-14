package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    TextView et_sjms;
    @ViewById
    TextView tv_bianjie;
    @ViewById
    TextView tv_delete;
    @ViewById
    Button btn_save;

    @Click
    void imgbtn_back()
    {
        finish();
    }
    @Click
    void tv_delete()
    {

        delete();
    }



    @Click
    void btn_case() {

    }

    @AfterViews
    void aftercreate() {
//        tv_reported.setText(reportedBean.getReportor());
        tv_reported.setText(reportedBean.getReportorId() + "-" + reportedBean.getReportor());
        tv_time.setText(reportedBean.getReporTime());
        tv_type.setText(reportedBean.getEventType());
//       et_sjms.setText(reportedBean.getEventContent());
        et_sjms.setText(reportedBean.getEventContent());

     /*   if (!reportedBean.getImageUrl().equals(""))
        addServerPicture(reportedBean.getImageUrl());*/
        if(!reportedBean.getFjxx().equals(""))
        {
           for(int i=0;i<reportedBean.getFjxx().size();i++)
           {
               addServerPicture(reportedBean.getFjxx().get(i));
           }
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

            FJ_SCFJ fj_SCFJ = new FJ_SCFJ();
//            fj_SCFJ.setFJBDLJ(FJBDLJ);
            fj_SCFJ.setFJLX("1");
            ll_picture.addView(imageView);
            list_picture.add(fj_SCFJ);
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
                                    File file = new File(list_picture.get(index_zp).getFJBDLJ());
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "image");
                                    startActivity(intent);
                                    break;
                                case R.id.btn_cancle:
//                                deletePhotos(list_picture.get(index_zp).getFJID(), list_picture, ll_picture, index_zp);
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
