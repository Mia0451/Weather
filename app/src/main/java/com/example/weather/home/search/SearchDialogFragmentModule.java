package com.example.weather.home.search;

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
public class SearchDialogFragmentModule {

    @Provides
    SearchDialogFragment provideHomeActivityView(Fragment fragment) {
        return (SearchDialogFragment) fragment;
    }

    @Provides
    SearchDialogFragmentContract.View provideSearchDialogFragmentView(SearchDialogFragment searchDialogFragment) {
        return searchDialogFragment;
    }

    @Provides
    @SearchDialogFragmentContract.SearchDialogFragmentLifecycleOwner
    LifecycleOwner provideLifecycleOwner(SearchDialogFragment searchDialogFragment) {
        return searchDialogFragment;
    }

    @Provides
    SearchDialogFragmentContract.Presenter providePresenter(SearchDialogFragmentPresenter presenter) {
        return presenter;
    }
}

