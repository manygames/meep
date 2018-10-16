package br.com.manygames.meep.ui.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.manygames.meep.R;
import br.com.manygames.meep.ui.activity.model.Nota;
import br.com.manygames.meep.ui.recyclerview.adapter.listener.OnItemClickListener;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final List<Nota> notas;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ListaNotasAdapter(Context context, List<Nota> notas){
        this.context = context;
        this.notas = notas;
    }

    public int quantidadeNotas(){
        return notas.size();
    }

    @Override
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaNotasAdapter.NotaViewHolder notaViewHolder, int position) {
        Nota nota = notas.get(position);
        //atribui posicao atual, pois ao remover uma nota as posições são alteradas. AS POSIÇÕES NÃO SÂO ESTÁTICAS!
        nota.setPosicao(position);
        notaViewHolder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void adiciona(Nota nota){
        notas.add(0, nota);
        notifyDataSetChanged();
    }

    public void altera(Nota nota) {
        notas.set(nota.getPosicao(), nota);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void remove(int posicao) {
        notas.remove(posicao);
        notifyItemRemoved(posicao);
        //necessário para que o onBind seja chamado e as novas posições sejam atribuidas corretamente.
        // sem esta chamada, as posicoes ficam mantidas após a remoção e em algum momento eu tomo um indexOutOfBounds
        notifyItemRangeChanged(posicao, notas.size());
    }

    public void troca(int posicaoInicial, int posicaoFinal) {
        Collections.swap(notas, posicaoInicial, posicaoFinal);
        notifyItemMoved(posicaoInicial, posicaoFinal);
    }

    public Nota pegaNota(int posicao){
        return notas.get(posicao);
    }

    public void trocaLayout(int inicio, int fim){
        notifyItemMoved(inicio, fim);
    }


    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;
        private final CardView cardView;
        private Nota nota;


        public void vincula(Nota nota) {
            this.nota = nota;
            preencheCampos();
        }

        private void preencheCampos() {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
            cardView.setCardBackgroundColor(nota.getCor());
        }

        public NotaViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
            cardView = itemView.findViewById(R.id.item_nota_card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(nota, getAdapterPosition());
                }
            });
        }
    }
}
