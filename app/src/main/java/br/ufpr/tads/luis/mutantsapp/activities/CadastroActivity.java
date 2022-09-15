package br.ufpr.tads.luis.mutantsapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.util.ArrayList;

import br.ufpr.tads.luis.mutantsapp.R;
import br.ufpr.tads.luis.mutantsapp.controllers.RetrofitConfig;
import br.ufpr.tads.luis.mutantsapp.models.Mutant;
import br.ufpr.tads.luis.mutantsapp.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {
    EditText editTextNomeMutante, editTextHabilidade1, editTextHabilidade2, editTextHabilidade3;
    ImageView imageViewFotoMutante;
    User user;

    private Uri selectedImage;
    String part_image;

    // Permissões para acessar o storage
    private static final int PICK_IMAGE_REQUEST = 1307;
    private static final int TAKE_PHOTO_REQUEST = 0304;

    private static final int REQUEST_PERMISSIONS = 0402;
    private static final String[] PERMISSIONS_TO_REQUEST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");

        editTextNomeMutante = findViewById(R.id.editTextCadastroNome);
        editTextHabilidade1 = findViewById(R.id.editTextCadastroHabilidade1);
        editTextHabilidade2 = findViewById(R.id.editTextCadastroHabilidade2);
        editTextHabilidade3 = findViewById(R.id.editTextCadastroHabilidade3);
        imageViewFotoMutante = findViewById(R.id.imageViewCadastroFoto);

        imageViewFotoMutante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent, "Abrir galeria"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void selectImage() {
        verifyPermissions(CadastroActivity.this);
        final CharSequence[] options = {"Tirar Foto", "Escolher da Galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
        builder.setTitle("Adicionar foto!");
        builder.setItems(options, (dialog, which) -> {
            if ("Tirar Foto".equals(options[which])) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(cameraIntent, TAKE_PHOTO_REQUEST);
            } else if ("Escolher da Galeria".equals(options[which])) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            } else if ("Cancelar".equals(options[which])) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    // Pega o caminho absoluto da imagem selecionada a partir da sua URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
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
                    Log.i("TESTE", "onActivityResult: " + part_image);
                    cursor.close();
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {

                }
                imageViewFotoMutante.setImageBitmap(bitmap);
            } else if (requestCode == TAKE_PHOTO_REQUEST) {

                File foto = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : foto.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        Log.i("FOTO CAMERA", "onActivityResult 0: " + temp.getName());
                        foto = temp;
                        break;
                    }
                }
                try {
                    Log.i("FOTO CAMERA", "onActivityResult 1: " + foto.getAbsolutePath());
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(foto.getAbsolutePath(),
                            bitmapOptions);
                    imageViewFotoMutante.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Foto" + File.separator + "Mutant";
                    String filename = System.currentTimeMillis() + ".jpg";
                    Log.i("FOTO CAMERA", "onActivityResult 2: " + filename);

                    part_image = foto.getAbsolutePath();
                    Log.i("FOTO CAMERA", "onActivityResult 3: " + part_image);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public static void verifyPermissions(Activity activity) {
        // Verifica se temos permissão de escrita
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if ((permission != PackageManager.PERMISSION_GRANTED) || (cameraPermission != PackageManager.PERMISSION_GRANTED)) {
            // Como não temos permissão, requisita ao usuário
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_TO_REQUEST,
                    REQUEST_PERMISSIONS
            );
        }
    }

    public void salvarMutante(View view) {
        if (editTextNomeMutante.length() == 0) {
            Toast.makeText(this, "Nome não pode ser vazio!", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((editTextHabilidade1.length() == 0) && (editTextHabilidade2.length() == 0) && (editTextHabilidade3.length() == 0)) {
            Toast.makeText(this, "Informe ao menos uma habilidade!", Toast.LENGTH_SHORT).show();
            return;
        }
        File foto = new File(part_image);

        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", foto.getName(), RequestBody.create(MediaType.parse("image/*"), foto));
        MultipartBody.Part partName = MultipartBody.Part.createFormData("name", editTextNomeMutante.getText().toString());

        ArrayList<RequestBody> habilidades = new ArrayList<>();
        habilidades.add(RequestBody.create(MediaType.parse("text"), editTextHabilidade1.getText().toString()));
        habilidades.add(RequestBody.create(MediaType.parse("text"), editTextHabilidade2.getText().toString()));
        habilidades.add(RequestBody.create(MediaType.parse("text"), editTextHabilidade3.getText().toString()));

        Call<Mutant> updateMutanteCall = new RetrofitConfig().getMutantsService().createMutant(this.user.getTokens().getAccessToken(), partImage, partName, habilidades);
        updateMutanteCall.enqueue(new Callback<Mutant>() {
            @Override
            public void onResponse(Call<Mutant> call, Response<Mutant> response) {
                if (response.isSuccessful()) {
                    new AlertDialog.Builder(CadastroActivity.this)
                            .setTitle("Sucesso")
                            .setMessage("Novo mutante salvo com sucesso")
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    recarregaDashboard();
                                }
                            })
                            .show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        new AlertDialog.Builder(CadastroActivity.this).setTitle("Erro").setMessage(jObjError.getString("error")).show();
                    } catch (JSONException | IOException e) {
                        new AlertDialog.Builder(CadastroActivity.this).setTitle("Erro").setMessage("Erro interno.").show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Mutant> call, Throwable t) {
                Log.e("ERRO", "Create Mutante: " + t.getMessage());
                new AlertDialog.Builder(CadastroActivity.this).setTitle("Erro interno").setMessage("Erro ao conectar ao servidor.").show();
            }
        });
    }

    private void recarregaDashboard() {
        Intent intent = new Intent(CadastroActivity.this, DashboardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}