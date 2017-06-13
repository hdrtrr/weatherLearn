package com.hdrtrr.ccc.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hdrtrr.ccc.weather.urls.AllUrls;
import com.hdrtrr.ccc.weather.util.HttpUtil;
import com.hdrtrr.ccc.weather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Button button = (Button) findViewById(R.id.testBtn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String address =  AllUrls.GET_PROVINCES;
//                HttpUtil.sendOlHttpRequest(address, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String responseText = response.body().string();
//                        Utility.handleProvinceBackData(responseText);
//
//                    }
//                });
//            }
//        });
    }
}
