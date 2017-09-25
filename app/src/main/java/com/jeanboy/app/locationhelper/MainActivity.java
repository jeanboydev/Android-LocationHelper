package com.jeanboy.app.locationhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jeanboy.lib.locationhelper.MyLocationHelper;
import com.jeanboy.lib.locationhelper.callback.LocationListener;
import com.jeanboy.lib.locationhelper.model.LocationModel;

public class MainActivity extends AppCompatActivity implements LocationListener {


    private TextView tv_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_info = (TextView) findViewById(R.id.tv_info);
    }


    @Override
    public void onUpdate(LocationModel locationModel) {
        Log.e(MainActivity.class.getName(), "定位成功 LocationModel:" + locationModel.toString());

        tv_info.setText(locationModel.toString());
    }

    @Override
    public void onNeedPermission() {
        Log.e(MainActivity.class.getName(), "定位失败 onNeedPermission");

        tv_info.setText("定位失败 onNeedPermission");

    }

    @Override
    public void onNeedPositionService() {
        Log.e(MainActivity.class.getName(), "定位失败 onNeedPositionService");

        tv_info.setText("定位失败 onNeedPositionService");

    }

    @Override
    public void onError() {
        Log.e(MainActivity.class.getName(), "定位失败 onError");

        tv_info.setText("定位失败 onError");

    }

    public void toSettings(View view) {
        startActivity(MyLocationHelper.getToLocationActionIntent());
    }

    public void getLocation(View view) {
        MyLocationHelper.requestLocation(this, this);
    }
}
