package br.ufpr.tads.luis.mutantsapp.controllers;

import java.util.concurrent.TimeUnit;

import br.ufpr.tads.luis.mutantsapp.services.LoginService;
import br.ufpr.tads.luis.mutantsapp.services.MutantsService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;
    //    private final String baseURL = "http://localhost:3000/";
    private final String baseURL = "http://10.0.2.2:3000/";


    public RetrofitConfig() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).callTimeout(10, TimeUnit.SECONDS).build();

        this.retrofit = new Retrofit.Builder().baseUrl(baseURL).client(client).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public LoginService loginService() {
        return this.retrofit.create(LoginService.class);
    }

    public MutantsService getMutantsService() {
        return this.retrofit.create(MutantsService.class);
    }
}
