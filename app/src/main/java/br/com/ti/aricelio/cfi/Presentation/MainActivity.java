package br.com.ti.aricelio.cfi.Presentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ti.aricelio.cfi.Adapter.FrequenciaAdapter;
import br.com.ti.aricelio.cfi.DataAccess.DBUtil;
import br.com.ti.aricelio.cfi.DataAccess.FrequenciaDAO;
import br.com.ti.aricelio.cfi.Enum.EnumFiltro;
import br.com.ti.aricelio.cfi.Model.Frequencia;
import br.com.ti.aricelio.cfi.Model.SobreActivity;
import br.com.ti.aricelio.cfi.R;
import br.com.ti.aricelio.libutils.Interfaces.RecyclerViewOnClickListener;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListener {

    // Variaveis
    private RecyclerView mRecyclerView;
    private List<Frequencia> mList;
    private FrequenciaAdapter mAdapter;

    // Método onCreate..............................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        // FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsereFrequenciaActivity.class);
                startActivity(intent);
            }
        });

        // RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_frequencia);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        // SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(DBUtil.PREFERENCES, MODE_PRIVATE).edit();
        editor.putBoolean("modo", false);
        editor.apply();
    }

    // Métood que carrega a RecyclerView............................................................
    private void loadRecyclerView(List<Frequencia> list) throws  Exception{
        try{
            FrequenciaDAO dao = new FrequenciaDAO(this);
            //mList = dao.find(7,true, DBUtil.F_DATA, "Data");
            mList = list;

            mAdapter = new FrequenciaAdapter(this, mList);
            mAdapter.setRecyclerViewOnClickListener(this);

            // RecyclerView
            mRecyclerView.setAdapter(mAdapter);
        } catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Método OnCreateOptionsMenu...................................................................
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Método OnOptionsItemSelected.................................................................
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_about){
            Intent i = new Intent(this, SobreActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_filtro){
            createFilterDialog();
        }
        else if(id == R.id.action_grafico){
            Toast.makeText(this,"Funcionalidade ainda não implementada!",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // Método OnResume..............................................................................
    @Override
    protected void onResume() {
        super.onResume();
        try{
            loadRecyclerView(getAllList());
            mAdapter.notifyDataSetChanged();

        } catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Load List....................................................................................
    private List<Frequencia> getAllList(){
        List<Frequencia> list = new ArrayList<>();

        try {
            FrequenciaDAO dao = new FrequenciaDAO(this);
            //mList = dao.find(7,true, DBUtil.F_DATA, "Data");
            list = dao.find(7,true, DBUtil.F_DATA, "Data");

        } catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return list;
    }

    // Método que captura o evento de clique na RecyclerView........................................
    @Override
    public void onClickListener(View view, int i) { }

    // Método que criar a caixa de diálogo para o filtro............................................
    private void createFilterDialog(){

        final FrequenciaDAO dao = new FrequenciaDAO(this);

        // Cria o Alert
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_filtro,null);
        builder.setView(dialogView);
        builder.setTitle("Filtrar");

        // Botão OK
        builder.setPositiveButton("Ok",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    try {

                        // Radiogroup
                        RadioGroup rdgFiltro = (RadioGroup) dialogView.findViewById(R.id.rdgfiltro);

                        EnumFiltro tmpEnum = null;
                        int selectedFiltro = rdgFiltro.getCheckedRadioButtonId();

                        switch (selectedFiltro){
                            case R.id.radio_filtro_todas:
                                tmpEnum = EnumFiltro.TODAS;
                                break;
                            case R.id.radio_filtro_ultimos_7_dias:
                                tmpEnum = EnumFiltro.ULTIMOS_7_DIAS;
                                break;
                            case R.id.radio_filtro_ultimos_mes:
                                tmpEnum = EnumFiltro.ULTIMOS_MES;
                                break;
                            case R.id.radio_filtro_ebd:
                                tmpEnum = EnumFiltro.EBD;
                                break;
                            case R.id.radio_filtro_senhoras:
                                tmpEnum = EnumFiltro.SENHORAS;
                                break;
                            case R.id.radio_filtro_glorificacao:
                                tmpEnum = EnumFiltro.GLORIFICACAO;
                                break;
                            case R.id.radio_filtro_madrugada:
                                tmpEnum = EnumFiltro.MADRUGADA;
                                break;
                        }

                        // Carrega a nova lista
                        List<Frequencia> list = dao.findWithFilter(tmpEnum);
                        loadRecyclerView(list);

                    } catch(Exception e){}
                }
            }
        );

        // Botão Cancelar
        builder.setNegativeButton("Cancelar",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { }
            }
        );
        alert = builder.create();
        alert.show();
    }
}
