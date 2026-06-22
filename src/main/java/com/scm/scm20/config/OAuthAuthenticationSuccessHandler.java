package com.scm.scm20.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm20.entities.Providers;
import com.scm.scm20.entities.User;
import com.scm.scm20.helper.AppConstant;
import com.scm.scm20.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuth2User oauthUser = token.getPrincipal();

        String email = oauthUser.getAttribute("email");

        String name = oauthUser.getAttribute("name");

        String picture = oauthUser.getAttribute("picture");

        String googleId = oauthUser.getAttribute("sub");

        Optional<User> userOptional = userRepo.findByEmail(email);

        if (userOptional.isEmpty()) {

            User user = new User();

            user.setUserId(
                    UUID.randomUUID().toString());

            user.setEmail(email);

            user.setName(name);

            user.setProfilePic(picture);

            user.setEnabled(true);

            user.setEmailVerified(true);

            user.setProvider(Providers.GOOGLE);

            user.setProviderId(googleId);

            user.setRoleList(
                    List.of(AppConstant.ROLE_USER));

            userRepo.save(user);
        }

        response.sendRedirect("/user/dashboard");
    }
}