package com.wheel;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ThreeWheel implements OnWheelChangedListener, WheelInterface
{
	private static PopupWindow popupWindow;
	private static View popupWindowView;
	private static WheelView firstItemView;
	private static WheelView secondItemView;
	private static WheelView thirdItemView;
	protected String[] firstItemData;
	protected HashMap<String, String[]> secondItemData = new HashMap<String, String[]>();
	protected HashMap<String, String[]> thirdItemData = new HashMap<String, String[]>();
	protected HashMap<String, String> mZipcodeDatasMap = new HashMap<String, String>();
	protected String firstItemSelected;
	protected String secondItemSelected;
	protected String thirdItemSelected = "";
	protected String mCurrentZipCode = "";
	static ThreeWheel threeWheel;
	static TextView tv_showfirstItemselected;
	Context context;

	public ThreeWheel(Context context)
	{
		this.context = context;
	}

	public void buildWheel(WheelView firstItemView, WheelView secondItemView, WheelView thirdItemView)
	{
		this.firstItemView = firstItemView;
		this.secondItemView = secondItemView;
		this.thirdItemView = thirdItemView;
		initProvinceDatas();
		setUpListener();
		setUpData();
	}

	// public void buildWheel(WheelView firstItemView,WheelView
	// secondItemView,WheelView thirdItemView,String[] firstItemData,
	// HashMap<String, String[]> secondItemData,HashMap<String, String[]>
	// thirdItemData)
	// {
	// this.firstItemView=firstItemView;
	// this.secondItemView=secondItemView;
	// this.thirdItemView=thirdItemView;
	// this.firstItemData=firstItemData;
	// this.secondItemData=secondItemData;
	// this.thirdItemData=thirdItemData;
	// setUpListener();
	// setUpData();
	// }
	private void setUpListener()
	{
		// 添加change事件
		firstItemView.addChangingListener(this);
		// 添加change事件
		secondItemView.addChangingListener(this);
		// 添加change事件
		thirdItemView.addChangingListener(this);
		// 添加onclick事件
	}

	private void setUpData()
	{
		firstItemView.setViewAdapter(new ArrayWheelAdapter<String>(context, firstItemData));
		// 设置可见条目数量
		firstItemView.setVisibleItems(0);
		secondItemView.setVisibleItems(0);
		thirdItemView.setVisibleItems(0);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue)
	{
		// TODO Auto-generated method stub
		if (wheel == firstItemView)
		{
			updateCities();
		} else if (wheel == secondItemView)
		{
			updateAreas();
		} else if (wheel == thirdItemView)
		{
			thirdItemSelected = thirdItemData.get(secondItemSelected)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(thirdItemSelected);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas()
	{
		int pCurrent = secondItemView.getCurrentItem();
		secondItemSelected = secondItemData.get(firstItemSelected)[pCurrent];
		String[] areas = thirdItemData.get(secondItemSelected);

		if (areas == null)
		{
			areas = new String[] { "" };
		}
		thirdItemView.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		thirdItemView.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities()
	{
		int pCurrent = firstItemView.getCurrentItem();
		firstItemSelected = firstItemData[pCurrent];
		String[] cities = secondItemData.get(firstItemSelected);
		if (cities == null)
		{
			cities = new String[] { "" };
		}
		secondItemView.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		secondItemView.setCurrentItem(0);
		updateAreas();
	}

	public String getFirstItemSelected()
	{
		return firstItemSelected;
	}

	public String getSecondItemSelected()
	{
		return secondItemSelected;
	}

	public String getThirdItemSelected()
	{
		return thirdItemSelected;
	}

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
		AssetManager asset = context.getAssets();
		try
		{
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty())
			{
				firstItemSelected = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty())
				{
					secondItemSelected = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					thirdItemSelected = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			firstItemData = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++)
			{
				// 遍历所有省的数据
				firstItemData[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++)
				{
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					String[] distrinctNameArray = new String[districtList.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					for (int k = 0; k < districtList.size(); k++)
					{
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					thirdItemData.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				secondItemData.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e)
		{
			e.printStackTrace();
		} finally
		{

		}
	}

	public static void showThreeWheel(Context context, TextView textView)
	{
		if (popupWindow != null && popupWindow.isShowing())
		{
			Toast.makeText(context, "请勿重复点击！", Toast.LENGTH_SHORT).show();
		} else
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			popupWindowView = inflater.inflate(R.layout.threewheel_in_bottom, null);
			popupWindow = new PopupWindow(popupWindowView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
			firstItemView = (WheelView) popupWindowView.findViewById(R.id.firstItem);
			secondItemView = (WheelView) popupWindowView.findViewById(R.id.secondItem);
			thirdItemView = (WheelView) popupWindowView.findViewById(R.id.thirdtItem);
			threeWheel = new ThreeWheel(context);
			threeWheel.buildWheel(firstItemView, secondItemView, thirdItemView);
			popupWindow.setAnimationStyle(R.style.bottominbottomout);// 设置PopupWindow的弹出和消失效果
			Button confirmButton = (Button) popupWindowView.findViewById(R.id.confirmButton);
			popupWindow.showAtLocation(confirmButton, Gravity.CENTER, 0, 0);
			tv_showfirstItemselected = textView;
			confirmButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					popupWindow.dismiss();
					tv_showfirstItemselected.setText(threeWheel.getFirstItemSelected() + threeWheel.getSecondItemSelected() + threeWheel.getThirdItemSelected());
				}
			});
			Button cancleButton = (Button) popupWindowView.findViewById(R.id.cancleButton);
			cancleButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					popupWindow.dismiss();
				}
			});
		}
	}
}
