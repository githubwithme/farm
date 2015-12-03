package com.farm.chart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.MM_RY_XXCJ_ALL;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class BarChartActivityMultiDataset extends DemoBase implements OnChartValueSelectedListener, OnClickListener
{

	private BarChart mChart;
	private BarChart chart_year;
	PieChart chart_pie;
	private Typeface tf;
	ImageButton btn_previous;
	String year_select;
	String type_select;
	Button btn_sure;
	TextView tv_year;
	TextView tv_type;
	ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
	ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
	ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
	// 全年
	int year_rys = 0;
	int year_czrks = 0;
	int year_ldrks = 0;
	int year_wgrs = 0;
	int year_lds = 0;
	int year_fjs = 0;
	int year_rws = 0;
	int year_hys = 0;
	int year_sjsbs = 0;
	int year_sjcls = 0;
	int year_qys = 0;
	List<MM_RY_XXCJ_ALL> list_MM_RY_XXCJ_ALL = new ArrayList<MM_RY_XXCJ_ALL>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_barchart);
		getActionBar().hide();

		tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		btn_previous = (ImageButton) findViewById(R.id.btn_previous);
		tv_year = (TextView) findViewById(R.id.tv_year);
		tv_year.setOnClickListener(this);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_type.setOnClickListener(this);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);

		// 每月统计
		mChart = (BarChart) findViewById(R.id.chart1);
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDescription("");
		mChart.setPinchZoom(false);
		mChart.setDrawBarShadow(false);
		mChart.setDrawGridBackground(false);
		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
		mChart.setMarkerView(mv);
		XAxis xAxis = mChart.getXAxis();// 设置x轴信息位置
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setTypeface(tf);
		xAxis.setDrawGridLines(false);
		xAxis.setSpaceBetweenLabels(2);
		// 全年统计
		chart_year = (BarChart) findViewById(R.id.chart_year);
		chart_year.setOnChartValueSelectedListener(this);
		chart_year.setDescription("");
		chart_year.setPinchZoom(false);
		chart_year.setDrawBarShadow(false);
		chart_year.setDrawGridBackground(false);
		chart_year.setMarkerView(mv);
		XAxis xAxis_year = chart_year.getXAxis(); // 设置x轴信息位置
		xAxis_year.setPosition(XAxisPosition.BOTTOM);
		xAxis_year.setTypeface(tf);
		xAxis_year.setDrawGridLines(false);
		xAxis_year.setSpaceBetweenLabels(2);
		// Legend l0 = mChart.getLegend(); //设置图注位置
		// l0.setPosition(LegendPosition.BELOW_CHART_LEFT);
		// l0.setForm(LegendForm.SQUARE);
		// l0.setFormSize(9f);
		// l0.setTextSize(11f);
		// l0.setXEntrySpace(4f);

		// 比率统计
		chart_pie = (PieChart) findViewById(R.id.chart_pie);
		chart_pie.setUsePercentValues(true);
		chart_pie.setDescription("");
		chart_pie.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
		chart_pie.setDrawHoleEnabled(true);
		chart_pie.setHoleColorTransparent(true);
		chart_pie.setTransparentCircleColor(Color.WHITE);
		chart_pie.setHoleRadius(58f);
		chart_pie.setTransparentCircleRadius(61f);
		chart_pie.setDrawCenterText(true);
		chart_pie.setRotationAngle(0);
		chart_pie.setRotationEnabled(true);
		chart_pie.setOnChartValueSelectedListener(this);
		chart_pie.setCenterText("人员占比情况");

		chart_pie.animateY(1500, Easing.EasingOption.EaseInOutQuad);// 设置图注位置
		Legend l = chart_pie.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(5f);

		// 获取数据并展示
		year_select = tv_year.getText().toString();
		type_select = tv_type.getText().toString();
		getMM_RY_XXCJ_ALL();
	}

	private void buildChart_Year()
	{
		getYearData();
		ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		int[] color = new int[] { Color.rgb(104, 241, 175), Color.rgb(44, 141, 75), Color.rgb(154, 31, 175), Color.rgb(104, 131, 15), Color.rgb(104, 241, 175), Color.rgb(44, 141, 75), Color.rgb(154, 31, 175), Color.rgb(104, 131, 15), Color.rgb(104, 241, 175), Color.rgb(44, 141, 75),
				Color.rgb(154, 31, 175) };
		String[] rytype = new String[] { "常住人口", "流动人口", "外国人", "人员", "楼栋", "企业", "房间", "会议", "任务", "事件上报", "事件处理" };
		int[] tjs = new int[] { year_czrks, year_ldrks, year_wgrs, year_rys, year_lds, year_qys, year_fjs, year_hys, year_rws, year_sjsbs, year_sjcls };
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < rytype.length; i++)
		{
			xVals.add(rytype[i]);
		}
		for (int i = 0; i < rytype.length; i++)
		{
			float val = Float.valueOf(String.valueOf(tjs[i]));
			yVals.add(new BarEntry(val, i));
		}
		BarDataSet set = new BarDataSet(yVals, "全年信息数据统计");
		set.setColor(color[(int) (Math.random() * 11) + 0]);
		dataSets.add(set);
		tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		Legend l = chart_year.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setTypeface(tf);

		BarData data = new BarData(xVals, dataSets);
		data.setValueTextSize(10f);
		data.setValueTypeface(tf);

		chart_year.setData(data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// getMenuInflater().inflate(R.menu.bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return true;
	}

	private void getYearData()
	{
		for (int k = 0; k < list_MM_RY_XXCJ_ALL.size(); k++)
		{
			String year = list_MM_RY_XXCJ_ALL.get(k).getIntMonth().toString().substring(0, 4);
			if (year.equals(year_select))
			{
				year_rys = year_rys + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getRKS());
				year_czrks = year_czrks + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getCZRKS());
				year_ldrks = year_ldrks + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getLDRKS());
				year_wgrs = year_wgrs + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getWGRS());
				year_lds = year_lds + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getLDS());
				year_fjs = year_fjs + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getFJS());
				year_rws = year_rws + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getRWS());
				year_hys = year_hys + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getHYS());
				year_sjsbs = year_sjsbs + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getSJSBS());
				year_sjcls = year_sjcls + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getSJCLS());
				year_qys = year_qys + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getQYS());
			}
		}
	}

	private void setData(int count, float range)
	{

		String[] rytype = new String[] { "常住人口", "流动人口", "外国人" };
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
		int rys = 0;
		int czrks = 0;
		int ldrks = 0;
		int wgrs = 0;
		for (int k = 0; k < list_MM_RY_XXCJ_ALL.size(); k++)
		{
			String year = list_MM_RY_XXCJ_ALL.get(k).getIntMonth().toString().substring(0, 4);
			if (year.equals(year_select))
			{
				rys = rys + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getCZRKS()) + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getLDRKS()) + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getWGRS());
				czrks = czrks + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getCZRKS());
				ldrks = czrks + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getLDRKS());
				wgrs = czrks + Integer.valueOf(list_MM_RY_XXCJ_ALL.get(k).getWGRS());
			}
		}
		for (int i = 0; i < rytype.length; i++)
		{
			if (rytype[i].equals("常住人口"))
			{
				yVals1.add(new Entry((float) czrks / rys, i));
			} else if (rytype[i].equals("流动人口"))
			{
				yVals1.add(new Entry((float) ldrks / rys, i));
			} else if (rytype[i].equals("外国人"))
			{
				yVals1.add(new Entry((float) wgrs / rys, i));
			}
		}

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < rytype.length; i++)// 添加图注
		{
			xVals.add(rytype[i]);
		}

		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(3f);
		dataSet.setSelectionShift(5f);

		ArrayList<Integer> colors = new ArrayList<Integer>();

		for (int c : ColorTemplate.VORDIPLOM_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.JOYFUL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.COLORFUL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.LIBERTY_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.PASTEL_COLORS)
			colors.add(c);

		colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.WHITE);
		data.setValueTypeface(tf);
		chart_pie.setData(data);

		// undo all highlights
		chart_pie.highlightValues(null);

		chart_pie.invalidate();
	}

	private void setData(ArrayList<BarDataSet> dataSets)
	{
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < 12; i++)
		{
			xVals.add((i + 1) + "月");
		}

		BarData data = new BarData(xVals, dataSets);
		data.setGroupSpace(240f);// 设置每组数据的间距
		data.setValueTypeface(tf);

		mChart.setData(data);

		tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setTypeface(tf);

		XAxis xl = mChart.getXAxis();
		xl.setTypeface(tf);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setTypeface(tf);
		leftAxis.setValueFormatter(new LargeValueFormatter());
		leftAxis.setDrawGridLines(false);
		leftAxis.setSpaceTop(25f);

		mChart.getAxisRight().setEnabled(false);
		mChart.invalidate();
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v)
	{
		// int id = v.getId();
		// if (id == R.id.tv_year)
		// {
		// OneWheel.showWheel(BarChartActivityMultiDataset.this,
		// DICT_YEAR.class, tv_year, "YEAR");
		// } else if (id == R.id.tv_type)
		// {
		// OneWheel.showWheel(BarChartActivityMultiDataset.this,
		// DICT_INFOTYPE.class, tv_type, "INFOTYPE");
		// } else if (id == R.id.btn_sure)
		// {
		// year_select = tv_year.getText().toString();
		// type_select = tv_type.getText().toString();
		// if (list_MM_RY_XXCJ_ALL.size() == 0)
		// {
		// getMM_RY_XXCJ_ALL();
		// } else
		// {
		// showTJ();
		// }
		// }
	}

	private void getMM_RY_XXCJ_ALL()
	{
		for (int i = 0; i < 12; i++)
		{
			MM_RY_XXCJ_ALL mm_RY_XXCJ_ALL = new MM_RY_XXCJ_ALL();
			mm_RY_XXCJ_ALL.setCZRKS("223");
			mm_RY_XXCJ_ALL.setFJS("342");
			mm_RY_XXCJ_ALL.setHYS("33");
			mm_RY_XXCJ_ALL.setIntMonth("2015-04");
			mm_RY_XXCJ_ALL.setLDRKS("34");
			mm_RY_XXCJ_ALL.setLDS("98");
			mm_RY_XXCJ_ALL.setQYS("58");
			mm_RY_XXCJ_ALL.setRKS("369");
			mm_RY_XXCJ_ALL.setRWS("356");
			mm_RY_XXCJ_ALL.setSJCLS("56");
			mm_RY_XXCJ_ALL.setSJSBS("55");
			mm_RY_XXCJ_ALL.setWGRS("12");
			list_MM_RY_XXCJ_ALL.add(mm_RY_XXCJ_ALL);
		}
		showTJ();
		setData(3, 100);
		buildChart_Year();

		// HashMap<String, String> hashMap = new HashMap<String, String>();
		// hashMap.put("UserID", UserInfo.getSingleInstance().getUserId());
		// String params = HttpUrlConnect.setParams("APP.MM_RY_XXCJ_ALL", "0",
		// hashMap);
		// new HttpUtils().send(HttpRequest.HttpMethod.POST,
		// GlobalVariable.dataBaseUrl, HttpUrlConnect.getParas(params), new
		// RequestCallBack<String>()
		// {
		// @Override
		// public void onSuccess(ResponseInfo<String> responseInfo)
		// {
		// JSONObject jsonObj = null;
		// try
		// {
		// jsonObj = new JSONObject(responseInfo.result);
		// if (jsonObj != null)
		// {
		// String result = HttpUrlConnect.getRowString(jsonObj);
		// if (!result.equals(""))
		// {
		// list_MM_RY_XXCJ_ALL = JSON.parseArray(result, MM_RY_XXCJ_ALL.class);
		// showTJ();
		// setData(3, 100);
		// buildChart_Year();
		// }
		// }
		// } catch (JSONException e)
		// {
		// e.printStackTrace();
		// } catch (org.json.JSONException e)
		// {
		// e.printStackTrace();
		// }
		// }
		//
		// @Override
		// public void onFailure(HttpException error, String msg)
		// {
		// Toast.makeText(BarChartActivityMultiDataset.this, "获取失败",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
	}

	public ArrayList<BarEntry> getyVals(String year_select, String type, String miniType)
	{
		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		String[] months = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		if (miniType.equals("常住人口"))
		{
			float val = 0;
			for (int i = 0; i < months.length; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getCZRKS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("流动人口"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getLDRKS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("外国人"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getWGRS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("企业"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getQYS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("楼栋"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getLDS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("事件上报"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getSJSBS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("事件处理"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getSJCLS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("任务"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getRWS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("会议"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getHYS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		} else if (miniType.equals("房间"))
		{
			float val = 0;
			for (int i = 0; i < 12; i++)
			{
				for (int j = 0; j < list_MM_RY_XXCJ_ALL.size(); j++)
				{
					String year = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(0, 4);
					String month = list_MM_RY_XXCJ_ALL.get(j).getIntMonth().toString().substring(4, 6);
					if (year.equals(year_select) && month.equals(months[i]))
					{
						val = Float.valueOf(list_MM_RY_XXCJ_ALL.get(j).getFJS().toString());
						yVals1.add(new BarEntry(val, i));
					}
				}
				if (val == 0)
				{
					yVals1.add(new BarEntry(val, i));
				}
				val = 0;
			}
		}
		return yVals1;
	}

	private void showYear()
	{
		String[] type = new String[] { "人员", "企业", "楼栋" };
		int[] color = new int[] { Color.rgb(104, 241, 175), Color.rgb(44, 141, 75), Color.rgb(154, 31, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showRY()
	{
		String[] type = new String[] { "常住人口", "流动人口", "外国人" };
		int[] color = new int[] { Color.rgb(104, 241, 175), Color.rgb(44, 141, 75), Color.rgb(154, 31, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showQY()
	{
		String[] type = new String[] { "企业" };
		int[] color = new int[] { Color.rgb(104, 241, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showLD()
	{
		String[] type = new String[] { "楼栋" };
		int[] color = new int[] { Color.rgb(104, 241, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showSJSB()
	{
		String[] type = new String[] { "事件上报" };
		int[] color = new int[] { Color.rgb(104, 241, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showSJCL()
	{
		String[] type = new String[] { "事件处理" };
		int[] color = new int[] { Color.rgb(104, 241, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showRW()
	{
		String[] type = new String[] { "任务" };
		int[] color = new int[] { Color.rgb(104, 241, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showHY()
	{
		String[] type = new String[] { "会议" };
		int[] color = new int[] { Color.rgb(104, 241, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showFJ()
	{
		String[] type = new String[] { "房间" };
		int[] color = new int[] { Color.rgb(104, 241, 175) };
		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for (int i = 0; i < type.length; i++)
		{
			ArrayList<BarEntry> yVals = getyVals(year_select, type_select, type[i]);
			BarDataSet set = new BarDataSet(yVals, type[i]);
			set.setColor(color[i]);
			dataSets.add(set);
		}
		setData(dataSets);
	}

	private void showTJ()
	{
		String type = tv_type.getText().toString();
		if (type.equals("人员"))
		{
			showRY();
		} else if (type.equals("企业"))
		{
			showQY();
		} else if (type.equals("楼栋"))
		{
			showLD();
		} else if (type.equals("事件上报"))
		{
			showSJSB();
		} else if (type.equals("事件处理"))
		{
			showSJCL();
		} else if (type.equals("任务"))
		{
			showRW();
		} else if (type.equals("会议"))
		{
			showHY();
		} else if (type.equals("房间"))
		{
			showFJ();
		}
		mChart.notifyDataSetChanged();
		mChart.invalidate();
	}
}
