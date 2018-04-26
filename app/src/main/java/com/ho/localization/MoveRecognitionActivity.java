package com.ho.localization;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

public class MoveRecognitionActivity extends AppCompatActivity {

    double time = 3;
    int samplingPeriodUs = 50000;
    double threshold = 0.1;
    String result = "";
    @BindView(R.id.result) TextView tv;

    private LinkedList<float[]> data = new LinkedList<>();
    SensorManager SM;
    SensorEventListener sL = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Called when there is a new SensorEvent. SensorEvent has values of sensor
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER :
                    float[] temp = {event.values[0], event.values[1], event.values[2]};
                    data.add(temp);
                    if (data.size() > time*1000000/samplingPeriodUs) {
                        data.remove();
                    }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Called when the accuracy of the registered sensor has changed.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_recognition);
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        SM.registerListener(sL, SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), samplingPeriodUs);
    }

    @OnClick(R.id.recognize_button)
    public void click(Button button)
    {
        double magnitude = 0;
        for(int i = 0; i < data.size(); i++) {
            float[] temp = data.get(i);
            magnitude += Math.sqrt(temp[0]*temp[0]+temp[1]*temp[1]+temp[2]*temp[2]) - 9.8;
        }
        magnitude = magnitude / data.size();
        if (magnitude > threshold)
            result = "Move";
        else
            result = "Not Move";
        tv.setText(result);
    }
}
