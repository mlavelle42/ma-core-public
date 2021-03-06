/**
 * Copyright (C) 2017 Infinite Automation Software. All rights reserved.
 */
package com.infiniteautomation.mango.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.codec.Base64;

import com.serotonin.ShouldNeverHappenException;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.EllipticCurveProvider;

/**
 * @author Jared Wiltshire
 */
public abstract class JwtSignerVerifier<T> {
    public static final String TOKEN_TYPE_CLAIM = "typ";
    
    private KeyPair keyPair;
    private JwtParser parser;
    
    protected JwtSignerVerifier() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        
        this.parser = Jwts.parser().require(TOKEN_TYPE_CLAIM, this.tokenType());
        
        this.keyPair = this.loadKeyPair();
        if (this.keyPair == null) {
            this.generateNewKeyPair();
        } else {
            this.parser.setSigningKey(this.keyPair.getPublic());
        }
    }
    
    protected final void generateNewKeyPair() {
        this.keyPair = EllipticCurveProvider.generateKeyPair(SignatureAlgorithm.ES512);
        this.parser.setSigningKey(this.keyPair.getPublic());
        this.saveKeyPair(this.keyPair);
    }

    protected abstract String tokenType();
    protected abstract T verifyClaims(Jws<Claims> token);
    protected abstract void saveKeyPair(KeyPair keyPair);
    protected abstract KeyPair loadKeyPair();
    
    protected final JwtBuilder newToken(String subject, Date expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiration);
    }

    protected final String sign(JwtBuilder builder) {
        return builder.claim(TOKEN_TYPE_CLAIM, this.tokenType())
            .signWith(SignatureAlgorithm.ES512, keyPair.getPrivate())
            .compact();
    }
    
    /**
     * Parses the token and verifies it's signature and expiration. Does NOT verify claims!
     * @param token
     * @return
     */
    public final Jws<Claims> parse(String token) {
        return parser.parseClaimsJws(token);
    }
    
    /**
     * Parses the token and verifies it's signature, expiration and claims.
     * @param token
     * @return
     */
    public final T verify(String token) {
        return this.verify(this.parse(token));
    }
    
    /**
     * Parses the token and verifies it's signature, expiration and claims.
     * @param token
     * @return
     */
    public final T verify(Jws<Claims> token) {
        return this.verifyClaims(token);
    }
    
    protected void verifyClaim(Jws<Claims> token, String expectedClaimName, Object expectedClaimValue) {
        JwsHeader<?> header = token.getHeader();
        Claims claims = token.getBody();
        
        Object actualClaimValue = claims.get(expectedClaimName);
        if (actualClaimValue == null) {
            String msg = String.format(
                ClaimJwtException.MISSING_EXPECTED_CLAIM_MESSAGE_TEMPLATE,
                expectedClaimName, expectedClaimValue
            );
            throw new MissingClaimException(header, claims, msg);
        } else if (!expectedClaimValue.equals(actualClaimValue)) {
            String msg = String.format(
                ClaimJwtException.INCORRECT_EXPECTED_CLAIM_MESSAGE_TEMPLATE,
                expectedClaimName, expectedClaimValue, actualClaimValue
            );
            throw new IncorrectClaimException(header, claims, msg);
        }
    }

    public String getPublicKey() {
        return keyToString(keyPair.getPublic());
    }
    
    public boolean isSignedJwt(String token) {
        return parser.isSigned(token);
    }

    public static KeyPair keysToKeyPair(String publicKeyStr, String privateKeyStr) {
        byte[] publicBase64 = Base64.decode(publicKeyStr.getBytes(StandardCharsets.ISO_8859_1));
        byte[] privateBase64 = Base64.decode(privateKeyStr.getBytes(StandardCharsets.ISO_8859_1));

        try {
            KeyFactory kf = KeyFactory.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
            PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicBase64));
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateBase64));
            return new KeyPair(publicKey, privateKey);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            throw new ShouldNeverHappenException(e);
        }
    }
    
    public static String keyToString(Key key) {
        byte[] publicBase64 = Base64.encode(key.getEncoded());
        return new String(publicBase64, StandardCharsets.ISO_8859_1);
    }
}
