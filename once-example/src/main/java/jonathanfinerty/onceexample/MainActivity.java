package jonathanfinerty.onceexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;

import static jonathanfinerty.once.Once.THIS_APP_INSTALL;
import static jonathanfinerty.once.Once.beenDone;
import static jonathanfinerty.once.Once.markDone;

public class MainActivity extends ActionBarActivity {

    public static final String SHOW_FRESH_INSTALL_DIALOG = "FreshInstallDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!beenDone(THIS_APP_INSTALL, SHOW_FRESH_INSTALL_DIALOG)) {
            showFreshInstallDialog();
            markDone(SHOW_FRESH_INSTALL_DIALOG);
        }

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
}
