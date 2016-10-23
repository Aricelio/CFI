package br.com.ti.aricelio.cfi.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.ti.aricelio.cfi.DataAccess.FrequenciaDAO;
import br.com.ti.aricelio.cfi.Extras.Extras;
import br.com.ti.aricelio.cfi.Enum.EnumTipoCulto;
import br.com.ti.aricelio.cfi.Model.Frequencia;
import br.com.ti.aricelio.cfi.R;
import br.com.ti.aricelio.libutils.Interfaces.RecyclerViewOnClickListener;

/**
 * Created by aricelio on 25/08/16.
 */
public class FrequenciaAdapter extends RecyclerView.Adapter<FrequenciaAdapter.MyViewHolder>{

    // Variaveis
    Context mContext;
    private List<Frequencia> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListener mRecyclerViewOnClickListener;

    // Construtor...................................................................................
    public FrequenciaAdapter(Context context, List<Frequencia> list){
        this.mContext = context;
        this.mList = list;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // SetRecyclerView..............................................................................
    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener r){
        mRecyclerViewOnClickListener = r;
    }

    // Método OnCreateViewHolder....................................................................
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_freq_card, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    // Método onBindViewHolder......................................................................
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        // Membros
        String membros = mContext.getString(R.string.membros) + " " + mList.get(position).getQtdeMembros();
        holder.tvQtdeMembros.setText(membros);

        // Visitantes
        String visitantesF = mContext.getString(R.string.visitantes_frequentes) + " " + mList.get(position).getQtdeVFreq();
        String visitantesNF = mContext.getString(R.string.visitantes_nao_frequentes) + " " + mList.get(position).getQtdeVNFreq();
        holder.tvQtdeVF.setText(visitantesF);
        holder.tvQtdeVNF.setText(visitantesNF);

        // Data do Culto
        String tipoCulto = mList.get(position).getTipoCulto().toString();
        String titulo="";
        String louvor="";
        String palavra="";

        // Se for EBD
        if(tipoCulto.equals(EnumTipoCulto.EBD.toString())){
            titulo = mContext.getString(R.string.ebd)+" "+Extras.getFormatDate(mList.get(position).getDataculto());
            louvor = mContext.getString(R.string.direcao) + " "+ mList.get(position).getOb_louvor();
            palavra = "";
        }
        // Senão sefor SENHORAS
        else if(tipoCulto.equals(EnumTipoCulto.SENHORAS.toString())){
            titulo = "Culto Senhoras: "+Extras.getFormatDate(mList.get(position).getDataculto());
            louvor = mContext.getString(R.string.louvor) + " "+ mList.get(position).getOb_louvor();
            palavra = mContext.getString(R.string.palavra) + " "+ mList.get(position).getOb_palavra();
        }
        else if(tipoCulto.equals(EnumTipoCulto.MADRUGADA.toString())){
            titulo = "Madrugada: "+Extras.getFormatDate(mList.get(position).getDataculto());
            louvor = mContext.getString(R.string.direcao) + " "+ mList.get(position).getOb_louvor();
            palavra = "";
        }
        // Senão se for Culto Normal
        else{
            titulo = mContext.getString(R.string.culto)+" "+ Extras.getFormatDate(mList.get(position).getDataculto());
            louvor = mContext.getString(R.string.louvor) + " "+ mList.get(position).getOb_louvor();
            palavra = mContext.getString(R.string.palavra) + " "+ mList.get(position).getOb_palavra();
        }

        // Seta os valores
        holder.tvData.setText(titulo);
        holder.tvLouvor.setText(louvor);
        holder.tvPalavra.setText(palavra);

