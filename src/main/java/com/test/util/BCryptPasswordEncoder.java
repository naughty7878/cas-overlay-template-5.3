package com.test.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
public class BCryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(final CharSequence password) {
        if (password == null) {
            return null;
        }
//        log.info("password = " + password );
        try {
            final String encoded = BCrypt.hashpw(password.toString(), BCrypt.gensalt());;
            return encoded;
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {

//        final String encodedRawPassword = StringUtils.isNotBlank(rawPassword) ? encode(rawPassword.toString()) : null;
        if (rawPassword == null) {
            return false;
        }
//        log.info("rawPassword = " + rawPassword );
//        log.info("encodedPassword = " + encodedPassword );
        final boolean matched = BCrypt.checkpw(rawPassword.toString(), encodedPassword);
//        StringUtils.equals(encodedRawPassword, encodedPassword);
        log.debug("Provided password does{}match the encoded password", BooleanUtils.toString(matched, StringUtils.EMPTY, " not "));
        return matched;
    }
}