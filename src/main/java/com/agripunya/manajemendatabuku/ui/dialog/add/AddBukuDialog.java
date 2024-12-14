package com.agripunya.manajemendatabuku.ui.dialog.add;

import com.agripunya.manajemendatabuku.ui.panel.BukuPanel;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBukuDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtJudul;
    private JTextField txtPenulis;
    private JTextField txtPenerbit;
    private JTextField txtTahunTerbit;

    public AddBukuDialog(BukuPanel bukuPanel) {
        setTitle("Tambah Buku"); // Set the title of the JFrame
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

        txtJudul.setBackground(fieldColor);
        txtJudul.setForeground(textColor);
        txtPenulis.setBackground(fieldColor);
        txtPenulis.setForeground(textColor);
        txtPenerbit.setBackground(fieldColor);
        txtPenerbit.setForeground(textColor);
        txtTahunTerbit.setBackground(fieldColor);
        txtTahunTerbit.setForeground(textColor);

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
        BukuPanel bukuPanel = new BukuPanel();
        AddBukuDialog dialog = new AddBukuDialog(bukuPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        String judul = txtJudul.getText();
        String penulis = txtPenulis.getText();
        String penerbit = txtPenerbit.getText();
        String tahunTerbit = txtTahunTerbit.getText();

        if (judul.isEmpty() || penulis.isEmpty() || penerbit.isEmpty() || tahunTerbit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!tahunTerbit.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Tahun Terbit harus berupa angka 4 digit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int tahun = Integer.parseInt(tahunTerbit);

            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO buku (judul, penulis, penerbit, tahun_terbit) VALUES (?, ?, ?, ?)")) {

                stmt.setString(1, judul);
                stmt.setString(2, penulis);
                stmt.setString(3, penerbit);
                stmt.setInt(4, tahun);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tahun Terbit harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }
}