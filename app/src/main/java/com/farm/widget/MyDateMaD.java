package com.farm.widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.SellOrder_New_First;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * Created by hasee on 2016/6/29.
 */
public class MyDateMaD
{
    private TextView mDateDisplay;
    private Button mPickDate;

    private int mYear;
    private int mMonth;
    private int mDay;
    SellOrder_New sellOrders;

    TextView textView = null;
    EditText editText = null;

    static final int DATE_DIALOG_ID = 0;
    Context context;
   private String type;

    public MyDateMaD(Context context, TextView textView,SellOrder_New sellOrders,String type)
    {
        this.context = context;
        this.textView = textView;
        this.sellOrders=sellOrders;
        this.type=type;
    }

    public MyDateMaD(Context context, EditText editText)
    {
        this.context = context;
        this.editText = editText;
    }

    public Dialog getDialog()
    {
        // ��õ�ǰʱ��
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(context, mDateSetListener, mYear, mMonth, mDay);
    }

    // updates the date we display in the TextView
    public void updateDisplay(TextView textView)
    {

        if (type.equals("1"))
        {
            textView.setText(new StringBuilder().append(mMonth + 1).append("/").append(mDay).append(" "));
            sellOrders.setBuyers(sellOrders.getBuyersId());
            sellOrders.setSaletime(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" ").toString());
        }else
        {
            sellOrders.setBuyers(sellOrders.getBuyersId());
            sellOrders.setOldsaletime(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" ").toString());
            sellOrders.setIsNeedAudit("0");
        }

      /*  StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\": [");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("]} ");*/
        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
                    newaddOrder(builder.toString());
    }

    public void updateDisplay(EditText editText)
    {
        editText.setText(new StringBuilder().append(mMonth + 1).append("/").append(mDay).append(" "));
    }

    public DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            if (textView == null)
            {
                updateDisplay(editText);
            } else
            {
                updateDisplay(textView);
            }

        }
    };
    private void newaddOrder( String data)
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
                        Intent intent = new Intent();
//                        intent.setAction(AppContext.BROADCAST_DD_REFASH);
                        intent.setAction(AppContext.BROADCAST_UPDATEAllORDER);
                        context.sendBroadcast(intent);
                    }

                } else
                {
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {

            }
        });
    }
}
