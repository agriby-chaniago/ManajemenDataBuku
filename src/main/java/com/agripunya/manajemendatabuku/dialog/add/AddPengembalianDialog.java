package com.agripunya.manajemendatabuku.dialog.add;

import com.agripunya.manajemendatabuku.panel.PengembalianPanel;
import com.agripunya.manajemendatabuku.util.DatabaseUtil;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AddPengembalianDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> cmbIdPeminjaman;
    private JDateChooser dateChooserTanggalDikembalikan;
    private JTextField txtDenda;
    private boolean confirmed;
    private int idBuku;
    private int idPeminjam;

    public AddPengembalianDialog(PengembalianPanel parent) {
        super(parent, true);
        setTitle("Tambah Pengembalian"); // Set the title of the JFrame
        setResizable(false);
        createUIComponents(); // Ensure custom components are created
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

        cmbIdPeminjaman.setBackground(fieldColor);
        cmbIdPeminjaman.setForeground(textColor);
        txtDenda.setBackground(fieldColor);
        txtDenda.setForeground(textColor);

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

        // Load data for cmbIdPeminjaman
        loadIdPeminjaman();
    }

    public static void main(String[] args) {
        AddPengembalianDialog dialog = new AddPengembalianDialog(new PengembalianPanel());
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

    private void onOK() {
        confirmed = true;
        String idPeminjaman = (String) cmbIdPeminjaman.getSelectedItem();
        java.util.Date tanggalDikembalikan = dateChooserTanggalDikembalikan.getDate();

        if (idPeminjaman == null || tanggalDikembalikan == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidReturnDate(idPeminjaman, tanggalDikembalikan)) {
            JOptionPane.showMessageDialog(this, "Return date must be on or after the borrow date", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int denda = calculateDenda(idPeminjaman, tanggalDikembalikan);

        String insertQuery = "INSERT INTO pengembalian (id_peminjaman, tanggal_dikembalikan, denda) VALUES (?, ?, ?)";
        String updatePeminjamanQuery = "UPDATE peminjaman SET isShow = 1 WHERE id = ?";
        String updateBukuQuery = "UPDATE buku SET isShow = 0 WHERE id = (SELECT id_buku FROM peminjaman WHERE id = ?)";
        String updatePeminjamQuery = "UPDATE peminjam SET isShow = 0 WHERE id = (SELECT id_peminjam FROM peminjaman WHERE id = ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             PreparedStatement updatePeminjamanStmt = conn.prepareStatement(updatePeminjamanQuery);
             PreparedStatement updateBukuStmt = conn.prepareStatement(updateBukuQuery);
             PreparedStatement updatePeminjamStmt = conn.prepareStatement(updatePeminjamQuery)) {

            insertStmt.setString(1, idPeminjaman);
            insertStmt.setDate(2, new java.sql.Date(tanggalDikembalikan.getTime()));
            insertStmt.setInt(3, denda);
            insertStmt.executeUpdate();

            updatePeminjamanStmt.setString(1, idPeminjaman);
            updatePeminjamanStmt.executeUpdate();

            updateBukuStmt.setString(1, idPeminjaman);
            updateBukuStmt.executeUpdate();

            updatePeminjamStmt.setString(1, idPeminjaman);
            updatePeminjamStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to save data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidReturnDate(String idPeminjaman, java.util.Date tanggalDikembalikan) {
        String query = "SELECT tanggal_pinjam FROM peminjaman WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, idPeminjaman);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date tanggalPinjam = rs.getDate("tanggal_pinjam");
                return !tanggalDikembalikan.before(tanggalPinjam);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to validate return date: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private int calculateDenda(String idPeminjaman, java.util.Date tanggalDikembalikan) {
        String query = "SELECT tanggal_kembali FROM peminjaman WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, idPeminjaman);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date tanggalKembali = rs.getDate("tanggal_kembali");
                LocalDate returnDate = tanggalDikembalikan.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                LocalDate dueDate = tanggalKembali.toLocalDate();
                if (returnDate.isAfter(dueDate)) {
                    long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
                    return (int) daysLate * 1500;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to calculate fine: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    private void createUIComponents() {
        // Custom component creation
        dateChooserTanggalDikembalikan = new JDateChooser();
        dateChooserTanggalDikembalikan.setPreferredSize(new Dimension(200, 30));
        dateChooserTanggalDikembalikan.setDate(new java.util.Date()); // Set to today's date

        txtDenda = new JTextField(); // Initialize txtDenda
    }

    private void loadIdPeminjaman() {
        String query = "SELECT id FROM peminjaman WHERE isShow = 0";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cmbIdPeminjaman.addItem(rs.getString("id"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load ID Peminjaman: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}