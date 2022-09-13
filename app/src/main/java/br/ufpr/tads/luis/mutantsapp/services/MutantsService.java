package br.ufpr.tads.luis.mutantsapp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufpr.tads.luis.mutantsapp.models.Mutant;
import br.ufpr.tads.luis.mutantsapp.models.TopAbility;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MutantsService {
    //Cria o registro de um novo mutante na API
    @Multipart
    @POST("/mutants/")
    Call<Mutant> createMutant(@Header("Authorization") String token, @Part MultipartBody.Part foto, @Part MultipartBody.Part nome, @Part("ability[]") ArrayList<RequestBody> habilidades);

    //Atualiza o registro de um mutante na API
    @Multipart
    @PUT("/mutants/{mutantId}/")
    Call<Mutant> updateMutant(@Header("Authorization") String token, @Path("mutantId") Integer mutantId, @Part MultipartBody.Part foto, @Part MultipartBody.Part nome, @Part("ability[]") ArrayList<RequestBody> habilidades);

    //Deleta o registro do mutante
    @DELETE("/mutants/{mutantId}")
    Call<ResponseBody> deleteMutant(@Header("Authorization") String token, @Path("mutantId") Integer mutantId);

    //Retorna a lista com todos os mutantes cadastrados
    @GET("/mutants/all/")
    Call<List<Mutant>> getAllMutants(@Header("Authorization") String token);

    //Retorna um mutante pelo ID
    @GET("/mutants")
    Call<Mutant> getMutantById(@Header("Authorization") String token, @Query("id") Integer mutantId);

    //Retorna o top Habilidades
    @GET("/mutants/ability/top/")
    Call<List<TopAbility>> getTopAbilities(@Header("Authorization") String token, @Query("limit") int limit);

    //Retorna a lista de mutantes por habilidade
    @GET("/mutants/ability")
    Call<List<Mutant>> getMutantsByAbility(@Header("Authorization") String token, @Query("ability") String ability);

    //Retorna a foto de um mutante a partir do seu id
    @GET("/mutants/photo")
    Call<ResponseBody> getMutantPhoto(@Header("Authorization") String token, @Query("id") Integer mutantId);
}
