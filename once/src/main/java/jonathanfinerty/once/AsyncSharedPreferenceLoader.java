package jonathanfinerty.once;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

class AsyncSharedPreferenceLoader {

    interface Listener {
        void onLoad(SharedPreferences sharedPreferences);
    }

    private final Context context;
    private final Listener listener;

    private final AsyncTask<String, Void, SharedPreferences> asyncTask = new AsyncTask<String, Void, SharedPreferences>() {
        @Override
        protected SharedPreferences doInBackground(String... names) {
            return context.getSharedPreferences(names[0], Context.MODE_PRIVATE);
        }

        @Override
        protected void onPostExecute(SharedPreferences sharedPreferences) {
            listener.onLoad(sharedPreferences);
        }
    };

    public AsyncSharedPreferenceLoader(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void load(@NonNull String name) {
        asyncTask.execute(name);
    }
}
