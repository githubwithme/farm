package com.farm.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author :sima
 * @version :1.0
 * @createTime：2015-8-9 下午9:27:19
 * @description :常用工具类
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
		String str = formatter.format(curDate);
		return str;
	}

	public static String getToday()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		String str = formatter.format(curDate);
		return str;
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
	 * @param min
	 *            指定范围最小值
	 * @param max
	 *            指定范围最大值
	 * @param n
	 *            随机数个数
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
	 * 
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
	 * @param jsonFileName
	 *            json文件名
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

}
