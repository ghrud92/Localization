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
    @BindView(R.id.gravity_x) TextView gravityX;
    @BindView(R.id.gravity_y) TextView gravityY;
    @BindView(R.id.gravity_z) TextView gravityZ;
    @BindView(R.id.uncali_x) TextView uncaliX;
    @BindView(R.id.uncali_y) TextView uncaliY;
    @BindView(R.id.uncali_z) TextView uncaliZ;
    @BindView(R.id.sum) TextView sum;
    @BindView(R.id.actual_result) TextView actualResult;
    @BindView(R.id.gravity_value) TextView gravityValue;

    float[] accel = new float[3];
    float[] gravity = new float[3];
    float[] accel_un = new float[3];
    final float alpha = 0.8f;

    SensorManager manager;
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER :
                    accel = event.values.clone();
                    resultX.setText(String.valueOf(accel[0]));
                    resultY.setText(String.valueOf(accel[1]));
                    resultZ.setText(String.valueOf(accel[2]));
                    sum.setText(String.valueOf(Math.sqrt(accel[0]*accel[0]+accel[1]*accel[1]+accel[2]*accel[2])));
                    actualResult.setText(String.valueOf(Math.sqrt(accel[0]*accel[0]+accel[1]*accel[1]+accel[2]*accel[2])-9.8
                    ));
                    break;
                case Sensor.TYPE_GRAVITY:
                    gravity = event.values.clone();
                    gravityX.setText(String.valueOf(gravity[0]));
                    gravityY.setText(String.valueOf(gravity[1]));
                    gravityZ.setText(String.valueOf(gravity[2]));
                    gravityValue.setText(String.valueOf(Math.sqrt(gravity[0]*gravity[0]+gravity[1]*gravity[1]+gravity[2]*gravity[2])));
                    break;
                case Sensor.TYPE_ACCELEROMETER_UNCALIBRATED:
                    if (gravity != null) {
                        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                        gravity[1] = alpha * gravity[2] + (1 - alpha) * event.values[2];
                    }
                    accel_un[0] = event.values[0] - gravity[0];
                    accel_un[1] = event.values[1] - gravity[1];
                    accel_un[2] = event.values[2] - gravity[2];
                    uncaliX.setText(String.valueOf(accel_un[0]));
                    uncaliY.setText(String.valueOf(accel_un[1]));
                    uncaliZ.setText(String.valueOf(accel_un[2]));
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(
                listener,
                manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                50000
        );
        manager.registerListener(
                listener,
                manager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                50000
        );
        manager.registerListener(
                listener,
                manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED),
                50000
        );
    }

    @Override
    protected void onPause() {
        manager.unregisterListener(listener);
        super.onPause();
    }

}
