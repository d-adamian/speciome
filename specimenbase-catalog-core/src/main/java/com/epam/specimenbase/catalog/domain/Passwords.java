package com.epam.specimenbase.catalog.domain;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.security.SecureRandom;

public final class Passwords {

    private Passwords() {
    }

    static String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[128];
        secureRandom.nextBytes(bytes);
        return BaseEncoding.base64().encode(bytes);
    }

    static String hashPassword(String salt, String password) {
        String stringToHash = salt + password;
        //noinspection UnstableApiUsage
        return Hashing.sha256().hashString(stringToHash, Charsets.UTF_8).toString();
    }
}
