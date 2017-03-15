package com.example.lcit.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by ${qiuweizhong} on 2017/3/14.
 */
public class BitmapDataFragment extends Fragment {
    public static final String TAG = "bitmapsaver";
    private String bitmap;

    public BitmapDataFragment() {

    }

    private BitmapDataFragment(String bitmap) {
        this.bitmap = bitmap;
    }

    public static BitmapDataFragment newInstance(String bitmap) {
        return new BitmapDataFragment(bitmap);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//关键啊
    }

    public String getData() {
        return bitmap;
    }
}
