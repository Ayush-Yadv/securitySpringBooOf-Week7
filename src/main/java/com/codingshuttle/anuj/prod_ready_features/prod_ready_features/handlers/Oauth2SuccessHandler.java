package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.handlers;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.User;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services.JwtServices;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor

public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private  final UserService userService;
    private final JwtServices jwtServices;

  @Override
   public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response,
                                       Authentication authentication ) throws IOException, ServletException, java.io.IOException {

      OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

      DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) token.getPrincipal();

      String email = defaultOAuth2User.getAttribute("email");

      User user = userService.getByEmail(email);
          if(user==null){
              User newUser= User.builder()
                      .name(defaultOAuth2User.getAttribute("name"))
                      .email(email)
                      .build();
         user =userService.saveUser(newUser);
      }
        String accessToken=jwtServices.generateAccessToken(user);
        String RefreshToken=jwtServices.generateRefreshToken(user) ;

      Cookie cookie=new Cookie("refreshToken",RefreshToken);

      cookie.setHttpOnly(true);
      response.addCookie(cookie);

      String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;

      getRedirectStrategy().sendRedirect(request, response, frontEndUrl);

  }
}
