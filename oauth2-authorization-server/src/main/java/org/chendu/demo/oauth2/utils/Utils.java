package org.chendu.demo.oauth2.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Chen Du @10/31/2018.
 * Version 1.0
 */
public class Utils {

    public static String passwordEncoder(String password) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        return encodedPassword;
    }
}
