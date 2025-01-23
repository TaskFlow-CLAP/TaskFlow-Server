package clap.server.common.annotation.jwt;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target(value = {
	ElementType.ANNOTATION_TYPE,
	ElementType.FIELD,
	ElementType.METHOD,
	ElementType.PARAMETER,
	ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("refreshTokenStrategy")
public @interface RefreshTokenStrategy {
}
