package com.example.iot_lab7_20220378;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.iot_lab7_20220378.R;
import com.example.iot_lab7_20220378.services.AuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1000;

    private ImageView imgPerfil;
    private TextView tvNombre, tvCorreo, tvDni;
    private Button btnSubirFoto;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private Uri imagenUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imgPerfil = findViewById(R.id.imgPerfil);
        tvNombre = findViewById(R.id.tvNombre);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvDni = findViewById(R.id.tvDni);
        btnSubirFoto = findViewById(R.id.btnSubirFoto);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        cargarDatosUsuario();

        imgPerfil.setOnClickListener(v -> seleccionarImagen());
        btnSubirFoto.setOnClickListener(v -> seleccionarImagen());
    }

    private void cargarDatosUsuario() {
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String nombre = doc.getString("nombre");
                        String correo = doc.getString("correo");
                        String dni = doc.getString("dni");
                        String foto = doc.getString("foto");

                        tvNombre.setText("Nombre: " + nombre);
                        tvCorreo.setText("Correo: " + correo);
                        tvDni.setText("DNI: " + dni);

                        if (foto != null && !foto.isEmpty()) {
                            Glide.with(this).load(foto).into(imgPerfil);
                        }
                    }
                });
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            imagenUri = data.getData();
            imgPerfil.setImageURI(imagenUri);
            subirImagen();
        }
    }

    private void subirImagen() {
        if (imagenUri == null) return;

        String uid = auth.getCurrentUser().getUid();
        StorageReference ref = storage.getReference("perfiles/" + uid + ".jpg");

        ref.putFile(imagenUri)
                .addOnSuccessListener(task -> {
                    ref.getDownloadUrl().addOnSuccessListener(url -> {

                        db.collection("users")
                                .document(uid)
                                .update("foto", url.toString());

                        Toast.makeText(HomeActivity.this,
                                "Imagen subida: " + url.toString(),
                                Toast.LENGTH_LONG).show();

                        Glide.with(this).load(url).into(imgPerfil);

                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                });
    }
}
