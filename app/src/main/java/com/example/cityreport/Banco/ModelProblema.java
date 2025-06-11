package com.example.cityreport.Banco;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ModelProblema implements Parcelable {
    //Context context, String emaiUsuario, String categoria,String dataHora, String descricao, Bitmap foto, Double latitude, Double longitude
 private String nomeUsuario, categoria, dataHora, descricao,status;
 private Uri foto;
 private Double latitude,longitude;
 private int id;


 public ModelProblema(){}
    protected ModelProblema(Parcel in) {
        nomeUsuario = in.readString();
        categoria = in.readString();
        dataHora = in.readString();
        descricao = in.readString();
        status = in.readString();
        foto = in.readParcelable(Uri.class.getClassLoader());
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        id = in.readInt();
    }

    public static final Creator<ModelProblema> CREATOR = new Creator<ModelProblema>() {
        @Override
        public ModelProblema createFromParcel(Parcel in) {
            return new ModelProblema(in);
        }

        @Override
        public ModelProblema[] newArray(int size) {
            return new ModelProblema[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nomeUsuario);
        dest.writeString(categoria);
        dest.writeString(dataHora);
        dest.writeString(descricao);
        dest.writeString(status);
        dest.writeParcelable(foto, flags);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeInt(id);
    }
}
