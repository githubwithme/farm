package com.farm.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomDialog_CallTip;

import java.util.HashMap;
import java.util.List;

public class Adapter_UserList extends BaseAdapter
{
    CustomDialog_CallTip custom_calltip;
    String audiopath;
    private Context context;// 运行上下文
    private List<commembertab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    ListItemView listItemView = null;
    commembertab commembertab;

    static class ListItemView
    {
        public TextView tv_username;
        public TextView tv_workarea;
        public Button btn_call;
        public CircleImageView circle_img;
    }

    public Adapter_UserList(Context context, List<commembertab> data)
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
        commembertab = listItems.get(position);
        // 自定义视图
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_userlist, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.btn_call = (Button) convertView.findViewById(R.id.btn_call);
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            listItemView.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            listItemView.tv_workarea = (TextView) convertView.findViewById(R.id.tv_workarea);
            listItemView.btn_call.setTag(position);
            listItemView.btn_call.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showDialog_addsaleinfo(listItems.get(Integer.valueOf(v.getTag().toString())).getuserCell());
                }
            });

            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        // 设置文字和图片

        listItemView.tv_username.setText(commembertab.getrealName());
        if (commembertab.getimgurl().equals(""))
        {
            listItemView.circle_img.setImageResource(R.drawable.noimg);
        } else
        {
            String url = AppConfig.baseurl + commembertab.getimgurl();
            BitmapHelper.setImageViewBackground(context, listItemView.circle_img, url);
        }

        listItemView.tv_workarea.setText(commembertab.getparkName() + commembertab.getareaName());

        return convertView;
    }

    public void showDialog_addsaleinfo(final String phone)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_calltip, null);
        custom_calltip = new CustomDialog_CallTip(context, R.style.MyDialog, dialog_layout);
        TextView tv_tips = (TextView) dialog_layout.findViewById(R.id.tv_tips);
        tv_tips.setText(phone + "拨打这个电话吗?");
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                custom_calltip.dismiss();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                custom_calltip.dismiss();
            }
        });
        custom_calltip.show();
    }

}