package com.example.weather.home;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;

/**
 * Activity scoped Module to provide bindings for HomeActivity.
 */
@InstallIn(ActivityComponent.class)
@Module
public class HomeActivityModule {

    @Provides
    HomeActivity provideHomeActivity(@ActivityContext Context context) {
        return (HomeActivity) context;
    }

    @Provides
    HomeActivityContract.View provideHomeActivityView(HomeActivity homeActivity) {
        return homeActivity;
    }

    @Provides
    @HomeActivityContract.HomeActivityLifecycleOwner
    LifecycleOwner provideLifecycleOwner(HomeActivity homeActivity) {
        return homeActivity;
    }

    @Provides
    HomeActivityContract.Presenter providePresenter(HomeActivityPresenter presenter) {
        return presenter;
    }
}
