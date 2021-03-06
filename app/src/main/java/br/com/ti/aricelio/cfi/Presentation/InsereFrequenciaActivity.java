package br.com.ti.aricelio.cfi.Presentation;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.ti.aricelio.cfi.DataAccess.FrequenciaDAO;
import br.com.ti.aricelio.cfi.Enum.EnumTipoCulto;
import br.com.ti.aricelio.cfi.Model.Frequencia;
import br.com.ti.aricelio.cfi.R;

public class InsereFrequenciaActivity extends AppCompatActivity {

    // Variaveis
    private String obL = "", obP = "", tmpData = "";
    boolean closeActivity = false;
    boolean isEBD = false;
    boolean isTransmissao = false;
    boolean isGlorificacao = false;
    boolean isSenhoras = false;
    boolean isMadrugada = false;
    boolean isAdvancedMode = false;
    boolean isModeMadrugadaSab = false;

    // Método OnCreate..............................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insere_frequencia);

        // Toolbar
        getSupportActionBar().setTitle("Nova Frequencia");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        // Preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean modo = sharedPref.getBoolean("modo",false);
        Boolean madrugadaSab = sharedPref.getBoolean("madrugada",false);
        isAdvancedMode = modo;
        isModeMadrugadaSab = madrugadaSab;

        // CheckBoxs
        CheckBox chbTipoCulto = (CheckBox) findViewById(R.id.chbTipoCulto);
        CheckBox chbMad = (CheckBox) findViewById(R.id.chbMadrugada);
        Calendar cal = Calendar.getInstance();

        int dia = cal.get(Calendar.DAY_OF_WEEK);

        // CheckBox Madrugada........................................*/
        // Se marcou quen tem madrugada todos os dias
        if(!isModeMadrugadaSab && dia != Calendar.SUNDAY){
            // Coloca o Checkbox visivel
            chbMad.setVisibility(CheckBox.VISIBLE);
        }
        // Senão, se marcou que tem madrugada só no Sab
        else{
            if(dia == Calendar.SATURDAY){
                // Coloca o Checkbox visivel
                chbMad.setVisibility(CheckBox.VISIBLE);
            }
            else{
                // Coloca o Checkbox invisivel
                chbMad.setVisibility(CheckBox.INVISIBLE);
            }
        }

