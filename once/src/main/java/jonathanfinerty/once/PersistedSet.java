package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PersistedSet {

    private static final String STRING_SET_KEY = "PersistedSetValues";
    private final SharedPreferences preferences;

    private Set<String> set = new HashSet<>();

    public PersistedSet(Context context, String setName) {
        preferences = context.getSharedPreferences(PersistedSet.class.getSimpleName() + setName, Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            set = preferences.getStringSet(STRING_SET_KEY, new HashSet<String>());
        }else{
            String setString = preferences.getString(STRING_SET_KEY, null);
            if (!TextUtils.isEmpty(setString)){
                set = new HashSet<>(Arrays.asList(setString.split(",")));
            }else{
                set = new HashSet<>();
            }
        }


    }

    public void put(String tag) {
        set.add(tag);
        updatePreferences();
    }

    public boolean contains(String tag) {
        return set.contains(tag);
    }

    public void remove(String tag) {
        set.remove(tag);
        updatePreferences();
    }

    public void clear() {
        set.clear();
        updatePreferences();
    }

    private void updatePreferences() {
        SharedPreferences.Editor edit = preferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            edit.putStringSet(STRING_SET_KEY, set);
        }else{
            edit.putString(STRING_SET_KEY, StringSetToString(set, ","));
        }
        edit.apply();
    }

    public static String StringSetToString(Set<String> set, String delimiter) {
        StringBuilder sb = new StringBuilder();
        String loopDelimiter = "";

        for (String s : set) {
            sb.append(loopDelimiter);
            sb.append(s);

            loopDelimiter = delimiter;
        }

        return sb.toString();
    }
}
