package com.example.delmor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class PermissionManager {

    public static final int PERMISSION_REQUEST_CODE = 123;

    private FragmentActivity activity;
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher;

    public PermissionManager(FragmentActivity activity) {
        this.activity = activity;
        initializeRequestMultiplePermissionsLauncher();
    }

    private void initializeRequestMultiplePermissionsLauncher() {
        requestMultiplePermissionsLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    // Manejar el resultado de la solicitud de permisos si es necesario
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public boolean checkPermissions() {

        String[] requiredPermissions;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            requiredPermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    // Agrega más permisos según sea necesario
            };

        } else {
            requiredPermissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    // Agrega más permisos según sea necesario
            };
        }


        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false; // Al menos un permiso no está concedido
            }
        }

        return true; // Todos los permisos están concedidos
    }

    public void requestPermissions() {
        String[] permissions;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            permissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    // Agrega más permisos según sea necesario
            };

        } else {
            permissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    // Agrega más permisos según sea necesario
            };
        }
        requestMultiplePermissionsLauncher.launch(permissions);
    }

    public boolean handlePermissionResult(int[] grantResults) {
        boolean allPermissionsGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        return allPermissionsGranted;
    }
}
