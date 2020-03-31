package org.revay.android.kankardes.veri.WebServisClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
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

public class IstekIptal extends AsyncTask<Void, Void, Void> {
    private StringBuilder sb = new StringBuilder();
    private long id;
    private WeakReference<View> weakReference;

    public IstekIptal(View view, long id){
        setWeakReference(new WeakReference<>(view));
        setId(id);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            Context context = getWeakReference().get().getContext();
            if(context == null)
                return null;

            URL url = new URL("https://"+ context.getString(R.string.siteUrl) +"/"+ context.getString(R.string.sitePath) +"/istek_iptal.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches (false);
            urlConnection.setDoInput (true);
            urlConnection.setDoOutput (true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Host", context.getString(R.string.siteUrl));

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            long vid = sharedPreferences.getLong(context.getString(R.string.pref_vid_key), 0);
            String api_id = sharedPreferences.getString(context.getString(R.string.pref_id_key), context.getString(R.string.pref_id_default));

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("vid", vid);
            jsonParam.put("id", api_id);
            jsonParam.put("istek_id", getId());

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
        Context context = getWeakReference().get().getContext();
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

    private WeakReference<View> getWeakReference() {
        return weakReference;
    }

    private void setWeakReference(WeakReference<View> weakReference) {
        this.weakReference = weakReference;
    }

    private long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }
}
