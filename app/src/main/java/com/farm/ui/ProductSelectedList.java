package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.widget.CustomDialog_EditOrderDetail;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2016/7/30.
 */
@EActivity(R.layout.productselectedlist)
public class ProductSelectedList extends Activity
{
    Adapter_ProductList adapterproductlist;
    List<SellOrderDetail_New> list_orderDetail;
    String saleid;
    @ViewById
    ListView lv;

    @AfterViews
    void AfterOncreate()
    {
        getActionBar().hide();
//        saleid = getIntent().getStringExtra("saleid");
        getsellOrderDetailBySaleId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void getsellOrderDetailBySaleId()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleId", "747bfeea-96af-40b4-a9e8-2d3bb7dec83f");
        params.addQueryStringParameter("action", "getsellOrderDetailBySaleId");
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
                        list_orderDetail = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
                        int allnumber = 0;
                        for (int i = 0; i < list_orderDetail.size(); i++)
                        {
                            if (!list_orderDetail.get(i).getplannumber().equals(""))
                                allnumber = allnumber + Integer.valueOf(list_orderDetail.get(i).getplannumber());
                        }
                    } else
                    {
                        list_orderDetail = new ArrayList<SellOrderDetail_New>();
                    }
                    if (adapterproductlist == null)
                    {
                        adapterproductlist = new Adapter_ProductList(ProductSelectedList.this);
                        lv.setAdapter(adapterproductlist);
                    } else
                    {
                        adapterproductlist.notifyDataSetChanged();
                    }
                } else
                {
                    AppContext.makeToast(ProductSelectedList.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(ProductSelectedList.this, "error_connectServer");
            }
        });
    }

    public class Adapter_ProductList extends BaseAdapter
    {
        int pos_delete = 0;
        int pos_edit = 0;
        MyDialog myDialog;
        private Activity context;// 运行上下文
        private LayoutInflater listContainer;// 视图容器
        SellOrderDetail_New SellOrderDetail;
        CustomDialog_EditOrderDetail customDialog_editOrderDetaill;
        EditText et_number;

        class ListItemView
        {
//            public TextView tv_area;
            public TextView tv_plannumber;
            public Button btn_editorderdetail;
            public Button btn_deleteorderdetail;

            public TextView tv_yq;
//            public TextView tv_pq;
            public TextView tv_batchtime;
        }

        public Adapter_ProductList(Activity context)
        {
            this.context = context;
            this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        }

        public int getCount()
        {
            return list_orderDetail.size();
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
            SellOrderDetail = list_orderDetail.get(position);
            // 自定义视图
            ListItemView listItemView = null;
            if (lmap.get(position) == null)
            {
                // 获取list_item布局文件的视图
                convertView = listContainer.inflate(R.layout.adapter_productlist, null);
                listItemView = new ListItemView();
                // 获取控件对象
//                listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
                listItemView.tv_plannumber = (TextView) convertView.findViewById(R.id.tv_plannumber);
                listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
                listItemView.btn_editorderdetail = (Button) convertView.findViewById(R.id.btn_editorderdetail);
                listItemView.btn_deleteorderdetail = (Button) convertView.findViewById(R.id.btn_deleteorderdetail);
                listItemView.btn_editorderdetail.setTag(R.id.tag_tv_number, listItemView.tv_plannumber);
                listItemView.btn_editorderdetail.setTag(R.id.tag_postion, position);
                listItemView.btn_editorderdetail.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        TextView TextView = (TextView) v.getTag(R.id.tag_tv_number);
                        pos_edit = (int) v.getTag(R.id.tag_postion);
                        showDialog_editNumber(list_orderDetail.get(pos_edit), TextView);
                    }
                });
                listItemView.btn_deleteorderdetail.setTag(position);
                listItemView.btn_deleteorderdetail.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        pos_delete = (int) v.getTag();
                        showDeleteTip(list_orderDetail.get(pos_delete));
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
            listItemView.tv_batchtime.setText(SellOrderDetail.getBatchTime());
            listItemView.tv_plannumber.setText(SellOrderDetail.getplannumber());
