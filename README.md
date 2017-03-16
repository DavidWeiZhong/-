# -
处理一些工作中遇到的问题，和一些技巧
#1，内存泄漏，内存泄漏很常见吧，可是往往会忽略了
内存泄漏出现的场景

1，hander的使用很常见吧，使用hander的时候经常会造成内存泄漏，所以在使用hander的时候，尽量新建一个静态的类然后继承hander

如果用Android studio开发，写handle发送消息，下面的写法会有黄色警告，因为可能会引发内存泄漏:

private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
};

为什么呢？非静态内部类会引用外部类对象（Activity），当它使用了 postDelayed 的时候，如果 Activity 已经 finish 了，而这个 handler 仍然引用着这个 Activity 就会致使内存泄漏，因为这个 handler 会在一段时间内继续被 mainLooper 持有，导致引用仍然存在，在这段时间内，如果内存不够使，可能OOM了。所以在这里要是使用了handle.postdelayed的时候就很可能造成你村泄漏
在自定义静态MyHander类中实例化一个外部类的WeakReference（弱引用）并且在Handler初始化时传入这个对象给你的Handler；将所有引用的外部类成员使用

WeakReference对象，因为静态内部类不会持有外部类的引用


2，资源释放问题

长期保存了 Context，io流，Course的引用，资源得不到释放造成内存溢出

3，对象内存过大

保存了多个耗用内存过大的对象（如Bitmap、XML 文件），造成内存超出限制。


4，static关键字的使用

static 是Java 中的一个关键字，当用它来修饰成员变量时，那么该变量就属于该类，而不是该类的实例。所以用static 修饰的变量，它的生命周期是很长的，如果用它来引用一些资源耗费过多的实例（Context 的情况最多），这时就要谨慎对待了。


5 线程导致内存溢出

public class MyActivity extends Activity {  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        new MyThread().start();  
    }  
    private class MyThread extends Thread{  
        @Override  
        public void run() {  
            super.run();  
            //do somthing while(true)  
        }  
    }  
}  

这段代码很平常也很简单，是我们经常使用的形式。我们思考一个问题：假设MyThread的run 函数是一个很费时的操作，当我们开启该线程后，将设备的横屏变为了竖屏，一般情况下当屏幕转换时会重新创建Activity，按照我们的想法，老的Activity 应该会被销毁才对，然而事实上并非如此。由于我们的线程是Activity 的内部类，所以MyThread 中保存了Activity 的一个引用，当MyThread 的run 函数没有结束时，MyThread 是不会被销毁的，因此它所引用的老的Activity 也不会被销毁，因此就出现了内存泄露的问题。

所以平时要注意

使用cursor 要关闭,
使用database 要关闭,
使用流要关闭,
使用Timer 必须calcel,
使用TimerTask 必须cancel,
注册内容观察者必须取消注册
内容观察者被添加到ContentQueryMap 后不用时delete 这个内容观察者
Handler 在activity 的onDestory 中removeMessageAndCallback(null)
io流记得关闭
使用了registReciver,一定要记得使用UnregistReciver
内存泄漏的原因有很多，比如activity的context引用，static引用，广播未取消注册，MVP设计时没有detachView，Rx没有取消subscribe订阅，动画处理等

所以平时要注意
使用Application的context
需要上下文的时候，如果不是非得activity对象，传入Application的context,因为Application的context的生命周期比Activity长，它是app全局的，相当于static的生命周期。static变量不要引用view实例


#内存泄漏查看工具：square的泄漏金丝雀，LeakCanary

用Eclipse开发自带的内存检测工具：Heap。

DDMS中的Heap工具用于大致分析是否存在“内存泄漏”，而MAT工具则用于分析“内存泄漏”发生在哪里，MAT工具中,File->Open Heap Dump，可以打开xxx.hprof文件分析。这个我现在不用。不多说。直接看下面介绍的LeakCanary。

