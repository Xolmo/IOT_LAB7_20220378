package com.example.iot_lab7_20220378.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroApiClient {

    private static RegistroApi instance;

    public static RegistroApi getService() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.18.171:8020")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(RegistroApi.class);
        }
        return instance;
    }
}
