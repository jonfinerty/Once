package jonathanfinerty.saw;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

class PersistedSeenSet {

    private final SharedPreferences sharedPreferences;
    private final String setName;

    public PersistedSeenSet(SharedPreferences sharedPreferences, String setName) {
        this.sharedPreferences = sharedPreferences;
        this.setName = setName;
    }

    public boolean contains(String seenTag) {
        Set<String> seenTags = sharedPreferences.getStringSet(setName, null);
        return (seenTags != null && seenTags.contains(seenTag));
    }

    public void add(String seenTag) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> seenTags = sharedPreferences.getStringSet(setName, new HashSet<String>());
        Set<String> updatedSeenTags = new HashSet<>(seenTags);
        updatedSeenTags.add(seenTag);
        editor.putStringSet(setName, updatedSeenTags);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(setName);
        editor.apply();
    }

}
