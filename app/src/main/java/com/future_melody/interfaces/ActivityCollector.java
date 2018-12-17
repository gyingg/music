package com.future_melody.interfaces;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author WZL
 * Date：2018/6/28 34
 * Notes:
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
