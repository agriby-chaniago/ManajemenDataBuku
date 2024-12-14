package com.agripunya.manajemendatabuku;

import com.agripunya.manajemendatabuku.ui.dialog.LoginDialog;
import com.agripunya.manajemendatabuku.ui.panel.BukuPanel;

/**
 * <h1>Manajemen Data Buku - Main Class</h1>
 * <p>
 * Kelas utama untuk menjalankan aplikasi Manajemen Data Buku. Aplikasi ini dimulai dengan menampilkan
 * dialog login, kemudian menampilkan panel utama (Main Menu) jika proses login berhasil.
 * </p>
 *
 * <h2>Fungsi Utama:</h2>
 * <ul>
 *     <li>Menampilkan dialog login.</li>
 *     <li>Melanjutkan ke tampilan utama setelah login berhasil.</li>
 * </ul>
 *
 * <h2>Cara Kerja:</h2>
 * <ol>
 *     <li>LoginDialog ditampilkan untuk meminta kredensial pengguna.</li>
 *     <li>Jika kredensial valid, aplikasi akan menampilkan BukuPanel sebagai antarmuka utama.</li>
 * </ol>
 *
 * <h2>Dependencies:</h2>
 * <ul>
 *     <li>{@link com.agripunya.manajemendatabuku.ui.dialog.LoginDialog}</li>
 *     <li>{@link com.agripunya.manajemendatabuku.ui.panel.BukuPanel}</li>
 * </ul>
 *
 * <h2>Catatan:</h2>
 * <p>Pastikan semua resource UI telah dikonfigurasi dengan benar untuk menghindari error runtime.</p>
 *
 * @author [Agriby D. Chaniago]
 * @version 1.0
 */
public class Main {

    /**
     * Metode utama untuk menjalankan aplikasi.
     *
     * @param args Argumen command line (tidak digunakan dalam aplikasi ini).
     */
    public static void main(String[] args) {
        // Menampilkan dialog login
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.pack(); // Mengatur ukuran dialog sesuai kontennya
        loginDialog.setLocationRelativeTo(null); // Menempatkan dialog di tengah layar
        loginDialog.setVisible(true); // Menampilkan dialog login kepada pengguna

        // Memeriksa apakah login berhasil
        if (loginDialog.isLoginSuccessful()) {
            // Jika login berhasil, menampilkan panel utama (BukuPanel)
            BukuPanel bukuPanel = new BukuPanel();
            bukuPanel.setVisible(true); // Menampilkan jendela utama aplikasi
        }
    }
}
