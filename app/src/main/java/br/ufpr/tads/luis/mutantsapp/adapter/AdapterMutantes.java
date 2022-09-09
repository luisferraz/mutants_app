package br.ufpr.tads.luis.mutantsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufpr.tads.luis.mutantsapp.R;
import br.ufpr.tads.luis.mutantsapp.models.Mutant;

public class AdapterMutantes extends RecyclerView.Adapter<AdapterMutantes.MyViewHolder> {
    private List<Mutant> mutantList;

    public AdapterMutantes(List<Mutant> mutantList) {
        this.mutantList = mutantList;
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
//        holder.foto.setImageBitmap();
//        holder.foto.setAdjustViewBounds(true);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.foto = itemView.findViewById(R.id.imageViewFotoMutante);
            this.nome = itemView.findViewById(R.id.textViewNomeMutante);
        }
    }
}
