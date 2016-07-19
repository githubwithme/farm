package com.farm.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.ui.NCZ_EditOrder_;
import com.farm.ui.RecoveryDetail_;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomDialog_CallTip;
import com.farm.widget.MyDateMaD;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/6/29.
 */
public class PG_WaitForSettlementAdapter extends BaseAdapter
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
        //        public CircleImageView circle_img;
        public TextView tv_mainpeple;
        public TextView tv_parkname;
        public TextView tv_buyer;
        public TextView tv_orderstate;
        public TextView tv_unpaid;
        //        public TextView tv_price;
//        public TextView tv_sum;
//        public TextView tv_from;
        public TextView tv_product;
        public Button btn_cancleorder;
        public Button btn_preparework;
        public Button btn_changetime;
        public Button btn_editorder;
        public Button btn_showSettlement;
        public CircleImageView circleImageView;
        public LinearLayout ll_car;
        public LinearLayout ll_mainpeople;
        public LinearLayout ll_undeposit;
        public LinearLayout ll_unfinalpay;
//        public FrameLayout fl_dynamic;

    }

    public PG_WaitForSettlementAdapter(Context context, List<SellOrder_New> data, String broadcast)
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
            convertView = listContainer.inflate(R.layout.adapter_pgwaitforsettlement, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);
            listItemView.tv_buyer = (TextView) convertView.findViewById(R.id.tv_buyer);
            listItemView.tv_unpaid = (TextView) convertView.findViewById(R.id.tv_unpaid);
            listItemView.tv_orderstate = (TextView) convertView.findViewById(R.id.tv_orderstate);
//            listItemView.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
//            listItemView.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
//            listItemView.tv_from = (TextView) convertView.findViewById(R.id.tv_from);
            listItemView.tv_product = (TextView) convertView.findViewById(R.id.tv_product);
            listItemView.btn_cancleorder = (Button) convertView.findViewById(R.id.btn_cancleorder);
            listItemView.btn_preparework = (Button) convertView.findViewById(R.id.btn_preparework);
            listItemView.btn_editorder = (Button) convertView.findViewById(R.id.btn_editorder);
            listItemView.btn_changetime = (Button) convertView.findViewById(R.id.btn_changetime);
            listItemView.btn_showSettlement = (Button) convertView.findViewById(R.id.btn_showSettlement);
//            listItemView.fl_dynamic = (FrameLayout) convertView.findViewById(R.id.fl_dynamic);
            listItemView.tv_mainpeple = (TextView) convertView.findViewById(R.id.tv_mainpeple);
            listItemView.circleImageView = (CircleImageView) convertView.findViewById(R.id.circleImageView);
            listItemView.ll_mainpeople = (LinearLayout) convertView.findViewById(R.id.ll_mainpeople);
            listItemView.ll_undeposit = (LinearLayout) convertView.findViewById(R.id.ll_undeposit);
            listItemView.ll_unfinalpay = (LinearLayout) convertView.findViewById(R.id.ll_unfinalpay);
            listItemView.ll_car = (LinearLayout) convertView.findViewById(R.id.ll_car);
//            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);

            if (sellOrder.getFreeDeposit().equals("1"))
            {
                listItemView.ll_undeposit.setVisibility(View.VISIBLE);
            } else
            {
                listItemView.ll_undeposit.setVisibility(View.GONE);
            }
            if (sellOrder.getFreeFinalPay().equals("1"))
            {
                listItemView.ll_unfinalpay.setVisibility(View.VISIBLE);
            } else
            {
                listItemView.ll_unfinalpay.setVisibility(View.GONE);
            }
            listItemView.tv_parkname.setText(sellOrder.getParkname());
            listItemView.tv_mainpeple.setText(sellOrder.getMainPeople());
//            listItemView.tv_unpaid.setText(sellOrder.getMainPeople());
//            listItemView.tv_car.setText(sellOrder.getProducer());
//            SpannableString content = new SpannableString(sellOrder.getPurchaName());
//            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            listItemView.tv_buyer.setText(sellOrder.getPurchaName());
            listItemView.tv_buyer.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showDialog_addsaleinfo("15989154871");
                }
            });
            listItemView.btn_changetime.setTag(R.id.tag_kg, listItemView);
            listItemView.btn_changetime.setTag(R.id.tag_hg, sellOrder);
            listItemView.btn_changetime.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    SellOrder_New sellOrders = (SellOrder_New) view.getTag(R.id.tag_hg);
                    ListItemView listItemView2 = (ListItemView) view.getTag(R.id.tag_kg);
                    MyDateMaD myDatepicker = new MyDateMaD(context, sellOrders, "1");
                    myDatepicker.getDialog().show();
                }
            });
            //            listItemView.btn_preparework.setTag(R.id.tag_danwei,sellOrder);
            listItemView.btn_preparework.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_danwei);
                    Intent intent = new Intent(context, RecoveryDetail_.class);
//                    intent.putExtra("zbstudio", sellOrder_new);
                    context.startActivity(intent);
                }
            });
            //            listItemView.btn_showSettlement.setTag(R.id.tag_danwei,sellOrder);
            listItemView.btn_showSettlement.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_danwei);
                    Intent intent = new Intent(context, RecoveryDetail_.class);
//                    intent.putExtra("zbstudio", sellOrder_new);
                    context.startActivity(intent);
                }
            });
            //            listItemView.ll_car.setTag(R.id.tag_danwei,sellOrder);
            listItemView.ll_car.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });
