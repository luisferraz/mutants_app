package br.ufpr.tads.luis.mutantsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.tads.luis.mutantsapp.R;
import br.ufpr.tads.luis.mutantsapp.controllers.RetrofitConfig;
import br.ufpr.tads.luis.mutantsapp.models.Mutant;
import br.ufpr.tads.luis.mutantsapp.models.TopAbility;
import br.ufpr.tads.luis.mutantsapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    TextView textViewTotalMutants, textViewTop3Abilities;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        textViewTotalMutants = findViewById(R.id.textViewTotalMutantes);
        textViewTop3Abilities = findViewById(R.id.textViewTop3Habilidades);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.getSerializable("user");

        countMutants(user.getTokens().getAccessToken());
        getTopAbilities(user.getTokens().getAccessToken());
    }

    //Requisita a lista de mutantes cadastrados para marcar a quantidade na tela
    private void countMutants(String accessToken) {
        Call<List<Mutant>> callMutants = new RetrofitConfig().getMutantsService().getAllMutants(accessToken);
        callMutants.enqueue(new Callback<List<Mutant>>() {
            @Override
            public void onResponse(Call<List<Mutant>> call, Response<List<Mutant>> response) {
                if (response.isSuccessful()) {
                    List<Mutant> mutantList = response.body();
                    textViewTotalMutants.setText(String.valueOf(mutantList.size()));

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e("ERRO", "countMutants " + jObjError.getString("error").toString());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Mutant>> call, Throwable t) {
                Log.e("ERRO", "countMutants: " + t.getMessage());
            }
        });
    }

    //Requisita o top 3 de Habilidades dos mutantes cadastrados
    private void getTopAbilities(String accessToken) {
        Call<List<TopAbility>> topAbilities = new RetrofitConfig().getMutantsService().getTopAbilities(accessToken, 3);
        topAbilities.enqueue(new Callback<List<TopAbility>>() {
            @Override
            public void onResponse(Call<List<TopAbility>> call, Response<List<TopAbility>> response) {
                if (response.isSuccessful()) {
                    List<TopAbility> abilities = response.body();
                    for (TopAbility ability : abilities) {
                        textViewTop3Abilities.append(String.format("%s (%d)\n", ability.getAbility(), ability.getCount()));
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e("ERRO", "getTopAbilities " + jObjError.getString("error").toString());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TopAbility>> call, Throwable t) {
                Log.e("ERRO", "getTopAbilities " + t.getMessage());
            }
        });
    }

    //Finaliza o aplicativo
    public void exitApp(View view) {
        finishAffinity();
    }

    //Passa para a activity de cadastro de mutantes
    public void cadastrarMutante(View view) {
        Intent intent = new Intent(DashboardActivity.this, CadastroActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Passa para a activity que lista todos os mutantes
    public void listaMutantes(View view) {
        Call<List<Mutant>> callMutants = new RetrofitConfig().getMutantsService().getAllMutants(user.getTokens().getAccessToken());
        callMutants.enqueue(new Callback<List<Mutant>>() {
            @Override
            public void onResponse(Call<List<Mutant>> call, Response<List<Mutant>> response) {
                if (response.isSuccessful()) {
                    List<Mutant> mutantList = new ArrayList<>(response.body());
                    if (mutantList.size() == 0) {
                        new AlertDialog.Builder(DashboardActivity.this).setTitle("Aten????o").setMessage("Nenhum mutante encontrado para exibi????o").show();
                        return;
                    }
                    Log.i("MutantList", "onResponse: " + mutantList.toString());
                    Intent intent = new Intent(DashboardActivity.this, ListaActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mutants", (Serializable) mutantList);
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new AlertDialog.Builder(DashboardActivity.this).setTitle(String.format("Erro %d", response.code())).setMessage(jObjError.getString("error")).show();
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

    //Passa para a activity que pesquisa os mutantes por habilidade
    public void pesquisaMutantes(View view) {
        Intent intent = new Intent(DashboardActivity.this, PesquisaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}