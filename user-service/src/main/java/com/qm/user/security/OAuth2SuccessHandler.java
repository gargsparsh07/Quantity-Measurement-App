package com.qm.user.security;

import com.qm.user.model.*;
import com.qm.user.repository.UserRepository;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component @RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attrs = oAuth2User.getAttributes();
        String email = (String) attrs.get("email");
        String name  = (String) attrs.get("name");
        String sub   = (String) attrs.get("sub");

        User user = userRepository.findByEmail(email).orElseGet(() ->
            userRepository.save(User.builder().name(name).email(email)
                .role(Role.ROLE_USER).provider(AuthProvider.GOOGLE).providerId(sub).build()));

        var ud = new org.springframework.security.core.userdetails.User(
            user.getEmail(), "",
            List.of(new SimpleGrantedAuthority(user.getRole().name())));
        String token = jwtService.generateToken(ud);

        response.setContentType("application/json");
        response.getWriter().write(
            "{\"token\":\"" + token + "\",\"email\":\"" + email + "\",\"name\":\"" + name + "\"}");
    }
}
