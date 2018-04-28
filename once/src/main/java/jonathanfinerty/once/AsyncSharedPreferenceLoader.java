package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

class AsyncSharedPreferenceLoader {

    private final AsyncTask<String, Void, SharedPreferences> asyncTask;

    AsyncSharedPreferenceLoader(Context context, String name) {
        asyncTask = new SharedPreferencesAsyncTask(context);
        asyncTask.execute(name);
    }

    SharedPreferences get() {
        try {
            return asyncTask.get();
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
    }

    private static class SharedPreferencesAsyncTask extends AsyncTask<String, Void, SharedPreferences> {

        private final WeakReference<Context> context;

        SharedPreferencesAsyncTask(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected SharedPreferences doInBackground(String... names) {
            return context.get().getSharedPreferences(names[0], Context.MODE_PRIVATE);
        }
    }
}
