package com.mano.passwordManager;

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
    private static String user_key = "key"; // To be changed

    private byte[] generateKeyFromPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            byte[] keyBytes = Arrays.copyOf(hashedBytes, 32);

            return keyBytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        }
    }

    private final Map<String, Map<String, String>> domains;
    private final byte[] SECRET_KEY;

    public Manager() {
        domains = new HashMap<String, Map<String, String>>();
        SECRET_KEY = generateKeyFromPassword(user_key);
    }

    public void addCredentials(final String domain,
            final String username,
            String password) {
        password = encrypt(password);
        if (domains.containsKey(domain)) {
            final Map<String, String> passwords = domains.get(domain);
            passwords.put(username, password);

            // System.out.println("Domain alredy exists");
        } else {
            final Map<String, String> passwords = new HashMap<String, String>();
            passwords.put(username, password);
            domains.put(domain, passwords);

            // System.out.println("Domain was created just now");
        }
    }

    public String getCredentials(final String domain, final String username) {
        final Map<String, String> passwords = domains.get(domain);
        final String password = decrypt(passwords.get(username));

        return password;
    }

    public String testEncription(String input) {
        final String cypher = encrypt(input);
        final String plain_text = decrypt(cypher);
        return plain_text;
    }

    public Integer testKeys() {
        final byte[] sk_bytes = SECRET_KEY;

        return sk_bytes.length;
    }

    private String encrypt(final String input) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, "AES");
            final IvParameterSpec iv = new IvParameterSpec(generateIV());

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            final byte[] encryptedBytes = cipher.doFinal(input.getBytes());

            // Combine IV and ciphertext
            byte[] combined = new byte[iv.getIV().length + encryptedBytes.length];
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
            byte[] inputBytes = Base64.getDecoder().decode(input);

            byte[] ivBytes = Arrays.copyOfRange(inputBytes, 0, 16);
            final Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, "AES");
            final IvParameterSpec iv = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decryptedBytes = cipher.doFinal(inputBytes, 16, inputBytes.length - 16);

            return new String(decryptedBytes);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] generateIV() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            return iv;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
