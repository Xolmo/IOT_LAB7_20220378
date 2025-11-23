package com.example.iot_lab7_20220378;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import com.example.iot_lab7_20220378.R;
import com.example.iot_lab7_20220378.services.AuthService;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etDni, etCorreoReg, etPasswordReg;
    private Button btnRegistrar;
    private TextView btnIrLogin;

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        authService = new AuthService(this);

        etNombre = findViewById(R.id.etNombre);
        etDni = findViewById(R.id.etDni);
        etCorreoReg = findViewById(R.id.etCorreoReg);
        etPasswordReg = findViewById(R.id.etPasswordReg);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnIrLogin = findViewById(R.id.btnIrLogin);

        btnRegistrar.setOnClickListener(v -> registrar());
        btnIrLogin.setOnClickListener(v ->
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class)));
    }

    private void registrar() {

        String nombre = etNombre.getText().toString().trim();
        String dni = etDni.getText().toString().trim();
        String correo = etCorreoReg.getText().toString().trim();
        String password = etPasswordReg.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("Ingrese su nombre");
            return;
        }

        if (password.length() < 6) {
            etPasswordReg.setError("La contraseña debe tener mínimo 6 caracteres");
            return;
        }

        authService.registrarUsuario(nombre, dni, correo, password, new AuthService.OnAuthResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RegistroActivity.this, error, Toast.LENGTH_LONG).show();
                Log.d("Error", error);
            }
        });
    }
}
