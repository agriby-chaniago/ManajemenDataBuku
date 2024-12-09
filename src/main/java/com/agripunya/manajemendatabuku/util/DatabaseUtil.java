package com.agripunya.manajemendatabuku.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    // Konfigurasi database
    private static final String URL = "jdbc:mysql://localhost:3306/manajemen_buku";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Metode untuk mendapatkan koneksi
    public static Connection getConnection() throws SQLException {
        try {
            // Memastikan driver MySQL dimuat
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                System.out.println("Koneksi ke database berhasil!");
            }
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL tidak ditemukan: " + e.getMessage());
            throw new SQLException("Driver tidak ditemukan");
        } catch (SQLException e) {
            System.out.println("Gagal terhubung ke database: " + e.getMessage());
            throw e;
        }
    }


    // Metode untuk menutup koneksi
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}
