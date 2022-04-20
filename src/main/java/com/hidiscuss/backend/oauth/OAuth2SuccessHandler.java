package com.hidiscuss.backend.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hidiscuss.backend.entity.User;
import com.hidiscuss.backend.oauth.token.Token;
import com.hidiscuss.backend.oauth.token.TokenService;
import com.hidiscuss.backend.oauth.util.UserRequestMapper;
import com.hidiscuss.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        String gitaccessToken = oAuth2User.getAttribute("accessToken");
        // 최초 로그인이라면 회원가입 처리를 한다.
        Token token = tokenService.generateToken(oAuth2User.getName());
        User user1 =userRepository.getByName(oAuth2User.getName());
        if (user1 == null) {
            User user = userRequestMapper.toUser(oAuth2User, gitaccessToken);
            userRepository.save(user);
        }
        Cookie gitAccessToken = new Cookie("gitAccessToken", gitaccessToken);
        Cookie accessToken = new Cookie("accessToken", token.getToken());
        Cookie refreshToken = new Cookie("refreshToken", token.getRefreshToken());

        gitAccessToken.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        accessToken.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        refreshToken.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정

        response.addCookie(gitAccessToken);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000");
    }
}