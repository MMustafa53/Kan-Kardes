package org.revay.android.kankardes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.revay.android.kankardes.ayarlar.AyarlarActivity;
import org.revay.android.kankardes.veri.Veri;
import org.revay.android.kankardes.veri.WebServisBaglanti;


public class YeniIstek extends AppCompatActivity {

    EditText editTextAdSoyad, editTextTelefon, editTextHastane;
    Spinner spinnerSehir, spinnerKanGrubu;
    RadioGroup radioGroupBilgiSec;
    ImageButton imbtn;
    final int PLACE_PICKER_REQUEST = 1;
    final int PLACE_AUTOCOMPLATE_REQUEST_CODE = 1;
    boolean konumFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_istek);

        try {
            int off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            if (off == 0) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(YeniIstek.this);
                alertBuilder.setMessage("GPS erişimi gerekli");
                alertBuilder.setNegativeButton("Şimdi değil!", null);
                alertBuilder.setPositiveButton("Tamam!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent onGps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(onGps);
                        konumFlag = true;
                    }
                });

                alertBuilder.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        imbtn = findViewById(R.id.plcButton);
        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(YeniIstek.this), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        radioGroupBilgiSec = findViewById(R.id.radioGroupBilgiSecim);
        radioGroupBilgiSec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                bilgiGetir();
            }
        });

        editTextAdSoyad = findViewById(R.id.editTextYeniIstekAdSoyad);
        editTextTelefon = findViewById(R.id.editTextYeniIstekTelNo);
        editTextHastane = findViewById(R.id.editTextYeniIstekHastane);

        spinnerSehir = findViewById(R.id.spinnerYeniIstekSehir);
        spinnerKanGrubu = findViewById(R.id.spinnerYeniIstekKanGrubu);
        bilgiGetir();

        editTextHastane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(Place.TYPE_COUNTRY)
                        .setCountry("TR")
                        .build();
                try {
                    Intent autocomp = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(YeniIstek.this);
                    startActivityForResult(autocomp, PLACE_AUTOCOMPLATE_REQUEST_CODE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yeni_istek, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ayarlar1) {
            Intent intent = new Intent(this, AyarlarActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void bilgiGetir() {
        int seciliId = radioGroupBilgiSec.getCheckedRadioButtonId();
        if (seciliId == R.id.radioButtonKendisi) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            String adSoyad = sharedPreferences.getString(getString(R.string.pref_ad_soyad_key), getString(R.string.pref_ad_soyad_default));
            String telefon = sharedPreferences.getString(getString(R.string.pref_tel_no_key), getString(R.string.pref_tel_no_default));
            String sehir = sharedPreferences.getString(getString(R.string.pref_yasanilan_yer_key), getString(R.string.pref_yasanilan_yer_default));
            String kanGrubu = sharedPreferences.getString(getString(R.string.pref_kan_grubu_key), getString(R.string.pref_kan_grubu_default));

            String[] sehirler = getResources().getStringArray(R.array.turkey_city);
            String[] kanGruplari = getResources().getStringArray(R.array.kan_gruplari);

            int sehirPosition = 0, kanGrubuPosition = 0;
            for (int i = 0; i < sehirler.length; i++) {
                String item = sehirler[i];
                if (item.equals(sehir)) {
                    sehirPosition = i;
                    break;
                }
            }

            for (int i = 0; i < kanGruplari.length; i++) {
                String item = kanGruplari[i];
                if (item.equals(kanGrubu)) {
                    kanGrubuPosition = i;
                    break;
                }
            }

            editTextAdSoyad.setText(adSoyad);
            editTextTelefon.setText(telefon);
            spinnerSehir.setSelection(sehirPosition);
            spinnerKanGrubu.setSelection(kanGrubuPosition);

            editTextAdSoyad.setEnabled(false);
            editTextTelefon.setEnabled(false);
            spinnerSehir.setEnabled(false);
            spinnerKanGrubu.setEnabled(false);
        } else {
            editTextAdSoyad.setText("");
            editTextTelefon.setText("");
            spinnerSehir.setSelection(0);
            spinnerKanGrubu.setSelection(0);

            editTextAdSoyad.setEnabled(true);
            editTextTelefon.setEnabled(true);
            spinnerSehir.setEnabled(true);
            spinnerKanGrubu.setEnabled(true);
        }
    }

    public void kaydet(View view) {
        Veri veri = new Veri();
        veri.setHastaAdi(editTextAdSoyad.getText().toString().trim());
        veri.setTelefon(editTextTelefon.getText().toString().trim());
        veri.setSehir(((String) spinnerSehir.getSelectedItem()).trim());
        veri.setKanGrubu(((String) spinnerKanGrubu.getSelectedItem()).trim());
        veri.setHastaneAdi(editTextHastane.getText().toString().trim());
        veri.setIstekZamani(System.currentTimeMillis());

        if(!veri.getHastaneAdi().equals("") && !veri.getTelefon().equals("") && !veri.getHastaAdi().equals("") &&
                !veri.getSehir().equals("") && !veri.getKanGrubu().equals("")) {
            WebServisBaglanti.yeniBagisIstegi(this, veri);
            Intent intent = getIntent();
            navigateUpTo(intent);
        } else {
            Toast.makeText(this, "Lütfen tüm bilgileri eksiksiz giriniz!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferenceAyarla();
    }

    private void sharedPreferenceAyarla(){
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);

        String id = sharedPreferences.getString(getString(R.string.pref_id_key), getString(R.string.pref_id_default));
        if (id.equals(getString(R.string.pref_id_default))){
            Intent intent = new Intent(this, IlkGiris.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                boolean hospitalControl = false;
                for (int i : place.getPlaceTypes()) {
                    Log.d("P IDS", "onActivityResult: " + i);
                    if (i == Place.TYPE_DOCTOR || i == Place.TYPE_HEALTH || i == Place.TYPE_HOSPITAL || i == Place.TYPE_ROUTE) {
                        editTextHastane.setText(place.getName());
                        hospitalControl = true;
                        break;
                    }
                }
                if (!hospitalControl) {
                    Toast.makeText(YeniIstek.this, "LÜTFEN HASTANE SEÇİNİZ", Toast.LENGTH_SHORT).show();
                }
            }
        }
            /*if(requestCode == PLACE_AUTOCOMPLATE_REQUEST_CODE){
                if(resultCode == RESULT_OK){
                    Place plc = PlaceAutocomplete.getPlace(this,data);
                    for(int i : plc.getPlaceTypes()){
                        if(i == Place.TYPE_DOCTOR || i == Place.TYPE_HEALTH || i == Place.TYPE_HOSPITAL){
                            editTextHastane.setText(plc.getName());
                        }
                        else{
                            Toast.makeText(YeniIstek.this,"LÜTFEN HASTANE seçin",Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }*/

    }
}
