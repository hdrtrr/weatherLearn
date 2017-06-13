package com.hdrtrr.ccc.weather.util;

import android.text.TextUtils;

import com.hdrtrr.ccc.weather.db.City;
import com.hdrtrr.ccc.weather.db.County;
import com.hdrtrr.ccc.weather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hdrtrr on 2017/6/12.
 * 解析网络请求返回的JSON数据
 * 解析获取天气预报中省市县的JSON数据
 */

public class Utility {
    /**
     * 处理返回的省份JSON
     *
     */
    public static boolean handleProvinceBackData(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allprovinces = new JSONArray(response);
                for (int i = 0; i < allprovinces.length(); i++) {
                    JSONObject provinceObject = allprovinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    /**
     * 处理省对应的市数据
     * @param response 待处理的市JSON数据
     * @param provinceId 所有市对应的省份
     * @return
     */
    public static boolean handleCityBackData(String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    /**
     * 处理所选市区下对应的县
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountyBackData(String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
