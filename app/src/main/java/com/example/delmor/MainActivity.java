package com.example.delmor;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LocationManagerHelper.LocationUpdateListener {

    private PermissionManager permissionManager;
    private LocationManagerHelper locationManagerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager(this);
        locationManagerHelper = new LocationManagerHelper(this, this);

        // Verifica y solicita permisos al iniciar la actividad
        checkAndRequestPermissions();

        // Inicia la actualización periódica de la ubicación
        locationManagerHelper.startLocationUpdates();
    }

    // Método para verificar y solicitar permisos
    private void checkAndRequestPermissions() {
        if (!permissionManager.checkPermissions()) {
            permissionManager.requestPermissions();
        }
    }

    // Manejar la respuesta de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionManager.PERMISSION_REQUEST_CODE) {
            boolean permissionsGranted = permissionManager.handlePermissionResult(grantResults);

            if (permissionsGranted) {
                // Inicia la actualización periódica de la ubicación después de conceder los permisos
                locationManagerHelper.startLocationUpdates();
            } else {
                // Puedes manejar la negación de permisos según sea necesario
            }
        }
    }

    // Implementa el método de LocationUpdateListener para recibir actualizaciones de ubicación
    @Override
    public void onLocationUpdate(Location location) {
        // Muestra la latitud y longitud en un Toast
        String message = "Latitud: " + location.getLatitude() + "\nLongitud: " + location.getLongitude();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Detiene la actualización periódica de la ubicación cuando la actividad se detiene
    @Override
    protected void onStop() {
        super.onStop();
        locationManagerHelper.stopLocationUpdates();
    }
}
