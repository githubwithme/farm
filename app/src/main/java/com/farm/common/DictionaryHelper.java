package com.farm.common;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.SelectRecords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DictionaryHelper
{
	public static Dictionary getDictionaryFromAssess(Context context, String type)
	{
		JSONObject jsonObject = utils.parseJsonFile(context, "dictionary.json");
		Dictionary dictionary = JSON.parseArray(jsonObject.getString(type), Dictionary.class).get(0);
		return dictionary;
	}

	public static Dictionary getNCZ_CMD_AreaDictionary(Context context, Dictionary dic_area, Dictionary dic_assess)
	{
		List<String> firstItemID_area = dic_area.getFirstItemID();
		List<String> firstItemName_area = dic_area.getFirstItemName();
		List<List<String>> secondItemId_area = dic_area.getSecondItemID();
		List<List<String>> secondItemName_area = dic_area.getSecondItemName();

		List<String> firstItemID = dic_assess.getFirstItemID();
		List<String> firstItemName = dic_assess.getFirstItemName();
		List<List<String>> secondItemId = dic_assess.getSecondItemID();
		List<List<String>> secondItemName = dic_assess.getSecondItemName();

		firstItemID.add(String.valueOf((firstItemID.size() + 1)));
		firstItemName.add("园区");

		List<String> firstItemID_temp = null;
		List<String> firstItemName_temp = null;

		firstItemID_area.add("-1");
		firstItemName_area.add("不限");
		firstItemID_temp = new ArrayList<String>();
		firstItemName_temp = new ArrayList<String>();
		for (int j = 0; j < firstItemID_area.size(); j++)
		{
			firstItemID_temp.add(firstItemID_area.get(firstItemID_area.size() - j - 1));
			firstItemName_temp.add(firstItemName_area.get(firstItemName_area.size() - j - 1));
		}
		firstItemID_temp.add("-2");
		firstItemName_temp.add("多选");

		secondItemId.add(firstItemID_temp);
		secondItemName.add(firstItemName_temp);

		firstItemID.add(String.valueOf((firstItemID.size() + 1)));
		firstItemName.add("片区");
		List<String> sID_temp = new ArrayList<String>();
		List<String> sName_temp = new ArrayList<String>();
		sID_temp.add("-1");
		sName_temp.add("不限");
		for (int i = 0; i < secondItemName_area.size(); i++)
		{
			String fn = dic_area.getFirstItemName().get(i);
			for (int j = 0; j < secondItemName_area.get(i).size(); j++)
			{
				sID_temp.add(secondItemId_area.get(i).get(j));
				sName_temp.add(fn + secondItemName_area.get(i).get(j));
			}
		}
		sID_temp.add("-2");
		sName_temp.add("多选");

		secondItemId.add(sID_temp);
		secondItemName.add(sName_temp);
		return dic_assess;
	}

	public static String getStrWhere_ncz_cmd(Context context, Dictionary dictionary)
	{
		List<SelectRecords> list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(context, SelectRecords.class, dictionary.getBELONG().toString());
		if (list_SelectRecords.size() == 0)
		{
			return "";
		}
		String strWher_yq = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("园区"))
			{
				strWher_yq = strWher_yq + list_SelectRecords.get(i).getSecondid() + "|";
			}
		}
		if (!strWher_yq.equals(""))
		{
			strWher_yq = "yq:" + strWher_yq.substring(0, strWher_yq.length() - 1) + ",";
		}
		String strWher_pq = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("片区"))
			{
				strWher_pq = strWher_pq + list_SelectRecords.get(i).getSecondid() + "|";
			}
		}
		if (!strWher_pq.equals(""))
		{
			strWher_pq = "pq:" + strWher_pq.substring(0, strWher_pq.length() - 1) + ",";
		}
		String strWher_fbsj = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("发布时间") && list_SelectRecords.get(i).getSecondtype().equals("本周"))
			{
				strWher_fbsj = strWher_fbsj + "1|";

			} else if (list_SelectRecords.get(i).getFirsttype().equals("发布时间") && list_SelectRecords.get(i).getSecondtype().equals("本月"))
			{
				strWher_fbsj = strWher_fbsj + "2|";

			} else if (list_SelectRecords.get(i).getFirsttype().equals("发布时间") && list_SelectRecords.get(i).getSecondtype().equals("本季"))
			{
				strWher_fbsj = strWher_fbsj + "3|";
			}
		}
		if (!strWher_fbsj.equals(""))
		{
			strWher_fbsj = "fbsj:" + strWher_fbsj.substring(0, strWher_fbsj.length() - 1) + ",";
		}
		String strWher_zlnr = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("指令内容") && list_SelectRecords.get(i).getSecondtype().equals("植保"))
			{
				strWher_zlnr = strWher_zlnr + "1|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("指令内容") && list_SelectRecords.get(i).getSecondtype().equals("施肥"))
			{
				strWher_zlnr = strWher_zlnr + "2|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("指令内容") && list_SelectRecords.get(i).getSecondtype().equals("蕾果"))
			{
				strWher_zlnr = strWher_zlnr + "3|";
			}
		}
		if (!strWher_zlnr.equals(""))
		{
			strWher_zlnr = "zlnr:" + strWher_zlnr.substring(0, strWher_zlnr.length() - 1) + ",";
		}
		String strWher_zyx = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("重要性") && list_SelectRecords.get(i).getSecondtype().equals("非常重要"))
			{
				strWher_zyx = strWher_zyx + "2|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("重要性") && list_SelectRecords.get(i).getSecondtype().equals("重要"))
			{
				strWher_zyx = strWher_zyx + "1|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("重要性") && list_SelectRecords.get(i).getSecondtype().equals("一般"))
			{
				strWher_zyx = strWher_zyx + "0|";
			}
		}
		if (!strWher_zyx.equals(""))
		{
			strWher_zyx = "zyx:" + strWher_zyx.substring(0, strWher_zyx.length() - 1) + ",";
		}
		String strWher_zt = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("状态") && list_SelectRecords.get(i).getSecondtype().equals("未开始"))
			{
				strWher_zt = strWher_zt + "0|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("状态") && list_SelectRecords.get(i).getSecondtype().equals("进行中"))
			{
				strWher_zt = strWher_zt + "1|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("状态") && list_SelectRecords.get(i).getSecondtype().equals("已结束"))
			{
				strWher_zt = strWher_zt + "2|";
			}
		}
		if (!strWher_zt.equals(""))
		{
			strWher_zt = "zt:" + strWher_zt.substring(0, strWher_zt.length() - 1) + ",";
		}
		String allStringWhere = strWher_yq + strWher_pq + strWher_fbsj + strWher_zlnr + strWher_zyx + strWher_zt;
		if (!allStringWhere.equals(""))
		{
			allStringWhere = allStringWhere.substring(0, allStringWhere.length() - 1);
		}
		// String strWher = getStrWhere();
		return allStringWhere;
	}

	public static String getStrWhere_morejob(Context context, String belong)
	{
		List<SelectRecords> list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(context, SelectRecords.class, belong);
		if (list_SelectRecords.size() == 0)
		{
			return "";
		}
		String strWher_gzsj = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("工作时间") && list_SelectRecords.get(i).getSecondtype().equals("本周"))
			{
				strWher_gzsj = strWher_gzsj + "1|";

			} else if (list_SelectRecords.get(i).getFirsttype().equals("工作时间") && list_SelectRecords.get(i).getSecondtype().equals("本月"))
			{
				strWher_gzsj = strWher_gzsj + "2|";

			} else if (list_SelectRecords.get(i).getFirsttype().equals("工作时间") && list_SelectRecords.get(i).getSecondtype().equals("本季"))
			{
				strWher_gzsj = strWher_gzsj + "3|";
			}
		}
		if (!strWher_gzsj.equals(""))
		{
			strWher_gzsj = "gzsj:" + strWher_gzsj.substring(0, strWher_gzsj.length() - 1) + ",";
		}
		String strWher_gzlx = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("工作类型") && list_SelectRecords.get(i).getSecondtype().equals("自发"))
			{
				strWher_gzlx = strWher_gzlx + "0|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("工作类型") && list_SelectRecords.get(i).getSecondtype().equals("指令执行"))
			{
				strWher_gzlx = strWher_gzlx + "1|";
			}
		}
		if (!strWher_gzlx.equals(""))
		{
			strWher_gzlx = "gzlx:" + strWher_gzlx.substring(0, strWher_gzlx.length() - 1) + ",";
		}
		String strWher_pf = "";
		for (int i = 0; i < list_SelectRecords.size(); i++)
		{
			if (list_SelectRecords.get(i).getFirsttype().equals("评分") && list_SelectRecords.get(i).getSecondtype().equals("优"))
			{
				strWher_pf = strWher_pf + "10|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("评分") && list_SelectRecords.get(i).getSecondtype().equals("合格"))
			{
				strWher_pf = strWher_pf + "8|";
			} else if (list_SelectRecords.get(i).getFirsttype().equals("评分") && list_SelectRecords.get(i).getSecondtype().equals("不合格"))
			{
				strWher_pf = strWher_pf + "0|";
			}
		}
		if (!strWher_pf.equals(""))
		{
			strWher_pf = "pf:" + strWher_pf.substring(0, strWher_pf.length() - 1) + ",";
		}
		String allStringWhere = strWher_gzsj + strWher_gzlx + strWher_pf;
		if (!allStringWhere.equals(""))
		{
			allStringWhere = allStringWhere.substring(0, allStringWhere.length() - 1);
		}
		return allStringWhere;
	}

	public static Dictionary_wheel getDictionary_Command(Dictionary dictionary)
	{
		String[] firstItemId = new String[dictionary.getFirstItemID().size()];
		String[] firstItemData = new String[dictionary.getFirstItemName().size()];
		HashMap<String, String[]> secondItemId = new HashMap<String, String[]>();
		HashMap<String, String[]> secondItemData = new HashMap<String, String[]>();
		for (int i = 0; i < dictionary.getFirstItemName().size(); i++)
		{
			firstItemData[i] = dictionary.getFirstItemName().get(i);
			firstItemId[i] = dictionary.getFirstItemID().get(i);
			String[] secondid = new String[dictionary.getSecondItemID().get(i).size()];
			String[] secondname = new String[dictionary.getSecondItemName().get(i).size()];
			for (int j = 0; j < dictionary.getSecondItemName().get(i).size(); j++)
			{
				secondid[j] = dictionary.getSecondItemID().get(i).get(j);
				secondname[j] = dictionary.getSecondItemName().get(i).get(j);

			}
			secondItemId.put(firstItemId[i], secondid);
			secondItemData.put(firstItemData[i], secondname);
		}
		Dictionary_wheel dictionary_wheel = new Dictionary_wheel();
		dictionary_wheel.setFirstItemID(firstItemId);
		dictionary_wheel.setFirstItemName(firstItemData);
		dictionary_wheel.setSecondItemID(secondItemId);
		dictionary_wheel.setSecondItemName(secondItemData);
		return dictionary_wheel;
	}

	public static Dictionary getParkDictionary(Dictionary dic_area)
	{
		List<String> fid = new ArrayList<String>();
		List<String> fname = new ArrayList<String>();
		List<List<String>> secondItemID = new ArrayList<List<String>>();
		List<List<String>> secondItemName = new ArrayList<List<String>>();
		fid.add("0");
		fname.add("园区执行");
		secondItemID.add(dic_area.getFirstItemID());
		secondItemName.add(dic_area.getFirstItemName());

		Dictionary park = new Dictionary();
		park.setBELONG("园区执行");
		park.setFirstItemID(fid);
		park.setFirstItemName(fname);
		park.setSecondItemID(secondItemID);
		park.setSecondItemName(secondItemName);

		return park;
	}
}
