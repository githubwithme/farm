package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditOrderDetail;
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
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/20.
 */

/**
 * 1
 */
@EActivity(R.layout.ncz_createorder)
public class NCZ_CreateOrder extends Activity
{
    String batchtime;
    List<SellOrderDetail_New> list_SellOrderDetail;
    Adapter_EditSellOrderDetail_NCZ adapter_sellOrderDetail;
    SellOrderDetail SellOrderDetail;
    @ViewById
    LinearLayout ll_flyl;
    @ViewById
    ListView lv;
    @ViewById
    Button btn_sure;
    @ViewById
    EditText et_values;
    @ViewById
    EditText et_name;
    @ViewById
    EditText et_address;
    @ViewById
    EditText et_price;
    @ViewById
    EditText et_weight;
    @ViewById
    EditText et_phone;
    @ViewById
    EditText et_email;
    @ViewById
    EditText et_note;
    @ViewById
    TextView tv_allnumber;

    @Click
    void btn_sure()
    {
        if (et_name.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_email.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_address.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_phone.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_price.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_weight.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_values.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        String uuid = java.util.UUID.randomUUID().toString();
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder.setid("");
        sellOrder.setUid("60");
        sellOrder.setUuid(uuid);
        sellOrder.setBatchTime(batchtime);
        sellOrder.setSelltype("0");
        sellOrder.setStatus("0");
        sellOrder.setBuyers(et_name.getText().toString());
        sellOrder.setAddress(et_address.getText().toString());
        sellOrder.setEmail(et_email.getText().toString());
        sellOrder.setPhone(et_phone.getText().toString());
        sellOrder.setPrice(et_price.getText().toString());
        sellOrder.setNumber(String.valueOf(countAllNumber()));
        sellOrder.setWeight(et_weight.getText().toString());
        sellOrder.setSumvalues(et_values.getText().toString());
        sellOrder.setActualprice("");
        sellOrder.setActualweight("");
        sellOrder.setActualnumber("");
        sellOrder.setActualsumvalues("");
        sellOrder.setDeposit("0");
        sellOrder.setReg(utils.getTime());
        sellOrder.setSaletime(utils.getTime());
        sellOrder.setYear(utils.getYear());
        sellOrder.setNote(et_note.getText().toString());
        sellOrder.setXxzt("0");
        sellOrder.setProducer("");


        List<String> list_detail = new ArrayList<>();
        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            list_detail.add(list_SellOrderDetail.get(i).getUuid());
        }

        List<SellOrder_New> SellOrderList = new ArrayList<>();
        SellOrderList.add(sellOrder);
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrderList\": ");
        builder.append(JSON.toJSONString(SellOrderList));
        builder.append(", \"SellOrderDetailLists\": ");
        builder.append(JSON.toJSONString(list_detail));
        builder.append("} ");
        addOrder(uuid, builder.toString());
    }

    @AfterViews
    void afterOncreate()
    {
        tv_allnumber.setText("共售" + String.valueOf(countAllNumber()) + "株");
        adapter_sellOrderDetail = new Adapter_EditSellOrderDetail_NCZ(NCZ_CreateOrder.this, list_SellOrderDetail);
        lv.setAdapter(adapter_sellOrderDetail);
        utils.setListViewHeight(lv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        list_SellOrderDetail = getIntent().getParcelableArrayListExtra("list");
    }

    public int countAllNumber()
    {
        int allnumber = 0;
        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            allnumber = allnumber + Integer.valueOf(list_SellOrderDetail.get(i).getplannumber());
        }
        return allnumber;
    }

    private void getListData()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("comID", SellOrderDetail.getUuid());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("page_size", "10");
        params.addQueryStringParameter("page_index", "10");
        params.addQueryStringParameter("action", "commandGetListBycomID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrderDetail_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
                        adapter_sellOrderDetail = new Adapter_EditSellOrderDetail_NCZ(NCZ_CreateOrder.this, listNewData);
                        lv.setAdapter(adapter_sellOrderDetail);
                        utils.setListViewHeight(lv);
                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_CreateOrder.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {

            }
        });
    }


    private void addOrder(String uuid, String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "addOrder");
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
                        Toast.makeText(NCZ_CreateOrder.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateOrder.this, "error_connectServer");
            }
        });
    }

    public class Adapter_EditSellOrderDetail_NCZ extends BaseAdapter
    {
        private Activity context;// 运行上下文
        private List<SellOrderDetail_New> listItems;// 数据集合
        private LayoutInflater listContainer;// 视图容器
        SellOrderDetail_New SellOrderDetail;
        CustomDialog_EditOrderDetail customDialog_editOrderDetaill;
        EditText et_number;

        class ListItemView
        {
            public TextView tv_area;
            public TextView tv_plannumber;
            public TextView tv_yq;
            public TextView tv_pq;
        }

        public Adapter_EditSellOrderDetail_NCZ(Activity context, List<SellOrderDetail_New> data)
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
                convertView = listContainer.inflate(R.layout.adapter_editsellorderdetail_ncz, null);
                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
                listItemView.tv_plannumber = (TextView) convertView.findViewById(R.id.tv_plannumber);
//                listItemView.btn_plannumber.setTag(position);
//                listItemView.btn_plannumber.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        int pos = (int) v.getTag();
//                        showDialog_editNumber(listItems.get(pos));
//                    }
//                });
                // 设置控件集到convertView
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }
            // 设置文字和图片
            listItemView.tv_plannumber.setText(SellOrderDetail.getplannumber());
            listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
            return convertView;
        }


