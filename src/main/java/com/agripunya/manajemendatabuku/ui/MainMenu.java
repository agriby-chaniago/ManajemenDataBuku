package com.agripunya.manajemendatabuku.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    // Constructor
    public MainMenu() {
        // Set title and default close operation
        setTitle("Manajemen Data Buku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);  // Center the window

        // Set layout manager
        setLayout(new BorderLayout());

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // HOME menu
        JMenu homeMenu = new JMenu("Home");
        menuBar.add(homeMenu);

        // Data Buku Menu
        JMenu dataBukuMenu = new JMenu("Data Buku");
        menuBar.add(dataBukuMenu);

        // Data Peminjam Menu
        JMenu dataPeminjamMenu = new JMenu("Data Peminjam");
        menuBar.add(dataPeminjamMenu);

        // Peminjaman Menu
        JMenu peminjamanMenu = new JMenu("Peminjaman");
        menuBar.add(peminjamanMenu);

        // Pengembalian Menu
        JMenu pengembalianMenu = new JMenu("Pengembalian");
        menuBar.add(pengembalianMenu);

        // Create a new menu for Search dan Search Field
        JMenu searchMenu = new JMenu("Search");
        JTextField SearchField = new JTextField("") ;

        // Add the search menu to the menu bar
        menuBar.add(searchMenu);
        menuBar.add(SearchField);

        // Set the menu bar
        setJMenuBar(menuBar);


        // Create Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // Toolbar buttons
        JButton tambahBukuButton = new JButton("Tambah Buku");
        JButton tambahPeminjamButton = new JButton("Tambah Peminjam");
        JButton tambahPeminjamanButton = new JButton("Tambah Peminjaman");
        JButton tambahPengembalianButton = new JButton("Tambah Pengembalian");

        toolBar.add(tambahBukuButton);
        toolBar.add(tambahPeminjamButton);
        toolBar.add(tambahPeminjamanButton);
        toolBar.add(tambahPengembalianButton);

        // Add the toolbar to the top of the frame
        add(toolBar, BorderLayout.NORTH);

        // Sample content panel
        JPanel contentPanel = new JPanel();
        contentPanel.add(new JLabel("Manajemen Data Buku"));
        add(contentPanel, BorderLayout.CENTER);
    }

    // Method to show the Data Buku table
    private void showDataBukuTable() {
        // Create a new panel to display the table
        JPanel dataBukuPanel = new JPanel();
        dataBukuPanel.setLayout(new BorderLayout());

        // Create the table with sample data
        String[] columnNames = {"ID Buku", "Judul", "Penulis", "Penerbit"};
        Object[][] data = {
                {"1", "Java Programming", "John Doe", "Tech Books"},
                {"2", "Advanced Java", "Jane Smith", "Code Publishers"},
                {"3", "Learning Swing", "Michael Brown", "GUI Press"}
        };
        JTable bukuTable = new JTable(data, columnNames);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(bukuTable);
        dataBukuPanel.add(scrollPane, BorderLayout.CENTER);

        // Display the new panel in the center of the frame
        getContentPane().removeAll();
        add(dataBukuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
            }
        });
    }
}
