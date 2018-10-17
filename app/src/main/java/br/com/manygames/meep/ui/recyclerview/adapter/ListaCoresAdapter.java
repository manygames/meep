package br.com.manygames.meep.ui.recyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import br.com.manygames.meep.R;

public class ListaCoresAdapter extends RecyclerView.Adapter<ListaCoresAdapter.CorViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(int cor, int position);
    }

    private ArrayList<Integer> cores;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ListaCoresAdapter(Context context) {
        this.cores = criaListaDeCores();
        this.context = context;
    }

    private ArrayList<Integer> criaListaDeCores() {
        ArrayList<Integer> cores = new ArrayList();
        cores.add(Color.GREEN);
        cores.add(Color.WHITE);
        cores.add(Color.RED);
        cores.add(Color.BLUE);
        cores.add(Color.YELLOW);
        cores.add(Color.MAGENTA);
        cores.add(Color.GRAY);
        cores.add(Color.CYAN);
        cores.add(Color.parseColor("#F1CBFF"));
        cores.add(Color.parseColor("#A47C48"));
        cores.add(Color.parseColor("#BE29EC"));
        return cores;
    }

    @Override
    public ListaCoresAdapter.CorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_cor, parent, false);
        return new CorViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaCoresAdapter.CorViewHolder holder, int position) {
        int cor = cores.get(position);
        holder.vincula(cor);
    }

    @Override
    public int getItemCount() {
        return cores.size();
    }

    class CorViewHolder extends RecyclerView.ViewHolder {

        private final ImageView campoCirculo;
        private int cor;

        public CorViewHolder(View itemView) {
            super(itemView);
            campoCirculo = itemView.findViewById(R.id.item_cor_circulo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(cor, getAdapterPosition());
                }
            });
        }

        public void vincula(int cor){
            this.cor = cor;
            GradientDrawable background = (GradientDrawable) campoCirculo.getBackground();
            background.setColor(cor);
        }
    }
}
