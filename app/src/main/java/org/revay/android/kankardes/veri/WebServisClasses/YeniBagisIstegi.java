package org.revay.android.kankardes.veri.WebServisClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.revay.android.kankardes.R;
import org.revay.android.kankardes.veri.Veri;

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

public class YeniBagisIstegi extends AsyncTask<Void, Void, Void> {
    private StringBuilder sb = new StringBuilder();
    private Veri veri;
    private WeakReference<AppCompatActivity> weakReference;

    public YeniBagisIstegi(AppCompatActivity activity, Veri veri){
        setWeakReference(new WeakReference<>(activity));
        this.setVeri(veri);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            Context context = getWeakReference().get().getBaseContext();
            if(context == null)
                return null;

            URL url = new URL("https://"+ context.getString(R.string.siteUrl) +"/"+ context.getString(R.string.sitePath) +"/yeni_istek.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput (true);
            urlConnection.setDoOutput (true);
            urlConnection.setUseCaches (false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Host", context.getString(R.string.siteUrl));

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            long vid = sharedPreferences.getLong(context.getString(R.string.pref_vid_key), 0);
            String api_id = sharedPreferences.getString(context.getString(R.string.pref_id_key), context.getString(R.string.pref_id_default));
            String ad_soyad = getVeri().getHastaAdi();
            String telefon = getVeri().getTelefon();
            String sehir = getVeri().getSehir();
            String kan_grubu = getVeri().getKanGrubu();
            String hastane_adi = getVeri().getHastaneAdi();
            long istek_zamani = getVeri().getIstekZamaniLong();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("vid", vid);
            jsonParam.put("id", api_id);
            jsonParam.put("ad_soyad", ad_soyad);
            jsonParam.put("telefon", telefon);
            jsonParam.put("sehir", sehir);
            jsonParam.put("kan_grubu", kan_grubu);
            jsonParam.put("hastaneAdi", hastane_adi);
            jsonParam.put("istek_zamani", istek_zamani);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            String jsonString = jsonParam.toString();
            Log.d("JSONVeri", jsonString);
            outputStream.writeBytes(URLEncoder.encode(jsonString,"UTF-8"));
            outputStream.flush();
            outputStream.close();


            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    getSb().append(line + "\n");
                }
                br.close();

                System.out.println(""+ getSb().toString());
            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (Exception e){
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

        try {
            JSONObject sonuc = new JSONObject(getSb().toString());
            Toast.makeText(context, sonuc.getString("mesaj"), Toast.LENGTH_LONG).show();
        } catch (JSONException ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private StringBuilder getSb() {
        return sb;
    }

    private void setSb(StringBuilder sb) {
        this.sb = sb;
    }

    private Veri getVeri() {
        return veri;
    }

    private void setVeri(Veri veri) {
        this.veri = veri;
    }

    private WeakReference<AppCompatActivity> getWeakReference() {
        return weakReference;
    }

    private void setWeakReference(WeakReference<AppCompatActivity> weakReference) {
        this.weakReference = weakReference;
    }
}
