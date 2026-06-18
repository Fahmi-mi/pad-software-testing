# language: id
@cms_management
Fitur: Manajemen Konten CMS oleh Admin
  Sebagai seorang admin sistem
  Saya ingin mengelola konten CMS (Promo, Layanan, Profil Dokter, Galeri, Testimoni, dan Artikel)
  Agar informasi klinis pada website Tentang Dental selalu mutakhir

  Latar Belakang:
  Diberikan admin membuka halaman login
    Dan admin melakukan login dengan kredensial yang valid

  # ============================================================
  # 1. PROMO
  # ============================================================
  Skenario: 1.1 Admin menambahkan promo baru
    Ketika admin membuka halaman manajemen Promo
    Dan admin mengunggah gambar promo "promo_scaling.png"
    Dan admin mengisi judul promo "Promo Scaling Gigi Sensasional"
    Dan admin mengisi harga awal promo "500000" dan harga diskon "250000"
    Dan admin mengisi deskripsi promo "Promo pembersihan karang gigi untuk umum"
    Dan admin menyimpan promo baru
    Maka promo baru "Promo Scaling Gigi Sensasional" harus terdaftar di halaman promo

  Skenario: 1.2 Verifikasi promo yang sudah ada melalui dialog
    Ketika admin membuka halaman manajemen Promo
    Dan admin membuka dialog edit promo "Promo Scaling Gigi Sensasional"
    Dan admin menunggu 5 detik
    Maka field judul promo pada dialog harus berisi "Promo Scaling Gigi Sensasional"
    Dan field harga awal pada dialog harus berisi "500000"
    Dan field harga diskon pada dialog harus berisi "250000"

  Skenario: 1.3 Admin mengedit promo melalui dialog
    Ketika admin membuka halaman manajemen Promo
    Dan admin membuka dialog edit promo "Promo Scaling Gigi Sensasional"
    Maka field judul promo pada dialog harus berisi "Promo Scaling Gigi Sensasional"
    Dan field harga awal pada dialog harus berisi "500000"
    Dan field harga diskon pada dialog harus berisi "250000"
    Ketika admin mengubah judul promo menjadi "Promo Scaling Gigi Sensasional - Diperbarui"
    Dan admin menyimpan perubahan promo
    Maka promo "Promo Scaling Gigi Sensasional - Diperbarui" harus terdaftar di halaman promo

  Skenario: 1.4 Admin menghapus promo melalui dialog
    Ketika admin membuka halaman manajemen Promo
    Dan admin membuka dialog edit promo "Promo Scaling Gigi Sensasional - Diperbarui"
    Ketika admin mengklik tombol Hapus Promo
    Maka promo "Promo Scaling Gigi Sensasional - Diperbarui" harus sudah dihapus dari halaman promo

  Skenario: 1.5 Validasi form promo tanpa input
    Ketika admin membuka halaman manajemen Promo
    Dan admin mengklik tombol submit promo tanpa mengisi form
    Maka field promo yang wajib diisi harus menampilkan indikator error

  # ============================================================
  # 2. LAYANAN
  # ============================================================
  Skenario: 2.1 Admin menambahkan layanan baru
    Ketika admin membuka halaman manajemen Layanan
    Dan admin mengisi nama layanan "Tambal Gigi Estetik"
    Dan admin mengisi deskripsi layanan "Perawatan penambalan gigi berlubang menggunakan bahan komposit resin terbaik"
    Dan admin mengisi artikel layanan "Tambal gigi estetik menggunakan bahan komposit resin berkualitas tinggi yang tahan lama dan estetik."
    Dan admin mengunggah gambar pendukung layanan "layanan_tambal.png"
    Dan admin mengunggah icon layanan "icon_tambal.png"
    Dan admin menyimpan layanan baru
    Maka layanan baru "Tambal Gigi Estetik" harus terdaftar di halaman layanan

  Skenario: 2.2 Verifikasi layanan yang sudah ada
    Ketika admin membuka halaman manajemen Layanan
    Maka card layanan "Tambal Gigi Estetik" harus ada di halaman
    Dan deskripsi layanan harus mengandung "bahan komposit resin"
    Dan icon layanan harus memiliki atribut src

  Skenario: 2.3 Admin mengedit layanan melalui dialog
    Ketika admin membuka halaman manajemen Layanan
    Dan admin membuka dialog edit layanan "Tambal Gigi Estetik"
    Dan admin menunggu 5 detik
    Maka field nama layanan pada dialog harus berisi "Tambal Gigi Estetik"
    Dan editor artikel layanan harus mengandung teks "komposit resin"
    Ketika admin mengubah nama layanan menjadi "Tambal Gigi Estetik Premium"
    Dan admin menyimpan perubahan layanan
    Maka layanan "Tambal Gigi Estetik Premium" harus terdaftar di halaman layanan

  Skenario: 2.4 Admin menghapus layanan melalui dialog
    Ketika admin membuka halaman manajemen Layanan
    Dan admin membuka dialog edit layanan "Tambal Gigi Estetik Premium"
    Ketika admin mengklik tombol Hapus Layanan
    Maka layanan "Tambal Gigi Estetik Premium" harus sudah dihapus dari halaman layanan

  Skenario: 2.5 Validasi form layanan tanpa input
    Ketika admin membuka halaman manajemen Layanan
    Dan admin mengklik tombol submit layanan tanpa mengisi form
    Maka field layanan yang wajib diisi harus menampilkan indikator error

  # ============================================================
  # 3. PROFIL DOKTER
  # ============================================================
  Skenario: 3.1 Admin menambahkan dokter baru
    Ketika admin membuka halaman manajemen Profil Dokter
    Dan admin mengisi nama dokter "drg. Budi Santoso, Sp.Ort"
    Dan admin mengisi spesialis dokter "Spesialis Ortodonti"
    Dan admin mengisi pernyataan jadwal dokter "Jadwal Praktek:\nSelasa, Kamis : 09:00 - 15:00"
    Dan admin membuka schedule picker dokter
    Dan admin memilih slot jadwal "Selasa 09:00 - 10:00"
    Dan admin mengunggah foto dokter "foto_dokter.png"
    Dan admin menyimpan profil dokter baru
    Maka profil dokter baru "drg. Budi Santoso, Sp.Ort" harus terdaftar di halaman profil dokter

  Skenario: 3.2 Verifikasi card dokter yang sudah ada
    Ketika admin membuka halaman manajemen Profil Dokter
    Maka card dokter ke-2 harus ada di halaman
    Dan nama dokter ke-2 harus "drg. Budi Santoso, Sp.Ort"
    Dan spesialisasi dokter ke-2 harus "Spesialis Ortodonti"
    Dan pernyataan dokter ke-2 harus mengandung "Selasa, Kamis : 09:00 - 15:00"
    Dan foto dokter ke-2 harus memiliki atribut src

  Skenario: 3.3 Admin mengedit dokter melalui dialog
    Ketika admin membuka halaman manajemen Profil Dokter
    Dan admin membuka dialog edit dokter ke-2
    Maka field nama dokter pada dialog harus berisi "drg. Budi Santoso, Sp.Ort"
    Dan field spesialis dokter pada dialog harus berisi "Spesialis Ortodonti"
    Dan tag jadwal "Selasa 09:00 - 10:00" harus terlihat pada dialog dokter
    Dan 1 slot jadwal harus tampil pada dialog dokter
    Dan editor dokter harus mengandung teks "Jadwal Praktek:"
    Ketika admin menyimpan perubahan dokter

  Skenario: 3.4 Admin menghapus dokter melalui dialog
    Ketika admin membuka halaman manajemen Profil Dokter
    Dan admin membuka dialog edit dokter ke-2
    Ketika admin mengklik tombol Hapus Dokter
    Maka dokter "drg. Budi Santoso, Sp.Ort" harus sudah dihapus dari grid

  Skenario: 3.5 Validasi form dokter tanpa input
    Ketika admin membuka halaman manajemen Profil Dokter
    Dan admin mengklik tombol submit dokter tanpa mengisi form
    Maka field dokter yang wajib diisi harus menampilkan indikator error

  # ============================================================
  # 4. GALERI
  # ============================================================
  Skenario: 4.1 Admin mengunggah gambar galeri baru
    Ketika admin membuka halaman manajemen Galeri
    Dan admin mengunggah foto galeri "klinik_dental.png"
    Dan admin mengklik tombol Tambahkan Gambar
    Maka gambar baru harus muncul di grid galeri

  Skenario: 4.2 Verifikasi gambar galeri yang sudah ada
    Ketika admin membuka halaman manajemen Galeri
    Maka gambar galeri harus tampil di halaman
    Dan 1 gambar harus ada di grid galeri
    Dan setiap gambar galeri harus memiliki tombol hapus

  Skenario: 4.3 Admin menghapus gambar galeri pertama
    Ketika admin membuka halaman manajemen Galeri
    Dan admin hover pada item galeri pertama
    Dan admin mengklik tombol hapus pada item galeri pertama
    Maka gambar tersebut harus sudah dihapus dari grid

  Skenario: 4.4 Validasi unggah galeri tanpa file
    Ketika admin membuka halaman manajemen Galeri
    Dan admin mengklik tombol Tambahkan Gambar tanpa memilih file
    Maka indikator error harus ditampilkan untuk input file galeri

  # ============================================================
  # 5. TESTIMONI
  # ============================================================
  Skenario: 5.1 Admin menambahkan testimoni baru
    Ketika admin membuka halaman manajemen Testimoni
    Dan admin mengisi nama pasien "Budi Prasetyo"
    Dan admin memilih rating 5 bintang
    Dan admin mengunggah foto testimoni "foto_pasien.png"
    Dan admin mengisi teks testimoni "Pelayanan sangat memuaskan dan profesional"
    Dan admin menyimpan testimoni baru
    Maka testimoni baru dari "Budi Prasetyo" harus terdaftar di halaman testimoni

  Skenario: 5.2 Verifikasi testimoni yang sudah ada
    Ketika admin membuka halaman manajemen Testimoni
    Maka foto "Budi Prasetyo" harus terlihat di daftar testimoni
    Dan 5 bintang kuning harus tampil
    Dan teks testimoni harus terlihat di elemen line-clamp

  Skenario: 5.3 Admin mengedit testimoni melalui dialog
    Ketika admin membuka halaman manajemen Testimoni
    Dan admin membuka dialog edit testimoni "Budi Prasetyo"
    Maka field nama testimoni pada dialog harus berisi "Budi Prasetyo"
    Dan semua 5 bintang harus berwarna kuning pada dialog
    Dan editor testimoni harus mengandung teks "Pelayanan sangat memuaskan dan profesional"
    Ketika admin mengubah teks testimoni menjadi "Dokter sangat ramah dan profesional - updated"
    Dan admin menyimpan perubahan testimoni

  Skenario: 5.4 Admin menghapus testimoni melalui dialog
    Ketika admin membuka halaman manajemen Testimoni
    Dan admin membuka dialog edit testimoni "Budi Prasetyo"
    Ketika admin mengklik tombol Hapus Testimoni
    Maka testimoni "Budi Prasetyo" harus sudah dihapus dari daftar

  Skenario: 5.5 Validasi form testimoni tanpa input
    Ketika admin membuka halaman manajemen Testimoni
    Dan admin mengklik tombol submit testimoni tanpa mengisi form
    Maka field testimoni yang wajib diisi harus menampilkan indikator error

  # ============================================================
  # 6. ARTIKEL
  # ============================================================
  Skenario: 6.1 Admin menambahkan artikel baru
    Ketika admin membuka halaman manajemen Artikel
    Dan admin mengunggah gambar artikel "artikel_gigi.png"
    Dan admin mengisi judul artikel "Tips Menjaga Kesehatan Gigi Sehari-hari"
    Dan admin mengisi konten artikel "Menjaga kebersihan gigi dapat mencegah penumpukan karang gigi dan penyakit gusi..."
    Dan admin menyimpan artikel baru
    Maka artikel baru dengan judul "Tips Menjaga Kesehatan Gigi Sehari-hari" harus terdaftar di halaman artikel

  Skenario: 6.2 Verifikasi artikel yang sudah ada
    Ketika admin membuka halaman manajemen Artikel
    Maka artikel "Anak 1 Tahun Boleh Sikat Gigi? Ini Panduan Lengkapnya" harus ada di halaman
    Dan penulis artikel "Admin Registration" harus ditampilkan
    Dan 3 artikel harus ada di grid

  Skenario: 6.3 Admin mengedit artikel melalui dialog
    Ketika admin membuka halaman manajemen Artikel
    Dan admin membuka dialog edit artikel "Anak 1 Tahun Boleh Sikat Gigi? Ini Panduan Lengkapnya"
    Maka field judul artikel pada dialog harus berisi "Anak 1 Tahun Boleh Sikat Gigi? Ini Panduan Lengkapnya"
    Dan editor artikel harus mengandung konten artikel
    Dan toolbar rich text editor artikel harus berfungsi
    Ketika admin menyimpan perubahan artikel

  Skenario: 6.4 Admin menghapus artikel melalui dialog
    Ketika admin membuka halaman manajemen Artikel
    Dan admin membuka dialog edit artikel "Anak 1 Tahun Boleh Sikat Gigi? Ini Panduan Lengkapnya"
    Ketika admin mengklik tombol Hapus Artikel
    Maka artikel "Anak 1 Tahun Boleh Sikat Gigi? Ini Panduan Lengkapnya" harus sudah dihapus dari daftar

  Skenario: 6.5 Validasi form artikel tanpa input
    Ketika admin membuka halaman manajemen Artikel
    Dan admin mengklik tombol submit artikel tanpa mengisi form
    Maka field artikel yang wajib diisi harus menampilkan indikator error
