package com.farm.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.ui.NCZ_DD_SH_Detail_;
import com.farm.ui.NCZ_EditOrder_;
import com.farm.ui.PG_EditOrder_;
import com.farm.ui.PG_SP_EditOreder_;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomDialog_CallTip;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/5.
 */
@SuppressLint("NewApi")
public class PG_NeedAdapter extends BaseAdapter
{

    static String name = "";
    CustomDialog_CallTip custom_calltip;
    MyDialog myDialog;
    String broadcast;
    private Context context;// 运行上下文
    private List<SellOrder_New> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrder_New sellOrder;

    static class ListItemView
    {


        public TextView tv_importance;
        public TextView tv_buyer;
        public TextView tv_price;
        public TextView tv_sum;
        public TextView tv_from;
        public TextView tv_batchtime;
        public TextView tv_parkname;

        public RelativeLayout fl_dynamic;
        public Button btn_pizhun;
        public Button btn_bohui;
        public Button chakan;

        public TextView tv_preparestatus;
        public TextView tv_orderstate;
        public LinearLayout ll_mainpeople;

    }

    public PG_NeedAdapter(Context context, List<SellOrder_New> data, String broadcast)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
        this.broadcast = broadcast;
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
        sellOrder = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.pg_needadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_preparestatus = (TextView) convertView.findViewById(R.id.tv_preparestatus);
            listItemView.tv_orderstate = (TextView) convertView.findViewById(R.id.tv_orderstate);
            listItemView.tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);
            listItemView.tv_buyer = (TextView) convertView.findViewById(R.id.tv_buyer);
            listItemView.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            listItemView.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
            listItemView.tv_from = (TextView) convertView.findViewById(R.id.tv_from);
            listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
            listItemView.fl_dynamic = (RelativeLayout) convertView.findViewById(R.id.fl_dynamic);
            listItemView.tv_importance = (TextView) convertView.findViewById(R.id.tv_importance);
            listItemView.chakan = (Button) convertView.findViewById(R.id.chakan);
            listItemView.btn_bohui = (Button) convertView.findViewById(R.id.btn_bohui);
            listItemView.ll_mainpeople = (LinearLayout) convertView.findViewById(R.id.ll_mainpeople);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);

        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        if (sellOrder.getIsReady().equals("False"))
        {
            listItemView.tv_preparestatus.setText("未就绪");
        } else
        {
            listItemView.tv_preparestatus.setText("就绪");
        }

        listItemView.tv_importance.setText(sellOrder.getMainPepName());

        final SpannableString content = new SpannableString(sellOrder.getPurchaName());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        listItemView.tv_buyer.setText(content);
        listItemView.tv_buyer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog_addsaleinfo(sellOrder.getPurchaTel());
            }
        });

        SpannableString spanStr_buyer = new SpannableString("就绪");
        spanStr_buyer.setSpan(new UnderlineSpan(), 0, spanStr_buyer.length(), 0);

        listItemView.tv_parkname.setText(sellOrder.getParkname());

        if (sellOrder.getFreeDeposit().equals("0"))
        {
            listItemView.tv_batchtime.setText("申请免付定金");
        } else if (sellOrder.getFreeFinalPay().equals("0"))
        {
            listItemView.tv_batchtime.setText("申请免付尾款");
        } else if (sellOrder.getIsNeedAudit().equals("0") && sellOrder.getCreatorid().equals(""))
        {
            listItemView.tv_batchtime.setText("订单申请修改");
        } else
        {
            listItemView.tv_batchtime.setText("自发订单申请");
        }

        if (sellOrder.getSelltype().equals("审批结算"))
        {
            listItemView.tv_batchtime.setText("申请审核结算单");
        }
        //片管修改订单
        listItemView.btn_bohui.setTag(R.id.tag_danxuan, sellOrder);
        listItemView.btn_bohui.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SellOrder_New sellOrdesr = new SellOrder_New();
                sellOrdesr = (SellOrder_New) view.getTag(R.id.tag_danxuan);
                Intent intent = new Intent(context, PG_SP_EditOreder_.class);
                intent.putExtra("bean", sellOrdesr);
                context.startActivity(intent);
            }
        });
        //片管取消订单
        listItemView.chakan.setTag(R.id.tag_cash, sellOrder);
        listItemView.chakan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SellOrder_New sellOrdesr = new SellOrder_New();
                sellOrdesr = (SellOrder_New) view.getTag(R.id.tag_cash);
                showDeleteTip(sellOrdesr.getUuid());
            }
        });


            listItemView.tv_buyer.setText(sellOrder.getPurchaName());
        listItemView.tv_price.setText(sellOrder.getPrice());
        listItemView.tv_from.setText(sellOrder.getProducer());

        if (sellOrder.getActualsumvalues().equals(""))
        {
            listItemView.tv_sum.setText("待反馈");
        } else
        {
            listItemView.tv_sum.setText(sellOrder.getActualsumvalues());
        }


