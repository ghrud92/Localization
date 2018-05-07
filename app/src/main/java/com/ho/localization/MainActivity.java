package com.ho.localization;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    enum Direction {UP, RIGHT, DOWN, LEFT}

//    @BindView(R.id.velocity) TextView velocityView;
//    @BindView(R.id.direction) TextView directionView;
//    @BindView(R.id.roll) TextView rollView;
    private DrawView drawView;

    private SensorManager manager;
    private SensorEventListener listener;
    private Sensor accelerometer, magneticField, gravitySensor;

    private float[] magnetic;
    private float[] accel;
    private float[] gravity;
    private float[] degrees;
    private float velocity;

    final float THROUGHPUT = 0.5f;
    final float MAX_X = 1440f;
    final float MAX_Y = 2060f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.activity_main, null);

        drawView = new DrawView(this);
        RelativeLayout.LayoutParams drawParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        drawView.setLayoutParams(drawParams);
        drawView.setBackgroundColor(Color.TRANSPARENT);

        relativeLayout.addView(drawView);

        setContentView(relativeLayout);
        ButterKnife.bind(this);

        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        accel = event.values.clone();
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        magnetic = event.values.clone();
                        break;
                    case Sensor.TYPE_GRAVITY:
                        gravity = event.values.clone();
                        break;
                }
                if (accel != null && magnetic != null && gravity != null) {
                    drawPath();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticField = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gravitySensor = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        degrees = new float[3];
        velocity = 0;
    }

    private void drawPath() {
        float[] temp = new float[9];

        // Load rotation matrix into temp
        SensorManager.getRotationMatrix(temp, null, gravity, magnetic);
        // Return the orientation values
        SensorManager.getOrientation(temp, degrees);
        // Convert to degrees
        for (int i = 0; i < degrees.length; i++) {
            degrees[i] = (float)Math.toDegrees(degrees[i]);
        }

        if (!isInPocket(degrees[1], degrees[2]))
            return;

        Direction direction = getDirection(degrees[0]);
        accel[0] -= gravity[0];
        accel[1] -= gravity[1];
        accel[2] -= gravity[2] - 0.35;
        // only think about z-axis velocity
        if (Math.abs(accel[2]) > THROUGHPUT) {
            velocity += accel[2] * 0.05;
//            velocityView.setText(String.valueOf(velocity));
////            velocityView.setText(String.valueOf(accel[2]));
//            switch (direction) {
//                case UP:
//                    directionView.setText("UP");
//                    break;
//                case RIGHT:
//                    directionView.setText("RIGHT");
//                    break;
//                case DOWN:
//                    directionView.setText("DOWN");
//                    break;
//                case LEFT:
//                    directionView.setText("LEFT");
//                    break;
//            }
//            rollView.setText(String.valueOf(degrees[2]));
        }
        drawView.draw(direction, velocity);
        // Display the compass direction
//        direction.setText(getDirectionFromDegree(values[0]) + values[0]);
        // Display the raw values
//        value.setText(String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]));
    }

    private boolean isInPocket(float pitch, float roll) {
        if (pitch > -20 && pitch < 10 && roll > -105 && roll < -75)
            return true;
        return false;
    }

    private Direction getDirection (float azimuth) {
        if (azimuth <= -85 && azimuth > -175)
            return Direction.DOWN;
        else if (azimuth <= 5 && azimuth > -85)
            return Direction.LEFT;
        else if (azimuth <= 95 && azimuth > 5)
            return Direction.UP;
        else
            return Direction.RIGHT;
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(listener, accelerometer, 50000);
        manager.registerListener(listener, magneticField, 50000);
        manager.registerListener(listener, gravitySensor, 50000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(listener);
    }

    private class DrawView extends View {
        Paint paint;
        Path path;
        boolean isTouched;
        float x, y;

        public DrawView(Context context) {
            super(context);

            paint = new Paint();
            paint.setStrokeWidth(10f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);

            path = new Path();
            isTouched = false;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //canvas.drawColor(Color.TRANSPARENT);
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.d("##### onTouchEvent", "x:" + event.getX() + ", y:" + event.getY());
            if (isTouched)
                return true;

            x = event.getX();
            y = event.getY();
            path.moveTo(x, y);
            invalidate();
            isTouched = true;

            return true;
        }

        public void draw(Direction direction, float velocity) {
            if (!isTouched)
                return;
            Log.d("#####", "draw");

            float distance = velocity * 0.1f;
            switch (direction) {
                case UP:
                    Log.d("#####", "UP distance:" + distance);
                    if (y - distance > 0 && y - distance < MAX_Y)
                        y -= distance;
                    break;
                case RIGHT:
                    Log.d("#####", "RIGHT distance:" + distance);
                    if (x + distance > 0 && x + distance < MAX_X)
                        x += distance;
                    break;
                case DOWN:
                    Log.d("#####", "DOWN distance:" + distance);
                    if (y + distance > 0 && y + distance < MAX_Y)
                        y += distance;
                    break;
                case LEFT:
                    Log.d("#####", "LEFT distance:" + distance);
                    if (x - distance > 0 && x - distance < MAX_X)
                        x -= distance;
                    break;
            }
            path.lineTo(x, y);
            invalidate();
        }
    }
/*
    @OnClick(R.id.recognize_button)
    public void goRecognize (Button button)
    {
        startActivity(new Intent(this, MoveRecognitionActivity.class));
    }

    @OnClick(R.id.wifi_button)
    public void goWifi (Button button)
    {
        startActivity(new Intent(this, WifiScanActivity.class));
    }

    @OnClick(R.id.draw_button)
    public void goDraw (Button button)
    {
        startActivity(new Intent(this, DrawActivity.class));
    }

    @OnClick(R.id.accelerometer_button)
    public void goAccelerometer (Button button)
    {
        startActivity(new Intent(this, AccelerometerActivity.class));
    }

    @OnClick(R.id.magnetic_button)
    public void goMagnetic (Button button)
    {
        startActivity(new Intent(this, MagneticActivity.class));
    }
*/
}
