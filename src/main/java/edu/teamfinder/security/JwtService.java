package edu.teamfinder.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Minimal HS256 JWT service with no hidden signing dependency. */
@Service
public class JwtService {
    private final byte[] key; private final long expirationMinutes;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {
        if (secret.length() < 32) throw new IllegalArgumentException("JWT_SECRET must have at least 32 characters.");
        this.key=secret.getBytes(StandardCharsets.UTF_8); this.expirationMinutes=expirationMinutes;
    }
    public String create(String email,String role) {
        String header=b64("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        long exp=Instant.now().plus(Duration.ofMinutes(expirationMinutes)).getEpochSecond();
        String payload=b64("{\"sub\":\""+escape(email)+"\",\"role\":\""+escape(role)+"\",\"exp\":"+exp+"}");
        return header+"."+payload+"."+sign(header+"."+payload);
    }
    public Optional<String> subject(String token) {
        try {
            String[] parts=token.split("\\."); if(parts.length!=3 || !MessageDigest.isEqual(sign(parts[0]+"."+parts[1]).getBytes(StandardCharsets.US_ASCII),parts[2].getBytes(StandardCharsets.US_ASCII))) return Optional.empty();
            String payload=new String(Base64.getUrlDecoder().decode(parts[1]),StandardCharsets.UTF_8);
            long exp=Long.parseLong(field(payload,"exp")); if(Instant.now().getEpochSecond()>=exp) return Optional.empty();
            return Optional.of(field(payload,"sub"));
        } catch(Exception ignored) { return Optional.empty(); }
    }
    private String sign(String value) { try { Mac mac=Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(key,"HmacSHA256")); return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.US_ASCII))); } catch (Exception ex) { throw new IllegalStateException("JWT signing is unavailable.",ex); } }
    private static String b64(String value) { return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8)); }
    private static String field(String payload,String name) { String marker="\""+name+"\":"; int start=payload.indexOf(marker)+marker.length(); if(start<marker.length()) throw new IllegalArgumentException(); boolean quote=payload.charAt(start)=='\"'; start+=quote?1:0; int end=quote?payload.indexOf('"',start):payload.indexOf('}',start); return payload.substring(start,end); }
    private static String escape(String s) { return s.replace("\\","\\\\").replace("\"","\\\""); }
}
