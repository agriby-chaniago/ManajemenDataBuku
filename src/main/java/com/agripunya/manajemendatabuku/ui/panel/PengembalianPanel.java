package com.agripunya.manajemendatabuku.ui.panel;

import com.agripunya.manajemendatabuku.ui.dialog.add.AddPengembalianDialog;
import com.agripunya.manajemendatabuku.ui.dialog.delete.DeletePengembalianDialog;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PengembalianPanel extends javax.swing.JFrame {

    private final int rowsPerPage = 20; // Updated to 20 rows per page
    private JTextField txtSearch;
    private JTable tblPengembalian;
    private JButton btnPrevious;
    private JButton btnNext;
    private int currentPage = 1;
    private javax.swing.JButton btnDataBuku;
    private javax.swing.JButton btnDataPeminjam;
    private javax.swing.JButton btnPeminjaman;
    private javax.swing.JButton btnPengembalian;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JScrollPane jScrollPane1;

    public PengembalianPanel() {
        setTitle("Manajemen Data Buku | Data Pengembalian"); // Set the title of the JFrame
        setResizable(false);
        initComponents();
        setLocationRelativeTo(null);
        loadDataPengembalian();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new PengembalianPanel().setVisible(true));
    }

    private void loadDataPengembalian() {
        DefaultTableModel model = (DefaultTableModel) tblPengembalian.getModel();
        model.setRowCount(0);

        String query = "SELECT p.id, p.id_peminjaman, p.tanggal_dikembalikan, p.denda, pm.tanggal_pinjam, pm.tanggal_kembali " +
                "FROM pengembalian p " +
                "JOIN peminjaman pm ON p.id_peminjaman = pm.id " +
                "WHERE p.isShow = 0 " +
                "LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rowsPerPage);
            stmt.setInt(2, (currentPage - 1) * rowsPerPage);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getInt("id_peminjaman"),
                        rs.getDate("tanggal_dikembalikan"),
                        rs.getDouble("denda"),
                        rs.getDate("tanggal_pinjam"),
                        rs.getDate("tanggal_kembali")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data pengembalian: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        btnDataBuku = new javax.swing.JButton();
        btnDataPeminjam = new javax.swing.JButton();
        btnPeminjaman = new javax.swing.JButton();
        btnPengembalian = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPengembalian = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Set colors
        Color backgroundColor = Color.decode("#FFE3E3");
        Color buttonColor = Color.decode("#789DBC");
        Color buttonTextColor = Color.decode("#FEF9F2");
        Color searchFieldColor = Color.decode("#FEF9F2");
        Color headerColor = Color.decode("#C9E9D2");
        Color dataColor = Color.decode("#FEF9F2");
        Color headerTextColor = Color.decode("#060930");
        Color dataTextColor = Color.decode("#060930");

        getContentPane().setBackground(backgroundColor);

        btnDataBuku.setText("Data Buku");
        btnDataBuku.setBackground(buttonColor);
        btnDataBuku.setForeground(buttonTextColor);
        btnDataBuku.addActionListener(evt -> btnDataBukuActionPerformed(evt));

        btnDataPeminjam.setText("Data Peminjam");
        btnDataPeminjam.setBackground(buttonColor);
        btnDataPeminjam.setForeground(buttonTextColor);
        btnDataPeminjam.addActionListener(evt -> btnDataPeminjamActionPerformed(evt));

        btnPeminjaman.setText("Peminjaman");
        btnPeminjaman.setBackground(buttonColor);
        btnPeminjaman.setForeground(buttonTextColor);
        btnPeminjaman.addActionListener(evt -> btnPeminjamanActionPerformed(evt));

        btnPengembalian.setText("Pengembalian");
        btnPengembalian.setBackground(buttonColor);
        btnPengembalian.setForeground(buttonTextColor);
        btnPengembalian.addActionListener(evt -> btnPengembalianActionPerformed(evt));

        tblPengembalian.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String[]{
                        "ID", "ID Peminjaman", "Tanggal Dikembalikan", "Denda", "Tanggal Pinjam", "Tanggal Kembali"
                }
        ));

        // Set table header colors
        JTableHeader header = tblPengembalian.getTableHeader();
        header.setBackground(headerColor);
        header.setForeground(headerTextColor);

        // Set table data colors
        tblPengembalian.setBackground(dataColor);
        tblPengembalian.setForeground(dataTextColor);

        jScrollPane1.setViewportView(tblPengembalian);

        txtSearch.setBackground(searchFieldColor);
        txtSearch.addActionListener(evt -> SearchFieldActionPerformed(evt));

        btnSearch.setText("Search");
        btnSearch.setBackground(buttonColor);
        btnSearch.setForeground(buttonTextColor);
        btnSearch.addActionListener(evt -> btnSearchActionPerformed(evt));

        btnTambah.setText("Tambah");
        btnTambah.setBackground(buttonColor);
        btnTambah.setForeground(buttonTextColor);
        btnTambah.addActionListener(evt -> btnTambahActionPerformed(evt));

        btnHapus.setText("Hapus");
        btnHapus.setBackground(buttonColor);
        btnHapus.setForeground(buttonTextColor);
        btnHapus.addActionListener(evt -> btnHapusActionPerformed(evt));

        btnPrevious.setText("Previous");
        btnPrevious.setBackground(buttonColor);
        btnPrevious.setForeground(buttonTextColor);
        btnPrevious.addActionListener(evt -> btnPreviousActionPerformed(evt));

        btnNext.setText("Next");
        btnNext.setBackground(buttonColor);
        btnNext.setForeground(buttonTextColor);
        btnNext.addActionListener(evt -> btnNextActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnDataBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnDataPeminjam, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnTambah)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnHapus)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnSearch))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnPrevious)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnNext)))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDataPeminjam, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDataBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnSearch))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnTambah)
                                                .addComponent(btnHapus)))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnPrevious)
                                        .addComponent(btnNext))
                                .addContainerGap(21, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, btnDataBuku, btnDataPeminjam, btnPeminjaman, btnPengembalian);

        pack();
    }

    private void btnDataBukuActionPerformed(ActionEvent evt) {
        new BukuPanel().setVisible(true);
        this.dispose();
    }

    private void btnDataPeminjamActionPerformed(ActionEvent evt) {
        new PeminjamPanel().setVisible(true);
        this.dispose();
    }

    private void btnPeminjamanActionPerformed(ActionEvent evt) {
        new PeminjamanPanel().setVisible(true);
        this.dispose();
    }

    private void btnPengembalianActionPerformed(ActionEvent evt) {
        loadDataPengembalian();
    }

    private void SearchFieldActionPerformed(ActionEvent evt) {
        performSearch();
    }

    private void btnSearchActionPerformed(ActionEvent evt) {
        performSearch();
    }

    private void performSearch() {
        String searchText = txtSearch.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tblPengembalian.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblPengembalian.setRowSorter(sorter);

        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void btnTambahActionPerformed(ActionEvent evt) {
        AddPengembalianDialog dialog = new AddPengembalianDialog(this);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        loadDataPengembalian();
    }

    private void btnHapusActionPerformed(ActionEvent evt) {
        int selectedRow = tblPengembalian.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih pengembalian yang ingin dihapus.", "Kesalahan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tblPengembalian.getValueAt(selectedRow, 0);
        DeletePengembalianDialog dialog = new DeletePengembalianDialog(id);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE pengembalian SET isShow = 1 WHERE id = ?")) {

                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Pengembalian berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus pengembalian: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        loadDataPengembalian();
    }

    private void btnPreviousActionPerformed(ActionEvent evt) {
        if (currentPage > 1) {
            currentPage--;
            loadDataPengembalian();
        }
    }

    private void btnNextActionPerformed(ActionEvent evt) {
        currentPage++;
        loadDataPengembalian();
    }
}