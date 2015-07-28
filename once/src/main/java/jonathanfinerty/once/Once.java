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

    /**
     * This method needs to be called before Once can be used.
     * Typically it will be called from your Application class's onCreate method.
     */
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

    /**
     * Checks if a tag has been marked done within a given scope.
     *
     * @param scope The scope in which to check whether the tag has been done, either
     *              {@code THIS_APP_INSTALL} or {@code THIS_APP_VERSION}.
     * @param tag   A string identifier unique to the operation.
     * @return {@code true} if the operation associated with {@code tag} has been marked done within
     * the given {@code scope}.
     */
    public static boolean beenDone(@Scope int scope, String tag) {

        Long tagLastSeenDate = tagLastSeenMap.get(tag);

        if (tagLastSeenDate == null) {
            return false;
        }

        if (scope == THIS_APP_INSTALL) {
            return true;
        }

        return tagLastSeenDate > lastAppUpdatedTime;
    }

    /**
     * Checks if a tag has been marked done within a given time span (e.g. the last 5 minutes)
     *
     * @param timeUnit The units of time to work in.
     * @param amount   The quantity of timeUnit.
     * @param tag      A string identifier unique to the operation.
     * @return {@code true} if the operation associated with {@code tag} has been marked done
     * within the last provide time span.
     */
    public static boolean beenDone(TimeUnit timeUnit, long amount, String tag) {
        long timeInMillis = timeUnit.toMillis(amount);
        return beenDone(timeInMillis, tag);
    }

    /**
     * Checks if a tag has been marked done within a the last X milliseconds
     *
     * @param timeSpanInMillis How many milliseconds ago to check if a tag has been marked done
     *                         since.
     * @param tag              A string identifier unique to the operation.
     * @return {@code true} if the operation associated with {@code tag} has been marked done
     * within the last X milliseconds.
     */
    public static boolean beenDone(long timeSpanInMillis, String tag) {
        Long timeTagSeen = tagLastSeenMap.get(tag);

        if (timeTagSeen == null) {
            return false;
        }

        long sinceSinceCheckTime = new Date().getTime() - timeSpanInMillis;

        return timeTagSeen > sinceSinceCheckTime;
    }

    /**
     * Marks a tag (associated with some operation) as done. The tag is marked done at the time
     * of calling this method
     *
     * @param tag A string identifier unique to the operation.
     */
    public static void markDone(String tag) {
        tagLastSeenMap.put(tag, new Date().getTime());
    }

    /**
     * Clears a tag as done. All checks with {@code beenDone()} with that tag will return true until
     * it is marked done again.
     *
     * @param tag A string identifier unique to the operation.
     */
    public static void clearDone(String tag) {
        tagLastSeenMap.remove(tag);
    }

    /**
     * Clears all tags as done. All checks with {@code beenDone()} with any tag will return true
     * until they are marked done again.
     */
    public static void clearAll() {
        tagLastSeenMap.clear();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({THIS_APP_INSTALL, THIS_APP_VERSION})
    public @interface Scope {
    }

}
