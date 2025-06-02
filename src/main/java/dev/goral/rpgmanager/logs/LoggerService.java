package dev.goral.rpgmanager.logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoggerService {
    private static final String LOG_DIR = "logs";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final CustomFormatter formatter = new CustomFormatter();

    private static void cleanupOldLogs() {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) return;
        File[] files = dir.listFiles((d, name) -> name.startsWith("log-") && name.endsWith(".log"));
        if (files == null) return;
        LocalDate threshold = LocalDate.now().minusDays(14);
        for (File file : files) {
            String datePart = file.getName().substring(4, 14);
            try {
                LocalDate fileDate = LocalDate.parse(datePart, FILE_DATE_FORMAT);
                if (fileDate.isBefore(threshold)) {
                    if (!file.delete())
                        System.err.println("Nie można usunąć starego logu: " + file.getName());
                    else
                        System.out.println("Usunięto stary log: " + file.getName());
                }
            } catch (Exception ignored) {
                System.err.println("Błąd parsowania daty logu: " + file.getName());
            }
        }
    }

    public static void log(String operationName, String level) {
        cleanupOldLogs();
        String message = String.format("Operacja: %s", operationName);
        String entry = formatter.format(new java.util.logging.LogRecord(
                java.util.logging.Level.parse(level), message));
        String filename = String.format("%s/log-%s.log", LOG_DIR, LocalDate.now().format(FILE_DATE_FORMAT));
        writeToFile(filename, entry);
    }

    public static void logInfo(String operationName) {
        log(operationName, "INFO");
    }

    public static void logWarning(String operationName) {
        log(operationName, "WARNING");
    }

    public static void logError(String operationName, Exception ex) {
        cleanupOldLogs();
        String message = String.format("Operacja: %s, Błąd: %s", operationName, ex.getMessage());
        String entry = formatter.format(new java.util.logging.LogRecord(
                java.util.logging.Level.SEVERE, message));
        String filename = String.format("%s/log-%s.log", LOG_DIR, LocalDate.now().format(FILE_DATE_FORMAT));
        writeToFile(filename, entry);
    }

    private static void writeToFile(String filename, String entry) {
        File dir = new File(LOG_DIR);
        if (!dir.exists())
            if (!dir.mkdirs()) {
                System.err.println("Nie można utworzyć katalogu logów: " + LOG_DIR);
                return;
            }
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(entry);
        } catch (IOException e) {
            System.err.println("Błąd zapisu logu: " + e.getMessage());
        }
    }
}