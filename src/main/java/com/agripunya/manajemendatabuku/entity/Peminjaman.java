package com.agripunya.manajemendatabuku.entity;

import java.util.Date;

public class Peminjaman {
    private int id;
    private int idBuku;
    private int idPeminjam;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private int denda;

    // Constructor
    public Peminjaman(int id, int idBuku, int idPeminjam, Date tanggalPinjam, Date tanggalKembali, int denda) {
        this.id = id;
        this.idBuku = idBuku;
        this.idPeminjam = idPeminjam;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.denda = denda;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public int getIdPeminjam() {
        return idPeminjam;
    }

    public void setIdPeminjam(int idPeminjam) {
        this.idPeminjam = idPeminjam;
    }

    public Date getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(Date tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }

    public Date getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(Date tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public int getDenda() {
        return denda;
    }

    public void setDenda(int denda) {
        this.denda = denda;
    }
}
