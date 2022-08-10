package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class ThemeActivity extends AppCompatActivity {



    DatabaseHandler databaseHandler;
    List<HashMap<String , Object>> data;
    private String id="1";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    LinearLayout wholeThemeLayout;
    //TextView test_tv = findViewById(R.id.test);
    // 0 is white and 1 is blue background
    int backgroundColor = 0 ;
    double changeInAcceleration;



    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];


            accelerationCurrentValue = Math.sqrt((x*x + y*y + z*z));
            changeInAcceleration = Math.abs(accelerationCurrentValue - accelerationPreviousValue);
            accelerationCurrentValue = accelerationPreviousValue;


            wholeThemeLayout = findViewById(R.id.whole_theme_layout);



            if(changeInAcceleration > 28) {
                if(backgroundColor == 0) {
                    //update database
                    backgroundColor = 1;
                    //databaseHandler = new DatabaseHandler(this);
                    //change
                    databaseHandler.open();
                    databaseHandler.setBookVisitState(id,"1");
                    databaseHandler.close();
                }
                else{
                    //update database
                    backgroundColor = 0;
                    //databaseHandler = new DatabaseHandler(this);
                    //change
                    databaseHandler.open();
                    databaseHandler.setBookVisitState(id,"0");
                    databaseHandler.close();
                }
            }


            //reading data from database

            databaseHandler.open();
            data  = databaseHandler.getTableOfContent();
            databaseHandler.close();
            if(/*backgroundColor == 0*/data.get(0).get("color").toString().equals("0")){
                wholeThemeLayout.setBackgroundColor(getResources().getColor(R.color.white));
            }
            if(/*backgroundColor == 1*/ data.get(0).get("color").toString().equals("1")) {
                wholeThemeLayout.setBackgroundColor(getResources().getColor(R.color.MyColor2));
            }

            //uncomment for testing
            // test_tv.setText("change = " + (int)changeInAcceleration);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        wholeThemeLayout = findViewById(R.id.whole_theme_layout);

        getSupportActionBar().setTitle("Theme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

/*
        String id ="1";
        String name ="working hora hora";


        //databaseHandler = new DatabaseHandler(this);
        //change
        databaseHandler.open();
        databaseHandler.setBookVisitState(id,name);
        databaseHandler.close();
*/

        databaseHandler = new DatabaseHandler(this);
        //read
        databaseHandler.open();
        data  = databaseHandler.getTableOfContent();
        if(data.get(0).get("color").toString().equals("0")) {
            Toast.makeText(this, "Day mode is activated!", Toast.LENGTH_SHORT).show();
            wholeThemeLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }else {
            Toast.makeText(this, "Night mode is activated!", Toast.LENGTH_SHORT).show();
            wholeThemeLayout.setBackgroundColor(getResources().getColor(R.color.MyColor2));
        }
        databaseHandler.close();





    }



    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }

}