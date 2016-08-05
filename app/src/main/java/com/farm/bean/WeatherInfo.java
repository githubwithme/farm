package com.farm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class WeatherInfo implements Parcelable
{
    @Override
    public String toString()
    {
        return "WeatherInfo{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                ", error_code=" + error_code +
                '}';
    }

    /**
     * reason : successed!
     * result : {"data":{"realtime":{"wind":{"windspeed":"14.0","direct":"北风","power":"2级","offset":null},"time":"19:00:00","weather":{"humidity":"85","img":"2","info":"阴","temperature":"19"},"dataUptime":1463917984,"date":"2016-05-22","city_code":"101250105","city_name":"望城","week":0,"moon":"四月十六"},"life":{"date":"2016-5-22","info":{"kongtiao":["较少开启","您将感到很舒适，一般不需要开启空调。"],"yundong":["较不宜","有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意携带雨具并注意避雨防滑。"],"ziwaixian":["最弱","属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"],"ganmao":["较易发","天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":["良","气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。"],"chuanyi":["较舒适","建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"]}},"weather":[{"date":"2016-05-22","info":{"night":["2","阴","18","无持续风向","微风","19:16"],"day":["1","多云","23","北风","微风","05:34"]},"week":"日","nongli":"四月十六"},{"date":"2016-05-23","info":{"dawn":["2","阴","18","无持续风向","微风","19:16"],"night":["2","阴","17","无持续风向","微风","19:16"],"day":["3","阵雨","22","西北风","微风","05:34"]},"week":"一","nongli":"四月十七"},{"date":"2016-05-24","info":{"dawn":["2","阴","17","无持续风向","微风","19:16"],"night":["1","多云","19","南风","微风","19:17"],"day":["1","多云","27","无持续风向","微风","05:33"]},"week":"二","nongli":"四月十八"},{"date":"2016-05-25","info":{"dawn":["1","多云","19","南风","微风","19:17"],"night":["8","中雨","20","南风","微风","19:17"],"day":["3","阵雨","27","南风","微风","05:33"]},"week":"三","nongli":"四月十九"},{"date":"2016-05-26","info":{"dawn":["8","中雨","20","南风","微风","19:17"],"night":["7","小雨","18","北风","3-4 级","19:18"],"day":["8","中雨","25","北风","3-4 级","05:33"]},"week":"四","nongli":"四月二十"},{"date":"2016-05-27","info":{"night":["3","阵雨","16","西北风","微风","19:30"],"day":["3","阵雨","20","西北风","微风","07:30"]},"week":"五","nongli":"四月廿一"},{"date":"2016-05-28","info":{"night":["1","多云","17","西北风","微风","19:30"],"day":["3","阵雨","23","西北风","微风","07:30"]},"week":"六","nongli":"四月廿二"}],"pm25":{"key":"","show_desc":0,"pm25":{"curPm":"","pm25":"","pm10":"","level":null,"quality":null,"des":null},"dateTime":"2016年05月22日19时","cityName":"望城"},"date":null,"isForeign":0}}
     * error_code : 0
     */

    private String reason;
    /**
     * data : {"realtime":{"wind":{"windspeed":"14.0","direct":"北风","power":"2级","offset":null},"time":"19:00:00","weather":{"humidity":"85","img":"2","info":"阴","temperature":"19"},"dataUptime":1463917984,"date":"2016-05-22","city_code":"101250105","city_name":"望城","week":0,"moon":"四月十六"},"life":{"date":"2016-5-22","info":{"kongtiao":["较少开启","您将感到很舒适，一般不需要开启空调。"],"yundong":["较不宜","有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意携带雨具并注意避雨防滑。"],"ziwaixian":["最弱","属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"],"ganmao":["较易发","天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":["良","气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。"],"chuanyi":["较舒适","建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"]}},"weather":[{"date":"2016-05-22","info":{"night":["2","阴","18","无持续风向","微风","19:16"],"day":["1","多云","23","北风","微风","05:34"]},"week":"日","nongli":"四月十六"},{"date":"2016-05-23","info":{"dawn":["2","阴","18","无持续风向","微风","19:16"],"night":["2","阴","17","无持续风向","微风","19:16"],"day":["3","阵雨","22","西北风","微风","05:34"]},"week":"一","nongli":"四月十七"},{"date":"2016-05-24","info":{"dawn":["2","阴","17","无持续风向","微风","19:16"],"night":["1","多云","19","南风","微风","19:17"],"day":["1","多云","27","无持续风向","微风","05:33"]},"week":"二","nongli":"四月十八"},{"date":"2016-05-25","info":{"dawn":["1","多云","19","南风","微风","19:17"],"night":["8","中雨","20","南风","微风","19:17"],"day":["3","阵雨","27","南风","微风","05:33"]},"week":"三","nongli":"四月十九"},{"date":"2016-05-26","info":{"dawn":["8","中雨","20","南风","微风","19:17"],"night":["7","小雨","18","北风","3-4 级","19:18"],"day":["8","中雨","25","北风","3-4 级","05:33"]},"week":"四","nongli":"四月二十"},{"date":"2016-05-27","info":{"night":["3","阵雨","16","西北风","微风","19:30"],"day":["3","阵雨","20","西北风","微风","07:30"]},"week":"五","nongli":"四月廿一"},{"date":"2016-05-28","info":{"night":["1","多云","17","西北风","微风","19:30"],"day":["3","阵雨","23","西北风","微风","07:30"]},"week":"六","nongli":"四月廿二"}],"pm25":{"key":"","show_desc":0,"pm25":{"curPm":"","pm25":"","pm10":"","level":null,"quality":null,"des":null},"dateTime":"2016年05月22日19时","cityName":"望城"},"date":null,"isForeign":0}
     */

    private ResultBean result;
    private int error_code;

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public ResultBean getResult()
    {
        return result;
    }

    public void setResult(ResultBean result)
    {
        this.result = result;
    }

    public int getError_code()
    {
        return error_code;
    }

    public void setError_code(int error_code)
    {
        this.error_code = error_code;
    }

    public static class ResultBean implements Parcelable
    {
        @Override
        public String toString()
        {
            return "ResultBean{" +
                    "data=" + data +
                    '}';
        }

        /**
         * realtime : {"wind":{"windspeed":"14.0","direct":"北风","power":"2级","offset":null},"time":"19:00:00","weather":{"humidity":"85","img":"2","info":"阴","temperature":"19"},"dataUptime":1463917984,"date":"2016-05-22","city_code":"101250105","city_name":"望城","week":0,"moon":"四月十六"}
         * life : {"date":"2016-5-22","info":{"kongtiao":["较少开启","您将感到很舒适，一般不需要开启空调。"],"yundong":["较不宜","有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意携带雨具并注意避雨防滑。"],"ziwaixian":["最弱","属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"],"ganmao":["较易发","天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":["良","气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。"],"chuanyi":["较舒适","建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"]}}
         * weather : [{"date":"2016-05-22","info":{"night":["2","阴","18","无持续风向","微风","19:16"],"day":["1","多云","23","北风","微风","05:34"]},"week":"日","nongli":"四月十六"},{"date":"2016-05-23","info":{"dawn":["2","阴","18","无持续风向","微风","19:16"],"night":["2","阴","17","无持续风向","微风","19:16"],"day":["3","阵雨","22","西北风","微风","05:34"]},"week":"一","nongli":"四月十七"},{"date":"2016-05-24","info":{"dawn":["2","阴","17","无持续风向","微风","19:16"],"night":["1","多云","19","南风","微风","19:17"],"day":["1","多云","27","无持续风向","微风","05:33"]},"week":"二","nongli":"四月十八"},{"date":"2016-05-25","info":{"dawn":["1","多云","19","南风","微风","19:17"],"night":["8","中雨","20","南风","微风","19:17"],"day":["3","阵雨","27","南风","微风","05:33"]},"week":"三","nongli":"四月十九"},{"date":"2016-05-26","info":{"dawn":["8","中雨","20","南风","微风","19:17"],"night":["7","小雨","18","北风","3-4 级","19:18"],"day":["8","中雨","25","北风","3-4 级","05:33"]},"week":"四","nongli":"四月二十"},{"date":"2016-05-27","info":{"night":["3","阵雨","16","西北风","微风","19:30"],"day":["3","阵雨","20","西北风","微风","07:30"]},"week":"五","nongli":"四月廿一"},{"date":"2016-05-28","info":{"night":["1","多云","17","西北风","微风","19:30"],"day":["3","阵雨","23","西北风","微风","07:30"]},"week":"六","nongli":"四月廿二"}]
         * pm25 : {"key":"","show_desc":0,"pm25":{"curPm":"","pm25":"","pm10":"","level":null,"quality":null,"des":null},"dateTime":"2016年05月22日19时","cityName":"望城"}
         * date : null
         * isForeign : 0
         */

        private DataBean data;

        public DataBean getData()
        {
            return data;
        }

        public void setData(DataBean data)
        {
            this.data = data;
        }

        public static class DataBean implements Parcelable
        {
            @Override
            public String toString()
            {
                return "DataBean{" +
                        "realtime=" + realtime +
                        ", life=" + life +
                        ", date=" + date +
                        ", isForeign=" + isForeign +
                        ", weather=" + weather +
                        '}';
            }

            /**
             * wind : {"windspeed":"14.0","direct":"北风","power":"2级","offset":null}
             * time : 19:00:00
             * weather : {"humidity":"85","img":"2","info":"阴","temperature":"19"}
             * dataUptime : 1463917984
             * date : 2016-05-22
             * city_code : 101250105
             * city_name : 望城
             * week : 0
             * moon : 四月十六
             */

            private RealtimeBean realtime;
            /**
             * date : 2016-5-22
             * info : {"kongtiao":["较少开启","您将感到很舒适，一般不需要开启空调。"],"yundong":["较不宜","有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意携带雨具并注意避雨防滑。"],"ziwaixian":["最弱","属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"],"ganmao":["较易发","天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":["良","气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。"],"chuanyi":["较舒适","建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"]}
             */

            private LifeBean life;
            private Object date;
            private int isForeign;
            /**
             * date : 2016-05-22
             * info : {"night":["2","阴","18","无持续风向","微风","19:16"],"day":["1","多云","23","北风","微风","05:34"]}
             * week : 日
             * nongli : 四月十六
             */

            private List<WeatherBean> weather;

            public RealtimeBean getRealtime()
            {
                return realtime;
            }

            public void setRealtime(RealtimeBean realtime)
            {
                this.realtime = realtime;
            }

            public LifeBean getLife()
            {
                return life;
            }

            public void setLife(LifeBean life)
            {
                this.life = life;
            }

            public Object getDate()
            {
                return date;
            }

            public void setDate(Object date)
            {
                this.date = date;
            }

            public int getIsForeign()
            {
                return isForeign;
            }

            public void setIsForeign(int isForeign)
            {
                this.isForeign = isForeign;
            }

            public List<WeatherBean> getWeather()
            {
                return weather;
            }

            public void setWeather(List<WeatherBean> weather)
            {
                this.weather = weather;
            }

            public static class RealtimeBean implements Parcelable
            {
                @Override
                public String toString()
                {
                    return "RealtimeBean{" +
                            "wind=" + wind +
                            ", time='" + time + '\'' +
                            ", weather=" + weather +
                            ", dataUptime=" + dataUptime +
                            ", date='" + date + '\'' +
                            ", city_code='" + city_code + '\'' +
                            ", city_name='" + city_name + '\'' +
                            ", week=" + week +
                            ", moon='" + moon + '\'' +
                            '}';
                }

                /**
                 * windspeed : 14.0
                 * direct : 北风
                 * power : 2级
                 * offset : null
                 */

                private WindBean wind;
                private String time;
                /**
                 * humidity : 85
                 * img : 2
                 * info : 阴
                 * temperature : 19
                 */

                private WeatherBean weather;
                private int dataUptime;
                private String date;
                private String city_code;
                private String city_name;
                private int week;
                private String moon;

                public WindBean getWind()
                {
                    return wind;
                }

                public void setWind(WindBean wind)
                {
                    this.wind = wind;
                }

                public String getTime()
                {
                    return time;
                }

                public void setTime(String time)
                {
                    this.time = time;
                }

                public WeatherBean getWeather()
                {
                    return weather;
                }

                public void setWeather(WeatherBean weather)
                {
                    this.weather = weather;
                }

                public int getDataUptime()
                {
                    return dataUptime;
                }

                public void setDataUptime(int dataUptime)
                {
                    this.dataUptime = dataUptime;
                }

                public String getDate()
                {
                    return date;
                }

                public void setDate(String date)
                {
                    this.date = date;
                }

                public String getCity_code()
                {
                    return city_code;
                }

                public void setCity_code(String city_code)
                {
                    this.city_code = city_code;
                }

                public String getCity_name()
                {
                    return city_name;
                }

                public void setCity_name(String city_name)
                {
                    this.city_name = city_name;
                }

                public int getWeek()
                {
                    return week;
                }

                public void setWeek(int week)
                {
                    this.week = week;
                }

                public String getMoon()
                {
                    return moon;
                }

                public void setMoon(String moon)
                {
                    this.moon = moon;
                }

                public static class WindBean implements Parcelable
                {
                    @Override
                    public String toString()
                    {
                        return "WindBean{" +
                                "windspeed='" + windspeed + '\'' +
                                ", direct='" + direct + '\'' +
                                ", power='" + power + '\'' +
                                ", offset=" + offset +
                                '}';
                    }

                    private String windspeed;
                    private String direct;
                    private String power;
                    private Object offset;

                    public String getWindspeed()
                    {
                        return windspeed;
                    }

                    public void setWindspeed(String windspeed)
                    {
                        this.windspeed = windspeed;
                    }

                    public String getDirect()
                    {
                        return direct;
                    }

                    public void setDirect(String direct)
                    {
                        this.direct = direct;
                    }

                    public String getPower()
                    {
                        return power;
                    }

                    public void setPower(String power)
                    {
                        this.power = power;
                    }

                    public Object getOffset()
                    {
                        return offset;
                    }

                    public void setOffset(Object offset)
                    {
                        this.offset = offset;
                    }

                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags)
                    {
                        dest.writeString(this.windspeed);
                        dest.writeString(this.direct);
                        dest.writeString(this.power);
                        dest.writeParcelable((Parcelable) this.offset, flags);
                    }

                    public WindBean()
                    {
                    }

                    protected WindBean(Parcel in)
                    {
                        this.windspeed = in.readString();
                        this.direct = in.readString();
                        this.power = in.readString();
                        this.offset = in.readParcelable(Object.class.getClassLoader());
                    }

                    public static final Creator<WindBean> CREATOR = new Creator<WindBean>()
                    {
                        @Override
                        public WindBean createFromParcel(Parcel source)
                        {
                            return new WindBean(source);
                        }

                        @Override
                        public WindBean[] newArray(int size)
                        {
                            return new WindBean[size];
                        }
                    };
                }

                public static class WeatherBean implements Parcelable
                {
                    @Override
                    public String toString()
                    {
                        return "WeatherBean{" +
                                "humidity='" + humidity + '\'' +
                                ", img='" + img + '\'' +
                                ", info='" + info + '\'' +
                                ", temperature='" + temperature + '\'' +
                                '}';
                    }

                    private String humidity;
                    private String img;
                    private String info;
                    private String temperature;

                    public String getHumidity()
                    {
                        return humidity;
                    }

                    public void setHumidity(String humidity)
                    {
                        this.humidity = humidity;
                    }

                    public String getImg()
                    {
                        return img;
                    }

                    public void setImg(String img)
                    {
                        this.img = img;
                    }

                    public String getInfo()
                    {
                        return info;
                    }

                    public void setInfo(String info)
                    {
                        this.info = info;
                    }

                    public String getTemperature()
                    {
                        return temperature;
                    }

                    public void setTemperature(String temperature)
                    {
                        this.temperature = temperature;
                    }

                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags)
                    {
                        dest.writeString(this.humidity);
                        dest.writeString(this.img);
                        dest.writeString(this.info);
                        dest.writeString(this.temperature);
                    }

                    public WeatherBean()
                    {
                    }

                    protected WeatherBean(Parcel in)
                    {
                        this.humidity = in.readString();
                        this.img = in.readString();
                        this.info = in.readString();
                        this.temperature = in.readString();
                    }

                    public static final Creator<WeatherBean> CREATOR = new Creator<WeatherBean>()
                    {
                        @Override
                        public WeatherBean createFromParcel(Parcel source)
                        {
                            return new WeatherBean(source);
                        }

                        @Override
                        public WeatherBean[] newArray(int size)
                        {
                            return new WeatherBean[size];
                        }
                    };
                }

                public RealtimeBean()
                {
                }

                @Override
                public int describeContents()
                {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags)
                {
                    dest.writeParcelable(this.wind, flags);
                    dest.writeString(this.time);
                    dest.writeParcelable(this.weather, flags);
                    dest.writeInt(this.dataUptime);
                    dest.writeString(this.date);
                    dest.writeString(this.city_code);
                    dest.writeString(this.city_name);
                    dest.writeInt(this.week);
                    dest.writeString(this.moon);
                }

                protected RealtimeBean(Parcel in)
                {
                    this.wind = in.readParcelable(WindBean.class.getClassLoader());
                    this.time = in.readString();
                    this.weather = in.readParcelable(WeatherBean.class.getClassLoader());
                    this.dataUptime = in.readInt();
                    this.date = in.readString();
                    this.city_code = in.readString();
                    this.city_name = in.readString();
                    this.week = in.readInt();
                    this.moon = in.readString();
                }

                public static final Creator<RealtimeBean> CREATOR = new Creator<RealtimeBean>()
                {
                    @Override
                    public RealtimeBean createFromParcel(Parcel source)
                    {
                        return new RealtimeBean(source);
                    }

                    @Override
                    public RealtimeBean[] newArray(int size)
                    {
                        return new RealtimeBean[size];
                    }
                };
            }

            public static class LifeBean implements Parcelable
            {
                @Override
                public String toString()
                {
                    return "LifeBean{" +
                            "date='" + date + '\'' +
                            ", info=" + info +
                            '}';
                }

                private String date;
                private InfoBean info;

                public String getDate()
                {
                    return date;
                }

                public void setDate(String date)
                {
                    this.date = date;
                }

                public InfoBean getInfo()
                {
                    return info;
                }

                public void setInfo(InfoBean info)
                {
                    this.info = info;
                }

                public static class InfoBean implements Parcelable
                {
                    @Override
                    public String toString()
                    {
                        return "InfoBean{" +
                                "kongtiao=" + kongtiao +
                                ", yundong=" + yundong +
                                ", ziwaixian=" + ziwaixian +
                                ", ganmao=" + ganmao +
                                ", xiche=" + xiche +
                                ", wuran=" + wuran +
                                ", chuanyi=" + chuanyi +
                                '}';
                    }

                    private List<String> kongtiao;
                    private List<String> yundong;
                    private List<String> ziwaixian;
                    private List<String> ganmao;
                    private List<String> xiche;
                    private List<String> wuran;
                    private List<String> chuanyi;

                    public List<String> getKongtiao()
                    {
                        return kongtiao;
                    }

                    public void setKongtiao(List<String> kongtiao)
                    {
                        this.kongtiao = kongtiao;
                    }

                    public List<String> getYundong()
                    {
                        return yundong;
                    }

                    public void setYundong(List<String> yundong)
                    {
                        this.yundong = yundong;
                    }

                    public List<String> getZiwaixian()
                    {
                        return ziwaixian;
                    }

                    public void setZiwaixian(List<String> ziwaixian)
                    {
                        this.ziwaixian = ziwaixian;
                    }

                    public List<String> getGanmao()
                    {
                        return ganmao;
                    }

                    public void setGanmao(List<String> ganmao)
                    {
                        this.ganmao = ganmao;
                    }

                    public List<String> getXiche()
                    {
                        return xiche;
                    }

                    public void setXiche(List<String> xiche)
                    {
                        this.xiche = xiche;
                    }

                    public List<String> getWuran()
                    {
                        return wuran;
                    }

                    public void setWuran(List<String> wuran)
                    {
                        this.wuran = wuran;
                    }

                    public List<String> getChuanyi()
                    {
                        return chuanyi;
                    }

                    public void setChuanyi(List<String> chuanyi)
                    {
                        this.chuanyi = chuanyi;
                    }

                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags)
                    {
                        dest.writeStringList(this.kongtiao);
                        dest.writeStringList(this.yundong);
                        dest.writeStringList(this.ziwaixian);
                        dest.writeStringList(this.ganmao);
                        dest.writeStringList(this.xiche);
                        dest.writeStringList(this.wuran);
                        dest.writeStringList(this.chuanyi);
                    }

                    public InfoBean()
                    {
                    }

                    protected InfoBean(Parcel in)
                    {
                        this.kongtiao = in.createStringArrayList();
                        this.yundong = in.createStringArrayList();
                        this.ziwaixian = in.createStringArrayList();
                        this.ganmao = in.createStringArrayList();
                        this.xiche = in.createStringArrayList();
                        this.wuran = in.createStringArrayList();
                        this.chuanyi = in.createStringArrayList();
                    }

                    public static final Creator<InfoBean> CREATOR = new Creator<InfoBean>()
                    {
                        @Override
                        public InfoBean createFromParcel(Parcel source)
                        {
                            return new InfoBean(source);
                        }

                        @Override
                        public InfoBean[] newArray(int size)
                        {
                            return new InfoBean[size];
                        }
                    };
                }

                @Override
                public int describeContents()
                {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags)
                {
                    dest.writeString(this.date);
                    dest.writeParcelable(this.info, flags);
                }

                public LifeBean()
                {
                }

                protected LifeBean(Parcel in)
                {
                    this.date = in.readString();
                    this.info = in.readParcelable(InfoBean.class.getClassLoader());
                }

                public static final Creator<LifeBean> CREATOR = new Creator<LifeBean>()
                {
                    @Override
                    public LifeBean createFromParcel(Parcel source)
                    {
                        return new LifeBean(source);
                    }

                    @Override
                    public LifeBean[] newArray(int size)
                    {
                        return new LifeBean[size];
                    }
                };
            }

            public static class WeatherBean implements Parcelable
            {
                @Override
                public String toString()
                {
                    return "WeatherBean{" +
                            "date='" + date + '\'' +
                            ", info=" + info +
                            ", week='" + week + '\'' +
                            ", nongli='" + nongli + '\'' +
                            '}';
                }

                private String date;//几月几日
                private InfoBean info;//信息
                private String week;//星期几
                private String nongli;//农历

                public String getDate()
                {
                    return date;
                }

                public void setDate(String date)
                {
                    this.date = date;
                }

                public InfoBean getInfo()
                {
                    return info;
                }

                public void setInfo(InfoBean info)
                {
                    this.info = info;
                }

                public String getWeek()
                {
                    return week;
                }

                public void setWeek(String week)
                {
                    this.week = week;
                }

                public String getNongli()
                {
                    return nongli;
                }

                public void setNongli(String nongli)
                {
                    this.nongli = nongli;
                }

                public static class InfoBean implements Parcelable
                {
                    @Override
                    public String toString()
                    {
                        return "InfoBean{" +
                                "night=" + night +
                                ", day=" + day +
                                '}';
                    }

                    private List<String> night;
                    private List<String> day;

                    public List<String> getNight()
                    {
                        return night;
                    }

                    public void setNight(List<String> night)
                    {
                        this.night = night;
                    }

                    public List<String> getDay()
                    {
                        return day;
                    }

                    public void setDay(List<String> day)
                    {
                        this.day = day;
                    }

                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags)
                    {
                        dest.writeStringList(this.night);
                        dest.writeStringList(this.day);
                    }

                    public InfoBean()
                    {
                    }

                    protected InfoBean(Parcel in)
                    {
                        this.night = in.createStringArrayList();
                        this.day = in.createStringArrayList();
                    }

                    public static final Creator<InfoBean> CREATOR = new Creator<InfoBean>()
                    {
                        @Override
                        public InfoBean createFromParcel(Parcel source)
                        {
                            return new InfoBean(source);
                        }

                        @Override
                        public InfoBean[] newArray(int size)
                        {
                            return new InfoBean[size];
                        }
                    };
                }

                @Override
                public int describeContents()
                {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags)
                {
                    dest.writeString(this.date);
                    dest.writeParcelable((Parcelable) this.info, flags);
                    dest.writeString(this.week);
                    dest.writeString(this.nongli);
                }

                public WeatherBean()
                {
                }

                protected WeatherBean(Parcel in)
                {
                    this.date = in.readString();
                    this.info = in.readParcelable(InfoBean.class.getClassLoader());
                    this.week = in.readString();
                    this.nongli = in.readString();
                }

                public static final Creator<WeatherBean> CREATOR = new Creator<WeatherBean>()
                {
                    @Override
                    public WeatherBean createFromParcel(Parcel source)
                    {
                        return new WeatherBean(source);
                    }

                    @Override
                    public WeatherBean[] newArray(int size)
                    {
                        return new WeatherBean[size];
                    }
                };
            }

            public DataBean()
            {
            }

            @Override
            public int describeContents()
            {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags)
            {
                dest.writeParcelable(this.realtime, flags);
                dest.writeParcelable(this.life, flags);
                dest.writeParcelable((Parcelable) this.date, flags);
                dest.writeInt(this.isForeign);
                dest.writeList(this.weather);
            }

            protected DataBean(Parcel in)
            {
                this.realtime = in.readParcelable(RealtimeBean.class.getClassLoader());
                this.life = in.readParcelable(LifeBean.class.getClassLoader());
                this.date = in.readParcelable(Object.class.getClassLoader());
                this.isForeign = in.readInt();
                this.weather = new ArrayList<WeatherBean>();
                in.readList(this.weather, WeatherBean.class.getClassLoader());
            }

            public static final Creator<DataBean> CREATOR = new Creator<DataBean>()
            {
                @Override
                public DataBean createFromParcel(Parcel source)
                {
                    return new DataBean(source);
                }

                @Override
                public DataBean[] newArray(int size)
                {
                    return new DataBean[size];
                }
            };
        }

        public ResultBean()
        {
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeParcelable(this.data, flags);
        }

        protected ResultBean(Parcel in)
        {
            this.data = in.readParcelable(DataBean.class.getClassLoader());
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>()
        {
            @Override
            public ResultBean createFromParcel(Parcel source)
            {
                return new ResultBean(source);
            }

            @Override
            public ResultBean[] newArray(int size)
            {
                return new ResultBean[size];
            }
        };
    }

    public WeatherInfo()
    {
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.reason);
        dest.writeParcelable(this.result, flags);
        dest.writeInt(this.error_code);
    }

    protected WeatherInfo(Parcel in)
    {
        this.reason = in.readString();
        this.result = in.readParcelable(ResultBean.class.getClassLoader());
        this.error_code = in.readInt();
    }

    public static final Creator<WeatherInfo> CREATOR = new Creator<WeatherInfo>()
    {
        @Override
        public WeatherInfo createFromParcel(Parcel source)
        {
            return new WeatherInfo(source);
        }

        @Override
        public WeatherInfo[] newArray(int size)
        {
            return new WeatherInfo[size];
        }
    };
}
