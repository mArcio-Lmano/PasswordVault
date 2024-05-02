package com.mano.passwordManager.util;

import java.io.Console;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Login
 */
// TODO: REFACTOR SOME CODE
public class Login {
    public String key;

    public Login() {
        Console console = System.console();

        if (console == null) {
            System.out.println("Err Finding a console");
            return;
        }
        char[] chars = console.readPassword("Enter the correct Key: ");
        String consoleKey = new String(chars);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(consoleKey.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashHexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hashHexString.append('0');
                }
                hashHexString.append(hex);
            }

            String hashFromFile = getLocalHash("src/hash.hash");
            String hashFromKey = new String(hashHexString);

            if (hashFromFile.equals(hashFromKey)) {
                System.out.println("The Password was correct");
                key = consoleKey;
            } else {
                System.out.println("Wrong Password");
                key = null;
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: SHA-256 algorithm not found");
        }
    }

    private String getLocalHash(String path) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            return content.strip();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
