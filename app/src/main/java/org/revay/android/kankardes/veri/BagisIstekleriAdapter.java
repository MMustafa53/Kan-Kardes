package org.revay.android.kankardes.veri;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.revay.android.kankardes.R;

public class BagisIstekleriAdapter extends RecyclerView.Adapter<BagisIstekleriAdapter.BagisIstekleriViewHolder> {

    Veri[] veriler;

    @Override
    public BagisIstekleriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.bagis_istekleri_item, parent, false);

        return new BagisIstekleriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BagisIstekleriViewHolder holder, final int position) {
        final Veri veri = veriler[position];

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bildirim = new AlertDialog.Builder(v.getContext());
                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                View view = layoutInflater.inflate(R.layout.bagis_istekleri_detay_item, null, false);
                bildirim.setView(view);

                TextView tvkan = view.getRootView().findViewById(R.id.textViewBagisIstekleriKanGrubuDetay);
                tvkan.setText(veriler[position].getKanGrubu());
                TextView tvhst = view.getRootView().findViewById(R.id.textViewBagisIstekleriHastaneDetay);
                tvhst.setText(veriler[position].getHastaneAdi());

                TextView tvhstad = view.getRootView().findViewById(R.id.textViewBagisIstekleriHastaneAdresDetay);
                tvhstad.setText(veriler[position].getHastaAdi());
                final TextView tvtel = view.getRootView().findViewById(R.id.textViewBagisIstekleriTelDetay);
                tvtel.setText(veriler[position].getTelefon());

                TextView tvIstekZamani = view.getRootView().findViewById(R.id.textViewIstekZamaniDetay);
                tvIstekZamani.setText(veriler[position].getIstekZamani());

                final String tell = tvtel.getText().toString();
                final Context context = v.getContext();

               bildirim.setPositiveButton("Ara!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebServisBaglanti.arama_logla(context, veriler[position].getId());
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(Uri.parse("tel:"+"0"+ tell));
                        context.startActivity(i);
                    }
                });
                bildirim.show();
            }
        });

        holder.bind(veri);
    }

    @Override
    public int getItemCount() {
        if (veriler != null) return veriler.length;
        return 0;
    }

    public void setVeriler(Veri[] veriler){
        this.veriler = veriler;
        notifyDataSetChanged();
    }

    class BagisIstekleriViewHolder extends RecyclerView.ViewHolder {
        TextView textViewKanGrubu;
        TextView textViewHastane;

        public BagisIstekleriViewHolder(View itemView) {
            super(itemView);

            textViewKanGrubu = itemView.findViewById(R.id.textViewBagisIstekleriKanGrubu);
            textViewHastane = itemView.findViewById(R.id.textViewBagisIstekleriHastane);
        }

        public void bind(Veri veri){
            textViewKanGrubu.setText(veri.getKanGrubu());
            textViewHastane.setText(veri.getHastaneAdi());
        }
    }
}
