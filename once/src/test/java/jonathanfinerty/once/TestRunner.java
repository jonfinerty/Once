package jonathanfinerty.once;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class TestRunner extends RobolectricGradleTestRunner {

    private static final String DEFAULT_MANIFEST_LOCATION = "src/main/AndroidManifest.xml";
    private static final String PROJECT_DIR = getProjectDirectory();
    private static final String MANIFEST_PROPERTY = PROJECT_DIR + DEFAULT_MANIFEST_LOCATION;
    private static final String RES_PROPERTY = PROJECT_DIR + "build/intermediates/res/debug/";
    private static final String ASSETS_PROPERTY = PROJECT_DIR + "build/intermediates/assets/debug/";

    private static final int MAX_SDK_SUPPORTED_BY_ROBOLECTRIC = 18;

    public TestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    private static String getProjectDirectory() {
        String path = "";

        try {
            File file = new File(".");
            path = file.getCanonicalPath();

            // Android studio has a different execution root for tests than pure gradle so append extra path part needed for Android Studio.
            if (!new File(DEFAULT_MANIFEST_LOCATION).exists()) {
                // Android Studio
                path = path + "/once/";
            } else {
                // Command line
                path = path + "/";
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return path;
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        return new AndroidManifest(Fs.fileFromPath(MANIFEST_PROPERTY), Fs.fileFromPath(RES_PROPERTY), Fs.fileFromPath(ASSETS_PROPERTY)) {

            @Override
            public int getTargetSdkVersion() {
                return MAX_SDK_SUPPORTED_BY_ROBOLECTRIC;
            }
        };
    }


}

