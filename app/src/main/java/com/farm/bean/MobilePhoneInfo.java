package com.farm.bean;

public class MobilePhoneInfo
{
    // 设备型号、OS版本号
    String BOARD, BOOTLOADER, BRAND, CPU_ABI, CPU_ABI2, DEVICE, DISPLAY, FINGERPRINT, HARDWARE, HOST, ID, MANUFACTURER, PRODUCT, RADIO, RADITAGSO, TIME, TYPE, USER, VERSIONCODENAME, VERSIONINCREMENTAL, VERSIONSDK_INT, UUID, PHONENUMBER, TOTALMEMORY, AVAILMEMORY, MODEL, IMSI, VERSIONSDK, VERSIONRELEASE;
    // //设备Id等信息
    String IMEI;

    // //其他信息
    // String DisplayMetrics,

    public void setMODEL(String mODEL)
    {
        MODEL = mODEL;
    }

    public String getMODEL()
    {
        return MODEL;
    }

    public String getUUID()
    {
        return UUID;
    }

    public void setUUID(String uUID)
    {
        UUID = uUID;
    }

    public void setVERSIONRELEASE(String vERSIONRELEASE)
    {
        VERSIONRELEASE = vERSIONRELEASE;
    }

    public String getVERSIONRELEASE()
    {
        return VERSIONRELEASE;
    }

    public void setIMSI(String iMSI)
    {
        IMSI = iMSI;
    }

    public String getIMSI()
    {
        return IMSI;
    }

    public void setTOTALMEMORY(String tOTALMEMORY)
    {
        TOTALMEMORY = tOTALMEMORY;
    }

    public String getTOTALMEMORY()
    {
        return TOTALMEMORY;
    }

    public void setAVAILMEMORY(String aVAILMEMORY)
    {
        AVAILMEMORY = aVAILMEMORY;
    }

    public String getAVAILMEMORY()
    {
        return AVAILMEMORY;
    }

    public void setPHONENUMBER(String pHONENUMBER)
    {
        PHONENUMBER = pHONENUMBER;
    }

    public String getPHONENUMBER()
    {
        return PHONENUMBER;
    }

    public void setVERSIONSDK(String vERSIONSDK)
    {
        VERSIONSDK = vERSIONSDK;
    }

    public String getVERSIONSDK()
    {
        return VERSIONSDK;
    }

}

