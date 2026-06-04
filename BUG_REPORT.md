# BUG REPORT — Form Reservasi Tentang Dental

**URL Yang Diuji** : https://tentangdental.netlify.app/reservasi  
**Tanggal Pengujian** : [isi tanggal]  
**Tester** : [nama]  
**Browser** : Google Chrome [versi]  
**OS** : Windows 11 / macOS

---

## BUG-001 · Dropdown Jam Tidak Disabled Sebelum Jadwal Dipilih

| Field | Detail |
|-------|--------|
| **ID** | BUG-001 |
| **Severity** | Major |
| **Prioritas** | High |
| **Status** | Open |
| **TC Terkait** | TC-04 |

**Langkah Reproduksi**
1. Buka https://tentangdental.netlify.app/reservasi
2. Jangan klik dropdown "Jadwal Periksa"
3. Langsung coba klik dropdown "Jam Reservasi"

**Expected** — Dropdown Jam tidak bisa diklik / tampil teks "Pilih jadwal periksa terlebih dahulu"  
**Actual** — [isi setelah diuji]  
**Screenshot** — [lampirkan]

---

## BUG-002 · Tidak Ada Validasi Format Nomor HP di Sisi Client

| Field | Detail |
|-------|--------|
| **ID** | BUG-002 |
| **Severity** | Major |
| **Prioritas** | High |
| **Status** | Open |
| **TC Terkait** | TC-11 |

**Langkah Reproduksi**
1. Buka halaman Reservasi
2. Isi Nomor Handphone dengan huruf: `abcdefghijk`
3. Isi field wajib lain dengan data valid
4. Klik Submit

**Expected** — Sistem menampilkan error "Nomor HP harus berupa angka (10–13 digit)"  
**Actual** — [isi setelah diuji]  
**Screenshot** — [lampirkan]

---

## BUG-003 · Field Umur Tidak Otomatis Terhitung dari Tanggal Lahir

| Field | Detail |
|-------|--------|
| **ID** | BUG-003 |
| **Severity** | Minor |
| **Prioritas** | Medium |
| **Status** | Open |
| **TC Terkait** | TC-13 |

**Langkah Reproduksi**
1. Buka halaman Reservasi
2. Isi Tanggal Lahir dengan: `1990-01-01`
3. Klik di luar field (blur)
4. Perhatikan field "Umur"

**Expected** — Field Umur otomatis terisi dengan `35`  
**Actual** — [isi setelah diuji]  
**Screenshot** — [lampirkan]

---

## BUG-004 · Submit Tanpa Memilih Dokter Tidak Menampilkan Pesan Error Spesifik

| Field | Detail |
|-------|--------|
| **ID** | BUG-004 |
| **Severity** | Major |
| **Prioritas** | High |
| **Status** | Open |
| **TC Terkait** | TC-05 |

**Langkah Reproduksi**
1. Buka halaman Reservasi
2. Isi semua field kecuali "Pilihan Dokter"
3. Klik Submit

**Expected** — Muncul pesan error di dekat field "Pilihan Dokter": "Dokter harus dipilih"  
**Actual** — [isi setelah diuji]  
**Screenshot** — [lampirkan]

---

## Template Bug Baru

Salin template ini untuk setiap bug yang ditemukan saat menjalankan test:

```
## BUG-00X · [Judul Singkat]

| Field | Detail |
|-------|--------|
| **ID** | BUG-00X |
| **Severity** | Blocker / Major / Minor / Trivial |
| **Prioritas** | Critical / High / Medium / Low |
| **Status** | Open |
| **TC Terkait** | TC-XX |

**Langkah Reproduksi**
1. 
2. 
3. 

**Expected** —   
**Actual** —   
**Screenshot** — [lampirkan]
```

---

## Ringkasan

| ID | Judul | Severity | Prioritas | Status |
|----|-------|----------|-----------|--------|
| BUG-001 | Dropdown Jam tidak disabled | Major | High | Open |
| BUG-002 | Validasi format HP tidak ada | Major | High | Open |
| BUG-003 | Umur tidak auto-hitung | Minor | Medium | Open |
| BUG-004 | Tidak ada error spesifik field Dokter | Major | High | Open |
