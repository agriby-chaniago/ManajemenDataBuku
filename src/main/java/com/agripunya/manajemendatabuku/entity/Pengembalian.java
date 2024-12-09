package com.agripunya.manajemendatabuku.entity;

import java.util.Date;

public class Pengembalian {
    private int id;
    private int idPeminjaman;
    private Date tanggalDikembalikan;
    private int denda;

    // Constructor
    public Pengembalian(int id, int idPeminjaman, Date tanggalDikembalikan, int denda) {
        this.id = id;
        this.idPeminjaman = idPeminjaman;
        this.tanggalDikembalikan = tanggalDikembalikan;
        this.denda = denda;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    public Date getTanggalDikembalikan() {
        return tanggalDikembalikan;
    }

    public void setTanggalDikembalikan(Date tanggalDikembalikan) {
        this.tanggalDikembalikan = tanggalDikembalikan;
    }

    public int getDenda() {
        return denda;
    }

    public void setDenda(int denda) {
        this.denda = denda;
    }
}
