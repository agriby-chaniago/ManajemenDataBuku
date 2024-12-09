package com.agripunya.manajemendatabuku;

import com.agripunya.manajemendatabuku.ui.MainMenu;

/**
 * Kelas utama untuk menjalankan aplikasi Manajemen Data Buku
 * @author taki-tachibana
 */
public class Main {
    public static void main(String[] args) {
        // Menjalankan aplikasi dengan menampilkan Main Menu
        MainMenu mainMenu = new MainMenu();
        mainMenu.setVisible(true); // Menampilkan jendela utama
    }
}
