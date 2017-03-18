package activity.task;

import android.app.Activity;

import java.util.LinkedList;

/**
 * 自定义的activity栈
 */
public class ActivityTask {

    private static LinkedList<Activity> activityStack;

    // 单例
    private static ActivityTask instance;

    private ActivityTask() {
    }

    public static ActivityTask getInstance() {
        if (instance == null) {
            instance = new ActivityTask();
        }
        return instance;
    }

    /*
     * 压入栈顶
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new LinkedList<Activity>();
        }
        activityStack.push(activity);
    }

    /*
     * 移除栈顶activity
     */
    public void popActivity() {
        Activity activity = activityStack.pop();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /*
     * 移除指定的activity
     */
    public void popOneActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /*
     * 获取当前栈顶activity
     */
    public Activity currentActivity() {
        Activity activity = activityStack.peek();
        return activity;
    }

    /*
     * 移除clazz之后的所有activity
     * 例如开启了A、B、C、D、E五个activity，popAllActivityExceptOne(C.class);
     * 则会移除D和E
     */
    public void popAllActivityExceptOne(Class clazz) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null || activity.getClass().equals(clazz)) {
                break;
            }
            popOneActivity(activity);
        }
    }

    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popOneActivity(activity);
        }
    }

    /*
     * 退出app,清除所有activity
     */
    public void exit() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popOneActivity(activity);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
