package com.ho.localization;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MagneticActivity extends AppCompatActivity {

    @BindView(R.id.direction) TextView direction;
    @BindView(R.id.value) TextView value;
    @BindView(R.id.save1) TextView save1;
    @BindView(R.id.save2) TextView save2;
    @BindView(R.id.save3) TextView save3;
    @BindView(R.id.save4) TextView save4;
    @BindView(R.id.save5) TextView save5;

    SensorManager manager;
    SensorEventListener listener;
    Sensor accelerometer, magneticField;

    float[] gravity;
    float[] magnetic;
    float[] values;

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
        values = new float[3];
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(listener, accelerometer, 50000);
        manager.registerListener(listener, magneticField, 50000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(listener);
    }

    private void updateDirection() {
        float[] temp = new float[9];

        // Load rotation matrix into R
        SensorManager.getRotationMatrix(temp, null, gravity, magnetic);
        // Return the orientation values
        SensorManager.getOrientation(temp, values);
        // Convert to degrees
        for (int i = 0; i < values.length; i++) {
            values[i] = (float)Math.toDegrees(values[i]);
        }
        // Display the compass direction
        direction.setText(getDirectionFromDegree(values[0]) + values[0]);
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

    @OnClick(R.id.save_button)
    public void saveClick(Button button) {
        SaveProcess thread = new SaveProcess();
        thread.setDaemon(true);
        thread.start();
    }

    class SaveProcess extends Thread {
        @Override
        public void run() {
            final String[] result = new String[5];
            try {
                sleep(5000);
                result[0] = String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]);
                sleep(1000);
                result[1] = String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]);
                sleep(1000);
                result[2] = String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]);
                sleep(1000);
                result[3] = String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]);
                sleep(1000);
                result[4] = String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    save1.setText(result[0]);
                    save2.setText(result[1]);
                    save3.setText(result[2]);
                    save4.setText(result[3]);
                    save5.setText(result[4]);
                }
            });
        }
    }
}
