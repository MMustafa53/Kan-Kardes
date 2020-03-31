package org.revay.android.kankardes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.revay.android.kankardes.veri.WebServisBaglanti;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class IlkGiris extends AppCompatActivity {

    EditText editTextAdSoyad;
    EditText editTextTelNo;
    Spinner spinnerSehir;
    Spinner spinnerKanGrubu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilk_giris);

        editTextAdSoyad =  findViewById(R.id.editTextAdSoyad);
        spinnerSehir =  findViewById(R.id.spinnerSehir);
        spinnerKanGrubu =  findViewById(R.id.spinnerKanGrubu);
        editTextTelNo =  findViewById(R.id.editTextTelNo);
    }

    public void kaydet(View view){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String adSoyad = editTextAdSoyad.getText().toString();
        String telNo = editTextTelNo.getText().toString();
        int yasanilanSehirId = spinnerSehir.getSelectedItemPosition();
        String yasanilanSehir = spinnerSehir.getSelectedItem().toString();
        String kanGrubu = spinnerKanGrubu.getSelectedItem().toString();

        if (!adSoyad.equals("") && !telNo.equals("") && yasanilanSehirId != 0) {
            String hashString = (adSoyad + "+" + telNo).toLowerCase(new Locale("tr", "TR"));

            int chLength = editTextTelNo.getText().toString().trim().length();
            if (chLength < 10 || editTextTelNo.getText().toString().charAt(0) == '0') {
                Toast.makeText(getApplicationContext(), "Lütfen Numaranızı Başında Sıfır Olmadan Giriniz", Toast.LENGTH_LONG).show();
            } else {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.pref_id_key), md5(hashString));
                editor.putString(getString(R.string.pref_ad_soyad_key), adSoyad);
                editor.putString(getString(R.string.pref_tel_no_key), telNo);
                editor.putString(getString(R.string.pref_yasanilan_yer_key), yasanilanSehir);
                editor.putString(getString(R.string.pref_kan_grubu_key), kanGrubu);
                editor.commit();


                WebServisBaglanti.kullanici_ekle(this);

                Toast.makeText(this, "Kayıt Oluşturuldu!\nProgram Yeniden Başlatılıyor!", Toast.LENGTH_LONG).show();

                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Lütfen Yukarıdaki Bilgileri Doğru Bir Şekilde Giriniz",Toast.LENGTH_LONG).show();
        }
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