        // CheckBox Tipo Culto......................................*/
        // Se for Domingo ou Terça
        if(dia == Calendar.SUNDAY || dia == Calendar.TUESDAY){

            // Coloca o Checkbox visivel
            chbTipoCulto.setVisibility(CheckBox.VISIBLE);

            // Altera o nome dependendo do CheckBox
            if(dia == Calendar.SUNDAY){
                chbTipoCulto.setText("EBD");
            }
            else{
                chbTipoCulto.setText("Transmissão Via Satelite");
            }
        }
        else{
            chbTipoCulto.setVisibility(CheckBox.INVISIBLE);
        }
    }

    // Método Salvar...............................................................................
    public void onClickSalvar(View v){
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

        // Se pressionar a Seta para voltar na ToolBar chama a Dialog
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
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        CheckBox chbMad = (CheckBox) findViewById(R.id.chbMadrugada);
        CheckBox chbTipoCulto = (CheckBox) findViewById(R.id.chbTipoCulto);

        // Senão é Domingo e o CheckBox da Madrugada foi selecionado
        if(day != Calendar.SUNDAY && chbMad.isChecked()){
            isMadrugada = true;
        }
        else{

            // Se for Domingo e o checkbox estiver selecionado, quer dizer que é EBD
            if(day == Calendar.SUNDAY && chbTipoCulto.isChecked()){
                isEBD = true;
            }
            // Se for Segunda é GLORIFICAÇÃO
            else if(day == Calendar.MONDAY ){
                isGlorificacao = true;
            }
            // Se for Terça e o checkbox estiver selecioando é TRANSMISSÃO
            else if(day == Calendar.TUESDAY && chbTipoCulto.isChecked()){
                isTransmissao = true;
            }
            // Se for Quarta é SENHORAS
            else if(day == Calendar.WEDNESDAY){
                isSenhoras = true;
            }
        }

        // Cria o AlertDialog
        AlertDialog alertDirecao;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView;

        // Cria uma alert diferente dependendo do Modo
        if(isAdvancedMode){
            dialogView = inflater.inflate(R.layout.dialog_obreiro_completo, null);
        }
        else{
            dialogView = inflater.inflate(R.layout.dialog_obreiro, null);
        }

        builder.setView(dialogView);
        final EditText edtOL = (EditText) dialogView.findViewById(R.id.edtObLouvor);
        final EditText edtOP = (EditText) dialogView.findViewById(R.id.edtObPalavra);
        final EditText edtData = (EditText) dialogView.findViewById(R.id.edtData);

        // Se o CheckBox EBD ou Madrugada foram selecionados
        if(isEBD || isMadrugada){
            edtOP.setVisibility(EditText.INVISIBLE);
            edtOL.setHint("Direção");
        }

        builder.setTitle("Direção do Culto");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            // Senão for EBD nem Madrugada
            if(!isEBD && !isMadrugada){
                obL = edtOL.getText().toString();
                obP = edtOP.getText().toString();

                // Se estiver no modo avançado pega a Data inserida
                if(isAdvancedMode){
                    tmpData = edtData.getText().toString();
                }
            }
            // Se for EBD ou Madrugada pega só o EDT da direção do culto
            else{
                obL = edtOL.getText().toString();
                if(isAdvancedMode){
                    tmpData = edtData.getText().toString();
                }
            }

            // Chama o método para Salvar
            salvar();
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
    public void salvar(){
        try {
            Frequencia f = new Frequencia();
            FrequenciaDAO fDAO = new FrequenciaDAO(this);
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");

            TextView tvQtdeMembros = (TextView) findViewById(R.id.tvQtdeMembros);
            TextView tvQtdeVFrequentes = (TextView) findViewById(R.id.tvQtdeVFrequentes);
            TextView tvQtdeVNFrequentes = (TextView) findViewById(R.id.tvQtdeVNFrequentes);

            int qtdeMembros = Integer.parseInt(tvQtdeMembros.getText().toString());
            int qtdeVFreq = Integer.parseInt(tvQtdeVFrequentes.getText().toString());
            int qtdeVNFreq = Integer.parseInt(tvQtdeVNFrequentes.getText().toString());

            // Define o tipo de culto
            if(isEBD){
                f.setTipoCulto(EnumTipoCulto.EBD);
            }
            else if(isTransmissao){
                f.setTipoCulto(EnumTipoCulto.TRANSMISSAO);
            }
            else if(isGlorificacao){
                f.setTipoCulto(EnumTipoCulto.GLORIFICACAO);
            }
            else if(isSenhoras){
                f.setTipoCulto(EnumTipoCulto.SENHORAS);
            }
            else if(isMadrugada){
                f.setTipoCulto(EnumTipoCulto.MADRUGADA);
            }

            // Se foi digitado algo no campo Data
            if(!tmpData.equals("")){
                f.setDataculto(dt.parse(tmpData));
            }

            f.setQtdeMembros(qtdeMembros);
            f.setQtdeVFreq(qtdeVFreq);
            f.setQtdeVNFreq(qtdeVNFreq);
            f.setOb_louvor(obL);
            f.setOb_palavra(obP);

            // Verifica se informou a qtde de membros
            if(f.getQtdeMembros() > 0) {

                // Salva o registro
                fDAO.save(f);

                // Mostra mensagem de confirmação
                Toast.makeText(this, "Frequencia Salva!", Toast.LENGTH_LONG).show();

                // Fecha a Activity
                finish();
            }
            else{
                // Mostra mensagem de confirmação
                Toast.makeText(this, "Atenção! Informe a quantidade de Membros!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
