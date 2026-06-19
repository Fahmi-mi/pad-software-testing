# language: en
# Halaman yang diuji: https://tentangdental.netlify.app/reservasi

@reservasi
Feature: Form Reservasi Tentang Dental
  Sebagai calon pasien Tentang Dental,
  Saya ingin mengisi form reservasi,
  Agar saya bisa mendapatkan jadwal pemeriksaan gigi.

  Background:
    Given Pengguna membuka halaman Reservasi

  # ==============================================================
  # BAGIAN 1: TAMPILAN FORM
  # Memverifikasi semua elemen form tersedia
  # ==============================================================

  @TC-01 @tampilan
  Scenario: Form reservasi menampilkan semua field yang diperlukan
    Then Form reservasi beserta semua field wajib ditampilkan

  @TC-02 @tampilan
  Scenario: Informasi kontak WhatsApp tersedia di halaman reservasi
    Then Informasi kontak WhatsApp ditampilkan

  @TC-03 @tampilan
  Scenario: Checkbox pasien lama tersedia di form
    Then Checkbox pasien lama tersedia di form

  @TC-04 @tampilan
  Scenario: Dropdown Jam Reservasi tidak aktif sebelum memilih jadwal
    Then Dropdown Jam Reservasi tidak aktif sebelum jadwal dipilih


  # ==============================================================
  # BAGIAN 2: VALIDASI NAMA LENGKAP
  # Metode: Equivalence Partitioning (EP) + Boundary Value Analysis (BVA)
  #
  # Partisi EP:
  #   - Kelas VALID   : 2–100 karakter, mengandung huruf
  #   - Kelas INVALID : kosong, 1 karakter, hanya angka/simbol
  #
  # BVA:
  #   Batas bawah: 1 char (invalid) | 2 char (valid) | 3 char (valid)
  # ==============================================================

  @TC-05 @validasi @EP
  Scenario: Submit form kosong ditolak sistem
    When Pengguna menekan tombol Submit tanpa mengisi form
    Then Sistem menolak submit dan menampilkan validasi

  @TC-06 @validasi @EP @nama-valid
  Scenario Outline: EP - Nama valid dari kelas VALID diterima sistem
    When Pengguna mengisi Nama Lengkap dengan "<nama>"
    Then Field Nama Lengkap berhasil menerima input "<nama>"

    Examples:
      | nama                   | keterangan                     |
      | Budi Santoso           | Nama normal dengan spasi       |
      | Siti                   | Nama pendek 4 karakter         |
      | Dr. Ahmad Rizal Sp.KG  | Nama dengan gelar dan titik    |

  @TC-07 @validasi @EP @nama-invalid
  Scenario Outline: EP - Nama dari kelas INVALID ditolak sistem
    When Pengguna mengisi Nama Lengkap dengan "<nama>"
    And Pengguna menekan tombol Submit tanpa mengisi form
    Then Sistem menolak submit dan menampilkan validasi

    Examples:
      | nama | keterangan                       |
      |      | Nama kosong (EP kelas invalid)   |
      | A    | 1 karakter (BVA batas bawah - 1) |

  @TC-08 @validasi @BVA @nama-bva
  Scenario Outline: BVA - Nama tepat di batas bawah (2 dan 3 karakter)
    When Pengguna mengisi Nama Lengkap dengan "<nama>"
    Then Field Nama Lengkap berhasil menerima input "<nama>"

    Examples:
      | nama | keterangan                     |
      | AB   | 2 karakter — BVA on-point bawah |
      | ABC  | 3 karakter — BVA in-point bawah |


  # ==============================================================
  # BAGIAN 3: VALIDASI NOMOR HANDPHONE
  # Metode: Equivalence Partitioning (EP) + Boundary Value Analysis (BVA)
  #
  # Partisi EP:
  #   - Kelas VALID   : 10–13 digit, semua angka
  #   - Kelas INVALID : kosong, huruf/simbol, < 10 digit, > 13 digit
  #
  # BVA:
  #   Batas bawah : 9 digit (invalid) | 10 digit (valid) | 11 digit (valid)
  #   Batas atas  : 12 digit (valid)  | 13 digit (valid) | 14 digit (invalid)
  # ==============================================================

  @TC-09 @validasi @EP @hp-valid
  Scenario Outline: EP - Nomor HP valid dari kelas VALID diterima
    When Pengguna mengisi Nomor Handphone dengan "<nomor>"
    Then Field Nomor Handphone berhasil menerima input "<nomor>"

    Examples:
      | nomor         | keterangan               |
      | 08132059835   | 11 digit — nominal valid  |
      | 081320598350  | 12 digit — kelas valid    |

  @TC-10 @validasi @BVA @hp-bva
  Scenario Outline: BVA - Nomor HP tepat di nilai batas
    When Pengguna mengisi Nomor Handphone dengan "<nomor>"
    Then Field Nomor Handphone berhasil menerima input "<nomor>"

    Examples:
      | nomor         | keterangan                  |
      | 0813205983    | 10 digit — BVA on-point bawah |
      | 0813205983500 | 13 digit — BVA on-point atas  |

  @TC-11 @validasi @EP @hp-invalid
  Scenario Outline: EP - Nomor HP kelas INVALID ditolak sistem
    When Pengguna mengisi Nomor Handphone dengan "<nomor>"
    And Pengguna menekan tombol Submit tanpa mengisi form
    Then Sistem menolak submit dan menampilkan validasi

    Examples:
      | nomor       | keterangan                      |
      |             | Kosong (EP kelas invalid)       |
      | 081320      | 6 digit (BVA batas bawah - 1)  |
      | abcdefghijk | Huruf semua (EP kelas invalid)  |


  # ==============================================================
  # BAGIAN 4: VALIDASI KELUHAN
  # Metode: Boundary Value Analysis (BVA)
  # Asumsi: minimum 5 karakter, tidak ada batas atas eksplisit
  # ==============================================================

  @TC-12 @validasi @BVA @keluhan-bva
  Scenario Outline: BVA - Keluhan pada nilai batas panjang karakter
    When Pengguna mengisi Keluhan dengan "<keluhan>"
    Then Field Keluhan berhasil menerima input

    Examples:
      | keluhan                                                          | keterangan              |
      | Sakit                                                            | 5 karakter (batas bawah)|
      | Gigi berlubang dan terasa nyeri saat makan makanan yang dingin   | Lebih dari 50 karakter  |


  # ==============================================================
  # BAGIAN 5: TANGGAL LAHIR & AUTO HITUNG UMUR
  # ==============================================================

  @TC-13 @fungsional
  Scenario Outline: Umur otomatis terhitung setelah tanggal lahir diisi
    When Pengguna mengisi Tanggal Lahir dengan "<tanggal>"
    Then Field Umur terisi secara otomatis

    Examples:
      | tanggal    | keterangan             |
      | 1990-01-01 | Pasien dewasa (1990)   |
      | 2005-06-15 | Pasien remaja (2005)   |


  # ==============================================================
  # BAGIAN 6: PILIHAN LAYANAN
  # ==============================================================

  @TC-14 @tampilan
  Scenario: Dropdown layanan tersedia di form reservasi
    Then Dropdown layanan ditampilkan

  @TC-15 @fungsional
  Scenario Outline: Pengguna dapat memilih layanan yang tersedia
    When Pengguna memilih layanan "<layanan>"
    Then Layanan "<layanan>" berhasil dipilih

    Examples:
      | layanan                     |
      | Scaling                      |
      | Tambal Gigi                  |
      | Oral Profilaksis             |
      | Perawatan Saluran Akar (PSA) |

  @TC-16 @validasi
  Scenario: Submit tanpa memilih layanan ditolak sistem
    When Pengguna mengisi seluruh field wajib kecuali layanan
    And Pengguna menekan tombol Submit
    Then Sistem menolak submit dan menampilkan validasi layanan


  # ==============================================================
  # BAGIAN 7: FITUR PASIEN LAMA
  # ==============================================================

  @TC-17 @fungsional @pasien-lama
  Scenario: Checkbox pasien lama dapat diaktifkan
    When Pengguna mencentang checkbox pasien lama
    Then Opsi nomor pasien aktif setelah checkbox dicentang

  @TC-18 @validasi @pasien-lama
  Scenario: Pasien lama wajib mengisi nomor pasien
    When Pengguna mencentang checkbox pasien lama
    And Pengguna menekan tombol Submit tanpa mengisi nomor pasien
    Then Sistem menolak submit dan menampilkan validasi nomor pasien

  @TC-19 @fungsional @pasien-lama
  Scenario: Pasien lama dapat mengisi nomor pasien
    When Pengguna mencentang checkbox pasien lama
    And Pengguna mengisi Nomor Pasien dengan "AA001"
    Then Field Nomor Pasien berhasil menerima input "AA001"


  # ==============================================================
  # BAGIAN 8: HAPPY PATH - Submit form lengkap
  # ==============================================================

  @TC-20 @happy-path
  Scenario: Submit form reservasi dengan data pasien baru yang valid
    When Pengguna mengisi form dengan data berikut:
      | nama         | Budi Santoso    |
      | tanggalLahir | 1995-08-20      |
      | noHP         | 08132059835     |
      | keluhan      | Gigi berlubang dan terasa sakit saat minum air dingin |
    And Pengguna menekan tombol Submit
    Then Sistem memproses pengiriman reservasi
