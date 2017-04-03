package jonathanfinerty.once;

import android.content.pm.PackageInfo;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.res.builder.RobolectricPackageManager;

import java.util.Date;

class TestUtils {

    static void simulateAppUpdate() {
        RobolectricPackageManager rpm = RuntimeEnvironment.getRobolectricPackageManager();
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.packageName = RuntimeEnvironment.application.getPackageName();
        packageInfo.lastUpdateTime = new Date().getTime();
        rpm.addPackage(packageInfo);
        Once.initialise(RuntimeEnvironment.application);
    }

}
