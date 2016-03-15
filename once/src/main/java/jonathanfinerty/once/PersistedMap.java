package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

class PersistedMap implements AsyncSharedPreferenceLoader.Listener {

    private static final String DELIMITER = ",";

    private final CountDownLatch loaded = new CountDownLatch(1);

    private SharedPreferences preferences;
    private final Map<String, List<Long>> map = new ConcurrentHashMap<>();

    public PersistedMap(Context context, String mapName) {
        AsyncSharedPreferenceLoader preferenceLoader = new AsyncSharedPreferenceLoader(context, this);
        preferenceLoader.load(PersistedMap.class.getSimpleName() + mapName);
    }

    @Override
    public void onLoad(SharedPreferences sharedPreferences) {
        preferences = sharedPreferences;
        Map<String, ?> allPreferences = preferences.getAll();

        for (String key : allPreferences.keySet()) {

            List<Long> values;
            try {
                values = stringToList(preferences.getString(key, null));
            } catch (ClassCastException exception) {
                values = loadFromLegacyStorageFormat(key);
            }

            map.put(key, values);
        }

        loaded.countDown();
    }

    private void waitForLoad() {
        try {
            loaded.await();
        } catch (InterruptedException ignored) {

        }
    }

    private List<Long> loadFromLegacyStorageFormat(String key) {
        long value = preferences.getLong(key, -1);
        List<Long> values = new ArrayList<>(1);
        values.add(value);

        preferences.edit().putString(key, listToString(values)).apply();

        return values;
    }

    @NonNull
    public List<Long> get(String tag) {
        waitForLoad();

        List<Long> longs = map.get(tag);
        if (longs == null) {
            return Collections.emptyList();
        }
        return longs;
    }

    public void put(String tag, long timeSeen) {
        waitForLoad();

        List<Long> lastSeenTimeStamps = map.get(tag);
        if (lastSeenTimeStamps == null) {
            lastSeenTimeStamps = new ArrayList<>(1);
        }
        lastSeenTimeStamps.add(timeSeen);

        map.put(tag, lastSeenTimeStamps);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(tag, listToString(lastSeenTimeStamps));
        edit.apply();
    }

    public void remove(String tag) {
        waitForLoad();

        map.remove(tag);
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(tag);
        edit.apply();
    }

    public void clear() {
        waitForLoad();

        map.clear();
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }

    private String listToString(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String loopDelimiter = "";

        for (Long l : list) {
            stringBuilder.append(loopDelimiter);
            stringBuilder.append(l);

            loopDelimiter = DELIMITER;
        }

        return stringBuilder.toString();
    }

    private List<Long> stringToList(String stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return Collections.emptyList();
        }

        String[] strings = stringList.split(DELIMITER);
        List<Long> list = new ArrayList<>(strings.length);

        for (String stringLong : strings) {
            list.add(Long.parseLong(stringLong));
        }

        return list;
    }


}
