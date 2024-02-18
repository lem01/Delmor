//package com.example.delmor;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//public class MainActivity extends AppCompatActivity {
//
//    TextView tvContenido;
//    FloatingActionButton fabGeolocalizacion;
//
//    private PermissionManager permissionManager;
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Detener las actualizaciones de ubicación cuando la actividad se destru
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initComponents();
//        permissionManager = new PermissionManager(this);
//
////        todo: Organizar mejor los Listeners
//        fabGeolocalizacion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Verificar si tienes permiso de geolocalización
//                if (permissionManager.checkPermission()) {
//                    // Permiso ya concedido, obtener la ubicación
//                    obtenerUbicacion();
//                } else {
//                    // Si no tienes permiso, solicítalo al usuario
//                    permissionManager.requestPermission();
//                }
//            }
//        });
//
//        permissionManager = new PermissionManager(this);
//
//        if (permissionManager.checkPermission()) {
//            // El permiso ya está concedido, realiza las acciones relacionadas con la geolocalización.
//        } else {
//            // Solicitar permiso al usuario
//            permissionManager.requestPermission();
//        }
//
//
//
//    }
//
//    // Método para obtener la ubicación una vez que se haya concedido el permiso
//    @SuppressLint("MissingPermission")
//    private void obtenerUbicacion() {
//        // Utiliza FusedLocationProviderClient para obtener la ubicación actual
//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            // Ubicación obtenida con éxito
//                            double latitude = location.getLatitude();
//                            double longitude = location.getLongitude();
//
//                            // Muestra la ubicación en un Toast
//                            mostrarToast("Ubicación: " + latitude + ", " + longitude);
//                        } else {
//                            // Manejar el caso en el que la ubicación es nula
//                            mostrarToast("No se pudo obtener la ubicación");
//                        }
//                    }
//
//                });
//    }
//
//    // Método para mostrar un Toast
//    private void mostrarToast(String mensaje) {
//        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (permissionManager.handlePermissionResult(requestCode, grantResults)) {
//            // Permiso concedido, realiza las acciones relacionadas con la geolocalización.
//        } else {
//            // Permiso denegado, puedes informar al usuario o realizar alguna acción alternativa.
//        }
//    }
//
//    private void openAppSettings(Context context) {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//        intent.setData(uri);
//        startActivity(intent);
//    }
//
//
//
//
//    private void initComponents() {
//        tvContenido = (TextView) findViewById(R.id.tv_cotenido);
//        fabGeolocalizacion = (FloatingActionButton) findViewById(R.id.fab_geolocalizacion);
//    }
//
//}


package com.example.delmor;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager(this);

        // Verifica y solicita permisos al iniciar la actividad
        checkAndRequestPermissions();

        // Aquí puedes agregar el resto del código de tu actividad
    }

    // Método para verificar y solicitar permisos
    private void checkAndRequestPermissions() {

        while (!permissionManager.checkPermissions()) {
            permissionManager.requestPermissions();
        }
//        if (!permissionManager.checkPermissions()) {
//            permissionManager.requestPermissions();
//        }
    }

    // Manejar la respuesta de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Delegar el manejo al PermissionManager
        if (requestCode == PermissionManager.PERMISSION_REQUEST_CODE) {
            boolean permissionsGranted = permissionManager.handlePermissionResult(grantResults);

            if (permissionsGranted) {
                // Acciones adicionales si todos los permisos fueron concedidos
                Toast.makeText(this, "Los permisos fueron concedidos", Toast.LENGTH_SHORT).show();
            } else {
                // Acciones adicionales si al menos un permiso fue denegado
                Toast.makeText(this, "Permisos no concedidos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

