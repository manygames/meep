package br.com.manygames.meep.ui.activity;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.manygames.meep.R;
import br.com.manygames.meep.preferences.Layouts;
import br.com.manygames.meep.preferences.NotasPreferences;
import br.com.manygames.meep.ui.activity.dao.NotaDAO;
import br.com.manygames.meep.ui.activity.dao.NotaDatabase;
import br.com.manygames.meep.ui.activity.dao.OldNotaDAO;
import br.com.manygames.meep.ui.activity.model.Nota;
import br.com.manygames.meep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.manygames.meep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.manygames.meep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.manygames.meep.preferences.Layouts.*;
import static br.com.manygames.meep.preferences.Layouts.LINEAR;
import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {
    public static final String TITULO_APPBAR = "Notas";
    private ListaNotasAdapter adapter;
    private RecyclerView listaNotas;
    private RecyclerView.LayoutManager layoutManager;
    private Layouts layoutAtual = LINEAR;
    private NotasPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITULO_APPBAR);
        setContentView(R.layout.activity_lista_notas);

        preferences = new NotasPreferences(this);

/*        NotaDAO dao = NotaDatabase.getNotaDatabase(this).notaDAO();
        long[] idsCriados = dao.insere(new Nota[]{
                new Nota("Weber", "2", Color.WHITE, 0),
                new Nota("Heitor", "3", Color.WHITE, 1),
                new Nota("Joice", "5", Color.WHITE, 2)});

        for(int i = 0; i < todasNotas.size(); i++){
            todasNotas.get(i).setId(idsCriados[i]);
        }*/

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraInsereNota();
    }

    private void configuraInsereNota() {
        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormNotasActivityInsere();
            }
        });
    }

    private void vaiParaFormNotasActivityInsere() {
        Intent vaiParaForm = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(vaiParaForm, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = NotaDatabase.getNotaDatabase(this).notaDAO();
        return dao.todas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (ehUmResultadoInsereNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }
        }

        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaAlterada = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                if (ehPosicaoValida(notaAlterada.getPosicao())) {
                    altera(notaAlterada);
                } else {
                    Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void altera(Nota nota) {
        NotaDatabase.getNotaDatabase(this).notaDAO().altera(nota);
        adapter.altera(nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode, CODIGO_REQUISICAO_ALTERA_NOTA) && temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode, int codigoRequisicaoAlteraNota) {
        return requestCode == codigoRequisicaoAlteraNota;
    }

    private void adiciona(Nota notaRecebida) {
        long[] idsCriados = NotaDatabase.getNotaDatabase(this).notaDAO().insere(notaRecebida);
        notaRecebida.setId(idsCriados[0]);
        adapter.adiciona(notaRecebida);
    }

    private boolean ehUmResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) && temNota(data);
    }

    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        listaNotas = findViewById(R.id.lista_notas_recycler_view);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(this, adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormNotaActivityAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormNotaActivityAltera(Nota nota, int posicao) {
        Intent editaFormulario = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        editaFormulario.putExtra(CHAVE_NOTA, nota);
        editaFormulario.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(editaFormulario, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_notas_muda_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void configuraLayoutManager() {
        listaNotas.setLayoutManager(layoutManager);
        adapter.trocaLayout(0, adapter.quantidadeNotas() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        trocaLayout(item);
        configuraLayoutManager();
        return super.onOptionsItemSelected(item);
    }

    private void trocaLayout(MenuItem item) {
        switch (layoutAtual) {
            case LINEAR:
                item.setIcon(R.drawable.ic_action_botao_alterna_linear);
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                layoutAtual = STAGGEREDGRID;
                break;
            case STAGGEREDGRID:
                item.setIcon(R.drawable.ic_action_botao_alterna_staggeredgrid);
                layoutManager = new LinearLayoutManager(this);
                layoutAtual = LINEAR;
                break;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        carregaUltimoLayoutSalvo(menu);
        configuraLayoutManager();
        return super.onPrepareOptionsMenu(menu);
    }

    private void carregaUltimoLayoutSalvo(Menu menu) {
        layoutAtual = getEnum(preferences.pegaLayout() - 1);
        MenuItem item = menu.findItem(R.id.menu_lista_notas_layouts);
        switch (layoutAtual) {
            case LINEAR:
                item.setIcon(R.drawable.ic_action_botao_alterna_staggeredgrid);
                layoutManager = new LinearLayoutManager(this);
                break;
            case STAGGEREDGRID:
                item.setIcon(R.drawable.ic_action_botao_alterna_linear);
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        preferences.salvaLayout(layoutAtual);
        super.onDestroy();
    }
}
