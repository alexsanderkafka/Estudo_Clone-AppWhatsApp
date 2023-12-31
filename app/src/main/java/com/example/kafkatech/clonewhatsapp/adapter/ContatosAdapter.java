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

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

    private List<Usuario> contatos;
    private final Context context;
    public ContatosAdapter(List<Usuario> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario user = contatos.get(position);
        boolean cabecalho = user.getEmail().isEmpty();

        holder.nome.setText(user.getNome());
        holder.email.setText(user.getEmail());
        Log.i("info", "Contatos photo --> " + user.getEmail());
        if(user.getFoto() != null){
            Uri uri = Uri.parse(user.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        }
        else if(cabecalho){
            holder.foto.setImageResource(R.drawable.icone_grupo);
            holder.email.setVisibility(View.GONE);
        }
        else{
            holder.foto.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView foto;
        public TextView nome, email;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageContatos);
            nome = itemView.findViewById(R.id.textNameContatos);
            email = itemView.findViewById(R.id.textMsgContatos);

        }
    }

    public List<Usuario> getContatos() {
        return this.contatos;
    }
}
