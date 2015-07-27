package jonathanfinerty.onceexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import jonathanfinerty.once.Once;

import static jonathanfinerty.once.Once.THIS_APP_INSTALL;
import static jonathanfinerty.once.Once.beenDone;
import static jonathanfinerty.once.Once.markDone;

public class MainActivity extends ActionBarActivity {

    public static final String SHOW_FRESH_INSTALL_DIALOG = "FreshInstallDialog";
    public static final String SHOW_NEW_VERSION_DIALOG = "NewVersionDialog";
    public static final String SHOW_MINUTE_DIALOG = "OncePerMinuteDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button oncePerInstallButton = (Button) findViewById(R.id.once_per_install_button);
        oncePerInstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beenDone(THIS_APP_INSTALL, SHOW_FRESH_INSTALL_DIALOG)) {
                    showFreshInstallDialog();
                    markDone(SHOW_FRESH_INSTALL_DIALOG);
                }
            }
        });

        Button oncePerVersionButton = (Button) findViewById(R.id.once_per_version_button);
        oncePerVersionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beenDone(Once.THIS_APP_VERSION, SHOW_NEW_VERSION_DIALOG)) {
                    showNewVersionDialog();
                    markDone(SHOW_NEW_VERSION_DIALOG);
                }
            }
        });

        Button oncePerMinuteButton = (Button) findViewById(R.id.once_per_minute_button);
        oncePerMinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!beenDone(TimeUnit.MINUTES, 1, SHOW_MINUTE_DIALOG)) {
                    showMinuteDialog();
                    markDone(SHOW_MINUTE_DIALOG);
                }*/
            }
        });

    }

    private void showFreshInstallDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("New Install Detected");
        alertDialog.setMessage("This dialog should only appear once per app installation");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showNewVersionDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("New Version Detected");
        alertDialog.setMessage("This dialog should only appear once per app version");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showMinuteDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Once per minute");
        alertDialog.setMessage("This dialog should only appear a maximum of once per minute");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
