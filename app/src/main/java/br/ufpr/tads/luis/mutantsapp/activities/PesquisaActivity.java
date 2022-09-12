package br.ufpr.tads.luis.mutantsapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class PesquisaActivity extends AppCompatActivity {
    User user;
    EditText editTextPesquisaHabilidade;
    ListView listViewResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        user = (User) getIntent().getSerializableExtra("user");
        listViewResultado = findViewById(R.id.listViewPesquisaResultado);
        editTextPesquisaHabilidade = findViewById(R.id.editTextPesquisaHabilidade);
    }

    public void pesquisaPorhabilidade(View view) {
        if (editTextPesquisaHabilidade.length() == 0) {
            Toast.makeText(this, "Insira uma habilidade para pesquisar!", Toast.LENGTH_SHORT).show();
            return;
        }
        String habilidade = editTextPesquisaHabilidade.getText().toString();
        Call<List<Mutant>> callMutants = new RetrofitConfig().getMutantsService().getMutantsByAbility(user.getTokens().getAccessToken(), habilidade);
        callMutants.enqueue(new Callback<List<Mutant>>() {
            @Override
            public void onResponse(Call<List<Mutant>> call, Response<List<Mutant>> response) {
                if (response.isSuccessful()) {
                    List<Mutant> mutantList = response.body();
                    List<String> mutantNames = new ArrayList<>();
                    for (Mutant mutant : mutantList) {
                        mutantNames.add(mutant.getName());
                    }
                    ArrayAdapter<String> array = new ArrayAdapter<>(PesquisaActivity.this, android.R.layout.simple_list_item_1, mutantNames);
                    listViewResultado.setAdapter(array);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new AlertDialog.Builder(PesquisaActivity.this).setTitle(String.format("Erro %d", response.code())).setMessage(jObjError.getString("error")).show();
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