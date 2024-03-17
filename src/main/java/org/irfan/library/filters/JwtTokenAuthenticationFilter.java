package org.irfan.library.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.irfan.library.services.JwtTokenService;
import org.irfan.library.services.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean shouldContinueFilterChain = true;
        try {
            Optional<String> tokenOptional = jwtTokenService.getJwtFromRequest(request);
            if (tokenOptional.isPresent()) {
                String token = tokenOptional.get();
                if (jwtTokenService.validateToken(token)) {
                    String username = jwtTokenService.getUsernameFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("Refresh Token");
                }
            } else {
                logger.warn("No JWT token found in request headers");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erreur d'authentification.");
            shouldContinueFilterChain = false;
        }
        if (shouldContinueFilterChain) {
            filterChain.doFilter(request, response);
        }
    }
}
