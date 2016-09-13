package jonathanfinerty.once;

import android.content.pm.PackageInfo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.res.builder.RobolectricPackageManager;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static jonathanfinerty.once.Amount.exactly;

@SuppressWarnings("ConstantConditions")
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OnceTests {

    private static final String tagUnderTest = "testTag";

    @Before
    public void Setup() {
        Once.initialise(RuntimeEnvironment.application);
    }

    @After
    public void CleanUp() {
        Once.clearAll();
    }

    @Test
    public void unseenTags() {
        Once.clearAll();

        boolean seenThisSession = Once.beenDone(Once.THIS_APP_SESSION, tagUnderTest);
        Assert.assertFalse(seenThisSession);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tagUnderTest);
        Assert.assertFalse(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tagUnderTest);
        Assert.assertFalse(seenThisAppVersion);

        boolean seenInTheLastDay = Once.beenDone(TimeUnit.DAYS, 1, tagUnderTest);
        Assert.assertFalse(seenInTheLastDay);
    }

    @Test
    public void seenTagImmediately() {
        Once.markDone(tagUnderTest);

        boolean seenThisSession = Once.beenDone(Once.THIS_APP_SESSION, tagUnderTest);
        Assert.assertTrue(seenThisSession);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tagUnderTest);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tagUnderTest);
        Assert.assertTrue(seenThisAppVersion);

        boolean seenThisMinute = Once.beenDone(TimeUnit.MINUTES, 1, tagUnderTest);
        Assert.assertTrue(seenThisMinute);
    }

    @Test
    public void removeFromDone() {
        Once.markDone(tagUnderTest);

        Once.clearDone(tagUnderTest);

        boolean seenThisSession = Once.beenDone(Once.THIS_APP_SESSION, tagUnderTest);
        Assert.assertFalse(seenThisSession);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tagUnderTest);
        Assert.assertFalse(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tagUnderTest);
        Assert.assertFalse(seenThisAppVersion);

        boolean seenInTheLastDay = Once.beenDone(TimeUnit.DAYS, 1, tagUnderTest);
        Assert.assertFalse(seenInTheLastDay);
    }

    @Test
    public void seenTagAfterAppUpdate() {
        Once.markDone(tagUnderTest);

        simulateAppUpdate();

        boolean seenThisSession = Once.beenDone(Once.THIS_APP_SESSION, tagUnderTest);
        Assert.assertTrue(seenThisSession);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tagUnderTest);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tagUnderTest);
        Assert.assertFalse(seenThisAppVersion);

        boolean seenThisMinute = Once.beenDone(TimeUnit.MINUTES, 1, tagUnderTest);
        Assert.assertTrue(seenThisMinute);
    }

    @Test
    public void seenTagAfterSecond() throws InterruptedException {
        Once.markDone(tagUnderTest);

        boolean seenThisSession = Once.beenDone(Once.THIS_APP_SESSION, tagUnderTest);
        Assert.assertTrue(seenThisSession);

        boolean seenThisInstall = Once.beenDone(Once.THIS_APP_INSTALL, tagUnderTest);
        Assert.assertTrue(seenThisInstall);

        boolean seenThisAppVersion = Once.beenDone(Once.THIS_APP_VERSION, tagUnderTest);
        Assert.assertTrue(seenThisAppVersion);

        Thread.sleep(TimeUnit.SECONDS.toMillis(1) + 1);
        boolean seenThisSecond = Once.beenDone(TimeUnit.SECONDS, 1, tagUnderTest);
        Assert.assertFalse(seenThisSecond);

        long secondInMillis = 1000;
        boolean seenThisSecondInMillis = Once.beenDone(secondInMillis, tagUnderTest);
        Assert.assertFalse(seenThisSecondInMillis);
    }

    @Test
    public void clearAll() {
        String tag1 = "tag1";
        String tag2 = "tag2";
        Once.markDone(tag1);
        Once.markDone(tag2);

        Once.clearAll();

        Assert.assertFalse(Once.beenDone(Once.THIS_APP_SESSION, tag1));
        Assert.assertFalse(Once.beenDone(Once.THIS_APP_INSTALL, tag1));
        Assert.assertFalse(Once.beenDone(Once.THIS_APP_VERSION, tag1));
        Assert.assertFalse(Once.beenDone(1000L, tag1));

        Assert.assertFalse(Once.beenDone(Once.THIS_APP_SESSION, tag2));
        Assert.assertFalse(Once.beenDone(Once.THIS_APP_INSTALL, tag2));
        Assert.assertFalse(Once.beenDone(Once.THIS_APP_VERSION, tag2));
        Assert.assertFalse(Once.beenDone(1000L, tag2));
    }

    @Test
    public void todo() {
        String task1 = "todo task";
        Assert.assertFalse(Once.needToDo(task1));
        Assert.assertFalse(Once.beenDone(task1));

        Once.toDo(task1);
        Assert.assertTrue(Once.needToDo(task1));
        Assert.assertFalse(Once.beenDone(task1));

        Once.markDone(task1);
        Assert.assertFalse(Once.needToDo(task1));
        Assert.assertTrue(Once.beenDone(task1));
        Assert.assertTrue(Once.beenDone(TimeUnit.SECONDS, 1, task1));
    }

    @Test
    public void repeatingToDos() {
        String tag = "repeating to do task";
        Once.toDo(tag);

        Assert.assertTrue(Once.needToDo(tag));
        Once.markDone(tag);

        Once.toDo(tag);
        Assert.assertTrue(Once.needToDo(tag));
    }

    @Test
    public void todoThisSession() {
        String tag = "to do this session task";

        Once.toDo(Once.THIS_APP_SESSION, tag);
        Assert.assertTrue(Once.needToDo(tag));
        Assert.assertFalse(Once.beenDone(tag));

        Once.markDone(tag);
        Assert.assertFalse(Once.needToDo(tag));
        Assert.assertTrue(Once.beenDone(tag));

        Once.toDo(Once.THIS_APP_SESSION, tag);
        Assert.assertFalse(Once.needToDo(tag));

        Once.toDo(tag);
        Assert.assertTrue(Once.needToDo(tag));
    }

    @Test
    public void todoThisInstall() {
        String tag = "to do this install task";

        Once.toDo(Once.THIS_APP_INSTALL, tag);
        Assert.assertTrue(Once.needToDo(tag));
        Assert.assertFalse(Once.beenDone(tag));

        Once.markDone(tag);
        Assert.assertFalse(Once.needToDo(tag));
        Assert.assertTrue(Once.beenDone(tag));

        Once.toDo(Once.THIS_APP_INSTALL, tag);
        Assert.assertFalse(Once.needToDo(tag));

        Once.toDo(tag);
        Assert.assertTrue(Once.needToDo(tag));
    }

    @Test
    public void todoThisAppVersion() {
        String tag = "todo this app version task";

        Once.toDo(Once.THIS_APP_VERSION, tag);
        Assert.assertTrue(Once.needToDo(tag));
        Assert.assertFalse(Once.beenDone(tag));

        Once.markDone(tag);
        Assert.assertFalse(Once.needToDo(tag));
        Assert.assertTrue(Once.beenDone(tag));

        Once.toDo(Once.THIS_APP_VERSION, tag);
        Assert.assertFalse(Once.needToDo(tag));

        simulateAppUpdate();

        Once.toDo(Once.THIS_APP_VERSION, tag);
        Assert.assertTrue(Once.needToDo(tag));

        Once.toDo(tag);
        Assert.assertTrue(Once.needToDo(tag));
    }

    @Test
    public void emptyTag() {
        String emptyTag = "";
        Assert.assertFalse(Once.beenDone(emptyTag));
        Once.markDone(emptyTag);
        Assert.assertTrue(Once.beenDone(emptyTag));
    }

    @Test
    public void beenDoneMultipleTimes() {
        String testTag = "action done several times";
        Once.markDone(testTag);
        Once.markDone(testTag);

        Assert.assertFalse(Once.beenDone(testTag,  exactly(3)));

        Once.markDone(testTag);

        Assert.assertTrue(Once.beenDone(testTag,  exactly(3)));
    }

    @Test
    public void beenDoneMultipleTimesAcrossScopes() throws InterruptedException {
        String testTag = "action done several times in different scopes";
        Once.markDone(testTag);

        simulateAppUpdate();
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        Once.markDone(testTag);

        Assert.assertTrue(Once.beenDone(Once.THIS_APP_INSTALL, testTag, exactly(2)));
        Assert.assertFalse(Once.beenDone(Once.THIS_APP_VERSION, testTag, exactly(2)));

        Once.markDone(testTag);

        Assert.assertTrue(Once.beenDone(Once.THIS_APP_INSTALL, testTag, exactly(3)));
        Assert.assertTrue(Once.beenDone(Once.THIS_APP_VERSION, testTag, exactly(2)));
    }

    @Test
    public void beenDoneDifferentTimeChecks() {
        String testTag = "test tag";
        Once.markDone(testTag);
        Once.markDone(testTag);
        Once.markDone(testTag);

        Assert.assertTrue(Once.beenDone(testTag, Amount.moreThan(-1)));
        Assert.assertTrue(Once.beenDone(testTag, Amount.moreThan(2)));
        Assert.assertFalse(Once.beenDone(testTag, Amount.moreThan(3)));

        Assert.assertTrue(Once.beenDone(testTag, Amount.lessThan(10)));
        Assert.assertTrue(Once.beenDone(testTag, Amount.lessThan(4)));
        Assert.assertFalse(Once.beenDone(testTag, Amount.lessThan(3)));
    }

    @Test
    public void beenDoneMultipleTimesWithTimeStamps() throws InterruptedException {
        Once.markDone(tagUnderTest);
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        Once.markDone(tagUnderTest);

        Assert.assertTrue(Once.beenDone(TimeUnit.SECONDS, 3, tagUnderTest, exactly(2)));
        Assert.assertTrue(Once.beenDone(TimeUnit.SECONDS, 1, tagUnderTest, exactly(1)));
    }

    @Test
    public void lastDoneWhenNeverDone() {
        Date lastDoneDate = Once.lastDone(tagUnderTest);
        Assert.assertNull(lastDoneDate);
    }

    @Test
    public void lastDone() {
        Once.markDone(tagUnderTest);
        Date expectedDate = new Date();
        Date lastDoneDate = Once.lastDone(tagUnderTest);
        Assert.assertTrue((lastDoneDate.getTime() - expectedDate.getTime()) < 10);
    }

    @Test
    public void lastDoneMultipleDates() throws InterruptedException {
        Once.markDone(tagUnderTest);
        Thread.sleep(100);
        Once.markDone(tagUnderTest);
        Date expectedDate = new Date();
        Date lastDoneDate = Once.lastDone(tagUnderTest);
        Assert.assertTrue((lastDoneDate.getTime() - expectedDate.getTime()) < 10);
    }

    private void simulateAppUpdate() {
        RobolectricPackageManager rpm = RuntimeEnvironment.getRobolectricPackageManager();
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.packageName = RuntimeEnvironment.application.getPackageName();
        packageInfo.lastUpdateTime = new Date().getTime();
        rpm.addPackage(packageInfo);
        Once.initialise(RuntimeEnvironment.application);
    }

}
