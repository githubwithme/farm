package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.WeatherInfo;
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

import java.util.List;

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EFragment
public class FarmManagerFragment extends Fragment
{
    WeatherInfo weather;
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
    TextView tv_cityname;
    @ViewById
    TextView tv_wind_windspeed;
    @ViewById
    TextView tv_weatherinfo0;
    @ViewById
    TextView tv_weatherinfo1;
    @ViewById
    TextView tv_weatherinfo2;
    @ViewById
    TextView tv_weatherinfo3;
    @ViewById
    TextView tv_weatherinfo4;
    @ViewById
    TextView tv_weatherinfo5;
    @ViewById
    TextView tv_weatherinfo6;
    @ViewById
    TextView tv_wind_power;
    @ViewById
    TextView tv_wind_direct;
    @ViewById
    TextView tv_temperature0;
    @ViewById
    TextView tv_temperature1;
    @ViewById
    TextView tv_temperature2;
    @ViewById
    TextView tv_temperature3;
    @ViewById
    TextView tv_temperature4;
    @ViewById
    TextView tv_temperature5;
    @ViewById
    TextView tv_temperature6;
    @ViewById
    TextView tv_date0;
    @ViewById
    TextView tv_date1;
    @ViewById
    TextView tv_date2;
    @ViewById
    TextView tv_date3;
    @ViewById
    TextView tv_date4;
    @ViewById
    TextView tv_date5;
    @ViewById
    TextView tv_date6;
    @ViewById
    ImageView img0;
    @ViewById
    ImageView img1;
    @ViewById
    ImageView img2;
    @ViewById
    ImageView img3;
    @ViewById
    ImageView img4;
    @ViewById
    ImageView img5;
    @ViewById
    ImageView img6;

    @AfterViews
    void afterOncrete()
    {
        getNCZ_getAllContractSaleData();
    }

    @Click
    void btn_add()
    {
        showPop_add();
    }

