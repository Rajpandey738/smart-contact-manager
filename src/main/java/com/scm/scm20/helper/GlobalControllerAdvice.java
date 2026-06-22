package com.scm.scm20.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm20.entities.User;
import com.scm.scm20.repositories.UserRepo;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserRepo userRepo;

    @ModelAttribute("loggedInUser")
    public User loggedInUser() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal()
                        .equals("anonymousUser")) {

            return null;
        }

        String email = authentication.getName();
        System.out.println("Authenticated user email: " + email);

        // For Google OAuth
        if (authentication.getPrincipal() instanceof DefaultOAuth2User oauthUser) {

            email = oauthUser.getAttribute("email");
            System.out.println("OAuth user email: " + email);
        }

        return userRepo.findByEmail(email)
                .orElse(null);
    }
}