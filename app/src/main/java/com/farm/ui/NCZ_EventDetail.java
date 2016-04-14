package com.farm.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    ReportedBean reportedBean;
    MyDialog myDialog;
    @ViewById
    LinearLayout ll_picture;
    @ViewById
    LinearLayout ll_video;
    List<FJ_SCFJ> list_picture = new ArrayList<FJ_SCFJ>();
    List<FJ_SCFJ> list_video = new ArrayList<FJ_SCFJ>();
    List<FJ_SCFJ> list_allfj = new ArrayList<FJ_SCFJ>();
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
            for(int i=0;i<reportedBean.getFjxx().size();i++)
            {
                addServerPicture(reportedBean.getFjxx().get(i));
            }
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
        BitmapHelper.setImageView(getActivity(), imageView, AppConfig.baseurl + flview.getFJLJ());

        FJ_SCFJ fj_SCFJ = new FJ_SCFJ();
//            fj_SCFJ.setFJBDLJ(FJBDLJ);
        fj_SCFJ.setFJLX("1");
        ll_picture.addView(imageView);
        list_picture.add(fj_SCFJ);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index_zp = ll_picture.indexOfChild(v);
                View dialog_layout = (LinearLayout)getActivity(). getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new MyDialog.CustomDialogListener() {
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
