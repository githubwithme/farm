package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_Dynamic;
import com.farm.adapter.Adapter_DynamicFragment;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.DynamicBean;
import com.farm.bean.DynamicEntity;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EFragment
public class DynamicFragment extends Fragment
{
    TimeThread timethread;
    PopupWindow pw_command;
    View pv_command;
    Adapter_Dynamic adapter_dynamic;
    List<DynamicBean> listData = new ArrayList<>();
    List<DynamicEntity> list_DynamicEntity;
    @ViewById
    Button btn_add;
    @ViewById
    View line;
    @ViewById
    Button btn_search;
    @ViewById
    ListView lv;
    @ViewById
    View view;

    int k = 0;

    @Click
    void btn_add()
    {
        showPop_add();
    }


    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            if (timethread != null)
            {
                timethread.setSleep(false);
            }
        } else
        {
            if (timethread != null)
            {
                timethread.setSleep(true);
            }
        }
    }


    @Override
    public void onStop()
    {
        super.onStop();
//        Toast.makeText(getActivity(), "hide3", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        if (getUserVisibleHint())
//        {
//            Toast.makeText(getActivity(), "show2", Toast.LENGTH_SHORT).show();
//        }
    }

    @Click
    void btn_search()
    {
        Intent intent = new Intent(getActivity(), SearchAllInformation_.class);
        getActivity().startActivity(intent);
    }


    @AfterViews
    void afterOncrete()
    {
//        getDynamicData_temp();
        getDynamicData();
//        getNewSaleList_test(
//        getDynamicData();
        timethread = new TimeThread();
        timethread.setSleep(false);
        timethread.start();
//        getNewSaleList_test();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.dynamicactivity, container, false);
        return rootView;
    }


    private void getDynamicData()
    {

        if (getActivity() == null)
        {
            return;
        }
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("action", "GetDynamicData1");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String aa = responseInfo.result;
                List<DynamicBean> list = null;
                listData = new ArrayList<DynamicBean>();
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    list = JSON.parseArray(result.getRows().toJSONString(), DynamicBean.class);
                    for (int i = 0; i < list.size(); i++)
                    {
                        if (list.get(i).getListdata().size() != 0)
                        {
                            for (int l = 0; l < list.get(i).getListdata().size(); l++)
                            {
                                if (list.get(i).getListdata().get(l).getFlashStr().equals("1"))
                                {
                                    k++;
                                }
                            }
                            listData.add(list.get(i));
                        }
                    }
                    list = utils.BubbleSortArray(list);
                    Intent intent = new Intent();
                    intent.putExtra("Num", k + "");
                    intent.setAction(AppContext.BROADCAST_NCZ_DT);
                    getActivity().sendBroadcast(intent);
                    k = 0;
                    adapter_dynamic = new Adapter_Dynamic(getActivity(), list);
                    lv.setAdapter(adapter_dynamic);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {

                            Intent intent = null;
                            String type = listData.get(position).getType();
                            if (type.equals("ZL"))
                            {
                                intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
                            } else if (type.equals("GZ"))
                            {
                                intent = new Intent(getActivity(), NCZ_JobActivity_.class);
                            } else if (type.equals("MQ"))
                            {
                                intent = new Intent(getActivity(), NCZ_MQActivity_.class);
                            } else if (type.equals("XS"))
                            {
//                                intent = new Intent(getActivity(), NCZ_FarmSale_.class);
                                intent = new Intent(getActivity(), NCZ_OrderManager_.class);
                            } else if (type.equals("KC"))
                            {
                                intent = new Intent(getActivity(), Ncz_wz_ll_.class);
                            } else if (type.equals("SP"))
                            {
                                intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
                            } else if (type.equals("SJ"))
                            {
                                intent = new Intent(getActivity(), NCZ_SJActivity_.class);
                            } else if (type.equals("DL"))
                            {
                                intent = new Intent(getActivity(), NCZ_DLdatail_.class);
                            }
                            getActivity().startActivity(intent);


//                                List<DynamicEntity> list = listData.get(position).getListdata();
//                                Intent intent = new Intent(getActivity(), DongtaiListview_.class);
//                                String type = listData.get(position).getType();
//                                if (type.equals("ZL"))
//                                {
//                                    intent.putExtra("type", "ZL");
//                                } else if (type.equals("GZ"))
//                                {
//                                    intent.putExtra("type", "GZ");
//                                } else if (type.equals("MQ"))
//                                {
//                                    intent.putExtra("type", "ZL");
//                                } else if (type.equals("XS"))
//                                {
//                                    intent.putExtra("type", "MQ");
//                                } else if (type.equals("KC"))
//                                {
//                                    intent.putExtra("type", "KC");
//                                } else if (type.equals("SP"))
//                                {
//                                    intent.putExtra("type", "SP");
//                                } else if (type.equals("SJ"))
//                                {
//                                    intent.putExtra("type", "SJ");
//                                } else if (type.equals("DL"))
//                                {
//                                    intent.putExtra("type", "DL");
//                                }
//                                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
//                                getActivity().startActivity(intent);
                        }
                    });
//                    }
                } else
                {
                    Toast.makeText(getActivity(), "连接数据库异常", Toast.LENGTH_SHORT).show();
//                    AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    if (!ishidding && timethread != null)
//                    {
//                        timethread.setSleep(false);
//                    }
                    return;
                }


            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = msg;
                Toast.makeText(getActivity(), "连接服务器异常", Toast.LENGTH_SHORT).show();
