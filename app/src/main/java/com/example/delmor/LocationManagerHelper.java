package com.example.delmor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class LocationManagerHelper {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Handler handler;
    private final int INTERVAL = 7000; // Intervalo de actualización en milisegundos

    public interface LocationUpdateListener {
        void onLocationUpdate(Location location);
    }

    private LocationUpdateListener locationUpdateListener;

    public LocationManagerHelper(Context context, LocationUpdateListener listener) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationUpdateListener = listener;
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        if (locationManager != null) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (locationUpdateListener != null) {
                        locationUpdateListener.onLocationUpdate(location);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };

            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(locationRunnable, INTERVAL);
            }

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        INTERVAL,
                        0, // Cambio mínimo en metros
                        locationListener
                );
            }
        }
    }

    public void stopLocationUpdates() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationListener = null;
            stopHandler();
        }
    }

    private void stopHandler() {
        if (handler != null) {
            handler.removeCallbacks(locationRunnable);
            handler = null;
        }
    }

    private final Runnable locationRunnable = new Runnable() {
        @Override
        public void run() {
            if (locationManager != null && locationListener != null) {
                @SuppressLint("MissingPermission") Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null && locationUpdateListener != null) {
                    locationUpdateListener.onLocationUpdate(lastKnownLocation);
                }
                handler.postDelayed(this, INTERVAL);
            }
        }
    };
}
