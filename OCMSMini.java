package ocms.mini;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Role {
    String userRole;
    String password;

    Role(String userRole, String password) {
        this.userRole = userRole;
        this.password = password;
    }
}

class Complain {
    int id;
    String nama;
    String judul;
    String deskripsi;
    String kategori;
    String status;
    String remark;
    LocalDateTime timestamp;
    String tanggaldibuat;
    String updateTerakhir;
    String selesai;

    Complain(int id, String nama, String judul, String deskripsi, String kategori) {
        this.id = id;
        this.nama = nama;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.status = "Dikirim";
        this.remark = null;
        this.timestamp = LocalDateTime.now();
        this.tanggaldibuat = this.timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

public class OCMSMini {

    static Role[] roles = {
        new Role("admin", "1234"),
        new Role("user", "")
    };

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Complain> complains = new ArrayList<>();
    static ArrayList<String> categories = new ArrayList<>();

    static int nextId = 1;

    public static void main(String[] args) {
        // Kategori default awal (bisa ditambah/dihapus oleh Admin)
        categories.add("Fasilitas");
        categories.add("Layanan");
        categories.add("Administrasi");
        categories.add("Teknis");

        opsiStatus();
    }

    // Helper untuk membaca input angka dengan aman agar tidak crash jika diisi huruf
    static int bacaAngka(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka. Silakan coba lagi.");
            }
        }
    }

    static void opsiStatus() {
        int opsiStatus;
        do {
            System.out.println("===== Welcome to Online Complaint Management System =====");
            System.out.println("Silakan masukkan status anda : ");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Keluar");

            opsiStatus = bacaAngka("Pilih status: ");
            System.out.println();

            switch (opsiStatus) {
                case 1 -> adminlogin();
                case 2 -> dashboardUser();
                case 3 -> {
                    System.out.println("Terima kasih telah menggunakan aplikasi ini.");
                    System.exit(0);
                }
                default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.\n");
            }
        } while (opsiStatus != 3);
    }

    static void adminlogin() {
        while (true) {
            System.out.println("===== Admin Login =====");
            System.out.println("Masukan password :");
            System.out.println("Pilih 0 untuk kembali ke halaman utama\n");
            System.out.print("Password : ");
            String passwordInput = scanner.nextLine();

            if (passwordInput.equals("0")) {
                return;
            }
            if (passwordInput.equals(roles[0].password)) {
                System.out.println("Password benar.\n");
                dashboardAdmin();
                return;
            } else {
                System.out.println("Password salah.");
                System.out.println("Silakan coba lagi.\n");
            }
        }
    }

    static void dashboardAdmin() {
        int pilihan;
        do {
            System.out.println("  === OCMS : Admin Dashboard ===");
            System.out.println("1. Buat Pengaduan");
            System.out.println("2. Lihat Pengaduan");
            System.out.println("3. Cari Pengaduan");
            System.out.println("4. Update Pengaduan");
            System.out.println("5. Statistik Pengaduan");
            System.out.println("6. Define Kategori");
            System.out.println("7. Kembali ke menu utama");
            System.out.println("8. Keluar");

            pilihan = bacaAngka("Pilih menu: ");
            System.out.println();

            switch (pilihan) {
                case 1 -> tambahPengaduan();
                case 2 -> lihatPengaduan();
                case 3 -> cariPengaduan();
                case 4 -> updatePengaduan();
                case 5 -> statistikPengaduan();
                case 6 -> kelolaKategori();
                case 7 -> { return; }
                case 8 -> {
                    System.out.println("Terima kasih telah menggunakan aplikasi ini.");
                    System.exit(0);
                }
                default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.\n");
            }
        } while (pilihan != 8);
    }

    static void dashboardUser() {
        int pilihan;
        do {
            System.out.println("  === OCMS : User Dashboard ===");
            System.out.println("0. Kembali");
            System.out.println("1. Buat Pengaduan");
            System.out.println("2. Lihat Pengaduan");
            System.out.println("3. Keluar");

            pilihan = bacaAngka("Pilih menu: ");
            System.out.println();

            switch (pilihan) {
                case 0 -> { return; }
                case 1 -> tambahPengaduan();
                case 2 -> lihatPengaduan();
                case 3 -> {
                    System.out.println("Terima kasih telah menggunakan aplikasi ini.");
                    System.exit(0);
                }
                default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.\n");
            }
        } while (pilihan != 3);
    }

