package com.example.cityreport.Telas;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cityreport.R;
import com.example.cityreport.Banco.Usuario;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private CheckBox checkBoxUsuario;
    private EditText editTextNomeUsuario,editTextEmailUsuario,editTextSenhaUsuario;
    private Button buttonUsuario;
    private boolean isCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkBoxUsuario = findViewById(R.id.checkBoxUsuario);
        buttonUsuario = findViewById(R.id.button_usuario);
        editTextNomeUsuario = findViewById(R.id.editTextNomeUsuario);
        editTextEmailUsuario = findViewById(R.id.editTextEmailUsuario);
        editTextSenhaUsuario = findViewById(R.id.editTextSenhaUsuario);

        isCadastro = false;
        checkBoxUsuario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCadastro = isChecked;
                if (isChecked){
                    editTextNomeUsuario.setVisibility(VISIBLE);
                }else {
                    editTextNomeUsuario.setVisibility(GONE);

                }
            }
        });


        buttonUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean b = false;

                if (isCadastro){

                    b = (editTextNomeUsuario.getText().toString().isEmpty()) || (editTextEmailUsuario.getText().toString().isEmpty()) || (editTextSenhaUsuario.getText().toString().isEmpty());

                }else {
                    b = (editTextEmailUsuario.getText().toString().isEmpty()) || (editTextSenhaUsuario.getText().toString().isEmpty());
                }


                if (editTextEmailUsuario.getText().toString().equals("email@prefeitura.com")){

                    if (editTextSenhaUsuario.getText().toString().equals("1234")) {
                        Intent intent = new Intent(getApplicationContext(), Prefeitura_Activity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"Usuário e/ou senha incorretos",Toast.LENGTH_LONG).show();
                    }
                }else {

                  if (b){
                      Toast.makeText(getApplicationContext(),"Preencha todos os campos",Toast.LENGTH_LONG).show();
                  }else {


                      if (isCadastro){

                          Usuario.criarUsuario(getApplicationContext(),editTextNomeUsuario.getText().toString(),editTextEmailUsuario.getText().toString(),editTextSenhaUsuario.getText().toString());
                         requestPermissions(
                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.POST_NOTIFICATIONS},
                                  0);

                      }else {
                        if (Usuario.checarUsuario(getApplicationContext(),editTextEmailUsuario.getText().toString(),editTextSenhaUsuario.getText().toString())){
                            Intent intent = new Intent(getApplicationContext(), UsuarioActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),"Usuário e/ou senha incorretos",Toast.LENGTH_LONG).show();
                        }
                      };



                  }


                }



            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent(getApplicationContext(),UsuarioActivity.class);
        startActivity(intent);
    }
}