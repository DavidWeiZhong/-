package com.example.lcit.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by ${qiuweizhong} on 2017/3/10.
 */
public class MySQLiteHlder extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table book ("

            + "id integer primary key autoincrement, "

            + "author text, "

            + "price real, "

            + "pages integer, "

            + "name text)";

    public static final String CREATE_BOOK_2 = "create table book ("

            + "id integer primary key autoincrement, "

            + "author text, "

            + "price real, "

            + "pages integer, "

            + "name text,"

            +"sex text)";

    private Context mContext;
    public MySQLiteHlder(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    public MySQLiteHlder(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    //执行操作数据库
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据库升级要调用的方法
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if (i != i1) {
            Toast.makeText(mContext, "数据库要升级了", Toast.LENGTH_LONG).show();
            //修改表名
            sqLiteDatabase.execSQL("ALTER TABLE book RENAME TO older_book");
            //创建一个临时表名（增加了新字段的），和旧表名字一样
            sqLiteDatabase.execSQL(CREATE_BOOK_2);

//            //把东西复制
//
            sqLiteDatabase.execSQL(" INSERT INTO book SELECT *, '1' FROM older_book");//1代表给sex赋予默认值
            //删除旧表
            sqLiteDatabase.execSQL("DROP TABLE older_book");
            Toast.makeText(mContext, "数据库升级成功", Toast.LENGTH_SHORT).show();
        }

    }
}
