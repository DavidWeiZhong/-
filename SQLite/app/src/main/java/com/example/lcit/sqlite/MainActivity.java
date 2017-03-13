package com.example.lcit.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MySQLiteHlder mMySQLiteHlder;
    private SQLiteDatabase mWritableDatabase;
    private ContentValues mContentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void btnclick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                mMySQLiteHlder = new MySQLiteHlder(this, "MyDb.db", null, 1);
                //创建一个数据库，第二次就不会调用onCreate了
                mWritableDatabase = mMySQLiteHlder.getWritableDatabase();
                break;
            case R.id.btn2:
                mContentValues = new ContentValues();
                mContentValues.put("id", 12);
                mContentValues.put("author", "邱伟中");
                mContentValues.put("price", 100);
                mContentValues.put("pages", 10000);
                mContentValues.put("name", "我知道伤心不能代表什么");
                mWritableDatabase.insert("book", null,mContentValues);
                Toast.makeText(this, "插入数据成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn3:
                //更新数据
                ContentValues contentValues = new ContentValues();
                contentValues.put("author", "熊弯");
                mWritableDatabase.update("book", contentValues, "id = 1", null);
                Toast.makeText(this, "数据更新成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn4:
                //删除数据，改成查询数据吧，哈哈
                Cursor cursor = mWritableDatabase.rawQuery("select * from book where price like '%" + 1 + "%'", null);//模糊查询id有1的用户
                while(cursor.moveToNext()) {
                    int anInt = cursor.getInt(0);//获取第一列的值
                    String anInt1 = cursor.getString(1);
                    int anInt2 = cursor.getInt(2);//获取第三列的值
                    Log.d("print", "" + anInt + "----" + anInt1 + "---" + anInt2);
                }
                Toast.makeText(this, "查询成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn5:
                //数据库的升级
                mMySQLiteHlder = new MySQLiteHlder(this, "MyDb.db", null, 2);
                //创建一个数据库，第二次就不会调用onCreate了
                mWritableDatabase = mMySQLiteHlder.getWritableDatabase();
                break;

        }

    }
}
