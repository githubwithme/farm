package com.farm.common;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-24 上午10:09:00
 * @description :
 */
public class DbHelper
{
	/**
	 * @description:获取指定轨迹的所有所有点
	 * @createTime：2015-8-24 上午11:26:25
	 * @param context
	 * @param c
	 * @param uuid
	 * @return
	 */
	public static <T> List<T> getTrackAllPoint(Context context, Class<T> c, String uuid)
	{
		DbUtils db = DbUtils.create(context);
		List<T> list = null;
		try
		{
			list = db.findAll(Selector.from(c).where("uuid", "=", uuid));
		} catch (DbException e)
		{
			e.printStackTrace();
		}
		if (list.isEmpty())
		{
			list = new ArrayList<T>();
		}
		return list;
	}

	/**
	 * @description:获取所有轨迹
	 * @createTime：2015-8-24 上午11:26:14
	 * @param context
	 * @param c
	 * @return
	 */
	public static <T> List<T> getAllTrack(Context context, Class<T> c)
	{
		DbUtils db = DbUtils.create(context);
		List<T> list = null;
		try
		{
			list = db.findAll(Selector.from(c));
		} catch (DbException e)
		{
			e.printStackTrace();
		}
		if (list.isEmpty())
		{
			list = new ArrayList<T>();
		}
		return list;
	}

	public static <T> List<T> getSameLat(Context context, Class<T> c, String lat, String uuid)
	{
		DbUtils db = DbUtils.create(context);
		List<T> list = null;
		try
		{
			list = db.findAll(Selector.from(c).where("lat", "=", lat).and("uuid", "=", uuid));
		} catch (DbException e)
		{
			e.printStackTrace();
		}
		if (list.isEmpty())
		{
			list = new ArrayList<T>();
		}
		return list;
	}

	public static <T> List<T> getSameLng(Context context, Class<T> c, String lng, String uuid)
	{
		DbUtils db = DbUtils.create(context);
		List<T> list = null;
		try
		{
			list = db.findAll(Selector.from(c).where("lon", "=", lng).and("uuid", "=", uuid));
		} catch (DbException e)
		{
			e.printStackTrace();
		}
		if (list.isEmpty())
		{
			list = new ArrayList<T>();
		}
		return list;
	}
}
