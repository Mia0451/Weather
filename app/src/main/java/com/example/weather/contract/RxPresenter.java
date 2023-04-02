package com.example.weather.contract;

import androidx.annotation.CallSuper;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class RxPresenter implements BaseContract.BasePresenter {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * Please add all Disposable(s) during the subscription (which runs in non trivial time) using
     * {@link #addDisposable(Disposable)} function.
     */
    public abstract void subscribe();

    // TODO(wenyi): add code analyzer to analyse changed code to enforce this?
    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @CallSuper
    public void unsubscribe() {
        compositeDisposable.dispose();
        // Better create a new one to avoid newly active subscription accidentally added
        // to the disposing CompositeDisposable.
        compositeDisposable = new CompositeDisposable();
    }
}
