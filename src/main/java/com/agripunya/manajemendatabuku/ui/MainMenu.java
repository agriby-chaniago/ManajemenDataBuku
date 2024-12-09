package com.agripunya.manajemendatabuku.ui;

import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainMenu extends JFrame {

    private JPanel contentPanel;
    private JPanel topPanel;  // Panel gabungan untuk button dan search field
    private JTextField searchField;  // Untuk menampung nilai pencarian

    public MainMenu() {
        setTitle("Manajemen Data Buku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Set Layout Manager
        setLayout(new BorderLayout());

        // Top Panel (Untuk tombol dan search field)
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));  // Layout vertikal
        add(topPanel, BorderLayout.NORTH);

        // Button Panel (untuk tombol seperti Home, Data Buku, dll.)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Horizontal layout with space
        topPanel.add(buttonPanel);

        // Create Buttons
        JButton homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(148, 30));  // Adjust size
        homeButton.addActionListener(e -> showHomeTable());
        buttonPanel.add(homeButton);

        JButton dataBukuButton = new JButton("Data Buku");
        dataBukuButton.setPreferredSize(new Dimension(148, 30));  // Adjust size
        dataBukuButton.addActionListener(e -> showDataBukuTable());
        buttonPanel.add(dataBukuButton);

        JButton dataPeminjamButton = new JButton("Data Peminjam");
        dataPeminjamButton.setPreferredSize(new Dimension(148, 30));  // Adjust size
        dataPeminjamButton.addActionListener(e -> showDataPeminjamTable());
        buttonPanel.add(dataPeminjamButton);

        JButton peminjamanButton = new JButton("Peminjaman");
        peminjamanButton.setPreferredSize(new Dimension(148, 30));  // Adjust size
        peminjamanButton.addActionListener(e -> showPeminjamanTable());
        buttonPanel.add(peminjamanButton);

        JButton pengembalianButton = new JButton("Pengembalian");
        pengembalianButton.setPreferredSize(new Dimension(148, 30));  // Adjust size
        pengembalianButton.addActionListener(e -> showPengembalianTable());
        buttonPanel.add(pengembalianButton);

        // Search Field and Button Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Space between components
        searchField = new JTextField(20); // Size of the search field
        JButton searchButton = new JButton("Search");

        // Add the search components to the panel
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add the search panel to the topPanel (after the buttonPanel)
        topPanel.add(searchPanel);

        // Add action listener for the search button
        searchButton.addActionListener(e -> performSearch());

        // Content Panel
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Default Home Table
        showHomeTable();
    }

    private void performSearch() {
        String searchTerm = searchField.getText();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Search Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Modify this query based on your actual search requirement
        String query = "SELECT * FROM buku WHERE judul LIKE '%" + searchTerm + "%' OR penulis LIKE '%" + searchTerm + "%'";

        // Display search results
        displayTable(query);
    }

    private void showHomeTable() {
        String query = "SELECT id AS id, judul, penulis, penerbit, NULL AS peminjam, NULL AS tanggal_pinjam, NULL AS tanggal_kembali, NULL AS denda " +
                "FROM buku " +
                "UNION ALL " +
                "SELECT id AS id, nama AS judul, NULL AS penulis, NULL AS penerbit, alamat AS peminjam, NULL AS tanggal_pinjam, NULL AS tanggal_kembali, NULL AS denda " +
                "FROM peminjam " +
                "UNION ALL " +
                "SELECT id AS id, NULL AS judul, NULL AS penulis, NULL AS penerbit, NULL AS peminjam, tanggal_pinjam, tanggal_kembali, denda " +
                "FROM peminjaman";
        displayTable(query);
    }

    private void showDataBukuTable() {
        String query = "SELECT * FROM buku";
        displayTable(query);
    }

    private void showDataPeminjamTable() {
        String query = "SELECT * FROM peminjam";
        displayTable(query);
    }

    private void showPeminjamanTable() {
        String query = "SELECT * FROM peminjaman";
        displayTable(query);
    }

    private void showPengembalianTable() {
        String query = "SELECT * FROM pengembalian";
        displayTable(query);
    }

    private void displayTable(String query) {
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel tableModel = new DefaultTableModel();
            int columnCount = rs.getMetaData().getColumnCount();

            // Set table columns
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(rs.getMetaData().getColumnName(i));
            }

            // Add rows to table model
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }

            // Create JTable and add to content panel
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}
