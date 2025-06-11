package com.example.cityreport.Banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseBanco extends SQLiteOpenHelper {

    public final static String DATABASE = "database";

    public final static String TABELA_USUARIOS = "usuarios";
    public final static String TABELA_PROBLEMAS = "problemas";
    public final static String TABELA_CATEGORIAS = "categorias";

    public final static String COLUNA_ID = "id";

    public final static String COLUNA_NOME = "nome";
    public final static String COLUNA_EMAIL = "email";

    public final static String COLUNA_SENHA = "senha";
    public final static String COLUNA_DESCRICAO = "descricao";

    public final static String COLUNA_CATEGORIA_ID= "categoria_id";

    public final static String COLUNA_USUARIO_ID= "usuario_id";

    public final static String COLUNA_FOTO= "foto";

    public final static String COLUNA_LATITUDE = "latitude";

    public final static String COLUNA_LONGITUDE = "longitude";

    public final static String COLUNA_DATA_HORA = "data_hora";
    public final static String COLUNA_STATUS = "status";

    private static final int VERSAO = 1;





    public DatabaseBanco(@Nullable Context context) {
        super(context, DATABASE, null,VERSAO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_USUARIOS + " (id INTEGER PRIMARY KEY AUTOINCREMENT,nome TEXT, email TEXT, senha TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_CATEGORIAS + " (id INTEGER PRIMARY KEY AUTOINCREMENT,nome TEXT,descricao TEXT,UNIQUE (nome))");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELA_PROBLEMAS + " (id INTEGER PRIMARY KEY AUTOINCREMENT,categoria_id INTEGER, usuario_id INTEGER, descricao TEXT,foto BLOB,latitude REAL, longitude REAL,data_hora TEXT, status TEXT, FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON UPDATE CASCADE ON DELETE CASCADE,FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON UPDATE CASCADE ON DELETE CASCADE)");
        db.execSQL("INSERT INTO " + TABELA_CATEGORIAS + " (nome,descricao) VALUES (\"Saneamento básico\",\"Problemas de saneamento básico nas ruas\")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}
