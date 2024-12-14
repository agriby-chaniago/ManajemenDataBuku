package com.agripunya.manajemendatabuku.ui.panel;

import com.agripunya.manajemendatabuku.ui.dialog.add.AddPeminjamanDialog;
import com.agripunya.manajemendatabuku.ui.dialog.delete.DeletePeminjamanDialog;
import com.agripunya.manajemendatabuku.ui.dialog.edit.EditPeminjamanDialog;
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
import java.util.ArrayList;
import java.util.List;

public class PeminjamanPanel extends javax.swing.JFrame {

    private final int rowsPerPage = 20; // Updated to 20 rows per page
    private JTextField txtSearch;
    private JTable tblPeminjaman;
    private JButton btnPrevious;
    private JButton btnNext;
    private int currentPage = 1;
    private javax.swing.JButton btnDataBuku;
    private javax.swing.JButton btnDataPeminjam;
    private javax.swing.JButton btnPengembalian;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JScrollPane jScrollPane1;
    private final List<Object[]> allData = new ArrayList<>();

    public PeminjamanPanel() {
        setTitle("Manajemen Data Buku | Data Peminjaman"); // Set the title of the JFrame
        setResizable(false);
        initComponents();
        setLocationRelativeTo(null);
        loadDataPeminjaman();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new PeminjamanPanel().setVisible(true));
    }

    private void loadDataPeminjaman() {
        DefaultTableModel model = (DefaultTableModel) tblPeminjaman.getModel();
        model.setRowCount(0);
        allData.clear(); // Clear the allData list

        String query = "SELECT id, id_buku, id_peminjam, tanggal_pinjam, tanggal_kembali FROM peminjaman " +
                "WHERE isShow = 0 LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rowsPerPage);
            stmt.setInt(2, (currentPage - 1) * rowsPerPage);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getInt("id_buku"),
                        rs.getInt("id_peminjam"),
                        rs.getDate("tanggal_pinjam"),
                        rs.getDate("tanggal_kembali")
                };
                model.addRow(row);
                allData.add(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data peminjaman: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Initialize components
        JButton btnDataBuku = new javax.swing.JButton();
        JButton btnDataPeminjam = new javax.swing.JButton();
        JButton btnPeminjaman = new javax.swing.JButton();
        JButton btnPengembalian = new javax.swing.JButton();
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        tblPeminjaman = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        JButton btnSearch = new javax.swing.JButton();
        JButton btnTambah = new javax.swing.JButton();
        JButton btnHapus = new javax.swing.JButton();
        JButton btnEdit = new javax.swing.JButton();
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

        tblPeminjaman.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String[]{
                        "ID", "ID Buku", "ID Peminjam", "Tanggal Pinjam", "Tanggal Kembali"
                }
        ));

        // Set table header colors
        JTableHeader header = tblPeminjaman.getTableHeader();
        header.setBackground(headerColor);
        header.setForeground(headerTextColor);

        // Set table data colors
        tblPeminjaman.setBackground(dataColor);
        tblPeminjaman.setForeground(dataTextColor);

        jScrollPane1.setViewportView(tblPeminjaman);

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

        btnEdit.setText("Edit");
        btnEdit.setBackground(buttonColor);
        btnEdit.setForeground(buttonTextColor);
        btnEdit.addActionListener(evt -> btnEditActionPerformed(evt));

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
                                                .addComponent(btnEdit)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                                .addComponent(btnHapus)
                                                .addComponent(btnEdit)))
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
        loadDataPeminjaman();
    }

    private void btnPengembalianActionPerformed(ActionEvent evt) {
        new PengembalianPanel().setVisible(true);
        this.dispose();
    }

    private void SearchFieldActionPerformed(ActionEvent evt) {
        performSearch();
    }

    private void btnSearchActionPerformed(ActionEvent evt) {
        performSearch();
    }

    private void performSearch() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tblPeminjaman.getModel();
        model.setRowCount(0); // Clear the table

        List<Object[]> filteredData = new ArrayList<>();
        for (Object[] row : allData) {
            boolean matches = false;
            for (Object cell : row) {
                if (cell.toString().toLowerCase().contains(searchText)) {
                    matches = true;
                    break;
                }
            }
            if (matches) {
                filteredData.add(row);
            }
        }

        for (Object[] row : filteredData) {
            model.addRow(row);
        }
    }

    private void btnTambahActionPerformed(ActionEvent evt) {
        AddPeminjamanDialog dialog = new AddPeminjamanDialog(this);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            int idBuku = dialog.getIdBuku();
            int idPeminjam = dialog.getIdPeminjam();

            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmtBuku = conn.prepareStatement("UPDATE buku SET isShow = 1 WHERE id = ?");
                 PreparedStatement stmtPeminjam = conn.prepareStatement("UPDATE peminjam SET isShow = 1 WHERE id = ?")) {

                stmtBuku.setInt(1, idBuku);
                stmtBuku.executeUpdate();

                stmtPeminjam.setInt(1, idPeminjam);
                stmtPeminjam.executeUpdate();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui status buku atau peminjam: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        loadDataPeminjaman();
    }

    private void btnHapusActionPerformed(ActionEvent evt) {
        int selectedRow = tblPeminjaman.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih peminjaman yang ingin dihapus.", "Kesalahan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tblPeminjaman.getValueAt(selectedRow, 0);
        DeletePeminjamanDialog dialog = new DeletePeminjamanDialog(id);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM peminjaman WHERE id = ?")) {

                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Peminjaman berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus peminjaman: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        loadDataPeminjaman();
    }

    private void btnEditActionPerformed(ActionEvent evt) {
        int selectedRow = tblPeminjaman.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih peminjaman yang ingin diedit.", "Kesalahan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tblPeminjaman.getValueAt(selectedRow, 0);
        EditPeminjamanDialog dialog = new EditPeminjamanDialog(id);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        loadDataPeminjaman();
    }

    private void btnPreviousActionPerformed(ActionEvent evt) {
        if (currentPage > 1) {
            currentPage--;
            loadDataPeminjaman();
        }
    }

    private void btnNextActionPerformed(ActionEvent evt) {
        currentPage++;
        loadDataPeminjaman();
    }
}