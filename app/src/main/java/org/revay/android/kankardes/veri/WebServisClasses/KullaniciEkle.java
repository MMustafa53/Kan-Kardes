package org.revay.android.kankardes.veri.WebServisClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import org.revay.android.kankardes.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Casper on 14.4.2018.
 */

public class KullaniciEkle extends AsyncTask<Void, Void, Void> {
    private StringBuilder sb;
    private JSONObject mesajJson = null;
    private WeakReference<AppCompatActivity> weakReference;

    public KullaniciEkle(AppCompatActivity activity){
        setWeakReference(new WeakReference<>(activity));
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            Context context = getWeakReference().get().getBaseContext();
            if(context == null)
                return null;

            URL url = new URL("https://"+ context.getString(R.string.siteUrl) +"/"+ context.getString(R.string.sitePath) +"/kullanici_ekle.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput (true);
            urlConnection.setDoOutput (true);
            urlConnection.setUseCaches (false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Host", context.getString(R.string.siteUrl));

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            String api_id = sharedPreferences.getString(context.getString(R.string.pref_id_key), context.getString(R.string.pref_id_default));
            String ad_soyad = sharedPreferences.getString(context.getString(R.string.pref_ad_soyad_key), context.getString(R.string.pref_ad_soyad_default));
            String telefon = sharedPreferences.getString(context.getString(R.string.pref_tel_no_key), context.getString(R.string.pref_tel_no_default));
            String sehir = sharedPreferences.getString(context.getString(R.string.pref_yasanilan_yer_key), context.getString(R.string.pref_yasanilan_yer_default));
            String kan_grubu = sharedPreferences.getString(context.getString(R.string.pref_kan_grubu_key), context.getString(R.string.pref_kan_grubu_default));

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", api_id);
            jsonParam.put("ad_soyad", ad_soyad);
            jsonParam.put("telefon", telefon);
            jsonParam.put("sehir", sehir);
            jsonParam.put("kan_grubu", kan_grubu);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            String jsonString = jsonParam.toString();
            Log.d("JSONVeri", jsonString);
            outputStream.writeBytes(URLEncoder.encode(jsonString,"UTF-8"));
            outputStream.flush();
            outputStream.close();

            setSb(new StringBuilder());
            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    getSb().append(line + "\n");
                }
                br.close();

                System.out.println(""+ getSb().toString());

                JSONObject sonuc = new JSONObject(getSb().toString());
                setMesajJson(sonuc.getJSONObject("mesaj"));
                SharedPreferences.Editor editor = sharedPreferences.edit();


                editor.putLong(context.getString(R.string.pref_vid_key), getMesajJson().getLong("id"));
                editor.commit();
                Log.d("VID", String.valueOf(sharedPreferences.getLong(context.getString(R.string.pref_vid_key), 0)));

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (Exception e){
            Log.e("Web Sayfa Sonucu: ", getSb().toString());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Context context = getWeakReference().get().getBaseContext();
        if(context == null)
            return;

        if(!getMesajJson().has("id"))
            Toast.makeText(context, getMesajJson().toString(), Toast.LENGTH_LONG).show();
    }

    private StringBuilder getSb() {
        return sb;
    }

    private void setSb(StringBuilder sb) {
        this.sb = sb;
    }

    private JSONObject getMesajJson() {
        return mesajJson;
    }

    private void setMesajJson(JSONObject mesajJson) {
        this.mesajJson = mesajJson;
    }

    private WeakReference<AppCompatActivity> getWeakReference() {
        return weakReference;
    }

    private void setWeakReference(WeakReference<AppCompatActivity> weakReference) {
        this.weakReference = weakReference;
    }
}
