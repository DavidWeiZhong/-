package com.example.lcit.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 展示activity的菜单
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        onCreateOptionsMenu()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.liangdu:
                Toast.makeText(this, "点击了亮度", 1).show();
//                startActivity(new Intent(this, LiangduActivity.class));
                break;
            case R.id.contact_us:
                Toast.makeText(this, "点击了关于我们", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
