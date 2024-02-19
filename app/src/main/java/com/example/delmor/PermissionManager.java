package com.example.delmor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class PermissionManager {

    private CountDownLatch storagePermissionLatch;

    public static final int PERMISSION_REQUEST_CODE = 123;

    public static final int REQUEST_MANAGE_ALL_FILES_ACCESS = 1;

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
                    // Maneja la respuesta de los permisos
                    boolean allPermissionsGranted = true;
                    for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                        if (!entry.getValue()) {
                            allPermissionsGranted = false;
                            break;
                        }
                    }

                    if (allPermissionsGranted) {
                        // Todos los permisos fueron concedidos
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            // Verifica si se otorgó el permiso MANAGE_EXTERNAL_STORAGE
                            if (!Environment.isExternalStorageManager()) {
                                openManageAllFilesAccessSettings();
                            }
                        }
                    } else {
                        // Al menos un permiso fue denegado
                        // Realiza acciones adicionales si es necesario
                    }
                });
    }

    public boolean checkPermissions() {
        // Verificar si se otorgó el permiso MANAGE_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            openManageAllFilesAccessSettings();
            return false; // No continuar la verificación de permisos
        }

        // Verificar otros permisos
        String[] requiredPermissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                // Agrega más permisos según sea necesario
        };

        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false; // Al menos un permiso no está concedido
            }
        }

        return true; // Todos los permisos están concedidos
    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            openManageAllFilesAccessSettings();
            String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    // Agrega más permisos según sea necesario
            };
            requestMultiplePermissionsLauncher.launch(permissions);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    // Agrega más permisos según sea necesario
            };
            requestMultiplePermissionsLauncher.launch(permissions);
        }
    }

    public boolean handlePermissionResult(int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Map<String, Boolean> permissionsMap = new HashMap<>();
            String[] requiredPermissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    // Agrega el permiso MANAGE_EXTERNAL_STORAGE para Android 11 y superior
            };

            for (int i = 0; i < requiredPermissions.length; i++) {
                permissionsMap.put(requiredPermissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }

            boolean allPermissionsGranted = !permissionsMap.containsValue(false);

            if (allPermissionsGranted) {
                if (Environment.isExternalStorageManager()) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } else {
            Map<String, Boolean> permissionsMap = new HashMap<>();
            String[] requiredPermissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    // Agrega el permiso MANAGE_EXTERNAL_STORAGE para Android 11 y superior
            };

            for (int i = 0; i < requiredPermissions.length; i++) {
                permissionsMap.put(requiredPermissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }

            boolean allPermissionsGranted = !permissionsMap.containsValue(false);

            return allPermissionsGranted;
        }
    }

    private void openManageAllFilesAccessSettings() {
        Toast.makeText(activity, "Permite el acceso al almacenamiento", Toast.LENGTH_SHORT).show();

        // Inicializa el CountDownLatch con 1
        storagePermissionLatch = new CountDownLatch(1);

        // Inicia la actividad para gestionar el permiso MANAGE_ALL_FILES_ACCESS
        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        activity.startActivityForResult(intent, REQUEST_MANAGE_ALL_FILES_ACCESS);

        try {
            // Espera hasta que se complete la gestión de permisos
            storagePermissionLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Continúa con la solicitud de otros permisos después de la gestión de permisos
//        requestOtherPermissions();
    }

    public void handleManageAllFilesAccessResult() {
        // Realiza acciones después de que la gestión de permisos de almacenamiento haya terminado

        // Por ejemplo, puedes realizar la solicitud de otros permisos aquí
//        requestOtherPermissions();

        // Libera el CountDownLatch para permitir que la ejecución continúe
        if (storagePermissionLatch != null) {
            storagePermissionLatch.countDown();
        }
    }



}
