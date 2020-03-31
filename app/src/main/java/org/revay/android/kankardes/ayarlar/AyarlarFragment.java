package org.revay.android.kankardes.ayarlar;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import org.revay.android.kankardes.R;
import org.revay.android.kankardes.veri.WebServisBaglanti;

public class AyarlarFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_ayarlar);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        final SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        int preferenceCount = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < preferenceCount; i++){
            Preference p = preferenceScreen.getPreference(i);
            Log.d("Pref Name ",p.getKey());
            final String deger = sharedPreferences.getString(p.getKey(), "");

            if (p instanceof EditTextPreference) {
                p.setSummary(deger);

                p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String yeniDeger = String.valueOf(newValue);
                        String key = preference.getKey();

                        if (yeniDeger.equals("")) {
                            Toast.makeText(getContext(), "Lütfen değerleri boş bırakmayınız!", Toast.LENGTH_LONG).show();
                            return false;
                        }

                        if(key.equals(getString(R.string.pref_tel_no_key))) {
                            if(yeniDeger.length() != 10) {
                                Toast.makeText(getContext(), "Lütfen telefon numaranızı doğru giriniz!", Toast.LENGTH_LONG).show();
                                return false;
                            } else if(yeniDeger.charAt(0) == '0') {
                                Toast.makeText(getContext(), "Telefon numaranızı başında 0 olmadan giriniz!", Toast.LENGTH_LONG).show();
                                return false;
                            }
                        }

                        return true;
                    }
                });
            } else if(p instanceof ListPreference){
                ListPreference listPreference = (ListPreference) p;
                int seciliID = listPreference.findIndexOfValue(deger);

                if (seciliID >= 0)
                    listPreference.setSummary(listPreference.getEntries()[seciliID]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);

        if (p != null){
            String deger = sharedPreferences.getString(key, "");
            p.setSummary(deger);
            WebServisBaglanti.kullanici_guncelle(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
