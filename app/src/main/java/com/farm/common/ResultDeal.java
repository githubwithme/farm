package com.farm.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.farm.bean.Result;

/**
 * @author :sima
 * @version :1.0
 * @createTime：2015-8-5 下午3:04:57
 * @description :服务器返回数据处理类
 */
public class ResultDeal
{
	int ResultCode;
	int AffectedRows;
	String Exception;
	String Rows;
	String ColumnNames;
	String ResultDesc;
	String OutParams;

	// {"ResultCode":200,"Exception":null,"Rows":[["\/Date(1438744304800+0800)\/"]],"ColumnNames":["Column1"],"ResultDesc":null,"OutParams":null,"AffectedRows":1}
	/**
	 * @description:将服务器返回的数据进行json化处理
	 * @createTime：2015-8-6 上午10:50:33
	 * @param result
	 * @return String
	 */
	public static String getAllRow(Result result)
	{
		JSONArray jsonArray_Rows = result.getRows();
		if (jsonArray_Rows != null && jsonArray_Rows.size() > 0)// //有符合条件的数据
		{
//			String str = dealAllRows(jsonArray_Rows, result.getColumnNames());
			return null;
		} else
		{
			return null;
		}
	}

	/**
	 * @description:将服务器返回的数据进行处理
	 * @createTime：2015-8-6 上午10:51:18
	 * @param result
	 * @return
	 */
	public static String getSingleRow(Result result)
	{
		JSONArray jsonArray_Rows = result.getRows();
		if (jsonArray_Rows != null && jsonArray_Rows.size() > 0)// //有符合条件的数据
		{
//			String str = dealSingleRows(jsonArray_Rows, result.getColumnNames());
			return null;
		} else
		{
			return null;
		}
	}

	/**
	 * @description:将类似json格式的字符串转换成真正的json格式
	 * @createTime：2015-8-6 上午10:49:16
	 * @param jsonArray_Rows
	 * @param jsonArray_ColumnNames
	 * @return String
	 */
	public static String dealSingleRows(JSONArray jsonArray_Rows, String[] jsonArray_ColumnNames)
	{
		StringBuffer buff = null;
		if (jsonArray_Rows.size() != 0)
		{
			for (int i = 0; i < jsonArray_Rows.size(); i++)
			{
				buff = new StringBuffer();
				buff.append("{");
				for (int j = 0; j < jsonArray_ColumnNames.length; j++)
				{
					try
					{
						buff.append("\"" + jsonArray_ColumnNames[j] + "\"" + ":" + "\"" + jsonArray_Rows.getJSONArray(i).getString(j) + "\"" + ",");
					} catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
				buff.replace(buff.length() - 1, buff.length(), "}");
			}
			return buff.toString();
		}
		return null;
	}

	/**
	 * @description:将类似json格式的字符串转换成真正的json格式
	 * @createTime：2015-8-6 上午10:50:17
	 * @param jsonArray_Rows
	 * @param jsonArray_ColumnNames
	 * @return String
	 */
	public static String dealAllRows(JSONArray jsonArray_Rows, String[] jsonArray_ColumnNames)
	{
		StringBuffer rows = null;
		try
		{
			if (jsonArray_Rows != null && jsonArray_Rows.size() != 0)
			{
				rows = new StringBuffer();
				rows.append("[");
				for (int i = 0; i < jsonArray_Rows.size(); i++)
				{
					rows.append("{");
					for (int j = 0; j < jsonArray_ColumnNames.length; j++)
					{
						rows.append("\"" + jsonArray_ColumnNames[j] + "\"" + ":" + "\"" + jsonArray_Rows.getJSONArray(i).getString(j) + "\"" + ",");
					}
					rows.replace(rows.length() - 1, rows.length(), "},");
				}
				rows.replace(rows.length() - 1, rows.length(), "]");
				return rows.toString();
			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
