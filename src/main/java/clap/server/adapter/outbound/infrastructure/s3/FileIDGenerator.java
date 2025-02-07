package clap.server.adapter.outbound.infrastructure.s3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class FileIDGenerator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public static String createFileId() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(formatter);
        int seq = sequence.getAndIncrement() % 1000; // 0부터 999까지 순환
        return String.format("%s-%03d", date, seq);
    }
}
