package dev.goral.rpghandyhelper.logs;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomFormatter extends Formatter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("[")
                .append(dateFormat.format(new Date(record.getMillis())))
                .append(" | ")
                .append(record.getLevel().getName())
                .append(" ] ==> [")
                .append(record.getMessage())
                .append("]\n");
        return sb.toString();
    }
}