//                AppContext.makeToast(getActivity(), "error_connectServer");
//                if (!ishidding && timethread != null)
//                {
//                    timethread.setSleep(false);
//                }
            }
        });
    }

    private void getDynamicData_temp()
    {
        if (getActivity() == null)
        {
            return;
        }
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("action", "parkGetList");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String aa = responseInfo.result;
                List<parktab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        list_DynamicEntity = new ArrayList<DynamicEntity>();
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
                        int zl = 0;
                        int gz = 0;
                        int mq = 0;
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            parktab parktab = listNewData.get(i);
                            zl = zl + Integer.valueOf(parktab.getCommandCount() + Integer.valueOf(parktab.getAreacommandCount()));
                            gz = gz + Integer.valueOf(parktab.getJobCount() + Integer.valueOf(parktab.getAreajobCount()));
                            mq = mq + Integer.valueOf(parktab.getPlantGrowCount() + Integer.valueOf(parktab.getAreaplantGrowCount()));
                        }
                        if (zl > 0)
                        {
                            DynamicEntity dynamicentity = new DynamicEntity();
                            dynamicentity.setDate(utils.getToday());
                            dynamicentity.setNote("查看");
                            dynamicentity.setTitle("指令");
                            dynamicentity.setType("ZL");
                            list_DynamicEntity.add(dynamicentity);
                        }
                        if (gz > 0)
                        {
                            DynamicEntity dynamicentity = new DynamicEntity();
                            dynamicentity.setDate(utils.getToday());
                            dynamicentity.setNote("查看");
                            dynamicentity.setTitle("工作");
                            dynamicentity.setType("GZ");
                            list_DynamicEntity.add(dynamicentity);
                        }
                        if (mq > 0)
                        {
                            DynamicEntity dynamicentity = new DynamicEntity();
                            dynamicentity.setDate(utils.getToday());
                            dynamicentity.setNote("查看");
                            dynamicentity.setTitle("苗情");
                            dynamicentity.setType("MQ");
                            list_DynamicEntity.add(dynamicentity);
                        }

                        Adapter_DynamicFragment adapter_dynamic = new Adapter_DynamicFragment(getActivity(), list_DynamicEntity);
                        lv.setAdapter(adapter_dynamic);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Intent intent = null;
                                String type = list_DynamicEntity.get(position).getType();
                                if (type.equals("ZL"))
                                {
                                    intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
                                } else if (type.equals("GZ"))
                                {
                                    intent = new Intent(getActivity(), NCZ_JobActivity_.class);
                                } else if (type.equals("MQ"))
                                {
                                    intent = new Intent(getActivity(), NCZ_MQActivity_.class);
                                } else if (type.equals("XS"))
                                {
                                    intent = new Intent(getActivity(), NCZ_FarmSale_.class);
                                } else if (type.equals("KC"))
                                {
                                    intent = new Intent(getActivity(), Ncz_wz_ll_.class);
                                } else if (type.equals("SP"))
                                {
                                    intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
                                } else if (type.equals("SJ"))
                                {
                                    intent = new Intent(getActivity(), NCZ_SJActivity_.class);
                                } else if (type.equals("DL"))
                                {
                                    intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
                                }
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    if (!ishidding && timethread != null)
//                    {
//                        timethread.setSleep(false);
//                    }
                    return;
                }


            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
//                if (!ishidding && timethread != null)
//                {
//                    timethread.setSleep(false);
//                }
            }
        });
    }

    public void showPop_add()
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.dynamicfragment_add, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                    return true;
                }
                return false;
            }
        });
        pv_command.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_command.isShowing())
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, 500, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_command.showAsDropDown(view, 0, 0);
//        int[] location = new int[2];
//        btn_add.getLocationOnScreen(location);
//        pw_command.showAtLocation(btn_add, Gravity.NO_GRAVITY, location[0]+line.getWidth(), location[1]);
        pw_command.setOutsideTouchable(true);
        LinearLayout ll_addcost = (LinearLayout) pv_command.findViewById(R.id.ll_addcost);
        LinearLayout ll_addcommand = (LinearLayout) pv_command.findViewById(R.id.ll_addcommand);


        ll_addcost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_command.dismiss();
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
                Intent intent = new Intent(getActivity(), AddSpecialCost_.class);
                startActivity(intent);
            }
        });
        ll_addcommand.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_command.dismiss();
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
                Intent intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
                getActivity().startActivity(intent);
            }
        });

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
    }

    class TimeThread extends Thread
    {
        private boolean isSleep = true;
        private boolean stop = false;

        public void run()
        {
            Long starttime = 0l;
            while (!stop)
            {
                if (isSleep)
                {
                } else
                {
                    try
                    {
                        timethread.sleep(AppContext.TIME_REFRESH);
                        starttime = starttime + 1000;
//                        getDynamicData_temp();
                        getDynamicData();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setSleep(boolean sleep)
        {
            isSleep = sleep;
        }

        public void setStop(boolean stop)
        {
            this.stop = stop;
        }
    }


    private void getEventList()
    {

        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getEventListByUID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<ReportedBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() > 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), ReportedBean.class);

                        Iterator<ReportedBean> it = listNewData.iterator();
                        while (it.hasNext())
                        {
                            ReportedBean reportedBean = it.next();
                            if (reportedBean.getIsflashStr().equals("0") || reportedBean.resultflashStr.equals("0"))
                            {
                                it.remove();
                            }
                        }
                        DynamicEntity dynamicentity = new DynamicEntity();
                        dynamicentity.setDate(utils.getToday());
                        dynamicentity.setNote(listNewData.size() + "事件更新");
                        dynamicentity.setTitle("事件");
                        dynamicentity.setType("SJ");
                        list_DynamicEntity.add(dynamicentity);
                    } else
                    {
                        listNewData = new ArrayList<ReportedBean>();
                    }
                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");

                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");

            }
        });
    }
}
