package com.example.lcit.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.iv);
    }

    /**
     * 通过volley获得json数据
     * @param view
     */
    public void getJson(View view) {
        Log.d("print", "点击有效");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://www.baidu.com",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("print", "" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("print", "" + error.getMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * 移步加载图片
     * @param view
     */
    public void getImage(View view) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final LruCache<String, Bitmap> lruCache = new LruCache<>(20);

        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                    lruCache.put(url, bitmap);
            }
        };

        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);

        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(mImageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);

        imageLoader.get("http://g.hiphotos.baidu.com/image/pic/item/1ad5ad6eddc451da7f05e1efb4fd5266d11632c7.jpg", imageListener);
    }
}
