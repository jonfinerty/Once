package jonathanfinerty.onceexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import jonathanfinerty.once.Once;

import static jonathanfinerty.once.Amount.exactly;
import static jonathanfinerty.once.Once.THIS_APP_INSTALL;
import static jonathanfinerty.once.Once.beenDone;
import static jonathanfinerty.once.Once.clearDone;
import static jonathanfinerty.once.Once.markDone;

public class MainActivity extends AppCompatActivity {

    private static final String SHOW_FRESH_INSTALL_DIALOG = "FreshInstallDialog";
    private static final String SHOW_NEW_VERSION_DIALOG = "NewVersionDialog";
    private static final String SHOW_MINUTE_DIALOG = "OncePerMinuteDialog";
    private static final String SHOW_SECOND_DIALOG = "OncePerSecondDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Once.markDone("Application Launched");

        setContentView(R.layout.activity_main);

        Button oncePerInstallButton = (Button) findViewById(R.id.once_per_install_button);
        oncePerInstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beenDone(THIS_APP_INSTALL, SHOW_FRESH_INSTALL_DIALOG)) {
                    showDialog("This dialog should only appear once per app installation");
                    markDone(SHOW_FRESH_INSTALL_DIALOG);
                }
            }
        });

        Button oncePerVersionButton = (Button) findViewById(R.id.once_per_version_button);
        oncePerVersionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beenDone(Once.THIS_APP_VERSION, SHOW_NEW_VERSION_DIALOG)) {
                    showDialog("This dialog should only appear once per app version");
                    markDone(SHOW_NEW_VERSION_DIALOG);
                }
            }
        });

        Button oncePerMinuteButton = (Button) findViewById(R.id.once_per_minute_button);
        oncePerMinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beenDone(TimeUnit.MINUTES, 1, SHOW_MINUTE_DIALOG)) {
                    showDialog("This dialog should only appear once per minute");
                    markDone(SHOW_MINUTE_DIALOG);
                }
            }
        });

        Button oncePerSecondButton = (Button) findViewById(R.id.once_per_second_button);
        oncePerSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beenDone(1000L, SHOW_SECOND_DIALOG)) {
                    showDialog("This dialog should only appear once per second");
                    markDone(SHOW_SECOND_DIALOG);
                }
            }
        });

        Button oncePerThreePressesButton = (Button) findViewById(R.id.three_presses_button);
        oncePerThreePressesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonPressedTag = "button pressed";
                markDone(buttonPressedTag);
                if (beenDone(buttonPressedTag, exactly(3))) {
                    showDialog("This dialog should only appear once every three presses");
                    clearDone(buttonPressedTag);
                }
            }
        });

        Button resetButton = (Button) findViewById(R.id.reset_all_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Once.clearAll();
            }
        });
    }

    private void showDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
