package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class PersistedSet {

    private static final String STRING_SET_KEY = "PersistedSetValues";
    private final SharedPreferences preferences;

    private Set<String> set = new HashSet<>();

    public PersistedSet(Context context, String setName) {
        preferences = context.getSharedPreferences(PersistedSet.class.getSimpleName() + setName, Context.MODE_PRIVATE);

        set = preferences.getStringSet(STRING_SET_KEY, new HashSet<String>());
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
        edit.putStringSet(STRING_SET_KEY, set);
        edit.apply();
    }
}
