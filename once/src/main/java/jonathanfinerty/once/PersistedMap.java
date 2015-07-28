package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

class PersistedMap {

    private static final long KEY_NOT_FOUND_VALUE = -1;
    private final SharedPreferences preferences;
    private Map<String, Long> map = new HashMap<>();

    public PersistedMap(Context context, String mapName) {
        preferences = context.getSharedPreferences(PersistedMap.class.getSimpleName() + mapName, Context.MODE_PRIVATE);
        Map<String, ?> allPreferences = preferences.getAll();

        for (String key : allPreferences.keySet()) {
            long value = preferences.getLong(key, KEY_NOT_FOUND_VALUE);

            if (value != KEY_NOT_FOUND_VALUE) {
                map.put(key, value);
            }
        }
    }

    public Long get(String tag) {
        return map.get(tag);
    }

    public void put(String tag, long timeSeen) {
        map.put(tag, timeSeen);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong(tag, timeSeen);
        edit.apply();
    }

    public void remove(String tag) {
        map.remove(tag);
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(tag);
        edit.apply();
    }
}
