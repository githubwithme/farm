package com.farm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.ui.NCZ_EditOrder_;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class NCZ_OrderAdapter extends BaseAdapter
{
    String broadcast;
    private Context context;// 运行上下文
    private List<SellOrder_New> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrder_New sellOrder;

    static class ListItemView
    {
        public TextView tv_buyer;
        public TextView tv_state;
        public TextView tv_price;
        public TextView tv_sum;
        public TextView tv_from;
        public TextView tv_batchtime;
        public Button btn_cancleorder;
        public Button btn_editorder;
        public FrameLayout fl_dynamic;

    }

    public NCZ_OrderAdapter(Context context, List<SellOrder_New> data,String broadcast)
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
            convertView = listContainer.inflate(R.layout.listitem_order, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_buyer = (TextView) convertView.findViewById(R.id.tv_buyer);
            listItemView.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            listItemView.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            listItemView.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
            listItemView.tv_from = (TextView) convertView.findViewById(R.id.tv_from);
            listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
            listItemView.btn_cancleorder = (Button) convertView.findViewById(R.id.btn_cancleorder);
            listItemView.btn_editorder = (Button) convertView.findViewById(R.id.btn_editorder);
            listItemView.fl_dynamic = (FrameLayout) convertView.findViewById(R.id.fl_dynamic);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);


            listItemView.tv_buyer.setText(sellOrder.getBuyers());
            listItemView.tv_price.setText(sellOrder.getPrice());
            listItemView.tv_from.setText(sellOrder.getProducer());
            listItemView.tv_batchtime.setText(sellOrder.getBatchTime());
            if (sellOrder.getActualsumvalues().equals(""))
            {
                listItemView.tv_sum.setText("待反馈");
            } else
            {
                listItemView.tv_sum.setText(sellOrder.getActualsumvalues());
            }
            if (sellOrder.getDeposit().equals("0"))
            {
                listItemView.tv_state.setText("等待买家付定金");
            } else
            {
                if (sellOrder.getFinalpayment().equals(""))
                {
                    listItemView.tv_state.setText("等待买家付尾款");
                } else
                {
                    listItemView.tv_state.setText("买家已付尾款");
                }
            }
            listItemView.btn_cancleorder.setTag(R.id.tag_cash,sellOrder);
            listItemView.btn_cancleorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SellOrder_New sellOrder_new= (SellOrder_New) v.getTag(R.id.tag_cash);
                    deleteSellOrderAndDetail(sellOrder_new.getUuid());
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
            listItemView.fl_dynamic.setVisibility(View.INVISIBLE);
        }else
        {
            listItemView.fl_dynamic.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private void deleteSellOrderAndDetail(String uuid)
    {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid",uuid );
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
}