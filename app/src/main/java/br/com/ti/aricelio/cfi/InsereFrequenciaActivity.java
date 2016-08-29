package br.com.ti.aricelio.cfi;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.com.ti.aricelio.cfi.DataAccess.FrequenciaDAO;
import br.com.ti.aricelio.cfi.Model.EnumTipoCulto;
import br.com.ti.aricelio.cfi.Model.Frequencia;

public class InsereFrequenciaActivity extends AppCompatActivity {

    // Variaveis
    private String obL = "", obP = "";
    boolean closeActivity = false;
    boolean checkEBD = false;

    // Método OnCreate..............................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insere_frequencia);

        // Toolbar
        getSupportActionBar().setTitle("Nova Frequencia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        // CheckBox
        CheckBox chbEBD = (CheckBox) findViewById(R.id.chbEBD);
        Calendar cal = Calendar.getInstance();

        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        if(diaSemana == Calendar.SUNDAY){
            chbEBD.setVisibility(CheckBox.VISIBLE);
        }
        else{
            chbEBD.setVisibility(CheckBox.INVISIBLE);
        }

    }

    // Método Salvar...............................................................................
    public void onClickSalvar(View v){
        // Chama a Dialog
        createDialogDirecao();
    }

    // Método add membros...........................................................................
    public void onClickAddMembro(View v){
        TextView tvQtdeMembros = (TextView) findViewById(R.id.tvQtdeMembros);
        tvQtdeMembros.setText( getStringQTDE(true,tvQtdeMembros));
        updateTotal();
    }

    // Método retira membros........................................................................
    public void onClickRetiraMembro(View v){
        TextView tvQtdeMembros = (TextView) findViewById(R.id.tvQtdeMembros);
        tvQtdeMembros.setText( getStringQTDE(false,tvQtdeMembros));
        updateTotal();
    }

    // Método add visitantes frequentes.............................................................
    public void onClickAddVF(View v){
        TextView tvQtdeVF = (TextView) findViewById(R.id.tvQtdeVFrequentes);
        tvQtdeVF.setText( getStringQTDE(true,tvQtdeVF));
        updateTotal();
    }

    // Método retira visitantes frequentes..........................................................
    public void onClickRetiraVF(View v){
        TextView tvQtdeVF = (TextView) findViewById(R.id.tvQtdeVFrequentes);
        tvQtdeVF.setText( getStringQTDE(false,tvQtdeVF));
        updateTotal();
    }

    // Método add visitantes não frequentes.........................................................
    public void onClickAddVNF(View v){
        TextView tvQtdeVNF = (TextView) findViewById(R.id.tvQtdeVNFrequentes);
        tvQtdeVNF.setText( getStringQTDE(true,tvQtdeVNF));
        updateTotal();
    }

    // Método retira visitantes não frequentes......................................................
    public void onClickRetiraVNF(View v){
        TextView tvQtdeVNF = (TextView) findViewById(R.id.tvQtdeVNFrequentes);
        tvQtdeVNF.setText( getStringQTDE(false,tvQtdeVNF));
        updateTotal();
    }

    // Método que retorna a String com o 0 na frente ou não.........................................
    private String getStringQTDE(boolean isAdd, TextView tvQTDE){

        int qtde = Integer.parseInt(tvQTDE.getText().toString());
        String strQtde;

        if(isAdd){
            qtde++;
        }
        else{
            if(qtde > 0){
                qtde--;
            }
        }

        if(qtde < 10)
            strQtde = "0" + qtde;
        else
            strQtde = String.valueOf(qtde);

        return strQtde;
    }

    // Método que atualiza o total..................................................................
    private void updateTotal(){
        TextView tvQtdeMembros = (TextView) findViewById(R.id.tvQtdeMembros);
        TextView tvQtdeVF = (TextView) findViewById(R.id.tvQtdeVFrequentes);
        TextView tvQtdeVNF = (TextView) findViewById(R.id.tvQtdeVNFrequentes);
        TextView tvTotal = (TextView) findViewById(R.id.tvTotal);

        int qm = Integer.parseInt(tvQtdeMembros.getText().toString());
        int qvf = Integer.parseInt(tvQtdeVF.getText().toString());
        int qvnf = Integer.parseInt(tvQtdeVNF.getText().toString());
        int t = qm + qvf + qvnf;

        if(t == 1)
            tvTotal.setText("Total: " + t + " Vida");
        else
            tvTotal.setText("Total: " + t + " Vidas");
    }

