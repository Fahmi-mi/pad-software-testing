package stepdefinitions;

import hooks.Hooks;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ReservasiPage;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservasiSteps {

    private final ReservasiPage reservasiPage = new ReservasiPage(Hooks.driver);

    @Given("Pengguna membuka halaman Reservasi")
    public void pengguna_membuka_halaman_reservasi() {
        reservasiPage.open();
    }

    @Then("Form reservasi beserta semua field wajib ditampilkan")
    public void form_reservasi_field_wajib_ditampilkan() {
        assertTrue(
                reservasiPage.isAllRequiredFieldsDisplayed(),
                "Tidak semua field wajib pada form reservasi tampil"
        );
    }

    @Then("Informasi kontak WhatsApp ditampilkan")
    public void informasi_kontak_whatsapp_ditampilkan() {
        assertTrue(
                reservasiPage.isWhatsAppContactDisplayed(),
                "Link/Informasi kontak WhatsApp tidak ditemukan di halaman"
        );
    }

    @Then("Checkbox pasien lama tersedia di form")
    public void checkbox_pasien_lama_tersedia() {
        assertTrue(
                reservasiPage.isCheckboxPasienLamaDisplayed(),
                "Checkbox pasien lama tidak ditemukan di form"
        );
    }

    @Then("Dropdown Jam Reservasi tidak aktif sebelum jadwal dipilih")
    public void dropdown_jam_reservasi_tidak_aktif() {
        assertTrue(
                reservasiPage.isJamReservasiDisabled(),
                "Dropdown Jam Reservasi seharusnya nonaktif sebelum Jadwal Periksa dipilih"
        );
    }

    @When("Pengguna menekan tombol Submit tanpa mengisi form")
    public void pengguna_menekan_submit_tanpa_mengisi_form() {
        reservasiPage.klikSubmit();
    }

    @When("Pengguna menekan tombol Submit")
    public void pengguna_menekan_submit() {
        reservasiPage.klikSubmit();
    }

    @Then("Sistem menolak submit dan menampilkan validasi")
    public void sistem_menolak_submit_dan_menampilkan_validasi() {
        assertTrue(
                reservasiPage.isValidationMessageDisplayed(),
                "Sistem seharusnya menampilkan pesan validasi saat submit ditolak"
        );
    }

    @When("Pengguna mengisi Nama Lengkap dengan {string}")
    public void pengguna_mengisi_nama_lengkap(String nama) {
        reservasiPage.isiNamaLengkap(nama);
    }

    @Then("Field Nama Lengkap berhasil menerima input {string}")
    public void field_nama_lengkap_berhasil_menerima_input(String nama) {
        assertEquals(
                nama,
                reservasiPage.getNamaLengkapValue(),
                "Nilai pada field Nama Lengkap tidak sesuai input"
        );
    }

    @When("Pengguna mengisi Nomor Handphone dengan {string}")
    public void pengguna_mengisi_nomor_handphone(String nomor) {
        reservasiPage.isiNomorHandphone(nomor);
    }

    @Then("Field Nomor Handphone berhasil menerima input {string}")
    public void field_nomor_handphone_berhasil_menerima_input(String nomor) {
        assertEquals(
                nomor,
                reservasiPage.getNomorHandphoneValue(),
                "Nilai pada field Nomor Handphone tidak sesuai input"
        );
    }

    @When("Pengguna mengisi Keluhan dengan {string}")
    public void pengguna_mengisi_keluhan(String keluhan) {
        reservasiPage.isiKeluhan(keluhan);
    }

    @Then("Field Keluhan berhasil menerima input")
    public void field_keluhan_berhasil_menerima_input() {
        String value = reservasiPage.getKeluhanValue();

        assertTrue(
                value != null && !value.trim().isEmpty(),
                "Field Keluhan seharusnya berisi teks yang diinput"
        );
    }

    @When("Pengguna mengisi Tanggal Lahir dengan {string}")
    public void pengguna_mengisi_tanggal_lahir(String tanggal) {
        reservasiPage.isiTanggalLahir(tanggal);
    }

    @Then("Field Umur terisi secara otomatis")
    public void field_umur_terisi_otomatis() {
        assertTrue(
                reservasiPage.isUmurTerisiOtomatis(),
                "Field Umur seharusnya terisi otomatis setelah Tanggal Lahir diisi"
        );
    }

    @Then("Dropdown layanan ditampilkan")
    public void dropdown_layanan_ditampilkan() {
        assertTrue(
                reservasiPage.isLayananDropdownDisplayed(),
                "Dropdown Layanan tidak ditemukan di form"
        );
    }

    @When("Pengguna memilih layanan {string}")
    public void pengguna_memilih_layanan(String layanan) {
        reservasiPage.pilihLayanan(layanan);
    }

    @Then("Layanan {string} berhasil dipilih")
    public void layanan_berhasil_dipilih(String namaLayanan) {
        assertEquals(
                namaLayanan,
                reservasiPage.getLayananTerpilih(),
                "Layanan yang terpilih tidak sesuai"
        );
    }

    @When("Pengguna mengisi seluruh field wajib kecuali layanan")
    public void pengguna_mengisi_seluruh_field_wajib_kecuali_layanan() {
        reservasiPage.isiSeluruhFieldWajibKecualiLayanan();
    }

    @Then("Sistem menolak submit dan menampilkan validasi layanan")
    public void sistem_menolak_submit_validasi_layanan() {
        assertTrue(
                reservasiPage.isValidationMessageDisplayedFor("Layanan"),
                "Sistem seharusnya menampilkan validasi khusus untuk field Layanan"
        );
    }

    @When("Pengguna mencentang checkbox pasien lama")
    public void pengguna_mencentang_checkbox_pasien_lama() {
        reservasiPage.centangCheckboxPasienLama();
    }

    @Then("Opsi nomor pasien aktif setelah checkbox dicentang")
    public void opsi_nomor_pasien_aktif() {
        assertTrue(
                reservasiPage.isNomorPasienAktif(),
                "Field Nomor Pasien seharusnya aktif setelah checkbox pasien lama dicentang"
        );
    }

    @When("Pengguna menekan tombol Submit tanpa mengisi nomor pasien")
    public void pengguna_menekan_submit_tanpa_mengisi_nomor_pasien() {
        reservasiPage.klikSubmit();
    }

    @Then("Sistem menolak submit dan menampilkan validasi nomor pasien")
    public void sistem_menolak_submit_validasi_nomor_pasien() {
        assertTrue(
                reservasiPage.isValidationMessageDisplayedFor("Nomor Pasien"),
                "Sistem seharusnya menampilkan validasi khusus untuk field Nomor Pasien"
        );
    }

    @When("Pengguna mengisi Nomor Pasien dengan {string}")
    public void pengguna_mengisi_nomor_pasien(String value) {
        reservasiPage.isiNomorPasien(value);
    }

    @Then("Field Nomor Pasien berhasil menerima input {string}")
    public void field_nomor_pasien_berhasil_menerima_input(String value) {
        assertEquals(
                value,
                reservasiPage.getNomorPasienValue(),
                "Nilai pada field Nomor Pasien tidak sesuai input"
        );
    }

    @When("Pengguna mengisi form dengan data berikut:")
    public void pengguna_mengisi_form_dengan_data_berikut(DataTable dataTable) {

        Map<String, String> data =
                dataTable.asMap(String.class, String.class);

        reservasiPage.isiFormLengkap(
                data.get("nama"),
                data.get("tanggalLahir"),
                data.get("noHP"),
                data.get("keluhan")
        );
    }

    @Then("Sistem memproses pengiriman reservasi")
    public void sistem_memproses_pengiriman_reservasi() {
        assertTrue(
                reservasiPage.isSubmissionProcessed(),
                "Sistem seharusnya menampilkan konfirmasi reservasi berhasil diproses"
        );
    }
}