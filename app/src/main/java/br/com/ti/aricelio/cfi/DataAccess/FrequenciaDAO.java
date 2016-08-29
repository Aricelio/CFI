package br.com.ti.aricelio.cfi.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.ti.aricelio.cfi.Interface.FrequenciaRepository;
import br.com.ti.aricelio.cfi.Model.EnumTipoCulto;
import br.com.ti.aricelio.cfi.Model.Frequencia;
import br.com.ti.aricelio.libutils.DataAccess.BuildSQL;

/**
 * Created by aricelio on 23/08/16.
 */
public class FrequenciaDAO implements FrequenciaRepository {

    Context mContext;

    // Construtor...................................................................................
    public FrequenciaDAO(Context mContext) {
        this.mContext = mContext;
    }

    // Método Abrir.................................................................................
    @Override
    public Frequencia open(Long aLong) throws Exception {
        return null;
    }

    // Método Salvar................................................................................
    @Override
    public void save(Frequencia f) throws Exception {
        DBUtil dbUtil = null;

        try{
            ContentValues values = new ContentValues();

            // Definição
            values.put(DBUtil.F_QTDE_MEMBROS, f.getQtdeMembros());
            values.put(DBUtil.F_QTDE_VFREQUENTES, f.getQtdeVFreq());
            values.put(DBUtil.F_QTDE_VNFREQUENTES, f.getQtdeVNFreq());
            values.put(DBUtil.F_DATA, f.getDataculto().toString());
            values.put(DBUtil.F_TCULTO, f.getTipoCulto().toString());
            values.put(DBUtil.F_OBLOUVOR, f.getOb_louvor());
            values.put(DBUtil.F_OBPALAVRA, f.getOb_palavra());

            // Inserção
            dbUtil = new DBUtil(mContext);
            dbUtil.getWritableDatabase().insert(DBUtil.T_FREQUENCIA, null, values);
        }
        catch(Exception e){
            throw new Exception("Erro ao Tentar Salvar!");
        }
        finally{
            if(dbUtil != null){
                dbUtil.close();
            }
        }
    }

    // Método Update................................................................................
    @Override
    public void update(Frequencia frequencia) throws Exception { }

    // Método Delete................................................................................
    @Override
    public void delete(Long id) throws Exception {

        // Definição do Array de Parametros
        String[] args = { id.toString() };

        // Exclusão do Projeto
        DBUtil db = new DBUtil(mContext);

        try{
            db.getWritableDatabase().delete(DBUtil.T_FREQUENCIA, "id=?", args);

            Log.i("LOG", "Frequencia deletada!");
        } catch(Exception e){
            throw new Exception("Não foi possivel apagar a Frequencia");
        } finally {
            db.close();
        }
    }

    // Método Buscar................................................................................
    @Override
    public List<Frequencia> find() throws Exception {

        List<Frequencia> list  = new ArrayList<>();
        SimpleDateFormat dt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        String sql = BuildSQL.select(DBUtil.T_FREQUENCIA);

        DBUtil dbUtil = new DBUtil(mContext);
        Cursor cursor = dbUtil.getReadableDatabase().rawQuery(sql, null);

        try{

            while(cursor.moveToNext()) {
                Frequencia f = new Frequencia();

                f.setId(cursor.getLong(0));
                f.setQtdeMembros(cursor.getInt(1));
                f.setQtdeVFreq(cursor.getInt(2));
                f.setQtdeVNFreq(cursor.getInt(3));
                f.setDataculto( dt.parse( cursor.getString(4)));
                f.setOb_louvor( cursor.getString(6));
                f.setOb_palavra( cursor.getString(7));

                if(cursor.getString(5).equals(EnumTipoCulto.NORMAL.toString()))
                    f.setTipoCulto(EnumTipoCulto.NORMAL);
                else if(cursor.getString(5).equals(EnumTipoCulto.SENHORAS.toString()))
                    f.setTipoCulto(EnumTipoCulto.SENHORAS);
                else if(cursor.getString(5).equals(EnumTipoCulto.EBD.toString()))
                    f.setTipoCulto(EnumTipoCulto.EBD);

                list.add(f);
            }

            return list;

        } catch(Exception e){
            throw new Exception("Erro ao buscar os dados!!");
        } finally {
            cursor.close();
            dbUtil.close();
        }
    }

    // Método Buscar com parametros.................................................................
    public List<Frequencia> find(int rows, boolean isOrderByDesc, String fieldOrderBy) throws Exception {

        List<Frequencia> list  = new ArrayList<>();
        SimpleDateFormat dt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        String sql = BuildSQL.select(DBUtil.T_FREQUENCIA,rows,isOrderByDesc,fieldOrderBy);

        DBUtil dbUtil = new DBUtil(mContext);
        Cursor cursor = dbUtil.getReadableDatabase().rawQuery(sql, null);

        try{
            while(cursor.moveToNext()) {
                Frequencia f = new Frequencia();

                f.setId(cursor.getLong(0));
                f.setQtdeMembros(cursor.getInt(1));
                f.setQtdeVFreq(cursor.getInt(2));
                f.setQtdeVNFreq(cursor.getInt(3));
                f.setDataculto( dt.parse( cursor.getString(4)));
                f.setOb_louvor( cursor.getString(6));
                f.setOb_palavra( cursor.getString(7));

                if(cursor.getString(5).equals(EnumTipoCulto.NORMAL.toString()))
                    f.setTipoCulto(EnumTipoCulto.NORMAL);
                else if(cursor.getString(5).equals(EnumTipoCulto.SENHORAS.toString()))
                    f.setTipoCulto(EnumTipoCulto.SENHORAS);
                else if(cursor.getString(5).equals(EnumTipoCulto.EBD.toString()))
                    f.setTipoCulto(EnumTipoCulto.EBD);

                list.add(f);
            }

            return list;

        } catch(Exception e){
            throw new Exception("Erro ao buscar os dados!!");
        } finally {
            cursor.close();
            dbUtil.close();
        }
    }
}
