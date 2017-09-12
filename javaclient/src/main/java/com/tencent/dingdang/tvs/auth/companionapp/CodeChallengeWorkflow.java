package com.tencent.dingdang.tvs.auth.companionapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("javadoc")
public class CodeChallengeWorkflow {
    private static final String SHA_256 = "S256";
    private static final String ALORITHM_SHA_256 = "SHA-256";

    private String codeVerifier;
    private String codeChallengeMethod;
    private String codeChallenge;
    private static CodeChallengeWorkflow instance = new CodeChallengeWorkflow();

    private CodeChallengeWorkflow() {
    }

    /**
     * @return the {@link CodeChallengeWorkflow} instance
     */
    public static CodeChallengeWorkflow getInstance() {
        return instance;
    }

    public void generateProofKeyParameters() {
        try {
            codeVerifier = generateCodeVerifier();
            codeChallengeMethod = SHA_256;
            codeChallenge = generateCodeChallenge(codeVerifier, codeChallengeMethod);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Your JRE does not support the required "
                    + CodeChallengeWorkflow.ALORITHM_SHA_256 + " algorithm.", e);
        }
    }

    /**
     * @return the codeVerifier generated.
     */
    public String getCodeVerifier() {
        return this.codeVerifier;
    }

    /**
     * @return the codeChallenge generated.
     */
    public String getCodeChallenge() {
        return this.codeChallenge;
    }

    /**
     * @return the codeChallengeMethod used. Defaults to {@value #SHA_256}
     */
    public String getCodeChallengeMethod() {
        return this.codeChallengeMethod;
    }

    private String generateCodeChallenge(String codeVerifier, String codeChallengeMethod)
            throws NoSuchAlgorithmException {
        String codeChallenge =
                base64UrlEncode(MessageDigest.getInstance(ALORITHM_SHA_256).digest(codeVerifier.getBytes()));
        return codeChallenge;
    }

    private String generateCodeVerifier() {
        byte[] randomOctetSequence = generateRandomOctetSequence();
        String codeVerifier = base64UrlEncode(randomOctetSequence);
        return codeVerifier;
    }

    /**
     * As per Proof Key/SPOP protocol Version 10
     * @return a random 32 sized octet sequence from allowed range
     */
    private byte[] generateRandomOctetSequence() {
        SecureRandom random = new SecureRandom();
        byte[] octetSequence = new byte[32];
        random.nextBytes(octetSequence);

        return octetSequence;
    }

    /**
     * This method is borrowed from the SPOP protocol spec version 10 here : http://datatracker.ietf.org/doc/draft-ietf-oauth-spop/?include_text=1
     * @param arg the string to convert
     * @return base64 URL encoded string value as specified by spec.
     */
    private String base64UrlEncode(byte[] arg) {
        return Base64.encodeBase64URLSafeString(arg);
    }
}
