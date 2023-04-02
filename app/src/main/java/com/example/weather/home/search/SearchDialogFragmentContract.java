package com.example.weather.home.search;

import androidx.lifecycle.LifecycleOwner;

import com.example.weather.contract.BaseContract;
import com.example.weather.contract.LifecycleAwareRxPresenter;

import java.util.List;

import javax.inject.Qualifier;

import io.reactivex.rxjava3.core.Observable;
import kotlin.Unit;

public interface SearchDialogFragmentContract {

    interface View extends BaseContract.FragmentView {
        Target getSearchTarget();
        void dismiss();
        void searchedCityAlreadyExist();
    }

    abstract class Presenter extends LifecycleAwareRxPresenter {

        Presenter(LifecycleOwner lifecycleOwner) {
            super(lifecycleOwner);
        }

        abstract void searchCity();
    }

    @Qualifier
    @interface SearchDialogFragmentLifecycleOwner {
    }
}
