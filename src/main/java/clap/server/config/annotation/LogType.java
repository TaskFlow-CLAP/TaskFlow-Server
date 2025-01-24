package clap.server.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 런타임 시 어노테이션 접근 가능
@Target(ElementType.METHOD) // 메서드
public @interface LogType {
    String value();
}
