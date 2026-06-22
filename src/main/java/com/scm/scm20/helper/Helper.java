package com.scm.scm20.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(
            Authentication authentication) {

        if (authentication.getPrincipal() instanceof DefaultOAuth2User oauthUser) {

            return oauthUser.getAttribute("email");
        }

        return authentication.getName();
    }
}