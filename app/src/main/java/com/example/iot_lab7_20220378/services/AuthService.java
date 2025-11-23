package com.example.iot_lab7_20220378.services;

import android.content.Context;


import androidx.annotation.NonNull;

import com.example.iot_lab7_20220378.models.RegistroRequest;
import com.example.iot_lab7_20220378.network.RegistroApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

public class AuthService {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Context context;

    public AuthService(Context context) {
        this.context = context;
        inicializar();
    }

    // ---------------------------------------------------------
    // 1. Inicialización de Firebase
    // ---------------------------------------------------------
    public void inicializar() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // ---------------------------------------------------------
    // 2. Login con email y contraseña
    // ---------------------------------------------------------
    public void iniciarSesion(String correo, String password,
                              @NonNull OnAuthResult listener) {

        auth.signInWithEmailAndPassword(correo, password)
                .addOnSuccessListener(result -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // ---------------------------------------------------------
    // 3. Recuperar contraseña por correo
    // ---------------------------------------------------------
    public void recuperarContrasena(String correo,
                                    @NonNull OnAuthResult listener) {

        auth.sendPasswordResetEmail(correo)
                .addOnSuccessListener(a -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // ---------------------------------------------------------
    // 4. Cerrar sesión
    // ---------------------------------------------------------
    public void cerrarSesion() {
        auth.signOut();
    }

    // ---------------------------------------------------------
    // 5. Registro con validación via microservicio
    // ---------------------------------------------------------
    public void registrarUsuario(String nombre, String dni,
                                 String correo, String password,
                                 @NonNull OnAuthResult listener) {

        RegistroRequest request = new RegistroRequest(dni, correo);

        RegistroApiClient.getService().validarRegistro(request)
                .enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<Void> call,
                                           @NonNull retrofit2.Response<Void> response) {

                        if (response.isSuccessful()) {

                            auth.createUserWithEmailAndPassword(correo, password)
                                    .addOnSuccessListener(userResult -> {

                                        // Guardar datos extras en Firestore
                                        String uid = userResult.getUser().getUid();

                                        db.collection("users")
                                                .document(uid)
                                                .set(new User(nombre, dni, correo))
                                                .addOnSuccessListener(v -> listener.onSuccess())
                                                .addOnFailureListener(e -> listener.onError(e.getMessage()));
                                    })
                                    .addOnFailureListener(e -> listener.onError(e.getMessage()));

                        } else {

                            try {
                                String errorMsg = response.errorBody().string();
                                String mensaje = new JSONObject(errorMsg).getString("error");
                                listener.onError(mensaje);
                            } catch (Exception e) {
                                listener.onError("Error de validación");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<Void> call,
                                          @NonNull Throwable t) {
                        listener.onError("Error de conexión: " + t.getMessage());
                    }
                });
    }


    public interface OnAuthResult {
        void onSuccess();
        void onError(String error);
    }


    public static class User {
        public String nombre;
        public String dni;
        public String correo;

        public User() {}

        public User(String nombre, String dni, String correo) {
            this.nombre = nombre;
            this.dni = dni;
            this.correo = correo;
        }
    }
}
