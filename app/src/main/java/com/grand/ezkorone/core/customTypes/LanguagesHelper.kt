package com.grand.ezkorone.core.customTypes

import android.content.Context
import android.content.res.Configuration
import com.grand.ezkorone.presentation.base.MABaseActivity
import timber.log.Timber
import java.util.*

object LanguagesHelper {

    fun changeLanguage(context: Context, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        setLanguage(lang)
    }

    fun setLanguage(language: String) {
        Timber.e("language set $language")

        // app is only one lang todo see below in case multiple ones isa.
    }

    fun getCurrentLanguage(): String = MABaseActivity.APPLICATION_DEFAULT_LANGUAGE

}
/*
import android.content.SharedPreferences;

import grand.app.moon.utils.storage.user.UserHelper;
import grand.app.moon.vollyutils.MyApplication;

    public static void setLanguage(String language) {
        Timber.e("languageSet:"+language);
        SharedPreferences userDetails = MyApplication.getInstance().getSharedPreferences(Constants.LANGUAGE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString(Constants.LANGUAGE, language);
        editor.putBoolean(Constants.LANGUAGE_HAVE, true);
        editor.commit();
    }

    public static String getCurrentLanguage() {
        SharedPreferences preferences = MyApplication.getInstance().getApplicationContext().getSharedPreferences(Constants.LANGUAGE_DATA, Context.MODE_PRIVATE);
        if (preferences.getString(Constants.LANGUAGE, "").length() > 0) {
            return preferences.getString(Constants.LANGUAGE, "");
        } else {
            setLanguage(Constants.DEFAULT_LANGUAGE);
            return Constants.DEFAULT_LANGUAGE;
        }
    }
}

 */
