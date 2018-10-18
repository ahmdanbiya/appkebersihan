package com.ccdp.appkebersihan5.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Susi on 3/14/2017.
 */

public class Kebersihan implements Serializable {
    private int id;
    @SerializedName("keterangan")
    private String Keterangan;
    @SerializedName("userid")
    private String Userid;
    @SerializedName("tgllapor")
    private String Tgllapor;
    @SerializedName("lokasi")
    private String Lokasi;

    public Kebersihan(int id, String keterangan, String userid, String tgllapor, String lokasi) {
        this.id = id;
        Keterangan = keterangan;
        Userid = userid;
        Tgllapor = tgllapor;
        Lokasi = lokasi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getTgllapor() {
        return Tgllapor;
    }

    public void setTgllapor(String tgllapor) {
        Tgllapor = tgllapor;
    }

    public String getLokasi() {
        return Lokasi;
    }

    public void setLokasi(String lokasi) {
        Lokasi = lokasi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kebersihan that = (Kebersihan) o;

        if (id != that.id) return false;
        if (!Keterangan.equals(that.Keterangan)) return false;
        if (!Userid.equals(that.Userid)) return false;
        if (!Tgllapor.equals(that.Tgllapor)) return false;
        return Lokasi.equals(that.Lokasi);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Keterangan.hashCode();
        result = 31 * result + Userid.hashCode();
        result = 31 * result + Tgllapor.hashCode();
        result = 31 * result + Lokasi.hashCode();
        return result;
    }
}