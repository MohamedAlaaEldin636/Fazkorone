package com.grand.ezkorone.core.customTypes;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import kotlin.jvm.functions.Function0;

public class TransformationsUtils {

    @MainThread
    @NonNull
    public static <X, Y> MutableLiveData<Y> map(
            @NonNull LiveData<X> source,
            @NonNull final Function<X, Y> mapFunction
    ) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, x -> result.setValue(mapFunction.apply(x)));
        return result;
    }

    @MainThread
    @NonNull
    public static <X, Y> LiveData<Y> switchMap(
            @NonNull LiveData<X> source,
            @NonNull final Function<X, LiveData<Y>> switchMapFunction
    ) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {
            LiveData<Y> mSource;

            @Override
            public void onChanged(@Nullable X x) {
                LiveData<Y> newLiveData = switchMapFunction.apply(x);
                if (mSource == newLiveData) {
                    return;
                }
                if (mSource != null) {
                    result.removeSource(mSource);
                }
                mSource = newLiveData;
                if (mSource != null) {
                    result.addSource(mSource, new Observer<Y>() {
                        @Override
                        public void onChanged(@Nullable Y y) {
                            result.setValue(y);
                        }
                    });
                }
            }
        });
        return result;
    }

    @MainThread
    @NonNull
    public static <Y> LiveData<Y> switchMapMultiple(
            @NonNull final Function0<LiveData<Y>> switchMapFunction,
            @NonNull LiveData<?>... sources
    ) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        for (var source : sources) {
            result.addSource(source, new Observer<Object>() {
                LiveData<Y> mSource;

                @Override
                public void onChanged(@Nullable Object x) {
                    LiveData<Y> newLiveData = switchMapFunction.invoke();
                    if (mSource == newLiveData) {
                        return;
                    }
                    if (mSource != null) {
                        result.removeSource(mSource);
                    }
                    mSource = newLiveData;
                    if (mSource != null) {
                        result.addSource(mSource, result::setValue);
                    }
                }
            });
        }
        return result;
    }

}
