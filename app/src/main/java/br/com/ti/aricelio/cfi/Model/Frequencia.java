package br.com.ti.aricelio.cfi.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aricelio on 23/08/16.
 */
public class Frequencia {

    private long id;
    private int qtdeMembros;
    private int qtdeVFreq;
    private int qtdeVNFreq;
    private Date dataculto;
    private String ob_louvor;
    private String ob_palavra;
    private EnumTipoCulto tipoCulto;

    // Construtor...................................................................................
    public Frequencia() {

        // Pega a data atual
        Calendar cal = Calendar.getInstance();
        int diaSemana = cal.get(cal.DAY_OF_WEEK);

        // Seta a Data
        this.dataculto = cal.getTime();

        if(diaSemana == Calendar.WEDNESDAY)
            this.tipoCulto = EnumTipoCulto.SENHORAS;
        else if(diaSemana == Calendar.MONDAY)
            this.tipoCulto = EnumTipoCulto.GLORIFICACAO;
        else
            this.tipoCulto = EnumTipoCulto.NORMAL;

    }
    // Getters e Setters............................................................................
    public EnumTipoCulto getTipoCulto() {
        return tipoCulto;
    }

    public void setTipoCulto(EnumTipoCulto tipoCulto) {
        this.tipoCulto = tipoCulto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQtdeMembros() {
        return qtdeMembros;
    }

    public void setQtdeMembros(int qtdeMembros) {
        this.qtdeMembros = qtdeMembros;
    }

    public int getQtdeVFreq() {
        return qtdeVFreq;
    }

    public void setQtdeVFreq(int qtdeVFreq) {
        this.qtdeVFreq = qtdeVFreq;
    }

    public int getQtdeVNFreq() {
        return qtdeVNFreq;
    }

    public void setQtdeVNFreq(int qtdeVNFreq) {
        this.qtdeVNFreq = qtdeVNFreq;
    }

    public Date getDataculto() {
        return dataculto;
    }

    public String getStringDataculto() {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        return dt.format(this.dataculto);
    }

    public void setDataculto(Date dataculto) {
        this.dataculto = dataculto;
    }

    public String getOb_louvor() {
        return ob_louvor;
    }

    public void setOb_louvor(String ob_louvor) {
        if(ob_louvor == null)
            this.ob_louvor = "";
        else
            this.ob_louvor = ob_louvor;
    }

    public String getOb_palavra() {
        return ob_palavra;
    }

    public void setOb_palavra(String ob_palavra) {
        this.ob_palavra = ob_palavra;
    }
}