        // Menu OverFlow
        holder.menuOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menuOverflow, position);
            }
        });

        // Whatsapp
        holder.btnWhatsapp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                callWhatsapp(position);
            }
        });

        // Excluir
        holder.btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            // Cria o AlertDialog
            AlertDialog alertDirecao;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setTitle("Atenção!");
            builder.setMessage("Deseja realmente apagar essa frequência?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    deleteItem(position);
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { }
            });

            alertDirecao = builder.create();
            alertDirecao.show();
            }
        });
    }

    // Método ShowPopupMenu.........................................................................
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card_main, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    // Classe para o evento do menu overflow =======================================================
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.action_card_main_edit:
                    Toast.makeText(mContext, "Clicou no Item: " + position, Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_card_main_delete:
                    deleteItem(position);
                    return true;

                case R.id.action_card_main_share:

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "\n\n");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getShareContent(position, false));
                    mContext.startActivity(Intent.createChooser(sharingIntent, "Compartilhar"));

                    return true;

                default:
            }
            return false;
        }
    }

    // Método getItemCount()........................................................................
    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    // Método que apaga um item da lista............................................................
    private void deleteItem(int position){
        try {
            Frequencia f = mList.get(position);
            FrequenciaDAO dao = new FrequenciaDAO(mContext);

            // Apaga o registro do banco de dados
            dao.delete(f.getId());

            // Remove o item da lista
            mList.remove(position);

            // Notifica a RecyclerView
            this.notifyDataSetChanged();

            // Mostra a mensagem de confirmação
            Toast.makeText(mContext,"Frequência excluida!",Toast.LENGTH_LONG).show();

        } catch(Exception e){
            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    // Método que retorna uma String com os dados a serem compartilhados............................
    private String getShareContent(int position, boolean isForWhatsapp){

        Frequencia f = mList.get(position);
        String strConteudo;
        int vf = 0;
        int vnf = 0;

        // SENÃO compartilhar com o Whatsapp
        if(!isForWhatsapp){

            // Se for EBD
            if(mList.get(position).getTipoCulto().equals(EnumTipoCulto.EBD)){
                strConteudo =
                    mContext.getString(R.string.ebd) + " " + f.getStringDataculto() + "\n"
                    + mContext.getString(R.string.membros) + " " + f.getQtdeMembros() + "\n";

                // Senão houve visitantes
                if(f.getQtdeVFreq() == 0 &&  f.getQtdeVFreq() == 0){
                    strConteudo += "Sem visitantes\n";
                }
                // Se houve visitantes
                else{
                    strConteudo +=
                        mContext.getString(R.string.visitantes_frequentes) + " " + f.getQtdeVFreq() + "\n"
                        + mContext.getString(R.string.visitantes_nao_frequentes) + " " + f.getQtdeVNFreq() + "\n";

                }
                // Add o obreiro que dirigiu o culto
                strConteudo += mContext.getString(R.string.direcao) + " " + f.getOb_louvor();
            }
            // Se for culto Normal
            else{
                strConteudo =
                    mContext.getString(R.string.culto) + " " + f.getStringDataculto() + "\n"
                    + mContext.getString(R.string.membros) + " " + f.getQtdeMembros() + "\n";

                // Senão houve visitantes
                if(f.getQtdeVFreq() == 0 &&  f.getQtdeVNFreq() == 0){
                    strConteudo += "Sem visitantes\n";
                }
                // Se houve vistantes
                else{
                    strConteudo +=
                        mContext.getString(R.string.visitantes_frequentes) + " " + f.getQtdeVFreq() + "\n"
                        + mContext.getString(R.string.visitantes_nao_frequentes) + " " + f.getQtdeVNFreq() + "\n";
                }
                // Add o obreiro que dirigiu o louvor e a palavra
                strConteudo +=
                        mContext.getString(R.string.direcao) + " " + f.getOb_louvor()
                        + mContext.getString(R.string.palavra) + " " + f.getOb_palavra();
            }
        }
        // Se for compartilhar com o Whatsapp
        else{
            // Se for EBD ou MADRUGADA
            String culto = mList.get(position).getTipoCulto().toString();
            String strEBD = EnumTipoCulto.EBD.toString();
            String strMadrugada = EnumTipoCulto.MADRUGADA.toString();

            if(culto.equals(strEBD) || culto.equals(strMadrugada)){

                // Add o titulo
                strConteudo = "";
                if(culto.equals(strEBD)){
                    strConteudo += mContext.getString(R.string.ebd_w) + " " + f.getStringDataculto() + "\n";
                }
                else if(culto.equals(strMadrugada)){
                    strConteudo += mContext.getString(R.string.madrugada_w) + " " + f.getStringDataculto() + "\n";
                }

                // Add a qtde de membros
                strConteudo += mContext.getString(R.string.membros_w) + " " + f.getQtdeMembros() + "\n";

                // Add a qtde de visitantes
                // Senão houve visitantes
                if(f.getQtdeVFreq() == 0 &&  f.getQtdeVNFreq() == 0){
                    strConteudo += "*Sem visitantes*\n";
                }
                // Se houve visitantes
                else{
                    strConteudo +=
                        mContext.getString(R.string.visitantes_frequentes_w) + " "
                        + f.getQtdeVFreq() + "\n"
                        + mContext.getString(R.string.visitantes_nao_frequentes_w) + " "
                        + f.getQtdeVNFreq() + "\n";
                }

                // Add o obreiro que dirigiu o culto
                strConteudo += mContext.getString(R.string.direcao_w)
                    + " " + f.getOb_louvor() + "\n";

            }
            // Senão for EBD
            else{
                strConteudo =
                    mContext.getString(R.string.culto_w) + " " + f.getStringDataculto() + "\n"
                    + mContext.getString(R.string.membros_w) + " " + f.getQtdeMembros() + "\n";

                // Senão houve visitantes
                vf = f.getQtdeVFreq();
                vnf = f.getQtdeVNFreq();

                if(vf == 0  && vnf == 0){
                    strConteudo += "*Sem visitantes*\n";
                }
                // Se houve visitantes
                else{
                    strConteudo +=
                       mContext.getString(R.string.visitantes_frequentes_w) + " "
                       + f.getQtdeVFreq() + "\n"
                       + mContext.getString(R.string.visitantes_nao_frequentes_w) + " "
                       + f.getQtdeVNFreq() + "\n";
                }

                // Add o obreiro que dirigiu o louvor e a palavra
                // Se o mesmo obreiro dirigiu o louvor e a palavra
                if(f.getOb_louvor().equals(f.getOb_palavra())){
                    strConteudo +=
                        mContext.getString(R.string.direcao_w) + " "
                        + f.getOb_louvor();
                }
                // senão
                else{
                    strConteudo +=
                       mContext.getString(R.string.louvor_w) + " " + f.getOb_louvor() + "\n"
                       + mContext.getString(R.string.palavra_w) + " " + f.getOb_palavra();
                }
            }
        }

        return strConteudo;
    }

    // Método que chama direto o Whatsapp...........................................................
    private void callWhatsapp(int position){
        PackageManager pm= mContext.getPackageManager();
        try {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String text = getShareContent(position, true);

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            intent.setPackage("com.whatsapp");

            intent.putExtra(Intent.EXTRA_TEXT, text);
            mContext.startActivity(Intent.createChooser(intent, "Compartilhar com"));
        } catch(Exception e){
            Toast.makeText(mContext,"Não foi possivel abrir o Whatsapp!",Toast.LENGTH_LONG).show();
        }
    }

    // Classe MyViewHolder =========================================================================
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Variaveis
        public TextView tvQtdeMembros;
        public TextView tvQtdeVF;
        public TextView tvQtdeVNF;
        public TextView tvData;
        //public TextView tvTipoCulto;
        public TextView tvLouvor;
        public TextView tvPalavra;
        public ImageView menuOverflow;
        public ImageView btnWhatsapp;
        public ImageView btnDelete;

        // Construtor
        public MyViewHolder(View itemView) {
            super(itemView);

            tvQtdeMembros = (TextView) itemView.findViewById(R.id.tv_card_qtde_membros);
            tvQtdeVF = (TextView) itemView.findViewById(R.id.tv_card_qtde_vf);
            tvQtdeVNF = (TextView) itemView.findViewById(R.id.tv_card_qtde_vnf);
            tvData = (TextView) itemView.findViewById(R.id.tv_card_data);
            tvLouvor = (TextView) itemView.findViewById(R.id.tv_card_louvor);
            tvPalavra = (TextView) itemView.findViewById(R.id.tv_card_palavra);
            menuOverflow = (ImageView) itemView.findViewById(R.id.menu_overflow);
            btnWhatsapp = (ImageView) itemView.findViewById(R.id.item_whatsapp);
            btnDelete = (ImageView) itemView.findViewById(R.id.item_detele);

            itemView.setOnClickListener(this);
        }

        // implementação da interface de clique
        @Override
        public void onClick(View view) {
            if(mRecyclerViewOnClickListener != null){
                mRecyclerViewOnClickListener.onClickListener(view, getPosition());
            }
        }
    }
}
