package com.example.weather.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.weather.model.entity.City;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

/**
 * This class is UI thread only, also should be treated as ActivityScoped.
 * <p>
 * TODO(wenyi): re-visit to make sure cancel can also be propagated.
 */
public class LocalLocationAdapter {

    public static Observable<City> localLocationObservable(Activity context, FusedLocationProviderClient fusedLocationClient) {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
            return Observable.fromMaybe(new MaybeSource<City>() {
                @SuppressLint("MissingPermission")
                @Override
                public void subscribe(@NonNull MaybeObserver<? super City> observer) {
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        observer.onSuccess(androidLocationToCity(location));
                                    } else { // No last known location.
                                        observer.onSuccess(getLocationWithLocationManager(context));
                                    }
                                }
                            }).addOnFailureListener(context, new OnFailureListener() {
                                @Override
                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                    // fallback to location manager
                                    observer.onSuccess(getLocationWithLocationManager(context));
                                }
                            });
                }
            });
        } else { // Google Play Services not available.
            return Observable.create(new ObservableOnSubscribe<City>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<City> emitter) throws Throwable {
                    emitter.onNext(getLocationWithLocationManager(context));
                }
            });
        }
    }

    private static City getLocationWithLocationManager(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            return androidLocationToCity(location);
        } else {
            // Android API LocationManager failed to get last known location, try other means.
            return getLocationWithCountryCode(activity);
        }
    }

    public static City getLocationWithCountryCode(Activity context) {
        String locale = context.getResources().getConfiguration().locale.getCountry();
        return new City(locale);
    }

    private static City androidLocationToCity(Location location) {
        Log.e("here", "here in LocalLocationAdapter.androidLocationToCity(Location location), " + location.getLatitude() + ", " + location.getLongitude());
        return new City(location.getLatitude(), location.getLongitude());
    }
}
