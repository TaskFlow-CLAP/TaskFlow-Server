package clap.server.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SpringEnvironmentHelper {

    private final Environment environment;

    private final String PROD = "prod";
    private final String DEV = "dev";
    private final String LOCAL = "local";

    private final List<String> LOCAL_AND_DEV = List.of("local", "dev");

    public Boolean isProdProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> currentProfile = Arrays.stream(activeProfiles).toList();
        return currentProfile.contains(PROD);
    }

    public Boolean isLocalProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> currentProfile = Arrays.stream(activeProfiles).toList();
        return currentProfile.contains(LOCAL);
    }

    public Boolean isDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> currentProfile = Arrays.stream(activeProfiles).toList();
        return currentProfile.contains(DEV);
    }

    public Boolean isLocalAndDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> currentProfile = Arrays.stream(activeProfiles).toList();
        return CollectionUtils.containsAny(LOCAL_AND_DEV, currentProfile);
    }
}
