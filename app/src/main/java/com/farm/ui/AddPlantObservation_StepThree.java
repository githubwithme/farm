package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.AddPlantObservationAdapter;
import com.farm.adapter.AddPlantObservation_StepTwo_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.bean.planttab;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.BitmapHelper;
import com.farm.common.utils;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.media.HomeFragmentActivity;
import com.media.MediaChooser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/18.
 */
@EFragment
public class AddPlantObservation_StepThree extends Fragment
{
    List<String> list_picture = new ArrayList<String>();
    MyDialog myDialog;
    @ViewById
    LinearLayout ll_picture;
    @ViewById
    ImageButton imgbtn_addpicture;
    FragmentCallBack fragmentCallBack = null;
    @ViewById
    ListView lv_plant;
    @ViewById
    Button btn_next;
    AddPlantObservation_StepTwo_Adapter addStd_cmd_stepOne_adapter;
    private List<Dictionary> listData = new ArrayList<Dictionary>();
    com.farm.bean.commembertab commembertab;
    Dictionary dic_comm;
    Dictionary_wheel dictionary_wheel;

    @Click
    void btn_next()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 2);
        fragmentCallBack.callbackFun2(bundle);
    }

    @Click
    void imgbtn_addpicture()
    {
        Intent intent = new Intent(getActivity(), HomeFragmentActivity.class);
        intent.putExtra("type", "picture");
        intent.putExtra("From", "plant");
        startActivity(intent);
    }

    @AfterViews
    void afterOncreate()
    {
        getPlantlist();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation__step_three, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        IntentFilter imageIntentFilter_yz = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        getActivity().registerReceiver(imageBroadcastReceiver_yz, imageIntentFilter_yz);
        return rootView;
    }

    BroadcastReceiver imageBroadcastReceiver_yz = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            for (int i = 0; i < intent.getStringArrayListExtra("list").size(); i++)
            {
                String FJBDLJ = intent.getStringArrayListExtra("list").get(i);
                list_picture.add(FJBDLJ);
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageView(getActivity(), imageView, FJBDLJ);

                imageView.setTag(FJBDLJ);
                ll_picture.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int index_zp = ll_picture.indexOfChild(v);
                        View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new MyDialog.CustomDialogListener()
                        {
                            @Override
                            public void OnClick(View v)
                            {
                                switch (v.getId())
                                {
                                    case R.id.btn_sure:
                                        // File file = new
                                        // File(list_SJ_SBXXFJ_picture.get(index_zp).getFJBDLJ());
                                        // Intent intent = new
                                        // Intent(Intent.ACTION_VIEW);
                                        // intent.setDataAndType(Uri.fromFile(file),
                                        // "image/*");
                                        // startActivity(intent);
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

    private void getPlantlist()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaid", "4");
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("strWhere", "");
        params.addQueryStringParameter("page_size", String.valueOf(AppContext.PAGE_SIZE));
        params.addQueryStringParameter("page_index", String.valueOf(0));
        params.addQueryStringParameter("action", "plantGetList");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
                List<planttab> lsitNewData = JSON.parseArray(JSON.parseObject(jsonObject.getString("plantlist"), Result.class).getRows().toJSONString(), planttab.class);
                if (lsitNewData != null)
                {
                    AddPlantObservationAdapter addPlantObservationAdapter = new AddPlantObservationAdapter(getActivity(), lsitNewData);
                    lv_plant.setAdapter(addPlantObservationAdapter);
                }
//                String a = responseInfo.result;
//                List<planttab> lsitNewData = null;
//                Result result = JSON.parseObject(responseInfo.result, Result.class);
//                if (result.getResultCode() == 1)
//                {
//                    if (result.getAffectedRows() != 0)
//                    {
//                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), planttab.class);
//                        if (lsitNewData != null)
//                        {
//                            AddPlantObservationAdapter addPlantObservationAdapter = new AddPlantObservationAdapter(getActivity(), lsitNewData);
//                            lv_plant.setAdapter(addPlantObservationAdapter);
//                        }
//
//                    } else
//                    {
//
//                    }
//                } else
//                {
//                    AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    return;
//                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentCallBack = (AddPlantObservation) activity;
    }
}
