package com.ho.localization;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MagneticActivity extends AppCompatActivity {

    @BindView(R.id.direction) TextView direction;
    @BindView(R.id.value) TextView value;

    SensorManager manager;
    SensorEventListener listener;
    Sensor accelerometer, magneticField;

    float[] gravity;
    float[] magnetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetic);

        ButterKnife.bind(this);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        gravity = event.values.clone();
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        magnetic = event.values.clone();
                        break;
                    default:
                        return;
                }

                if (gravity != null && magnetic != null) {
                    updateDirection();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticField = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        manager.registerListener(listener, magneticField, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(listener);
    }

    private void updateDirection() {
        float[] temp = new float[9];
        float[] R = new float[9];

        // Load rotation matrix into R
        SensorManager.getRotationMatrix(temp, null, gravity, magnetic);
        // Remap to camera's point-of-view
        SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Z, R);
        // Return the orientation values
        float[] values = new float[3];
        SensorManager.getOrientation(R, values);
        // Convert to degrees
        for (int i = 0; i < values.length; i++) {
            Double degree = (values[i] * 180) / Math.PI;
            values[i] = degree.floatValue();
        }
        // Display the compass direction
        direction.setText(getDirectionFromDegree(values[0]));
        // Display the raw values
        value.setText(String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]));
    }

    private String getDirectionFromDegree(float degrees) {
        if(degrees >= -22.5 && degrees < 22.5) { return "N"; }
        if(degrees >= 22.5 && degrees < 67.5) { return "NE"; }
        if(degrees >= 67.5 && degrees < 112.5) { return "E"; }
        if(degrees >= 112.5 && degrees < 157.5) { return "SE"; }
        if(degrees >= 157.5 || degrees < -157.5) { return "S"; }
        if(degrees >= -157.5 && degrees < -112.5) { return "SW"; }
        if(degrees >= -112.5 && degrees < -67.5) { return "W"; }
        if(degrees >= -67.5 && degrees < -22.5) { return "NW"; }

        return null;
    }
}
