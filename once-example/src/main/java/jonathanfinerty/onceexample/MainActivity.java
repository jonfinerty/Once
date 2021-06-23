package jonathanfinerty.onceexample;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import jonathanfinerty.once.Once;

import static jonathanfinerty.once.Amount.exactly;
import static jonathanfinerty.once.Once.THIS_APP_INSTALL;
import static jonathanfinerty.once.Once.beenDone;
import static jonathanfinerty.once.Once.clearDone;
import static jonathanfinerty.once.Once.markDone;

public class MainActivity extends AppCompatActivity {

    private static final String SHOW_NEW_SESSION_DIALOG = "NewSessionDialog";
    private static final String SHOW_FRESH_INSTALL_DIALOG = "FreshInstallDialog";
    private static final String SHOW_NEW_VERSION_DIALOG = "NewVersionDialog";
    private static final String SHOW_MINUTE_DIALOG = "OncePerMinuteDialog";
    private static final String SHOW_SECOND_DIALOG = "OncePerSecondDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Once.markDone("Application Launched");

        setContentView(R.layout.activity_main);

        Button oncePerSessionButton = findViewById(R.id.once_per_session_button);
        oncePerSessionButton.setOnClickListener(v -> {
            if (!beenDone(Once.THIS_APP_SESSION, SHOW_NEW_SESSION_DIALOG)) {
                showDialog("This dialog should only appear once per app session");
                markDone(SHOW_NEW_SESSION_DIALOG);
            }
        });

        Button oncePerInstallButton = findViewById(R.id.once_per_install_button);
        oncePerInstallButton.setOnClickListener(v -> {
            if (!beenDone(THIS_APP_INSTALL, SHOW_FRESH_INSTALL_DIALOG)) {
                showDialog("This dialog should only appear once per app installation");
                markDone(SHOW_FRESH_INSTALL_DIALOG);
            }
        });

        Button oncePerVersionButton = findViewById(R.id.once_per_version_button);
        oncePerVersionButton.setOnClickListener(v -> {
            if (!beenDone(Once.THIS_APP_VERSION, SHOW_NEW_VERSION_DIALOG)) {
                showDialog("This dialog should only appear once per app version");
                markDone(SHOW_NEW_VERSION_DIALOG);
            }
        });

        Button oncePerMinuteButton = findViewById(R.id.once_per_minute_button);
        oncePerMinuteButton.setOnClickListener(v -> {
            if (!beenDone(TimeUnit.MINUTES, 1, SHOW_MINUTE_DIALOG)) {
                showDialog("This dialog should only appear once per minute");
                markDone(SHOW_MINUTE_DIALOG);
            }
        });

        Button oncePerSecondButton = findViewById(R.id.once_per_second_button);
        oncePerSecondButton.setOnClickListener(v -> {
            if (!beenDone(1000L, SHOW_SECOND_DIALOG)) {
                showDialog("This dialog should only appear once per second");
                markDone(SHOW_SECOND_DIALOG);
            }
        });

        Button oncePerThreePressesButton = findViewById(R.id.three_presses_button);
        oncePerThreePressesButton.setOnClickListener(v -> {
            String buttonPressedTag = "button pressed";
            markDone(buttonPressedTag);
            if (beenDone(buttonPressedTag, exactly(3))) {
                showDialog("This dialog should only appear once every three presses");
                clearDone(buttonPressedTag);
            }
        });

        Button resetButton = findViewById(R.id.reset_all_button);
        resetButton.setOnClickListener(v -> Once.clearAll());
    }

    private void showDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
