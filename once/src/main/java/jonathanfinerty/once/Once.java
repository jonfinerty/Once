package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;

public class Once {

    public static final int SCOPE_APP_INSTALL = 0;
    public static final int SCOPE_APP_VERSION = 1;
    public static final int SCOPE_APP_RUN = 2;

    private static final int NO_PREVIOUS_APP_VERSION = -1;
    private static final String PREVIOUS_APP_VERSION_KEY = "previousAppVersion";
    private static PersistedSeenSet appInstallSeen;
    private static PersistedSeenSet appVersionSeen;
    private static Set<String> appRunSeen;
    private static SharedPreferences sharedPreferences;

    private Once() {
    }

    public static void initialise(Context context) {
        sharedPreferences = context.getSharedPreferences("oncePreferences", Context.MODE_PRIVATE);
        appInstallSeen = new PersistedSeenSet(sharedPreferences, "appInstall");
        appVersionSeen = new PersistedSeenSet(sharedPreferences, "appVersion");
        appRunSeen = new HashSet<>();

        if (isNewAppVersion()) {
            appVersionSeen.clear();
            saveLatestAppVersion();
        }
    }

    private static void saveLatestAppVersion() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREVIOUS_APP_VERSION_KEY, BuildConfig.VERSION_CODE);
        editor.apply();
    }

    private static boolean isNewAppVersion() {
        int previousAppVersion = sharedPreferences.getInt("previousAppVersion", NO_PREVIOUS_APP_VERSION);

        return previousAppVersion != NO_PREVIOUS_APP_VERSION && previousAppVersion != BuildConfig.VERSION_CODE;
    }

    public static boolean hasSeen(String tag, @Scope int seenScope) {

        if (appRunSeen.contains(tag)) {
            return true;
        }

        if (seenScope == SCOPE_APP_RUN) {
            return false;
        }

        if (appVersionSeen.contains(tag)) {
            return true;
        }

        if (seenScope == SCOPE_APP_VERSION) {
            return false;
        }

        if (appInstallSeen.contains(tag)) {
            return true;
        }

        return false;
    }

    public static void markSeen(String tag) {
        appInstallSeen.add(tag);
        appVersionSeen.add(tag);
        appRunSeen.add(tag);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SCOPE_APP_INSTALL, SCOPE_APP_VERSION, SCOPE_APP_RUN})
    public @interface Scope {
    }

}
