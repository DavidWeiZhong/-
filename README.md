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
在自定义MyHander类中实例化一个外部类的WeakReference（弱引用）并且在Handler初始化时传入这个对象给你的Handler；将所有引用的外部类成员使用

WeakReference对象，英文静态内部类不会持有外部类的引用


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
    ![](https://github.com/DavidWeiZhong/-/blob/master/pic/QQ%E6%88%AA%E5%9B%BE20170313110534.png)
    ![](https://github.com/DavidWeiZhong/-/blob/master/pic/QQ截图20170313110543.png)
    
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
