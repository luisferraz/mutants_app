package br.ufpr.tads.luis.mutantsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.ufpr.tads.luis.mutantsapp.controller.RetrofitConfig;
import br.ufpr.tads.luis.mutantsapp.model.LoginRequest;
import br.ufpr.tads.luis.mutantsapp.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Função de Login
    public void login(View view) {
        EditText editTextUser = findViewById(R.id.editTextLogin);
        EditText editTextSenha = findViewById(R.id.editTextSenha);

        if ((editTextUser.length() == 0) || (editTextSenha.length() == 0)) {
            Toast.makeText(this, "Informe o usuário e a senha para login!", Toast.LENGTH_SHORT).show();
            return;
        }
        String username = editTextUser.getText().toString();
        String senha = editTextSenha.getText().toString();

        //Cria um objeto de login para enviar as informações
        LoginRequest loginInfo = new LoginRequest();
        loginInfo.setUsername(username);
        loginInfo.setPassword(senha);

        //Efetua a chamada de login
        Call<User> userCall = new RetrofitConfig().loginService().loginUser(loginInfo);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //Caso a chamada tenha sucesso, recebemos os dados do usuario, passamos-o entao para a proxima activity
                if (response.isSuccessful()) {
                    User user = response.body();
                    Log.i("Login Successful", "onResponse: " + user.toString());
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                }
                else {
                    new AlertDialog.Builder(getApplicationContext()).setTitle("Erro!").setMessage("Login ou Senha incorretos").show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("CALL ERROR", "onFailure: " + t.getMessage(), t);
                new AlertDialog.Builder(getApplicationContext()).setTitle("Erro!").setMessage(t.getMessage()).show();
            }
        });

    }
}