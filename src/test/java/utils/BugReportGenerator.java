package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BugReportGenerator {

    private static final String JSON_PATH = "target/cucumber-reports/cucumber.json";
    private static final String OUT_DIR = "target/bug-reports";
    private static final String OUT_PATH = OUT_DIR + "/bug_report_auto.csv";

    public static void main(String[] args) throws IOException {
        if (!Files.exists(Paths.get(JSON_PATH))) {
            System.out.println("[BugReportGenerator] " + JSON_PATH + " tidak ditemukan. "
                    + "Jalankan `mvn test` terlebih dahulu sebelum menjalankan generator ini.");
            return;
        }

        String content = new String(Files.readAllBytes(Paths.get(JSON_PATH)));
        JSONArray features = new JSONArray(content);

        List<String[]> bugRows = new ArrayList<>();
        int bugCounter = 1;

        for (int f = 0; f < features.length(); f++) {
            JSONObject feature = features.getJSONObject(f);
            String featureName = feature.optString("name", "Unknown Feature");
            JSONArray elements = feature.optJSONArray("elements");
            if (elements == null) continue;

            for (int e = 0; e < elements.length(); e++) {
                JSONObject scenario = elements.getJSONObject(e);
                if (!"scenario".equals(scenario.optString("type"))) continue;

                String scenarioName = scenario.optString("name", "Unknown Scenario");
                JSONArray steps = scenario.optJSONArray("steps");
                if (steps == null) continue;

                StringBuilder reproSteps = new StringBuilder();
                boolean scenarioFailed = false;
                String failedStepText = "";
                String errorMessage = "";
                int stepNo = 1;

                for (int s = 0; s < steps.length(); s++) {
                    JSONObject step = steps.getJSONObject(s);
                    String keyword = step.optString("keyword", "").trim();
                    String name = step.optString("name", "");
                    reproSteps.append(stepNo++).append(". ").append(keyword).append(" ").append(name).append(" | ");

                    JSONObject result = step.optJSONObject("result");
                    if (result != null && "failed".equals(result.optString("status"))) {
                        scenarioFailed = true;
                        failedStepText = keyword + " " + name;
                        errorMessage = result.optString("error_message", "")
                                .split("\n")[0]; // ambil baris pertama saja, hindari stack trace panjang
                    }
                }

                if (scenarioFailed) {
                    String bugId = String.format("BUG-AUTO-%03d", bugCounter++);
                    String severity = guessSeverity(scenarioName);
                    String[] row = new String[]{
                            bugId,
                            "[OTOMATIS] Skenario gagal: " + scenarioName,
                            featureName,
                            severity,
                            "High",
                            scenarioName,
                            reproSteps.toString(),
                            "Skenario berjalan sesuai langkah Gherkin tanpa error",
                            "Gagal pada step: " + failedStepText + " -- Pesan: " + errorMessage,
                            "Chrome (Selenium WebDriver) / cucumber.json hasil mvn test",
                            "Open - Perlu Verifikasi Manual",
                            "Automated Test Runner",
                            LocalDate.now().toString()
                    };
                    bugRows.add(row);
                }
            }
        }

        Files.createDirectories(Paths.get(OUT_DIR));
        try (FileWriter writer = new FileWriter(OUT_PATH)) {
            writer.write("Bug ID,Judul Bug,Modul/Feature,Severity,Priority,Test Case/Skenario Terkait,"
                    + "Langkah Reproduksi,Hasil Diharapkan,Hasil Aktual,Environment,Status,Reporter,Tanggal\n");
            for (String[] row : bugRows) {
                writer.write(toCsvLine(row));
                writer.write("\n");
            }
        }

        System.out.println("[BugReportGenerator] Selesai. " + bugRows.size()
                + " bug otomatis ditemukan dari skenario yang gagal.");
        System.out.println("[BugReportGenerator] Hasil tersimpan di: " + OUT_PATH);
    }

    private static String guessSeverity(String scenarioName) {
        String lower = scenarioName.toLowerCase();
        if (lower.contains("berhasil") || lower.contains("e2e")) return "Critical";
        if (lower.contains("kosong") || lower.contains("wajib")) return "High";
        return "Medium";
    }

    private static String toCsvLine(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            String v = fields[i] == null ? "" : fields[i].replace("\"", "\"\"");
            sb.append("\"").append(v).append("\"");
            if (i < fields.length - 1) sb.append(",");
        }
        return sb.toString();
    }
}
