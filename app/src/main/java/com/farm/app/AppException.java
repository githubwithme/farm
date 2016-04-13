package com.farm.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.bean.ExceptionInfo;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.GetMobilePhoneInfo;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

/**
 * @author :sima
 * @version :
 * @createTime：2015-8-6 上午11:36:38
 * @description :UncaughtException处理类,当程序发生Uncaught异常的时候,用户可以自己处理也可使用系统默认处理;当程序发生自定义异常时
 * ，保存异常，并提示发送错误报告. 仅debug模式开启记录错误日志
 */
public class AppException implements UncaughtExceptionHandler
{
    /**
     * Debug Log Tag
     */
    public static final String TAG = "CrashHandler";
    /**
     * 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能
     */
    public static final boolean DEBUG = true;
    /**
     * CrashHandler实例
     */
    private static AppException INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".txt";

    /**
     * 保证只有一个CrashHandler实例
     */
    private AppException()
    {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static AppException getInstance()
    {
        if (INSTANCE == null) INSTANCE = new AppException();
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx)
    {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            // mDefaultHandler.uncaughtException(thread, ex);

            // Sleep一会后结束程序，来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle(R.string.app_error);
            builder.setMessage(R.string.app_error_relogin);
            builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    // 退出
                    AppManager.getAppManager().AppExit(mContext);
                }
            });
            builder.show();
        }

    }

    // 三个参数
    // 第一个是文件名字
    // 第二个是文件存放的目录
    // 第三个是文件内容
    public static void writeToApplicationFile(String file, File destDir, String szOutText)
    {
        File myFile = null;
        try
        {
            myFile = new File(destDir + File.separator + file); // 打开文件

            if (myFile.exists()) // 判断文件是否存在,存在则删除
            {
                myFile.delete();
            }
            myFile.createNewFile();// 创建文件
            FileOutputStream outputStream = new FileOutputStream(myFile, true); // 写数据
            // 注意这里，两个参数，第一个是写入的文件，第二个是指是覆盖还是追加，
            // //默认是覆盖的，就是不写第二个参数，这里设置为true就是说不覆盖，是在后面追加。
            outputStream.write(szOutText.getBytes());// 写入内容
            outputStream.close();// 关闭流
        } catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex)
    {
        if (ex == null)
        {
            return true;
        }
        final String msg = ex.getLocalizedMessage();
        // 收集设备信息
        collectCrashDeviceInfo(mContext);
        // 保存错误报告文件
        String crashFileName = saveCrashInfoToFile(ex);
        // 发送错误报告到服务器
//        sendCrashReportsToServer(mContext);

        return true;
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx)
    {
        try
        {
            // Class for retrieving various kinds of information related to the
            // application packages that are currently installed on the device.
            // You can find this class through getPackageManager().
            PackageManager pm = ctx.getPackageManager();
            // getPackageInfo(String packageName, int flags)
            // Retrieve overall information about an application package that is
            // installed on the system.
            // public static final int GET_ACTIVITIES
            // Since: API Level 1 PackageInfo flag: return information about
            // activities in the package in activities.
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null)
            {
                // public String versionName The version name of this package,
                // as specified by the <manifest> tag's versionName attribute.
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                // public int versionCode The version number of this package,
                // as specified by the <manifest> tag's versionCode attribute.
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
            }
        } catch (NameNotFoundException e)
        {
            Log.e(TAG, "Error while collect package info", e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                // setAccessible(boolean flag)
                // 将此对象的 accessible 标志设置为指示的布尔值。
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null));
                if (DEBUG)
                {
                    Log.d(TAG, field.getName() + " : " + field.get(null));
                }
            } catch (Exception e)
            {
                Log.e(TAG, "Error while collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private String saveCrashInfoToFile(Throwable ex)
    {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        // printStackTrace(PrintWriter s)
        // 将此 throwable 及其追踪输出到指定的 PrintWriter
        ex.printStackTrace(printWriter);

        // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        // toString() 以字符串的形式返回该缓冲区的当前值。
        PackageInfo packageInfo = null;
        try
        {
            packageInfo = mContext.getApplicationContext().getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }

//        StringBuilder result=new StringBuilder();
//        result.append("##应用程序发生错误##"+ utils.getTime()+"***\n"+"网络状态:"+GetMobilePhoneInfo.getCurrentNetType(mContext)+"***\n"+"手机型号:"+GetMobilePhoneInfo.getModel()+"***\n"+"手机系统版本:"+GetMobilePhoneInfo.getAndroidVersion()+"***\n"+"SDK版本:"+GetMobilePhoneInfo.getAndroidSDKVersion()+"***\n"+"CPU信息:"+GetMobilePhoneInfo.getCpuInfo()[0]+"***\n"+"总内存:"+GetMobilePhoneInfo.getMemory(mContext)[0]+"可用内存:"+GetMobilePhoneInfo.getMemory(mContext)[1]+"***\n");
//        result.append(String.valueOf(info));
        String result = "######应用程序发生错误:" +utils.getTime() + "***\n" + "网络状态:" + GetMobilePhoneInfo.getCurrentNetType(mContext) + "***\n" + "手机型号:" + GetMobilePhoneInfo.getModel() + "***\n" + "手机系统版本:" + GetMobilePhoneInfo.getAndroidVersion() + "***\n" + "SDK版本:" + GetMobilePhoneInfo.getAndroidSDKVersion() + "***\n" + "CPU信息:" + GetMobilePhoneInfo.getCpuInfo()[0] + "***\n" + "总内存:" + GetMobilePhoneInfo.getMemory(mContext)[0] + "可用内存:" + GetMobilePhoneInfo.getMemory(mContext)[1] + "***\n" + String.valueOf(info);
        printWriter.close();
        mDeviceCrashInfo.put(STACK_TRACE, result.toString());
        try
        {
            long timestamp = System.currentTimeMillis();
            String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
            // 保存文件
            File destDirStr = mContext.getFilesDir();
            writeToApplicationFile(fileName, destDirStr, result.toString());//保存手机本地
            saveExceptionInfoInSqlite(mContext, result);
//            sendExceptionInfoToServer(mContext, result.toString());//保存远程数据库
//            Intent intent_log = new Intent(mContext, SendExceptionInfoToSerer.class);
//            intent_log.putExtra("info", result);
//            intent_log.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startService(intent_log);
//            Log.e(TAG, "应用程序发生错误:" + result.toString(), ex);//记录日志
//			FileOutputStream trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
//			mDeviceCrashInfo.store(trace, "");
//			trace.flush();
//			trace.close();
            return fileName;
        } catch (Exception e)
        {
            Log.e(TAG, "an error occured while writing report file...", e);
            saveExceptionInfoInSqlite(mContext, result);
//            sendExceptionInfoToServer(mContext, e.getMessage());//保存远程数据库
        }
        return null;
    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     *
     * @param ctx
     */
    private void sendCrashReportsToServer(Context ctx)
    {
        String[] crFiles = getCrashReportFiles(ctx);
        if (crFiles != null && crFiles.length > 0)
        {
            TreeSet<String> sortedFiles = new TreeSet<String>();
            sortedFiles.addAll(Arrays.asList(crFiles));

            for (String fileName : sortedFiles)
            {
                File cr = new File(ctx.getFilesDir(), fileName);
                postReport(cr);
                cr.delete();// 删除已发送的报告
            }
        }
    }

    private void saveExceptionInfoInSqlite(Context ctx, String info)
    {
        String userid="10000";
        String username="未知姓名";
        String exceptionid=java.util.UUID.randomUUID().toString();
        commembertab commembertab = AppContext.getUserInfo(mContext);
        if (commembertab != null)
        {
            userid=commembertab.getId();
            username=commembertab.getrealName();
        }
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setId("");
        exceptionInfo.setUuid(GetMobilePhoneInfo.getDeviceUuid(ctx).toString());
        exceptionInfo.setExceptionid(exceptionid);
        exceptionInfo.setExceptionInfo(info);
        exceptionInfo.setUserid(userid);
        exceptionInfo.setUsername(username);
        exceptionInfo.setRegtime(utils.getTime());
        exceptionInfo.setIsSolve("0");
        SqliteDb.save(ctx, exceptionInfo);
    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     *
     * @param ctx
     */
    private void sendExceptionInfoToServer(Context ctx, String info)
    {
        commembertab commembertab = AppContext.getUserInfo(mContext);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("UUID", GetMobilePhoneInfo.getDeviceUuid(ctx).toString());
        params.addQueryStringParameter("exceptionInfo", info);
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("action", "saveAppException");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        Toast.makeText(mContext, "已处理异常信息！", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        AppContext.makeToast(mContext, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(mContext, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1)
            {
                AppContext.makeToast(mContext, "error_connectServer");
                AppContext.makeToast(mContext, "error_connectServer");
            }
        });
    }

    /**
     * @description:提示发送错误报告
     * @createTime：2015-8-6 下午1:04:05
     */
    private void sendCrashReportDialog(final Context cont, final String crashReport)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.app_error);
        builder.setMessage(R.string.app_error_message);
        builder.setPositiveButton(R.string.submit_report, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                sendCrashReportsToServer(cont);
                // // 发送异常报告
                // Intent i = new Intent(Intent.ACTION_SEND);
                // // i.setType("text/plain"); //模拟器
                // i.setType("message/rfc822"); // 真机
                // i.putExtra(Intent.EXTRA_EMAIL,
                // new String[] { "jxsmallmouse@163.com" });
                // i.putExtra(Intent.EXTRA_SUBJECT,
                // "开源中国Android客户端 - 错误报告");
                // i.putExtra(Intent.EXTRA_TEXT, crashReport);
                // cont.startActivity(Intent.createChooser(i,
                // "发送错误报告"));
                // 退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.show();
    }

    /**
     * 获取错误报告文件名
     *
     * @param ctx
     * @return
     */
    private String[] getCrashReportFiles(Context ctx)
    {
        File filesDir = ctx.getFilesDir();
        // 实现FilenameFilter接口的类实例可用于过滤器文件名
        FilenameFilter filter = new FilenameFilter()
        {
            // accept(File dir, String name)
            // 测试指定文件是否应该包含在某一文件列表中。
            public boolean accept(File dir, String name)
            {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        // list(FilenameFilter filter)
        // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录
        return filesDir.list(filter);
    }

    private void postReport(File file)
    {
        // TODO 使用HTTP Post 发送错误报告到服务器
        // 这里不再详述,开发者可以根据OPhoneSDN上的其他网络操作
        // 教程来提交错误报告
    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    public void sendPreviousReportsToServer()
    {
        sendCrashReportsToServer(mContext);
    }

}
