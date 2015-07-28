package jonathanfinerty.once;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Once {

    public static final int THIS_APP_INSTALL = 0;
    public static final int THIS_APP_VERSION = 1;

    private static long lastAppUpdatedTime = -1;

    private static PersistedMap tagLastSeenMap;

    private Once() {
    }

    public static void initialise(Context context) {
        if (tagLastSeenMap == null) {
            tagLastSeenMap = new PersistedMap(context, "TagLastSeenMap");
        }

        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            lastAppUpdatedTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException ignored) {

        }
    }

    public static boolean beenDone(@Scope int doneScope, String tag) {

        Long tagLastSeenDate = tagLastSeenMap.get(tag);

        if (tagLastSeenDate == null) {
            return false;
        }

        if (doneScope == THIS_APP_INSTALL) {
            return true;
        }

        return tagLastSeenDate > lastAppUpdatedTime;
    }

    public static boolean beenDone(TimeUnit timeUnit, long time, String tag) {
        long timeInMillis = timeUnit.toMillis(time);
        return beenDone(timeInMillis, tag);
    }

    public static boolean beenDone(long timeSpanInMillis, String tag) {
        Long timeTagSeen = tagLastSeenMap.get(tag);

        if (timeTagSeen == null) {
            return false;
        }

        long sinceSinceCheckTime = new Date().getTime() - timeSpanInMillis;

        return timeTagSeen > sinceSinceCheckTime;
    }

    public static void markDone(String tag) {
        tagLastSeenMap.put(tag, new Date().getTime());
    }

    public static void clearDone(String tag) {
        tagLastSeenMap.remove(tag);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({THIS_APP_INSTALL, THIS_APP_VERSION})
    public @interface Scope {
    }

}
