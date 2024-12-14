package com.agripunya.manajemendatabuku.ui.dialog.edit;

import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditBukuDialog extends JDialog {
    private final int bukuId;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtJudul;
    private JTextField txtPenulis;
    private JTextField txtPenerbit;
    private JTextField txtTahunTerbit;

    public EditBukuDialog(int bukuId) {
        setTitle("Edit Buku"); // Set the title of the JFrame
        setResizable(false);
        this.bukuId = bukuId;
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

        contentPane.setBackground(fieldColor);

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

        loadData();
    }

    public static void main(String[] args) {
        EditBukuDialog dialog = new EditBukuDialog(1); // Pass the actual bukuId here
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void loadData() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM buku WHERE id = ?")) {
            stmt.setInt(1, bukuId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                txtJudul.setText(rs.getString("judul"));
                txtPenulis.setText(rs.getString("penulis"));
                txtPenerbit.setText(rs.getString("penerbit"));
                txtTahunTerbit.setText(rs.getString("tahun_terbit"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
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

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE buku SET judul = ?, penulis = ?, penerbit = ?, tahun_terbit = ? WHERE id = ?")) {

            stmt.setString(1, judul);
            stmt.setString(2, penulis);
            stmt.setString(3, penerbit);
            stmt.setInt(4, Integer.parseInt(tahunTerbit));
            stmt.setInt(5, bukuId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Buku berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }
}