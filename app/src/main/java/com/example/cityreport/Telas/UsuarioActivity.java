package com.example.cityreport.Telas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityreport.Apoio.NotificationHelper;
import com.example.cityreport.Banco.Problema;
import com.example.cityreport.R;
import com.example.cityreport.Banco.Usuario;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UsuarioActivity extends AppCompatActivity {
    private EditText editTextDescricaoProblema;
    private Spinner spinnerCategoriaProblema;
    private Button salvarProblema, buttonFoto,verSalvos;
    private Uri foto;
    private Bitmap fotoBitmap;
    private String categoria = "";
    private FusedLocationProviderClient fusedLocationClient;
    private Double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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

        verSalvos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ListaProblemasUsuarioActivity.class);
                startActivity(in);
            }
        });

        obterLocalização();

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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Problema.obterNomesCategorias(getApplicationContext()));
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
                 salvarProblema();
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

    private void obterLocalização(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(UsuarioActivity.this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            // Obtém a localização do usuário

                            Location location = task.getResult();
                             latitude = location.getLatitude();
                             longitude = location.getLongitude();

                             Log.i("DEBUG2",String.valueOf(latitude));


                        } else {
                            Toast.makeText(getApplicationContext(), "Falha ao obter a localização", Toast.LENGTH_SHORT).show();

                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ERROMAPA",e.getMessage());
                    }
                });
    }

    private void salvarProblema(){
        Toast.makeText(getApplicationContext(), "Aguarde...", Toast.LENGTH_SHORT).show();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date currentDate = new Date();

        int i = Problema.criarProblema(getApplicationContext(), Usuario.obterLogado(getApplicationContext()).get(1),categoria,sdf.format(currentDate),
                editTextDescricaoProblema.getText().toString(),foto,latitude,longitude);
                NotificationHelper.showNotification(getApplicationContext(),"Novo problema relatado",editTextDescricaoProblema.getText().toString());
        if (i==-1){
            Toast.makeText(getApplicationContext(), "Falha ao salvar", Toast.LENGTH_SHORT).show();

        }else {
            Intent in = new Intent(getApplicationContext(),ListaProblemasUsuarioActivity.class);
            startActivity(in);
        }
    }
}
