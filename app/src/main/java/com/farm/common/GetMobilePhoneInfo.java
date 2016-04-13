package com.farm.common;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.farm.bean.MobilePhoneInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

public class GetMobilePhoneInfo
{

    static final String PREFS_FILE = "device_id.xml";
    static final String PREFS_DEVICE_ID = "device_id";
    static UUID uuid;
    static String[] memory;

    /**
     * 获取CPU信息
     *
     * @return
     */
    public static String[] getCpuInfo()
    {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""}; // 1-cpu型号 //2-cpu频率
        String[] arrayOfString;
        try
        {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++)
            {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e)
        {
        }
        // Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return cpuInfo;
    }

    /**
     * 获取设备型号、OS版本号等信息
     *
     * @return
     */
    public static MobilePhoneInfo getinfo(Context context)
    {
        getDeviceUuid(context);
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        String ProvidersName = null;
        if (imsi.startsWith("46000") || imsi.startsWith("46002"))
        {
            ProvidersName = "中国移动";
        } else if (imsi.startsWith("46001"))
        {
            ProvidersName = "中国联通";
        } else if (imsi.startsWith("46003"))
        {
            ProvidersName = "中国电信";
        }

        MobilePhoneInfo mobilePhoneInfo = new MobilePhoneInfo();
        mobilePhoneInfo.setMODEL(android.os.Build.MODEL);
        // mobilePhoneInfo.setUUID(uuid.toString());
        // mobilePhoneInfo.setVERSIONRELEASE(android.os.Build.VERSION.RELEASE);
        mobilePhoneInfo.setVERSIONSDK(android.os.Build.VERSION.SDK);
        // mobilePhoneInfo.setAVAILMEMORY(memory[1]);
        // mobilePhoneInfo.setTOTALMEMORY(memory[0]);
        mobilePhoneInfo.setIMSI(ProvidersName);
        mobilePhoneInfo.setPHONENUMBER(numer);
        return mobilePhoneInfo;

    }

    public static String getAndroidVersion()
    {
        String anroidVersion = android.os.Build.VERSION.RELEASE;
        return anroidVersion;

    }

    public static String getAndroidSDKVersion()
    {
        String androidSDKVersion = android.os.Build.VERSION.SDK;
        return androidSDKVersion;

    }

    public static String getModel()
    {
        String model = android.os.Build.MODEL;
        return model;

    }

    /**
     * 获取手机可用内存和总内存
     */
    public static String[] getMemory(Context context)
    {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String[] memory = {"", ""}; // 1-total 2-avail
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(mi);
        long mTotalMem = 0;
        long mAvailMem = mi.availMem;
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        try
        {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            mTotalMem = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        memory[0] = Formatter.formatFileSize(context, mTotalMem);
        memory[1] = Formatter.formatFileSize(context, mAvailMem);
        // Log.i(TAG, "meminfo total:" + result[0] + " used:" + result[1]);
        return memory;
    }

    /**
     * 获取手机安装的应用信息（排除系统自带）
     *
     * @return
     */
    public static String getAllApp(Context context)
    {
        String result = "";
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo i : packages)
        {
            if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                result += i.applicationInfo.loadLabel(context.getPackageManager()).toString() + ",";
            }
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 获取手机MAC地址
     *
     * @return
     */
    public static String getMacAddress(Context context)
    {
        String result = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        // Log.i(TAG, "macAdd:" + result);
        return result;
    }

    /**
     * 获取手机屏幕高度
     */
    public static String getWeithAndHeight(Context context)
    {
        @SuppressLint("ServiceCast") WindowManager windowManager = (WindowManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 这种方式在service中无法使用，
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels; // 宽
        int height = dm.heightPixels; // 高

        // 在service中也能得到高和宽
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = mWindowManager.getDefaultDisplay().getWidth();
        height = mWindowManager.getDefaultDisplay().getHeight();
        return "width:" + width + "height:" + height;
    }

    public static UUID getDeviceUuid(Context context)
    {
        if (uuid == null)
        {
            synchronized (DeviceUuidFactory.class)
            {
                if (uuid == null)
                {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);

                    if (id != null)
                    {
                        // Use the ids previously computed and stored in the
                        // prefs file
                        uuid = UUID.fromString(id);

                    } else
                    {

                        final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

                        // Use the Android ID unless it's broken, in which case
                        // fallback on deviceId,
                        // unless it's not available, then fallback on a random
                        // number which we store
                        // to a prefs file
                        try
                        {
                            if (!"9774d56d682e549c".equals(androidId))
                            {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else
                            {
                                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException e)
                        {
                            throw new RuntimeException(e);
                        }

                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();

                    }

                }
            }
        }
        return uuid;
    }

    /**
     * 得到当前的手机网络类型
     *
     * @param context
     * @return
     */
    public static String getCurrentNetType(Context context)
    {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null)
        {
            type = "null";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI)
        {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE)
        {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS || subType == TelephonyManager.NETWORK_TYPE_EDGE)
            {
                type = "2g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0 || subType == TelephonyManager.NETWORK_TYPE_EVDO_B)
            {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE)
            {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4g";
            }
        }
        return type;
    }
}
