package com.example.lcit.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 处理fragment切换数据重新加载的问题
 */
public class MainActivity extends AppCompatActivity {

    private Fragment1 mFragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragment1 = new Fragment1();
    }

    public void btnclick(View view) {

        switch (view.getId()) {
            case R.id.btn1:
//                getSupportFragmentManager().beginTransaction().add(R.id.fl, mFragment1)
                break;
            case R.id.btn2:
                break;
        }

    }

}
