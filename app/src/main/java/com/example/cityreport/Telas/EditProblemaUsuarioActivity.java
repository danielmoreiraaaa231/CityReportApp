package com.example.cityreport.Telas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityreport.Banco.ModelProblema;
import com.example.cityreport.Banco.Problema;
import com.example.cityreport.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EditProblemaUsuarioActivity extends AppCompatActivity {
    private EditText editTextDescricaoProblema;
    private Spinner spinnerCategoriaProblema;
    private Button salvarProblema, buttonFoto,verSalvos;
    private Uri foto;
    private Bitmap fotoBitmap;
    private String categoria = "";
    private Double latitude, longitude;
    private ModelProblema mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        salvarProblema = findViewById(R.id.button_salvar_problema);
        buttonFoto = findViewById(R.id.button_foto);
        spinnerCategoriaProblema = findViewById(R.id.spinnerCategoriaProblema);
        editTextDescricaoProblema = findViewById(R.id.editTextDescricaoProblema);
        verSalvos = findViewById(R.id.button_ver_salvos);
        verSalvos.setText("APAGAR PROBLEMA");

        Bundle dados = getIntent().getExtras();
        mp = dados.getParcelable("PROBLEMA");


        verSalvos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Problema.deletarProblema(getApplicationContext(),mp.getId());
                Intent intent = new Intent(getApplicationContext(), ListaProblemasUsuarioActivity.class);
                startActivity(intent);
            }
        });




        List<String> dadosSpinner = Problema.obterNomesCategorias(getApplicationContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dadosSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoriaProblema.setAdapter(adapter);

        spinnerCategoriaProblema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                categoria = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada selecionado
            }
        });

        editTextDescricaoProblema.setText(mp.getDescricao());
        int indiceSpinner = 0;
        for (int i=0;i<dadosSpinner.size();i++){
            if (dadosSpinner.get(i).equals(mp.getCategoria())){
                indiceSpinner = i;
            }
        }
        spinnerCategoriaProblema.setSelection(indiceSpinner);



        File photoFile = new File(getExternalFilesDir(null), UUID.randomUUID().toString()+".jpg");

        // Usa o FileProvider para gerar um URI seguro
        foto = FileProvider.getUriForFile(this, "com.example.cityreport", photoFile);

        // Registra o ActivityResultLauncher para tirar uma foto
        ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result != null && result) {
                            try {
                                // Carrega a imagem a partir do URI
                                Bitmap imageBitmap = loadImageFromUri(foto);
                                fotoBitmap = imageBitmap;
                                buttonFoto.setText("(FOTO TIRADA)");
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(), "Falha ao carregar a imagem", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Falha ao tirar foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture.launch(foto);
            }
        });

        salvarProblema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((editTextDescricaoProblema.getText().toString().isEmpty())||(foto==null)){
                    Toast.makeText(getApplicationContext(),"Por favor, preencha a descrição e tire uma foto",Toast.LENGTH_LONG).show();
                }else {
                 mp.setCategoria(categoria);
                 mp.setDescricao(editTextDescricaoProblema.getText().toString());
                 mp.setFoto(foto);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date currentDate = new Date();
                 mp.setDataHora(sdf.format(currentDate));

                 Problema.atualizarProblema(getApplicationContext(),mp.getId(),mp.getCategoria(),mp.getDataHora(),mp.getDescricao(),mp.getFoto());

                 Intent intent = new Intent(getApplicationContext(),ListaProblemasUsuarioActivity.class);
                 startActivity(intent);


                }
            }
        });


    }

    private Bitmap loadImageFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);

        if (inputStream != null) {
            return BitmapFactory.decodeStream(inputStream);
        } else {
            throw new FileNotFoundException("Arquivo não encontrado: " + uri);
        }
    }

}