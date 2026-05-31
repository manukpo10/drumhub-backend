package com.drumhub.payment.service;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class SignatureVerifierTest {

    private static final String SECRET = "test-webhook-secret";
    private static final String DATA_ID = "12345";
    private static final String REQUEST_ID = "req-abc-123";

    // Utility to generate a valid X-Signature header for test vectors
    private String buildSignatureHeader(String ts, String dataId, String requestId, String secret) throws Exception {
        String manifest = "id:" + dataId + ";request-id:" + requestId + ";ts:" + ts + ";";
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(manifest.getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : raw) {
            hex.append(String.format("%02x", b));
        }
        return "ts=" + ts + ",v1=" + hex;
    }

    @Test
    void verify_withValidSignature_returnsTrue() throws Exception {
        String ts = "1700000000";
        String header = buildSignatureHeader(ts, DATA_ID, REQUEST_ID, SECRET);

        boolean result = SignatureVerifier.verify(DATA_ID, REQUEST_ID, header, SECRET);

        assertThat(result).isTrue();
    }

    @Test
    void verify_withForgedSignature_returnsFalse() {
        String header = "ts=1700000000,v1=deadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeef";

        boolean result = SignatureVerifier.verify(DATA_ID, REQUEST_ID, header, SECRET);

        assertThat(result).isFalse();
    }

    @Test
    void verify_withTamperedTs_returnsFalse() throws Exception {
        // Build valid signature with ts=1700000000, then tamper ts to 9999999999 in the header
        String validTs = "1700000000";
        String header = buildSignatureHeader(validTs, DATA_ID, REQUEST_ID, SECRET);
        // Replace the ts value to simulate tampering
        String tamperedHeader = header.replace("ts=" + validTs, "ts=9999999999");

        boolean result = SignatureVerifier.verify(DATA_ID, REQUEST_ID, tamperedHeader, SECRET);

        assertThat(result).isFalse();
    }

    @Test
    void verify_withMissingHeader_returnsFalse() {
        boolean result = SignatureVerifier.verify(DATA_ID, REQUEST_ID, null, SECRET);

        assertThat(result).isFalse();
    }

    @Test
    void verify_withEmptyHeader_returnsFalse() {
        boolean result = SignatureVerifier.verify(DATA_ID, REQUEST_ID, "", SECRET);

        assertThat(result).isFalse();
    }

    @Test
    void verify_withMissingV1Part_returnsFalse() {
        String header = "ts=1700000000";

        boolean result = SignatureVerifier.verify(DATA_ID, REQUEST_ID, header, SECRET);

        assertThat(result).isFalse();
    }

    @Test
    void verify_withDifferentDataId_returnsFalse() throws Exception {
        String ts = "1700000000";
        String header = buildSignatureHeader(ts, DATA_ID, REQUEST_ID, SECRET);

        boolean result = SignatureVerifier.verify("different-id", REQUEST_ID, header, SECRET);

        assertThat(result).isFalse();
    }
}
