package clap.server.adapter.inbound.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;

@Slf4j
public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        return Optional.ofNullable(super.getParameterValues(parameter))
            .map(values -> Arrays.stream(values)
                .map(this::sanitize)
                .toArray(String[]::new))
            .orElse(null);
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        log.info("Original parameter [{}]: {}", parameter, value);

        String sanitizedValue = Optional.ofNullable(value)
                .map(this::sanitize)
                .orElse(null);

        log.info("Sanitized parameter [{}]: {}", parameter, sanitizedValue);
        return sanitizedValue;
    }

    @Override
    public String getHeader(String name) {
        String originalValue = super.getHeader(name);
        String sanitizedValue = sanitize(originalValue);
        log.debug("Original header [{}]: {}", name, originalValue);
        log.debug("Sanitized header [{}]: {}", name, sanitizedValue);
        return sanitizedValue;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return new Enumeration<String>() {
            private Enumeration<String> enum1 = XssRequestWrapper.super.getHeaderNames();
            @Override
            public boolean hasMoreElements() {
                return enum1.hasMoreElements();
            }
            @Override
            public String nextElement() {
                return sanitize(enum1.nextElement());
            }
        };
    }


    private String sanitize(String value) {
        return Optional.ofNullable(value)
            .map(str -> Jsoup.clean(str, Safelist.basic()))
            .orElse(null);
    }
}