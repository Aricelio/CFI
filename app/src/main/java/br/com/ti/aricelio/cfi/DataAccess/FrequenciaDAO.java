package br.com.ti.aricelio.cfi.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.ti.aricelio.cfi.Enum.EnumFiltro;
import br.com.ti.aricelio.cfi.Interface.FrequenciaRepository;
import br.com.ti.aricelio.cfi.Enum.EnumTipoCulto;
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
            values.put(DBUtil.F_DATA, f.getStringCompletaDataculto());
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

    // Faz uma busca com base no filtro passado.....................................................
    public List<Frequencia> findWithFilter(EnumFiltro filtro) throws Exception{

        boolean isFiltroGlorificacao = false;
        List<Frequencia> list  = new ArrayList<>();
        //SimpleDateFormat dt = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        String sql="";

        // Define a consulta
        // Se escolheu o filtro TODAS
        if(filtro.equals(EnumFiltro.TODAS)){
            sql = BuildSQL.select(DBUtil.T_FREQUENCIA)
                  + " ORDER BY " + DBUtil.F_DATA + " DESC ";
        }
        // Se escolheu o filtro EBD
        else if(filtro.equals(EnumFiltro.EBD)){
            sql = BuildSQL.select(DBUtil.T_FREQUENCIA);
            sql += " WHERE " + DBUtil.F_TCULTO + " LIKE '" + EnumTipoCulto.EBD + "'"
                + " ORDER BY " + DBUtil.F_DATA + " DESC ";
        }
        // Se escolheu o filtro SENHORAS
        else if(filtro.equals(EnumFiltro.SENHORAS)){
            sql = BuildSQL.select(DBUtil.T_FREQUENCIA);
            sql += " WHERE " + DBUtil.F_TCULTO + " LIKE '" + EnumTipoCulto.SENHORAS + "'"
                + " ORDER BY " + DBUtil.F_DATA + " DESC ";
        }
        // Se escolheu o filtro ULTIMOS 7 DIAS
        else if(filtro.equals(EnumFiltro.ULTIMOS_7_DIAS)){
            sql = BuildSQL.select(DBUtil.T_FREQUENCIA);
            sql += " ORDER BY " + DBUtil.F_DATA + " DESC LIMIT 7";
        }
        // Se escolheu o filtro ULTIMO MES
        else if(filtro.equals(EnumFiltro.ULTIMOS_MES)){
            sql = BuildSQL.select(DBUtil.T_FREQUENCIA);
            sql += " ORDER BY " + DBUtil.F_DATA + " DESC LIMIT 30";
        }
        // Se escolheu o filtro GLORIFICAÇÃO
        else if(filtro.equals(EnumFiltro.GLORIFICACAO)){
            sql = BuildSQL.select(DBUtil.T_FREQUENCIA)
                    + " ORDER BY " + DBUtil.F_DATA + " DESC ";
            isFiltroGlorificacao = true;
        }
        else if(filtro.equals(EnumFiltro.MADRUGADA)){
            sql = BuildSQL.select(DBUtil.T_FREQUENCIA);
            sql += " WHERE " + DBUtil.F_TCULTO + " LIKE '" + EnumTipoCulto.MADRUGADA + "'"
                    + " ORDER BY " + DBUtil.F_DATA + " DESC ";
        }

        // Realiza a consulta
        DBUtil dbUtil = new DBUtil(mContext);
        Cursor cursor = dbUtil.getReadableDatabase().rawQuery(sql, null);

        // Preenche a lista com os dados
        try{
            while(cursor.moveToNext()) {
                list.add(getFrequencia(cursor));
            }

            if(!isFiltroGlorificacao){
                return list;
            }
            else{
                return getListFiltradaGlorificacao(list);
            }

        } catch(Exception e){
            throw new Exception("Erro ao buscar os dados!!");
        } finally {
            cursor.close();
            dbUtil.close();
        }
    }

    // Método que realiza o filtro do tipo Glorificação.............................................
    private List<Frequencia> getListFiltradaGlorificacao(List<Frequencia> list){
        List<Frequencia> listaFiltrada = new ArrayList<>();

        for(Frequencia f : list){
            Calendar cal = Calendar.getInstance();
            cal.setTime(f.getDataculto());
            int dia = cal.get(Calendar.DAY_OF_WEEK);
            if(dia == Calendar.MONDAY){
                listaFiltrada.add(f);
            }
        }

        return listaFiltrada;
    }

    // Método Buscar com parametros.................................................................
    public List<Frequencia> find(int rows, boolean isOrderByDesc, String fieldOrderBy, String typeFieldOrderBy) throws Exception {

        List<Frequencia> list  = new ArrayList<>();
        //SimpleDateFormat dt2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        String sql = BuildSQL.select(DBUtil.T_FREQUENCIA,rows,isOrderByDesc,fieldOrderBy, typeFieldOrderBy);

        DBUtil dbUtil = new DBUtil(mContext);
        Cursor cursor = dbUtil.getReadableDatabase().rawQuery(sql, null);

        try{
            while(cursor.moveToNext()) {
                list.add(getFrequencia(cursor));
            }
            return list;

        } catch(Exception e){
            throw new Exception("Erro ao buscar os dados!!");
        } finally {
            cursor.close();
            dbUtil.close();
        }
    }

    // Setar os dados na lista
    private Frequencia getFrequencia(Cursor cursor) throws Exception{

        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Frequencia f = new Frequencia();

        f.setId(cursor.getLong(0));
        f.setQtdeMembros(cursor.getInt(1));
        f.setQtdeVFreq(cursor.getInt(2));
        f.setQtdeVNFreq(cursor.getInt(3));

        f.setOb_louvor( cursor.getString(6));
        f.setOb_palavra( cursor.getString(7));

        // Data
        f.setDataculto( dt.parse( cursor.getString(4)));

        // Tipo Culto
        if(cursor.getString(5).equals(EnumTipoCulto.NORMAL.toString()))
            f.setTipoCulto(EnumTipoCulto.NORMAL);
        else if(cursor.getString(5).equals(EnumTipoCulto.SENHORAS.toString()))
            f.setTipoCulto(EnumTipoCulto.SENHORAS);
        else if(cursor.getString(5).equals(EnumTipoCulto.EBD.toString()))
            f.setTipoCulto(EnumTipoCulto.EBD);
        else if(cursor.getString(5).equals(EnumTipoCulto.MADRUGADA.toString()))
            f.setTipoCulto(EnumTipoCulto.MADRUGADA);

        return f;
    }
}