1,在bulid.gradle
compile 'com.squareup.leakcanary:leakcanary-android:1.4-beta2'


//然后在你的Application的子类中install:
public class ExampleApplication extends Application{
  @Override public void onCreate() {
    super.onCreate();
    LeakCanary.install(this);
  }
}
现在来模拟一个内存泄漏的过程，先在MainActivity里面点击进入OtherActivity，OtherActivity里面有两个按钮，一个button1是发送handle.postDelayed的，一个button2是用来结束OtherActivity的，这样当点击了button1,延时8秒的按钮期间在点击button2结束当前activity就会发生内存溢出，因为handler持有一个OtherActivity的引用，但是OtherActivity已经结束了，就会发生内存溢出
下面是具体的代码
MainaActivity

public void btnclick(View view) {
        startActivity(new Intent(this, OtherActivity.class));
    }
    
    
  OtherActivity
  
  
   private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
//                Toast.makeText(OtherActivity.this, "测试内存溢出", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtherActivity.this.finish();
            }
        });
    }

    public void btnclick(View vie) {
        Message message = new Message();
        message.arg1 = 123;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(OtherActivity.this, "测试内存溢出", Toast.LENGTH_SHORT).show();
            }
        }, 8000);

    }
    
    
    然后你运行程序，先点击MainActivit里面的button1,跳转到OtherActivity，在点击OtherActivity里面的开始handler.postDelayed，然后过个1秒钟点击结束的按钮回退回MainActivity，这样屏幕上面就会弹出LeackCanary的Toask，同时在通知栏上面也会有相应的通知点击通知就可以看到具体是哪个类，泄漏类多少内存了，也可以在log中查看相应的内存泄漏信息，图片请参考
    ![image](https://github.com/DavidWeiZhong/-/blob/master/pic/QQ%E6%88%AA%E5%9B%BE20170313110534.png)
    ![image](https://github.com/DavidWeiZhong/-/blob/master/pic/QQ截图20170313110543.png)
    
# 传统的app检测跟新和自动更新


自动更新，

1、使用前端框架，如果是这种情况的话，整个应用都没有多少原生的东西，比较火的框架：React Native、ionic等；
2、纯原生，这种方式是通过图片下载，本地图片读取，动态生成StateListDrawable，大致这样一个流程实现不更新应用就更新底部按钮的图标；
3、JS交互：将整个底部按钮做成H5，然后使用webview加载，这也是今天的重点。（京东， 淘宝，下面的按钮键就是这样子，可以根据节日来变化，还有斗鱼的）

传统更新，

用户在登陆的时候，像服务器发送一个请求，服务器返回版本号，与自己app的版本号匹配，如果不一致，则弹出下载新版本的dialog提示用户下载（检测跟新），app中写好下载的io流代码，然后写好自动安装的代码，这里用户下载的都是在服务器上面已经加固了（360加固）的app，而不是在应用商店中的app


#3，app加固

1，可以用360加密app然后发布到应用市场中去（全自动的）

#4，应用发布到小米商店，Android市场
需要到他们的市场上面提交审核，如营业许可证，app不同分辨率的logo，等等，第一次提交的时候会复杂一点，后面就好了


#4，数据库SQLite版本的更新

讲一下大致的方法吧

在Andoird的SQLiteOpenHelper类中有一个onUpgrade方法中：
如果你的数据库需要升级，及某一个表中需要增加字段，而又不想丢失之前的数据

比如你先改user表
则1，将旧的user表改一下表名temp_user
  2,新建一个user表（新增字段或则删除了字段）
  3将将旧表的数据保存到新表中
  4，删除旧表（temp_user）
  
  怎么知道数据库需要升级呢，在这个方法中有参数oldVersion和当前版本数据库的version进行对比就知道了数据库需要升级了
  
  具体可以参考http://blog.csdn.net/crystalddd/article/details/37742021
  
  下午研究一下数据库升级吧
  
  首先是创建一个数据库
  
  自定义MySQLiteHlder extends SQLiteOpenHelper,然后有一个构造方法
  private Context mContext;
    public MySQLiteHlder(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }
    
 还有，两个重载方法 
 
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

   
   //执行操作数据库
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);//初次创建book表的时候会调用改方法，以后重复创建不会调用该方法
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据库升级要调用的方法
     * @param sqLiteDatabase
     * @param i，旧版本的数据库版本
     * @param i1，新版本数据库版本 */，当数据库版本改变的时候会调用改方法
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    
    
    
    
    接着在MainActivity中
    
     public void btnclick(View view) {
        switch (view.getId()) {
            case R.id.btn1:

                mMySQLiteHlder = new MySQLiteHlder(this, "MyDb.db", null, 1);//数据库的版本为1


                //创建一个数据库，第二次就不会调用onCreate了


                mWritableDatabase = mMySQLiteHlder.getWritableDatabase();


                break;
            case R.id.btn2:


            mContentValues = new ContentValues();//值得注意的是，每次操作数据的时候都要新建一个ContentValues
            

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
                ContentValues contentValues = new ContentValues();//值得注意的是，每次操作数据的时候都要新建一个ContentValues
                

                        contentValues.put("author", "邱邱邱");//把邱伟中改成邱邱邱


                mWritableDatabase.update("book", contentValues, "id = 1", null);



                        Toast.makeText(this, "数据更新成功", Toast.LENGTH_SHORT).show();


                break;
            case R.id.btn4:
                //查询数，哈哈
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

                mMySQLiteHlder = new MySQLiteHlder(this, "MyDb.db", null, 2);//数据库的版本变为了2


                //创建一个数据库，第二次就不会调用onCreate了


                mWritableDatabase = mMySQLiteHlder.getWritableDatabase();


                break;

        }
        
        
        现在来具体看看数据库升级的吧，背景，以前的数据库表名字为book,后来需求发展，要在book上面新增sex字段
        1，把旧版数据库表book改一下名字,改为older_book
        2,新建一个book表，增加了sex字段的
        3，吧older_book的数据复制给book表，以防书丢失
        4，删除older_book表
        
        
        具体在onUpgrade方法里面进行数据库的更新，其中app的重新安装不会把数据库数据格式化，只有把app卸载了才会
        
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

#Activity中菜单（menu）的使用

首先在res 目录下新建一个menu 文件夹，右击res 目录→New→Folder，输入文件夹名menu，点击Finish。接着在这个文件夹下再新建一个名叫main 的菜单文件，右击menu 文件夹→New→Android XML File,下面的代码是写option_menu_main.xml文件的

<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <group android:checkableBehavior="single">
        <item
            android:id="@+id/contact_us"
            android:orderInCategory="1"
            android:showAsAction=""
            android:title="联系我们"/>

        <item android:title="关于我们"/>
    </group>

    <group>
        <item android:title="系统设置"

              android:orderInCategory="2"
              android:icon="@mipmap/ic_launcher"
              android:id="@+id/system_setting"
            >
<!—二级菜单
            <menu>
                <item android:title="亮度设置" android:id="@+id/liangdu"/>
                <item android:enabled="false" android:title="主题设置" android:id="@+id/theme"/>
            </menu>
        </item>
    </group>


</menu>

然后在Activity中重写两个方法，onCreateOptionsMenu，和onOptionsItemSelected

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
，然后就不用做什么处理了，运行程序就可以了，运行效果图如图所示

![image](https://github.com/DavidWeiZhong/-/blob/master/pic/QQ截图20170313165301.png)
![image](https://github.com/DavidWeiZhong/-/blob/master/pic/QQ截图20170313165311.png)


值得注意的是，在fragment中如果要有菜单的话，除了重载上面的两个方法外，还必须在onCreat()中setHasOptionsMenu(true);把其打开
#定时器的使用，可以用于每隔一秒就发送一个消息，banner无线滚动就可以用这个做

//定义一个计时器,设置为延迟0ms后执行，每隔1s执行一次（这里如果设置为 timer.schedule(task,1000)表示执行一次）
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                Message message = new Message();
                message.arg1 = 123;
                mHandler.sendMessage(message);
            }
        }, 0, 1000);

#数据的恢复onSaveInstanceState  restart过程中涉及到的四种数据存储恢复的方法

1，onSaveInstanceState，用的还是比较常见的。
我们知道，当Configuration Change发生的时候（比如横竖屏切换等），会导致Activity重启，即先destroy，然后会restart，一般情况下restart的时间比较短，为了保证一致的用户体验，我们应该在Activity重启前将一些数据存储下来，然后在restart的时候重新根据这些数据更新UI。当然你可能想将这些数据写到物理文件或数据库中，但是这样有缺点，因为IO操作时耗时操作，会影响restart的过程，甚至导致ANR程序无响应，本文将介绍几种将数据缓存在内存中以便restart时进行恢复的方法，所以数据的恢复和保存尤为的重要
一提到数据的恢复可能就会想到onSaveInstanceState()方法了，其实这个方法最常用的是恢复view的状态，比如你选择了一个checkBox然后以横竖屏回来之后checkBox依然是你选择的那一个，还比如播放视频的室友进度条的长度等等，即存储UI持久化的一些信息，值得注意的是，如果用户显式地通过back键退出了程序，那么不会调用onSaveInstanceState方法。

而且我发现，在代码里面控制手机横竖屏切换，也并不会调用onSaveInstanceState,但是这样切换横竖屏了以后再onCreat()里面的onSaveInstanceState对象并不等于空，不知道为什么就是做不出来效果，但是如果不用代码的控制，而是手机的方向锁定没有打开来，手动横竖屏的话就会产生效果了。
等等，经过我的分析，原来是我搞错了，onSaveInsanceState()有两个重载方法，不要搞错了，用只有一个参数的，智障！！！
好了，言归正传
使用场景，视频播放进度条，小说读的页数，view的状态等等

案列

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
 另外bundle中的savedInstanceState不能存储一些大数据，如大的bitmip
 
 
 2使用静态变量存储数据
 
 我们知道Java中如果一个类的成员变量是static的，那么该static成员变量的生命周期就与该类的生命周期相同，具体来说：当Java虚拟机加载该类的时候，就会给该类的static成员变量分配内存空间；当Java虚拟机卸载该类的时候，该类的static成员变量的内存才会被回收。Android也具有该特性，假设我们的Activity中有一个static的成员变量，在Activity进行restart的过程中，Java虚拟机没有卸载掉该Activity，因为在后面的restart的过程中会用到，所以在restart过程中，该Activity的static的成员变量的内存没有被回收，这样我们就可以在restart之前往该Activity的static成员变量中写入值，在restart之后从Activity的static成员变量中读取值，这样就跨restart过程持有了数据。使用该特性也要注意，在非restart导致的destroy的时候，我们需要将Activity的static成员变量赋值为null，防止内存泄露。，注意啊，要防止内存溢出！！！！！static级别的都属于类级别的，哈哈
 
 
 3onPuse()
 
 
 翻译过来就是：无论出现怎样的情况，比如程序突然死亡了，能保证的就是onPause方法是一定会调用的，而onStop和onDestory方法并不一定，所以这个特性使得onPause是持久化相关数据的最后的可靠时机。当然onPause方法不能做大量的操作，这会影响下一个Activity入栈 
 
 临时数据使用onSaveInstanceState保存恢复，永久性数据使用onPause方法保存。
 
 
 4利用Fragment存储大量数据，大数据！！！！！！
 
 
 由于configuration change导致Activity销毁的时候，Activity中标记为保留的Fragment不会销毁，所以可以利用该特性实现存取数据，具体方法如下：

扩展Fragment类，并定义好相应字段存取数据，对外暴露出设置数据和获取数据的方法，比如setData和getData

在Fragment的onCreate方法中调用方法setRetainInstance(true)，标记该Fragment为保留的

将该Fragment加入到Activity中

在Activity的onDestroy方法中将activity的中需要保存的数据调用Fragment中上述定义的setData方法，将其保存在Fragment中

在Activity销毁重新生成执行onCreate的时候，重新从Fragmet中调用getData获取之前保存的数据

最后需要注意内存泄露的发生，练习一个例子吧！，其本质还是通过onSaveInstanceState来保存fragment，然后在重fragment中取出数据
这里我就不模拟保存bitmap了，就先模拟保存String吧bitmap其实也是一样的！！。调整手机位置横竖屏以后就可以看出效果


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
当旋转屏幕的时候输出    保存的bitmap--我就是要保存的bitmap

#重温ListView，RecycleView瀑布流形式

值得注意的是RecycleView与ListView的用法基本相似，只是RecycleView多了一个mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//普通形式
不然显示不出来
listview；涉及到了ListView的优化了。

1，convertView的复用
2，用一个最好是静态的ViewHolder用来缓存控件
/**
 * listview的使用，今天回忆了一下listview的使用
 */
public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private List<String> date = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        for (int i = 0; i < 100; i++) {
            date.add(i + "");
        }
        MyAdapter myAdapter = new MyAdapter(this, date);
        mListView.setAdapter(myAdapter);

    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> date;
        public MyAdapter(Context context, List<String> date) {
            this.mContext = context;
            this.date = date;
        }
        @Override
        public int getCount() {
            return date.size();
        }

        @Override
        public Object getItem(int i) {
            return date.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder myViewHolder = null;
            if (view == null) {
                view = View.inflate(mContext, R.layout.item_list, null);
                myViewHolder = new MyViewHolder();
                myViewHolder.mTextView = (TextView) view.findViewById(R.id.tv);
                view.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) view.getTag();
            }
            if (i == date.size() - 1) {
                myViewHolder.mTextView.setText("+");
            } else {
                myViewHolder.mTextView.setText(date.get(i));
            }
            myViewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(mContext, RecycleView.class));
                }
            });
            return view;
        }

        class MyViewHolder {
            TextView mTextView;
        }
    }
}


