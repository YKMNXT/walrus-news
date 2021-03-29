package com.walrus.news.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utility {

    private static CustomProgressDialog mProgressDlg;

    public static void showProgressDialog(String msg, Activity mActivity) {
        //There is no need of showing a Progress Dialog if the Activity is Finishing
        if (mActivity.isFinishing()) {
            return;
        }
        dismissProgressDialog();
        mProgressDlg = new CustomProgressDialog(mActivity);

        mProgressDlg.setMessage(msg);

        mProgressDlg.getWindow().setBackgroundDrawable(
                mActivity.getResources().getDrawable(android.R.color.transparent));

        mProgressDlg.setCancelable(false);
        if (mActivity != null && !mActivity.isFinishing())
            mProgressDlg.show();

    }
    public static void dismissProgressDialog() {

        try {
            if (mProgressDlg != null && mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
            }

        } catch (Exception e) {
            // We understand that we should not catch generic Exception
            // But Progress Dialog exception are high during Stress Test resulting a Call from separate Thread
            // So we make sure here that when Activity gets destroyed ;even though we dismiss the Progress Dialog;App doesnt crash
            // NO Data is lost and App Stability is preserved
            e.printStackTrace();
        }

    }

    public static long dateToMillis(String dateString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isNetworkOnline(Activity activity) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }

}
