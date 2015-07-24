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

        boolean seenThisInstall = Once.hasSeen(unseenTag, Once.SCOPE_APP_INSTALL);
        Assert.assertFalse(seenThisInstall);

        boolean seenThisAppVersion = Once.hasSeen(unseenTag, Once.SCOPE_APP_VERSION);
        Assert.assertFalse(seenThisAppVersion);

        boolean seenThisRun = Once.hasSeen(unseenTag, Once.SCOPE_APP_RUN);
        Assert.assertFalse(seenThisRun);
    }

    @Test
    public void markTagAsSeen() {
        Once.initialise(RuntimeEnvironment.application);

        String tag = "seen tag";

        Once.markSeen(tag);

        boolean seenThisInstall = Once.hasSeen(tag, Once.SCOPE_APP_INSTALL);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.hasSeen(tag, Once.SCOPE_APP_VERSION);
        Assert.assertTrue(seenThisAppVersion);

        boolean seenThisRun = Once.hasSeen(tag, Once.SCOPE_APP_RUN);
        Assert.assertTrue(seenThisRun);
    }

    @Test
    public void seenTagAfterAppRestart() {
        Once.initialise(RuntimeEnvironment.application);

        String tag = "seen tag";
        Once.markSeen(tag);

        // Is there a better way to simulate an app restart?
        Once.initialise(RuntimeEnvironment.application);

        boolean seenThisInstall = Once.hasSeen(tag, Once.SCOPE_APP_INSTALL);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.hasSeen(tag, Once.SCOPE_APP_VERSION);
        Assert.assertTrue(seenThisAppVersion);

        boolean seenThisRun = Once.hasSeen(tag, Once.SCOPE_APP_RUN);
        Assert.assertFalse(seenThisRun);
    }

}
