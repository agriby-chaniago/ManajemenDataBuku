package com.agripunya.manajemendatabuku.ui.dialog.add;

import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddPeminjamDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtNama;
    private JTextField txtAlamat;
    private JTextField txtTelepon;

    public AddPeminjamDialog(JFrame parent) {
        super(parent, true); // Call the parent constructor with the parent frame and modal flag
        setTitle("Tambah Peminjam"); // Set the title of the JFrame
        setResizable(false);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // Set colors
        Color buttonColor = Color.decode("#789DBC");
        Color buttonTextColor = Color.decode("#FEF9F2");
        Color fieldColor = Color.decode("#FEF9F2");
        Color textColor = Color.decode("#060930");

        buttonOK.setBackground(buttonColor);
        buttonOK.setForeground(buttonTextColor);
        buttonCancel.setBackground(buttonColor);
        buttonCancel.setForeground(buttonTextColor);

        txtNama.setBackground(fieldColor);
        txtNama.setForeground(textColor);
        txtAlamat.setBackground(fieldColor);
        txtAlamat.setForeground(textColor);
        txtTelepon.setBackground(fieldColor);
        txtTelepon.setForeground(textColor);

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
        AddPeminjamDialog dialog = new AddPeminjamDialog(null); // Example usage without a parent
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        String nama = txtNama.getText();
        String alamat = txtAlamat.getText();
        String telepon = txtTelepon.getText();

        if (nama.isEmpty() || alamat.isEmpty() || telepon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!telepon.matches("\\d{11,15}")) {
            JOptionPane.showMessageDialog(this, "Nomor telepon harus berupa angka dan memiliki panjang antara 11 hingga 15 digit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO peminjam (nama, alamat, telepon) VALUES (?, ?, ?)")) {

            stmt.setString(1, nama);
            stmt.setString(2, alamat);
            stmt.setString(3, telepon);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Peminjam berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }
}