package com.example.kafkatech.clonewhatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrupoAdapterSelecionado extends RecyclerView.Adapter<GrupoAdapterSelecionado.MyViewHolder> {

    private final List<Usuario> contatosSelecionado;
    private final Context context;
    public GrupoAdapterSelecionado(List<Usuario> listaContatos, Context c) {
        this.contatosSelecionado = listaContatos;
        this.context = c;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_grupo_selecionado, parent, false);
        return new GrupoAdapterSelecionado.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario user = contatosSelecionado.get(position);

        holder.nome.setText(user.getNome());

        if(user.getFoto() != null){
            Uri uri = Uri.parse(user.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        }
        else {
            holder.foto.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contatosSelecionado.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView foto;
        public TextView nome;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            nome = itemView.findViewById(R.id.textNameMembroSelecionado);
        }
    }
}
