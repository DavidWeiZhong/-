package com.example.lcit.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ${qiuweizhong} on 2017/3/16.
 */
public class MyTextView extends TextView{
    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
    }


}
