package jonathanfinerty.once;

import android.content.Context;
import android.content.pm.PackageInfo;

import androidx.test.core.app.ApplicationProvider;

import org.robolectric.shadows.ShadowPackageManager;

import java.util.Date;

import static org.robolectric.Shadows.shadowOf;

class TestUtils {

    static void simulateAppUpdate() {
        Context applicationContext = ApplicationProvider.getApplicationContext();
        ShadowPackageManager spm = shadowOf(applicationContext.getPackageManager());
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.packageName = applicationContext.getPackageName();
        packageInfo.lastUpdateTime = new Date().getTime();
        spm.installPackage(packageInfo);
        Once.initialise(applicationContext);
    }

}
