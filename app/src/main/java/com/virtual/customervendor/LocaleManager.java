package com.virtual.customervendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.virtual.customervendor.managers.SharedPreferenceManager;
import com.virtual.customervendor.utills.AppKeys;
import com.virtual.customervendor.utills.AppUtils;

import java.util.Locale;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.N;

public class LocaleManager {


    private final SharedPreferences prefs;

    public LocaleManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context setLocale(Context c) {
        return updateResources(c, prefs.getString(AppKeys.USER_LANGUAGE, "en"));
    }

    public Context setNewLocale(Context c, String language) {
        return updateResources(c, language);
    }

    public String getLanguage() {
        return SharedPreferenceManager.getUserLanguage();
    }

    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (AppUtils.isAtLeastVersion(JELLY_BEAN_MR1)) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return AppUtils.isAtLeastVersion(N) ? config.getLocales().get(0) : config.locale;
    }
}