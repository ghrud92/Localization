package com.ho.localization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    int x, y;   // current location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
