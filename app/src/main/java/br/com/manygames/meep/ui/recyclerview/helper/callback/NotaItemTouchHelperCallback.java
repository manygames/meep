package br.com.manygames.meep.ui.recyclerview.helper.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int marcacoes = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(0, marcacoes);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
