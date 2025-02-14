package clap.server.adapter.inbound.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.Arrays;
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
        String sanitizedValue = Optional.ofNullable(value)
                .map(this::sanitize)
                .orElse(null);
        log.info("Original parameter [{}]: {}", parameter, value);
        log.info("Sanitized parameter [{}]: {}", parameter, sanitizedValue);
        return sanitizedValue;
    }

    @Override
    public String getHeader(String name) {
        return Optional.ofNullable(super.getHeader(name))
                .map(this::sanitize)
                .orElse(null);
    }


    public String sanitize(String value) {
        if (value == null) {
            return null;
        }
        if (value.toLowerCase().startsWith("javascript:")) {
            return "";
        }
        return Jsoup.clean(value, Safelist.basic());
    }
}