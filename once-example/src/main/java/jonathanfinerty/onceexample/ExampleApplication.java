package jonathanfinerty.onceexample;

import android.app.Application;

import jonathanfinerty.once.Once;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Once.initialise(this);
    }
}
