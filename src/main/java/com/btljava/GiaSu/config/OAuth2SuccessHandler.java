package com.btljava.GiaSu.config;

import com.btljava.GiaSu.entity.TaiKhoan;
import com.btljava.GiaSu.repository.TaiKhoanRepository;
import com.btljava.GiaSu.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TaiKhoanRepository taiKhoanRepository;
    private final JwtService jwtService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        Optional<TaiKhoan> taiKhoanOptional = taiKhoanRepository.findByEmail(email);

        if (taiKhoanOptional.isPresent()) {

            TaiKhoan taiKhoan = taiKhoanOptional.get();
            String token = jwtService.generateToken(taiKhoan);
            String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                    .queryParam("token", token)
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {

            String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/select-role")
                    .queryParam("email", email)
                    .queryParam("name", name)
                    .queryParam("avatar", picture)
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}
