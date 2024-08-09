package com.gabrielflores.myfortune.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public class HttpUtils {

    public static String getBaseServerUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    }

    public static String getBaseServerUrl(final HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
    }
}
