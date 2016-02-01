package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.FirstItemAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.HaveReadRecord;
import com.farm.bean.Result;
import com.farm.bean.SelectRecords;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.common.DictionaryHelper;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDatepicker;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;

@EActivity(R.layout.addnotproductcommand)
public class AddNotProductCommand extends Activity implements OnClickListener
{
    String workday;
    CustomDialog_ListView customDialog_listView;
    FirstItemAdapter firstItemAdapter;
    private ListView mainlist;
    private ListView morelist;
    PopupWindow popupWindow_selector;
    View popupWindowView_selector;
    PopupWindow popupWindow_recent;
    View popupWindowView_recent;
    @ViewById
    TextView tv_workday;
    CountDownLatch latch;
    @ViewById
    View line;
    @ViewById
    TextView tv_area;
    @ViewById
    TextView tv_timelimit;
    @ViewById
    EditText et_note;
    @ViewById
    TextView tv_importance;
    @ViewById
    RadioGroup rg_level;
    @ViewById
    Button btn_save;
    // List<Dictionary> dic_cmdlist = null;
    // List<Dictionary> dic_arealist = null;
    Dictionary dic_comm;
    Dictionary dic_area;
    Dictionary dic_park;
    String level = "";
    String importance_id = "";
    String importance = "";
    commembertab commembertab;

    @Click
    void btn_save()
    {
        commandTabAdd();
    }

    @Click
    void tv_importance()
    {
        JSONObject jsonObject = utils.parseJsonFile(AddNotProductCommand.this, "dictionary.json");
        JSONArray jsonArray_id= JSONArray.parseArray(jsonObject.getString("importance_id"));
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("importance"));

