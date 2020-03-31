package org.revay.android.kankardes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.revay.android.kankardes.veri.WebServisBaglanti;

public class OneriSikayet extends AppCompatActivity {
    EditText konuet,iceriket;
    Spinner oneriSikayet;
    Button gonder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oneri_sikayet);

        konuet = findViewById(R.id.oneriSikayetKonuet);
        iceriket = findViewById(R.id.oneriSikayetet);
        oneriSikayet = findViewById(R.id.oneriSikayetspin);
        gonder = findViewById(R.id.oneriSikayetgonder);

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!String.valueOf(oneriSikayet.getSelectedItem()).equals("Öneri/Şikayet Seçiniz") && !konuet.getText().toString().equals("") && !iceriket.getText().toString().equals("")){
                WebServisBaglanti.oneri_sikayet(v.getContext(),(String)oneriSikayet.getSelectedItem(),konuet.getText().toString(),iceriket
                .getText().toString());
                Intent intent = getIntent();
                navigateUpTo(intent);
                }
            }
        });

    }
}
