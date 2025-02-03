package clap.server.adapter.inbound.security.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;

public class CustomGrantedAuthority implements GrantedAuthority, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String role;

    @JsonCreator
    public CustomGrantedAuthority(
            @JsonProperty("authority") @NotNull
            String role
    ) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof CustomGrantedAuthority cga) {
            return this.role.equals(cga.getAuthority());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }
}
