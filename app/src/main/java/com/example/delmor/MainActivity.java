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
                // Acciones adicionales si todos los permisos fueron concedidos
                Toast.makeText(this, "Los permisos fueron concedidos", Toast.LENGTH_SHORT).show();
            } else {
                // Acciones adicionales si al menos un permiso fue denegado
                Toast.makeText(this, "Permisos no concedidos", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PermissionManager.REQUEST_MANAGE_ALL_FILES_ACCESS) {
            // La gestión de permisos se ha completado
            permissionManager.handleManageAllFilesAccessResult();
        }
    }


    // Método para obtener la ubicación una vez que se haya concedido el permiso
    private void obtenerUbicacion() {
        // Puedes agregar aquí el código para obtener la ubicación, similar a tu implementación anterior
    }
}
