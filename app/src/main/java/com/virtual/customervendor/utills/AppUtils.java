package com.virtual.customervendor.utills;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.virtual.customervendor.listener.TimeListener;
import com.virtual.customervendor.managers.CachingManager;
import com.virtual.customervendor.managers.SharedPreferenceManager;
import com.virtual.customervendor.model.DayAviliability;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

public class AppUtils {
    private static final String TAG = "Fiverr-";

    /**
     * Check internet connection is connected or not
     *
     * @param context
     * @return
     */
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Generate Date String in Jan 11, 2017 format
     *
     * @param sec
     * @return
     */
    public static String generateDateStringFromSeconds(long sec) {
        Date date = new Date(sec * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * Convert long time into date String
     *
     * @param sec time
     * @return Today, 8:27AM / 4 days ago, 12:44PM / Mar 31, 9:28AM
     */
    public static String getRelativeTimeFromSeconds(long sec) {
        return (String) DateUtils.getRelativeDateTimeString(
                CachingManager.getApplicationContext(),
                sec * 1000,
                DateUtils.DAY_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,//The maximum resolution at which the time will switch to default date instead of spans.
                0);
    }


    public static void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static float convertDpToPx(Context context, float f) {
        try {
            return TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
        } catch (Exception context2) {
            return f;
        }
    }

//    public static String createFractionString(double fraction) {
//        return String.format(Locale.getDefault(), AppConstants.FRACTIONAL_FORMAT, fraction);
//    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    public static void makeTextViewResizable(final TextView tv,
                                             final int maxLine, final String expandText, final boolean viewMore) {
        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
        if (str.contains(spanableText)) {

            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "..... Less", false);
                    } else {
                        makeTextViewResizable(tv, 2, "..... More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static boolean validateTimeSlot(ArrayList<DayAviliability.TimeSlot> slots, DayAviliability.TimeSlot timeSlot,boolean isStartTime) {

        SimpleDateFormat format=new SimpleDateFormat("hh:mm");

        for (DayAviliability.TimeSlot slot :slots) {
            if (slot != timeSlot){
                String selectedTime;
                if(isStartTime) {
                    selectedTime = timeSlot.getStartTime();
                } else {
                    selectedTime = timeSlot.getStopTime();
                }
                try {
                    Date dateTimeStart=format.parse(slot.getStartTime());
                    Date dateTimeStop=format.parse(slot.getStopTime());
                    Date dateSelected=format.parse(selectedTime);

                    if(dateSelected.compareTo(dateTimeStart)==0 || dateSelected.compareTo(dateTimeStop)==0)
                        return false;
                    else if (dateSelected.compareTo(dateTimeStart) > 0 && dateSelected.compareTo(dateTimeStop)<0)
                        return false;
                    else
                        return true;

                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }
        }

        return false;
    }

    static class MySpannable extends ClickableSpan {

        private boolean isUnderline = false;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);
            ds.setFakeBoldText(true);
            ds.setColor(Color.parseColor("#343434"));

        }

        @Override
        public void onClick(View widget) {

        }
    }

    public static void alert(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });
        builder.show();
    }


    public static String getStatusString(boolean status) {
        if (status) return "1";
        else
            return "0";
    }


    public static Boolean getStatusBoolean(String status) {
        if (status.equals("1")) return true;
        else
            return false;
    }


    public static void getDate(FragmentManager fragmentManager, DatePickerDialog.OnDateSetListener activity, Calendar[] daysArray) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                activity,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );

