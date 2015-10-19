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
    private static PersistedSet toDoSet;

    private Once() {
    }

    /**
     * This method needs to be called before Once can be used.
     * Typically it will be called from your Application class's onCreate method.
     *
     * @param context Application context
     */
    public static void initialise(Context context) {
        if (tagLastSeenMap == null) {
            tagLastSeenMap = new PersistedMap(context, "TagLastSeenMap");
        }

        if (toDoSet == null) {
            toDoSet = new PersistedSet(context, "ToDoSet");
        }

        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            lastAppUpdatedTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException ignored) {

        }
    }

    /**
     * Mark a tag as 'to do' within a given scope, if it has already marked to do or been done
     * within that scope then it will not be marked.
     *
     * @param scope The scope to not repeat the to do task in
     * @param tag   A string identifier unique to the operation.
     * */
    public static void toDo(@Scope int scope, String tag) {

        Long tagLastSeenDate = tagLastSeenMap.get(tag);

        if (tagLastSeenDate == null) {
            toDoSet.put(tag);
            return;
        }

        if (scope == THIS_APP_VERSION && tagLastSeenDate <= lastAppUpdatedTime) {
            toDoSet.put(tag);
        }
    }

    /**
     * Mark a tag as 'to do' regardless of whether or not its ever been marked done before
     * @param tag   A string identifier unique to the operation.
     * */
    public static void toDo(String tag) {
        toDoSet.put(tag);
    }

    /**
     * Checks if a tag is currently marked as 'to do'.
     *
     * @param tag   A string identifier unique to the operation.
     * @return {@code true} if the operation associated with {@code tag} has been marked 'to do' and has not been passed to {@code markDone()} since.
     * */
    public static boolean needToDo(String tag) {
        return toDoSet.contains(tag);
    }

    /**
     * Checks if a tag has been marked done, ever.
     *
     * Equivalent of calling {@code beenDone(int scope, String tag)} with scope of {@code THIS_APP_INSTALL}.
     *
     * @param tag   A string identifier unique to the operation.
     * @return {@code true} if the operation associated with {@code tag} has been marked done within
     * the given {@code scope}.
     */
    public static boolean beenDone(String tag) {
        return beenDone(THIS_APP_INSTALL, tag);
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
     * Checks if a tag has been marked done within a given time span. (e.g. the last 5 minutes)
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
     * Checks if a tag has been marked done within a the last {@code timeSpanInMillis} milliseconds.
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
     * Marks a tag (associated with some operation) as done. The {@code tag} is marked done at the time
     * of calling this method.
     *
     * @param tag A string identifier unique to the operation.
     */
    public static void markDone(String tag) {
        tagLastSeenMap.put(tag, new Date().getTime());
        toDoSet.remove(tag);
    }

    /**
     * Clears a tag as done. All checks with {@code beenDone()} with that tag will return {@code false} until
     * it is marked done again.
     *
     * @param tag A string identifier unique to the operation.
     */
    public static void clearDone(String tag) {
        tagLastSeenMap.remove(tag);
    }

    /**
     * Clears a tag as 'to do'. All checks with {@code needToDo()} with that tag will return {@code false} until
     * it is marked 'to do' again.
     *
     * @param tag A string identifier unique to the operation.
     */
    public static void clearToDo(String tag) {
        toDoSet.remove(tag);
    }

    /**
     * Clears all tags as done. All checks with {@code beenDone()} with any tag will return {@code false}
     * until they are marked done again.
     */
    public static void clearAll() {
        tagLastSeenMap.clear();
    }

    /**
     * Clears all tags as 'to do'. All checks with {@code needToDo()} with any tag will return {@code false}
     * until they are marked 'to do' again.
     */
    public static void clearAllToDos() {
        toDoSet.clear();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({THIS_APP_INSTALL, THIS_APP_VERSION})
    public @interface Scope {
    }
}
