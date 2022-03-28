package com.grand.ezkorone.core.customTypes;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.BindingConversion;
import androidx.lifecycle.LiveData;

public class BindingConversions {

    @BindingConversion
    public static int convertBooleanToViewVisibility(@Nullable Boolean isVisible) {
        if (isVisible != null && isVisible) {
            return View.VISIBLE;
        }else {
            return View.GONE;
        }
    }

    @BindingConversion
    public static int convertBooleanToViewVisibility(@Nullable LiveData<Boolean> value) {
        Boolean bool = null;
        if (value != null) {
            bool = value.getValue();
        }
        return convertBooleanToViewVisibility(bool);
    }

}
