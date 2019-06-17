package com.virtual.customervendor.utills;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogLoader {
    private ProgressDialog pd;

    private static ProgressDialog progressDialog;

    Context context;
    static Activity activity;

    public ProgressDialogLoader(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

//    public void progressDialogCreation() {
//        try {
//            if (pd == null)
//                pd = ProgressDialog.show(activity, "", "Loading", true);
//        } catch (Exception e) {
//
//        }
//    }

    public static void progressDialogCreation(Activity activity1, String title) {

        activity =  activity1;

        try {
            if (progressDialog == null && activity !=null && !activity.isFinishing()) {
                progressDialog = ProgressDialog.show(activity, "", title, true);
//                progressDialog.setCancelable(false);
            }
        } catch (Exception e) {

        }
    }

    public static void progressDialogDismiss() {
        if (progressDialog != null && progressDialog.isShowing() &&  !activity.isFinishing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
}