package com.gabrielflores.myfortune.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
public class AuditorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                final UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
                final WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
                ObjectHolder.setCurrentUserId(user.getId());
                ObjectHolder.setCurrentIp(details.getRemoteAddress());
            } else {
                ObjectHolder.setCurrentIp(request.getRemoteAddr());
            }
            filterChain.doFilter(request, response);
        } finally {
            ObjectHolder.removeCurrentUserId();
            ObjectHolder.removeCurrentIp();
        }
    }

}
