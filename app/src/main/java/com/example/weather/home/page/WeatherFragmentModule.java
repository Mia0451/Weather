package com.example.weather.home.page;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;

/**
 * Activity scoped Module to provide bindings for HomeActivity.
 */
@InstallIn(FragmentComponent.class)
@Module
public class WeatherFragmentModule {

    @Provides
    WeatherFragment provideHomeActivityView(Fragment fragment) {
        return (WeatherFragment) fragment;
    }

    @Provides
    WeatherFragmentContract.View provideWeatherFragmentView(WeatherFragment weatherFragment) {
        return weatherFragment;
    }

    @Provides
    @WeatherFragmentContract.WeatherFragmentLifecycleOwner
    LifecycleOwner provideLifecycleOwner(WeatherFragment weatherFragment) {
        return weatherFragment;
    }

    @Provides
    WeatherFragmentContract.Presenter providePresenter(WeatherFragmentPresenter presenter) {
        return presenter;
    }
}

