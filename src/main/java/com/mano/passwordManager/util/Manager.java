package com.mano.passwordManager.util;

import com.mano.passwordManager.storage.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple passwords manager
 */
public class Manager {
    private final String userKey;
    private final Map<String, Map<String, String>> domains;

    private final byte[] SECRET_KEY;
    private final Storage storage;
    public Integer overrideFlag;

    public Manager(final String path, final String key) {
        storage = new Storage(path);
        userKey = key;
        overrideFlag = 0;
        if (storage.fileExists) {
            domains = storage.readPasswordsFromStorage();
        } else {
            domains = new HashMap<String, Map<String, String>>();
        }
        SECRET_KEY = generateKeyFromPassword(userKey);
    }

    public void addCredentials(final String domain,
            final String username,
            String password) {
        password = encrypt(password);
        if (domains.containsKey(domain)) {
            final Map<String, String> passwords = domains.get(domain);
            if (!passwords.containsKey(username)) {
                passwords.put(username, password);
            } else {
                if (overrideFlag == 1) {
                    passwords.put(username, password);
                    domains.put(domain, passwords);
                    overrideFlag = 0;
                } else {
                    overrideFlag = 1;
                }
            }
        } else {
            final Map<String, String> passwords = new HashMap<String, String>();
            passwords.put(username, password);
            domains.put(domain, passwords);
        }
    }

    public void removeCredentials(final String domain, final String username) {
        Map<String, String> domainToRemove = domains.get(domain);
        domainToRemove.remove(username);
        if (domainToRemove.isEmpty()) {
            domains.remove(domain);
        }
    }

    public Map<String, Map<String, String>> getDomains() {
        return domains;
    }

    public String getCredentials(final String domain, final String username) {
        final Map<String, String> passwords = domains.get(domain);
        final String password = decrypt(passwords.get(username));

        return password;
    }

    public String testEncription(final String input) {
        final String cypher = encrypt(input);
        final String plain_text = decrypt(cypher);
        return plain_text;
    }

    public Integer testKeys() {
        final byte[] sk_bytes = SECRET_KEY;

        return sk_bytes.length;
    }

    public void saveToFile() {
        storage.savePasswordsToStorage(domains);
    }

    private byte[] generateKeyFromPassword(final String password) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] hashedBytes = md.digest(password.getBytes());
            final byte[] keyBytes = Arrays.copyOf(hashedBytes, 32);

            return keyBytes;
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        }
    }

    private String encrypt(final String input) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, "AES");
            final IvParameterSpec iv = new IvParameterSpec(generateIV());

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            final byte[] encryptedBytes = cipher.doFinal(input.getBytes());

            final byte[] combined = new byte[iv.getIV().length + encryptedBytes.length];
            System.arraycopy(iv.getIV(), 0, combined, 0, iv.getIV().length);
            System.arraycopy(encryptedBytes, 0, combined, iv.getIV().length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String decrypt(final String input) {
        try {
            final byte[] inputBytes = Base64.getDecoder().decode(input);

            final byte[] ivBytes = Arrays.copyOfRange(inputBytes, 0, 16);
            final Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, "AES");
            final IvParameterSpec iv = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            final byte[] decryptedBytes = cipher.doFinal(inputBytes, 16, inputBytes.length - 16);

            return new String(decryptedBytes);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] generateIV() {
        try {
            final SecureRandom random = SecureRandom.getInstanceStrong();
            final byte[] iv = new byte[16];
            random.nextBytes(iv);
            return iv;
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
