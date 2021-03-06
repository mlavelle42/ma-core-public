/**
 * Copyright (C) 2017 Infinite Automation Software. All rights reserved.
 */
package com.serotonin.m2m2.web.mvc.spring.components;

import java.security.KeyPair;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.infiniteautomation.mango.jwt.JwtSignerVerifier;
import com.serotonin.m2m2.db.dao.SystemSettingsDao;
import com.serotonin.m2m2.db.dao.UserDao;
import com.serotonin.m2m2.vo.User;
import com.serotonin.m2m2.vo.exception.NotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;

/**
 * @author Jared Wiltshire
 */
@Service
public final class TokenAuthenticationService extends JwtSignerVerifier<User> {
    public static final String PUBLIC_KEY_SYSTEM_SETTING = "jwt.userAuth.publicKey";
    public static final String PRIVATE_KEY_SYSTEM_SETTING = "jwt.userAuth.privateKey";

    public static final String TOKEN_TYPE_VALUE = "auth";
    public static final String USER_ID_CLAIM = "id";
    public static final String USER_TOKEN_VERSION_CLAIM = "v";
    
    private final UserDetailsService userDetailsService;
    
    public TokenAuthenticationService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected String tokenType() {
        return TOKEN_TYPE_VALUE;
    }
    
    @Override
    protected void saveKeyPair(KeyPair keyPair) {
    	SystemSettingsDao.instance.setValue(PUBLIC_KEY_SYSTEM_SETTING, keyToString(keyPair.getPublic()));
    	SystemSettingsDao.instance.setValue(PRIVATE_KEY_SYSTEM_SETTING, keyToString(keyPair.getPrivate()));
    }
    
    @Override
    protected KeyPair loadKeyPair() {
        String publicKeyStr = SystemSettingsDao.getValue(PUBLIC_KEY_SYSTEM_SETTING);
        String privateKeyStr = SystemSettingsDao.getValue(PRIVATE_KEY_SYSTEM_SETTING);
        
        if (publicKeyStr != null && !publicKeyStr.isEmpty() && privateKeyStr != null && !privateKeyStr.isEmpty()) {
            return keysToKeyPair(publicKeyStr, privateKeyStr);
        }
        return null;
    }
    
    public void resetKeys() {
        this.generateNewKeyPair();
    }
    
    public String generateToken(User user, Date expiry) {
        JwtBuilder builder = this.newToken(user.getUsername(), expiry)
                .claim(USER_ID_CLAIM, user.getId())
                .claim(USER_TOKEN_VERSION_CLAIM, user.getTokenVersion());

        return this.sign(builder);
    }

    public void revokeTokens(User user) {
        UserDao.instance.revokeTokens(user);
    }

    @Override
    protected User verifyClaims(Jws<Claims> token) {
        Claims claims = token.getBody();
        
        String username = claims.getSubject();
        if (username == null) {
            throw new NotFoundException();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        if (!(userDetails instanceof User)) {
            throw new RuntimeException("Expected user details to be instance of User");
        }
        
        User user = (User) userDetails;

        Integer userId = user.getId();
        this.verifyClaim(token, USER_ID_CLAIM, userId);

        Integer tokenVersion = user.getTokenVersion();
        this.verifyClaim(token, USER_TOKEN_VERSION_CLAIM, tokenVersion);
        
        return user;
    }
}
