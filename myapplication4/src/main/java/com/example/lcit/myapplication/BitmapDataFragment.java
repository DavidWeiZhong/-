package com.example.lcit.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by ${qiuweizhong} on 2017/3/14.
 */
public class BitmapDataFragment extends Fragment {
    public static final String TAG = "bitmapsaver";
    private Bitmap bitmap;

    public BitmapDataFragment() {

    }

    private BitmapDataFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static BitmapDataFragment newInstance(Bitmap bitmap) {
        return new BitmapDataFragment(bitmap);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//关键啊
    }

    public Bitmap getData() {
        return bitmap;
    }
}
