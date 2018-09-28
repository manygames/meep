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
import br.com.manygames.meep.ui.activity.dao.NotaDAO;
import br.com.manygames.meep.ui.activity.model.Nota;
import br.com.manygames.meep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.manygames.meep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.manygames.meep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

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
    private int layoutAtual = R.id.menu_lista_notas_para_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITULO_APPBAR);

        setContentView(R.layout.activity_lista_notas);
        List<Nota> todasNotas = pegaTodasNotas();
        todasNotas.add(new Nota("1","2"));
        todasNotas.add(new Nota("3","4"));
        todasNotas.add(new Nota("5","Oi, eu sou o Heitor, filho do Weber"));
        todasNotas.add(new Nota("7","Eu tenho o melhor pai do mundo! rsrsrsrsrsrsrsrsrsrsrsrsrsrsrs"));
        todasNotas.add(new Nota("10","11"));
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
        NotaDAO dao = new NotaDAO();
        return dao.todos();
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
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (ehPosicaoValida(posicaoRecebida)) {
                    altera(notaAlterada, posicaoRecebida);
                } else {
                    Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
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
        new NotaDAO().insere(notaRecebida);
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
        //Não é necessário porque foi definido no XML
        configuraLayoutManager();
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraLayoutManager() {
        switch (layoutAtual) {
            case R.id.menu_lista_notas_para_linear:
                layoutManager = new LinearLayoutManager(this);
                break;
            case R.id.menu_lista_notas_para_staggeredgrid:
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
        }
        listaNotas.setLayoutManager(layoutManager);
        adapter.trocaLayout(0, adapter.quantidadeNotas() - 1);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        invalidateOptionsMenu();
        trocaLayoutPara(item);
        return super.onOptionsItemSelected(item);
    }

    private void trocaLayoutPara(MenuItem item) {
        layoutAtual = item.getItemId();
        configuraLayoutManager();
    }

    private boolean ehTrocaParaLinear(MenuItem item) {
        return R.id.menu_lista_notas_para_linear == item.getItemId();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (layoutAtual){
            case R.id.menu_lista_notas_para_linear:
                menu.findItem(R.id.menu_lista_notas_para_linear).setVisible(false);
                break;
            case R.id.menu_lista_notas_para_staggeredgrid:
                menu.findItem(R.id.menu_lista_notas_para_staggeredgrid).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
