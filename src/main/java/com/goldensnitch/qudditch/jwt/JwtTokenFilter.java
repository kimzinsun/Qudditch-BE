package com.goldensnitch.qudditch.jwt;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 토큰에서 권한을 추출하여 Security Context에 저장하는 메소드
    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    //         throws ServletException, IOException {
    //     try {
    //         if (!isLoginRequest(request)) {
    //             String token = getTokenFromRequest(request);
    //             if (token != null && jwtTokenProvider.validateToken(token)) {
    //                 Claims claims = jwtTokenProvider.extractClaims(token);
    //                 UsernamePasswordAuthenticationToken authentication =
    //                     new UsernamePasswordAuthenticationToken(claims.getSubject(), null, jwtTokenProvider.getAuthorities(token));
    //                 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    //                 SecurityContextHolder.getContext().setAuthentication(authentication);
    //             }
    //         }

    //     } catch (ExpiredJwtException e) {
    //         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
    //     } catch (MalformedJwtException e) {
    //         response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token format");
    //     } catch (UnsupportedJwtException e) {
    //         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported JWT token");
    //     } catch (JwtException e) {
    //         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
    //     }
    //     filterChain.doFilter(request, response);
    // }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
        Claims claims = jwtTokenProvider.extractClaims(token);
        Collection<? extends GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            claims.getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
filterChain.doFilter(request, response);

}

    private boolean isLoginRequest(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/login") || path.equals("/store/login") || path.startsWith("/social-login");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
