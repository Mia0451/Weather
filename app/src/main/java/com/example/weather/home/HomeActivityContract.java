package com.example.weather.home;

import androidx.lifecycle.LifecycleOwner;

import com.example.weather.contract.BaseContract;
import com.example.weather.contract.LifecycleAwareRxPresenter;
import com.example.weather.model.WeatherResponse;
import com.example.weather.model.entity.City;

import java.util.List;

import javax.inject.Qualifier;

import io.reactivex.rxjava3.core.Observable;

public interface HomeActivityContract {

    interface View extends BaseContract.ActivityView {
        void showCities(List<City> cities);

        Observable<City> getLocalCity();

        void deleteHistorySuccessfully();
    }

    abstract class Presenter extends LifecycleAwareRxPresenter {

        Presenter(LifecycleOwner lifecycleOwner) {
            super(lifecycleOwner);
        }

        abstract void clearCityHistory();
    }

    @Qualifier
    @interface HomeActivityLifecycleOwner {
    }
}
