package com.agripunya.manajemendatabuku.dao;

import com.agripunya.manajemendatabuku.entity.Peminjaman;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>PeminjamanDAO</h1>
 * <p>
 * Kelas Data Access Object (DAO) untuk entitas Peminjaman. Kelas ini bertanggung jawab untuk
 * melakukan operasi database terkait dengan tabel `peminjaman`.
 * </p>
 *
 * <h2>Fungsi Utama:</h2>
 * <ul>
 *     <li>Mengambil semua data peminjaman dari tabel `peminjaman`.</li>
 *     <li>Menambahkan data peminjaman baru ke tabel `peminjaman`.</li>
 *     <li>Mengambil data peminjaman berdasarkan ID (fitur dalam pengembangan).</li>
 * </ul>
 *
 * <h2>Dependencies:</h2>
 * <ul>
 *     <li>{@link com.agripunya.manajemendatabuku.entity.Peminjaman}</li>
 *     <li>{@link com.agripunya.manajemendatabuku.util.DatabaseUtil}</li>
 * </ul>
 *
 * <h2>Catatan:</h2>
 * <p>
 * - Pastikan tabel `peminjaman` di database telah dibuat sesuai struktur yang digunakan.
 * <br>
 * - Koneksi database harus dikonfigurasi dengan benar di kelas {@link DatabaseUtil}.
 * </p>
 *
 * <h2>Penggunaan:</h2>
 * <pre>{@code
 * PeminjamanDAO peminjamanDAO = new PeminjamanDAO();
 * List<Peminjaman> semuaPeminjaman = peminjamanDAO.getAllPeminjaman();
 * }</pre>
 *
 * @author
 * Agriby D. Chaniago
 * @version 1.0
 */
public class PeminjamanDAO {

    /**
     * Mengambil semua data peminjaman dari tabel `peminjaman`.
     *
     * @return List berisi objek {@link Peminjaman} yang merepresentasikan data peminjaman.
     * @throws SQLException Jika terjadi kesalahan saat mengakses database.
     */
    public List<Peminjaman> getAllPeminjaman() throws SQLException {
        List<Peminjaman> daftarPeminjaman = new ArrayList<>();
        Connection connection = DatabaseUtil.getConnection();
        String sql = "SELECT * FROM peminjaman";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Iterasi hasil query dan memasukkan data ke dalam daftarPeminjaman
        while (resultSet.next()) {
            Peminjaman peminjaman = new Peminjaman(
                    resultSet.getInt("id"),
                    resultSet.getInt("id_buku"),
                    resultSet.getInt("id_peminjam"),
                    resultSet.getDate("tanggal_pinjam"),
                    resultSet.getDate("tanggal_kembali"),
                    resultSet.getInt("denda")
            );
            daftarPeminjaman.add(peminjaman);
        }

        // Menutup resources untuk menghindari kebocoran memori
        resultSet.close();
        statement.close();
        connection.close();
        return daftarPeminjaman;
    }

    /**
     * Menambahkan data peminjaman baru ke tabel `peminjaman`.
     *
     * @param peminjaman Objek {@link Peminjaman} yang akan ditambahkan ke database.
     * @throws SQLException Jika terjadi kesalahan saat mengakses database.
     */
    public void tambahPeminjaman(Peminjaman peminjaman) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "INSERT INTO peminjaman (id_buku, id_peminjam, tanggal_pinjam, tanggal_kembali, denda) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // Menyisipkan data peminjaman ke dalam query
        preparedStatement.setInt(1, peminjaman.getIdBuku());
        preparedStatement.setInt(2, peminjaman.getIdPeminjam());
        preparedStatement.setDate(3, new java.sql.Date(peminjaman.getTanggalPinjam().getTime()));
        preparedStatement.setDate(4, new java.sql.Date(peminjaman.getTanggalKembali().getTime()));
        preparedStatement.setInt(5, peminjaman.getDenda());

        // Menjalankan query
        preparedStatement.executeUpdate();

        // Menutup resources
        preparedStatement.close();
        connection.close();
    }
}