2，讲一下RecycleView的瀑布流形式吧，其实就是在onBindViewHolder方法中随机给item一个高度，也就产生了瀑布流形式了，另外还必须设置为

 mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));//瀑布流形式
                
   不能设置为mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));//瀑布流形式，不然没有效果
   
   
   
   
   public class RecycleView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> date = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        for (int i = 0; i < 1000; i++) {
            date.add("" + i);
        }
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//普通形式
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));//瀑布流形式
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));//瀑布流形式
        MyAdapter myAdapter = new MyAdapter(date);
        mRecyclerView.setAdapter(myAdapter);

    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<String> date;
        private List<Integer> height;
        public MyAdapter(List<String> date){
            this.date = new ArrayList<>();
            this.height = new ArrayList<>();
            for (int i = 0; i < date.size(); i ++) {
                height.add((int) (100 + Math.random() * 300));
            }
            this.date = date;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(RecycleView.this).inflate(R.layout.item_list,
                    parent, false));
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(date.get(position) + "RecycleView");
            ViewGroup.LayoutParams p = holder.tv.getLayoutParams();
//            LayoutParams p =  holder.tv.getLayoutParams();
            p.height = this.height.get(position);
            holder.tv.setLayoutParams(p);
//            holder.tv.setHeight((int)(Math.random() * 100));
//            holder.tv.setHeight(1010);
        }

        @Override
        public int getItemCount() {
            return date.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}


效果图如下![image](https://github.com/DavidWeiZhong/-/blob/master/pic/QQ截图20170315161626.png)

其中要注意的是白色的分割线，其实就是在item.xml文件的跟布局中设置了一个margin就会产生分割显得效果了。
                
             
                
                
                
       
