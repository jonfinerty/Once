package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static jonathanfinerty.once.Amount.exactly;

@RunWith(RobolectricTestRunner.class)
@Config(sdk=28)
public class BackwardsCompatibilityTests {

    @Test
    public void backwardsCompatibilityWithPre1Versions() {
        String tag = "version 0.5 tag";

        Context applicationContext = ApplicationProvider.getApplicationContext();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(PersistedMap.class.getSimpleName() + "TagLastSeenMap", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(tag, 1234L).apply();

        Once.initialise(applicationContext);

        Once.markDone(tag);

        Assert.assertTrue(Once.beenDone(tag, exactly(2)));
    }

}
