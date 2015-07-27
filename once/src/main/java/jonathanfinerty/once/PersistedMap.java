package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class PersistedMap {

    private Map<String, Long> map = new HashMap<>();
    private final SharedPreferences preferences;

    public PersistedMap(Context context, String mapName) {
        preferences = context.getSharedPreferences(PersistedMap.class.getSimpleName() + mapName, Context.MODE_PRIVATE);
        //noinspection unchecked
        map = (Map<String, Long>) preferences.getAll();
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
}
