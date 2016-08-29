package br.com.ti.aricelio.cfi.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.ti.aricelio.cfi.Model.TempFreq;
import br.com.ti.aricelio.libutils.DataAccess.BuildSQL;
import br.com.ti.aricelio.libutils.DataAccess.Type;

/**
 * Created by aricelio on 23/08/16.
 */
public class DBUtil extends SQLiteOpenHelper {

    // Variaveis
    private static final int VERSION = 1;
    private static final String DATABASE = "dbCFI";

    // Tabela Frequencia
    public static final String T_FREQUENCIA = "FREQUENCIA";
    public static final String F_ID = "id";
    public static final String F_QTDE_MEMBROS = "QTDE_MEMBROS";
    public static final String F_QTDE_VFREQUENTES = "QTDE_VFREQUENTES";
    public static final String F_QTDE_VNFREQUENTES = "QTDE_VNFREQUENTES";
    public static final String F_DATA = "DATA_CULTO";
    public static final String F_TCULTO = "TIPO_CULTO";
    public static final String F_OBLOUVOR = "OBREIRO_LOUVOR";
    public static final String F_OBPALAVRA = "OBREIRO_PALAVRA";

    // Tabela TempFreq
    public static final String T_TEMPFREQ = "TEMP_FREQ";


    /*........... Construtor......................................................................*/
    public DBUtil(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    /* ........ Método OnCreate...................................................................*/
    @Override
    public void onCreate(SQLiteDatabase database) {

        // Tabela Produto
        String[] fFreq = {F_ID, F_QTDE_MEMBROS, F_QTDE_VFREQUENTES, F_QTDE_VNFREQUENTES, F_DATA,F_TCULTO, F_OBLOUVOR, F_OBPALAVRA};
        String[] tFreq = {Type.PK, Type.INT, Type.INT, Type.INT, Type.DATE, Type.TEXT, Type.TEXT, Type.TEXT};
        String ddlFreq = BuildSQL.createTable(T_FREQUENCIA,fFreq.length,2,fFreq,tFreq);

        /* Tabela TempFreq
        String[] fTemp = {F_ID, F_QTDE_MEMBROS, F_QTDE_VFREQUENTES, F_QTDE_VNFREQUENTES};
        String[] tTemp = {Type.PK, Type.INT, Type.INT, Type.INT};
        String ddlTempFreq = BuildSQL.createTable(T_TEMPFREQ,fTemp.length,2,fTemp,tTemp);*/

        // Execução das instruções SQL para criação das Tabelas
        database.execSQL(ddlFreq);
        //database.execSQL(ddlTempFreq);
        Log.i("LOG","Criou as tabelas");

        /* Inserção do primeiro registro na tabela tempFreq
        ContentValues values = new ContentValues();
        values.put(F_QTDE_MEMBROS,0);
        values.put(F_QTDE_VFREQUENTES,0);
        values.put(F_QTDE_VNFREQUENTES,0);
        database.insert(T_TEMPFREQ,null,values);
        Log.i("LOG","Inseriu o primeiro registro na TempFreq");*/
    }

    /* ........ Método OnUpgrade..................................................................*/
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Método que atualiza a tabela tempFreq........................................................
    public void updateTempFreq(TempFreq t, Context c) throws Exception{

        DBUtil db = new DBUtil(c);

        try{
            ContentValues values = new ContentValues();

            values.put(F_QTDE_MEMBROS,t.qtdeMembros);
            values.put(F_QTDE_VFREQUENTES,t.qtdeVF);
            values.put(F_QTDE_VNFREQUENTES,t.qtdeVNF);

            String where = "id = 1";

            // Update
            db.getWritableDatabase().update(T_TEMPFREQ,values,where,null);

        } catch(Exception e){
            throw new Exception("Erro!");
        } finally {
            if(db != null){
                db.close();
            }
        }
    }
}
