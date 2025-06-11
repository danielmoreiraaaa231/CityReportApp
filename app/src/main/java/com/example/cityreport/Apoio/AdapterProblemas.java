package com.example.cityreport.Apoio;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityreport.Banco.ModelProblema;
import com.example.cityreport.Banco.Problema;
import com.example.cityreport.R;
import com.example.cityreport.Telas.EditProblemaUsuarioActivity;
import com.example.cityreport.Telas.ListaProblemasPrefeituraActivity;
import com.example.cityreport.Telas.MapaActivity;

import java.util.List;
import java.util.Objects;

public class AdapterProblemas extends RecyclerView.Adapter<AdapterProblemas.MyViewHolder> {
    private List<ModelProblema> problemas;
    private Context context, contextDialog;

    public static final String MODO_USUARIO = "usuario";
    public static final String MODO_PREFEITURA = "prefeitura";

    private String modo;
    public AdapterProblemas(Context context,Context contextDialog,List<ModelProblema> modelProblemas,String modo) {
        this.problemas = modelProblemas;
        this.context = context;
        this.modo = modo;
        this.contextDialog = contextDialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter_problemas_usuario,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textViewUsuarioRecyclerProblema.setText(problemas.get(position).getNomeUsuario());
        holder.textDataHoraRecyclerProblema.setText(problemas.get(position).getDataHora());
        holder.textDescricaoRecyclerProblema.setText(problemas.get(position).getDescricao());
        holder.textStatusRecyclerProblema.setText(problemas.get(position).getStatus());
        holder.textCategoriaRecyclerProblema.setText(problemas.get(position).getCategoria());

        holder.imageViewRecyclerProblema.setImageURI(problemas.get(position).getFoto());

        if (problemas.get(position).getStatus().equals(Problema.STATUS_REPORTADO)){
            holder.textStatusRecyclerProblema.setTextColor(Color.GRAY);
        }else if (problemas.get(position).getStatus().equals(Problema.STATUS_EM_ANALISE)){
            holder.textStatusRecyclerProblema.setTextColor(Color.BLUE);
        }else {
            holder.textStatusRecyclerProblema.setTextColor(Color.GREEN);
        }



        if (Objects.equals(modo, MODO_USUARIO)){

            if (!problemas.get(position).getStatus().equals(Problema.STATUS_REPORTADO)) {
                holder.editar.setVisibility(View.GONE);
            }else {
                holder.editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, EditProblemaUsuarioActivity.class);
                        intent.putExtra("PROBLEMA",problemas.get(position));
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        }else {
            holder.editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     Toast.makeText(context,"jjjjjjjjjjjj",Toast.LENGTH_LONG).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(contextDialog);
                    builder.setTitle("Escolha uma opção")
                            .setMessage("Selecione uma das opções abaixo:")

                            .setPositiveButton("Tornar Problema : Em Análise", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Problema.atualizarStatusProblema(context,problemas.get(position).getId(),Problema.STATUS_EM_ANALISE);
                                    Intent intent = new Intent(context, ListaProblemasPrefeituraActivity.class);
                                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHelper.showNotification(context,"Novo problema em análise",problemas.get(position).getDescricao());
                                    context.startActivity(intent);
                                   }
                            })

                            .setNegativeButton("Tornar Problema : Resolvido", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Problema.atualizarStatusProblema(context,problemas.get(position).getId(),Problema.STATUS_RESOLVIDO);
                                    Intent intent = new Intent(context, ListaProblemasPrefeituraActivity.class);
                                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    NotificationHelper.showNotification(context,"Novo problema resolvido",problemas.get(position).getDescricao());
                                    context.startActivity(intent);
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        holder.verMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapaActivity.class);
                intent.putExtra("LATITUDE",problemas.get(position).getLatitude());
                intent.putExtra("LONGITUDE",problemas.get(position).getLongitude());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });




    }


    @Override
    public int getItemCount() {
        return problemas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textDescricaoRecyclerProblema, textStatusRecyclerProblema,textViewUsuarioRecyclerProblema,textCategoriaRecyclerProblema,textDataHoraRecyclerProblema;
        private ImageView imageViewRecyclerProblema;
        //private MapView mapViewRecyclerProblema;
        private Button editar,verMapa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textDescricaoRecyclerProblema = itemView.findViewById(R.id.textViewDescricaoProblemaRecycler);
            textStatusRecyclerProblema = itemView.findViewById(R.id.textViewStatusProblemaRecycler);
            textViewUsuarioRecyclerProblema = itemView.findViewById(R.id.textViewUsuarioRecyclerProblema);
            textCategoriaRecyclerProblema = itemView.findViewById(R.id.textCategoriaRecyclerProblema);
            textDataHoraRecyclerProblema = itemView.findViewById(R.id.textDataHoraRecyclerProblema);
            imageViewRecyclerProblema = itemView.findViewById(R.id.imageViewRecyclerProblema);
           // mapViewRecyclerProblema = itemView.findViewById(R.id.mapViewRecyclerProblema);
            editar = itemView.findViewById(R.id.buttonEditarProblema);
            verMapa = itemView.findViewById(R.id.buttonVerMapaProblema);



        }
    }


}
