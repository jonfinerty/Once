package jonathanfinerty.once;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class ConcurrencyTests {

    @Before
    public void Setup() {
        Once.initialise(ApplicationProvider.getApplicationContext());
    }

    @After
    public void CleanUp() {
        Once.clearAll();
    }

    @Test
    public void concurrentMarkDone() throws Throwable {
        testConcurrency(() -> Once.markDone("tag under test"));
    }

    @Test
    public void concurrentToDo() throws Throwable {
        testConcurrency(() -> Once.toDo("tag under test"));
    }

    private void testConcurrency(Runnable functionUnderTest) throws Throwable {
        ExceptionTrackingThreadFactory threadFactory = new ExceptionTrackingThreadFactory();
        ExecutorService exec = Executors.newFixedThreadPool(16, threadFactory);
        for (int i = 0; i < 10000; i++) {
            exec.execute(functionUnderTest);
        }
        exec.shutdown();
        exec.awaitTermination(10, TimeUnit.SECONDS);
        if (threadFactory.getLastExceptionThrown() != null) {
            throw threadFactory.getLastExceptionThrown();
        }
    }

    public static final class ExceptionTrackingThreadFactory implements ThreadFactory {
        private Throwable lastExceptionThrown;

        public Throwable getLastExceptionThrown() {
            return lastExceptionThrown;
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread t = new Thread(runnable);
            t.setUncaughtExceptionHandler((thread, throwable) -> lastExceptionThrown = throwable);
            return t;
        }
    }
}
