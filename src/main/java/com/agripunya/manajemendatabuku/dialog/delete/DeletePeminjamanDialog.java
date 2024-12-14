package com.agripunya.manajemendatabuku.dialog.delete;

import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeletePeminjamanDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private final int id;
    private boolean confirmed = false;

    public DeletePeminjamanDialog(int id) {
        setTitle("Hapus Peminjaman"); // Set the title of the JFrame
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

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        DeletePeminjamanDialog dialog = new DeletePeminjamanDialog(1);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        if (id <= 0) {
            JOptionPane.showMessageDialog(this, "ID peminjaman tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Get id_buku and id_peminjam
            int idBuku = 0;
            int idPeminjam = 0;
            String selectQuery = "SELECT id_buku, id_peminjam FROM peminjaman WHERE id = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    idBuku = rs.getInt("id_buku");
                    idPeminjam = rs.getInt("id_peminjam");
                }
            }

            // Delete peminjaman
            String deleteQuery = "DELETE FROM peminjaman WHERE id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, id);
                int affectedRows = deleteStmt.executeUpdate();

                if (affectedRows > 0) {
                    // Update isShow for buku and peminjam
                    String updateBukuQuery = "UPDATE buku SET isShow = 0 WHERE id = ?";
                    try (PreparedStatement updateBukuStmt = conn.prepareStatement(updateBukuQuery)) {
                        updateBukuStmt.setInt(1, idBuku);
                        updateBukuStmt.executeUpdate();
                    }

                    String updatePeminjamQuery = "UPDATE peminjam SET isShow = 0 WHERE id = ?";
                    try (PreparedStatement updatePeminjamStmt = conn.prepareStatement(updatePeminjamQuery)) {
                        updatePeminjamStmt.setInt(1, idPeminjam);
                        updatePeminjamStmt.executeUpdate();
                    }

                    conn.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(this, "Peminjaman berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    confirmed = true;
                } else {
                    conn.rollback(); // Rollback transaction if no rows affected
                    JOptionPane.showMessageDialog(this, "ID peminjaman tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction on error
                JOptionPane.showMessageDialog(this, "Gagal menghapus peminjaman: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus peminjaman: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            dispose();
        }
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}