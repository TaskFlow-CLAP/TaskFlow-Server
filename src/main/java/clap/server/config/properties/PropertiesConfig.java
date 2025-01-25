package clap.server.config.properties;

import clap.server.common.properties.PasswordPolicyProperties;
import clap.server.common.properties.ServerDomainProperties;
import clap.server.common.properties.WebDomainProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        ServerDomainProperties.class,
        WebDomainProperties.class,
        PasswordPolicyProperties.class
})
public class PropertiesConfig {
}