        List<String> list = new ArrayList<String>();
        List<String> list_id = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        for (int i = 0; i < jsonArray_id.size(); i++)
        {
            list_id.add(jsonArray_id.getString(i));
        }
        showDialog_Importance(list_id,list);
    }

    @Click
    void tv_workday()
    {
        JSONObject jsonObject = utils.parseJsonFile(this, "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        showDialog_workday(list);
    }


    @Click
    void tv_area()
    {
        if (level.equals("0"))
        {
            if (dic_park.getFirstItemName().size() != 0)
            {
                DictionaryFragment dictionaryFragment = new DictionaryFragment_();
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", dic_park);
                dictionaryFragment.setArguments(bundle);
                dictionaryFragment.setResultview(tv_area);
                switchContent_top(mContent_top, dictionaryFragment);
            } else
            {
                Toast.makeText(AddNotProductCommand.this, "获取数据异常！", Toast.LENGTH_SHORT).show();
            }

        } else if (level.equals("1"))
        {
            if (dic_area != null)
            {
                DictionaryFragment dictionaryFragment = new DictionaryFragment_();
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", dic_area);
                dictionaryFragment.setArguments(bundle);
                dictionaryFragment.setResultview(tv_area);
                switchContent_top(mContent_top, dictionaryFragment);
            } else
            {
                Toast.makeText(AddNotProductCommand.this, "获取数据异常！", Toast.LENGTH_SHORT).show();
            }

        } else
        {
            Toast.makeText(AddNotProductCommand.this, "请先选择执行级别！", Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void tv_timelimit()
    {
        MyDatepicker myDatepicker = new MyDatepicker(AddNotProductCommand.this, tv_timelimit);
        myDatepicker.getDialog().show();
    }

    @AfterViews
    void afterOncreate()
    {
        rg_level.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1)
            {

                RadioButton radioButton = (RadioButton) findViewById(arg0.getCheckedRadioButtonId());
                if (radioButton.getText().equals("园区"))
                {
                    level = "0";
                    if (dic_area != null)
                    {
                        SqliteDb.deleteAllRecordtemp(AddNotProductCommand.this, SelectRecords.class, dic_area.getBELONG());
                    } else
                    {
                        Toast.makeText(AddNotProductCommand.this, "获取数据异常！", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    level = "1";
                    if (dic_park != null)
                    {
                        SqliteDb.deleteAllRecordtemp(AddNotProductCommand.this, SelectRecords.class, dic_park.getBELONG());
                    } else
                    {
                        Toast.makeText(AddNotProductCommand.this, "获取数据异常！", Toast.LENGTH_SHORT).show();
                    }

                }
                tv_area.setText("请选择区域");
            }
        });
        getCommandlist();
        getArealist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(AddNotProductCommand.this);
    }

    private void commandTabAdd()
    {
        if (tv_area.getText().toString().equals(""))
        {
            Toast.makeText(AddNotProductCommand.this, "请选择区域！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_timelimit.getText().toString().equals(""))
        {
            Toast.makeText(AddNotProductCommand.this, "请选择开始时间！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tv_workday.getText().toString().equals(""))
        {
            Toast.makeText(AddNotProductCommand.this, "请选择作业天数！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_note.getText().toString().equals(""))
        {
            Toast.makeText(AddNotProductCommand.this, "请填写说明！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (importance_id.equals(""))
        {
            Toast.makeText(AddNotProductCommand.this, "请选择重要性！", Toast.LENGTH_SHORT).show();
            return;
        }
        List<SelectRecords> list_SelectRecords;
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("userName", commembertab.getrealName());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "commandTabAdd");

        if (level.equals("0"))
        {
            list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(this, SelectRecords.class, dic_park.getBELONG().toString());
            String tempid = "";
            String tempname = "";
            for (int i = 0; i < list_SelectRecords.size(); i++)
            {
                tempid = tempid + list_SelectRecords.get(i).getSecondid() + ",";
                tempname = tempname + list_SelectRecords.get(i).getSecondtype() + ",";
            }
            params.addQueryStringParameter("parkId", tempid.substring(0, tempid.length() - 1));
            params.addQueryStringParameter("parkName", tempname.substring(0, tempname.length() - 1));
        } else if (level.equals("1"))
        {
            list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(this, SelectRecords.class, dic_area.getBELONG().toString());
            String tempid = "";
            String tempname = "";
            for (int i = 0; i < list_SelectRecords.size(); i++)
            {
                tempid = tempid + list_SelectRecords.get(i).getFirstid() + ":" + list_SelectRecords.get(i).getSecondid() + ",";
                tempname = tempname + list_SelectRecords.get(i).getFirsttype() + ":" + list_SelectRecords.get(i).getSecondtype() + ",";
            }
            params.addQueryStringParameter("areaId", tempid.substring(0, tempid.length() - 1));
            params.addQueryStringParameter("areaName", tempname.substring(0, tempname.length() - 1));
        } else
        {
            Toast.makeText(AddNotProductCommand.this, "请选择执行级别！", Toast.LENGTH_SHORT).show();
            return;
        }


        params.addQueryStringParameter("nongziName", et_note.getText().toString());
        params.addQueryStringParameter("amount", "");
        params.addQueryStringParameter("commNote", et_note.getText().toString());
        params.addQueryStringParameter("commDays", tv_workday.getText().toString());
        params.addQueryStringParameter("commComDate", tv_timelimit.getText().toString());
        params.addQueryStringParameter("stdJobType", "-1");
        params.addQueryStringParameter("stdJobTypeName", "");
        params.addQueryStringParameter("stdJobId", "-1");
        params.addQueryStringParameter("stdJobName", "");
        params.addQueryStringParameter("importance", importance_id);
        params.addQueryStringParameter("execLevel", level);
        params.addQueryStringParameter("commFromVPath", "0");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<jobtab> listData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
                    if (listData == null)
                    {
                        AppContext.makeToast(AddNotProductCommand.this, "error_connectDataBase");
                    } else
                    {
                        HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(AddNotProductCommand.this, AppContext.TAG_NCZ_CMD);
                        if (haveReadRecord != null)
                        {
                            SqliteDb.updateHaveReadRecord(AddNotProductCommand.this, AppContext.TAG_NCZ_CMD, String.valueOf((Integer.valueOf(haveReadRecord.getNum()) + 1)));
                        } else
                        {
                            SqliteDb.saveHaveReadRecord(AddNotProductCommand.this, AppContext.TAG_NCZ_CMD, "1");
                        }
                        Toast.makeText(AddNotProductCommand.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(AddNotProductCommand.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                String a = error.getMessage();
                AppContext.makeToast(AddNotProductCommand.this, "error_connectServer");
            }
        });
    }

    private void getCommandlist()
    {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("name", "Zuoye");
        params.addQueryStringParameter("action", "getDict");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            dic_comm = lsitNewData.get(0);
                        }

                    } else
                    {
                        lsitNewData = new ArrayList<Dictionary>();
                    }
                } else
                {
                    AppContext.makeToast(AddNotProductCommand.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(AddNotProductCommand.this, "error_connectServer");
            }
        });
    }

    private void getArealist()
    {
        commembertab commembertab = AppContext.getUserInfo(AddNotProductCommand.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("name", "getstdPark");
        params.addQueryStringParameter("action", "getDict");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            dic_area = lsitNewData.get(0);
                            dic_area.setBELONG("片区执行");
                            dic_park = DictionaryHelper.getParkDictionary(dic_area);
                        }
                    } else
                    {
                        lsitNewData = new ArrayList<Dictionary>();
                    }
                } else
                {
                    AppContext.makeToast(AddNotProductCommand.this, "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(AddNotProductCommand.this, "error_connectServer");
            }
        });
    }

    @Override
    public void onClick(View arg0)
    {

    }

    Fragment mContent = new Fragment();

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    Fragment mContent_top = new Fragment();

    public void showDialog_Importance(List<String> list_id,List<String> list)
    {
        View dialog_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(AddNotProductCommand.this, R.style.MyDialog, dialog_layout, list, list_id, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                importance_id = bundle.getString("id");
                importance = bundle.getString("name");
                tv_importance.setText(importance);
            }
        });
        customDialog_listView.show();
    }

    public void showDialog_workday(List<String> list)
    {
        View dialog_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                workday = bundle.getString("name");
                tv_workday.setText(workday);
            }
        });
        customDialog_listView.show();
    }

    public void switchContent_top(Fragment from, Fragment to)
    {
        if (mContent_top != to)
        {
            mContent_top = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.top_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
