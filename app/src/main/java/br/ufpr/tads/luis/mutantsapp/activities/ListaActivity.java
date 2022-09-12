package br.ufpr.tads.luis.mutantsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufpr.tads.luis.mutantsapp.R;
import br.ufpr.tads.luis.mutantsapp.adapter.AdapterMutantes;
import br.ufpr.tads.luis.mutantsapp.adapter.RecyclerItemClickListener;
import br.ufpr.tads.luis.mutantsapp.models.Mutant;
import br.ufpr.tads.luis.mutantsapp.models.User;

public class ListaActivity extends AppCompatActivity {
    private List<Mutant> mutantList = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        //Pega o user passado pela intent
        Bundle bundle = getIntent().getExtras();
        this.user = (User) bundle.getSerializable("user");
        this.mutantList = (List<Mutant>) bundle.getSerializable("mutants");


        RecyclerView recyclerViewMutantes = findViewById(R.id.recyclerViewMutantes);

        //Monta a lista de mutantes para exibição na tela
        AdapterMutantes adapter = new AdapterMutantes(mutantList, user);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMutantes.setLayoutManager(layoutManager);
        recyclerViewMutantes.setHasFixedSize(true);
        recyclerViewMutantes.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerViewMutantes.setAdapter(adapter);
        recyclerViewMutantes.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerViewMutantes, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                Mutant mutant = mutantList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putSerializable("mutant", mutant);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                onItemClick(view, position);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


    }

}