package br.com.manygames.meep.ui.recyclerview.helper.callback;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import br.com.manygames.meep.ui.activity.dao.NotaDAO;
import br.com.manygames.meep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter) {
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
        int posicaoFinal = target.getAdapterPosition();
        int posicaoInicial = viewHolder.getAdapterPosition();
        trocaNotas(posicaoInicial, posicaoFinal);
        return true;
    }

    private void trocaNotas(int posicaoInicial, int posicaoFinal) {
        new NotaDAO().troca(posicaoInicial, posicaoFinal);
        adapter.troca(posicaoInicial, posicaoFinal);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int posicao = viewHolder.getAdapterPosition();
        removeNota(posicao);
    }

    private void removeNota(int posicao) {
        new NotaDAO().remove(posicao);
        adapter.remove(posicao);
    }
}
