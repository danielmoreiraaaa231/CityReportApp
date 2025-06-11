package com.example.cityreport.Telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityreport.Apoio.AdapterCategorias;
import com.example.cityreport.Banco.Problema;
import com.example.cityreport.R;

public class Prefeitura_Activity extends AppCompatActivity {
private RecyclerView recyclerView;
private Button button_gerenciar_problemas_prefeitura,button_adicionar_categoria,button_mapa_espacial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prefeitura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewCategorias);
        button_gerenciar_problemas_prefeitura = findViewById(R.id.button_gerenciar_problemas_prefeitura);
        button_adicionar_categoria = findViewById(R.id.button_adicioanar_categoria);
        button_mapa_espacial = findViewById(R.id.button_mapa_espacial_prefeitura);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.HORIZONTAL));
        AdapterCategorias adapter = new AdapterCategorias(getApplicationContext(), Problema.obterDadosCategorias(getApplicationContext()));
       // Toast.makeText(getApplicationContext(),String.valueOf(Problema.obterDadosCategorias(getApplicationContext()).size()),LENGTH_LONG).show();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        button_adicionar_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoriaEditActivity.class);
                intent.putExtra("MODO",CategoriaEditActivity.MODO_CRIACAO);
                startActivity(intent);
            }
        });

        button_gerenciar_problemas_prefeitura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListaProblemasPrefeituraActivity.class);
                startActivity(intent);
            }
        });

        button_mapa_espacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapaEspacialPrefeituraActivity.class);
                startActivity(intent);
            }
        });


    }
}