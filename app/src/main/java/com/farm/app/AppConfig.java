package com.farm.app;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author :sima
 * @version :1.0
 * @createTime：2015-8-6 上午10:54:13
 * @description :应用程序的配置类：用于保存用户相关信息及设置
 */
public class AppConfig
{
    private final static String APP_CONFIG = "config";
    public final static String CONF_VOICE = "perf_voice";
    public static String DOWNLOADPATH_VIDEO = Environment.getExternalStorageDirectory().getPath() + "/bhq/FJ/VIDEO/"; /* 事件附件下载到本地的路径 */
    /* 多媒体制作保存路径 */
    public static String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/Farm/MEDIA/";
//    	 public static String baseurl = "http://192.168.31.163:8055/";//
    // 注意将8055端口添加到防火墙规则中
//    public static String baseurl = "http://www.farmm.cn/";// 注意将8055端口添加到防火墙规则中
    //	 public static String baseurl = "http://192.168.23.1:8055/";//
	 public static String baseurl = "http://192.168.1.6:8055/";//
    // 注意将8055端口添加到防火墙规则中
    public static String testurl = baseurl + "webService.ashx";
    public static String uploadurl = baseurl + "tools/upload_ajax.ashx";
    public static String apkpath = Environment.getExternalStorageDirectory().getPath() + "/Farm/APK/";
    public static String audiopath = Environment.getExternalStorageDirectory().getPath() + "/Farm/AUDIO/";
    public static String imagepath = Environment.getExternalStorageDirectory().getPath() + "/Farm/IMAGE/";
    public static String videopath = Environment.getExternalStorageDirectory().getPath() + "/Farm/VIDEO/";
    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context)
    {
        if (appConfig == null)
        {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    public String get(String key)
    {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get()
    {
        FileInputStream fis = null;
        Properties props = new Properties();
        try
        {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);

            props.load(fis);
        } catch (Exception e)
        {
        } finally
        {
            try
            {
                fis.close();
            } catch (Exception e)
            {
            }
        }
        return props;
    }
}
