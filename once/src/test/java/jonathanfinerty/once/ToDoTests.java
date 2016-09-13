package jonathanfinerty.once;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import static jonathanfinerty.once.TestUtils.simulateAppUpdate;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ToDoTests {

    @Before
    public void Setup() {
        Once.initialise(RuntimeEnvironment.application);
    }

    @After
    public void CleanUp() {
        Once.clearAll();
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

}
