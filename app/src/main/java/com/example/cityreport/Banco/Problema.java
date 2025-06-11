package com.example.cityreport.Banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Problema {
    public static final String STATUS_REPORTADO = "reportado";
    public static final String STATUS_EM_ANALISE = "em_analise";
    public static final String STATUS_RESOLVIDO = "resolvido";


    public static long criarCategoria(Context context,String nome, String descricao){

        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome",nome);
        values.put("descricao",descricao);

       return  database.insert(DatabaseBanco.TABELA_CATEGORIAS,null,values);

    }

    public static int obteridCategoria(Context context,String nome){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT id FROM categorias WHERE nome = ?",new String[]{nome});
        while (c.moveToFirst()){
            return c.getInt(0);
        }
        return -1;
    }

    public static String obterNomeCategoria(Context context,int id){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT nome FROM categorias WHERE id = ?",new String[]{String.valueOf(id)});
        while (c.moveToFirst()){
            return c.getString(0);
        }
        return "";
    }

    public static List<String> obterNomesCategorias(Context context){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT nome FROM categorias",null);
        List<String> s = new ArrayList<>();
        while (c.moveToNext()){
            s.add(c.getString(0));
        }
        return s;
    }

    public static int criarProblema(Context context, String emaiUsuario, String categoria,String dataHora, String descricao, Uri foto, Double latitude, Double longitude){

        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getWritableDatabase();

        //recuperar id da categoria
        int id_categoria = obteridCategoria(context,categoria);

            // converter bitmap em blob
           /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
            foto.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] image = stream.toByteArray();
*/
            // salvar o problema
            ContentValues values = new ContentValues();
            values.put(DatabaseBanco.COLUNA_CATEGORIA_ID,id_categoria);
            values.put(DatabaseBanco.COLUNA_USUARIO_ID, Usuario.obterIdUsuario(context,emaiUsuario));
            values.put(DatabaseBanco.COLUNA_DESCRICAO,descricao);
            values.put(DatabaseBanco.COLUNA_FOTO, String.valueOf(foto));
            values.put(DatabaseBanco.COLUNA_LATITUDE,latitude);Log.w("DEBUG3",String.valueOf(latitude));
            values.put(DatabaseBanco.COLUNA_LONGITUDE,longitude);
            values.put(DatabaseBanco.COLUNA_DATA_HORA,dataHora);
            values.put(DatabaseBanco.COLUNA_STATUS,STATUS_REPORTADO);

           return (int) database.insert(DatabaseBanco.TABELA_PROBLEMAS,null,values);





    }

    public static void atualizarStatusProblema(Context context,int id,String status){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseBanco.COLUNA_STATUS,status);
        database.update(DatabaseBanco.TABELA_PROBLEMAS,values,"id=?",new String[]{String.valueOf(id)});
    }
    public static void atualizarProblema(Context context, int id,String categoria,String dataHora, String descricao, Uri foto){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getWritableDatabase();

        //conversao foto
        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] image = stream.toByteArray();
