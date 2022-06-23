package com.example.perpustakaan.model;

import java.sql.Date;

public class PerpusModel {

    // Deklarasi Variable untuk menyimpan dan mengambil data perpustakaan
    private String id,Nama,JudulBuku;
    private String TglPinjam;
    private String TglKembali;

    public PerpusModel(String nama_anggota, String judul_buku, String tglPinjam, String tglKembali) {
        Nama = nama_anggota;
        JudulBuku = judul_buku;
        TglPinjam = tglPinjam;
        TglKembali = tglKembali;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getJudulBuku() {
        return JudulBuku;
    }

    public void setJudulBuku(String judulBuku) {
        JudulBuku = judulBuku;
    }

    public String getTglPinjam() {
        return TglPinjam;
    }

    public void setTglPinjam(String tglPinjam) {
        TglPinjam = tglPinjam;
    }

    public String getTglKembali() {
        return TglKembali;
    }

    public void setTglKembali(String tglKembali) {
        TglKembali = tglKembali;
    }
}
