package com.ftovaro.topappstoreapps.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.ftovaro.topappstoreapps.R;

/**
 * Class to manage the different ways to show information to the user.
 * Created by FelipeTovar on 10-Apr-16.
 */
public class InfoShower {

    private static ProgressDialog progressDialog;

    /**
     * Shows a Snackbar.
     * @param coordinatorLayout where the snack will be shown.
     * @param message           what the Snackbar will say.
     * @param duration          how long is going to be visible.
     */
    public static void showSnack(CoordinatorLayout coordinatorLayout, String message, int duration) {
        Snackbar.make(coordinatorLayout, message, duration).show();
    }

    /**
     * Shows a Snackbar.
     * @param coordinatorLayout where the snack will be shown.
     * @param message           what the Snackbar will say.
     */
    public static void showSnack(CoordinatorLayout coordinatorLayout, String message) {
        showSnack(coordinatorLayout, message, Snackbar.LENGTH_LONG);
    }

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