*/
        //editar

        ContentValues values = new ContentValues();
        values.put(DatabaseBanco.COLUNA_CATEGORIA_ID,obteridCategoria(context,categoria));
        values.put(DatabaseBanco.COLUNA_DESCRICAO,descricao);
        values.put(DatabaseBanco.COLUNA_FOTO, String.valueOf(foto));
        values.put(DatabaseBanco.COLUNA_DATA_HORA,dataHora);
        values.put(DatabaseBanco.COLUNA_STATUS,STATUS_REPORTADO);
        database.update(DatabaseBanco.TABELA_PROBLEMAS,values,"id=?",new String[]{String.valueOf(id)});
    }

    public static void deletarProblema(Context context, int id){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getWritableDatabase();
        database.delete(DatabaseBanco.TABELA_PROBLEMAS,"id=?",new String[]{String.valueOf(id)});
    }

    public static void deletarCategoria(Context context, int id){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getWritableDatabase();
        database.delete(DatabaseBanco.TABELA_CATEGORIAS,"id=?",new String[]{String.valueOf(id)});
    }

    public static List<Categoria> obterDadosCategorias(Context context){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM categorias",null);
        List<Categoria> categoriaList = new ArrayList<>();
        while (c.moveToNext()){
            Categoria categoria = new Categoria();
            categoria.setId(c.getInt(0));
            categoria.setNome(c.getString(1));
            categoria.setDescricao(c.getString(2));
            categoriaList.add(categoria);
        }

        return categoriaList;

    }

    public static void atualizarCategoria(Context context,int id, String nome, String descricao){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome",nome);
        values.put("descricao",descricao);

        database.update(DatabaseBanco.TABELA_CATEGORIAS,values,"id=?",new String[]{String.valueOf(id)});
    }

    public static List<ModelProblema> obterProblemasporUsuario(Context context, String email){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT usuarios.nome,problemas.categoria_id,problemas.data_hora,problemas.descricao,problemas.status,problemas.foto,problemas.latitude,problemas.longitude, problemas.id FROM problemas INNER JOIN usuarios ON problemas.usuario_id = usuarios.id WHERE usuarios.id =?",new String[]{String.valueOf(Usuario.obterIdUsuario(context,email))});

        List<ModelProblema> modelProblemasList = new ArrayList<>();
        while (c.moveToNext()){
            Log.w("DEBUG4",String.valueOf(c.getDouble(6)));

            ModelProblema mp = new ModelProblema();
            mp.setNomeUsuario(c.getString(0));
            mp.setCategoria(obterNomeCategoria(context,c.getInt(1)));
            mp.setDataHora(c.getString(2));
            mp.setDescricao(c.getString(3));
            mp.setStatus(c.getString(4));
        //    byte[] image = c.getBlob(5);
            mp.setFoto(Uri.parse(c.getString(5)));
            mp.setLatitude(c.getDouble(6));
            mp.setLongitude(c.getDouble(7));
            mp.setId(c.getInt(8));

           // mp.setFoto(BitmapFactory.decodeByteArray(image, 0, image.length));

            modelProblemasList.add(mp);

        }
        return modelProblemasList;
    }

    public static List<ModelProblema> obterProblemas(Context context){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT usuarios.nome,problemas.categoria_id,problemas.data_hora,problemas.descricao,problemas.status,problemas.foto,problemas.latitude,problemas.longitude, problemas.id FROM problemas INNER JOIN usuarios ON problemas.usuario_id = usuarios.id",null);

        List<ModelProblema> modelProblemasList = new ArrayList<>();
        while (c.moveToNext()){

            ModelProblema mp = new ModelProblema();
            mp.setNomeUsuario(c.getString(0));
            mp.setCategoria(obterNomeCategoria(context,c.getInt(1)));
            mp.setDataHora(c.getString(2));
            mp.setDescricao(c.getString(3));
            mp.setStatus(c.getString(4));
            //    byte[] image = c.getBlob(5);
            mp.setFoto(Uri.parse(c.getString(5)));
            mp.setLatitude(c.getDouble(6));
            mp.setLongitude(c.getDouble(7));
            mp.setId(c.getInt(8));

            // mp.setFoto(BitmapFactory.decodeByteArray(image, 0, image.length));

            modelProblemasList.add(mp);

        }
        return modelProblemasList;
    }

    public static List<LatLngProblemas> obterLatLngroblemas(Context context){
        DatabaseBanco banco = new DatabaseBanco(context);
        SQLiteDatabase database = banco.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT descricao, latitude, longitude FROM problemas",null);

        List<LatLngProblemas> latLngProblemasList = new ArrayList<>();
        while (c.moveToNext()){

            LatLngProblemas latLngProblemas = new LatLngProblemas();
            latLngProblemas.setDescricao(c.getString(0));
            latLngProblemas.setLatitude(c.getDouble(1));
            latLngProblemas.setLongitude(c.getDouble(2));
            latLngProblemasList.add(latLngProblemas);
        }
        return latLngProblemasList;
    }

}
