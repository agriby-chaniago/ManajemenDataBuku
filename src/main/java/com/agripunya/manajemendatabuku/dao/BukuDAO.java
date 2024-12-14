package com.agripunya.manajemendatabuku.dao;

import com.agripunya.manajemendatabuku.entity.Buku;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>BukuDAO</h1>
 * <p>
 * Kelas Data Access Object (DAO) untuk entitas Buku. Kelas ini bertanggung jawab untuk
 * melakukan operasi database terkait dengan tabel `buku`.
 * </p>
 *
 * <h2>Fungsi Utama:</h2>
 * <ul>
 *     <li>Mengambil semua data buku dari tabel `buku`.</li>
 *     <li>Menambahkan buku baru ke tabel `buku`.</li>
 * </ul>
 *
 * <h2>Dependencies:</h2>
 * <ul>
 *     <li>{@link com.agripunya.manajemendatabuku.entity.Buku}</li>
 *     <li>{@link com.agripunya.manajemendatabuku.util.DatabaseUtil}</li>
 * </ul>
 *
 * <h2>Catatan:</h2>
 * <p>Pastikan koneksi database dikonfigurasi dengan benar di {@link DatabaseUtil}.</p>
 *
 * @author [Agriby D. Chaniago]
 * @version 1.0
 */
public class BukuDAO {

    /**
     * Mengambil semua data buku dari tabel `buku`.
     *
     * @return List berisi objek {@link Buku} yang merepresentasikan data buku di tabel.
     * @throws SQLException Jika terjadi kesalahan saat mengakses database.
     */
    public List<Buku> getAllBuku() throws SQLException {
        List<Buku> daftarBuku = new ArrayList<>();
        Connection connection = DatabaseUtil.getConnection();
        String sql = "SELECT * FROM buku";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Iterasi hasil query dan memasukkan data ke dalam daftarBuku
        while (resultSet.next()) {
            Buku buku = new Buku(
                    resultSet.getInt("id"),
                    resultSet.getString("judul"),
                    resultSet.getString("penulis"),
                    resultSet.getString("penerbit"),
                    resultSet.getInt("tahun_terbit"),
                    resultSet.getInt("jumlah")
            );
            daftarBuku.add(buku);
        }

        // Menutup resources untuk menghindari kebocoran memori
        resultSet.close();
        statement.close();
        connection.close();
        return daftarBuku;
    }

    /**
     * Menambahkan buku baru ke tabel `buku`.
     *
     * @param buku Objek {@link Buku} yang akan ditambahkan ke database.
     * @throws SQLException Jika terjadi kesalahan saat mengakses database.
     */
    public void tambahBuku(Buku buku) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "INSERT INTO buku (judul, penulis, penerbit, tahun_terbit, jumlah) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // Menyisipkan data buku ke dalam query
        preparedStatement.setString(1, buku.getJudul());
        preparedStatement.setString(2, buku.getPenulis());
        preparedStatement.setString(3, buku.getPenerbit());
        preparedStatement.setInt(4, buku.getTahunTerbit());
        preparedStatement.setInt(5, buku.getJumlah());

        // Menjalankan query
        preparedStatement.executeUpdate();

        // Menutup resources
        preparedStatement.close();
        connection.close();
    }
}
