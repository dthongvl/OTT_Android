package vizion.com.ott.Utils;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by dthongvl on 11/13/16.
 */
public class MyProgressDialog {
    private ProgressDialog progressDialog;
    private Activity activity;
    private String message;
    private static MyProgressDialog ourInstance = new MyProgressDialog();

    public static MyProgressDialog getInstance(Activity activity, String message) {
        ourInstance.activity = activity;
        ourInstance.message = message;
        return ourInstance;
    }

    private MyProgressDialog() {
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(ourInstance.activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(ourInstance.message);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
