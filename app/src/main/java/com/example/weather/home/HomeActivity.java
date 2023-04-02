package com.example.weather.home;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weather.R;
import com.example.weather.home.search.SearchDialogFragment;
import com.example.weather.location.LocalLocationAdapter;
import com.example.weather.model.entity.City;
import com.example.weather.permission.LocationPermissionRationaleDialogFragment;
import com.example.weather.permission.OnLocationPermissionListener;
import com.example.weather.permission.PermissionUtil;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity implements OnLocationPermissionListener, HomeActivityContract.View {

    @Inject
    HomeActivityContract.Presenter presenter;

    private DrawerLayout drawerLayout;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private List<City> displayedCities = new ArrayList<>(); // Mutable list contains all displayed cities.
    @VisibleForTesting
    PageAdapter pageAdapter = new PageAdapter(this, displayedCities);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        viewPager2.setAdapter(pageAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@androidx.annotation.NonNull TabLayout.Tab tab, int position) {
                // Do nothing.
            }
        }).attach();

        PermissionUtil.checkPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                locationPermissionRequestLauncher,
                () -> LocationPermissionRationaleDialogFragment.possiblyShow(this));

        TextView textView = findViewById(R.id.build_version);
        try {
            PackageInfo packageInfo =
                    getPackageManager().getPackageInfo(getPackageName(), 0);
            textView.setText(getString(R.string.build_version, packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("here", "Failed to get package info.");
        }

        findViewById(R.id.drawer_item_clear_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                presenter.clearCityHistory();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.search:
                SearchDialogFragment.possiblyShow(this);
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // The city list will be guaranteed to have at least one item.
    @Override
    public void showCities(List<City> cities) {
        int previousNum = displayedCities.size();
        displayedCities.clear();
        displayedCities.addAll(cities);
        pageAdapter.notifyDataSetChanged();
        if (displayedCities.size() != previousNum) { // either search city or cleared history.
            viewPager2.setCurrentItem(0);
        }
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void showErrorSnackbar() {
        Snackbar.make(viewPager2, R.string.something_wrong, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Observable<City> getLocalCity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("here", "here in HomeActivity.getLocalCity(), has permission, ");
            return LocalLocationAdapter.localLocationObservable(this, LocationServices.getFusedLocationProviderClient(this));
        } else {
            return Observable.create(new ObservableOnSubscribe<City>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<City> emitter) throws Throwable {
                    emitter.onNext(LocalLocationAdapter.getLocationWithCountryCode(HomeActivity.this));
                }
            });
        }
    }

    @Override
    public void deleteHistorySuccessfully() {
        Snackbar.make(viewPager2, R.string.history_cleared, Snackbar.LENGTH_LONG).show();
    }

    // From OnLocationPermissionListener.
    @Override
    public void grantLocationPermission() {
        locationPermissionRequestLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private ActivityResultLauncher<String> locationPermissionRequestLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.e("here", "here in HomeActivity.ActivityResultLauncher, location Permission granted");
                } else {
                    Log.e("here", "here in HomeActivity.ActivityResultLauncher, location Permission denied");
                }
            });
}
