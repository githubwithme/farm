package com.farm.common;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.bean.BatchOfProduct;
import com.farm.bean.BatchTimeBean;
import com.farm.bean.CusPoint;
import com.farm.bean.DynamicBean;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2015-8-9 下午9:27:19
 * 常用工具类
 */
public class utils
{
    public static Bitmap drawable2Bitmap(Drawable drawable)
    {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String getTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static String getHours()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String getToday()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String getYear()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String parseDateToDateString(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String str = formatter.format(date);
        return str;
    }

    public static String getTodayAndwWeek()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd EEEE");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static String getIntervalTime(String time1, String time2)
    {
        String dt = new String();
        if (time1.equals("") || time1.equals("null") || time2.equals("") || time2.equals("null"))
        {
            return "";
        }
        try
        {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date begin = dfs.parse(time1);
            java.util.Date end = dfs.parse(time2);

            if (end.before(begin))
            {
                dt = "-1";
                return dt;
            }
            long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
            long day1 = between / (24 * 3600);
            dt = String.valueOf(day1);
        } catch (Exception e)
        {
        }
        return dt;
    }

    public static String getDayDifference(String time1, String time2)
    {
        String dt = new String();
        if (time1.equals("") || time1.equals("null") || time2.equals("") || time2.equals("null"))
        {
            return "";
        }
        try
        {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date begin = dfs.parse(time1);
            java.util.Date end = dfs.parse(time2);

            if (end.before(begin))
            {
                dt = "已到期";
                return dt;
            }
            long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
            long day1 = between / (24 * 3600);
            dt = "距离" + day1 + "天";
        } catch (Exception e)
        {
        }
        return dt;
    }

    public static String OffSetOfDate(String today, String date)
    {
        String dt = new String();
        if (today.equals("") || today.equals("null") || date.equals("") || date.equals("null"))
        {
            return "";
        }
        try
        {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date_today = dfs.parse(today);
            java.util.Date date_date = dfs.parse(date);

            long between = (date_today.getTime() - date_date.getTime()) / 1000;// 除以1000是为了转换成秒
            long day1 = between / (24 * 3600);
            if (date_date.before(date_today))
            {
                if (day1 == 1)
                {
                    dt = "昨天" + date.substring(date.lastIndexOf(" "), date.lastIndexOf(":"));
                } else if (day1 == 2)
                {
                    dt = "前天";
                } else if (day1 == 3)
                {
                    dt = "3天前";
                } else if (day1 >= 7)
                {
                    dt = "一周前";
                }
                return dt;
            } else
            {
                dt = date.substring(date.lastIndexOf(" "), date.lastIndexOf(":"));
            }


        } catch (Exception e)
        {
        }
        return dt;
    }

    public static List<DynamicBean> BubbleSortArray(List<DynamicBean> list)
    {
        int n = list.size();
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n - i-1; j++)
            {
                if (list.get(j).getListdata().size()>0&&list.get(j+1).getListdata().size()>0)
                {
                    String date1 = list.get(j).getListdata().get(0).getDate();
                    String date2 = list.get(j + 1).getListdata().get(0).getDate();
                    date1 = date1.replace("/", "-");
                    date2 = date2.replace("/", "-");
                    Date date_date1 = null;
                    Date date_date2 = null;
                    try
                    {
                        date_date1 = dfs.parse(date1);
                        date_date2 = dfs.parse(date2);
                    } catch (ParseException e)
                    {
                        e.printStackTrace();
                    }

                    if (date_date1.before(date_date2))//比较交换相邻元素
                    {
                        DynamicBean temp;
                        temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                    }
                }else if (list.get(j).getListdata().size()==0&&list.get(j+1).getListdata().size()>0)
                {
                    DynamicBean temp;
                    temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }

    public static int getDayAcount(String time1, String time2)
    {
        int dt = 0;
        if (time1.equals("") || time1.equals("null") || time2.equals("") || time2.equals("null"))
        {
            return 0;
        }
        try
        {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date begin = dfs.parse(time1);
            java.util.Date end = dfs.parse(time2);

            if (end.before(begin))
            {
                dt = 0;
                return dt;
            }
            int between = (int) ((end.getTime() - begin.getTime()) / 1000);// 除以1000是为了转换成秒
            int day1 = between / (24 * 3600);
            dt = day1;
        } catch (Exception e)
        {
        }
        return dt;
    }

    /**
     * 随机指定范围内N个不重复的数 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     */
    public static Double[] randomCommon(Double min, Double max, int n)
    {
        if (n > (max - min + 1) || max < min)
        {
            return null;
        }
        Double[] result = new Double[n];
        int count = 0;
        while (count < n)
        {
            Double num = (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++)
            {
                if (num == result[j])
                {
                    flag = false;
                    break;
                }
            }
            if (flag)
            {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    public static int[] randomInt(int min, int max, int n)
    {
        if (n > (max - min + 1) || max < min)
        {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n)
        {
            int num = (int) ((Math.random() * (max - min)) + min);
            boolean flag = true;
            for (int j = 0; j < n; j++)
            {
                if (num == result[j])
                {
                    flag = false;
                    break;
                }
            }
            if (flag)
            {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    /**
     * @param mss
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss)
    {
        String h = new String();
        String m = new String();
        String s = new String();
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if (hours != 0)
        {
            if (hours < 10)
            {
                h = "0" + hours;
            }
        } else
        {
            h = "00";
        }
        if (minutes != 0)
        {
            if (minutes < 10)
            {
                m = "0" + minutes;
            }
        } else
        {
            m = "00";
        }
        if (seconds != 0)
        {
            if (seconds < 10)
            {
                s = "0" + seconds;
            } else
            {
                s = String.valueOf(seconds);
            }
        } else
        {
            s = "00";
        }
        return h + " : " + m + " : " + s;
    }

    public static int getStatusHeight(Context context)
    {

        int statusHeight = -1;
        try
        {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network)
        {
            return true;
        }

        return false;
    }

    /**
     * 强制帮用户打开GPS
     */
    public static final void openGPS(Context context)
    {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try
        {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e)
        {
            e.printStackTrace();
        }
    }

    public static int openGPSSettings(Activity context)
    {
        // 获取位置管理服务
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
        {
            // Toast.makeText(context, "GPS正常", Toast.LENGTH_SHORT).show();
            return 1;
        }
        // Boolean status = false;
        Toast.makeText(context, "GPS定位服务还没打开，请开启GPS！", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        // context.startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
        return 0;
    }

    /**
     * 从assetts文件夹中读取json文件，然后转化为json对象
     *
     * @param jsonFileName json文件名
     */
    public static JSONObject parseJsonFile(Context context, String jsonFileName)
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            InputStream is = context.getAssets().open(jsonFileName);
            int len = -1;
            byte[] buf = new byte[1024 * 1024];
            while ((len = is.read(buf)) != -1)
            {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            JSONObject jsonObject = JSONObject.parseObject(sb.toString());
            return jsonObject;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRate(int a, int b)
    {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) a / (float) b * 100);
        return result;
    }

    public static String getRateoffloat(float a, float b)
    {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) a / (float) b * 100);
        return result;
    }

    public static List<BatchTimeBean> getBatchTime(Context context, String contractid, String dateString, int timeInterval)
    {
        String day_last = dateString;
        String startDay = dateString;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BatchTimeBean> list_time = new ArrayList<>();
        try
        {
            Date sd = sdf.parse(startDay);
            Calendar c = Calendar.getInstance();
            c.setTime(sd);
            for (int i = 0; i < 12; i++)
            {
                BatchTimeBean batchTimeBean = new BatchTimeBean();
                c.add(Calendar.DATE, timeInterval);
                String batchTime = day_last + "至" + sdf.format(c.getTime());
                boolean isexist = SqliteDb.isExistBatch(context, contractid, batchTime);
                if (!isexist)
                {
                    batchTimeBean.setIsexist("0");
                    batchTimeBean.setBatchtime(batchTime);
                    list_time.add(batchTimeBean);
                }
                day_last = sdf.format(c.getTime());
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return list_time;
    }

    public static int getBatchColorByName(String colorname)
    {
        if (colorname.equals("青色"))
        {
            return Color.argb(1000, 0, 255, 255);
        } else if (colorname.equals("灰色"))
        {
            return Color.argb(1000, 192, 192, 192);
        } else if (colorname.equals("深橄榄绿"))
        {
            return Color.argb(1000, 79, 79, 47);
        } else if (colorname.equals("土黄色"))
        {
            return Color.argb(1000, 159, 159, 95);
        } else if (colorname.equals("绿色"))
        {
            return Color.argb(1000, 0, 255, 0);
        }
        return 0;
    }

    public static int returnBatchColorByName(String colorname)
    {
        if (colorname.equals("青色"))
        {
            return R.color.navyblue;
        } else if (colorname.equals("灰色"))
        {
            return R.color.curegray;
        } else if (colorname.equals("深橄榄绿"))
        {
            return R.color.darkOlivegreen;
        } else if (colorname.equals("土黄色"))
        {
            return R.color.soilyellow;
        }
        return 0;
    }

    public static void getAllBatchTime(Context context, String dateString, int timeInterval)
    {
        String day_last = dateString;
        String startDay = dateString;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date sd = sdf.parse(startDay);
            Calendar c = Calendar.getInstance();
            c.setTime(sd);
            for (int i = 0; i < 12; i++)
            {
                BatchOfProduct batchOfProduct = new BatchOfProduct();
                c.add(Calendar.DATE, timeInterval);
                String batchTime = day_last + "至" + sdf.format(c.getTime());
                batchOfProduct.setBatchTime(batchTime);
                batchOfProduct.setuId("60");
                batchOfProduct.setYear(utils.getYear());
                SqliteDb.save(context, batchOfProduct);
                day_last = sdf.format(c.getTime());
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public static String getDateAfterNDay(String dateString, int timeInterval)
    {
        String day = "";
        String startDay = dateString;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date sd = sdf.parse(startDay);
            Calendar c = Calendar.getInstance();
            c.setTime(sd);
            c.add(Calendar.DATE, timeInterval);
            day = dateString + "至" + sdf.format(c.getTime());
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return day;
    }

    public static String[] getMoreDays(String dateString)
    {
        String startDay = dateString;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] result = new String[7];
        try
        {
            Date sd = sdf.parse(startDay);
            Calendar c = Calendar.getInstance();
            c.setTime(sd);
            result[0] = sdf.format(c.getTime());
            for (int i = 1; i < result.length; i++)
            {
                c.add(Calendar.DAY_OF_MONTH, 1);
                result[i] = sdf.format(c.getTime());
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static void setGridViewHeight(GridView gridView)
    {
        ListAdapter listAdapter = gridView.getAdapter();
        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getMeasuredHeight() * (listAdapter.getCount() - 1));
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    public static void setListViewHeight(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static int getListViewHeight(ExpandableListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    public static int CheckInPloy(Point[] pts, int n, Point pt) //pts为多边形的顶点数组，n为多边形顶点数，pt为将要被判断的点
    {
        int i, j, k, wn = 0;
        for (i = n - 1, j = 0; j < n; i = j, j++)
        {
            k = (pt.x - pts[i].x) * (pts[j].y - pts[i].y) - (pts[j].x - pts[i].x) * (pt.y - pts[i].y);
            if ((pt.y >= pts[i].y && pt.y <= pts[j].y) || (pt.y <= pts[i].y && pt.y >= pts[j].y))
            {
                if (k < 0) wn++;
                else if (k > 0) wn--;
                else
                {
                    if ((pt.y <= pts[i].y && pt.y >= pts[j].y && pt.x <= pts[i].x && pt.x >= pts[j].x) ||
                            (pt.y <= pts[i].y && pt.y >= pts[j].y && pt.x >= pts[i].x && pt.x <= pts[j].x) ||
                            (pt.y >= pts[i].y && pt.y <= pts[j].y && pt.x <= pts[i].x && pt.x >= pts[j].x) ||
                            (pt.y >= pts[i].y && pt.y <= pts[j].y && pt.x >= pts[i].x && pt.x <= pts[j].x))
                        return 0; //点在多边形边界上
                }

            }
        }
        if (wn == 0) return 1; //点在多边形外部
        else return -1; //点在多边形内部
    }

    /**
     * 判断点是否在线上
     *
     * @return
     */
    public static boolean isPointInLine(Point touchpoint, Point point1, Point point2)
    {
//        Point(" + x + ", " + y + ")
//        String p=point.toString();
//        String[] str=p.split(",");
//        int x=Integer.valueOf(str[0].substring(6, str[0].length()));
//        int y=Integer.valueOf(str[1].substring(0, str[1].length()-1));

//        double a,b,x1,x2,y1,y2;
//        scanf("%lf%lf",&a,&b);          //Q(a,b)
//        scanf("%lf%lf",&x1,&y1);        //P1(x1,y1)
//        scanf("%lf%lf",&x2,&y2);        //P2(x2,y2)
//        double s1=a-x1, t1=b-y1;        //Q-P1(s1,t1)
//        double s2=x1-x2,t2=y1-y2;       //P1-P2(s2,t2)
//        printf("%s\n",s1/s2==t1/t2?"Yes":"No");

//        double s1=touchpoint.x-point1.x;
//        double t1=touchpoint.y-point1.y;        //Q-P1(s1,t1)
//        double s2=point1.x-point2.x;
//        double t2=point1.y-point2.y;       //P1-P2(s2,t2)
//        double aa=s1/s2;
//        double bb=t1/t2;
//        if (s1/s2==t1/t2)
//        {
//            return true;
//        }else
//        {
//            return false;
//        }

        boolean pdline = (touchpoint.x - point1.x) * (point1.y - point2.y) == (point1.x - point2.x) * (touchpoint.y - point1.y);
        if (pdline)
        {
            System.out.println("您输入的点在该条直线上");
        } else
        {
            System.out.println("您输入的点不在该条直线上");
        }
        return false;
    }

    // 求两条平面直线的交点
    public static CusPoint intersectPoint(CusPoint a, CusPoint b, CusPoint c, CusPoint d)
    {
        float deita = (b.x - a.x) * (c.y - d.y) - (d.x - c.x) * (a.y - b.y);
        float x = (c.y * d.x - c.x * d.y) * (b.x - a.x) - (a.y * b.x - a.x * b.y) * (d.x - c.x);
        x /= deita;
        float y = (a.y * b.x - a.x * b.y) * (c.y - d.y) - (c.y * d.x - c.x * d.y) * (a.y - b.y);
        y /= deita;

        return new CusPoint(x, y);
    }

    //求两直线的交点，斜率相同的话res=u.a
//    Point intersection(Line u, Line v)
//    {
//        Point res = u.a;
//        double t = ((u.a.x - v.a.x) * (v.b.y - v.a.y) - (u.a.y - v.a.y) * (v.b.x - v.a.x)) / ((u.a.x - u.b.x) * (v.b.y - v.a.y) - (u.a.y - u.b.y) * (v.b.x - v.a.x));
//        res.x += (u.b.x - u.a.x) * t;
//        res.y += (u.b.y - u.a.y) * t;
//        return res;
//    }

    /**
     * 求线段交点（非直线）
     * 测试完全可用
     *
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @return
     */
    public static CusPoint getCrossPoint(CusPoint p1, CusPoint p2, CusPoint p3, CusPoint p4)
    {
        float x;
        float y;
        float x1 = p1.x;
        float y1 = p1.y;
        float x2 = p2.x;
        float y2 = p2.y;
        float x3 = p3.x;
        float y3 = p3.y;
        float x4 = p4.x;
        float y4 = p4.y;
        float k1 = Float.MAX_VALUE;
        float k2 = Float.MAX_VALUE;
        boolean flag1 = false;
        boolean flag2 = false;
        if ((x1 - x2) == 0) flag1 = true;
        if ((x3 - x4) == 0) flag2 = true;
        if (!flag1) k1 = (y1 - y2) / (x1 - x2);
        if (!flag2) k2 = (y3 - y4) / (x3 - x4);
        if (k1 == k2) return null;
        if (flag1)
        {
            if (flag2) return null;
            x = x1;
            if (k2 == 0)
            {
                y = y3;
            } else
            {
                y = k2 * (x - x4) + y4;
            }
        } else if (flag2)
        {
            x = x3;
            if (k1 == 0)
            {
                y = y1;
            } else
            {
                y = k1 * (x - x2) + y2;
            }
        } else
        {
            if (k1 == 0)
            {
                y = y1;
                x = (y - y4) / k2 + x4;
            } else if (k2 == 0)
            {
                y = y3;
                x = (y - y2) / k1 + x2;
            } else
            {
                x = (k1 * x2 - k2 * x4 + y4 - y2) / (k1 - k2);
                y = k1 * (x - x2) + y2;
            }
        }
        if (between(x1, x2, x) && between(y1, y2, y) && between(x3, x4, x) && between(y3, y4, y))
        {
            CusPoint point = new CusPoint(x, y);
            if (point.equals(p1) || point.equals(p2)) return null;
            return point;
        } else
        {
            return null;
        }
    }

    public static boolean between(float a, float b, float target)
    {
        if (target >= a - 0.01 && target <= b + 0.01 || target <= a + 0.01 && target >= b - 0.01)
            return true;
        else return false;
    }

    public static double getDistance(float x1, float y1, float x2, float y2)
    {
        double d = (x2 - x1) * (x2 - x1) - (y2 - y1) * (y2 - y1);
        return Math.sqrt(d);
    }

    /**
     * 判断两条线是否相交 a 线段1起点坐标 b 线段1终点坐标 c 线段2起点坐标 d 线段2终点坐标 intersection 相交点坐标
     * reutrn 是否相交: 0 : 两线平行 -1 : 不平行且未相交 1 : 两线相交
     */

    public static CusPoint GetIntersection(CusPoint a, CusPoint b, CusPoint c, CusPoint d)
    {
        CusPoint intersection = new CusPoint(0, 0);

        if (Math.abs(b.y - a.y) + Math.abs(b.x - a.x) + Math.abs(d.y - c.y) + Math.abs(d.x - c.x) == 0)
        {
            if ((c.x - a.x) + (c.y - a.y) == 0)
            {
                System.out.println("ABCD是同一个点！");
            } else
            {
                System.out.println("AB是一个点，CD是一个点，且AC不同！");
            }
//            return 0;
            return null;
        }


        if (Math.abs(b.y - a.y) + Math.abs(b.x - a.x) == 0)
        {
            if ((a.x - d.x) * (c.y - d.y) - (a.y - d.y) * (c.x - d.x) == 0)
            {
                System.out.println("A、B是一个点，且在CD线段上！");
            } else
            {
                System.out.println("A、B是一个点，且不在CD线段上！");
            }
//            return 0;
            return null;
        }
        if (Math.abs(d.y - c.y) + Math.abs(d.x - c.x) == 0)
        {
            if ((d.x - b.x) * (a.y - b.y) - (d.y - b.y) * (a.x - b.x) == 0)
            {
                System.out.println("C、D是一个点，且在AB线段上！");
            } else
            {
                System.out.println("C、D是一个点，且不在AB线段上！");
            }
//            return 0;
            return null;
        }

        if ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y) == 0)
        {
            System.out.println("线段平行，无交点！");
//            return 0;
            return null;
        }

        intersection.x = ((b.x - a.x) * (c.x - d.x) * (c.y - a.y) - c.x * (b.x - a.x) * (c.y - d.y) + a.x * (b.y - a.y) * (c.x - d.x)) / ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y));
        intersection.y = ((b.y - a.y) * (c.y - d.y) * (c.x - a.x) - c.y * (b.y - a.y) * (c.x - d.x) + a.y * (b.x - a.x) * (c.y - d.y)) / ((b.x - a.x) * (c.y - d.y) - (b.y - a.y) * (c.x - d.x));

        if ((intersection.x - a.x) * (intersection.x - b.x) <= 0 && (intersection.x - c.x) * (intersection.x - d.x) <= 0 && (intersection.y - a.y) * (intersection.y - b.y) <= 0 && (intersection.y - c.y) * (intersection.y - d.y) <= 0)
        {

            System.out.println("线段相交于点(" + intersection.x + "," + intersection.y + ")！");
//            return 1; // '相交
            return intersection;
        } else
        {
            System.out.println("线段相交于虚交点(" + intersection.x + "," + intersection.y + ")！");
//            return -1; // '相交但不在线段上
            return intersection;
        }
    }

    public static double getNum(String time1)
    {
        String[] num = new String[]{"0", "0", "0"};
        String bb;
        String aa = time1;
//        string[] num = message.Split(new char[] { '件', '箱', '盒', '袋', '包', '瓶', '桶'});

        String[] str = {"件", "箱", "盒", "袋", "包", "瓶", "桶"};
        int l = 0;
        for (int k = 0; k < str.length; k++)
        {

            String[] dd = aa.split(str[k]);
            if (dd.length == 2)
            {
                aa = dd[1];
                num[l] = dd[0];
                l++;

            } else
            {
                aa = dd[0];
                //价格判断是否为数字
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(aa);
                if (m.matches())
                {
                    num[l] = dd[0];
                }

            }

        }
        if (!num[2].equals("0"))
        {
            bb = num[2];
        } else if (!num[1].equals("0") && num[2].equals("0"))
        {
            bb = num[1];
        } else
        {
            bb = num[0];
        }
        return Double.valueOf(bb);
    }


}
