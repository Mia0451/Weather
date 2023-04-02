package com.example.weather.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weather.home.page.WeatherFragment;
import com.example.weather.model.entity.City;

import java.util.List;

public class PageAdapter extends FragmentStateAdapter {

    List<City> cities; // Mutable list, don't change this list inside this class.

    public PageAdapter(@NonNull FragmentActivity fragmentActivity, List<City> cities) {
        super(fragmentActivity);
        this.cities = cities;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return WeatherFragment.newInstance(cities.get(position));
    }

    @Override
    public long getItemId(int position) {
        return cities.get(position).hashCode(); // Needed because ViewPager will cache views.
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}
