package com.example.weather;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.weather.model.dao.CityDao;
import com.example.weather.model.entity.City;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Room Database for the app. The sometime provide bindings for the related DAO.
 *
 * For schema update, please add new {@link Migration} and change the version number accordingly.
 */
@Database(entities = {City.class}, exportSchema = false, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    @InstallIn(SingletonComponent.class)
    @Module
    public static class DatabaseModule {

        @Singleton
        @Provides
        WeatherDatabase provideWeatherDatabase(@ApplicationContext Context context) {
            return Room.databaseBuilder(context, WeatherDatabase.class, "weather.db")
                    //.addMigrations(MIGRATION_1_2)
                    .build();
        }

        @Singleton
        @Provides
        CityDao provideCityDao(WeatherDatabase weatherDatabase) {
            return weatherDatabase.cityDao();
        }
    }

    public abstract CityDao cityDao();

    // An example migration.
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Your migration strategy here
            // database.execSQL("ALTER TABLE City ADD COLUMN state VARCHAR(100)")
        }
    };
}
