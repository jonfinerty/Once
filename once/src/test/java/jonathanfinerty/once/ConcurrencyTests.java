package jonathanfinerty.once;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
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
    public void concurrentMarkDone() throws InterruptedException {
        boolean success = testConcurrency(new Runnable() {
            @Override
            public void run() {
                Once.markDone("tag under test");
            }
        });

        Assert.assertTrue(success);
    }

    private boolean testConcurrency(Runnable functionUnderTest) throws InterruptedException {
        ExceptionTrackingThreadFactory threadFactory = new ExceptionTrackingThreadFactory();
        ExecutorService exec = Executors.newFixedThreadPool(16, threadFactory);
        for (int i = 0; i < 10000; i++) {
            exec.execute(functionUnderTest);
        }
        exec.shutdown();
        exec.awaitTermination(10, TimeUnit.SECONDS);
        return !threadFactory.ExceptionThrown();
    }

    public final class ExceptionTrackingThreadFactory implements ThreadFactory {
        private boolean threadThrewException;

        public boolean ExceptionThrown() {
            return threadThrewException;
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread t = new Thread(runnable);
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                    threadThrewException = true;
                }
            });
            return t;
        }
    }
}
