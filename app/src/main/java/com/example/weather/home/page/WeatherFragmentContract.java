package com.example.weather.home.page;

import androidx.lifecycle.LifecycleOwner;

import com.example.weather.contract.BaseContract;
import com.example.weather.contract.LifecycleAwareRxPresenter;
import com.example.weather.model.WeatherResponse;
import com.example.weather.model.entity.City;

import javax.inject.Qualifier;

public interface WeatherFragmentContract {

    interface View extends BaseContract.FragmentView {
        void showWeatherData(WeatherResponse weatherResponse);
    }

    abstract class Presenter extends LifecycleAwareRxPresenter {

        Presenter(LifecycleOwner lifecycleOwner) {
            super(lifecycleOwner);
        }

        abstract void setCity(City city);
    }

    @Qualifier
    @interface WeatherFragmentLifecycleOwner {
    }
}
