package com.agripunya.manajemendatabuku.dao;

import com.agripunya.manajemendatabuku.entity.Buku;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {
    public List<Buku> getAllBuku() throws SQLException {
        List<Buku> daftarBuku = new ArrayList<>();
        Connection connection = DatabaseUtil.getConnection();
        String sql = "SELECT * FROM buku";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

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

        resultSet.close();
        statement.close();
        connection.close();
        return daftarBuku;
    }

    public void tambahBuku(Buku buku) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "INSERT INTO buku (judul, penulis, penerbit, tahun_terbit, jumlah) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, buku.getJudul());
        preparedStatement.setString(2, buku.getPenulis());
        preparedStatement.setString(3, buku.getPenerbit());
        preparedStatement.setInt(4, buku.getTahunTerbit());
        preparedStatement.setInt(5, buku.getJumlah());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }
}
