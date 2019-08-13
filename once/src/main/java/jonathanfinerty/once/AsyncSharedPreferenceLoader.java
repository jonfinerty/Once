package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

class AsyncSharedPreferenceLoader {

    private final AsyncTask<String, Void, SharedPreferences> asyncTask;

    AsyncSharedPreferenceLoader(@NonNull Context context, @NonNull String name) {
        this(context, null, name);
    }

    AsyncSharedPreferenceLoader(@NonNull Context context, @Nullable Executor executor, @NonNull String name) {
        asyncTask = new SharedPreferencesAsyncTask(context);
        if (executor != null) {
            asyncTask.executeOnExecutor(executor, name);
        } else {
            asyncTask.execute(name);
        }
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
