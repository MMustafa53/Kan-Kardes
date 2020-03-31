package org.revay.android.kankardes.veri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Veri {
    private long id;
    private String kanGrubu;
    private String hastaneAdi;
    private String hastaAdi;
    private String telefon;
    private String sehir;
    private long istekZamani;
    private boolean aktif;

    public Veri(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKanGrubu() {
        return kanGrubu;
    }

    public void setKanGrubu(String kanGrubu) {
        this.kanGrubu = kanGrubu;
    }

    public String getHastaneAdi() {
        return hastaneAdi;
    }

    public void setHastaneAdi(String hastaneAdi) {
        this.hastaneAdi = hastaneAdi;
    }

    public String getHastaAdi() {
        return hastaAdi;
    }

    public void setHastaAdi(String hastaAdi) {
        this.hastaAdi = hastaAdi;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public long getIstekZamaniLong(){
        return istekZamani;
    }

    public String getIstekZamani() {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(istekZamani);
        return simpleDateFormat.format(date);
    }

    public void setIstekZamani(long istekZamani) {
        this.istekZamani = istekZamani;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(int aktif) {
        this.aktif = aktif == 1;
    }
}
