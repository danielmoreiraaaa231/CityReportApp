package com.example.cityreport.Telas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityreport.Apoio.AdapterProblemas;
import com.example.cityreport.Banco.Problema;
import com.example.cityreport.R;
import com.example.cityreport.Banco.Usuario;

public class ListaProblemasUsuarioActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_problemas_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AdapterProblemas adapter = new AdapterProblemas(getApplicationContext(),ListaProblemasUsuarioActivity.this, Problema.obterProblemasporUsuario(getApplicationContext(), Usuario.obterLogado(getApplicationContext()).get(1)),AdapterProblemas.MODO_USUARIO);
        recyclerView = findViewById(R.id.recyclerViewProblemasUsuario);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
}