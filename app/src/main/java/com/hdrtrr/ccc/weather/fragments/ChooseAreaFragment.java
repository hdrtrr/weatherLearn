package com.hdrtrr.ccc.weather.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdrtrr.ccc.weather.R;
import com.hdrtrr.ccc.weather.db.City;
import com.hdrtrr.ccc.weather.db.County;
import com.hdrtrr.ccc.weather.db.Province;
import com.hdrtrr.ccc.weather.urls.AllUrls;
import com.hdrtrr.ccc.weather.util.HttpUtil;
import com.hdrtrr.ccc.weather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hdrtrr on 2017/6/13.
 */

public class ChooseAreaFragment extends Fragment{
    public static final int LEVEL_PROVINCE = 0;
    public static  final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private TextView titleText;
    private Button backBtn;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ProgressDialog progressDialog;

//    省列表
    private List<Province> provinceList;
//    市列表
    private List<City> cityList;
//    县列表
    private List<County> countyList;
//    选中的省
    private Province selectedProvince;
//    选中的市
    private City selectedCity;
//    选中的县
    private County selectedCounty;
//    当前选中的级别
    private int currentLevel;
    private List<String> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
         titleText = (TextView) view.findViewById(R.id.title_text);
         backBtn = (Button) view.findViewById(R.id.back_btn);
         listView = (ListView) view.findViewById(R.id.choose_area_list_view);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //点击item事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                选择的是省份列表
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }
//                选择是市对应的列表
                else if (currentLevel== LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
//        返回按钮事件
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
//        默认先查询所有的省
        queryProvinces();

    }

    /**
     *
     * @param address 查询地址
     * @param type 表示需要查询的是什么类型的数据（省||市||县）
     */
    private void queryFromServer(String address, final String type) {
        showProgessDialog();
        HttpUtil.sendOlHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败调用，通过runOnUiThread方法回到主线程处理相应逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                请求返回的文本数据
                String responseText = response.body().string();
                boolean result = false;
                //如果请求的是省份数据
                if ("province".equals(type)){
                    result = Utility.handleProvinceBackData(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCityBackData(responseText,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyBackData(responseText,selectedCity.getId());
                }
                //如果返回数据处理正确，通知主线程listView显示对应的数据
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });
    }

    /**
     * 显示等待对话框
     */
    private void showProgessDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**
     * 关闭对话框
     */
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    /**
     * 查询所有的省份，先从数据库中查询，无则从服务器中查询
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backBtn.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);//从数据库中查询省
        if (provinceList.size()>0){
            dataList.clear();//列表数据清空
            for (Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();//通知列表更新
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = AllUrls.GET_PROVINCES;
            queryFromServer(address,"province");
        }
    }

    /**
     * 查询选中的省份所对应的所有的市，先从数据库中查询，无则从服务器中查询
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());//更换头标题
        backBtn.setVisibility(View.VISIBLE);//显示返回按钮
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size()>0){
            dataList.clear();
            for (City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = AllUrls.GET_PROVINCES +"/" +provinceCode;
            queryFromServer(address,"city");
        }
    }
    /**
     * 查询选中的市对应下的所有县，先从数据库中查询，无则再从服务器中查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backBtn.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        //如果数据库中有值
        if (countyList.size()>0){
            dataList.clear();
            for (County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = AllUrls.GET_PROVINCES + "/" +provinceCode + "/" +cityCode;
            queryFromServer(address,"county");
        }

    }

}
