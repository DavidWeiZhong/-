package com.example.lcit.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 用fragment保存大数据
 */
public class MainActivity extends AppCompatActivity {

    private String mBitmap = "我就是要保存的bitmap";
//    mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            BitmapDataFragment fragment = (BitmapDataFragment) getSupportFragmentManager()
                    .findFragmentByTag(BitmapDataFragment.TAG);
            String bitmap = fragment.getData();

            Log.d("print", "保存的bitmap--" + bitmap);
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mBitmap != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(BitmapDataFragment.newInstance(mBitmap), BitmapDataFragment.TAG)
                    .commit();
            outState.putBoolean("image", true);
        } else {
            outState.putBoolean("image", false);
        }
        super.onSaveInstanceState(outState);
    }
}



