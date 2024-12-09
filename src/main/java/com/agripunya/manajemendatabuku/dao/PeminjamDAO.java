package com.agripunya.manajemendatabuku.dao;

import com.agripunya.manajemendatabuku.entity.Peminjam;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeminjamDAO {
    public List<Peminjam> getAllPeminjam() throws SQLException {
        List<Peminjam> daftarPeminjam = new ArrayList<>();
        Connection connection = DatabaseUtil.getConnection();
        String sql = "SELECT * FROM peminjam";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Peminjam peminjam = new Peminjam(
                    resultSet.getInt("id"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat"),
                    resultSet.getString("telepon")
            );
            daftarPeminjam.add(peminjam);
        }

        resultSet.close();
        statement.close();
        connection.close();
        return daftarPeminjam;
    }

    public void tambahPeminjam(Peminjam peminjam) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "INSERT INTO peminjam (nama, alamat, telepon) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, peminjam.getNama());
        preparedStatement.setString(2, peminjam.getAlamat());
        preparedStatement.setString(3, peminjam.getTelepon());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }
}
