package com.grand.ezkorone.core.customTypes

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

object MyContextWrapper {

    fun wrap(context: Context, language: String): ContextWrapper {
        val res = context.resources
        val configuration = res.configuration
        val newLocale = Locale(language)

        val newContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale)
            val localeList = LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context.createConfigurationContext(configuration);
        }else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.displayMetrics)
            context
        }

        return ContextWrapper(newContext)
    }

}

/*
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;


         {


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);

        }

        return new ContextWrapper(context);
    }
}

 */