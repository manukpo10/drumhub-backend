package com.drumhub.payment.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Validates Mercado Pago HMAC-SHA256 webhook signatures.
 *
 * X-Signature header format: "ts=<timestamp>,v1=<hex-hash>"
 * Signed manifest: "id:<dataId>;request-id:<xRequestId>;ts:<ts>;"
 */
public final class SignatureVerifier {

    private SignatureVerifier() {
        // utility class
    }

    /**
     * Verifies the MP webhook X-Signature header using constant-time comparison.
     *
     * @param dataId           the data.id query param from the webhook URL
     * @param xRequestId       the X-Request-Id header value
     * @param xSignatureHeader the full X-Signature header value
     * @param secret           the MP webhook secret (MP_WEBHOOK_SECRET)
     * @return true if the signature is valid, false otherwise
     */
    public static boolean verify(String dataId, String xRequestId, String xSignatureHeader, String secret) {
        if (xSignatureHeader == null || xSignatureHeader.isBlank()) {
            return false;
        }

        String ts = null;
        String v1 = null;

        for (String part : xSignatureHeader.split(",")) {
            part = part.trim();
            int eq = part.indexOf('=');
            if (eq < 0) {
                continue;
            }
            String key = part.substring(0, eq).trim();
            String value = part.substring(eq + 1).trim();
            if ("ts".equals(key)) {
                ts = value;
            } else if ("v1".equals(key)) {
                v1 = value;
            }
        }

        if (ts == null || v1 == null) {
            return false;
        }

        try {
            String manifest = "id:" + dataId + ";request-id:" + xRequestId + ";ts:" + ts + ";";
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] rawHash = mac.doFinal(manifest.getBytes(StandardCharsets.UTF_8));

            String expected = toHex(rawHash);

            // Constant-time comparison
            return MessageDigest.isEqual(
                    expected.getBytes(StandardCharsets.UTF_8),
                    v1.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Diagnostic helper: rebuilds the manifest and computes the expected hash so we can
     * compare against the v1 that MP sent. Does NOT expose the secret. For debugging only.
     */
    public static String diagnose(String dataId, String xRequestId, String xSignatureHeader, String secret) {
        String ts = null;
        String v1 = null;
        if (xSignatureHeader != null) {
            for (String part : xSignatureHeader.split(",")) {
                part = part.trim();
                int eq = part.indexOf('=');
                if (eq < 0) continue;
                String key = part.substring(0, eq).trim();
                String value = part.substring(eq + 1).trim();
                if ("ts".equals(key)) ts = value;
                else if ("v1".equals(key)) v1 = value;
            }
        }
        String manifest = "id:" + dataId + ";request-id:" + xRequestId + ";ts:" + ts + ";";
        String expected;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            expected = toHex(mac.doFinal(manifest.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            expected = "ERROR:" + e.getMessage();
        }
        return "manifest='" + manifest + "' expected='" + expected + "' received-v1='" + v1
                + "' secretLen=" + (secret == null ? "null" : secret.length());
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
