package jonathanfinerty.saw;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SawTests {

    @Test
    public void unseenTags() {
        Saw.initialise(RuntimeEnvironment.application);

        String unseenTag = "unseen tag";

        boolean seenThisInstall = Saw.hasSeen(unseenTag, Saw.SCOPE_APP_INSTALL);
        Assert.assertFalse(seenThisInstall);

        boolean seenThisAppVersion = Saw.hasSeen(unseenTag, Saw.SCOPE_APP_VERSION);
        Assert.assertFalse(seenThisAppVersion);

        boolean seenThisRun = Saw.hasSeen(unseenTag, Saw.SCOPE_APP_RUN);
        Assert.assertFalse(seenThisRun);
    }

    @Test
    public void markTagAsSeen() {
        Saw.initialise(RuntimeEnvironment.application);

        String tag = "seen tag";

        Saw.markSeen(tag);

        boolean seenThisInstall = Saw.hasSeen(tag, Saw.SCOPE_APP_INSTALL);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Saw.hasSeen(tag, Saw.SCOPE_APP_VERSION);
        Assert.assertTrue(seenThisAppVersion);

        boolean seenThisRun = Saw.hasSeen(tag, Saw.SCOPE_APP_RUN);
        Assert.assertTrue(seenThisRun);
    }

    @Test
    public void seenTagAfterAppRestart() {
        Saw.initialise(RuntimeEnvironment.application);

        String tag = "seen tag";
        Saw.markSeen(tag);

        // Is there a better way to simulate an app restart?
        Saw.initialise(RuntimeEnvironment.application);

        boolean seenThisInstall = Saw.hasSeen(tag, Saw.SCOPE_APP_INSTALL);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Saw.hasSeen(tag, Saw.SCOPE_APP_VERSION);
        Assert.assertTrue(seenThisAppVersion);

        boolean seenThisRun = Saw.hasSeen(tag, Saw.SCOPE_APP_RUN);
        Assert.assertFalse(seenThisRun);
    }

}
