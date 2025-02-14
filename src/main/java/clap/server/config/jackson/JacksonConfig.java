package clap.server.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Slf4j
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // XSS 방지를 위한 커스텀 모듈 추가
        SimpleModule xssModule = new SimpleModule();
        xssModule.addDeserializer(String.class, new JsonHtmlXssDeserializer());
        mapper.registerModule(xssModule);

        return mapper;
    }

    // XSS 방지를 위한 Jackson 설정
    public static class JsonHtmlXssDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            if (value == null) {
                return null;
            }
            if (value.toLowerCase().startsWith("javascript:")) {
                return "";
            }
            return Jsoup.clean(value, Safelist.basic());
        }
    }
}
