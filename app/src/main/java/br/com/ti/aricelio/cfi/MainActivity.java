package br.com.ti.aricelio.cfi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.com.ti.aricelio.cfi.Adapter.FrequenciaAdapter;
import br.com.ti.aricelio.cfi.DataAccess.DBUtil;
import br.com.ti.aricelio.cfi.DataAccess.FrequenciaDAO;
import br.com.ti.aricelio.cfi.Model.Frequencia;
import br.com.ti.aricelio.libutils.Interfaces.RecyclerViewOnClickListener;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListener {

    // Variaveis
    private RecyclerView mRecyclerView;
    private List<Frequencia> mList;
    private FrequenciaAdapter mAdapter;

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
    }

    // Métood que carrega a RecyclerView............................................................
    private void loadRecyclerView() throws  Exception{
        try{
            FrequenciaDAO dao = new FrequenciaDAO(this);
            mList = dao.find(7,false, DBUtil.F_DATA);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Método OnResume..............................................................................
    @Override
    protected void onResume() {
        super.onResume();
        try{
            loadRecyclerView();
            mAdapter.notifyDataSetChanged();
        } catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Método que captura o evento de clique na RecyclerView........................................
    @Override
    public void onClickListener(View view, int i) {

    }
}
