package com.example.shopease.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomEncryptorPasswordEncoder implements PasswordEncoder {

    private final Encryptor encryptor;

    public CustomEncryptorPasswordEncoder(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return encryptor.generateSecurePassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String decryptedPassword = encryptor.gerDecryptedPassword(encodedPassword);
        return rawPassword.toString().equals(decryptedPassword);
    }
}
