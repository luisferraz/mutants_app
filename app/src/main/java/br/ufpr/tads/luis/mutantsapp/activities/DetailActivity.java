package br.ufpr.tads.luis.mutantsapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.tads.luis.mutantsapp.R;
import br.ufpr.tads.luis.mutantsapp.controllers.RetrofitConfig;
import br.ufpr.tads.luis.mutantsapp.models.Ability;
import br.ufpr.tads.luis.mutantsapp.models.Mutant;
import br.ufpr.tads.luis.mutantsapp.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    User user;
    Mutant mutant;
    ImageView imageViewFotoMutante;
    EditText editTextNomeMutante, editTextHabilidade1, editTextHabilidade2, editTextHabilidade3;

    private Uri selectedImage;
    String part_image;

    // Permissões para acessar o storage
    private static final int PICK_IMAGE_REQUEST = 1307;
    private static final int REQUEST_EXTERNAL_STORAGE = 0402;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        mutant = (Mutant) intent.getSerializableExtra("mutant");

        editTextNomeMutante = findViewById(R.id.editTextDetalheNome);
        imageViewFotoMutante = findViewById(R.id.imageViewDetalheFoto);
        editTextHabilidade1 = findViewById(R.id.editTextDetalheHabilidade1);
        editTextHabilidade2 = findViewById(R.id.editTextDetalheHabilidade2);
        editTextHabilidade3 = findViewById(R.id.editTextDetalheHabilidade3);
        editTextNomeMutante.setText(mutant.getName());

        for (Ability ability : mutant.getAbilities()) {
            if (editTextHabilidade1.length() == 0) {
                editTextHabilidade1.setText(ability.getAbility());
            } else {
                if (editTextHabilidade2.length() == 0) {
                    editTextHabilidade2.setText(ability.getAbility());
                } else {
                    if (editTextHabilidade3.length() == 0) {
                        editTextHabilidade3.setText(ability.getAbility());
                    }
                }
            }
        }

        Call<ResponseBody> mutantPhotoCall = new RetrofitConfig().getMutantsService().getMutantPhoto(this.user.getTokens().getAccessToken(), mutant.getId());
        mutantPhotoCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    imageViewFotoMutante.setImageBitmap(image);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        imageViewFotoMutante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(DetailActivity.this);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Abrir galeria"), PICK_IMAGE_REQUEST);
            }
        });
    }

    // Pega o caminho absoluto da imagem selecionada a partir da sua URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Bitmap bitmap = null;
            try {
                selectedImage = data.getData();
                Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
                cursor.moveToFirst();
                String documentId = cursor.getString(0);
                documentId = documentId.substring(documentId.lastIndexOf(":") + 1);
                cursor.close();

                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ?", new String[]{documentId}, null);
                cursor.moveToFirst();
                part_image = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.close();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {

            }
            imageViewFotoMutante.setImageBitmap(bitmap);
        }

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Verifica se temos permissão de escrita
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Como não temos permissão, requisita ao usuário
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void salvarAlteracoes(View view) {
        if (editTextNomeMutante.length() == 0) {
            Toast.makeText(this, "Nome não pode ser vazio!", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((editTextHabilidade1.length() == 0) && (editTextHabilidade2.length() == 0) && (editTextHabilidade3.length() == 0)) {
            Toast.makeText(this, "Informe ao menos uma habilidade!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (part_image == null) {
            Toast.makeText(this, "Selecione uma imagem.", Toast.LENGTH_SHORT).show();
            return;
        }
        String novoNome = editTextNomeMutante.getText().toString();
        File novaFoto = new File(part_image);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", novaFoto.getName(), RequestBody.create(MediaType.parse("image/*"), novaFoto));
        MultipartBody.Part partName = MultipartBody.Part.createFormData("name", novoNome);

        ArrayList<RequestBody> habilidades = new ArrayList<>();
        habilidades.add(RequestBody.create(MediaType.parse("text"), editTextHabilidade1.getText().toString()));
        habilidades.add(RequestBody.create(MediaType.parse("text"), editTextHabilidade2.getText().toString()));
        habilidades.add(RequestBody.create(MediaType.parse("text"), editTextHabilidade3.getText().toString()));

        Call<Mutant> updateMutanteCall = new RetrofitConfig().getMutantsService().updateMutant(this.user.getTokens().getAccessToken(), this.mutant.getId(), partImage, partName, habilidades);
        updateMutanteCall.enqueue(new Callback<Mutant>() {
            @Override
            public void onResponse(Call<Mutant> call, Response<Mutant> response) {
                if (response.isSuccessful()) {
                    recarregaLista("Mutante atualizado com sucesso!");
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new AlertDialog.Builder(DetailActivity.this).setTitle("Erro").setMessage(jObjError.getString("error")).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        new AlertDialog.Builder(DetailActivity.this).setTitle("Erro").setMessage(response.errorBody().toString()).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Mutant> call, Throwable t) {
                Log.e("ERRO", "Update Mutante: " + t.getMessage());
                new AlertDialog.Builder(DetailActivity.this).setTitle("Erro!").setMessage(t.getMessage()).show();
            }
        });
    }

    public void excluirMutante(View view) {
        Call<ResponseBody> mutantDeleteCall = new RetrofitConfig().getMutantsService().deleteMutant(this.user.getTokens().getAccessToken(), this.mutant.getId());
        mutantDeleteCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    recarregaLista("Mutante removido com sucesso!");
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new AlertDialog.Builder(DetailActivity.this).setTitle("Erro").setMessage(jObjError.getString("error")).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        new AlertDialog.Builder(DetailActivity.this).setTitle("Erro").setMessage(response.errorBody().toString()).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERRO", "Delete Mutante: " + t.getMessage());
                new AlertDialog.Builder(DetailActivity.this).setTitle("Erro!").setMessage(t.getMessage()).show();
            }
        });

    }

    private void recarregaLista(String message) {
        Call<List<Mutant>> callMutants = new RetrofitConfig().getMutantsService().getAllMutants(user.getTokens().getAccessToken());
        callMutants.enqueue(new Callback<List<Mutant>>() {
            @Override
            public void onResponse(Call<List<Mutant>> call, Response<List<Mutant>> response) {
                if (response.isSuccessful()) {
                    List<Mutant> mutantList = new ArrayList<>(response.body());
                    //Se a lista estiver vazia, vai para a dashboard
                    Intent intent;
                    if (mutantList.size() == 0) {
                        intent = new Intent(DetailActivity.this, DashboardActivity.class);
                    } else {
                        intent = new Intent(DetailActivity.this, ListaActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mutants", (Serializable) mutantList);
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    new AlertDialog.Builder(DetailActivity.this).setTitle("Sucesso").setMessage(message).show();
                    startActivity(intent);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new AlertDialog.Builder(DetailActivity.this).setTitle("Erro").setMessage(jObjError.getString("error")).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        new AlertDialog.Builder(DetailActivity.this).setTitle("Erro").setMessage(response.errorBody().toString()).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Mutant>> call, Throwable t) {
                Log.e("ERRO", "getAllMutants: " + t.getMessage());
                new AlertDialog.Builder(DetailActivity.this).setTitle("Erro!").setMessage(t.getMessage()).show();
            }
        });
    }

}