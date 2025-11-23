package com.example.iot_lab7_20220378.models;
public class RegistroRequest {
    private String dni;
    private String correo;

    public RegistroRequest(String dni, String correo) {
        this.dni = dni;
        this.correo = correo;
    }

    public String getDni() { return dni; }
    public String getCorreo() { return correo; }
}
