package com.agripunya.manajemendatabuku.dao;


import com.agripunya.manajemendatabuku.entity.Peminjaman;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanDAO {
    public List<Peminjaman> getAllPeminjaman() throws SQLException {
        List<Peminjaman> daftarPeminjaman = new ArrayList<>();
        Connection connection = DatabaseUtil.getConnection();
        String sql = "SELECT * FROM peminjaman";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

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

        resultSet.close();
        statement.close();
        connection.close();
        return daftarPeminjaman;
    }

    public void tambahPeminjaman(Peminjaman peminjaman) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "INSERT INTO peminjaman (id_buku, id_peminjam, tanggal_pinjam, tanggal_kembali, denda) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, peminjaman.getIdBuku());
        preparedStatement.setInt(2, peminjaman.getIdPeminjam());
        preparedStatement.setDate(3, new java.sql.Date(peminjaman.getTanggalPinjam().getTime()));
        preparedStatement.setDate(4, new java.sql.Date(peminjaman.getTanggalKembali().getTime()));
        preparedStatement.setInt(5, peminjaman.getDenda());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public Peminjaman getPeminjamanById(int idPeminjaman) {
        return null;
    }
}
