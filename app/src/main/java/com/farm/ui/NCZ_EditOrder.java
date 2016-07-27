package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_CreateSellOrderDetail_NCZ;
import com.farm.adapter.Adapter_New_SellDetail;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.PeopelList;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.SellOrder_New_First;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditOrderDetail;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDatepicker;
import com.farm.widget.MyDialog;
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
@EActivity(R.layout.ncz_editorder)
public class NCZ_EditOrder extends Activity
{
    SellOrder_New sellOrder_new;
    Adapter_New_SellDetail adapter_sellOrderDetail;
    List<SellOrderDetail_New> list_orderDetail;
    MyDialog myDialog;
    CustomDialog_ListView customDialog_listView;
    String zzsl = "";
    List<Purchaser> listData_CG = new ArrayList<Purchaser>();
    List<Purchaser> listData_BY = new ArrayList<Purchaser>();
    List<Purchaser> listData_BZ = new ArrayList<Purchaser>();
    List<PeopelList> listpeople = new ArrayList<PeopelList>();

    String broadcast;
    SellOrder_New sellOrder;
    Adapter_EditSellOrderDetail_NCZ adapter_editSellOrderDetail_ncz;
    SellOrderDetail SellOrderDetail;
    /*    @ViewById
        LinearLayout ll_flyl;*/
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
    /*    @ViewById
        EditText et_number;*/
    @ViewById
    EditText et_phone;
    @ViewById
    EditText et_email;
    @ViewById
    EditText et_note;
    @ViewById
    TextView tv_allnumber;

    //
    @ViewById
    EditText by_danjia;
    @ViewById
    EditText bz_guige;
    @ViewById
    EditText bz_danjia;
    @ViewById
    EditText dingjin;
    @ViewById
    EditText dd_fzr;

    @ViewById
    TextView dd_bz;
    @ViewById
    TextView dd_by;
    @ViewById
    TextView dd_time;
    @ViewById
    EditText chanpin;

    String cgId = "";
    String byId = "";
    String bzId = "";
    String fzrId = "";

    @Click
    void btn_addcg()
    {
        Intent intent = new Intent(NCZ_EditOrder.this, Add_workPeopel_.class);
        intent.putExtra("type", "采购商");
        startActivity(intent);
    }

    @Click
    void btn_addby()
    {
        Intent intent = new Intent(NCZ_EditOrder.this, Add_workPeopel_.class);
        intent.putExtra("type", "搬运工头");
        startActivity(intent);
    }

    @Click
    void btn_addbz()
    {
        Intent intent = new Intent(NCZ_EditOrder.this, Add_workPeopel_.class);
        intent.putExtra("type", "包装工头");
        startActivity(intent);
    }

    @Click
    void dd_fzr()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listpeople.size(); i++)
        {
            listdata.add(listpeople.get(i).getRealName());
            listid.add(listpeople.get(i).getId());
        }
        showDialog_fzr(listdata, listid);
    }

    @Click
