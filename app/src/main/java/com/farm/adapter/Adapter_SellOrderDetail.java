package com.farm.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.commembertab;
import com.farm.widget.MyDialog;
import com.farm.widget.MyDialog.CustomDialogListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.List;

public class Adapter_SellOrderDetail extends BaseAdapter
{
    private Activity context;// 运行上下文
    private List<SellOrderDetail> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrderDetail SellOrderDetail;

    static class ListItemView
    {
        public TextView tv_number;
        public TextView tv_area;

        public TextView tv_yq;
        public TextView tv_pq;
    }

    public Adapter_SellOrderDetail(Activity context, List<SellOrderDetail> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }

    public int getCount()
    {
        return listItems.size();
    }

    public Object getItem(int arg0)
    {
        return null;
    }

    public long getItemId(int arg0)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        SellOrderDetail = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_sellorderdetail, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
//            listItemView.btn_delete.setId(position);
//            listItemView.btn_delete.setOnClickListener(new OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    showDeleteTip(listItems.get(v.getId()).getId(), listItems.get(v.getId()).getStatusid());
//                }
//            });
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片
        listItemView.tv_number.setText("出售"+SellOrderDetail.getplannumber()+"株");
        listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
        return convertView;
    }

    private void deleteCmd(String cmdid, String statusID)
    {
        commembertab commembertab = AppContext.getUserInfo(context);
        RequestParams params = new RequestParams();
        // params.addQueryStringParameter("workuserid", workuserid);
        params.addQueryStringParameter("statusID", statusID);
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("comID", cmdid);
        params.addQueryStringParameter("action", "delCommandByID");
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
                        Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                        myDialog.cancel();
                    } else
                    {
                        Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    AppContext.makeToast(context, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(context, "error_connectServer");
            }
        });
    }

    MyDialog myDialog;

    private void showDeleteTip(final String cmdid, final String statusID)
    {
        View dialog_layout = (LinearLayout) context.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(context, R.style.MyDialog, dialog_layout, "图片", "确定删除吗?", "删除", "取消", new CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        deleteCmd(cmdid, statusID);
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