//package com.example.delmor;
//import android.Manifest;
//import android.app.Activity;
//import android.content.pm.PackageManager;
//import android.os.Build;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class PermissionManager {
//
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
//
//    private Activity activity;
//
//    public PermissionManager(Activity activity) {
//        this.activity = activity;
//    }
//
//    public boolean checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            int permissionResult = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
//            return permissionResult == PackageManager.PERMISSION_GRANTED;
//
//        }
//        return true;
//    }
//
//    public void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//
//
//
//
//        }
//
//    }
//
//    public boolean handlePermissionResult(int requestCode, int[] grantResults) {
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permiso concedido
//                return true;
//            } else {
//                // Permiso denegado
//                return false;
//            }
//        }
//        return false;
//    }
//}
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
                    // Aquí puedes manejar la respuesta de los permisos
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

// SI usa android 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
//        Permiso no esta concedido
//        return  false;
                return false;
            }

            String[] requiredPermissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    // Agrega más permisos según sea necesario

            };

            for (String permission : requiredPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false; // Al menos un permiso no está concedido
                }
            }

            return true; // Todos los permisos están concedidos


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] requiredPermissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    // Agrega más permisos según sea necesario

//                    // Agrega el permiso MANAGE_EXTERNAL_STORAGE para Android 11 y superior
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ?
//                            Manifest.permission.MANAGE_EXTERNAL_STORAGE : ""
            };

            for (String permission : requiredPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false; // Al menos un permiso no está concedido
                }
            }

            return true; // Todos los permisos están concedidos
        }
        return true; // No es necesario verificar permisos en versiones anteriores a Marshmallow
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

//                    // Agrega el permiso MANAGE_EXTERNAL_STORAGE para Android 11 y superior
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ?
//                            Manifest.permission.MANAGE_EXTERNAL_STORAGE : ""
            };


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                openManageAllFilesAccessSettings();

            }
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

//        if (!allPermissionsGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // Al menos un permiso fue denegado y estamos en Android 11 o superior
//            openManageAllFilesAccessSettings();
//        }

            //se compruebe si todos los permisos son concecidos
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
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ?
//                        Manifest.permission.MANAGE_EXTERNAL_STORAGE : ""
            };

            for (int i = 0; i < requiredPermissions.length; i++) {
                permissionsMap.put(requiredPermissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }

            boolean allPermissionsGranted = !permissionsMap.containsValue(false);

            return allPermissionsGranted;

        }
    }

    private void openManageAllFilesAccessSettings() {
        Toast.makeText(activity, "Permite el acceso al alamacenamiento", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }
}