//采购商
    void et_name()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_CG.size(); i++)
        {
            listdata.add(listData_CG.get(i).getName());
            listid.add(listData_CG.get(i).getId());
        }
        showDialog_workday(listdata, listid);
    }

    @Click
    void dd_bz()//包装工
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_BZ.size(); i++)
        {
            listdata.add(listData_BZ.get(i).getName());
            listid.add(listData_BZ.get(i).getId());
        }
        showDialog_bz(listdata, listid);
    }

    @Click
        //搬运工
    void dd_by()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_BY.size(); i++)
        {
            listdata.add(listData_BY.get(i).getName());
            listid.add(listData_BY.get(i).getId());
        }
        showDialog_by(listdata, listid);
    }

    @Click
    void btn_sure()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_EditOrder.this);

        if (dd_time.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
   /*     if (et_name.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (et_price.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_weight.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_values.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> list_uuid = new ArrayList<>();
        String batchtime = "";
        String producer = "";
        List<String> list_batchtime = new ArrayList<>();
        List<String> list_producer = new ArrayList<>();
        for (int i = 0; i < list_orderDetail.size(); i++)
        {
            list_uuid.add(list_orderDetail.get(i).getUuid());
            if (i == 0)
            {
                list_batchtime.add(list_orderDetail.get(0).getBatchTime());
                batchtime = batchtime + list_orderDetail.get(0).getBatchTime() + ";";
            }
            if (i == 0)
            {
                //园区名字，注释的是多个园区   目前是单园区，所有另写
//                list_producer.add(list_SellOrderDetail.get(0).getparkname());
//                producer = producer + list_SellOrderDetail.get(0).getparkname() + ";";
                producer=list_orderDetail.get(0).getparkname();
            }
            for (int j = 0; j < list_batchtime.size(); j++)
            {
                if (list_orderDetail.get(i).getBatchTime().equals(list_batchtime.get(j)))
                {
                    break;
                } else if (i == list_batchtime.size() - 1)
                {
                    list_batchtime.add(list_orderDetail.get(i).getBatchTime());
                    batchtime = batchtime + list_orderDetail.get(i).getBatchTime() + ";";
                }
            }
            for (int j = 0; j < list_producer.size(); j++)
            {
                if (list_orderDetail.get(i).getparkname().equals(list_producer.get(j)))
                {
                    break;
                } else if (i == list_producer.size() - 1)
                {
                    //园区名字，注释的是多个园区   目前是单园区，所有另写
//                list_producer.add(list_SellOrderDetail.get(0).getparkname());
//                producer = producer + list_SellOrderDetail.get(0).getparkname() + ";";
                    producer=list_orderDetail.get(0).getparkname();
                }
            }

        }

        SellOrder_New sellOrders = new SellOrder_New();
        sellOrders = sellOrder;
        sellOrders.setUid(commembertab.getuId());
        sellOrders.setUuid(sellOrder.getUuid());
        sellOrders.setGoodsname(chanpin.getText().toString());
        sellOrders.setBatchTime(sellOrder.getBatchTime());
        sellOrders.setBuyers(cgId);
        sellOrders.setAddress(et_address.getText().toString());
        sellOrders.setEmail(et_email.getText().toString());
        sellOrders.setPhone(et_phone.getText().toString());
        sellOrders.setSumvalues(et_values.getText().toString());
/*        sellOrders.setActualprice("");
        sellOrders.setActualweight("");
        sellOrders.setActualnumber("");
        sellOrders.setActualsumvalues("");
        sellOrders.setDeposit("");*/
        sellOrders.setReg(utils.getTime());
//        sellOrder.setSaletime(utils.getTime());

        sellOrders.setYear(utils.getYear());
        sellOrders.setNote(et_note.getText().toString());
        sellOrders.setXxzt("0");
        sellOrders.setProducer(producer);
        sellOrders.setWaitDeposit(dingjin.getText().toString());
//        sellOrders.setMainPepole(fzrId);

        sellOrders.setPrice(et_price.getText().toString());
        sellOrders.setSaletime(dd_time.getText().toString());
        sellOrders.setWeight(et_weight.getText().toString());
        List<SellOrder_New> SellOrderList = new ArrayList<>();
        SellOrderList.add(sellOrder);
        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
/*        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\": [");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("]} ");*/
        newaddOrder(builder.toString());
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
                        Toast.makeText(NCZ_EditOrder.this, "订单修改成功！", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
//                        intent.setAction(AppContext.BROADCAST_DD_REFASH);
                        intent.setAction(AppContext.BROADCAST_UPDATEAllORDER);
                        NCZ_EditOrder.this.sendBroadcast(intent);

                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_EditOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_EditOrder.this, "error_connectServer");
            }
        });
    }
  /*  @Click
    void btn_sure()
    {
        if (et_name.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_email.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_address.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_phone.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_price.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_weight.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
     *//*   if (et_number.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }*//*
        if (et_values.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        editOrder(sellOrder.getUuid(), et_name.getText().toString(), et_address.getText().toString(), et_email.getText().toString(), et_phone.getText().toString(), et_price.getText().toString(), et_weight.getText().toString(), et_values.getText().toString(), et_note.getText().toString());

    }*/

    @AfterViews
    void afterOncreate()
    {

        getsellOrderDetailBySaleId();
        cgId = sellOrder.getBuyers();
        byId = sellOrder.getPickId();
        bzId = sellOrder.getContractorId();
        fzrId = sellOrder.getMainPepole();


        showData();
        getpurchaser();
        getlistdata();


        //计算
        et_price.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (et_price.getText().toString().equals(""))
                {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写单价", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_weight.getText().toString().equals(""))
                {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写重量", Toast.LENGTH_SHORT).show();
                    return;
                }
                double ss = Double.valueOf(et_price.getText().toString()) * Double.valueOf(et_weight.getText().toString());
                et_values.setText(String.format("%.2f", ss));
            }
        });
        et_weight.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (et_price.getText().toString().equals(""))
                {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写单价", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_weight.getText().toString().equals(""))
                {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写重量", Toast.LENGTH_SHORT).show();
                    return;
                }
                double ss = Double.valueOf(et_price.getText().toString()) * Double.valueOf(et_weight.getText().toString());
                et_values.setText(String.format("%.2f", ss));
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder = getIntent().getParcelableExtra("bean");
        broadcast = getIntent().getStringExtra("broadcast");
/*        list_orderDetail = sellOrder.getSellOrderDetailList();
        if (list_orderDetail == null)
        {
            list_orderDetail = new ArrayList<>();
        }*/
    }

