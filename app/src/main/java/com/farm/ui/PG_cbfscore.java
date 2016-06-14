package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.bean.FJxx;
import com.farm.bean.contractTab;

import com.farm.R;
import com.farm.common.BitmapHelper;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDialog;
import com.media.HomeFragmentActivity;
import com.media.MediaChooser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/6/13.
 */
@EActivity(R.layout.pg_cbfscore)
public class PG_cbfscore extends Activity
{
    MyDialog myDialog;
    CustomDialog_ListView customDialog_listView;
    contractTab contracttab;
    List<FJxx> list_picture = new ArrayList<FJxx>();
    @ViewById
    LinearLayout ll_picture;
    @ViewById
    TextView tv_title;

    @Click
    void imgbtn_addpicture()
    {
        Intent intent = new Intent(PG_cbfscore.this, HomeFragmentActivity.class);
        intent.putExtra("type", "picture");
        startActivity(intent);
    }
    @AfterViews
    void afterview()
    {
        tv_title.setText(contracttab.getContractNum());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contracttab=getIntent().getParcelableExtra("contracttab");
        getActionBar().hide();
        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);

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
                final ImageView imageView = new ImageView(PG_cbfscore.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageView(PG_cbfscore.this, imageView, FJBDLJ);
                imageView.setTag(FJBDLJ);

                FJxx fj_SCFJ = new FJxx();
                fj_SCFJ.setFJBDLJ(FJBDLJ);
                fj_SCFJ.setFJLX("1");

                list_picture.add(fj_SCFJ);
                ll_picture.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int index_zp = ll_picture.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(PG_cbfscore.this, R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new MyDialog.CustomDialogListener()
                        {
                            @Override
                            public void OnClick(View v)
                            {
                                switch (v.getId())
                                {
                                    case R.id.btn_sure:

                                  /*      File file = new File(list_picture.get(index_zp).getFJBDLJ());
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), "image");
                                        startActivity(intent);*/
                                        Intent intent = new Intent(PG_cbfscore.this,DisplayImageOfLocal_.class);
                                        intent.putExtra("url", list_picture.get(index_zp).getFJBDLJ());
                                        String aa=list_picture.get(index_zp).getFJBDLJ();
                                        startActivity(intent);

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
}
