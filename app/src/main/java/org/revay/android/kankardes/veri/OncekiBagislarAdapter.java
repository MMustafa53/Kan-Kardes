package org.revay.android.kankardes.veri;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.revay.android.kankardes.R;

/**
 * Created by Mustafa on 16.02.2018.
 */

public class OncekiBagislarAdapter extends RecyclerView.Adapter<OncekiBagislarAdapter.OncekibagislarViewHolder>{

    Veri[] veriler;
    @Override
    public OncekibagislarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.onceki_bagislar_item, parent, false);

        return new OncekiBagislarAdapter.OncekibagislarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OncekibagislarViewHolder holder, int position) {
        Veri veri = veriler[position];
        holder.bind(veri);

    }

    @Override
    public int getItemCount() {
        if(veriler!=null)
            return veriler.length;
        return 0;
    }

    public void setVeriler(Veri[] veriler){
        this.veriler = veriler;
        notifyDataSetChanged();
    }

    class OncekibagislarViewHolder extends RecyclerView.ViewHolder{
        TextView textViewOncekiBagislarKanGrubu;
        TextView textViewOncekiBagislarHastane;
        ImageButton imagebutton;

        public OncekibagislarViewHolder(View itemView) {
            super(itemView);

            textViewOncekiBagislarKanGrubu = itemView.findViewById(R.id.textViewOncekiBagislarKanGrubu);
            textViewOncekiBagislarHastane= itemView.findViewById(R.id.textViewOncekiBagislarHastane);
            imagebutton = itemView.findViewById(R.id.ib_onceki_bagislar);
        }
        public void bind(final Veri veri){
            textViewOncekiBagislarKanGrubu.setText(veri.getKanGrubu());
            textViewOncekiBagislarHastane.setText(veri.getHastaneAdi());
            if(!veri.isAktif()) {
                imagebutton.setEnabled(false);
                imagebutton.setColorFilter(android.R.color.darker_gray);
            } else {
                imagebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebServisBaglanti.istek_iptal(v, veri.getId());
                        veri.setAktif(0);
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
