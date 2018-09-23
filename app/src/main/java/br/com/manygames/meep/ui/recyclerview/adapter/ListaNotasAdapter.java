package br.com.manygames.meep.ui.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.manygames.meep.R;
import br.com.manygames.meep.ui.activity.model.Nota;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final List<Nota> notas;
    private final Context context;
    private int qtdViewHolder = 0;

    public ListaNotasAdapter(Context context, List<Nota> notas){
        this.context = context;
        this.notas = notas;
    }

    @Override
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        qtdViewHolder++;
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        Log.i("recycerview", "qtd viewholder " + qtdViewHolder);
        return new NotaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaNotasAdapter.NotaViewHolder notaViewHolder, int position) {
        Nota nota = notas.get(position);
        notaViewHolder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void adiciona(Nota nota){
        notas.add(nota);
        notifyDataSetChanged();
    }

    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;

        public NotaViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
        }

        public void vincula(Nota nota) {
            preencheCampos(nota);
        }

        private void preencheCampos(Nota nota) {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
        }
    }
}
