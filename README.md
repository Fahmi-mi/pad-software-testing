# Analisis Masalah pada Test Suite CMS Management

## Ringkasan Error

### 1. Chrome 149 vs Selenium 4.29.0 — CDP Tidak Kompatibel
**Error:**
```
Unable to find CDP implementation matching 149
Unable to find version of CDP to use for 149.0.7827.103
```
**Penyebab:** Chrome 149 sangat baru; Selenium 4.29.0 belum memiliki DevTools Protocol untuk versi ini.  
**Dampak:** Tidak fatal untuk otomasi dasar (navigasi, klik, input), tetapi fitur DevTools (pengambilan log, network interception) tidak akan bekerja.

---

### 2. Browser Tidak Navigasi ke URL yang Benar (sudah diperbaiki)
**Error:** `data:` sebagai URL, timeout `login-email-input`  
**Lokasi:** `hooks/Hooks.java`  
**Perbaikan:** Tambah `WebDriverManager.chromedriver().setup()` dan `driver.get("...")` di `@Before`.

---

### 3. Selector Elemen Tidak Cocok dengan Aplikasi
**Error (Layanan):**
```
waiting for visibility of [data-testid='layanan-deskripsi-input'], #layanan-deskripsi, textarea
```
**Lokasi:** `pages/LayananPage.java:38`  
**Masalah:** Selector `data-testid` mungkin tidak sesuai dengan atribut HTML di aplikasi. Cari selector alternatif.

**Error (Dokter):**
```
waiting for visibility of [data-testid='dokter-nama-input']
```
**Lokasi:** `pages/ProfilDokterPage.java:137`

**Error (Testimoni):**
```
waiting for visibility of [data-testid='testimoni-nama-input']
```
**Lokasi:** `pages/TestimoniPage.java:127`

**Error (Artikel):**
```
waiting for element to be clickable: By.xpath: //*[contains(@class,'cursor-pointer')...
```
**Lokasi:** `pages/ArtikelPage.java:122`

---

### 4. Ketergantungan Data Antar Skenario (Test Pollution)
Beberapa skenario menulis data, skenario lain membaca data yang sama — menyebabkan kegagalan berantai:

| Skenario | Expected | Actual | Penyebab |
|----------|----------|--------|----------|
| 3.2 Nama dokter ke-0 | `drg. Putri, Sp.KGA` | `drg. Budi Santoso, Sp.Ort` | Skenario 3.1 menambah dokter baru |
| 4.2 Jumlah gambar galeri | 6 | 7 | Skenario 4.1 menambah 1 gambar |
| 6.2 Jumlah artikel | 5 | 6 | Skenario 6.1 menambah 1 artikel |
| 3.1 Dokter baru terdaftar | true | false | Kemungkinan submit gagal/tidak terdeteksi |

**Rekomendasi:** Jalankan skenario secara independen atau reset data sebelum tiap skenario. Gunakan `@Before` untuk menyetel ulang state aplikasi.

---

### 5. Form Validation Test Tidak Akurat
**Error (Dokter form validation):**
```
AssertionFailedError: Form dokter terkirim padahal tidak ada input.
```
**Lokasi:** `steps/CmsManagementSteps.java:307`  
**Masalah:** Assert `assertFalse(isDoctorPresentInList(""))` tidak tepat karena string kosong mungkin tetap lolos. Gunakan validasi HTML5 (`checkValidity()` atau `validationMessage`).

**Error (Testimoni form validation):**
```
AssertionFailedError: Field nama testimoni tidak menampilkan validasi error.
```
**Lokasi:** `pages/TestimoniPage.java:isNameInputInvalid()`  
**Masalah:** Method `isNameInputInvalid()` mengembalikan `false` — selector atau logic validasi perlu diperbaiki.

---

### 6. Aksi Hover dan Hapus Galeri Gagal
**Error:**
```
Gambar galeri masih tampil setelah dihapus.
```
**Lokasi:** `pages/GaleriPage.java`  
**Masalah:** Tombol hapus mungkin tidak muncul (CSS hover mungkin tidak bekerja di Selenium), atau konfirmasi dialog tidak ditangani.

---

### 7. Dialog Edit Menampilkan Data Kosong
**Error (Artikel edit):**
```
Expected: "5 Efek Samping..."
Actual  : ""
```
**Lokasi:** `pages/ArtikelPage.java`  
**Masalah:** Dialog terbuka (ada screenshot 183KB) tetapi field judul kosong. Mungkin ada animation delay, atau dialog belum selesai load saat dibaca.

---

### 8. SLF4J No Providers
**Error:**
```
SLF4J(W): No SLF4J providers were found.
```
**Perbaikan:** Tambahkan dependency logger seperti `logback-classic` atau `log4j-slf4j2-impl` di `pom.xml`.

---

## Rekomendasi Prioritas

| Prioritas | Perbaikan | File Terkait |
|-----------|-----------|-------------|
| 🔴 Tinggi | Fix selector layanan, dokter, testimoni, artikel agar cocok dengan DOM aktual | Semua `pages/*Page.java` |
| 🔴 Tinggi | Isolasi data antar skenario (gunakan DB seed atau reset) | `features/*.feature` |
| 🟡 Sedang | Perbaiki form validation tests dengan `checkValidity()` | `steps/CmsManagementSteps.java` |
| 🟡 Sedang | Fix hover + delete di galeri | `pages/GaleriPage.java` |
| 🟢 Rendah | Tambah SLF4J provider | `pom.xml` |
| 🟢 Rendah | Upgrade Selenium ke versi terbaru untuk dukungan Chrome 149 | `pom.xml` |
