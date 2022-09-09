package br.ufpr.tads.luis.mutantsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.ufpr.tads.luis.mutantsapp.R;
import br.ufpr.tads.luis.mutantsapp.controllers.RetrofitConfig;
import br.ufpr.tads.luis.mutantsapp.models.LoginRequest;
import br.ufpr.tads.luis.mutantsapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText editTextUser, editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUser = findViewById(R.id.editTextLogin);
        editTextSenha = findViewById(R.id.editTextSenha);
    }

    //Função de Login
    public void login(View view) {
//        if ((editTextUser.length() == 0) || (editTextSenha.length() == 0)) {
//            Toast.makeText(this, "Informe o usuário e a senha para login!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String username = editTextUser.getText().toString();
//        String senha = editTextSenha.getText().toString();
        String username = "admin";
        String senha = "123456789";

        //Cria um objeto de login para enviar as informações
        LoginRequest loginInfo = new LoginRequest();
        loginInfo.setUsername(username);
        loginInfo.setPassword(senha);


        //Efetua a chamada de login
        Call<User> userCall = new RetrofitConfig().loginService().loginUser(loginInfo);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //Caso a chamada tenha sucesso, recebemos os dados do usuário, passamos-o entao para a proxima activity
                if (response.isSuccessful()) {
                    User user = response.body();
                    Log.i("Login Successful", "onResponse: " + user.toString());
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
                else {
                    new AlertDialog.Builder(MainActivity.this).setTitle("Erro!").setMessage("Login ou Senha incorretos").show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("CALL ERROR", "onFailure: " + t.getMessage(), t);
                new AlertDialog.Builder(MainActivity.this).setTitle("Erro!").setMessage(t.getMessage()).show();
            }
        });

    }
}