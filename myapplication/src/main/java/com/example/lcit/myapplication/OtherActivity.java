package com.example.lcit.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by ${qiuweizhong} on 2017/3/13.
 * 在这个类中制造内存溢出，使用handle的postdelayed
 */
public class OtherActivity extends FragmentActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
//                Toast.makeText(OtherActivity.this, "测试内存溢出", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtherActivity.this.finish();
            }
        });
    }

    public void btnclick(View vie) {
        Message message = new Message();
        message.arg1 = 123;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(OtherActivity.this, "测试内存溢出", Toast.LENGTH_SHORT).show();
            }
        }, 8000);

    }
}