//        private void editPolygon(final SellOrderDetail_New sellOrderDetail, String number_new, String number_difference)
//        {
//            commembertab commembertab = AppContext.getUserInfo(context);
//            RequestParams params = new RequestParams();
//            params.addQueryStringParameter("uid", sellOrderDetail.getuid());
//            params.addQueryStringParameter("contractid", sellOrderDetail.getcontractid());
//            params.addQueryStringParameter("year", sellOrderDetail.getYear());
//            params.addQueryStringParameter("uuid", sellOrderDetail.getUuid());
//            params.addQueryStringParameter("batchTime", sellOrderDetail.getBatchTime());
//            params.addQueryStringParameter("number_difference", number_difference);
//            params.addQueryStringParameter("number_new", number_new);
//            params.addQueryStringParameter("action", "editSellOrderDetail");
//            HttpUtils http = new HttpUtils();
//            http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
//            {
//                @Override
//                public void onSuccess(ResponseInfo<String> responseInfo)
//                {
//                    String a = responseInfo.result;
//                    Result result = JSON.parseObject(responseInfo.result, Result.class);
//                    if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//                    {
//                        String rows = result.getRows().get(0).toString();
//                        if (rows.equals("1"))
//                        {
//                            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
//                        } else if (rows.equals("0"))
//                        {
//                            Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else
//                    {
//                        Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(HttpException error, String msg)
//                {
//                    AppContext.makeToast(context, "error_connectServer");
//                }
//            });
//        }
//
//
//        private void showDialog_editNumber(final SellOrderDetail_New sellOrderDetail_new)
//        {
//            final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_editorderdetail, null);
//            customDialog_editOrderDetaill = new CustomDialog_EditOrderDetail(context, R.style.MyDialog, dialog_layout);
//            et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
//            Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
//            Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
//            btn_sure.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    customDialog_editOrderDetaill.dismiss();
//                    int number_difference = Integer.valueOf(sellOrderDetail_new.getplannumber()) - Integer.valueOf(et_number.getText().toString());
//                    editPolygon(sellOrderDetail_new, et_number.getText().toString(), String.valueOf(number_difference));
//                }
//            });
//            btn_cancle.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    customDialog_editOrderDetaill.dismiss();
//                }
//            });
//            customDialog_editOrderDetaill.show();
//        }
    }
}
