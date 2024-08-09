package com.gabrielflores.myfortune.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Component
@RequiredArgsConstructor
public class ProfileManager {

    private static final String PROFILE_PROD = "petconnect";
    private static final String PROFILE_HOMOLOGACAO = "homologacao";

    @Value("${spring.profiles.active:}")
    private final String profileAtual;

    public Boolean isDev() {
        return !isProducao() && !isHomologacao();
    }

    public Boolean isProducao() {
        return PROFILE_PROD.equalsIgnoreCase(profileAtual);
    }

    public Boolean isHomologacao() {
        return PROFILE_HOMOLOGACAO.equalsIgnoreCase(profileAtual);
    }

    public String getProfileAtual() {
        return profileAtual;
    }

}
