package com.example.kafkatech.clonewhatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.Conversa;
import com.example.kafkatech.clonewhatsapp.model.Grupo;
import com.example.kafkatech.clonewhatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    public List<Conversa> conversas;
    public Context context;
    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
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
        Conversa conversa = conversas.get(position);
        holder.ultimaMensagem.setText(conversa.getUltimaMensagem());

        if(conversa.getIsGrupo().equals("true")){
            Grupo grupo = conversa.getGrupo();
            holder.nome.setText(grupo.getNome());

            if(grupo.getFoto() != null){
                Uri uri = Uri.parse(grupo.getFoto());
                Glide.with(context).load(uri).into(holder.foto);
            }
            else{
                holder.foto.setImageResource(R.drawable.padrao);
            }
        }
        else{
            Usuario user = conversa.getUserExibicao();
            if(user != null){
                holder.nome.setText(user.getNome());

                if(user.getFoto() != null){
                    Uri uri = Uri.parse(user.getFoto());
                    Glide.with(context).load(uri).into(holder.foto);
                }
                else{
                    holder.foto.setImageResource(R.drawable.padrao);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView foto;
        public TextView nome, ultimaMensagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageContatos);
            nome = itemView.findViewById(R.id.textNameContatos);
            ultimaMensagem = itemView.findViewById(R.id.textMsgContatos);
        }
    }
}
