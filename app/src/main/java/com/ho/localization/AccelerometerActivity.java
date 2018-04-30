package com.ho.localization;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccelerometerActivity extends AppCompatActivity {

    @BindView(R.id.result_x) TextView resultX;
    @BindView(R.id.result_y) TextView resultY;
    @BindView(R.id.result_z) TextView resultZ;
    @BindView(R.id.sum) TextView sum;
    @BindView(R.id.actual_result) TextView actualResult;

    SensorManager manager;
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER :
                    float[] temp = {event.values[0], event.values[1], event.values[2]};
                    resultX.setText(String.valueOf(temp[0]));
                    resultY.setText(String.valueOf(temp[1]));
                    resultZ.setText(String.valueOf(temp[2]));
                    sum.setText(String.valueOf(Math.sqrt(temp[0]*temp[0]+temp[1]*temp[1]+temp[2]*temp[2])));
                    actualResult.setText(String.valueOf(Math.sqrt(temp[0]*temp[0]+temp[1]*temp[1]+temp[2]*temp[2])-9.62));
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        ButterKnife.bind(this);

        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        manager.registerListener(listener
        , manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        , 50000);
    }
}
