package com.example.weather.home.page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.weather.R;
import com.example.weather.model.WeatherResponse;
import com.example.weather.model.entity.City;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WeatherFragment extends Fragment implements WeatherFragmentContract.View {
    private static final String ARG_CITY_TAG = "city";
    private static final String WEATHER_ICON_URL = "http://openweathermap.org/img/wn/%s@2x.png";

    public static WeatherFragment newInstance(City city) {
        Bundle argument = new Bundle();
        argument.putParcelable(ARG_CITY_TAG, city);
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.setArguments(argument);
        return weatherFragment;
    }

    @Inject
    WeatherFragmentContract.Presenter presenter;

    private TextView cityName;
    private TextView temp;
    private TextView weather;
    private ImageView weatherIcon;
    private TextView lowHighTemp;

    private TextView humidity;
    private TextView wind;
    private TextView pressure;
    private TextView sunrise;
    private TextView sunset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        cityName = view.findViewById(R.id.city);
        temp = view.findViewById(R.id.temp);
        weather = view.findViewById(R.id.weather);
        weatherIcon = view.findViewById(R.id.icon);
        lowHighTemp = view.findViewById(R.id.low_high_temp);

        humidity = view.findViewById(R.id.humidity);
        wind = view.findViewById(R.id.wind);
        pressure = view.findViewById(R.id.pressure);
        sunrise = view.findViewById(R.id.sunrise);
        sunset = view.findViewById(R.id.sunset);

        presenter.setCity(getArguments().getParcelable(ARG_CITY_TAG));

        return view;
    }

    @Override
    public void showWeatherData(WeatherResponse weatherResponse) {
        cityName.setText(new StringBuilder(weatherResponse.getName()).append(", ").append(weatherResponse.getSys().country));
        temp.setText(new StringBuilder(String.format("%.2f", kelvinToFar(weatherResponse.getMain().temp))).append(" \u2109"));
        weather.setText(weatherResponse.getWeather()[0].main);
        Glide.with(this).load(String.format(WEATHER_ICON_URL, weatherResponse.getWeather()[0].icon)).into(weatherIcon);
        lowHighTemp.setText(new StringBuilder("Low: ").append(String.format("%.2f", kelvinToFar(weatherResponse.getMain().temp_min))).append(" \u2109").append(", High: ").append(String.format("%.2f", kelvinToFar(weatherResponse.getMain().temp_max))).append(" \u2109"));

        humidity.setText(String.valueOf(weatherResponse.getMain().humidity));
        wind.setText(String.valueOf(weatherResponse.getWind().speed));
        pressure.setText(String.valueOf(weatherResponse.getMain().pressure));

        sunrise.setText(readableData(weatherResponse.getSys().sunrise * 1000 + weatherResponse.getTimezone() * 1000));
        sunset.setText(readableData(weatherResponse.getSys().sunset * 1000 + weatherResponse.getTimezone() * 1000));
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void showErrorSnackbar() {
        Snackbar.make(getView(), R.string.weather_data_is_not_available, Snackbar.LENGTH_LONG).show();
    }

    // TODO(wenyi): should allow user to switch between kelvin, celsius and farenheit.
    // TODO(wenyi): use either SharedPreferences or room database to observe the change
    private double kelvinToFar(double kelvin) {
        return (kelvin - 273.15) * 1.8 + 32;
    }

    private String readableData(long millis) {
        Date currentDate = new Date(millis);
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(currentDate);
    }
}
