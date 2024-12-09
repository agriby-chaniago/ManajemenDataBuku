package com.agripunya.manajemendatabuku.dao;

import com.agripunya.manajemendatabuku.entity.Pengembalian;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PengembalianDAO {
    public List<Pengembalian> getAllPengembalian() throws SQLException {
        List<Pengembalian> daftarPengembalian = new ArrayList<>();
        Connection connection = DatabaseUtil.getConnection();
        String sql = "SELECT * FROM pengembalian";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Pengembalian pengembalian = new Pengembalian(
                    resultSet.getInt("id"),
                    resultSet.getInt("id_peminjaman"),
                    resultSet.getDate("tanggal_dikembalikan"),
                    resultSet.getInt("denda")
            );
            daftarPengembalian.add(pengembalian);
        }

        resultSet.close();
        statement.close();
        connection.close();
        return daftarPengembalian;
    }

    public void tambahPengembalian(Pengembalian pengembalian) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "INSERT INTO pengembalian (id_peminjaman, tanggal_dikembalikan, denda) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, pengembalian.getIdPeminjaman());
        preparedStatement.setDate(2, new java.sql.Date(pengembalian.getTanggalDikembalikan().getTime()));
        preparedStatement.setInt(3, pengembalian.getDenda());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public void hapusPengembalian(int id) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "DELETE FROM pengembalian WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public int hitungDenda(int idPeminjaman, Date tanggalDikembalikan) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "SELECT tanggal_kembali FROM peminjaman WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idPeminjaman);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Date tanggalKembali = resultSet.getDate("tanggal_kembali");
            long selisihHari = (tanggalDikembalikan.getTime() - tanggalKembali.getTime()) / (1000 * 60 * 60 * 24);
            if (selisihHari > 0) {
                return (int) selisihHari * 1000; // Misalnya denda Rp1000 per hari
            }
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return 0; // Tidak ada denda
    }

}
