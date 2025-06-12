package com.example.BoatRegistry.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationHelper extends OncePerRequestFilter {
    private final UserDetailsServiceImp userDetailsService;
    private final JwtHelper jwtHelper;

    public JwtAuthenticationHelper(UserDetailsServiceImp userDetailsService, JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String email = null;
            if (authHeader != null && authHeader.startsWith("Bearer")) {
                token = authHeader.substring(7);
                email = jwtHelper.getEmail(token);
            }

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtHelper.validateToken(token, userDetails)) {
                    var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
                    var webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                    authenticationToken.setDetails(webAuthenticationDetails);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(e.getMessage());
        } catch (UsernameNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
        }
    }
}
