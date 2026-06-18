# Unmatched Locators Report (VERIFIED vs DOM Aktual)

Hasil verifikasi dengan membaca langsung source code React.
✅ = locator BENAR | ❌ = locator SALAH (sudah diperbaiki di kode)

---

## ✅ PromoPage.java — SEMUA VALID

| Locator | Status |
|---|---|
| `[data-testid='promo-image-upload']` | ✅ Ada di `PromoForm.tsx:73` |
| `[data-testid='promo-judul-input']` | ✅ Ada di `PromoForm.tsx:88` |
| `[data-testid='promo-original-price-input']` | ✅ Ada di `PromoForm.tsx:107` |
| `[data-testid='promo-discount-price-input']` | ✅ Ada di `PromoForm.tsx:124` |
| `[data-testid='promo-submit-button']` | ✅ Ada di `PromoForm.tsx:152` |

---

## ❌ LayananPage.java — 2 SALAH (sudah diperbaiki)

| Locator | Status | Perbaikan |
|---|---|---|
| `[data-testid='layanan-nama-input']` | ✅ Ada di `LayananForm.tsx:76` | — |
| `[data-testid='layanan-deskripsi-input']` | ❌ **SALAH** — nama asli `layanan-detail-input` | ✅ Ganti ke `layanan-detail-input` |
| `[data-testid='layanan-harga-input']` | ❌ **SALAH** — form tidak punya field harga | ✅ Method `fillPrice()` dijadikan no-op |
| `[data-testid='layanan-submit-button']` | ✅ Ada di `LayananForm.tsx:147` | — |

---

## ✅ ProfilDokterPage.java — SEMUA VALID

| Locator | Status |
|---|---|
| `[data-testid='dokter-nama-input']` | ✅ Ada di `ProfilDokterForm.tsx:89` |
| `[data-testid='dokter-spesialis-input']` | ✅ Ada di `ProfilDokterForm.tsx:107` |
| `[data-testid='dokter-submit-button']` | ✅ Ada di `ProfilDokterForm.tsx:159` |
| `dokter-card-${index}` | ✅ Pattern di `ProfilDokter.tsx:47` |
| `dokter-name-${index}` | ✅ Pattern di `ProfilDokter.tsx:60` |
| `dokter-specialization-${index}` | ✅ Pattern di `ProfilDokter.tsx:61` |
| `button.bg-red-400` | ✅ Ada di admin route `profil-dokter.tsx` |

---

## ✅ TestimoniPage.java — SEMUA VALID

| Locator | Status |
|---|---|
| `[data-testid='testimoni-nama-input']` | ✅ Ada di `TestimoniForm.tsx:69` |
| `[data-testid='testimoni-foto-upload']` | ✅ Ada di `TestimoniForm.tsx:102` |
| `[data-testid='testimoni-submit-button']` | ✅ Ada di `TestimoniForm.tsx:130` |
| `button.bg-red-400` | ✅ Ada di admin route `testimoni.tsx` |
| `testimoni-star-1` s/d `testimoni-star-5` | ✅ Pattern di `TestimoniForm.tsx:82` |
| `.line-clamp-2` | ✅ Class Tailwind utilitas, tersedia |

---

## ❌ GaleriPage.java — 2 SALAH (sudah diperbaiki)

| Locator | Status | Perbaikan |
|---|---|---|
| `input[type='file'][accept='...']` | ✅ Ada di `galeri.tsx:120` | — |
| `//button[@data-slot='button' ...]` | ❌ **SALAH** — pakai `data-testid='galeri-tambah-button'` | ✅ Ganti ke `[data-testid='galeri-tambah-button']` |
| `.grid.sm\\:grid-cols-2...` | ✅ Ada di `galeri.tsx:151` | — |
| `img[alt^='Galeri']` | ✅ Pattern `Galeri ${item.id}` di `galeri.tsx:156` | — |
| `.group.relative` | ✅ Ada di `galeri.tsx:153` | — |
| `.group-hover\\:opacity-100 button` | ❌ **SALAH** — button pakai `opacity-80 hover:opacity-100` | ✅ Ganti ke `button.opacity-80` |

---

## ❌ ArtikelPage.java — 1 SALAH (sudah diperbaiki)

| Locator | Status | Perbaikan |
|---|---|---|
| `input[type='file'][accept='...']` | ✅ Ada di `artikel.tsx:143` | — |
| `input[placeholder='Masukkan Judul Artikel']` | ✅ Ada di `artikel.tsx:158` | — |
| `.tiptap.ProseMirror` | ✅ Class internal TipTap | — |
| `.flex.flex-wrap.justify-center.gap-6` | ✅ Ada di `artikel.tsx:307` | — |
| `button[data-slot='dialog-close'].size-8` | ❌ **SALAH** — pakai `data-testid='artikel-edit-batal'` | ✅ Ganti ke `[data-testid='artikel-edit-batal']` |
| `button.bg-red-400` | ✅ Ada di `artikel.tsx:352` | — |

---

## Ringkasan Perbaikan

| File | Locator Salah | Sudah Diperbaiki? |
|---|---|---|
| `LayananPage.java` | `layanan-deskripsi-input` → `layanan-detail-input` | ✅ |
| `LayananPage.java` | `layanan-harga-input` → hapus (no-op) | ✅ |
| `GaleriPage.java` | `@data-slot='button'` → `data-testid='galeri-tambah-button'` | ✅ |
| `GaleriPage.java` | `.group-hover\\:opacity-100` → `button.opacity-80` | ✅ |
| `ArtikelPage.java` | `button[data-slot='dialog-close'].size-8` → `[data-testid='artikel-edit-batal']` | ✅ |

---

## Locator yang Lain — SEMUA VALID (false positive sebelumnya)

Semua locator di `PromoPage.java`, `ProfilDokterPage.java`, dan `TestimoniPage.java` **sudah benar sejak awal**.
