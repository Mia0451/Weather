package com.example.weather.home.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.weather.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding4.view.RxView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Observable;
import kotlin.Unit;

@AndroidEntryPoint
public class SearchDialogFragment extends AppCompatDialogFragment implements SearchDialogFragmentContract.View {

    private static final String TAG = "SearchDialogFragment";

    public static void possiblyShow(AppCompatActivity parent) {
        if (parent.getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            new SearchDialogFragment().showNow(parent.getSupportFragmentManager(), TAG);
        }
    }

    @Inject
    SearchDialogFragmentContract.Presenter presenter;

    private TextInputEditText cityInput;
    private TextInputEditText stateInput;
    private TextInputEditText countryInput;
    private TextInputEditText zipCodeInput;
    private Button searchButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        cityInput = view.findViewById(R.id.city_input);
        stateInput = view.findViewById(R.id.state_input);
        countryInput = view.findViewById(R.id.country_input);
        zipCodeInput = view.findViewById(R.id.zip_code_input);
        Button cancel = view.findViewById(R.id.cancel);
        searchButton = view.findViewById(R.id.search);

        Log.e("here", "here in SearchFragment.onCreateView(), search button value assigned");

        cancel.setOnClickListener(v -> dismissAllowingStateLoss());
        // TODO(wenyi): consider use jakewharton.rxbinding4
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.searchCity();
            }
        });

        return view;
    }

    @Override
    public Target getSearchTarget() {
        return new Target(
                cityInput.getText().toString().trim(),
                stateInput.getText().toString().trim(),
                countryInput.getText().toString().trim(),
                zipCodeInput.getText().toString().trim());
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void searchedCityAlreadyExist() {
        Snackbar.make(getView(), R.string.city_already_exist, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorSnackbar() {
        Snackbar.make(getView(), R.string.missing_location_details, Snackbar.LENGTH_LONG).show();
    }
}
