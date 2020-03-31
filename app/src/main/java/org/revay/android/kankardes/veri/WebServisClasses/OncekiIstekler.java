package org.revay.android.kankardes.veri.WebServisClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Casper on 14.4.2018.
 */

public class OncekiIstekler extends AsyncTask<Void, Void, Veri[]> {
    private StringBuilder sb = new StringBuilder();
    private JSONObject sonuc = null;
    private JSONObject mesajJson = null;
    private Veri[] veriler = null;
    private WeakReference<AppCompatActivity> weakReference;

    public OncekiIstekler(AppCompatActivity activity){
        setWeakReference(new WeakReference<>(activity));
    }

    @Override
    protected Veri[] doInBackground(Void... voids) {
        try {
            Context context = getWeakReference().get().getBaseContext();
            if(context == null)
                return null;

            URL url = new URL("https://"+ context.getString(R.string.siteUrl) +"/"+ context.getString(R.string.sitePath) +"/onceki_istekler.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches (false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput (true);
            urlConnection.setDoOutput (true);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Host", context.getString(R.string.siteUrl));

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            long vid = sharedPreferences.getLong(context.getString(R.string.pref_vid_key), 0);
            String api_id = sharedPreferences.getString(context.getString(R.string.pref_id_key), context.getString(R.string.pref_id_default));

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("vid", vid);
            jsonParam.put("id", api_id);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            String jsonString = jsonParam.toString();
            Log.d("JSONVeri", jsonString);
            outputStream.writeBytes(URLEncoder.encode(jsonString,"UTF-8"));
            outputStream.flush();
            outputStream.close();


            int HttpResult = urlConnection.getResponseCode();
            System.out.println(HttpResult == HttpURLConnection.HTTP_OK);
            if(HttpResult == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    getSb().append(line + "\n");
                }
                br.close();

                setSonuc(new JSONObject(getSb().toString()));
                setMesajJson(getSonuc().getJSONObject("mesaj"));

                System.out.println(""+ getSb().toString());
            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            JSONArray verilerJson = getMesajJson().getJSONArray("veriler");
            Log.d("VerilerLog", verilerJson.toString());

            if(!verilerJson.toString().equals("")) {
                setVeriler(new Veri[verilerJson.length()]);
                for (int i = 0; i < verilerJson.length(); i++) {
                    JSONObject veri = verilerJson.getJSONObject(i);
                    Log.d("GelenVeri", veri.toString());
                    getVeriler()[i] = new Veri();
                    getVeriler()[i].setId(veri.getLong("id"));
                    getVeriler()[i].setHastaAdi(veri.getString("ad_soyad"));
                    getVeriler()[i].setHastaneAdi(veri.getString("hastane"));
                    getVeriler()[i].setTelefon(veri.getString("telefon"));
                    getVeriler()[i].setKanGrubu(veri.getString("kan_grubu"));
                    getVeriler()[i].setAktif(veri.getInt("aktif"));
                    SimpleDateFormat simpleDateFormat =
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = simpleDateFormat.parse(veri.getString("istek_zamani"));
                    getVeriler()[i].setIstekZamani(date.getTime());
                }
            } else {
                setVeriler(new Veri[0]);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return getVeriler();
    }

    @Override
    protected void onPostExecute(Veri[] veris) {
        super.onPostExecute(veris);

        Context context = getWeakReference().get().getBaseContext();
        Log.d("Con", String.valueOf(context == null));
        if(context == null)
            return;

        if(getVeriler() == null){
            try {
                Toast.makeText(context, getSonuc().getString("mesaj"), Toast.LENGTH_LONG).show();
            } catch (JSONException ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private StringBuilder getSb() {
        return sb;
    }

    private void setSb(StringBuilder sb) {
        this.sb = sb;
    }

    private JSONObject getSonuc() {
        return sonuc;
    }

    private void setSonuc(JSONObject sonuc) {
        this.sonuc = sonuc;
    }

    private JSONObject getMesajJson() {
        return mesajJson;
    }

    private void setMesajJson(JSONObject mesajJson) {
        this.mesajJson = mesajJson;
    }

    private Veri[] getVeriler() {
        return veriler;
    }

    private void setVeriler(Veri[] veriler) {
        this.veriler = veriler;
    }

    private WeakReference<AppCompatActivity> getWeakReference() {
        return weakReference;
    }

    private void setWeakReference(WeakReference<AppCompatActivity> weakReference) {
        this.weakReference = weakReference;
    }
}