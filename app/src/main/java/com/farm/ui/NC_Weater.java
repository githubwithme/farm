package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;


import com.farm.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hasee on 2016/6/17.
 */
@EActivity(R.layout.nc_weater)
public class NC_Weater extends Activity
{

    @ViewById
    TextView city_name;//用于显示城市名
    @ViewById
    TextView publish_text;//用于显示发布时间
    @ViewById
    TextView weather_desp; //用于显示天气描述信息
    @ViewById
    TextView temp1; //用于显示最低气温
    @ViewById
    TextView temp2;//用于显示最高气温
    @ViewById
    TextView current_date;//用于显示当前日期
    @ViewById
    Button close_weather; //退出程序
    @ViewById
    Button refresh_weather;//更新天气按钮
    private String weatherCode;          //天气代码
    private String weatherJson;          //获取JSON格式*/

/*    //自定义变量
    private TextView cityNameText;       //用于显示城市名
    private TextView publishText;        //用于显示发布时间
    private TextView weatherDespText;    //用于显示天气描述信息
    private TextView temp1Text;          //用于显示最低气温
    private TextView temp2Text;          //用于显示最高气温
    private TextView currentDateText;    //用于显示当前日期
    private Button closeWeather;         //退出程序
    private Button refreshWeather;       //更新天气按钮
    private String weatherCode;          //天气代码
    private String weatherJson;          //获取JSON格式*/
    @Click
    void close_weather()
    {
        finish();
    }
    @Click
    void refresh_weather()
    {
//        DialogChooseCity();
        getWeather("南宁");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

    }
    private void DialogChooseCity() {
        //创建对话框
        AlertDialog.Builder  builder = new AlertDialog.Builder(NC_Weater.this);
        builder.setTitle("请选择一个城市");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        //指定下拉列表的显示数据
        final String[] cities = {"北京", "上海", "天津", "广州", "贵阳", "台北", "香港"};
        final String[] codes = {"101010100", "101020100", "101030100","101280101",
                "101260101", "101340101", "101320101"};
        builder.setItems(cities, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                weatherCode = codes[which];
                //执行线程访问http
                //否则 NetworkOnMainThreadException
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //访问中国天气网
                        String weatherUrl = "http://www.weather.com.cn/data/cityinfo/"
                                + weatherCode + ".html";
                        String url="http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&day=2&city="+"南宁"+"&dfc=3";
                        weatherJson = queryStringForGet(weatherUrl);
                        //JSON格式解析
                        try {
                            JSONObject jsonObject = new JSONObject(weatherJson);
                            JSONObject weatherObject = jsonObject
                                    .getJSONObject("weatherinfo");
                            Message message = new Message();
                            message.obj = weatherObject;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        builder.show();
    }

    /**
     * 解析Json格式数据并显示
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            JSONObject object = (JSONObject) msg.obj;
            try {
                city_name.setText(object.getString("city"));
                temp1.setText(object.getString("temp1"));
                temp2.setText(object.getString("temp2"));
                weather_desp.setText(object.getString("weather"));
                publish_text.setText("今天"+object.getString("ptime")+"发布");
                //获取当前日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
                current_date.setText(sdf.format(new Date()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    /**
     * 网络查询
     */
    private String queryStringForGet(String url) {
        HttpGet request = new HttpGet(url);
        String result = null;
        try {
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                return result;
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String getWeather(String city){
        String result=null;
        String url="http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&day=2&city="+city+"&dfc=3";
        HttpGet request = new HttpGet(url);
        try{
            HttpResponse response = new DefaultHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() == 200)
            {
                result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
