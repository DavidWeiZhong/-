# -
处理一些工作中遇到的问题，和一些技巧
#1，内存泄漏，内存泄漏很常见吧，可是往往会忽略了
内存泄漏出现的场景

1，hander的使用很常见吧，使用hander的时候经常会造成内存泄漏，所以在使用hander的时候，尽量新建一个静态的类然后继承hander

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
