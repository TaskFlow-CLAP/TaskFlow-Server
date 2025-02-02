package clap.server.exception;

public class ErrorContext {

    private static final ThreadLocal<String> customCodeHolder = new ThreadLocal<>();

    // customCode 설정
    public static void setCustomCode(String customCode) {
        customCodeHolder.set(customCode);
    }

    // customCode 가져오기
    public static String getCustomCode() {
        return customCodeHolder.get();
    }

    // ThreadLocal 초기화
    public static void clear() {
        customCodeHolder.remove();
    }
}
