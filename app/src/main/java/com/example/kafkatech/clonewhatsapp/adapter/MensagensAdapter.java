package com.example.kafkatech.clonewhatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.helper.UsuarioFirebase;
import com.example.kafkatech.clonewhatsapp.model.Mensagem;

import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MensagensAdapter(List<Mensagem> lista, Context c){
        this.mensagens = lista;
        this.context = c;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = null;
        if(viewType == TIPO_REMETENTE){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente, parent, false);
        }
        else if(viewType == TIPO_DESTINATARIO){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario, parent, false);
        }
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String imagem = mensagem.getFoto();
        if(imagem != null){
            Uri url = Uri.parse(imagem);
            Glide.with(context).load(url).into(holder.imagem);

            String nome = mensagem.getNome();
            if(!nome.isEmpty()){
                holder.nome.setText(nome);
            }
            else{
                holder.nome.setVisibility(View.GONE);
            }

            //Esconder o texto
            holder.mensagem.setVisibility(View.GONE);
        }
        else{
            String nome = mensagem.getNome();
            if(!nome.isEmpty()){
                holder.nome.setText(nome);
            }
            else{
                holder.nome.setVisibility(View.GONE);
            }
            holder.mensagem.setText(msg);
            //Esconder a imagem
            holder.imagem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);
        String idUser = UsuarioFirebase.getidUsuario();

        if(idUser.equals(mensagem.getIdUser())){
            return TIPO_REMETENTE;
        }
        else{
            return TIPO_DESTINATARIO;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mensagem;
        private ImageView imagem;
        private TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mensagem = itemView.findViewById(R.id.textMensagemTexto);
            imagem = itemView.findViewById(R.id.imageMensagemFoto);
            nome = itemView.findViewById(R.id.textNomeExibicao);
        }
    }
}