//            listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
            return convertView;
        }


        private void editNumber(final TextView tv_plannumber, final SellOrderDetail_New sellOrderDetail, final String number_new, String number_difference)
        {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("uid", sellOrderDetail.getuid());
            params.addQueryStringParameter("contractid", sellOrderDetail.getcontractid());
            params.addQueryStringParameter("year", sellOrderDetail.getYear());
            params.addQueryStringParameter("uuid", sellOrderDetail.getUuid());
            params.addQueryStringParameter("batchTime", sellOrderDetail.getBatchTime());
            params.addQueryStringParameter("number_difference", number_difference);
            params.addQueryStringParameter("number_new", number_new);
            params.addQueryStringParameter("action", "editOrderDetail");
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
                        if (result.getAffectedRows() == 1)
                        {
                            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
                            tv_plannumber.setText(number_new);
                            list_orderDetail.get(pos_edit).setplannumber(number_new);
                            getsellOrderDetailBySaleId();
                        } else
                        {
                            Toast.makeText(context, "改承包区该批次剩余株数不足！", Toast.LENGTH_SHORT).show();
                        }

                    } else
                    {
                        Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(HttpException error, String msg)
                {
                    AppContext.makeToast(context, "error_connectServer");
                }
            });
        }


        private void showDeleteTip(final SellOrderDetail_New sellOrderDetail_new)
        {
            View dialog_layout = context.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
            myDialog = new MyDialog(context, R.style.MyDialog, dialog_layout, "订单", "确定删除吗?", "删除", "取消", new MyDialog.CustomDialogListener()
            {
                @Override
                public void OnClick(View v)
                {
                    switch (v.getId())
                    {
                        case R.id.btn_sure:
                            deleteOrderDetail(sellOrderDetail_new.getplannumber(), sellOrderDetail_new.getUuid(), sellOrderDetail_new.getuid(), sellOrderDetail_new.getcontractid(), sellOrderDetail_new.getYear(), sellOrderDetail_new.getBatchTime());
                            break;
                        case R.id.btn_cancle:
                            myDialog.cancel();
                            break;
                    }
                }
            });
            myDialog.show();
        }

        private void deleteOrderDetail(String number_difference, String uuid, String uid, String contractid, String year, String batchTime)
        {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("number_difference", number_difference);
            params.addQueryStringParameter("uuid", uuid);
            params.addQueryStringParameter("uid", uid);
            params.addQueryStringParameter("contractid", contractid);
            params.addQueryStringParameter("year", year);
            params.addQueryStringParameter("batchTime", batchTime);
            params.addQueryStringParameter("action", "deleteOrderDetail");
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
                            list_orderDetail.remove(pos_delete);
                            getsellOrderDetailBySaleId();
                        } else
                        {
                            Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        AppContext.makeToast(context, "error_connectDataBase");
                        return;
                    }
                    myDialog.cancel();
                }

                @Override
                public void onFailure(HttpException error, String msg)
                {
                    AppContext.makeToast(context, "error_connectServer");
                }
            });
        }

        private void showDialog_editNumber(final SellOrderDetail_New sellOrderDetail_new, final TextView textView)
        {
            final View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_editorderdetail, null);
            customDialog_editOrderDetaill = new CustomDialog_EditOrderDetail(context, R.style.MyDialog, dialog_layout);
            et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
            Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
            Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
            btn_sure.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    customDialog_editOrderDetaill.dismiss();
                    int number_difference = Integer.valueOf(sellOrderDetail_new.getplannumber()) - Integer.valueOf(et_number.getText().toString());
                    editNumber(textView, sellOrderDetail_new, et_number.getText().toString(), String.valueOf(number_difference));
                }
            });
            btn_cancle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    customDialog_editOrderDetaill.dismiss();
                }
            });
            customDialog_editOrderDetaill.show();
        }
    }
}
