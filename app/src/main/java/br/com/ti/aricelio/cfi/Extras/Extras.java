package br.com.ti.aricelio.cfi.Extras;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aricelio on 07/09/16.
 */
public class Extras {
    public static String getFormatDate(Date data){
        String strDiaPorExtenso="";
        String strDia ="";
        String strAno ="";
        String strMes="";
        String strReturn="";

        Locale.setDefault (new Locale ("pt", "BR"));
        SimpleDateFormat dtDiaPorExtenso = new SimpleDateFormat("EEEEEE");
        SimpleDateFormat dtMes = new SimpleDateFormat("MMM");
        SimpleDateFormat dtDia = new SimpleDateFormat("dd");
        SimpleDateFormat dtAno = new SimpleDateFormat("yyyy");

        strDiaPorExtenso = dtDiaPorExtenso.format(data);
        strDia = dtDia.format(data);
        strMes = dtMes.format(data);
        strAno = dtAno.format(data);

        // Troca o dia
        switch (strDiaPorExtenso){
            case "segunda-feira":
                strReturn = "Segunda";
                break;
            case "terça-feira":
                strReturn = "Terça";
                break;
            case "quarta-feira":
                strReturn = "Quarta";
                break;
            case "quinta-feira":
                strReturn = "Quinta";
                break;
            case "sexta-feira":
                strReturn = "Sexta";
                break;
            case "sábado":
                strReturn = "Sábado";
                break;
            case "domingo":
                strReturn = "Domingo";
                break;
            case "seg":
                strReturn = "Segunda";
                break;
            case "ter":
                strReturn = "Terça";
                break;
            case "qua":
                strReturn = "Quarta";
                break;
            case "qui":
                strReturn = "Quinta";
                break;
            case "sex":
                strReturn = "Sexta";
                break;
            case "sáb":
                strReturn = "Sábado";
                break;
            case "dom":
                strReturn = "Domingo";
                break;
            default:
                strReturn = "Erro";
                break;
        }

        // Troca o mes

        switch (strMes){
            case "jan":
                strMes = "Jan";
                break;
            case "fev":
                strMes = "Fev";
                break;
            case "mar":
                strMes = "Mar";
                break;
            case "abr":
                strMes = "Abr";
                break;
            case "mai":
                strMes = "Mai";
                break;
            case "jun":
                strMes = "Jun";
                break;
            case "jul":
                strMes = "Jul";
                break;
            case "ago":
                strMes = "Ago";
                break;
            case "set":
                strMes = "Set";
                break;
            case "out":
                strMes = "Out";
                break;
            case "nov":
                strMes = "Nov";
                break;
            case "dez":
                strMes = "Dez";
                break;
            default:
                strMes = "Erro";
                break;
        }

        return strReturn + " - " + strDia + " "
                + strMes + " " + strAno;
    }
}
