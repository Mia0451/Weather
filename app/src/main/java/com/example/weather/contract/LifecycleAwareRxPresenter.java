package com.example.weather.contract;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * We use onResume/onPause as the places to subscribe and unsubscribe, this might be suboptimal
 * since any time user navigate to another app, another activity or dialog shown we will re-start
 * the background task. However this is really case by case, we could consider move to
 * onStart/onStop if there is persuading situation.
 */
public abstract class LifecycleAwareRxPresenter extends RxPresenter implements DefaultLifecycleObserver {

    public LifecycleAwareRxPresenter(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onResume(owner);
        subscribe();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onPause(owner);
        unsubscribe();
    }
}