//        listItemView.ll_mainpeople.setTag(R.id.tag_fi, listData.get(groupPosition).getDate());
            listItemView.ll_mainpeople.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showDialog_addsaleinfo("15989154871");
                }
            });
            listItemView.tv_product.setText(sellOrder.getGoodsname());
            //下划线就绪
/*            SpannableString spanStr_buyer = new SpannableString("就绪");
            spanStr_buyer.setSpan(new UnderlineSpan(), 0, spanStr_buyer.length(), 0);
            listItemView.tv_batchtime.setText(spanStr_buyer);
            listItemView.tv_batchtime.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, RecoveryDetail_.class);
                    context.startActivity(intent);
                }
            });*/
//            listItemView.tv_buyer.setText(sellOrder.getBuyers());
//            listItemView.tv_price.setText(sellOrder.getPrice());
//            listItemView.tv_from.setText(sellOrder.getProducer());
//            listItemView.tv_batchtime.setText(sellOrder.getBatchTime());
//            if (sellOrder.getActualsumvalues().equals(""))
//            {
//                listItemView.tv_sum.setText("待反馈");
//            } else
//            {
//                listItemView.tv_sum.setText(sellOrder.getActualsumvalues());
//            }
//            if (sellOrder.getSelltype().equals("待付定金"))
//            {
//                listItemView.tv_orderstate.setText("等待买家付定金");
//            } else if (sellOrder.getSelltype().equals("已付定金"))
//            {
//                listItemView.tv_orderstate.setText("已付定金");
//            } else if (sellOrder.getSelltype().equals("待付尾款"))
//            {
//                listItemView.tv_orderstate.setText("等待卖家付尾款");
//            } else if (sellOrder.getSelltype().equals("审核结算单"))
//            {
//                listItemView.tv_orderstate.setText("审批结算");
//            }

/*            if (sellOrder.getDeposit().equals("0"))
            {
                listItemView.tv_state.setText("等待买家付定金");
            } else
            {
                if (sellOrder.getFinalpayment().equals("0"))
                {
                    listItemView.tv_state.setText("等待买家付尾款");
                } else
                {
                    listItemView.tv_state.setText("买家已付尾款");
                }
            }*/
//            listItemView.btn_editorder.setTag(R.id.tag_kg, listItemView);
//            listItemView.btn_editorder.setTag(R.id.tag_hg, sellOrder);
//            listItemView.btn_editorder.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    SellOrder_New sellOrders = (SellOrder_New) view.getTag(R.id.tag_hg);
//                    ListItemView listItemView2 = (ListItemView) view.getTag(R.id.tag_kg);
//                    MyDateMaD myDatepicker = new MyDateMaD(context, listItemView2.tv_name, sellOrders, "1");
//                    myDatepicker.getDialog().show();
//                }
//            });
            listItemView.btn_cancleorder.setTag(R.id.tag_cash, sellOrder);
            listItemView.btn_cancleorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_cash);
                    showDeleteTip(sellOrder_new.getUuid());
//                    deleteSellOrderAndDetail(sellOrder_new.getUuid());
                }
            });
            listItemView.btn_editorder.setTag(R.id.tag_postion, position);
            listItemView.btn_editorder.setTag(R.id.tag_bean, sellOrder);
            listItemView.btn_editorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = (int) v.getTag(R.id.tag_postion);
                    SellOrder_New sellOrder = (SellOrder_New) v.getTag(R.id.tag_bean);
                    Intent intent = new Intent(context, NCZ_EditOrder_.class);
                    intent.putExtra("bean", sellOrder);
                    intent.putExtra("broadcast", broadcast);
                    context.startActivity(intent);
                }
            });
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        if (listItems.get(position).getFlashStr().equals("0"))
        {
            listItemView.circleImageView.setVisibility(View.INVISIBLE);
        } else
        {
            listItemView.circleImageView.setVisibility(View.VISIBLE);
        }
        //
        int[] color = new int[]{R.color.bg_ask, R.color.red, R.color.blue, R.color.gray, R.color.green, R.color.bg_work, R.color.blue, R.color.color_orange, R.color.bg_job, R.color.bg_plant, R.color.bg_main, R.color.bg_text_small,};
        if (name.equals(""))
        {
            name += sellOrder.getMainPepole() + ",";
        }

//        for(int i=0;i<position;i++)
        int str = position;


        //                String[] nongzi = commandtab.getnongziName().split(",");

//        if (name.indexOf(listItems.get(position).getMainPepole()) != -1)
//        {
//            String[] data = name.split(",");
//            for (int j = 0; j < data.length; j++)
//            {
//                if (data[j].equals(listItems.get(position).getMainPepole()))
//                    listItemView.circle_img.setImageResource(color[j % color.length]);
//                int x = j % color.length;
//            }
//        } else
//        {
//            String[] data = name.split(",");
//            name += listItems.get(position).getMainPepole() + ",";
//            listItemView.circle_img.setImageResource(color[(data.length) % color.length]);
//            int y = (data.length) % color.length;
//        }
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
//                        intent.setAction(AppContext.BROADCAST_DD_REFASH);
                    intent.setAction(AppContext.BROADCAST_UPDATEAllORDER);
                    context.sendBroadcast(intent);
               /* if (result.getAffectedRows() != 0)
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);

                } else
                {
                    listData = new ArrayList<SellOrder_New>();
                }*/

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

    private void showDeleteTip(final String uuid)
    {

        View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_callback, null);
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

}
