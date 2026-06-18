# language: id
@authentication
Fitur: Autentikasi Admin
  Sebagai seorang admin sistem
  Saya ingin login ke sistem manajemen CMS
  Agar saya dapat mengelola konten website Tentang Dental dengan aman

  Latar Belakang:
  Diberikan admin membuka halaman login

  # ============================================================
  # LOGIN POSITIF
  # ============================================================
  Skenario: 1.1 Admin berhasil login dengan kredensial valid
    Ketika admin mengisi email dengan "admin@tentangdental.com"
    Dan admin mengisi password dengan "password"
    Dan admin mengklik tombol Login
    Maka admin harus diarahkan ke halaman dashboard
    Dan nama admin harus ditampilkan pada header dashboard

  Skenario: 1.2 Admin berhasil login dan membuka dashboard di tab baru
    Ketika admin mengisi email dengan "admin@tentangdental.com"
    Dan admin mengisi password dengan "password"
    Dan admin mengklik tombol Login
    Maka admin harus diarahkan ke halaman dashboard
    Ketika admin membuka halaman admin di tab baru
    Maka halaman dashboard admin harus ditampilkan

  # ============================================================
  # LOGIN NEGATIF
  # ============================================================
  Skenario: 2.1 Admin gagal login dengan password salah
    Ketika admin mengisi email dengan "admin@tentangdental.com"
    Dan admin mengisi password dengan "PasswordSalah123"
    Dan admin mengklik tombol Login
    Maka pesan error "Email atau password yang Anda masukkan salah." harus ditampilkan
    Dan admin harus tetap berada di halaman login

  Skenario: 2.2 Admin gagal login dengan email yang tidak terdaftar
    Ketika admin mengisi email dengan "tidakterdaftar@tentangdental.com"
    Dan admin mengisi password dengan "password"
    Dan admin mengklik tombol Login
    Maka pesan error "Validasi gagal. Periksa input Anda." harus ditampilkan
    Dan admin harus tetap berada di halaman login

  Skenario: 2.3 Admin gagal login dengan format email tidak valid
    Ketika admin mengisi email dengan "admin-tentangdental"
    Dan admin mengisi password dengan "password"
    Dan admin mengklik tombol Login
    Maka field email harus menampilkan indikator error format tidak valid
    Dan admin harus tetap berada di halaman login

  Skenario: 2.4a Admin gagal login karena email kosong
    Ketika admin mengisi email dengan ""
    Dan admin mengisi password dengan "password123"
    Dan admin mengklik tombol Login
    Maka pesan error "Email wajib diisi." harus ditampilkan

  Skenario: 2.4b Admin gagal login karena password kosong
    Ketika admin mengisi email dengan "admin@test.com"
    Dan admin mengisi password dengan ""
    Dan admin mengklik tombol Login
    Maka pesan error "Password wajib diisi." harus ditampilkan

  Skenario: 2.4c Admin gagal login karena email dan password kosong
    Ketika admin mengisi email dengan ""
    Dan admin mengisi password dengan ""
    Dan admin mengklik tombol Login
    Maka pesan error "Email wajib diisi." harus ditampilkan

  Skenario: 2.5 Tombol Login nonaktif selama proses autentikasi berlangsung
    Ketika admin mengisi email dengan "admin@tentangdental.com"
    Dan admin mengisi password dengan "password"
    Dan admin mengklik tombol Login
    Maka tombol Login harus menampilkan status loading
    Dan tombol Login harus nonaktif selama proses autentikasi
