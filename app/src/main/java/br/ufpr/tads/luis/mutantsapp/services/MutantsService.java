package br.ufpr.tads.luis.mutantsapp.services;

import java.util.List;

import br.ufpr.tads.luis.mutantsapp.models.Mutant;
import br.ufpr.tads.luis.mutantsapp.models.TopAbility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MutantsService {
    //Cria o registro de um novo mutante na API
    @Multipart
    @POST("/mutants/")
    Call<Mutant> createMutant(@Header("Authorization") String token, @Part("photo") String foto, @Part("name") String nome, @Part("ability[]") List<String> habilidades);

    //Atualiza o registro de um mutante na API
    @Multipart
    @PUT("/mutants/{mutantId}/")
    Call<Mutant> updateMutant(@Header("Authorization") String token, @Path ("mutantId") Integer mutantId, @Part("photo") String foto, @Part("name") String nome, @Part("ability[]") List<String> habilidades);

    //Deleta o registro do mutante
    @DELETE("/mutants/{mutantId}")
    Call<ResponseBody> deleteMutant(@Header("Authorization") String token, @Path("mutantId") Integer mutantId);

    //Retorna a lista com todos os mutantes cadastrados
    @GET("/mutants/all/")
    Call<List<Mutant>> getAllMutants(@Header("Authorization") String token);

    //Retorna um mutante pelo ID
    @GET("/mutants")
    Call<Mutant> getMutantById(@Header("Authorization") String token, @Query("id") Integer mutantId);

    //Retorna o top 3 Habilidades
    @GET("/mutants/ability/top/")
    Call<List<TopAbility>> getTopAbilities(@Header("Authorization") String token);


}
