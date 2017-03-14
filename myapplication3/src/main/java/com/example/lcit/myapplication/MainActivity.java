package com.example.lcit.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

/**
 * 展示处理恢复数据savedInstanceState的使用
 */
public class MainActivity extends AppCompatActivity {

    boolean mBoolean = false;
    private CheckBox cb1, cb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String cb2 = savedInstanceState.getString("cb");
            Log.d("print", "" + cb2 + "---" );
//            this.cb2.setTextColor(Color.parseColor(savedInstanceState.getString("cb2")));
        }
        setContentView(R.layout.activity_main);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);


    }

    public void btnclick(View view) {
        Log.d("print", "" + mBoolean);
        if (mBoolean) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //竖屏设置
            mBoolean = false;
        } else {
            mBoolean = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //横屏设置
        }
        Log.d("print", "" + mBoolean);
    }

    public void btnclick1(View view){
        startActivity(new Intent(this, OtherActivity.class));

}


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cb", "123456");
        outState.putString("cb2", "#ff0000");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("print", "" + savedInstanceState.getString("cb"));
        super.onRestoreInstanceState(savedInstanceState);
    }
}





//import android.app.Activity;
//import android.content.pm.ActivityInfo;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Display;
//import android.view.WindowManager;
//
//public class MainActivity extends Activity {
//
//    final String LOG = "ScreenOrientation" ;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //强行开启屏幕旋转效果
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//        if(savedInstanceState == null){
//            setContentView(R.layout.activity_main);
//        }
//        if(savedInstanceState != null){
//            //横屏
//            if( ScreenOrient(this)==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE  )
//                setContentView(R.layout.activity_main);
//            //竖屏
//            if( ScreenOrient(this)==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT  )
//                setContentView(R.layout.activity_other);
//            String temp = savedInstanceState.getString("data_key") ;
//            Log.d("print" , "重新创建了Activity，之前保存的内容是"+temp) ;
//        }
//    }
//
//    //判定当前的屏幕是竖屏还是横屏
//    public int ScreenOrient(Activity activity)
//    {
//        int orient = activity.getRequestedOrientation();
//        if(orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            WindowManager windowManager = activity.getWindowManager();
//            Display display = windowManager.getDefaultDisplay();
//            int screenWidth  = display.getWidth();
//            int screenHeight = display.getHeight();
//            orient = screenWidth < screenHeight ?  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//        }
//        return orient;
//    }
//
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        String tempData = "Something you want to save";
//        outState.putString("data_key", tempData);
//        Log.d("print", "onSaveInstanceState");
//    }
//}
