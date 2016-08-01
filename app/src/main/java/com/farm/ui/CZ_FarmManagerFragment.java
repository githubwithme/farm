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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.areatab;
import com.farm.bean.commembertab;
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
import java.util.List;

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EFragment
public class CZ_FarmManagerFragment extends Fragment
{
    TimeThread timethread;
    commembertab commembertab;
    PopupWindow pw_command;
    View pv_command;
    @ViewById
    Button btn_add;
    @ViewById
    Button btn_search;
    @ViewById
    GridView gv;
    @ViewById
    View view;
    @ViewById
    TextView tv_plantnumber_new;
    @ViewById
    TextView tv_cmdnumber_new;
    @ViewById
    TextView tv_worknumber_new;
    @ViewById
    FrameLayout fl_worknumber_new;
    @ViewById
    FrameLayout fl_plantnumber_new;
    @ViewById
    FrameLayout fl_cmdnumber_new;

    @AfterViews
    void afterOncrete()
    {
        commembertab = AppContext.getUserInfo(getActivity());
        getListData();
        timethread = new TimeThread();
        timethread.setSleep(false);
        timethread.start();
    }

    @Click
    void btn_add()
    {
        showPop_add();
    }

    @Click
    void btn_search()
    {
        Intent intent = new Intent(getActivity(), SearchAllInformation_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_zl()
    {
//        Intent intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
//        getActivity().startActivity(intent);
        Intent intent = new Intent(getActivity(), CZ_CommandListActivity_.class);
        intent.putExtra("parkid", commembertab.getparkId());
        startActivity(intent);
    }

    @Click
    void ll_gz()
    {
//        Intent intent = new Intent(getActivity(), NCZ_JobActivity_.class);
//        getActivity().startActivity(intent);
        Intent intent = new Intent(getActivity(), CZ_JobActivity_.class);
        intent.putExtra("parkid", commembertab.getparkId());
        startActivity(intent);
    }

    @Click
    void ll_mq()
    {
//        Intent intent = new Intent(getActivity(), NCZ_MQActivity_.class);
        Intent intent = new Intent(getActivity(), CZ_MQActivity_.class);
        getActivity().startActivity(intent);
//        Intent intent = new Intent(getActivity(), CZ_ToDayPQ_.class);
//        intent.putExtra("parkid", commembertab.getparkId());
//        startActivity(intent);
    }

    @Click
    void ll_sj()
    {
        Intent intent = new Intent(getActivity(), PG_ListOfEvents_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_kc()
    {
        Intent intent = new Intent(getActivity(), Ncz_wz_ll_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_xs()
    {
//        Intent intent = new Intent(getActivity(), CZ_OrderManager_.class);
//        Intent intent = new Intent(getActivity(), CZ_SaleActivity_.class);
//        Intent intent = new Intent(getActivity(), CZ_AreaSaleActivity_.class);  //1
        Intent intent = new Intent(getActivity(), CZ_NEW_AreaSaleActivty_.class);
//        Intent intent = new Intent(getActivity(), PG_SaleActivity_.class);
        getActivity().startActivity(intent);
    }



    @Click
    void ll_dl()
    {
//        Intent intent = new Intent(getActivity(), CZ_DLFragment_.class);
        Intent intent = new Intent(getActivity(), CZ_BreakOffActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_sp()
    {
        Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
        getActivity().startActivity(intent);
    }

//    @Click
//    void ll_tj()
//    {
////        Intent intent = new Intent(getActivity(), NCZ_Statistics_.class);
//        Intent intent = new Intent(getActivity(), NCZ_AnalysisModule_.class);
//        getActivity().startActivity(intent);
//    }

    @Click
    void ll_tq()
    {
        Intent intent = new Intent(getActivity(), WeatherActivity_.class);
        intent.putExtra("parkid", "80");
        getActivity().startActivity(intent);
    }

    @Click
    void ll_jobself()
    {
        Intent intent = new Intent(getActivity(), CZ_JobFragment_.class);
        getActivity().startActivity(intent);
    }    @Click
    void ll_sk()
    {
        Intent intent = new Intent(getActivity(), NCZ_FarmMapActivity_.class);
        intent.putExtra("parkid", "80");
        getActivity().startActivity(intent);
    }

    @Click
    void ll_more()
    {
//        Intent intent = new Intent(getActivity(), NCZ_MoreModule_.class);
//        intent.putExtra("parkid", "80");
//        getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.cz_farmmanagerfragment, container, false);
        return rootView;
    }

    private void getListData()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("parkID", commembertab.getparkId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("action", "parkGetbyParkID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<areatab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), areatab.class);
                        if (listNewData != null)
                        {

                            areatab areatab = listNewData.get(0);
                            Intent intent = new Intent();
                            intent.putExtra("number", Integer.valueOf(areatab.getJobCount()) + Integer.valueOf(areatab.getCommandCount()) + Integer.valueOf(areatab.getPlantGrowCount()));
                            intent.setAction(AppContext.UPDATEMESSAGE_FARMMANAGER);
                            getActivity().sendBroadcast(intent);
                            if (Integer.valueOf(areatab.getJobCount()) > 0)
                            {
                                fl_worknumber_new.setVisibility(View.VISIBLE);
                                tv_worknumber_new.setText(areatab.getJobCount());
                            } else
                            {
                                fl_worknumber_new.setVisibility(View.GONE);
                            }
                            if (Integer.valueOf(areatab.getCommandCount()) > 0)
                            {
                                fl_cmdnumber_new.setVisibility(View.VISIBLE);
                                tv_cmdnumber_new.setText(areatab.getCommandCount());
                            } else
                            {
                                fl_cmdnumber_new.setVisibility(View.GONE);
                            }
                            if (Integer.valueOf(areatab.getPlantGrowCount()) > 0)
                            {
                                fl_plantnumber_new.setVisibility(View.VISIBLE);
                                tv_plantnumber_new.setText(areatab.getPlantGrowCount());
                            } else
                            {
                                fl_plantnumber_new.setVisibility(View.GONE);
                            }
                        }
                    } else
                    {
                        listNewData = new ArrayList<areatab>();
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
                AppContext.makeToast(getActivity(), "error_connectServer");
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
                Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
                getActivity().startActivity(intent);
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
                    return;
                } else
                {
                    try
                    {
                        Thread.sleep(AppContext.TIME_REFRESH);
                        starttime = starttime + 1000;
                        getListData();
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

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (timethread != null && timethread.isAlive())
        {
            timethread.setStop(true);
            timethread.interrupt();
            timethread = null;
        }
    }
}
