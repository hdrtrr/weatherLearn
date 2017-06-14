package com.hdrtrr.ccc.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hdrtrr on 2017/6/14.
 * 查询天气返回的基本信息
 */

public class Basic {
//    城市名
    @SerializedName("city")
    public String cityName;

//    城市对应的天气id
    @SerializedName("id")
    public String weatherId;

//    天气更新时间
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
