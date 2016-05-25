package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditOrderDetail;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2016/5/15.
 */
@EActivity(R.layout.ncz_newsalelist)
public class NCZ_NewSaleList extends Activity
{
    Adapter_NewSaleList adapter_sellOrderDetail;
    List<SellOrderDetail_New> list_SellOrderDetail;
    @ViewById
    Button btn_createorder;
    @ViewById
    ListView lv;

    @Click
    void btn_createorder()
    {
        if (list_SellOrderDetail != null && list_SellOrderDetail.size() > 0)
        {
            Intent intent = new Intent(NCZ_NewSaleList.this, NCZ_CreateOrder_.class);
            intent.putExtra("batchtime", "2016-05-11");
            intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list_SellOrderDetail);
            startActivity(intent);
        } else
        {
            Toast.makeText(this, "拟售清单为空，请先填加要出售的产品！", Toast.LENGTH_SHORT).show();
        }

    }

    @AfterViews
    void afterOncreate()
    {
        getNewSaleList();
//        getNewSaleList_test();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATENEWSALELIST);
        registerReceiver(receiver_update, intentfilter_update);
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            NCZ_NewSaleList.this.finish();
        }
    };

    private void getNewSaleList_test()
    {
        list_SellOrderDetail = FileHelper.getAssetsData(NCZ_NewSaleList.this, "getNewSaleList", SellOrderDetail_New.class);
        adapter_sellOrderDetail = new Adapter_NewSaleList(NCZ_NewSaleList.this, list_SellOrderDetail);
        lv.setAdapter(adapter_sellOrderDetail);
        utils.setListViewHeight(lv);
    }

    private void getNewSaleList()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_NewSaleList.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getSellOrderDetailList");//jobGetList1
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
                        list_SellOrderDetail = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
                        adapter_sellOrderDetail = new Adapter_NewSaleList(NCZ_NewSaleList.this, list_SellOrderDetail);
                        lv.setAdapter(adapter_sellOrderDetail);
                        utils.setListViewHeight(lv);

                    } else
                    {
                        list_SellOrderDetail = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_NewSaleList.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_NewSaleList.this, "error_connectServer");
            }
        });
    }

    public class Adapter_NewSaleList extends BaseAdapter
    {
        private Activity context;// 运行上下文
        private List<SellOrderDetail_New> listItems;// 数据集合
        private LayoutInflater listContainer;// 视图容器
        SellOrderDetail_New SellOrderDetail;
        CustomDialog_EditOrderDetail customDialog_editOrderDetaill;
        EditText et_number;

        class ListItemView
        {
            public TextView tv_number;
            public TextView tv_batchtime;
            public TextView tv_area;
            public Button btn_editorderdetail;
            public Button btn_delete;

            public TextView tv_yq;
            public TextView tv_pq;
        }

        public Adapter_NewSaleList(Activity context, List<SellOrderDetail_New> data)
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
                convertView = listContainer.inflate(R.layout.adapter_newsalelist, null);
                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                listItemView.btn_editorderdetail = (Button) convertView.findViewById(R.id.btn_editorderdetail);
                listItemView.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
                listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
                listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
                listItemView.btn_delete.setTag(position);
                listItemView.btn_delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = (int) v.getTag();
                        showDeleteTip(listItems.get(pos));
                    }
                });
                listItemView.btn_editorderdetail.setTag(position);
                listItemView.btn_editorderdetail.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = (int) v.getTag();
                        showDialog_editNumber(listItems.get(pos));
                    }
                });
                // 设置文字和图片
                listItemView.tv_number.setText("出售" + SellOrderDetail.getplannumber() + "株");
                listItemView.tv_batchtime.setText("批次:" + SellOrderDetail.getBatchTime());
                listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
                // 设置控件集到convertView
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }

            return convertView;
        }

        private void editNumber(final SellOrderDetail_New sellOrderDetail, final String number_new, String number_difference)
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
                            Intent intent = new Intent();
                            intent.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                            sendBroadcast(intent);
                            getNewSaleList();
                        } else
                        {
                            Toast.makeText(context, "该承包区该批次剩余株数不足", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent();
                            intent.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                            sendBroadcast(intent);
                            getNewSaleList();
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

        private void showDeleteTip(final SellOrderDetail_New sellOrderDetail_new)
        {
            View dialog_layout = (LinearLayout) context.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
            myDialog = new MyDialog(context, R.style.MyDialog, dialog_layout, "图片", "确定删除吗?", "删除", "取消", new MyDialog.CustomDialogListener()
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

        private void showDialog_editNumber(final SellOrderDetail_New sellOrderDetail_new)
        {
            final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_editorderdetail, null);
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
                    editNumber(sellOrderDetail_new, et_number.getText().toString(), String.valueOf(number_difference));
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
