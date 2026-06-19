# Reservasi Test Automation - Tentang Dental

Automated testing untuk halaman **Reservasi** (https://tentangdental.netlify.app/reservasi)
menggunakan **Selenium WebDriver + Page Object Model (POM) + Cucumber (Gherkin/BDD)**.

## Struktur Proyek

```
automation/
├── pom.xml                                   # dependency & plugin Maven
└── src/test/
    ├── java/
    │   ├── pages/
    │   │   ├── BasePage.java                 # helper umum (POM base class)
    │   │   └── ReservasiPage.java             # Page Object form Reservasi
    │   ├── hooks/
    │   │   └── Hooks.java                     # setup/teardown WebDriver + screenshot on fail
    │   ├── stepdefinitions/
    │   │   └── ReservasiSteps.java            # Glue code Gherkin -> Selenium
    │   ├── runners/
    │   │   └── TestRunner.java                # entry point JUnit + Cucumber
    │   └── utils/
    │       └── BugReportGenerator.java        # AUTOMATE GENERATION OF BUG REPORT
    └── resources/features/
        └── reservasi.feature                  # skenario Gherkin (EP & BVA)
```

## Prasyarat

- JDK 11+
- Maven 3.8+
- Google Chrome terinstal (driver dikelola otomatis oleh WebDriverManager)
- Koneksi internet (untuk download dependency Maven & untuk mengakses situs target)

## Cara Menjalankan

```bash
cd automation

# 1. Jalankan seluruh skenario test
mvn test

# Atau hanya skenario smoke test
mvn test -Dcucumber.filter.tags="@smoke"

# 2. Generate laporan HTML cantik dari hasil test (otomatis via plugin saat verify)
mvn verify
# -> buka target/cucumber-html-reports/overview-features.html di browser

# 3. AUTOMATE GENERATION BUG REPORT
# Setelah `mvn test` selesai, jalankan generator berikut. Script ini membaca
# target/cucumber-reports/cucumber.json dan otomatis membuat baris bug untuk
# setiap skenario yang FAILED.
mvn exec:java -Dexec.mainClass="utils.BugReportGenerator"
# -> hasil: target/bug-reports/bug_report_auto.csv
# Salin baris-baris pada CSV ini ke sheet "Bug Report" pada
# Bug_Report_Reservasi_TentangDental.xlsx untuk dokumentasi resmi kelompok.
```

## PENTING: Tentang Locator (WAJIB DIBACA SEBELUM RUN)

Situs target dibangun dengan React/Next.js (SPA). Saya menyusun `ReservasiPage.java`
berdasarkan **teks label yang terlihat di halaman** (Nama Lengkap, Nomor Handphone,
Jadwal Periksa, dst.) karena atribut DOM asli (id/name/data-testid) tidak bisa
dipastikan tanpa membuka DevTools langsung pada browser kalian.

Sebelum menjalankan test untuk pertama kali, sebaiknya tim:

1. Buka https://tentangdental.netlify.app/reservasi di Chrome.
2. Klik kanan pada tiap field -> **Inspect** -> catat atribut `id`, `name`, atau
   `data-testid` jika ada, serta cek bagaimana date-picker dan dropdown jam
   bekerja (apakah `<input>`, custom `<div>` kalender, atau `<select>` biasa).
3. Sesuaikan method locator di `ReservasiPage.java` (mis. ganti `inputByLabel(...)`
   menjadi `By.id("namaLengkap")` jika tersedia) supaya lebih stabil & cepat.
4. Jalankan dulu tag `@smoke` saja untuk memverifikasi locator sebelum
   menjalankan seluruh suite.

Ini adalah praktik wajar dalam pengujian otomatis terhadap SPA: locator memang
perlu diverifikasi terhadap DOM aktual, bukan hanya dari hasil render visual.

## Mapping Test Case <-> Skenario Gherkin

`reservasi.feature` versi final tim menggunakan tag `@TC-01` s/d `@TC-20` pada
tiap skenario (mis. `@TC-20 @happy-path`). Tag inilah yang jadi ID acuan untuk
traceability ke test suite, bukan ID `TC_RSV_xxx` pada draft awal. Jika ingin
sinkron penuh, sheet "Traceability Matrix" di
`Test_Suite_Reservasi_TentangDental.xlsx` sebaiknya diperbarui memakai ID
TC-01..TC-20 ini (beri tahu saya kalau mau dibuatkan ulang).

Ringkasan cakupan TC-01..TC-20:
- TC-01 s/d TC-04: tampilan form (field wajib, kontak WhatsApp, checkbox pasien
  lama, dropdown Jam Reservasi terkunci)
- TC-05 s/d TC-08: EP & BVA Nama Lengkap
- TC-09 s/d TC-11: EP & BVA Nomor Handphone
- TC-12: BVA Keluhan
- TC-13: auto-hitung Umur dari Tanggal Lahir
- TC-14 s/d TC-16: dropdown & validasi Layanan
- TC-17 s/d TC-19: fitur pasien lama & Nomor Pasien
- TC-20: happy path submit form lengkap (pakai Gherkin DataTable)

## Field "Nomor Pasien" (TC-17..TC-19)

Field ini diasumsikan baru muncul/aktif setelah checkbox "pasien lama"
dicentang. `ReservasiPage.isNomorPasienAktif()` mengecek field tersebut
ditampilkan DAN enabled. Jika ternyata field ini sebenarnya selalu ada di DOM
tapi disabled (bukan hidden), perilaku itu sudah otomatis tertangani oleh
`isEnabled()`. Sesuaikan locator `nomorPasien` di `ReservasiPage.java` setelah
Inspect jika label berbeda dari "Nomor Pasien".

## Teknik Pengujian yang Digunakan

- **Equivalence Partitioning (EP)**: tag `@equivalence-partitioning` - menguji
  representasi dari partisi valid & invalid (format nomor HP, nama kosong, dst.)
- **Boundary Value Analysis (BVA)**: tag `@boundary-value-analysis` - menguji
  nilai tepat di batas (9/10/13/14 digit nomor HP, 2/3/50/51 karakter nama, dst.)

## Catatan Nilai Tambahan (Automated Report Generation)

Dua lapis automated reporting disediakan:

1. **Laporan eksekusi test** (HTML, melalui plugin `net.masterthought:maven-cucumber-reporting`)
   - Dibuat otomatis setiap `mvn verify` dari `target/cucumber-reports/cucumber.json`.
2. **Laporan bug otomatis** (`utils.BugReportGenerator`)
   - Membaca hasil eksekusi yang sama, lalu otomatis menghasilkan baris bug
     report (CSV) untuk setiap skenario yang gagal, lengkap dengan langkah
     reproduksi, pesan error, dan severity yang ditebak otomatis dari nama
     skenario.
