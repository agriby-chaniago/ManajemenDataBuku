package com.agripunya.manajemendatabuku.dialog.edit;

import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditPeminjamDialog extends JDialog {
    private final int peminjamId;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtNama;
    private JTextField txtAlamat;
    private JTextField txtTelepon;

    public EditPeminjamDialog(int peminjamId) {
        setTitle("Edit Peminjam"); // Set the title of the JFrame
        setResizable(false);
        this.peminjamId = peminjamId;
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

        loadPeminjamData();
    }

    public static void main(String[] args) {
        EditPeminjamDialog dialog = new EditPeminjamDialog(1); // Example ID
        dialog.pack();
        dialog.setLocationRelativeTo(null); // Center the dialog
        dialog.setVisible(true);
        System.exit(0);
    }

    private void loadPeminjamData() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT nama, alamat, telepon FROM peminjam WHERE id = ?")) {

            stmt.setInt(1, peminjamId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtNama.setText(rs.getString("nama"));
                txtAlamat.setText(rs.getString("alamat"));
                txtTelepon.setText(rs.getString("telepon"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data peminjam: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onOK() {
        String nama = txtNama.getText();
        String alamat = txtAlamat.getText();
        String telepon = txtTelepon.getText();

        if (nama.isEmpty() || alamat.isEmpty() || telepon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE peminjam SET nama = ?, alamat = ?, telepon = ? WHERE id = ?")) {

            stmt.setString(1, nama);
            stmt.setString(2, alamat);
            stmt.setString(3, telepon);
            stmt.setInt(4, peminjamId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Peminjam berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui peminjam: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }
}