/*        if (sellOrder.getDeposit().equals("0"))
        {
            listItemView.tv_orderstate.setText("等待买家付定金");
        } else
        {
            if (sellOrder.getFinalpayment().equals("0"))
            {
                listItemView.tv_orderstate.setText("等待买家付尾款");
            } else
            {
                listItemView.tv_orderstate.setText("买家已付尾款");
            }
        }*/

        listItemView.tv_orderstate.setText(sellOrder.getSelltype());

        listItemView.fl_dynamic.setTag(R.id.tag_fi,sellOrder.getBuyersPhone());
        listItemView.fl_dynamic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String phone = (String) v.getTag(R.id.tag_fi);
                showDialog_addsaleinfo(phone);
            }
        });
        listItemView.ll_mainpeople.setTag(R.id.tag_czdl, sellOrder.getMainPeoplePhone());
        listItemView.ll_mainpeople.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String phone = (String) v.getTag(R.id.tag_czdl);
                showDialog_addsaleinfo(phone);
            }
        });
        int[] color = new int[]{R.color.bg_ask, R.color.red, R.color.blue, R.color.gray, R.color.green, R.color.bg_work, R.color.blue, R.color.color_orange, R.color.bg_job, R.color.bg_plant, R.color.bg_main, R.color.bg_text_small,};
        if (name.equals(""))
        {
            name += sellOrder.getMainPepole() + ",";
        }

//        for(int i=0;i<position;i++)
        int str = position;


        //                String[] nongzi = commandtab.getnongziName().split(",");

/*        if (name.indexOf(listItems.get(position).getMainPepole()) != -1)
        {
            String[] data = name.split(",");
            for (int j = 0; j < data.length; j++)
            {
                if (data[j].equals(listItems.get(position).getMainPepole()))
                    listItemView.circle_img.setImageResource(color[j % color.length]);
                int x = j % color.length;
            }
        } else
        {
            String[] data = name.split(",");
            name += listItems.get(position).getMainPepole() + ",";
            listItemView.circle_img.setImageResource(color[(data.length) % color.length]);
            int y = (data.length) % color.length;
        }*/
        return convertView;
    }

    private void deleteSellOrderAndDetail(String uuid)
    {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "deleteSellOrderAndDetail");//jobGetList1
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
                    Intent intent = new Intent();
                    intent.setAction(AppContext.UPDATEMESSAGE_FARMMANAGER);
                    context.sendBroadcast(intent);

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

    public void showDialog_addsaleinfo(final String phone)
    {
        final View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_calltip, null);
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

    private void showDeleteTip(final String uuid)
    {

        View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(context, R.style.MyDialog, dialog_layout, "订单", "确定删除吗?", "删除", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        deleteSellOrderAndDetail(uuid);
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void newaddOrder(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "editOrder");
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
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
                        Toast.makeText(context, "已完成审批！", Toast.LENGTH_SHORT).show();

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
}
