package br.ufpr.tads.luis.mutantsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.tads.luis.mutantsapp.R;
import br.ufpr.tads.luis.mutantsapp.controllers.RetrofitConfig;
import br.ufpr.tads.luis.mutantsapp.models.Mutant;
import br.ufpr.tads.luis.mutantsapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMutantes;
    private List<Mutant> mutantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        recyclerViewMutantes = findViewById(R.id.recyclerViewMutantes);
        //Pega o user passado pela intent
        User user = (User) getIntent().getSerializableExtra("user");


        //Requista a lista de mutantes cadastrados
        getAllMutants(user.getTokens().getAccessToken());


    }

    //Requista a lista de mutantes cadastrados
    private void getAllMutants(String accessToken) {
        Call<List<Mutant>> callMutants = new RetrofitConfig().getMutantsService().getAllMutants(accessToken);
        callMutants.enqueue(new Callback<List<Mutant>>() {
            @Override
            public void onResponse(Call<List<Mutant>> call, Response<List<Mutant>> response) {
                if (response.isSuccessful()) {
                    mutantList = response.body();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new AlertDialog.Builder(ListaActivity.this).setTitle(String.format("Erro %d", response.code())).setMessage(jObjError.getString("error")).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Mutant>> call, Throwable t) {
                Log.e("ERRO", "getAllMutans: " + t.getMessage());
            }
        });
    }
}