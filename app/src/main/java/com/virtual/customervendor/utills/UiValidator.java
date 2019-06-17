package com.virtual.customervendor.utills;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.virtual.customervendor.R;

import java.util.regex.Pattern;

public class UiValidator {

    /**
     * (?=.*\d)	  must contains one digit from 0-9
     * (?=.*[A-Z])	must contains one uppercase characters
     * (?=.*[@#$%])  must contains one special symbols in the list "@#$%"
     * {6,20}	length at least 6 characters and maximum of 20
     * (?=.*[a-z]) must contains one lowercase characters
     */
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

    // 6 or more characters, that include must start with a letters, digit or special symbol "_" only
    private static final String USERNAME_PATTERN = "^[a-zA-Z]{5,15}$";
//
    public static void setValidationError(EditText inputLayout, String errorMessage) {
//        inputLayout.setErrorEnabled(true);
        inputLayout.setError(errorMessage);

    }

    public static void setValidationError(TextInputLayout inputLayout, String errorMessage) {
        inputLayout.setErrorEnabled(true);
        inputLayout.setError(errorMessage);
    }

    public static void disableValidationError(EditText inputLayout) {
//        if (inputLayout.isErrorEnabled()) {
//            inputLayout.setErrorEnabled(false);
//        }
    }

    public static void disableValidationError(TextInputLayout inputLayout) {
        if (inputLayout.isErrorEnabled()) {
            inputLayout.setErrorEnabled(false);
        }
    }

    /**
     * Method for checking text that is it a valid mail address or not
     */
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPassword(String password) {
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

    public static boolean isValidUserName(String userName) {
        return true;
//        return Pattern.compile(USERNAME_PATTERN).matcher(userName).matches();
    }

    public static boolean isValidPin(String pin) {
        return Pattern.compile("[0-9]{4}").matcher(pin).matches();
    }

    public static boolean isValidPhoneNumber(String target) {
        if (target != null && target.length() >= 5 ){
//                && Pattern.compile("(^\\+[1-9]{1}|([00]{2}))[0-9]{5,14}$").matcher(target).find()) {
            return true;
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
    public static void hideSoftKeyboardNew(Activity activity,View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)  ;
        if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static void displayMsg(Context context, String value) {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
    }

    public static void displayMsgSnack(View coordinatorLayout, Context context, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sbView.setBackgroundColor(Color.parseColor("#022E8D"));
        snackbar.show();
    }
    public static void displayMsgSnackShort(View coordinatorLayout, Context context, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sbView.setBackgroundColor(Color.parseColor("#022E8D"));
        snackbar.show();
    }

    public static void displayMsgSnackError(View coordinatorLayout, Context context, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        sbView.setBackgroundColor(Color.parseColor("#000E8D"));
        snackbar.show();
    }


    public static void setEditTextErrorBg(com.virtual.customervendor.customview.CustomEditText editText, Context context) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            editText.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.edittext_bg_error));
        else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            editText.setBackground(context.getResources().getDrawable(R.drawable.edittext_bg_error));
        else
            editText.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_bg_error));
    }

    public static void setEditTextBg(com.virtual.customervendor.customview.CustomEditText editText, Context context) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            editText.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.edittext_bg));
        else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1)
            editText.setBackground(context.getResources().getDrawable(R.drawable.edittext_bg));
        else
            editText.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_bg));
    }




}
