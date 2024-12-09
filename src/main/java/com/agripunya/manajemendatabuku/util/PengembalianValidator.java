package com.agripunya.manajemendatabuku.util;

import com.agripunya.manajemendatabuku.dao.PeminjamanDAO;
import com.agripunya.manajemendatabuku.entity.Peminjaman;

import java.util.Date;

public class PengembalianValidator {

    private PeminjamanDAO peminjamanDAO;

    public PengembalianValidator() {
        this.peminjamanDAO = new PeminjamanDAO();
    }

    /**
     * Validasi apakah ID peminjaman valid dan ada di database.
     * @param idPeminjaman ID peminjaman.
     * @return true jika valid, false jika tidak valid.
     */
    public boolean isValidPeminjaman(int idPeminjaman) {
        try {
            Peminjaman peminjaman = peminjamanDAO.getPeminjamanById(idPeminjaman);
            return peminjaman != null; // Valid jika peminjaman ditemukan
        } catch (Exception e) {
            System.out.println("Error validasi peminjaman: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validasi apakah tanggal dikembalikan tidak lebih awal dari tanggal pinjam.
     * @param tanggalPinjam Tanggal pinjam.
     * @param tanggalDikembalikan Tanggal dikembalikan.
     * @return true jika valid, false jika tidak valid.
     */
    public boolean isValidTanggal(Date tanggalPinjam, Date tanggalDikembalikan) {
        return !tanggalDikembalikan.before(tanggalPinjam);
    }

    /**
     * Validasi apakah denda yang dihitung sesuai aturan bisnis.
     * @param tanggalKembali Tanggal kembali seharusnya.
     * @param tanggalDikembalikan Tanggal dikembalikan.
     * @param denda Denda yang diberikan.
     * @return true jika valid, false jika tidak valid.
     */
    public boolean isValidDenda(Date tanggalKembali, Date tanggalDikembalikan, int denda) {
        long selisihHari = (tanggalDikembalikan.getTime() - tanggalKembali.getTime()) / (1000 * 60 * 60 * 24);
        int dendaSebenarnya = (selisihHari > 0) ? (int) selisihHari * 1000 : 0; // Rp1000/hari
        return denda == dendaSebenarnya;
    }
}

