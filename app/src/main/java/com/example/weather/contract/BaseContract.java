package com.example.weather.contract;

/**
 * Base contracts for MVP design.
 */
public interface BaseContract {

    /**
     * Base view. Please implements {@link ActivityView} or {@link FragmentView} instead of this.
     */
    interface BaseView {
        void showLoading(boolean show);

        void showErrorSnackbar();
    }

    interface ActivityView extends BaseView {
    }

    interface FragmentView extends BaseView {
    }

    /**
     * Base presenter.
     */
    interface BasePresenter {
    }
}
