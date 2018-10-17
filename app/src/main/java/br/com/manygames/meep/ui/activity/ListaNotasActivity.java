package br.com.manygames.meep.ui.activity;

import android.app.Activity;
import android.content.Intent;
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
import br.com.manygames.meep.ui.activity.model.Nota;
import br.com.manygames.meep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.manygames.meep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.manygames.meep.preferences.Layouts.LINEAR;
import static br.com.manygames.meep.preferences.Layouts.STAGGEREDGRID;
import static br.com.manygames.meep.preferences.Layouts.getEnum;
import static br.com.manygames.meep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
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
        List<Nota> notas = dao.todas();
        return notas;
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

    private void adiciona(Nota notaRecebida) {
        NotaDatabase.getNotaDatabase(this).notaDAO().atualizaPosicoesAposInserir();
        long[] idsCriados = NotaDatabase.getNotaDatabase(this).notaDAO().insere(notaRecebida);
        notaRecebida.setId(idsCriados[0]);
        adapter.adiciona(notaRecebida);
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

    private void configuraAdapter(List<Nota> todasNotas, final RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);

        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListaNotasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota) {
                vaiParaFormNotaActivityAltera(nota);
            }
        });
    }

    private void vaiParaFormNotaActivityAltera(Nota nota) {
        Intent editaFormulario = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        editaFormulario.putExtra(CHAVE_NOTA, nota);
        startActivityForResult(editaFormulario, CODIGO_REQUISICAO_ALTERA_NOTA);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_notas_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_lista_notas_muda_layout:
                trocaLayout(item);
                configuraLayoutManager();
                break;
            case R.id.menu_lista_notas_feedback:
                Intent intent = new Intent(ListaNotasActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void trocaLayout(MenuItem item) {
        switch (layoutAtual) {
            case LINEAR:
                mudaParaStaggeredGrid(item);
                break;
            case STAGGEREDGRID:
                mudaParaLinear(item);
                break;
        }
    }

    private void mudaParaLinear(MenuItem item) {
        alteraIconePara(item, R.drawable.ic_action_botao_staggeredgrid);
        layoutManager = new LinearLayoutManager(this);
        layoutAtual = LINEAR;
    }

    private void mudaParaStaggeredGrid(MenuItem item) {
        alteraIconePara(item, R.drawable.ic_action_botao_linear);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutAtual = STAGGEREDGRID;
    }

    private void configuraLayoutManager() {
        listaNotas.setLayoutManager(layoutManager);
        adapter.notificaTrocaDeLayout(0, adapter.quantidadeNotas() - 1);
    }

    private void alteraIconePara(MenuItem item, int novoIcone){
        item.setIcon(novoIcone);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        carregaUltimoLayoutSalvo(menu);
        configuraLayoutManager();
        return super.onPrepareOptionsMenu(menu);
    }

    private void carregaUltimoLayoutSalvo(Menu menu) {
        layoutAtual = getEnum(preferences.pegaLayout() - 1);
        MenuItem item = menu.findItem(R.id.menu_lista_notas_muda_layout);
        switch (layoutAtual) {
            case LINEAR:
                item.setIcon(R.drawable.ic_action_botao_staggeredgrid);
                layoutManager = new LinearLayoutManager(this);
                break;
            case STAGGEREDGRID:
                item.setIcon(R.drawable.ic_action_botao_linear);
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
