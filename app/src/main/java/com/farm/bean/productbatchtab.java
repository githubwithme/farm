

package com.farm.bean;
import android.os.Parcel;
import android.os.Parcelable;
import com.lidroid.xutils.db.annotation.Table;
/**
 *
 * Description: productbatchtab 实体类</p>
 *
 * Copyright: Copyright (c) 2015
 *
 * Company: 广州海川信息科技有限公司
 * @version 1.0
 */
@Table(name="productbatchtab")
public class productbatchtab implements Parcelable
{
	public String id;
	public String parkid;
	public String parkname;
	public String areaid;
	public String areaname;
	public String contractid;
	public String contractname;
	public String amountforsale;
	public String amountofsold;
	public String productbatchtime;


	public String getid()
	{
		return id;
	}

	public void setid(String id)
	{
		this.id = id;
	}

	public String getparkid()
	{
		return parkid;
	}

	public void setparkid(String parkid)
	{
		this.parkid = parkid;
	}

	public String getparkname()
	{
		return parkname;
	}

	public void setparkname(String parkname)
	{
		this.parkname = parkname;
	}

	public String getareaid()
	{
		return areaid;
	}

	public void setareaid(String areaid)
	{
		this.areaid = areaid;
	}

	public String getareaname()
	{
		return areaname;
	}

	public void setareaname(String areaname)
	{
		this.areaname = areaname;
	}

	public String getcontractid()
	{
		return contractid;
	}

	public void setcontractid(String contractid)
	{
		this.contractid = contractid;
	}

	public String getcontractname()
	{
		return contractname;
	}

	public void setcontractname(String contractname)
	{
		this.contractname = contractname;
	}

	public String getamountforsale()
	{
		return amountforsale;
	}

	public void setamountforsale(String amountforsale)
	{
		this.amountforsale = amountforsale;
	}

	public String getamountofsold()
	{
		return amountofsold;
	}

	public void setamountofsold(String amountofsold)
	{
		this.amountofsold = amountofsold;
	}

	public String getproductbatchtime()
	{
		return productbatchtime;
	}

	public void setproductbatchtime(String productbatchtime)
	{
		this.productbatchtime = productbatchtime;
	}

	public boolean equals(Object o)
	{
		return false;
	}

	public int hashCode()
	{
		return 0;
	}

	public static final Creator<productbatchtab> CREATOR = new Creator()
	{
		@Override
		public productbatchtab createFromParcel(Parcel source)
		{
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
			productbatchtab p = new productbatchtab();
			p.setid(source.readString());
			p.setparkid(source.readString());
			p.setparkname(source.readString());
			p.setareaid(source.readString());
			p.setareaname(source.readString());
			p.setcontractid(source.readString());
			p.setcontractname(source.readString());
			p.setamountforsale(source.readString());
			p.setamountofsold(source.readString());
			p.setproductbatchtime(source.readString());
			return p;
		}

		@Override
		public productbatchtab[] newArray(int size)
		{
			return new productbatchtab[size];
		}
	};

	@Override
	public void writeToParcel(Parcel p, int arg1)
	{
		p.writeString(id);
		p.writeString(parkid);
		p.writeString(parkname);
		p.writeString(areaid);
		p.writeString(areaname);
		p.writeString(contractid);
		p.writeString(contractname);
		p.writeString(amountforsale);
		p.writeString(amountofsold);
		p.writeString(productbatchtime);
	}
	@Override
	public int describeContents()
	{
		return 0;
	}
}
