package com.example.lcit.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 用fragment保存大数据
 */
public class MainActivity extends AppCompatActivity {

    private Bitmap mBitmap, bitmap;
//    mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState.getBoolean("image")) {
            BitmapDataFragment fragment = (BitmapDataFragment) getSupportFragmentManager()
                    .findFragmentByTag(BitmapDataFragment.TAG);
            bitmap = fragment.getData();
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



