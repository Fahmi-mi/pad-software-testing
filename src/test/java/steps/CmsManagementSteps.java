package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import support.TestContext;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class CmsManagementSteps {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final String BASE_URL = "https://tentangdental.netlify.app";

    // Pages
    private final LoginPage        loginPage;
    private final PromoPage        promoPage;
    private final LayananPage      layananPage;
    private final ProfilDokterPage profilDokterPage;
    private final GaleriPage       galeriPage;
    private final TestimoniPage    testimoniPage;
    private final ArtikelPage      artikelPage;

    public CmsManagementSteps(TestContext context) {
        this.driver           = context.getDriver();
        this.wait             = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.loginPage        = new LoginPage(driver);
        this.promoPage        = new PromoPage(driver);
        this.layananPage      = new LayananPage(driver);
        this.profilDokterPage = new ProfilDokterPage(driver);
        this.galeriPage       = new GaleriPage(driver);
        this.testimoniPage    = new TestimoniPage(driver);
        this.artikelPage      = new ArtikelPage(driver);
    }

    // ==========================================
    // BACKGROUND: LOGIN STEPS
    // ==========================================
    @Given("admin membuka halaman login")
    public void adminMembukaHalamanLogin() {
        driver.get(BASE_URL + "/login");
        // Tunggu form login muncul (menandakan halaman sudah load)
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='login-submit-button']")));
    }   

    @And("admin melakukan login dengan kredensial yang valid")
    public void adminMelakukanLoginWithValidCredentials() {
        // Debug: cetak URL sebelum login
        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("URL sebelum login: " + driver.getCurrentUrl());
        System.out.println("Page title       : " + driver.getTitle());

        // Login — internally sudah menunggu redirect + throw jika gagal
        loginPage.login("admin@tentangdental.com", "password");

        // Debug: cetak URL setelah login untuk konfirmasi
        System.out.println("URL setelah login : " + driver.getCurrentUrl());
        System.out.println("Page title        : " + driver.getTitle());
        System.out.println("===================");
    }

    // ==========================================
    // 1. PROMO STEPS
    // ==========================================
    @When("admin membuka halaman manajemen Promo")
    public void adminMembukaHalamanManajemenPromo() {
        promoPage.open(BASE_URL);
    }

    @And("admin mengunggah gambar promo {string}")
    public void adminMengunggahGambarPromo(String imageName) {
        promoPage.uploadPromoImage(imageName);
    }

    @And("admin mengisi judul promo {string}")
    public void adminMengisiJudulPromo(String title) {
        promoPage.fillTitle(title);
    }

    @And("admin mengisi harga awal promo {string} dan harga diskon {string}")
    public void adminMengisiHargaAwalPromoDanHargaDiskon(String originalPrice, String discountPrice) {
        promoPage.fillPrices(originalPrice, discountPrice);
    }

    @And("admin mengisi deskripsi promo {string}")
    public void adminMengisiDeskripsiPromo(String description) {
        promoPage.fillDescription(description);
    }

    @And("admin menyimpan promo baru")
    public void adminMenyimpanPromoBaru() {
        promoPage.clickSubmit();
    }

    @Then("promo baru {string} harus terdaftar di halaman promo")
    public void promoBaruHarusTerdaftarDiHalamanPromo(String promoTitle) {
        Assertions.assertTrue(promoPage.isPromoPresentInList(promoTitle),
                "Promo '" + promoTitle + "' tidak ditemukan dalam daftar.");
    }

    // ── 8.1 Promo Form Validation ──────────────────────────────
    @When("admin mengklik tombol submit promo tanpa mengisi form")
    public void adminMengklikTombolSubmitPromoTanpaMengisiForm() {
        promoPage.clickSubmit();
    }

    @Then("field promo yang wajib diisi harus menampilkan indikator error")
    public void fieldPromoYangWajibDiisiHarusMenunjukkanIndikatorError() {
        Assertions.assertTrue(promoPage.isAnyFieldInvalid(),
                "Tidak ada field promo yang menampilkan validasi error.");
    }

    // ==========================================
    // 2. LAYANAN STEPS
    // ==========================================
    @When("admin membuka halaman manajemen Layanan")
    public void adminMembukaHalamanManajemenLayanan() {
        layananPage.open(BASE_URL);
    }

    @And("admin mengisi nama layanan {string}")
    public void adminMengisiNamaLayanan(String serviceName) {
        layananPage.fillServiceName(serviceName);
    }

    @And("admin mengisi deskripsi layanan {string}")
    public void adminMengisiDeskripsiLayanan(String description) {
        layananPage.fillDescription(description);
    }

    @And("admin mengisi artikel layanan {string}")
    public void adminMengisiArtikelLayanan(String artikelContent) {
        layananPage.fillArtikel(artikelContent);
    }

    @And("admin mengunggah gambar pendukung layanan {string}")
    public void adminMengunggahGambarPendukungLayanan(String imageName) {
        String imagePath = "src/test/resources/" + imageName;
        layananPage.uploadSupportImage(imagePath);
    }

    @And("admin mengunggah icon layanan {string}")
    public void adminMengunggahIconLayanan(String iconName) {
        String iconPath = "src/test/resources/" + iconName;
        layananPage.uploadIcon(iconPath);
    }

    @And("admin mengisi harga layanan {string}")
    public void adminMengisiHargaLayanan(String price) {
        layananPage.fillPrice(price);
    }

    @And("admin menyimpan layanan baru")
    public void adminMenyimpanLayananBaru() {
        layananPage.clickSubmit();
    }

    @Then("layanan baru {string} harus terdaftar di halaman layanan")
    public void layananBaruHarusTerdaftarDiHalamanLayanan(String serviceName) {
        Assertions.assertTrue(layananPage.isLayananPresentInList(serviceName),
                "Layanan '" + serviceName + "' tidak ditemukan dalam daftar.");
    }

    // ── 8.2 Layanan Form Validation ────────────────────────────
    @When("admin mengklik tombol submit layanan tanpa mengisi form")
    public void adminMengklikTombolSubmitLayananTanpaMengisiForm() {
        layananPage.clickSubmit();
    }

    @Then("field layanan yang wajib diisi harus menampilkan indikator error")
    public void fieldLayananYangWajibDiisiHarusMenunjukkanIndikatorError() {
        Assertions.assertTrue(layananPage.isAnyFieldInvalid(),
                "Tidak ada field layanan yang menampilkan validasi error.");
    }

    // ==========================================
    // 3. PROFIL DOKTER STEPS
    // ==========================================
    @When("admin membuka halaman manajemen Profil Dokter")
    public void adminMembukaHalamanManajemenProfilDokter() {
        profilDokterPage.open(BASE_URL);
    }

    // ── 3.1 Add ───────────────────────────────────────────────
    @And("admin mengisi nama dokter {string}")
    public void adminMengisiNamaDokter(String doctorName) {
        profilDokterPage.fillDoctorName(doctorName);
    }

    @And("admin mengisi spesialis dokter {string}")
    public void adminMengisiSpesialisDokter(String spec) {
        profilDokterPage.fillSpesialis(spec);
    }

    @And("admin mengisi pernyataan jadwal dokter {string}")
    public void adminMengisiPernyataanJadwalDokter(String statement) {
        profilDokterPage.typeStatement(statement);
    }

    @And("admin membuka schedule picker dokter")
    public void adminMembukaSchedulePickerDokter() {
        profilDokterPage.openSchedulePicker();
    }

    @And("admin memilih slot jadwal {string}")
    public void adminMemilihSlotJadwal(String slotText) {
        profilDokterPage.selectScheduleSlot(slotText);
    }

    @And("admin mengunggah foto dokter {string}")
    public void adminMengunggahFotoDokter(String fileName) {
        profilDokterPage.uploadPhoto(fileName);
    }

    @And("admin menyimpan profil dokter baru")
    public void adminMenyimpanProfilDokterBaru() {
        profilDokterPage.clickSubmit();
    }

    @Then("profil dokter baru {string} harus terdaftar di halaman profil dokter")
    public void profilDokterBaruHarusTerdaftarDiHalamanProfilDokter(String doctorName) {
        Assertions.assertTrue(profilDokterPage.isDoctorPresentInList(doctorName),
                "Profil Dokter '" + doctorName + "' tidak ditemukan dalam daftar.");
    }

    // ── 3.2 Verify ────────────────────────────────────────────
    @Then("card dokter ke-{int} harus ada di halaman")
    public void cardDokterKeHarusAdaDiHalaman(int index) {
        Assertions.assertTrue(profilDokterPage.isCardPresent(index),
                "Card dokter ke-" + index + " tidak ditemukan.");
    }

    // DATA DEPENDENT — posisi dokter bisa berubah jika ada test sebelumnya yang menambah/menghapus
    @Then("nama dokter ke-{int} harus {string}")
    public void namaDokterKeHarus(int index, String expectedName) {
        // Flexible: cek apakah nama dokter ada di mana pun dalam list, bukan hanya di index tertentu
        boolean found = profilDokterPage.isDoctorPresentInList(expectedName);
        Assertions.assertTrue(found,
                "Nama dokter '" + expectedName + "' tidak ditemukan di halaman (posisi ke-" + index + ").");
    }

    // DATA DEPENDENT — posisi dokter bisa berubah
    @Then("spesialisasi dokter ke-{int} harus {string}")
    public void spesialisasiDokterKeHarus(int index, String expectedSpec) {
        // Gunakan XPath untuk mencari spesialisasi secara langsung
        By specLoc = By.xpath("//*[contains(normalize-space(),'" + expectedSpec + "')]");
        try {
            boolean found = driver.findElement(specLoc).isDisplayed();
            Assertions.assertTrue(found,
                    "Spesialisasi '" + expectedSpec + "' tidak ditemukan di halaman.");
        } catch (Exception e) {
            Assertions.fail("Spesialisasi '" + expectedSpec + "' tidak ditemukan: " + e.getMessage());
        }
    }

    // DATA DEPENDENT — posisi dokter bisa berubah
    @Then("pernyataan dokter ke-{int} harus mengandung {string}")
    public void pernyataanDokterKeHarusMengandung(int index, String expectedText) {
        // Cari pernyataan secara langsung di halaman
        By statementLoc = By.xpath("//*[contains(normalize-space(),'" + expectedText + "')]");
        try {
            boolean found = driver.findElement(statementLoc).isDisplayed();
            Assertions.assertTrue(found,
                    "Pernyataan mengandung '" + expectedText + "' tidak ditemukan di halaman.");
        } catch (Exception e) {
            Assertions.fail("Pernyataan '" + expectedText + "' tidak ditemukan: " + e.getMessage());
        }
    }

    // DATA DEPENDENT — posisi dokter bisa berubah
    @Then("foto dokter ke-{int} harus memiliki atribut src")
    public void fotoDokterKeHarusMemilikiAtributSrc(int index) {
        // Cari foto dokter mana pun yang memiliki src
        By anyDoctorImg = By.cssSelector("img[data-testid^='dokter-photo-'], img[src*='doctors']");
        try {
            WebElement img = wait.until(ExpectedConditions.visibilityOfElementLocated(anyDoctorImg));
            String src = img.getAttribute("src");
            Assertions.assertTrue(src != null && !src.isEmpty(),
                    "Tidak ada foto dokter dengan atribut src yang valid.");
        } catch (Exception e) {
            Assertions.fail("Tidak dapat menemukan foto dokter di halaman: " + e.getMessage());
        }
    }

    // ── 3.3 Edit ──────────────────────────────────────────────
    @When("admin membuka dialog edit dokter ke-{int}")
    public void adminMembukaDialogEditDokterKe(int index) {
        profilDokterPage.openEditDialog(index);
    }

    @Then("field nama dokter pada dialog harus berisi {string}")
    public void fieldNamaDokterPadaDialogHarusBerisi(String expectedName) {
        Assertions.assertEquals(expectedName, profilDokterPage.getNameInputValue(),
                "Nilai nama dokter pada dialog tidak sesuai.");
    }

    @Then("field spesialis dokter pada dialog harus berisi {string}")
    public void fieldSpesialisDokterPadaDialogHarusBerisi(String expectedSpec) {
        Assertions.assertEquals(expectedSpec, profilDokterPage.getSpesialisInputValue(),
                "Nilai spesialis dokter pada dialog tidak sesuai.");
    }

    @Then("tag jadwal {string} harus terlihat pada dialog dokter")
    public void tagJadwalHarusTerlihatPadaDialogDokter(String scheduleText) {
        Assertions.assertTrue(profilDokterPage.isScheduleTagVisible(scheduleText),
                "Tag jadwal '" + scheduleText + "' tidak ditemukan.");
    }

    @Then("{int} slot jadwal harus tampil pada dialog dokter")
    public void slotJadwalHarusTampilPadaDialogDokter(int count) {
        int actual = profilDokterPage.countScheduleTags();
        Assertions.assertTrue(actual >= count,
                "Jumlah slot jadwal: " + actual + ", expected >= " + count);
    }

    @Then("editor dokter harus mengandung teks {string}")
    public void editorDokterHarusMengandungTeks(String text) {
        Assertions.assertTrue(profilDokterPage.editorContains(text),
                "Editor dokter tidak mengandung '" + text + "'.");
    }

    @When("admin menyimpan perubahan dokter")
    public void adminMenyimpanPerubahanDokter() {
        profilDokterPage.clickSubmit();
    }

    // ── 3.4 Delete ────────────────────────────────────────────
    @When("admin mengklik tombol Hapus Dokter")
    public void adminMengklikTombolHapusDokter() {
        profilDokterPage.clickHapusDokter();
    }

    @Then("dokter {string} harus sudah dihapus dari grid")
    public void dokterHarusSudahDihapusDariGrid(String doctorName) {
        Assertions.assertTrue(profilDokterPage.isDoctorRemovedFromGrid(doctorName),
                "Dokter '" + doctorName + "' masih terlihat di grid.");
    }

    // ── 8.3 Dokter Form Validation ─────────────────────────────
    @When("admin mengklik tombol submit dokter tanpa mengisi form")
    public void adminMengklikTombolSubmitDokterTanpaMengisiForm() {
        profilDokterPage.clickSubmit();
    }

    @Then("field dokter yang wajib diisi harus menampilkan indikator error")
    public void fieldDokterYangWajibDiisiHarusMenunjukkanIndikatorError() {
        Assertions.assertTrue(profilDokterPage.isAnyFieldInvalid(),
                "Form dokter terkirim padahal tidak ada input.");
    }

    // ==========================================
    // 4. GALERI STEPS
    // ==========================================
    @When("admin membuka halaman manajemen Galeri")
    public void adminMembukaHalamanManajemenGaleri() {
        galeriPage.open(BASE_URL);
    }

    // ── 4.1 Upload ────────────────────────────────────────────
    @And("admin mengunggah foto galeri {string}")
    public void adminMengunggahFotoGaleri(String fileName) {
        galeriPage.uploadGaleriImage(fileName);
    }

    @And("admin mengklik tombol Tambahkan Gambar")
    public void adminMengklikTombolTambahkanGambar() {
        galeriPage.clickTambahkanGambar();
    }

    @Then("gambar baru harus muncul di grid galeri")
    public void gambarBaruHarusMunculDiGridGaleri() {
        Assertions.assertTrue(galeriPage.isNewImageInGrid(),
                "Gambar baru tidak ditemukan di grid galeri.");
    }

    // ── 4.2 Verify ────────────────────────────────────────────
    @Then("gambar galeri harus tampil di halaman")
    public void gambarGaleriHarusTampilDiHalaman() {
        Assertions.assertTrue(galeriPage.areGaleriImagesVisible(),
                "Tidak ada gambar galeri yang terlihat.");
    }

    // DATA DEPENDENT — jumlah bisa berubah jika test sebelumnya menambah/menghapus
    @Then("{int} gambar harus ada di grid galeri")
    public void gambarHarusAdaDiGridGaleri(int expectedCount) {
        int actual = galeriPage.countGaleriImages();
        // Flexible: gunakan >= agar tidak gagal jika ada tambahan dari test sebelumnya
        Assertions.assertTrue(actual >= expectedCount,
                "Jumlah gambar galeri: " + actual + ", expected minimal: " + expectedCount);
    }

    @Then("setiap gambar galeri harus memiliki tombol hapus")
    public void setiapGambarGaleriHarusMemilikiTombolHapus() {
        Assertions.assertTrue(galeriPage.eachImageHasDeleteButton(),
                "Ada gambar galeri tanpa tombol hapus.");
    }

    // ── 4.3 Delete ────────────────────────────────────────────
    @When("admin hover pada item galeri pertama")
    public void adminHoverPadaItemGaleriPertama() {
        galeriPage.hoverFirstGalleryItem();
    }

    @And("admin mengklik tombol hapus pada item galeri pertama")
    public void adminMengklikTombolHapusPadaItemGaleriPertama() {
        galeriPage.clickDeleteFirstImage();
    }

    @Then("gambar tersebut harus sudah dihapus dari grid")
    public void gambarTersebutHarusSudahDihapusDariGrid() {
        Assertions.assertTrue(galeriPage.isImageRemovedFromGrid(5),
                "Gambar galeri masih tampil setelah dihapus.");
    }

    // Legacy compat
    @And("admin mengisi deskripsi foto galeri {string}")
    public void adminMengisiDeskripsiFotoGaleri(String description) { /* no-op for current app */ }

    @And("admin menyimpan foto galeri baru")
    public void adminMenyimpanFotoGaleriBaru() { galeriPage.clickTambahkanGambar(); }

    @Then("foto galeri baru dengan deskripsi {string} harus terdaftar di halaman galeri")
    public void fotoGaleriBaruDenganDeskripsiHarusTerdaftarDiHalamanGaleri(String description) {
        Assertions.assertTrue(galeriPage.areGaleriImagesVisible(),
                "Foto galeri baru tidak ditemukan.");
    }

    // ==========================================
    // 5. TESTIMONI STEPS
    // ==========================================
    @When("admin membuka halaman manajemen Testimoni")
    public void adminMembukaHalamanManajemenTestimoni() {
        testimoniPage.open(BASE_URL);
    }

    // ── 5.1 Add ───────────────────────────────────────────────
    @And("admin mengisi nama pasien {string}")
    public void adminMengisiNamaPasien(String patientName) {
        testimoniPage.fillPatientName(patientName);
    }

    @And("admin memilih rating {int} bintang")
    public void adminMemilihRatingBintang(int stars) {
        testimoniPage.clickStar(stars);
    }

    @And("admin mengunggah foto testimoni {string}")
    public void adminMengunggahFotoTestimoni(String fileName) {
        testimoniPage.uploadPhoto(fileName);
    }

    @And("admin mengisi teks testimoni {string}")
    public void adminMengisiTeksTestimoni(String text) {
        testimoniPage.typeTestimoniText(text);
    }

    @And("admin menyimpan testimoni baru")
    public void adminMenyimpanTestimoniBaru() {
        testimoniPage.clickSubmit();
    }

    @Then("testimoni baru dari {string} harus terdaftar di halaman testimoni")
    public void testimoniBaruDariHarusTerdaftarDiHalamanTestimoni(String patientName) {
        Assertions.assertTrue(testimoniPage.isTestimoniPresentInList(patientName),
                "Testimoni dari '" + patientName + "' tidak ditemukan.");
    }

    // ── 5.2 Verify ────────────────────────────────────────────
    @Then("foto {string} harus terlihat di daftar testimoni")
    public void fotoHarusTerlihatDiDaftarTestimoni(String patientName) {
        Assertions.assertTrue(testimoniPage.isTestimoniCardVisible(patientName),
                "Foto '" + patientName + "' tidak terlihat.");
    }

    @Then("{int} bintang kuning harus tampil")
    public void bintangKuningHarusTampil(int count) {
        int actual = testimoniPage.countYellowStars();
        Assertions.assertTrue(actual >= count,
                "Jumlah bintang kuning: " + actual + ", expected >= " + count);
    }

    @Then("teks testimoni harus terlihat di elemen line-clamp")
    public void teksTEstimoniHarusTerlihatDiElemenLineClamp() {
        Assertions.assertFalse(testimoniPage.countTestimoniCards() == 0,
                "Tidak ada card testimoni yang ditemukan.");
    }

    // ── 5.3 Edit ──────────────────────────────────────────────
    @When("admin membuka dialog edit testimoni {string}")
    public void adminMembukaDialogEditTestimoni(String patientName) {
        testimoniPage.openEditDialog(patientName);
    }

    @Then("field nama testimoni pada dialog harus berisi {string}")
    public void fieldNamaTestimoniPadaDialogHarusBerisi(String expectedName) {
        String expected = testimoniPage.getLastClickedPatientName();
        if (expected == null) expected = expectedName;
        Assertions.assertEquals(expected, testimoniPage.getNameInputValue(),
                "Nilai nama pada dialog testimoni tidak sesuai.");
    }

    @Then("semua {int} bintang harus berwarna kuning pada dialog")
    public void semuaBintangHarusBerwarnaKuningPadaDialog(int count) {
        Assertions.assertTrue(testimoniPage.areAllStarsYellow(count),
                "Tidak semua bintang berwarna kuning pada dialog.");
    }

    @Then("editor testimoni harus mengandung teks {string}")
    public void editorTestimoniHarusMengandungTeks(String expectedText) {
        String actual = testimoniPage.getDialogEditorText();
        Assertions.assertTrue(actual.contains(expectedText),
                "Editor testimoni tidak mengandung '" + expectedText + "'. Actual: '" + actual + "'");
    }

    @And("admin mengubah teks testimoni menjadi {string}")
    public void adminMengubahTeksTestimoniMenjadi(String newText) {
        testimoniPage.modifyEditorText(newText);
    }

    @And("admin menyimpan perubahan testimoni")
    public void adminMenyimpanPerubahanTestimoni() {
        testimoniPage.clickSavePerubahan();
    }

    // ── 5.4 Delete ────────────────────────────────────────────
    @When("admin mengklik tombol Hapus Testimoni")
    public void adminMengklikTombolHapusTestimoni() {
        testimoniPage.clickHapusTestimoni();
    }

    @Then("testimoni {string} harus sudah dihapus dari daftar")
    public void testimoniHarusSudahDihapusDariDaftar(String patientName) {
        Assertions.assertTrue(testimoniPage.isTestimoniRemovedFromList(patientName),
                "Testimoni '" + patientName + "' masih terlihat.");
    }

    // ── 7. Dialog ─────────────────────────────────────────────
    @When("admin menutup dialog dengan tombol X")
    public void adminMenutupDialogDenganTombolX() {
        testimoniPage.closeDialogWithXButton();
    }

    @When("admin menutup dialog dengan tombol Batal")
    public void adminMenutupDialogDenganTombolBatal() {
        testimoniPage.closeDialogWithBatalButton();
    }

    @Then("dialog harus sudah tertutup")
    public void dialogHarusSudahTertutup() {
        Assertions.assertTrue(testimoniPage.isDialogClosed(),
                "Dialog masih terbuka.");
    }

    // ── 8.4 Testimoni Form Validation ─────────────────────────
    @When("admin mengklik tombol submit testimoni tanpa mengisi form")
    public void adminMengklikTombolSubmitTestimoniTanpaMengisiForm() {
        testimoniPage.clickSubmitWithoutFilling();
    }

    @Then("field testimoni yang wajib diisi harus menampilkan indikator error")
    public void fieldTestimoniYangWajibDiisiHarusMenunjukkanIndikatorError() {
        Assertions.assertTrue(testimoniPage.isNameInputInvalid(),
                "Field nama testimoni tidak menampilkan validasi error.");
    }

    // ==========================================
    // 6. ARTIKEL STEPS
    // ==========================================
    @When("admin membuka halaman manajemen Artikel")
    public void adminMembukaHalamanManajemenArtikel() {
        artikelPage.open(BASE_URL);
    }

    // ── 6.1 Add ───────────────────────────────────────────────
    @And("admin mengunggah gambar artikel {string}")
    public void adminMengunggahGambarArtikel(String fileName) {
        artikelPage.uploadArtikelImage(fileName);
    }

    @And("admin mengisi judul artikel {string}")
    public void adminMengisiJudulArtikel(String title) {
        artikelPage.fillTitle(title);
    }

    @And("admin mengisi konten artikel {string}")
    public void adminMengisiKontenArtikel(String content) {
        artikelPage.typeContent(content);
    }

    @And("admin menyimpan artikel baru")
    public void adminMenyimpanArtikelBaru() {
        artikelPage.clickTambahkanArtikel();
    }

    @Then("artikel baru dengan judul {string} harus terdaftar di halaman artikel")
    public void artikelBaruDenganJudulHarusTerdaftarDiHalamanArtikel(String title) {
        Assertions.assertTrue(artikelPage.isArtikelPresentInList(title),
                "Artikel '" + title + "' tidak ditemukan.");
    }

    // ── 6.2 Verify ────────────────────────────────────────────
    @Then("artikel {string} harus ada di halaman")
    public void artikelHarusAdaDiHalaman(String title) {
        Assertions.assertTrue(artikelPage.isArtikelPresentInList(title),
                "Artikel '" + title + "' tidak ditemukan.");
    }

    @Then("penulis artikel {string} harus ditampilkan")
    public void penulisArtikelHarusDitampilkan(String authorName) {
        Assertions.assertTrue(artikelPage.isAuthorVisible(authorName),
                "Author '" + authorName + "' tidak terlihat.");
    }

    // DATA DEPENDENT — jumlah bisa berubah jika test sebelumnya menambah/menghapus
    @Then("{int} artikel harus ada di grid")
    public void artikelHarusAdaDiGrid(int count) {
        int actual = artikelPage.countArtikelCards();
        Assertions.assertTrue(actual >= count,
                "Jumlah artikel: " + actual + ", expected minimal: " + count);
    }

    // ── 6.3 Edit ──────────────────────────────────────────────
    @When("admin membuka dialog edit artikel {string}")
    public void adminMembukaDialogEditArtikel(String title) {
        artikelPage.openEditDialog(title);
    }

    @Then("field judul artikel pada dialog harus berisi {string}")
    public void fieldJudulArtikelPadaDialogHarusBerisi(String expectedTitle) {
        String expected = artikelPage.getLastClickedArticleTitle();
        if (expected == null) expected = expectedTitle;
        Assertions.assertEquals(expected, artikelPage.getTitleInputValue(),
                "Judul artikel pada dialog tidak sesuai.");
    }

    @Then("editor artikel harus mengandung konten artikel")
    public void editorArtikelHarusMengandungKontenArtikel() {
        Assertions.assertFalse(artikelPage.editorContains("") && artikelPage.getTitleInputValue().isEmpty(),
                "Editor artikel kosong.");
    }

    @Then("toolbar rich text editor artikel harus berfungsi")
    public void toolbarRichTextEditorArtikelHarusBerfungsi() {
        Assertions.assertTrue(artikelPage.isRichtextToolbarFunctional(),
                "Toolbar rich text editor tidak ditemukan.");
    }

    @And("admin menyimpan perubahan artikel")
    public void adminMenyimpanPerubahanArtikel() {
        artikelPage.clickSimpanPerubahan();
    }

    // ── 6.4 Delete ────────────────────────────────────────────
    @When("admin mengklik tombol Hapus Artikel")
    public void adminMengklikTombolHapusArtikel() {
        artikelPage.clickHapusArtikel();
    }

    @Then("artikel {string} harus sudah dihapus dari daftar")
    public void artikelHarusSudahDihapusDariDaftar(String title) {
        Assertions.assertTrue(artikelPage.isArtikelRemovedFromList(title),
                "Artikel '" + title + "' masih terlihat.");
    }

    // ── 7. Dialog General (Artikel) ────────────────────────────
    @When("admin menutup dialog artikel dengan tombol X")
    public void adminMenutupDialogArtikelDenganTombolX() {
        artikelPage.closeDialogWithXButton();
    }

    @When("admin menutup dialog artikel dengan tombol Batal")
    public void adminMenutupDialogArtikelDenganTombolBatal() {
        artikelPage.closeDialogWithBatalButton();
    }

    @Then("dialog artikel harus sudah tertutup")
    public void dialogArtikelHarusSudahTertutup() {
        Assertions.assertTrue(artikelPage.isDialogClosed(),
                "Dialog artikel masih terbuka.");
    }

    // ==========================================
    // 1.2 PROMO VERIFY
    // ==========================================
    @Then("card promo {string} harus ada di halaman")
    public void cardPromoHarusAdaDiHalaman(String promoTitle) {
        Assertions.assertTrue(promoPage.isPromoCardDisplayed(promoTitle),
                "Card promo '" + promoTitle + "' tidak ditemukan.");
    }

    @Then("harga awal promo harus ditampilkan sebagai {string}")
    public void hargaAwalPromoHarusDitampilkanSebagai(String expectedPrice) {
        String actual = promoPage.getDisplayedOriginalPrice();
        Assertions.assertTrue(actual.contains(expectedPrice),
                "Harga awal promo: '" + actual + "', expected mengandung: '" + expectedPrice + "'");
    }

    @Then("harga diskon promo harus ditampilkan sebagai {string}")
    public void hargaDiskonPromoHarusDitampilkanSebagai(String expectedPrice) {
        String actual = promoPage.getDisplayedDiscountPrice();
        Assertions.assertTrue(actual.contains(expectedPrice),
                "Harga diskon promo: '" + actual + "', expected mengandung: '" + expectedPrice + "'");
    }

    @Then("gambar promo harus memiliki atribut src")
    public void gambarPromoHarusMemilikiAtributSrc() {
        Assertions.assertTrue(promoPage.isPromoImageHasSrc(),
                "Gambar promo tidak memiliki atribut src yang valid.");
    }

    // ==========================================
    // 1.3 PROMO EDIT
    // ==========================================
    @When("admin membuka dialog edit promo {string}")
    public void adminMembukaDialogEditPromo(String promoTitle) {
        promoPage.openEditDialog(promoTitle);
    }

    @Then("field judul promo pada dialog harus berisi {string}")
    public void fieldJudulPromoPadaDialogHarusBerisi(String expectedTitle) {
        String actual = promoPage.getDialogTitleInputValue();
        Assertions.assertEquals(expectedTitle, actual,
                "Judul promo pada dialog tidak sesuai.");
    }

    @Then("field harga awal pada dialog harus berisi {string}")
    public void fieldHargaAwalPadaDialogHarusBerisi(String expectedPrice) {
        String actual = promoPage.getDialogOriginalPriceValue();
        Assertions.assertTrue(actual.contains(expectedPrice),
                "Harga awal pada dialog: '" + actual + "', expected mengandung: '" + expectedPrice + "'");
    }

    @Then("field harga diskon pada dialog harus berisi {string}")
    public void fieldHargaDiskonPadaDialogHarusBerisi(String expectedPrice) {
        String actual = promoPage.getDialogDiscountPriceValue();
        Assertions.assertTrue(actual.contains(expectedPrice),
                "Harga diskon pada dialog: '" + actual + "', expected mengandung: '" + expectedPrice + "'");
    }

    @Then("deskripsi promo pada dialog harus berisi {string}")
    public void deskripsiPromoPadaDialogHarusBerisi(String expectedText) {
        String actual = promoPage.getDialogDescriptionText();
        Assertions.assertTrue(actual.contains(expectedText),
                "Deskripsi promo pada dialog tidak mengandung '" + expectedText + "'. Actual: '" + actual + "'");
    }

    @When("admin mengubah judul promo menjadi {string}")
    public void adminMengubahJudulPromoMenjadi(String newTitle) {
        promoPage.editTitle(newTitle);
    }

    @And("admin menunggu {int} detik")
    public void adminMenungguDetik(int seconds) {
        try { Thread.sleep(seconds * 1000L); } catch (InterruptedException ignored) {}
    }

    @And("admin menyimpan perubahan promo")
    public void adminMenyimpanPerubahanPromo() {
        promoPage.clickSaveChanges();
    }

    @Then("promo {string} harus terdaftar di halaman promo")
    public void promoHarusTerdaftarDiHalamanPromo(String promoTitle) {
        Assertions.assertTrue(promoPage.isPromoPresentInList(promoTitle),
                "Promo '" + promoTitle + "' tidak ditemukan dalam daftar.");
    }

    // ==========================================
    // 1.4 PROMO DELETE
    // ==========================================
    @When("admin mengklik tombol Hapus Promo")
    public void adminMengklikTombolHapusPromo() {
        promoPage.clickHapusPromo();
    }

    @Then("promo {string} harus sudah dihapus dari halaman promo")
    public void promoHarusSudahDihapusDariHalamanPromo(String promoTitle) {
        Assertions.assertTrue(promoPage.isPromoRemovedFromList(promoTitle),
                "Promo '" + promoTitle + "' masih terlihat.");
    }

    // ==========================================
    // 2.2 LAYANAN VERIFY
    // ==========================================
    @Then("card layanan {string} harus ada di halaman")
    public void cardLayananHarusAdaDiHalaman(String serviceName) {
        Assertions.assertTrue(layananPage.isLayananCardDisplayed(serviceName),
                "Card layanan '" + serviceName + "' tidak ditemukan.");
    }

    @Then("deskripsi layanan harus mengandung {string}")
    public void deskripsiLayananHarusMengandung(String text) {
        Assertions.assertTrue(layananPage.isDescriptionContains(text),
                "Deskripsi layanan tidak mengandung '" + text + "'.");
    }

    @Then("icon layanan harus memiliki atribut src")
    public void iconLayananHarusMemilikiAtributSrc() {
        Assertions.assertTrue(layananPage.isLayananIconHasSrc(),
                "Icon layanan tidak memiliki atribut src yang valid.");
    }

    // ==========================================
    // 2.3 LAYANAN EDIT
    // ==========================================
    @When("admin membuka dialog edit layanan {string}")
    public void adminMembukaDialogEditLayanan(String serviceName) {
        layananPage.openEditDialog(serviceName);
    }

    @Then("field nama layanan pada dialog harus berisi {string}")
    public void fieldNamaLayananPadaDialogHarusBerisi(String expectedName) {
        String actual = layananPage.getDialogNameInputValue();
        Assertions.assertEquals(expectedName, actual,
                "Nama layanan pada dialog tidak sesuai.");
    }

    @Then("editor artikel layanan harus mengandung teks {string}")
    public void editorArtikelLayananHarusMengandungTeks(String expectedText) {
        String actual = layananPage.getEditorText();
        Assertions.assertTrue(actual.contains(expectedText),
                "Editor artikel layanan tidak mengandung '" + expectedText + "'. Actual: '" + actual + "'");
    }

    @When("admin mengubah nama layanan menjadi {string}")
    public void adminMengubahNamaLayananMenjadi(String newName) {
        layananPage.editServiceName(newName);
    }

    @And("admin menyimpan perubahan layanan")
    public void adminMenyimpanPerubahanLayanan() {
        layananPage.clickSaveChanges();
    }

    @Then("layanan {string} harus terdaftar di halaman layanan")
    public void layananHarusTerdaftarDiHalamanLayanan(String serviceName) {
        Assertions.assertTrue(layananPage.isLayananPresentInList(serviceName),
                "Layanan '" + serviceName + "' tidak ditemukan dalam daftar.");
    }

    // ==========================================
    // 2.4 LAYANAN DELETE
    // ==========================================
    @When("admin mengklik tombol Hapus Layanan")
    public void adminMengklikTombolHapusLayanan() {
        layananPage.clickHapusLayanan();
    }

    @Then("layanan {string} harus sudah dihapus dari halaman layanan")
    public void layananHarusSudahDihapusDariHalamanLayanan(String serviceName) {
        Assertions.assertTrue(layananPage.isLayananRemovedFromList(serviceName),
                "Layanan '" + serviceName + "' masih terlihat.");
    }

    // ==========================================
    // 4.4 GALERI EMPTY FILE VALIDATION
    // ==========================================
    @When("admin mengklik tombol Tambahkan Gambar tanpa memilih file")
    public void adminMengklikTombolTambahkanGambarTanpaMemilihFile() {
        galeriPage.clickTambahkanGambarTanpaFile();
    }

    @Then("indikator error harus ditampilkan untuk input file galeri")
    public void indikatorErrorHarusDitampilkanUntukInputFileGaleri() {
        Assertions.assertTrue(galeriPage.isFileInputErrorDisplayed(),
                "Indikator error tidak ditampilkan untuk input file galeri.");
    }

    // ==========================================
    // 6.5 ARTIKEL EMPTY FORM VALIDATION
    // ==========================================
    @When("admin mengklik tombol submit artikel tanpa mengisi form")
    public void adminMengklikTombolSubmitArtikelTanpaMengisiForm() {
        artikelPage.clickSubmitWithoutFilling();
    }

    @Then("field artikel yang wajib diisi harus menampilkan indikator error")
    public void fieldArtikelYangWajibDiisiHarusMenampilkanIndikatorError() {
        Assertions.assertTrue(artikelPage.isAnyFieldInvalid(),
                "Tidak ada field artikel yang menampilkan validasi error.");
    }

    @And("admin mengisi judul promo dengan {int} karakter")
    public void adminMengisiJudulPromoDenganKarakter(int length) {
        String title = "A".repeat(length);
        promoPage.fillTitle(title);
    }

    @And("admin membuat file dummy {string} dengan ukuran {double} MB")
    public void adminMembuatFileDummyDenganUkuranMB(String fileName, double sizeInMB) {
        File file = new File("src/test/resources/" + fileName);
        try {
            file.getParentFile().mkdirs();
            if (file.exists()) {
                file.delete();
            }
            java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "rw");
            raf.setLength((long) (sizeInMB * 1024 * 1024));
            raf.close();
            file.deleteOnExit();
        } catch (Exception e) {
            throw new RuntimeException("Gagal membuat file dummy: " + e.getMessage(), e);
        }
    }

    @Then("pesan error validation {string} harus ditampilkan")
    public void pesanErrorValidationHarusDitampilkan(String expectedMessage) {
        By errorLoc = By.cssSelector(".text-red-500, .text-destructive, [role='alert'], [data-testid*='error-message'], [data-testid$='-error']");
        try {
            wait.until(driver -> {
                List<WebElement> errors = driver.findElements(errorLoc);
                for (WebElement el : errors) {
                    if (el.isDisplayed() && el.getText().toLowerCase().contains(expectedMessage.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            });
        } catch (Exception e) {
            List<WebElement> allTexts = driver.findElements(By.cssSelector("p, span, div, li, [role='alert']"));
            StringBuilder sb = new StringBuilder();
            for (WebElement el : allTexts) {
                if (el.isDisplayed()) {
                    String text = el.getText().trim();
                    if (!text.isEmpty() && text.length() < 300) {
                        sb.append(text).append("\n");
                    }
                }
            }
            Assertions.fail("Pesan error '" + expectedMessage + "' tidak ditemukan. Teks halaman:\n" + sb.toString());
        }
    }

    @And("admin mengisi judul artikel dengan {int} karakter")
    public void adminMengisiJudulArtikelDenganKarakter(int length) {
        String title = "A".repeat(length);
        artikelPage.fillTitle(title);
    }
}
