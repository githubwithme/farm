package com.farm.bean;

import java.util.HashMap;
import java.util.List;

public class CommandEntity
{

	String CommandName, PrincipalId, PrincipalType, RequestId, v_flag;
	Boolean IsCached;
	HashMap<String, String> params;
	List<HashMap<String, String>> dataSycParams;

	public void setCommandName(String commandName)
	{
		CommandName = commandName;
	}

	public String getCommandName()
	{
		return CommandName;
	}

	public void setPrincipalId(String principalId)
	{
		PrincipalId = principalId;
	}

	public String getPrincipalId()
	{
		return PrincipalId;
	}

	public void setPrincipalType(String principalType)
	{
		PrincipalType = principalType;
	}

	public String getPrincipalType()
	{
		return PrincipalType;
	}

	public String getRequestId()
	{
		return RequestId;
	}

	public void setRequestId(String requestId)
	{
		RequestId = requestId;
	}

	public void setIsCached(Boolean isCached)
	{
		IsCached = isCached;
	}

	public Boolean getIsCached()
	{
		return IsCached;
	}

	public void setV_flag(String v_flag)
	{
		this.v_flag = v_flag;
	}

	public String getV_flag()
	{
		return v_flag;
	}

	public void setParams(HashMap<String, String> params)
	{
		this.params = params;
	}

	public HashMap<String, String> getParams()
	{
		return params;
	}

	public void setDataSycParams(List<HashMap<String, String>> dataSycParams)
	{
		this.dataSycParams = dataSycParams;
	}

	public List<HashMap<String, String>> getDataSycParams()
	{
		return dataSycParams;
	}

}
