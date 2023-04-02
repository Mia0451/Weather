package com.example.weather;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Hilt app entry point to bootstrap application.
 */
@HiltAndroidApp
public final class WeatherApp extends MultiDexApplication {}