package com.farm.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.widget.Toast;

import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.farm.common.StringUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 2015-8-6 下午1:50:43
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class AppContext extends Application
{
    public final static String BELONG_ADD_CMD_AREA = "BELONG_ADD_CMD_AREA";
    public final static String ACTION_CURRENTITEM = "ACTION_CURRENTITEM";
    public final static String ACTION_REFRESH_ANIMAL = "REFRESH_ANIMAL";
    public final static String ACTION_REFRESH_PEST = "REFRESH_PEST";
    public final static String ACTION_REFRESH_INTERFERE = "REFRESH_INTERFERE";
    public final static String ACTION_REFRESH_FACILITY = "REFRESH_FACILITY";
    public final static String ACTION_REFRESH_PLANT = "REFRESH_PLANT";
    public final static String BROADCAST_MAP_MORE = "MAP_MORE";
    public final static String BROADCAST_OPENDL = "OPENDL";
    public final static String BROADCAST_UPDATEBREAKOFFINFO = "UPDATEBREAKOFFINFO";
    public final static String BROADCAST_UPDATEPLANT = "UPDATEPLANT";
    public final static String BROADCAST_UPDATEAllORDER= "BROADCAST_UPDATEAllORDER";
    public final static String BROADCAST_UPDATENEWSALELIST= "BROADCAST_UPDATENEWSALELIST";
    public final static String BROADCAST_UPDATESELLORDER= "BROADCAST_UPDATESELLORDER";
    public final static String BROADCAST_FINISH= "BROADCAST_FINISH";
    public final static String BROADCAST_UPDATESALENUMBER= "BROADCAST_UPDATESALENUMBER";
    public final static String BROADCAST_UPDATENOTPAYORDER= "BROADCAST_UPDATENOTPAYORDER";
    public final static String BROADCAST_UPDATEDEALINGORDER= "BROADCAST_UPDATEDEALINGORDER";
    //	public final static String BROADCAST_UPDATECMD_SELECT = "UPDATECMD_SELECT";
    public final static String BROADCAST_UPDATEPCMD_SORT = "UPDATEPCMD_SORT";
    public final static String BROADCAST_UPDATEORDER = "BROADCAST_UPDATEORDER";
    public final static String BROADCAST_UPDATEALLORDER_CZ = "BROADCAST_UPDATEALLORDER_CZ";
    public final static String BROADCAST_UPDATENEEDFEEDBACKORDER_CZ = "BROADCAST_UPDATENEEDFEEDBACKORDER_CZ";
    public final static String BROADCAST_ADDWORK = "ADDWORK";
    public final static String BROADCAST_SELECTOR = "SELECTOR";
    public final static String BROADCAST_REFRESHRECORD = "REFRESHRECORD";
    public final static String BROADCAST_SHOWDIALOG = "BROADCAST_SHOWDIALOG";
    public final static String BROADCAST_PG_UPEVENT = "PG_UPEVENT";
    public final static String BROADCAST_PG_REFASH = "PG_REASH";
    public final static String TAG_NCZ_CMD = "TAG_NCZ_CMD";
    public final static String BROADCAST_PG_DATA = "PG_DATA";
    public final static String BROADCAST_Record = "EVENT_RECORD";

    public final static int TIME_REFRESH = 10000;
    public final static int TIME_GZ = 60000;

    public final static int TIME_STOPWATCH = 1001;
    public final static int DISTANCEBETWEEN = 1002;
    public static final int PAGE_SIZE = 10;// 默认分页大小
    public static final int PAGE_SIZE_RECORD = 10000;// 默认分页大小
    public static final int PAGE_SIZE_YQPQ = 10000;// 默认分页大小
    String LOCATION_X = "";// 当前坐标
    String LOCATION_Y = "";// 当前坐标

    @Override
    public void onCreate()
    {
        super.onCreate();
        AppException appException = AppException.getInstance();
        // 注册crashHandler
        appException.init(getApplicationContext());
        // 发送以前没发送的报告(可选)
        appException.sendPreviousReportsToServer();
    }

    public void setLOCATION_X(String lOCATION_X)
    {
        LOCATION_X = lOCATION_X;
    }

    public String getLOCATION_X()
    {
        return LOCATION_X;
    }

    public void setLOCATION_Y(String lOCATION_Y)
    {
        LOCATION_Y = lOCATION_Y;
    }

    public String getLOCATION_Y()
    {
        return LOCATION_Y;
    }

    /**
     * 应用程序是否发出提示音
     *
     * @return
     */
    public boolean isAppSound()
    {
        return isAudioNormal() && isVoice();
    }

    /**
     * 检测当前系统声音是否为正常模式
     *
     * @return
     */
    public boolean isAudioNormal()
    {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 是否发出提示音
     *
     * @return
     */
    public boolean isVoice()
    {
        String perf_voice = getProperty(AppConfig.CONF_VOICE);
        // 默认是开启提示声音
        if (StringUtils.isEmpty(perf_voice)) return true;
        else return StringUtils.toBool(perf_voice);
    }

    public String getProperty(String key)
    {
        return AppConfig.getAppConfig(this).get(key);
    }

    /**
     * @param ctx  上下文
     * @param type 提示类型
     * @description:提示信息
     * @createTime：2015-8-11 上午10:43:42
     */
    public static void makeToast(Context ctx, String type)
    {
        if (type.equals("load_error"))
        {
            Toast.makeText(ctx, "加载异常!", Toast.LENGTH_SHORT).show();
        } else if (type.equals("error_connectDataBase"))
        {
            Toast.makeText(ctx, "连接数据库异常！", Toast.LENGTH_SHORT).show();
        } else if (type.equals("error_connectServer"))
        {
            Toast.makeText(ctx, "连接服务器异常！", Toast.LENGTH_SHORT).show();
        } else if (type.equals("exception_network"))
        {
            Toast.makeText(ctx, "网络异常！", Toast.LENGTH_SHORT).show();
        } else if (type.equals(""))
        {
            Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
        } else if (type.equals(""))
        {
            Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
        } else if (type.equals(""))
        {
            Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
        }
    }

    public static commembertab getUserInfo(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("userInfo", MODE_PRIVATE);
        String userName = sp.getString("userName", "");
        commembertab commembertab = (commembertab) SqliteDb.getCurrentUser(context, commembertab.class, userName);
        return commembertab;
    }

    public static Typeface getTypeface(Context context, String front)
    {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), front);
        return customFont;
    }

    public static void updateStatus(Context context, String comLX, String jobID, String comID, String workuserid)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("workuserid", workuserid);
        params.addQueryStringParameter("comID", comID);
        params.addQueryStringParameter("jobID", jobID);
        params.addQueryStringParameter("comLX", comLX);
        params.addQueryStringParameter("action", "updateView");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;

            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
            }
        });
    }


    public static void eventStatus(Context context, String type, String eventID, String userID )
    {
        //
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("id", eventID );
        params.addQueryStringParameter("userId", userID);
        params.addQueryStringParameter("action", "deleteflashStr");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;

            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
            }
        });
    }
}
