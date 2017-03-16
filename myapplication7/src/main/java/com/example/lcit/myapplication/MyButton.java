package com.example.lcit.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by ${qiuweizhong} on 2017/3/16.
 */
public class MyButton extends Button {
    public MyButton(Context context) {
        super(context);
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        Log.d("print", "MyButton  dispatchTouchEvent");//点击一次，抬起一次
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("print", "ACTION_DOWN  dispatchTouchEvent");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("print", "ACTION_MOVE  dispatchTouchEvent");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("print", "ACTION_UP  dispatchTouchEvent");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("print", "MyButton  onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("print", "ACTION_DOWN  onTouchEvent");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("print", "ACTION_MOVE  onTouchEvent");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("print", "ACTION_UP  onTouchEvent");
                break;
        }
        return false;
    }
}
