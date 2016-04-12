package com.ftovaro.topappstoreapps.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.ftovaro.topappstoreapps.R;

/**
 * Created by FelipeTovar on 10-Apr-16.
 */
public class InfoShower {
    private static ProgressDialog progressDialog;

    /**
     * Display a progress dialog.
     * @param context   activity who calls the progress dialog.
     */
    public static void showDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Hide the current progress dialog.
     */
    public static void hideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
