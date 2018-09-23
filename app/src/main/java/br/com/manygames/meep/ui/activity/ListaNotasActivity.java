package br.com.manygames.meep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import br.com.manygames.meep.R;
import br.com.manygames.meep.ui.activity.dao.NotaDAO;
import br.com.manygames.meep.ui.activity.model.Nota;
import br.com.manygames.meep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;
    private List<Nota> todasNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        todasNotas = notasDeExemplo();
        configuraRecyclerView(todasNotas);

        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vaiParaForm = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                startActivityForResult(vaiParaForm, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == 2 && data.hasExtra("nota")){
            Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            new NotaDAO().insere(notaRecebida);
            adapter.adiciona(notaRecebida);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        //configuraLayoutManager(listaN7otas);
    }

//    private void configuraLayoutManager(RecyclerView listaNotas) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        listaNotas.setLayoutManager(layoutManager);
//    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }
}
