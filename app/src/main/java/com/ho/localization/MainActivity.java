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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    enum Direction {UP, RIGHT, DOWN, LEFT}

    private SensorManager manager;
    private SensorEventListener listener;
    private Sensor accelerometer, magneticField, gravitySensor;

    private float[] magnetic;
    private float[] accel;
    private float[] gravity;
    private float[] degrees;
    private float velocity;

    final float THROUGHPUT = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.activity_main, null);

//        DrawView drawView = new DrawView(this);
//        RelativeLayout.LayoutParams drawParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        drawView.setLayoutParams(drawParams);
//        drawView.setBackgroundColor(Color.TRANSPARENT);

//        relativeLayout.addView(drawView);

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

        Direction direction = getDirection(degrees[0]);
        accel[0] -= gravity[0];
        accel[1] -= gravity[1];
        accel[2] -= gravity[2];
        // only think about z-axis velocity
        if (Math.abs(accel[2]) > THROUGHPUT) {
            velocity += accel[2] * 0.05;
        }
        draw(direction);
        // Display the compass direction
//        direction.setText(getDirectionFromDegree(values[0]) + values[0]);
        // Display the raw values
//        value.setText(String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f", values[0], values[1], values[2]));
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

    private void draw(Direction direction) {

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

        public DrawView(Context context) {
            super(context);

            paint = new Paint();
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);

            path = new Path();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //canvas.drawColor(Color.TRANSPARENT);
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x, y);
                case MotionEvent.ACTION_UP:
                    break;
            }
            invalidate();

            return true;
        }
    }

    private void buttonClick (int number)
    {
        switch (number)
        {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            default:
                Toast.makeText(this, "Wrong button number!", Toast.LENGTH_SHORT).show();
        }

        startMove();
    }

    private void startMove ()
    {

    }

    private void stopMove ()
    {

    }

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

    @OnClick(R.id.stop_button)
    public void clickStop (Button button)
    {
        stopMove();
    }

    @OnClick(R.id.button1)
    public void click1 (ImageView imageView)
    {
        buttonClick(1);
    }

    @OnClick(R.id.button2)
    public void click2 (ImageView imageView)
    {
        buttonClick(2);
    }

    @OnClick(R.id.button3)
    public void click3 (ImageView imageView)
    {
        buttonClick(3);
    }

    @OnClick(R.id.button4)
    public void click4 (ImageView imageView)
    {
        buttonClick(4);
    }

    @OnClick(R.id.button5)
    public void click5 (ImageView imageView)
    {
        buttonClick(5);
    }

    @OnClick(R.id.button6)
    public void click6 (ImageView imageView)
    {
        buttonClick(6);
    }

    @OnClick(R.id.button7)
    public void click7 (ImageView imageView)
    {
        buttonClick(7);
    }

    @OnClick(R.id.button8)
    public void click8 (ImageView imageView)
    {
        buttonClick(8);
    }

    @OnClick(R.id.button9)
    public void click9 (ImageView imageView)
    {
        buttonClick(9);
    }
}
