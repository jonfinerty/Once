package jonathanfinerty.once;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OnceTests {

    @Test
    public void unseenTags() {
        Once.initialise(RuntimeEnvironment.application);

        String unseenTag = "unseen tag";

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, unseenTag);
        Assert.assertFalse(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, unseenTag);
        Assert.assertFalse(seenThisAppVersion);

        boolean seenThisRun = Once.beenDone(Once.THIS_APP_RUN, unseenTag);
        Assert.assertFalse(seenThisRun);
    }

    @Test
    public void markTagAsSeen() {
        Once.initialise(RuntimeEnvironment.application);

        String tag = "seen tag";

        Once.markDone(tag);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tag);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tag);
        Assert.assertTrue(seenThisAppVersion);

        boolean seenThisRun = Once.beenDone(Once.THIS_APP_RUN, tag);
        Assert.assertTrue(seenThisRun);
    }

    @Test
    public void seenTagAfterAppRestart() {
        Once.initialise(RuntimeEnvironment.application);

        String tag = "seen tag";
        Once.markDone(tag);

        // Is there a better way to simulate an app restart?
        Once.initialise(RuntimeEnvironment.application);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tag);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tag);
        Assert.assertTrue(seenThisAppVersion);

        boolean seenThisRun = Once.beenDone(Once.THIS_APP_RUN, tag);
        Assert.assertFalse(seenThisRun);
    }

}
