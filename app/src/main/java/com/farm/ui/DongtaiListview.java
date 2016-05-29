package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.farm.R;
import com.farm.adapter.DongtaiListviewAdapter;
import com.farm.app.AppContext;
import com.farm.bean.DynamicEntity;
import com.farm.bean.commembertab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by hasee on 2016/5/29.
 */
@EActivity(R.layout.dongtailistview)
public class DongtaiListview extends Activity
{
    String type;
    List<DynamicEntity> listdata;
    DongtaiListviewAdapter dongtaiListviewAdapter;
    @ViewById
    ListView listviewss;

    @AfterViews
    void aftetcreate()
    {
        dongtaiListviewAdapter = new DongtaiListviewAdapter(DongtaiListview.this, listdata,type);
        listviewss.setAdapter(dongtaiListviewAdapter);
        listviewss.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                commembertab commembertab = AppContext.getUserInfo(DongtaiListview.this);
                DynamicEntity dynamicEntity = listdata.get(position);
                if (type.equals("ZL"))
                {
//                    AppContext.updateStatus(DongtaiListview.this, "0", listdata.get(position).getUuid(), "1", commembertab.getId());
                    AppContext.eventStatus(DongtaiListview.this, "3", listdata.get(position).getUuid(), commembertab.getId());
//                    Intent intent = new Intent(DongtaiListview.this, NCZ_CommandDetail_.class);
//                    intent.putExtra("bean", commandtab);// 因为list中添加了头部,因此要去掉一个
//                    startActivity(intent);
                } else if (type.equals("GZ"))
                {
                    AppContext.updateStatus(DongtaiListview.this, "0", listdata.get(position).getUuid(), "1", commembertab.getId());
//                    AppContext.updateStatus(DongtaiListview.this, "0", listdata.get(position).getUuid(), "1", commembertab.getId());
//                    if (commembertab.getnlevel().equals("0"))
//                    {
//                        Intent intent = new Intent(DongtaiListview.this, Common_JobDetail_Show_.class);
//                        intent.putExtra("bean", jobtab);// 因为list中添加了头部,因此要去掉一个
//                        startActivity(intent);
//                    } else
//                    {
//                        Intent intent = new Intent(DongtaiListview.this, Common_JobDetail_Assess_.class);
//                        intent.putExtra("bean", jobtab);// 因为list中添加了头部,因此要去掉一个
//                        startActivity(intent);
//                    }
                } else if (type.equals("MQ"))
                {
                    AppContext.updateStatus(DongtaiListview.this, "0", listdata.get(position).getUuid(), "3", commembertab.getId());
//                    Intent intent = new Intent(DongtaiListview.this, NCZ_todaymyDetail_.class);
//                    intent.putExtra("bean_gcd", PlantGcd); // 因为list中添加了头部,因此要去掉一个
//                    startActivity(intent);
                } else if (type.equals("SJ"))
                {
                    AppContext.eventStatus(DongtaiListview.this, "1", listdata.get(position).getUuid(), commembertab.getId());
//                    Intent intent=new Intent(DongtaiListview.this,NCZ_EventDatails_.class);
//                    intent.putExtra("reportedBean",reportedBean);
//                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        listdata = getIntent().getParcelableArrayListExtra("list");
        type = getIntent().getStringExtra("type");
    }


//    private void getEventList()
//    {
//
//        commembertab commembertab = AppContext.getUserInfo(DongtaiListview.this);
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("userid", commembertab.getId());
//        params.addQueryStringParameter("action", "GetDynamicData");
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
//        {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo)
//            {
//                String a = responseInfo.result;
//                List<DynamicEntity> listNewData = null;
//                Result result = JSON.parseObject(responseInfo.result, Result.class);
//                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//                {
//                    if (result.getAffectedRows() > 0)
//                    {
//                        listNewData = JSON.parseArray(result.getRows().toJSONString(), DynamicEntity.class);
//                        dongtaiListviewAdapter = new DongtaiListviewAdapter(DongtaiListview.this, listNewData);
//                        listviewss.setAdapter(dongtaiListviewAdapter);
//
//
//                    }
//                } else
//                {
//                    AppContext.makeToast(DongtaiListview.this, "error_connectDataBase");
//
//                    return;
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg)
//            {
//                String a = error.getMessage();
//                AppContext.makeToast(DongtaiListview.this, "error_connectServer");
//
//            }
//        });
//    }
}
