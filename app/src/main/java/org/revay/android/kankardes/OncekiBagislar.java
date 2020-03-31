package org.revay.android.kankardes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.revay.android.kankardes.veri.OncekiBagislarAdapter;
import org.revay.android.kankardes.veri.Veri;
import org.revay.android.kankardes.veri.WebServisBaglanti;

public class OncekiBagislar extends AppCompatActivity {

    OncekiBagislarAdapter oncekiBagislarAdapter;
    RecyclerView recyclerViewoncekiBagislar;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onceki_bagislar);

        recyclerViewoncekiBagislar = findViewById(R.id.rv_onceki_bagislar);
        oncekiBagislarAdapter = new OncekiBagislarAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewoncekiBagislar.setLayoutManager(layoutManager);
        recyclerViewoncekiBagislar.setHasFixedSize(true);

        recyclerViewoncekiBagislar.setAdapter(oncekiBagislarAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Veri[] veriler = WebServisBaglanti.oncekiIstekler(this);
        oncekiBagislarAdapter.setVeriler(new Veri[0]);
        if(veriler != null) {
            if(veriler.length != 0) {
                Log.d("Veriler", veriler[0].getHastaAdi());
                oncekiBagislarAdapter.setVeriler(veriler);
            } else {
                Toast.makeText(this, "Önceden Bağış İsteğiniz Yok!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Veriler çekilemiyor!", Toast.LENGTH_LONG).show();
        }
    }
}