// /*
// * 电话状态：
// * 1.tm.CALL_STATE_IDLE=0 无活动
// * 2.tm.CALL_STATE_RINGING=1 响铃
// * 3.tm.CALL_STATE_OFFHOOK=2 摘机
// */
// tm.getCallState();//int
// /*
// * 电话方位：
// *
// */
// tm.getCellLocation();//CellLocation
// /*
// * 唯一的设备ID：
// * GSM手机的 IMEI 和 CDMA手机的 MEID.
// * Return null if device ID is not available.
// */
// tm.getDeviceId();//String
// /*
// * 设备的软件版本号：
// * 例如：the IMEI/SV(software version) for GSM phones.
// * Return null if the software version is not available.
// */
// tm.getDeviceSoftwareVersion();//String
// /*
// * 手机号：
// * GSM手机的 MSISDN.
// * Return null if it is unavailable.
// */
// tm.getLine1Number();//String
// /*
// * 附近的电话的信息:
// * 类型：List
// * 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
// */
// tm.getNeighboringCellInfo();//List
// /*
// * 获取ISO标准的国家码，即国际长途区号。
// * 注意：仅当用户已在网络注册后有效。
// * 在CDMA网络中结果也许不可靠。
// */
// tm.getNetworkCountryIso();//String
// /*
// * MCC+MNC(mobile country code + mobile network code)
// * 注意：仅当用户已在网络注册时有效。
// * 在CDMA网络中结果也许不可靠。
// */
// tm.getNetworkOperator();//String
// /*
// * 按照字母次序的current registered operator(当前已注册的用户)的名字
// * 注意：仅当用户已在网络注册时有效。
// * 在CDMA网络中结果也许不可靠。
// */
// tm.getNetworkOperatorName();//String
// /*
// * 当前使用的网络类型：
// * 例如： NETWORK_TYPE_UNKNOWN 网络类型未知 0
// NETWORK_TYPE_GPRS GPRS网络 1
// NETWORK_TYPE_EDGE EDGE网络 2
// NETWORK_TYPE_UMTS UMTS网络 3
// NETWORK_TYPE_HSDPA HSDPA网络 8
// NETWORK_TYPE_HSUPA HSUPA网络 9
// NETWORK_TYPE_HSPA HSPA网络 10
// NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4
// NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5
// NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6
// NETWORK_TYPE_1xRTT 1xRTT网络 7
// */
// tm.getNetworkType();//int
// /*
// * 手机类型：
// * 例如： PHONE_TYPE_NONE 无信号
// PHONE_TYPE_GSM GSM信号
// PHONE_TYPE_CDMA CDMA信号
// */
// tm.getPhoneType();//int
// /*
// * Returns the ISO country code equivalent for the SIM provider's country
// code.
// * 获取ISO国家码，相当于提供SIM卡的国家码。
// *
// */
// tm.getSimCountryIso();//String
// /*
// * Returns the MCC+MNC (mobile country code + mobile network code) of the
// provider of the SIM. 5 or 6 decimal digits.
// * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
// * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
// */
// tm.getSimOperator();//String
// /*
// * 服务商名称：
// * 例如：中国移动、联通
// * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
// */
// tm.getSimOperatorName();//String
// /*
// * SIM卡的序列号：
// * 需要权限：READ_PHONE_STATE
// */
// tm.getSimSerialNumber();//String
// /*
// * SIM的状态信息：
// * SIM_STATE_UNKNOWN 未知状态 0
// SIM_STATE_ABSENT 没插卡 1
// SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2
// SIM_STATE_PUK_REQUIRED 锁定状态，需要用户的PUK码解锁 3
// SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4
// SIM_STATE_READY 就绪状态 5
// */
// tm.getSimState();//int
// /*
// * 唯一的用户ID：
// * 例如：IMSI(国际移动用户识别码) for a GSM phone.
// * 需要权限：READ_PHONE_STATE
// */
// tm.getSubscriberId();//String
// /*
// * 取得和语音邮件相关的标签，即为识别符
// * 需要权限：READ_PHONE_STATE
// */
// tm.getVoiceMailAlphaTag();//String
// /*
// * 获取语音邮件号码：
// * 需要权限：READ_PHONE_STATE
// */
// tm.getVoiceMailNumber();//String
// /*
// * ICC卡是否存在
// */
// tm.hasIccCard();//boolean
// /*
// * 是否漫游:
// * (在GSM用途下)
// */
// tm.isNetworkRoaming();//

// //BOARD 主板
// String phoneInfo = "BOARD: " + android.os.Build.BOARD;
// phoneInfo += ", BOOTLOADER: " + android.os.Build.BOOTLOADER;
// //BRAND 运营商
// phoneInfo += ", BRAND: " + android.os.Build.BRAND;
// phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
// phoneInfo += ", CPU_ABI2: " + android.os.Build.CPU_ABI2;
// //DEVICE 驱动
// phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
// //DISPLAY 显示
// phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
// //指纹
// phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
// //HARDWARE 硬件
// phoneInfo += ", HARDWARE: " + android.os.Build.HARDWARE;
// phoneInfo += ", HOST: " + android.os.Build.HOST;
// phoneInfo += ", ID: " + android.os.Build.ID;
// //MANUFACTURER 生产厂家
// phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
// //MODEL 机型
// phoneInfo += ", MODEL: " + android.os.Build.MODEL;
// phoneInfo += ", PRODUCT: " + android.os.Build.PRODUCT;
// phoneInfo += ", RADIO: " + android.os.Build.RADIO;
// phoneInfo += ", RADITAGSO: " + android.os.Build.TAGS;
// phoneInfo += ", TIME: " + android.os.Build.TIME;
// phoneInfo += ", TYPE: " + android.os.Build.TYPE;
// phoneInfo += ", USER: " + android.os.Build.USER;
// //VERSION.RELEASE 固件版本
// phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
// phoneInfo += ", VERSION.CODENAME: " + android.os.Build.VERSION.CODENAME;
// //VERSION.INCREMENTAL 基带版本
// phoneInfo += ", VERSION.INCREMENTAL: " +
// android.os.Build.VERSION.INCREMENTAL;
// //VERSION.SDK SDK版本
// phoneInfo += ", VERSION.SDK: " + android.os.Build.VERSION.SDK;
// phoneInfo += ", VERSION.SDK_INT: " + android.os.Build.VERSION.SDK_INT;
// return phoneInfo;