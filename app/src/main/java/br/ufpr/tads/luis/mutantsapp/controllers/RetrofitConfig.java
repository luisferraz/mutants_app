package br.ufpr.tads.luis.mutantsapp.controllers;

import br.ufpr.tads.luis.mutantsapp.services.LoginService;
import br.ufpr.tads.luis.mutantsapp.services.MutantsService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;
    //    private final String baseURL = "http://localhost:3000/";
    private final String baseURL = "http://10.0.2.2:3000/";


    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public LoginService loginService() {
        return this.retrofit.create(LoginService.class);
    }

    public MutantsService getMutantsService() {
        return this.retrofit.create(MutantsService.class);
    }
}
