package jonathanfinerty.once;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OnceTests {

    @Before
    public void Setup() {
        Once.initialise(RuntimeEnvironment.application);
    }

    @Test
    public void unseenTags() {
        String unseenTag = "unseen tag";

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, unseenTag);
        Assert.assertFalse(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, unseenTag);
        Assert.assertFalse(seenThisAppVersion);

        boolean seenInTheLastDay = Once.beenDone(TimeUnit.DAYS, 1, unseenTag);
        Assert.assertFalse(seenInTheLastDay);
    }

    @Test
    public void markTagAsSeen() {
        String tag = "seen tag";
        Once.markDone(tag);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tag);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tag);
        Assert.assertTrue(seenThisAppVersion);

        boolean seenThisDay = Once.beenDone(TimeUnit.DAYS, 1, tag);
        Assert.assertTrue(seenThisDay);
    }

    @Test
    public void seenTagAfterAppRestart() {
        String tag = "seen tag";
        Once.markDone(tag);

        // Is there a better way to simulate an app restart?
        Once.initialise(RuntimeEnvironment.application);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tag);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tag);
        Assert.assertTrue(seenThisAppVersion);
    }

    @Test
    public void seenTagAfterAppUpdate() {
        // fake packageInfo.lastUpdateTime
    }

}
