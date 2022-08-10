package com.example.mylogin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import java.sql.Time;

public class MyDrawMain extends View {


    float radius=300;

    Rect myRect;
    RadialGradient shader;
    Matrix m  = new Matrix();
    float bx = 0;
    float by = 0;
    float vx = 50;
    float vy = 100;
    long current= System.currentTimeMillis();

    public MyDrawMain(Context context) {
        super(context);

        myRect = new Rect();

        shader = new RadialGradient(0, 0,radius , getResources().getColor(R.color.MyColor2)
                , getResources().getColor(R.color.MyColor1), Shader.TileMode.MIRROR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        long now = System.currentTimeMillis();
        float dt = now - current;
        current = now;

       //Log.i("var = ","now = "+now +" current = "+current+"dt = "+dt + " bx = "+ bx);


        //we want to restrict to screen
//        if (bx > getWidth()-radius || bx < 0)
//        {
//            vx *= -1f;
//
//        }
        bx = bx + vx * -1 * dt / 1000;
        //we want to restrict to screen
//        if (by > getHeight()-radius || by < 0)
//        {
//            vy *= -1f;
//
//        }
        by = by + vy * -1 * dt / 1000;

        m.setTranslate(bx, by);


        myRect.top = 0;
        myRect.bottom = getHeight();
        myRect.right = getWidth();
        myRect.left = 0;

        Paint newPaint = new Paint();
        newPaint.setShader(shader);
        newPaint.getShader().setLocalMatrix(m);


        canvas.drawRect(myRect, newPaint);

        Rect textBound = new Rect();
        String n =getResources().getText(R.string.app_name).toString();
        Paint textPaint =new Paint();
        textPaint.setColor(getResources().getColor(R.color.white));
        textPaint.setTextSize(120);

        textPaint.getTextBounds(n,0,n.length(),textBound);
        float offset1 = Math.abs(textBound.left - textBound.right)/2;
        canvas.drawText(n,getWidth()/2-offset1,getHeight()/3,textPaint);


        Rect textBound_tap = new Rect();
        Paint textPaint_tap =new Paint();
        textPaint_tap.setColor(getResources().getColor(R.color.white));
        textPaint_tap.setTextSize(40);


        String tap ="tap the screen to continue!";
        textPaint.getTextBounds(tap,0,n.length(),textBound);
        float offset2 = Math.abs(textBound.left - textBound.right)/2;
        canvas.drawText(tap,getWidth()/2-offset2,2*(getHeight()/3),textPaint_tap);


        invalidate();



    }
}