    // Fitur Define Category untuk Admin
    static void kelolaKategori() {
        int pilihan;
        do {
            System.out.println("===== Define / Kelola Kategori =====");
            System.out.println("1. Lihat Daftar Kategori");
            System.out.println("2. Tambah Kategori Baru");
            System.out.println("3. Hapus Kategori");
            System.out.println("0. Kembali ke Dashboard Admin");

            pilihan = bacaAngka("Pilih aksi: ");
            System.out.println();

            switch (pilihan) {
                case 1 -> {
                    System.out.println("--- Daftar Kategori Saat Ini ---");
                    if (categories.isEmpty()) {
                        System.out.println("Belum ada kategori yang didefinisikan.");
                    } else {
                        for (int i = 0; i < categories.size(); i++) {
                            System.out.println((i + 1) + ". " + categories.get(i));
                        }
                    }
                    System.out.println();
                }
                case 2 -> {
                    System.out.print("Masukkan nama kategori baru: ");
                    String kategoriBaru = scanner.nextLine().trim();

                    if (kategoriBaru.isEmpty()) {
                        System.out.println("Nama kategori tidak boleh kosong.\n");
                    } else {
                        boolean sudahAda = false;
                        for (String kat : categories) {
                            if (kat.equalsIgnoreCase(kategoriBaru)) {
                                sudahAda = true;
                                break;
                            }
                        }

                        if (sudahAda) {
                            System.out.println("Kategori \"" + kategoriBaru + "\" sudah terdaftar.\n");
                        } else {
                            categories.add(kategoriBaru);
                            System.out.println("Kategori \"" + kategoriBaru + "\" berhasil ditambahkan.\n");
                        }
                    }
                }
                case 3 -> {
                    if (categories.isEmpty()) {
                        System.out.println("Tidak ada kategori untuk dihapus.\n");
                        break;
                    }
                    System.out.println("--- Pilih Kategori yang Akan Dihapus ---");
                    for (int i = 0; i < categories.size(); i++) {
                        System.out.println((i + 1) + ". " + categories.get(i));
                    }
                    int indeksHapus = bacaAngka("Nomor kategori (0 untuk batal): ");
                    if (indeksHapus == 0) {
                        System.out.println("Penghapusan dibatalkan.\n");
                    } else if (indeksHapus >= 1 && indeksHapus <= categories.size()) {
                        String dihapus = categories.remove(indeksHapus - 1);
                        System.out.println("Kategori \"" + dihapus + "\" berhasil dihapus.\n");
                    } else {
                        System.out.println("Nomor kategori tidak valid.\n");
                    }
                }
                case 0 -> { return; }
                default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.\n");
            }
        } while (pilihan != 0);
    }