// If you're calling this from a support Fragment
        dpd.show(fragmentManager, "Select Date");
        dpd.setSelectableDays(daysArray);
    }


    public static Calendar[] getEnabledDays(ArrayList<Integer> selectedDays) {
        GregorianCalendar g1 = new GregorianCalendar();
        g1.add(Calendar.DATE, 1);
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.MONTH, 6);
        List<Calendar> dayslist = new LinkedList<Calendar>();
        Calendar[] daysArray;
        Calendar cAux = Calendar.getInstance();
        while (cAux.getTimeInMillis() <= gc.getTimeInMillis()) {

            if (selectedDays.contains(cAux.get(Calendar.DAY_OF_WEEK))) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(cAux.getTimeInMillis());
                dayslist.add(c);
            }


            cAux.setTimeInMillis(cAux.getTimeInMillis() + (24 * 60 * 60 * 1000));
        }
        daysArray = new Calendar[dayslist.size()];
        for (int i = 0; i < daysArray.length; i++) {
            daysArray[i] = dayslist.get(i);
        }
        return daysArray;
    }


    public static String getRateWithSymbol(String val) {
        Locale locale;
        if (val != null && !val.isEmpty()) {
            Double currencyValue = Double.valueOf(val);
//        Locale locale = new Locale("en", "" + SharedPreferenceManager.getCurrentCountryDetails().getData().getCountryCode());
            /*surender*/
//            if (CachingManager.getCurrentCountry() != null) {
//                locale = new Locale("en", "" + CachingManager.getCurrentCountry().getCountryCode());
//            } else
            locale = new Locale("en", "US");
            // Currency currency = Currency.getInstance(locale);
            //String symbol = currency.getSymbol();
            NumberFormat mCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
            return mCurrencyFormat.format(currencyValue);
        } else {
            return "";
        }
    }

    public static SortedMap<Currency, Locale> currencyLocaleMap;

    static {
        currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
            public int compare(Currency c1, Currency c2) {
                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                currencyLocaleMap.put(currency, locale);
            } catch (Exception e) {
            }
        }
    }

    public static String getCurrencySymbol() {
//        Locale locale = new Locale("en", "" + SharedPreferenceManager.getRegisterCountryDetails().getData().getCountryCode());
//
//        String currencyCode = Currency.getInstance(locale).getCurrencyCode();
//        Currency currency = Currency.getInstance(currencyCode);
//        return currency.getSymbol(currencyLocaleMap.get(currency));

        return "$";
    }

    public static String getRegisterRateWithSymbol(String val) {
        Locale locale;
        if (val != null && !val.equals("")) {
            Double currencyValue = Double.valueOf(val);
//        Locale locale = new Locale("en", "" + SharedPreferenceManager.getCurrentCountryDetails().getData().getCountryCode());
            /*surender*/
//            if (SharedPreferenceManager.getRegisterCountryDetails() != null && SharedPreferenceManager.getRegisterCountryDetails().getData() != null)
//                locale = new Locale("en", "" + SharedPreferenceManager.getRegisterCountryDetails().getData().getCountryCode());
//            else
            locale = new Locale("en", "US");
            // Currency currency = Currency.getInstance(locale);
            //String symbol = currency.getSymbol();
            NumberFormat mCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
            return mCurrencyFormat.format(currencyValue);
        } else return "";
    }

    public static boolean isAtLeastVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static void getTimeNew(final EditText editText, AppCompatActivity activity) {
        new TimePickerDialogFixedNougatSpinner(activity, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                String min = "";
                String hour = "";

                if (hours < 10) {
                    hour = "0" + hours;
                } else
                    hour = "" + hours;

                if (minutes < 10) {
                    min = "0" + minutes;
                } else
                    min = "" + minutes;

                editText.setText("" + hour + ":" + min);
            }
        }, 0, 0, true
        ).show();
    }
    public static void getTimeNew(Context context, final TimeListener listener) {
        new TimePickerDialogFixedNougatSpinner(context, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                String min = "";
                String hour = "";

                if (hours < 10) {
                    hour = "0" + hours;
                } else
                    hour = "" + hours;

                if (minutes < 10) {
                    min = "0" + minutes;
                } else
                    min = "" + minutes;
                listener.onTimeSelect("" + hour + ":" + min);
            }
        }, 0, 0, true
        ).show();
    }
    public static void updateLanguageResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());


    }

}
