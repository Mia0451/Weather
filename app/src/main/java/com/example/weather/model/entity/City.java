package com.example.weather.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"lon", "lat"}, unique = true)})
public class City implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "state")
    public String state;

    // Country code
    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "zip_code")
    public String zipcode;

    @ColumnInfo(name = "lon")
    public Double longitude;

    @ColumnInfo(name = "lat")
    public Double latitude;

    // Entities and POJOs must have a usable public constructor.
    public City() {}

    public City(double latitude, double longitude) {
        this.latitude = Math.floor(latitude * 100) / 100;  // we convert 3.4561 to 3.45
        this.longitude = Math.floor(longitude * 100) / 100;
    }

    public City(String countryCode) {
        this.country = countryCode;
    }

    public City(String city, String state, String country, double latitude, double longitude) {
        this(latitude, longitude);
        this.city = city;
        this.state = state;
        this.country = country;
    }

    // TODO(wenyi): This is really NOT good.
    public boolean hasLatLon() {
        return latitude != null && longitude != null;
    }

    // Auto-generated code for making City Parcelable
    protected City(Parcel in) {
        id = in.readInt();
        city = in.readString();
        country = in.readString();
        zipcode = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(zipcode);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}