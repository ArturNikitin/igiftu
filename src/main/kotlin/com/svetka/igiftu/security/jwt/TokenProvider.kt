package com.svetka.igiftu.security.jwt

import com.svetka.igiftu.config.AppProperties
import io.jsonwebtoken.Jwts
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.util.*
import java.util.stream.Collectors
import javax.crypto.SecretKey

@Service
class TokenProvider(
    private val properties: AppProperties,
    private val secretKey: SecretKey
) {
    fun createToken(authentication: Authentication) : String {
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim("authorities", authentication.authorities)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + properties.auth.tokenExpirationMsec))
            .signWith(secretKey)
            .compact()
    }

    fun getUserEmailFromToken(jwtToken: String): String {
        val claimsJws = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build().parseClaimsJws(jwtToken)

        return claimsJws.body.subject
    }

    fun getUserRolesFromToken(jwtToken: String): Set<SimpleGrantedAuthority> {
        val claimsJws = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build().parseClaimsJws(jwtToken)

        val authorities = claimsJws.body["authorities"] as List<Map<String, String>>?
        return authorities!!.stream()
            .map { auth: Map<String, String> -> SimpleGrantedAuthority(auth["authority"]) }
            .collect(Collectors.toSet())
    }

    fun validateToken(jwtToken: String): Boolean {
        try {
            val claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(jwtToken)
            return true
         } catch (ex: Exception) {
//        } catch (ex: SignatureException) {
            return false
//            TODO
//            logger.error("Invalid JWT signature");
//        } catch (MalformedJwtException ex) {
//            logger.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            logger.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            logger.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            logger.error("JWT claims string is empty.");
        }
    }
}