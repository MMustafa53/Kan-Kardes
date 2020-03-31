package org.revay.android.kankardes.veri;

import org.revay.android.kankardes.veri.WebServisClasses.*;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

public class WebServisBaglanti {

    public static void kullanici_ekle(final AppCompatActivity activity){
        new KullaniciEkle(activity).execute();
    }

    public static void kullanici_guncelle(final PreferenceFragmentCompat activity){
        new KullaniciGuncelle(activity).execute();
    }

    public static void yeniBagisIstegi(final AppCompatActivity activity, Veri veri){
        new YeniBagisIstegi(activity, veri).execute();
    }

    public static Veri[] bagislariGetir(final AppCompatActivity activity){
        AsyncTask<Void, Void, Veri[]> asyncTask = new BagislariGetir(activity);

        Veri[] veriler = null;
        try {
            veriler = asyncTask.execute().get();
        } catch (Exception e){
            e.printStackTrace();
        }

        return veriler;
    }

    public static Veri[] oncekiIstekler(final AppCompatActivity activity){
        AsyncTask<Void, Void, Veri[]> asyncTask = new OncekiIstekler(activity);

        Veri[] veriler = null;
        try {
            veriler = asyncTask.execute().get();
        } catch (Exception e){
            e.printStackTrace();
        }

        return veriler;
    }

    public static void istek_iptal(final View view, final long id){
        new IstekIptal(view, id).execute();
    }

    public static void oneri_sikayet(final Context context, String konu, String baslik, String icerik){
        AsyncTask<Void, Void, Void> asyncTask = new OneriSikayet(context, konu, baslik, icerik).execute();
    }

    public static void arama_logla(final Context context, final long id) {
        AsyncTask<Void, Void, Void> asyncTask = new AramaLogla(context, id).execute();
    }
}

