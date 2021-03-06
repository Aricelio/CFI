package br.com.ti.aricelio.cfi.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.ti.aricelio.cfi.Enum.EnumTipoCulto;

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

        this.tipoCulto = EnumTipoCulto.NORMAL;
        this.dataculto = cal.getTime();

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

    public String getStringQtdeMembros() {
        return String.valueOf(qtdeMembros);
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

    public String getStringCompletaDataculto() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dt.format(this.dataculto);
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
