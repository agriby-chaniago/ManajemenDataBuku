package com.agripunya.manajemendatabuku.ui.dialog.delete;

import com.agripunya.manajemendatabuku.util.DatabaseUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeletePeminjamDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private final int peminjamId;
    private boolean confirmed = false;

    public DeletePeminjamDialog(int peminjamId) {
        setTitle("Hapus Peminjam"); // Set the title of the JFrame
        setResizable(false);
        this.peminjamId = peminjamId;
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
    }

    public static void main(String[] args) {
        DeletePeminjamDialog dialog = new DeletePeminjamDialog(1); // Example ID
        dialog.pack();
        dialog.setLocationRelativeTo(null); // Center the dialog
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        if (peminjamId <= 0) {
            JOptionPane.showMessageDialog(this, "ID peminjam tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM peminjam WHERE id = ?")) {

            stmt.setInt(1, peminjamId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                confirmed = true;
                JOptionPane.showMessageDialog(this, "Peminjam berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "ID peminjam tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus peminjam: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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