package br.com.manygames.meep.ui.recyclerview.helper.callback;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.List;

import br.com.manygames.meep.ui.activity.dao.NotaDatabase;
import br.com.manygames.meep.ui.activity.model.Nota;
import br.com.manygames.meep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private Context context;
    private final ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallback(Context context, ListaNotasAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();

        adapter.troca(posicaoInicial, posicaoFinal);

        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        trocaNotas();
    }

    private void trocaNotas() {
        Nota[] notas = adapter.pegaNotas();
        NotaDatabase.getNotaDatabase(context).notaDAO().altera(notas);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Nota nota = adapter.pegaNota(viewHolder.getAdapterPosition());
        removeNota(nota);
    }

    private void removeNota(Nota nota) {
        NotaDatabase.getNotaDatabase(context).notaDAO().remove(nota);
        NotaDatabase.getNotaDatabase(context).notaDAO().atualizaPosicoesAposRemover(nota.getPosicao());
        adapter.remove(nota.getPosicao());
    }
}
