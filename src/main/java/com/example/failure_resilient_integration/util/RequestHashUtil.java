package com.example.failure_resilient_integration.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class RequestHashUtil {

    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encoded);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash request", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
