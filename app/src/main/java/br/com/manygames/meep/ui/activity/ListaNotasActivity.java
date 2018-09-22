package br.com.manygames.meep.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import br.com.manygames.meep.R;
import br.com.manygames.meep.ui.activity.dao.NotaDAO;
import br.com.manygames.meep.ui.activity.model.Nota;
import br.com.manygames.meep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        List<Nota> todasNotas = notasDeExemplo();
        configuraRecyclerView(todasNotas);
    }

    private List<Nota> notasDeExemplo() {
        NotaDAO dao = new NotaDAO();
        dao.insere(new Nota("Primeira","Pequena"));
        dao.insere(new Nota("Segunda","Segunda descrição é bem maior que a da primeira nota"));
        return dao.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycler_view);
        configuraAdapter(todasNotas, listaNotas);

        //Não é necessário porque foi definido no XML
        //configuraLayoutManager(listaNotas);
    }

//    private void configuraLayoutManager(RecyclerView listaNotas) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        listaNotas.setLayoutManager(layoutManager);
//    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNotas));
    }
}
