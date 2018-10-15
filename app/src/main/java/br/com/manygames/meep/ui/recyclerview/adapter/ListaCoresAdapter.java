package br.com.manygames.meep.ui.recyclerview.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

    public ListaCoresAdapter(Context context, ArrayList<Integer> cores) {
        this.cores = cores;
        this.context = context;
    }

    @Override
    public ListaCoresAdapter.CorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_cor, parent, false);
        return new CorViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaCoresAdapter.CorViewHolder holder, int position) {
        int cor = cores.get(position);
        //String cor = Cor.pegaHash(position);
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
