package com.example.cityreport.Telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityreport.Banco.Problema;
import com.example.cityreport.R;

public class CategoriaEditActivity extends AppCompatActivity {
    public static final String MODO_CRIACAO = "criacao";
    public static final String MODO_EDICAO = "edicao";

    private EditText nome,descricao;
    private Button salvar;

    //MODO, NOME, DESCRICAO, ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categoria_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nome = findViewById(R.id.editTextNomeCategoriaEdit);
        descricao = findViewById(R.id.editTextDescricaoEditCategoria);
        salvar = findViewById(R.id.button_edit_categoria);

        Bundle dados = getIntent().getExtras();
        String modo = dados.getString("MODO");

        if (modo.equals(MODO_EDICAO)){
            nome.setText(dados.getString("NOME"));
            descricao.setText(dados.getString("DESCRICAO"));
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nome.getText().toString().isEmpty()||descricao.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Por favor, preecha todos os campos",Toast.LENGTH_LONG).show();
                }else {
                    if (modo.equals(MODO_EDICAO)){
                    Problema.atualizarCategoria(getApplicationContext(),dados.getInt("ID"),nome.getText().toString(),descricao.getText().toString());
                    Intent i = new Intent(getApplicationContext(), Prefeitura_Activity.class);
                    startActivity(i);
                }else {

                    long l=    Problema.criarCategoria(getApplicationContext(),nome.getText().toString(),descricao.getText().toString());
                      //  Toast.makeText(getApplicationContext(),String.valueOf(l),Toast.LENGTH_LONG).show();

                        Intent i = new Intent(getApplicationContext(),Prefeitura_Activity.class);
                        startActivity(i);
                    }
                }
            }
        });



    }
}