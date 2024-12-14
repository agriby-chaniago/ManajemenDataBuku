package com.agripunya.manajemendatabuku.dialog.add;

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

public class AddPeminjamanDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> cmbIdBuku;
    private JComboBox<String> cmbIdPeminjam;
    private JDateChooser dateChooserKembali;
    private JDateChooser dateChooserPinjam;
    private boolean confirmed;
    private int idBuku;
    private int idPeminjam;

    public AddPeminjamanDialog(JFrame parent) {
        super(parent, true); // Call the parent constructor with the parent frame and modal flag
        setTitle("Tambah Peminjaman"); // Set the title of the JFrame
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

        cmbIdBuku.setBackground(fieldColor);
        cmbIdBuku.setForeground(textColor);
        cmbIdPeminjam.setBackground(fieldColor);
        cmbIdPeminjam.setForeground(textColor);

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

        // Load data into combo boxes
        loadComboBoxData();
    }

    public static void main(String[] args) {
        AddPeminjamanDialog dialog = new AddPeminjamanDialog(null); // Example usage without a parent
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public int getIdPeminjam() {
        return idPeminjam;
    }

    private void loadComboBoxData() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmtBuku = conn.prepareStatement("SELECT id FROM buku WHERE isShow = 0");
             PreparedStatement stmtPeminjam = conn.prepareStatement("SELECT id FROM peminjam WHERE isShow = 0");
             ResultSet rsBuku = stmtBuku.executeQuery();
             ResultSet rsPeminjam = stmtPeminjam.executeQuery()) {

            while (rsBuku.next()) {
                cmbIdBuku.addItem(String.valueOf(rsBuku.getInt("id")));
            }

            while (rsPeminjam.next()) {
                cmbIdPeminjam.addItem(String.valueOf(rsPeminjam.getInt("id")));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onOK() {
        if (cmbIdBuku.getSelectedItem() == null || cmbIdPeminjam.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        idBuku = Integer.parseInt((String) cmbIdBuku.getSelectedItem());
        idPeminjam = Integer.parseInt((String) cmbIdPeminjam.getSelectedItem());
        Date tanggalPinjam = dateChooserPinjam.getDate();
        Date tanggalKembali = dateChooserKembali.getDate();

        if (tanggalPinjam == null || tanggalKembali == null) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tanggalKembali.before(tanggalPinjam)) {
            JOptionPane.showMessageDialog(this, "Tanggal kembali harus setelah tanggal pinjam.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO peminjaman (id_buku, id_peminjam, tanggal_pinjam, tanggal_kembali) VALUES (?, ?, ?, ?)")) {

            stmt.setInt(1, idBuku);
            stmt.setInt(2, idPeminjam);
            stmt.setDate(3, new java.sql.Date(tanggalPinjam.getTime()));
            stmt.setDate(4, new java.sql.Date(tanggalKembali.getTime()));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Peminjaman berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            confirmed = true;
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    private void createUIComponents() {
        // Custom component creation
        dateChooserKembali = new JDateChooser();
        dateChooserPinjam = new JDateChooser();

        // Set preferred size to match JTextField
        Dimension preferredSize = new Dimension(150, 24);
        dateChooserKembali.setPreferredSize(preferredSize);
        dateChooserPinjam.setPreferredSize(preferredSize);
    }
}