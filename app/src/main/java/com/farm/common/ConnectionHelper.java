package com.farm.common;

import com.alibaba.fastjson.JSON;
import com.farm.bean.CommandEntity;
import com.farm.bean.Connector;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @author :sima
 * @version :
 * @createTime：2015-8-10 下午4:42:19
 * @description :连接网络帮助类
 */
public class ConnectionHelper implements Connector
{
	public static String setParams(String commandName, String principalId, HashMap<String, String> hashMap)
	{
		CommandEntity commandEntity = new CommandEntity();
		commandEntity.setCommandName(commandName);
		commandEntity.setIsCached(true);
		commandEntity.setPrincipalType("1");
		commandEntity.setPrincipalId(principalId);
		commandEntity.setRequestId("22856468-507e-40dc-bc40-470c9fe9f44c");
		commandEntity.setParams(hashMap);
		String params = JSON.toJSONString(commandEntity);
		return params;
	}

	public static RequestParams getParas(String p)
	{
		RequestParams params = new RequestParams();
		try
		{
			params.setContentType("application/json");
			params.setBodyEntity(new StringEntity(p, "utf-8"));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return params;
	}
}
