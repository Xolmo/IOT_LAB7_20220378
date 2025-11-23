package com.example.iot_lab7_20220378;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iot_lab7_20220378.R;
import com.example.iot_lab7_20220378.services.AuthService;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo, etPassword;
    private Button btnLogin;
    private TextView btnOlvidoPassword, btnIrRegistro;

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authService = new AuthService(this);

        // Vincular vistas
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnOlvidoPassword = findViewById(R.id.btnOlvidoPassword);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);

        // Acciones
        btnLogin.setOnClickListener(v -> login());
        btnOlvidoPassword.setOnClickListener(v -> recuperarPassword());
        btnIrRegistro.setOnClickListener(v -> irARegistro());
    }

    private void login() {
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (correo.isEmpty()) {
            etCorreo.setError("Ingrese su correo");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingrese su contraseña");
            return;
        }

        authService.iniciarSesion(correo, password, new AuthService.OnAuthResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                Log.d("Error", error);
            }
        });
    }

    private void recuperarPassword() {
        String correo = etCorreo.getText().toString().trim();

        if (correo.isEmpty()) {
            Toast.makeText(this, "Ingrese su correo para recuperar su contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        authService.recuperarContrasena(correo, new AuthService.OnAuthResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "Correo enviado para restablecer contraseña", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void irARegistro() {
        startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
    }
}
