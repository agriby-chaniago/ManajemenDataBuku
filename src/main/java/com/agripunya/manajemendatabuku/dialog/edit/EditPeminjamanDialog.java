package com.agripunya.manajemendatabuku.dialog.edit;

import com.agripunya.manajemendatabuku.util.DatabaseUtil;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EditPeminjamanDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JDateChooser dateChooserTanggalPinjam;
    private JDateChooser dateChooserTanggalKembali;
    private final int id;

    public EditPeminjamanDialog(int id) {
        setTitle("Edit Peminjaman"); // Set the title of the JFrame
        setResizable(false);
        this.id = id;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // Set colors
        Color buttonColor = Color.decode("#789DBC");
        Color buttonTextColor = Color.decode("#FEF9F2");

        buttonOK.setBackground(buttonColor);
        buttonOK.setForeground(buttonTextColor);
        buttonCancel.setBackground(buttonColor);
        buttonCancel.setForeground(buttonTextColor);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        loadData();
    }

    public static void main(String[] args) {
        EditPeminjamanDialog dialog = new EditPeminjamanDialog(1);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void loadData() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT tanggal_pinjam, tanggal_kembali FROM peminjaman WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dateChooserTanggalPinjam.setDate(rs.getDate("tanggal_pinjam"));
                    dateChooserTanggalKembali.setDate(rs.getDate("tanggal_kembali"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onOK() {
        Date tanggalPinjam = dateChooserTanggalPinjam.getDate();
        Date tanggalKembali = dateChooserTanggalKembali.getDate();

        if (tanggalPinjam == null || tanggalKembali == null) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!tanggalKembali.after(tanggalPinjam)) {
            JOptionPane.showMessageDialog(this, "Tanggal kembali harus setelah tanggal pinjam.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement stmt = conn.prepareStatement("UPDATE peminjaman SET tanggal_pinjam = ?, tanggal_kembali = ? WHERE id = ?")) {
                stmt.setDate(1, new java.sql.Date(tanggalPinjam.getTime()));
                stmt.setDate(2, new java.sql.Date(tanggalKembali.getTime()));
                stmt.setInt(3, id);
                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    conn.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(this, "Peminjaman berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the dialog
                } else {
                    conn.rollback(); // Rollback transaction if no rows updated
                    JOptionPane.showMessageDialog(this, "Tidak ada data yang diperbarui.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction on error
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        dateChooserTanggalPinjam = new JDateChooser();
        dateChooserTanggalKembali = new JDateChooser();

        Dimension preferredSize = new Dimension(150, 24);
        dateChooserTanggalPinjam.setPreferredSize(preferredSize);
        dateChooserTanggalKembali.setPreferredSize(preferredSize);
    }
}