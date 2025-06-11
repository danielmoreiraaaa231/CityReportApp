package com.example.cityreport.Apoio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityreport.Banco.Categoria;
import com.example.cityreport.Banco.Problema;
import com.example.cityreport.R;
import com.example.cityreport.Telas.CategoriaEditActivity;
import com.example.cityreport.Telas.Prefeitura_Activity;

import java.util.List;

public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.CategoriaViewHolder> {
    private Context context;
    private List<Categoria> categoriaList;


    public AdapterCategorias(Context context, List<Categoria> categoriaList) {
        this.context = context;
        this.categoriaList = categoriaList;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterCategorias.CategoriaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter_categorias,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        holder.nome.setText(categoriaList.get(position).getNome());
        holder.descricao.setText(categoriaList.get(position).getDescricao());

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Problema.deletarProblema(context,categoriaList.get(position).getId());
                Intent i = new Intent(context, Prefeitura_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                return false;
            }
        });

         holder.layout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, CategoriaEditActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 intent.putExtra("MODO",CategoriaEditActivity.MODO_EDICAO);
                 intent.putExtra("NOME",categoriaList.get(position).getNome());
                 intent.putExtra("DESCRICAO",categoriaList.get(position).getDescricao());
                 intent.putExtra("ID",categoriaList.get(position).getId());



                 context.startActivity(intent);
             }
         });

    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public class CategoriaViewHolder extends RecyclerView.ViewHolder{
        private TextView nome,descricao;
        private LinearLayout layout;
        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textViewNomeCategoria);
            descricao = itemView.findViewById(R.id.textViewDescricaoCategoria);
            layout = itemView.findViewById(R.id.layout_linear_categoria);
        }
    }
}
