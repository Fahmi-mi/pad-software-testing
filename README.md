# Automated Testing Reservasi Tentang Dental

## Deskripsi Proyek

Proyek ini merupakan tugas akhir Praktikum Pengujian Perangkat Lunak yang bertujuan untuk melakukan pengujian otomatis (Automation Testing) pada fitur Reservasi aplikasi Tentang Dental.

Pengujian dilakukan menggunakan pendekatan Black Box Testing dengan metode:

* Equivalence Partitioning (EP)
* Boundary Value Analysis (BVA)

Implementasi automation testing menggunakan:

* Selenium WebDriver
* Cucumber BDD
* JUnit 5
* Page Object Model (POM)

Selain itu, proyek ini juga mengimplementasikan Automated Bug Reporting yang secara otomatis menghasilkan laporan bug berdasarkan hasil eksekusi test yang gagal.

---

## User Flow yang Diuji

Fitur yang diuji adalah proses reservasi pasien pada aplikasi Tentang Dental.

Alur pengujian:

1. Pengguna membuka halaman reservasi
2. Mengisi data pasien
3. Memilih dokter
4. Memilih layanan
5. Mengisi keluhan
6. Melakukan submit reservasi

---

## Metode Pengujian

### Equivalence Partitioning (EP)

Digunakan untuk membagi data uji ke dalam kelas valid dan tidak valid.

Contoh:

#### Nama Lengkap

Valid:

* Budi Santoso
* Siti
* Drg. Sania Dara Afiati, Sp.KG

Invalid:

* Kosong
* A

#### Nomor Handphone

Valid:

* 08132059835
* 081320598350

Invalid:

* Kosong
* 081320
* abcdefghijk

---

### Boundary Value Analysis (BVA)

Digunakan untuk menguji nilai batas.

Contoh:

#### Nama Lengkap

* 1 karakter (invalid)
* 2 karakter (valid)
* 3 karakter (valid)

#### Nomor Handphone

* 9 digit (invalid)
* 10 digit (valid)
* 13 digit (valid)
* 14 digit (invalid)

---

## Struktur Project

```text
src
├── test
│   ├── java
│   │   ├── hooks
│   │   ├── pages
│   │   ├── runners
│   │   ├── stepdefinitions
│   │   └── utils
│   │
│   └── resources
│       └── features
```

### Penjelasan Folder

#### pages

Berisi implementasi Page Object Model.

Contoh:

* ReservasiPage.java

#### stepdefinitions

Berisi implementasi langkah-langkah Gherkin yang terhubung dengan Selenium.

Contoh:

* ReservasiSteps.java

#### runners

Berisi Test Runner untuk menjalankan seluruh feature Cucumber.

Contoh:

* TestRunner.java

#### hooks

Berisi setup dan teardown WebDriver.

#### utils

Berisi utility tambahan seperti:

* BugReportGenerator.java

#### features

Berisi file pengujian dalam format Gherkin.

Contoh:

* reservasi.feature

---

## Teknologi yang Digunakan

* Java 17
* Maven
* Selenium WebDriver
* Cucumber
* JUnit 5
* ChromeDriver

---

## Menjalankan Pengujian

### Clone Repository

```bash
git clone <repository-url>
```

### Masuk ke Folder Project

```bash
cd pad-software-testing
```

### Install Dependency

```bash
mvn clean install
```

### Menjalankan Automation Test

```bash
mvn test
```

Hasil pengujian akan menghasilkan file:

```text
target/cucumber-reports/cucumber.json
```

---

## Automated Bug Reporting

Proyek ini mengimplementasikan fitur Automated Bug Reporting.

Alur kerja:

```text
Cucumber Test
        ↓
cucumber.json
        ↓
BugReportGenerator
        ↓
bug_report_auto.csv
```

Generator akan membaca seluruh hasil eksekusi test dari file:

```text
target/cucumber-reports/cucumber.json
```

Kemudian secara otomatis membuat laporan bug untuk setiap skenario yang gagal.

---

## Menjalankan Bug Report Generator

Setelah test selesai dijalankan:

```bash
mvn test
```

Jalankan:

```bash
mvn exec:java -Dexec.mainClass="utils.BugReportGenerator"
```

Atau langsung menjalankan:

```java
BugReportGenerator.main()
```

Hasil laporan akan tersimpan pada:

```text
target/bug-reports/bug_report_auto.csv
```

---

## Hasil Pengujian

### Total Test Execution

* Total Execution : 33
* Passed : 32
* Failed : 1

### Bug yang Ditemukan

BUG-AUTO-001

Kegagalan terjadi pada skenario:

```text
Submit form reservasi dengan data pasien baru yang valid
```

Penyebab:

```text
TimeoutException saat memilih layanan Scaling
```

---

## Tim Pengembang

Proyek ini dikembangkan sebagai tugas akhir Praktikum Pengujian Perangkat Lunak menggunakan pendekatan Automation Testing berbasis Selenium dan Cucumber.
