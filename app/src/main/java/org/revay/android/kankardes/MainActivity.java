package org.revay.android.kankardes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.revay.android.kankardes.ayarlar.AyarlarActivity;
import org.revay.android.kankardes.veri.BagisIstekleriAdapter;
import org.revay.android.kankardes.veri.Veri;
import org.revay.android.kankardes.veri.WebServisBaglanti;

public class MainActivity extends AppCompatActivity {
    Context context;
    public SharedPreferences appPreferences;
    BagisIstekleriAdapter bagisIstekleriAdapter;
    RecyclerView recyclerViewBagisIstekleri;
    boolean isAppInstalled = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bagis_istekleri);

        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);

        String id1 = sharedPreferences.getString(getString(R.string.pref_id_key), getString(R.string.pref_id_default));
        if (id1.equals(getString(R.string.pref_id_default))){
            Toast.makeText(MainActivity.this,"LÜTFEN GİRİŞ YAPINIZ", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, IlkGiris.class);
            startActivity(intent);
        }

        recyclerViewBagisIstekleri =  findViewById(R.id.rv_bagis_istekleri);
        bagisIstekleriAdapter = new BagisIstekleriAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewBagisIstekleri.setLayoutManager(layoutManager);
        recyclerViewBagisIstekleri.setHasFixedSize(true);

        recyclerViewBagisIstekleri.setAdapter(bagisIstekleriAdapter);

        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled",true);
        if(isAppInstalled)
            createShortcut();
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), YeniIstek.class);
                startActivity(intent);
            }
        });
    }
    public void createShortcut(){
        Intent scut = new Intent(getApplicationContext(),MainActivity.class);
        scut.setAction(Intent.ACTION_MAIN);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,scut);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"Kan Kardeş");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource
                .fromContext(getApplicationContext(), R.drawable.red_blood_drop));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(intent);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean("isAppInstalled", false);
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();

        Veri[] veriler = WebServisBaglanti.bagislariGetir(this);
        bagisIstekleriAdapter.setVeriler(new Veri[0]);
        if(veriler != null) {
            if(veriler.length != 0) {
                Log.d("Veriler", veriler[0].getHastaAdi());
                bagisIstekleriAdapter.setVeriler(veriler);
            } else {
                Toast.makeText(this, "Şuanda İstek Yok!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Veriler çekilemiyor!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ayarlar1){
            SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);

            String id1 = sharedPreferences.getString(getString(R.string.pref_id_key), getString(R.string.pref_id_default));
            if (id1.equals(getString(R.string.pref_id_default))){
                Toast.makeText(MainActivity.this,"LÜTFEN GİRİŞ YAPINIZ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, IlkGiris.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(this, AyarlarActivity.class);
                startActivity(intent);
            }

            return true;
        }
        if(id == R.id.oncekitalepler){
            Intent intent = new Intent(this,OncekiBagislar.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.onerisikayet1){
            Intent intent = new Intent(this,OneriSikayet.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.hakkinda1){
            Intent intent = new Intent(this,Hakkinda.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
