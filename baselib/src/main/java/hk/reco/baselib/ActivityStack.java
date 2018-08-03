package hk.reco.baselib;

import android.app.Activity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ActivityStack {
    private static ActivityStack instance;

    private final Map<String, Activity> stack = new LinkedHashMap<>();

    //SongListActivity页面，购物车唯一实例保存，manifest不再使用singleTask。
    private final Map<String, Activity> singleActivityMap = new LinkedHashMap<>();

    private Activity resumeActivity;

    private ActivityStack() {

    }

    public static ActivityStack getInstance() {
        if (instance == null) {
            synchronized (ActivityStack.class) {
                if (instance == null) {
                    instance = new ActivityStack();
                }
            }
        }
        return instance;
    }

    public static boolean isTopWithGivenClassName(String activityClassName) {
        Activity activity = ActivityStack.getInstance().getResumeActivity();
        if (activity != null && activity.getClass().getSimpleName().equals(activityClassName)) {
            return true;
        }
        return false;
    }

    /**
     * @return the resumeActivity
     */
    public Activity getResumeActivity() {
        return resumeActivity;
    }

    /**
     * @param resumeActivity the resumeActivity to set
     */
    public void setResumeActivity(Activity resumeActivity) {
        this.resumeActivity = resumeActivity;
    }

    /**
     * 方法名称： removeView 方法描述： 结束界面 输入参数： @param name 输入参数： @return 返回类型： boolean
     */
    public boolean removeActivity(String name) {
        if (null == name) {
            return false;
        }
        if (stack.containsKey(name)) {
            stack.get(name).finish();
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * 方法名称： popupView 方法描述： 界面出栈 输入参数：
     *
     * @param name 输入参数：
     * @return 返回类型： boolean
     */
    public boolean popupActivity(String name) {
        if (null == name) {
            return false;
        }
        if (stack.containsKey(name)) {
            stack.remove(name);
            return true;
        }
        return false;
    }

    public boolean popupActivity(Activity view) {
        if (null == view) {
            return false;
        }

        String name = view.getClass().getName();
        if (stack.containsKey(name)) {
            stack.remove(name);
            return true;
        }
        return false;
    }

    /**
     * 方法名称： pushView 方法描述： 界面入站 输入参数： @param view 返回类型： void
     */
    public void pushActivity(Activity view) {
        stack.remove(view.getClass().getName());
        stack.put(view.getClass().getName(), view);
    }

    /**
     * 方法名称： popupAllView 方法描述： 结束所有界面 输入参数： 返回类型： void
     */
    public void popupAllActivity() {
        Collection<Activity> views = stack.values();
        int size = views.size();
        Activity[] viewArray = new Activity[size];
        views.toArray(viewArray);
        for (int i = size - 1; i >= 0; --i) {
            viewArray[i].finish();
        }
        stack.clear();
    }

    /**
     * 方法名称： popupUnRelatedViews 方法描述： 结束所有不相关的界面 输入参数： @param name 返回类型： void
     */
    public void popupUnRelatedActivitys(String name) {
        Collection<Activity> views = stack.values();
        int size = views.size();
        Activity[] viewArray = new Activity[size];
        views.toArray(viewArray);
        for (int i = size - 1; i >= 0; --i) {
            if (!viewArray[i].getClass().getName().equals(name)) {
                viewArray[i].finish();
                stack.remove(viewArray[i].getClass().getName());
            }
        }
    }

    /**
     * 方法名称： popupAllActivityUpHome 方法描述： 将主界面之上的所有界面结束 输入参数： 返回类型： void
     */
    public void popupAllActivityUpHome() {
        Collection<Activity> views = stack.values();
        int size = views.size();
        Activity[] viewArray = new Activity[size];
        views.toArray(viewArray);
        for (int i = size - 1; i >= 0; --i) {
            if (!isCertain(viewArray[i].getClass().getName())) {
                viewArray[i].finish();
                stack.remove(viewArray[i].getClass().getName());
            }
        }
    }

    /**
     * 方法名称： remain 方法描述： if the activity name equals the certain activity 输入参数： @param
     * name The acvitity name 输入参数： @return 返回类型： boolean
     */
    private boolean isCertain(String name) {
        return true;
    }

    /**
     * 方法名称： getTopView 方法描述： 获得栈顶界面 输入参数：
     *
     * @return 返回类型： Activity
     */
    public Activity getTopActivity() {
        if (stack.isEmpty()) {
            return null;
        }
        String name = "";
        for (String s : stack.keySet()) {
            name = s;
        }
        return stack.get(name);
    }

    /**
     * 方法名称： containsView 方法描述： 判断界面栈是否包含界面 输入参数：
     *
     * @param name 输入参数：
     * @return 返回类型： boolean
     */
    public boolean containsActivity(String name) {
        return stack.containsKey(name);
    }

    /**
     * 方法名称： getView 方法描述： 获得指定界面 输入参数： @param name 输入参数： @return 返回类型： Activity
     */
    public Activity getActivity(String name) {
        return stack.get(name);
    }

    public String topActivityName() {
        if (stack.isEmpty()) {
            return null;
        }
        String name = "";
        for (String s : stack.keySet()) {
            name = s;
        }
        return name;
    }

    /**
     * 将activity加入map中
     */
    public void addSingleActivity(Activity activity) {
        singleActivityMap.remove(activity.getClass().getName());
        singleActivityMap.put(activity.getClass().getName(), activity);
    }


    /**
     * finish上一个该activity的实例，并移除map
     * 注意这个方法需要在activity oncreate页面之前就调用，避免页面创建和finish同步。
     */
    public void finishLastSingleActivity(String activityClassName) {
        if (singleActivityMap.containsKey(activityClassName)) {
            Activity activity = singleActivityMap.get(activityClassName);
            singleActivityMap.remove(activity);
            activity.finish();
        }
    }

    /**
     * 将activity移除map
     */
    public void removeSingleActivity(Activity activity) {
        if (singleActivityMap.containsValue(activity)) {
            singleActivityMap.remove(activity);
        }
    }
}