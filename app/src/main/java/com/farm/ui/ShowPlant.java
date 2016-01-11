package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.planttab;
import com.farm.widget.MyDialog;
import com.farm.widget.MyDialog.CustomDialogListener;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午6:33:24
 * @description :展示动物类
 */
@SuppressLint("NewApi")
@EActivity(R.layout.showplant)
public class ShowPlant extends Activity
{
    TextView tv_wNum;
    TextView tv_yNum;
    TextView tv_hNum;
    TextView tv_zDate;
    TextView tv_yColor;
    TextView tv_xNum;
    TextView tv_cDate;
    LinearLayout ll_video;
    Fragment mContent_container = new Fragment();
    Fragment mContent_container_more = new Fragment();
    planttab planttab;
    TextView tv_description;
    TextView tv_bz;
    OtherFragment otherFragment;
    FoundationFragment foundationFragment;
    TreeFragment treeFragment;
    @ViewById
    Button btn_foundation;
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    Button btn_other;
    @ViewById
    Button btn_video;
    @ViewById
    Button btn_record;
    @ViewById
    Button btn_delete;
    @ViewById
    TextView tv_title;
    @ViewById
    FrameLayout fl_contain;
    @ViewById
    FrameLayout contain_more;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        planttab = (planttab) getIntent().getParcelableExtra("bean");
        otherFragment = new OtherFragment();
        foundationFragment = new FoundationFragment();
        treeFragment = new TreeFragment_();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", planttab);
        treeFragment.setArguments(bundle);
    }

    @AfterViews
    void afterOncreate()
    {
        btn_video.setSelected(true);
        btn_foundation.setSelected(true);
        btn_foundation.setTextColor(getResources().getColor(R.color.black));
        switchcontainer(R.id.fl_contain, mContent_container, foundationFragment);
        switchcontainermore(R.id.contain_more, mContent_container_more, treeFragment);
        show(planttab);
        setImage(planttab.getImgUrl());
    }

    @Click
    void btn_delete()
    {
        showDeleteTip(planttab.getId());
    }

    @Click
    void imgbtn_back()
    {
        finish();
    }

    @Click
    void btn_foundation()
    {
        switchcontainer(R.id.fl_contain, mContent_container, foundationFragment);
        btn_other.setSelected(false);
        btn_foundation.setSelected(true);
        btn_foundation.setTextColor(getResources().getColor(R.color.red));
        btn_other.setTextColor(getResources().getColor(R.color.black));
    }

    private void show(planttab planttab)
    {
        tv_title.setText(planttab.getplantName());
    }

    @Click
    void btn_video()
    {
        switchcontainermore(R.id.contain_more, mContent_container_more, treeFragment);
        btn_record.setSelected(false);
        btn_video.setSelected(true);
    }

    @Click
    void btn_record()
    {
        switchcontainermore(R.id.contain_more, mContent_container_more, treeFragment);
        btn_video.setSelected(false);
        btn_record.setSelected(true);
    }

    @Click
    void btn_other()
    {
        switchcontainer(R.id.fl_contain, mContent_container, otherFragment);
        btn_foundation.setSelected(false);
        btn_other.setSelected(true);
        btn_other.setTextColor(getResources().getColor(R.color.red));
        btn_foundation.setTextColor(getResources().getColor(R.color.black));
    }

    class FoundationFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.foundationfragment_plant, container, false);
            tv_hNum = (TextView) rootView.findViewById(R.id.tv_hNum);
            tv_wNum = (TextView) rootView.findViewById(R.id.tv_wNum);
            tv_yNum = (TextView) rootView.findViewById(R.id.tv_yNum);
            tv_yColor = (TextView) rootView.findViewById(R.id.tv_yColor);
            tv_xNum = (TextView) rootView.findViewById(R.id.tv_xNum);
            tv_cDate = (TextView) rootView.findViewById(R.id.tv_cDate);
            tv_zDate = (TextView) rootView.findViewById(R.id.tv_zDate);

            if (planttab.getGrowthDate().equals(""))
            {
                btn_foundation.setText("暂无观察");
                btn_foundation.setTextColor(getResources().getColor(R.color.red));
                tv_hNum.setText("");
                tv_wNum.setText("");
                tv_yNum.setText("");
                tv_yColor.setText("");
                tv_xNum.setText("");
                tv_cDate.setText("");
                tv_zDate.setText("");
            } else
            {
                btn_foundation.setText(planttab.getregDate().substring(0, planttab.getregDate().lastIndexOf(" ")) + "观察情况");
                btn_foundation.setTextColor(getResources().getColor(R.color.red));
                tv_hNum.setText(planttab.gethNum());
                tv_wNum.setText(planttab.getwNum());
                tv_yNum.setText(planttab.getyNum());
                tv_yColor.setText(planttab.getyColor());
                tv_xNum.setText(planttab.getxNum());
                if (planttab.getcDate().equals("False"))
                {
                    tv_cDate.setText("未抽蕾");
                } else
                {
                    tv_cDate.setText(planttab.getregDate());
                }
                if (planttab.getzDate().equals("False"))
                {
                    tv_zDate.setText("未留芽");
                } else
                {
                    tv_zDate.setText(planttab.getregDate());
                }


            }

            return rootView;
        }
    }

    class OtherFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.otherinfomationfragment_animal, container, false);
            TextView tv_cjr = (TextView) rootView.findViewById(R.id.tv_cjr);
            TextView tv_cjsj = (TextView) rootView.findViewById(R.id.tv_cjsj);
            TextView tv_xgr = (TextView) rootView.findViewById(R.id.tv_xgr);
            TextView tv_xgsj = (TextView) rootView.findViewById(R.id.tv_xgsj);

            tv_cjr.setText(planttab.getCjUserName());
            tv_cjsj.setText(planttab.getGrowthDate());
            tv_xgr.setText(planttab.getCjUserName());
            tv_xgsj.setText(planttab.getregDate());
            return rootView;
        }
    }

    public void switchcontainer(int resource, Fragment from, Fragment to)
    {
        if (mContent_container != to)
        {
            mContent_container = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(resource, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public void switchcontainermore(int resource, Fragment from, Fragment to)
    {
        if (mContent_container_more != to)
        {
            mContent_container_more = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(resource, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private void setImage(List<String> imglist)
    {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < imglist.size(); i++)
        {
            list.add(AppConfig.baseurl + imglist.get(i));
        }
        PictureScrollFragment pictureScrollFragment = new PictureScrollFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imgurl", (ArrayList<String>) list);
        pictureScrollFragment.setArguments(bundle);
        ShowPlant.this.getFragmentManager().beginTransaction().replace(R.id.img_container, pictureScrollFragment).commit();
    }

    private void deleteplant(String plantID)
    {
        commembertab commembertab = AppContext.getUserInfo(ShowPlant.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("plantID", plantID);
        params.addQueryStringParameter("action", "delplantByID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
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
                        Toast.makeText(ShowPlant.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
                        finish();
                    } else
                    {
                        Toast.makeText(ShowPlant.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    AppContext.makeToast(ShowPlant.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(ShowPlant.this, "error_connectServer");
            }
        });
    }

    MyDialog myDialog;

    private void showDeleteTip(final String plantID)
    {
        View dialog_layout = (LinearLayout) ShowPlant.this.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(ShowPlant.this, R.style.MyDialog, dialog_layout, "植株", "确定删除吗?", "删除", "取消", new CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        deleteplant(plantID);
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }
}