    static void tambahPengaduan() {
        System.out.println("===== Buat Pengaduan =====");

        if (categories.isEmpty()) {
            System.out.println("Kategori pengaduan belum tersedia. Silakan hubungi Admin untuk mendefinisikan kategori terlebih dahulu.\n");
            return;
        }

        System.out.print("Masukkan nama: ");
        String nama = scanner.nextLine();

        System.out.print("Masukkan judul pengaduan: ");
        String judul = scanner.nextLine();

        System.out.print("Masukkan deskripsi pengaduan: ");
        String deskripsi = scanner.nextLine();

        System.out.println("Pilih kategori pengaduan:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }

        int pilihanKategori;
        while (true) {
            pilihanKategori = bacaAngka("Masukkan nomor kategori: ");
            if (pilihanKategori >= 1 && pilihanKategori <= categories.size()) {
                break;
            }
            System.out.println("Nomor kategori tidak valid. Pilih antara 1 sampai " + categories.size() + ".");
        }
        String kategori = categories.get(pilihanKategori - 1);

        Complain complain = new Complain(nextId, nama, judul, deskripsi, kategori);
        complains.add(complain);

        System.out.println("\nPengaduan berhasil ditambahkan.");
        System.out.println("ID Pengaduan: " + nextId + "\n");

        nextId++;
    }

    static void lihatPengaduan() {
        System.out.println("===== Lihat Pengaduan =====");
        if (complains.isEmpty()) {
            System.out.println("Belum ada pengaduan yang dibuat.");
        } else {
            for (Complain complain : complains) {
                System.out.println("ID: " + complain.id);
                System.out.println("Nama: " + complain.nama);
                System.out.println("Judul: " + complain.judul);
                System.out.println("Deskripsi: " + complain.deskripsi);
                System.out.println("Kategori: " + complain.kategori);
                System.out.println("Status: " + complain.status);
                System.out.println("Tanggal dibuat: " + complain.tanggaldibuat);
                System.out.println("Update Terakhir: " + (complain.updateTerakhir != null ? complain.updateTerakhir : "-"));
                System.out.println("Selesai: " + (complain.selesai != null ? complain.selesai : "-"));
                if (complain.remark != null) {
                    System.out.println("Catatan: " + complain.remark);
                }
                System.out.println("-------------------------");
            }
        }
        System.out.println();
    }

    static void cariPengaduan() {
        System.out.println("===== Cari Pengaduan =====");
        int id = bacaAngka("Masukkan ID pengaduan: ");

        boolean found = false;
        for (Complain complain : complains) {
            if (complain.id == id) {
                System.out.println("ID: " + complain.id);
                System.out.println("Nama: " + complain.nama);
                System.out.println("Judul: " + complain.judul);
                System.out.println("Deskripsi: " + complain.deskripsi);
                System.out.println("Kategori: " + complain.kategori);
                System.out.println("Status: " + complain.status);
                System.out.println("Tanggal dibuat: " + complain.tanggaldibuat);
                System.out.println("Update Terakhir: " + (complain.updateTerakhir != null ? complain.updateTerakhir : "-"));
                System.out.println("Selesai: " + (complain.selesai != null ? complain.selesai : "-"));
                if (complain.remark != null) {
                    System.out.println("Catatan: " + complain.remark);
                }
                System.out.println("\n");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Pengaduan dengan ID " + id + " tidak ditemukan.\n");
        }
    }

    static void updatePengaduan() {
        System.out.println("===== Update Pengaduan =====");
        int id = bacaAngka("Masukkan ID pengaduan yang ingin diupdate: ");
        System.out.println();

        boolean found = false;
        for (Complain complain : complains) {
            if (complain.id == id) {
                System.out.println("=== Masukan Status Baru ===");
                System.out.println("1. Dikirim");
                System.out.println("2. Diproses");
                System.out.println("3. Selesai");
                System.out.println("4. Pending");
                System.out.println("5. Ditolak");
                int statusBaru = bacaAngka("Pilih status baru : ");

                switch (statusBaru) {
                    case 1 -> {
                        complain.status = "Dikirim";
                        complain.remark = null;
                    }
                    case 2 -> {
                        complain.status = "Diproses";
                        complain.remark = null;
                    }
                    case 3 -> {
                        complain.status = "Selesai";
                        complain.remark = null;
                        complain.selesai = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                    case 4 -> complain.status = "Pending";
                    case 5 -> complain.status = "Ditolak";
                    default -> {
                        System.out.println("Status tidak valid. Status tidak diperbarui.\n");
                        return;
                    }
                }

                if (statusBaru == 4 || statusBaru == 5) {
                    System.out.print("Tuliskan alasan : ");
                    complain.remark = scanner.nextLine();
                }

                System.out.println("Status pengaduan berhasil diperbarui.\n");
                complain.updateTerakhir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Pengaduan dengan ID " + id + " tidak ditemukan.\n");
        }
    }

    static void statistikPengaduan() {
        int dikirim = 0;
        int diproses = 0;
        int selesai = 0;
        int pending = 0;
        int ditolak = 0;

        for (Complain complain : complains) {
            switch (complain.status) {
                case "Dikirim" -> dikirim++;
                case "Diproses" -> diproses++;
                case "Selesai" -> selesai++;
                case "Pending" -> pending++;
                case "Ditolak" -> ditolak++;
            }
        }

        System.out.println("===== Statistik Pengaduan =====");
        int totalPengaduan = complains.size();
        System.out.println("Total Pengaduan: " + totalPengaduan);
        System.out.println("Dikirim: " + dikirim);
        System.out.println("Diproses: " + diproses);
        System.out.println("Selesai: " + selesai);
        System.out.println("Pending: " + pending);
        System.out.println("Ditolak: " + ditolak);
        System.out.println();
    }
}
