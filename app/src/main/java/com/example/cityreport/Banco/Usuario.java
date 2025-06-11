package com.example.cityreport.Banco;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
  public static final String SP = "sp";
  public static final String SP_NOME = "sp_nome";
  public static final String SP_SENHA = "sp_senha";
  public static final String SP_EMAIL = "sp_email";


    public static void criarUsuario(Context context, String nome, String email, String senha){

        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseBanco.COLUNA_NOME,nome);
        values.put(DatabaseBanco.COLUNA_EMAIL,email);
        values.put(DatabaseBanco.COLUNA_SENHA,senha);

        database.insert(DatabaseBanco.TABELA_USUARIOS,null,values);

        SharedPreferences sp = context.getSharedPreferences(SP,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_EMAIL,email);
        editor.putString(SP_SENHA,senha);
        editor.putString(SP_NOME,nome);
        editor.apply();

    }

    public static boolean checarUsuario(Context context, String email, String senha){

        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT nome FROM usuarios WHERE email = ? AND senha = ?",new String[]{email,senha});
        while (c.moveToFirst()){
            SharedPreferences sp = context.getSharedPreferences(SP,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(SP_EMAIL,email);
            editor.putString(SP_SENHA,senha);
            editor.putString(SP_NOME,c.getString(0));
            editor.apply();
            return true;
        }

        return false;
    }

    public static List<String> obterLogado(Context context){

        SharedPreferences sp = context.getSharedPreferences(SP,MODE_PRIVATE);
        List<String> s = new ArrayList<>();
        s.add(sp.getString(SP_NOME,""));
        s.add(sp.getString(SP_EMAIL,""));
        return s;
    }

    public static int obterIdUsuario(Context context,String email){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT id FROM usuarios WHERE email = ?",new String[]{email});
        while (c.moveToFirst()){
            return c.getInt(0);
        }
        return -1;
    }

}
