package com.ho.localization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class DrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewEx view = new ViewEx(this);

        //todo 내가 가진 뷰 위에서 그림 그리는법?
        setContentView(view);
    }

    protected class ViewEx extends View {

        public ViewEx(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);

            Paint paint = new Paint();
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GRAY);

            Path path = new Path();
            path.moveTo(100, 100);
            path.lineTo(100, 100);
            path.lineTo(200, 200);
            canvas.drawPath(path, paint);
        }
    }
}