    @Click
    void btn_search()
    {
//        Intent intent = new Intent(getActivity(), SearchAllInformation_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    //    @Click
//    void ce_tq()//测试天气
//    {
//        Intent intent = new Intent(getActivity(), NC_Weater_.class);
//        getActivity().startActivity(intent);
//    }
    @Click
    void rl_zl()
    {
//        Intent intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
//        Intent intent = new Intent(getActivity(), NCZ_CommandActivity_.class);
////        Intent intent = new Intent(getActivity(), PG_CommandActivity_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();

    }

    @Click
    void rl_gz()
    {
//        Intent intent = new Intent(getActivity(), NCZ_JobActivity_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    @Click
    void rl_mq()
    {
//        Intent intent = new Intent(getActivity(), NCZ_MQActivity_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    @Click
    void rl_sj()
    {
//        Intent intent = new Intent(getActivity(), NCZ_SJActivity_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    @Click
    void rl_dd()
    {
        Intent intent = new Intent(getActivity(), NCZ_OrderManager_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void rl_ht()
    {
        Intent intent = new Intent(getActivity(), NCZ_CustomerContract_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void rl_kc()
    {
//        Intent intent = new Intent(getActivity(), Ncz_wz_ll_.class);
//        Intent intent = new Intent(getActivity(), NCZ_GoogdsManagerActivity_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    @Click
    void img_location()
    {
        Toast.makeText(getActivity(), "该农场暂无其他城市分布!", Toast.LENGTH_SHORT).show();
    }

    @Click
    void rl_xs()
    {
//        Intent intent = new Intent(getActivity(), NCZ_SaleChart_.class);
//        getActivity().startActivity(intent);
//        Intent intent = new Intent(getActivity(), NCZ_SaleInfor_.class);
//        Intent intent = new Intent(getActivity(), NCZ_FarmSaleData_.class);
//        Intent intent = new Intent(getActivity(), NCZ_SaleModuleActivity_.class);
//        Intent intent = new Intent(getActivity(), NCZ_AreaSaleActivity_.class);
        Intent intent = new Intent(getActivity(), NCZ_ParkSaleActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void rl_cw()
    {
//        Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    @Click
    void rl_dl()
    {
        Intent intent = new Intent(getActivity(), NCZ_BreakOffActivity_.class);
//        Intent intent = new Intent(getActivity(), NCZ_DLdatail_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_sp()
    {
//        Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    @Click
    void rl_tj()
    {
//        Intent intent = new Intent(getActivity(), NCZ_Statistics_.class);
//        Intent intent = new Intent(getActivity(), NCZ_AnalysisModule_.class);
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

//    @Click
//    void ll_tq()
//    {
//        Intent intent = new Intent(getActivity(), WeatherActivity_.class);
//        intent.putExtra("parkid", "80");
//        getActivity().startActivity(intent);
//    }

    @Click
    void rl_sk()
    {
//        Intent intent = new Intent(getActivity(), NCZ_FarmMapActivity_.class);
//        intent.putExtra("parkid", "80");
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    @Click
    void ll_weather()
    {
//        Intent intent = new Intent(getActivity(), WeatherActivity_.class);
//        intent.putExtra("parkid", "80");
//        getActivity().startActivity(intent);
    }

    @Click
    void rl_more()
    {
//        Intent intent = new Intent(getActivity(), NCZ_MoreModule_.class);
//        intent.putExtra("parkid", "80");
//        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), "该功能待上线喔!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.farmmanagerfragment, container, false);
        return rootView;
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

    public void getNCZ_getAllContractSaleData()
    {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", "a408788d6a2703d75f3bc7f970b046a2");
        params.addBodyParameter("cityname", "南宁市");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.weatherurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String info = responseInfo.result;
                WeatherInfo wea = JSON.parseObject(info, WeatherInfo.class);
                Log.e("TAG", wea.toString() + "==");
                //今日天气的适配
                WeatherInfo.ResultBean.DataBean.RealtimeBean realtimeBean = wea.getResult().getData().getRealtime();
                if (realtimeBean != null)
                {
                    tv_cityname.setText(realtimeBean.getCity_name());
                    tv_date0.setText(realtimeBean.getDate().substring(5, realtimeBean.getDate().length()));
                    tv_temperature0.setText(realtimeBean.getWeather().getTemperature() + "℃");
                    tv_weatherinfo0.setText(realtimeBean.getWeather().getInfo());
                    tv_wind_windspeed.setText(realtimeBean.getWind().getWindspeed() + "m/s");
                    tv_wind_power.setText("风力：" + realtimeBean.getWind().getPower());
                    tv_wind_direct.setText("风向：" + realtimeBean.getWind().getDirect());
                }
                //未来天气的适配
                List<WeatherInfo.ResultBean.DataBean.WeatherBean> list_weatherBean = wea.getResult().getData().getWeather();
                for (int i = 0; i < list_weatherBean.size(); i++)
                {
                    WeatherInfo.ResultBean.DataBean.WeatherBean weatherbean = list_weatherBean.get(i);
                    switch (i)
                    {
                        case 1:
                            tv_date1.setText("明天");
                            tv_temperature1.setText(weatherbean.getInfo().getDay().get(2) + "℃");
                            tv_weatherinfo1.setText(weatherbean.getInfo().getDay().get(1));
                            if (weatherbean == null)
                            {

                            }
                            break;
                        case 2:
                            tv_date2.setText("后天");
                            tv_temperature2.setText(weatherbean.getInfo().getDay().get(2) + "℃");
                            tv_weatherinfo2.setText(weatherbean.getInfo().getDay().get(1));

                            break;
                        case 3:
                            tv_date3.setText("周" + weatherbean.getWeek());
                            tv_temperature3.setText(weatherbean.getInfo().getDay().get(2) + "℃");
                            tv_weatherinfo3.setText(weatherbean.getInfo().getDay().get(1));
                            break;
                        case 4:
                            tv_date4.setText("周" + weatherbean.getWeek());
                            tv_temperature4.setText(weatherbean.getInfo().getDay().get(2) + "℃");
                            tv_weatherinfo4.setText(weatherbean.getInfo().getDay().get(1));
                            break;
                        case 5:
                            tv_date5.setText("周" + weatherbean.getWeek());
                            tv_temperature5.setText(weatherbean.getInfo().getDay().get(2) + "℃");
                            tv_weatherinfo5.setText(weatherbean.getInfo().getDay().get(1));

                            break;
                        case 6:
                            tv_date6.setText("周" + weatherbean.getWeek());
                            tv_temperature6.setText(weatherbean.getInfo().getDay().get(2) + "℃");
                            tv_weatherinfo6.setText(weatherbean.getInfo().getDay().get(1));
                            break;
                    }

                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
//                dialog.loadingTip(getText(R.string.error_network).toString());
            }
        });
    }
}
