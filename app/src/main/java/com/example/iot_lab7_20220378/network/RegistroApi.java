package com.example.iot_lab7_20220378.network;

import com.example.iot_lab7_20220378.models.RegistroRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistroApi {

    @POST("/registro")
    Call<Void> validarRegistro(@Body RegistroRequest request);
}

