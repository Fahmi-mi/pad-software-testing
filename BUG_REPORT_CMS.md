# LAPORAN BUG FORMAL — CMS Tentang Dental

Berikut adalah 2 contoh laporan bug formal yang sering ditemukan pada modul CMS/Manajemen Konten web medis atau dental.

---

## BUG-005 · Gambar Promo Gagal Terunggah Karena Validasi Ukuran File Sisi Klien Tidak Konsisten

| Field | Detail |
|-------|--------|
| **ID** | BUG-005 |
| **Severity** | **High** (Fungsi utama terganggu) |
| **Prioritas** | **High** |
| **Status** | Open |
| **TC Terkait** | TC-Promo-02 |
| **Modul** | CMS - Promo Page |

### Deskripsi Masalah
Ketika Admin mencoba mengunggah gambar promo beresolusi tinggi (format JPG, ukuran 4.5MB), sistem tidak memberikan pesan error/validasi apa pun di antarmuka (UI). Namun, saat tombol "Tambahkan Promo" diklik, form terkirim tetapi gambar tidak muncul di daftar promo (pecah/corrupted), dan log jaringan (network tab) menunjukkan response `500 Internal Server Error` dari server backend. Validasi batas maksimal ukuran file (max 2MB) tidak dipaksakan di sisi klien (frontend).

### Langkah Reproduksi
1. Login sebagai Admin ke dashboard Tentang Dental.
2. Navigasi ke halaman manajemen Promo (`/admin/promo`).
3. Klik area "Unggah Gambar Promo" dan pilih file gambar berukuran **4.5MB**.
4. Isi field judul, harga awal, harga diskon, dan deskripsi dengan data valid.
5. Klik tombol **Tambahkan Promo**.
6. Amati perilaku UI dan daftar promo yang terbuat.

### Hasil yang Diharapkan (Expected Result)
* Sistem harus mendeteksi secara instan di sisi klien jika ukuran file melebihi batas (misal 2MB) saat diunggah.
* Menampilkan pesan error validasi merah yang jelas di bawah input file: *"Ukuran gambar tidak boleh melebihi 2MB."*
* Tombol "Tambahkan Promo" otomatis dinonaktifkan (disabled) sampai file yang valid diunggah.

### Hasil Aktual (Actual Result)
* File 4.5MB diterima di form tanpa ada peringatan klien.
* Klik submit menghasilkan kegagalan unggah di backend (HTTP 500), namun UI tetap menampilkan state sukses atau membuat card promo kosong tanpa gambar di daftar.

---

## BUG-006 · Karakter Khusus HTML Ter-render Sebagai Teks Mentah pada Halaman Artikel

| Field | Detail |
|-------|--------|
| **ID** | **BUG-006** |
| **Severity** | **Medium** (Masalah kosmetik/fungsional minor) |
| **Prioritas** | **Medium** |
| **Status** | Open |
| **TC Terkait** | TC-Artikel-04 |
| **Modul** | CMS - Artikel Page |

### Deskripsi Masalah
Ketika Admin menulis artikel menggunakan editor Rich Text (Tiptap Editor) dan menyisipkan simbol atau kutipan khusus seperti tanda kutip ganda `“` atau simbol ampersand `&`, karakter-karakter tersebut di-render sebagai entitas HTML mentah (seperti `&ldquo;`, `&rdquo;`, `&amp;`) pada preview artikel maupun saat artikel tersebut di-publish ke publik.

### Langkah Reproduksi
1. Login sebagai Admin ke dashboard Tentang Dental.
2. Navigasi ke halaman manajemen Artikel (`/admin/artikel`).
3. Tulis judul artikel baru.
4. Pada area editor konten, tulis teks berikut: *"Kesehatan gigi & mulut adalah investasi masa depan."*
5. Klik tombol **Simpan Artikel**.
6. Buka halaman daftar artikel publik atau klik tombol preview.

### Hasil yang Diharapkan (Expected Result)
* Karakter khusus dan entitas HTML di-decode secara aman dan di-render sebagai karakter simbol normal (`&`, `“`, `”`) untuk pembaca.

### Hasil Aktual (Actual Result)
* Konten menampilkan teks mentah hasil encoding HTML: *"Kesehatan gigi &amp; mulut adalah investasi masa depan."*
