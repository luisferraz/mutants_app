package br.ufpr.tads.luis.mutantsapp.services;

import br.ufpr.tads.luis.mutantsapp.model.LoginRequest;
import br.ufpr.tads.luis.mutantsapp.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/users/login")
    Call<User> loginUser(@Body LoginRequest loginRequest);
}
