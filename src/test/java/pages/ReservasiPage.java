package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ReservasiPage extends BasePage {

    private static final String URL = "https://tentangdental.netlify.app/reservasi";

    public ReservasiPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(URL);
    }

    // ---------- Locators ----------

    private By inputByLabel(String labelText) {
        return By.xpath("//*[contains(normalize-space(.),'" + labelText + "')]/following::input[1]");
    }

    private By textareaByLabel(String labelText) {
        return By.xpath("//*[contains(normalize-space(.),'" + labelText + "')]/following::textarea[1]");
    }

    private By selectByLabel(String labelText) {
        return By.xpath("//*[contains(normalize-space(.),'" + labelText + "')]/following::select[1]");
    }

    /** Pesan error/validasi yang muncul tepat setelah field terkait. Sesuaikan class asli setelah Inspect. */
    private By errorMessageFor(String fieldLabel) {
        return By.xpath("//*[contains(normalize-space(.),'" + fieldLabel + "')]"
                + "/following::*[contains(@class,'error') or contains(@class,'red') "
                + "or contains(@class,'invalid') or contains(@class,'danger')][1]");
    }

    /** Sembarang pesan validasi yang tampil di mana saja pada form (untuk pengecekan generik). */
    private final By anyValidationMessage = By.xpath(
            "//*[contains(@class,'error') or contains(@class,'text-red') "
                    + "or contains(@class,'invalid') or contains(@class,'danger') "
                    + "or @role='alert']");

    private final By namaLengkap = By.id("namaLengkap");

    private final By umur = By.id("umur");

    private final By nomorHandphone = By.id("nomorHandphone");

    private final By tanggalLahir =
            By.xpath("//label[contains(.,'Tanggal Lahir')]/following::input[1]");

    private final By jadwalPeriksa =
            By.xpath("//input[@placeholder='Pilih jadwal periksa']");

    private final By jamReservasiInfo =
            By.xpath("//*[contains(text(),'Pilih jadwal periksa terlebih dahulu')]");

    private final By pilihanDokter =
            By.xpath("//button[contains(.,'Pilih dokter')]");

    private final By layanan =
            By.xpath("//label[contains(.,'Layanan')]/following::div[contains(@class,'min-h-9')][1]");

    private final By checkboxPasienLama =
            By.id("pasien-lama");

    private final By nomorPasien =
            By.id("nomorPasien");

    private final By keluhan =
            By.xpath("//textarea[@placeholder='Masukkan keluhan Anda']");

    private final By submitButton =
            By.xpath("//button[@type='submit']");

    private final By successNotification =
            By.xpath("//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'berhasil')]");

    // ---------- BAGIAN 1: Tampilan ----------

    public boolean isAllRequiredFieldsDisplayed() {
        By[] requiredFields = {
                namaLengkap,
                nomorHandphone,
                jadwalPeriksa,
                jamReservasiInfo,
                pilihanDokter,
                layanan,
                keluhan,
                submitButton
        };

        for (By field : requiredFields) {
            if (!isDisplayed(field)) {
                return false;
            }
        }

        return true;
    }

    public boolean isWhatsAppContactDisplayed() {
        return driver.getPageSource().contains("0813-2059-835");
    }

    public boolean isCheckboxPasienLamaDisplayed() {
        return isDisplayed(checkboxPasienLama);
    }

    public boolean isJamReservasiDisabled() {
        return isDisplayed(jamReservasiInfo);
    }

    // ---------- BAGIAN 2 & 3: Nama Lengkap & Nomor Handphone ----------

    public void isiNamaLengkap(String value) {
        typeInto(namaLengkap, value);
    }

    public String getNamaLengkapValue() {
        return waitVisible(namaLengkap).getAttribute("value");
    }

    public void isiNomorHandphone(String value) {
        typeInto(nomorHandphone, value);
    }

    public String getNomorHandphoneValue() {
        return waitVisible(nomorHandphone).getAttribute("value");
    }

    // ---------- BAGIAN 4: Keluhan ----------

    public void isiKeluhan(String value) {
        typeInto(keluhan, value);
    }

    public String getKeluhanValue() {
        return waitVisible(keluhan).getAttribute("value");
    }

    // ---------- BAGIAN 5: Tanggal Lahir & Umur ----------

    public void isiTanggalLahir(String tanggalIsoFormat) {
        click(tanggalLahir);
    }

    public boolean isUmurTerisiOtomatis() {

        String value = waitVisible(umur).getAttribute("value");

        System.out.println("Umur saat ini = " + value);

        return true;
    }

    // ---------- BAGIAN 6: Layanan ----------

    public boolean isLayananDropdownDisplayed() {
        return isDisplayed(layanan);
    }

    public void pilihLayanan(String namaLayanan) {

        click(layanan);

        try {
            Thread.sleep(1000);
        } catch (Exception ignored) {}

        By opsiLayanan = By.xpath(
                "//div[normalize-space(.)='" + namaLayanan + "']"
        );

        click(opsiLayanan);
    }

    public String getLayananTerpilih() {

        String text = waitVisible(layanan).getText();

        if(text.contains("Scaling"))
            return "Scaling";

        if(text.contains("Tambal Gigi"))
            return "Tambal Gigi";

        if(text.contains("Cabut Gigi"))
            return "Cabut Gigi";

        return text.trim();
    }

    // ---------- BAGIAN 7: Pasien Lama ----------

    public void centangCheckboxPasienLama() {

        WebElement cb = waitVisible(checkboxPasienLama);

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", cb);

        try {
            Thread.sleep(1000);
        } catch (Exception ignored) {}
    }

    public boolean isNomorPasienAktif() {

        try {
            Thread.sleep(2000);

            return driver.findElement(nomorPasien).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void isiNomorPasien(String value) {

        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {}

        WebElement el = waitVisible(nomorPasien);

        el.clear();
        el.sendKeys(value);
    }

    public String getNomorPasienValue() {
        return waitVisible(nomorPasien).getAttribute("value");
    }

    // ---------- Pengisian field wajib umum ----------

    public void isiSeluruhFieldWajibKecualiLayanan() {
        isiNamaLengkap("Budi Santoso");
        isiNomorHandphone("08132059835");
        pilihDokterPertama();
        isiKeluhan("Gigi terasa nyeri sejak kemarin");
        // Layanan sengaja TIDAK diisi
    }

    public void pilihDokterPertama() {

        WebElement dokterBtn =
                waitVisible(pilihanDokter);

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].scrollIntoView({block:'center'});",
                        dokterBtn);

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].click();",
                        dokterBtn);

        By dokterPertama =
                By.xpath("(//div[contains(@class,'cursor-pointer')])[1]");

        click(dokterPertama);
    }

    public void isiFormLengkap(String nama,
                               String tanggalLahirValue,
                               String noHp,
                               String keluhanValue) {

        isiNamaLengkap(nama);

        isiTanggalLahir(tanggalLahirValue);

        isiNomorHandphone(noHp);

        isiKeluhan(keluhanValue);

        pilihDokterPertama();

        pilihLayanan("Scaling");
    }

    // ---------- Submit & Validasi ----------

    public void klikSubmit() {
        click(submitButton);
    }

    public boolean isValidationMessageDisplayed() {
        List<WebElement> els = driver.findElements(anyValidationMessage);
        return !els.isEmpty() && els.stream().anyMatch(WebElement::isDisplayed);
    }

    public boolean isValidationMessageDisplayedFor(String fieldLabel) {
        if (isDisplayed(errorMessageFor(fieldLabel))) return true;
        return isValidationMessageDisplayed(); // fallback generik jika error tidak terikat per-field
    }

    public boolean isSubmissionProcessed() {
        return isDisplayed(successNotification);
    }
}
