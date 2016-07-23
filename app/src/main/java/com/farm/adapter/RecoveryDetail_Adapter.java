package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.PeopelList;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.ui.RecoveryDetail;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/20.
 */
public class RecoveryDetail_Adapter extends BaseAdapter
{
    private Context context;
    private List<SellOrder_New> listData;
    SellOrder_New sellOrder_new;
    private LayoutInflater layoutInflater;

    public RecoveryDetail_Adapter(Context context, List<SellOrder_New> listData)
    {
        this.context = context;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    class ListItemView
    {
        public EditText CR_chanpin;
        public EditText packPrice;
        public EditText carryPrice;
        public TextView tv_bz;
        public TextView tv_by;
        public Button button_update;
        public Button button_delete;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        sellOrder_new = listData.get(position);
        int num = position + 1;
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            convertView = layoutInflater.inflate(R.layout.recoverydetail_adapter, null);
            listItemView = new ListItemView();
            listItemView.CR_chanpin = (EditText) convertView.findViewById(R.id.CR_chanpin);
            listItemView.packPrice = (EditText) convertView.findViewById(R.id.packPrice);
            listItemView.carryPrice = (EditText) convertView.findViewById(R.id.carryPrice);

            listItemView.tv_bz = (TextView) convertView.findViewById(R.id.tv_bz);
            listItemView.tv_by = (TextView) convertView.findViewById(R.id.tv_by);
            listItemView.button_update = (Button) convertView.findViewById(R.id.button_update);
            listItemView.button_delete = (Button) convertView.findViewById(R.id.button_delete);
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }

        listItemView.CR_chanpin.setText(sellOrder_new.getPlateNumber());
        listItemView.packPrice.setText(sellOrder_new.getPackPrice());
        listItemView.carryPrice.setText(sellOrder_new.getCarryPrice());
        listItemView.tv_bz.setText(sellOrder_new.getPickName());
        listItemView.tv_by.setText(sellOrder_new.getContractorName());
        listItemView.button_delete.setTag(R.id.tag_bean, sellOrder_new);
        listItemView.button_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SellOrder_New sellOrder = new SellOrder_New();
                sellOrder = (SellOrder_New) view.getTag(R.id.tag_bean);
                DeletesellOrderSettlement(sellOrder.getid());
            }
        });
        //"批次号:" +
//        listItemView.tv_yq.setText(wz_storehouse.getBatchName()+"--"+wz_storehouse.getQuantity());
        return convertView;
    }

    @Override
    public int getCount()
    {
        return listData.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        return 0;
    }

    private void DeletesellOrderSettlement(String id)
    {
//        commembertab commembertab = AppContext.getUserInfo(context);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("action", "DeletesellOrderSettlement");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PeopelList> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(AppContext.UPDATEMESSAGE_PGDETAIL_UPDATE_DINGDAN);
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
                String a = error.getMessage();
                AppContext.makeToast(context, "error_connectServer");

            }
        });
    }
}
