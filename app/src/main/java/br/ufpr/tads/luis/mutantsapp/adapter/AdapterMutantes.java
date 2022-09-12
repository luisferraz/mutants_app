package br.ufpr.tads.luis.mutantsapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.List;

import br.ufpr.tads.luis.mutantsapp.R;
import br.ufpr.tads.luis.mutantsapp.controllers.RetrofitConfig;
import br.ufpr.tads.luis.mutantsapp.models.Mutant;
import br.ufpr.tads.luis.mutantsapp.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMutantes extends RecyclerView.Adapter<AdapterMutantes.MyViewHolder> {
    private List<Mutant> mutantList;
    private User user;

    public AdapterMutantes(List<Mutant> mutantList, User user) {
        this.mutantList = mutantList;
        this.user = user;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_mutantes, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMutantes.MyViewHolder holder, int position) {
        Mutant mutant = mutantList.get(position);
        holder.nome.setText(mutant.getName());
        Call<ResponseBody> mutantPhotoCall = new RetrofitConfig().getMutantsService().getMutantPhoto(this.user.getTokens().getAccessToken(), mutant.getId());
        mutantPhotoCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    holder.foto.setImageBitmap(image);
                    holder.foto.setAdjustViewBounds(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mutantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.foto = itemView.findViewById(R.id.imageViewFotoMutante);
            this.nome = itemView.findViewById(R.id.textViewDetalheNome);
        }
    }
}