/*    public int countAllNumber()
    {
        List<SellOrderDetail_New> list = sellOrder.getSellOrderDetailList();
        int allnumber = 0;
        for (int i = 0; i < list.size(); i++)
        {
            allnumber = allnumber + Integer.valueOf(list.get(i).getplannumber());
        }
        return allnumber;
    }*/

    /*    @ViewById
        EditText by_danjia;
        @ViewById
        EditText bz_guige;
        @ViewById
        EditText bz_danjia;
        @ViewById
        EditText dingjin;
        @ViewById
        EditText dd_fzr;
        @ViewById
        EditText dd_cl;
        @ViewById
        EditText dd_tv;
        @ViewById
        EditText by_tv;*/
    @Click
    void add_jsd()
    {

        Intent intent = new Intent(NCZ_EditOrder.this, RecoveryDetail_.class);
        intent.putExtra("uuid", sellOrder.getUuid());
        intent.putExtra("bean", sellOrder_new);
        startActivity(intent);
    }
    @Click
    void dd_time()
    {
        MyDatepicker myDatepicker = new MyDatepicker(NCZ_EditOrder.this, dd_time);
        myDatepicker.getDialog().show();
    }

    private void showData()
    {
//        chanpin.setText(sellOrder.getGoodsname());//产品
        chanpin.setText(sellOrder.getProduct());//产品
//        et_name.setText(sellOrder.getPurchaName());//采购商名字
//        et_name.setText(sellOrder.getBuyersName());//采购商名字


        et_price.setText(sellOrder.getPrice());
        et_weight.setText(sellOrder.getWeight());
        et_values.setText(sellOrder.getSumvalues());
//        by_danjia.setText(sellOrder.getCarryPrice());
//        bz_guige.setText(sellOrder.getPackPec());
//        bz_danjia.setText(sellOrder.getPackPrice());
//        dd_fzr.setText(sellOrder.getMainPepName());
        dd_fzr.setText(sellOrder.getMainPeople());
        dd_bz.setText(sellOrder.getContractorName());
        dd_by.setText(sellOrder.getPickName());
        et_address.setText(sellOrder.getAddress());
        dingjin.setText(sellOrder.getWaitDeposit());
        dd_time.setText(sellOrder.getSaletime().substring(0, sellOrder.getSaletime().length() - 8));


        et_note.setText(sellOrder.getNote());
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
                        Toast.makeText(NCZ_EditOrder.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_EditOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_EditOrder.this, "error_connectServer");
            }
        });
    }

    private void editOrder(String uuid, String buyers, String address, String email, String phone, String price, String weight, String sumvalues, String note)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("buyers", buyers);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("email", email);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("price", price);
        params.addQueryStringParameter("weight", weight);
        params.addQueryStringParameter("sumvalues", sumvalues);
        params.addQueryStringParameter("note", note);
        params.addQueryStringParameter("action", "editOrder");
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
            /*        String rows = result.getRows().get(0).toString();
                    if (rows.equals("1"))
                    {
                        Toast.makeText(NCZ_EditOrder.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(broadcast);
                        sendBroadcast(intent);
                    } else if (rows.equals("0"))
                    {
                        Toast.makeText(NCZ_EditOrder.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
*/
                    finish();
                } else
                {
                    Toast.makeText(NCZ_EditOrder.this, "修改失败！", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_EditOrder.this, "error_connectServer");
            }
        });
    }

    public class Adapter_EditSellOrderDetail_NCZ extends BaseAdapter
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
            public TextView tv_area;
            public TextView tv_plannumber;
            public Button btn_editorderdetail;
            public Button btn_deleteorderdetail;

            public TextView tv_yq;
            public TextView tv_pq;
            public TextView tv_batchtime;
        }

        public Adapter_EditSellOrderDetail_NCZ(Activity context)
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
                convertView = listContainer.inflate(R.layout.adapter_editsellorderdetail_ncz, null);
                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
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
            listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
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
                            sellOrder.setSellOrderDetailList(list_orderDetail);
                            adapter_editSellOrderDetail_ncz.notifyDataSetChanged();
                            Intent intent1 = new Intent();
                            intent1.setAction(broadcast);
                            sendBroadcast(intent1);

                            Intent intent2 = new Intent();
                            intent2.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                            sendBroadcast(intent2);
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
                            sellOrder.setSellOrderDetailList(list_orderDetail);
                            adapter_editSellOrderDetail_ncz.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction(broadcast);
                            sendBroadcast(intent);

                            Intent intent2 = new Intent();
                            intent2.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                            sendBroadcast(intent2);
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

    private void getpurchaser()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_EditOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getpurchaser");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Purchaser> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        if (result.getAffectedRows() != 0)
                        {
                            listNewData = JSON.parseArray(result.getRows().toJSONString(), Purchaser.class);
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                if (listNewData.get(i).userType.equals("采购商"))
                                {
                                    listData_CG.add(listNewData.get(i));
                                } else if (listNewData.get(i).userType.equals("包装工头"))
                                {
                                    listData_BZ.add(listNewData.get(i));
                                } else
                                {
                                    listData_BY.add(listNewData.get(i));
                                }
                            }


                        } else
                        {
                            listNewData = new ArrayList<Purchaser>();
                        }

                    } else
                    {
                        listNewData = new ArrayList<Purchaser>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_EditOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_EditOrder.this, "error_connectServer");
            }
        });
    }

    //采购商的弹窗
    public void showDialog_workday(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_EditOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_EditOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                et_name.setText(zzsl);
                cgId = bundle.getString("id");

            }
        });
        customDialog_listView.show();
    }

    //包装工
    public void showDialog_bz(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_EditOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_EditOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                dd_bz.setText(zzsl);
                bzId = bundle.getString("id");

            }
        });
        customDialog_listView.show();
    }

    //搬运工
    public void showDialog_by(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_EditOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_EditOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                dd_by.setText(zzsl);
                byId = bundle.getString("id");

            }
        });
        customDialog_listView.show();
    }

    //负责人
    public void showDialog_fzr(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_EditOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_EditOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                dd_fzr.setText(zzsl);
                fzrId = bundle.getString("id");

            }
        });
        customDialog_listView.show();
    }

    //获取人员列表
    private void getlistdata()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_EditOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("nlevel", "1,2");
        params.addQueryStringParameter("action", "getUserlisttByUID");
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
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PeopelList.class);

                        listpeople.addAll(listNewData);
                        //方法一
                     /*   List<String> list = new ArrayList<String>();
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            list.add(listNewData.get(i).getUserlevelName()+"-"+listNewData.get(i).getRealName());
//                            list.add(jsonArray.getString(i));
                        }
                        showDialog_workday(list);*/
                    } else
                    {
                        listNewData = new ArrayList<PeopelList>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_EditOrder.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_EditOrder.this, "error_connectServer");

            }
        });

    }

    private void getsellOrderDetailBySaleId()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_EditOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleId", sellOrder.getUuid());
        params.addQueryStringParameter("action", "getsellOrderDetailBySaleId");
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


                        list_orderDetail = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
           /*             adapter_sellOrderDetail = new Adapter_New_SellDetail(NCZ_EditOrder.this, list_orderDetail);
                        lv.setAdapter(adapter_sellOrderDetail);
                        utils.setListViewHeight(lv);*/
                        int allnumber = 0;
                        for (int i = 0; i < list_orderDetail.size(); i++)
                        {
                            if (!list_orderDetail.get(i).getplannumber().equals(""))
                            allnumber = allnumber + Integer.valueOf(list_orderDetail.get(i).getplannumber());
                        }
                        tv_allnumber.setText(allnumber+"");
                        adapter_editSellOrderDetail_ncz = new Adapter_EditSellOrderDetail_NCZ(NCZ_EditOrder.this);
                        lv.setAdapter(adapter_editSellOrderDetail_ncz);
                        utils.setListViewHeight(lv);
                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_EditOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_EditOrder.this, "error_connectServer");
            }
        });
    }
}