    // Metodo OnOptionsItemSelected.................................................................
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Se pressionar a Seta para voltar na ToolBar, finaliza a Activity
        if (id == android.R.id.home) {

            // Cria a caixa de alertFecharActivity
            createDialogFechaActivity();
        }

        return true;
    }

    // Método onResume..............................................................................
    @Override
    protected void onResume() {
        super.onResume();

    }

    // Método onBackPressed.........................................................................
    @Override
    public void onBackPressed() {
        createDialogFechaActivity();
        if(closeActivity){
            super.onBackPressed();
        }
    }

    // Método createDialogDirecaoCulto..............................................................
    private void createDialogDirecao(){

        // Verifica o ChechBox
        CheckBox chbEBD = (CheckBox) findViewById(R.id.chbEBD);
        Calendar cal = Calendar.getInstance();
        int diaSemana = cal.get(Calendar.DAY_OF_WEEK);

        if(diaSemana == Calendar.SUNDAY){
            if(chbEBD.isChecked()){
                checkEBD = true;
            }
        }

        // Cria o AlertDialog
        AlertDialog alertDirecao;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_obreiro, null);
        builder.setView(dialogView);

        final EditText edtOL = (EditText) dialogView.findViewById(R.id.edtObLouvor);
        final EditText edtOP = (EditText) dialogView.findViewById(R.id.edtObPalavra);

        if(checkEBD){
            edtOP.setVisibility(EditText.INVISIBLE);
            edtOL.setHint("Direção");
        }

        builder.setTitle("Direção do Culto");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(!checkEBD){
                    obL = edtOL.getText().toString();
                    obP = edtOP.getText().toString();
                }
                else{
                    obL = edtOL.getText().toString();
                }

                save();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { }
        });

        alertDirecao = builder.create();
        alertDirecao.show();
    }

    // Método createDialogFecharActivity............................................................
    private void createDialogFechaActivity(){
        closeActivity = false;
        AlertDialog alertFecharActivity;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção!");
        builder.setMessage("A Frequencia não foi salva!\nDeseja sair mesmo assim?");
        builder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        closeActivity = true;
                        finish();
                    }
                }
        );
        builder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    closeActivity = false;
                    }
                }
        );

        // Atribui o alert
        alertFecharActivity = builder.create();

        // Mostra o alert
        alertFecharActivity.show();
    }

    // Método Salvar................................................................................
    public void save(){
        try {
            Frequencia f = new Frequencia();
            FrequenciaDAO fDAO = new FrequenciaDAO(this);

            TextView tvQtdeMembros = (TextView) findViewById(R.id.tvQtdeMembros);
            TextView tvQtdeVFrequentes = (TextView) findViewById(R.id.tvQtdeVFrequentes);
            TextView tvQtdeVNFrequentes = (TextView) findViewById(R.id.tvQtdeVNFrequentes);
            CheckBox chbEBD = (CheckBox) findViewById(R.id.chbEBD);

            int qtdeMembros = Integer.parseInt(tvQtdeMembros.getText().toString());
            int qtdeVFreq = Integer.parseInt(tvQtdeVFrequentes.getText().toString());
            int qtdeVNFreq = Integer.parseInt(tvQtdeVNFrequentes.getText().toString());

            if (chbEBD != null) {
                if (chbEBD.isChecked()) {
                    f.setTipoCulto(EnumTipoCulto.EBD);
                }
            }

            f.setQtdeMembros(qtdeMembros);
            f.setQtdeVFreq(qtdeVFreq);
            f.setQtdeVNFreq(qtdeVNFreq);
            f.setOb_louvor(obL);
            f.setOb_palavra(obP);

            // Salva o registro
            fDAO.save(f);

            // Mostra mensagem de confirmação
            Toast.makeText(this, "Frequencia Salva!", Toast.LENGTH_LONG).show();

            // Fecha a Activity
            finish();
        } catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
