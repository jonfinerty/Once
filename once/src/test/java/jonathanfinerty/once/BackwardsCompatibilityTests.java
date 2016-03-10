package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static jonathanfinerty.once.Amount.exactly;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BackwardsCompatibilityTests {

    @Test
    public void backwardsCompatibilityWithPre1Versions() {
        String tag = "version 0.5 tag";

        SharedPreferences sharedPreferences = RuntimeEnvironment.application.getSharedPreferences(PersistedMap.class.getSimpleName() + "TagLastSeenMap", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(tag, 1234L).apply();

        Once.initialise(RuntimeEnvironment.application);

        Once.markDone(tag);

        Assert.assertTrue(Once.beenDone(tag, exactly(2)));
    }

}
